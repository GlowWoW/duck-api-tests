package autotests.tests.duckActionControllerTests;

import autotests.clients.FlyClient;
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

@Epic("Тесты duck-action-controller")
@Feature("Полет уточки")
@Story("Эндпоинт /api/duck/action/fly")
public class FlyTest extends FlyClient {
    @Test(description = "Заставить полететь утку с активными крыльями")
    @CitrusTest
    public void successfulFlyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'ACTIVE');";
        databaseUpdate(runner, sqlInsert);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "ACTIVE");

        flyDuck(runner, "${duckId}");
        validateResponse(runner, "I am flying :)");
    }

    @Test(description = "Заставить полететь утку со связанными крыльями, с помощью payloads")
    @CitrusTest
    public void successfulFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "FIXED");

        flyDuck(runner, "${duckId}");
        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("I can not fly :C");
        validateResponse(runner, expectedResponse);
    }

    @Test(description = "Заставить полететь утку с крыльями в неопределенном состоянии, с помощью resources")
    @CitrusTest
    public void successfulFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'UNDEFINED');";
        databaseUpdate(runner, sqlInsert);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "UNDEFINED");

        flyDuck(runner, "${duckId}");
        validateResponseResources(runner, "flyTest/DuckFlyMessageResponse.json");
    }
}
