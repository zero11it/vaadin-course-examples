package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "hello")
@PageTitle("Hello")
public class HelloWorldWithImageView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public HelloWorldWithImageView() {
        add(new Label("Hello from Zero11 & Vaadin!"));
        
        Image logoZero11 = new Image("icons/logo-zero11.svg", "Zero11");
        logoZero11.setWidth("200px");
        add(logoZero11);
        
        Image logoVaadin = new Image("icons/logo-vaadin.svg", "Vaadin");
        logoVaadin.setWidth("200px");
        add(logoVaadin);
        
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

}
