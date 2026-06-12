package autotests.tests.duckControllerTests;

import autotests.clients.CreateClient;
import autotests.payloads.request.DuckProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Тесты duck-controller")
@Feature("Создание уточки")
@Story("Эндпоинт /api/duck/create")
public class CreateTest extends CreateClient {
    @Test(description = "Создание утки с material = rubber, с помощью payloads")
    @CitrusTest
    public void createRubberWithPayloads(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId", "citrus:randomNumber(6)");
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(0.03)
                .material("rubber")
                .sound("quack")
                .wingsState(DuckProperties.WingsState.FIXED);
        createDuck(runner, duckProperties);
        validateResponse(runner, "@isNumber()@", "yellow", 0.03, "rubber", "quack", "FIXED");
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "rubber", "quack", "FIXED");
        deleteDuckFromDB(runner, "${duckId}");
    }

    @Test(description = "Создание утки с material = rubber, с помощью resources")
    @CitrusTest
    public void createRubberWithResources(@Optional @CitrusResource TestCaseRunner runner) {
        createDuckResources(runner, "createTest/DuckProperties.json");
        validateResponseResources(runner, "createTest/DuckPropertiesResponse.json");
        deleteDuck(runner, "${duckId}");//Удаление после создания
    }

    @Test(description = "Создание утки с material = wood")
    @CitrusTest
    public void createWood(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId", "citrus:randomNumber(6)");
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (${duckId},'yellow', 0.03,'wood','quack', 'FIXED');";
        databaseUpdate(runner, sqlInsert);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.03", "wood", "quack", "FIXED");
        deleteDuckFromDB(runner, "${duckId}");
    }

    /* //Параметризованный тест для создания. Не используется
    DuckProperties duckProperties = new DuckProperties()
            .color("yellow")
            .height(0.03)
            .material("rubber")
            .sound("quack")
            .wingsState(DuckProperties.WingsState.FIXED);

    @Test(description = "Параметризованный тест", dataProvider="ducks")
    @CitrusTest
    @CitrusParameters({"properties","expected","runner"})
    public void createRubber1(DuckProperties properties, DuckPropertiesResponse expected, @Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, properties);
    }

    @DataProvider(name="ducks")
    public Object[][] duckProvider(){
        return new Object[][]{
                {properties1,expectedResponse1,null},
                {properties1,expectedResponse1,null},
                {properties1,expectedResponse1,null}
        };
    }*/
}


