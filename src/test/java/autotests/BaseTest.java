package autotests;

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
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})

public class BaseTest extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDb;

    @Step("Обновление БД")
    public void databaseUpdate(TestCaseRunner runner, String query) {
        runner.$(sql(testDb).statement(query));
    }

    @Step("Запрос методом post (Object)")
    public void sendPostMethodObject(TestCaseRunner runner, String path, Object body, HttpClient httpClient) {
        runner.$(http()
                .client(httpClient)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    @Step("Запрос методом post (Resources)")
    public void sendPostMethodResources(TestCaseRunner runner, String path, String resource, HttpClient httpClient) {
        runner.$(http()
                .client(httpClient)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(resource)));
    }

    @Step("Запрос методом post ")
    public void sendPostMethod(TestCaseRunner runner, String path, String resource, HttpClient httpClient) {
        runner.$(http()
                .client(httpClient)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(resource));
    }

    @Step("Запрос методом put")
    public void sendPutMethod(TestCaseRunner runner, String path, HttpClient httpClient) {
        runner.$(http()
                .client(httpClient)
                .send()
                .put(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Step("Запрос методом get")
    public void sendGetMethod(TestCaseRunner runner, String path, HttpClient httpClient) {
        runner.$(http()
                .client(httpClient)
                .send()
                .get(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Step("Запрос методом delete")
    public void sendDeleteMethod(TestCaseRunner runner, String path, String queryName, String queryParameter, HttpClient httpClient) {
        runner.$(http()
                .client(httpClient)
                .send()
                .delete(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(queryName, queryParameter));
    }

    @Step("Эндпоинт для action get-методов, требующих 1 query-параметр")
    public void sendGetQueryMethod(TestCaseRunner runner, String path, String queryName, String queryParameter, HttpClient httpClient) {
        runner.$(http()
                .client(httpClient)
                .send()
                .get(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(queryName, queryParameter));
    }

    @Step("Валидация через String")
    public void validateResponse(TestCaseRunner runner, String body, HttpStatus status) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(status)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .body(body));
    }

    @Step("Валидация через resources")
    public void validateResponseResources(TestCaseRunner runner, String resourcePath, HttpStatus status) {
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

    @Step("Валидация через payload")
    public void validateResponse(TestCaseRunner runner, Object expectedPayload, HttpStatus status) {
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


}
