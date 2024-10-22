package it.zero11.vaadin.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import it.zero11.vaadin.course.model.Ruolo;
import it.zero11.vaadin.course.model.User;
import it.zero11.vaadin.course.service.UserService;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@PWA(name = "Project Base for Vaadin with Spring", shortName = "Vaadin Locale")
@Theme("my-theme")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        
        UserService userService = ctx.getBean(UserService.class);
        User user = userService.getByUsername("admin");
        if (user == null) {        
        	user = new User();
        	user.setUsername("admin");
        	user.setPassword("admin");
        	user.setRole(Ruolo.Admin);
        	user.setActive(true);
        	userService.save(user);
        };        
        
    }

}
