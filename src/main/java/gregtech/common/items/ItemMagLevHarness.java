package gregtech.common.items;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.FakePlayer;

import com.gtnewhorizon.gtnhlib.GTNHLib;

import baubles.api.BaubleType;
import baubles.api.expanded.BaubleItemHelper;
import baubles.api.expanded.IBaubleExpanded;
import baubles.common.lib.PlayerHandler;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.items.GTGenericItem;
import gregtech.api.net.GTPacketTether;
import gregtech.common.data.maglev.Tether;

public class ItemMagLevHarness extends GTGenericItem implements IBaubleExpanded {

    public ItemMagLevHarness() {
        super("MaglevHarness", "MagLev Harness", null);
        setMaxStackSize(1);

        ItemList.MagLevHarness.set(this);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return null;
    }

    @Override
    public String[] getBaubleTypes(ItemStack itemstack) {
        return new String[] { "belt", "wings" };
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> tooltip, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, tooltip, aF3_H);
        tooltip.add(StatCollector.translateToLocalFormatted("GT5U.maglevHarness.tooltip"));
        BaubleItemHelper.addSlotInformation(tooltip, getBaubleTypes(aStack));
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (entityLivingBase.worldObj == null || entityLivingBase.worldObj.isRemote) return;
        if (!(entityLivingBase instanceof EntityPlayerMP player)) return;
        if (player instanceof FakePlayer) return;

        Tether activeTether = GTMod.proxy.tetherManager.getConnectedPylon(player);
        Tether closestActivePylon = GTMod.proxy.tetherManager.getClosestActivePylon(player, 48);
        Tether newTether = null;

        if (closestActivePylon != null) {
            newTether = closestActivePylon;
        }

        if (activeTether == newTether) return;
        // only trigger the below if the player's tether changes
        if (newTether != null) {
            GTValues.NW.sendToPlayer(
                new GTPacketTether(newTether.sourceX(), newTether.sourceY(), newTether.sourceZ()),
                player);
        } else { // only run on tether disconnect
            if (Math.random() <= 0.03) {
                GTNHLib.proxy.sendMessageAboveHotbar(
                    player,
                    new ChatComponentTranslation(StatCollector.translateToLocal("GT5U.maglevHarness.pylons")),
                    25,
                    true,
                    false);
            }
        }

        GTMod.proxy.tetherManager.connectPlayer(player, newTether);

        setFly(player, player.capabilities.isCreativeMode || newTether != null);
    }

    private static void setFly(EntityPlayer player, boolean fly) {
        if (player.capabilities.allowFlying == fly) {
            player.sendPlayerAbilities();
            return;
        }
        if (fly) {
            player.capabilities.allowFlying = true;
        } else {
            if (!player.capabilities.isCreativeMode) {
                player.fallDistance = 0;
                player.capabilities.allowFlying = false;
                player.capabilities.isFlying = false;
            }
        }
        player.sendPlayerAbilities();
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (entityLivingBase.worldObj != null && entityLivingBase.worldObj.isRemote) return;
        if (!(entityLivingBase instanceof EntityPlayer player)) return;
        if (player instanceof FakePlayer) return;

        GTMod.proxy.tetherManager.disconnectPlayer(player);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (entityLivingBase.worldObj != null && entityLivingBase.worldObj.isRemote) return;
        if (!(entityLivingBase instanceof EntityPlayer player)) return;
        if (player instanceof FakePlayer) return;

        GTMod.proxy.tetherManager.disconnectPlayer(player);
        setFly(player, false);
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (!(entityLivingBase instanceof EntityPlayer player)) return false;
        if (player instanceof FakePlayer) return false;

        var baubleInv = PlayerHandler.getPlayerBaubles(player);

        for (int i = 0; i < baubleInv.getSizeInventory(); i++) {
            ItemStack stack = baubleInv.getStackInSlot(i);
            if (stack != null && stack.getItem() == this) return false;
        }
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        return true;
    }
}
