package org.example.courier;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.steps.BaseSteps;
import org.example.BaseTest;
import org.example.data.Courier;
import org.example.steps.CourierSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

public class CreateCourierTest extends BaseTest {
    private final CourierSteps courierTest = new CourierSteps();

    int courierId;

    @Test
    @DisplayName("Successful account creation")
    @Description("201: Successful account creation")
    void createCourierSuccessfulTest() {
        Courier courier = Courier.courierWithRandomLogin();

        Response response = courierTest.createCourier(courier);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_CREATED);
        BaseSteps.verifyResponse(response, "ok", true);

        Response loginResponse = courierTest.loginCourier(courier);
        courierId = BaseSteps.getId(loginResponse);
    }

    void createCourierBadRequestTest(Object courier) {
        Response response = courierTest.createCourier(courier);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_BAD_REQUEST);
        BaseSteps.verifyResponse(response, "message","Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Creating an account without a login")
    @Description("400: Creating an account without a login")
    void createCourierBadRequestWithoutLoginTest() {
        Courier courier = Courier.courierWithoutLogin();

        createCourierBadRequestTest(courier);
    }

    @Test
    @DisplayName("Creating an account without a password")
    @Description("400: Creating an account without a password")
    void createCourierBadRequestWithoutPasswordTest() {
        Courier courier = Courier.courierWithoutPassword();

        createCourierBadRequestTest(courier);
    }

//    @Test
//    @DisplayName("Creating an account with a duplicate login")
//    @Description("409: Creating an account with a duplicate login")
//    void createCourierConflictWithDuplicateLoginTest() {
//        Courier courier = Courier.courierWithRandomLogin();
//
//        courierTest.createCourier(courier);
//        Response loginResponse = courierTest.loginCourier(courier);
//        courierId = BaseSteps.getId(loginResponse);
//
//        Response responseDuplicate = courierTest.createCourier(courier);
//        BaseSteps.checkStatusCode(responseDuplicate, HttpURLConnection.HTTP_CONFLICT);
//        BaseSteps.verifyResponse(responseDuplicate, "message","Этот логин уже используется");
//    }

    @AfterEach
    public void tearDown() {
        if (courierId != 0) {
            courierTest.deleteById(courierId);
        }
    }

}
