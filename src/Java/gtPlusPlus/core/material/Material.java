package gtPlusPlus.core.material;

import java.util.ArrayList;

import gregtech.api.enums.*;
import gtPlusPlus.core.item.base.cell.BaseItemCell;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.materials.MaterialUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class Material {

	private final String				unlocalizedName;
	private final String				localizedName;

	private final Fluid					vMoltenFluid;

	protected Object					dataVar;

	private ArrayList<MaterialStack>	vMaterialInput	= new ArrayList<MaterialStack>();
	public final long[]					vSmallestRatio;

	private final short[]				RGBA;

	private final boolean				usesBlastFurnace;
	public final boolean				isRadioactive;
	public final byte					vRadioationLevel;

	private final int					meltingPointK;
	private final int					boilingPointK;
	private final int					meltingPointC;
	private final int					boilingPointC;
	private final long					vProtons;
	private final long					vNeutrons;
	private final long					vMass;
	public final int					smallestStackSizeWhenProcessing;					// Add
																							// a
																							// check
																							// for
																							// <=0
																							// ||
																							// >
																							// 64
	public final int					vTier;
	public final int					vVoltageMultiplier;
	public final String					vChemicalFormula;
	public final String					vChemicalSymbol;

	public Material(final String materialName, final short[] rgba, final int meltingPoint, final int boilingPoint,
			final long protons, final long neutrons, final boolean blastFurnace, final int radiationLevel,
			final MaterialStack... inputs) {
		this(materialName, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", radiationLevel,
				inputs);
	}

	public Material(final String materialName, final short[] rgba, final int meltingPoint, final int boilingPoint,
			final long protons, final long neutrons, final boolean blastFurnace, final MaterialStack... inputs) {
		this(materialName, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", 0, inputs);
	}

	public Material(final String materialName, final short[] rgba, final int meltingPoint, final int boilingPoint,
			final long protons, final long neutrons, final boolean blastFurnace, final String chemicalSymbol,
			final int radiationLevel, final MaterialStack... inputs) {

		this.unlocalizedName = Utils.sanitizeString(materialName);
		this.localizedName = materialName;
		this.RGBA = rgba;
		this.meltingPointC = meltingPoint;
		if (boilingPoint != 0) {
			this.boilingPointC = boilingPoint;
		}
		else {
			this.boilingPointC = meltingPoint * 4;
		}
		this.meltingPointK = (int) MathUtils.celsiusToKelvin(this.meltingPointC);
		this.boilingPointK = (int) MathUtils.celsiusToKelvin(this.boilingPointC);
		this.vProtons = protons;
		this.vNeutrons = neutrons;
		this.vMass = this.getMass();

		// Sets the Rad level
		if (radiationLevel != 0) {
			this.isRadioactive = true;
			this.vRadioationLevel = (byte) radiationLevel;
		}
		else {
			this.isRadioactive = false;
			this.vRadioationLevel = (byte) radiationLevel;
		}

		// Sets the materials 'tier'. Will probably replace this logic.
		this.vTier = MaterialUtils.getTierOfMaterial((int) MathUtils.celsiusToKelvin(meltingPoint));

		this.usesBlastFurnace = blastFurnace;
		this.vVoltageMultiplier = this.getMeltingPointK() >= 2800 ? 64 : 16;

		if (inputs == null) {
			this.vMaterialInput = null;
		}
		else {
			if (inputs.length != 0) {
				for (int i = 0; i < inputs.length; i++) {
					if (inputs[i] != null) {
						this.vMaterialInput.add(i, inputs[i]);
					}
				}
			}
		}

		this.vSmallestRatio = this.getSmallestRatio(this.vMaterialInput);
		int tempSmallestSize = 0;

		if (this.vSmallestRatio != null) {
			for (int v = 0; v < this.vSmallestRatio.length; v++) {
				tempSmallestSize = (int) (tempSmallestSize + this.vSmallestRatio[v]);
			}
			this.smallestStackSizeWhenProcessing = tempSmallestSize; // Valid
																		// stacksizes
		}
		else {
			this.smallestStackSizeWhenProcessing = 1; // Valid stacksizes
		}

		// Makes a Fancy Chemical Tooltip
		this.vChemicalSymbol = chemicalSymbol;
		if (this.vMaterialInput != null) {
			this.vChemicalFormula = this.getToolTip(chemicalSymbol, OrePrefixes.dust.mMaterialAmount / GT_Values.M,
					true);
		}
		else if (!this.vChemicalSymbol.equals("")) {
			Utils.LOG_WARNING("materialInput is null, using a valid chemical symbol.");
			this.vChemicalFormula = this.vChemicalSymbol;
		}
		else {
			Utils.LOG_WARNING("MaterialInput == null && chemicalSymbol probably equals nothing");
			this.vChemicalFormula = "??";
		}

		final Materials isValid = Materials.get(this.getLocalizedName());
		if (isValid == Materials._NULL) {
			this.vMoltenFluid = this.generateFluid();
		}
		else {
			if (isValid.mFluid != null) {
				this.vMoltenFluid = isValid.mFluid;
			}
			else if (isValid.mGas != null) {
				this.vMoltenFluid = isValid.mGas;
			}
			else if (isValid.mPlasma != null) {
				this.vMoltenFluid = isValid.mPlasma;
			}
			else {
				this.vMoltenFluid = this.generateFluid();
			}
		}

		// dataVar = MathUtils.generateSingularRandomHexValue();

		String ratio = "";
		if (this.vSmallestRatio != null) {
			for (int hu = 0; hu < this.vSmallestRatio.length; hu++) {
				if (ratio.equals("")) {
					ratio = String.valueOf(this.vSmallestRatio[hu]);
				}
				else {
					ratio = ratio + ":" + this.vSmallestRatio[hu];
				}
			}
		}

		Utils.LOG_INFO("Creating a Material instance for " + materialName);
		Utils.LOG_INFO("Formula: " + this.vChemicalFormula + " Smallest Stack: " + this.smallestStackSizeWhenProcessing
				+ " Smallest Ratio:" + ratio);
		Utils.LOG_INFO("Protons: " + this.vProtons);
		Utils.LOG_INFO("Neutrons: " + this.vNeutrons);
		Utils.LOG_INFO("Mass: " + this.vMass + "/units");
		Utils.LOG_INFO("Melting Point: " + this.meltingPointC + "C.");
		Utils.LOG_INFO("Boiling Point: " + this.boilingPointC + "C.");
	}

	final Fluid generateFluid() {
		if (Materials.get(this.localizedName).mFluid == null) {
			Utils.LOG_WARNING("Generating our own fluid.");

			// Generate a Cell if we need to
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + this.getUnlocalizedName(), 1) == null) {
				@SuppressWarnings("unused")
				final Item temp = new BaseItemCell(this);
			}
			return FluidUtils.addGTFluid(this.getUnlocalizedName(), "Molten " + this.getLocalizedName(), this.RGBA, 4,
					this.getMeltingPointK(),
					ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + this.getUnlocalizedName(), 1),
					ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
		}
		Utils.LOG_WARNING("Getting the fluid from a GT material instead.");
		return Materials.get(this.localizedName).mFluid;
	}

	final public int getBoilingPointC() {
		return this.boilingPointC;
	}

	final public int getBoilingPointK() {
		return this.boilingPointK;
	}

	final public ItemStack getBolt(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("bolt" + this.unlocalizedName, stacksize);
	}

	final public ArrayList<MaterialStack> getComposites() {
		return this.vMaterialInput;
	}

	final public ItemStack getDust(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dust" + this.unlocalizedName, stacksize);
	}

	final public FluidStack getFluid(final int fluidAmount) {
		Utils.LOG_WARNING("Attempting to get " + fluidAmount + "L of " + this.vMoltenFluid.getName());

		final FluidStack moltenFluid = new FluidStack(this.vMoltenFluid, fluidAmount);

		Utils.LOG_WARNING("Info: " + moltenFluid.getFluid().getName() + " Info: " + moltenFluid.amount + " Info: "
				+ moltenFluid.getFluidID());

		// FluidStack moltenFluid =
		// FluidUtils.getFluidStack(this.vMoltenFluid.getName(), fluidAmount);
		/*
		 * boolean isNull = (moltenFluid == null); if (isNull)
		 * Utils.LOG_WARNING("Did not obtain fluid."); else Utils.LOG_WARNING(
		 * "Found fluid."); if (isNull){ return null; }
		 */
		return moltenFluid;
	}

	final public ItemStack getFrameBox(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("frameGt" + this.unlocalizedName, stacksize);
	}

	final public ItemStack getGear(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("gear" + this.unlocalizedName, stacksize);
	}

	final public ItemStack getIngot(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingot" + this.unlocalizedName, stacksize);
	}

	final public String getLocalizedName() {
		return this.localizedName;
	}

	final public ItemStack getLongRod(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("stickLong" + this.unlocalizedName, stacksize);
	}

	final public long getMass() {
		return this.vProtons + this.vNeutrons;
	}

	final public ItemStack[] getMaterialComposites() {
		// Utils.LOG_WARNING("Something requested the materials needed for
		// "+localizedName);
		if (this.vMaterialInput != null) {
			if (!this.vMaterialInput.isEmpty()) {
				final ItemStack[] temp = new ItemStack[this.vMaterialInput.size()];
				for (int i = 0; i < this.vMaterialInput.size(); i++) {
					// Utils.LOG_WARNING("i:"+i);
					ItemStack testNull = null;
					try {
						testNull = this.vMaterialInput.get(i).getDustStack();
					}
					catch (final Throwable r) {
						Utils.LOG_WARNING("Failed gathering material stack for " + this.localizedName + ".");
						Utils.LOG_WARNING("What Failed: Length:" + this.vMaterialInput.size() + " current:" + i);
					}
					try {
						if (testNull != null) {
							// Utils.LOG_WARNING("not null");
							temp[i] = this.vMaterialInput.get(i).getDustStack();
						}
					}
					catch (final Throwable r) {
						Utils.LOG_WARNING("Failed setting slot " + i + ", using " + this.localizedName);
					}
				}
				return temp;
			}
		}
		return new ItemStack[] {};
	}

	final public int[] getMaterialCompositeStackSizes() {
		if (!this.vMaterialInput.isEmpty()) {
			final int[] temp = new int[this.vMaterialInput.size()];
			for (int i = 0; i < this.vMaterialInput.size(); i++) {
				if (this.vMaterialInput.get(i) != null) {
					temp[i] = this.vMaterialInput.get(i).getDustStack().stackSize;
				}
				else {
					temp[i] = 0;
				}
			}
			return temp;
		}
		return new int[] {};
	}

	final public int getMeltingPointC() {
		return this.meltingPointC;
	}

	final public int getMeltingPointK() {
		return this.meltingPointK;
	}

	final public long getNeutrons() {
		return this.vNeutrons;
	}

	final public ItemStack getPlate(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plate" + this.unlocalizedName, stacksize);
	}

	final public ItemStack getPlateDouble(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDouble" + this.unlocalizedName, stacksize);
	}

	final public long getProtons() {
		return this.vProtons;
	}

	final public short[] getRGBA() {
		return this.RGBA;
	}

	final public int getRgbAsHex() {

		final int returnValue = Utils.rgbtoHexValue(this.RGBA[0], this.RGBA[1], this.RGBA[2]);
		if (returnValue == 0) {
			return (int) this.dataVar;
		}
		return Utils.rgbtoHexValue(this.RGBA[0], this.RGBA[1], this.RGBA[2]);
	}

	final public ItemStack getRing(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ring" + this.unlocalizedName, stacksize);
	}

	final public ItemStack getRod(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("stick" + this.unlocalizedName, stacksize);
	}

	final public ItemStack getRotor(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("rotor" + this.unlocalizedName, stacksize);
	}

	final public ItemStack getScrew(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("screw" + this.unlocalizedName, stacksize);
	}

	final public ItemStack getSmallDust(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustSmall" + this.unlocalizedName, stacksize);
	}

	@SuppressWarnings("static-method")
	final public long[] getSmallestRatio(final ArrayList<MaterialStack> tempInput) {
		if (tempInput != null) {
			if (!tempInput.isEmpty()) {
				Utils.LOG_WARNING("length: " + tempInput.size());
				Utils.LOG_WARNING("(inputs != null): " + (tempInput != null));
				// Utils.LOG_WARNING("length: "+inputs.length);
				final long[] tempRatio = new long[tempInput.size()];
				for (int x = 0; x < tempInput.size(); x++) {
					// tempPercentage =
					// tempPercentage+inputs[x].percentageToUse;
					// this.mMaterialList.add(inputs[x]);
					if (tempInput.get(x) != null) {
						tempRatio[x] = tempInput.get(x).getPartsPerOneHundred();
					}
				}

				final long[] smallestRatio = MathUtils.simplifyNumbersToSmallestForm(tempRatio);

				if (smallestRatio.length > 0) {
					String tempRatioStringThing1 = "";
					for (int r = 0; r < tempRatio.length; r++) {
						tempRatioStringThing1 = tempRatioStringThing1 + tempRatio[r] + " : ";
					}
					Utils.LOG_WARNING("Default Ratio: " + tempRatioStringThing1);

					String tempRatioStringThing = "";
					int tempSmallestCraftingUseSize = 0;
					for (int r = 0; r < smallestRatio.length; r++) {
						tempRatioStringThing = tempRatioStringThing + smallestRatio[r] + " : ";
						tempSmallestCraftingUseSize = (int) (tempSmallestCraftingUseSize + smallestRatio[r]);
					}
					// this.smallestStackSizeWhenProcessing =
					// tempSmallestCraftingUseSize;
					Utils.LOG_WARNING("Smallest Ratio: " + tempRatioStringThing);
					return smallestRatio;
				}
			}
		}
		return null;
	}

	final public ItemStack getTinyDust(final int stacksize) {
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustTiny" + this.unlocalizedName, stacksize);
	}

	@SuppressWarnings("unused")
	final String getToolTip(final String chemSymbol, final long aMultiplier, final boolean aShowQuestionMarks) {
		if (!aShowQuestionMarks && (this.vChemicalFormula.equals("?") || this.vChemicalFormula.equals("??"))) {
			return "";
		}
		Utils.LOG_WARNING(
				"===============| Calculating Atomic Formula for " + this.localizedName + " |===============");
		if (!chemSymbol.equals("")) {
			return chemSymbol;
		}
		final ArrayList<MaterialStack> tempInput = this.vMaterialInput;
		if (tempInput != null) {
			if (!tempInput.isEmpty()) {
				String dummyFormula = "";
				final long[] dummyFormulaArray = this.getSmallestRatio(tempInput);
				if (dummyFormulaArray != null) {
					if (dummyFormulaArray.length >= 1) {
						for (int e = 0; e < tempInput.size(); e++) {
							if (tempInput.get(e) != null) {
								if (tempInput.get(e).getStackMaterial() != null) {
									if (!tempInput.get(e).getStackMaterial().vChemicalSymbol.equals("??")) {
										if (dummyFormulaArray[e] > 1) {

											if (tempInput.get(e).getStackMaterial().vChemicalFormula.length() > 3) {
												dummyFormula = dummyFormula + "("
														+ tempInput.get(e).getStackMaterial().vChemicalFormula + ")"
														+ dummyFormulaArray[e];
											}
											else {
												dummyFormula = dummyFormula
														+ tempInput.get(e).getStackMaterial().vChemicalFormula
														+ dummyFormulaArray[e];
											}
										}
										else if (dummyFormulaArray[e] == 1) {
											if (tempInput.get(e).getStackMaterial().vChemicalFormula.length() > 3) {
												dummyFormula = dummyFormula + "("
														+ tempInput.get(e).getStackMaterial().vChemicalFormula + ")";
											}
											else {
												dummyFormula = dummyFormula
														+ tempInput.get(e).getStackMaterial().vChemicalFormula;
											}
										}
									}
									else {
										dummyFormula = dummyFormula + "??";
									}
								}
								else {
									dummyFormula = dummyFormula + "▓▓";
								}
							}
						}
						return MaterialUtils.subscript(dummyFormula);
						// return dummyFormula;
					}
					Utils.LOG_WARNING("dummyFormulaArray <= 0");
				}
				Utils.LOG_WARNING("dummyFormulaArray == null");
			}
			Utils.LOG_WARNING("tempInput.length <= 0");
		}
		Utils.LOG_WARNING("tempInput == null");
		return "??";

	}

	final public String getUnlocalizedName() {
		return this.unlocalizedName;
	}

	final public ItemStack[] getValidInputStacks() {
		return ItemUtils.validItemsForOreDict(this.unlocalizedName);
	}

	final public boolean requiresBlastFurnace() {
		return this.usesBlastFurnace;
	}

}
