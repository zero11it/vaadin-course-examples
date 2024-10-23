package it.zero11.vaadin.course.view.brand;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import it.zero11.vaadin.course.data.BrandRepository;
import it.zero11.vaadin.course.layout.AuthenticatedLayout;
import it.zero11.vaadin.course.model.Brand;
import it.zero11.vaadin.course.view.AbstractSearchView;
import it.zero11.vaadin.course.view.products.ProductsEditView;

@SuppressWarnings("serial")
@Route(value = "brands", layout = AuthenticatedLayout.class)
public class BrandsView extends AbstractSearchView<Brand> implements HasDynamicTitle {
		
	private ListDataProvider<Brand> dataProvider;
	private NativeLabel totalResultLabel;
	
	private final BrandRepository brandRepository;
	
	public BrandsView(BrandRepository brandRepository) {
		this.brandRepository = brandRepository;	
		
		render();
		updateBrandData();
	}

	private void updateBrandData() {
		List<Brand> brands = brandRepository.findAll();
		dataProvider = new ListDataProvider<Brand>(brands);
		grid.setDataProvider(dataProvider);
		totalResultLabel.setText(getTranslation("generic.results") + ": " + brands.size());
	}

	@Override
	protected String getTitle() {
		return getTranslation("brands.title");
	}

	@Override
	protected void addFilters(HasComponents container) {
		TextField searchTextField = new TextField();
		searchTextField.setLabel(getTranslation("brands.search.name"));
		searchTextField.addValueChangeListener(event -> {
			if (event.getValue() == null)
				dataProvider.clearFilters();
			else
				dataProvider.setFilter(brand -> {
					return brand.getName().contains(event.getValue());
				});
		});
		searchTextField.setValueChangeMode(ValueChangeMode.LAZY);
		
		container.add(searchTextField);
	}

	@Override
	protected Grid<Brand> createGrid() {
		Grid<Brand> brandGrid = new Grid<>();		
		Column<Brand> idCol = brandGrid.addColumn(Brand::getId)
			.setHeader("Id")
			.setFlexGrow(0)
			.setWidth("50px");
		Column<Brand> nameCol = brandGrid.addColumn(Brand::getName).setHeader(getTranslation("brands.grid.name"))
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
		.setHeader(getTranslation("brands.grid.image"));
		
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
		.setHeader(getTranslation("brands.grid.actions"));

		brandGrid.setItemDetailsRenderer(new ComponentRenderer<Component, Brand>(
				brand -> {
					NativeLabel content = new NativeLabel();
					//content.setText(brand.getDescription());
					content.getElement().setProperty("innerHTML", brand.getDescription());
					return content;
				}));	
		
		brandGrid.setSelectionMode(SelectionMode.MULTI);
				
		totalResultLabel = new NativeLabel();
		FooterRow footer = brandGrid.appendFooterRow();
		footer.getCell(nameCol).setComponent(totalResultLabel);
		
		return brandGrid;
	}

	@Override
	protected void addActions(HasComponents container) {

		container.add(new RouterLink(getTranslation("brands.new"), BrandsEditView.class));
		
		Button multiDeleteButton = new Button(getTranslation("generic.delete.selected"));
		multiDeleteButton.addClickListener(e -> {
			for (Brand brand : grid.getSelectedItems()) {
				brandRepository.delete(brand);
			}
			updateBrandData();
			Notification.show(getTranslation("generic.delete.completed"));
		});
		multiDeleteButton.setThemeName("tertiary");
		container.add(multiDeleteButton);
		
	}
	

}
