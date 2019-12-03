package it.zero11.vaadin.course.servlets;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.vaadin.flow.server.Constants;
import com.vaadin.flow.server.VaadinServlet;

@WebServlet(
		urlPatterns = "/*", 
		asyncSupported = true,
		initParams = @WebInitParam(
				name = Constants.I18N_PROVIDER,
				value = "it.zero11.vaadin.course.utils.TranslationProvider"
		)
)
public class ApplicationServlet extends VaadinServlet {
}
