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

    /**
     *Returns a boolean telling whether the lowercase form of text input into the filter is contain by any of the lowercase vesions of the book titles.
     * @param where the books titles its comparing to
     * @param what  the filter wood being compared to the book titles
     * @return Boolean telling if the lower case value of the filter input and the book titles match
     */
    private Boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
    }



    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
