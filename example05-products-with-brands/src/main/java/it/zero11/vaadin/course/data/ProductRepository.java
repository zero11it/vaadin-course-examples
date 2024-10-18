package it.zero11.vaadin.course.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.zero11.vaadin.course.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
		
}
