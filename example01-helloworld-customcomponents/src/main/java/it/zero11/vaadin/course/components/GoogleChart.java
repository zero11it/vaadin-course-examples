package it.zero11.vaadin.course.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@Tag("google-chart")
@NpmPackage(value = "@google-web-components/google-chart", version = "3.3.0")
@JsModule("@google-web-components/google-chart/google-chart.js")
public class GoogleChart extends Component {
	private static final long serialVersionUID = 1L;

	public void setData(String data) {
		getElement().setAttribute("data", data);
	}
	
	public void getData() {
		getElement().getAttribute("data");
	}
}
