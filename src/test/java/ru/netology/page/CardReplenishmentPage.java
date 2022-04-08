package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CardReplenishmentPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement actionButton = $("[data-test-id=action-transfer]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");

    public CardReplenishmentPage() {
        amountField.shouldBe(visible);
        fromField.shouldBe(visible);
    }

    public DashboardPage transfer(String amount, String numberCard) {
        this.amountField.setValue(amount);
        fromField.setValue(numberCard);
        actionButton.click();
        return new DashboardPage();
    }

    public DashboardPage cancel() {
        cancelButton.click();
        return new DashboardPage();
    }

    public void errorTransfer() {
        $("[data-test-id=error-notification].notification__content").shouldHave(text("Недостаточно средств на счёте"));
    }
}
