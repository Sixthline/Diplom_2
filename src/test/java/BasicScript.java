import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import model.Order;
import model.User;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;

public class BasicScript {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected Order ingredients;
    protected User user;
    protected String token;

    @Before
    public void setUp() {
        RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig().httpClientFactory(() -> {
            var cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(50);
            cm.setDefaultMaxPerRoute(20);
            return HttpClients.custom()
                    .setConnectionManager(cm)
                    .build();
        }).and().dontReuseHttpClientInstance());
        RestAssured.replaceFiltersWith(new AllureRestAssured());
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .addHeader("Content-type", "application/json")
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        RestAssured.filters(new ResponseLoggingFilter());
    }
}
