package gregtech.common.items.flinttools;

import net.minecraft.item.ItemPickaxe;

public class FlintPickaxe extends ItemPickaxe {

    public FlintPickaxe() {
        super(FlintTools.FLINT_MATERIAL);
        this.setUnlocalizedName("flintPickaxe");
        this.setTextureName("gregtech:tools/flintPickaxe");
    }
}
