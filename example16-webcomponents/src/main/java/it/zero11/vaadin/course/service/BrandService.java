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
			
			return em.createQuery("from Brand order", Brand.class)					
					.getResultList();
		});
	}

	public static Brand load(Long id) {
		return JPAUtils.runInTransaction(em -> {
			return em.find(Brand.class, id);
		});
	}
	
	public static void save(Brand brand) {
		JPAUtils.runInTransaction(em -> {
			em.merge(brand);
		});
	}

	public static void remove(Brand brand) {
		JPAUtils.runInTransaction(em -> {
			em.remove(em.find(Brand.class, brand.getId()));
		});
	}
}
