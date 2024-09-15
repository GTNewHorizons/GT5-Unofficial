package gregtech.common.items.flinttools;

import net.minecraft.item.ItemSpade;
import net.minecraft.creativetab.CreativeTabs;

public class FlintShovel extends ItemSpade {

    public FlintShovel() {
        super(FlintTools.FLINT_MATERIAL);
        this.setUnlocalizedName("flintShovel");
        this.setTextureName("gregtech:tools/flintShovel");
        this.setCreativeTab(CreativeTabs.tabTools);
    }
}
