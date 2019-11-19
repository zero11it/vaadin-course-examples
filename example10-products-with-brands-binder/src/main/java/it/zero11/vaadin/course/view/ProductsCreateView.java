package it.zero11.vaadin.course.view;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
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
	
	private final Product product;

	public ProductsCreateView() {
		product = new Product();
		
		Binder<Product> binder = new Binder<>(Product.class);
		
		TextField skuTextField = new TextField();
		skuTextField.setLabel("SKU");
		binder.forField(skuTextField)
			.asRequired("Required")
			.bind(Product::getSku, Product::setSku);
		add(skuTextField);

		ComboBox<Brand> brandComboBox = new ComboBox<Brand>();
		brandComboBox.setLabel("Brand");
		brandComboBox.setItemLabelGenerator(Brand::getName);
		brandComboBox.setItems(BrandService.findAll());
		binder.forField(brandComboBox)
			.asRequired("Required")
			.bind(Product::getBrand, Product::setBrand);
		add(brandComboBox);

		TextArea descriptionTextArea = new TextArea();
		descriptionTextArea.setLabel("Description");
		binder.forField(descriptionTextArea)
			.bind(Product::getDescription, Product::setDescription);
		add(descriptionTextArea);

		DatePicker publishDatePicker = new DatePicker("Publish Date");
		binder.forField(publishDatePicker)
			.withValidator((d) -> d == null || d.isAfter(LocalDate.now()), "Leave empty of select a date in the future")
			.withConverter(localDate -> (Date) Date.from(localDate.atStartOfDay()
				      .atZone(ZoneId.systemDefault())
				      .toInstant()), date -> Instant.ofEpochMilli(date.getTime())
				      .atZone(ZoneId.systemDefault())
				      .toLocalDate())
			.bind(Product::getPublishDate, Product::setPublishDate);
		add(publishDatePicker);
		

		DatePicker firstAvailablePicker = new DatePicker("First Available Date");
		binder.forField(firstAvailablePicker)
			.bind(Product::getFirstAvailable, Product::setFirstAvailable);
		add(firstAvailablePicker);
		
		TextField availability = new TextField("Availability");
		binder.forField(availability)
			.asRequired("Required")
			.withConverter(new StringToIntegerConverter("Invalid number"))
			.bind(Product::getAvailability, Product::setAvailability);
		add(availability);

		TextField weight = new TextField("Weight");
		binder.forField(weight)
			.withNullRepresentation("")
			.withConverter(new StringToBigDecimalConverter("Invalid number"))
			.bind(Product::getWeight, Product::setWeight);
		add(weight);

		TextField price = new TextField("Price");
		binder.forField(price)
			.asRequired()
			.withConverter(new StringToBigDecimalConverter("Invalid price"))
			.bind(Product::getPrice, Product::setPrice);
		add(price);

		TextField cost = new TextField("Cost");
		binder.forField(cost)
			.withNullRepresentation("")
			.withConverter(new StringToBigDecimalConverter("Invalid number"))
			.bind(Product::getCost, Product::setCost);
		add(cost);
		
		TextField ean = new TextField("EAN");
		binder.forField(ean)
			.withNullRepresentation("")
			.withValidator(v -> v == null || v.length() == 0 || (StringUtils.isNumeric(v) && v.length() == 13), "Invalid EAN")
			.bind(Product::getEan, Product::setEan);
		add(ean);
		
		binder.setBean(product);
		
		Button productSaveButton = new Button("Save product");
		productSaveButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
				ProductService.save(product);
				UI.getCurrent().navigate(ProductsView.class);
			}
		});
		add(productSaveButton);
	}
}
