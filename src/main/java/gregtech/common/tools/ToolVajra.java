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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.ItemTool;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class ToolVajra extends ItemTool implements IElectricItem {

    public int maxCharge = (int) 1e8;
    public int baseCost = 3333;
    public int tier = 5;
    public double transferLimit = V[tier];

    public ToolVajra(String aUnlocalized, String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage,
        boolean aSwingIfUsed) {
        super(aUnlocalized, aEnglish, aTooltip, aMaxDamage, aEntityDamage, aSwingIfUsed);
    }

    @Override
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
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        // By default GTGenericItem sneak-use will trigger block activation. We don't want that since shift-right click
        // of vajra should be handled by the vajra and we don't want to open any gui.
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (!ElectricItem.manager.canUse(stack, baseCost))
            return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        if (world.isRemote) {
            // This sends a packet to server, break on server side is handled by packet handler
            Minecraft.getMinecraft().playerController.clickBlock(x, y, z, side);
            player.swingItem();
        }
        stack.getTagCompound()
            .setBoolean("harvested", true); // prevent onItemRightClick from going through
        // This allows offhand to be used, since returning false will continue to try to process offhand
        return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player) {
        if (ItemStackNBT.getBoolean(stack, "harvested")) {
            ItemStackNBT.removeTag(stack, "harvested");
            return super.onItemRightClick(stack, worldIn, player);
        }
        if (!worldIn.isRemote && player.isSneaking()) {
            if (ItemStackNBT.hasKey(stack, "ench")) {
                ItemStackNBT.removeTag(stack, "ench");
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Disabled silk touch"));
            } else {
                // Adds the "ench" tag to the tool
                stack.addEnchantment(Enchantment.silkTouch, 1);
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Enabled silk touch"));
            }
        }
        return super.onItemRightClick(stack, worldIn, player);
    }
}
