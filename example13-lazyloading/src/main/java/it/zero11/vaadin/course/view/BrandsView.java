package it.zero11.vaadin.course.view;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.data.BrandRepository;
import it.zero11.vaadin.course.layout.MyLayout;
import it.zero11.vaadin.course.model.Brand;

@Route(value = "brands", layout = MyLayout.class)
@PageTitle("Brands")
public class BrandsView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final Grid<Brand> brandGrid;
	
	private ListDataProvider<Brand> dataProvider;
	private NativeLabel totalResultLabel;
	
	private final BrandRepository brandRepository;
	
	public BrandsView(BrandRepository brandRepository) {
		this.brandRepository = brandRepository;	
		add(new H4("Brands"));
		
		TextField searchTextField = new TextField();
		searchTextField.setLabel("Ricerca per nome");
		searchTextField.addValueChangeListener(event -> {
			if (event.getValue() == null)
				dataProvider.clearFilters();
			else
				dataProvider.setFilter(brand -> {
					return brand.getName().contains(event.getValue());
				});
		});
		searchTextField.setValueChangeMode(ValueChangeMode.LAZY);

		brandGrid = new Grid<>();
		brandGrid.setHeight("300px");
		Column<Brand> idCol = brandGrid.addColumn(Brand::getId)
			.setHeader("Id")
			.setFlexGrow(0)
			.setWidth("50px");
		Column<Brand> nameCol = brandGrid.addColumn(Brand::getName).setHeader("Name")
			.setFlexGrow(1)
			.setSortable(true);
		brandGrid.addColumn(new ComponentRenderer<>(brand -> {
			if (brand.getImageUrl() == null) 
				return new NativeLabel();
			else {
				Image image = new Image(brand.getImageUrl(), "boh");
				image.setHeight("40px");
				return image;
			}
		}))
		.setFlexGrow(0)
		.setHeader("Image");
		
		brandGrid.addColumn(new ComponentRenderer<Component, Brand>(brand -> {
			Span container = new Span();
			
			Button edit = new Button("", VaadinIcon.EDIT.create());
			edit.addClickListener(event -> {
				UI.getCurrent().navigate(BrandsEditView.class, brand.getId());
			});
			
			Button delete = new Button("", VaadinIcon.TRASH.create());
			delete.addClickListener(event -> {
				try {
					brandRepository.delete(brand);

					updateBrandData();
				}catch(Exception exception) {
					Notification.show("An error occurred while deleting the brand");
				}
			});

			container.add(edit, delete);
			return container;
		}))
		.setFlexGrow(0)
		.setWidth("50px")
		.setHeader("Azioni");

		brandGrid.setItemDetailsRenderer(new ComponentRenderer<Component, Brand>(
				brand -> {
					NativeLabel content = new NativeLabel();
					content.setText(brand.getDescription());
					return content;
				}));	
		
		brandGrid.setSelectionMode(SelectionMode.MULTI);
		
		HeaderRow header = brandGrid.prependHeaderRow();
		HeaderCell searchCell = header.join(idCol, nameCol);
		searchCell.setComponent(searchTextField);
		
		totalResultLabel = new NativeLabel();
		FooterRow footer = brandGrid.appendFooterRow();
		footer.getCell(nameCol).setComponent(totalResultLabel);
		add(brandGrid);

		add(new RouterLink("Nuovo Brand", BrandsEditView.class));
		
		Button multiDeleteButton = new Button("Cancella selezionati");
		multiDeleteButton.addClickListener(e -> {
			for (Brand brand : brandGrid.getSelectedItems()) {
				brandRepository.delete(brand);
			}
			updateBrandData();
			Notification.show("Cancellazione effettuata");
		});
		add(multiDeleteButton);
		
		updateBrandData();
	}

	private void updateBrandData() {
		List<Brand> brands = brandRepository.findAll();
		dataProvider = new ListDataProvider<Brand>(brands);
		brandGrid.setDataProvider(dataProvider);
		totalResultLabel.setText("Risultati: " + brands.size());
	}

}
