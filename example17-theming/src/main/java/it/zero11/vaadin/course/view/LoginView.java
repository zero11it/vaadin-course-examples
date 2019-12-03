package it.zero11.vaadin.course.view;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.Cookie;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

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

		Checkbox rememberMe = new Checkbox("Ricordami");
		
		LoginForm loginForm = new LoginForm();
		loginForm.addLoginListener(event -> {
			try {
				User user = UserService.login(event.getUsername(), 
						event.getPassword());
				UI.getCurrent().getSession()
					.setAttribute(USER_SESSION_ATTRIBUTE, user);
				
				if (rememberMe.getValue() != null && rememberMe.getValue()) {
					String token = UUID.randomUUID().toString();
					user.setToken(token);
					user.setPassword(null);
					UserService.save(user);
					
					
					Cookie cookie = new Cookie("token", token);
					cookie.setMaxAge(86400);
					VaadinServletResponse.getCurrent().addCookie(cookie);
				}
				
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
		
		add(rememberMe);
		
		boolean found = false;
		Cookie[] cookies = VaadinServletRequest.getCurrent().getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("privacy-accepted")) {
					found = true;
				}
			}
		} 
		if (!found) {
			Dialog cookieDialog = new Dialog();
			cookieDialog.add(new Paragraph("Attetti i cookie ?"));
			
			Button acceptButton = new Button("Accetta", e -> {
				Cookie cookie = new Cookie("privacy-accepted", "true");
				VaadinServletResponse.getCurrent().addCookie(cookie);
				cookieDialog.close();
			});
			
			Button rejectButton = new Button("Rifiuta", e -> {
				UI.getCurrent().getPage().setLocation("https://www.google.it");
			});
			
			cookieDialog.add(new HorizontalLayout(acceptButton, rejectButton));
			cookieDialog.open();
		}
		
	}

}
