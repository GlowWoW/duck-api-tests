package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class FlyClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    public void flyDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/action/fly";
        runner.$(http()
                .client(duckService)
                .send()
                .get(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("id", duckId));
    }

    public void validateResponse(TestCaseRunner runner, String textReceive) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n\"message\": \"" + textReceive + "\"\n}"));
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http()
                .client(duckService)
                .send()
                .post("/api/duck/create")
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
