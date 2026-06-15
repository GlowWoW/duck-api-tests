package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class UpdateClient extends DuckClient {

    public void updateDuck(TestCaseRunner runner, String color, double height, String duckID, String material, String sound, String wingsState) {
        String path = "/api/duck/update" + "?color=" + color + "&height=" + height + "&id=" + duckID + "&material=" + material + "&sound=" + sound + "&wingsState=" + wingsState;
        runner.$(http()
                .client(duckService)
                .send()
                .put(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    public void updateDuck(TestCaseRunner runner, String duckId, Object payload) {
        String path = "/api/duck/update";
        runner.$(http()
                .client(duckService)
                .send()
                .put(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("id", duckId)
                .body(new ObjectMappingPayloadBuilder(payload, new ObjectMapper())));

    }

    public void validateResponse(TestCaseRunner runner, String body) {
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
