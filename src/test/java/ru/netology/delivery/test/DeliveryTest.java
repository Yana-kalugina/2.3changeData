package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE, firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=success-notification]").should(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(10));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE, secondMeetingDate);
        $(withText("Запланировать")).click();
        $("[data-test-id=replan-notification]").should(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(10));
        $(withText("Перепланировать")).click();
        $("[data-test-id=success-notification]").should(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(10));
    }
}
