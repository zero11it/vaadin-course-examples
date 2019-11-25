package it.zero11.vaadin.course.view;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.layout.MyLayout;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.service.ProductService;

@Route(value = "", layout = MyLayout.class)
@PageTitle("Products")
public class ProductsView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final Grid<Product> productsGrid;
	
	private ListDataProvider<Product> dataProvider;
	private NumberField minPriceFilter;
	private NumberField maxPriceFilter;
	
	public ProductsView() {
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
				Label label = new Label(product.getBrand().getName());
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
		.setHeader("Brand").setSortable(true);
		Column<Product> eanCol = productsGrid.addColumn(Product::getEan).setHeader("Ean")
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
				.setSortable(true).setResizable(true).setFlexGrow(0);
		Column<Product> priceCol = productsGrid.addColumn(Product::getPrice).setHeader("Price").setSortable(true)
				.setResizable(true).setFlexGrow(0);
			
		Column<Product> actionCol = productsGrid.addColumn(new ComponentRenderer<Span, Product>(product -> {
			Span span = new Span();
			
			Button edit = new Button("", VaadinIcon.EDIT.create());
			edit.addClickListener(e -> {
				UI.getCurrent().navigate(ProductsEditView.class, product.getId());
			});
			
			Button delete = new Button("", VaadinIcon.TRASH.create());
			delete.addClickListener(e -> {
				ProductService.remove(product);

				updateProductsData();
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
		anagCell.setComponent(new Label("Dati anagrafici"));

		HeaderCell warehouseCell = header.join(availabilityCol, priceCol, actionCol);
		warehouseCell.setComponent(new Label("Dati magazzino"));
		
		HorizontalLayout filterLayout = new HorizontalLayout();
		filterLayout.setWidthFull();
		filterLayout.add(new Label("Sku"));
		TextField skuFilter = new TextField();	
		skuFilter.setValueChangeMode(ValueChangeMode.LAZY);
		skuFilter.addValueChangeListener(event -> {
			dataProvider.setFilter(product -> {
					return product.getSku().contains(event.getValue());
				});
		});
		filterLayout.add(skuFilter);
		filterLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		filterLayout.add(new Label("Range di prezzo"));
		minPriceFilter = new NumberField();
		maxPriceFilter = new NumberField();
		minPriceFilter.setValueChangeMode(ValueChangeMode.LAZY);
		maxPriceFilter.setValueChangeMode(ValueChangeMode.LAZY);
		minPriceFilter.addValueChangeListener(event -> applyPriceFilter());
		maxPriceFilter.addValueChangeListener(event -> applyPriceFilter());
		
		filterLayout.add(minPriceFilter, maxPriceFilter);		
		
		header = productsGrid.prependHeaderRow();
		header.getCell(idCol).setComponent(filterLayout);
		add(filterLayout);
		
		
		add(productsGrid);

		add(new RouterLink("New product", ProductsEditView.class));
		updateProductsData();
	}

	private void applyPriceFilter() {
		dataProvider.setFilter(product -> {
			double minPrice = minPriceFilter.getValue() != null ? 
					minPriceFilter.getValue() : 0;
			double maxPrice = maxPriceFilter.getValue() != null ? 
					maxPriceFilter.getValue() : Double.MAX_VALUE;
			if (product.getPrice() == null)
				return false;
			else
				return product.getPrice().doubleValue() >= minPrice && 
						 product.getPrice().doubleValue() <= maxPrice;
		});
	}
	
	private void updateProductsData() {
		List<Product> products = ProductService.findAll();
		dataProvider = new ListDataProvider<Product>(products);
		productsGrid.setDataProvider(dataProvider);
		
	}

}
