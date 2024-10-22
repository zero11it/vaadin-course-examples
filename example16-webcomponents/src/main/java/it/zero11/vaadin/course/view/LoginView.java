package it.zero11.vaadin.course.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.components.LocaleSelector;
import it.zero11.vaadin.course.exceptions.NoUserException;
import it.zero11.vaadin.course.exceptions.UserDisabledException;
import it.zero11.vaadin.course.exceptions.WrongPasswordException;
import it.zero11.vaadin.course.i18n.Corso18NProvider;
import it.zero11.vaadin.course.model.User;
import it.zero11.vaadin.course.service.UserService;
import it.zero11.vaadin.course.view.products.ProductsView;

@SuppressWarnings("serial")
@Route(value = "login")
public class LoginView extends VerticalLayout {

	public static final String USER_SESSION_ATTRIBUTE = "user";
	
	@Autowired
	private UserService userService;
	
	public LoginView(Corso18NProvider i18n) {
		add(new H1(getTranslation("login.welcome")));
		setAlignItems(Alignment.CENTER);
		
		add(new LocaleSelector(i18n));
		
		LoginForm loginForm = new LoginForm();
		loginForm.addLoginListener(event -> {
			try {
				User user = userService.login(event.getUsername(), 
						event.getPassword());
				UI.getCurrent().getSession()
					.setAttribute(USER_SESSION_ATTRIBUTE, user);
				UI.getCurrent().navigate(ProductsView.class);
			} catch (NoUserException e) {
				Notification.show("L'utente non esiste");
				loginForm.setEnabled(true);
			} catch (UserDisabledException e) {
				Notification.show("L'utente è stato disabilitato. Si prega di contattare "
						+ "l'amministratore");
				loginForm.setEnabled(true);
			} catch (WrongPasswordException e) {
				Notification.show("Password errata");
				loginForm.setEnabled(true);				
			}
		});		
		add(loginForm);
		
	}

}
