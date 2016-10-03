package gtPlusPlus.core.item.base.rings;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.recipe.UtilsRecipe;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemRing extends Item{

	final Material ringMaterial;
	final String materialName;
	final String unlocalName;

	public BaseItemRing(Material material) {
		this.ringMaterial = material;
		this.unlocalName = "itemRing"+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemRing");
		GameRegistry.registerItem(this, unlocalName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemR", "r"), UtilsItems.getSimpleStack(this));
		addExtruderRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Ring");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A " + materialName + " Ring.");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (ringMaterial.getRgbAsHex() == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return ringMaterial.getRgbAsHex();

	}

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Rings");
		
		//Extruder Recipe
		String tempIngot = unlocalName.replace("itemRing", "ingot");
		ItemStack tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		if (null != tempOutputStack){
			GT_Values.RA.addExtruderRecipe(tempOutputStack,
					ItemList.Shape_Extruder_Ring.get(1),
					UtilsItems.getSimpleStack(this, 4),
					(int) Math.max(ringMaterial.getMass() * 2L * 1, 1),
					6 * ringMaterial.vVoltageMultiplier);	
		}		
		
		//Shaped Recipe
		tempIngot = unlocalName.replace("itemRing", "stick");
		tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		if (null != tempOutputStack){
			UtilsRecipe.addShapedGregtechRecipe(
					"craftingToolWrench", null, null,
					null, tempOutputStack, null,
					null, null, null,
					UtilsItems.getSimpleStack(this, 1));
		}
	}

}
