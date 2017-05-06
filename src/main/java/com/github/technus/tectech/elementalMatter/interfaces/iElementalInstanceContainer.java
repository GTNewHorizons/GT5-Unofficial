package com.github.technus.tectech.elementalMatter.interfaces;

import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;

/**
 * Created by danie_000 on 25.01.2017.
 */
public interface iElementalInstanceContainer {
    cElementalInstanceStackMap getContainerHandler();

    float purgeOverflow();
}
