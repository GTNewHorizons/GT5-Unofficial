package miscutil.core.item.base.rods;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_OreDictUnificator;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.recipe.UtilsRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemRod extends Item{

	protected int colour;
	protected String materialName;
	protected String unlocalName;
	private int mTier;

	public BaseItemRod(String unlocalizedName, String materialName, int colour, int tier) {
		setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setTextureName(CORE.MODID + ":" + "itemRod");
		this.setMaxStackSize(64);
		this.colour = colour;
		this.mTier = tier;
		this.materialName = materialName;
		GameRegistry.registerItem(this, unlocalizedName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemR", "r"), UtilsItems.getSimpleStack(this));
		addExtruderRecipe();
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
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return Utils.generateSingularRandomHexValue();
		}
		return colour;

	}
	
	private void addExtruderRecipe(){
		Utils.LOG_INFO("Adding recipe for "+materialName+" Rods");
		String tempIngot = unlocalName.replace("itemRod", "ingot");
		ItemStack tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		if (null != tempOutputStack){
			GT_Values.RA.addExtruderRecipe(tempOutputStack,
					ItemList.Shape_Extruder_Rod.get(1),
					UtilsItems.getSimpleStack(this, 2),
					12*mTier*20, 24*mTier);	
		}	
		ItemStack rods = UtilsItems.getSimpleStack(this, 1);
		UtilsRecipe.addShapedGregtechRecipe(
				rods, rods, rods,
				rods, "craftingToolWrench", rods,
				rods, rods, rods,
				UtilsItems.getItemStackOfAmountFromOreDict(unlocalName.replace("itemRod", "frameGt"), 2));
	}

}
