package gregtech.common.tools;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Mods;
import gregtech.api.items.ItemTool;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.common.blocks.BlockFrameBox;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import mods.railcraft.common.blocks.machine.TileMultiBlock;
import thaumcraft.common.tiles.TileOwned;

public class ToolVajra extends ItemTool implements IElectricItem {

    public int maxCharge = (int) 1e8;
    public int baseCost = 3333;
    public int tier = 5;
    public double transferLimit = V[tier];
    private final String tooltip;

    public ToolVajra(String aUnlocalized, String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage,
        boolean aSwingIfUsed) {
        super(aUnlocalized, aEnglish, aTooltip, aMaxDamage, aEntityDamage, aSwingIfUsed);
        this.tooltip = aTooltip;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List<ItemStack> itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        if (getChargedItem(itemStack) == this) {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, 2147483647, 2147483647, true, false);
            itemList.add(charged);
        }
        if (getEmptyItem(itemStack) == this) {
            itemList.add(new ItemStack(this, 1, getMaxDamage()));
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int par4, int par5, int par6,
        EntityLivingBase entityLiving) {
        ElectricItem.manager.use(stack, baseCost, entityLiving);
        return true;
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return ElectricItem.manager.canUse(stack, baseCost);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        return Integer.MAX_VALUE;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if (!ElectricItem.manager.canUse(stack, baseCost)) {
            return 0.0F;
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase target, EntityLivingBase source) {
        if (source instanceof EntityPlayer player) {
            target.attackEntityFrom(DamageSource.causePlayerDamage(player), this.attackDamage);
        } else {
            target.attackEntityFrom(DamageSource.causeMobDamage(source), this.attackDamage);
        }
        return true;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List<String> list, boolean par4) {
        list.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("gt.vajra.tooltip.flavor"));
        list.add(
            EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted("gt.vajra.tooltip.charge", VN[tier]));
        list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("gt.vajra.tooltip.silk_touch"));
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return maxCharge;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return tier;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return transferLimit;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) return this.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        Block target = world.getBlock(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        int metaData = world.getBlockMetadata(x, y, z);

        if (target.blockHardness < 0) return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        if (!ElectricItem.manager.canUse(stack, baseCost))
            return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        if (!isHarvestableTileEntity(tileEntity, target, player) && !player.isSneaking()
            || !isHarvestableOwned(tileEntity, player))
            return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);

        if (world.isRemote) {
            Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(x, y, z, side);
            player.swingItem();
        } else {
            target.onBlockHarvested(world, x, y, z, metaData, player);
            if (target.removedByPlayer(world, player, x, y, z, true)) {
                target.onBlockDestroyedByPlayer(world, x, y, z, metaData);
                target.harvestBlock(world, player, x, y, z, metaData);
                world.notifyBlocksOfNeighborChange(x, y, z, target);
            }
        }
        stack.getTagCompound()
            .setBoolean("harvested", true); // prevent onItemRightClick from going through
        ElectricItem.manager.use(stack, baseCost, player);
        return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    private boolean isHarvestableOwned(TileEntity tileEntity, EntityPlayer player) {
        if (!Mods.Thaumcraft.isModLoaded() || !(tileEntity instanceof TileOwned owned)) return true;
        return owned.owner.equals(player.getDisplayName());
    }

    private boolean isHarvestableTileEntity(TileEntity tileEntity, Block target, EntityPlayer player) {
        if (Mods.Railcraft.isModLoaded() && isUnformedRCMulti(tileEntity)) return true;
        if (tileEntity instanceof IInventory inv && inv.getSizeInventory() > 0) return false;
        if (isHarvestableGTSpecial(target, tileEntity) && !player.isSneaking()) return true;
        if (tileEntity instanceof BaseTileEntity bte && bte.useModularUI()) return false;
        return true;
    }

    @Optional.Method(modid = Mods.ModIDs.RAILCRAFT)
    private boolean isUnformedRCMulti(TileEntity tileEntity) {
        return tileEntity instanceof TileMultiBlock tmb && !tmb.isStructureValid();
    }

    private boolean isHarvestableGTSpecial(Block target, TileEntity tileEntity) {
        if (target instanceof BlockFrameBox) return tileEntity == null;

        // Cables extend BaseMetaPipeEntity (???)
        if (tileEntity instanceof BaseMetaPipeEntity) return true;
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        NBTTagCompound tag = itemStackIn.hasTagCompound() ? itemStackIn.getTagCompound() : new NBTTagCompound();
        if (!worldIn.isRemote && !itemStackIn.hasTagCompound()) {
            itemStackIn.setTagCompound(tag);
        }
        if (tag.getBoolean("harvested")) {
            tag.removeTag("harvested");
            return super.onItemRightClick(itemStackIn, worldIn, player);
        }

        if (!worldIn.isRemote && player.isSneaking()) {
            if (itemStackIn.getTagCompound()
                .hasKey("ench")) {
                tag.removeTag("ench");
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Disabled silk touch"));
            } else {
                // Adds the "ench" tag to the tool
                itemStackIn.addEnchantment(Enchantment.silkTouch, 1);
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Enabled silk touch"));
            }
        }
        return super.onItemRightClick(itemStackIn, worldIn, player);
    }
}
