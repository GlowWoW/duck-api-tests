package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDb;

    public void databaseUpdate(TestCaseRunner runner, String query) {
        runner.$(sql(testDb).statement(query));
    }

    @Step("Возвращение следующего id для создания утки")
    public void getNextIdDB(TestCaseRunner runner) {
        runner.$(query(testDb)
                .statement("select COALESCE(MAX(id), 0) + 1 as id from DUCK")
                .extract("id", "duckId"));
    }

    @Step("Валидация проверки наличия утки в бд")
    protected void validateDuckInDatabase(TestCaseRunner runner, String duckId, String color, String height,
                                          String material, String sound, String wingsState) {
        runner.$(query(testDb)
                .statement("SELECT * FROM DUCK WHERE ID=" + duckId)
                .validate("COLOR", color)
                .validate("HEIGHT", height)
                .validate("MATERIAL", material)
                .validate("SOUND", sound)
                .validate("WINGS_STATE", wingsState));

    }

    @Step("Создание утки через передачу string в body (json)")
    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        String path = "/api/duck/create";
        runner.$(http()
                .client(duckService)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "\"color\": \"" + color + "\",\n" +
                        "\"height\": " + height + ",\n" +
                        "\"material\": \"" + material + "\",\n" +
                        "\"sound\": \"" + sound + "\",\n" +
                        "\"wingsState\": \"" + wingsState + "\"\n" + "}"));
    }

    @Step("Создание утки через payload")
    public void createDuck(TestCaseRunner runner, Object payload) {
        String path = "/api/duck/create";
        runner.$(http()
                .client(duckService)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(payload, new ObjectMapper())));
    }

    @Step("Создание утки через resources")
    public void createDuckResources(TestCaseRunner runner, String resource) { //Для resources
        String path = "/api/duck/create";
        runner.$(http()
                .client(duckService)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(resource)));
    }

    @Step("Удаление утки через endpoint")
    public void duckDelete(TestCaseRunner runner, String duckId) { //Удаление утки
        String path = "/api/duck/delete";
        runner.$(http()
                .client(duckService)
                .send()
                .delete(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("id", duckId));
    }

    @Step("Валидация через payload")
    public void validateResponse(TestCaseRunner runner, Object expectedPayload) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    @Step("Валидация через resources")
    public void validateResponseResources(TestCaseRunner runner, String resourcePath) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .body(new ClassPathResource(resourcePath)));
    }

    @Step("Эндпоинт для action методов")
    public void actionDuck(TestCaseRunner runner, String action, String duckId) {
        runner.$(http()
                .client(duckService)
                .send()
                .get("/api/duck/action/" + action)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("id", duckId));
    }

    @Step("Валидация через payload, с передачей статуса")
    public void validateResponseStatus(TestCaseRunner runner, Object expectedPayload, HttpStatus status) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(status)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    @Step("Валидация через resources, с передачей статуса")
    public void validateResponseResourcesStatus(TestCaseRunner runner, String resourcePath, HttpStatus status) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(status)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .body(new ClassPathResource(resourcePath)));
    }

}

