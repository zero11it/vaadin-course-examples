package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "notification")
@PageTitle("Notifications")
public class NotificationView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public NotificationView() {
		Button errorNotificationButton = new Button("Mostra Notification", e -> {
			Notification.show("Messaggio di notifica");			
		});
		add(errorNotificationButton);
		
		Button notificationButton = new Button("Mostra Notification di errore", e -> {
			Notification notification = Notification.show("Rilevato errore nell'applicazione", 5000, Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		});
		add(notificationButton);
		
		Button dialogButton = new Button("Mostra Dialog", VaadinIcon.DESKTOP.create(), e -> {
			showDialog();
		});
		add(dialogButton);
	}
	
	private void showDialog() {
		Dialog dialog = new Dialog();

		dialog.setHeaderTitle("Dialog di test");

		dialog.add(new Div("Linea1"));
		dialog.add(new Div("Linea2"));
		
		Button cancelButton = new Button("Esci", e -> dialog.close());
		dialog.getFooter().add(cancelButton);
		
		dialog.open();
	}
	    
}
