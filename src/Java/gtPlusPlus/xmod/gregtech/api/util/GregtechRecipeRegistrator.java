package gtPlusPlus.xmod.gregtech.api.util;

import java.util.*;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.interfaces.internal.IThaumcraftCompat;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.objects.GregtechItemData;
import gtPlusPlus.xmod.gregtech.api.objects.GregtechMaterialStack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Class for Automatic Recipe registering.
 */
public class GregtechRecipeRegistrator {
	/**
	 * List of GT_Materials, which are used in the Creation of Sticks. All Rod
	 * GT_Materials are automatically added to this List.
	 */
	public static final List<GT_Materials>	sRodMaterialList	= new ArrayList<GT_Materials>();
	private static final ItemStack			sMt1				= new ItemStack(Blocks.dirt, 1, 0),
			sMt2 = new ItemStack(Blocks.dirt, 1, 0);
	private static final String				s_H					= "h", s_F = "f", s_I = "I", s_P = "P", s_R = "R";
	private static final ItemStack[][]		sShapes1			= new ItemStack[][] {
			{
					GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					null, GregtechRecipeRegistrator.sMt1, null
			}, {
					GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1
			}, {
					null, GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					null, GregtechRecipeRegistrator.sMt1
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1, null, null, null
			}, {
					GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1
			}, {
					null, null, null, GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1
			}, {
					null, GregtechRecipeRegistrator.sMt1, null, null, GregtechRecipeRegistrator.sMt1, null, null,
					GregtechRecipeRegistrator.sMt2, null
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					null, GregtechRecipeRegistrator.sMt2, null, null, GregtechRecipeRegistrator.sMt2, null
			}, {
					null, GregtechRecipeRegistrator.sMt1, null, null, GregtechRecipeRegistrator.sMt2, null, null,
					GregtechRecipeRegistrator.sMt2, null
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, null,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt2, null, null,
					GregtechRecipeRegistrator.sMt2, null
			}, {
					null, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, null,
					GregtechRecipeRegistrator.sMt2, GregtechRecipeRegistrator.sMt1, null,
					GregtechRecipeRegistrator.sMt2, null
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, null, null,
					GregtechRecipeRegistrator.sMt2, null, null, GregtechRecipeRegistrator.sMt2, null
			}, {
					null, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, null,
					GregtechRecipeRegistrator.sMt2, null, null, GregtechRecipeRegistrator.sMt2, null
			}, {
					null, GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1, null, null, null,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt2
			}, {
					null, GregtechRecipeRegistrator.sMt1, null, null, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt2, GregtechRecipeRegistrator.sMt1, null
			}, {
					null, GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1, null,
					GregtechRecipeRegistrator.sMt1, null, null, GregtechRecipeRegistrator.sMt2
			}, {
					null, GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1, null,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt2, null, null
			}, {
					null, GregtechRecipeRegistrator.sMt2, null, null, GregtechRecipeRegistrator.sMt1, null, null,
					GregtechRecipeRegistrator.sMt1, null
			}, {
					null, GregtechRecipeRegistrator.sMt2, null, null, GregtechRecipeRegistrator.sMt2, null,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1
			}, {
					null, GregtechRecipeRegistrator.sMt2, null, null, GregtechRecipeRegistrator.sMt2, null, null,
					GregtechRecipeRegistrator.sMt1, null
			}, {
					null, GregtechRecipeRegistrator.sMt2, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt2, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, null
			}, {
					null, GregtechRecipeRegistrator.sMt2, null, null, GregtechRecipeRegistrator.sMt2,
					GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1
			}, {
					null, GregtechRecipeRegistrator.sMt2, null, null, GregtechRecipeRegistrator.sMt2, null,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, null
			}, {
					GregtechRecipeRegistrator.sMt1, null, null, null, GregtechRecipeRegistrator.sMt2, null, null, null,
					GregtechRecipeRegistrator.sMt2
			}, {
					null, null, GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt2, null,
					GregtechRecipeRegistrator.sMt2, null, null
			}, {
					GregtechRecipeRegistrator.sMt1, null, null, null, GregtechRecipeRegistrator.sMt2, null, null, null,
					null
			}, {
					null, null, GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt2, null, null, null,
					null
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt2, null, null, null, null, null, null,
					null
			}, {
					GregtechRecipeRegistrator.sMt2, GregtechRecipeRegistrator.sMt1, null, null, null, null, null, null,
					null
			}, {
					GregtechRecipeRegistrator.sMt1, null, null, GregtechRecipeRegistrator.sMt2, null, null, null, null,
					null
			}, {
					GregtechRecipeRegistrator.sMt2, null, null, GregtechRecipeRegistrator.sMt1, null, null, null, null,
					null
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					null, GregtechRecipeRegistrator.sMt2, null
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, null,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt2,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, null
			}, {
					null, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt2, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					null, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1
			}, {
					null, GregtechRecipeRegistrator.sMt2, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt2, GregtechRecipeRegistrator.sMt1,
					null, GregtechRecipeRegistrator.sMt2, null
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, null,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt2, GregtechRecipeRegistrator.sMt2,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, null
			}, {
					null, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt2, GregtechRecipeRegistrator.sMt2, GregtechRecipeRegistrator.sMt1,
					null, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1
			}, {
					null, GregtechRecipeRegistrator.sMt2, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt2, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1
			}, {
					GregtechRecipeRegistrator.sMt1, null, null, null, GregtechRecipeRegistrator.sMt1, null, null, null,
					null
			}, {
					null, GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt1, null, null, null, null,
					null
			}, {
					GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1, null,
					GregtechRecipeRegistrator.sMt2, null, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt2, null, null
			}, {
					null, GregtechRecipeRegistrator.sMt1, GregtechRecipeRegistrator.sMt1,
					GregtechRecipeRegistrator.sMt1, null, GregtechRecipeRegistrator.sMt2, null, null,
					GregtechRecipeRegistrator.sMt2
			}
	};
	private static final String[][]			sShapesA			= new String[][] {
			null, null, null, {
					"Helmet",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P
			}, {
					"ChestPlate",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_P
			}, {
					"Pants",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_P + " " + GregtechRecipeRegistrator.s_P
			}, {
					"Boots", GregtechRecipeRegistrator.s_P + " " + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P
			}, {
					"Sword", " " + GregtechRecipeRegistrator.s_P + " ",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H,
					" " + GregtechRecipeRegistrator.s_R + " "
			}, {
					"Pickaxe",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_I,
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + GregtechRecipeRegistrator.s_H,
					" " + GregtechRecipeRegistrator.s_R + " "
			}, {
					"Shovel",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H,
					" " + GregtechRecipeRegistrator.s_R + " ", " " + GregtechRecipeRegistrator.s_R + " "
			}, {
					"Axe",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_H,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + " "
			}, {
					"Axe",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_H,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + " "
			}, {
					"Hoe",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_H,
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + " ",
					" " + GregtechRecipeRegistrator.s_R + " "
			}, {
					"Hoe",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_H,
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + " ",
					" " + GregtechRecipeRegistrator.s_R + " "
			}, {
					"Sickle", " " + GregtechRecipeRegistrator.s_P + " ",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_F + " ",
					GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R
			}, {
					"Sickle", " " + GregtechRecipeRegistrator.s_P + " ",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_F + " ",
					GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R
			}, {
					"Sickle", " " + GregtechRecipeRegistrator.s_P + " ",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_F + " ",
					GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R
			}, {
					"Sickle", " " + GregtechRecipeRegistrator.s_P + " ",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_F + " ",
					GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R
			}, {
					"Sword", " " + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H,
					" " + GregtechRecipeRegistrator.s_P + " "
			}, {
					"Pickaxe", " " + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + GregtechRecipeRegistrator.s_H,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_I
			}, {
					"Shovel", " " + GregtechRecipeRegistrator.s_R + " ", " " + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H
			}, {
					"Axe", GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_H
			}, {
					"Axe", GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_H
			}, {
					"Hoe", " " + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_H
			}, {
					"Hoe", " " + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + " ",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_H
			}, {
					"Spear", GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H + " ",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + " ",
					" " + " " + GregtechRecipeRegistrator.s_R
			}, {
					"Spear", GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H + " ",
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + " ",
					" " + " " + GregtechRecipeRegistrator.s_R
			}, {
					"Knive", GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_R + GregtechRecipeRegistrator.s_F
			}, {
					"Knive", GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_H,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R
			}, {
					"Knive", GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_H,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R
			}, {
					"Knive", GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_F,
					GregtechRecipeRegistrator.s_R + GregtechRecipeRegistrator.s_H
			}, {
					"Knive", GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_F,
					GregtechRecipeRegistrator.s_R + GregtechRecipeRegistrator.s_H
			}, null, null, null, null, {
					"WarAxe",
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_R + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R + GregtechRecipeRegistrator.s_H
			}, null, null, null, {
					"Shears", GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_F
			}, {
					"Shears", GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_F
			}, {
					"Scythe",
					GregtechRecipeRegistrator.s_I + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_H,
					GregtechRecipeRegistrator.s_R + GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_P,
					GregtechRecipeRegistrator.s_R + " " + " "
			}, {
					"Scythe",
					GregtechRecipeRegistrator.s_H + GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_I,
					GregtechRecipeRegistrator.s_P + GregtechRecipeRegistrator.s_F + GregtechRecipeRegistrator.s_R,
					" " + " " + GregtechRecipeRegistrator.s_R
			}
	};
	public static volatile int				VERSION				= 508;

