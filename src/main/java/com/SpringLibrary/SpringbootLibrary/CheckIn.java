package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/13/2017.
 */
import Model.BookReturn;
import Model.Member;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@SpringView(name = CheckIn.VIEW_NAME)
public class CheckIn extends VerticalLayout implements View {
    public static final String VIEW_NAME = "CheckIn";

    private VerticalLayout      primaryPanel;
    private HorizontalLayout    gridPanel;


    @PostConstruct
    void init() {

        setupPrimaryPanel();
        addFilters();
        setupGridPanel();
        addCheckInButton();

    }

    private void addFilters() {

        HorizontalLayout filterPanel = new HorizontalLayout();

        TextField titleFilter = new TextField();
        titleFilter.setWidth(100, Unit.PERCENTAGE);
        titleFilter.setPlaceholder("Book Title...");
        titleFilter.setResponsive(true);
        //titleFilter.addValueChangeListener(this::titleFilterGridChange);
        filterPanel.addComponent(titleFilter);

        filterPanel.setSpacing(true);

        TextField authorFilter = new TextField();
        authorFilter.setWidth(100, Unit.PERCENTAGE);
        authorFilter.setPlaceholder("Last Name...");
        authorFilter.setResponsive(true);
        //authorFilter.addValueChangeListener(this::authorFilterGridChange);
        filterPanel.addComponent(authorFilter);

        primaryPanel.addComponent(filterPanel);
    }

    private void addCheckInButton() {
        VerticalLayout holdsButton = new VerticalLayout();
        Button checkIn = new Button ("Check In");
        checkIn.setResponsive(true);
        holdsButton.addComponent(checkIn);
        holdsButton.setResponsive(true);
        addComponent(holdsButton);
    }

    private void setupGridPanel() {

        gridPanel = new HorizontalLayout();
        Grid<BookReturn> bookReturnGrid = new Grid<>();
        bookReturnGrid.setWidth("100%");
        gridPanel.addComponent(bookReturnGrid);
        gridPanel.setResponsive(true);
        primaryPanel.addComponent(gridPanel);
    }

    private void setupPrimaryPanel() {

        primaryPanel = new VerticalLayout();
        primaryPanel.setSpacing(true);
        primaryPanel.setResponsive(true);

        addComponent(primaryPanel);
    }




    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
