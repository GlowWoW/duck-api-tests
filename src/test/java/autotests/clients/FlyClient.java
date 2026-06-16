package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class FlyClient extends DuckClient {

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