	public static void registerMaterialRecycling(final ItemStack aStack, final GregtechItemData aData) {
		if (GT_Utility.isStackInvalid(aStack) || GT_Utility.areStacksEqual(new ItemStack(Items.blaze_rod), aStack)
				|| aData == null || !aData.hasValidMaterialData() || aData.mMaterial.mAmount <= 0
				|| GT_Utility.getFluidForFilledItem(aStack, false) != null) {
			return;
		}
		GregtechRecipeRegistrator.registerReverseMacerating(GT_Utility.copyAmount(1, aStack), aData,
				aData.mPrefix == null);
		GregtechRecipeRegistrator.registerReverseSmelting(GT_Utility.copyAmount(1, aStack), aData.mMaterial.mMaterial,
				aData.mMaterial.mAmount, true);
		GregtechRecipeRegistrator.registerReverseFluidSmelting(GT_Utility.copyAmount(1, aStack),
				aData.mMaterial.mMaterial, aData.mMaterial.mAmount, aData.getByProduct(0));
		GregtechRecipeRegistrator.registerReverseArcSmelting(GT_Utility.copyAmount(1, aStack), aData);
	}

	public static void registerMaterialRecycling(final ItemStack aStack, final GT_Materials aMaterial,
			final long aMaterialAmount, GregtechMaterialStack aByproduct) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return;
		}
		if (aByproduct != null) {
			aByproduct = aByproduct.clone();
			aByproduct.mAmount /= aStack.stackSize;
		}
		GregtechOreDictUnificator.addItemData(GT_Utility.copyAmount(1, aStack),
				new GregtechItemData(aMaterial, aMaterialAmount / aStack.stackSize, aByproduct));
	}

	public static void registerReverseArcSmelting(final ItemStack aStack, GregtechItemData aData) {
		if (aStack == null || aData == null) {
			return;
		}
		aData = new GregtechItemData(aData);

		if (!aData.hasValidMaterialData()) {
			return;
		}

		for (final GregtechMaterialStack tMaterial : aData.getAllGT_MaterialStacks()) {
			if (tMaterial.mMaterial.contains(SubTag.UNBURNABLE)) {
				tMaterial.mMaterial = tMaterial.mMaterial.mSmeltInto.mArcSmeltInto;
				continue;
			}
			if (tMaterial.mMaterial.contains(SubTag.EXPLOSIVE)) {
				tMaterial.mAmount /= 4;
				continue;
			}
			if (tMaterial.mMaterial.contains(SubTag.FLAMMABLE)) {
				tMaterial.mAmount /= 2;
				continue;
			}
			if (tMaterial.mMaterial.contains(SubTag.NO_SMELTING)) {
				tMaterial.mAmount = 0;
				continue;
			}
			if (tMaterial.mMaterial.contains(SubTag.METAL)) {
				tMaterial.mMaterial = tMaterial.mMaterial.mSmeltInto.mArcSmeltInto;
				continue;
			}
			tMaterial.mAmount = 0;
		}

		aData = new GregtechItemData(aData);

		if (aData.mByProducts.length > 3) {
			for (final GregtechMaterialStack tMaterial : aData.getAllGT_MaterialStacks()) {
				aData = new GregtechItemData(aData);
			}
		}

		if (!aData.hasValidMaterialData()) {
			return;
		}

		long tAmount = 0;
		for (final GregtechMaterialStack tMaterial : aData.getAllGT_MaterialStacks()) {
			tAmount += tMaterial.mAmount * tMaterial.mMaterial.getMass();
		}

		GT_Values.RA.addArcFurnaceRecipe(aStack, new ItemStack[] {
				GregtechOreDictUnificator.getIngotOrDust(aData.mMaterial),
				GregtechOreDictUnificator.getIngotOrDust(aData.getByProduct(0)),
				GregtechOreDictUnificator.getIngotOrDust(aData.getByProduct(1)),
				GregtechOreDictUnificator.getIngotOrDust(aData.getByProduct(2))
		}, null, (int) Math.max(16, tAmount / GT_Values.M), 96);
	}

	public static void registerReverseArcSmelting(final ItemStack aStack, final GT_Materials aMaterial,
			final long aMaterialAmount, final GregtechMaterialStack aByProduct01,
			final GregtechMaterialStack aByProduct02, final GregtechMaterialStack aByProduct03) {
		GregtechRecipeRegistrator.registerReverseArcSmelting(aStack,
				new GregtechItemData(aMaterial == null ? null : new GregtechMaterialStack(aMaterial, aMaterialAmount),
						aByProduct01, aByProduct02, aByProduct03));
	}

	/**
	 * @param aStack
	 *            the stack to be recycled.
	 * @param aMaterial
	 *            the Material.
	 * @param aMaterialAmount
	 *            the amount of it in Material Units.
	 */
	public static void registerReverseFluidSmelting(final ItemStack aStack, final GT_Materials aMaterial,
			final long aMaterialAmount, final GregtechMaterialStack aByproduct) {
		if (aStack == null || aMaterial == null || aMaterial.mSmeltInto.mStandardMoltenFluid == null
				|| !aMaterial.contains(SubTag.SMELTING_TO_FLUID)
				|| GT_Values.L * aMaterialAmount / (GT_Values.M * aStack.stackSize) <= 0) {
			return;
		}
		GT_Values.RA.addFluidSmelterRecipe(GT_Utility.copyAmount(1, aStack), aByproduct == null ? null
				: aByproduct.mMaterial.contains(SubTag.NO_SMELTING) || !aByproduct.mMaterial.contains(SubTag.METAL)
						? aByproduct.mMaterial.contains(SubTag.FLAMMABLE)
								? GregtechOreDictUnificator.getDust(GT_Materials._NULL, aByproduct.mAmount / 2)
								: aByproduct.mMaterial.contains(SubTag.UNBURNABLE) ? GregtechOreDictUnificator
										.getDustOrIngot(aByproduct.mMaterial.mSmeltInto, aByproduct.mAmount) : null
						: GregtechOreDictUnificator.getIngotOrDust(aByproduct.mMaterial.mSmeltInto, aByproduct.mAmount),
				aMaterial.mSmeltInto.getMolten(GT_Values.L * aMaterialAmount / (GT_Values.M * aStack.stackSize)), 10000,
				(int) Math.max(1, 24 * aMaterialAmount / GT_Values.M),
				Math.max(8, (int) Math.sqrt(2 * aMaterial.mSmeltInto.mStandardMoltenFluid.getTemperature())));
	}

	public static void registerReverseMacerating(final ItemStack aStack, GregtechItemData aData,
			final boolean aAllowHammer) {
		if (aStack == null || aData == null) {
			return;
		}
		aData = new GregtechItemData(aData);

		if (!aData.hasValidMaterialData()) {
			return;
		}

		for (final GregtechMaterialStack tMaterial : aData.getAllGT_MaterialStacks()) {
			tMaterial.mMaterial = tMaterial.mMaterial.mMacerateInto;
		}

		aData = new GregtechItemData(aData);

		if (!aData.hasValidMaterialData()) {
			return;
		}

		long tAmount = 0;
		for (final GregtechMaterialStack tMaterial : aData.getAllGT_MaterialStacks()) {
			tAmount += tMaterial.mAmount * tMaterial.mMaterial.getMass();
		}

		GT_Values.RA.addPulveriserRecipe(aStack, new ItemStack[] {
				GregtechOreDictUnificator.getDust(aData.mMaterial),
				GregtechOreDictUnificator.getDust(aData.getByProduct(0)),
				GregtechOreDictUnificator.getDust(aData.getByProduct(1)),
				GregtechOreDictUnificator.getDust(aData.getByProduct(2))
		}, null, (int) Math.max(16, tAmount / GT_Values.M), 4);

		if (aAllowHammer) {
			for (final GregtechMaterialStack tMaterial : aData.getAllGT_MaterialStacks()) {
				if (tMaterial.mMaterial.contains(SubTag.CRYSTAL) && !tMaterial.mMaterial.contains(SubTag.METAL)) {
					if (GT_Values.RA.addForgeHammerRecipe(GT_Utility.copyAmount(1, aStack),
							GregtechOreDictUnificator.getDust(aData.mMaterial), 200, 32)) {
						break;
					}
				}
			}
		}
		final ItemStack tDust = GregtechOreDictUnificator.getDust(aData.mMaterial);
		if (tDust != null && GT_ModHandler.addPulverisationRecipe(GT_Utility.copyAmount(1, aStack), tDust,
				GregtechOreDictUnificator.getDust(aData.getByProduct(0)), 100,
				GregtechOreDictUnificator.getDust(aData.getByProduct(1)), 100, true)) {
			if (GregTech_API.sThaumcraftCompat != null) {
				GregTech_API.sThaumcraftCompat.addCrucibleRecipe(IThaumcraftCompat.ADVANCEDENTROPICPROCESSING, aStack,
						tDust, Arrays.asList(new TC_AspectStack(TC_Aspects.PERDITIO,
								Math.max(1, aData.mMaterial.mAmount * 2 / GT_Values.M))));
			}
		}
	}

	public static void registerReverseMacerating(final ItemStack aStack, final GT_Materials aMaterial,
			final long aMaterialAmount, final GregtechMaterialStack aByProduct01,
			final GregtechMaterialStack aByProduct02, final GregtechMaterialStack aByProduct03,
			final boolean aAllowHammer) {
		GregtechRecipeRegistrator.registerReverseMacerating(aStack,
				new GregtechItemData(aMaterial == null ? null : new GregtechMaterialStack(aMaterial, aMaterialAmount),
						aByProduct01, aByProduct02, aByProduct03),
				aAllowHammer);
	}

	/**
	 * @param aStack
	 *            the stack to be recycled.
	 * @param aMaterial
	 *            the Material.
	 * @param aMaterialAmount
	 *            the amount of it in Material Units.
	 * @param aAllowAlloySmelter
	 *            if it is allowed to be recycled inside the Alloy Smelter.
	 */
	public static void registerReverseSmelting(final ItemStack aStack, final GT_Materials aMaterial,
			long aMaterialAmount, final boolean aAllowAlloySmelter) {
		if (aStack == null || aMaterial == null || aMaterialAmount <= 0 || aMaterial.contains(SubTag.NO_SMELTING)
				|| aMaterialAmount > GT_Values.M && aMaterial.contains(SubTag.METAL)) {
			return;
		}
		aMaterialAmount /= aStack.stackSize;

		if (aAllowAlloySmelter) {
			CORE.GT_Recipe.addSmeltingAndAlloySmeltingRecipe(GT_Utility.copyAmount(1, aStack),
					GregtechOreDictUnificator.getIngot(aMaterial.mSmeltInto, aMaterialAmount));
		}
		else {
			GT_ModHandler.addSmeltingRecipe(GT_Utility.copyAmount(1, aStack),
					GregtechOreDictUnificator.getIngot(aMaterial.mSmeltInto, aMaterialAmount));
		}
	}

	/**
	 * You give this Function a Material and it will scan almost everything for
	 * adding recycling Recipes
	 *
	 * @param aMat
	 *            a Material, for example an Ingot or a Gem.
	 * @param aOutput
	 *            the Dust you usually get from macerating aMat
	 * @param aRecipeReplacing
	 *            allows to replace the Recipe with a Plate variant
	 */
	public static synchronized void registerUsagesForGT_Materials(ItemStack aMat, String aPlate,
			final boolean aRecipeReplacing) {
		if (aMat == null) {
			return;
		}
		aMat = GT_Utility.copy(aMat);
		ItemStack tStack;
		final GregtechItemData aItemData = GregtechOreDictUnificator.getItemData(aMat);
		if (aItemData == null || aItemData.mPrefix != GregtechOrePrefixes.ingot) {
			aPlate = null;
		}
		if (aPlate != null && GregtechOreDictUnificator.getFirstOre(aPlate, 1) == null) {
			aPlate = null;
		}

		GregtechRecipeRegistrator.sMt1.func_150996_a(aMat.getItem());
		GregtechRecipeRegistrator.sMt1.stackSize = 1;
		Items.feather.setDamage(GregtechRecipeRegistrator.sMt1, Items.feather.getDamage(aMat));

		GregtechRecipeRegistrator.sMt2.func_150996_a(new ItemStack(Blocks.dirt).getItem());
		GregtechRecipeRegistrator.sMt2.stackSize = 1;
		Items.feather.setDamage(GregtechRecipeRegistrator.sMt2, 0);

		for (final ItemStack[] tRecipe : GregtechRecipeRegistrator.sShapes1) {
			int tAmount1 = 0;
			for (final ItemStack tMat : tRecipe) {
				if (tMat == GregtechRecipeRegistrator.sMt1) {
					tAmount1++;
				}
			}
			if (aItemData != null && aItemData.hasValidPrefixMaterialData()) {
				for (final ItemStack tCrafted : GT_ModHandler.getRecipeOutputs(tRecipe)) {
					GregtechOreDictUnificator.addItemData(tCrafted, new GregtechItemData(aItemData.mMaterial.mMaterial,
							aItemData.mMaterial.mAmount * tAmount1));
				}
			}
		}

		for (final GT_Materials tMaterial : GregtechRecipeRegistrator.sRodMaterialList) {
			final ItemStack tMt2 = GregtechOreDictUnificator.get(GregtechOrePrefixes.stick, tMaterial, 1);
			if (tMt2 != null) {
				GregtechRecipeRegistrator.sMt2.func_150996_a(tMt2.getItem());
				GregtechRecipeRegistrator.sMt2.stackSize = 1;
				Items.feather.setDamage(GregtechRecipeRegistrator.sMt2, Items.feather.getDamage(tMt2));

				for (int i = 0; i < GregtechRecipeRegistrator.sShapes1.length; i++) {
					final ItemStack[] tRecipe = GregtechRecipeRegistrator.sShapes1[i];

					int tAmount1 = 0, tAmount2 = 0;
					for (final ItemStack tMat : tRecipe) {
						if (tMat == GregtechRecipeRegistrator.sMt1) {
							tAmount1++;
						}
						if (tMat == GregtechRecipeRegistrator.sMt2) {
							tAmount2++;
						}
					}
					for (final ItemStack tCrafted : GT_ModHandler.getVanillyToolRecipeOutputs(tRecipe)) {
						if (aItemData != null && aItemData.hasValidPrefixMaterialData()) {
							GregtechOreDictUnificator.addItemData(tCrafted,
									new GregtechItemData(aItemData.mMaterial.mMaterial,
											aItemData.mMaterial.mAmount * tAmount1, new GregtechMaterialStack(tMaterial,
													OrePrefixes.stick.mMaterialAmount * tAmount2)));
						}

						if (aRecipeReplacing && aPlate != null && GregtechRecipeRegistrator.sShapesA[i] != null
								&& GregtechRecipeRegistrator.sShapesA[i].length > 1) {
							assert aItemData != null;
							if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.recipereplacements,
									aItemData.mMaterial.mMaterial + "." + GregtechRecipeRegistrator.sShapesA[i][0],
									true)) {
								if (null != (tStack = GT_ModHandler.removeRecipe(tRecipe))) {
									switch (GregtechRecipeRegistrator.sShapesA[i].length) {
										case 2:
											GT_ModHandler.addCraftingRecipe(tStack, GT_ModHandler.RecipeBits.BUFFERED,
													new Object[] {
															GregtechRecipeRegistrator.sShapesA[i][1],
															GregtechRecipeRegistrator.s_P.charAt(0), aPlate,
															GregtechRecipeRegistrator.s_R.charAt(0),
															OrePrefixes.stick.get(tMaterial),
															GregtechRecipeRegistrator.s_I.charAt(0), aItemData
													});
											break;
										case 3:
											GT_ModHandler.addCraftingRecipe(tStack, GT_ModHandler.RecipeBits.BUFFERED,
													new Object[] {
															GregtechRecipeRegistrator.sShapesA[i][1],
															GregtechRecipeRegistrator.sShapesA[i][2],
															GregtechRecipeRegistrator.s_P.charAt(0), aPlate,
															GregtechRecipeRegistrator.s_R.charAt(0),
															OrePrefixes.stick.get(tMaterial),
															GregtechRecipeRegistrator.s_I.charAt(0), aItemData
													});
											break;
										default:
											GT_ModHandler.addCraftingRecipe(tStack, GT_ModHandler.RecipeBits.BUFFERED,
													new Object[] {
															GregtechRecipeRegistrator.sShapesA[i][1],
															GregtechRecipeRegistrator.sShapesA[i][2],
															GregtechRecipeRegistrator.sShapesA[i][3],
															GregtechRecipeRegistrator.s_P.charAt(0), aPlate,
															GregtechRecipeRegistrator.s_R.charAt(0),
															OrePrefixes.stick.get(tMaterial),
															GregtechRecipeRegistrator.s_I.charAt(0), aItemData
													});
											break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
