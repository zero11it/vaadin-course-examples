package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.service.BrandService;

@Route(value = "brands/create")
@PageTitle("Create Brand")
public class BrandsCreateView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final TextField brandNameTextField;
	private final Button brandSaveButton;

	public BrandsCreateView() {
		brandNameTextField = new TextField();
		brandNameTextField.setLabel("Brand");
		add(brandNameTextField);

		brandSaveButton = new Button("Save Brand");
		brandSaveButton.addClickListener(e -> {
			Brand b = new Brand();
			b.setName(brandNameTextField.getValue());
			BrandService.save(b);

			UI.getCurrent().navigate(BrandsView.class);
		});
		add(brandSaveButton);
	}

}
