package com.SpringLibrary.SpringbootLibrary;


import Model.Member;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import static com.SpringLibrary.SpringbootLibrary.LibraryUI.getLibraryViewDisplay;
import static com.SpringLibrary.SpringbootLibrary.LibraryUI.menuBar;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ricky.clevinger on 7/13/2017.
 *
 * Modified by ricky.clevinger  7/26/17
 */

@SpringView(name = DefaultView.VIEW_NAME)
public class DefaultView extends VerticalLayout implements View
{
    static final String VIEW_NAME = ""; // View Name. Default View auto displayed.
    private RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    JWSSigner signer;
    public static SignedJWT signedJWT;
    public static JWEObject jweObject;
    public static String jweString;
    public static SecretKey secretKey;


    /**
     * Variable containing url to access backing service
     */
    @Value("${my.bookMemUrl}")
    private String bookMemUrl;

    /**
     * Re-sizes the panel
     * Constructs the Default view for display
     * Adds Horizontal layout containing buttons
     * Adjusts alignment/spacing
     *
     * last modified by coalsonc 7/26/17
     */
    @PostConstruct
    @SuppressWarnings("unused")
    void init()
    {
        getLibraryViewDisplay().setSizeUndefined();
        setSpacing(true);
        securityFix();
        VerticalLayout layout = addLogin();
        addComponent(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

        Page.getCurrent().setTitle("CGI Library");

    }//end init


    private VerticalLayout addLogin()
    {

        //Create layout and buttons
        VerticalLayout layout = new VerticalLayout();
        TextField email   = new TextField("Email");
        TextField password   = new TextField("Password");
        Button login                  = new Button("Login");

        email.setId("search_email");
        password.setId("search_password");
        login.setId("button_login");

        login.addClickListener(event -> {

            List<Member> user = Arrays.asList(restTemplate.getForObject(bookMemUrl + "/members/login/" + email.getValue() +
                    "/" + password.getValue(), Member[].class));

            if (! user.isEmpty()){

                email.setValue("");
                password.setValue("");
                menuBar.setVisible(true);
                // Generate 256-bit AES key for HMAC as well as encryption
                KeyGenerator keyGen = null;
                try {
                    keyGen = KeyGenerator.getInstance("AES");

                    keyGen.init(256);
                    secretKey = keyGen.generateKey();

                    // Create HMAC signer
                    signer = new MACSigner(secretKey.getEncoded());


                    String role;

                    if (Integer.parseInt(user.get(0).getRole()) == 1){
                        role = "User";
                    }
                    else if (Integer.parseInt(user.get(0).getRole()) == 2){
                        role = "Librarian";
                    }
                    else {
                        role = "Admin";
                    }

                    // Prepare JWT with claims set
                    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                            .subject(role)
                            .expirationTime(new Date())
                            .claim("LibraryApp", true)
                            .build();

                    signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

                    // Apply the HMAC
                    signedJWT.sign(signer);


                    // Create JWE object with signed JWT as payload
                    jweObject = new JWEObject(
                            new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
                                    .contentType("JWT") // required to signal nested JWT
                                    .build(),
                            new Payload(signedJWT));

                    // Perform encryption
                    jweObject.encrypt(new DirectEncrypter(secretKey.getEncoded()));

                    // Serialise to JWE compact form
                    jweString = jweObject.serialize();


                    if (Integer.parseInt(user.get(0).getRole()) == 1){
                        getUI().getNavigator().navigateTo(UserHome.VIEW_NAME);
                    }
                    else if (Integer.parseInt(user.get(0).getRole()) == 2){
                        getUI().getNavigator().navigateTo(LibrarianHome.VIEW_NAME);
                    }
                    else {
                        getUI().getNavigator().navigateTo(AdminHome.VIEW_NAME);
                    }

                    // Parse the JWE string
                    jweObject = JWEObject.parse(jweString);

                    // Decrypt with shared key
                    jweObject.decrypt(new DirectDecrypter(secretKey.getEncoded()));

                    // Extract payload
                    signedJWT = jweObject.getPayload().toSignedJWT();

                    System.out.println(signedJWT.getJWTClaimsSet().getSubject());

                } catch (ParseException | NoSuchAlgorithmException | JOSEException e) {
                    e.printStackTrace();
                }
            }});

        //add buttons to layout and adjust spacing
        layout.addComponents(email,password,login);
        layout.setSpacing(true);
        return layout;
    }//end HorizontalLayout



    private void securityFix()
    {
        Field field = null;
        try {
            field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");

            field.setAccessible(true);

            Field modifiersField = null;

            modifiersField = Field.class.getDeclaredField("modifiers");

            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, false);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }//end addCheckInButton


    /**
     * Sets a listener that automatically changes the default view when a selection is made
     * @param event on view change
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event)
    {
        // This view is constructed in the init() method()
    }//end

}