package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/12/2017.
 */
import Model.Guy;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;


@SpringView(name = All.VIEW_NAME)
public class All extends VerticalLayout implements View {
    public static final String VIEW_NAME = "All";
    private TextField nameFilter;
    Grid<Guy> grid;

    @PostConstruct
    void init() {
        addComponent(new Label("<h2>Title</h2>", ContentMode.HTML));
        addComponent(new Label("<h2>Author</h2>", ContentMode.HTML));
        addComponent(new Label("<h2>Checked In/Out</h2>", ContentMode.HTML));

        List<Guy> guys = Arrays.asList(
                new Guy("Nicolaus Copernicus", 1543),
                new Guy("Galileo Galilei", 1564),
                new Guy("Johannes Kepler", 1571));

// Create a grid bound to the list
        grid = new Grid<>();
        grid.setItems(guys);
        grid.addColumn(Guy::getName).setCaption("Name");
        grid.addColumn(Guy::getBirthYear).setCaption("Year of birth");

        FilteredGridLayout();
        addComponent(grid);
    }

    public void FilteredGridLayout() {
        nameFilter = new TextField();
        nameFilter.setPlaceholder("Title...");
        nameFilter.addValueChangeListener(this::onNameFilterTextChange);
        addComponent(nameFilter);

    }

    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Guy> dataProvider = (ListDataProvider<Guy>) grid.getDataProvider();
        dataProvider.setFilter(Guy::getName, s -> caseInsensitiveContains(s, event.getValue()));
    }

    private Boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
    }




    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
