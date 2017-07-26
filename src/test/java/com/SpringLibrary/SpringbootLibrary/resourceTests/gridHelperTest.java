package com.SpringLibrary.SpringbootLibrary.resourceTests;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;

import static Resource.gridHelper.overdue;
import static Resource.gridHelper.stringClean;

public class gridHelperTest {

    @Test
    public void stringCleanBadInputTest() throws Exception {

        //What the expected result is
        String expected = "";

        //Sets the result using getter
        String result = stringClean("3");
        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);

        result = stringClean("!23214");

        Assert.assertEquals(expected, result);
    }

    @Test
    public void stringCleanGoodInputTest() throws Exception {

        //What the expected result is
        String expected = "Cat";

        //Sets the result using getter
        String result = stringClean("Cat");
        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);

        expected = "Turtle";
        result = stringClean("Turtle");

        Assert.assertEquals(expected, result);
    }

    @Test
    public void overdueTest() throws Exception {

        Date date1 = new Date(0);
        Date date2 = new Date(1);

        //What the expected result is
        String expected = "Overdue";

        //Sets the result using getter
        String result = overdue(date1,date2);
        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);

    }
}
