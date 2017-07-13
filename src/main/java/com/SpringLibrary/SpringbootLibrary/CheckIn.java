package com.SpringLibrary.SpringbootLibrary;

/**
 * Created by ricky.clevinger on 7/13/2017.
 */
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

    @PostConstruct
    void init() {
        addComponent(new Label("Check In"));


        Button CheckOut = new Button("Check Out");
        CheckOut.addStyleName(ValoTheme.BUTTON_SMALL);
        CheckOut.addClickListener(event -> getUI().getNavigator().navigateTo("CheckOut"));
        addComponent(CheckOut);

        RestTemplate restTemplate = new RestTemplate();
        Member[] members = restTemplate.getForObject("http://localhost:8090/members/all", Member[].class);

        TextArea area = new TextArea();
        area.setValue("First name is "+members[0].getFName() + " and second is "+members[1].getFName());

        addComponent(area);
    }




    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
