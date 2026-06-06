package autotests.tests.duckControllerTests;

import autotests.clients.UpdateClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class UpdateTest extends UpdateClient {

    @Test(description = "Обновление цвета и высоты утки, string")
    @CitrusTest
    public void successfulUpdateColorHeight(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "woodd", "quack", "FIXED");
        updateDuck(runner, "black", 0.05, getDuckId(runner), "wood", "quack", "FIXED");
        validateResponse(runner, "${duckId}"); //Валидация обновления
        duckDelete(runner, "${duckId}");
    }

    @Test(description = "Обновление цвета и высоты утки, валидация с помощью payloads")
    @CitrusTest
    public void successfulUpdateColorHeightPayloads(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        updateDuck(runner, "black", 0.05, getDuckId(runner), "wood", "quack", "FIXED");
        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("Duck with id = " + "${duckId}" + " is updated");
        validateResponse(runner, expectedResponse);
        duckDelete(runner, "${duckId}");//Удаление после создания
    }

    @Test(description = "Обновление цвета и звука утки с помощью resources")
    @CitrusTest
    public void successfulUpdateColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "woodd", "quack", "FIXED");
        updateDuck(runner, "white", 0.05, getDuckId(runner), "wood", "noquack", "FIXED");
        validateResponseResources(runner, "updateTest/DuckUpdateMessageResponse.json");
        duckDelete(runner, "${duckId}");
    }
}
