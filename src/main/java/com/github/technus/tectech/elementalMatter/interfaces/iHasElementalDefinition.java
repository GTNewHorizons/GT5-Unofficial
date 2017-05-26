package com.github.technus.tectech.elementalMatter.interfaces;

/**
 * Created by danie_000 on 30.01.2017.
 */
public interface iHasElementalDefinition extends Comparable<iHasElementalDefinition>,Cloneable {
    iElementalDefinition getDefinition();

    int getAmount();

    float getMass();

    int getCharge();

    iHasElementalDefinition clone();
}
