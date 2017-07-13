package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/12/2017.
 */
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;


import javax.annotation.PostConstruct;

@SpringView(name = All.VIEW_NAME)
public class All extends HorizontalLayout implements View {
    public static final String VIEW_NAME = "All";

    @PostConstruct
    void init() {
        addComponent(new Label("<h2>Title</h2>", ContentMode.HTML));
        addComponent(new Label("<h2>Author</h2>", ContentMode.HTML));
        addComponent(new Label("<h2>Checked In/Out</h2>", ContentMode.HTML));

    }



    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
