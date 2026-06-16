package autotests.tests.duckControllerTests;

import autotests.clients.CreateClient;
import autotests.payloads.request.DuckProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты duck-controller")
@Feature("Создание уточки")
@Story("Эндпоинт /api/duck/create")
public class CreateTest extends CreateClient {
    @Test(description = "Создание утки с material = rubber, с помощью payloads")
    @CitrusTest
    public void createRubberWithPayloads(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        validateResponse(runner, "@isNumber()@", "yellow", 0.03, "rubber", "quack", "FIXED");
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "FIXED");
    }

    @Test(description = "Создание утки с material = rubber, с помощью resources")
    @CitrusTest
    public void createWoodWithResources(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        createDuckResources(runner, "createTest/DuckProperties.json");
        validateResponseResources(runner, "createTest/DuckPropertiesResponse.json");
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "FIXED");
    }
}


