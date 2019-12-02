package it.zero11.vaadin.course.utils;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.text.NumberFormatter;

public class LanguageTest {

	public static void main(String[] args) {
		Locale locale = Locale.ITALY;
		
		ResourceBundle bundle = ResourceBundle.getBundle("Corso", locale);
		
		System.out.println(bundle.getString("hello") + ", Michele");
		
		String msg = MessageFormat.format(bundle.getString("credit"), 50);
				
		System.out.println(msg);
		
		DateTimeFormatter df = DateTimeFormatter
				.ofLocalizedDate(FormatStyle.MEDIUM)
				.withLocale(locale);
		
		System.out.println(LocalDate.now().format(df));
		
		String n =NumberFormat.getCurrencyInstance(locale).format(1223323.23);
		System.out.println(n);
		
		
	}

}
