package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;

public abstract class AbstractEditView<T> extends VerticalLayout
	implements BeforeLeaveObserver, HasDynamicTitle, HasUrlParameter<Long> {

	protected Binder<T> binder;
	
	protected T entity;
	
	protected FormLayout formLayout;
	
	protected HorizontalLayout actionLayout;
	
	protected Button saveButton;
	
	protected Button cancelButton;
		
	public AbstractEditView() {		
		setSizeFull();
		
		binder = new Binder<T>();
		
		formLayout = new FormLayout();
		formLayout.getStyle().set("overflow-y", "auto");
		populateForm(formLayout);
		add(formLayout);
		setFlexGrow(1, formLayout);
		
		actionLayout = new HorizontalLayout();
		actionLayout.setWidthFull();
		
		saveButton = new Button("Save");
		saveButton.addClickListener(e -> onSave());
		cancelButton = new Button("Cancel", e -> onCancel());

		actionLayout.add(saveButton, cancelButton);
		add(actionLayout);
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
			entity = createEntity();
		else
			entity = loadEntity(parameter);
		binder.readBean(entity);
	}

	abstract protected T createEntity();
	
	abstract protected T loadEntity(Long id);
	
	abstract protected void populateForm(FormLayout layout);
	
	abstract protected void onSave();

	abstract protected void onCancel();
	
}

