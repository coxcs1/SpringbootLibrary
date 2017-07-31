package com.SpringLibrary.SpringbootLibrary.modelTests;

import Model.Book;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;


public class BooksTest {

    private Book mockBook = new Book();

    @Test
    public void getSetTitleTest() throws Exception {

        //setter
        mockBook.setTitle("Harry Potter");

        //Sets the result using getter
        String result = mockBook.getTitle();

        //What the expected result is
        String expected = "Harry Potter";

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetTitleTest

    @Test
    public void getSetBookIdTest() throws Exception {

        //setter
        mockBook.setBookId("1");

        //Sets the result using getter
        String result = mockBook.getBookId();

        //What the expected result is
        String expected = "1";

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetBookIdTest

    @Test
    public void getSetAuthFNameTest() throws Exception {

        //setter
        mockBook.setAuthFName("Joanne");

        //Sets the result using getter
        String result = mockBook.getAuthFName();

        //What the expected result is
        String expected = "Joanne";

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetAuthFNameTest

    @Test
    public void getSetAuthLNameTest() throws Exception {

        //setter
        mockBook.setAuthLName("Rowling");

        //Sets the result using getter
        String result = mockBook.getAuthLName();

        //What the expected result is
        String expected = "Rowling";

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetAuthLNameTest

    @Test
    public void getSetLibIdTest() throws Exception {

        //setter
        mockBook.setLibId("1");

        //Sets the result using getter
        String result = mockBook.getLibId();

        //What the expected result is
        String expected = "1";

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetLibIdTest

    @Test
    public void getSetCheckTest() throws Exception {

        //setter
        mockBook.setCheck("1");

        //Sets the result using getter
        String result = mockBook.getCheck();

        //What the expected result is
        String expected = "1";

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetCheckTest

    @Test
    public void getSetMidTest() throws Exception {

        //setter
        mockBook.setMid("0");

        //Sets the result using getter
        String result = mockBook.getMid();

        //What the expected result is
        String expected = "0";

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetMidTest

    @Test
    public void getSetOutDateTest() throws Exception {

        //setter
        mockBook.setOutDate(new Date(0));

        //Sets the result using getter
        String result = mockBook.getOutDate() + "";

        //What the expected result is
        String expected = "" + new Date(0);

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetMidTest
}