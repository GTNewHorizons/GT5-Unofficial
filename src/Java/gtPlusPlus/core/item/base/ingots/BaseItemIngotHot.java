package gtPlusPlus.core.item.base.ingots;

import java.util.List;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BaseItemIngotHot extends BaseItemIngot {

	private final ItemStack	outputIngot;
	private int				tickCounter		= 0;
	private final int		tickCounterMax	= 200;
	private final int		mTier;

	public BaseItemIngotHot(final String unlocalizedName, final String materialName, final ItemStack coldIngot,
			final int tier) {
		super(unlocalizedName, materialName, Utils.rgbtoHexValue(225, 225, 225), 0);
		this.setTextureName(CORE.MODID + ":" + "itemIngotHot");
		this.outputIngot = coldIngot;
		this.mTier = tier;
		this.generateRecipe();
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.materialName != null && this.materialName != "" && !this.materialName.equals("")) {
			list.add(EnumChatFormatting.GRAY + "A " + EnumChatFormatting.RED + "burning hot" + EnumChatFormatting.GRAY
					+ " ingot of " + this.materialName + ".");
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	private void generateRecipe() {
		Utils.LOG_WARNING("Adding Vacuum Freezer recipe for a Hot Ingot of " + this.materialName + ".");
		GT_Values.RA.addVacuumFreezerRecipe(ItemUtils.getSimpleStack(this), this.outputIngot.copy(), 60 * this.mTier);

	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return Utils.rgbtoHexValue(225, 225, 225);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		return "Hot " + this.materialName + " Ingot";
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
			final boolean p_77663_5_) {
		if (!world.isRemote) {
			if (this.tickCounter < this.tickCounterMax) {
				this.tickCounter++;
			}
			else if (this.tickCounter == this.tickCounterMax) {
				entityHolding.attackEntityFrom(DamageSource.onFire, 1);
				this.tickCounter = 0;
			}
			super.onUpdate(iStack, world, entityHolding, p_77663_4_, p_77663_5_);
		}
	}

}
