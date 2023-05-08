import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.GenerateUser;
import util.UserSteps;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;
public class ChangeUserDataTest extends BasicScript {
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
    @DisplayName("Редактирование авторизованного пользователя")
    @Description("Изменение данных авторизованного пользователя")
    public void changeUserDataAuthorizationTest() {
        UserSteps.edit(GenerateUser.getUserWithModified(), token)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true))
                .body("user.email", is("usersemen@yandex.ru"))
                .body("user.name", is("Usersema"));
    }

    @Test
    @DisplayName("Редактирование неавторизованного пользователя")
    @Description("Редактирование полей пользователя")
    public void changeUserDataWithoutAuthorizationTest() {
        UserSteps.editNotAuthorization(GenerateUser.getUserWithModified())
                .then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}