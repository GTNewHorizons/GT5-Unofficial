package gtPlusPlus.core.item.base.rods;

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

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemRodLong extends Item{

	final Material rodLongMaterial;
	final String materialName;
	final String unlocalName;

	public BaseItemRodLong(Material material, int sRadioactivity) {
		this.rodLongMaterial = material;
		this.unlocalName = "itemRodLong"+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(material.getUnlocalizedName());
		this.setTextureName(CORE.MODID + ":" + "itemRodLong");
		this.setMaxStackSize(64);
		this.sRadiation = sRadioactivity;
		GameRegistry.registerItem(this, unlocalName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemRod", "stick"), UtilsItems.getSimpleStack(this));
		addExtruderRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return ("Long "+materialName+ " Rod");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A 80cm Rod of " + materialName + ".");		
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
		if (rodLongMaterial.getRgbAsHex() == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return rodLongMaterial.getRgbAsHex();

	}

	protected final int sRadiation;
	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		Utils.applyRadiationDamageToEntity(sRadiation, world, entityHolding);
	}

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for Long "+materialName+" Rods");

		String tempStick = unlocalName.replace("itemRodLong", "stick");
		String tempStickLong = unlocalName.replace("itemRodLong", "stickLong");
		ItemStack stackStick = UtilsItems.getItemStackOfAmountFromOreDict(tempStick, 1);
		ItemStack stackLong = UtilsItems.getItemStackOfAmountFromOreDict(tempStickLong, 1);

		UtilsRecipe.addShapedGregtechRecipe(
				stackStick, "craftingToolHardHammer", stackStick,
				null, null, null,
				null, null, null,
				stackLong);

		ItemStack temp = stackStick;
		temp.stackSize = 2;

		GT_Values.RA.addForgeHammerRecipe(
				temp,
				stackLong,
				(int) Math.max(rodLongMaterial.getMass(), 1L),
				16);

		GT_Values.RA.addCutterRecipe(
				stackLong,
				temp,
				null,
				(int) Math.max(rodLongMaterial.getMass(), 1L),
				4);

		//Shaped Recipe - Long Rod to two smalls
		if (null != stackLong){
			stackStick.stackSize = 2;
			UtilsRecipe.recipeBuilder(
					"craftingToolSaw", null, null,
					stackLong, null, null,
					null, null, null,
					stackStick);
		}
	}

}
