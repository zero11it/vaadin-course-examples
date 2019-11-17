package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.WrapMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.layout.MyAppLayout;

@Route(value = "ordered-exercise")
public class OrderedLayoutExercise extends VerticalLayout {
	public OrderedLayoutExercise() {
		getElement().getStyle().set("backgroundColor", "#eeeeee");
		setWidthFull();
		setHeightFull();
		setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
		add(getNewExampleLayout(false));
		VerticalLayout scrollLayout = new VerticalLayout();
		scrollLayout.getElement().getStyle().set("overflow-y", "auto");
		scrollLayout.setPadding(false);
		setFlexGrow(1, scrollLayout);

		scrollLayout.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
		HorizontalLayout firstItem = getNewExampleLayout(false); 
		scrollLayout.add(firstItem);
		scrollLayout.setHorizontalComponentAlignment(Alignment.CENTER, firstItem);
		scrollLayout.add(getNewExampleLayout(true));
		scrollLayout.add(getNewExampleLayout(false));
		scrollLayout.add(getNewExampleLayout(true));
		scrollLayout.add(getNewExampleLayout(false));
		scrollLayout.add(getNewExampleLayout(true));
		scrollLayout.add(getNewExampleLayout(false));
		add(scrollLayout);
		FlexLayout footer = new FlexLayout();
		footer.setWrapMode(WrapMode.WRAP);
		footer.setJustifyContentMode(JustifyContentMode.EVENLY);
		footer.getElement().getStyle().set("backgroundColor", "#ffffff");
		footer.getElement().getStyle().set("border", "2px solid #888888");
		for (int i = 0; i < 30; i++) {
			footer.add(createBox("40px"));
		}
		add(footer);
	}

	private HorizontalLayout getNewExampleLayout(boolean withText) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(false);
		layout.setPadding(true);
		layout.getElement().getStyle().set("backgroundColor", "#ffffff");
		layout.getElement().getStyle().set("border", "2px solid #888888");
		Div firstBox = createBox("40px"); 
		layout.add(firstBox);
		if (withText) {
			firstBox.getStyle().set("flex-shrink", "0");
			Div secondBox = new Div(new Span("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
			layout.add(secondBox);
			layout.setFlexGrow(1, secondBox);
		}else {
			Div secondBox = createBox("10px");
			layout.add(secondBox);
			layout.setFlexGrow(1, secondBox);
		}
		return layout;
	}
	
	private Div createBox(String height) {
		Div div = new Div();
		div.setWidth("50px");
		div.setHeight(height);
		div.getElement().getStyle().set("background-image", "linear-gradient(45deg, #98CD8D 25%, #F6F0CF 25%, #F6F0CF 50%, #98CD8D 50%, #98CD8D 75%, #F6F0CF 75%, #F6F0CF 100%)");
		div.getElement().getStyle().set("background-size", "34px 34px");
		return div;
	}
}
