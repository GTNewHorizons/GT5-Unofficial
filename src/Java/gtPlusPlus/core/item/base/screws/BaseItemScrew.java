package gtPlusPlus.core.item.base.screws;

import gregtech.api.enums.GT_Values;
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

public class BaseItemScrew extends Item{

	final Material screwMaterial;
	final String materialName;
	final String unlocalName;

	public BaseItemScrew(Material material) {
		this.screwMaterial = material;
		this.unlocalName = "itemScrew"+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemScrew");
		GameRegistry.registerItem(this, unlocalName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemS", "s"), UtilsItems.getSimpleStack(this));
		addLatheRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Screw");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A 8mm Screw, fabricated out of some " + materialName + ".");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (screwMaterial.getRgbAsHex() == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return screwMaterial.getRgbAsHex();

	}

	private void addLatheRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Screws");
		ItemStack boltStack = UtilsItems.getItemStackOfAmountFromOreDict(unlocalName.replace("itemScrew", "bolt"), 1);
		if (null != boltStack){			
	         GT_Values.RA.addLatheRecipe(
	        		 boltStack,
	        		 UtilsItems.getSimpleStack(this),
	        		 null,
	        		 (int) Math.max(screwMaterial.getMass() / 8L, 1L),
	        		 4);
	         
			
			UtilsRecipe.recipeBuilder(
					"craftingToolFile", boltStack, null,
					boltStack, null, null,
					null, null, null,
					UtilsItems.getSimpleStack(this));
		}				
	}

}
