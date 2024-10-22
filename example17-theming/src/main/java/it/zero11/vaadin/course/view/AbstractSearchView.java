package it.zero11.vaadin.course.view;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;

@SuppressWarnings("serial")
public abstract class AbstractSearchView<T> extends VerticalLayout implements HasDynamicTitle {

	protected Grid<T> grid;
	
	public AbstractSearchView() {
		
	}
	
	protected void render() {
		setSizeFull();
		
		H1 title = new H1(getTitle());
		
		HorizontalLayout actionLayout = new HorizontalLayout();
		actionLayout.setWidthFull();
		actionLayout.setDefaultVerticalComponentAlignment(
				Alignment.CENTER);
		actionLayout.add(title);
		actionLayout.setFlexGrow(1, title);
		
		addActions(actionLayout);
		
		HorizontalLayout filterLayout = new HorizontalLayout();
		filterLayout.setWidthFull();
		filterLayout.setDefaultVerticalComponentAlignment(Alignment.END);
		filterLayout.setJustifyContentMode(JustifyContentMode.END);
		addFilters(filterLayout);
		
		grid = createGrid();
		grid.setSizeFull();
		
		add(actionLayout, filterLayout, grid);
	}

	abstract protected String getTitle();
	
	abstract protected void addFilters(HasComponents container);
	
	abstract protected Grid<T> createGrid();
	
	abstract protected void addActions(HasComponents container);
	
	@Override
	public String getPageTitle() {
		return getTitle();
	}
}
