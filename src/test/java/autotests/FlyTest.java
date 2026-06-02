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

public class FlyTest extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void flyDuck(TestCaseRunner runner, String duckID) {
        String path = "/api/duck/action/fly?id=" + duckID;
        runner.$(http()
                .client(URL)
                .send()
                .get(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    public void validateResponse(TestCaseRunner runner, String textReceive) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n\"message\": \"" + textReceive + "\"\n}"));
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

    @Test(description = "Заставить полететь утку с активными крыльями")
    @CitrusTest
    public void successfulFlyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        flyDuck(runner, "${duckId}");
        validateResponse(runner, "I am flying :)");
    }

    @Test(description = "Заставить полететь утку со связанными крыльями")
    @CitrusTest
    public void successfulFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        flyDuck(runner, "${duckId}");
        validateResponse(runner, "I can not fly :C");
    }

    @Test(description = "Заставить полететь утку с крыльями в неопределенном состоянии")
    @CitrusTest
    public void successfulFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "UNDEFINED");
        flyDuck(runner, "${duckId}");
        validateResponse(runner, "Wings are not detected :(");
    }
}
