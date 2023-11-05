package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DeliveryTest {

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
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        var sity = DataGenerator.generateCity("ru");
        var name = DataGenerator.generateName("ru");
        var phone = DataGenerator.generatePhone("ru");
        $("[data-test-id='city'] .input__control").setValue(sity);
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(firstMeetingDate).sendKeys(Keys.ENTER);
        $("[data-test-id='name'] .input__control").setValue(name);
        $("[data-test-id='phone'] .input__control").setValue(phone);
        $("[data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Запланировать')]").click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible);
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(secondMeetingDate).sendKeys(Keys.ESCAPE);
        $x("//span[contains(text(), 'Запланировать')]").click();
        $x("//div[contains(text(), 'Перепланировать')]").
                shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(Condition.visible);
        $x("//span[contains(text(), 'Перепланировать')]").click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(Condition.visible);
    }

    @Test
    void shouldSuccessfulPlanAndReplanMeetingWithUser() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(firstMeetingDate).sendKeys(Keys.ENTER);
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Запланировать')]").click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible);
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(secondMeetingDate).sendKeys(Keys.ESCAPE);
        $x("//span[contains(text(), 'Запланировать')]").click();
        $x("//div[contains(text(), 'Перепланировать')]").shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(Condition.visible);
        $x("//span[contains(text(), 'Перепланировать')]").click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(Condition.visible);
    }
    @Test // проверка на пустое поле Город
    void shouldRegisteredEmptyFieldCity() {
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] .input__control").setValue("");
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(firstMeetingDate).sendKeys(Keys.ENTER);
        $("[data-test-id='name'] .input__control").setValue("Васильев Василий");
        $("[data-test-id='phone'] .input__control").setValue("+79879871122");
        $("[data-test-id='agreement']").click();
        $$("[role='button']").filter(Condition.visible).last().click();
        $x("//span[@data-test-id='city' and contains(@class, 'input_invalid')]//span[contains(text(), 'Поле')]").should(Condition.appear);
    }

    @Test //пустое поле даты
    void shouldRegisteredEmptyFieldDate() {
        $("[data-test-id='city'] .input__control").setValue("Чебоксары");
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='name'] .input__control").setValue("Васильев Василий");
        $("[data-test-id='phone'] .input__control").setValue("+79879871122");
        $("[data-test-id='agreement']").click();
        $$("[role='button']").filter(Condition.visible).last().click();
        $x("//span[@data-test-id='date']//span[contains(@class, 'input_invalid')]//span[contains(text(), 'Неверно')]").should(Condition.appear);
    }

    @Test //проверка на пустое поле Фамилия и имя
    void shouldRegisteredEmptyFieldName() {
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] .input__control").setValue("Чебоксары");
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(firstMeetingDate).sendKeys(Keys.ENTER);
        $("[data-test-id='name'] .input__control").setValue("");
        $("[data-test-id='phone'] .input__control").setValue("+79879871122");
        $("[data-test-id='agreement']").click();
        $$("[role='button']").filter(Condition.visible).last().click();
        $x("//span[@data-test-id='name' and contains(@class, 'input_invalid')]//span[contains(text(), 'Поле')]").should(Condition.appear);
    }

    @Test //пустое поле Номер телефона
    void shouldRegisteredEmptyFieldPhone() {
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] .input__control").setValue("Чебоксары");
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(firstMeetingDate).sendKeys(Keys.ENTER);
        $("[data-test-id='name'] .input__control").setValue("Васильев Василий");
        $("[data-test-id='phone'] .input__control").setValue("");
        $("[data-test-id='agreement']").click();
        $$("[role='button']").filter(Condition.visible).last().click();
        $x("//span[@data-test-id='phone' and contains(@class, 'input_invalid')]//span[contains(text(), 'Поле')]").should(Condition.appear);
    }

    @Test //не проставлен чек-бокс
    void shouldRegisteredEmptyFieldCheckBox() {
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] .input__control").setValue("Чебоксары");
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(firstMeetingDate).sendKeys(Keys.ENTER);
        $("[data-test-id='name'] .input__control").setValue("Васильев Василий");
        $("[data-test-id='phone'] .input__control").setValue("+79879871122");
        //$("[data-test-id='agreement']").click();
        $$("[role='button']").filter(Condition.visible).last().click();
        assertTrue($x("//label[@data-test-id='agreement' and contains(@class, 'input_invalid')]").isDisplayed());
    }

    @Test
    void shouldRegisteredCityIsNotCenter() {
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] .input__control").setValue("Новочебоксарск");
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(firstMeetingDate).sendKeys(Keys.ENTER);
        $("[data-test-id='name'] .input__control").setValue("Васильев Василий");
        $("[data-test-id='phone'] .input__control").setValue("+79879871122");
        $("[data-test-id='agreement']").click();
        $$("[role='button']").filter(Condition.visible).last().click();
        $x("//span[@data-test-id='city' and contains(@class, 'input_invalid')]//span[contains(text(), 'Доставка')]").should(Condition.appear);
    }

    @Test //невалидные значения для фамилии латиница
    void shouldRegisteredNameLatin() {
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] .input__control").setValue("Чебоксары");
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(firstMeetingDate).sendKeys(Keys.ENTER);
        $("[data-test-id='name'] .input__control").setValue("Grigoriev Ivan");
        $("[data-test-id='phone'] .input__control").setValue("+79879871122");
        $("[data-test-id='agreement']").click();
        $$("[role='button']").filter(Condition.visible).last().click();
        $x("//span[@data-test-id='name' and contains(@class, 'input_invalid')]//span[contains(text(), 'Допустимы')]").should(Condition.appear);
    }

    @Test //невалидные значения для фамилии спецсимволы
    void shouldRegisteredNameSpecialSymbol() {
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] .input__control").setValue("Чебоксары");
        $("[data-test-id='date'] [formnovalidate]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [formnovalidate]").setValue(firstMeetingDate).sendKeys(Keys.ENTER);
        $("[data-test-id='name'] .input__control").setValue("Васильев_Василий");
        $("[data-test-id='phone'] .input__control").setValue("+79879871122");
        $("[data-test-id='agreement']").click();
        $$("[role='button']").filter(Condition.visible).last().click();
        $x("//span[@data-test-id='name' and contains(@class, 'input_invalid')]//span[contains(text(), 'Допустимы')]").should(Condition.appear);
    }
}