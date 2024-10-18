package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.data.ProductRepository;
import it.zero11.vaadin.course.layout.MyLayout;
import it.zero11.vaadin.course.model.Product;

@Route(value = "", layout = MyLayout.class)
@PageTitle("Products")
public class ProductsView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final Grid<Product> productsGrid;

	private final ProductRepository productRepository;
	
	public ProductsView(ProductRepository productRepository) {
		this.productRepository = productRepository;		
		add(new H1("Products"));
		
		productsGrid = new Grid<>();
		productsGrid.setHeight("300px");
		productsGrid.addColumn(Product::getId).setHeader("Id");
		productsGrid.addColumn(p -> (p.getBrand() != null ? p.getBrand().getName() : "No Brand")).setHeader("Brand").setSortable(true);
		productsGrid.addColumn(Product::getSku).setHeader("SKU");
		productsGrid.addColumn(Product::getDescription).setHeader("Description");
		productsGrid.addColumn(new ComponentRenderer<Button, Product>(product -> {
			Button delete = new Button("", VaadinIcon.TRASH.create());
			delete.addClickListener(e -> {
				productRepository.delete(product);

				updateProductsData();
			});

			return delete;
		}));


		add(productsGrid);

		add(new RouterLink("New product", ProductsCreateView.class));
		updateProductsData();
	}

	private void updateProductsData() {
		productsGrid.setItems(productRepository.findAll());
	}

}
