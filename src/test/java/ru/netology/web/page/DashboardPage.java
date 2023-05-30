package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    private ElementsCollection cards = $$(".list__item div");
    private SelenideElement reloadButton = $("[data-test-id=action-reload]");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public TransactionPage getTransactionPage(String id) {
        SelenideElement el = cards.find(attribute("data-test-id", id));
        el.find(".button__text").click();
        return new TransactionPage();
    }

    public int getCardBalance(String id) {
        int balance = 0;
        SelenideElement el = cards.find(attribute("data-test-id", id));
        balance = extractBalance(el.text());
        return balance;
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}