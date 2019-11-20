package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("ordered-exercise-simple")
public class OrderedLayoutExerciseSimple extends VerticalLayout{
	public OrderedLayoutExerciseSimple() {
		setSizeFull();
		add(new H1("Title"));
		VerticalLayout content = new VerticalLayout();
		content.getStyle().set("overflow-y", "auto");
		for (int i = 1; i <= 100; i++) {
			content.add(new Paragraph("Prova " + i));
		}
		add(content);
		add(new H2("Footer"));
	}
}
