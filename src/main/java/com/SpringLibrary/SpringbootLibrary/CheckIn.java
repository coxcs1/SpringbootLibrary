package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/13/2017.
 */
import Model.Book;
import Model.Member;
import Resource.gridHelper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.TextRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static Resource.gridHelper.createLayout;
import static Resource.gridHelper.titleFilterGridChange;

@SpringView(name = CheckIn.VIEW_NAME)
public class CheckIn extends VerticalLayout implements View {
    public static final String VIEW_NAME = "CheckIn";

    HorizontalLayout hLayout;
    Grid<Book> bookReturnGrid;
    String titleId;  // Id used to determine which item is selected in the grid.
    String memberId;  // Id used to determine which item is selected in the grid.
    private TextField titleFilter;   // TextField will be used to filter the results on the grid.
    private HorizontalLayout    gridPanel;
    RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    List<Book> books; // Used to store data retrieved from micro-service. Placed into the grid.

    @Value("${my.bookUrl}")
    private String bookUrl;


    @PostConstruct
    void init() {

        createLayout(this);
        addFilters();
        setupGridPanel();
        addCheckInButton();
    }

    private void addFilters() {

        titleFilter = new TextField();
        titleFilter.setWidth(100, Unit.PERCENTAGE);
        titleFilter.setPlaceholder("Title...");

        titleFilter.addValueChangeListener(event -> {

            try
            {
                titleFilterGridChange(event, bookReturnGrid);
            }
            catch (NullPointerException error)
            {
                titleFilter.setValue("");
                Notification.show("Service unavailable, please try again in a few minutes");

            }
        });

        addComponent(titleFilter);

    }

    private void addCheckInButton() {
        VerticalLayout holdsButton = new VerticalLayout();
        Button checkIn = new Button ("Check In");
        checkIn.addClickListener(event -> {

            try {
                this.restTemplate.getForObject(bookUrl + "/trans/insert/" + this.titleId + "/" + 1 + "/" + memberId, String.class);
                this.restTemplate.getForObject(bookUrl + "/books/cho/" + this.titleId + "/" + 1 + "/" + 0, String.class);
                getUI().getNavigator().navigateTo(CheckIn.VIEW_NAME);
            }
            catch (ResourceAccessException error)
            {

                Notification.show("Service unavailable, please try again in a few minutes");

            }

        });
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
            if (event.getAllSelectedItems().isEmpty()){
                this.titleId = 0 + "";
                this.memberId = 0 + "";}
            else {
                this.titleId = event.getFirstSelectedItem().get().getBookId() + "";
                this.memberId = event.getFirstSelectedItem().get().getMid() + "";
            }
        });
        bookReturnGrid.setItems(books);
        //Specifies what parts of the objects in the grid are shown.
        bookReturnGrid.addColumn(Book::getTitle, new TextRenderer()).setCaption("Title");
        bookReturnGrid.addColumn(Book ->
                Arrays.asList(restTemplate.getForObject(bookUrl + "/members/id/"
                        + Book.getMid(), Member[].class)).get(0).getFName() + " "
                        + Arrays.asList(restTemplate.getForObject(bookUrl + "/members/id/"
                        + Book.getMid(), Member[].class)).get(0).getLName()).setCaption("Checked Out By");
        bookReturnGrid.addColumn(Book -> gridHelper.overdue(Book.getOutDate(), new Date(System.currentTimeMillis()))).setCaption("Due Date");

        bookReturnGrid.setWidth(100, Unit.PERCENTAGE);

        gridPanel.addComponent(bookReturnGrid);
        gridPanel.setResponsive(true);
        addComponent(gridPanel);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
