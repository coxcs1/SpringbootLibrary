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

        //rickySetupLayout();

        setupPrimaryPanel();
        addFilters();
        setupGridPanel();
        //addGrid();
        addCheckInButton();

    }

    private void addFilters() {

        HorizontalLayout filterPanel = new HorizontalLayout();

        TextField titleFilter = new TextField();
        titleFilter.setWidth(100, Unit.PERCENTAGE);
        titleFilter.setPlaceholder("Book Title...");
        //titleFilter.addValueChangeListener(this::titleFilterGridChange);
        filterPanel.addComponent(titleFilter);

        TextField authorFilter = new TextField();
        authorFilter.setWidth(100, Unit.PERCENTAGE);
        authorFilter.setPlaceholder("Last Name...");
        //authorFilter.addValueChangeListener(this::authorFilterGridChange);
        filterPanel.addComponent(authorFilter);

        primaryPanel.addComponent(filterPanel);
    }

    private void addCheckInButton() {
        VerticalLayout holdsButton = new VerticalLayout();
        Button checkIn = new Button ("Check In");
        holdsButton.addComponent(checkIn);
        addComponent(holdsButton);
    }

    private void setupGridPanel() {

        gridPanel = new HorizontalLayout();
        Grid<BookReturn> bookReturnGrid = new Grid<>();
        bookReturnGrid.setWidth("100%");
        gridPanel.addComponent(bookReturnGrid);
        primaryPanel.addComponent(gridPanel);
    }

    private void setupPrimaryPanel() {

        primaryPanel = new VerticalLayout();
        primaryPanel.setSpacing(true);

        addComponent(primaryPanel);
    }

    private void rickySetupLayout() {


        addComponent(new Label("Check In"));


        Button CheckOut = new Button("Check Out");
        CheckOut.addStyleName(ValoTheme.BUTTON_SMALL);
        CheckOut.addClickListener(event -> getUI().getNavigator().navigateTo("CheckOut"));
        addComponent(CheckOut);

        RestTemplate restTemplate = new RestTemplate();
        Member[] members = restTemplate.getForObject("http://localhost:8091/members/all", Member[].class);

        TextArea area = new TextArea();
        area.setValue("First name is "+members[0].getFName() + " and second is "+members[1].getFName());

        addComponent(area);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
