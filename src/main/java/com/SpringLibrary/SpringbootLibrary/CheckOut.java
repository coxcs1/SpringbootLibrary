package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/12/2017.
 */
import Model.Book;
import Model.Member;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
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

@SpringView(name = CheckOut.VIEW_NAME)
public class CheckOut extends VerticalLayout implements View {
    public static final String VIEW_NAME = "CheckOut";

    HorizontalLayout hLayout;
    Button checkOut;
    private TextField titleFilter;   // TextField will be used to filter the results on the grid.
    private TextField authorFilter;   // TextField will be used to filter the results on the grid.
    String titleId;  // Id used to determine which item is selected in the grid.
    String memberId;  // Id used to determine which item is selected in the grid.
    RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    List<Book> books; // Used to store data retrieved from micro-service. Placed into the grid.
    List<Member> members; // Used to store data retrieved from micro-service. Placed into the grid.
    Grid<Member> memberGrid;
    Grid<Book> bookGrid;

    @Value("${my.bookUrl}")
    private String bookUrl;

    @Value("${my.memberUrl}")
    private String memUrl;

    @PostConstruct
    void init() {

        createLayout();
        createFilter();
        createBookGrid();
        createMemberGrid();
        addCheckOutButton();
    }

    private void addCheckOutButton() {

        checkOut = new Button ("Check Out");

        checkOut.addClickListener(event -> {
            this.restTemplate.getForObject(bookUrl + "/trans/insert/" + this.titleId + "/" + 2 + "/" + memberId, String.class);
            this.restTemplate.getForObject(bookUrl + "/books/cho/" + this.titleId + "/" + 2 + "/" + memberId, String.class);
            getUI().getNavigator().navigateTo(CheckOut.VIEW_NAME);
        });

        addComponent(checkOut);

    }
    private void createMemberGrid() {

        members = Arrays.asList(restTemplate.getForObject(memUrl + "/members/all", Member[].class));
        memberGrid = new Grid<>();
        memberGrid.setWidth(100, Unit.PERCENTAGE);

        // Retrieves the data from the book micro-service.


        // Create a grid and adds listener to record selected item.
        memberGrid = new Grid<>();
        memberGrid.addSelectionListener(event -> {
            this.memberId = event.getFirstSelectedItem().get().getId() + "";
        });

        // Sets the width of the grid.
        memberGrid.setWidth(100, Unit.PERCENTAGE);
        // Sets list to the grid
        memberGrid.setItems(members);
        //Specifies what parts of the objects in the grid are shown.
        memberGrid.addColumn(Member::getFName, new TextRenderer()).setCaption("First Name");
        memberGrid.addColumn(Member::getLName, new TextRenderer()).setCaption("Last Name");

        hLayout.addComponent(memberGrid);
    }

    private void createBookGrid() {

        books = Arrays.asList(restTemplate.getForObject(bookUrl + "/books/check/1", Book[].class));
        bookGrid = new Grid<>();

        bookGrid.addSelectionListener(event -> {
            this.titleId = event.getFirstSelectedItem().get().getBookId() + "";
        });

        // Sets the width of the grid.
        bookGrid.setWidth(100, Unit.PERCENTAGE);
        // Sets list to the grid
        bookGrid.setItems(books);
        //Specifies what parts of the objects in the grid are shown.
        bookGrid.addColumn(Book::getTitle, new TextRenderer()).setCaption("Title");
        bookGrid.addColumn(Book ->
                Book.getAuthFName() + " " + Book.getAuthLName()).setCaption("Author");

        bookGrid.setWidth(100, Unit.PERCENTAGE);

        hLayout.addComponent(bookGrid);

    }

    private void createLayout() {

        hLayout = new HorizontalLayout();
        hLayout.setSpacing(true);
        addComponent(hLayout);

    }

    /**
     * Function shall add the search filter to the page.
     * User shall type in part of a book title and the grid will changed accordingly.
     *
     * last modified by ricky.clevinger 7/19/17
     */
    public void createFilter() {
        titleFilter = new TextField();
        titleFilter.setWidth(100, Unit.PERCENTAGE);
        titleFilter.setPlaceholder("Title...");
        titleFilter.addValueChangeListener(this::titleFilterGridChange);
        addComponent(titleFilter);

        authorFilter = new TextField();
        authorFilter.setWidth(100, Unit.PERCENTAGE);
        authorFilter.setPlaceholder("Last Name...");
        authorFilter.addValueChangeListener(this::lNameFilterGridChange);
        addComponent(authorFilter);
    }//end createFilter

    /**
     * Helper function for the createFilter.
     * Changes the grid and compares the titles.
     * @param event
     * last modified by ricky.clevinger 7/19/17
     */
    private void titleFilterGridChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Book> dataProvider = (ListDataProvider<Book>) bookGrid.getDataProvider();
        dataProvider.setFilter(Book::getTitle, s -> caseInsensitiveContains(s, event.getValue()));
    }//end fNameFilterGridChange

    /**
     * Helper function for the createFilter.
     * Changes the grid and compares the titles.
     * @param event
     * last modified by ricky.clevinger 7/19/17
     */
    private void lNameFilterGridChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Member> dataProvider = (ListDataProvider<Member>) memberGrid.getDataProvider();
        dataProvider.setFilter(Member::getLName, s -> caseInsensitiveContains(s, event.getValue()));
    }//end lNameFilterGridChange

    /**
     *Returns a boolean telling if the lowercase form of text input into the filter is contain
     * by any of the lowercase versions of the book titles.
     * @param where the books titles its comparing to
     * @param what  the filter wood being compared to the book titles
     * @return Boolean telling if the lower case value of the filter input and the book titles match
     *
     * last modified by ricky.clevinger 7/19/17
     */
    private Boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
    }//end caseInsensitiveContains

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
