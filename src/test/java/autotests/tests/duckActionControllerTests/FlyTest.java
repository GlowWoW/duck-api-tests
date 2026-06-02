package autotests.tests.duckActionControllerTests;

import autotests.clients.FlyClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class FlyTest extends FlyClient {
    @Test(description = "Заставить полететь утку с активными крыльями")
    @CitrusTest
    public void successfulFlyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "ACTIVE");
        flyDuck(runner, getDuckId(runner));
        validateResponse(runner, "I am flying :)");
        duckDelete(runner, "${duckId}");
    }

    @Test(description = "Заставить полететь утку со связанными крыльями")
    @CitrusTest
    public void successfulFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        flyDuck(runner, getDuckId(runner));
        validateResponse(runner, "I can not fly :C");
        duckDelete(runner, "${duckId}");
    }

    @Test(description = "Заставить полететь утку с крыльями в неопределенном состоянии")
    @CitrusTest
    public void successfulFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "UNDEFINED");
        flyDuck(runner, getDuckId(runner));
        validateResponse(runner, "Wings are not detected :(");
        duckDelete(runner, "${duckId}");
    }
}
