package it.zero11.vaadin.course.service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.zero11.vaadin.course.data.UserRepository;
import it.zero11.vaadin.course.exceptions.NoUserException;
import it.zero11.vaadin.course.exceptions.UserDisabledException;
import it.zero11.vaadin.course.exceptions.WrongPasswordException;
import it.zero11.vaadin.course.model.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User load(Long id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User getByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}
	
	public void save(User user) {
		User existingUser = getByUsername(user.getUsername());
		if (existingUser != null && (user.getPassword() == null || user.getPassword().isEmpty())) {
			user.setPassword(existingUser.getPassword());
		} else {
			Random rnd = new Random();
			byte[] bytes = new byte[5];				
			rnd.nextBytes(bytes);
			String salt = new String(bytes, Charset.forName("UTF-8"));				
			String hash = toHash(salt, user.getPassword());
			user.setPassword(hash);		
			user.setSalt(salt);
		}
		userRepository.save(user);
	}

	private String toHash(String salt, String password) {
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
	
	public User login(String username, String password) 
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
	
	public void remove(User user) {
		userRepository.delete(user);
	}
}
