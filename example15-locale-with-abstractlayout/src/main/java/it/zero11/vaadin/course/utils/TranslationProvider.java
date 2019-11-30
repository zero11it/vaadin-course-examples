package it.zero11.vaadin.course.utils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.vaadin.flow.i18n.I18NProvider;

public class TranslationProvider implements I18NProvider {

	private Map<String, String> cache = new HashMap<>();
	
	@Override
	public List<Locale> getProvidedLocales() {
		return Arrays.asList(
				Locale.ITALY,
				Locale.US
		);
	}

	@Override
	public String getTranslation(String key, Locale locale, 
			Object... params) {
		
		if (params.length == 0 && cache.containsKey(key))
			return cache.get(key);
		
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("Corso");
			
			if (params.length == 0) {
				String translation = bundle.getString(key);
				cache.put(key, translation);
				return translation;
			} else {
				return MessageFormat.format(bundle.getString(key), params);
			}
		} catch (Exception e) {
			return "No translation";
		}			 	
	}

}
