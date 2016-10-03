package gtPlusPlus.core.item.base.gears;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemGear extends Item{

	final Material gearMaterial;
	final String materialName;
	final String unlocalName;

	public BaseItemGear(Material material) {
		this.gearMaterial = material;
		this.unlocalName = "itemGear"+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemGear");
		GameRegistry.registerItem(this, unlocalName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemG", "g"), UtilsItems.getSimpleStack(this));
		addExtruderRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (gearMaterial.getLocalizedName()+ " Gear");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A large Gear, constructed from " + materialName + ".");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (gearMaterial.getRgbAsHex() == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return gearMaterial.getRgbAsHex();

	}

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Gears");
		ItemStack tempOutputStack = gearMaterial.getIngot(8);
		if (null != tempOutputStack){
			
			GT_Values.RA.addExtruderRecipe(
					tempOutputStack,
					ItemList.Shape_Extruder_Gear.get(1),
					UtilsItems.getSimpleStack(this),
					(int) Math.max(gearMaterial.getMass() * 5L, 1),
					8 * gearMaterial.vVoltageMultiplier);
		}				
	}

}
