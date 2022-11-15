package ru.otus.homework;

import ru.otus.homework.exceptions.BanknoteException;

import java.util.*;

public class Storage {
    private int balance;
    private NavigableMap<Banknote, BanknoteCell> storage;

    public Storage(Banknote ... banknoteType) {
        this.balance = 0;
        storage = new TreeMap<>(Comparator.comparingInt(Banknote::getNominate).reversed());
        for (Banknote banknote : banknoteType) {
            storage.put(banknote, new BanknoteCell(banknote));
        }
    }


    public void addAll(Collection<Banknote> banknotes){
        for (Banknote banknote : banknotes) {
            storage.get(banknote).addBanknote();
        }
        updateStorageBalance();
    }

    private void updateStorageBalance(){
        int balance = 0;
        for (var entry : storage.entrySet()) {
            balance = balance + entry.getValue().getSum();
        }
        this.balance = balance;
    }

    public Collection<Banknote> issue(int sum){
        checkSum(sum);
        return getFromStorage(countBanknoteToIssue(sum));
    }

    private void checkSum(int sum){
        if(sum < 0) throw new BanknoteException("Невозможно выдать отрицательную сумму");
        if(balance < sum) throw new BanknoteException("В банкомате отсутствует запрошенная сумма: " + sum);
        if(sum % storage.lastKey().getNominate() != 0) throw new BanknoteException("Невозможно выдать сумму не кратную минимальному номиналу банкнот");
    }

    private Map<Banknote, Integer> countBanknoteToIssue(int sum){
        Map<Banknote, Integer> mapToIssue = new HashMap<>(Banknote.values().length);
        int banknoteCount;
        for (var entry : storage.entrySet()) {
            banknoteCount = sum / entry.getKey().getNominate();
            mapToIssue.put(entry.getKey(), banknoteCount);
            sum = sum - banknoteCount * entry.getKey().getNominate();
            if(sum == 0) break;
        }
        if(sum != 0) throw new BanknoteException("Невозможно выдать запрошенную сумму");
        return mapToIssue;
    }

    private Collection<Banknote> getFromStorage(Map<Banknote, Integer> mapToIssue){
        List<Banknote> banknotes = new ArrayList<>();

        for (var issueEntry : mapToIssue.entrySet()) {
            for (int i = 1; i <= issueEntry.getValue(); i++) {
                storage.get(issueEntry.getKey()).peekBanknote();
                banknotes.add(issueEntry.getKey());
            }
        }

        updateStorageBalance();
        return  banknotes;
    }

    public int getBalance() {
        return balance;
    }
}
