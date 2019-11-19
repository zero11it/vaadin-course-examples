package it.zero11.vaadin.course.layout;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.view.BrandsView;
import it.zero11.vaadin.course.view.ProductsView;

public class MyLayout extends VerticalLayout implements RouterLayout {

	public MyLayout() {
		add(new HorizontalLayout(new RouterLink("Products", ProductsView.class),
				new RouterLink("Brands", BrandsView.class)));
	}
}
