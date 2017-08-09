package Resource;

import Model.Member;
import Model.Book;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;

import static com.SpringLibrary.SpringbootLibrary.LibraryUI.*;

public class gridHelper extends VerticalLayout implements View
{

     /**
      * Helper function for the createFilter.
      * Changes the grid and compares the titles.
      * @param event
      * last modified by ricky.clevinger 7/19/17
      */
     public static void titleFilterGridChange(HasValue.ValueChangeEvent<String> event, Grid<Book> grid)
     {
         @SuppressWarnings("unchecked")
         ListDataProvider<Book> dataProvider = (ListDataProvider<Book>) grid.getDataProvider();
         dataProvider.setFilter(Book::getTitle, s -> caseInsensitiveContains(s, event.getValue()));

     }//end titleFilterGridChange


     /**
       * Helper function for the createFilter.
       * Changes the grid and compares the titles.
       * @param event
       * last modified by ricky.clevinger 7/19/17
       */
     public static void fNameFilterGridChange(HasValue.ValueChangeEvent<String> event, Grid<Member> grid)
     {
         @SuppressWarnings("unchecked")
         ListDataProvider<Member> dataProvider = (ListDataProvider<Member>) grid.getDataProvider();
         dataProvider.setFilter(Member::getFName, s -> caseInsensitiveContains(s, event.getValue()));

     }//end fNameFilterGridChange


      /**
       * Helper function for the createFilter.
       * Changes the grid and compares the titles.
       * @param event
       * last modified by ricky.clevinger 7/19/17
       */
     public static void lNameFilterGridChange(HasValue.ValueChangeEvent<String> event, Grid<Member> grid)
     {
         @SuppressWarnings("unchecked")
         ListDataProvider<Member> dataProvider = (ListDataProvider<Member>) grid.getDataProvider();
         dataProvider.setFilter(Member::getLName, s -> caseInsensitiveContains(s, event.getValue()));

     }//end lNameFilterGridChange


    /**
     * Helper function for the createFilter.
     * Changes the grid and compares the titles.
     * @param event
     * last modified by ricky.clevinger 7/19/17
     */
    public static void authorFilterGridChange(HasValue.ValueChangeEvent<String> event, Grid<Book> grid)
    {
        @SuppressWarnings("unchecked")
        ListDataProvider<Book> dataProvider = (ListDataProvider<Book>) grid.getDataProvider();
        dataProvider.setFilter(Book::getAuthLName, s -> caseInsensitiveContains(s, event.getValue()));

    }//end authorFilterGridChange


     /**
       *Returns a boolean telling if the lowercase form of text input into the filter is contain
       * by any of the lowercase versions of the book titles.
       * @param where the books titles its comparing to
       * @param what  the filter wood being compared to the book titles
       * @return Boolean telling if the lower case value of the filter input and the book titles match
       *
       * last modified by ricky.clevinger 7/19/17
      */
     public static Boolean caseInsensitiveContains(String where, String what)
     {
         return where.toLowerCase().contains(what.toLowerCase());

     }//end caseInsensitiveContains


    /**
     * Compares two dates to see if a due date is past today's date.
     * @param date1 First date to compare
     * @param date2 Second date to compare
     * @return String with current date or Overdue
     *
     * last modified by ricky.clevinger 7/25/17
     */
    public static String overdue(Date date1, Date date2)
    {
        if (new Date((date1.getTime() + (1000*60*60*24*7) )).compareTo(new Date(date2.getTime())) > 0)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            c.add(Calendar.DATE, 7);
            c.getTime();

            return new Date(c.getTimeInMillis()).toString();
        }
        else
        {
            return "Overdue";
        }

    }// end overdue


    /**
     * Cleans input strings and returns them to the calling method
     *
     * last modified by charles.coalson 7/25/17
     */
    public static String stringClean(String toClean)
    {
        String  temp = toClean.trim();
        temp = temp.replaceAll("[^a-zA-Z.\\- ]","");
        return temp;

    }//end stringClean


    /**
     * Sets a listener that automatically changes the default view when a selection is made
     * @param viewChangeEvent on view change
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {

    }


    public static Boolean authenticate(String role) throws ParseException, JOSEException {
        // Parse the JWE string
        if (!(jweObject == null)) {
            jweObject = JWEObject.parse(jweString);

            // Decrypt with shared key
            jweObject.decrypt(new DirectDecrypter(secretKey.getEncoded()));

            // Extract payload
            signedJWT = jweObject.getPayload().toSignedJWT();

            if (signedJWT.getJWTClaimsSet().getSubject().toString().equals(role)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
