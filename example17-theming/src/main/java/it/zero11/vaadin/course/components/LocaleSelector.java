package it.zero11.vaadin.course.components;

import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.material.Material;

import it.zero11.vaadin.course.utils.TranslationProvider;

public class LocaleSelector extends RadioButtonGroup<Locale> {
	
	public LocaleSelector(String height) {
		setItems(new TranslationProvider().getProvidedLocales());
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
	
	public LocaleSelector() {
		this("25px");
	}
	
}
