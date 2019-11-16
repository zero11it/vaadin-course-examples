package it.zero11.vaadin.course.utils;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtils {
	private static EntityManagerFactory factory;

	public static EntityManagerFactory getFactory() {
		if (factory == null) {
			synchronized (JPAUtils.class) {
				if (factory == null) {
					factory = Persistence.createEntityManagerFactory("example-pu");
				}
			}
		}
		
		return factory;
	}

	public static void destroy() {
		if (factory != null) {
			factory.close();
		}
	}
	
	public static <T> T runInTransaction(Function<EntityManager, T> function) {
		EntityManager em = null;
		
		try {
			em = getFactory().createEntityManager();
			em.getTransaction().begin();
			
			T result = function.apply(em);
			
			em.getTransaction().commit();
			return result;
		}finally {
			if (em != null) {
				em.close();
			}
		}
	}
	
	public static void runInTransaction(Consumer<EntityManager> function) {
		EntityManager em = null;
		
		try {
			em = getFactory().createEntityManager();
			em.getTransaction().begin();
			
			function.accept(em);
			
			em.getTransaction().commit();
			return;
		}finally {
			if (em != null) {
				em.close();
			}
		}
	}
}
