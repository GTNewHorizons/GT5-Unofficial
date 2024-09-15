package gregtech.common.items.flinttools;

import net.minecraft.item.ItemSword;
import net.minecraft.creativetab.CreativeTabs;

public class FlintSword extends ItemSword {

    public FlintSword() {
        super(FlintTools.FLINT_MATERIAL);
        this.setUnlocalizedName("flintSword");
        this.setTextureName("gregtech:tools/flintSword");
        this.setCreativeTab(CreativeTabs.tabCombat);
    }
}
