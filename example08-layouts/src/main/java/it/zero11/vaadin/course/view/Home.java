package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.layout.MyAppLayout;

@Route(value = "", layout = MyAppLayout.class)
public class Home extends Div {
	public Home() {
		add(new H1("Home Page"));
	}
}
