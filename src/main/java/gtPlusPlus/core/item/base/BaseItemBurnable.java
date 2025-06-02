package gtPlusPlus.core.item.base;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.Pair;

import gtPlusPlus.core.lib.GTPPCore;

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
        GTPPCore.burnables.add(Pair.of(burnTime, new ItemStack(this, 1)));
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
