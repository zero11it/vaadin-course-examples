package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "textfield")
@PageTitle("TextField")
public class TextFieldView extends FormLayout {
	private static final long serialVersionUID = 1L;

	public TextFieldView() {
		getElement().getStyle().set("padding", "1em");
		
		TextField textField = new TextField();
        textField.setLabel("Text field label");
        textField.setPlaceholder("placeholder text");
        add(textField);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password field");
        passwordField.setPlaceholder("password field");
        add(passwordField);

        TextField textFieldWithClearButton = new TextField();
        textFieldWithClearButton.setLabel("Text field label");
        textFieldWithClearButton.setPlaceholder("with clear button");
        textFieldWithClearButton.setClearButtonVisible(true);
        add(textFieldWithClearButton);

        TextField textFieldReadOnly = new TextField();
        textFieldReadOnly.setLabel("Text field label");
        textFieldReadOnly.setPlaceholder("read only");
        textFieldReadOnly.setValue("read only");
        textFieldReadOnly.setReadOnly(true);
        add(textFieldReadOnly);

        TextField textFieldRequired = new TextField();
        textFieldRequired.setLabel("Text field label");
        textFieldRequired.setPlaceholder("required");
        textFieldRequired.setRequired(true);
        textFieldRequired.setErrorMessage("This field is required");
        textFieldRequired.setRequiredIndicatorVisible(true);
        add(textFieldRequired);

		TextArea textArea = new TextArea();
		textArea.setLabel("Text area label");
		textArea.setPlaceholder("placeholder textarea");
        add(textArea);
        
        setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("40em", 2),
                new ResponsiveStep("60em", 3));
    }

}
