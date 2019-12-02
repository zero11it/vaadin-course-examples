package it.zero11.vaadin.course.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DebounceSettings;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.dom.DebouncePhase;

@DomEvent(value = "high-value-changed",
	debounce = @DebounceSettings(
	    timeout = 250,
	    phases = DebouncePhase.TRAILING))
public class HighValueChangeEvent extends ComponentEvent<RangeSlider> {

	public HighValueChangeEvent(RangeSlider source, boolean fromClient) {
		super(source, fromClient);		
	}

}
