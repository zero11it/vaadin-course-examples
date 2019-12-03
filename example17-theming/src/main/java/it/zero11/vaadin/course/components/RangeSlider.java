package it.zero11.vaadin.course.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;

@Tag("paperwave-range-slider")
@NpmPackage(value = "paperwave-range-slider", version = "1.0.6")
@JsModule("paperwave-range-slider/paperwave-range-slider.js")
public class RangeSlider extends Component {

	public void setPin(Boolean value) {
		getElement().setProperty("pin", value);
	}
	
	public Boolean getPin() {
		return Boolean.valueOf(getElement().getProperty("pin"));
	}
	
	public void setMin(double min) {
		getElement().setProperty("min", min);
	}
	
	public double getMin() {
		return Double.parseDouble(getElement().getProperty("min"));
	}
	
	public void setMax(double max) {
		getElement().setProperty("max", max);
	}
	
	public double getMax() {
		return Double.parseDouble(getElement().getProperty("max"));
	}
		
	public void setLowValue(double value) {
		getElement().setProperty("lowValue", value);
	}
	
	@Synchronize("low-value-changed")
	public double getLowValue() {
		return Double.parseDouble(getElement().getProperty("lowValue"));
	}
	
	public void setHighValue(double value) {
		getElement().setProperty("highValue", value);
	}
	
	@Synchronize("high-value-changed")
	public double getHighValue() {
		return Double.parseDouble(getElement().getProperty("highValue"));
	}
	
	public void setMinInterval(double value) {
		getElement().setProperty("minInterval", value);
	}
	
	public double getMinInterval() {
		return Double.parseDouble(getElement().getProperty("minInterval"));
	}
	
	public void setLowHighValue(double min, double max) {
		getElement().setProperty("lowValue", min);
		getElement().setProperty("highValue", max);
	}
	
	public Registration addLowValueChangeListener(ComponentEventListener<LowValueChangeEvent> listener) {
		return addListener(LowValueChangeEvent.class, listener);
	}
	
	public Registration addHighValueChangeListener(ComponentEventListener<HighValueChangeEvent> listener) {
		return addListener(HighValueChangeEvent.class, listener);
	}
}
