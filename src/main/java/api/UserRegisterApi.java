package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;

public class UserRegisterApi extends BaseHttpClient {
    @Step("Регистрация пользователя")
    public ValidatableResponse userRegister(User user) {
        return doPostRequest(URL.USER_REGISTER, user);
    }
}