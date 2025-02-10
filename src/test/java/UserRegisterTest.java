import api.UserApi;
import api.UserRegisterApi;
import common.GenerateTestData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UserRegisterTest {

    GenerateTestData generateData = new GenerateTestData();
    UserRegisterApi userRegisterApi = new UserRegisterApi();
    UserApi userApi = new UserApi();
    User user;
    String authToken;
    ValidatableResponse response;

    @Before
    public void setUp() {
        user = new User(generateData.generateString() + "@gmail.com", generateData.generateString(), generateData.generateString());
    }

    @After
    public void endTest() {
        if (authToken != null) {
            response = userApi.deleteUser(user, authToken);
            response.statusCode(202);
            response.body("success", is(true));
            response.body("message", is("User successfully removed"));
        }
    }

    @Test
    @DisplayName("Уникальный пользователь может быть успешно зарегистрирован")
    public void userCanRegisterSuccessfully() {
        response = userRegisterApi.userRegister(user);
        authToken = response.extract().path("accessToken");
        response.statusCode(200);
        response.body("success", is(true));
    }

    @Test
    @DisplayName("Пользователь не может быть зарегистрирован повторно")
    public void userCanNotRegisterWithTheExistingData() {
        response = userRegisterApi.userRegister(user);
        authToken = response.extract().path("accessToken");
        response = userRegisterApi.userRegister(user);
        response.statusCode(403);
        response.body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Пользователь не может быть зарегистрирован без пароля")
    public void userCanNotRegisterWithEmptyPassword() {
        user.setPassword("");
        response = userRegisterApi.userRegister(user);
        response.statusCode(403);
        response.body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Пользователь не может быть зарегистрирован без имейла")
    public void userCanNotRegisterWithEmptyEmail() {
        user.setEmail("");
        response = userRegisterApi.userRegister(user);
        response.statusCode(403);
        response.body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Пользователь не может быть зарегистрирован без имени")
    public void userCanNotRegisterWithEmptyName() {
        user.setName("");
        response = userRegisterApi.userRegister(user);
        response.statusCode(403);
        response.body("message", is("Email, password and name are required fields"));
    }
}