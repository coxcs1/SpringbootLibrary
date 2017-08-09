package com.SpringLibrary.SpringbootLibrary;

import Resource.LibraryErrorHelper;
import com.nimbusds.jose.JOSEException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Set;

import static Resource.gridHelper.authenticate;
import static Resource.gridHelper.stringClean;
import static com.SpringLibrary.SpringbootLibrary.LibraryUI.getLibraryViewDisplay;
import static com.vaadin.ui.UI.getCurrent;

/**
 * Created by ricky.clevinger on 7/31/2017.
 *
 * last modified by ricky.clevinger on 7/31/17
 */

@SpringView(name = AddUser.VIEW_NAME)
public class AddUser extends VerticalLayout implements View
{
    static final String VIEW_NAME = "addUser";

    /**
     * Variable Declaration
     */
    private RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    private LibraryErrorHelper errorHelper = new LibraryErrorHelper();//error printer
    Set<String> selected;
    int roleNum;

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
    private void init() throws ParseException, JOSEException {

            if (authenticate("Admin").equals(true) || authenticate("Librarian").equals(true)) {
                try {
                    getLibraryViewDisplay().setSizeUndefined();
                    getLibraryViewDisplay().setResponsive(true);
                } catch (RuntimeException error) {
                    Notification.show("Notify your administrator of a session ID error");
                }
                addUser();
            }
            else{

            }


    }//end init

    /**
     * Creates addUser in button
     * Adds checkIn button functionality
     * Inserts new member into database
     *
     * last modified by ricky.clevinger 7/31/17
     */
    private void addUser()
    {
        Button addUser = new Button ("Add User");
        addUser.setId("button_addUser");
        TextField fName   = new TextField("First Name");
        fName.setId("search_firstName");
        TextField lName   = new TextField("Last Name");
        lName.setId("search_lastName");
        TextField email   = new TextField("Email");
        email.setId("search_email");
        TextField password   = new TextField("Password");
        password.setId("search_password");


        // Create the selection component
        ListSelect<String> roles = new ListSelect<>("Role");


// Add some items
        roles.setItems("User", "Librarian", "Admin");

// Show 5 items and a scrollbar if there are more
        roles.setRows(3);

        roles.addValueChangeListener(event -> {
            selected = event.getValue();
            if (selected.contains("User")){
                roleNum = 1;
            }
            else if (selected.contains("Librarian")){
                roleNum = 2;
            }
            else {
                roleNum = 3;
            }
        });

        addUser.addClickListener(event ->
        {
            try
            {
                String lastName = lName.getValue();
                String firstName = fName.getValue();
                lastName = stringClean(lastName);
                firstName = stringClean(firstName);

                String emails = email.getValue().toString();
                String passwords = password.getValue();

                if (lastName.equals("") || firstName.equals(""))
                {
                    Notification.show("Please Enter a First and Last Name");
                    lName.setValue("");
                    fName.setValue("");
                }
                else if (emails.equals("") || passwords.equals(""))
                {
                    Notification.show("Please Enter an email and password");
                    email.setValue("");
                    password.setValue("");
                }
                else
                {
                    this.restTemplate.getForObject(bookMemUrl + "/members/insert/" + firstName + "/"
                            + lastName + "/" + emails + "/" + passwords + "/" + roleNum, String.class);
                    Notification.show(firstName + " "
                            + lastName + " has been added as a member.");
                    fName.setValue("");
                    lName.setValue("");
                    email.setValue("");
                    password.setValue("");
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
        addComponents(fName,lName,email,password,roles, addUser);

    }//end addUser

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
