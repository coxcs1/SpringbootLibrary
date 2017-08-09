package com.SpringLibrary.SpringbootLibrary;

import com.nimbusds.jose.JOSEException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import javax.annotation.PostConstruct;
import java.text.ParseException;
import static Resource.gridHelper.authenticate;
import static com.SpringLibrary.SpringbootLibrary.LibraryUI.getLibraryViewDisplay;
import static com.SpringLibrary.SpringbootLibrary.LibraryUI.menuBar;
import static com.vaadin.ui.UI.getCurrent;

/**
 * Created by ricky.clevinger on 7/31/2017.
 *
 * last modified by ricky.clevinger on 7/31/17
 */

@SpringView(name = UserHome.VIEW_NAME)
public class UserHome extends VerticalLayout implements View
{
    public static final String VIEW_NAME = "UserHome";


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
        if (authenticate("User").equals(true)){
            changeUserMenuBar();
            getLibraryViewDisplay().setSizeUndefined();
            getLibraryViewDisplay().setResponsive(true);
            addUser();
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
        Label label = new Label("Welcome User");

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(label);

    }//end addUser


    /**
     * Removes any old items on menubar and adds
     * Admin specific items to it in button
     *
     * last modified by ricky.clevinger 8/9/17
     */
    private void changeUserMenuBar()
    {
        menuBar.removeItems();
        menuBar.addItem("Home", null, (MenuBar.Command) event -> getCurrent().getNavigator().navigateTo(DefaultView.VIEW_NAME));
        menuBar.addItem("View Books", null, (MenuBar.Command) event -> getCurrent().getNavigator().navigateTo(AllBooks.VIEW_NAME));
    }//end changeUserMenuBar


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
