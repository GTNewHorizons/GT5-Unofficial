package gtPlusPlus.core.world.darkworld.item;

import java.util.List;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class itemDarkWorldPortalTrigger extends Item {
	public itemDarkWorldPortalTrigger() {
		super();
		this.maxStackSize = 1;
		setMaxDamage(64);
		setCreativeTab(CreativeTabs.tabTools);
		this.setTextureName(CORE.MODID + ":" + "itemAlkalusDisk");
	}

	@Override
	public Item setMaxStackSize(int int1) {
		return super.setMaxStackSize(1);
	}

	@Override
	public EnumRarity getRarity(ItemStack thisItem) {
		return EnumRarity.epic;
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return true;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return EnumChatFormatting.GOLD+"Alkalus Disk ["+EnumChatFormatting.RED+"Activated"+EnumChatFormatting.GOLD+"]";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(EnumChatFormatting.GREEN+"A key for entering the Dark World.");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) {
		return Utils.rgbtoHexValue(255, 128, 0);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7,
			float par8, float par9, float par10) {
		if (par7 == 0) {
			par5--;
		}
		if (par7 == 1) {
			par5++;
		}
		if (par7 == 2) {
			par6--;
		}
		if (par7 == 3) {
			par6++;
		}
		if (par7 == 4) {
			par4--;
		}
		if (par7 == 5) {
			par4++;
		}
		if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
			return false;
		}
		Block i1 = par3World.getBlock(par4, par5, par6);
		if (i1 == Blocks.air) {
			par3World.playSoundEffect(par4 + 0.5D, par5 + 0.5D, par6 + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
			if (Dimension_DarkWorld.portalBlock.tryToCreatePortal(par3World, par4, par5, par6)){
				//Make a Portal
			}
			else {
				if (!par3World.isRemote){
					par3World.setBlock(par4, par5, par6, ModBlocks.blockHellfire, 0, 3);
				}
			}
		}
		par1ItemStack.damageItem(1, par2EntityPlayer);
		return true;
	}
}