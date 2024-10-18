package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.layout.AdminLayout;

@Route(value = "products", layout = AdminLayout.class)
public class ProductsSearch extends Div {
	public ProductsSearch() {
		add(new H2("ProductSearch"));
		add(new H3(new RouterLink("Existing one", ProductDetail.class, "existing-one")));
		add(new H3(new RouterLink("Existing two", ProductDetail.class, "existing-two")));
		add(new H3(new RouterLink("NotExisting", ProductDetail.class, "notexisting")));
	}
}
