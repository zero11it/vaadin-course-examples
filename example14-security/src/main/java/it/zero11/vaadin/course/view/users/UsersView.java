package it.zero11.vaadin.course.view.users;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.layout.AuthenticatedLayout;
import it.zero11.vaadin.course.model.Ruolo;
import it.zero11.vaadin.course.model.User;
import it.zero11.vaadin.course.service.UserService;
import it.zero11.vaadin.course.view.LoginView;

@Route(value = "users", layout = AuthenticatedLayout.class)
@PageTitle("Users")
public class UsersView extends VerticalLayout 
	implements BeforeEnterObserver {
	private static final long serialVersionUID = 1L;
	private final Grid<User> userGrid;
	
	private ListDataProvider<User> dataProvider;
	
	public UsersView() {
		add(new H4("Users"));
		
		userGrid = new Grid<>();
		userGrid.setHeight("300px");
		Column<User> idCol = userGrid.addColumn(User::getId)
			.setHeader("Id")
			.setFlexGrow(0)
			.setWidth("50px");
		Column<User> nameCol = userGrid.addColumn(User::getUsername).setHeader("Username")
			.setFlexGrow(1)
			.setSortable(true);
		Column<User> roleCol = userGrid.addColumn(User::getRole).setHeader("Role")
				.setFlexGrow(0)
				.setSortable(true);
		Column<User> activeCol = userGrid.addColumn(User::isActive).setHeader("Active")
				.setFlexGrow(0)
				.setSortable(true);
		
		userGrid.addColumn(new ComponentRenderer<Component, User>(user -> {
			Span container = new Span();
			
			Button edit = new Button("", VaadinIcon.EDIT.create());
			edit.addClickListener(event -> {
				UI.getCurrent().navigate(UserEditView.class, user.getId());
			});
			
			Button delete = new Button("", VaadinIcon.TRASH.create());
			delete.addClickListener(event -> {
				try {
					UserService.remove(user);

					updateUserData();
				}catch(Exception exception) {
					Notification.show("An error occurred while deleting the user");
				}
			});

			container.add(edit, delete);
			return container;
		}))
		.setFlexGrow(0)
		.setWidth("80px")
		.setHeader("Azioni");

		add(userGrid);
		
		add(new RouterLink("Nuovo User", UserEditView.class));
		
		updateUserData();
	}

	private void updateUserData() {
		List<User> users = UserService.findAll();
		userGrid.setItems(users);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		User user = (User) UI.getCurrent().getSession()
				.getAttribute(LoginView.USER_SESSION_ATTRIBUTE);
		
		if (user != null && !user.getRole().equals(Ruolo.Admin)) {
			event.rerouteTo("");
		}		
	}

}
