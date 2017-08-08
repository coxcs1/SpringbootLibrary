package com.SpringLibrary.SpringbootLibrary;

import Resource.LibraryErrorHelper;
import com.nimbusds.jose.JWEObject;
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
    public static JWEObject jweObject;
    private static  Panel LibraryViewDisplay; // Panel used to display the views (pages).
    private VerticalLayout layout = new VerticalLayout(); // Layout to place to the components (Header, Panel, etcetera).
    private LibraryErrorHelper errorHelper = new LibraryErrorHelper(); // Instantiates LibraryErrorHelper
    private ConnectorTracker tracker; // Connection Tracker


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
            addMenu();
            addDefaultView();
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

    private void addMenu() {
        MenuBar menuBar = new MenuBar();
        menuBar.setSizeUndefined();
        menuBar.addItem("Home",null, (MenuBar.Command) event -> getUI().getNavigator().navigateTo(DefaultView.VIEW_NAME));
        menuBar.addItem("Check In",null,(MenuBar.Command) event ->
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
        menuBar.addItem("Check Out",null, (MenuBar.Command) event ->
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
        }
        );
        MenuBar.MenuItem Admin = menuBar.addItem("Admin", null, null);
        Admin.addItem("Add User", null, (MenuBar.Command) event -> getUI().getNavigator().navigateTo(AddUser.VIEW_NAME));
        Admin.addItem("View Users", null, (MenuBar.Command) event -> getUI().getNavigator().navigateTo(AllMembers.VIEW_NAME));
        Admin.addItem("Add Book", null, (MenuBar.Command) event -> getUI().getNavigator().navigateTo(AddBooks.VIEW_NAME));
        Admin.addItem("View Books", null, (MenuBar.Command) event -> getUI().getNavigator().navigateTo(AllBooks.VIEW_NAME));

        layout.addComponent(menuBar);
    }


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
     * @param event session close
     */
    @Override
    public void detach(DetachEvent event)
    {
        VaadinSession.getCurrent().getSession().isNew();
    }
}
