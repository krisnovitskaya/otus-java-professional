package ru.otus.homework;

public enum Banknote {
    RANK_100(100),
    RANK_500(500),
    RANK_1000(1000),
    RANK_2000(2000),
    RANK_5000(5000);

    private final int nominate;

    Banknote(int nominate) {
        this.nominate = nominate;
    }

    public int getNominate() {
        return nominate;
    }
}
