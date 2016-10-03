package gtPlusPlus.core.item.base.rods;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.recipe.UtilsRecipe;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemRod extends Item{

	final Material rodMaterial;
	final String materialName;
	final String unlocalName;

	public BaseItemRod(Material material, int sRadioactivity) {
		this.rodMaterial = material;
		this.unlocalName = "itemRod"+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalName);
		this.setTextureName(CORE.MODID + ":" + "itemRod");
		this.setMaxStackSize(64);
		this.sRadiation = sRadioactivity;
		GameRegistry.registerItem(this, unlocalName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemRod", "stick"), UtilsItems.getSimpleStack(this));
		
		if (!material.equals(ELEMENT.URANIUM233)){
			addExtruderRecipe();			
		}
		
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Rod");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A 40cm Rod of " + materialName + ".");		
		}
		if (sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (rodMaterial.getRgbAsHex() == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return rodMaterial.getRgbAsHex();

	}

	protected final int sRadiation;
	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		Utils.applyRadiationDamageToEntity(sRadiation, world, entityHolding);
	}

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Rods");

		String tempStick = unlocalName.replace("itemRod", "stick");
		String tempStickLong = unlocalName.replace("itemRod", "stickLong");
		String tempBolt = unlocalName.replace("itemRod", "bolt");
		ItemStack stackStick = UtilsItems.getItemStackOfAmountFromOreDict(tempStick, 1);
		ItemStack stackStick2 = UtilsItems.getItemStackOfAmountFromOreDict(tempStick, 2);
		ItemStack stackBolt = UtilsItems.getItemStackOfAmountFromOreDict(tempBolt, 4);
		ItemStack stackStickLong = UtilsItems.getItemStackOfAmountFromOreDict(tempStickLong, 1);
		ItemStack stackIngot = rodMaterial.getIngot(1);


		GT_Values.RA.addExtruderRecipe(
				stackIngot,
				ItemList.Shape_Extruder_Rod.get(1),
				stackStick2,
				(int) Math.max(rodMaterial.getMass() * 2L * 1, 1),
				6 * rodMaterial.vVoltageMultiplier);

		GT_Values.RA.addCutterRecipe(
				stackStick,
				stackBolt,
				null,
				(int) Math.max(rodMaterial.getMass() * 2L, 1L),
				4);	 

		if (sRadiation == 0){
			UtilsRecipe.recipeBuilder(
					stackStick, stackStick, stackStick,
					stackStick, "craftingToolWrench", stackStick,
					stackStick, stackStick, stackStick,
					UtilsItems.getItemStackOfAmountFromOreDict(unlocalName.replace("itemRod", "frameGt"), 2));
		}

		//Shaped Recipe - Bolts
		stackBolt = UtilsItems.getItemStackOfAmountFromOreDict(tempBolt, 2);
		if (null != stackBolt){
			UtilsRecipe.recipeBuilder(
					"craftingToolSaw", null, null,
					null, stackStick, null,
					null, null, null,
					stackBolt);
		}

		//Shaped Recipe - Ingot to Rod
		if (null != stackIngot){
			UtilsRecipe.recipeBuilder(
					"craftingToolFile", null, null,
					null, stackIngot, null,
					null, null, null,
					stackStick);
		}

	}

}
