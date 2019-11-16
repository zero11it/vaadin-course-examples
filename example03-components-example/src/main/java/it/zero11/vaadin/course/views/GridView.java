package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "grid")
@PageTitle("Grid")
public class GridView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public GridView() {
		getElement().getStyle().set("padding", "1em");
		
		Grid<Book> grid = new Grid<>();
		grid.setWidthFull();
		grid.addColumn(Book::getAuthor).setHeader("Author");
		grid.addColumn(Book::getTitle).setHeader("Title").setSortable(true);
		grid.addColumn(new ComponentRenderer<Button, Book>(book -> {
			Button play = new Button("", VaadinIcon.PLAY_CIRCLE.create());
			play.addClickListener(e -> {
				Notification.show(book.getAuthor() + ", " + book.getTitle(), 5000, Position.MIDDLE);
			});
			
			return play;
		})).setWidth("80px").setFlexGrow(0);
		
		grid.setItems(new Book("J. R. R. Tolkien", "The Lord of the Rings"),
				new Book("J. K. Rowling","Harry Potter and the Philosopher's Stone"),
				new Book("Carlo Collodi","Le avventure di Pinocchio"),
				new Book("Dan Brown", "The Da Vinci Code"));
		
		add(grid);
    }
	
	public static class Book{
		private String author;
		private String title;
		
		public Book(String author, String title) {
			this.author = author;
			this.title = title;
		}
		
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
	}

}
