import api.IngredientsApi;
import api.OrdersApi;
import api.UserApi;
import api.UserRegisterApi;
import common.GenerateTestData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class GetOrdersTest {

    GenerateTestData generateData = new GenerateTestData();
    UserRegisterApi userRegisterApi = new UserRegisterApi();
    UserApi userApi = new UserApi();
    IngredientsApi ingredientsApi = new IngredientsApi();
    OrdersApi ordersApi = new OrdersApi();
    User user;
    Order order;
    String authToken;
    ValidatableResponse response;

    @Before
    public void setUp() {

        // Создать пользователя
        user = new User(generateData.generateString() + "@gmail.com", generateData.generateString(), generateData.generateString());
        response = userRegisterApi.userRegister(user);
        authToken = response.extract().path("accessToken");

        // Получить список ингредиентов
        response = ingredientsApi.getOrders();
        String id1 = response.extract().path("data[0]._id");
        String id2 = response.extract().path("data[1]._id");

        // Создать заказ
        order = new Order();
        order.setIngredients(List.of(id1, id2));
        response = ordersApi.createOrder(order, authToken);
        response.statusCode(200);
        response.body("success", is(true));
    }

    @After
    public void endTest() {
        response = userApi.deleteUser(user, authToken);
        response.statusCode(202);
        response.body("success", is(true));
        response.body("message", is("User successfully removed"));
    }

    @Test
    @DisplayName("Список заказов может быть успешно получен пользователем с авторизацией")
    public void getOrdersWithAuthorizationReturnsOrders(){

        response = ordersApi.getOrder(authToken);
        response.statusCode(200);
        response.body("success", is(true));
    }

    @Test
    @DisplayName("Список заказов может не может быть успешно получен пользователем без авторизации")
    public void getOrdersWithoutAuthorizationReturnsError(){

        response = ordersApi.getOrder();
        response.statusCode(401);
        response.body("success", is(false));
        response.body("message", is("You should be authorised"));

    }
}