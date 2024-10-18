package it.zero11.vaadin.course.view;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.data.BrandRepository;
import it.zero11.vaadin.course.data.ProductRepository;
import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.model.Product;

@Route(value = "products/edit")
@PageTitle("Products")
public class ProductsEditView extends VerticalLayout
	implements BeforeLeaveObserver, HasUrlParameter<Long> {
	private static final long serialVersionUID = 1L;
	
	private final Binder<Product> binder;

	private Product product;
	
	private final ProductRepository productRepository;
	
	public ProductsEditView(ProductRepository productRepository, BrandRepository brandRepository) {
		this.productRepository = productRepository;
		setSizeFull();
		
		binder = new Binder<>(Product.class);
		
		FormLayout formLayout = new FormLayout();
		formLayout.getStyle().set("overflow-y", "auto");
		formLayout.setWidthFull();
		
		TextField skuTextField = new TextField();
		skuTextField.setLabel("SKU");
		binder.forField(skuTextField)
			.withNullRepresentation("")
			.asRequired("Required")
			.bind(Product::getSku, Product::setSku);
		formLayout.add(skuTextField);

		ComboBox<Brand> brandComboBox = new ComboBox<Brand>();
		brandComboBox.setLabel("Brand");
		brandComboBox.setItemLabelGenerator(Brand::getName);
		brandComboBox.setItems(brandRepository.findAll());
		binder.forField(brandComboBox)
			.asRequired("Required")
			.bind(Product::getBrand, Product::setBrand);
		formLayout.add(brandComboBox);

		TextArea descriptionTextArea = new TextArea();
		descriptionTextArea.setLabel("Description");
		binder.forField(descriptionTextArea)
			.withNullRepresentation("")
			.bind(Product::getDescription, Product::setDescription);
		formLayout.add(descriptionTextArea);
		formLayout.setColspan(descriptionTextArea, 4);
		
		DatePicker publishDatePicker = new DatePicker("Publish Date");
		binder.forField(publishDatePicker)
			.withValidator((d) -> d == null || d.isAfter(LocalDate.now()), "Leave empty of select a date in the future")
			.bind(Product::getPublishDate, Product::setPublishDate);
		formLayout.add(publishDatePicker);
		

		DatePicker firstAvailablePicker = new DatePicker("First Available Date");
		binder.forField(firstAvailablePicker)
			.bind(Product::getFirstAvailable, Product::setFirstAvailable);
		formLayout.add(firstAvailablePicker);
		
		TextField availability = new TextField("Availability");
		binder.forField(availability)
			.withNullRepresentation("")
			.asRequired("Required")
			.withConverter(new StringToIntegerConverter("Invalid number"))
			.bind(Product::getAvailability, Product::setAvailability);
		formLayout.add(availability);

		TextField weight = new TextField("Weight");
		binder.forField(weight)
			.withNullRepresentation("")
			.withConverter(new StringToBigDecimalConverter("Invalid number"))
			.bind(Product::getWeight, Product::setWeight);
		formLayout.add(weight);

		TextField price = new TextField("Price");
		binder.forField(price)
			.withNullRepresentation("")
			.asRequired()
			.withConverter(new StringToBigDecimalConverter("Invalid price"))
			.bind(Product::getPrice, Product::setPrice);
		formLayout.add(price);

		TextField cost = new TextField("Cost");
		binder.forField(cost)
			.withNullRepresentation("")
			.withConverter(new StringToBigDecimalConverter("Invalid number"))
			.bind(Product::getCost, Product::setCost);
		formLayout.add(cost);
		
		TextField ean = new TextField("EAN");
		binder.forField(ean)
			.withNullRepresentation("")
			.withValidator(v -> v == null || v.length() == 0 || (StringUtils.isNumeric(v) && v.length() == 13), "Invalid EAN")
			.bind(Product::getEan, Product::setEan);
		formLayout.add(ean);
		
		add(formLayout);
		setFlexGrow(1, formLayout);
		
		Button productSaveButton = new Button("Save product");
		productSaveButton.addClickListener(e -> {			
			if (binder.writeBeanIfValid(product)) {
				productRepository.save(product);
				UI.getCurrent().navigate(ProductsView.class);
			}
		});

		Button cancelButton = new Button("Cancel",
				e -> UI.getCurrent().navigate(ProductsView.class));

		add(new HorizontalLayout(productSaveButton, cancelButton));
	}


	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		if (binder.hasChanges()) {
			ContinueNavigationAction continueNavigationAction = event.postpone();

			Dialog confirmDialog = new Dialog();
			confirmDialog.setCloseOnOutsideClick(false);
			confirmDialog.add(new Paragraph("Vuoi uscire ?"));
			Button confirmButton = new Button("Sì", e -> {
				continueNavigationAction.proceed();
				confirmDialog.close();
			});
			Button cancelButton = new Button("No", e -> {
				confirmDialog.close();
			});
			confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
			confirmDialog.open();
		}
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
		if (parameter == null)
			product = new Product();
		else
			product = productRepository.findById(parameter).orElseThrow();
		binder.setBean(product);		
	}
}
