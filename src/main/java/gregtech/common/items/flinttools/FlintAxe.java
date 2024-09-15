package gregtech.common.items.flinttools;

import net.minecraft.item.ItemAxe;
import net.minecraft.creativetab.CreativeTabs;

public class FlintAxe extends ItemAxe {

    public FlintAxe() {
        super(Util.FLINT_MATERIAL);
        this.setUnlocalizedName("flintAxe");
        this.setTextureName("gregtech:tools/flintAxe");
        this.setCreativeTab(CreativeTabs.tabTools);
    }
}
