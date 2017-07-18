package com.SpringLibrary.SpringbootLibrary;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricky.clevinger on 7/12/2017.
 *
 * last modified by charles.coalson on 7/18/17
 */
@Theme("valo")
@SpringUI
@SpringViewDisplay
public class LibraryUI extends UI implements ViewDisplay {

    private  Panel LibraryViewDisplay;
    private final VerticalLayout layout = new VerticalLayout();


    /**
     * Creates the web page and sets the elements within it
     *
     * Calls a method that sets the base layout for the page
     * Calls a method that adds a Header element
     * Calls method that sets the default panel in the page which will change depending on function
     * Adds an Accordion for navigation
     *
     * last modified by coalsonc 7/18/17
     */
    @Override
    protected void init(VaadinRequest request) {

        setupLayout();
        addHeader();
        addDefaultView();
        createAccordion();

    }//end init

    /**
     * Constructs the default panel that will change based on function selection
     *
     * Creates a new panel and sets the size to be responsive
     * Adds the panel to the base layout and sets its expand ratio
     *
     * last modified by coalsonc 7/17/17
     */

    private void addDefaultView() {
        LibraryViewDisplay = new Panel();
        LibraryViewDisplay.setSizeUndefined();
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
    private void setupLayout(){

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
    private void addHeader() {
        Label header = new Label ("Welcome to the Library");
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();
        layout.addComponent(header);
    }//end addHeader

    /**
     * Method that Vaadin/Spring Boot use to display the page
     *
     * last modified by coalsonc 7/17/17
     */
    @Override
    public void showView(View view) {
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

        Accordion accordion = new Accordion();
        accordion.addStyleName(ValoTheme.ACCORDION_BORDERLESS);
        accordion.setWidth("20%");

        accordion.addTab(addAccordionNavigationButtons(), "Navigation");
        accordion.addTab(addAccordionAdminButtons(), "Admin");
        layout.addComponent(accordion);


    }//end createAccordion

    /**
     * Method used by CreateAccordion to make the navigation buttons
     * Creates the buttons and the layout to pin them to
     * Adds listeners to the buttons to allow navigation
     * Adds buttons to layout and returns it to add to accordion
     *
     * last modified by coalsonc 7/18/17
     */
    private Layout addAccordionNavigationButtons(){

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
     * Method used by CreateAccordion to make the admin activity buttons
     * Creates the buttons and the layout to add to the accordion
     * Adds listeners to the buttons for navigation to the admin pages
     * Adds the buttons to the layout and returns it to the CreateAccordion method
     *
     * last modified by coalsonc 7/17/17
     */
    private Layout addAccordionAdminButtons(){

        Layout tab          = new VerticalLayout();
        Button addUser      = new Button("Add User");
        Button addBook      = new Button("Add Book");
        Button removeUser   = new Button("Remove User");
        Button removeBook   = new Button("Remove Book");

        addUser.addClickListener(event -> getUI().getNavigator().navigateTo("AddUser"));
        addBook.addClickListener(event -> getUI().getNavigator().navigateTo("AddBook"));
        removeUser.addClickListener(event -> getUI().getNavigator().navigateTo("RemoveUser"));
        removeBook.addClickListener(event -> getUI().getNavigator().navigateTo("RemoveBook"));

        tab.addComponents(addBook, addUser, removeBook, removeUser);

        return tab;
    }// end addAccordionAdminButtons
}
