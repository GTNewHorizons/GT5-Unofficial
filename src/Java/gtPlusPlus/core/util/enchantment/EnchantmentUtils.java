package gtPlusPlus.core.util.enchantment;

import gtPlusPlus.core.util.Utils;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class EnchantmentUtils {


	public static final int XP_PER_BOTTLE = 8;
	public static final int RATIO = 20;
	public static final int LIQUID_PER_XP_BOTTLE = 160;
	public static final double RATIO_MOB_ESSENCE_TO_LIQUID_XP = 13.32;

	public static int liquidToXpRatio(final int liquid) {
		return liquid / RATIO;
	}

	public static int xpToLiquidRatio(final int xp) {
		return xp * RATIO;
	}

	public static FluidStack getEssenceFromLiquidXp(final int xpAmount){
		if (xpAmount <= 0){
			return null;
		}
		return getMobEssence((int) (xpAmount*RATIO_MOB_ESSENCE_TO_LIQUID_XP));
	}

	public static FluidStack getLiquidXpFromEssence(final int essenceAmount){
		if (essenceAmount <= 0){
			return null;
		}
		return getLiquidXP((int) (essenceAmount/RATIO_MOB_ESSENCE_TO_LIQUID_XP));
	}

	public static int getLiquidForLevel(final int level) {
		final int xp = getExperienceForLevel(level);
		return xpToLiquidRatio(xp);
	}

	public static int getLevelForLiquid(final int liquid) {
		final int xp = liquidToXpRatio(liquid);
		return getLevelForExperience(xp);
	}

	public static int getExperienceForLevel(final int level) {
		if (level == 0) {
			return 0;
		}
		if ((level > 0) && (level < 16)) {
			return level * 17;
		}
		if ((level > 15) && (level < 31)) {
			return (int) (((1.5 * Math.pow(level, 2.0)) - (29.5 * level)) + 360.0);
		}
		return (int) (((3.5 * Math.pow(level, 2.0)) - (151.5 * level)) + 2220.0);
	}

	public static int getXpToNextLevel(final int level) {
		final int levelXP = getLevelForExperience(level);
		final int nextXP = getExperienceForLevel(level + 1);
		return nextXP - levelXP;
	}

	public static int getLevelForExperience(final int experience) {
		int i;
		for (i = 0; getExperienceForLevel(i) <= experience; ++i) {
		}
		return i - 1;
	}







	//Xp Fluids
	public static FluidStack getMobEssence(final int amount){
		Utils.LOG_WARNING("Trying to get a fluid stack of Mob Essence.");
		try {
			return FluidRegistry.getFluidStack("mobessence", amount).copy();
		}
		catch (final Throwable e){
			return null;
		}

	}

	public static FluidStack getLiquidXP(final int amount){
		Utils.LOG_WARNING("Trying to get a fluid stack of Liquid XP.");
		try {
			return FluidRegistry.getFluidStack("xpjuice", amount).copy();
		}
		catch (final Throwable e){
			return null;
		}

	}

}
