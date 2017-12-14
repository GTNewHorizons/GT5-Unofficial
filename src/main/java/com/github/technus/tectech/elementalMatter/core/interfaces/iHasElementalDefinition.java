package com.github.technus.tectech.elementalMatter.core.interfaces;

import com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition;

/**
 * Created by danie_000 on 30.01.2017.
 */
public interface iHasElementalDefinition extends Comparable<iHasElementalDefinition>,Cloneable {
    iElementalDefinition getDefinition();

    long getAmount();

    long getCharge();

    float getMass();

    iHasElementalDefinition clone();
}
