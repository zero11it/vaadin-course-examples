package it.zero11.vaadin.course.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.zero11.vaadin.course.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
		
}
