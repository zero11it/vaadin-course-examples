package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.service.BrandService;

@Route(value = "brands/create")
@PageTitle("Create Brand")
public class BrandsCreateView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final Brand brand;

	public BrandsCreateView() {
		brand = new Brand();
		
		Binder<Brand> binder = new Binder<>(Brand.class);
		
		TextField brandNameTextField = new TextField();
		brandNameTextField.setLabel("Brand");
		binder.forField(brandNameTextField)
			.asRequired("Campo obbligatorio")
			.bind(Brand::getName, Brand::setName);
		add(brandNameTextField);

		binder.setBean(brand);
		
		Button brandSaveButton = new Button("Save Brand");
		brandSaveButton.addClickListener(e -> {
			if (binder.validate().isOk()) {
				BrandService.save(brand);
	
				UI.getCurrent().navigate(BrandsView.class);
			}
		});
		add(brandSaveButton);
	}

}
