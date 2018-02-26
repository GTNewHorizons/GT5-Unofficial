package gtPlusPlus.core.item.base.dusts.decimal;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;

public class BaseItemDecidust extends Item{

	final Material dustMaterial;
	final String materialName;
	final String unlocalName;

	public BaseItemDecidust(final Material material) {
		this.dustMaterial = material;
		this.unlocalName = "itemDecidust"+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(this.unlocalName);
		this.setMaxStackSize(10);
		this.setTextureName(CORE.MODID + ":" + "itemDecidust"); //TODO
		GameRegistry.registerItem(this, this.unlocalName);
		//GT_OreDictUnificator.registerOre(unlocalName.replace("itemR", "r"), UtilsItems.getSimpleStack(this)); //TODO
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		return (this.materialName+ " Decidust");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if ((this.materialName != null) && (this.materialName != "") && !this.materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"10% of a " + this.materialName + " dust pile.");
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return this.materialName;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return this.dustMaterial.getRgbAsHex();
	}

}
