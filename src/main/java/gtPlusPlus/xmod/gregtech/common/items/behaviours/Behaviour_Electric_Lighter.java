package gtPlusPlus.xmod.gregtech.common.items.behaviours;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import codechicken.lib.math.MathHelper;
import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourNone;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.entity.projectile.EntityLightningAttack;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.helpers.ChargingHelper;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import ic2.api.item.IElectricItemManager;

public class Behaviour_Electric_Lighter extends BehaviourNone {

    private final String mTooltip = GTLanguageManager
        .addStringLocalization("gt.behaviour.lighter.tooltip", "Can light things on Fire");
    private final String mTooltipUses = GTLanguageManager
        .addStringLocalization("gt.behaviour.lighter.uses", "Remaining Uses:");
    private final String mTooltipUnstackable = GTLanguageManager
        .addStringLocalization("gt.behaviour.unstackable", "Not usable when stacked!");

    public Behaviour_Electric_Lighter() {}

    @Override
    public boolean onLeftClickEntity(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
        if (!aPlayer.worldObj.isRemote && aStack != null && aStack.stackSize == 1) {
            boolean rOutput = false;
            if (aEntity instanceof EntityCreeper) {
                if (this.prepare(aStack) || aPlayer.capabilities.isCreativeMode) {
                    GTUtility.sendSoundToPlayers(
                        aPlayer.worldObj,
                        SoundResource.FIRE_IGNITE,
                        1.0F,
                        1.0F,
                        MathHelper.floor_double(aEntity.posX),
                        MathHelper.floor_double(aEntity.posY),
                        MathHelper.floor_double(aEntity.posZ));
                    ((EntityCreeper) aEntity).func_146079_cb();
                    rOutput = true;
                }
            }
            return rOutput;
        } else {
            return false;
        }
    }

