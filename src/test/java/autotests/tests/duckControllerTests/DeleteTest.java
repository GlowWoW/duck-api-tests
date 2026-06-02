package autotests.tests.duckControllerTests;

import autotests.clients.DeleteClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import com.consol.citrus.context.TestContext;

public class DeleteTest extends DeleteClient {
    @Test(description = "Создание утки для последующего удаления")
    @CitrusTest
    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        createDuck(runner, "yellow", 0.03, "wood", "quack", "FIXED");
        duckDelete(runner, getDuckId(runner));
        validateResponse(runner);
    }
}
