package gtPlusPlus.core.item.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class ItemBlueprint extends Item {

    public ItemBlueprint(final String unlocalizedName) {
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(GTPlusPlus.ID + ":" + unlocalizedName);
        this.setMaxStackSize(1);
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        // this.bpID = MathUtils.randInt(0, 1000);
        GameRegistry.registerItem(this, unlocalizedName);
    }
}
