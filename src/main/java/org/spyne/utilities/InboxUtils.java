package org.spyne.utilities;

import io.restassured.RestAssured;
import org.awaitility.Awaitility;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class InboxUtils
{
    private static String randomEmailAddress;

    private InboxUtils()
    {
    }

    private static String getRandomNumber()
    {
        var random = new SecureRandom();
        int randomInt = random.nextInt(6, 99999);
        return String.valueOf(randomInt);
    }

    public static String createInbox()
    {
        randomEmailAddress = getRandomEmailAddress();
        RestAssured.given().get("https://inboxes.com/api/v2/inbox/" + randomEmailAddress).then().assertThat().statusCode(200);
        return randomEmailAddress;
    }

    private static String getRandomEmailAddress()
    {
        randomEmailAddress = "dummy" + getRandomNumber() + "@spicysoda.com";
        return randomEmailAddress;
    }

    private static String getUID()
    {
        Awaitility.await().atMost(Duration.ofSeconds(30)).until(() ->
        {
            try
            {
                return !RestAssured.given().get("https://inboxes.com/api/v2/inbox/" + randomEmailAddress).then().extract().body().jsonPath()
                        .getList("msgs", HashMap.class).getFirst().isEmpty();
            }
            catch (NoSuchElementException e)
            {
                return false;
            }
        });
        return RestAssured.given().get("https://inboxes.com/api/v2/inbox/" + randomEmailAddress).then().extract().body().jsonPath().getList("msgs", HashMap.class).getFirst().get("uid").toString();
    }

    public static String getOTP()
    {
        String uid = getUID();
        String emailContent = RestAssured.given().get("https://inboxes.com/api/v2/message/" + uid).then().assertThat().statusCode(200).extract()
                .body().jsonPath().getMap("").get("text").toString();

        var pattern = Pattern.compile("\\b\\d{6}\\b");
        var matcher = pattern.matcher(emailContent);
        if (matcher.find())
            return matcher.group();
        else
            throw new RuntimeException("OTP not detected!");
    }
}
