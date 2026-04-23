package org.example.order;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.BaseTest;
import org.example.steps.BaseSteps;
import org.example.data.Order;
import org.example.steps.OrderSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

import static org.example.utils.RandomValue.randomNumber;

public class GetOrderTest extends BaseTest {
    private final OrderSteps orderTest = new OrderSteps();

    int track;

    @Test
    @DisplayName("Response success")
    @Description("200: Response success")
    void getOrderSuccessfulTest() {
        Order order = Order.getOrderWithColor();

        Response response = orderTest.createOrder(order);

        track = orderTest.getTrack(response);
        Response getOrderResponse = orderTest.getOrderByTrack(track);
        BaseSteps.checkStatusCode(getOrderResponse, HttpURLConnection.HTTP_OK);
        orderTest.verifyResponseOrder(getOrderResponse, "order", order);
    }

    @Test
    @DisplayName("Response without track")
    @Description("400: Response without track")
    void getOrderBadRequestTest() {
        Response getOrderResponse = orderTest.getOrderWithoutTrack();
        BaseSteps.checkStatusCode(getOrderResponse, HttpURLConnection.HTTP_BAD_REQUEST);
        BaseSteps.verifyResponse(getOrderResponse, "message", "Недостаточно данных для поиска");
    }

//    @Test
//    @DisplayName("Response with non-exist track")
//    @Description("404: Response with non-exist track")
//    void getOrderNotFoundTest() {
//        int wrongTrack = randomNumber();
//
//        Response getOrderResponse = orderTest.getOrderByTrack(wrongTrack);
//        BaseSteps.checkStatusCode(getOrderResponse, HttpURLConnection.HTTP_NOT_FOUND);
//        BaseSteps.verifyResponse(getOrderResponse, "message", "Заказ не найден");
//    }

    @AfterEach
    public void tearDown() {
        if (track != 0) {
            orderTest.cancelOrder(track);
        }
    }
}
