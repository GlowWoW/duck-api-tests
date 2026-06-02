package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class PropertiesClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    public void propertiesDuck(TestCaseRunner runner, String duckID) {
        String path = "/api/duck/action/properties";
        runner.$(http()
                .client(duckService)
                .send()
                .get(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("id", duckID));
    }

    public void validateResponse(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState, Boolean isEmpty) {
        String body = "{}";
        if (!isEmpty) {
            body = "{\n" +
                    "\"color\": \"" + color + "\",\n" +
                    "\"height\": " + height + ",\n" +
                    "\"material\": \"" + material + "\",\n" +
                    "\"sound\": \"" + sound + "\",\n" +
                    "\"wingsState\": \"" + wingsState + "\"\n" + "}";
        }
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(body));
    }

    public void duckDelete(TestCaseRunner runner, String duckId) {
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
