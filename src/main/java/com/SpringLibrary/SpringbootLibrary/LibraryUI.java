package com.SpringLibrary.SpringbootLibrary;

import Resource.LibraryErrorHelper;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.BeanCreationException;


/**
 * Created by ricky.clevinger on 7/12/2017.
 *
 * last modified by ricky.clevinger on 7/25/17
 */
@Theme("valo")
@SpringUI
@SpringViewDisplay
@PreserveOnRefresh
public class LibraryUI extends UI implements ViewDisplay, ClientConnector.DetachListener
{
    /**
     * Variable Declarations
     */
    private static  Panel LibraryViewDisplay;
    private VerticalLayout layout = new VerticalLayout();
    private LibraryErrorHelper errorHelper = new LibraryErrorHelper();
    private ConnectorTracker tracker;


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
     * Method to detect session expiry errors.
     *
     * Once Errors have been detected and resolved, remove this method
     *
     * @return tracker (this currently goes nowhere)
     */

    @Override
    public ConnectorTracker getConnectorTracker()
    {
        if (this.tracker == null)
        {
            this.tracker = new ConnectorTracker(this)
            {
                @Override
                public void registerConnector(ClientConnector connector)
                {
                    try
                    {
                        super.registerConnector(connector);
                    }
                    catch(RuntimeException error)
                    {
                        errorHelper.errorWithMessage(error, "Failed Connector in Library UI");
                    }
                }
            };
        }

        return tracker;
    }

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
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        layout.addComponent(header);

    }//end addHeader


    /**
     * Method that Vaadin/Spring Boot use to display the page
     * @param view
     * last modified by coalsonc 7/17/17
     */
    @Override
    public void showView(View view)
    {
        try
        {
            LibraryViewDisplay.setContent((Component) view);
        }
        catch(RuntimeException error)
        {
            Notification.show("Notify your administrator of a session ID error");
        }
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
        Accordion accordion = new Accordion();
        accordion.setSizeUndefined();
        accordion.setId("accordion");

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

        checkIn.addClickListener(event ->
        {
            try
            {
                getUI().getNavigator().navigateTo(CheckIn.VIEW_NAME);
            }
            catch (BeanCreationException error)
            {
                errorHelper.genericError(error);
                Notification.show("The session has expired.");
            }
        });
        checkIn.setId("nav_checkIn");
        checkOut.addClickListener(event ->
                {
                    try
                    {
                        getUI().getNavigator().navigateTo(CheckOut.VIEW_NAME);
                    }
                    catch (BeanCreationException error)
                    {
                        errorHelper.genericError(error);
                        Notification.show("The session has expired");
                    }
                });
        checkOut.setId("nav_checkOut");
        home.addClickListener(event -> getUI().getNavigator().navigateTo(DefaultView.VIEW_NAME));
        home.setId("nav_home");

        tab.setId("navigation_tab");
        tab.addComponents(checkIn, checkOut, home);
        return tab;
    }//end addAccordionNavigationButtons


    /**
     * Method used by CreateAccordion to make the admin activity Accordion
     * Creates the admin accordion and the layout to add to the accordion
     * Adds listeners to the buttons for navigation.
     * Adds the buttons to the layout and returns it to the CreateAccordion method
     *
     * last modified by ricky.clevinger 7/18/17
     */
    private Component addAdminAccordion()
    {
        Layout tab          = new VerticalLayout();
        Button addUsers     = new Button("Add User");
        Button viewUsers    = new Button("View Users");
        Button addBooks     = new Button("Add Books");
        Button viewBooks    = new Button("View Books");

        addUsers.addClickListener(event -> getUI().getNavigator().navigateTo(AddUser.VIEW_NAME));
        addUsers.setId("admin_addUsers");
        viewUsers.addClickListener(event -> getUI().getNavigator().navigateTo(AllMembers.VIEW_NAME));
        viewUsers.setId("admin_viewUsers");
        addBooks.addClickListener(event -> getUI().getNavigator().navigateTo(AddBooks.VIEW_NAME));
        addBooks.setId("admin_addBooks");
        viewBooks.addClickListener(event -> getUI().getNavigator().navigateTo(AllBooks.VIEW_NAME));
        viewBooks.setId("admin_viewBooks");

        tab.setId("admin_tab");
        tab.addComponents(addUsers, viewUsers, addBooks,viewBooks);

        return tab;
    }// end addAdminAccordion


    /**
     * Getter for the panel. Used to change sizes depending on the current view.
     * @return LibraryViewDisplay
     *
     * Last modified by ricky.clevinger 7/26/17
     */
    static Panel getLibraryViewDisplay()
    {
        return LibraryViewDisplay;
    }//end getLibraryViewDisplay

    /**
     * Detects the end of a session or a page close and creates a new session if necessary
     *
     * @param event the Detach event
     */
    @Override
    public void detach(DetachEvent event)
    {
        VaadinSession.getCurrent().getSession().isNew();
    }
}
