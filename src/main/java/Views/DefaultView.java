package Views;


import Model.Member;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;

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

import static com.SpringLibrary.SpringbootLibrary.LibraryUI.*;

/**
 * Created by ricky.clevinger on 7/13/2017.
 *
 * Modified by ricky.clevinger  7/26/17
 */

@SpringView(name = DefaultView.VIEW_NAME)
public class DefaultView extends VerticalLayout implements View
{
    public static final String VIEW_NAME = ""; // View Name. Default View auto displayed.
    private RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    JWSSigner signer;
    SignedJWT signedJWT;


    // Variable containing url to access backing service
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
        //HorizontalLayout horizontalLayout = addButtons();
        securityFix();
        VerticalLayout layout = addLogin();
        addComponent(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

        Page.getCurrent().setTitle("CGI Library");

    }//end init


    /**
     * Creates the button layout
     * Sets Buttons returned from respective methods
     * Sets spacing for readability/usability
     *
     * @return Horizontal layout containing primary buttons
     * last modified by coalsonc 7/17/17
     */
    private HorizontalLayout addButtons()
    {
        //Create layout and buttons
        HorizontalLayout layout = new HorizontalLayout();
        Button checkIn = addCheckInButton();
        Button checkOut = addCheckOutButton();

        //add buttons to layout and adjust spacing
        layout.addComponent(checkIn);
        layout.setSpacing(true);
        layout.addComponent(checkOut);

        return layout;

    }//end HorizontalLayout


    private VerticalLayout addLogin()
    {

        //Create layout and buttons
        VerticalLayout layout = new VerticalLayout();
        com.vaadin.ui.TextField email   = new com.vaadin.ui.TextField("Email");
        com.vaadin.ui.TextField password   = new com.vaadin.ui.TextField("Password");
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
                    SecretKey secretKey = keyGen.generateKey();

                    // Create HMAC signer
                    signer = new MACSigner(secretKey.getEncoded());

                    // Prepare JWT with claims set
                    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                            .subject("alice")
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



                    /////////////////////////////////////////////////////////////////
                    //The Parsing part. Helper function eventually.


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


    /**
     * Creates Check In button
     * Sets button Theme
     * Adds listener and points it to the Check In View
     *
     * @return the completed Check In button
     * last modified by coalsonc 7/17/17
     */
    private Button addCheckInButton()
    {
        Button checkIn = new Button("Check In");
        checkIn.addStyleName(ValoTheme.BUTTON_LARGE);
        checkIn.setId("button_checkIn");

        checkIn.addClickListener(event ->
        {
            getLibraryViewDisplay().setSizeFull();
            getUI().getNavigator().navigateTo("CheckIn");
        });

        return checkIn;

    }//end addCheckInButton

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
     * Creates Check Out button
     * Sets button Theme
     * Adds listener and points it to the Check Out View
     *
     * @return the completed Check Out button
     * last modified by coalsonc 7/17/17
     */
    private Button addCheckOutButton()
    {
        Button checkOut = new Button("Check Out");
        checkOut.setId("button_checkOut");
        checkOut.addStyleName(ValoTheme.BUTTON_LARGE);

        checkOut.addClickListener(event ->
        {
            getLibraryViewDisplay().setSizeFull();
            getUI().getNavigator().navigateTo("CheckOut");
        });

        return checkOut;

    }//end addCheckOutButton


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