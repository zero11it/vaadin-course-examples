package it.zero11.vaadin.course.data;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.zero11.vaadin.course.model.Order;
import it.zero11.vaadin.course.model.Product;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List<Order> searchBy(String customerName, BigDecimal minPrice, BigDecimal maxPrice, Product product, Pageable paging);

	BigDecimal getTotalSold(String customerName, BigDecimal minPrice, BigDecimal maxPrice, Product product);
}
