package gtnhlanth.common.item;

import gregtech.api.enums.Mods;
import net.minecraft.item.Item;

import gtnhlanth.Tags;

public class ItemLanth extends Item {

    public ItemLanth(String name) {
        super();
        this.setUnlocalizedName(name);
        this.setTextureName(Mods.ModIDs.G_T_N_H_LANTHANIDES + ":" + name);
    }

}
