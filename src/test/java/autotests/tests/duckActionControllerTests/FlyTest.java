package autotests.tests.duckActionControllerTests;

import autotests.clients.FlyClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class FlyTest extends FlyClient {
    @Test(description = "Заставить полететь утку с активными крыльями")
    @CitrusTest
    public void successfulFlyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.ACTIVE);
        createDuck(runner, duckProperties);
        flyDuck(runner, getDuckId(runner));
        validateResponse(runner, "I am flying :)");
        duckDelete(runner, "${duckId}");
    }

    @Test(description = "Заставить полететь утку со связанными крыльями, с помощью payloads")
    @CitrusTest
    public void successfulFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        flyDuck(runner, getDuckId(runner));
        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("I can not fly :C");
        validateResponse(runner, expectedResponse);
        duckDelete(runner, "${duckId}");
    }

    @Test(description = "Заставить полететь утку с крыльями в неопределенном состоянии, с помощью resources")
    @CitrusTest
    public void successfulFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.UNDEFINED);
        createDuck(runner, duckProperties);
        flyDuck(runner, getDuckId(runner));
        validateResponseResources(runner, "flyTest/DuckFlyMessageResponse.json");
        duckDelete(runner, "${duckId}");
    }
}
