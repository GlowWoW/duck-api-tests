package autotests.tests.duckActionControllerTests;

import autotests.clients.SwimClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class SwimTest extends SwimClient {
    @Test(description = "Заставить поплыть существующую утку")
    @CitrusTest
    public void successfulSwimExist(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        swimDuck(runner, getDuckId(runner)); //Существующий ID
        validateResponse(runner);
        duckDelete(runner, "${duckId}");
    }

    @Test(description = "Заставить поплыть несуществующую утку")
    @CitrusTest
    public void successfulSwimNoExist(@Optional @CitrusResource TestCaseRunner runner) {
        int duckIdNoExist = 99999; //Несуществующий ID
        swimDuck(runner, Integer.toString(duckIdNoExist));
        validateResponse(runner);
    }
}
