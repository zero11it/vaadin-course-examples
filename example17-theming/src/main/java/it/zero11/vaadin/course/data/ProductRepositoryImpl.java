package it.zero11.vaadin.course.data;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.zero11.vaadin.course.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Component
public class ProductRepositoryImpl {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<Product> searchBy(String sku, BigDecimal minPrice, 
			BigDecimal maxPrice, Pageable paging) {
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
				
		if (!paging.getSort().isEmpty()) {
			hql.append(" order by ");
			hql.append( paging.getSort().stream()
				.map(order -> order.getProperty() + " " + order.getDirection().name())
				.collect(Collectors.joining(", "))
			);					
		} 
		
		TypedQuery<Product> query = em.createQuery(hql.toString(), Product.class);
		if (sku != null)
			query.setParameter("sku", "%" + sku.toLowerCase() + "%");
		
		if (minPrice != null)
			query.setParameter("minPrice", minPrice);
		
		if (maxPrice != null)
			query.setParameter("maxPrice", maxPrice);
		
		query.setFirstResult(paging.getPageNumber() * paging.getPageSize());
		query.setMaxResults(paging.getPageSize());
		
		return query.getResultList();		
	}
	
}
