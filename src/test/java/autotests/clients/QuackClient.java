package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class QuackClient extends DuckClient {
    @Autowired
    protected HttpClient duckService;

    public void quackDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/action/quack"; //Перепутаны повторения и число звуков
        runner.$(http()
                .client(duckService)
                .send()
                .get(path)
                .queryParam("id", duckId)
                .queryParam("repetitionCount", "2")
                .queryParam("soundCount", "1"));
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
                        .client(duckService)
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
                .client(duckService)
                .send()
                .get(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
