package ru.otus;

import ru.otus.homework.ATM;
import ru.otus.homework.Banknote;
import ru.otus.homework.BaseATM;

import java.util.Collection;
import java.util.List;

/*
Написать эмулятор АТМ (банкомата).
Объект класса АТМ должен уметь:
принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)
выдавать запрошенную сумму минимальным количеством банкнот или ошибку, если сумму нельзя выдать.
Это задание не на алгоритмы, а на проектирование.
Поэтому оптимизировать выдачу не надо.
выдавать сумму остатка денежных средств
В этом задании больше думайте об архитектуре приложения.
Не отвлекайтесь на создание таких объектов как: пользователь, авторизация, клавиатура, дисплей, UI (консольный, Web, Swing), валюта, счет, карта, т.д.
Все это не только не нужно, но и вредно!
 */
public class Homework06Starter {
    public static void main(String... args) {
        ATM atm = new BaseATM();
        System.out.println("Начальный баланс " + atm.getBalance());

        atm.topUp(List.of(Banknote.RANK_5000, Banknote.RANK_2000, Banknote.RANK_1000, Banknote.RANK_500, Banknote.RANK_100, Banknote.RANK_1000, Banknote.RANK_100));
        System.out.println("Баланс после внесения 9700: " + atm.getBalance());

        Collection<Banknote> issued = atm.issue(3500);
        System.out.println("Сняли 3500");
        System.out.println("Печатаем номиналы полученных купюр");
        issued.stream().forEachOrdered(item -> System.out.println(item));
        System.out.println("Баланс после снятия 9700 - 3500: " + atm.getBalance());
    }
}
