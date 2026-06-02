package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class CreateTest extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        String path = "/api/duck/create";
        runner.$(http()
                .client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .type(MessageType.JSON)
                .body("{\n" +
                        "\"color\": \"" + color + "\",\n" +
                        "\"height\": " + height + ",\n" +
                        "\"material\": \"" + material + "\",\n" +
                        "\"sound\": \"" + sound + "\",\n" +
                        "\"wingsState\": \"" + wingsState + "\"\n" + "}"));
    }


    public void validateResponse(TestCaseRunner runner, String duckId, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
                        .validate(jsonPath()
                                .expression("$.id", duckId)
                                .expression("$.color", color)
                                .expression("$.height", height)
                                .expression("$.material", material)
                                .expression("$.sound", sound)
                                .expression("$.wingsState", wingsState)));
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

    @Test(description = "Создание утки с material = rubber")
    @CitrusTest
    public void createRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        validateResponse(runner, "@isNumber()@", "yellow", 0.03, "rubber", "quack", "FIXED");
        duckDelete(runner, "${duckId}");//Удаление после создания
    }

    @Test(description = "Создание утки с material = wood")
    @CitrusTest
    public void createWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "FIXED");
        validateResponse(runner, "@isNumber()@", "yellow", 0.03, "wood", "quack", "FIXED");
        duckDelete(runner, "${duckId}");//Удаление после создания
    }
}


