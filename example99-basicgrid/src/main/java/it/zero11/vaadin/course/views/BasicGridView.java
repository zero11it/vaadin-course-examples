package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.model.User;
import it.zero11.vaadin.course.services.UserService;

@Route("")
@PageTitle("Basic Grid View")
public class BasicGridView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public BasicGridView() {
		Grid<User> userGrid = new Grid<>();
		userGrid.setItems(new UserService().getAllUsers());
		userGrid.addColumn(User::getEmail).setHeader("Email");
		userGrid.addColumn(User::getName).setHeader("Name");
		add(userGrid);
	}
}
