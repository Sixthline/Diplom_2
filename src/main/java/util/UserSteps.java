package util;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;

import static io.restassured.RestAssured.given;
import static util.Endpoints.*;
import static util.OrderSteps.AUT;

public class UserSteps {
    @Step("Создание пользователя")
    public static Response create(User user) {
        return given()
                .body(user)
                .when()
                .post(REGISTER);
    }

    @Step("Удаление пользователя")
    public static Response delete(String accessToken) {
        return given()
                .header(AUT, accessToken)
                .body("")
                .when()
                .delete(USER);
    }

    @Step("Логин пользователя")
    public static Response login(UserCredentials userCredentials) {
        return given()
                .body(userCredentials)
                .when()
                .post(LOGIN);

    }

    @Step("Изменение данных пользователя c авторизацией")
    public static Response edit(User user, String token) {
        return given()
                .header("Authorization", token)
                .body(user)
                .when()
                .patch(USER);
    }

    @Step("Изменение данных пользователя без авторизации")
    public static Response editNotAuthorization(User user) {
        return given()
                .body(user)
                .when()
                .patch(USER);
    }
}