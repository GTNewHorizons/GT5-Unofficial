package gregtech.common.items;

import static gregtech.api.enums.GTValues.NW;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import gregtech.api.items.GTGenericItem;
import gregtech.api.net.GTPacketRequestWirelessEU;
import gregtech.common.handlers.PowerGogglesHudHandler;

public class ItemPowerNerdGoggles extends GTGenericItem implements IBauble {

    private static int ticks = 0;

    public ItemPowerNerdGoggles(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.UNIVERSAL;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if ((ticks++ % PowerGogglesHudHandler.ticksBetweenMeasurements) == 0)
            NW.sendToServer(new GTPacketRequestWirelessEU(player.getUniqueID()));
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
