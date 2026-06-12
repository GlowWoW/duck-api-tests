package autotests.tests.duckActionControllerTests;

import autotests.clients.DuckClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

//Четность не имеет значения в методе Properties, в каждом тесте реализуется 2 создания
@Epic("Тесты duck-action-controller")
@Feature("Свойства уточки")
@Story("Эндпоинт /api/duck/action/properties")
public class PropertiesTest extends DuckClient {
    @Test(description = "Параметризованный тест с материалом = wood", dataProvider = "ducksWood")
    @CitrusTest
    @CitrusParameters({"duckId", "color", "height", "material", "sound", "wingsState", "runner"})
    public void createWoodDataTest(String duckId, String color, double height, String material, String sound, String wingsState, @Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=" + duckId)));
        databaseCreate(runner, duckId, color, height, material, sound, wingsState);
        propertiesDuck(runner, duckId);
        validateResponseResources(runner, "propertiesWood/DuckPropertiesWoodResponse.json");
    }

    @DataProvider(name = "ducksWood")
    public Object[][] ducksWood() {
        return new Object[][]{
                {"1001", "white", 0.03, "wood", "quack", "FIXED", null},
                {"1002", "yellow", 4, "wood", "bork", "ACTIVE", null},
                {"1003", "red", 0.05, "wood", "quack", "UNDEFINED", null},
                {"1004", "black", 6, "wood", "bork", "FIXED", null},
                {"1005", "green", 0.07, "wood", "quack", "ACTIVE", null}
        };
    }

    @Test(description = "Параметризованный тест с материалом = rubber", dataProvider = "ducksRubber")
    @CitrusTest
    @CitrusParameters({"duckId", "color", "height", "material", "sound", "wingsState", "expectedResponse", "runner"})
    public void createRubberDataTest(String duckId, String color, double height, String material, String sound, String wingsState, DuckPropertiesResponse expectedResponse, @Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=" + duckId)));
        databaseCreate(runner, duckId, color, height, material, sound, wingsState);
        propertiesDuck(runner, duckId);
        validateResponse(runner, expectedResponse);
    }

    @DataProvider(name = "ducksRubber")
    public Object[][] ducksRubber() {
        return new Object[][]{
                {"2001", "white", 0.03, "rubber", "quack", "FIXED", expectedResponse1, null},
                {"2002", "yellow", 4, "rubber", "bork", "ACTIVE", expectedResponse2, null},
                {"2003", "red", 0.05, "rubber", "quack", "UNDEFINED", expectedResponse3, null},
                {"2004", "black", 6, "rubber", "bork", "FIXED", expectedResponse4, null},
                {"2005", "green", 0.03, "rubber", "quack", "ACTIVE", expectedResponse5, null}
        };
    }

    DuckPropertiesResponse expectedResponse1 = new DuckPropertiesResponse()
            .color("white")
            .height(3)
            .material("rubber")
            .sound("quack")
            .wingsState(DuckPropertiesResponse.WingsState.FIXED);

    DuckPropertiesResponse expectedResponse2 = new DuckPropertiesResponse()
            .color("yellow")
            .height(400)
            .material("rubber")
            .sound("bork")
            .wingsState(DuckPropertiesResponse.WingsState.ACTIVE);

    DuckPropertiesResponse expectedResponse3 = new DuckPropertiesResponse()
            .color("red")
            .height(5)
            .material("rubber")
            .sound("quack")
            .wingsState(DuckPropertiesResponse.WingsState.UNDEFINED);

    DuckPropertiesResponse expectedResponse4 = new DuckPropertiesResponse()
            .color("black")
            .height(600)
            .material("rubber")
            .sound("bork")
            .wingsState(DuckPropertiesResponse.WingsState.FIXED);

    DuckPropertiesResponse expectedResponse5 = new DuckPropertiesResponse()
            .color("green")
            .height(3)
            .material("rubber")
            .sound("quack")
            .wingsState(DuckPropertiesResponse.WingsState.ACTIVE);


    @Test(description = "Свойства утки с материалом wood, Resources")
    @CitrusTest
    public void successfulPropertiesWoodResources(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'wood','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);
        propertiesDuck(runner, "${duckId}");
        validateResponseResources(runner, "propertiesWood/DuckPropertiesWoodResponse.json");
    }

    @Test(description = "Свойства утки с материалом rubber, Payloads")
    @CitrusTest
    public void successfulPropertiesRubberPayloads(@Optional @CitrusResource TestCaseRunner runner) {
        getNextIdDB(runner);
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'rubber','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);

        propertiesDuck(runner, "${duckId}");
        DuckProperties expectedResponse = new DuckProperties()
                .color("yellow")
                .height(3)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        validateResponse(runner, expectedResponse);
    }
}
