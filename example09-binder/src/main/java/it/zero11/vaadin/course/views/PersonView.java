package it.zero11.vaadin.course.views;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.model.Person;

@Route(value = "")
public class PersonView extends VerticalLayout {
	private Person person;

	public PersonView() {
		person = new Person();
		person.setFirstName("Pippo");
		person.setLastName("Franco");

		Binder<Person> binder = new Binder<>(Person.class);

		TextField firstName = new TextField("First Name");
		binder.forField(firstName).bind("firstName");
		add(firstName);

		TextField lastName = new TextField("Last Name");
		binder.forField(lastName).bind(Person::getLastName, Person::setLastName);
		add(lastName);

		TextField title = new TextField("Title");
		binder.bind(title, Person::getTitle, Person::setTitle);
		add(title);

		TextField email = new TextField("Email");
		binder.forField(email).withValidator(new EmailValidator("Email non valida"))
			.bind(Person::getEmail, Person::setEmail);
		add(email);

		TextField zip = new TextField("Zip");
		
		binder.forField(zip)
			.withValidator(new StringLengthValidator("Must be 5 character", 5, 5))
			.withValidator((v) -> StringUtils.isNumeric(v), "Must be numeric")
			.bind(Person::getZipCode, Person::setZipCode);
		add(zip);
		
		TextField yearOfBirth = new TextField("Year of birth");
		binder.forField(yearOfBirth)
			.withNullRepresentation("")
			.withValidator(new StringLengthValidator("Must be 4 digit", 4, 4))
			.withConverter(new StringToIntegerConverter("Invalid number"))
			.withValidator((v)-> v >= 1900 && v <= Calendar.getInstance().get(Calendar.YEAR), "Invalid birth date")
			.bind(Person::getYearOfBirth, Person::setYearOfBirth);
		add(yearOfBirth);
		
		binder.setBean(person);

		add(new Button("Reset", (e) -> binder.readBean(person)));
		add(new Button("Save", (e) -> {
			if (binder.validate().isOk()) {
				Notification.show(person.getTitle() + " " + person.getFirstName() + " " + person.getLastName());
			}else {
				Notification.show("Invalid fields");	
			}
		}));
	}
}
