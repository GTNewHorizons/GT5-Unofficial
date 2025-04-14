package gregtech.common.tools;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.ItemTool;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class ToolVajraButNotARR extends ItemTool implements IElectricItem {

    public int maxCharge = (int) 1e7;
    public int baseCost = 1000;
    public int tier = 5;
    public double transferLimit = V[tier];
    private final String tooltip;

    public ToolVajraButNotARR(String aUnlocalized, String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage,
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
        ElectricItem.manager.use(stack, baseCost * block.blockHardness, entityLiving);
        return true;
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return ElectricItem.manager.canUse(stack, baseCost * block.blockHardness);
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if (!ElectricItem.manager.canUse(stack, baseCost * block.blockHardness)) {
            return 0.0F;
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
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
        list.add(this.tooltip);
        list.add("Charge with " + VN[tier] + " thing idk");
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
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
                                  int side, float hitX, float hitY, float hitZ) {
        Block target = world.getBlock(x,y,z);
        int metaData = world.getBlockMetadata(x,y,z);

        if(!ElectricItem.manager.canUse(stack, baseCost * target.blockHardness)) return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        if(target instanceof ITileEntityProvider && !player.isSneaking()) return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);

        if(world.isRemote){
            Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(x,y,z,side);
        } else {
            target.onBlockHarvested(world, x, y, z, metaData, player);
            if (target.removedByPlayer(world, player, x, y, z, true)) {
                target.onBlockDestroyedByPlayer(world, x, y, z, metaData);
                target.harvestBlock(world, player, x, y, z, metaData);
            }
        }
        stack.getTagCompound().setBoolean("harvested",true);
        ElectricItem.manager.use(stack, baseCost * target.blockHardness, player);
        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        NBTTagCompound tag = itemStackIn.hasTagCompound() ? itemStackIn.getTagCompound() : new NBTTagCompound();
        if(!worldIn.isRemote && !itemStackIn.hasTagCompound()){
            tag.setBoolean("silk",false);
            itemStackIn.setTagCompound(tag);
        }
        if(tag.getBoolean("harvested")) {
            tag.removeTag("harvested");
            return super.onItemRightClick(itemStackIn,worldIn,player);
        }

        if(!worldIn.isRemote && player.isSneaking()){
            if(itemStackIn.getTagCompound().getBoolean("silk")){
                tag.removeTag("ench");
                tag.setBoolean("silk",false);
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Disabled vajra silk touch"));
            } else {
                itemStackIn.addEnchantment(Enchantment.silkTouch, 1);
                tag.setBoolean("silk", true);
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Enabled vajra silk touch"));
            }
        }
        return super.onItemRightClick(itemStackIn,worldIn,player);
    }
}
