package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/13/2017.
 */
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;

@SpringView(name = DefaultView.VIEW_NAME)
public class DefaultView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "";

    @PostConstruct
    void init() {

        Button CheckIn = new Button("Check In");
        CheckIn.addStyleName(ValoTheme.BUTTON_LARGE);
        CheckIn.addClickListener(event -> getUI().getNavigator().navigateTo("CheckIn"));
        addComponent(CheckIn);
        setComponentAlignment(CheckIn, Alignment.MIDDLE_CENTER);

        Button CheckOut = new Button("Check Out");
        CheckOut.addStyleName(ValoTheme.BUTTON_LARGE);
        CheckOut.addClickListener(event -> getUI().getNavigator().navigateTo("CheckOut"));
        addComponent(CheckOut);


    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}