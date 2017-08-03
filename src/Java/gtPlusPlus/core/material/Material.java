package gtPlusPlus.core.material;

import static gregtech.api.enums.GT_Values.M;

import java.util.ArrayList;

import gregtech.api.enums.*;
import gtPlusPlus.core.item.base.cell.BaseItemCell;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.StringUtils;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.materials.MaterialUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class Material {

	private final String unlocalizedName;
	private final String localizedName;

	private final MaterialState materialState;

	private final Fluid vMoltenFluid;
	private final Fluid vPlasma;

	protected Object dataVar = MathUtils.generateSingularRandomHexValue();

	private ArrayList<MaterialStack> vMaterialInput = new ArrayList<>();
	public final long[] vSmallestRatio;
	public final short vComponentCount;

	private final short[] RGBA;

	private final boolean usesBlastFurnace;
	public final boolean isRadioactive;
	public final byte vRadiationLevel;

	private final int meltingPointK;
	private final int boilingPointK;
	private final int meltingPointC;
	private final int boilingPointC;
	private final long vProtons;
	private final long vNeutrons;
	private final long vMass;
	public final int smallestStackSizeWhenProcessing; //Add a check for <=0 || > 64
	public final int vTier;
	public final int vVoltageMultiplier;
	public final String vChemicalFormula;
	public final String vChemicalSymbol;

	public final long vDurability;
	public final int vToolQuality;
	public final int vHarvestLevel;

	public Material(final String materialName, final MaterialState defaultState,final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final MaterialStack... inputs){
		this(materialName, defaultState, 0, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", 0, inputs);
	}

	public Material(final String materialName, final MaterialState defaultState,final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final int radiationLevel, final MaterialStack... inputs){
		this(materialName, defaultState, 0, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", radiationLevel, inputs);
	}

	public Material(final String materialName, final MaterialState defaultState,final long durability, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final int radiationLevel, final MaterialStack... inputs){
		this(materialName, defaultState, durability, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", radiationLevel, inputs);
	}

	public Material(final String materialName, final MaterialState defaultState,final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel, final MaterialStack... inputs){
		this(materialName, defaultState, 0, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, chemicalSymbol, radiationLevel, inputs);
	}

	public Material(final String materialName, final MaterialState defaultState, final long durability, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel, final MaterialStack... inputs){

		this.unlocalizedName = Utils.sanitizeString(materialName);
		this.localizedName = materialName;
		this.materialState = defaultState;
		this.RGBA = rgba;

		//Add Components to an array.
		if (inputs == null){
			this.vMaterialInput = null;
		}
		else {
			if (inputs.length != 0){
				for (int i=0; i < inputs.length; i++){
					if (inputs[i] != null){
						this.vMaterialInput.add(i, inputs[i]);
					}
				}
			}
		}
		

		//Set Melting/Boiling point, if value is -1 calculate it from compound inputs.
		if (meltingPoint != -1){
			this.meltingPointC = meltingPoint;
		}
		else {
			this.meltingPointC = this.calculateMeltingPoint();
		}
		if (boilingPoint != -1){
			if (boilingPoint != 0){
				this.boilingPointC = boilingPoint;
			}
			else {
				this.boilingPointC = meltingPoint*4;
			}
		}
		else {
			this.boilingPointC = this.calculateMeltingPoint();
		}

		this.meltingPointK = (int) MathUtils.celsiusToKelvin(this.meltingPointC);
		this.boilingPointK = (int) MathUtils.celsiusToKelvin(this.boilingPointC);
		
		//Set Proton/Neutron count, if value is -1 calculate it from compound inputs.
		if (protons != -1){
			this.vProtons = protons;
		}
		else {
			this.vProtons = this.calculateProtons();
		}
		if (boilingPoint != -1){
			this.vNeutrons = neutrons;
		}
		else {
			this.vNeutrons = this.calculateNeutrons();
		}
		
		
		
		
		this.vMass = this.getMass();

		//Sets tool Durability
		if (durability != 0){
			this.vDurability = durability;
		}
		else {
			if (inputs != null){
				long durabilityTemp = 0;
				int counterTemp = 0;
				for (final MaterialStack m : inputs){
					if (m.getStackMaterial() != null){
						if (m.getStackMaterial().vDurability != 0){
							durabilityTemp =  (durabilityTemp+m.getStackMaterial().vDurability);
							counterTemp++;

						}
					}
				}
				if ((durabilityTemp != 0) && (counterTemp != 0)){
					this.vDurability = (durabilityTemp/counterTemp);
				}
				else {
					this.vDurability = 8196;
				}
			}
			else {
				this.vDurability = 0;
			}
		}

		if ((this.vDurability >= 0) && (this.vDurability < 64000)){
			this.vToolQuality = 1;
			this.vHarvestLevel = 2;
		}
		else if ((this.vDurability >= 64000) && (this.vDurability < 128000)){
			this.vToolQuality = 2;
			this.vHarvestLevel = 2;
		}
		else if ((this.vDurability >= 128000) && (this.vDurability < 256000)){
			this.vToolQuality = 3;
			this.vHarvestLevel = 2;
		}
		else if ((this.vDurability >= 256000) && (this.vDurability < 512000)){
			this.vToolQuality = 3;
			this.vHarvestLevel = 3;
		}
		else if ((this.vDurability >= 512000) && (this.vDurability <= Integer.MAX_VALUE)){
			this.vToolQuality = 4;
			this.vHarvestLevel = 4;
		}
		else {
			this.vToolQuality = 0;
			this.vHarvestLevel = 0;
		}

		//Sets the Rad level
		if (radiationLevel != 0){
			this.isRadioactive = true;
			this.vRadiationLevel = (byte) radiationLevel;
		}
		else {
			this.isRadioactive = false;
			this.vRadiationLevel = (byte) radiationLevel;
		}

		//Sets the materials 'tier'. Will probably replace this logic.
		this.vTier = MaterialUtils.getTierOfMaterial((int) MathUtils.celsiusToKelvin(meltingPoint));

		this.usesBlastFurnace = blastFurnace;
		this.vVoltageMultiplier = this.getMeltingPointK() >= 2800 ? 60 : 15;

		this.vComponentCount = this.getComponentCount(inputs);
		this.vSmallestRatio = this.getSmallestRatio(this.vMaterialInput);
		int tempSmallestSize = 0;

		if (this.vSmallestRatio != null){
			for (int v=0;v<this.vSmallestRatio.length;v++){
				tempSmallestSize=(int) (tempSmallestSize+this.vSmallestRatio[v]);
			}
			this.smallestStackSizeWhenProcessing = tempSmallestSize; //Valid stacksizes
		}
		else {
			this.smallestStackSizeWhenProcessing = 1; //Valid stacksizes
		}


		//Makes a Fancy Chemical Tooltip
		this.vChemicalSymbol = chemicalSymbol;
		if (this.vMaterialInput != null){
			this.vChemicalFormula = this.getToolTip(chemicalSymbol, OrePrefixes.dust.mMaterialAmount / M, true);
		}
		else if (!this.vChemicalSymbol.equals("")){
			Utils.LOG_MACHINE_INFO("materialInput is null, using a valid chemical symbol.");
			this.vChemicalFormula = this.vChemicalSymbol;
		}
		else{
			Utils.LOG_MACHINE_INFO("MaterialInput == null && chemicalSymbol probably equals nothing");
			this.vChemicalFormula = "??";
		}

		final Materials isValid = Materials.get(this.getLocalizedName());
		if (isValid == Materials._NULL){
			this.vMoltenFluid = this.generateFluid();
		}
		else {
			if (isValid.mFluid != null){
				this.vMoltenFluid = isValid.mFluid;
			}
			else if (isValid.mGas != null){
				this.vMoltenFluid = isValid.mGas;
			}
			else {
				this.vMoltenFluid = this.generateFluid();
			}
		}

		this.vPlasma = this.generatePlasma();

		//dataVar = MathUtils.generateSingularRandomHexValue();

		String ratio = "";
		if (this.vSmallestRatio != null) {
			for (int hu=0;hu<this.vSmallestRatio.length;hu++){
				if (ratio.equals("")){
					ratio = String.valueOf(this.vSmallestRatio[hu]);
				}
				else {
					ratio = ratio + ":" +this.vSmallestRatio[hu];
				}
			}
		}

		Utils.LOG_MACHINE_INFO("Creating a Material instance for "+materialName);
		Utils.LOG_MACHINE_INFO("Formula: "+this.vChemicalFormula + " Smallest Stack: "+this.smallestStackSizeWhenProcessing+" Smallest Ratio:"+ratio);
		Utils.LOG_MACHINE_INFO("Protons: "+this.vProtons);
		Utils.LOG_MACHINE_INFO("Neutrons: "+this.vNeutrons);
		Utils.LOG_MACHINE_INFO("Mass: "+this.vMass+"/units");
		Utils.LOG_MACHINE_INFO("Melting Point: "+this.meltingPointC+"C.");
		Utils.LOG_MACHINE_INFO("Boiling Point: "+this.boilingPointC+"C.");
	}

	public final String getLocalizedName(){
		if (this.localizedName != null) {
			return this.localizedName;
		}
		return "ERROR BAD LOCALIZED NAME";
	}

	public final String getUnlocalizedName(){
		if (this.unlocalizedName != null) {
			return this.unlocalizedName;
		}
		return "ERROR.BAD.UNLOCALIZED.NAME";
	}

	final public MaterialState getState(){
		return this.materialState;
	}

	final public short[] getRGBA(){
		if (this.RGBA != null) {
			return this.RGBA;
		}
		return new short[] {255,0,0};
	}

	final public int getRgbAsHex(){

		final int returnValue = Utils.rgbtoHexValue(this.RGBA[0], this.RGBA[1], this.RGBA[2]);
		if (returnValue == 0){
			return (int) this.dataVar;
		}
		return Utils.rgbtoHexValue(this.RGBA[0], this.RGBA[1], this.RGBA[2]);
	}

	final public long getProtons() {
		return this.vProtons;
	}

	public final long getNeutrons() {
		return this.vNeutrons;
	}

	final public long getMass() {
		return this.vProtons + this.vNeutrons;
	}

	public final int getMeltingPointC() {
		return this.meltingPointC;
	}

	public final int getBoilingPointC() {
		return this.boilingPointC;
	}

	public final int getMeltingPointK() {
		return this.meltingPointK;
	}

	public final int getBoilingPointK() {
		return this.boilingPointK;
	}

	public final boolean requiresBlastFurnace(){
		return this.usesBlastFurnace;
	}

	final public Block getBlock(){
		return Block.getBlockFromItem(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("block"+this.unlocalizedName, 1).getItem());
	}

	public final ItemStack getBlock(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("block"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getDust(final int stacksize){
		return ItemUtils.getGregtechDust("dust"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getSmallDust(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustSmall"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getTinyDust(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustTiny"+this.unlocalizedName, stacksize);
	}

	public final ItemStack[] getValidInputStacks(){
		return ItemUtils.validItemsForOreDict(this.unlocalizedName);
	}

	public final ItemStack getIngot(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingot"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getPlate(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plate"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getPlateDouble(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDouble"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getGear(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("gear"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getRod(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("stick"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getLongRod(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("stickLong"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getBolt(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("bolt"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getScrew(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("screw"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getRing(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ring"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getRotor(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("rotor"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getFrameBox(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("frameGt"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getCell(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getPlasmaCell(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellPlasma"+this.unlocalizedName, stacksize);
	}

	public final ItemStack getNugget(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("nugget"+this.unlocalizedName, stacksize);
	}

	final public ItemStack[] getMaterialComposites(){
		if (this.vMaterialInput != null){
			if (!this.vMaterialInput.isEmpty()){
				final ItemStack[] temp = new ItemStack[this.vMaterialInput.size()];
				for (int i=0;i<this.vMaterialInput.size();i++){
					//Utils.LOG_MACHINE_INFO("i:"+i);
					ItemStack testNull = null;
					try {
						testNull = this.vMaterialInput.get(i).getValidStack();
					} catch (final Throwable r){
						Utils.LOG_MACHINE_INFO("Failed gathering material stack for "+this.localizedName+".");
						Utils.LOG_MACHINE_INFO("What Failed: Length:"+this.vMaterialInput.size()+" current:"+i);
					}
					try {
						if (testNull != null){
							//Utils.LOG_MACHINE_INFO("not null");
							temp[i] = this.vMaterialInput.get(i).getValidStack();
						}
					} catch (final Throwable r){
						Utils.LOG_MACHINE_INFO("Failed setting slot "+i+", using "+this.localizedName);
					}
				}
				return temp;
			}
		}
		return new ItemStack[]{};
	}

	public final ArrayList<MaterialStack> getComposites(){
		return this.vMaterialInput;
	}

	final public int[] getMaterialCompositeStackSizes(){
		if (!this.vMaterialInput.isEmpty()){
			final int[] temp = new int[this.vMaterialInput.size()];
			for (int i=0;i<this.vMaterialInput.size();i++){
				if (this.vMaterialInput.get(i) != null) {
					temp[i] = this.vMaterialInput.get(i).getDustStack().stackSize;
				} else {
					temp[i]=0;
				}
			}
			return temp;
		}
		return new int[]{};
	}

	private final short getComponentCount(final MaterialStack[] inputs){
		int counterTemp = 0;
		for (final MaterialStack m : inputs){
			if (m.getStackMaterial() != null){
				counterTemp++;
			}
		}
		if (counterTemp != 0){
			return (short) counterTemp;
		}
		else {
			return 1;
		}
	}


	@SuppressWarnings("static-method")
	public final long[] getSmallestRatio(final ArrayList<MaterialStack> tempInput){
		if (tempInput != null){
			if (!tempInput.isEmpty()){
				Utils.LOG_MACHINE_INFO("length: "+tempInput.size());
				Utils.LOG_MACHINE_INFO("(inputs != null): "+(tempInput != null));
				//Utils.LOG_MACHINE_INFO("length: "+inputs.length);
				final long[] tempRatio = new long[tempInput.size()];
				for (int x=0;x<tempInput.size();x++){
					//tempPercentage = tempPercentage+inputs[x].percentageToUse;
					//this.mMaterialList.add(inputs[x]);
					if (tempInput.get(x) != null){
						tempRatio[x] = tempInput.get(x).getPartsPerOneHundred();
					}
				}

				final long[] smallestRatio = MathUtils.simplifyNumbersToSmallestForm(tempRatio);

				if (smallestRatio.length > 0){
					String tempRatioStringThing1 = "";
					for (int r=0;r<tempRatio.length;r++){
						tempRatioStringThing1 = tempRatioStringThing1 + tempRatio[r] +" : ";
					}
					Utils.LOG_MACHINE_INFO("Default Ratio: "+tempRatioStringThing1);

					String tempRatioStringThing = "";
					int tempSmallestCraftingUseSize = 0;
					for (int r=0;r<smallestRatio.length;r++){
						tempRatioStringThing = tempRatioStringThing + smallestRatio[r] +" : ";
						tempSmallestCraftingUseSize = (int) (tempSmallestCraftingUseSize + smallestRatio[r]);
					}
					//this.smallestStackSizeWhenProcessing = tempSmallestCraftingUseSize;
					Utils.LOG_MACHINE_INFO("Smallest Ratio: "+tempRatioStringThing);
					return smallestRatio;
				}
			}
		}
		return null;
	}

	public final String getToolTip(final String chemSymbol, final long aMultiplier, final boolean aShowQuestionMarks) {
		if (!aShowQuestionMarks && (this.vChemicalFormula.equals("?")||this.vChemicalFormula.equals("??"))) {
			return "";
		}
		Utils.LOG_MACHINE_INFO("===============| Calculating Atomic Formula for "+this.localizedName+" |===============");
		if (!chemSymbol.equals("")) {
			return chemSymbol;
		}
		final ArrayList<MaterialStack> tempInput = this.vMaterialInput;
		if (tempInput != null){
			if (!tempInput.isEmpty()){
				String dummyFormula = "";
				final long[] dummyFormulaArray = this.getSmallestRatio(tempInput);
				if (dummyFormulaArray != null){
					if (dummyFormulaArray.length >= 1){
						for (int e=0;e<tempInput.size();e++){
							if (tempInput.get(e) != null){
								if (tempInput.get(e).getStackMaterial() != null){
									if (!tempInput.get(e).getStackMaterial().vChemicalSymbol.equals("??")){
										if (dummyFormulaArray[e] > 1){

											if (tempInput.get(e).getStackMaterial().vChemicalFormula.length() > 3){
												dummyFormula = dummyFormula + "(" + tempInput.get(e).getStackMaterial().vChemicalFormula + ")" + dummyFormulaArray[e];
											}
											else {
												dummyFormula = dummyFormula + tempInput.get(e).getStackMaterial().vChemicalFormula + dummyFormulaArray[e];
											}
										}
										else if (dummyFormulaArray[e] == 1){
											if (tempInput.get(e).getStackMaterial().vChemicalFormula.length() > 3){
												dummyFormula = dummyFormula + "(" +tempInput.get(e).getStackMaterial().vChemicalFormula + ")";
											}
											else {
												dummyFormula = dummyFormula +tempInput.get(e).getStackMaterial().vChemicalFormula;
											}
										}
									} else {
										dummyFormula = dummyFormula + "??";
									}
								} else {
									dummyFormula = dummyFormula + "▓▓";
								}
							}
						}
						return StringUtils.subscript(dummyFormula);
						//return dummyFormula;
					}
					Utils.LOG_MACHINE_INFO("dummyFormulaArray <= 0");
				}
				Utils.LOG_MACHINE_INFO("dummyFormulaArray == null");
			}
			Utils.LOG_MACHINE_INFO("tempInput.length <= 0");
		}
		Utils.LOG_MACHINE_INFO("tempInput == null");
		return "??";

	}

	public final Fluid generateFluid(){

		try {
			if (Materials.get(this.localizedName) == Materials.Clay){
				return null;
			}
		} catch (final Throwable e){}

		if (Materials.get(this.localizedName).mFluid == null){
			Utils.LOG_MACHINE_INFO("Generating our own fluid.");

			//Generate a Cell if we need to
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+this.getUnlocalizedName(), 1) == null){
				@SuppressWarnings("unused")
				final
				Item temp = new BaseItemCell(this);
			}

			if (this.materialState == MaterialState.SOLID){
				return FluidUtils.addGTFluid(
						this.getUnlocalizedName(),
						"Molten "+this.getLocalizedName(),
						this.RGBA,
						this.materialState.ID(),
						this.getMeltingPointK(),
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+this.getUnlocalizedName(), 1),
						ItemList.Cell_Empty.get(1L, new Object[0]),
						1000);
			}
			else if (this.materialState == MaterialState.LIQUID){
				return FluidUtils.addGTFluid(
						this.getUnlocalizedName(),
						this.getLocalizedName(),
						this.RGBA,
						this.materialState.ID(),
						this.getMeltingPointK(),
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+this.getUnlocalizedName(), 1),
						ItemList.Cell_Empty.get(1L, new Object[0]),
						1000);
			}
			else if (this.materialState == MaterialState.GAS){
				return FluidUtils.addGTFluid(
						this.getUnlocalizedName(),
						this.getLocalizedName()+" Gas",
						this.RGBA,
						this.materialState.ID(),
						this.getMeltingPointK(),
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+this.getUnlocalizedName(), 1),
						ItemList.Cell_Empty.get(1L, new Object[0]),
						1000);
			}
			else { //Plasma
				return this.generatePlasma();
			}
		}
		Utils.LOG_MACHINE_INFO("Getting the fluid from a GT material instead.");
		return Materials.get(this.localizedName).mFluid;
	}

	public final Fluid generatePlasma(){
		final Materials isValid = Materials.get(this.getLocalizedName());
		if ((isValid != Materials._NULL) && (isValid != null) && (isValid != Materials.Clay) && (isValid != Materials.Clay)
				&& (isValid != Materials.Phosphorus) && (isValid != Materials.Steel) && (isValid != Materials.Bronze)){
			if (isValid.mPlasma != null){
				Utils.LOG_MACHINE_INFO("Using a pre-defined Plasma from GT.");
				return isValid.mPlasma;
			}
		}
		Utils.LOG_MACHINE_INFO("Generating our own Plasma.");
		return FluidUtils.addGTPlasma(this);
		//return null;
	}



	final public FluidStack getFluid(final int fluidAmount) {
		Utils.LOG_MACHINE_INFO("Attempting to get "+fluidAmount+"L of "+this.vMoltenFluid.getName());
		final FluidStack moltenFluid = new FluidStack(this.vMoltenFluid, fluidAmount);
		Utils.LOG_MACHINE_INFO("Info: "+moltenFluid.getFluid().getName()+" Info: "+moltenFluid.amount+" Info: "+moltenFluid.getFluidID());
		return moltenFluid;
	}


	final public int calculateMeltingPoint(){
		int meltingPoint = 0;
		for (MaterialStack  part : this.vMaterialInput){
			int incrementor = part.getStackMaterial().getMeltingPointC();
			meltingPoint += incrementor;
			Utils.LOG_MACHINE_INFO("Melting Point for "+this.getLocalizedName()+" increased to "+ incrementor);
		}
		int divisor = (this.vMaterialInput.size()>0 ? this.vMaterialInput.size() : 1);
		Utils.LOG_MACHINE_INFO("Dividing "+meltingPoint+" / "+divisor+" to get average melting point.");
		meltingPoint = (meltingPoint/divisor);
		return meltingPoint;
	}

	final public int calculateBoilingPoint(){
		int boilingPoint = 0;
		for (MaterialStack  part : this.vMaterialInput){
			boilingPoint += part.getStackMaterial().getBoilingPointC();
		}
		int divisor = (this.vMaterialInput.size()>0 ? this.vMaterialInput.size() : 1);
		boilingPoint = (boilingPoint/divisor);
		return boilingPoint;
	}

	final public long calculateProtons(){
		long protonCount = 0;
		for (MaterialStack  part : this.vMaterialInput){
			protonCount += (part.getStackMaterial().getProtons());
		}
		int divisor = (this.vMaterialInput.size()>0 ? this.vMaterialInput.size() : 1);
		protonCount = (protonCount/divisor);
		return protonCount;
	}

	final public long calculateNeutrons(){
		long neutronCount = 0;
		for (MaterialStack  part : this.vMaterialInput){
			neutronCount += (part.getStackMaterial().getNeutrons());
		}
		int divisor = (this.vMaterialInput.size()>0 ? this.vMaterialInput.size() : 1);
		neutronCount = (neutronCount/divisor);
		return neutronCount;
	}





























}
