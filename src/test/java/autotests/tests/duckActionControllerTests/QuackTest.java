package autotests.tests.duckActionControllerTests;

import autotests.clients.QuackClient;
import autotests.payloads.response.DuckQuackResponse;
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
@Feature("Кряканье уточки")
@Story("Эндпоинт /api/duck/action/quack")
public class QuackTest extends QuackClient {
    @Test(description = "Заставить крякать уточку с нечетным id и корректным звуком, Payloads")
    @CitrusTest
    public void successfulQuackOddGoodSound(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        String duckId = "999999";
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=" + duckId)));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (" + duckId + ",'yellow', 0.03,'wood','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);

        quackDuck(runner, duckId);
        DuckQuackResponse expectedResponse = new DuckQuackResponse()
                .sound("quack-quack"); //"repetitionCount"="2", "soundCount"="1"
        validateResponse(runner, expectedResponse);
    }

    @Test(description = "Заставить крякать уточку с четным id и некорректным звуком, Resources")
    @CitrusTest
    public void successfulQuackEvenBadSound(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        String duckId = "1000000";
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=" + duckId)));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (" + duckId + ",'yellow', 0.03,'wood','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);

        quackDuck(runner, duckId);
        validateResponseResources(runner, "quackTest/DuckQuackMessageResponse.json");
        duckDelete(runner, duckId);
    }
}
