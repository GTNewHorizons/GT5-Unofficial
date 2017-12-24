package gtPlusPlus.core.util.nbt;

import baubles.api.BaubleType;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.array.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModularArmourUtils {

	public static ItemStack addComponent(ItemStack tArmour, ItemStack[] tComponents) {
		if (tArmour != null) {
			ItemStack rArmour = NBTUtils.writeItemsToGtCraftingComponents(tArmour, tComponents, true);
			if (rArmour != null) {

			}
		}
		return null;
	}

	public static enum Modifiers {
		BOOST_HP("skill.hpboost"), BOOST_DEF("skill.defenceboost"), BOOST_SPEED("skill.speedboost"), BOOST_MINING(
				"skill.miningboost"), BOOST_DAMAGE("skill.damageboost"), BOOST_HOLY("skill.holyboost");
		private String MODIFIER_NAME;

		private Modifiers(final String mModifier) {
			this.MODIFIER_NAME = mModifier;
		}

		public String getModifier() {
			return this.MODIFIER_NAME;
		}

		public boolean isValidLevel(int i) {
			if (i >= 0 && i <= 100) {
				return true;
			}
			return false;
		}
	}

	public static enum BT {
		TYPE_AMULET(BaubleType.AMULET, 0), TYPE_RING(BaubleType.RING, 1), TYPE_BELT(BaubleType.BELT, 2);
		private final BaubleType mType;
		private final int mID;
		private final String mBaubleType;

		private BT(final BaubleType tType, int tID) {
			this.mType = tType;
			this.mID = tID;
			this.mBaubleType = tType.name().toLowerCase();
		}

		public BaubleType getType() {
			return this.mType;
		}

		public BT getThis() {
			return this;
		}

		public BaubleType getBaubleByID(int tID) {
			if (tID == 0) {
				return BaubleType.AMULET;
			} else if (tID == 1) {
				return BaubleType.RING;
			} else if (tID == 2) {
				return BaubleType.BELT;
			} else {
				return BaubleType.RING;
			}
		}

		public int getID() {
			return this.mID;
		}

		public String getTypeAsString() {
			return this.mBaubleType;
		}
	}

	public static void setModifierLevel(ItemStack aStack, Pair<Modifiers, Integer> mPair) {
		setModifierLevel(aStack, mPair.getKey(), mPair.getValue().intValue());
	}

	public static void setModifierLevel(ItemStack aStack, Modifiers aMod, Integer aInt) {
		setModifierLevel(aStack, aMod, aInt.intValue());
	}

	public static void setModifierLevel(ItemStack aStack, Modifiers aMod, int aInt) {

		int mCurrentLevel = getModifierLevel(aStack, aMod);
		int mNewTotalLevel = mCurrentLevel + aInt;

		NBTTagCompound tNBT = NBTUtils.getNBT(aStack);
		if (aMod.isValidLevel(mNewTotalLevel)) {
			tNBT.setInteger(aMod.getModifier(), mNewTotalLevel);
			GT_Utility.ItemNBT.setNBT(aStack, tNBT);
		} else {
			if (getModifierLevel(aStack, aMod) > 100) {
				setModifierLevel(aStack, aMod, 100);
			}
		}
	}

	public static int getModifierLevel(ItemStack aStack, Pair<Modifiers, Integer> newPair) {
		return getModifierLevel(aStack, newPair.getKey());
	}

	public static int getModifierLevel(ItemStack aStack, Modifiers aMod) {
		NBTTagCompound tNBT = NBTUtils.getNBT(aStack);
		return tNBT.getInteger(aMod.getModifier());
	}

	public static void setBaubleType(ItemStack aStack, BT aMod) {
		Logger.INFO("Changing bauble type.");
		NBTTagCompound tNBT = NBTUtils.getNBT(aStack);
		if (aMod != null) {
			tNBT.setInteger("mBaubleType", aMod.getID());
			GT_Utility.ItemNBT.setNBT(aStack, tNBT);
		}
	}

	public static int getBaubleTypeID(ItemStack aStack) {
		NBTTagCompound tNBT = NBTUtils.getNBT(aStack);
		return tNBT.getInteger("mBaubleType");
	}

	public static BaubleType getBaubleType(ItemStack aStack) {
		NBTTagCompound tNBT = NBTUtils.getNBT(aStack);
		return getBaubleByID(tNBT.getInteger("mBaubleType"));
	}

	public static BaubleType getBaubleByID(int tID) {
		if (tID == 0) {
			return BaubleType.AMULET;
		} else if (tID == 1) {
			return BaubleType.RING;
		} else if (tID == 2) {
			return BaubleType.BELT;
		} else {
			return BaubleType.RING;
		}
	}

	public static ItemStack setDefaultStats(ItemStack aStack) {
		ItemStack tempStack = aStack;
		setModifierLevel(tempStack, Modifiers.BOOST_DAMAGE, 0);
		setModifierLevel(tempStack, Modifiers.BOOST_DEF, 0);
		setModifierLevel(tempStack, Modifiers.BOOST_HOLY, 0);
		setModifierLevel(tempStack, Modifiers.BOOST_HP, 0);
		setModifierLevel(tempStack, Modifiers.BOOST_MINING, 0);
		setModifierLevel(tempStack, Modifiers.BOOST_SPEED, 0);
		return tempStack;
	}

}
