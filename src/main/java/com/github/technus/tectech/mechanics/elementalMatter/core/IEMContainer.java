package com.github.technus.tectech.mechanics.elementalMatter.core;

import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;

/**
 * Created by danie_000 on 25.01.2017.
 */
public interface IEMContainer {
    EMInstanceStackMap getContentHandler();

    void purgeOverflow();
}
