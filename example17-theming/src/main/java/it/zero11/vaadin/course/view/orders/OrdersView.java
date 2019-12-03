package it.zero11.vaadin.course.view.orders;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.components.CircleComponent;
import it.zero11.vaadin.course.components.RangeSlider;
import it.zero11.vaadin.course.layout.AuthenticatedLayout;
import it.zero11.vaadin.course.model.Order;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.service.OrderService;
import it.zero11.vaadin.course.service.ProductService;
import it.zero11.vaadin.course.utils.OrderUtils;
import it.zero11.vaadin.course.view.AbstractSearchView;

@Route(value = "orders", layout = AuthenticatedLayout.class)
public class OrdersView extends AbstractSearchView<Order> {

	private TextField customerFilter;
//	private NumberField minPriceFilter;
//	private NumberField maxPriceFilter;
	private ComboBox<Product> productFilter;
	
	private RangeSlider rangeSlider;
	private Label soldLabel;
	private CircleComponent circleComponent;
	
	public OrdersView() {
		super();
	}

	private DataProvider<Order, Void> createDataProvider() {
	
		return DataProvider.fromCallbacks( 
				query -> {
//					BigDecimal min = minPriceFilter.getValue() != null ? 
//							new BigDecimal(minPriceFilter.getValue()) : null;
//					BigDecimal max = maxPriceFilter.getValue() != null ? 
//							new BigDecimal(maxPriceFilter.getValue()) : null;
					BigDecimal min = new BigDecimal(rangeSlider.getLowValue());
					BigDecimal max = new BigDecimal(rangeSlider.getHighValue());
										
					BigDecimal current = OrderService.getTotalSold(customerFilter.getValue(), 
							min, max, productFilter.getValue());					
					BigDecimal total = OrderService.getTotalSold();
					
//					soldLabel.setText("Vendite " + current + " su " + total + " - " +
//							current.divide(total).toString() + "%");
					soldLabel.setText("Vendite " + current + " su " + total + " - " +
							current.doubleValue() / total.doubleValue() * 100.0 + "%");
					
					circleComponent.setPercent(							
							(int) (current.doubleValue() / total.doubleValue() * 100.0)
							 );
					
					return OrderService.findBy(query.getOffset(), query.getLimit(), 
							query.getSortOrders(),
							customerFilter.getValue(), min, max, productFilter.getValue())
							.stream();
				},
				query -> {
//					BigDecimal min = minPriceFilter.getValue() != null ? 
//							new BigDecimal(minPriceFilter.getValue()) : null;
//					BigDecimal max = maxPriceFilter.getValue() != null ? 
//							new BigDecimal(maxPriceFilter.getValue()) : null;
					BigDecimal min = new BigDecimal(rangeSlider.getLowValue());
					BigDecimal max = new BigDecimal(rangeSlider.getHighValue());
									
					return OrderService.countBy(customerFilter.getValue(), min, max, 
							productFilter.getValue()).intValue();
				}
			);
	}
	
	private void updateData() {
		grid.getDataProvider().refreshAll();
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
		
		VerticalLayout rangeSliderWrapper = new VerticalLayout();
		rangeSliderWrapper.setWidth("auto");
		
		Label label = new Label("Intervallo prezzo");
		label.getElement().getStyle().set("font-size", "var(--lumo-font-size-s)");
		
		rangeSlider = new RangeSlider();
		rangeSlider.setMin(0.0);
		rangeSlider.setMax(4000.0);
		rangeSlider.setLowHighValue(800.0, 900.0);
		rangeSlider.setPin(Boolean.TRUE);
		rangeSlider.addLowValueChangeListener(e -> updateData());
		rangeSlider.addHighValueChangeListener(e -> updateData());
		
		rangeSliderWrapper.add(label, rangeSlider);
		
		productFilter = new ComboBox<>();
		productFilter.setLabel(getTranslation("orders.product"));
		productFilter.setItems(ProductService.findAll());
		productFilter.setItemLabelGenerator(
				product -> product.getSku() != null ? 
						product.getSku() : "?"
		);
		productFilter.addValueChangeListener(e -> updateData());

		circleComponent = new CircleComponent();
		circleComponent.setPercent(0.0);
		circleComponent.setColor("#333");
		circleComponent.setTitle("");
		circleComponent.setSWidth(3);
		circleComponent.setRadio(50);
		container.add(circleComponent, customerFilter, rangeSliderWrapper, productFilter);
	}


	@Override
	protected Grid<Order> createGrid() {
		Grid<Order> ordersGrid = new Grid<>();
		ordersGrid.setHeightFull();
		
		Column<Order> idCol = ordersGrid.addColumn(Order::getId)
				.setResizable(true)
			.setHeader("Id").setWidth("30px");
		ordersGrid.addColumn(Order::getCustomerName)
			.setResizable(true)
			.setHeader("Customer").setWidth("150px");
		ordersGrid.addColumn(
			order -> order.getOrderDate().format(
					DateTimeFormatter
						.ofLocalizedDate(FormatStyle.SHORT)
						.withLocale(UI.getCurrent().getLocale()))
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
		ordersGrid.addColumn(order -> {
				return NumberFormat
						.getCurrencyInstance(
							new Locale(UI.getCurrent().getLocale().getLanguage(),
							Locale.ITALY.getCountry())
						)
						.format(order.getPrice());
			})
			.setSortable(true)
			.setSortProperty("price")
			.setHeader("Price").setWidth("70px");
		
		soldLabel = new Label();
		FooterRow footer = ordersGrid.appendFooterRow();
		footer.getCell(idCol).setComponent(soldLabel);
		
		ordersGrid.setDataProvider(createDataProvider());
		
		return ordersGrid;
	}


	@Override
	protected void addActions(HasComponents container) {
		Button generateButton = new Button("Generate orders");
		generateButton.addClickListener(e -> {
			OrderUtils.generateRandomOrders(1000);
			updateData();
			Notification.show("Operation completed successfully");
		});
		
		generateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
		
		container.add(generateButton);
	}
}
