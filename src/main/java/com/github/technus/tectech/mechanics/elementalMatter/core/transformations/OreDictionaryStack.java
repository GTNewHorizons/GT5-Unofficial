package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

public class OreDictionaryStack {
    private final int amount;
    private final int id;

    public OreDictionaryStack(int amount, int id) {
        this.amount = amount;
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public int getOreId() {
        return id;
    }
}
