package gregtech.common.items.flinttools;

import net.minecraft.item.ItemPickaxe;
import net.minecraft.creativetab.CreativeTabs;

public class FlintPickaxe extends ItemPickaxe {

    public FlintPickaxe() {
        super(Util.FLINT_MATERIAL);
        this.setUnlocalizedName("flintPickaxe");
        this.setCreativeTab(CreativeTabs.tabTools);
    }
}
