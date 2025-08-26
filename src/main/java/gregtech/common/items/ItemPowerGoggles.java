package gregtech.common.items;

import static gregtech.api.enums.GTValues.NW;

import java.util.List;
import java.util.Objects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import appeng.api.util.DimensionalCoord;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GTGenericItem;
import gregtech.api.net.GTPacketUpdateItem;
import gregtech.common.handlers.PowerGogglesEventHandler;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class ItemPowerGoggles extends GTGenericItem implements IBauble, INetworkUpdatableItem {

    public ItemPowerGoggles(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
    }

    @Override
    // Links the goggles to the right clicked LSC
    // This is only called by the client and when a block is right clicked. onItemRightClick called if false returned
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        // Shift right click -> Player wants to unlink goggles so skip to onItemRightClick
        if (player.isSneaking()) {
            return false;
        }

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!isLSC(tileEntity)) {
            player
                .addChatMessage(new ChatComponentText(StatCollector.translateToLocal("GT5U.power_goggles.link_fail")));
            return true;
        }

        linkGoggles(stack, x, y, z, player, tileEntity);

        return true;
    }

    private void linkGoggles(ItemStack stack, int x, int y, int z, EntityPlayer player, TileEntity tileEntity) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);
        tag.setInteger("dim", player.dimension);
        tag.setString("dimName", ((IGregTechTileEntity) tileEntity).getWorld().provider.getDimensionName());
        NW.sendToServer(new GTPacketUpdateItem(tag));
        stack.setTagCompound(tag);

        player.addChatMessage(
            new ChatComponentText(StatCollector.translateToLocalFormatted("GT5U.power_goggles.link_lsc", x, y, z)));
    }

    private boolean isLSC(TileEntity tileEntity) {
        return tileEntity instanceof IGregTechTileEntity te
            && te.getMetaTileEntity() instanceof MTELapotronicSuperCapacitor;
    }

    @Override
    // Called by both server and client
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            return super.onItemRightClick(stack, world, player);
        }

        if (player.isSneaking()) {
            unlinkGoggles(stack, player);
            return super.onItemRightClick(stack, world, player);
        }

        ItemStack goggles = equipHeldGoggles(player, stack);
        return goggles != null ? goggles : super.onItemRightClick(stack, world, player);
    }

    private void unlinkGoggles(ItemStack stack, EntityPlayer player) {
        stack.setTagCompound(new NBTTagCompound());
        player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("GT5U.power_goggles.unlink")));
    }

    private ItemStack equipHeldGoggles(EntityPlayer player, ItemStack stack) {
        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);

        if (canEquip(stack, player)) {
            return equipIntoFreeSlot(stack, baubles, player);
        }
        return replaceWornGoggles(stack, baubles, player);
    }

    private ItemStack equipIntoFreeSlot(ItemStack stack, InventoryBaubles baubles, EntityPlayer player) {
        for (int i = 0; i < baubles.getSizeInventory(); i++) {
            ItemStack bauble = baubles.getStackInSlot(i);
            if (bauble != null) {
                continue;
            }

            baubles.setInventorySlotContents(i, stack.copy());
            this.onEquipped(stack, player);
            if (!player.capabilities.isCreativeMode) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
            return stack;
        }

        // No empty slots or can't equip goggles
        return null;
    }

    private ItemStack replaceWornGoggles(ItemStack stack, InventoryBaubles baubles, EntityPlayer player) {
        for (int i = 0; i < baubles.getSizeInventory(); i++) {
            ItemStack bauble = baubles.getStackInSlot(i);
            if (bauble == null || !(bauble.getItem() instanceof ItemPowerGoggles)) {
                continue;
            }

            baubles.setInventorySlotContents(i, stack.copy());
            ((IBauble) bauble.getItem()).onEquipped(stack, player);
            if (!player.capabilities.isCreativeMode) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
            return bauble.copy();
        }
        // No goggles to replace
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean bool) {
        super.addInformation(stack, player, tooltip, bool);
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || tag.hasNoTags()) {
            return;
        }
        tooltip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.power_goggles.link_tooltip",
                tag.getInteger("x"),
                tag.getInteger("y"),
                tag.getInteger("z"),
                tag.getString("dimName")));
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.UNIVERSAL;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        if (player.worldObj.isRemote || !(player instanceof EntityPlayerMP playerMP)) return;

        NBTTagCompound tag = itemstack.getTagCompound();
        DimensionalCoord coords = null;
        if (tag != null && !tag.hasNoTags()) {
            coords = new DimensionalCoord(
                tag.getInteger("x"),
                tag.getInteger("y"),
                tag.getInteger("z"),
                tag.getInteger("dim"));
        }

        DimensionalCoord current = PowerGogglesEventHandler.getLscLink(player.getUniqueID());
        if (!Objects.equals(current, coords)) {
            PowerGogglesEventHandler.setLscLink(playerMP, coords);
            PowerGogglesEventHandler.forceUpdate = true;
            PowerGogglesEventHandler.forceRefresh = true;
        }
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return getEquippedPowerGoggles(player) == null;
    }

    public static ItemStack getEquippedPowerGoggles(EntityLivingBase player) {
        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles((EntityPlayer) player);
        for (ItemStack bauble : baubles.stackList) {
            if (bauble != null && bauble.getItem() instanceof ItemPowerGoggles) {
                return bauble;
            }
        }
        return null;
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
