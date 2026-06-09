package autotests.tests.duckControllerTests;

import autotests.clients.UpdateClient;
import autotests.payloads.response.DuckMessageResponse;
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
@Feature("Обновление уточки")
@Story("Эндпоинт /api/duck/update")
public class UpdateTest extends UpdateClient {

    @Test(description = "Обновление цвета и высоты утки, string")
    @CitrusTest
    public void successfulUpdateColorHeight(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "FIXED");
        updateDuck(runner, "black", 0.05, "${duckId}", "wood", "quack", "FIXED");
        validateResponse(runner, "${duckId}"); //Валидация обновления

    }

    @Test(description = "Обновление цвета и высоты утки, валидация с помощью payloads")
    @CitrusTest
    public void successfulUpdateColorHeightPayloads(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "FIXED");

        updateDuck(runner, "black", 0.05, "${duckId}", "wood", "quack", "FIXED");
        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("Duck with id = " + "${duckId}" + " is updated");
        validateResponse(runner, expectedResponse);
    }

    @Test(description = "Обновление цвета и звука утки с помощью resources")
    @CitrusTest
    public void successfulUpdateColorSoundResources(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "FIXED");

        updateDuck(runner, "black", 0.05, "${duckId}", "wood", "quack", "FIXED");
        validateResponseResources(runner, "updateTest/DuckUpdateMessageResponse.json");
    }
}
