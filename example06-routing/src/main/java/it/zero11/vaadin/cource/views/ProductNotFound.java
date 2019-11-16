package it.zero11.vaadin.cource.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.layout.AdminLayout;

@Route(value = "product/notfound", layout = AdminLayout.class)
public class ProductNotFound extends Div{
	public ProductNotFound() {
		add(new H2("Product Not Found"));
	}
}
