package it.zero11.vaadin.course.data;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.zero11.vaadin.course.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> searchBy(String sku, BigDecimal minPrice, BigDecimal maxPrice, Pageable paging);
	
}