    @Override
    public boolean onItemUse(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY,
        int aZ, int ordinalSide, float hitX, float hitY, float hitZ) {
        if (!aWorld.isRemote && aStack != null && aStack.stackSize == 1) {
            if (aPlayer.isSneaking()) {
                Logger.INFO("Changing Mode");
                boolean aCurrentMode = NBTUtils.getBoolean(aStack, "aFireballMode");
                Logger.INFO("Is currently in Fireball mode? " + aCurrentMode);
                boolean aNewMode = !aCurrentMode;
                Logger.INFO("Is now set to Fireball mode? " + aNewMode);
                aStack.getTagCompound()
                    .setBoolean("aFireballMod", aNewMode);
                // NBTUtils.setBoolean(aStack, "aFireballMode", aNewMode);
                PlayerUtils.messagePlayer(
                    aPlayer,
                    "Current Mode: " + EnumChatFormatting.RED + (aNewMode ? "Projectile" : "Fire Starter"));
            } else {
                boolean aCurrentMode = NBTUtils.getBoolean(aStack, "aFireballMode");
                if (aCurrentMode) {
                    // Shoot Lightning Attack
                    aWorld.playSoundAtEntity(
                        aPlayer,
                        "random.bow",
                        0.5F,
                        0.4F / (GTPPCore.RANDOM.nextFloat() * 0.4F + 0.8F));
                    if (!aWorld.isRemote) {
                        aWorld.spawnEntityInWorld(new EntityLightningAttack(aWorld, aPlayer, hitX, hitY, hitZ));
                    }
                } else {
                    // Lights Fires Mode
                    Logger.WARNING("Preparing Lighter a");
                    boolean rOutput = false;
                    ForgeDirection tDirection = ForgeDirection.getOrientation(ordinalSide);
                    aX += tDirection.offsetX;
                    aY += tDirection.offsetY;
                    aZ += tDirection.offsetZ;
                    if (GTUtility.isBlockAir(aWorld, aX, aY, aZ)
                        && aPlayer.canPlayerEdit(aX, aY, aZ, ordinalSide, aStack)) {
                        Logger.WARNING("Preparing Lighter b");
                        if (this.prepare(aStack) || aPlayer.capabilities.isCreativeMode) {
                            Logger.WARNING("Preparing Lighter c");
                            GTUtility.sendSoundToPlayers(aWorld, SoundResource.FIRE_IGNITE, 1.0F, 1.0F, aX, aY, aZ);
                            aWorld.setBlock(aX, aY, aZ, Blocks.fire);
                            rOutput = true;
                            // ItemNBT.setLighterFuel(aStack, tFuelAmount);
                            return rOutput;
                        }
                    }
                }
            }
        }
        Logger.WARNING("Preparing Lighter z");
        return false;
    }

    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, int ordinalSide, float hitX, float hitY, float hitZ) {
        if (!aWorld.isRemote && aStack != null && aStack.stackSize == 1) {
            if (aPlayer.isSneaking()) {
                Logger.INFO("Changing Mode");
                boolean aCurrentMode = NBTUtils.getBoolean(aStack, "aFireballMode");
                Logger.INFO("Is currently in Fireball mode? " + aCurrentMode);
                boolean aNewMode = !aCurrentMode;
                Logger.INFO("Is now set to Fireball mode? " + aNewMode);
                aStack.getTagCompound()
                    .setBoolean("aFireballMode", aNewMode);
                // NBTUtils.setBoolean(aStack, "aFireballMode", aNewMode);
                PlayerUtils.messagePlayer(
                    aPlayer,
                    "Current Mode: " + EnumChatFormatting.RED + (aNewMode ? "Projectile" : "Fire Starter"));
            } else {
                boolean aCurrentMode = NBTUtils.getBoolean(aStack, "aFireballMode");
                if (aCurrentMode) {
                    // Shoot Lightning Attack
                    aWorld.playSoundAtEntity(
                        aPlayer,
                        "random.bow",
                        0.5F,
                        0.4F / (GTPPCore.RANDOM.nextFloat() * 0.4F + 0.8F));
                    if (!aWorld.isRemote) {
                        aWorld.spawnEntityInWorld(new EntityLightningAttack(aWorld, aPlayer, hitX, hitY, hitZ));
                    }
                } else {
                    // Lights Fires Mode
                    Logger.WARNING("Preparing Lighter a");
                    boolean rOutput = false;
                    ForgeDirection tDirection = ForgeDirection.getOrientation(ordinalSide);
                    aX += tDirection.offsetX;
                    aY += tDirection.offsetY;
                    aZ += tDirection.offsetZ;
                    if (GTUtility.isBlockAir(aWorld, aX, aY, aZ)
                        && aPlayer.canPlayerEdit(aX, aY, aZ, ordinalSide, aStack)) {
                        Logger.WARNING("Preparing Lighter b");
                        if (this.prepare(aStack) || aPlayer.capabilities.isCreativeMode) {
                            Logger.WARNING("Preparing Lighter c");
                            GTUtility.sendSoundToPlayers(aWorld, SoundResource.FIRE_IGNITE, 1.0F, 1.0F, aX, aY, aZ);
                            aWorld.setBlock(aX, aY, aZ, Blocks.fire);
                            rOutput = true;
                            // ItemNBT.setLighterFuel(aStack, tFuelAmount);
                            return rOutput;
                        }
                    }
                }
            }
        }
        Logger.WARNING("Preparing Lighter z");
        return false;
    }

    private boolean prepare(ItemStack aStack) {
        if (aStack != null) {
            Logger.WARNING("Preparing Lighter 1");
            if (aStack.getItem() instanceof MetaGeneratedGregtechTools) {
                Logger.WARNING("Preparing Lighter 2");
                if (ChargingHelper.isItemValid(aStack)) {
                    Logger.WARNING("Preparing Lighter 3");
                    if (aStack.getItem() instanceof IElectricItemManager aItemElec) {
                        Logger.WARNING("Preparing Lighter 4");
                        double aCharge = aItemElec.getCharge(aStack);
                        long aEuCost = 4096 * 2;
                        if (aCharge >= aEuCost) {
                            Logger.WARNING("Preparing Lighter 5");
                            aItemElec.discharge(aStack, aEuCost, 3, true, true, false);
                            return true;
                        }
                    }
                }
            }
        }
        Logger.WARNING("Preparing Lighter 0");
        return false;
    }

    private void useUp(ItemStack aStack) {}

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        int aUses = 0;
        if (aStack != null) {
            if (aStack.getItem() instanceof MetaGeneratedGregtechTools) {
                if (ChargingHelper.isItemValid(aStack)) {
                    if (aStack.getItem() instanceof IElectricItemManager aItemElec) {
                        double aCharge = aItemElec.getCharge(aStack);
                        long aEuCost = 4096 * 2;
                        aUses = (int) (aCharge / aEuCost);
                    }
                }
                boolean aCurrentMode;
                if (!NBTUtils.hasTagCompound(aStack)) {
                    NBTUtils.createEmptyTagCompound(aStack);
                }
                if (NBTUtils.hasKey(aStack, "aFireballMode")) {
                    aCurrentMode = NBTUtils.getBoolean(aStack, "aFireballMode");
                } else {
                    aStack.getTagCompound()
                        .setBoolean("aFireballMode", false);
                    aCurrentMode = false;
                }
                aList.add("Current Mode: " + EnumChatFormatting.RED + (aCurrentMode ? "Projectile" : "Fire Starter"));
            }
        }

        aList.add(this.mTooltipUses + " " + aUses);
        aList.add(this.mTooltipUnstackable);
        return aList;
    }
}
