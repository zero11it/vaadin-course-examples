package it.zero11.vaadin.course.view.products;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.data.ProductRepository;
import it.zero11.vaadin.course.layout.AuthenticatedLayout;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.utils.VaadinUtils;
import it.zero11.vaadin.course.view.AbstractSearchView;

@Route(value = "", layout = AuthenticatedLayout.class)
public class ProductsView extends AbstractSearchView<Product> {
	private static final long serialVersionUID = 1L;
	
	private TextField skuFilter;
	private NumberField minPriceFilter;
	private NumberField maxPriceFilter;
	
	private final ProductRepository productRepository; 

	public ProductsView(ProductRepository productRepository) {
		this.productRepository = productRepository;
		render();
	}

	@Override
	protected String getTitle() {
		return getTranslation("products.title");
	}

	@Override
	protected void addFilters(HasComponents container) {
		
		skuFilter = new TextField(getTranslation("products.sku"));	
		skuFilter.setValueChangeMode(ValueChangeMode.LAZY);
		
		skuFilter.addValueChangeListener(event -> 
			grid.getDataProvider().refreshAll());
		
		VerticalLayout priceRangeLayout = new VerticalLayout();
		priceRangeLayout.setMargin(false);
		priceRangeLayout.setPadding(false);
		priceRangeLayout.add(new NativeLabel(getTranslation("products.pricerange")));		
		minPriceFilter = new NumberField();
		maxPriceFilter = new NumberField();
		minPriceFilter.setValueChangeMode(ValueChangeMode.LAZY);
		maxPriceFilter.setValueChangeMode(ValueChangeMode.LAZY);
		minPriceFilter.addValueChangeListener(event -> grid.getDataProvider().refreshAll());
		maxPriceFilter.addValueChangeListener(event -> grid.getDataProvider().refreshAll());
		HorizontalLayout priceRangeControls = new HorizontalLayout();
		priceRangeControls.add(minPriceFilter, maxPriceFilter);
		priceRangeControls.setMargin(false);
		priceRangeControls.setPadding(false);
		priceRangeLayout.add(priceRangeControls);
		
		container.add(skuFilter, priceRangeLayout);		
	}

	@Override
	protected Grid<Product> createGrid() {
		Grid<Product> productsGrid = new Grid<>();
		Column<Product> idCol = productsGrid.addColumn(Product::getId)
				.setWidth("40px")
				.setFlexGrow(0)
				.setHeader("Id");
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
		.setWidth("150px")
		.setFlexGrow(1)
		.setHeader(getTranslation("brands.brand")).setSortable(true);
		Column<Product> eanCol = productsGrid.addColumn(Product::getEan).setHeader("Ean")
			.setSortProperty("ean")
			.setSortable(true).setResizable(true).setFlexGrow(0);
		Column<Product> publishDateCol =productsGrid.addColumn(product -> {
			return product.getPublishDate() == null ?
					"" :
					product.getPublishDate().format(
							DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}).setHeader(getTranslation("products.publishingdate"))
		.setWidth("150px")
		.setSortProperty("publishDate")
			.setResizable(true).setSortable(true).setFlexGrow(0);		
		Column<Product> skuCol = productsGrid.addColumn(Product::getSku).setHeader(getTranslation("products.sku"));
		Column<Product> descCol =productsGrid.addColumn(Product::getDescription).setHeader(getTranslation("products.description"));

		Column<Product> availabilityCol = productsGrid.addColumn(Product::getAvailability).setHeader(getTranslation("products.availablility"))
				.setSortProperty("availability")
				.setSortable(true).setResizable(true).setFlexGrow(0);
		Column<Product> priceCol = productsGrid.addColumn(Product::getPrice).setHeader(getTranslation("products.price")).setSortable(true)
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
				try {
					productRepository.delete(product);

					productsGrid.getDataProvider().refreshAll();
				} catch (Exception e1) {
					Notification.show(e1.getLocalizedMessage(), 4000, Position.MIDDLE);
				}
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
		anagCell.setComponent(new NativeLabel(getTranslation("products.infodata")));

		HeaderCell warehouseCell = header.join(availabilityCol, priceCol, actionCol);
		warehouseCell.setComponent(new NativeLabel(getTranslation("products.warehousedata")));
		
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
		
		return productsGrid;
	}

	@Override
	protected void addActions(HasComponents container) {
		container.add(new RouterLink(getTranslation("products.new"), ProductsEditView.class));
	}

}
