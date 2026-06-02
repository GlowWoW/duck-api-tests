package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class QuackTest extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void quackDuck(TestCaseRunner runner, int duckID) {
        String path = "/api/duck/action/quack?id=" + duckID + "&repetitionCount=2&soundCount=1"; //Перепутаны повторения и число звуков
        runner.$(http()
                .client(URL)
                .send()
                .get(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    public void validateResponse(TestCaseRunner runner, String sound) {
        if (!"quack".equals(sound)) {
            sound = "moo";
        }
        String body = "{\n" +
                "\"sound\": \"" + sound + "-" + sound +
                "\"\n}";
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .body(body));
    }

    public void propertiesDuck(TestCaseRunner runner, int duckID) {
        String path = "/api/duck/action/properties?id=" + duckID;
        runner.$(http()
                .client(URL)
                .send()
                .get(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        runner.$(http()
                .client(URL)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .type(MessageType.JSON)
                .extract(fromBody().expression("$.sound", "sound")));
    }

    @Test(description = "Заставить крякать уточку с нечетным id и корректным звуком")
    @CitrusTest
    public void successfulQuackOddGoodSound(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        int duckIdOdd = 27; //Нечётный существующий id, звук "quack". Обязательно с материалом rubber, см. PropertiesTest
        propertiesDuck(runner, duckIdOdd);//Получить звук
        String sound = context.getVariable("sound");
        quackDuck(runner, duckIdOdd);
        validateResponse(runner, sound); //Валидация
    }

    @Test(description = "Заставить крякать уточку с четным id и некорректным звуком")
    @CitrusTest
    public void successfulQuackEvenBadSound(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        int duckIdOdd = 12; //Чётный существующий id, звук отличный "quack" (любой другой звук возвращается как "moo"), валидацию не пройдет
        propertiesDuck(runner, duckIdOdd);//Получить звук
        String sound = context.getVariable("sound");
        quackDuck(runner, duckIdOdd);
        validateResponse(runner, sound); //Валидация
    }
}
