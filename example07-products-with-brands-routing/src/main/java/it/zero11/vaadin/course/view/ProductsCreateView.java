package it.zero11.vaadin.course.view;

import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.service.BrandService;
import it.zero11.vaadin.course.service.ProductService;

@Route(value = "products/create")
@PageTitle("Products")
public class ProductsCreateView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final ComboBox<Brand> brandComboBox;
	private final TextField skuTextField;
	private final TextArea descriptionTextArea;
	private final Button productSaveButton;

	public ProductsCreateView() {
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
			ProductService.save(p);

			UI.getCurrent().navigate(ProductsView.class);
		});
		add(productSaveButton);

		updateBrandData();
	}

	private void updateBrandData() {
		List<Brand> brands = BrandService.findAll();
		brandComboBox.setItems(brands);
	}
}
