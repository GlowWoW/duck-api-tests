package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class FlyClient extends DuckClient {
    @Autowired
    protected HttpClient duckService;

    @Step("Эндпоинт для полета утки")
    public void flyDuck(TestCaseRunner runner, String duckId) {
        actionDuck(runner, "fly", duckId);
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
}
