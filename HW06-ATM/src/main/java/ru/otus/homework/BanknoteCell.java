package ru.otus.homework;

import ru.otus.homework.exceptions.BanknoteException;


public class BanknoteCell {
    private final Banknote nominate;
    private int count;

    public BanknoteCell(Banknote nominate) {
        this.nominate = nominate;
        this.count = 0;
    }

    public int getNominateValue() {
        return nominate.getNominate();
    }

    public void addBanknote(){
        this.count++;
    }

    public int getSum(){
        return count*nominate.getNominate();
    }

    public void peekBanknote(){
        if(count == 0) throw new BanknoteException("Отсутсвуют банкноты с номиналом " + nominate.getNominate());
        this.count--;
    }
}
