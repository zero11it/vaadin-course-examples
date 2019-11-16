package it.zero11.vaadin.cource.views;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.RouterLink;

public class UnexistingRoute extends Div implements HasErrorParameter<NotFoundException> {
	private static final long serialVersionUID = 1L;
	private Paragraph message;
	
	public UnexistingRoute() {
		message = new Paragraph();
		add(message);
		add(new RouterLink("Return to home page", Home.class));
		
	}
	
	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
		message.setText("Could not navigate to '"
                + event.getLocation().getPath()
                + "'");
		return HttpServletResponse.SC_NOT_FOUND;
	}

}
