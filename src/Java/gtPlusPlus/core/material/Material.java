package gtPlusPlus.core.material;

import static gregtech.api.enums.GT_Values.M;
import gregtech.api.enums.*;
import gtPlusPlus.core.item.base.cell.BaseItemCell;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.materials.MaterialUtils;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class Material {

	private final String unlocalizedName;
	private final String localizedName;

	private final Fluid vMoltenFluid;

	protected Object dataVar = MathUtils.generateSingularRandomHexValue();

	private ArrayList<MaterialStack> vMaterialInput = new ArrayList<MaterialStack>();
	public final long[] vSmallestRatio;

	private final short[] RGBA;

	private final boolean usesBlastFurnace;
	public final boolean isRadioactive;
	public final byte vRadioationLevel;

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

	public Material(final String materialName, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final MaterialStack... inputs){		
		this(materialName, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", 0, inputs);
	}

	public Material(final String materialName, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final int radiationLevel, MaterialStack... inputs){		
		this(materialName, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", radiationLevel, inputs);
	}

	public Material(final String materialName, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel, final MaterialStack... inputs){

		this.unlocalizedName = Utils.sanitizeString(materialName);
		this.localizedName = materialName;
		this.RGBA = rgba;
		this.meltingPointC = meltingPoint;
		if (boilingPoint != 0){
			this.boilingPointC = boilingPoint;
		}
		else {
			this.boilingPointC = meltingPoint*4;
		}
		this.meltingPointK = (int) MathUtils.celsiusToKelvin(meltingPointC);
		this.boilingPointK = (int) MathUtils.celsiusToKelvin(boilingPointC);
		this.vProtons = protons;
		this.vNeutrons = neutrons;
		this.vMass = getMass();

		//Sets the Rad level
		if (radiationLevel != 0){
			this.isRadioactive = true;
			this.vRadioationLevel = (byte) radiationLevel;
		}
		else {
			this.isRadioactive = false;
			this.vRadioationLevel = (byte) radiationLevel;
		}

		//Sets the materials 'tier'. Will probably replace this logic.
		this.vTier = MaterialUtils.getTierOfMaterial((int) MathUtils.celsiusToKelvin(meltingPoint));

		this.usesBlastFurnace = blastFurnace;
		this.vVoltageMultiplier = this.getMeltingPointK() >= 2800 ? 64 : 16;

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

		this.vSmallestRatio = getSmallestRatio(vMaterialInput);
		int tempSmallestSize = 0;

		if (vSmallestRatio != null){
			for (int v=0;v<this.vSmallestRatio.length;v++){
				tempSmallestSize=(int) (tempSmallestSize+vSmallestRatio[v]);
			}
			this.smallestStackSizeWhenProcessing = tempSmallestSize; //Valid stacksizes
		}
		else {
			this.smallestStackSizeWhenProcessing = 1; //Valid stacksizes			
		}


		//Makes a Fancy Chemical Tooltip
		this.vChemicalSymbol = chemicalSymbol;
		if (vMaterialInput != null){
			this.vChemicalFormula = getToolTip(chemicalSymbol, OrePrefixes.dust.mMaterialAmount / M, true);
		}
		else if (!this.vChemicalSymbol.equals("")){
			Utils.LOG_WARNING("materialInput is null, using a valid chemical symbol.");
			this.vChemicalFormula = this.vChemicalSymbol;
		}
		else{
			Utils.LOG_WARNING("MaterialInput == null && chemicalSymbol probably equals nothing");
			this.vChemicalFormula = "??";
		}

		Materials isValid = Materials.get(getLocalizedName());
		if (isValid == Materials._NULL){
			this.vMoltenFluid = generateFluid();
		}
		else {
			if (isValid.mFluid != null){
				this.vMoltenFluid = isValid.mFluid;
			}
			else if (isValid.mGas != null){
				this.vMoltenFluid = isValid.mGas;
			}
			else if (isValid.mPlasma != null){
				this.vMoltenFluid = isValid.mPlasma;
			}
			else {
				this.vMoltenFluid = generateFluid();
			}
		}

		//dataVar = MathUtils.generateSingularRandomHexValue();

		String ratio = "";
		if (vSmallestRatio != null)
			for (int hu=0;hu<vSmallestRatio.length;hu++){
				if (ratio.equals("")){
					ratio = String.valueOf(vSmallestRatio[hu]);
				}
				else {
					ratio = ratio + ":" +vSmallestRatio[hu];
				}		
			}

		Utils.LOG_INFO("Creating a Material instance for "+materialName);
		Utils.LOG_INFO("Formula: "+vChemicalFormula + " Smallest Stack: "+smallestStackSizeWhenProcessing+" Smallest Ratio:"+ratio);
		Utils.LOG_INFO("Protons: "+vProtons);
		Utils.LOG_INFO("Neutrons: "+vNeutrons);
		Utils.LOG_INFO("Mass: "+vMass+"/units");
		Utils.LOG_INFO("Melting Point: "+meltingPointC+"C.");
		Utils.LOG_INFO("Boiling Point: "+boilingPointC+"C.");
	}

	final public String getLocalizedName(){
		if (this.localizedName != null)
			return this.localizedName;
		return "ERROR BAD LOCALIZED NAME";
	}

	final public String getUnlocalizedName(){
		if (this.unlocalizedName != null)
			return this.unlocalizedName;
		return "ERROR.BAD.UNLOCALIZED.NAME";
	}

	final public short[] getRGBA(){
		if (this.RGBA != null)
			return this.RGBA;
		return new short[] {255,0,0};
	}

	final public int getRgbAsHex(){

		int returnValue = Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
		if (returnValue == 0){
			return (int) dataVar;
		}		
		return Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
	}

	final public long getProtons() {
		return vProtons;
	}

	final public long getNeutrons() {
		return vNeutrons;
	}

	final public long getMass() {
		return vProtons + vNeutrons;
	}

	final public int getMeltingPointC() {
		return meltingPointC;
	}

	final public int getBoilingPointC() {
		return boilingPointC;
	}

	final public int getMeltingPointK() {
		return meltingPointK;
	}

	final public int getBoilingPointK() {
		return boilingPointK;
	}

	final public boolean requiresBlastFurnace(){
		return usesBlastFurnace;
	}
	
	final public Block getBlock(){
		return Block.getBlockFromItem(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("block"+unlocalizedName, 1).getItem());
	}
	
	final public ItemStack getBlock(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("block"+unlocalizedName, stacksize);
	}

	final public ItemStack getDust(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dust"+unlocalizedName, stacksize);
	}

	final public ItemStack getSmallDust(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustSmall"+unlocalizedName, stacksize);
	}

	final public ItemStack getTinyDust(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustTiny"+unlocalizedName, stacksize);
	}

	final public ItemStack[] getValidInputStacks(){
		return ItemUtils.validItemsForOreDict(unlocalizedName);
	}

	final public ItemStack getIngot(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingot"+unlocalizedName, stacksize);
	}

	final public ItemStack getPlate(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plate"+unlocalizedName, stacksize);
	}

	final public ItemStack getPlateDouble(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDouble"+unlocalizedName, stacksize);
	}

	final public ItemStack getGear(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("gear"+unlocalizedName, stacksize);
	}

	final public ItemStack getRod(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("stick"+unlocalizedName, stacksize);
	}

	final public ItemStack getLongRod(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("stickLong"+unlocalizedName, stacksize);
	}

	final public ItemStack getBolt(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("bolt"+unlocalizedName, stacksize);
	}

	final public ItemStack getScrew(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("screw"+unlocalizedName, stacksize);
	}

	final public ItemStack getRing(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ring"+unlocalizedName, stacksize);
	}

	final public ItemStack getRotor(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("rotor"+unlocalizedName, stacksize);
	}

	final public ItemStack getFrameBox(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("frameGt"+unlocalizedName, stacksize);
	}
	
	final public ItemStack getCell(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+unlocalizedName, stacksize);
	}
	
	final public ItemStack getNugget(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("nugget"+unlocalizedName, stacksize);
	}

	final public ItemStack[] getMaterialComposites(){
		//Utils.LOG_WARNING("Something requested the materials needed for "+localizedName);
		if (vMaterialInput != null){
			if (!vMaterialInput.isEmpty()){
				ItemStack[] temp = new ItemStack[vMaterialInput.size()];
				for (int i=0;i<vMaterialInput.size();i++){
					//Utils.LOG_WARNING("i:"+i);
					ItemStack testNull = null;
					try {
						testNull = vMaterialInput.get(i).getValidStack();
					} catch (Throwable r){
						Utils.LOG_WARNING("Failed gathering material stack for "+localizedName+".");
						Utils.LOG_WARNING("What Failed: Length:"+vMaterialInput.size()+" current:"+i);
					}
					try {
						if (testNull != null){
							//Utils.LOG_WARNING("not null");
							temp[i] = vMaterialInput.get(i).getValidStack();
						}
					} catch (Throwable r){
						Utils.LOG_WARNING("Failed setting slot "+i+", using "+localizedName);
					}
				}		
				return temp;
			}
		}
		return new ItemStack[]{};
	}

	final public ArrayList<MaterialStack> getComposites(){		
		return this.vMaterialInput;
	}

	final public int[] getMaterialCompositeStackSizes(){
		if (!vMaterialInput.isEmpty()){
			int[] temp = new int[vMaterialInput.size()];
			for (int i=0;i<vMaterialInput.size();i++){
				if (vMaterialInput.get(i) != null)
					temp[i] = vMaterialInput.get(i).getDustStack().stackSize;
				else
					temp[i]=0;
			}		
			return temp;
		}
		return new int[]{};
	}


	@SuppressWarnings("static-method")
	final public long[] getSmallestRatio(ArrayList<MaterialStack> tempInput){
		if (tempInput != null){
			if (!tempInput.isEmpty()){
				Utils.LOG_WARNING("length: "+tempInput.size());
				Utils.LOG_WARNING("(inputs != null): "+(tempInput != null));
				//Utils.LOG_WARNING("length: "+inputs.length);
				long[] tempRatio = new long[tempInput.size()];
				for (int x=0;x<tempInput.size();x++){
					//tempPercentage = tempPercentage+inputs[x].percentageToUse;
					//this.mMaterialList.add(inputs[x]);
					if (tempInput.get(x) != null){
						tempRatio[x] = tempInput.get(x).getPartsPerOneHundred();						
					}
				}

				long[] smallestRatio = MathUtils.simplifyNumbersToSmallestForm(tempRatio);

				if (smallestRatio.length > 0){
					String tempRatioStringThing1 = "";
					for (int r=0;r<tempRatio.length;r++){
						tempRatioStringThing1 = tempRatioStringThing1 + tempRatio[r] +" : ";
					}
					Utils.LOG_WARNING("Default Ratio: "+tempRatioStringThing1);

					String tempRatioStringThing = "";
					int tempSmallestCraftingUseSize = 0;
					for (int r=0;r<smallestRatio.length;r++){
						tempRatioStringThing = tempRatioStringThing + smallestRatio[r] +" : ";
						tempSmallestCraftingUseSize = (int) (tempSmallestCraftingUseSize + smallestRatio[r]);
					}
					//this.smallestStackSizeWhenProcessing = tempSmallestCraftingUseSize;
					Utils.LOG_WARNING("Smallest Ratio: "+tempRatioStringThing);
					return smallestRatio;
				}
			}		
		}
		return null;
	}

	@SuppressWarnings("unused")
	final String getToolTip(String chemSymbol, long aMultiplier, boolean aShowQuestionMarks) {
		if (!aShowQuestionMarks && (vChemicalFormula.equals("?")||vChemicalFormula.equals("??"))) return "";
		Utils.LOG_WARNING("===============| Calculating Atomic Formula for "+this.localizedName+" |===============");
		if (!chemSymbol.equals(""))
			return chemSymbol;
		ArrayList<MaterialStack> tempInput = vMaterialInput;
		if (tempInput != null){
			if (!tempInput.isEmpty()){
				String dummyFormula = "";
				long[] dummyFormulaArray = getSmallestRatio(tempInput);
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
									}
									else
										dummyFormula = dummyFormula + "??";
								}
								else
									dummyFormula = dummyFormula + "▓▓";
							}
						}
						return MaterialUtils.subscript(dummyFormula);
						//return dummyFormula;
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

	final Fluid generateFluid(){
		if (Materials.get(localizedName).mFluid == null){
			Utils.LOG_WARNING("Generating our own fluid.");

			//Generate a Cell if we need to
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+getUnlocalizedName(), 1) == null){
				@SuppressWarnings("unused")
				Item temp = new BaseItemCell(this);
			}
			return FluidUtils.addGTFluid(
					this.getUnlocalizedName(),
					"Molten "+this.getLocalizedName(),		
					this.RGBA,
					4,
					this.getMeltingPointK(),
					ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+getUnlocalizedName(), 1),
					ItemList.Cell_Empty.get(1L, new Object[0]),
					1000);
		}
		Utils.LOG_WARNING("Getting the fluid from a GT material instead.");
		return Materials.get(localizedName).mFluid;
	}

	final public FluidStack getFluid(int fluidAmount) {
		Utils.LOG_WARNING("Attempting to get "+fluidAmount+"L of "+this.vMoltenFluid.getName());

		FluidStack moltenFluid = new FluidStack(this.vMoltenFluid, fluidAmount);

		Utils.LOG_WARNING("Info: "+moltenFluid.getFluid().getName()+" Info: "+moltenFluid.amount+" Info: "+moltenFluid.getFluidID());

		//FluidStack moltenFluid = FluidUtils.getFluidStack(this.vMoltenFluid.getName(), fluidAmount);
		/*boolean isNull = (moltenFluid == null);
		if (isNull) Utils.LOG_WARNING("Did not obtain fluid.");
		else Utils.LOG_WARNING("Found fluid.");
		if (isNull){
			return null;
		}*/
		return moltenFluid;
	}































}
