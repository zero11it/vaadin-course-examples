package it.zero11.vaadin.course.view.products;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.layout.AuthenticatedLayout;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.service.ProductService;
import it.zero11.vaadin.course.view.AbstractSearchView;
import it.zero11.vaadin.course.view.brand.BrandsEditView;

@Route(value = "", layout = AuthenticatedLayout.class)
@PageTitle("Products")
public class ProductsView extends AbstractSearchView<Product> {
	private static final long serialVersionUID = 1L;
	
	private TextField skuFilter;
	private NumberField minPriceFilter;
	private NumberField maxPriceFilter;
	
	public ProductsView() {
		super();		
	}

	@Override
	protected String getTitle() {
		return "Products";
	}

	private DataProvider<Product, Void> createDataProvider() {
		return DataProvider.fromCallbacks( 
				query -> {
					BigDecimal min = minPriceFilter.getValue() != null ? 
							new BigDecimal(minPriceFilter.getValue()) : null;
					BigDecimal max = maxPriceFilter.getValue() != null ? 
							new BigDecimal(maxPriceFilter.getValue()) : null;
					
					return ProductService.findBy(query.getOffset(), query.getLimit(), query.getSortOrders(),
							skuFilter.getValue(), min, max)
							.stream();
				},
				query -> {
					BigDecimal min = minPriceFilter.getValue() != null ? 
							new BigDecimal(minPriceFilter.getValue()) : null;
					BigDecimal max = maxPriceFilter.getValue() != null ? 
							new BigDecimal(maxPriceFilter.getValue()) : null;
							
					return ProductService.countBy(skuFilter.getValue(), min, max).intValue();
				}
			);	
	}
	
	@Override
	protected void addFilters(HasComponents container) {
		
		skuFilter = new TextField("Sku");	
		skuFilter.setValueChangeMode(ValueChangeMode.LAZY);
		
		skuFilter.addValueChangeListener(event -> 
			grid.getDataProvider().refreshAll());
		
		VerticalLayout priceRangeLayout = new VerticalLayout();
		priceRangeLayout.setSpacing(false);
		priceRangeLayout.setWidth("auto");
		priceRangeLayout.setMargin(false);
		priceRangeLayout.setPadding(false);
		Label label = new Label("Range di prezzo");
		label.getElement().getStyle().set("font-size", "var(--lumo-font-size-s)");
		priceRangeLayout.add(label);
		priceRangeLayout.setAlignSelf(Alignment.CENTER, label);
		
		
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
			
		Column<Product> actionCol = productsGrid.addColumn(new ComponentRenderer<HorizontalLayout, Product>(product -> {
			HorizontalLayout layout = new HorizontalLayout();
			
			Button edit = new Button("", VaadinIcon.EDIT.create());
			edit.addClickListener(e -> {
				UI.getCurrent().navigate(ProductsEditView.class, product.getId());
			});
			
			Button delete = new Button("", VaadinIcon.TRASH.create());
			delete.addClickListener(e -> {
				ProductService.remove(product);

				productsGrid.getDataProvider().refreshAll();
			});

			layout.add(edit, delete);
			return layout;
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
		
		productsGrid.setDataProvider(createDataProvider());
		
		return productsGrid;
	}

	@Override
	protected void addActions(HasComponents container) {
		Button newProduct = new Button(VaadinIcon.PLUS.create(), e -> {
			UI.getCurrent().navigate(ProductsEditView.class);
		});
		newProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
		container.add(newProduct);
	}

}
