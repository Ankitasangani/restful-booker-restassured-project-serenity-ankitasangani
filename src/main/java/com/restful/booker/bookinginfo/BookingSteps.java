package com.restful.booker.bookinginfo;


import com.restful.booker.constants.EndPoints;
import com.restful.booker.constants.Path;
import com.restful.booker.model.BookingPojo;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

public class BookingSteps {

    public static String token;
    @Step("Creating Auth token for user with username: {0}, password: {1}")
    public ValidatableResponse authUser(String username, String password) {
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setUsername(username);
        bookingPojo.setPassword(password);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(bookingPojo)
                .when()
                .post(EndPoints.CREATE_AUTH)
                .then();

    }

    @Step("Creating booking with firstname:{0}, lastname: {1}, totalprice: {2}, depositpaid: {3}, checkIn: {4}, checkout:{5}, additionalneeds: {6}")
    public ValidatableResponse createBooking(String firstName, String lastName, int totalPrice, boolean depositPaid, String checkin, String checkout, String additionalNeeds) {

        BookingPojo bookingPojo = BookingPojo.getBookingPojo(firstName, lastName, totalPrice, depositPaid, checkin, checkout, additionalNeeds);

        return SerenityRest.given().
                header("Content-Type", "application/json")
                .when()
                .body(bookingPojo)
                .post(Path.BOOKING)
                .then();
    }

    @Step("Getting the booking information with id : {0}")
    public ValidatableResponse getBookingInfoById(int id) {

        return SerenityRest.given()
                .header("Authorization", "Bearer 2633d0058d2069545c8073896cbd4e12533b9b68bab8bb0ef0b969588bd7f7af")
                .header("Connection", "keep-alive")
                .pathParam("id", id)
                .when()
                .get(EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then().statusCode(200);

    }

    @Step("Updating booking with firstname:{0}, lastname: {1}, totalprice: {2}, depositpaid: {3}, checkIn: {4}, checkout:{5}, additionalneeds: {5}")
    public ValidatableResponse updateBooking(int id, String firstname, String lastname, int totalprice, boolean depositpaid, String checkin, String checkout, String additionalneeds) {

        BookingPojo bookingPojo = BookingPojo.getBookingPojo(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);

        return SerenityRest.given()
                .header("Content-Type", "application/json")
                .header("Connection", "keep-alive")
                .pathParam("id", id)
                .auth().preemptive().basic("admin", "password123")
                .body(bookingPojo)
                .when()
                .put(EndPoints.UPDATE_BOOKING_BY_ID)
                .then();
    }

    @Step("Deleting booking information with id: {0}")
    public ValidatableResponse deleteBooking(int id) {
        return SerenityRest.given()
                .pathParam("id", id)
                .when()
                .auth().preemptive().basic("admin", "password123")
                .delete(EndPoints.DELETE_BOOKING_BY_ID)
                .then();
    }

    @Step("Getting booking information with id: {0}")
    public ValidatableResponse getBookingById(int id) {
        return SerenityRest.given()
                .pathParam("id", id)
                .when()
                .auth().preemptive().basic("admin", "password123")
                .get(EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then();
    }
}
