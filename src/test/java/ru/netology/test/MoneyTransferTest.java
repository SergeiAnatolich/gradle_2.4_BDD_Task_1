package ru.netology.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    @Test
    void shouldTransferFromCard1ToCard2() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int actualBalanceCard1 = dashboardPage.getCardBalance(0);
        int actualBalanceCard2 = dashboardPage.getCardBalance(1);
        var cardNumber = DataHelper.getCardNumber1().getNumber();
        var toppingUpCard = dashboardPage.transfer(1);
        toppingUpCard.transfer("1000", cardNumber);
        Assertions.assertEquals(actualBalanceCard1 - 1000, dashboardPage.getCardBalance(0));
        Assertions.assertEquals(actualBalanceCard2 + 1000, dashboardPage.getCardBalance(1));
    }

    @Test
    void shouldCancelTransfer() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var toppingUpCard = dashboardPage.transfer(0);
        toppingUpCard.cancel();
    }

    @Test
    void shouldNotBeEnoughMoney() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int actualBalanceCard1 = dashboardPage.getCardBalance(0);
        int actualBalanceCard2 = dashboardPage.getCardBalance(1);
        var cardNumber = DataHelper.getCardNumber1().getNumber();
        var toppingUpCard = dashboardPage.transfer(1);
        toppingUpCard.transfer(Integer.toString(actualBalanceCard1 + 1000), cardNumber);
        toppingUpCard.errorTransfer();
        Assertions.assertEquals(actualBalanceCard1, dashboardPage.getCardBalance(0));
        Assertions.assertEquals(actualBalanceCard2, dashboardPage.getCardBalance(1));
    }
}

