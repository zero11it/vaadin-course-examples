package it.zero11.vaadin.course.view;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.layout.MyLayout;
import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.service.BrandService;

@Route(value = "brands", layout = MyLayout.class)
@PageTitle("Brands")
public class BrandsView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final Grid<Brand> brandGrid;

	public BrandsView() {
		add(new H1("Brands"));
		
		brandGrid = new Grid<>();
		brandGrid.setHeight("300px");
		brandGrid.addColumn(Brand::getId).setHeader("Id");
		brandGrid.addColumn(Brand::getName).setHeader("Name").setSortable(true);
		brandGrid.addColumn(new ComponentRenderer<Component, Brand>(brand -> {
			Span container = new Span();
			
			Button edit = new Button("", VaadinIcon.EDIT.create());
			edit.addClickListener(event -> {
				UI.getCurrent().navigate(BrandsEditView.class, brand.getId());
			});
			
			Button delete = new Button("", VaadinIcon.TRASH.create());
			delete.addClickListener(event -> {
				try {
					BrandService.remove(brand);

					updateBrandData();
				}catch(Exception exception) {
					Notification.show("An error occurred while deleting the brand");
				}
			});

			container.add(edit, delete);
			return container;
		})).setHeader("Azioni");

		add(brandGrid);

		add(new RouterLink("Nuovo Brand", BrandsEditView.class));
		
		updateBrandData();
	}

	private void updateBrandData() {
		List<Brand> brands = BrandService.findAll();
		brandGrid.setItems(brands);
	}

}
