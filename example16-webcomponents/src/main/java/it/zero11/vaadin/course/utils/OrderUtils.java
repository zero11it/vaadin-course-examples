package it.zero11.vaadin.course.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import it.zero11.vaadin.course.model.Order;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.service.OrderService;
import it.zero11.vaadin.course.service.ProductService;

public class OrderUtils {

	private static final String[] customers = new String[] {
		"Mario Rossi",
		"Luca Bianchi",
		"Coca cola",
		"Fiat SPA",
		"Giovanni Verdi",
		"Luca Gialli",
		"Formaweb srl",
		"Zero11 srl"
	};
	
	public static void generateRandomOrders(int orders) {
		Random rnd = new Random();
		List<Product> products = ProductService.findAll();
		
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
			
			OrderService.save(order);
		}		
	}
}
