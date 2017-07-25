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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static Resource.gridHelper.authorFilterGridChange;
import static Resource.gridHelper.titleFilterGridChange;

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

    @Value("${my.bookUrl}")
    private String bookUrl;

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

            try
            {
                this.restTemplate.getForObject(bookUrl + "/books/delete/" + this.id, String.class);
                getUI().getNavigator().navigateTo(AllBooks.VIEW_NAME);

            }

            catch (ResourceAccessException error)
            {

                Notification.show("Service unavailable, please try again in a few minutes");

            }

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

        try{
            // Retrieves the data from the book micro-service.
            books = Arrays.asList(restTemplate.getForObject(bookUrl + "/books/all", Book[].class));

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
        }
        catch (ResourceAccessException error)
        {

            Notification.show("The Book Service is currently unavailable. Please try again in a "+"" +
                    "few minutes");
        }
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
        titleFilter.addValueChangeListener(event -> {

            try
            {
                titleFilterGridChange(event, grid);
            }
            catch (NullPointerException error)
            {
                titleFilter.setValue("");
                Notification.show("Service unavailable, please try again in a few minutes");
            }
        });

        addComponent(titleFilter);

        authorFilter = new TextField();
        authorFilter.setWidth(100, Unit.PERCENTAGE);
        authorFilter.setPlaceholder("Last Name...");
        authorFilter.addValueChangeListener(event -> {

            try
            {

                authorFilterGridChange(event, grid);
            }
            catch (NullPointerException error)
            {
                authorFilter.setValue("");
                Notification.show("Service unavailable, please try again in a few minutes");
            }
        });

        addComponent(authorFilter);
    }//end createFilter



    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
