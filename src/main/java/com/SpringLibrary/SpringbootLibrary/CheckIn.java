package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/13/2017.
 */
import Model.Person;
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

    @PostConstruct
    void init() {
        addComponent(new Label("Check In"));


        Button CheckOut = new Button("Check Out");
        CheckOut.addStyleName(ValoTheme.BUTTON_SMALL);
        CheckOut.addClickListener(event -> getUI().getNavigator().navigateTo("CheckOut"));
        addComponent(CheckOut);

        RestTemplate restTemplate = new RestTemplate();
        Person[] persons = restTemplate.getForObject("http://localhost:8090/persons/all", Person[].class);

        TextArea people = new TextArea();
        people.setValue("First name is "+persons[0].getFirstName() + " and second is "+persons[1].getFirstName());

        addComponent(people);
    }




    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
