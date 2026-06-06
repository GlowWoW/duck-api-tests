package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

@ContextConfiguration(classes = {EndpointConfig.class})
public class CreateClient extends DuckClient {
    @Autowired
    protected HttpClient duckService;

    public void validateResponse(TestCaseRunner runner, String duckId, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
                        .validate(jsonPath()
                                .expression("$.id", duckId) //Нельзя проверить "@isNumber()@" через .body
                                .expression("$.color", color)
                                .expression("$.height", height)
                                .expression("$.material", material)
                                .expression("$.sound", sound)
                                .expression("$.wingsState", wingsState)));
    }

    public void validateResponse(TestCaseRunner runner, Object expectedPayload) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
                        .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    public void validateResponse(TestCaseRunner runner, String resourcePath) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
                        .body(new ClassPathResource(resourcePath)));
    }
}
