package autotests.tests.duckActionControllerTests;

import autotests.clients.PropertiesClient;
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

//Четность не имеет значения в методе Properties, в каждом тесте реализуется 2 создания
@Epic("Тесты duck-action-controller")
@Feature("Свойства уточки")
@Story("Эндпоинт /api/duck/action/properties")
public class PropertiesTest extends PropertiesClient {

    @Test(description = "Свойства утки с материалом wood, Payloads")
    @CitrusTest
    public void successfulPropertiesWoodPayloads(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'wood','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);
        propertiesDuck(runner, "${duckId}");
        validateResponse(runner, null);
    }

    @Test(description = "Свойства утки с материалом wood, Resources")
    @CitrusTest
    public void successfulPropertiesWoodResources(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'wood','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);
        propertiesDuck(runner, "${duckId}");
        validateResponseResources(runner, "propertiesWood/DuckPropertiesWoodResponse.json");
    }


    @Test(description = "Свойства утки с материалом rubber, Resources")
    @CitrusTest
    public void successfulPropertiesRubberResources(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);

        propertiesDuck(runner, "${duckId}");
        validateResponseResources(runner, "propertiesRubber/DuckPropertiesRubberResponse.json");
    }

    @Test(description = "Свойства утки с материалом rubber, Payloads")
    @CitrusTest
    public void successfulPropertiesRubberPayloads(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);

        propertiesDuck(runner, "${duckId}");
        DuckProperties expectedResponse = new DuckProperties()
                .color("yellow")
                .height(3)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        validateResponse(runner, expectedResponse);
    }
}
