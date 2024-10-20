package it.zero11.vaadin.course.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.view.brand.BrandsView;
import it.zero11.vaadin.course.view.orders.OrdersView;
import it.zero11.vaadin.course.view.products.ProductsView;
import it.zero11.vaadin.course.view.users.UsersView;

public class MyLayout extends AppLayout implements RouterLayout {

	public MyLayout() {
		setPrimarySection(Section.NAVBAR);

		Image img = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
		img.setHeight("44px");
		addToNavbar(new DrawerToggle(), img);
		Tabs tabs = new Tabs(
				new Tab(new RouterLink("Products", ProductsView.class)),
				new Tab(new RouterLink("Brands", BrandsView.class)),
				new Tab(new RouterLink("Orders", OrdersView.class)),
				new Tab(new RouterLink("Users", UsersView.class))
		);
		tabs.setOrientation(Tabs.Orientation.VERTICAL);
		addToDrawer(tabs);
	}
}
