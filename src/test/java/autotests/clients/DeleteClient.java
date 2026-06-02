package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.consol.citrus.http.client.HttpClient;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DeleteClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

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

    public String getDuckId(TestCaseRunner runner) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
        return "${duckId}";
    }

    public void validateResponse(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\"message\":\"Duck is deleted\"}"));
    }

    public void duckDelete(TestCaseRunner runner, String duckId) { //Удаление после проверки метода, для корректной работы следующих тестов
        String path = "/api/duck/delete";
        runner.$(http()
                .client(duckService)
                .send()
                .delete(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("id", duckId));
    }
}
