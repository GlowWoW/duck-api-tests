package autotests.tests.duckControllerTests;

import autotests.clients.CreateClient;
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

    @Test(description = "Создание утки с material = wood")
    @CitrusTest
    public void createWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "FIXED");
        validateResponse(runner, "@isNumber()@", "yellow", 0.03, "wood", "quack", "FIXED");
        duckDelete(runner, "${duckId}");//Удаление после создания
    }
}


