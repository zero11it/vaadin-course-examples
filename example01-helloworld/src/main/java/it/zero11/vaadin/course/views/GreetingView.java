package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "greeting")
@PageTitle("Greeting")
public class GreetingView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public GreetingView() {
		TextField nameTextField = new TextField("Name");
        add(nameTextField);
        
		Button greetButton = new Button("Greet", (event)->{
			add(new Span("Hello,  " + nameTextField.getValue()));
		});
        add(greetButton);
    }
}
