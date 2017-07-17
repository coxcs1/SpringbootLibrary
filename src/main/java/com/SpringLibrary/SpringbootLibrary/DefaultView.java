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

        HorizontalLayout horizontalLayout = addButtons();

        addComponent(horizontalLayout);
        setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }

    private HorizontalLayout addButtons(){
        HorizontalLayout layout = new HorizontalLayout();
        Button checkIn = addCheckInButton();
        Button checkOut = addCheckOutButton();

        layout.addComponent(checkIn);
        layout.setSpacing(true);
        layout.addComponent(checkOut);

        return layout;
    }

    private Button addCheckInButton(){

        Button CheckIn = new Button("Check In");
        CheckIn.addStyleName(ValoTheme.BUTTON_LARGE);
        CheckIn.addClickListener(event -> getUI().getNavigator().navigateTo("CheckIn"));

        return CheckIn;
    }

    private Button addCheckOutButton(){

        Button checkOut = new Button("Check Out");
        checkOut.addStyleName(ValoTheme.BUTTON_LARGE);
        checkOut.addClickListener(event -> getUI().getNavigator().navigateTo("CheckOut"));

        return checkOut;
    }

}