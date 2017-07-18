package com.SpringLibrary.SpringbootLibrary;


import Model.Book;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by ricky.clevinger on 7/12/2017.
 */

@SpringView(name = AllBooks.VIEW_NAME)
public class AllBooks extends VerticalLayout implements View {
    public static final String VIEW_NAME = "AllBooks";  // Name of the View, or "Page".
    private TextField titleFilter;   // TextField will be used to filter the results on the grid.
    Grid<Book> grid;  // Grid that will display and organize books on the all.java page.
    private String title;
    String id;
    RestTemplate restTemplate = new RestTemplate();
    List<Book> books;

    @PostConstruct
    /**
     * Retrieves the data and constructs the view.
     */
    void init() {
        // Retrieved the data from the book micro-service.
        books = Arrays.asList(restTemplate.getForObject("http://localhost:8090/books/all", Book[].class));
        //List<Book> list = Arrays.asList(books);

        // Create a grid
        grid = new Grid<>();
        final String id = "";
        grid.addSelectionListener(event -> {
            Set<Book> selected = event.getAllSelectedItems();
            //Notification.show(selected.size() + " items selected");
            Notification.show(event.getFirstSelectedItem().get().getBookId() + "");
            this.id = event.getFirstSelectedItem().get().getBookId() + "";
        });




        grid.setWidth(100, Unit.PERCENTAGE);
        // Bound array to the grid
        grid.setItems(books);
        //Choose what parts of the objects in the grid are shown.
        grid.addColumn(Book::getTitle, new TextRenderer()).setCaption("Title");
        grid.addColumn(Book::getAuthFName, new TextRenderer()).setCaption("Author First Name");
        grid.addColumn(Book::getAuthLName, new TextRenderer()).setCaption("Author Last Name");

        Button delete = new Button("Delete");
        delete.addClickListener(event -> {
                System.out.print(this.id + "");
            this.restTemplate.getForObject("http://localhost:8090/books/delete/" + this.id, String.class);
            
        });
        // Add Filter box and the grid to the view.
        FilteredGridLayout();
        addComponent(grid);
        addComponent(delete);




    }



    /**
     * Function shall add the search filter to the page.
     * User shall type in part of a book title and the grid will changed accordingly.
     */
    public void FilteredGridLayout() {
        titleFilter = new TextField();
        titleFilter.setWidth(100, Unit.PERCENTAGE);
        titleFilter.setPlaceholder("Book Title...");
        titleFilter.addValueChangeListener(this::onNameFilterTextChange);
        addComponent(titleFilter);
    }

    /**
     * Helper function for the FilteredGridLayout.
     * Chnages the grid and compares the titles.
     * @param event
     */
    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Book> dataProvider = (ListDataProvider<Book>) grid.getDataProvider();
        dataProvider.setFilter(Book::getTitle, s -> caseInsensitiveContains(s, event.getValue()));
    }

    /**
     *Returns a boolean telling if the lowercase form of text input into the filter is contain by any of the lowercase versions of the book titles.
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
