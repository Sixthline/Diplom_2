import model.Order;
import org.junit.Before;
import io.qameta.allure.Description;
import org.junit.After;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import util.GenerateUser;
import util.OrderSteps;
import util.UserSteps;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.is;

public class OrderCreateTest extends BasicScript {
    @Before
    public void tearUp() {
        user = GenerateUser.getUserRandom();
        token = UserSteps.create(user).then().extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (token != null) UserSteps.delete(token);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Заказ c авторизацией (корректные ингредиенты)")
    public void createOrderContainIngredientsWithAuth() {
        ingredients = Order.makeIngredients();
        OrderSteps.create(ingredients, token)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Заказ без авторизации (корректные ингредиенты)")
    public void createOrderEmptyIngredientsWithoutAuth() {
        ingredients = Order.makeIngredients();
        OrderSteps.createWithoutAuth(ingredients)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Заказ c авторизацией (без ингридиентов)")
    public void createOrderEmptyIngredientsWithAuth() {
        OrderSteps.createWithAuthWithoutIngredient(token)
                .then()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Заказ с авторизацией и неверным хешем ингредиентов")
    public void createOrderInvalidIngredientsWithAuth() {
        ingredients = Order.makeIngredientsWithWrongHash();
        OrderSteps.createWithAuthWithIncorrectHashIngredient(ingredients, token)
                .then()
                .assertThat()
                .statusCode(HTTP_INTERNAL_ERROR);
    }
}