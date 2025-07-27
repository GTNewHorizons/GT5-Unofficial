package gtPlusPlus.core.util.minecraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.handler.events.EntityDeathHandler;

public class EntityUtils {

    public static BlockPos findBlockPosOfEntity(final Entity parEntity) {
        final int blockX = MathHelper.floor_double(parEntity.posX);
        final int blockY = MathHelper.floor_double(parEntity.boundingBox.minY);
        final int blockZ = MathHelper.floor_double(parEntity.posZ);
        return new BlockPos(blockX, blockY, blockZ, parEntity.worldObj);
    }

    public static void applyRadiationDamageToEntity(final int stackSize, final int radiationLevel, final World world,
        final Entity entityHolding) {
        if (!world.isRemote && entityHolding instanceof EntityPlayer player) {
            if (!player.capabilities.isCreativeMode) {
                GTUtility.applyRadioactivity(player, radiationLevel, stackSize);
            }
        }
    }

    /**
     * Provides the ability to provide custom drops upon the death of EntityLivingBase objects.
     *
     * @param aMobClass  - The Base Class you want to drop this item.
     * @param aStack     - The ItemStack, stack size is not respected.
     * @param aMaxAmount - The maximum size of the ItemStack which drops.
     * @param aChance    - Chance out of 10000, where 100 is 1%. (1 = 0.01% - this is ok)
     */
    public static void registerDropsForMob(Class<?> aMobClass, ItemStack aStack, int aMaxAmount, int aChance) {
        EntityDeathHandler.registerDropsForMob(aMobClass, aStack, aMaxAmount, aChance);
    }
}
