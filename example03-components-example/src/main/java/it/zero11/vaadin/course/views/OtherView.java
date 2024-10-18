package it.zero11.vaadin.course.views;

import java.util.concurrent.TimeUnit;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "other")
@PageTitle("Other")
public class OtherView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public OtherView() {
		add(new Checkbox("checkBox", true));
		
		RadioButtonGroup<TimeUnit> radioButtonGroup = new RadioButtonGroup<>();
		radioButtonGroup.setLabel("Radio");
		radioButtonGroup.setItems(TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES);
		add(radioButtonGroup);
		
		Select<TimeUnit> select = new Select<>();
		select.setItems(TimeUnit.values());
		select.setLabel("Select");
		add(select);
		
		ListBox<TimeUnit> listBox = new ListBox<>();
		listBox.setItems(TimeUnit.values());
		add(listBox);
		
		ComboBox<TimeUnit> combobox = new ComboBox<>("ComboBox", TimeUnit.values());
		add(combobox);
	}
}
