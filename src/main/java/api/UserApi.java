package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;

public class UserApi extends BaseHttpClient {
    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(User user, String accessToken) {
        return doDeleteRequest(URL.USER_DATA, user, accessToken);
    }

    @Step("Редактирование данныхх пользователя")
    public ValidatableResponse editUser(User user, String accessToken) {
        return doPatchRequest(URL.USER_DATA, user, accessToken);
    }
}