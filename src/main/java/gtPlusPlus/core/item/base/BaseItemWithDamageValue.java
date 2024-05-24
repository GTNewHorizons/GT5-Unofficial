package gtPlusPlus.core.item.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaseItemWithDamageValue extends Item {

    public BaseItemWithDamageValue(final String unlocalizedName) {
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(GTPlusPlus.ID + ":" + unlocalizedName);
        this.setMaxStackSize(1);
        this.setMaxDamage(100);
    }

    @Override
    public void setDamage(final ItemStack stack, final int damage) {
        super.setDamage(stack, damage);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        super.addInformation(stack, aPlayer, list, bool);
    }
}
