package it.zero11.vaadin.course.view;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.data.BrandRepository;
import it.zero11.vaadin.course.data.ProductRepository;
import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.model.Product;

@Route(value = "")
@PageTitle("Index")
public class ProductsView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final Grid<Product> productsGrid;
	private final ComboBox<Brand> brandComboBox;
	private final TextField skuTextField;
	private final TextArea descriptionTextArea;
	private final Button productSaveButton;
	private final Grid<Brand> brandGrid;
	private final TextField brandNameTextField;
	private final Button brandSaveButton;

	private final ProductRepository productRepository;
	private final BrandRepository brandRepository;
	
	public ProductsView(ProductRepository productRepository, BrandRepository brandRepository) {
		this.productRepository = productRepository;
		this.brandRepository = brandRepository;
		
		brandGrid = new Grid<>();
		brandGrid.setHeight("150px");
		brandGrid.addColumn(Brand::getId).setHeader("Id");
		brandGrid.addColumn(Brand::getName).setHeader("Name").setSortable(true);
		brandGrid.addColumn(new ComponentRenderer<Button, Brand>(brand -> {
			Button delete = new Button("", VaadinIcon.TRASH.create());
			delete.addClickListener(event -> {
				try {
					brandRepository.delete(brand);

					updateBrandData();
				}catch(Exception exception) {
					Notification.show("An error occurred while deleting the brand");
				}
			});

			return delete;
		}));

		add(brandGrid);

		brandNameTextField = new TextField();
		brandNameTextField.setLabel("Brand");
		add(brandNameTextField);

		brandSaveButton = new Button("Save Brand");
		brandSaveButton.addClickListener(e -> {
			Brand b = new Brand();
			b.setName(brandNameTextField.getValue());
			brandRepository.save(b);

			brandNameTextField.clear();
			brandNameTextField.focus();

			updateBrandData();
		});
		add(brandSaveButton);

		productsGrid = new Grid<>();
		productsGrid.setHeight("150px");
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

		skuTextField = new TextField();
		skuTextField.setLabel("SKU");
		add(skuTextField);

		brandComboBox = new ComboBox<Brand>();
		brandComboBox.setLabel("Brand");
		brandComboBox.setItemLabelGenerator(Brand::getName);
		add(brandComboBox);

		descriptionTextArea = new TextArea();
		descriptionTextArea.setLabel("Description");
		add(descriptionTextArea);

		productSaveButton = new Button("Save product");
		productSaveButton.addClickListener(e -> {
			Product p = new Product();
			p.setSku(skuTextField.getValue());
			p.setBrand(brandComboBox.getValue());
			p.setDescription(descriptionTextArea.getValue());
			productRepository.save(p);

			skuTextField.clear();
			brandComboBox.clear();
			descriptionTextArea.clear();
			skuTextField.focus();

			updateProductsData();
		});
		add(productSaveButton);

		updateBrandData();
		updateProductsData();
	}

	private void updateBrandData() {
		List<Brand> brands = brandRepository.findAll();
		brandGrid.setItems(brands);
		brandComboBox.setItems(brands);
	}

	private void updateProductsData() {
		productsGrid.setItems(productRepository.findAll());
	}

}
