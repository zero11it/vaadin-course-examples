package it.zero11.vaadin.course.service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import javax.persistence.NoResultException;

import it.zero11.vaadin.course.exceptions.NoUserException;
import it.zero11.vaadin.course.exceptions.UserDisabledException;
import it.zero11.vaadin.course.exceptions.WrongPasswordException;
import it.zero11.vaadin.course.model.User;
import it.zero11.vaadin.course.utils.JPAUtils;

public class UserService {
	public static List<User> findAll() {
		return JPAUtils.runInTransaction(em -> {
			
			return em.createQuery("from User", User.class)					
					.getResultList();
		});
	}

	public static User load(Long id) {
		return JPAUtils.runInTransaction(em -> {
			return em.find(User.class, id);
		});
	}
	
	public static User getByUsername(String username) {
		return JPAUtils.runInTransaction(em -> {			
			try {
				return em.createQuery("from User where lower(username) = :username", User.class)
						.setParameter("username", username.toLowerCase())
						.getSingleResult();
			} catch (NoResultException e) {
				return null;
			}
		});
	}
	
	public static void save(User user) {
		JPAUtils.runInTransaction(em -> {
			if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
				User oldUser = em.find(User.class, user.getId());
				user.setPassword(oldUser.getPassword());
			} else {
				Random rnd = new Random();
				byte[] bytes = new byte[5];				
				rnd.nextBytes(bytes);
				String salt = new String(bytes, Charset.forName("UTF-8"));				
				String hash = toHash(salt, user.getPassword());
				
				user.setPassword(hash);		
				user.setSalt(salt);
			}
			em.merge(user);
		});
	}

	private static String toHash(String salt, String password) {
		String hash = salt + password;
		try {
			return new String( 
					MessageDigest.getInstance("MD5").digest(hash.getBytes()),
					Charset.forName("UTF-8")
				);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public static User login(String username, String password) 
			throws NoUserException, UserDisabledException, WrongPasswordException {
		User user = getByUsername(username);
		if (user == null)
			throw new NoUserException();
		
		if (!user.isActive()) {
			throw new UserDisabledException();
		}
		
		String salt = user.getSalt();
		String currentHash = toHash(salt, password);
		
		if (currentHash.equals(user.getPassword()))
			return user;
		else
			throw new WrongPasswordException();
	}
	
	public static void remove(User user) {
		JPAUtils.runInTransaction(em -> {
			em.remove(em.find(User.class, user.getId()));
		});
	}

	public static User findByToken(String token) {
		return JPAUtils.runInTransaction(em -> {			
			try {
				return em.createQuery("from User where token = :token", User.class)
						.setParameter("token", token)
						.getSingleResult();
			} catch (NoResultException e) {
				return null;
			}
		});
	}
}
