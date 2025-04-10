package gregtech.common.items;

import static gregtech.api.enums.GTValues.NW;

import java.util.List;

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
import gregtech.api.net.GTPacketLinkPowerGoggles;
import gregtech.api.net.GTPacketUpdateItem;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class ItemPowerGoggles extends GTGenericItem implements IBauble, INetworkUpdatableItem {

    public ItemPowerGoggles(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
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
                    tag.setString("dimName", te.getWorld().provider.getDimensionName());
                    NW.sendToServer(new GTPacketUpdateItem(tag));
                    stack.setTagCompound(tag);
                    player.addChatMessage(
                        new ChatComponentText(String.format("Goggles linked to LSC at %d,%d,%d", x, y, z)));
                }
            } else {
                player.addChatMessage(new ChatComponentText("That's not an LSC..."));
            }
            return true;
        }
        return super.onItemUseFirst(stack, player, world, x, y, z, ordinalSide, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) { // called by both server and
                                                                                           // client
        if (player instanceof EntityPlayerMP mPlayer) {
            if (player.isSneaking()) {
                stack.setTagCompound(new NBTTagCompound());
                player.addChatMessage(new ChatComponentText("Goggles unlinked."));
            } else {

                InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
                // Try to equip into empty slots first
                for (int i = 0; i < baubles.getSizeInventory(); i++) {
                    ItemStack bauble = baubles.getStackInSlot(i);
                    if (bauble == null && canEquip(stack, player)) {
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
                    if (bauble != null && ((IBauble) bauble.getItem()).canUnequip(bauble, player)
                        && bauble.getUnlocalizedName()
                            .equals("gt.Power_Goggles")) {
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
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean bool) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && !stack.getTagCompound()
            .hasNoTags()) {
            tooltip.add(
                String.format(
                    "Linked to LSC at %d,%d,%d: %s",
                    tag.getInteger("x"),
                    tag.getInteger("y"),
                    tag.getInteger("z"),
                    tag.getString("dimName")));
        }
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.UNIVERSAL;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        NBTTagCompound tag = itemstack.getTagCompound();
        if (tag != null && !tag.hasNoTags()) {
            NW.sendToServer(
                new GTPacketLinkPowerGoggles(
                    new DimensionalCoord(
                        tag.getInteger("x"),
                        tag.getInteger("y"),
                        tag.getInteger("z"),
                        tag.getInteger("dim"))));
        } else {
            NW.sendToServer(new GTPacketLinkPowerGoggles());
        }
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles((EntityPlayer) player);
        for (int i = 0; i < baubles.getSizeInventory(); i++) {
            ItemStack bauble = baubles.getStackInSlot(i);
            if (bauble == null) continue;
            if (baubles.getStackInSlot(i)
                .getUnlocalizedName()
                .equals("gt.Power_Goggles")) {
                return false;
            }
        }
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
