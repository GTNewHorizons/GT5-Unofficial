package gtPlusPlus.core.item.base.plates;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemPlateDouble extends Item{

	protected int colour;
	protected String materialName;
	protected String unlocalName;
	private int mTier;

	public BaseItemPlateDouble(String unlocalizedName, String materialName, int colour, int tier, int sRadioactivity) {
		setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setMaxStackSize(32);
		this.setTextureName(CORE.MODID + ":" + "itemPlateDouble");
		this.colour = colour;
		this.mTier = tier;
		this.materialName = materialName;
		this.sRadiation = sRadioactivity;
		GameRegistry.registerItem(this, unlocalizedName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemP", "p"), UtilsItems.getSimpleStack(this));
		addBendingRecipe();
		addCraftingRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return ("Double "+materialName+ " Plate");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A double plate of " + materialName + ".");		
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
		if (colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return colour;

	}
	
	protected final int sRadiation;
	 @Override
		public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		 Utils.applyRadiationDamageToEntity(sRadiation, world, entityHolding);
		}

	private void addBendingRecipe(){
		Utils.LOG_WARNING("Adding bender recipe for "+materialName+" Double Plates");
		String tempIngot = unlocalName.replace("itemPlateDouble", "ingot");
		String tempPlate = unlocalName.replace("itemPlateDouble", "plate");
		ItemStack inputIngot = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 2);
		ItemStack inputPlate = UtilsItems.getItemStackOfAmountFromOreDict(tempPlate, 2);
		if (null != inputIngot){
			GT_Values.RA.addBenderRecipe(inputIngot,
					UtilsItems.getSimpleStack(this),
					4*20,
					96);	
		}	
		if (null != inputPlate){
			GT_Values.RA.addBenderRecipe(inputPlate,
					UtilsItems.getSimpleStack(this),
					4*20,
					96);	
		}
	}
	
	private void addCraftingRecipe(){
		Utils.LOG_WARNING("Adding crafting recipes for "+materialName+" Double Plates");
		String tempPlate = unlocalName.replace("itemPlateDouble", "plate");
		ItemStack inputPlate = UtilsItems.getItemStackOfAmountFromOreDict(tempPlate, 1);	
		if (null != inputPlate){

			 GT_ModHandler.addCraftingRecipe(
					 GT_Utility.copyAmount(1L, new Object[]{UtilsItems.getSimpleStack(this)}),
					 gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
					 new Object[]{"I", "B", "h",
						 Character.valueOf('I'),
						 inputPlate,
						 Character.valueOf('B'),
						 inputPlate});
	        
			 GT_ModHandler.addShapelessCraftingRecipe(
					 GT_Utility.copyAmount(1L, new Object[]{UtilsItems.getSimpleStack(this)}),
					 new Object[]{gregtech.api.enums.ToolDictNames.craftingToolForgeHammer,
						 inputPlate,
						 inputPlate});	     
		}
	}

}
