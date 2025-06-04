package gregtech.common.items;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.FakePlayer;

import com.gtnewhorizon.gtnhlib.util.DistanceUtil;

import baubles.api.BaubleType;
import baubles.api.expanded.BaubleItemHelper;
import baubles.api.expanded.IBaubleExpanded;
import gregtech.api.enums.ItemList;
import gregtech.api.items.GTGenericItem;
import gregtech.common.data.maglev.Tether;
import gregtech.common.data.maglev.TetherManager;

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
        if (entityLivingBase.worldObj != null && entityLivingBase.worldObj.isRemote) return;
        if (!(entityLivingBase instanceof EntityPlayer player)) return;
        if (player instanceof FakePlayer) return;

        Tether activeTether;
        var grid = TetherManager.ACTIVE_PYLONS.get(player.dimension);
        var nearbyPylon = grid.findClosestNearbyChebyshev((int) player.posX, (int) player.posY, (int) player.posZ, 64);

        if (nearbyPylon != null && DistanceUtil.chebyshevDistance(
            player.posX,
            player.posY,
            player.posZ,
            nearbyPylon.getBaseMetaTileEntity()
                .getXCoord(),
            nearbyPylon.getBaseMetaTileEntity()
                .getYCoord(),
            nearbyPylon.getBaseMetaTileEntity()
                .getZCoord())
            <= nearbyPylon.machineTether.range()) {
            activeTether = nearbyPylon.machineTether;
        } else {
            activeTether = null;
        }

        TetherManager.PLAYER_TETHERS.replace(player, activeTether);

        setFly(player, player.capabilities.isCreativeMode || activeTether != null);
    }

    private static void setFly(EntityPlayer player, boolean fly) {
        if (player.capabilities.allowFlying == fly) {
            return;
        }
        if (fly) {
            player.capabilities.allowFlying = true;
        } else {
            player.capabilities.isFlying = false;
            // so that the player doesn't go splat when going from inside range to outside range
            // it does allow them to double space to try to fly again, but it is only for a tick
            if (player.onGround && player.fallDistance < 1F) {
                player.capabilities.allowFlying = false;
            }
        }
        player.sendPlayerAbilities();
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (entityLivingBase.worldObj != null && entityLivingBase.worldObj.isRemote) return;
        if (!(entityLivingBase instanceof EntityPlayer player)) return;
        if (player instanceof FakePlayer) return;

        TetherManager.PLAYER_TETHERS.replace(player, null);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (entityLivingBase.worldObj != null && entityLivingBase.worldObj.isRemote) return;
        if (!(entityLivingBase instanceof EntityPlayer player)) return;
        if (player instanceof FakePlayer) return;

        TetherManager.PLAYER_TETHERS.replace(player, null);
        if (!player.capabilities.isCreativeMode) {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            player.sendPlayerAbilities();
        }
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        return (entityLivingBase instanceof EntityPlayer player) && !(player instanceof FakePlayer);
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        return true;
    }

}
