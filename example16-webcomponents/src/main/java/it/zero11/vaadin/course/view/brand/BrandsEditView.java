package it.zero11.vaadin.course.view.brand;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.data.BrandRepository;
import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.view.AbstractEditView;

@SuppressWarnings("serial")
@Route(value = "brands/edit")
public class BrandsEditView extends AbstractEditView<Brand> {
	
	private final BrandRepository brandRepository;
	
	public BrandsEditView(BrandRepository brandRepository) {
		super();
		this.brandRepository = brandRepository;	
	}

	@Override
	protected Brand createEntity() {
		return new Brand();
	}

	@Override
	protected Brand loadEntity(Long id) {
		return brandRepository.findById(id).orElseThrow();
	}

	@Override
	protected void populateForm(FormLayout layout) {
		TextField brandNameTextField = new TextField();
		brandNameTextField.setLabel(getTranslation("brands.brand"));
		binder.forField(brandNameTextField)
			.withNullRepresentation("")
			.asRequired(getTranslation("generic.mandatory"))
			.bind(Brand::getName, Brand::setName);
		layout.add(brandNameTextField);

		TextField imageUrlTextField = new TextField();
		imageUrlTextField.setLabel(getTranslation("brands.grid.image"));
		binder.forField(imageUrlTextField)			
			.bind(Brand::getImageUrl, Brand::setImageUrl);
		layout.add(imageUrlTextField);
	
		TextArea descriptionTextArea = new TextArea();				
		binder.forField(descriptionTextArea)
			.bind(Brand::getDescription, Brand::setDescription);
		layout.add(descriptionTextArea);
	}

	@Override
	protected void onSave() {
		try {
			if (binder.writeBeanIfValid(entity)) {
				brandRepository.save(entity);
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

	@Override
	public String getPageTitle() {
		return getTranslation("brands.edit");
	}

}
