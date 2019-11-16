package it.zero11.vaadin.cource.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
public class Home extends Div {
	public Home() {
		add(new H1("Home page"));
		add(new RouterLink("go to Products", ProductsSearch.class));
	}
}
