package com.elisis.gtnhlanth.common.item;

import net.minecraft.item.Item;

import com.elisis.gtnhlanth.Tags;

public class LanthItem extends Item {

    public LanthItem(String name) {
        super();
        this.setUnlocalizedName(name);
        this.setTextureName(Tags.MODID + ":" + name);
    }

}
