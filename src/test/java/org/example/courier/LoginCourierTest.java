package org.example.courier;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.BaseTest;
import org.example.steps.BaseSteps;
import org.example.data.Courier;
import org.example.steps.CourierSteps;
import org.junit.jupiter.api.*;

import java.net.HttpURLConnection;

public class LoginCourierTest extends BaseTest {
    private final CourierSteps courierTest = new CourierSteps();

    Courier courier;

    int courierId;

    @BeforeEach
    public void setUpLoginCourier() {
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);

        Response loginResponse = courierTest.loginCourier(courier);
        courierId = BaseSteps.verifyResponseId(loginResponse);
    }

    @Test
    @DisplayName("Successful login to an account")
    @Description("200: Successful login to an account")
    public void checkLoginCourierSuccessfulTest() {
        Response response = courierTest.loginCourier(courier);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_OK);
    }

    public void checkLoginCourierBadRequestTest(Object courier) {
        Response response = courierTest.loginCourierWithLog(courier);//.loginCourier(courier);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_BAD_REQUEST);
        BaseSteps.verifyResponse(response, "message","Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Logging an account without login")
    @Description("400: Logging an account without login")
    public void checkLoginCourierBadRequestWithoutLoginTest() {
        Courier courierWithoutLogin = Courier.currentCourierWithoutLogin(courier);

        checkLoginCourierBadRequestTest(courierWithoutLogin);
    }

//    @Test
//    @DisplayName("Logging an account without password")
//    @Description("400: Logging an account without password")
//    public void checkLoginCourierBadRequestWithoutPasswordTest() {
//        Courier courierWithoutPassword = Courier.currentCourierWithoutPassword(courier);
//
//        checkLoginCourierBadRequestTest(courierWithoutPassword);
//    }

    public void checkLoginNotFound(Object courier) {
        Response response = courierTest.loginCourier(courier);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_NOT_FOUND);
        BaseSteps.verifyResponse(response, "message","Учетная запись не найдена");
    }

    @Test
    @DisplayName("Logging an account with non-existent login/password pair")
    @Description("404: Logging an account with non-existent login/password pair")
    public void checkLoginNotFoundWithNonExistLoginPasswordPair() {
        Courier courier = Courier.courierWithRandomLogin();

        checkLoginNotFound(courier);
    }

    @Test
    @DisplayName("Logging an account with wrong login")
    @Description("404: Logging an account with wrong login")
    public void checkLoginNotFoundWithWrongLogin() {
        Courier courierWrongLogin = Courier.courierWrongLogin(courier);

        checkLoginNotFound(courierWrongLogin);
    }

    @Test
    @DisplayName("Logging an account with wrong password")
    @Description("404: Logging an account with wrong password")
    public void checkLoginNotFoundWithWrongPassword() {
        Courier courierWrongPassword = Courier.courierWrongPassword(courier);

        checkLoginNotFound(courierWrongPassword);
    }

    @AfterEach
    public void tearDown() {
        if (courierId != 0) {
            courierTest.deleteById(courierId);
        }
    }

}
