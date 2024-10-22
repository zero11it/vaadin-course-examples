package it.zero11.vaadin.course.i18n;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.vaadin.flow.i18n.I18NProvider;

@Component
public class Corso18NProvider implements I18NProvider {

	@Override
	public List<Locale> getProvidedLocales() {
		return Arrays.asList(
				Locale.ITALIAN,
				Locale.ENGLISH
		);
	}

	@Override
	public String getTranslation(String key, Locale locale, 
			Object... params) {
				
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("Corso", locale);
			
			if (params.length == 0) {
				String translation = bundle.getString(key);
				return translation;
			} else {
				return MessageFormat.format(bundle.getString(key), params);
			}
		} catch (Exception e) {
			return "No translation";
		}			 	
	}

}
