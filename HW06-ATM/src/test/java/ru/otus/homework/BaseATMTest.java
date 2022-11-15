package ru.otus.homework;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.exceptions.BanknoteException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
class BaseATMTest {

    @Test
    @DisplayName("Проверка, что банкомат создается с балансом = 0")
    void test1(){
        ATM atm = new BaseATM();
        assertThat(atm.getBalance()).isEqualTo(0);
    }


    @Test
    @DisplayName("Проверка добавления банкнот")
    void test2(){
        ATM atm = new BaseATM();

        atm.topUp(List.of(Banknote.RANK_5000, Banknote.RANK_2000, Banknote.RANK_1000, Banknote.RANK_500, Banknote.RANK_100));
        int expectedBalance = 8600;

        assertThat(expectedBalance).isEqualTo(atm.getBalance());
    }


    @Test
    @DisplayName("Проверка снятия банкнот")
    void test3(){
        ATM atm = new BaseATM();

        atm.topUp(List.of(Banknote.RANK_5000, Banknote.RANK_2000, Banknote.RANK_1000, Banknote.RANK_500, Banknote.RANK_100));

        Collection<Banknote> expected = List.of(Banknote.RANK_1000, Banknote.RANK_500);

        Collection<Banknote> issue = atm.issue(1500);
        int expectedBalanceAfterIssue = 7100;

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(expected.size()).isEqualTo(issue.size());
        softAssertions.assertThat(expectedBalanceAfterIssue).isEqualTo(atm.getBalance());
        softAssertions.assertAll();

    }

    @Test
    @DisplayName("Проверка запроса некорректных сумм")
    void test4(){

        ATM atm = new BaseATM();
        atm.topUp(List.of(Banknote.RANK_5000, Banknote.RANK_2000, Banknote.RANK_1000, Banknote.RANK_500, Banknote.RANK_100));


        Assertions.assertThrowsExactly(BanknoteException.class, () -> atm.issue(-5));
        Assertions.assertThrowsExactly(BanknoteException.class, () -> atm.issue(105));
        Assertions.assertThrowsExactly(BanknoteException.class, () -> atm.issue(8700));

    }

}
