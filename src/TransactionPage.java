package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class TransactionPage {

    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement actionButton = $("[data-test-id=action-transfer] span");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel] span");

    public void doTransaction(String cardNumber, String amount) {
        amountField.sendKeys(Keys.CONTROL, "a", Keys.DELETE);
        amountField.setValue(amount);
        fromField.sendKeys(Keys.CONTROL, "a", Keys.DELETE);
        fromField.setValue(cardNumber);
        actionButton.click();
    }

    public DashboardPage cancelTransaction() {
        cancelButton.click();
        return new DashboardPage();
    }
}