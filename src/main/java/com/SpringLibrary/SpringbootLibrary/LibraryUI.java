package com.SpringLibrary.SpringbootLibrary;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricky.clevinger on 7/12/2017.
 */
@Theme("valo")
@SpringUI
@SpringViewDisplay
public class LibraryUI extends UI implements ViewDisplay {

    private  Panel LibraryViewDisplay;

    @Override
    protected void init(VaadinRequest request) {
        final  HorizontalLayout root = new HorizontalLayout();
        root.setSizeFull();
        setContent(root);

        final GridLayout navigationBar = new GridLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        List<String> inventory = new ArrayList<>();
        inventory.add("All");
        inventory.add("CheckIn");
        inventory.add("CheckOut");

        ComboBox<String> selectInv =
                new ComboBox<>();
        selectInv.setItems(inventory);
        selectInv.setTextInputAllowed(false);
        selectInv.setPlaceholder("Inventory");
        navigationBar.addComponent(selectInv);
        root.addComponent(navigationBar);

        navigationBar.addComponent(createNavigationButton("Check In",
                CheckIn.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Check Out",
                CheckOut.VIEW_NAME));

        root.setComponentAlignment(navigationBar, Alignment.MIDDLE_CENTER);


        selectInv.addValueChangeListener(event -> getUI().getNavigator().navigateTo(event.getValue()));


        LibraryViewDisplay = new Panel();
        LibraryViewDisplay.setSizeFull();
        root.addComponent(LibraryViewDisplay);
        root.setExpandRatio(LibraryViewDisplay, 1.0f);


    }

    private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }

    @Override
    public void showView(View view) {
        LibraryViewDisplay.setContent((Component) view);
    }
}
