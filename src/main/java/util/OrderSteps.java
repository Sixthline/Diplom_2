package util;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;
import static util.Endpoints.CREATE_ORDER;

public class OrderSteps {
    public static final String AUT = "Authorization";

    @Step("Создание закаказа c авторизацией и c ингредиентами")
    public static Response create(Order order, String token) {
        return given().header(AUT, token).body(order).when().post(CREATE_ORDER);
    }

    @Step("Создание закаказа без авторизации c ингредиентами")
    public static Response createWithoutAuth(Order order) {
        return given().body(order).when().post(CREATE_ORDER);
    }

    @Step("Создание закаказа c авторизацией без ингредиентов")
    public static Response createWithAuthWithoutIngredient(String token) {
        return given().header(AUT, token).when().post(CREATE_ORDER);
    }

    @Step("Создание заказа c авторизацией и неверно указанными ингредиентами")
    public static Response createWithAuthWithIncorrectHashIngredient(Order order, String token) {
        return given().header(AUT, token).body(order).when().post(CREATE_ORDER);
    }

    @Step("Получение заказов конкретного пользователя")
    public static Response getOrdersWithAuth(String token) {
        return given().header(AUT, token).when().get(CREATE_ORDER);
    }

    @Step("Получение заказов конкретного пользователя")
    public static Response getOrdersWithoutAuth() {
        return given().when().get(CREATE_ORDER);
    }
}
