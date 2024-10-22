package it.zero11.vaadin.course.view.users;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.layout.AuthenticatedLayout;
import it.zero11.vaadin.course.model.Ruolo;
import it.zero11.vaadin.course.model.User;
import it.zero11.vaadin.course.service.UserService;
import it.zero11.vaadin.course.view.LoginView;

@Route(value = "users/edit", layout = AuthenticatedLayout.class)
@PageTitle("Edit User")
public class UserEditView extends VerticalLayout
	implements BeforeLeaveObserver, HasUrlParameter<Long>,
		BeforeEnterObserver {
	private static final long serialVersionUID = 1L;
	private final Binder<User> binder;
	
	private User user;
	
	private final UserService userService;
	
	public UserEditView(UserService userService) {
		this.userService = userService;
		setSizeFull();
		
		binder = new Binder<>(User.class);
		
		FormLayout formLayout = new FormLayout();
		formLayout.getStyle().set("overflow-y", "auto");
		
		TextField usernameNameTextField = new TextField();
		usernameNameTextField.setLabel("Username");
		binder.forField(usernameNameTextField)
			.withNullRepresentation("")
			.asRequired("Campo obbligatorio")
			.bind(User::getUsername, User::setUsername);
		formLayout.add(usernameNameTextField);

		PasswordField passwordTextField = new PasswordField();
		passwordTextField.setLabel("Password");
		formLayout.add(passwordTextField);
		
		Select<Ruolo> ruoloField = new Select<Ruolo>();
		ruoloField.setItems(Ruolo.values());
		binder.forField(ruoloField)
			.asRequired("Campo obbligatorio")
			.bind(User::getRole, User::setRole);
		formLayout.add(ruoloField);
		
		Checkbox activeField = new Checkbox();
		binder.forField(activeField)
			.bind(User::isActive, User::setActive);
		formLayout.add(activeField);
		
		add(formLayout);
		setFlexGrow(1, formLayout);
		
		Button saveButton = new Button("Save User");
		saveButton.addClickListener(e -> {			
			try {
				if (user.getId() == null) {
					if (passwordTextField.getValue() == null || 
							passwordTextField.getValue().length() < 8) {
						Notification.show("La password è obbligatoria e deve essere di almeno 8 caratteri");
						return;
					}						
				} else {
					if (passwordTextField.getValue() != null &&
							passwordTextField.getValue().length() > 0 &&
							passwordTextField.getValue().length() < 8) {
						Notification.show("La password è deve essere di almeno 8 caratteri");
						return;
					}						
				}
				user.setPassword(passwordTextField.getValue());
				
				if (binder.writeBeanIfValid(user)) {
					userService.save(user);

					UI.getCurrent().navigate(UsersView.class);
				}
			} catch (Exception e1) {
				Notification.show(e1.getMessage());
			}
		});
		add(saveButton);
		
		Button cancelButton = new Button("Cancel",
				e -> UI.getCurrent().navigate(UsersView.class));

		add(new HorizontalLayout(saveButton, cancelButton));
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
		if (parameter == null) {
			user = new User();
			user.setActive(true);
		} else
			user = userService.load(parameter);		
		binder.setBean(user);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		User user = (User) UI.getCurrent().getSession()
				.getAttribute(LoginView.USER_SESSION_ATTRIBUTE);
		
		if (user != null && !user.getRole().equals(Ruolo.Admin)) {
			event.rerouteTo("");
		}	
	}
}
