package it.zero11.vaadin.course.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

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

	public static List<Product> findBy(int offset, int limit, List<QuerySortOrder> sorts,
			String sku, BigDecimal minPrice, BigDecimal maxPrice) {
		
		return JPAUtils.runInTransaction(em -> {
			StringBuilder hql = new StringBuilder("from Product p ");
			String comma = " where ";
			
			if (sku != null) {
				hql.append(comma).append("LOWER(sku) like :sku ");
				comma = " and ";
			}
			
			if (minPrice != null) {
				hql.append(comma).append("price >= :minPrice ");
				comma = " and ";
			}
						
			if (maxPrice != null) {
				hql.append(comma).append("price <= :maxPrice ");
				comma = " and ";
			}
			
			if (sorts.size() > 0) {
				QuerySortOrder sort = sorts.get(0);
				hql.append(" order by ").append(sort.getSorted());
				hql.append(" ").append(sort.getDirection() == SortDirection.ASCENDING ? 
						"asc" : 
						"desc");
			} 
			
			TypedQuery<Product> query = em.createQuery(hql.toString(), Product.class);
			if (sku != null)
				query.setParameter("sku", "%" + sku.toLowerCase() + "%");
			
			if (minPrice != null)
				query.setParameter("minPrice", minPrice);
			
			if (maxPrice != null)
				query.setParameter("maxPrice", maxPrice);
			
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			
			return query.getResultList();
		});
	}
	
	public static Long countBy( 
			String sku, BigDecimal minPrice, BigDecimal maxPrice) {
		
		return JPAUtils.runInTransaction(em -> {
			StringBuilder hql = new StringBuilder("select count(*) from Product ");
			String comma = " where ";
			
			if (sku != null) {
				hql.append(comma).append("sku like :sku ");
				comma = " and ";
			}
			
			if (minPrice != null) {
				hql.append(comma).append("price >= :minPrice ");
				comma = " and ";
			}
			
			if (maxPrice != null) {
				hql.append(comma).append("price <= :maxPrice ");
				comma = " and ";
			}
			
			TypedQuery<Long> query = em.createQuery(hql.toString(), Long.class);
			if (sku != null)
				query.setParameter("sku", "%" + sku + "%");
			
			if (minPrice != null)
				query.setParameter("minPrice", minPrice);
			
			if (maxPrice != null)
				query.setParameter("maxPrice", maxPrice);
			
			return query.getSingleResult();
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
