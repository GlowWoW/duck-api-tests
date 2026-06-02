package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import com.consol.citrus.context.TestContext;


import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DeleteTest extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        String path="/api/duck/create";
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

    public String getDuckId(TestCaseRunner runner){
        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
        return "${duckId}";
    }

    public void validateResponse(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\"message\":\"Duck is deleted\"}"));
    }

    @Test(description = "Создание утки для последующего удаления")
    @CitrusTest
    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "FIXED");
        duckDelete(runner, getDuckId(runner));
        validateResponse(runner);
    }

    public void duckDelete(TestCaseRunner runner, String duckId) {
        String path="/api/duck/delete?id=" + duckId;
        runner.$(http()
                .client(URL)
                .send()
                .delete(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
