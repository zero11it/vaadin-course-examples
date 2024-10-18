package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.layout.AdminLayout;

@Route(value = "orders", layout = AdminLayout.class)
public class OrdersSearch extends Div  implements BeforeEnterObserver {
	final Span queryParamSpan;
	public OrdersSearch() {
		add(new H2("OrdersSearch"));
		queryParamSpan = new Span();
		add(queryParamSpan);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		queryParamSpan.setText("Query params: " + event.getLocation().getQueryParameters().getQueryString());
	}
}
