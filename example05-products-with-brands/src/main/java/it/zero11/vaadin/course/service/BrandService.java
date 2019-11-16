package it.zero11.vaadin.course.service;

import java.util.List;

import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.utils.JPAUtils;

public class BrandService {
	public static List<Brand> findAll() {
		return JPAUtils.runInTransaction(em -> {
			/*CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Brand> q = cb.createQuery(Brand.class);
			Root<Brand> c = q.from(Brand.class);
			return em.createQuery(q.select(c)).getResultList();*/
			
			return em.createQuery("from Brand", Brand.class).getResultList();
		});
	}

	public static void save(Brand brand) {
		JPAUtils.runInTransaction(em -> {
			em.persist(brand);
		});
	}

	public static void remove(Brand brand) {
		JPAUtils.runInTransaction(em -> {
			em.remove(em.find(Brand.class, brand.getId()));
		});
	}
}
