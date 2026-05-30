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


import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class GetAllBirds extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

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

    public void validateResponse(TestCaseRunner runner, String duckId1, String duckId2) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("[" + duckId1 + ", " + duckId2 + "]"));
    }

    @Test(description = "Получение Id существующих уток")
    @CitrusTest
    public void successfulGetAllBirds(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "FIXED");//Создать 1ю птицу
        String duckId1 = context.getVariable("duckId");
        createDuck(runner, "yellow", 0.03, "wood", "quack", "FIXED");//Создать 2ю птицу
        String duckId2 = context.getVariable("duckId");
        //String duckId = context.getVariable("duckId");
        //System.out.println("======Создана утка с ID: " + duckId);
        duckGetAllBirds(runner);
        validateResponse(runner, duckId1, duckId2); //В правильной валидации должно проверяться сколь угодно значений, а не только лишь 2. Может поэтому в ДЗ метод GetAllBirds не задан. Захотелось разобраться.
        duckDelete(runner, duckId1);//Удалить утку 1 после теста
        duckDelete(runner, duckId2);//Удалить утку 2 после теста
    }

    public void duckDelete(TestCaseRunner runner, String duckId) {
        runner.$(http()
                .client(URL)
                .send()
                .delete("/api/duck/delete?id=" + duckId)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));

    }

    public void duckGetAllBirds(TestCaseRunner runner) {
        runner.$(http()
                .client(URL)
                .send()
                .get("/api/duck/getAllIds")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));

    }
}
