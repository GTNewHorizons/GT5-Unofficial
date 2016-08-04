package miscutil.core.item.base.rotors;

import gregtech.api.util.GT_OreDictUnificator;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.math.MathUtils;
import miscutil.core.util.recipe.UtilsRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemRotor extends Item{

	protected int colour;
	protected String materialName;
	protected String unlocalName;

	public BaseItemRotor(String unlocalizedName, String materialName, int colour) {
		setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemRotor");
		this.colour = colour;
		this.materialName = materialName;
		GameRegistry.registerItem(this, unlocalizedName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemR", "r"), UtilsItems.getSimpleStack(this));
		generateRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Rotor");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A spindley Rotor made out of " + materialName + ". ");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return colour;

	}
	
	public static boolean getValidItemStack(ItemStack validStack){
		if (validStack != null){
			return true;
		}
		return false;
	}
	
	public void generateRecipe(){
		
		Utils.LOG_INFO("Adding recipe for "+materialName+" Rotors");
		String tempIngot = unlocalName.replace("itemRotor", "plate");
		ItemStack tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		Utils.LOG_WARNING("Found for recipe:"+tempIngot+ "isValidStack()="+getValidItemStack(tempOutputStack));
		String screw = unlocalName.replace("itemRotor", "screw");
		ItemStack screwStack = UtilsItems.getItemStackOfAmountFromOreDict(screw, 1);
		Utils.LOG_WARNING("Found for recipe:"+screw+ "isValidStack()="+getValidItemStack(screwStack));	
		String ring = unlocalName.replace("itemRotor", "ring");
		ItemStack ringStack = UtilsItems.getItemStackOfAmountFromOreDict(ring, 1);
		Utils.LOG_WARNING("Found for recipe:"+ring+ "isValidStack()="+getValidItemStack(ringStack));	
		
		UtilsRecipe.addShapedGregtechRecipe(
				tempOutputStack, "craftingToolHardHammer", tempOutputStack,
				screwStack, ringStack, "craftingToolFile",
				tempOutputStack, "craftingToolScrewdriver", tempOutputStack,
				UtilsItems.getSimpleStack(this));
	}

}
