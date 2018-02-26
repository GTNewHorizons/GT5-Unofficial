package gtPlusPlus.xmod.gregtech.api.util;

import static gregtech.api.enums.GT_Values.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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

/**
 * Class for Automatic Recipe registering.
 */
public class GregtechRecipeRegistrator {
	/**
	 * List of GT_Materials, which are used in the Creation of Sticks. All Rod GT_Materials are automatically added to this List.
	 */
	public static final List<GT_Materials> sRodMaterialList = new ArrayList<>();
	private static final ItemStack sMt1 = new ItemStack(Blocks.dirt, 1, 0), sMt2 = new ItemStack(Blocks.dirt, 1, 0);
	private static final String s_H = "h", s_F = "f", s_I = "I", s_P = "P", s_R = "R";
	private static final ItemStack[][]
			sShapes1 = new ItemStack[][]{
		{sMt1, null, sMt1, sMt1, sMt1, sMt1, null, sMt1, null},
		{sMt1, null, sMt1, sMt1, null, sMt1, sMt1, sMt1, sMt1},
		{null, sMt1, null, sMt1, sMt1, sMt1, sMt1, null, sMt1},
		{sMt1, sMt1, sMt1, sMt1, null, sMt1, null, null, null},
		{sMt1, null, sMt1, sMt1, sMt1, sMt1, sMt1, sMt1, sMt1},
		{sMt1, sMt1, sMt1, sMt1, null, sMt1, sMt1, null, sMt1},
		{null, null, null, sMt1, null, sMt1, sMt1, null, sMt1},
		{null, sMt1, null, null, sMt1, null, null, sMt2, null},
		{sMt1, sMt1, sMt1, null, sMt2, null, null, sMt2, null},
		{null, sMt1, null, null, sMt2, null, null, sMt2, null},
		{sMt1, sMt1, null, sMt1, sMt2, null, null, sMt2, null},
		{null, sMt1, sMt1, null, sMt2, sMt1, null, sMt2, null},
		{sMt1, sMt1, null, null, sMt2, null, null, sMt2, null},
		{null, sMt1, sMt1, null, sMt2, null, null, sMt2, null},
		{null, sMt1, null, sMt1, null, null, null, sMt1, sMt2},
		{null, sMt1, null, null, null, sMt1, sMt2, sMt1, null},
		{null, sMt1, null, sMt1, null, sMt1, null, null, sMt2},
		{null, sMt1, null, sMt1, null, sMt1, sMt2, null, null},
		{null, sMt2, null, null, sMt1, null, null, sMt1, null},
		{null, sMt2, null, null, sMt2, null, sMt1, sMt1, sMt1},
		{null, sMt2, null, null, sMt2, null, null, sMt1, null},
		{null, sMt2, null, sMt1, sMt2, null, sMt1, sMt1, null},
		{null, sMt2, null, null, sMt2, sMt1, null, sMt1, sMt1},
		{null, sMt2, null, null, sMt2, null, sMt1, sMt1, null},
		{sMt1, null, null, null, sMt2, null, null, null, sMt2},
		{null, null, sMt1, null, sMt2, null, sMt2, null, null},
		{sMt1, null, null, null, sMt2, null, null, null, null},
		{null, null, sMt1, null, sMt2, null, null, null, null},
		{sMt1, sMt2, null, null, null, null, null, null, null},
		{sMt2, sMt1, null, null, null, null, null, null, null},
		{sMt1, null, null, sMt2, null, null, null, null, null},
		{sMt2, null, null, sMt1, null, null, null, null, null},
		{sMt1, sMt1, sMt1, sMt1, sMt1, sMt1, null, sMt2, null},
		{sMt1, sMt1, null, sMt1, sMt1, sMt2, sMt1, sMt1, null},
		{null, sMt1, sMt1, sMt2, sMt1, sMt1, null, sMt1, sMt1},
		{null, sMt2, null, sMt1, sMt1, sMt1, sMt1, sMt1, sMt1},
		{sMt1, sMt1, sMt1, sMt1, sMt2, sMt1, null, sMt2, null},
		{sMt1, sMt1, null, sMt1, sMt2, sMt2, sMt1, sMt1, null},
		{null, sMt1, sMt1, sMt2, sMt2, sMt1, null, sMt1, sMt1},
		{null, sMt2, null, sMt1, sMt2, sMt1, sMt1, sMt1, sMt1},
		{sMt1, null, null, null, sMt1, null, null, null, null},
		{null, sMt1, null, sMt1, null, null, null, null, null},
		{sMt1, sMt1, null, sMt2, null, sMt1, sMt2, null, null},
		{null, sMt1, sMt1, sMt1, null, sMt2, null, null, sMt2}
	};
	private static final String[][] sShapesA = new String[][]{
		null,
		null,
		null,
		{"Helmet", s_P + s_P + s_P, s_P + s_H + s_P},
		{"ChestPlate", s_P + s_H + s_P, s_P + s_P + s_P, s_P + s_P + s_P},
		{"Pants", s_P + s_P + s_P, s_P + s_H + s_P, s_P + " " + s_P},
		{"Boots", s_P + " " + s_P, s_P + s_H + s_P},
		{"Sword", " " + s_P + " ", s_F + s_P + s_H, " " + s_R + " "},
		{"Pickaxe", s_P + s_I + s_I, s_F + s_R + s_H, " " + s_R + " "},
		{"Shovel", s_F + s_P + s_H, " " + s_R + " ", " " + s_R + " "},
		{"Axe", s_P + s_I + s_H, s_P + s_R + " ", s_F + s_R + " "},
		{"Axe", s_P + s_I + s_H, s_P + s_R + " ", s_F + s_R + " "},
		{"Hoe", s_P + s_I + s_H, s_F + s_R + " ", " " + s_R + " "},
		{"Hoe", s_P + s_I + s_H, s_F + s_R + " ", " " + s_R + " "},
		{"Sickle", " " + s_P + " ", s_P + s_F + " ", s_H + s_P + s_R},
		{"Sickle", " " + s_P + " ", s_P + s_F + " ", s_H + s_P + s_R},
		{"Sickle", " " + s_P + " ", s_P + s_F + " ", s_H + s_P + s_R},
		{"Sickle", " " + s_P + " ", s_P + s_F + " ", s_H + s_P + s_R},
		{"Sword", " " + s_R + " ", s_F + s_P + s_H, " " + s_P + " "},
		{"Pickaxe", " " + s_R + " ", s_F + s_R + s_H, s_P + s_I + s_I},
		{"Shovel", " " + s_R + " ", " " + s_R + " ", s_F + s_P + s_H},
		{"Axe", s_F + s_R + " ", s_P + s_R + " ", s_P + s_I + s_H},
		{"Axe", s_F + s_R + " ", s_P + s_R + " ", s_P + s_I + s_H},
		{"Hoe", " " + s_R + " ", s_F + s_R + " ", s_P + s_I + s_H},
		{"Hoe", " " + s_R + " ", s_F + s_R + " ", s_P + s_I + s_H},
		{"Spear", s_P + s_H + " ", s_F + s_R + " ", " " + " " + s_R},
		{"Spear", s_P + s_H + " ", s_F + s_R + " ", " " + " " + s_R},
		{"Knive", s_H + s_P, s_R + s_F},
		{"Knive", s_F + s_H, s_P + s_R},
		{"Knive", s_F + s_H, s_P + s_R},
		{"Knive", s_P + s_F, s_R + s_H},
		{"Knive", s_P + s_F, s_R + s_H},
		null,
		null,
		null,
		null,
		{"WarAxe", s_P + s_P + s_P, s_P + s_R + s_P, s_F + s_R + s_H},
		null,
		null,
		null,
		{"Shears", s_H + s_P, s_P + s_F},
		{"Shears", s_H + s_P, s_P + s_F},
		{"Scythe", s_I + s_P + s_H, s_R + s_F + s_P, s_R + " " + " "},
		{"Scythe", s_H + s_P + s_I, s_P + s_F + s_R, " " + " " + s_R}
	};
	public static volatile int VERSION = 508;

