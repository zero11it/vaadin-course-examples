package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.layout.MyAppLayout;

@Route(value = "ordered", layout = MyAppLayout.class)
public class OrderedLayout extends VerticalLayout {
	public OrderedLayout() {
		add(new H1("Ordered Layout"));

		{
			add(new Span("Default"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			add(layout);
		}

		{
			add(new Span("No spacing"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			layout.setSpacing(false);
			add(layout);
		}

		{
			add(new Span("Padding"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			layout.setPadding(true);
			add(layout);
		}


		{
			add(new Span("Align items center"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			layout.setAlignItems(Alignment.CENTER);
			add(layout);
		}

		{
			add(new Span("Align items end"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			layout.setAlignItems(Alignment.END);
			add(layout);
		}

		{
			add(new Span("flexGrow"));
			HorizontalLayout layout = getNewExampleLayout();
			Div firstBox = createBox("10px");
			layout.add(firstBox);
			layout.setFlexGrow(1, firstBox);
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			
			add(layout);
		}

		{
			add(new Span("Default full width"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.setWidthFull();
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			add(layout);
		}
		
		{
			add(new Span("flexGrow"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.setWidthFull();
			Div firstBox = createBox("10px");
			layout.add(firstBox);
			layout.setFlexGrow(1, firstBox);
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			
			add(layout);
		}

		{
			add(new Span("Justify Space around"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.setWidthFull();
			layout.setSpacing(false);
			layout.setJustifyContentMode(JustifyContentMode.AROUND);
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			add(layout);
		}

		{
			add(new Span("Justify Space between"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.setWidthFull();
			layout.setSpacing(false);
			layout.setJustifyContentMode(JustifyContentMode.BETWEEN);
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			add(layout);
		}

		{
			add(new Span("Justify evenly"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.setWidthFull();
			layout.setSpacing(false);
			layout.setJustifyContentMode(JustifyContentMode.EVENLY);
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			add(layout);
		}

		{
			add(new Span("Justify center"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.setWidthFull();
			layout.setJustifyContentMode(JustifyContentMode.CENTER);
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			add(layout);
		}

		{
			add(new Span("Justify start"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.setWidthFull();
			layout.setJustifyContentMode(JustifyContentMode.START);
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			add(layout);
		}

		{
			add(new Span("Justify end"));
			HorizontalLayout layout = getNewExampleLayout();
			layout.setWidthFull();
			layout.setJustifyContentMode(JustifyContentMode.END);
			layout.add(createBox("10px"));
			layout.add(createBox("20px"));
			layout.add(createBox("10px"));
			add(layout);
		}
	}

	private HorizontalLayout getNewExampleLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.getElement().getStyle().set("border", "1px solid #ff3d00");
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
