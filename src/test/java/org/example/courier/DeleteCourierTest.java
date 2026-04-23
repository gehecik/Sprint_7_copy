package org.example.courier;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.BaseTest;
import org.example.steps.BaseSteps;
import org.example.data.Courier;
import org.example.steps.CourierSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

import static org.example.utils.RandomValue.randomNumber;

public class DeleteCourierTest extends BaseTest {
    private final CourierSteps courierTest = new CourierSteps();
    Courier courier;
    int courierId;

    @Test
    @DisplayName("Successful delete courier")
    @Description("200: Successful delete courier")
    public void deleteCourierSuccessfulTest() {
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);

        Response responselogin = courierTest.loginCourier(courier);
        courierId = BaseSteps.verifyResponseId(responselogin);
        Response response = courierTest.deleteById(courierId);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_OK);
    }

//    @Test
//    @DisplayName("Delete courier with non-exist id")
//    @Description("404: Delete courier with non-exist id")
//    public void deleteCourierNotFoundWithNonExistIdTest() {
//        courierId = randomNumber();
//        Response response = courierTest.deleteById(courierId);
//        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_NOT_FOUND);
//        BaseSteps.verifyResponse(response, "message","Курьера с таким id нет");
//    }
//
//    @Test
//    @DisplayName("Delete courier without id")
//    @Description("400: Delete courier without id")
//    public void deleteCourierBadRequestWithoutIdTest() {
//        Response response = courierTest.deleteWithoutId();
//        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_BAD_REQUEST);
//        BaseSteps.verifyResponse(response, "message","Недостаточно данных для удаления курьера");
//    }

}
