package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/13/2017.
 */
import Model.Book;
import Model.BookReturn;
import Model.Member;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.TextRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@SpringView(name = CheckIn.VIEW_NAME)
public class CheckIn extends VerticalLayout implements View {
    public static final String VIEW_NAME = "CheckIn";

    Grid<Book> bookReturnGrid;
    String titleId;  // Id used to determine which item is selected in the grid.
    private VerticalLayout      primaryPanel;
    private HorizontalLayout    gridPanel;
    RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    List<Book> books; // Used to store data retrieved from micro-service. Placed into the grid.
    List<Member> members; // Used to store data retrieved from micro-service. Placed into the grid.

    @Value("${my.bookUrl}")
    private String bookUrl;

    @Value("${my.memberUrl}")
    private String memUrl;

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

        books = Arrays.asList(restTemplate.getForObject(bookUrl + "/books/check/2", Book[].class));

        gridPanel = new HorizontalLayout();
        bookReturnGrid = new Grid<>();
        bookReturnGrid.addSelectionListener(event -> {
            this.titleId = event.getFirstSelectedItem().get().getBookId() + "";
        });
        bookReturnGrid.setItems(books);
        //Specifies what parts of the objects in the grid are shown.
        bookReturnGrid.addColumn(Book::getTitle, new TextRenderer()).setCaption("Title");
        bookReturnGrid.addColumn(Book ->
                " " + Arrays.asList(restTemplate.getForObject(memUrl + "/members/id/"
                        + Book.getMid(), Member[].class)).get(0).getFName()).setCaption("Member");

        bookReturnGrid.setWidth(100, Unit.PERCENTAGE);

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
