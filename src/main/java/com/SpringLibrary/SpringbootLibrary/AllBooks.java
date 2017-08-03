package com.SpringLibrary.SpringbootLibrary;

import Model.Book;
import Resource.LibraryErrorHelper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.TextRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import static Resource.gridHelper.authorFilterGridChange;
import static Resource.gridHelper.titleFilterGridChange;
import static com.SpringLibrary.SpringbootLibrary.LibraryUI.getLibraryViewDisplay;

/**
 * Created by ricky.clevinger on 7/12/2017.
 *
 * last modified by ricky.clevinger on 7/26/17
 */
@SpringView(name = AllBooks.VIEW_NAME)
public class AllBooks extends VerticalLayout implements View
{
    static final String VIEW_NAME = "AllBooks";  // Name of the View, or "Page".

    /**
     * Variable Declarations
     */
    private TextField titleFilter;   // TextField will be used to filter the results on the grid.
    private TextField authorFilter;   // TextField will be used to filter the results on the grid.
    private Grid<Book> grid;  // Grid that will display and organize books on the all.java page.
    private String id;  // Id used to determine which item is selected in the grid.
    private RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    private LibraryErrorHelper errorHelper = new LibraryErrorHelper(); // Instantiates the LibraryErrorHelper
    private HorizontalLayout hLayout = new HorizontalLayout();
    private VerticalLayout popupContent = new VerticalLayout();
    private PopupView popup;

    /**
     * Variable containing url to access backing service
     */
    @Value("${my.bookMemUrl}")
    private String bookMemUrl;


    /**
     * Initializes the view..
     * Re-sizes the panel
     * Calls createFilter to create search filter for the grid.
     * Calls createGrid to create and populate the grid.
     * Calls createDeleteButton to create delete button and add functionality to the button.
     *
     * last modified by ricky.clevinger 7/26/17
     */
    @PostConstruct
    @SuppressWarnings("unused")
    void init()
    {
        getLibraryViewDisplay().setSizeFull();
        createFilter();
        createBookGrid();
        createDeleteButton();
        createPopup();
        Page.getCurrent().setTitle("View All Books");
    }//end init


    /**
     * Method add delete button to view.
     * On button click the selected item in the grid will be removed from grid and backing micro-service.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    private void createDeleteButton()
    {
        // Delete button to remove selected item from the grid as well as the micro-service.
        Button delete = new Button("Delete");
        hLayout.addComponent(delete);
        delete.setId("button_deleteUser");
        delete.addClickListener(event ->
        {
            try
            {
                if (Integer.parseInt(this.id) > 0 ){
                    popup.setPopupVisible(true);
                }
            }

            catch (ResourceAccessException error)
            {
                errorHelper.genericError(error);
                Notification.show("Service unavailable, please try again in a few minutes");
            }
            catch (HttpClientErrorException error)
            {
                errorHelper.genericError(error);
                Notification.show("Please Select a Book to Delete");
            }
            catch (NumberFormatException error){
                errorHelper.genericError(error);
                Notification.show("No Book Selected");
            }
        });
        // Add delete button to the view.
        addComponent(hLayout);
    }//end createDeleteButton

    /**
     * Method add popup to view.
     * On click of delete button, popup appears to ensure user is sure of the delete.
     *
     * last modified by ricky.clevinger 8/3/17
     */
    private void createPopup(){
        try {
            Button yes = new Button("Yes");
            Button no = new Button("No");
            Label label = new Label("Are you sure?");

            yes.addClickListener(clickEvent -> {
                this.restTemplate.getForObject(bookMemUrl + "/books/delete/" + this.id, String.class);
                getUI().getNavigator().navigateTo(AllBooks.VIEW_NAME);
            });

            no.addClickListener(clickEvent -> popup.setPopupVisible(false));
            popupContent.addComponents(label, yes, no);

            // The component itself
            popup = new PopupView(null, popupContent);
            popup.setPopupVisible(false);
            hLayout.addComponent(popup);
            hLayout.setComponentAlignment(popup, Alignment.TOP_CENTER);
        }
        catch (NullPointerException error){
            errorHelper.genericError(error);
            Notification.show("No Book Selected");
        }
    }


    /**
     * Function shall retrieve data from microservice.
     * Creates and populates the grid.
     * Adds listener to record the user selected item.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    private void createBookGrid()
    {
        try
        {
            // Retrieves the data from the book micro-service.
            List<Book> books = Arrays.asList(restTemplate.getForObject(bookMemUrl + "/books/all", Book[].class));

                // Create a grid and adds listener to record selected item.
                grid = new Grid<>();
                grid.setId("grid_books");
                grid.addSelectionListener(event ->
                {
                    try
                    {
                        if(event.getFirstSelectedItem().isPresent())
                        {
                            this.id = event.getFirstSelectedItem().get().getBookId() + "";
                        }
                    }
                    catch(NoSuchElementException error)
                    {
                        errorHelper.genericError(error);
                        Notification.show("Please do not double click the grid");
                    }
                });

            // Sets the width of the grid.
            grid.setWidth(100, Unit.PERCENTAGE);

            // Sets list to the grid
            grid.setItems(books);

            //Specifies what parts of the objects in the grid are shown.
            grid.addColumn(Book::getBookId, new TextRenderer()).setCaption("ID");
            grid.addColumn(Book::getTitle, new TextRenderer()).setCaption("Title");
            grid.addColumn(Book ->
                Book.getAuthFName() + " " + Book.getAuthLName()).setCaption("Author");

            grid.setSizeFull();
            // Add the grid to the view.
            addComponent(grid);
        }//try
        catch (ResourceAccessException error)
        {
            errorHelper.genericError(error);
            Notification.show("The Book Service is currently unavailable. Please try again in a "+"" +
                    "few minutes");
        }//catch

    }//end createGrid


    /**
     * Function shall add the search filter to the page.
     * User shall type in part of a book title and the grid will changed accordingly.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    private void createFilter()
    {
        titleFilter = new TextField();
        titleFilter.setWidth(100, Unit.PERCENTAGE);
        titleFilter.setPlaceholder("Book Title...");
        titleFilter.setId("search_title");
        titleFilter.addValueChangeListener(event -> {
            try
            {
                titleFilterGridChange(event, grid);
            }
            catch (NullPointerException error)
            {
                errorHelper.genericError(error);
                titleFilter.setValue("");
                Notification.show("Service unavailable, please try again in a few minutes");
            }
        });
        titleFilter.addFocusListener(event -> authorFilter.setValue(""));

        addComponent(titleFilter);

        authorFilter = new TextField();
        authorFilter.setWidth(100, Unit.PERCENTAGE);
        authorFilter.setPlaceholder("Last Name...");
        authorFilter.setId("search_authorLastName");
        authorFilter.addValueChangeListener(event ->
        {
            try
            {
                authorFilterGridChange(event, grid);
            }
            catch (NullPointerException error)
            {
                errorHelper.genericError(error);
                authorFilter.setValue("");
                Notification.show("Service unavailable, please try again in a few minutes");
            }
        });
        authorFilter.addFocusListener(event -> titleFilter.setValue(""));
        addComponent(authorFilter);
    }//end createFilter


    /**
     * Sets a listener that automatically changes the default view when a selection is made
     * @param event on view change
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }//end enter
}
