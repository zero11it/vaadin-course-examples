package it.zero11.vaadin.course.components;

import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.VaadinSession;

import it.zero11.vaadin.course.i18n.Corso18NProvider;

public class LocaleSelector extends Select<Locale> {
	
	public LocaleSelector(String height, Corso18NProvider i18n) {
		setItems(i18n.getProvidedLocales());
		setRenderer(new ComponentRenderer<Image, Locale>(
				locale -> {
					Image img = new Image(
							locale.equals(Locale.ITALIAN) ?
									"https://cdn.shopify.com/s/files/1/1166/0602/products/italy-flag_1024x1024.jpeg?v=1457043237" :
									"https://upload.wikimedia.org/wikipedia/en/thumb/a/a4/Flag_of_the_United_States.svg/1280px-Flag_of_the_United_States.svg.png"	
							, locale.toString());
					img.setHeight(height);
					
					return img;
				}));
		setValue(VaadinSession.getCurrent().getLocale());
		addValueChangeListener(event -> {				
			VaadinSession.getCurrent().setLocale(getValue());			
			UI.getCurrent().getPage().reload();
		});
	}
	
	public LocaleSelector(Corso18NProvider i18n) {
		this("25px", i18n);
	}
	
}
