package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_Fluids extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	public RecipeGen_Fluids(final Material M) {
		this(M, false);
	}

	public RecipeGen_Fluids(final Material M, final boolean dO) {
		this.toGenerate = M;
		this.disableOptional = dO;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate, this.disableOptional);
	}

	private void generateRecipes(final Material material, final boolean dO) {

		if (material == null) {
			return;
		}

		// Melting Shapes to fluid
		if (material.getFluid(1) != null
				&& !material.getFluid(1).getUnlocalizedName().toLowerCase().contains("plasma")) {

			if (!material.requiresBlastFurnace()) {

				// Ingot
				if (ItemUtils.checkForInvalidItems(material.getIngot(1)))
					if (CORE.RA.addFluidExtractionRecipe(
							material.getIngot(1), // Input
							material.getFluid(144), // Fluid Output
							1 * 20, // Duration
							material.vVoltageMultiplier // Eu Tick
					)) {
						Logger.WARNING("144l fluid extractor from 1 ingot Recipe: " + material.getLocalizedName()
								+ " - Success");
					} else {
						Logger.WARNING("144l fluid extractor from 1 ingot Recipe: " + material.getLocalizedName()
								+ " - Failed");
					}

				// Plate
				if (ItemUtils.checkForInvalidItems(material.getPlate(1)))
					if (CORE.RA.addFluidExtractionRecipe(
							material.getPlate(1), // Input
							material.getFluid(144), // Fluid Output
							1 * 20, // Duration
							material.vVoltageMultiplier // Eu Tick
					)) {
						Logger.WARNING("144l fluid extractor from 1 plate Recipe: " + material.getLocalizedName()
								+ " - Success");
					} else {
						Logger.WARNING("144l fluid extractor from 1 plate Recipe: " + material.getLocalizedName()
								+ " - Failed");
					}

				// Double Plate
				if (ItemUtils.checkForInvalidItems(material.getPlateDouble(1)))
					if (CORE.RA.addFluidExtractionRecipe(
							material.getPlateDouble(1), // Input
							material.getFluid(288), // Fluid Output
							1 * 20, // Duration
							material.vVoltageMultiplier // Eu Tick
					)) {
						Logger.WARNING("144l fluid extractor from 1 double plate Recipe: " + material.getLocalizedName()
								+ " - Success");
					} else {
						Logger.WARNING("144l fluid extractor from 1 double plate Recipe: " + material.getLocalizedName()
								+ " - Failed");
					}

				// Nugget
				if (ItemUtils.checkForInvalidItems(material.getNugget(1)))
					if (CORE.RA.addFluidExtractionRecipe(
							material.getNugget(1), // Input
							material.getFluid(16), // Fluid Output
							16, // Duration
							material.vVoltageMultiplier // Eu Tick
					)) {
						Logger.WARNING("16l fluid extractor from 1 nugget Recipe: " + material.getLocalizedName()
								+ " - Success");
					} else {
						Logger.WARNING("16l fluid extractor from 1 nugget Recipe: " + material.getLocalizedName()
								+ " - Failed");
					}

				// Block
				if (ItemUtils.checkForInvalidItems(material.getBlock(1)))
					if (CORE.RA.addFluidExtractionRecipe(
							material.getBlock(1), // Input
							material.getFluid(144 * 9), // Fluid Output
							288, // Duration
							material.vVoltageMultiplier // Eu Tick
					)) {
						Logger.WARNING((144 * 9) + "l fluid extractor from 1 block Recipe: "
								+ material.getLocalizedName() + " - Success");
					} else {
						Logger.WARNING((144 * 9) + "l fluid extractor from 1 block Recipe: "
								+ material.getLocalizedName() + " - Failed");
					}

			}

			// Making Shapes from fluid

			// Ingot
			if (ItemUtils.checkForInvalidItems(material.getIngot(1)))
				if (GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ingot.get(0), // Item Shape
						material.getFluid(144), // Fluid Input
						material.getIngot(1), // output
						32, // Duration
						material.vVoltageMultiplier // Eu Tick
				)) {
					Logger.WARNING(
							"144l fluid molder for 1 ingot Recipe: " + material.getLocalizedName() + " - Success");
				} else {
					Logger.WARNING(
							"144l fluid molder for 1 ingot Recipe: " + material.getLocalizedName() + " - Failed");
				}

			// Plate
			if (ItemUtils.checkForInvalidItems(material.getPlate(1)))
				if (GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Plate.get(0), // Item Shape
						material.getFluid(144), // Fluid Input
						material.getPlate(1), // output
						32, // Duration
						material.vVoltageMultiplier // Eu Tick
				)) {
					Logger.WARNING(
							"144l fluid molder for 1 plate Recipe: " + material.getLocalizedName() + " - Success");
				} else {
					Logger.WARNING(
							"144l fluid molder for 1 plate Recipe: " + material.getLocalizedName() + " - Failed");
				}

			// Nugget
			if (ItemUtils.checkForInvalidItems(material.getNugget(1)))
				if (GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Nugget.get(0), // Item Shape
						material.getFluid(16), // Fluid Input
						material.getNugget(1), // output
						16, // Duration
						material.vVoltageMultiplier // Eu Tick
				)) {
					Logger.WARNING(
							"16l fluid molder for 1 nugget Recipe: " + material.getLocalizedName() + " - Success");
				} else {
					Logger.WARNING(
							"16l fluid molder for 1 nugget Recipe: " + material.getLocalizedName() + " - Failed");
				}

			// Gears
			if (ItemUtils.checkForInvalidItems(material.getGear(1)))
				if (GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Gear.get(0), // Item Shape
						material.getFluid(576), // Fluid Input
						material.getGear(1), // output
						128, // Duration
						material.vVoltageMultiplier // Eu Tick
				)) {
					Logger.WARNING(
							"576l fluid molder for 1 gear Recipe: " + material.getLocalizedName() + " - Success");
				} else {
					Logger.WARNING("576l fluid molder for 1 gear Recipe: " + material.getLocalizedName() + " - Failed");
				}

			// Blocks
			if (ItemUtils.checkForInvalidItems(material.getBlock(1)))
				if (GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0), // Item Shape
						material.getFluid(144 * 9), // Fluid Input
						material.getBlock(1), // output
						288, // Duration
						material.vVoltageMultiplier // Eu Tick
				)) {
					Logger.WARNING((144 * 9) + "l fluid molder from 1 block Recipe: " + material.getLocalizedName()
							+ " - Success");
				} else {
					Logger.WARNING((144 * 9) + "l fluid molder from 1 block Recipe: " + material.getLocalizedName()
							+ " - Failed");
				}

			if (CORE.GTNH) {

				// GTNH

				// Shape_Mold_Rod
				// Shape_Mold_Rod_Long
				// Shape_Mold_Bolt,
				// Shape_Mold_Screw,
				// Shape_Mold_Ring,

				
				ItemList mold_Rod = gtPlusPlus.core.util.Utils.getValueOfItemList("Shape_Mold_Rod", null);
				ItemList mold_Rod_Long = gtPlusPlus.core.util.Utils.getValueOfItemList("Shape_Mold_Rod_Long", null);
				ItemList mold_Bolt = gtPlusPlus.core.util.Utils.getValueOfItemList("Shape_Mold_Bolt", null);
				ItemList mold_Screw = gtPlusPlus.core.util.Utils.getValueOfItemList("Shape_Mold_Screw", null);
				ItemList mold_Ring = gtPlusPlus.core.util.Utils.getValueOfItemList("Shape_Mold_Ring", null);

				// Rod
				if (ItemUtils.checkForInvalidItems(material.getRod(1)))
					if (mold_Rod != null && GT_Values.RA.addFluidSolidifierRecipe(mold_Rod.get(0), // Item Shape
							material.getFluid(72), // Fluid Input
							material.getRod(1), // output
							150, // Duration
							material.vVoltageMultiplier // Eu Tick
					)) {
						Logger.WARNING((144 * 9) + "l fluid molder from 1 rod Recipe: " + material.getLocalizedName()
								+ " - Success");
					} else {
						Logger.WARNING((144 * 9) + "l fluid molder from 1 rod Recipe: " + material.getLocalizedName()
								+ " - Failed");
					}

				// Rod Long
				if (ItemUtils.checkForInvalidItems(material.getLongRod(1)))
					if (mold_Rod_Long != null && GT_Values.RA.addFluidSolidifierRecipe(mold_Rod_Long.get(0), // Item
																												// Shape
							material.getFluid(144), // Fluid Input
							material.getLongRod(1), // output
							300, // Duration
							material.vVoltageMultiplier // Eu Tick
					)) {
						Logger.WARNING((144 * 9) + "l fluid molder from 1 rod long Recipe: "
								+ material.getLocalizedName() + " - Success");
					} else {
						Logger.WARNING((144 * 9) + "l fluid molder from 1 rod long Recipe: "
								+ material.getLocalizedName() + " - Failed");
					}

				// Bolt
				if (ItemUtils.checkForInvalidItems(material.getBolt(1)))
					if (mold_Bolt != null && GT_Values.RA.addFluidSolidifierRecipe(mold_Bolt.get(0), // Item Shape
							material.getFluid(18), // Fluid Input
							material.getBolt(1), // output
							50, // Duration
							material.vVoltageMultiplier // Eu Tick
					)) {
						Logger.WARNING((144 * 9) + "l fluid molder from 1 bolt Recipe: " + material.getLocalizedName()
								+ " - Success");
					} else {
						Logger.WARNING((144 * 9) + "l fluid molder from 1 bolt Recipe: " + material.getLocalizedName()
								+ " - Failed");
					}

				// Screw
				if (ItemUtils.checkForInvalidItems(material.getScrew(1)))
					if (mold_Screw != null && GT_Values.RA.addFluidSolidifierRecipe(mold_Screw.get(0), // Item Shape
							material.getFluid(18), // Fluid Input
							material.getScrew(1), // output
							50, // Duration
							material.vVoltageMultiplier // Eu Tick
					)) {
						Logger.WARNING((144 * 9) + "l fluid molder from 1 screw Recipe: " + material.getLocalizedName()
								+ " - Success");
					} else {
						Logger.WARNING((144 * 9) + "l fluid molder from 1 screw Recipe: " + material.getLocalizedName()
								+ " - Failed");
					}

				// Ring
				if (ItemUtils.checkForInvalidItems(material.getRing(1)))
					if (mold_Ring != null && GT_Values.RA.addFluidSolidifierRecipe(mold_Ring.get(0), // Item Shape
							material.getFluid(36), // Fluid Input
							material.getRing(1), // output
							100, // Duration
							material.vVoltageMultiplier // Eu Tick
					)) {
						Logger.WARNING((144 * 9) + "l fluid molder from 1 ring Recipe: " + material.getLocalizedName()
								+ " - Success");
					} else {
						Logger.WARNING((144 * 9) + "l fluid molder from 1 ring Recipe: " + material.getLocalizedName()
								+ " - Failed");
					}

			}
		}
	}
}
