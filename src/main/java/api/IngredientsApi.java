package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class IngredientsApi extends BaseHttpClient {
    @Step("Получение данных об ингредиентах")
    public ValidatableResponse getOrders() {
        return doGetRequest(URL.INGREDIENTS);
    }
}