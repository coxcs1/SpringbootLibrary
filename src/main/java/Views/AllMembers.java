package Views;

import Model.Book;
import Model.Member;
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
import static Resource.gridHelper.fNameFilterGridChange;
import static Resource.gridHelper.lNameFilterGridChange;
import static com.SpringLibrary.SpringbootLibrary.LibraryUI.getLibraryViewDisplay;

/**
 * Created by ricky.clevinger on 7/12/2017.
 *
 * last modified by ricky.clevinger on 7/26/17
 */
@SpringView(name = AllMembers.VIEW_NAME)
public class AllMembers extends VerticalLayout implements View
{
    public static final String VIEW_NAME = "AllMembers";  // Name of the View, or "Page".

    /**
     * Variable Declarations
     */
    private TextField fNameFilter;   // TextField will be used to filter the results on the grid.
    private TextField lNameFilter; // TextField will be used to filter the results on the grid.
    private Grid<Member> grid;  // Grid that will display and organize books on the all.java page.
    private String id;  // Id used to determine which item is selected in the grid.
    private RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    private LibraryErrorHelper errorHelper = new LibraryErrorHelper(); // Creates instance of LibraryErrorHelper
    private HorizontalLayout hLayout = new HorizontalLayout(); // Layout that hold the delete button and the popup
    private VerticalLayout popupContent = new VerticalLayout(); // Layout that hold the popup components
    private PopupView popup; // Popup that appears when delete button is clicked

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
        createMemberGrid();
        createDeleteButton();
        createPopup();
        Page.getCurrent().setTitle("View All Members");

    }//end init

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

                List<Book> books = Arrays.asList(this.restTemplate.getForObject(bookMemUrl + "/books/mid/" + this.id, Book[].class));

                if (books.isEmpty()){
                    this.restTemplate.getForObject(bookMemUrl + "/members/delete/" + this.id, String.class);
                    getUI().getNavigator().navigateTo(AllMembers.VIEW_NAME);
                }
                else {
                    Notification.show("Member currently has a book checked out");
                }
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
            Notification.show("No Member Selected");
        }
    }

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
        delete.setId("button_deleteMember");

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
                Notification.show("Please Select a Member Account to Delete");
            }
            catch (NumberFormatException error){
                errorHelper.genericError(error);
                Notification.show("No Member Selected");
            }
        });//end click listener

        // Add delete button to the view.
        addComponent(hLayout);

    }//end createDeleteButton


    /**
     * Function shall retrieve data from micro-service.
     * Creates and populates the grid.
     * Adds listener to record the user selected item.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    private void createMemberGrid()
    {
        try
        {
            // Retrieves the data from the book micro-service.
            List<Member> members = Arrays.asList(restTemplate.getForObject(bookMemUrl + "/members/all", Member[].class));

            // Create a grid and adds listener to record selected item.
            grid = new Grid<>();
            grid.setId("grid_member");
            grid.addSelectionListener(event ->
            {
                try
                {
                    if(event.getFirstSelectedItem().isPresent()) {
                        this.id = event.getFirstSelectedItem().get().getId() + "";
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
            grid.setItems(members);

            //Specifies what parts of the objects in the grid are shown.
            grid.addColumn(Member::getId, new TextRenderer()).setCaption("ID");
            grid.addColumn(Member::getFName, new TextRenderer()).setCaption("First Name");
            grid.addColumn(Member::getLName, new TextRenderer()).setCaption("Last Name");

            grid.setSizeFull();
            grid.getColumns().get(0).setResizable(false);
            grid.getColumns().get(1).setResizable(false);
            grid.getColumns().get(2).setResizable(false);
            // Add the grid to the view.
            addComponent(grid);
        }
        catch (ResourceAccessException error)
        {
            errorHelper.genericError(error);
            Notification.show("The Book Service is currently unavailable. Please try again in a few minutes");
        }
    }//end createGrid


    /**
     * Function shall add the search filter to the page.
     * User shall type in part of a book title and the grid will changed accordingly.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    private void createFilter()
    {
        fNameFilter = new TextField();
        fNameFilter.setWidth(100, Unit.PERCENTAGE);
        fNameFilter.setPlaceholder("First Name...");
        fNameFilter.setId("search_authorFirstName");

        fNameFilter.addValueChangeListener(event ->
        {
            try
            {
                fNameFilterGridChange(event, grid);
            }
            catch (NullPointerException error)
            {
                errorHelper.genericError(error);
                fNameFilter.setValue("");
                Notification.show("Service unavailable, please try again in a few minutes");
            }
        });
        fNameFilter.addFocusListener(event -> lNameFilter.setValue(""));
        addComponent(fNameFilter);

        lNameFilter = new TextField();
        lNameFilter.setWidth(100, Unit.PERCENTAGE);
        lNameFilter.setPlaceholder("Last Name...");
        lNameFilter.setId("search_authorLastName");

        lNameFilter.addValueChangeListener(event ->
        {
            try
            {
                lNameFilterGridChange(event, grid);
            }
            catch (NullPointerException error)
            {
                errorHelper.genericError(error);
                fNameFilter.setValue("");
                Notification.show("Service unavailable, please try again in a few minutes");
            }
        }
        );
        lNameFilter.addFocusListener(event -> fNameFilter.setValue(""));

        addComponent(lNameFilter);
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
