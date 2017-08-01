package com.SpringLibrary.SpringbootLibrary.resourceTests;

import Resource.LibraryErrorHelper;
import org.junit.Assert;
import org.junit.Test;


import static Resource.gridHelper.*;

public class libraryErrorHelperTests {
    private String expectedString;
    private String resultString;
    LibraryErrorHelper libraryErrorHelper = new LibraryErrorHelper();
    Exception e = new Exception();

    @Test
    public void genericErrorTest() throws Exception {

        //What the expected result is
        expectedString = "The string was empty";

        //Sets the result using getter
        resultString = libraryErrorHelper.genericError(e);
        //Compares expected result with the actual result.
        Assert.assertEquals(expectedString, resultString);

    }

}
