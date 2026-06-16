package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class PropertiesClient extends DuckClient {
    @Step("Эндпоинт для свойств утки")
    public void propertiesDuck(TestCaseRunner runner, String duckId) {
        actionDuck(runner, "properties", duckId);
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
