package it.zero11.vaadin.course.data;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import it.zero11.vaadin.course.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Component
public class OrderRepositoryImpl {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<Order> searchBy(String customerName, BigDecimal minPrice, 
			BigDecimal maxPrice, Product product, Pageable paging) {
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
		
		if (!paging.getSort().isEmpty()) {
			hql.append(" order by ");
			hql.append( paging.getSort().stream()
				.map(order -> order.getProperty() + " " + order.getDirection().name())
				.collect(Collectors.joining(", "))
			);					
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
		
		query.setFirstResult(paging.getPageNumber() * paging.getPageSize());
		query.setMaxResults(paging.getPageSize());
		
		return query.getResultList();		
	}
	
	public BigDecimal getTotalSold( 
			String customerName, BigDecimal minPrice, BigDecimal maxPrice, Product product) {
		
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
	}
}
