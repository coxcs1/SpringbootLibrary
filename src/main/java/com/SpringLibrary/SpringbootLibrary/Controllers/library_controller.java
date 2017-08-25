package com.SpringLibrary.SpringbootLibrary.Controllers;
import Model.Book;
import Model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by crystal.cox on 8/25/2017.
 */
@Controller
public class library_controller {
    /**
     * Variable Declaration
     */
    private RestTemplate restTemplate = new RestTemplate();  // RestTemplate used to make calls to micro-service.
    /**
     * Variable containing url to access backing service
     */
    @Value("${my.bookMemUrl}")
    private String bookMemUrl;
    private List<Book> books;
    private List<User> users;
    private String memberId;
    private String titleId;
    @GetMapping("/")
    public String home1() {
        return "/login";
    }
    @GetMapping("/home")
    public String home() {
        return "/home";
    }
    @GetMapping("/library")
    public String library(Model model) {
        books = Arrays.asList(restTemplate.getForObject(bookMemUrl + "/books/all/", Book[].class));
        users = Arrays.asList(restTemplate.getForObject(bookMemUrl + "/members/all/", User[].class));
        //checkedIn = Arrays.asList(restTemplate.getForObject(bookMemUrl + "/books/checkAndId/1/" + this.titleId, Book[].class));
        model.addAttribute("books", books);
        model.addAttribute("users", users);
        /*
       checkoutModel.addAttribute("checkedIn", checkedIn);
       if (! checkedIn.isEmpty()){
       this.restTemplate.getForObject(bookMemUrl + "/trans/insert/" + this.titleId + "/" + 2 + "/"
       + memberId, String.class);
       this.restTemplate.getForObject(bookMemUrl + "/books/cho/" + this.titleId + "/" + 2 + "/"
       + memberId, String.class);
       getUI().getNavigator().navigateTo(CheckOut.VIEW_NAME);
       }
       else{
       getUI().getNavigator().navigateTo(CheckOut.VIEW_NAME);
       Notification.show("Book has already been checked out.");
       }
       checkoutModel.addAttribute("books",books);
       */

        return "/library";
    }
    @RequestMapping(value = "http://localhost:8060/delete", method = RequestMethod.POST)
    public String handleDeleteBook(@RequestParam(name="bookId")String bookId) {
        System.out.println(bookId);
        System.out.println("test");
        return "/checkout";
    }
    @GetMapping("/about")
    public String about() {
        return "/about";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

}
