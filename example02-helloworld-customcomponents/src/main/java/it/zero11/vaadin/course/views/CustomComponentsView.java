package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.components.GoogleChart;

@Route(value = "")
@PageTitle("Custom Components")
public class CustomComponentsView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public CustomComponentsView() {
		GoogleChart chart = new GoogleChart();
		chart.setData("[[\"a\", \"b\"], [1, 2], [2, 3]]");
        add(chart);
    }

}
