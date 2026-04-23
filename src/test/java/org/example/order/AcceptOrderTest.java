package org.example.order;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.BaseTest;
import org.example.steps.BaseSteps;
import org.example.steps.CourierSteps;
import org.example.data.Courier;
import org.example.data.Order;
import org.example.steps.OrderSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.example.utils.RandomValue.randomNumber;

public class AcceptOrderTest extends BaseTest {
    private final OrderSteps orderTest = new OrderSteps();
    private final CourierSteps courierTest = new CourierSteps();

    Courier courier;
    int courierId;
    int id;
    int track;


    @Test
    @DisplayName("Successfully accepted order")
    @Description("200: Successfully accepted order")
    void acceptOrderSuccessfulTest() {
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);
        Response loginResponse = courierTest.loginCourier(courier);
        courierId = BaseSteps.verifyResponseId(loginResponse);

        Order order = Order.getOrderWithColor();
        Response orderResponse = orderTest.createOrder(order);
        track = orderTest.getTrack(orderResponse);

        Response getOrderResponse = orderTest.getOrderByTrack(track);
        id = orderTest.getOrderId(getOrderResponse);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courierId", courierId);

        Response response = orderTest.putOrder(id, parameters);

        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        BaseSteps.verifyResponse(response, "ok", true);
    }


//    @Test
//    @DisplayName("Accept order without id")
//    @Description("400: Accept order without id")
//    void acceptOrderBadRequestTest() {
//        courier = Courier.courierWithRandomLogin();
//        courierTest.createCourier(courier);
//        Response loginResponse = courierTest.loginCourier(courier);
//        courierId = BaseSteps.verifyResponseId(loginResponse);
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("courierId", courierId);
//
//        Response response = orderTest.putOrderWithoutId(parameters);
//
//        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_BAD_REQUEST);
//        BaseSteps.verifyResponse(response, "message", "Недостаточно данных для поиска");
//    }

    @Test
    @DisplayName("Accept order with non-exist id")
    @Description("404: Accept order with non-exist id")
    void acceptOrderNotFoundNotExistIdTest() {
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);
        Response loginResponse = courierTest.loginCourier(courier);
        courierId = BaseSteps.verifyResponseId(loginResponse);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courierId", courierId);

        int wrongId = randomNumber();

        Response response = orderTest.putOrder(wrongId, parameters);

        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_NOT_FOUND);
        BaseSteps.verifyResponse(response, "message", "Заказа с таким id не существует");
    }


    @Test
    @DisplayName("Accept order with non-exist courier's id")
    @Description("404: Accept order with non-exist courier's id")
    void acceptOrderNotFoundNotExistCourierTest() {
        int wrongCourierId = randomNumber();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courierId", wrongCourierId);

        Order order = Order.getOrderWithColor();
        Response orderResponse = orderTest.createOrder(order);
        track = orderTest.getTrack(orderResponse);

        Response getOrderResponse = orderTest.getOrderByTrack(track);
        id = orderTest.getOrderId(getOrderResponse);

        Response response = orderTest.putOrder(id, parameters);

        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_NOT_FOUND);
        BaseSteps.verifyResponse(response, "message", "Курьера с таким id не существует");
    }

    @Test
    @DisplayName("Accept order without courier's id")
    @Description("400: Accept order without courier's id")
    void acceptOrderBadRequestWithoutCourierIdTest() {
        Map<String, Object> parameters = new HashMap<>();

        Order order = Order.getOrderWithColor();
        Response orderResponse = orderTest.createOrder(order);
        track = orderTest.getTrack(orderResponse);

        Response getOrderResponse = orderTest.getOrderByTrack(track);
        id = orderTest.getOrderId(getOrderResponse);

        Response response = orderTest.putOrder(id, parameters);

        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_BAD_REQUEST);
        BaseSteps.verifyResponse(response, "message", "Недостаточно данных для поиска");
    }



//    @Test
//    @DisplayName("Accept order with non-exist id or courier's id")
//    @Description("409: Что вообще хотели проверить? \n" +
//            "В ручке указан код 400, но Conflict, а такая проверка вообще не имеет смысла.\n" +
//            "HTTP/1.1 400 Conflict\n" +
//            "{\n" +
//            "  \"message\": \"Недостаточно данных для поиска\"\n" +
//            "}\n" +
//            "Поэтому проверяю конфликт.")
//    void acceptOrderConflict() {
//        courier = Courier.courierWithRandomLogin();
//        courierTest.createCourier(courier);
//        Response loginResponse = courierTest.loginCourier(courier);
//        courierId = BaseSteps.verifyResponseId(loginResponse);
//
//        Order order = Order.getOrderWithColor();
//        Response orderResponse = orderTest.createOrder(order);
//        track = orderTest.getTrack(orderResponse);
//
//        Response getOrderResponse = orderTest.getOrderByTrack(track);
//        id = orderTest.getOrderId(getOrderResponse);
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("courierId", courierId);
//
//        Response response = orderTest.putOrder(id, parameters);
//
//        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_OK);
//
//        Response responseDuplicate = orderTest.putOrder(id, parameters);
//
//        BaseSteps.checkStatusCode(responseDuplicate, HttpURLConnection.HTTP_CONFLICT);
//        BaseSteps.verifyResponse(response, "message", "Недостаточно данных для поиска");
//    }

    @AfterEach
    public void tearDown() {
        if (courierId != 0) {
            courierTest.deleteById(courierId);
        }
        if (track != 0) {
            orderTest.cancelOrder(track);
        }
    }

}
