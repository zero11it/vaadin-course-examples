package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.layout.MyAppLayout;

@Route(value = "formlayout", layout = MyAppLayout.class)
public class MyFormLayout extends VerticalLayout {
	public MyFormLayout() {
		add(new H1("Form Layout"));

		FormLayout columnLayout = new FormLayout();
		// Setting the desired responsive steps for the columns in the layout
		columnLayout.setResponsiveSteps(new ResponsiveStep("25em", 1), new ResponsiveStep("32em", 2),
				new ResponsiveStep("40em", 3));
		TextField firstName = new TextField();
		firstName.setLabel("First Name");
		TextField lastName = new TextField();
		lastName.setLabel("Last Name");
		TextField email = new TextField();
		email.setLabel("Email");
		TextField nickname = new TextField();
		nickname.setLabel("Username");
		TextField website = new TextField();
		website.setLabel("Link to personal website");
		TextField description = new TextField();
		description.setLabel("Enter a short description about yourself");
		columnLayout.add(firstName, lastName, nickname, email, website);
		// You can set the desired column span for the components individually.
		columnLayout.setColspan(website, 2);
		// Or just set it as you add them.
		columnLayout.add(description, 3);
		
		add(columnLayout);
	}
}
