package it.zero11.vaadin.course.view.brand;

import org.vaadin.pekka.WysiwygE;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.service.BrandService;
import it.zero11.vaadin.course.view.AbstractEditView;

@SuppressWarnings("serial")
@Route(value = "brands/edit")
@PageTitle("Create Brand")
public class BrandsEditView extends AbstractEditView<Brand> {
	
	public BrandsEditView() {
		super();			
	}

	@Override
	protected Brand createEntity() {
		return new Brand();
	}

	@Override
	protected Brand loadEntity(Long id) {
		return BrandService.load(id);
	}

	@Override
	protected void populateForm(FormLayout layout) {
		TextField brandNameTextField = new TextField();
		brandNameTextField.setLabel("Brand");
		binder.forField(brandNameTextField)
			.withNullRepresentation("")
			.asRequired("Campo obbligatorio")
			.bind(Brand::getName, Brand::setName);
		layout.add(brandNameTextField);

		TextField imageUrlTextField = new TextField();
		imageUrlTextField.setLabel("Image");
		binder.forField(imageUrlTextField)			
			.bind(Brand::getImageUrl, Brand::setImageUrl);
		layout.add(imageUrlTextField);
	
		WysiwygE descriptionTextArea = new WysiwygE();				
		binder.forField(descriptionTextArea)
			.bind(Brand::getDescription, Brand::setDescription);
		layout.add(descriptionTextArea);
	}

	@Override
	protected void onSave() {
		try {
			if (binder.writeBeanIfValid(entity)) {
				BrandService.save(entity);
				UI.getCurrent().navigate(BrandsView.class);
			}
		} catch (Exception e1) {
			Notification.show(e1.getMessage());
		}
	}

	@Override
	protected void onCancel() {
		UI.getCurrent().navigate(BrandsView.class);
	}

}
