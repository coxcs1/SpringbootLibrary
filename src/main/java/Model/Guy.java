package Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by ricky.clevinger on 7/11/2017.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Guy {
    String Name;
    String BirthYear;


    public Guy(String name, String birthYear) {
        Name = name;
        BirthYear = birthYear;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirthYear() {
        return BirthYear;
    }

    public void setBirthYear(String birthYear) {
        BirthYear = birthYear;
    }
}
