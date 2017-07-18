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
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

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
        accordion.addTab(addAdminAccordion(), "Admin");
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
     * Method used by CreateAccordion to make the admin activity Accordion
     * Creates the admin accordion and the layout to add to the accordion
     * Adds listeners to the buttons for admin input.
     * Adds the inputs and buttons to the layout and returns it to the CreateAccordion method
     *
     * last modified by ricky.clevinger 7/18/17
     */
    private Component addAdminAccordion(){

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
     * last modified by ricky.clevinger 7/18/17
     */
    private Layout addUserInput(){

        Layout tab      = new VerticalLayout();
        com.vaadin.ui.TextField fName  = new com.vaadin.ui.TextField("First Name");
        com.vaadin.ui.TextField lName  = new com.vaadin.ui.TextField("Last Name");
        Button submit  = new Button("Submit");

      //  checkIn.addClickListener(event -> getUI().getNavigator().navigateTo(CheckIn.VIEW_NAME));
      //  checkOut.addClickListener(event -> getUI().getNavigator().navigateTo(CheckOut.VIEW_NAME));
      //  home.addClickListener(event -> getUI().getNavigator().navigateTo(DefaultView.VIEW_NAME));

        tab.addComponents(fName,lName, submit);
        return tab;
    }//end addUserInput


    /**
     * Method used by AddAdminAccordion to create the add book functionality
     * Creates the textFields, button, and the layout to pin them to
     * Adds listeners to the buttons to allow navigation
     * Adds textFields and a button to layout and returns it to add to accordion for add book
     *
     * last modified by ricky.clevinger 7/18/17
     */
    private Layout addBookInput(){

        Layout tab      = new VerticalLayout();
        com.vaadin.ui.TextField title  = new com.vaadin.ui.TextField("Title");
        com.vaadin.ui.TextField fName  = new com.vaadin.ui.TextField("Author: First Name");
        com.vaadin.ui.TextField lName  = new com.vaadin.ui.TextField("Author: Last Name");
        Button submit  = new Button("Submit");

        //  checkIn.addClickListener(event -> getUI().getNavigator().navigateTo(CheckIn.VIEW_NAME));
        //  checkOut.addClickListener(event -> getUI().getNavigator().navigateTo(CheckOut.VIEW_NAME));
        //  home.addClickListener(event -> getUI().getNavigator().navigateTo(DefaultView.VIEW_NAME));

        tab.addComponents(title,fName,lName, submit);
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
    private Layout removeUser(){

        Layout tab  = new VerticalLayout();
        Button all  = new Button("AllBooks Users");

      //    all.addClickListener(event -> getUI().getNavigator().navigateTo(AllBooks.VIEW_NAME));
        //  checkOut.addClickListener(event -> getUI().getNavigator().navigateTo(CheckOut.VIEW_NAME));
        //  home.addClickListener(event -> getUI().getNavigator().navigateTo(DefaultView.VIEW_NAME));

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
    private Layout removeBook(){

        Layout tab  = new VerticalLayout();
        Button all  = new Button("All Books");

          all.addClickListener(event -> getUI().getNavigator().navigateTo(AllBooks.VIEW_NAME));
        //  checkOut.addClickListener(event -> getUI().getNavigator().navigateTo(CheckOut.VIEW_NAME));
        //  home.addClickListener(event -> getUI().getNavigator().navigateTo(DefaultView.VIEW_NAME));

        tab.addComponents(all);
        return tab;
    }//end removeBook
}
