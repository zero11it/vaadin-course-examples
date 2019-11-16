package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(value = "")
@PageTitle("Index")
public class IndexView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public IndexView() {
        add(new RouterLink("TextField", TextFieldView.class));
        add(new RouterLink("TextField (events)", TextFieldEventsView.class));
        add(new RouterLink("Buttons", ButtonsView.class));
        add(new RouterLink("Grid", GridView.class));
        add(new RouterLink("Other", OtherView.class));
    }

}
