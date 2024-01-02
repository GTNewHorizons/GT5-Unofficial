package gregtech.api.modernmaterials.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.util.GT_Utility;
import ic2.core.IC2Potion;

public class Effects {

    public static void radiation(ItemStack itemStack, World world, Entity player, int slotIndex,
        boolean isCurrentItem) {
        if (player instanceof EntityLivingBase playerLiving) {
            if (!GT_Utility.isWearingFullRadioHazmat(playerLiving)) {
                IC2Potion.radiation.applyTo(playerLiving, 20, 10);
            }
        }
    }
}
