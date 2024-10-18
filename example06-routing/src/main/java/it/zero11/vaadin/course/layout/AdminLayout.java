package it.zero11.vaadin.course.layout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.views.OrdersSearch;
import it.zero11.vaadin.course.views.ProductsSearch;

@RoutePrefix("admin")
public class AdminLayout extends Div implements RouterLayout {
	private static final long serialVersionUID = 1L;

	public AdminLayout() {
		add(new H4(new RouterLink("Products search", ProductsSearch.class)));
		
		
		add(new Button("Orders search", (e)-> {
			UI.getCurrent().navigate(OrdersSearch.class);
		}));
	}
}
