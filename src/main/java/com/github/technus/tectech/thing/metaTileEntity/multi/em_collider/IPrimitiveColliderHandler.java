package com.github.technus.tectech.thing.metaTileEntity.multi.em_collider;

import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;

public interface IPrimitiveColliderHandler {
    void collide(EMInstanceStack in1, EMInstanceStack in2, EMInstanceStackMap out);
}
