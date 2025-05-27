package gregtech.common.items;

import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;

import com.gtnewhorizon.gtnhlib.util.DistanceUtil;

import baubles.api.BaubleType;
import baubles.api.expanded.IBaubleExpanded;
import gregtech.api.enums.ItemList;
import gregtech.api.items.GTGenericItem;
import gregtech.common.data.maglev.Tether;
import gregtech.common.data.maglev.TetherManager;
import gregtech.common.tileentities.machines.basic.MTEMagLevPylon;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
        return new String[] { "belt" };
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (!(entityLivingBase instanceof EntityPlayer player)) return;
        if (player instanceof FakePlayer) return;
        if (player.worldObj.isRemote) return;

        Tether activeTether;
        var grid = TetherManager.ACTIVE_PYLONS.get(player.dimension);
        var nearby = (ObjectArrayList<MTEMagLevPylon>) grid
            .findNearbyChebyshev((int) player.posX, (int) player.posY, (int) player.posZ, 64);

        if (!nearby.isEmpty()) {
            // set pylon distances
            var pylonDistances = nearby.stream()
                .collect(
                    Collectors.toMap(
                        pylon -> pylon,
                        pylon -> DistanceUtil.chebyshevDistance(
                            player.posX,
                            player.posY,
                            player.posZ,
                            pylon.getBaseMetaTileEntity()
                                .getXCoord(),
                            pylon.getBaseMetaTileEntity()
                                .getYCoord(),
                            pylon.getBaseMetaTileEntity()
                                .getZCoord())));
            // get closest one
            MTEMagLevPylon closest = pylonDistances.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .orElse(null)
                .getKey();

            if (closest != null && pylonDistances.get(closest) <= closest.machineTether.range()) {
                activeTether = closest.machineTether;
            } else {
                activeTether = null;
            }
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
            // so that the player doesnt go splat when going from inside range to outside range
            // it does allow them to double space to try to fly again, but it is only for a tick
            if (player.onGround && player.fallDistance < 1F) {
                player.capabilities.allowFlying = false;
            }
        }
        player.sendPlayerAbilities();
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (!(entityLivingBase instanceof EntityPlayer player)) return;
        if (player instanceof FakePlayer) return;
        TetherManager.PLAYER_TETHERS.replace(player, null);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (!(entityLivingBase instanceof EntityPlayer player)) return;
        if (player instanceof FakePlayer) return;
        TetherManager.PLAYER_TETHERS.replace(player, null);
        if (!player.capabilities.isCreativeMode) {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            if (!player.worldObj.isRemote) {
                player.sendPlayerAbilities();
            }
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
