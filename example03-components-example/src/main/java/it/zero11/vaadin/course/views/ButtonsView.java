package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "buttons")
@PageTitle("Buttons")
public class ButtonsView extends HorizontalLayout {
	private static final long serialVersionUID = 1L;

	public ButtonsView() {
		Button button = new Button();
		button.setText("Disable on click");
		button.setDisableOnClick(true);
		button.addClickListener(e -> {button.setEnabled(true);});
		add(button);

		Button button2 = new Button();
		button2.setText("With icon");
		button2.setIcon(VaadinIcon.AIRPLANE.create());
		add(button2);
		
		Button button3 = new Button();
		button3.setText("With them variants");
		button3.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
		add(button3);
	}
}
