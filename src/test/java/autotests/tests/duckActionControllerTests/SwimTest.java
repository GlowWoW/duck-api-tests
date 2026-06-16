package autotests.tests.duckActionControllerTests;

import autotests.clients.SwimClient;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты duck-action-controller")
@Feature("Плаванье уточки")
@Story("Эндпоинт /api/duck/action/swim")
public class SwimTest extends SwimClient {
    @Test(description = "Заставить поплыть существующую утку")
    @CitrusTest
    public void successfulSwimExist(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "insert into duck (id,color, height, material,sound, wings_state) values (${duckId},'orange', 0.03,'cheese','boo', 'ACTIVE');";
        databaseUpdate(runner, sqlInsert);
        swimDuck(runner, "${duckId}");
        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("Paws are not found ((((");
        validateResponseStatus(runner, expectedResponse, HttpStatus.NOT_FOUND);
    }

    @Test(description = "Заставить поплыть несуществующую утку")
    @CitrusTest
    public void successfulSwimNoExist(@Optional @CitrusResource TestCaseRunner runner) {
        int duckIdNoExist = 99999; //Несуществующий ID
        swimDuck(runner, Integer.toString(duckIdNoExist));
        validateResponseResourcesStatus(runner, "swimTest/DuckSwimMessageResponse.json", HttpStatus.NOT_FOUND);
    }
}
