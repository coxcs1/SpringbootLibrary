package com.SpringLibrary.SpringbootLibrary.resourceTests;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;

import static Resource.gridHelper.caseInsensitiveContains;
import static Resource.gridHelper.overdue;
import static Resource.gridHelper.stringClean;

public class gridHelperTest {
    Boolean resultBoolean;
    Boolean expectedBoolean;
    String expectedString;
    String resultString;

    @Test
    public void stringCleanBadInputTest() throws Exception {

        //What the expected result is
        expectedString = "";

        //Sets the result using getter
        resultString = stringClean("3");
        //Compares expected result with the actual result.
        Assert.assertEquals(expectedString, resultString);

        resultString = stringClean("!23214");

        Assert.assertEquals(expectedString, resultString);
    }

    @Test
    public void stringCleanGoodInputTest() throws Exception {

        //What the expected result is
        expectedString = "Cat";

        //Sets the result using getter
        resultString = stringClean("Cat");
        //Compares expected result with the actual result.
        Assert.assertEquals(expectedString, resultString);

        expectedString = "Turtle";
        resultString = stringClean("Turtle");

        Assert.assertEquals(expectedString, resultString);
    }

    @Test
    public void overdueTest() throws Exception {

        Date date1 = new Date(0);
        Date date2 = new Date(1);

        //What the expected result is
        expectedString = "Overdue";

        //Sets the result using getter
        String result = overdue(date1,date2);
        //Compares expected result with the actual result.
        Assert.assertEquals(expectedString, result);

    }

    @Test
    public void caseInsensitiveContainsTest() throws Exception {

        //What the expected result is
        expectedBoolean = true;

        //Sets the result using getter
        resultBoolean = caseInsensitiveContains("ROAR","ro");
        //Compares expected result with the actual result.
        Assert.assertEquals(expectedBoolean, resultBoolean);

        expectedBoolean = false;
        resultBoolean = caseInsensitiveContains("ROAR","rooooo");

        Assert.assertEquals(expectedBoolean, resultBoolean);

    }
}
