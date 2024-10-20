package it.zero11.vaadin.course.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.zero11.vaadin.course.data.OrderRepository;
import it.zero11.vaadin.course.data.ProductRepository;
import it.zero11.vaadin.course.model.Order;
import it.zero11.vaadin.course.model.Product;
import jakarta.transaction.Transactional;

@Service
public class OrderService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	public BigDecimal getTotalSold() {
		return orderRepository.getTotalSold(null, null, null, null);
	}
	
	public BigDecimal getTotalSold( 
			String customerName, BigDecimal minPrice, BigDecimal maxPrice, Product product) {
		
		return orderRepository.getTotalSold(customerName, minPrice, maxPrice, product); 		
	}

	public List<Order> searchBy(String customerName, BigDecimal minPrice, 
			BigDecimal maxPrice, Product product, Pageable paging) {
		return orderRepository.searchBy(customerName, minPrice, maxPrice, product, paging);
	}

	private String[] customers = new String[] {
		"Mario Rossi",
		"Luca Bianchi",
		"Coca cola",
		"Fiat SPA",
		"Giovanni Verdi",
		"Luca Gialli",
		"Formaweb srl",
		"Zero11 srl"
	};
	
	@Transactional
	public void generateRandomOrders(int orders) {
		Random rnd = new Random();
		List<Product> products = productRepository.findAll();
		
		for (int i = 0; i < orders; i++) {
			Order order = new Order();
			order.setCustomerName(
					customers[rnd.nextInt(customers.length)]);			
			order.setOrderDate(
					LocalDate.now().minusDays(rnd.nextInt(200))					
			);
			order.setPrice(new BigDecimal(rnd.nextDouble() * 2000.0));
			order.setQuantity(rnd.nextInt(15));
			order.setProduct(
				products.get(
					rnd.nextInt(products.size())
				)
			);
			
			orderRepository.save(order);
		}		
	}
	
}
