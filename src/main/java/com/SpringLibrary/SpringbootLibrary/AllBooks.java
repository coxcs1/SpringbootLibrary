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
 *
 * last modified by ricky.clevinger on 7/19/17
 */
@SpringView(name = AllBooks.VIEW_NAME)
public class AllBooks extends VerticalLayout implements View {
    public static final String VIEW_NAME = "AllBooks";  // Name of the View, or "Page".
    private TextField titleFilter;   // TextField will be used to filter the results on the grid.
    private TextField authorFilter;   // TextField will be used to filter the results on the grid.
    Grid<Book> grid;  // Grid that will display and organize books on the all.java page.
    String id;  // Id used to determine which item is selected in the grid.
    RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    List<Book> books; // Used to store data retrieved from micro-service. Placed into the grid.

    @PostConstruct
    /**
     * Initializes the view..
     * Calls createFilter to create search filter for the grid.
     * Calls createGrid to create and populate the grid.
     * Calls createDeleteButton to create delete button and add functionality to the button.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    void init() {
        createFilter();
        createBookGrid();
        createDeleteButton();
    }//end init

    /**
     * Method add delete button to view.
     * On button click the selected item in the grid will be removed from grid and backing micro-service.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    public void createDeleteButton() {

        // Delete button to remove selected item from the grid as well as the micro-service.
        Button delete = new Button("Delete");
        delete.addClickListener(event -> {
            this.restTemplate.getForObject("http://localhost:8090/books/delete/" + this.id, String.class);
            getUI().getNavigator().navigateTo(AllBooks.VIEW_NAME);
        });
        // Add delete button to the view.
        addComponent(delete);
    }//end createDeleteButton

    /**
     * Function shall retrieve data from microservice.
     * Creates and populates the grid.
     * Adds listener to record the user selected item.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    public void createBookGrid() {

        // Retrieves the data from the book micro-service.
        books = Arrays.asList(restTemplate.getForObject("http://localhost:8090/books/all", Book[].class));

        // Create a grid and adds listener to record selected item.
        grid = new Grid<>();
        grid.addSelectionListener(event -> {
            this.id = event.getFirstSelectedItem().get().getBookId() + "";
        });

        // Sets the width of the grid.
        grid.setWidth(100, Unit.PERCENTAGE);
        // Sets list to the grid
        grid.setItems(books);
        //Specifies what parts of the objects in the grid are shown.
        grid.addColumn(Book::getTitle, new TextRenderer()).setCaption("Title");
        grid.addColumn(Book ->
                Book.getAuthFName() + " " + Book.getAuthLName()).setCaption("Author");

        // Add the grid to the view.
        addComponent(grid);
    }//end createGrid

    /**
     * Function shall add the search filter to the page.
     * User shall type in part of a book title and the grid will changed accordingly.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    public void createFilter() {
        titleFilter = new TextField();
        titleFilter.setWidth(100, Unit.PERCENTAGE);
        titleFilter.setPlaceholder("Book Title...");
        titleFilter.addValueChangeListener(this::titleFilterGridChange);
        addComponent(titleFilter);

        authorFilter = new TextField();
        authorFilter.setWidth(100, Unit.PERCENTAGE);
        authorFilter.setPlaceholder("Last Name...");
        authorFilter.addValueChangeListener(this::authorFilterGridChange);
        addComponent(authorFilter);
    }//end createFilter

    /**
     * Helper function for the createFilter.
     * Changes the grid and compares the titles.
     * @param event
     * last modified by ricky.clevinger 7/19/17
     */
    private void titleFilterGridChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Book> dataProvider = (ListDataProvider<Book>) grid.getDataProvider();
        dataProvider.setFilter(Book::getTitle, s -> caseInsensitiveContains(s, event.getValue()));
    }//end titleFilterGridChange

    /**
     * Helper function for the createFilter.
     * Changes the grid and compares the titles.
     * @param event
     * last modified by ricky.clevinger 7/19/17
     */
    private void authorFilterGridChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Book> dataProvider = (ListDataProvider<Book>) grid.getDataProvider();
        dataProvider.setFilter(Book::getAuthLName, s -> caseInsensitiveContains(s, event.getValue()));
    }//end titleFilterGridChange

    /**
     *Returns a boolean telling if the lowercase form of text input into the filter is contain
     * by any of the lowercase versions of the book titles.
     * @param where the books titles its comparing to
     * @param what  the filter wood being compared to the book titles
     * @return Boolean telling if the lower case value of the filter input and the book titles match
     *
     * last modified by ricky.clevinger 7/19/17
     */
    private Boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
    }//end caseInsensitiveContains


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
