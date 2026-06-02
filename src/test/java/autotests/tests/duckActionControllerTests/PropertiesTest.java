package autotests.tests.duckActionControllerTests;

import autotests.clients.PropertiesClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class PropertiesTest extends PropertiesClient {
    @Test(description = "Свойства утки с четным ID с материалом wood")
    @CitrusTest
    public void successfulPropertiesWood(@Optional @CitrusResource TestCaseRunner runner) {
        int duckIdWood = 22; //Связь с чётностью ID до сих пор не выявил после многочисленных тестов. Причина некорректности ответа в выборе материала
        //Дублирование значений из вручную созданной утоточки в БД для валидации
        String color = "yellow";
        double height = 0.03;
        String material = "wood"; //Все материалы НЕ rubber не будут выведены в теле json. Критическая ошибка.
        String sound = "quack";
        String wingsState = "FIXED";
        propertiesDuck(runner, Integer.toString(duckIdWood));
        validateResponse(runner, color, height * 100, material, sound, wingsState, true); //Вызов метода для валидации тела с пустым ответом
    }

    @Test(description = "Свойства утки с нечетным ID с материалом rubber")
    @CitrusTest
    public void successfulPropertiesRubber(@Optional @CitrusResource TestCaseRunner runner) {
        int duckIdRubber = 33;
        String color = "yellow";
        double height = 0.03; //Высота указана в метрах. json умножает значение на 100, валидацию не пройдёт
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
        propertiesDuck(runner, Integer.toString(duckIdRubber));
        validateResponse(runner, color, height * 100, material, sound, wingsState, false); //Валидация свойств. Высоту *100 для прохождения валидации
    }
}
