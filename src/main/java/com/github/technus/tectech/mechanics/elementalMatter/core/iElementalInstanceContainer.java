package com.github.technus.tectech.mechanics.elementalMatter.core;

/**
 * Created by danie_000 on 25.01.2017.
 */
public interface iElementalInstanceContainer extends Cloneable {
    cElementalInstanceStackMap getContainerHandler();

    void purgeOverflow();
}
