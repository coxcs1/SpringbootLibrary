package Resource;

import Model.Member;

import Model.Book;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;


public class gridHelper {




public static void createLayout(VerticalLayout verticalLayout) {
    HorizontalLayout hLayout = new HorizontalLayout();
    hLayout.setSpacing(true);
    verticalLayout.addComponent(hLayout);
    }




     /**
      * Helper function for the createFilter.
      * Changes the grid and compares the titles.
      * @param event
      * last modified by ricky.clevinger 7/19/17
      */
     public static void titleFilterGridChange(HasValue.ValueChangeEvent<String> event, Grid<Book> grid) {
         ListDataProvider<Book> dataProvider = (ListDataProvider<Book>) grid.getDataProvider();
         dataProvider.setFilter(Book::getTitle, s -> caseInsensitiveContains(s, event.getValue()));
     }//end fNameFilterGridChange



     /**
       * Helper function for the createFilter.
       * Changes the grid and compares the titles.
       * @param event
       * last modified by ricky.clevinger 7/19/17
       */
     public static void fNameFilterGridChange(HasValue.ValueChangeEvent<String> event, Grid<Member> grid) {
         ListDataProvider<Member> dataProvider = (ListDataProvider<Member>) grid.getDataProvider();
         dataProvider.setFilter(Member::getFName, s -> caseInsensitiveContains(s, event.getValue()));
     }//end fNameFilterGridChange


      /**
       * Helper function for the createFilter.
       * Changes the grid and compares the titles.
       * @param event
       * last modified by ricky.clevinger 7/19/17
       */
     public static void lNameFilterGridChange(HasValue.ValueChangeEvent<String> event, Grid<Member> grid) {
         ListDataProvider<Member> dataProvider = (ListDataProvider<Member>) grid.getDataProvider();
         dataProvider.setFilter(Member::getLName, s -> caseInsensitiveContains(s, event.getValue()));
     }//end lNameFilterGridChange


    /**
     * Helper function for the createFilter.
     * Changes the grid and compares the titles.
     * @param event
     * last modified by ricky.clevinger 7/19/17
     */
    public static void authorFilterGridChange(HasValue.ValueChangeEvent<String> event, Grid<Book> grid) {
        ListDataProvider<Book> dataProvider = (ListDataProvider<Book>) grid.getDataProvider();
        dataProvider.setFilter(Book::getAuthLName, s -> caseInsensitiveContains(s, event.getValue()));
    }//end titleFilterGridChange



     /**
       *Returns a boolean telling if the lowercase form of text input into the filter is contain
       * by any of the lowercase versions of the book titles.
       * @param where the books titles its comparing to
       * @param what  the filter wood being compared to the book titles
       * @return Boolean telling if the lower case value of the filter input and the book titles match
       *
       * last modified by ricky.clevinger 7/19/17
      */
     public static Boolean caseInsensitiveContains(String where, String what) {
         return where.toLowerCase().contains(what.toLowerCase());
     }//end caseInsensitiveContains




}