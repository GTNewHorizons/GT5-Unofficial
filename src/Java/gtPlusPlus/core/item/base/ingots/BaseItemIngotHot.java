package gtPlusPlus.core.item.base.ingots;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BaseItemIngotHot extends BaseItemIngot{

	private ItemStack outputIngot;
	private int tickCounter = 0;
	private int tickCounterMax = 200;
	private int mTier;

	public BaseItemIngotHot(Material material) {
		super(material, ComponentTypes.HOTINGOT);
		this.setTextureName(CORE.MODID + ":" + "itemIngotHot");
		this.outputIngot = material.getIngot(1);
		this.mTier = material.vTier;
		generateRecipe();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		return ("Hot "+materialName+ " Ingot");
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
		}
		super.onUpdate(iStack, world, entityHolding, p_77663_4_, p_77663_5_);
	}


}
