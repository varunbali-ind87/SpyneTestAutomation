package org.spyne.utilities;

import io.restassured.RestAssured;

import java.security.SecureRandom;

public class InboxUtils
{
    private InboxUtils()
    {
    }

    private static String getRandomNumber()
    {
        var random = new SecureRandom();
        int randomInt =random.nextInt(6, 99999);
        return String.valueOf(randomInt);
    }

    public static String createInbox()
    {
        RestAssured.baseURI = "https://inboxes.com/api/v2/inbox/";
        String randomEmailAddress = "dummy" + getRandomNumber() + "@spicysoda.com";
        RestAssured.given().get().then().assertThat().statusCode(200);
        return randomEmailAddress;
    }
}
