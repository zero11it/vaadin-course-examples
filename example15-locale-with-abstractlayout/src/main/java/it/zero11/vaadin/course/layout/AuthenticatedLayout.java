package it.zero11.vaadin.course.layout;

import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

import it.zero11.vaadin.course.i18n.Corso18NProvider;
import it.zero11.vaadin.course.model.Ruolo;
import it.zero11.vaadin.course.model.User;
import it.zero11.vaadin.course.view.LoginView;
import it.zero11.vaadin.course.view.brand.BrandsView;
import it.zero11.vaadin.course.view.orders.OrdersView;
import it.zero11.vaadin.course.view.products.ProductsView;
import it.zero11.vaadin.course.view.users.UsersView;

@SuppressWarnings("serial")
public class AuthenticatedLayout extends AppLayout 
	implements RouterLayout, BeforeEnterObserver {
	
	private User user;
	private NativeLabel welcome;
	
	private Tab userTab;
	private Select<Locale> locales;
	
	public AuthenticatedLayout(Corso18NProvider it18n) {
		setPrimarySection(Section.NAVBAR);

		Image img = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
		img.setHeight("44px");

		
		locales = new Select<Locale>();
		locales.setItems(it18n.getProvidedLocales());
		locales.setValue(VaadinSession.getCurrent().getLocale());
		locales.addValueChangeListener(event -> {		
			VaadinSession.getCurrent().setLocale(locales.getValue());			
			UI.getCurrent().getPage().reload();
		});
		
		welcome = new NativeLabel();
		welcome.getElement().getStyle().set("width", "100%");
		
		Button logoutButton = new Button(VaadinIcon.EXIT.create());
		logoutButton.addClickListener(event -> {
			UI.getCurrent().getSession()
				.setAttribute(LoginView.USER_SESSION_ATTRIBUTE, null);
			UI.getCurrent().navigate(LoginView.class);
		});
		
		addToNavbar(new DrawerToggle(), img, welcome, locales, logoutButton);
		
		Tabs tabs = new Tabs(
				new Tab(new RouterLink(getTranslation("products.title"), ProductsView.class)),
				new Tab(new RouterLink(getTranslation("brands.title"), BrandsView.class)),
				new Tab(new RouterLink(getTranslation("orders.title"), OrdersView.class))				
		);
		userTab = new Tab(new RouterLink(getTranslation("users.title"), UsersView.class));
		userTab.setVisible(false);
		tabs.add(userTab);
		
		tabs.setOrientation(Tabs.Orientation.VERTICAL);
		addToDrawer(tabs);
	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		user = (User) UI.getCurrent().getSession()
				.getAttribute(LoginView.USER_SESSION_ATTRIBUTE);
		
		if (user == null) {
			event.forwardTo(LoginView.class);
		} else {
			welcome.setText(getTranslation("hello") + ", " + user.getUsername());
			userTab.setVisible(user.getRole().equals(Ruolo.Admin));				
		}
	}
}
