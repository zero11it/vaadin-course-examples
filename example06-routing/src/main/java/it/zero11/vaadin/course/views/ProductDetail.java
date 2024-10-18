package it.zero11.vaadin.course.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import it.zero11.vaadin.course.layout.AdminLayout;

@Route(value = "products", layout = AdminLayout.class)
public class ProductDetail extends Div
implements HasUrlParameter<String>,
	HasDynamicTitle,
	BeforeLeaveObserver{
	
	private H4 productTitle;
	
	public ProductDetail() {
		add(new H2("ProductSearch"));
		productTitle = new H4();
		add(productTitle);
	}

	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		if (!parameter.startsWith("existing")) {
			event.rerouteTo(ProductNotFound.class);
		}else {
			productTitle.setText("Product " + parameter);
		}
	}

	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		if (hasChanges()) {
			ContinueNavigationAction action = event.postpone();
			add(new Button("Leave without saving ?", (e)-> action.proceed()));
		}
	}

	private boolean hasChanges() {
		return true;
	}

	@Override
	public String getPageTitle() {
		return productTitle.getText();
	}
}
