import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.GenerateUser;
import util.UserSteps;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
public class CreateUserTest extends BasicScript {
    @Before
    public void tearUp() {
        user = GenerateUser.getUserRandom();
    }

    @After
    public void tearDown() {
        if (token != null) UserSteps.delete(token);
    }

    @Test
    @DisplayName("Создание нового пользователя")
    @Description("Создание нового уникального пользователя")
    public void createUniqUserTest() {
        token = UserSteps.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true))
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Попытка создания пользователя, который уже был зарегестрирован")
    @Description("Создание нового пользователя и попытка его повторной регистрации")
    public void createNoUniqUserTest() {
        token = UserSteps.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true))
                .extract().path("accessToken");
        UserSteps.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Создание пользователя без имени (name)")
    public void createUserWithoutNameFieldTest() {
        user = GenerateUser.getUserWithoutName();
        UserSteps.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без электронной почты")
    @Description("Создание пользователя без электронной почты (email)")
    public void createUserWithoutEmailFieldTest() {
        user = GenerateUser.getUserWithoutEmail();
        UserSteps.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }


    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Создание нового пользователя без пароля (password)")
    public void createUserWithoutPasswordFieldTest() {
        user = GenerateUser.getUserWithoutPassword();
        UserSteps.create(user)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }
}