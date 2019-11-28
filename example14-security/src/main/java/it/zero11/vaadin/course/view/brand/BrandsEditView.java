package it.zero11.vaadin.course.view.brand;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.service.BrandService;

@Route(value = "brands/edit")
@PageTitle("Create Brand")
public class BrandsEditView extends VerticalLayout
	implements BeforeLeaveObserver, HasUrlParameter<Long>{
	private static final long serialVersionUID = 1L;
	private final Binder<Brand> binder;
	
	private Brand brand;
	
	public BrandsEditView() {
		setSizeFull();
		
		binder = new Binder<>(Brand.class);
		
		FormLayout formLayout = new FormLayout();
		formLayout.getStyle().set("overflow-y", "auto");
		
		TextField brandNameTextField = new TextField();
		brandNameTextField.setLabel("Brand");
		binder.forField(brandNameTextField)
			.withNullRepresentation("")
			.asRequired("Campo obbligatorio")
			.bind(Brand::getName, Brand::setName);
		formLayout.add(brandNameTextField);

		TextField imageUrlTextField = new TextField();
		imageUrlTextField.setLabel("Image");
		binder.forField(imageUrlTextField)			
			.bind(Brand::getImageUrl, Brand::setImageUrl);
		formLayout.add(imageUrlTextField);
	
		TextArea descriptionTextArea = new TextArea();
		descriptionTextArea.setLabel("Description");
		binder.forField(descriptionTextArea)
			.bind(Brand::getDescription, Brand::setDescription);
		formLayout.add(descriptionTextArea);
		
		add(formLayout);
		setFlexGrow(1, formLayout);
		
		Button brandSaveButton = new Button("Save Brand");
		brandSaveButton.addClickListener(e -> {			
			try {
				if (binder.writeBeanIfValid(brand)) {
					BrandService.save(brand);

					UI.getCurrent().navigate(BrandsView.class);
				}
			} catch (Exception e1) {
				Notification.show(e1.getMessage());
			}
		});
		add(brandSaveButton);
		
		Button cancelButton = new Button("Cancel",
				e -> UI.getCurrent().navigate(BrandsView.class));

		add(new HorizontalLayout(brandSaveButton, cancelButton));
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
			brand = new Brand();
		else
			brand = BrandService.load(parameter);
		binder.setBean(brand);
	}
}
