import api.UserApi;
import api.UserLoginApi;
import api.UserRegisterApi;
import common.GenerateTestData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UserLoginTest {

    GenerateTestData generateData = new GenerateTestData();
    UserRegisterApi userRegisterApi = new UserRegisterApi();
    UserLoginApi userLoginApi = new UserLoginApi();
    UserApi userApi = new UserApi();
    User user;
    String authToken;
    ValidatableResponse response;

    @Before
    public void setUp() {
        user = new User(generateData.generateString() + "@gmail.com", generateData.generateString(), generateData.generateString());
        response = userRegisterApi.userRegister(user);
        authToken = response.extract().path("accessToken");
    }

    @After
    public void endTest() {
        response = userApi.deleteUser(user, authToken);
        response.statusCode(202);
        response.body("success", is(true));
        response.body("message", is("User successfully removed"));
    }

    @Test
    @DisplayName("Зарегистрированный пользователь может быть успешно авторизован")
    public void userCanLoginSuccessfully() {
        user = new User(user.getEmail(), user.getPassword());
        response = userLoginApi.userLogin(user, authToken);
        response.statusCode(200);
        response.body("success", is(true));
    }

    @Test
    @DisplayName("Пользователь не может быть авторизован, если неверно указан пароль")
    public void userCanNotLoginWithIncorrectPassword() {
        user = new User(user.getEmail(), user.getPassword() + 1);
        response = userLoginApi.userLogin(user, authToken);
        response.statusCode(401);
        response.body("success", is(false));
        response.body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Пользователь не может быть авторизован, если неверно указан логин")
    public void userCanNotLoginWithIncorrectLogin() {
        user = new User(user.getEmail() + 1, user.getPassword());
        response = userLoginApi.userLogin(user, authToken);
        response.statusCode(401);
        response.body("success", is(false));
        response.body("message", is("email or password are incorrect"));
    }
}