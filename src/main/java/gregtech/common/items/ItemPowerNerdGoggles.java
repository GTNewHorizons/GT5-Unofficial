package gregtech.common.items;

import static gregtech.api.enums.GTValues.NW;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import appeng.api.util.DimensionalCoord;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GTGenericItem;
import gregtech.api.net.GTPacketLinkGoggles;
import gregtech.api.net.GTPacketUpdateItem;
import gregtech.common.handlers.PowerGogglesEventHandler;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class ItemPowerNerdGoggles extends GTGenericItem implements IBauble, INetworkUpdatableItem {

    public ItemPowerNerdGoggles(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) { // this is only ever called by client
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IGregTechTileEntity te) {
            if (te.getMetaTileEntity() instanceof MTELapotronicSuperCapacitor lsc) {
                if (player instanceof EntityClientPlayerMP && !player.isSneaking()) {
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setInteger("x", x);
                    tag.setInteger("y", y);
                    tag.setInteger("z", z);
                    tag.setInteger("dim", player.dimension);
                    NW.sendToServer(new GTPacketUpdateItem(tag));
                    stack.setTagCompound(tag);
                    NW.sendToServer(new GTPacketLinkGoggles(new DimensionalCoord(tileEntity)));
                    player.addChatMessage(
                        new ChatComponentText(String.format("Goggles linked to LSC at %d,%d,%d", x, y, z)));
                }
            } else {
                player.addChatMessage(new ChatComponentText("Dude... That's not an LSC. Are you dumb?"));
            }
            return true;
        }
        return super.onItemUseFirst(stack, player, world, x, y, z, ordinalSide, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) { // called by both server and
                                                                                           // client
        if (player instanceof EntityPlayerMP) {
            if (player.isSneaking()) {
                PowerGogglesEventHandler.lscLink = null;
                stack.setTagCompound(new NBTTagCompound());
                PowerGogglesEventHandler.forceUpdate = true;
                PowerGogglesEventHandler.forceRefresh = true;
                player.addChatMessage(new ChatComponentText("Goggles unlinked."));
            } else {

                InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
                // Try to equip into empty slots first
                for (int i = 0; i < baubles.getSizeInventory(); i++) {
                    ItemStack bauble = baubles.getStackInSlot(i);
                    if (bauble == null) {
                        baubles.setInventorySlotContents(i, stack.copy());
                        this.onEquipped(stack, player);
                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }
                        return stack;
                    }
                }
                for (int i = 0; i < baubles.getSizeInventory(); i++) {
                    ItemStack bauble = baubles.getStackInSlot(i);
                    if (bauble != null && ((IBauble) bauble.getItem()).canUnequip(bauble, player)) {
                        baubles.setInventorySlotContents(i, stack.copy());
                        ((IBauble) bauble.getItem()).onEquipped(bauble, player);
                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }
                        return bauble.copy();
                    }
                }
            }
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.UNIVERSAL;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

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

    @Override
    public boolean receive(ItemStack stack, EntityPlayerMP player, NBTTagCompound tag) {
        stack.setTagCompound(tag);
        return true;
    }
}
