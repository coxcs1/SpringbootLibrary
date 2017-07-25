package Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ricky.clevinger on 7/17/2017.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private String bookId;
    private String title;
    private String authFName;
    private String authLName;
    private String libId;
    private String check;
    private String mid;

    public Book() {
    }

    //Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthFName() {
        return authFName;
    }

    public void setAuthFName(String fname) {
        this.authFName = fname;
    }

    public String getAuthLName() {
        return authLName;
    }

    public void setAuthLName(String lname) {
        this.authLName = lname;
    }

    public String getLibId() {
        return libId;
    }

    public void setLibId(String libId) {
        this.libId = libId;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}