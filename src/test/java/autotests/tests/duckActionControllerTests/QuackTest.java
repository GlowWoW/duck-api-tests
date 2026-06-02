package autotests.tests.duckActionControllerTests;

import autotests.clients.QuackClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class QuackTest extends QuackClient {
    @Test(description = "Заставить крякать уточку с нечетным id и корректным звуком")
    @CitrusTest
    public void successfulQuackOddGoodSound(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        int duckIdOdd = 27; //Нечётный существующий id, звук "quack". Обязательно с материалом rubber, см. PropertiesTest
        propertiesDuck(runner, duckIdOdd);
        String sound = getSound(runner, context); //Получить звук
        quackDuck(runner, Integer.toString(duckIdOdd));
        validateResponse(runner, sound); //Валидация
    }

    @Test(description = "Заставить крякать уточку с четным id и некорректным звуком")
    @CitrusTest
    public void successfulQuackEvenBadSound(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        int duckIdOdd = 12; //Чётный существующий id, звук отличный "quack" (любой другой звук возвращается как "moo"), валидацию не пройдет
        propertiesDuck(runner, duckIdOdd);
        String sound = getSound(runner, context); //Получить звук
        quackDuck(runner, Integer.toString(duckIdOdd));
        validateResponse(runner, sound); //Валидация
    }
}
