package Model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookReturn {

    private String bookId;
    private String bookTitle;
    private String memberId;
    private String memberFName;
    private String memberLName;

    /*
     * Getters and Setters
     */
    public String getBookId(){
        return this.bookId;
    }

    public void setBookId(String bookId){
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberid(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberFName() {
        return memberFName;
    }

    public void setMemberFName(String memberFName) {
        this.memberFName = memberFName;
    }

    public String getMemberLName() {
        return memberLName;
    }

    public void setMemberLName(String memberLName) {
        this.memberLName = memberLName;
    }
}
