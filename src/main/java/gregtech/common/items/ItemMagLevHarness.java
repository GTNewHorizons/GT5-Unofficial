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

public class ItemMagLevHarness extends GTGenericItem implements IBaubleExpanded {

    //private Tether activeTether;

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

//        if (activeTether != null) {
//            if (player.worldObj.provider.dimensionId != activeTether.dimID()
//                || player.getDistance(activeTether.sourceX(), activeTether.sourceY(), activeTether.sourceZ())
//                    > activeTether.range()) {
//                activeTether = null;
//                if (!player.capabilities.isCreativeMode) {
//                    player.capabilities.allowFlying = false;
//                    player.capabilities.isFlying = false;
//                    if (!player.worldObj.isRemote) {
//                        player.sendPlayerAbilities();
//                    }
//                }
//                player.capabilities.setFlySpeed(0.05f);
//            } else {
//                player.capabilities.allowFlying = true;
//            }
//        } else {
//
//        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        //activeTether = null;
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        return entityLivingBase instanceof EntityPlayer;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        return true;
    }

}
