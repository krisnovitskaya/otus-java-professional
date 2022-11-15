package ru.otus.homework;

import java.util.Collection;

public class BaseATM implements ATM{

    private final Storage storage;

    public BaseATM() {
        storage = new Storage(Banknote.values());
    }

    @Override
    public void topUp(Collection<Banknote> banknotes) {
        storage.addAll(banknotes);
    }

    @Override
    public Collection<Banknote> issue(int sum) {
        return storage.issue(sum);
    }

    @Override
    public int getBalance() {
        return storage.getBalance();
    }
}
