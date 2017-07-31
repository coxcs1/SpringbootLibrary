package com.SpringLibrary.SpringbootLibrary.modelTests;

import Model.Member;
import org.junit.Assert;
import org.junit.Test;

public class MemberTest {

    private Member mockMem = new Member();

    @Test
    public void getSetIdTest() throws Exception {

        //setter
        mockMem.setId("1");

        //Sets the result using getter
        String result = mockMem.getId();

        //What the expected result is
        String expected = "1";

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetIdTest

    @Test
    public void getSetFNameTest() throws Exception {

        //setter
        mockMem.setFName("Jack");

        //Sets the result using getter
        String result = mockMem.getFName();

        //What the expected result is
        String expected = "Jack";

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetFNameTest

    @Test
    public void getSetLNameTest() throws Exception {

        //setter
        mockMem.setLName("Sparrow");

        //Sets the result using getter
        String result = mockMem.getLName();

        //What the expected result is
        String expected = "Sparrow";

        //Compares expected result with the actual result.
        Assert.assertEquals(expected, result);
    }//end getSetLNameTest

}