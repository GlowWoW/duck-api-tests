package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class PropertiesClient extends DuckClient {

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

}
