package it.zero11.vaadin.course.view;

import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import it.zero11.vaadin.course.components.LocaleSelector;
import it.zero11.vaadin.course.exceptions.NoUserException;
import it.zero11.vaadin.course.exceptions.UserDisabledException;
import it.zero11.vaadin.course.exceptions.WrongPasswordException;
import it.zero11.vaadin.course.model.User;
import it.zero11.vaadin.course.service.UserService;
import it.zero11.vaadin.course.utils.TranslationProvider;
import it.zero11.vaadin.course.view.products.ProductsView;

@SuppressWarnings("serial")
@Route(value = "login")
public class LoginView extends VerticalLayout {

	public static final String USER_SESSION_ATTRIBUTE = "user";
	
	public LoginView() {
		add(new H1(getTranslation("login.welcome")));
		setAlignItems(Alignment.CENTER);
		
		add(new LocaleSelector());
		
		LoginForm loginForm = new LoginForm();
		loginForm.addLoginListener(event -> {
			try {
				User user = UserService.login(event.getUsername(), 
						event.getPassword());
				UI.getCurrent().getSession()
					.setAttribute(USER_SESSION_ATTRIBUTE, user);
				
				UI.getCurrent().navigate(ProductsView.class);				
			} catch (NoUserException e) {
				Notification.show(getTranslation("login.nouser"));
				loginForm.setEnabled(true);
			} catch (UserDisabledException e) {
				Notification.show(getTranslation("login.userdisabled"));
				loginForm.setEnabled(true);
			} catch (WrongPasswordException e) {
				Notification.show(getTranslation("login.wrongpassword"));
				loginForm.setEnabled(true);				
			}
		});	
		loginForm.setForgotPasswordButtonVisible(false);
		add(loginForm);
		
	}

}
