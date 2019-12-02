package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("serial")
public abstract class AbstractSearchView<T> extends VerticalLayout {

	protected Grid<T> grid;
	
	public AbstractSearchView() {
		setSizeFull();
		
		H1 title = new H1(getTitle());
		title.getElement().getStyle().set("color", "red");
		title.setWidthFull();
		
		FlexLayout middleLayout = new FlexLayout();
		middleLayout.setSizeFull();
		
		VerticalLayout filterLayout = new VerticalLayout();
		filterLayout.setWidthFull();
		filterLayout.setDefaultHorizontalComponentAlignment(
				Alignment.STRETCH);
		filterLayout.setWidth("150px");
		addFilters(filterLayout);
		
		grid = createGrid();
		grid.setSizeFull();
		
		middleLayout.add(grid, filterLayout);
		middleLayout.setFlexGrow(1.0, grid);
		
		
		HorizontalLayout actionLayout = new HorizontalLayout();
		actionLayout.setWidthFull();
		actionLayout.setDefaultVerticalComponentAlignment(
				Alignment.BASELINE);
		addActions(actionLayout);
		
		add(title, actionLayout, middleLayout);
	}

	abstract protected String getTitle();
	
	abstract protected void addFilters(HasComponents container);
	
	abstract protected Grid<T> createGrid();
	
	abstract protected void addActions(HasComponents container);
}
