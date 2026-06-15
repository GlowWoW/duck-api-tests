package autotests.clients;

import com.consol.citrus.TestCaseRunner;

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
