package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Order;

public class OrdersApi extends BaseHttpClient {
    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return doPostRequest(URL.ORDERS, order, accessToken);
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrder(Order order) {
        return doPostRequest(URL.ORDERS, order);
    }

    @Step("Получение заказа пользователя")
    public ValidatableResponse getOrder(String accessToken) {
        return doGetRequest(URL.ORDERS, accessToken);
    }

    @Step("Получение заказа без авторизации")
    public ValidatableResponse getOrder() {
        return doGetRequest(URL.ORDERS);
    }
}