package gtPlusPlus.core.item.base.ingots;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BaseItemIngotHot extends BaseItemIngot{

	private ItemStack outputIngot;
	private int tickCounter = 0;
	private int tickCounterMax = 200;
	private int mTier;

	public BaseItemIngotHot(String unlocalizedName, String materialName, ItemStack coldIngot, int tier) {
		super(unlocalizedName, materialName, Utils.rgbtoHexValue(225, 225, 225), 0);
		this.setTextureName(CORE.MODID + ":" + "itemIngotHot");
		this.outputIngot = coldIngot;
		this.mTier = tier;
		generateRecipe();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return ("Hot "+materialName+ " Ingot");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A "+EnumChatFormatting.RED+"burning hot"+EnumChatFormatting.GRAY+" ingot of " + materialName + ".");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		return Utils.rgbtoHexValue(225, 225, 225);
	}

	private void generateRecipe(){
		Utils.LOG_WARNING("Adding Vacuum Freezer recipe for a Hot Ingot of "+materialName+".");
		GT_Values.RA.addVacuumFreezerRecipe(ItemUtils.getSimpleStack(this), outputIngot.copy(), 60*mTier);

		
	}	

	@Override
	public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		if (!world.isRemote){
			if(tickCounter < tickCounterMax){
				tickCounter++;
			}	
			else if(tickCounter == tickCounterMax){
				entityHolding.attackEntityFrom(DamageSource.onFire, 1);
				tickCounter = 0;
			}
			super.onUpdate(iStack, world, entityHolding, p_77663_4_, p_77663_5_);
		}
	}


}
