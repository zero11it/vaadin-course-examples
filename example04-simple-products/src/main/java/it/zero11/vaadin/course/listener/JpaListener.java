package it.zero11.vaadin.course.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import it.zero11.vaadin.course.utils.JPAUtils;

@WebListener
public class JpaListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		JPAUtils.destroy();
	}

}
