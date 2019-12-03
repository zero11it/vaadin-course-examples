package it.zero11.vaadin.course.layout;

import java.util.Locale;

import javax.servlet.http.Cookie;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.material.Material;

import it.zero11.vaadin.course.components.LocaleSelector;
import it.zero11.vaadin.course.model.Ruolo;
import it.zero11.vaadin.course.model.User;
import it.zero11.vaadin.course.service.UserService;
import it.zero11.vaadin.course.utils.TranslationProvider;
import it.zero11.vaadin.course.view.LoginView;
import it.zero11.vaadin.course.view.brand.BrandsView;
import it.zero11.vaadin.course.view.orders.OrdersView;
import it.zero11.vaadin.course.view.products.ProductsView;
import it.zero11.vaadin.course.view.users.UsersView;

@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport(value = "./styles/shared-styles.css")
@CssImport(value = "./styles/my-app-layout-style.css", themeFor = "vaadin-app-layout")
@CssImport(value = "./styles/my-tab-style.css", themeFor = "vaadin-tab")
@SuppressWarnings("serial")
public class AuthenticatedLayout extends AppLayout 
	implements RouterLayout, BeforeEnterObserver {
	
	private User user;
	private Label welcome;
	
	private Tab userTab;
	
	public AuthenticatedLayout() {
		setPrimarySection(Section.NAVBAR);

		Image img = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
		img.setHeight("44px");
		
		welcome = new Label();
		welcome.getElement().getStyle().set("width", "100%");
		
		Button logoutButton = new Button(VaadinIcon.EXIT.create());
		logoutButton.addClickListener(event -> {
			UI.getCurrent().getSession()
				.setAttribute(LoginView.USER_SESSION_ATTRIBUTE, null);
			Cookie token = new Cookie("token", "");
			token.setMaxAge(0);
			VaadinServletResponse.getCurrent().addCookie(token);
			
			UI.getCurrent().navigate(LoginView.class);
		});
		
		LocaleSelector selector = new LocaleSelector("10px");
		addToNavbar(new DrawerToggle(), img, welcome, selector, logoutButton);
		
		Tabs tabs = new Tabs(
				createTab("Products", ProductsView.class),
				createTab("Brands", BrandsView.class),
				createTab("Orders", OrdersView.class)				
		);
		userTab = createTab("Users", UsersView.class);
		userTab.setVisible(false);
		tabs.add(userTab);
		
		tabs.setOrientation(Tabs.Orientation.VERTICAL);
		addToDrawer(tabs);
	}
	
	private Tab createTab(String text, Class<? extends Component> view) {
		Tab tab = new Tab(new RouterLink(text, view));
		tab.addClassName("menu");
		return tab;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		user = (User) UI.getCurrent().getSession()
				.getAttribute(LoginView.USER_SESSION_ATTRIBUTE);
		
		if (user == null) {
			String token = null;
			Cookie[] cookies = VaadinServletRequest.getCurrent().getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("token")) {
						token = cookie.getValue();
					}
				}
			} 
			if (token != null) {
				User user = UserService.findByToken(token);
				if (user != null) {
					UI.getCurrent().getSession()
					.setAttribute(LoginView.USER_SESSION_ATTRIBUTE, user);
					welcome.setText(getTranslation("hello") + ", " + user.getUsername());
					userTab.setVisible(user.getRole().equals(Ruolo.Admin));
				} else {
					event.forwardTo(LoginView.class);		
				}
			} else {
				event.forwardTo(LoginView.class);
			}
		} else {
			welcome.setText(getTranslation("hello") + ", " + user.getUsername());
			userTab.setVisible(user.getRole().equals(Ruolo.Admin));				
		}
	}
}
