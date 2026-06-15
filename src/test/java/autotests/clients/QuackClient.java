package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class QuackClient extends DuckClient {

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
}
