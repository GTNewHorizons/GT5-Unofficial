package gregtech.common.items;

import gregtech.api.GregTechAPI;
import net.minecraft.item.Item;

public class ItemStickyResin extends Item {

    public ItemStickyResin() {
        setUnlocalizedName("gt.sticky_resin");
        setTextureName("gregtech:sticky_resin");
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);
        setMaxStackSize(64);
    }
}
