package autotests.tests.duckActionControllerTests;

import autotests.clients.SwimClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class SwimTest extends SwimClient {
    @Test(description = "Заставить поплыть существующую утку")
    @CitrusTest
    public void successfulSwimExist(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        swimDuck(runner, getDuckId(runner));
        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("Paws are not found ((((");
        validateResponseStatusBody(runner, expectedResponse, HttpStatus.NOT_FOUND);
        duckDelete(runner, "${duckId}");
    }

    @Test(description = "Заставить поплыть несуществующую утку")
    @CitrusTest
    public void successfulSwimNoExist(@Optional @CitrusResource TestCaseRunner runner) {
        int duckIdNoExist = 99999; //Несуществующий ID
        swimDuck(runner, Integer.toString(duckIdNoExist));
        validateResponseResourcesStatusBody(runner, "swimTest/DuckSwimMessageResponse.json", HttpStatus.NOT_FOUND);
    }
}
