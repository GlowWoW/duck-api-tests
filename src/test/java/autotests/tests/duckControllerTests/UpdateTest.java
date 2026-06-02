package autotests.tests.duckControllerTests;

import autotests.clients.UpdateClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class UpdateTest extends UpdateClient {
    @Test(description = "Обновление цвета и высоты утки")
    @CitrusTest
    public void successfulUpdateColorHeight(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "woodd", "quack", "FIXED");
        updateDuck(runner, "black", 0.05, getDuckId(runner), "wood", "quack", "FIXED");
        validateResponse(runner, "${duckId}"); //Валидация обновления
        duckDelete(runner, "${duckId}");
    }

    @Test(description = "Обновление цвета и звука утки")
    @CitrusTest
    public void successfulUpdateColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.03, "woodd", "quack", "FIXED");
        updateDuck(runner, "white", 0.05, getDuckId(runner), "wood", "noquack", "FIXED");
        validateResponse(runner, "${duckId}"); //Валидация обновления
        duckDelete(runner, "${duckId}");
    }
}
