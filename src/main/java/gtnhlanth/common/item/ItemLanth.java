package gtnhlanth.common.item;

import net.minecraft.item.Item;

import gtnhlanth.Tags;

public class ItemLanth extends Item {

    public ItemLanth(String name) {
        super();
        this.setUnlocalizedName(name);
        this.setTextureName(Tags.MODID + ":" + name);
    }

}