	public static void registerMaterialRecycling(final ItemStack aStack, final GT_Materials aMaterial, final long aMaterialAmount, GregtechMaterialStack aByproduct) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return;
		}
		if (aByproduct != null) {
			aByproduct = aByproduct.clone();
			aByproduct.mAmount /= aStack.stackSize;
		}
		GregtechOreDictUnificator.addItemData(GT_Utility.copyAmount(1, aStack), new GregtechItemData(aMaterial, aMaterialAmount / aStack.stackSize, aByproduct));
	}

	public static void registerMaterialRecycling(final ItemStack aStack, final GregtechItemData aData) {
		if (GT_Utility.isStackInvalid(aStack) || GT_Utility.areStacksEqual(new ItemStack(Items.blaze_rod), aStack) || (aData == null) || !aData.hasValidMaterialData() || (aData.mMaterial.mAmount <= 0) || (GT_Utility.getFluidForFilledItem(aStack, false) != null)) {
			return;
		}
		registerReverseMacerating(GT_Utility.copyAmount(1, aStack), aData, aData.mPrefix == null);
		registerReverseSmelting(GT_Utility.copyAmount(1, aStack), aData.mMaterial.mMaterial, aData.mMaterial.mAmount, true);
		registerReverseFluidSmelting(GT_Utility.copyAmount(1, aStack), aData.mMaterial.mMaterial, aData.mMaterial.mAmount, aData.getByProduct(0));
		registerReverseArcSmelting(GT_Utility.copyAmount(1, aStack), aData);
	}

	/**
	 * @param aStack          the stack to be recycled.
	 * @param aMaterial       the Material.
	 * @param aMaterialAmount the amount of it in Material Units.
	 */
	public static void registerReverseFluidSmelting(final ItemStack aStack, final GT_Materials aMaterial, final long aMaterialAmount, final GregtechMaterialStack aByproduct) {
		if ((aStack == null) || (aMaterial == null) || (aMaterial.mSmeltInto.mStandardMoltenFluid == null) || !aMaterial.contains(SubTag.SMELTING_TO_FLUID) || (((L * aMaterialAmount) / (M * aStack.stackSize)) <= 0)) {
			return;
		}
		RA.addFluidSmelterRecipe(GT_Utility.copyAmount(1, aStack), aByproduct == null ? null : aByproduct.mMaterial.contains(SubTag.NO_SMELTING) || !aByproduct.mMaterial.contains(SubTag.METAL) ? aByproduct.mMaterial.contains(SubTag.FLAMMABLE) ? GregtechOreDictUnificator.getDust(GT_Materials._NULL, aByproduct.mAmount / 2) : aByproduct.mMaterial.contains(SubTag.UNBURNABLE) ? GregtechOreDictUnificator.getDustOrIngot(aByproduct.mMaterial.mSmeltInto, aByproduct.mAmount) : null : GregtechOreDictUnificator.getIngotOrDust(aByproduct.mMaterial.mSmeltInto, aByproduct.mAmount), aMaterial.mSmeltInto.getMolten((L * aMaterialAmount) / (M * aStack.stackSize)), 10000, (int) Math.max(1, (24 * aMaterialAmount) / M), Math.max(8, (int) Math.sqrt(2 * aMaterial.mSmeltInto.mStandardMoltenFluid.getTemperature())));
	}

	/**
	 * @param aStack             the stack to be recycled.
	 * @param aMaterial          the Material.
	 * @param aMaterialAmount    the amount of it in Material Units.
	 * @param aAllowAlloySmelter if it is allowed to be recycled inside the Alloy Smelter.
	 */
	public static void registerReverseSmelting(final ItemStack aStack, final GT_Materials aMaterial, long aMaterialAmount, final boolean aAllowAlloySmelter) {
		if ((aStack == null) || (aMaterial == null) || (aMaterialAmount <= 0) || aMaterial.contains(SubTag.NO_SMELTING) || ((aMaterialAmount > M) && aMaterial.contains(SubTag.METAL))) {
			return;
		}
		aMaterialAmount /= aStack.stackSize;

		if (aAllowAlloySmelter) {
			CORE.GT_Recipe.addSmeltingAndAlloySmeltingRecipe(GT_Utility.copyAmount(1, aStack), GregtechOreDictUnificator.getIngot(aMaterial.mSmeltInto, aMaterialAmount));
		} else {
			GT_ModHandler.addSmeltingRecipe(GT_Utility.copyAmount(1, aStack), GregtechOreDictUnificator.getIngot(aMaterial.mSmeltInto, aMaterialAmount));
		}
	}

	public static void registerReverseArcSmelting(final ItemStack aStack, GregtechItemData aData) {
		if ((aStack == null) || (aData == null)) {
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

		RA.addArcFurnaceRecipe(aStack, new ItemStack[]{GregtechOreDictUnificator.getIngotOrDust(aData.mMaterial), GregtechOreDictUnificator.getIngotOrDust(aData.getByProduct(0)), GregtechOreDictUnificator.getIngotOrDust(aData.getByProduct(1)), GregtechOreDictUnificator.getIngotOrDust(aData.getByProduct(2))}, null, (int) Math.max(16, tAmount / M), 96);
	}

	public static void registerReverseMacerating(final ItemStack aStack, GregtechItemData aData, final boolean aAllowHammer) {
		if ((aStack == null) || (aData == null)) {
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

		RA.addPulveriserRecipe(aStack, new ItemStack[]{GregtechOreDictUnificator.getDust(aData.mMaterial), GregtechOreDictUnificator.getDust(aData.getByProduct(0)), GregtechOreDictUnificator.getDust(aData.getByProduct(1)), GregtechOreDictUnificator.getDust(aData.getByProduct(2))}, null, (int) Math.max(16, tAmount / M), 4);

		if (aAllowHammer) {
			for (final GregtechMaterialStack tMaterial : aData.getAllGT_MaterialStacks()) {
				if (tMaterial.mMaterial.contains(SubTag.CRYSTAL) && !tMaterial.mMaterial.contains(SubTag.METAL)) {
					if (RA.addForgeHammerRecipe(GT_Utility.copyAmount(1, aStack), GregtechOreDictUnificator.getDust(aData.mMaterial), 200, 32)) {
						break;
					}
				}
			}
		}
		final ItemStack tDust = GregtechOreDictUnificator.getDust(aData.mMaterial);
		if ((tDust != null) && GT_ModHandler.addPulverisationRecipe(GT_Utility.copyAmount(1, aStack), tDust, GregtechOreDictUnificator.getDust(aData.getByProduct(0)), 100, GregtechOreDictUnificator.getDust(aData.getByProduct(1)), 100, true)) {
			if (GregTech_API.sThaumcraftCompat != null) {
				GregTech_API.sThaumcraftCompat.addCrucibleRecipe(IThaumcraftCompat.ADVANCEDENTROPICPROCESSING, aStack, tDust, Arrays.asList(new TC_AspectStack(TC_Aspects.PERDITIO, Math.max(1, (aData.mMaterial.mAmount * 2) / M))));
			}
		}
	}

	/**
	 * You give this Function a Material and it will scan almost everything for adding recycling Recipes
	 *
	 * @param aMat             a Material, for example an Ingot or a Gem.
	 * @param aOutput          the Dust you usually get from macerating aMat
	 * @param aRecipeReplacing allows to replace the Recipe with a Plate variant
	 */
	public static synchronized void registerUsagesForGT_Materials(ItemStack aMat, String aPlate, final boolean aRecipeReplacing) {
		if (aMat == null) {
			return;
		}
		aMat = GT_Utility.copy(aMat);
		ItemStack tStack;
		final GregtechItemData aItemData = GregtechOreDictUnificator.getItemData(aMat);
		if ((aItemData == null) || (aItemData.mPrefix != GregtechOrePrefixes.ingot)) {
			aPlate = null;
		}
		if ((aPlate != null) && (GregtechOreDictUnificator.getFirstOre(aPlate, 1) == null)) {
			aPlate = null;
		}

		sMt1.func_150996_a(aMat.getItem());
		sMt1.stackSize = 1;
		Items.feather.setDamage(sMt1, Items.feather.getDamage(aMat));

		sMt2.func_150996_a(new ItemStack(Blocks.dirt).getItem());
		sMt2.stackSize = 1;
		Items.feather.setDamage(sMt2, 0);

		for (final ItemStack[] tRecipe : sShapes1) {
			int tAmount1 = 0;
			for (final ItemStack tMat : tRecipe) {
				if (tMat == sMt1) {
					tAmount1++;
				}
			}
			if ((aItemData != null) && aItemData.hasValidPrefixMaterialData()) {
				for (final ItemStack tCrafted : GT_ModHandler.getRecipeOutputs(tRecipe)) {
					GregtechOreDictUnificator.addItemData(tCrafted, new GregtechItemData(aItemData.mMaterial.mMaterial, aItemData.mMaterial.mAmount * tAmount1));
				}
			}
		}

		for (final GT_Materials tMaterial : sRodMaterialList) {
			final ItemStack tMt2 = GregtechOreDictUnificator.get(GregtechOrePrefixes.stick, tMaterial, 1);
			if (tMt2 != null) {
				sMt2.func_150996_a(tMt2.getItem());
				sMt2.stackSize = 1;
				Items.feather.setDamage(sMt2, Items.feather.getDamage(tMt2));

				for (int i = 0; i < sShapes1.length; i++) {
					final ItemStack[] tRecipe = sShapes1[i];

					int tAmount1 = 0, tAmount2 = 0;
					for (final ItemStack tMat : tRecipe) {
						if (tMat == sMt1) {
							tAmount1++;
						}
						if (tMat == sMt2) {
							tAmount2++;
						}
					}
					for (final ItemStack tCrafted : GT_ModHandler.getVanillyToolRecipeOutputs(tRecipe)) {
						if ((aItemData != null) && aItemData.hasValidPrefixMaterialData()) {
							GregtechOreDictUnificator.addItemData(tCrafted, new GregtechItemData(aItemData.mMaterial.mMaterial, aItemData.mMaterial.mAmount * tAmount1, new GregtechMaterialStack(tMaterial, OrePrefixes.stick.mMaterialAmount * tAmount2)));
						}

						if (aRecipeReplacing && (aPlate != null) && (sShapesA[i] != null) && (sShapesA[i].length > 1)) {
							assert aItemData != null;
							if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.recipereplacements, aItemData.mMaterial.mMaterial + "." + sShapesA[i][0], true)) {
								if (null != (tStack = GT_ModHandler.removeRecipe(tRecipe))) {
									switch (sShapesA[i].length) {
									case 2:
										GT_ModHandler.addCraftingRecipe(tStack, GT_ModHandler.RecipeBits.BUFFERED, new Object[]{sShapesA[i][1], s_P.charAt(0), aPlate, s_R.charAt(0), OrePrefixes.stick.get(tMaterial), s_I.charAt(0), aItemData});
										break;
									case 3:
										GT_ModHandler.addCraftingRecipe(tStack, GT_ModHandler.RecipeBits.BUFFERED, new Object[]{sShapesA[i][1], sShapesA[i][2], s_P.charAt(0), aPlate, s_R.charAt(0), OrePrefixes.stick.get(tMaterial), s_I.charAt(0), aItemData});
										break;
									default:
										GT_ModHandler.addCraftingRecipe(tStack, GT_ModHandler.RecipeBits.BUFFERED, new Object[]{sShapesA[i][1], sShapesA[i][2], sShapesA[i][3], s_P.charAt(0), aPlate, s_R.charAt(0), OrePrefixes.stick.get(tMaterial), s_I.charAt(0), aItemData});
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
