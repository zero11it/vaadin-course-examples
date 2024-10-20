package it.zero11.vaadin.course.view.products;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.data.BrandRepository;
import it.zero11.vaadin.course.data.ProductRepository;
import it.zero11.vaadin.course.layout.AuthenticatedLayout;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.utils.VaadinUtils;

@Route(value = "products", layout = AuthenticatedLayout.class)
@PageTitle("Products")
public class ProductsView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final Grid<Product> productsGrid;
	
	private NumberField minPriceFilter;
	private NumberField maxPriceFilter;
	
	public ProductsView(ProductRepository productRepository, BrandRepository brandRepository) {
		add(new H1("Products"));
		
		productsGrid = new Grid<>();
		productsGrid.setHeight("300px");
		Column<Product> idCol = productsGrid.addColumn(Product::getId).setHeader("Id");
		Column<Product> brandCol = productsGrid.addColumn(new ComponentRenderer<>(product -> {
			HorizontalLayout component = new HorizontalLayout();
			component.setDefaultVerticalComponentAlignment(Alignment.CENTER);
			if (product.getBrand() != null) {
				if (product.getBrand().getImageUrl() != null) {
					Image image = new Image(product.getBrand().getImageUrl(), "alt text");
					image.setHeight("40px");
					component.add(image);
				}
				NativeLabel label = new NativeLabel(product.getBrand().getName());
				label.getElement().getStyle().set("padding", "10px");
				component.add(label);
			}
			
			return component;
		}))
		.setComparator((p1, p2) -> {
			String name1 = p1.getBrand() != null ? p1.getBrand().getName() : "";
			String name2 = p2.getBrand() != null ? p2.getBrand().getName() : "";
			return name1.compareTo(name2);
		})
		.setSortProperty("brand.name")
		.setHeader("Brand").setSortable(true);
		Column<Product> eanCol = productsGrid.addColumn(Product::getEan).setHeader("Ean")
			.setSortProperty("ean")
			.setSortable(true).setResizable(true).setFlexGrow(0);
		Column<Product> publishDateCol =productsGrid.addColumn(product -> {
			return product.getPublishDate() == null ?
					"" :
					product.getPublishDate().format(
							DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}).setHeader("Published date")
		.setSortProperty("publishDate")
			.setResizable(true).setSortable(true).setFlexGrow(0);		
		Column<Product> skuCol = productsGrid.addColumn(Product::getSku).setHeader("SKU");
		Column<Product> descCol =productsGrid.addColumn(Product::getDescription).setHeader("Description");

		Column<Product> availabilityCol = productsGrid.addColumn(Product::getAvailability).setHeader("Availability")
				.setSortProperty("availability")
				.setSortable(true).setResizable(true).setFlexGrow(0);
		Column<Product> priceCol = productsGrid.addColumn(Product::getPrice).setHeader("Price").setSortable(true)
				.setSortProperty("price")				
				.setResizable(true).setFlexGrow(0);
			
		Column<Product> actionCol = productsGrid.addColumn(new ComponentRenderer<Span, Product>(product -> {
			Span span = new Span();
			
			Button edit = new Button("", VaadinIcon.EDIT.create());
			edit.addClickListener(e -> {
				UI.getCurrent().navigate(ProductsEditView.class, product.getId());
			});
			
			Button delete = new Button("", VaadinIcon.TRASH.create());
			delete.addClickListener(e -> {
				productRepository.delete(product);

				productsGrid.getDataProvider().refreshAll();
			});

			span.add(edit, delete);
			return span;
		}));

		productsGrid.setColumnReorderingAllowed(true);
		
//		HeaderRow header = productsGrid.prependHeaderRow();
//		HeaderCell anagCell = header.join(idCol, brandCol, eanCol, 
//				publishDateCol, skuCol, descCol);
//		anagCell.setComponent(new Label("Dati anagrafici"));
//		
//		HeaderCell warehouseCell = header.join(availabilityCol, priceCol, actionCol);
//		warehouseCell.setComponent(new Label("Dati magazzino"));
		
		
		HeaderRow header = productsGrid.prependHeaderRow();
		HeaderCell anagCell = header.join(idCol, brandCol, eanCol, 
				publishDateCol, skuCol, descCol);
		anagCell.setComponent(new NativeLabel("Dati anagrafici"));

		HeaderCell warehouseCell = header.join(availabilityCol, priceCol, actionCol);
		warehouseCell.setComponent(new NativeLabel("Dati magazzino"));
		
		HorizontalLayout filterLayout = new HorizontalLayout();
		filterLayout.setWidthFull();
		filterLayout.add(new NativeLabel("Sku"));
		TextField skuFilter = new TextField();	
		skuFilter.setValueChangeMode(ValueChangeMode.LAZY);
		
		skuFilter.addValueChangeListener(event -> 
			productsGrid.getDataProvider().refreshAll());
		
		filterLayout.add(skuFilter);
		filterLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		filterLayout.add(new NativeLabel("Range di prezzo"));
		minPriceFilter = new NumberField();
		maxPriceFilter = new NumberField();
		minPriceFilter.setValueChangeMode(ValueChangeMode.LAZY);
		maxPriceFilter.setValueChangeMode(ValueChangeMode.LAZY);
		minPriceFilter.addValueChangeListener(event -> productsGrid.getDataProvider().refreshAll());
		maxPriceFilter.addValueChangeListener(event -> productsGrid.getDataProvider().refreshAll());
		
		filterLayout.add(minPriceFilter, maxPriceFilter);
		header = productsGrid.prependHeaderRow();
		header.getCell(idCol).setComponent(filterLayout);
//		add(filterLayout);
		
		productsGrid.setItems(query -> {
		      
			try {
				BigDecimal min = minPriceFilter.getValue() != null ? 
						new BigDecimal(minPriceFilter.getValue()) : null;
				BigDecimal max = maxPriceFilter.getValue() != null ? 
						new BigDecimal(maxPriceFilter.getValue()) : null;
				Pageable pageable = VaadinUtils.toPageable(query);
				
				return productRepository.searchBy(skuFilter.getValue(), min, max, pageable).stream();
			} catch (Exception e) {
				Notification.show(e.getMessage());
				return Stream.empty();
			}
		});
		
		add(productsGrid);

		add(new RouterLink("New product", ProductsEditView.class));
	}

}
