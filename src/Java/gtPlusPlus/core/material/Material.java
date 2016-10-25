package gtPlusPlus.core.material;

import static gregtech.api.enums.GT_Values.M;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
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

	final String unlocalizedName;
	final String localizedName;

	final Fluid vMoltenFluid;

	protected Object dataVar;

	private MaterialStack[] vMaterialInput = new MaterialStack[9];
	public final long[] vSmallestRatio;

	final short[] RGBA;

	final boolean usesBlastFurnace;
	public final boolean isRadioactive;
	public final byte vRadioationLevel;

	final int meltingPointK;
	final int boilingPointK;
	final int meltingPointC;
	final int boilingPointC;
	final long vProtons;
	final long vNeutrons;
	final long vMass;
	public final int smallestStackSizeWhenProcessing; //Add a check for <=0 || > 64
	public final int vTier;
	public final int vVoltageMultiplier;
	public final String vChemicalFormula;
	public final String vChemicalSymbol;

	public Material(String materialName, short[] rgba, int meltingPoint, int boilingPoint, long protons, long neutrons, boolean blastFurnace, MaterialStack[] inputs){		
		this(materialName, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, inputs, "", 0);
	}

	public Material(String materialName, short[] rgba, int meltingPoint, int boilingPoint, long protons, long neutrons, boolean blastFurnace, MaterialStack[] inputs, int radiationLevel){		
		this(materialName, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, inputs, "", radiationLevel);
	}

	public Material(String materialName, short[] rgba, int meltingPoint, int boilingPoint, long protons, long neutrons, boolean blastFurnace, MaterialStack[] inputs, String chemicalSymbol, int radiationLevel){

		this.unlocalizedName = Utils.sanitizeString(materialName);
		this.localizedName = materialName;
		this.RGBA = rgba;
		this.meltingPointC = meltingPoint;
		if (boilingPoint == 0){
			boilingPoint = meltingPoint*4;
		}
		this.boilingPointC = boilingPoint;
		this.meltingPointK = (int) MathUtils.celsiusToKelvin(meltingPointC);
		this.boilingPointK = (int) MathUtils.celsiusToKelvin(boilingPointC);
		this.vProtons = protons;
		this.vNeutrons = neutrons;
		this.vMass = getMass();

		/*//List<MaterialStack> inputArray = Arrays.asList(inputs);
		int tempSmallestSize = getSmallestStackForCrafting(inputs);
		if (tempSmallestSize <= 64 && tempSmallestSize >= 1){
			this.smallestStackSizeWhenProcessing = tempSmallestSize; //Valid stacksizes
		}
		else {
			this.smallestStackSizeWhenProcessing = 50; //Can divide my math by 1/2 and round it~
		}*/

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
		if (getMeltingPoint_K() >= 0 && getMeltingPoint_K() <= 750){
			this.vTier = 1;
		}
		else if(getMeltingPoint_K() >= 751 && getMeltingPoint_K() <= 1250){
			this.vTier = 2;
		}
		else if(getMeltingPoint_K() >= 1251 && getMeltingPoint_K() <= 1750){
			this.vTier = 3;
		}
		else if(getMeltingPoint_K() >= 1751 && getMeltingPoint_K() <= 2250){
			this.vTier = 4;
		}
		else if(getMeltingPoint_K() >= 2251 && getMeltingPoint_K() <= 2750){
			this.vTier = 5;
		}
		else if(getMeltingPoint_K() >= 2751 && getMeltingPoint_K() <= 3250){
			this.vTier = 6;
		}
		else if(getMeltingPoint_K() >= 3251 && getMeltingPoint_K() <= 3750){
			this.vTier = 7;
		}
		else if(getMeltingPoint_K() >= 3751 && getMeltingPoint_K() <= 4250){
			this.vTier = 8;
		}
		else if(getMeltingPoint_K() >= 4251 && getMeltingPoint_K() <= 4750){
			this.vTier = 9;
		}
		else if(getMeltingPoint_K() >= 4751 && getMeltingPoint_K() <= 9999){
			this.vTier = 10;
		}
		else {
			this.vTier = 0;
		}

		this.usesBlastFurnace = blastFurnace;
		this.vVoltageMultiplier = this.getMeltingPoint_K() >= 2800 ? 64 : 16;

		if (inputs == null){
			this.vMaterialInput = null;			
		}
		else {
			if (inputs.length != 0){
				for (int i=0; i < inputs.length; i++){
					if (inputs[i] != null){
						this.vMaterialInput[i] = inputs[i];
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
		
		this.vMoltenFluid = generateFluid();


		dataVar = MathUtils.generateSingularRandomHexValue();

		Utils.LOG_INFO("Creating a Material instance for "+materialName);
		Utils.LOG_INFO("Formula: "+vChemicalFormula + " Smallest Stack: "+smallestStackSizeWhenProcessing+" Smallest Ratio:"+vSmallestRatio);
		Utils.LOG_INFO("Protons: "+vProtons);
		Utils.LOG_INFO("Neutrons: "+vNeutrons);
		Utils.LOG_INFO("Mass: "+vMass+"/units");
		Utils.LOG_INFO("Melting Point: "+meltingPointC+"C.");
		Utils.LOG_INFO("Boiling Point: "+boilingPointC+"C.");
	}

	public String getLocalizedName(){
		return localizedName;
	}

	public String getUnlocalizedName(){
		return unlocalizedName;
	}

	public short[] getRGBA(){
		return RGBA;
	}

	public int getRgbAsHex(){

		int returnValue = Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
		if (returnValue == 0){
			return (int) dataVar;
		}		
		return Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
	}

	public long getProtons() {
		return vProtons;
	}

	public long getNeutrons() {
		return vNeutrons;
	}

	public long getMass() {
		return vProtons + vNeutrons;
	}

	public int getMeltingPoint_C() {
		return meltingPointC;
	}

	public int getBoilingPoint_C() {
		return boilingPointC;
	}

	public int getMeltingPoint_K() {
		return meltingPointK;
	}

	public int getBoilingPoint_K() {
		return boilingPointK;
	}

	public boolean requiresBlastFurnace(){
		return usesBlastFurnace;
	}

	public ItemStack getDust(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dust"+unlocalizedName, stacksize);
	}

	public ItemStack getSmallDust(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustSmall"+unlocalizedName, stacksize);
	}

	public ItemStack getTinyDust(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustTiny"+unlocalizedName, stacksize);
	}

	public ItemStack[] getValidInputStacks(){
		return ItemUtils.validItemsForOreDict(unlocalizedName);
	}

	public ItemStack getIngot(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingot"+unlocalizedName, stacksize);
	}

	public ItemStack getPlate(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plate"+unlocalizedName, stacksize);
	}

	public ItemStack getPlateDouble(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDouble"+unlocalizedName, stacksize);
	}

	public ItemStack getGear(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("gear"+unlocalizedName, stacksize);
	}

	public ItemStack getRod(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("stick"+unlocalizedName, stacksize);
	}

	public ItemStack getLongRod(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("stickLong"+unlocalizedName, stacksize);
	}

	public ItemStack getBolt(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("bolt"+unlocalizedName, stacksize);
	}

	public ItemStack getScrew(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("screw"+unlocalizedName, stacksize);
	}

	public ItemStack getRing(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ring"+unlocalizedName, stacksize);
	}

	public ItemStack getRotor(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("rotor"+unlocalizedName, stacksize);
	}

	public ItemStack getFrameBox(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("frameGt"+unlocalizedName, stacksize);
	}

	public ItemStack[] getMaterialComposites(){
		//Utils.LOG_INFO("Something requested the materials needed for "+localizedName);
		if (vMaterialInput != null && vMaterialInput.length >= 1){
			ItemStack[] temp = new ItemStack[vMaterialInput.length];
			for (int i=0;i<vMaterialInput.length;i++){
				//Utils.LOG_INFO("i:"+i);
				ItemStack testNull = null;
				try {
					testNull = vMaterialInput[i].getDustStack();
				} catch (Throwable r){
					Utils.LOG_WARNING("Failed gathering material stack for "+localizedName+".");
					Utils.LOG_WARNING("What Failed: Length:"+vMaterialInput.length+" current:"+i);
				}
				try {
					if (testNull != null){
						//Utils.LOG_INFO("not null");
						temp[i] = vMaterialInput[i].getDustStack();
					}
				} catch (Throwable r){
					Utils.LOG_WARNING("Failed setting slot "+i+", using "+localizedName);
				}
			}		
			return temp;
		}
		return new ItemStack[]{};
	}

	public MaterialStack[] getComposites(){		
		return this.vMaterialInput;
	}

	public int[] getMaterialCompositeStackSizes(){
		if (vMaterialInput != null && vMaterialInput.length >= 1){
			int[] temp = new int[vMaterialInput.length];
			for (int i=0;i<vMaterialInput.length;i++){
				if (vMaterialInput[i] != null)
					temp[i] = vMaterialInput[i].getDustStack().stackSize;
				else
					temp[i]=0;
			}		
			return temp;
		}
		return new int[]{};
	}




	private int getInputMaterialCount(MaterialStack[] materialInput){
		int i = 0;
		for (int r=0;r<4;r++){
			try {
				if (!materialInput[r].equals(null)){
					i++;
				}
			} catch(Throwable x){
				return i;
			}
		}
		return i;
	}


	public String getChemicalFormula(MaterialStack[] materialInput){
		if (materialInput != null && materialInput.length >= 1){
			int f = getInputMaterialCount(materialInput);
			String[] formulaComponents = new String[f];
			for (int i=0;i<f;i++){
				try {		
					if (materialInput[i] != null){					
						formulaComponents[i] = materialInput[i].stackMaterial.vChemicalFormula;	
						Utils.LOG_WARNING("LOOK AT ME IN THE LOG - " + formulaComponents[i]);	
					}
					else{
						Utils.LOG_WARNING("LOOK AT ME IN THE LOG - materialInput[i] was null");
					}
				} catch (Throwable e){
					Utils.LOG_WARNING("LOOK AT ME IN THE LOG - got an error");
					return "??";
				}				
			}

			String properName = "";
			for (int r = 0; r < f; r++){
				properName = properName + formulaComponents[r];
				Utils.LOG_WARNING("LOOK AT ME IN THE LOG - "+properName);
			}
			if (!properName.equals(""))
				return properName;

		}
		return "??";

	}

	public long[] getSmallestRatio(MaterialStack[] inputs){
		if (inputs != null){
			if (inputs.length > 0){
				Utils.LOG_WARNING("length: "+inputs.length);
				Utils.LOG_WARNING("(inputs != null): "+(inputs != null));
				//Utils.LOG_INFO("length: "+inputs.length);
				double tempPercentage=0;
				long[] tempRatio = new long[inputs.length];
				for (int x=0;x<inputs.length;x++){
					//tempPercentage = tempPercentage+inputs[x].percentageToUse;
					//this.mMaterialList.add(inputs[x]);
					if (inputs[x] != null){
						tempRatio[x] = inputs[x].getPartsPerOneHundred();						
					}
				}
				//Check if % of added materials equals roughly 100%
				/*if (tempPercentage <= 95 || tempPercentage >= 101){
					Utils.LOG_INFO("The compound for "+localizedName+" doesn't equal 98-100%, this isn't good.");
				}*/

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

	private int getSmallestStackForCrafting(MaterialStack[] inputs){
		if (inputs != null){
			if (inputs.length != 0){
				long[] smallestRatio = getSmallestRatio(inputs);
				if (smallestRatio.length > 0){
					int tempSmallestCraftingUseSize = 0;
					for (int r=0;r<smallestRatio.length;r++){
						tempSmallestCraftingUseSize = (int) (tempSmallestCraftingUseSize + smallestRatio[r]);
					}
					return tempSmallestCraftingUseSize;
				}
			}		
		}
		return 1;
	}


	public String getToolTip(String chemSymbol, long aMultiplier, boolean aShowQuestionMarks) {
		if (!aShowQuestionMarks && (vChemicalFormula.equals("?")||vChemicalFormula.equals("??"))) return "";
		Utils.LOG_WARNING("===============| Calculating Atomic Formula for "+this.localizedName+" |===============");
		if (!chemSymbol.equals(""))
			return chemSymbol;
		MaterialStack[] tempInput = vMaterialInput;
		if (tempInput != null){
			if (tempInput.length >= 1){
				String dummyFormula = "";
				long[] dummyFormulaArray = getSmallestRatio(tempInput);
				if (dummyFormulaArray != null){
					if (dummyFormulaArray.length >= 1){
						for (int e=0;e<tempInput.length;e++){
							if (tempInput[e] != null){
								if (!tempInput[e].stackMaterial.vChemicalSymbol.equals("??")){
									if (dummyFormulaArray[e] > 1){
										dummyFormula = dummyFormula + tempInput[e].stackMaterial.vChemicalSymbol + dummyFormulaArray[e];
									}
									else if (dummyFormulaArray[e] == 1){
										dummyFormula = dummyFormula + tempInput[e].stackMaterial.vChemicalSymbol;
									}
									else if (dummyFormulaArray[e] <= 0){
										dummyFormula = dummyFormula+"";
									}

								}else
									dummyFormula = dummyFormula + "??";
							}
							else {
								dummyFormula = dummyFormula+"";
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
	
	Fluid generateFluid(){
		if (Materials.get(localizedName).mFluid == null){
			Utils.LOG_INFO("Generating our own fluid.");
			
			//Generate a Cell if we need to
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+getUnlocalizedName(), 1) == null){
				Item temp = new BaseItemCell(this);
			}
			return FluidUtils.addGTFluid(
					this.getUnlocalizedName(),
					"Molten "+this.getLocalizedName(),		
					this.RGBA,
					4,
					this.getMeltingPoint_K(),
					ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+getUnlocalizedName(), 1),
					ItemList.Cell_Empty.get(1L, new Object[0]),
					1000);
		}
		Utils.LOG_INFO("Getting the fluid from a GT material instead.");
		return Materials.get(localizedName).mFluid;
	}

	public FluidStack getFluid(int fluidAmount) {
		Utils.LOG_INFO("Attempting to get "+fluidAmount+"L of "+this.vMoltenFluid.getName());

		FluidStack moltenFluid = new FluidStack(this.vMoltenFluid, fluidAmount);
		
		Utils.LOG_INFO("Info: "+moltenFluid.getFluid().getName()+" Info: "+moltenFluid.amount+" Info: "+moltenFluid.getFluidID());
		
		//FluidStack moltenFluid = FluidUtils.getFluidStack(this.vMoltenFluid.getName(), fluidAmount);
		/*boolean isNull = (moltenFluid == null);
		if (isNull) Utils.LOG_INFO("Did not obtain fluid.");
		else Utils.LOG_INFO("Found fluid.");
		if (isNull){
			return null;
		}*/
		return moltenFluid;
	}































}
