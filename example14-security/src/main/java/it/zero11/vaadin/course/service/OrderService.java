package it.zero11.vaadin.course.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

import it.zero11.vaadin.course.model.Order;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.utils.JPAUtils;

public class OrderService {

	public static List<Order> findAll() {
		return JPAUtils.runInTransaction(em -> {
			return em.createQuery("from Order", Order.class).getResultList();
		});
	}
	
	public static List<Order> findBy(int offset, int limit, List<QuerySortOrder> sorts,
			String customerName, BigDecimal minPrice, BigDecimal maxPrice, Product product) {
		
		return JPAUtils.runInTransaction(em -> {
			StringBuilder hql = new StringBuilder("from Order ");
			String comma = " where ";
			
			if (customerName != null) {
				hql.append(comma).append("LOWER(customerName) like :customerName ");
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
			
			if (product != null) {
				hql.append(comma).append("product = :product ");
				comma = " and ";
			}
			
			if (sorts.size() > 0) {
				QuerySortOrder sort = sorts.get(0);
				hql.append(" order by ").append(sort.getSorted())
					.append(sort.getDirection() == SortDirection.ASCENDING ?
							" asc" : " desc");
			}
			
			TypedQuery<Order> query = em.createQuery(hql.toString(), Order.class);
			if (customerName != null)
				query.setParameter("customerName", "%" + customerName.toLowerCase() + "%");
			
			if (minPrice != null)
				query.setParameter("minPrice", minPrice);
			
			if (maxPrice != null)
				query.setParameter("maxPrice", maxPrice);
			
			if (product != null)
				query.setParameter("product", product);
			
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			
			return query.getResultList();
		});
	}
	
	public static Long countBy( 
			String customerName, BigDecimal minPrice, BigDecimal maxPrice, Product product) {
		
		return JPAUtils.runInTransaction(em -> {
			StringBuilder hql = new StringBuilder("select count(*) from Order ");
			String comma = " where ";
			
			if (customerName != null) {
				hql.append(comma).append("customerName like :customerName ");
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
			
			if (product != null) {
				hql.append(comma).append("product = :product ");
				comma = " and ";
			}
			
			
			TypedQuery<Long> query = em.createQuery(hql.toString(), Long.class);
			if (customerName != null)
				query.setParameter("customerName", "%" + customerName + "%");
			
			if (minPrice != null)
				query.setParameter("minPrice", minPrice);
			
			if (maxPrice != null)
				query.setParameter("maxPrice", maxPrice);
			
			if (product != null)
				query.setParameter("product", product);
			
			return query.getSingleResult();
		});
	}
	
	public static BigDecimal getTotalSold() {
		return getTotalSold(null, null, null, null);
	}
	
	public static BigDecimal getTotalSold( 
			String customerName, BigDecimal minPrice, BigDecimal maxPrice, Product product) {
		
		return JPAUtils.runInTransaction(em -> {
			StringBuilder hql = new StringBuilder("select sum(quantity * price) from Order ");
			String comma = " where ";
			
			if (customerName != null) {
				hql.append(comma).append("customerName like :customerName ");
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
			
			if (product != null) {
				hql.append(comma).append("product = :product ");
				comma = " and ";
			}
			
			
			TypedQuery<BigDecimal> query = em.createQuery(hql.toString(), BigDecimal.class);
			if (customerName != null)
				query.setParameter("customerName", "%" + customerName + "%");
			
			if (minPrice != null)
				query.setParameter("minPrice", minPrice);
			
			if (maxPrice != null)
				query.setParameter("maxPrice", maxPrice);
			
			if (product != null)
				query.setParameter("product", product);
			
			return query.getSingleResult();
		});
	}
	
	public static Order load(Long id) {
		return JPAUtils.runInTransaction(em -> {
			return em.find(Order.class, id);
		});
	}
	
	public static void save(Order order) {
		JPAUtils.runInTransaction(em -> {
			em.merge(order);
		});
	}
}
