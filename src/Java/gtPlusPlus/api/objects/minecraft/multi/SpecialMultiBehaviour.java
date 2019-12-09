package gtPlusPlus.api.objects.minecraft.multi;

import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

/**
 * Extend this class to implement custom behaviour for multiblocks.
 * The Trigger item when in a special slot or input bus, will cause the multiblock to behave as specified.
 * Not overriding a method here will cause the default values to be used.
 * @author Alkalus
 *
 */
public abstract class SpecialMultiBehaviour {

	private final int mMaxParallelRecipes = Short.MIN_VALUE;
	private final int mEUPercent = Short.MIN_VALUE;
	private final int mSpeedBonusPercent = Short.MIN_VALUE;
	private final int mOutputChanceRoll = Short.MIN_VALUE;	
	
	public abstract ItemStack getTriggerItem();
	
	public abstract String getTriggerItemTooltip();

	public int getMaxParallelRecipes() {
		return this.mMaxParallelRecipes;
	}

	public int getEUPercent() {
		return this.mEUPercent;
	}

	public int getSpeedBonusPercent() {
		return this.mSpeedBonusPercent;
	}

	public int getOutputChanceRoll() {
		return this.mOutputChanceRoll;
	}	
	
	public final boolean isTriggerItem(ItemStack aToMatch) {
		return GT_Utility.areStacksEqual(getTriggerItem(), aToMatch, false);
	}
	
}
