package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class SwimClient extends DuckClient {
    @Step("Эндпоинт для плавания утки")
    public void swimDuck(TestCaseRunner runner, String duckId) {
        actionDuck(runner, "swim", duckId);
    }

    public void validateResponse(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.NOT_FOUND) //Для всех случаев будет код 404, метод /api/duck/action/swim работает не корректно.
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n\"message\": \"Paws are not found ((((\"\n}"));
    }

}
