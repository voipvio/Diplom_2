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

public class UserEditTest {

    GenerateTestData generateData = new GenerateTestData();
    UserRegisterApi userRegisterApi = new UserRegisterApi();
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
    @DisplayName("Имя авторизованного пользователя может быть успешно изменено")
    public void userNameCanBeChangedSuccessfully() {
        user.setName(user.getName() + 1);
        response = userApi.editUser(user, authToken);
        response.statusCode(200);
        response.body("success", is(true));
    }

    @Test
    @DisplayName("Имейл авторизованного пользователя может быть успешно изменен")
    public void userEmailCanBeChangedSuccessfully() {
        user.setEmail("k" + user.getEmail());
        response = userApi.editUser(user, authToken);
        response.statusCode(200);
        response.body("success", is(true));
    }

    @Test
    @DisplayName("Пароль авторизованного пользователя может быть успешно изменен")
    public void userPasswordCanBeChangedSuccessfully() {
        user.setPassword(user.getPassword() + 1);
        response = userApi.editUser(user, authToken);
        response.statusCode(200);
        response.body("success", is(true));
    }

    @Test
    @DisplayName("Имя неавторизованного пользователя не может быть изменено")
    public void unauthorizedUserNameCanNotBeChanged() {
        user.setName(user.getName() + 1);
        response = userApi.editUser(user, "");
        response.statusCode(401);
        response.body("success", is(false));
        response.body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Имейл неавторизованного пользователя не может быть изменен")
    public void unauthorizedUserEmailCanNotBeChanged() {
        user.setEmail("k" + user.getEmail());
        response = userApi.editUser(user, "");
        response.statusCode(401);
        response.body("success", is(false));
        response.body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Пароль неавторизованного пользователя не может быть изменен")
    public void unauthorizedUserPasswordCanNotBeChanged() {
        user.setPassword(user.getPassword() + 1);
        response = userApi.editUser(user, "");
        response.statusCode(401);
        response.body("success", is(false));
        response.body("message", is("You should be authorised"));
    }
}