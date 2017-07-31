package com.SpringLibrary.SpringbootLibrary;

import Model.Book;
import Model.Member;
import Resource.LibraryErrorHelper;
import Resource.gridHelper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.TextRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static Resource.gridHelper.stringClean;
import static Resource.gridHelper.titleFilterGridChange;
import static com.SpringLibrary.SpringbootLibrary.LibraryUI.getLibraryViewDisplay;



/**
 * Created by ricky.clevinger on 7/13/2017.
 *
 * last modified by ricky.clevinger on 7/26/17
 */

@SpringView(name = AddUser.VIEW_NAME)
public class AddUser extends VerticalLayout implements View
{
    public static final String VIEW_NAME = "addUser";

    /**
     * Variable Declaration
     */

    private         Label errorDisplay;
    private Grid<Book> bookReturnGrid;
    private String titleId;  // Id used to determine which item is selected in the grid.
    private String memberId;  // Id used to determine which item is selected in the grid.
    private TextField titleFilter;   // TextField will be used to filter the results on the grid.
    private RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    private List<Book> books; // Used to store data retrieved from micro-service. Placed into the grid.
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
    void init()
    {
        getLibraryViewDisplay().setSizeUndefined();
        addUser();
    }//end init


    /**
     * Creates Check in button
     * Adds checkIn button functionality
     * Sends query to record checkout in transaction database
     * Updates book to it is checked in
     *
     * last modified by ricky.clevinger 7/26/17
     */
    private void addUser()
    {
        Button addUser = new Button ("Add User");
        com.vaadin.ui.TextField fName   = new com.vaadin.ui.TextField("First Name");
        com.vaadin.ui.TextField lName   = new com.vaadin.ui.TextField("Last Name");

        addUser.addClickListener(event ->
        {
            try
            {
                String lastName = lName.getValue();
                String firstName = fName.getValue();
                lastName = stringClean(lastName);
                firstName = stringClean(firstName);

                if (lastName.equals("") || firstName.equals(""))
                {
                    Notification.show("Please Enter a First and Last Name");
                    lName.setValue("");
                    fName.setValue("");
                }
                else
                {
                    this.restTemplate.getForObject(bookMemUrl + "/members/insert/" + firstName + "/"
                            + lastName, String.class);
                    Notification.show(firstName + " "
                            + lastName + " has been added as a member.");
                    fName.setValue("");
                    lName.setValue("");
                }
            }//end try
            catch (HttpClientErrorException error)
            {
                errorHelper.httpError(error);
                Notification.show("Cannot access add user service, please try again in a few minutes.");
            }
            catch (NullPointerException error)
            {
                errorHelper.genericError(error);
                Notification.show("Cannot access Add User Service, please try again in a few minutes");
            }
            catch (ResourceAccessException e)
            {
                Notification.show("Cannot access Add User Service, please try again in a few minutes");
                errorHelper.genericError(e);
            }
        });//end add click event
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponents(fName,lName, addUser);

    }//end addCheckInButton


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
