package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;

public class UserLoginApi extends BaseHttpClient {
    @Step("Логин пользователя")
    public ValidatableResponse userLogin(User user, String authToken) {
        return doPostRequest(URL.USER_LOGIN, user, authToken);
    }
}