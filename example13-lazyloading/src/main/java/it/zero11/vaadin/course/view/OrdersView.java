package it.zero11.vaadin.course.view;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.data.ProductRepository;
import it.zero11.vaadin.course.layout.MyLayout;
import it.zero11.vaadin.course.model.Order;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.service.OrderService;
import it.zero11.vaadin.course.utils.VaadinUtils;

@Route(value = "orders", layout = MyLayout.class)
public class OrdersView extends VerticalLayout {

	private TextField customerFilter;
	private NumberField minPriceFilter;
	private NumberField maxPriceFilter;
	private ComboBox<Product> productFilter;
	private Grid<Order> ordersGrid;
	
	private NativeLabel soldLabel;
	private ProgressBar progressBar;
	
	private final ProductRepository productRepository;
	private final OrderService orderService;
	
	public OrdersView(ProductRepository productRepository, OrderService orderService) {
		this.productRepository = productRepository;
		this.orderService = orderService;
		
		setSizeFull();
		
		add(new H1("Orders"));
		
		progressBar = new ProgressBar();
		add(progressBar);
		
		add(createFilters());
		
		add(createGrid());	
		
		add(createButtons());
	}
	
	private Component createFilters() {
		HorizontalLayout container = new HorizontalLayout();
				
		customerFilter = new TextField("Customer");
		customerFilter.setValueChangeMode(ValueChangeMode.LAZY);
		customerFilter.addValueChangeListener(e -> updateData());
		
		minPriceFilter = new NumberField("Min price");
		minPriceFilter.setValueChangeMode(ValueChangeMode.LAZY);
		minPriceFilter.addValueChangeListener(e -> updateData());
		
		maxPriceFilter = new NumberField("Max price");
		maxPriceFilter.setValueChangeMode(ValueChangeMode.LAZY);
		maxPriceFilter.addValueChangeListener(e -> updateData());
		
		productFilter = new ComboBox<>();
		productFilter.setLabel("Product");
		productFilter.setItems(productRepository.findAll());
		productFilter.setItemLabelGenerator(
				product -> product.getSku() != null ? 
						product.getSku() : "?"
		);
		productFilter.addValueChangeListener(e -> updateData());
		
		container.add(customerFilter, minPriceFilter, maxPriceFilter, productFilter);
		return container;
	}
	
	private Grid<Order> createGrid() {
		ordersGrid = new Grid<>();
		ordersGrid.setHeightFull();
		
		Column<Order> idCol = ordersGrid.addColumn(Order::getId)
				.setResizable(true)
			.setHeader("Id").setWidth("30px");
		ordersGrid.addColumn(Order::getCustomerName)
			.setResizable(true)
			.setHeader("Customer").setWidth("150px");
		ordersGrid.addColumn(
			order -> order.getOrderDate().format(
					DateTimeFormatter.ofPattern("dd/MM/yyyy"))
			)
			.setSortable(true)
			.setSortProperty("orderDate")
			.setHeader("Order date").setWidth("50px");
		ordersGrid.addColumn(order -> order.getProduct().getSku())
			.setResizable(true)
			.setSortable(true)
			.setSortProperty("product.sku")
			.setHeader("Product").setWidth("100px");
		ordersGrid.addColumn(Order::getQuantity)
			.setSortable(true)
			.setSortProperty("quantity")
			.setHeader("Quantity").setWidth("50px");
		ordersGrid.addColumn(Order::getPrice)
			.setSortable(true)
			.setSortProperty("price")
			.setHeader("Price").setWidth("70px");
		
		soldLabel = new NativeLabel();
		FooterRow footer = ordersGrid.appendFooterRow();
		footer.getCell(idCol).setComponent(soldLabel);
		
		ordersGrid.setItems(query -> {
		      
			try {
				BigDecimal min = minPriceFilter.getValue() != null ? 
						new BigDecimal(minPriceFilter.getValue()) : null;
				BigDecimal max = maxPriceFilter.getValue() != null ? 
						new BigDecimal(maxPriceFilter.getValue()) : null;
				
				BigDecimal current = orderService.getTotalSold(customerFilter.getValue(), 
						min, max, productFilter.getValue());					
				BigDecimal total = orderService.getTotalSold();
				
//				soldLabel.setText("Vendite " + current + " su " + total + " - " +
//						current.divide(total).toString() + "%");
				soldLabel.setText("Vendite " + current + " su " + total + " - " +
						current.doubleValue() / total.doubleValue() * 100.0 + "%");
				
				progressBar.setMax(total.doubleValue());
				progressBar.setValue(current.doubleValue());
				
				Pageable pageable = VaadinUtils.toPageable(query);
				
				return orderService.searchBy(customerFilter.getValue(), min, max, 
						productFilter.getValue(), pageable).stream();
			} catch (Exception e) {
				Notification.show(e.getMessage());
				return Stream.empty();
			}
		});
		
		return ordersGrid;
	}
	
	private Component createButtons() {
		Button generateButton = new Button("Generate orders");
		generateButton.addClickListener(e -> {
			orderService.generateRandomOrders(1000);
			updateData();
			Notification.show("Operation completed successfully");
		});
		
		return generateButton;
	}
	
	private void updateData() {
		ordersGrid.getDataProvider().refreshAll();
	}
}
