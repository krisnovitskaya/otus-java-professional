package ru.otus.homework;

import java.util.Collection;

public interface ATM {
    void topUp(Collection<Banknote> banknotes);

    Collection<Banknote> issue(int sum);

    int getBalance();
}
