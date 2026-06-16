package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class QuackClient extends DuckClient {
    @Step("Эндпоинт для кряканья утки")
    public void quackDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/action/quack"; //Перепутаны повторения и число звуков
        runner.$(http()
                .client(duckService)
                .send()
                .get(path)
                .queryParam("id", duckId)
                .queryParam("repetitionCount", "2")
                .queryParam("soundCount", "1"));
    }


    public void validateResponse(TestCaseRunner runner, String sound) {
        if (!"quack" .equals(sound)) {
            sound = "moo";
        }
        String body = "{\n" +
                "\"sound\": \"" + sound + "-" + sound +
                "\"\n}";
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .body(body));
    }
}
