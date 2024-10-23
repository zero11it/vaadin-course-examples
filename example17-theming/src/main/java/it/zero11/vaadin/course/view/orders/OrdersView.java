package it.zero11.vaadin.course.view.orders;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.components.CircleComponent;
import it.zero11.vaadin.course.components.RangeSlider;
import it.zero11.vaadin.course.data.ProductRepository;
import it.zero11.vaadin.course.layout.AuthenticatedLayout;
import it.zero11.vaadin.course.model.Order;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.service.OrderService;
import it.zero11.vaadin.course.utils.VaadinUtils;
import it.zero11.vaadin.course.view.AbstractSearchView;

@Route(value = "orders", layout = AuthenticatedLayout.class)
public class OrdersView extends AbstractSearchView<Order> {

	private TextField customerFilter;
//	private NumberField minPriceFilter;
//	private NumberField maxPriceFilter;
	private ComboBox<Product> productFilter;
	
	private RangeSlider rangeSlider;
	private NativeLabel soldLabel;
	private CircleComponent circleComponent;
	
	private final ProductRepository productRepository;
	private final OrderService orderService;
	
	public OrdersView(ProductRepository productRepository, OrderService orderService) {
		this.productRepository = productRepository;
		this.orderService = orderService;
				
		render();
	}

	@Override
	protected void render() {
		super.render();
		circleComponent = new CircleComponent();
		circleComponent.setPercent(0.0);
		circleComponent.setColor("pink");
		add(circleComponent);
	}

	private void updateData() {
		grid.setItems(query -> {
		      
			try {
//				BigDecimal min = minPriceFilter.getValue() != null ? 
//						new BigDecimal(minPriceFilter.getValue()) : null;
//				BigDecimal max = maxPriceFilter.getValue() != null ? 
//						new BigDecimal(maxPriceFilter.getValue()) : null;
				BigDecimal min = new BigDecimal(rangeSlider.getLowValue());
				BigDecimal max = new BigDecimal(rangeSlider.getHighValue());
				
				BigDecimal current = orderService.getTotalSold(customerFilter.getValue(), 
						min, max, productFilter.getValue());					
				BigDecimal total = orderService.getTotalSold();
				
				if (current != null && total != null) {
					double percent = current.doubleValue() / total.doubleValue() * 100.0;
					
					soldLabel.setText(
							String.format(getTranslation("orders.sellsummary"), 
							current.toString(), total.toString(), percent + "%"));
					
					circleComponent.setPercent((int) percent);
				}
				
				Pageable pageable = VaadinUtils.toPageable(query);
				
				return orderService.searchBy(customerFilter.getValue(), min, max, 
						productFilter.getValue(), pageable).stream();
			} catch (Exception e) {
				Notification.show(e.getMessage());
				return Stream.empty();
			}
		});
	}

	@Override
	protected String getTitle() {
		return getTranslation("orders.title");
	}


	@Override
	protected void addFilters(HasComponents container) {
		customerFilter = new TextField(getTranslation("orders.customer"));
		customerFilter.setValueChangeMode(ValueChangeMode.LAZY);
		customerFilter.addValueChangeListener(e -> updateData());
		
//		minPriceFilter = new NumberField(getTranslation("orders.minprice"));
//		minPriceFilter.setValueChangeMode(ValueChangeMode.LAZY);
//		minPriceFilter.addValueChangeListener(e -> updateData());
//		
//		maxPriceFilter = new NumberField(getTranslation("orders.maxprice"));
//		maxPriceFilter.setValueChangeMode(ValueChangeMode.LAZY);
//		maxPriceFilter.addValueChangeListener(e -> updateData());
		
		rangeSlider = new RangeSlider();
		rangeSlider.setMin(0.0);
		rangeSlider.setMax(2000.0);
		rangeSlider.setLowHighValue(0.0, 2000.0);
		rangeSlider.addLowValueChangeListener(e -> updateData());
		rangeSlider.addHighValueChangeListener(e -> updateData());
		
		productFilter = new ComboBox<>();
		productFilter.setLabel(getTranslation("products.product"));
		productFilter.setItems(productRepository.findAll());
		productFilter.setItemLabelGenerator(
				product -> product.getSku() != null ? 
						product.getSku() : "?"
		);
		productFilter.addValueChangeListener(e -> updateData());
		
		container.add(customerFilter, rangeSlider, productFilter);
	}


	@Override
	protected Grid<Order> createGrid() {
		Grid<Order> ordersGrid = new Grid<>();
		ordersGrid.setHeightFull();
		
		Column<Order> idCol = ordersGrid.addColumn(Order::getId)
				.setResizable(true)
			.setHeader("Id").setWidth("30px");
		Column<Order> customerCol =ordersGrid.addColumn(Order::getCustomerName)
			.setResizable(true)
			.setHeader(getTranslation("orders.customer")).setWidth("150px");
		ordersGrid.addColumn(
			order -> order.getOrderDate().format(
					DateTimeFormatter.ofPattern("dd/MM/yyyy"))
			)
			.setSortable(true)
			.setSortProperty("orderDate")
			.setHeader(getTranslation("orders.orderdate")).setWidth("50px");
		ordersGrid.addColumn(order -> order.getProduct().getSku())
			.setResizable(true)
			.setSortable(true)
			.setSortProperty("product.sku")
			.setHeader(getTranslation("products.product")).setWidth("100px");
		ordersGrid.addColumn(order -> {
			return NumberFormat
					.getCurrencyInstance(UI.getCurrent().getLocale())
					.format(order.getPrice());
		})
		.setHeader(getTranslation("orders.quantity")).setWidth("50px");		
		ordersGrid.addColumn(Order::getPrice)
			.setSortable(true)
			.setSortProperty("price")
			.setHeader(getTranslation("orders.price")).setWidth("70px");
		
		soldLabel = new NativeLabel();
		FooterRow footer = ordersGrid.appendFooterRow();
		footer = ordersGrid.appendFooterRow();
		footer.join(idCol, customerCol).setComponent(soldLabel);
		
		return ordersGrid;
	}


	@Override
	protected void addActions(HasComponents container) {
		Button generateButton = new Button(getTranslation("orders.generate"));
		generateButton.addClickListener(e -> {
			orderService.generateRandomOrders(1000);
			updateData();
			Notification.show(getTranslation("generic.operationok"));
		});
		
		container.add(generateButton);
	}
}
