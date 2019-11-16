package it.zero11.vaadin.course.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.view.Home;
import it.zero11.vaadin.course.view.MyFormLayout;
import it.zero11.vaadin.course.view.OrderedLayout;

public class MyAppLayout extends AppLayout {
	public MyAppLayout() {
		setPrimarySection(Section.NAVBAR);

		Image img = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
		img.setHeight("44px");
		addToNavbar(new DrawerToggle(), img);
		Tabs tabs = new Tabs(new Tab(new RouterLink("Home", Home.class)),
				new Tab(new RouterLink("Form Layout", MyFormLayout.class)),
						new Tab(new RouterLink("Ordered Layout (Flexbox)", OrderedLayout.class)));
		tabs.setOrientation(Tabs.Orientation.VERTICAL);
		addToDrawer(tabs);

	}
}
