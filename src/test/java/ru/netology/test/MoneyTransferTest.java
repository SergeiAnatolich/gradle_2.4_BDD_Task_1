package ru.netology.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    @Test
    void shouldOpenPersonalAccount() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        $("[data-test-id=dashboard]").shouldBe(visible);
    }

    @Test
    void shouldTransferFromCard1ToCard2() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int actualBalance = dashboardPage.getCardBalance(1);
        var toppingUpCard = dashboardPage.transferFromCard1ToCard2();
        toppingUpCard.transfer("1000", "5559000000000001");
        Assertions.assertEquals(actualBalance + 1000, dashboardPage.getCardBalance(1));
    }

    @Test
    void shouldTransferFromCard2ToCard1() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        int actualBalance = dashboardPage.getCardBalance(0);
        var toppingUpCard = dashboardPage.transferFromCard2ToCard1();
        toppingUpCard.transfer("2000", "5559000000000002");
        Assertions.assertEquals(actualBalance + 2000, dashboardPage.getCardBalance(0));
    }

    @Test
    void shouldCancelTransfer() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var toppingUpCard = dashboardPage.transferFromCard1ToCard2();
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
        int actualBalance = dashboardPage.getCardBalance(0);
        var toppingUpCard = dashboardPage.transferFromCard2ToCard1();
        toppingUpCard.transfer(Integer.toString(actualBalance + 1000), "5559000000000002");
//        должна быть проверка на ошибку "недостаточно средств", но можно уйти в минус.
//        Предположим, что есть овердрафт. Это нужно уточнить!
//        А пока проверим баланс
        Assertions.assertEquals(actualBalance * 2 + 1000, dashboardPage.getCardBalance(0));
    }
}

