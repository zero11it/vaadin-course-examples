package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "textfieldevents")
@PageTitle("TextField")
public class TextFieldEventsView extends FormLayout {
	private static final long serialVersionUID = 1L;

	public TextFieldEventsView() {
		getElement().getStyle().set("padding", "1em");
		
		TextField textField = new TextField();
        textField.setLabel("Value");
        add(textField);
        
		TextArea logTextArea = new TextArea();
		logTextArea.setLabel("Log");
		logTextArea.setReadOnly(true);
		logTextArea.setHeight("100px");
        add(logTextArea);
        
        textField.addValueChangeListener((e)->{
        	logTextArea.setValue(textField.getValue() + "\n" + logTextArea.getValue());
        });
        
        RadioButtonGroup<ValueChangeMode> valueChangeModes = new RadioButtonGroup<>();
        valueChangeModes.setItems(ValueChangeMode.values());
        valueChangeModes.addValueChangeListener((e) -> textField.setValueChangeMode(e.getValue()));
        valueChangeModes.setValue(textField.getValueChangeMode());
        add(valueChangeModes);
        
        setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("40em", 2),
                new ResponsiveStep("60em", 3));
    }

}
