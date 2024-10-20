package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.layout.MyLayout;

@Route(value = "", layout = MyLayout.class)
@PageTitle("Ecommerce Exercise")
public class HomeView extends VerticalLayout{

	public HomeView() {
		add(new H3("Benvenuto"));
		Button login = new Button("Login", e -> {
			UI.getCurrent().navigate(LoginView.class);			
		});
		add(login);
	}

}
