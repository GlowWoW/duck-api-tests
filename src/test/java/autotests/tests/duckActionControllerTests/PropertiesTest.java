package autotests.tests.duckActionControllerTests;

import autotests.clients.PropertiesClient;
import autotests.payloads.request.DuckProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

//Четность не имеет значения в методе Properties, в каждом тесте реализуется 2 создания
public class PropertiesTest extends PropertiesClient {

    @Test(description = "Свойства утки с материалом wood, Payloads")
    @CitrusTest
    public void successfulPropertiesWoodPayloads(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("wood")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        propertiesDuck(runner, getDuckId(runner));
        validateResponse(runner, null);
        duckDelete(runner, "${duckId}");//Удаление после создания
    }

    @Test(description = "Свойства утки с материалом wood, Resources")
    @CitrusTest
    public void successfulPropertiesWoodResources(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("wood")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        propertiesDuck(runner, getDuckId(runner));
        validateResponseResources(runner, "propertiesWood/DuckPropertiesWoodResponse.json");
        duckDelete(runner, "${duckId}");//Удаление после создания
    }


    @Test(description = "Свойства утки с материалом rubber, Resources")
    @CitrusTest
    public void successfulPropertiesRubberResources(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        propertiesDuck(runner, getDuckId(runner));
        validateResponseResources(runner, "propertiesRubber/DuckPropertiesRubberResponse.json");
        duckDelete(runner, "${duckId}");//Удаление после создания
    }

    @Test(description = "Свойства утки с материалом rubber, Payloads")
    @CitrusTest
    public void successfulPropertiesRubberPayloads(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        propertiesDuck(runner, getDuckId(runner));
        DuckProperties expectedResponse = new DuckProperties()
                .color("yellow")
                .height(3)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        validateResponse(runner, expectedResponse);
        duckDelete(runner, "${duckId}");//Удаление после создания
    }
}
