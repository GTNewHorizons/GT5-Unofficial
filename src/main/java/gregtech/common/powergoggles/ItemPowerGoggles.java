package gregtech.common.powergoggles;

import static gregtech.api.enums.GTValues.NW;

import java.util.List;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GTGenericItem;
import gregtech.api.net.GTPacketUpdateItem;
import gregtech.common.powergoggles.handlers.PowerGogglesEventHandler;

public class ItemPowerGoggles extends GTGenericItem implements IBauble, INetworkUpdatableItem {

    private final PowerGogglesEventHandler EVENT_HANDLER = PowerGogglesEventHandler.getInstance();

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
        if (!PowerGogglesUtil.isLSC(tileEntity)) {
            player
                .addChatMessage(new ChatComponentText(StatCollector.translateToLocal("GT5U.power_goggles.link_fail")));
            return true;
        }

        linkGoggles(stack, x, y, z, player, tileEntity);

        return true;
    }

    private void linkGoggles(ItemStack stack, int x, int y, int z, EntityPlayer player, TileEntity tileEntity) {
        ItemStack oldStack = stack.copy();

        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);
        tag.setInteger("dim", player.dimension);
        tag.setString("dimName", ((IGregTechTileEntity) tileEntity).getWorld().provider.getDimensionName());
        stack.setTagCompound(tag);
        setEnchantmentData(oldStack, stack);
        NW.sendToServer(new GTPacketUpdateItem(tag));

        player.addChatMessage(
            new ChatComponentText(StatCollector.translateToLocalFormatted("GT5U.power_goggles.link_lsc", x, y, z)));
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
        ItemStack oldStack = stack.copy();
        stack.setTagCompound(new NBTTagCompound());
        setEnchantmentData(oldStack, stack);
        player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("GT5U.power_goggles.unlink")));
    }

    private void setEnchantmentData(ItemStack oldStack, ItemStack newStack) {
        var enchantments = EnchantmentHelper.getEnchantments(oldStack);
        if (!enchantments.isEmpty()) {
            EnchantmentHelper.setEnchantments(enchantments, newStack);
            if (oldStack.getTagCompound()
                .hasKey("RepairCost", Constants.NBT.TAG_INT)) {
                newStack.getTagCompound()
                    .setInteger(
                        "RepairCost",
                        oldStack.getTagCompound()
                            .getInteger("RepairCost"));
            }
        }
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
        EVENT_HANDLER.updatePlayerLink(itemstack, playerMP);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return !PowerGogglesUtil.isPlayerWearingGoggles((EntityPlayer) player);
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
