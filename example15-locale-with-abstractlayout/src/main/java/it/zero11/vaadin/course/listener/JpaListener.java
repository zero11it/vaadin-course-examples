package it.zero11.vaadin.course.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import it.zero11.vaadin.course.model.Ruolo;
import it.zero11.vaadin.course.model.User;
import it.zero11.vaadin.course.service.UserService;
import it.zero11.vaadin.course.utils.JPAUtils;

@WebListener
public class JpaListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		List<User> users = UserService.findAll();
		if (users.size() == 0) {
			User user = new User();
			user.setActive(true);
			user.setPassword("admin");
			user.setUsername("admin");
			user.setRole(Ruolo.Admin);
			UserService.save(user);
		}
			
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		JPAUtils.destroy();
	}

}
