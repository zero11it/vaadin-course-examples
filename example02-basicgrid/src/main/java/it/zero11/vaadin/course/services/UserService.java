package it.zero11.vaadin.course.services;

import java.util.Arrays;
import java.util.List;

import it.zero11.vaadin.course.model.User;

public class UserService {
	public List<User> getAllUsers(){
		return Arrays.asList(
				new User("Pippo", "pippo@disney.com"),
				new User("Pluto", "pluto@disney.com"),
				new User("Paperino", "paperino@disney.com")
			);
	}
}
