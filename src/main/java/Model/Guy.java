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
    int BirthYear;


    public Guy(String name, int birthYear) {
        Name = name;
        BirthYear = birthYear;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getBirthYear() {
        return BirthYear;
    }

    public void setBirthYear(int birthYear) {
        BirthYear = birthYear;
    }
}
