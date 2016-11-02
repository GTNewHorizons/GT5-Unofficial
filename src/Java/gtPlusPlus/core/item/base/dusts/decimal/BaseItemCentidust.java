package gtPlusPlus.core.item.base.dusts.decimal;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemCentidust extends Item{

	final Material dustMaterial;
	final String materialName;
	final String unlocalName;

	public BaseItemCentidust(Material material) {
		this.dustMaterial = material;
		this.unlocalName = "itemCentidust"+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(10);
		this.setTextureName(CORE.MODID + ":" + "itemCentidust"); //TODO
		GameRegistry.registerItem(this, unlocalName);
		//GT_OreDictUnificator.registerOre(unlocalName.replace("itemR", "r"), UtilsItems.getSimpleStack(this)); //TODO
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Centidust");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"1% of a " + materialName + " dust pile.");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		return dustMaterial.getRgbAsHex();
	}

}
