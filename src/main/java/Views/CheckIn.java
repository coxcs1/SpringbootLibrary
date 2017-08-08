package Views;

import Model.Book;
import Model.Member;
import Resource.LibraryErrorHelper;
import Resource.gridHelper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.TextRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import static Resource.gridHelper.titleFilterGridChange;
import static com.SpringLibrary.SpringbootLibrary.LibraryUI.getLibraryViewDisplay;

/**
 * Created by ricky.clevinger on 7/13/2017.
 *
 * last modified by ricky.clevinger on 7/26/17
 */

@SpringView(name = CheckIn.VIEW_NAME)
public class CheckIn extends VerticalLayout implements View
{
    public static final String VIEW_NAME = "CheckIn";

    /**
     * Variable Declaration
     */

    private Grid<Book> bookReturnGrid; // Grid used to display the books.
    private String titleId;  // Id used to determine which item is selected in the grid.
    private String memberId;  // Id used to determine which item is selected in the grid.
    private TextField titleFilter;   // TextField will be used to filter the results on the grid.
    private RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    private LibraryErrorHelper errorHelper = new LibraryErrorHelper();//error printer

    /**
     * Variable containing url to access backing service
     */
    @Value("${my.bookMemUrl}")
    private String bookMemUrl;

    /**
     * Initializes the view..
     * Re-sizes the panel
     * Calls addFilters to create search filter for the grid.
     * Calls setupGrid to create and populate the grid.
     * Calls addCheckInButton to create Check In button and add functionality to the button.
     *
     * last modified by ricky.clevinger 7/26/17
     */
    @PostConstruct
    @SuppressWarnings("unused")
    void init()
    {
        getLibraryViewDisplay().setSizeFull();
        addFilters();
        setupGrid();
        addCheckInButton();
        Page.getCurrent().setTitle("Check In");
    }//end init


    /**
     * Function shall add the search filter to the page.
     * User shall type in part of a book title and the grid will changed accordingly.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    private void addFilters()
    {
        titleFilter = new TextField();
        titleFilter.setWidth(100, Unit.PERCENTAGE);
        titleFilter.setPlaceholder("Title...");
        titleFilter.setId("search_title");

        titleFilter.addValueChangeListener(event ->
        {
            try
            {
                titleFilterGridChange(event, bookReturnGrid);
            }
            catch (NullPointerException error)
            {
                errorHelper.genericError(error);
                titleFilter.setValue("");
                Notification.show("Service unavailable, please try again in a few minutes");
            }
        });

        addComponent(titleFilter);
    }//end addFilters



    /**
     * Creates Check in button
     * Adds checkIn button functionality
     * Sends query to record checkout in transaction database
     * Updates book to it is checked in
     *
     * last modified by ricky.clevinger 7/26/17
     */
    private void addCheckInButton()
    {
        Button checkIn = new Button ("Check In");
        checkIn.setId("button_checkIn");

        checkIn.addClickListener(event ->
        {
            try
            {
                List<Book> checkedIn = Arrays.asList(restTemplate.getForObject(bookMemUrl + "/books/checkAndId/1/" + this.titleId, Book[].class));
                if ( checkedIn.isEmpty()){
                    this.restTemplate.getForObject(bookMemUrl + "/trans/insert/" + this.titleId + "/" + 1 + "/"
                            + memberId, String.class);
                    this.restTemplate.getForObject(bookMemUrl + "/books/cho/" + this.titleId + "/" + 1 + "/"
                            + 0, String.class);

                    getUI().getNavigator().navigateTo(CheckIn.VIEW_NAME);
                }
                else{
                    getUI().getNavigator().navigateTo(CheckIn.VIEW_NAME);
                    Notification.show("Book has already been checked in.");
                }
            }//end try

            catch (ResourceAccessException error)
            {
                errorHelper.genericError(error);
                Notification.show("Service unavailable, please try again in a few minutes");
            }//end catch
            catch (HttpClientErrorException error)
            {
                errorHelper.genericError(error);
                Notification.show("Please select a book to check in");
            }//end catch
            catch (HttpServerErrorException | HttpMessageNotReadableException error){
                errorHelper.genericError(error);
                Notification.show("No Book Selected");
            }
        });

        checkIn.setResponsive(true);
        addComponent(checkIn);
        setResponsive(true);
        addComponent(checkIn);

    }//end addCheckInButton


    /**
     * Creates list of all books
     * Listener to select a book in the grid
     * Add columns to the grid for title  and author
     * Pulls id and compares to members
     * Adds columns for member name
     * Adds column for due date.
     *
     * last modified by ricky.clevinger 7/26/17
     */
    private void setupGrid()
    {
        bookReturnGrid = new Grid<>();
        bookReturnGrid.setId("grid_bookReturn");
        try
        {
            List<Book> books = Arrays.asList(restTemplate.getForObject(bookMemUrl + "/books/check/2", Book[].class));

            bookReturnGrid.addSelectionListener(event ->
            {
                if (event.getAllSelectedItems().isEmpty())
                {
                    this.titleId = 0 + "";
                    this.memberId = 0 + "";
                }
                else
                {
                    if (event.getFirstSelectedItem().isPresent()) {
                        this.titleId = event.getFirstSelectedItem().get().getBookId() + "";
                        this.memberId = event.getFirstSelectedItem().get().getMid() + "";
                    }
                }
        });//end select listener

        bookReturnGrid.setItems(books);

        //Specifies what parts of the objects in the grid are shown.
        bookReturnGrid.addColumn(Book::getBookId, new TextRenderer()).setCaption("Book ID");
        bookReturnGrid.addColumn(Book::getTitle, new TextRenderer()).setCaption("Title");

        bookReturnGrid.addColumn(Book ->
                Arrays.asList(restTemplate.getForObject(bookMemUrl + "/members/id/"
                        + Book.getMid(), Member[].class)).get(0).getFName() + " "
                        + Arrays.asList(restTemplate.getForObject(bookMemUrl + "/members/id/"
                        + Book.getMid(), Member[].class)).get(0).getLName()).setCaption("Checked Out By");
        bookReturnGrid.addColumn(Book -> gridHelper.overdue(Book.getOutDate(),
                new Date(System.currentTimeMillis()))).setCaption("Due Date");

        }//end try
        catch (ResourceAccessException error)
        {
            errorHelper.genericError(error);
            Notification.show("The Book Service is currently unavailable. Please try again in a "+"" +
                    "few minutes");
        }//end catch

        bookReturnGrid.setSizeFull();
        bookReturnGrid.getColumns().get(0).setResizable(false);
        bookReturnGrid.getColumns().get(1).setResizable(false);
        bookReturnGrid.getColumns().get(2).setResizable(false);
        bookReturnGrid.getColumns().get(3).setResizable(false);
        addComponent(bookReturnGrid);

    }//end setupGrid


    /**
     * Sets a listener that automatically changes the default view when a selection is made
     * @param event on view change
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event)
    {
        // This view is constructed in the init() method()
    }//end enter
}
