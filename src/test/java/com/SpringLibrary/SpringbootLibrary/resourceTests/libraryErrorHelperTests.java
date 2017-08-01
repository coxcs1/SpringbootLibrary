package com.SpringLibrary.SpringbootLibrary.resourceTests;

import Resource.LibraryErrorHelper;
import org.junit.Assert;
import org.junit.Test;

public class libraryErrorHelperTests {
    private LibraryErrorHelper libraryErrorHelper = new LibraryErrorHelper();
    private Exception e = new Exception();

    @Test
    public void genericErrorTest() throws Exception {

        //What the expected result is
        String expectedString = "The string was empty";

        //Sets the result using getter
        String resultString = libraryErrorHelper.genericError(e);
        //Compares expected result with the actual result.
        Assert.assertEquals(expectedString, resultString);

    }

}
