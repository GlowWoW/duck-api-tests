package autotests.tests.duckActionControllerTests;

import autotests.clients.DuckClient;
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

@Epic("Тесты duck-action-controller")
@Feature("Плаванье уточки")
@Story("Эндпоинт /api/duck/action/swim")
public class SwimTest extends DuckClient {
    @Test(description = "Заставить поплыть существующую утку")
    @CitrusTest
    public void successfulSwimExist(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        String sqlInsert = "insert into duck (id,color, height, material,sound, wings_state) values (${duckId},'orange', 0.03,'cheese','boo', 'ACTIVE');";
        databaseUpdate(runner, sqlInsert);
        swimDuck(runner, "${duckId}");
        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("Paws are not found ((((");
        validateResponse(runner, expectedResponse, HttpStatus.NOT_FOUND);
        deleteDuck(runner, "${duckId}");
    }


    @Test(description = "Заставить поплыть несуществующую утку")
    @CitrusTest
    public void successfulSwimNoExist(@Optional @CitrusResource TestCaseRunner runner) {
        String duckIdNoExists = "99999"; //Несуществующий ID
        swimDuck(runner, duckIdNoExists);
        validateResponseResources(runner, "swimTest/DuckSwimMessageResponse.json", HttpStatus.NOT_FOUND);
    }
}
