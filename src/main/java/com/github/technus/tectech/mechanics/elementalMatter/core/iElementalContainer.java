package com.github.technus.tectech.mechanics.elementalMatter.core;

import com.github.technus.tectech.mechanics.elementalMatter.core.maps.cElementalInstanceStackMap;

/**
 * Created by danie_000 on 25.01.2017.
 */
public interface iElementalContainer {
    cElementalInstanceStackMap getContentHandler();

    void purgeOverflow();
}
