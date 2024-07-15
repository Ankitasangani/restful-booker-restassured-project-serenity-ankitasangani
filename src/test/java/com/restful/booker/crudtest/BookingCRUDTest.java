package com.restful.booker.crudtest;


import com.restful.booker.bookinginfo.BookingSteps;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasKey;

@RunWith(SerenityRunner.class)
public class BookingCRUDTest extends TestBase {
    static String firstname = "Jim" + TestUtils.getRandomValue();
    static String lastname = "Brown" + TestUtils.getRandomValue();
    static int totalPrice = 111;
    static boolean depositPaid = true;
    static String additionalNeeds = "Breakfast";
    static String extractedFirstname;
    static String token;
    String checkIn = "2020-05-03";
    String checkOut = "2020-05-10";
    public static String username = "admin";
    public static String password = "password123";
    static int id;
    @Steps
    BookingSteps bookingSteps;

    @Title("This will create Auth token for user")
    @Test
    public void test001() {
        ValidatableResponse response = bookingSteps.authUser(username, password);

        response.log().all().statusCode(200);

        HashMap<Object, Object> tokenMap = response.log().all().extract().path("");

        Assert.assertThat(tokenMap, hasKey("token"));
        String jsonString = response.extract().asString();
        token = JsonPath.from(jsonString).get("token");

        System.out.println(token);
    }

    @Title("This will create a new booking")
    @Test
    public void test002() {

        ValidatableResponse response = bookingSteps.createBooking(firstname, lastname, totalPrice, depositPaid, checkIn, checkOut, additionalNeeds);

        response.statusCode(200);
        id = response.extract().path("bookingid");

    }

    @Title("Verify if the booking was added to the application")
    @Test
    public void test003() {
        ValidatableResponse response = bookingSteps.getBookingInfoById(id);
    }

    @Title("Update the booking information and verify the updated information")
    @Test
    public void test004() {

        ValidatableResponse response = bookingSteps.updateBooking(id, firstname, lastname, totalPrice, depositPaid, checkIn, checkOut, additionalNeeds).statusCode(200);

    }

    @Title("Delete the booking and verify if the booking is deleted")
    @Test
    public void test005() {

        bookingSteps.deleteBooking(id).statusCode(201);
        bookingSteps.getBookingById(id).statusCode(404);
    }
}
