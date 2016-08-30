package miscutil.core.item.base.rods;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_OreDictUnificator;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.math.MathUtils;
import miscutil.core.util.recipe.UtilsRecipe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemRodLong extends Item{

	protected int colour;
	protected String materialName;
	protected String unlocalName;
	private int mTier;

	public BaseItemRodLong(String unlocalizedName, String materialName, int colour, int tier, int sRadioactivity) {
		setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setTextureName(CORE.MODID + ":" + "itemRodLong");
		this.setMaxStackSize(64);
		this.colour = colour;
		this.mTier = tier;
		this.materialName = materialName;
		this.sRadiation = sRadioactivity;
		GameRegistry.registerItem(this, unlocalizedName);
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

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for Long "+materialName+" Rods");
		String tempIngot = unlocalName.replace("itemRodLong", "stick");
		ItemStack tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 2);
		if (null != tempOutputStack){
			GT_Values.RA.addForgeHammerRecipe(tempOutputStack,
					UtilsItems.getSimpleStack(this, 1),
					12*mTier*20, 24*mTier);	
		}	
		ItemStack rods = UtilsItems.getSimpleStack(this, 1);
		ItemStack tempOutputStack2 = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		UtilsRecipe.addShapedGregtechRecipe(
				tempOutputStack2, "craftingToolHardHammer", tempOutputStack2,
				null, null, null,
				null, null, null,
				rods);
	}

}
