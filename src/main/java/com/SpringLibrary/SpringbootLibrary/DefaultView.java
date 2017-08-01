package com.SpringLibrary.SpringbootLibrary;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import static com.SpringLibrary.SpringbootLibrary.LibraryUI.getLibraryViewDisplay;

/**
 * Created by ricky.clevinger on 7/13/2017.
 *
 * Modified by ricky.clevinger  7/26/17
 */

@SpringView(name = DefaultView.VIEW_NAME)
public class DefaultView extends VerticalLayout implements View
{
    static final String VIEW_NAME = "";

    /**
     * Re-sizes the panel
     * Constructs the Default view for display
     * Adds Horizontal layout containing buttons
     * Adjusts alignment/spacing
     *
     * last modified by coalsonc 7/26/17
     */
    @PostConstruct
    @SuppressWarnings("unused")
    void init()
    {
        getLibraryViewDisplay().setSizeUndefined();
        setSpacing(true);
        HorizontalLayout horizontalLayout = addButtons();
        addComponent(horizontalLayout);
        setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        Page.getCurrent().setTitle("CGI Library");

    }//end init


    /**
     * Creates the button layout
     * Sets Buttons returned from respective methods
     * Sets spacing for readability/usability
     *
     * @return Horizontal layout containing primary buttons
     * last modified by coalsonc 7/17/17
     */
    private HorizontalLayout addButtons()
    {
        //Create layout and buttons
        HorizontalLayout layout = new HorizontalLayout();
        Button checkIn = addCheckInButton();
        Button checkOut = addCheckOutButton();

        //add buttons to layout and adjust spacing
        layout.addComponent(checkIn);
        layout.setSpacing(true);
        layout.addComponent(checkOut);

        return layout;

    }//end HorizontalLayout


    /**
     * Creates Check In button
     * Sets button Theme
     * Adds listener and points it to the Check In View
     *
     * @return the completed Check In button
     * last modified by coalsonc 7/17/17
     */
    private Button addCheckInButton()
    {
        Button checkIn = new Button("Check In");
        checkIn.addStyleName(ValoTheme.BUTTON_LARGE);
        checkIn.setId("button_checkIn");

        checkIn.addClickListener(event ->
        {
            getLibraryViewDisplay().setSizeFull();
            getUI().getNavigator().navigateTo("CheckIn");
        });

        return checkIn;

    }//end addCheckInButton


    /**
     * Creates Check Out button
     * Sets button Theme
     * Adds listener and points it to the Check Out View
     *
     * @return the completed Check Out button
     * last modified by coalsonc 7/17/17
     */
    private Button addCheckOutButton()
    {
        Button checkOut = new Button("Check Out");
        checkOut.setId("button_checkOut");
        checkOut.addStyleName(ValoTheme.BUTTON_LARGE);

        checkOut.addClickListener(event ->
        {
            getLibraryViewDisplay().setSizeFull();
            getUI().getNavigator().navigateTo("CheckOut");
        });

        return checkOut;

    }//end addCheckOutButton


    /**
     * Sets a listener that automatically changes the default view when a selection is made
     * @param event on view change
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event)
    {
        // This view is constructed in the init() method()
    }//end

}