package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/12/2017.
 */
import Model.Book;
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

@SpringView(name = CheckOut.VIEW_NAME)
public class CheckOut extends VerticalLayout implements View {
    public static final String VIEW_NAME = "CheckOut";

    HorizontalLayout hLayout;
    Button checkOut;

    @Value("${my.bookUrl}")
    private String bookUrl;


    @PostConstruct
    void init() {

        createLayout();
        createBookGrid();
        createMemberGrid();
        addCheckOutButton();


    }

    private void addCheckOutButton() {

        VerticalLayout holdsButton = new VerticalLayout();
        checkOut = new Button ("Check Out");
        holdsButton.addComponent(checkOut);
        addComponent(holdsButton);

    }
    private void createMemberGrid() {

        Grid<Member> memberGrid = new Grid<>();
        memberGrid.setWidth(100, Unit.PERCENTAGE);
        hLayout.addComponent(memberGrid);
    }

    private void createBookGrid() {

        Grid<Book> bookGrid = new Grid<>();
        TextField authorLastNameFilter;
        TextField bookTitleField;
        String id;
        RestTemplate microServiceConnection = new RestTemplate();
        List<Book> bookList;


        //bookList = Arrays.asList(microServiceConnection.getForObject(bookUrl + "/books/all", Book[].class));
        /*bookGrid.addSelectionListener(event -> {
            this.id = event.getFirstSelectedItem().get().getBookId() + "";
        });*/
        bookGrid.setWidth(100, Unit.PERCENTAGE);
        //bookGrid.setItems(bookList);
        /*bookGrid.addColumn(Book::getTitle, new TextRenderer()).setCaption("Title");
        bookGrid.addColumn(Book ->
                Book.getAuthFName() + " " + Book.getAuthLName()).setCaption("Author");*/
        hLayout.addComponent(bookGrid);

    }

    private void createLayout() {

        hLayout = new HorizontalLayout();
        hLayout.setSpacing(true);
        addComponent(hLayout);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
