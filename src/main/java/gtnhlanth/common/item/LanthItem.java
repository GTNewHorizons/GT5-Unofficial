package gtnhlanth.common.item;

import net.minecraft.item.Item;

import gtnhlanth.Tags;

public class LanthItem extends Item {

    public LanthItem(String name) {
        super();
        this.setUnlocalizedName(name);
        this.setTextureName(Tags.MODID + ":" + name);
    }

}
