package miscutil.core.item.base.screws;

import gregtech.api.enums.GT_Values;
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

public class BaseItemScrew extends Item{

	protected int colour;
	protected String materialName;
	protected String unlocalName;
	private int mTier;

	public BaseItemScrew(String unlocalizedName, String materialName, int colour, int tier) {
		setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemScrew");
		this.setMaxStackSize(64);
		this.colour = colour;
		this.mTier = tier;
		this.materialName = materialName;
		GameRegistry.registerItem(this, unlocalizedName);
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
		if (colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return colour;

	}

	private void addLatheRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Screws");
		ItemStack boltStack = UtilsItems.getItemStackOfAmountFromOreDict(unlocalName.replace("itemScrew", "bolt"), 1);
		if (null != boltStack){
			GT_Values.RA.addLatheRecipe(boltStack,
					UtilsItems.getSimpleStack(this), null,
					60*mTier, 16*mTier);	
			UtilsRecipe.addShapedGregtechRecipe(
					"craftingToolFile", boltStack, null,
					boltStack, null, null,
					null, null, null,
					UtilsItems.getSimpleStack(this));
		}				
	}

}
