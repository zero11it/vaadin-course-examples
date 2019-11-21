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
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginI18n.Form;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;

import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.model.Product;
import it.zero11.vaadin.course.service.BrandService;
import it.zero11.vaadin.course.service.ProductService;

@Route(value = "products/create")
@PageTitle("Products")
public class ProductsCreateView extends VerticalLayout
	implements BeforeLeaveObserver{
	private static final long serialVersionUID = 1L;
	
	private final Binder<Product> binder;

	public ProductsCreateView() {
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
		brandComboBox.setItems(BrandService.findAll());
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
			Product product = new Product();
			if (binder.writeBeanIfValid(product)) {
				ProductService.save(product);
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
}
