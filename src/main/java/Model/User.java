package Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ricky.clevinger on 7/13/2017.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String id;
    private String FName;
    private String LName;


    //Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }
}
