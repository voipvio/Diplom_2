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

public class OrderCreateTest {

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
    @DisplayName("Заказ может быть успешно создан с авторизацией")
    public void orderCanBeCreatedWithAuthorization(){
        response = ingredientsApi.getOrders();
        String id1 = response.extract().path("data[0]._id");
        String id2 = response.extract().path("data[1]._id");

        order = new Order();
        order.setIngredients(List.of(id1, id2));
        response = ordersApi.createOrder(order, authToken);
        response.statusCode(200);
        response.body("success", is(true));
    }

    @Test
    @DisplayName("Заказ может быть успешно создан без авторизации")
    public void orderCanBeCreatedWithoutAuthorization(){
        response = ingredientsApi.getOrders();
        String id1 = response.extract().path("data[0]._id");
        String id2 = response.extract().path("data[1]._id");

        order = new Order();
        order.setIngredients(List.of(id1, id2));
        response = ordersApi.createOrder(order);
        response.statusCode(200);
        response.body("success", is(true));
    }

    @Test
    @DisplayName("Заказ может быть успешно создан с ингредиентами")
    public void orderCanBeCreatedWithIngredients(){
        response = ingredientsApi.getOrders();
        String id1 = response.extract().path("data[0]._id");
        String id2 = response.extract().path("data[1]._id");

        order = new Order();
        order.setIngredients(List.of(id1, id2));
        response = ordersApi.createOrder(order, authToken);
        response.statusCode(200);
        response.body("success", is(true));
    }

    @Test
    @DisplayName("Заказ не может быть создан без ингредиентов")
    public void orderCanNotBeCreatedWithoutIngredients(){
        order = new Order();
        response = ordersApi.createOrder(order, authToken);
        response.statusCode(400);
        response.body("success", is(false));
        response.body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Заказ не может быть успешно создан с неверным хэшем ингредиента")
    public void orderCanNotBeCreatedWithInvalidIngredients(){
        response = ingredientsApi.getOrders();
        String id1 = response.extract().path("data[0]._id");
        String id2 = response.extract().path("data[1]._id");

        order = new Order();
        order.setIngredients(List.of(id1 + 1, id2 + 1));
        response = ordersApi.createOrder(order, authToken);
        response.statusCode(500);
    }
}