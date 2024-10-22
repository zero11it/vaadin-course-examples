package it.zero11.vaadin.course.layout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.components.LocaleSelector;
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
	private Label welcome;
	
	private Tab userTab;
	
	public AuthenticatedLayout(Corso18NProvider i18n) {
		setPrimarySection(Section.NAVBAR);

		Image img = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
		img.setHeight("44px");
		
		welcome = new Label();
		welcome.getElement().getStyle().set("width", "100%");
		
		Button logoutButton = new Button(VaadinIcon.EXIT.create());
		logoutButton.addClickListener(event -> {
			UI.getCurrent().getSession()
				.setAttribute(LoginView.USER_SESSION_ATTRIBUTE, null);
			UI.getCurrent().navigate(LoginView.class);
		});
		
		LocaleSelector selector = new LocaleSelector("10px", i18n);
		addToNavbar(new DrawerToggle(), img, welcome, selector, logoutButton);
		
		Tabs tabs = new Tabs(
				new Tab(new RouterLink("Products", ProductsView.class)),
				new Tab(new RouterLink("Brands", BrandsView.class)),
				new Tab(new RouterLink("Orders", OrdersView.class))				
		);
		userTab = new Tab(new RouterLink("Users", UsersView.class));
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
