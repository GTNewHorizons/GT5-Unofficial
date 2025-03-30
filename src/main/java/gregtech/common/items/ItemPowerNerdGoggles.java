package gregtech.common.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import gregtech.api.items.GTGenericItem;
import gtPlusPlus.core.item.wearable.armour.ArmourLoader;
import gtPlusPlus.core.item.wearable.armour.base.BaseArmourHelm;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class ItemPowerNerdGoggles extends GTGenericItem implements IBauble {


    public ItemPowerNerdGoggles(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.UNIVERSAL;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }
}
