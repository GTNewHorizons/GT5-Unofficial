package gtnhlanth.common.item;

import net.minecraft.item.Item;

import gregtech.api.enums.Mods;

public class ItemLanth extends Item {

    public ItemLanth(String name) {
        super();
        this.setUnlocalizedName(name);
        this.setTextureName(Mods.ModIDs.G_T_N_H_LANTHANIDES + ":" + name);
    }

}
