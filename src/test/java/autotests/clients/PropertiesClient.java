package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class PropertiesClient extends DuckClient {
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

    public void validateResponse(TestCaseRunner runner, Object expectedPayload) {
        if (expectedPayload == null) {
            runner.$(
                    http()
                            .client(duckService)
                            .receive()
                            .response(HttpStatus.OK)
                            .message()
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .type(MessageType.JSON)
                            .body("{}"));
        } else {
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
    }
}
