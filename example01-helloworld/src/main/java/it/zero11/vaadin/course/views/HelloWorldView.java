package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "")
@PageTitle("Hello")
public class HelloWorldView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public HelloWorldView() {
        add(new Label("Hello from Vaadin!"));
    }

}
