package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class Swim extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void swimDuck(TestCaseRunner runner, int duckID) {
        runner.$(http()
                .client(URL)
                .send()
                .get("/api/duck/action/swim?id=" + duckID)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    public void validateResponse(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        //.response(HttpStatus.NOT_FOUND) //Для всех случаев будет код 404, метод /api/duck/action/swim работает не корректно.
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n\"message\": \"I’m swimming\"\n}"));
        //.body("{\n\"message\": \"Paws are not found ((((\"\n}"));
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http()
                .client(URL)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "\"color\": \"" + color + "\",\n" +
                        "\"height\": " + height + ",\n" +
                        "\"material\": \"" + material + "\",\n" +
                        "\"sound\": \"" + sound + "\",\n" +
                        "\"wingsState\": \"" + wingsState + "\"\n" + "}"));

        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    @Test(description = "Заставить поплыть существующую утку")
    @CitrusTest
    public void successfulSwimExist(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        String duckId = context.getVariable("duckId");
        swimDuck(runner, Integer.parseInt(duckId)); //Существующий ID
        validateResponse(runner); //Вернет код 404
    }

    @Test(description = "Заставить поплыть несуществующую утку")
    @CitrusTest
    public void successfulSwimNoExist(@Optional @CitrusResource TestCaseRunner runner) {
        int duckIdExist = 99999; //Несуществующий ID
        swimDuck(runner, duckIdExist);
        validateResponse(runner); //Вернет код 404
    }
}
