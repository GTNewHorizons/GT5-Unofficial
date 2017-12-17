package com.github.technus.tectech.elementalMatter.core.interfaces;

import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;

/**
 * Created by danie_000 on 25.01.2017.
 */
public interface iElementalInstanceContainer extends Cloneable {
    cElementalInstanceStackMap getContainerHandler();

    float purgeOverflow();
}
