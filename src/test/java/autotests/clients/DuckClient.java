package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

public class DuckClient extends BaseTest {

    @Step("Создание БД")
    public void databaseCreate(TestCaseRunner runner, String duckId, String color, double height, String material, String sound, String wingsState) {
        String sqlInsert = "INSERT INTO DUCK (id,color, height, material,sound, wings_state) VALUES (" +
                "'" + duckId + "', '" + color + "', " + height + ", '" + material + "', '" + sound + "', '" + wingsState + "')";
        databaseUpdate(runner, sqlInsert);
    }


    @Step("Возвращение следующего id для создания утки")
    public void getNextIdDB(TestCaseRunner runner) {
        runner.$(query(testDb)
                .statement("select COALESCE(MAX(id), 0) + 1 as id from DUCK")
                .extract("id", "duckId"));
    }

    @Step("Валидация проверки наличия утки в бд")
    protected void validateDuckInDatabase(TestCaseRunner runner, String duckId, String color, String height,
                                          String material, String sound, String wingsState) {
        runner.$(query(testDb)
                .statement("SELECT * FROM DUCK WHERE ID=" + duckId)
                .validate("COLOR", color)
                .validate("HEIGHT", height)
                .validate("MATERIAL", material)
                .validate("SOUND", sound)
                .validate("WINGS_STATE", wingsState));

    }

    @Step("Удаление утки через БД")
    protected void deleteDuckFromDB(TestCaseRunner runner, String duckId) {
        runner.$(sql(testDb)
                .statement("DELETE FROM DUCK WHERE ID=" + duckId));
    }

    @Step("Обновление утки через String")
    public void updateDuck(TestCaseRunner runner, String color, double height, String duckID, String material, String sound, String wingsState) {
        String path = "/api/duck/update" + "?color=" + color + "&height=" + height + "&id=" + duckID + "&material=" + material + "&sound=" + sound + "&wingsState=" + wingsState;
        sendPutMethod(runner, path, duckService);
    }

    @Step("Эндпоинт Fly")
    public void flyDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/action/fly";
        sendGetQueryMethod(runner, path, "id", duckId, duckService);
    }

    @Step("Эндпоинт для кряканья утки")
    public void quackDuck(TestCaseRunner runner, String duckId) { //Требуются спецефические параметры repetitionCount и soundCount. Оставил QuackClient
        String path = "/api/duck/action/quack?id=" + duckId + "&repetitionCount=2&soundCount=1"; //Перепутаны повторения и число звуков
        sendGetMethod(runner, path, duckService);
    }

    @Step("Эндпоинт Swim")
    public void swimDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/action/swim";
        sendGetQueryMethod(runner, path, "id", duckId, duckService);
    }

    @Step("Эндпоинт Properties")
    public void propertiesDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/action/properties";
        sendGetQueryMethod(runner, path, "id", duckId, duckService);
    }

    @Step("Обновление утки через String")
    public void deleteDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/delete";
        sendDeleteMethod(runner, path, "id", duckId, duckService);
    }

    @Step("Создание утки через payload")
    public void createDuck(TestCaseRunner runner, Object payload) {
        String path = "/api/duck/create";
        sendPostMethodObject(runner, path, payload, duckService);
    }

    @Step("Создание утки через resources")
    public void createDuckResources(TestCaseRunner runner, String resource) {
        String path = "/api/duck/create";
        sendPostMethodResources(runner, path, resource, duckService);
    }


    @Step("Валидация через payload")
    public void validateResponse(TestCaseRunner runner, Object expectedPayload) {
        validateResponse(runner, expectedPayload, HttpStatus.OK);
    }


    @Step("Валидация через resources")
    public void validateResponseResources(TestCaseRunner runner, String resourcePath) {
        validateResponseResources(runner, resourcePath, HttpStatus.OK);
    }

    @Step("Валидация через String")
    public void validateResponse(TestCaseRunner runner, String body) {
        validateResponse(runner, body, HttpStatus.OK);
    }


}

