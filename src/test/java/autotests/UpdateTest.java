package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class UpdateTest extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        String path = "/api/duck/create";
        runner.$(http()
                .client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "\"color\": \"" + color + "\",\n" +
                        "\"height\": " + height + ",\n" +
                        "\"material\": \"" + material + "\",\n" +
                        "\"sound\": \"" + sound + "\",\n" +
                        "\"wingsState\": \"" + wingsState + "\"\n" + "}"));
    }

    public String getDuckId(TestCaseRunner runner) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
        return "${duckId}";
    }

    public void updateDuck(TestCaseRunner runner, String color, double height, String duckID, String material, String sound, String wingsState) {
        String path = "/api/duck/update" + "?color=" + color + "&height=" + height + "&id=" + duckID + "&material=" + material + "&sound=" + sound + "&wingsState=" + wingsState;
        runner.$(http()
                .client(URL)
                .send()
                .put(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    public void validateResponse(TestCaseRunner runner, String duckId) {
        String body = "{\"message\":\"Duck with id = " + duckId + " is updated\"}";
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(body));
    }

    @Test(description = "Обновление цвета и высоты утки")
    @CitrusTest
    public void successfulUpdateColorHeight(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "woodd", "quack", "FIXED");
        updateDuck(runner, "black", 0.05, getDuckId(runner), "wood", "quack", "FIXED");
        validateResponse(runner, "${duckId}"); //Валидация обновления
        duckDelete(runner, "${duckId}");

    }

    @Test(description = "Обновление цвета и звука утки")
    @CitrusTest
    public void successfulUpdateColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "woodd", "quack", "FIXED");
        updateDuck(runner, "white", 0.05, getDuckId(runner), "wood", "noquack", "FIXED");
        validateResponse(runner, "${duckId}"); //Валидация обновления
        duckDelete(runner, "${duckId}");

    }

    public void duckDelete(TestCaseRunner runner, String duckId) { //Удаление после проверки метода, для корректной работы следующих тестов
        String path = "/api/duck/delete?id=" + duckId;
        runner.$(http()
                .client(URL)
                .send()
                .delete(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
