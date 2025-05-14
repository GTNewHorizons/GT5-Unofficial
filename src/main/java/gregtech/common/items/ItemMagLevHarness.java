package gregtech.common.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;

import baubles.api.BaubleType;
import baubles.api.expanded.IBaubleExpanded;
import gregtech.api.enums.ItemList;
import gregtech.api.items.GTGenericItem;
import gregtech.common.data.maglev.Tether;
import gregtech.common.data.maglev.TetherManager;
import gregtech.common.tileentities.machines.basic.MTEMagLevPylon;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public class ItemMagLevHarness extends GTGenericItem implements IBaubleExpanded {

    // private Tether activeTether;

    public ItemMagLevHarness() {
        super("maglev_harness", "MagLev Harness", null);
        setMaxStackSize(1);

        ItemList.MagLevHarness.set(this);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return null;
    }

    @Override
    public String[] getBaubleTypes(ItemStack itemstack) {
        return new String[] { "universal" };
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (!(entityLivingBase instanceof EntityPlayer player)) return;
        if (player instanceof FakePlayer) return;

        Tether activeTether = TetherManager.PLAYER_TETHERS.get(player);
        if (activeTether != null) {
            if (player.worldObj.provider.dimensionId != activeTether.dimID()
                || player.getDistance(activeTether.sourceX(), activeTether.sourceY(), activeTether.sourceZ())
                    > activeTether.range()) {
                TetherManager.PLAYER_TETHERS.replace(player, null);
                if (!player.capabilities.isCreativeMode) {
                    player.capabilities.allowFlying = false;
                    player.capabilities.isFlying = false;
                    if (!player.worldObj.isRemote) {
                        player.sendPlayerAbilities();
                    }
                }
            } else {
                player.capabilities.allowFlying = true;
                if (player.worldObj.isRemote) player.capabilities.setFlySpeed(0.05f);
            }
        } else {
            ObjectOpenHashSet<MTEMagLevPylon> pylons = TetherManager.ACTIVE_PYLONS
                .get(player.worldObj.provider.dimensionId);
            var iterator = pylons.iterator();
            while (iterator.hasNext()) {
                MTEMagLevPylon pylon = iterator.next();
                if (player.getDistance(
                    pylon.machineTether.sourceX(),
                    pylon.machineTether.sourceY(),
                    pylon.machineTether.sourceZ()) <= pylon.machineTether.range()) {
                    TetherManager.PLAYER_TETHERS.replace(player, pylon.machineTether);
                    break;
                }
            }
        }
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
