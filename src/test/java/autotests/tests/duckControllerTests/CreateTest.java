package autotests.tests.duckControllerTests;

import autotests.clients.CreateClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class CreateTest extends CreateClient {
    @Test(description = "Создание утки с material = rubber")
    @CitrusTest
    public void createRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "rubber", "quack", "FIXED");
        validateResponse(runner, "@isNumber()@", "yellow", 0.03, "rubber", "quack", "FIXED");
        duckDelete(runner, "${duckId}");//Удаление после создания
    }

    @Test(description = "Создание утки с material = rubber, с помощью payloads")
    @CitrusTest
    public void createRubberWithPayloads(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);

        DuckPropertiesResponse expectedResponse = new DuckPropertiesResponse()
                .color("yellow")
                .height(0.03)
                .id("@isNumber()@") //Пришлось в payloads для id объявить тип Object
                .material("rubber")
                .sound("quack")
                .wingsState(DuckPropertiesResponse.WingsState.FIXED);
        validateResponse(runner, expectedResponse);
        duckDelete(runner, "${duckId}");//Удаление после создания
    }

    @Test(description = "Создание утки с material = rubber, с помощью resources")
    @CitrusTest
    public void createRubberWithResources(@Optional @CitrusResource TestCaseRunner runner) {
        createDuckResources(runner, "createTest/DuckProperties.json");
        validateResponse(runner, "createTest/DuckPropertiesResponse.json");
        duckDelete(runner, "${duckId}");//Удаление после создания
    }

    @Test(description = "Создание утки с material = wood")
    @CitrusTest
    public void createWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "FIXED");
        validateResponse(runner, "@isNumber()@", "yellow", 0.03, "wood", "quack", "FIXED");
        duckDelete(runner, "${duckId}");//Удаление после создания
    }
}


