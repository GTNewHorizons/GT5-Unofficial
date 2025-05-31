package gtPlusPlus.core.item.base;

import gregtech.api.util.GTUtility;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraftforge.oredict.OreDictionary;

public class BaseItemBurnable extends CoreItem {

    protected final int meta;

    public BaseItemBurnable(String unlocalizedName, String displayName, CreativeTabs creativeTab, int stackSize,
        int maxDmg, String description, String oredictName, int burnTime, int meta) {
        super(unlocalizedName, creativeTab, stackSize, maxDmg, description);
        this.itemName = displayName;
        this.meta = meta;
        if (oredictName != null && !oredictName.isEmpty()) {
            OreDictionary.registerOre(oredictName, new ItemStack(this));
        }
        ItemUtils.registerFuel(new ItemStack(this, 1), burnTime);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return this.meta;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }
}
