package gtPlusPlus.core.item.general;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.handler.GuiHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaseItemGrindle extends Item{

	protected final String unlocalName;


	public BaseItemGrindle(){
		this.unlocalName = "itemGrindleTablet";
		this.setUnlocalizedName("itemGrindleTablet");
		this.setTextureName(CORE.MODID + ":" + "itemTablet");
		GameRegistry.registerItem(this, "itemGrindleTablet");
		GT_OreDictUnificator.registerOre("tabletGit", ItemUtils.getSimpleStack(this));
		this.setMaxStackSize(1);
		this.setCreativeTab(AddToCreativeTab.tabOther);
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack) {
		return 1; 
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer player)
	{
		if (!world.isRemote){
			if (!player.isSneaking()) {
				player.openGui(GTplusplus.instance, GuiHandler.GUI9, world, 0, 0, 0);
			}
		}

		return itemstack;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return ("Git");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(CORE.MODID + ":" + "itemTablet");
	}

	@Override
	public String getPotionEffect(ItemStack p_150896_1_) {
		// TODO Auto-generated method stub
		return super.getPotionEffect(p_150896_1_);
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_,
			List p_77624_3_, boolean p_77624_4_) {
		// TODO Auto-generated method stub
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
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
