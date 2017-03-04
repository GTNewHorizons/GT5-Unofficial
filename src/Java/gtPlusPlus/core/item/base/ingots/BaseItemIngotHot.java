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

	private final ItemStack outputIngot;
	private int tickCounter = 0;
	private final int tickCounterMax = 200;
	private final int mTier;

	public BaseItemIngotHot(final Material material) {
		super(material, ComponentTypes.HOTINGOT);
		this.setTextureName(CORE.MODID + ":" + "itemIngotHot");
		this.outputIngot = material.getIngot(1);
		this.mTier = material.vTier;
		this.generateRecipe();
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return ("Hot "+this.materialName+ " Ingot");
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return Utils.rgbtoHexValue(225, 225, 225);
	}

	private void generateRecipe(){
		Utils.LOG_WARNING("Adding Vacuum Freezer recipe for a Hot Ingot of "+this.materialName+".");
		GT_Values.RA.addVacuumFreezerRecipe(ItemUtils.getSimpleStack(this), this.outputIngot.copy(), 60*this.mTier);
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		if (!world.isRemote){
			if(this.tickCounter < this.tickCounterMax){
				this.tickCounter++;
			}
			else if(this.tickCounter == this.tickCounterMax){
				entityHolding.attackEntityFrom(DamageSource.onFire, 1);
				this.tickCounter = 0;
			}
		}
		super.onUpdate(iStack, world, entityHolding, p_77663_4_, p_77663_5_);
	}


}
