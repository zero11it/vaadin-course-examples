package it.zero11.vaadin.course.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageTest {

	public static void main(String[] args) {
		Locale locale = Locale.ITALY;
		
		ResourceBundle bundle = ResourceBundle.getBundle("Corso", locale);
		
		System.out.println(bundle.getString("hello") + ", Michele");
		
		String msg = MessageFormat.format(bundle.getString("credit"), 50);
				
		System.out.println(msg);
		
	}

}
