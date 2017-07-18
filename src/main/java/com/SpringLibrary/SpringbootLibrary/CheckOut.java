package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/12/2017.
 */
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;

@SpringView(name = CheckOut.VIEW_NAME)
public class CheckOut extends VerticalLayout implements View {
    public static final String VIEW_NAME = "CheckOut";

    @PostConstruct
    void init() {
        addComponent(new Label("Check Out Page"));


        Button Checkin = new Button("Check In");
        Checkin.addStyleName(ValoTheme.BUTTON_SMALL);


        Checkin.addClickListener(event -> getUI().getNavigator().navigateTo("CheckIn"));
        addComponent(Checkin);


    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
