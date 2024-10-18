package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.data.ProductRepository;
import it.zero11.vaadin.course.model.Product;

@Route(value = "")
@PageTitle("Index")
public class ProductsView extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;
	private final Grid<Product> productsGrid;
	private final TextField brandTextField;
	private final TextField skuTextField;
	private final TextArea descriptionTextArea;
	private final Button saveButton;
	
	private final ProductRepository productRepository;
	
	public ProductsView(ProductRepository productRepository) {
		this.productRepository = productRepository;
		
		productsGrid = new Grid<>(Product.class);
		productsGrid.setHeight("300px");
		productsGrid.addColumn(new ComponentRenderer<Button, Product>(product -> {
			Button delete = new Button("", VaadinIcon.TRASH.create());
			delete.addClickListener(e -> {
				productRepository.delete(product);
					
				updateGridData();
			});
			
			return delete;
		}));
		
		
		add(productsGrid);

		skuTextField = new TextField();
		skuTextField.setLabel("SKU");
		add(skuTextField);

		brandTextField = new TextField();
		brandTextField.setLabel("Brand");
		add(brandTextField);
		
		descriptionTextArea = new TextArea();
		descriptionTextArea.setLabel("Description");
		add(descriptionTextArea);
		
		saveButton = new Button("Save");
		saveButton.addClickListener(e -> {
			Product p = new Product();
			p.setSku(skuTextField.getValue());
			p.setBrand(brandTextField.getValue());
			p.setDescription(descriptionTextArea.getValue());
			productRepository.save(p);
			
			skuTextField.clear();
			brandTextField.clear();
			descriptionTextArea.clear();
			skuTextField.focus();
			
			updateGridData();
		});
		add(saveButton);
		
		updateGridData();
    }
	
	private void updateGridData() {
		productsGrid.setItems(productRepository.findAll());
	}

}
