package ru.netology.web.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV2;

import java.util.Random;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {
    @Test
    void shouldCancelTransfer() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var balanceForFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstCardInfo().getCardID());
        var balanceForSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondCardInfo().getCardID());
        var transactionPage = dashboardPage.getTransactionPage(DataHelper.getFirstCardInfo().getCardID());
        transactionPage.cancelTransaction();

        Assertions.assertEquals(balanceForFirstCard, dashboardPage.getCardBalance(DataHelper.getFirstCardInfo().getCardID()));
        Assertions.assertEquals(balanceForSecondCard, dashboardPage.getCardBalance(DataHelper.getSecondCardInfo().getCardID()));
    }

    @Test
    void shouldNotTransferTheSameCard() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var balanceForFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstCardInfo().getCardID());
        var balanceForSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondCardInfo().getCardID());
        var transactionPage = dashboardPage.getTransactionPage(DataHelper.getFirstCardInfo().getCardID());
        var enoughAmount = new Random().nextInt(balanceForFirstCard);
        transactionPage.doTransaction(DataHelper.getFirstCardInfo().getCardNumber(), Integer.toString(enoughAmount));

        Assertions.assertEquals(balanceForFirstCard, dashboardPage.getCardBalance(DataHelper.getFirstCardInfo().getCardID()));
        Assertions.assertEquals(balanceForSecondCard, dashboardPage.getCardBalance(DataHelper.getSecondCardInfo().getCardID()));
    }

    @Test
    void shouldTransferMoneyLessThanBalance() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var balanceForFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstCardInfo().getCardID());
        var balanceForSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondCardInfo().getCardID());
        var transactionPage = dashboardPage.getTransactionPage(DataHelper.getFirstCardInfo().getCardID());
        var enoughAmount = new Random().nextInt(balanceForFirstCard);
        transactionPage.doTransaction(DataHelper.getSecondCardInfo().getCardNumber(), Integer.toString(enoughAmount));

        Assertions.assertEquals(balanceForFirstCard + enoughAmount, dashboardPage.getCardBalance(DataHelper.getFirstCardInfo().getCardID()));
        Assertions.assertEquals(balanceForSecondCard - enoughAmount, dashboardPage.getCardBalance(DataHelper.getSecondCardInfo().getCardID()));

        dashboardPage.getTransactionPage(DataHelper.getSecondCardInfo().getCardID());
        transactionPage.doTransaction(DataHelper.getFirstCardInfo().getCardNumber(), Integer.toString(enoughAmount));
    }

    @Test
    void shouldTransferAllBalance() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var balanceForFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstCardInfo().getCardID());
        var balanceForSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondCardInfo().getCardID());
        var transactionPage = dashboardPage.getTransactionPage(DataHelper.getFirstCardInfo().getCardID());
        transactionPage.doTransaction(DataHelper.getSecondCardInfo().getCardNumber(), Integer.toString(balanceForSecondCard));

        Assertions.assertEquals(balanceForFirstCard + balanceForSecondCard, dashboardPage.getCardBalance(DataHelper.getFirstCardInfo().getCardID()));
        Assertions.assertEquals(0, dashboardPage.getCardBalance(DataHelper.getSecondCardInfo().getCardID()));

        dashboardPage.getTransactionPage(DataHelper.getSecondCardInfo().getCardID());
        transactionPage.doTransaction(DataHelper.getFirstCardInfo().getCardNumber(), Integer.toString(balanceForSecondCard));
    }

    @Test
    void shouldNotTransferMoneyMoreThanBalance() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var balanceForFirstCard = dashboardPage.getCardBalance(DataHelper.getFirstCardInfo().getCardID());
        var balanceForSecondCard = dashboardPage.getCardBalance(DataHelper.getSecondCardInfo().getCardID());

        var transactionPage = dashboardPage.getTransactionPage(DataHelper.getSecondCardInfo().getCardID());
        var notEnoughAmount = balanceForFirstCard + balanceForFirstCard;
        transactionPage.doTransaction(DataHelper.getFirstCardInfo().getCardNumber(), Integer.toString(notEnoughAmount));

        Assertions.assertEquals(balanceForFirstCard, dashboardPage.getCardBalance(DataHelper.getFirstCardInfo().getCardID());
        Assertions.assertEquals(balanceForSecondCard, dashboardPage.getCardBalance(DataHelper.getSecondCardInfo().getCardID());
    }
}