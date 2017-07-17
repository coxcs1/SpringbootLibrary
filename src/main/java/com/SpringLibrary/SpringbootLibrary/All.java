package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/12/2017.
 */
import Model.Book;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static jdk.nashorn.internal.objects.NativeArray.length;


@SpringView(name = All.VIEW_NAME)
public class All extends VerticalLayout implements View {
    public static final String VIEW_NAME = "All";
    private TextField titleFilter;
    Grid<Book> grid;

    @PostConstruct
    void init() {


        RestTemplate restTemplate = new RestTemplate();
        Book[] books = restTemplate.getForObject("http://localhost:8090/books/all", Book[].class);




// Create a grid bound to the list
        grid = new Grid<>();
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setItems(books);
        grid.addColumn(Book::getTitle).setCaption("Title");
        grid.addColumn(Book::getAuthFName).setCaption("Author First Name");
        grid.addColumn(Book::getAuthLName).setCaption("Author Last Name");

        FilteredGridLayout();
        addComponent(grid);
    }

    public void FilteredGridLayout() {
        titleFilter = new TextField();
        titleFilter.setWidth(100, Unit.PERCENTAGE);
        titleFilter.setPlaceholder("Title...");
        titleFilter.addValueChangeListener(this::onNameFilterTextChange);
        addComponent(titleFilter);


    }

    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Book> dataProvider = (ListDataProvider<Book>) grid.getDataProvider();
        dataProvider.setFilter(Book::getTitle, s -> caseInsensitiveContains(s, event.getValue()));
    }


    private Boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
    }




    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
