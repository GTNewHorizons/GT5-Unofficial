package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;

public class BehaviourLighter extends BehaviourNone {

    private final ItemStack mEmptyLighter;
    private final ItemStack mUsedLighter;
    private final ItemStack mFullLighter;
    private final long mFuelAmount;

    public BehaviourLighter(ItemStack aEmptyLighter, ItemStack aUsedLighter, ItemStack aFullLighter, long aFuelAmount) {
        this.mFullLighter = aFullLighter;
        this.mUsedLighter = aUsedLighter;
        this.mEmptyLighter = aEmptyLighter;
        this.mFuelAmount = aFuelAmount;
    }

    @Override
    public boolean onLeftClickEntity(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
        if ((aPlayer.worldObj.isRemote) || (aStack.stackSize != 1)) {
            return false;
        }
        boolean rOutput = false;
        if ((aEntity instanceof EntityCreeper)) {
            prepare(aStack);
            long tFuelAmount = GTUtility.ItemNBT.getLighterFuel(aStack);
            if (GTUtility.areStacksEqual(aStack, this.mUsedLighter, true)) {
                GTUtility.sendSoundToPlayers(
                    aPlayer.worldObj,
                    SoundResource.FIRE_IGNITE,
                    1.0F,
                    1.0F,
                    aEntity.posX,
                    aEntity.posY,
                    aEntity.posZ);
                ((EntityCreeper) aEntity).func_146079_cb();
                if (!aPlayer.capabilities.isCreativeMode) {
                    tFuelAmount -= 1L;
                }
                rOutput = true;
            }
            GTUtility.ItemNBT.setLighterFuel(aStack, tFuelAmount);
            if (tFuelAmount <= 0L) {
                useUp(aStack);
            }
        }
        return rOutput;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if ((aWorld.isRemote) || (aStack.stackSize != 1)) {
            return false;
        }
        boolean rOutput = false;

        aX += side.offsetX;
        aY += side.offsetY;
        aZ += side.offsetZ;
        if ((!GTUtility.isBlockAir(aWorld, aX, aY, aZ))
            || (!aPlayer.canPlayerEdit(aX, aY, aZ, side.ordinal(), aStack))) {
            return false;
        }
        prepare(aStack);
        long tFuelAmount = GTUtility.ItemNBT.getLighterFuel(aStack);
        if (GTUtility.areStacksEqual(aStack, this.mUsedLighter, true)) {
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.FIRE_IGNITE, 1.0F, 1.0F, hitX, hitY, hitZ);
            aWorld.setBlock(aX, aY, aZ, Blocks.fire);
            if (!aPlayer.capabilities.isCreativeMode) {
                tFuelAmount -= 1L;
            }
            rOutput = true;
        }
        GTUtility.ItemNBT.setLighterFuel(aStack, tFuelAmount);
        if (tFuelAmount <= 0L) {
            useUp(aStack);
        }
        return rOutput;
    }

    private void prepare(ItemStack aStack) {
        if (GTUtility.areStacksEqual(aStack, this.mFullLighter, true)) {
            aStack.func_150996_a(this.mUsedLighter.getItem());
            Items.feather.setDamage(aStack, Items.feather.getDamage(this.mUsedLighter));
            GTUtility.ItemNBT.setLighterFuel(aStack, this.mFuelAmount);
        }
    }

    private void useUp(ItemStack aStack) {
        if (this.mEmptyLighter == null) {
            aStack.stackSize -= 1;
        } else {
            aStack.func_150996_a(this.mEmptyLighter.getItem());
            Items.feather.setDamage(aStack, Items.feather.getDamage(this.mEmptyLighter));
        }
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(StatCollector.translateToLocal("gt.behaviour.lighter.tooltip"));
        NBTTagCompound tNBT = aStack.getTagCompound();
        long tFuelAmount = tNBT == null ? this.mFuelAmount
            : GTUtility.areStacksEqual(aStack, this.mFullLighter, true) ? this.mFuelAmount
                : tNBT.getLong("GT.LighterFuel");
        aList.add(StatCollector.translateToLocalFormatted("gt.behaviour.lighter.uses", tFuelAmount));
        aList.add(StatCollector.translateToLocal("gt.behaviour.unstackable"));
        return aList;
    }
}
