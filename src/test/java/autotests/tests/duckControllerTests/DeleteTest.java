package autotests.tests.duckControllerTests;


import autotests.clients.DuckClient;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Тесты duck-controller")
@Feature("Удаление уточки")
@Story("Эндпоинт /api/duck/delete")
public class DeleteTest extends DuckClient {
    @Test(description = "Создание утки для последующего удаления с помощью resources")
    @CitrusTest
    public void successfulDeleteResources(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "FIXED");
        deleteDuck(runner, "${duckId}");
        validateResponseResources(runner, "deleteTest/DuckDeleteMessageResponse.json");
    }

    @Test(description = "Создание утки для последующего удаления, валидация с помощью payloads")
    @CitrusTest
    public void successfulDeletePayloads(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "FIXED");
        deleteDuck(runner, "${duckId}");
        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("Duck is deleted");
        validateResponse(runner, expectedResponse);
    }
}
