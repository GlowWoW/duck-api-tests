package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class UpdateClient extends DuckClient {
    @Autowired
    protected HttpClient duckService;

    @Step("Обновление через String")
    public void updateDuck(TestCaseRunner runner, String color, double height, String duckID, String material, String sound, String wingsState) {
        String path = "/api/duck/update" + "?color=" + color + "&height=" + height + "&id=" + duckID + "&material=" + material + "&sound=" + sound + "&wingsState=" + wingsState;
        runner.$(http()
                .client(duckService)
                .send()
                .put(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Step("Обновление через payload")
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

    @Step("Обновление через resources")
    public void validateResponse(TestCaseRunner runner, String duckId) {
        String body = "{\"message\":\"Duck with id = " + duckId + " is updated\"}";
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
