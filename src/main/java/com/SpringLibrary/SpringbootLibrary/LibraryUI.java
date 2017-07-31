package com.SpringLibrary.SpringbootLibrary;

import Resource.LibraryErrorHelper;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import static Resource.gridHelper.stringClean;

/**
 * Created by ricky.clevinger on 7/12/2017.
 *
 * last modified by ricky.clevinger on 7/25/17
 */
@Theme("valo")
@SpringUI
@SpringViewDisplay
public class LibraryUI extends UI implements ViewDisplay
{
    /**
     * Variable Declarations
     */
    private         RestTemplate restTemplate = new RestTemplate();
    private static  Panel LibraryViewDisplay;
    private static  VerticalLayout layout = new VerticalLayout();
    private         Label errorDisplay;
    private         LibraryErrorHelper errorHelper = new LibraryErrorHelper();
    private static  Accordion accordion;

    /**
     * Variable containing url to access backing service
     */
    @Value("${my.bookMemUrl}")
    private String bookUrl;


    /**
     * Creates the web page and sets the elements within it
     *
     * Calls a method that sets the base layout for the page
     * Calls a method that adds a Header element
     * Calls method that sets the default panel in the page which will change depending on function
     * Adds an Accordion for navigation
     *
     * @param request
     * last modified by coalsonc 7/25/17
     */
    @Override
    protected void init(VaadinRequest request)
    {
        try
        {
            setupLayout();
            addHeader();
            addDefaultView();
            createAccordion();

        }
        catch(Exception e)
        {
            String message = errorHelper.genericError(e);
            Notification.show(message);

        }

    }//end init


    /**
     * Constructs the default panel that will change based on function selection
     *
     * Creates a new panel and sets the size to be responsive
     * Adds the panel to the base layout and sets its expand ratio
     *
     * last modified by Ricky.Clevinger 7/26/17
     * */
    private void addDefaultView()
    {
        LibraryViewDisplay = new Panel();
        layout.addComponent(LibraryViewDisplay);
        layout.setExpandRatio(LibraryViewDisplay, 1.0f);

    }//end addDefaultView


    /**
     * Sets the initial layout for the page
     *
     * Adds spacing above the header for readability
     * Sets the default alignment and adds layout to page
     *
     * last modified by coalsonc 7/17/17
     */
    private void setupLayout()
    {
        layout.setSpacing(true);
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(layout);

    }//end setupLayout


    /**
     * Adds a header to the page
     *
     * Creates a new header, and sets text, size, and makes it responsive
     * Adds header to the page
     *
     * last modified by coalsonc 7/17/17
     */
    private void addHeader()
    {
        Label header = new Label ("Welcome to the Library");
        errorDisplay = new Label("");
        errorDisplay.addStyleName(ValoTheme.LABEL_H4);
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        errorDisplay.setSizeUndefined();
        layout.addComponents(header, errorDisplay);

    }//end addHeader


    /**
     * Method that Vaadin/Spring Boot use to display the page
     * @param view
     * last modified by coalsonc 7/17/17
     */
    @Override
    public void showView(View view)
    {
        LibraryViewDisplay.setContent((Component) view);
    }//end showView


    /**
     * Creates the navigation accordion, sets style and size for readability
     * Calls a method to populate the accordion buttons
     * Adds the accordion to the layout
     *
     * last modified by coalsonc 7/18/17
     */
    private void createAccordion()
    {
        accordion = new Accordion();
        //accordion.addStyleName(ValoTheme.ACCORDION_BORDERLESS);
        accordion.setWidth("20%");
        accordion.setSizeUndefined();

        accordion.addTab(addAccordionNavigationButtons(), "Navigation");
        accordion.addTab(addAdminAccordion(), "Admin");
        layout.addComponent(accordion);
    }//end createAccordion


    /**
     * Method used by CreateAccordion to make the navigation buttons
     * Creates the buttons and the layout to pin them to
     * Adds listeners to the buttons to allow navigation
     * Adds buttons to layout and returns it to add to accordion
     *
     * last modified by Ricky.Clevinger 7/26/17
     */
    private Layout addAccordionNavigationButtons()
    {
        Layout tab      = new VerticalLayout();
        Button checkIn  = new Button("Check In");
        Button checkOut = new Button("Check Out");
        Button home     = new Button("Home");

        checkIn.addClickListener(event -> getUI().getNavigator().navigateTo(CheckIn.VIEW_NAME));
        checkOut.addClickListener(event -> getUI().getNavigator().navigateTo(CheckOut.VIEW_NAME));
        home.addClickListener(event -> getUI().getNavigator().navigateTo(DefaultView.VIEW_NAME));

        tab.addComponents(checkIn, checkOut, home);

        return tab;
    }//end addAccordionNavigationButtons


