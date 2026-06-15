package autotests.tests.duckActionControllerTests;

import autotests.clients.QuackClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckQuackResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class QuackTest extends QuackClient {
    @Test(description = "Заставить крякать уточку с нечетным id и корректным звуком, Payloads")
    @CitrusTest
    public void successfulQuackOddGoodSound(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        getDuckId(runner); // извлекает id и sound
        String id = context.getVariable("duckId");
        if (Integer.parseInt(id) % 2 == 0) {
            duckDelete(runner, id);
            createDuck(runner, duckProperties);
            getDuckId(runner);
            id = context.getVariable("duckId");
        }
        quackDuck(runner, id);
        DuckQuackResponse expectedResponse = new DuckQuackResponse()
                .sound("quack-quack"); //"repetitionCount"="2", "soundCount"="1"
        validateResponse(runner, expectedResponse);
        duckDelete(runner, id);
    }

    @Test(description = "Заставить крякать уточку с четным id и некорректным звуком, Resources")
    @CitrusTest
    public void successfulQuackEvenBadSound(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("boooooo")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        getDuckId(runner);
        String id = context.getVariable("duckId");
        if (Integer.parseInt(id) % 2 == 1) {
            duckDelete(runner, id);
            createDuck(runner, duckProperties);
            getDuckId(runner);
        }
        quackDuck(runner, id);
        validateResponseResources(runner, "quackTest/DuckQuackMessageResponse.json");
        duckDelete(runner, id);
    }
}
