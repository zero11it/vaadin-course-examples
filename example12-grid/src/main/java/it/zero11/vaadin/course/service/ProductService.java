package it.zero11.vaadin.course.service;

import java.util.List;

import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.utils.JPAUtils;

public class ProductService {
	public static List<Product> findAll() {
		return JPAUtils.runInTransaction(em -> {
			/*CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> q = cb.createQuery(Product.class);
			Root<Product> c = q.from(Product.class);
			return em.createQuery(q.select(c)).getResultList();*/
			
			return em.createQuery("from Product", Product.class).getResultList();
		});
	}

	public static Product load(Long id) {
		return JPAUtils.runInTransaction(em -> {
			return em.find(Product.class, id);
		});
	}
	
	public static void save(Product product) {
		JPAUtils.runInTransaction(em -> {
			em.merge(product);
		});
	}

	public static void remove(Product product) {
		JPAUtils.runInTransaction(em -> {
			em.remove(em.find(Product.class, product.getId()));
		});
	}
}
