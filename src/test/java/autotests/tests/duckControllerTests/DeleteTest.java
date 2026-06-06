package autotests.tests.duckControllerTests;

import autotests.clients.DeleteClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import com.consol.citrus.context.TestContext;

public class DeleteTest extends DeleteClient {
    @Test(description = "Создание утки для последующего удаления")
    @CitrusTest
    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("wood")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        duckDelete(runner, getDuckId(runner));
        validateResponse(runner);
    }

    @Test(description = "Создание утки для последующего удаления, валидация с помощью payloads")
    @CitrusTest
    public void successfulDeletePayloads(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("wood")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        duckDelete(runner, getDuckId(runner));
        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("Duck is deleted");
        validateResponse(runner, expectedResponse);
    }

    @Test(description = "Обновление цвета и звука утки с помощью resources")
    @CitrusTest
    public void successfulUpdateColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "woodd", "quack", "FIXED");
        duckDelete(runner, getDuckId(runner));
        validateResponseResources(runner, "deleteTest/DuckDeleteMessageResponse.json");
        duckDelete(runner, "${duckId}");
    }
}
