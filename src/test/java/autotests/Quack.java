package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class Quack extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void quackDuck(TestCaseRunner runner, int duckID, int repetitionCount, int soundCount) {
        runner.$(http()
                .client(URL)
                .send()
                .get("/api/duck/action/quack?id=" + duckID + "&repetitionCount=" + repetitionCount + "&soundCount=" + soundCount)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    public void validateResponse(TestCaseRunner runner, int repetitionCount, int soundCount, String sound) {
        StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder.append("\"");
        for (int i = 1; i <= repetitionCount; i++) {
            stringBuilder.append(sound);
            for (int j = 1; j < soundCount; j++) {
                stringBuilder.append("-" + sound);
            }
            if (i < repetitionCount) stringBuilder.append(", ");
        }
        //stringBuilder.append("\"");
        System.out.println("=========Сформированный body: " + stringBuilder.toString());

        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" +
                                "\"sound\": \"" + stringBuilder.toString() +
                                "\"\n}"));
    }

    public void propertiesDuck(TestCaseRunner runner, int duckID) {
        runner.$(http()
                .client(URL)
                .send()
                .get("/api/duck/action/properties?id=" + duckID)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.sound", "sound")));
    }

    @Test(description = "Заставить крякать уточку с нечетным id и корректным звуком")
    @CitrusTest
    public void successfulQuackOddGoodSound(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        int duckIdOdd = 9; //Нечётный существующий id, звук "quack"
        int repetitionCount = 2;
        int soundCount = 2;
        propertiesDuck(runner, duckIdOdd);//Получить звук
        String sound = context.getVariable("sound");
        quackDuck(runner, duckIdOdd, repetitionCount, soundCount);
        validateResponse(runner, repetitionCount, soundCount, sound); //Валидация
    }

    @Test(description = "Заставить крякать уточку с четным id и некорректным звуком")
    @CitrusTest
    public void successfulQuackEvenBadSound(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        int duckIdOdd = 12; //Чётный существующий id, звук отличный "quack" (любой другой звук возвращается как "moo"), валидацию не пройдет
        int repetitionCount=2;
        int soundCount=2;
        propertiesDuck(runner, duckIdOdd);//Получить звук
        String sound = context.getVariable("sound");
        quackDuck(runner, duckIdOdd,repetitionCount,soundCount);
        validateResponse(runner,repetitionCount,soundCount,sound); //Валидация
    }
}