    /**
     * Method used by CreateAccordion to make the admin activity Accordion
     * Creates the admin accordion and the layout to add to the accordion
     * Adds listeners to the buttons for admin input.
     * Adds the inputs and buttons to the layout and returns it to the CreateAccordion method
     *
     * last modified by ricky.clevinger 7/18/17
     */
    private Component addAdminAccordion()
    {
        Accordion accordion = new Accordion();
        accordion.addStyleName(ValoTheme.ACCORDION_BORDERLESS);
        accordion.setWidth("100%");

        accordion.addTab(addUserInput(), "Add User");
        accordion.addTab(addBookInput(), "Add Book");
        accordion.addTab(removeUser(), "Remove User");
        accordion.addTab(removeBook(), "Remove Book");

        return accordion;
    }// end addAdminAccordion


    /**
     * Method used by AddAdminAccordion to create the add user functionality
     * Creates the textFields, button, and the layout to pin them to
     * Adds listeners to the buttons to allow navigation
     * Adds textFields and a button to layout and returns it to add to accordion for add user
     *
     * last modified by ricky.clevinger 7/19/17
     */
    private Layout addUserInput()
    {
        Layout tab  = new VerticalLayout();
        Button users  = new Button("Add Users");
        users.addClickListener(event -> {

            errorDisplay.setCaption("");
            getUI().getNavigator().navigateTo(AddUser.VIEW_NAME);

        });
        tab.addComponents(users);
        return tab;

    }//end addUserInput


    /**
     * Method used by AddAdminAccordion to create the add book functionality
     * Creates the textFields, button, and the layout to pin them to
     * Adds listeners to the buttons to allow navigation
     * Adds textFields and a button to layout and returns it to add to accordion for add book
     *
     * Last modified by coalsonc 7/25/17
     */
    private Layout addBookInput()
    {
        Layout tab  = new VerticalLayout();
        Button books  = new Button("Add Books");
        books.addClickListener(event -> {

            errorDisplay.setCaption("");
            getUI().getNavigator().navigateTo(AddBooks.VIEW_NAME);

        });
        tab.addComponents(books);
        return tab;

    }//end addBookInput


    /**
     * Method used by AddAdminAccordion to create the remove user functionality
     * Creates the button and the layout to pin them to
     * Adds listeners to the buttons to allow navigation
     * Adds textFields and a button to layout and returns it to add to accordion for remove user
     *
     * last modified by ricky.clevinger 7/18/17
     */
    private Layout removeUser()
    {

        Layout tab  = new VerticalLayout();
        Button all  = new Button("View All Users");
        all.addClickListener(event -> {
            errorDisplay.setCaption("");
            getUI().getNavigator().navigateTo(AllMembers.VIEW_NAME);
        });
        tab.addComponents(all);
        return tab;

    }//end removeUser


    /**
     * Method used by AddAdminAccordion to create the remove book functionality
     * Creates the button and the layout to pin them to
     * Adds listeners to the buttons to allow navigation
     * Adds textFields and a button to layout and returns it to add to accordion for remove book
     *
     * last modified by ricky.clevinger 7/18/17
     */
    private Layout removeBook()
    {
        Layout tab  = new VerticalLayout();
        Button all  = new Button("View All Books");
        all.addClickListener(event -> {

            errorDisplay.setCaption("");
            getUI().getNavigator().navigateTo(AllBooks.VIEW_NAME);

        });
        tab.addComponents(all);
        return tab;

    }//end removeBook


    /**
     * Getter for the panel. Used to change sizes depending on the current view.
     * @return LibraryViewDisplay
     *
     * Last modified by ricky.clevinger 7/26/17
     */
    public static Panel getLibraryViewDisplay()
    {
        return LibraryViewDisplay;
    }//end getLibraryViewDisplay


    /**
     * Setter for the panel.
     *
     * Last modified by ricky.clevinger 7/26/17
     */
    public void setLibraryViewDisplay(Panel libraryViewDisplay)
    {
        LibraryViewDisplay = libraryViewDisplay;
    }//end setLibraryViewDisplay

}
