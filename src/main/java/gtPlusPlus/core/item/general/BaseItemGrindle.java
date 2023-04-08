package gtPlusPlus.core.item.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.handler.GuiHandler;

public class BaseItemGrindle extends Item {

    protected final String unlocalName;

    public BaseItemGrindle() {
        this.unlocalName = "itemGrindleTablet";
        this.setUnlocalizedName("itemGrindleTablet");
        this.setTextureName(GTPlusPlus.ID + ":" + "itemTablet");
        GameRegistry.registerItem(this, "itemGrindleTablet");
        this.setMaxStackSize(1);
        this.setCreativeTab(AddToCreativeTab.tabOther);
    }

    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 1;
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer player) {
        if (!world.isRemote) {
            if (!player.isSneaking()) {
                player.openGui(GTplusplus.instance, GuiHandler.GUI9, world, 0, 0, 0);
            }
        }
        return itemstack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(GTPlusPlus.ID + ":" + "itemTablet");
    }

    @Override
    public String getPotionEffect(ItemStack p_150896_1_) {
        return super.getPotionEffect(p_150896_1_);
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
        super.addInformation(p_77624_1_, p_77624_2_, aList, p_77624_4_);
        aList.add(StatCollector.translateToLocal("item.itemGrindleTablet.tooltip"));
    }

    @Override
    public EnumRarity getRarity(ItemStack i) {
        return EnumRarity.uncommon;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }
}
