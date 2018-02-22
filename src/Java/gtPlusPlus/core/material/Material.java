package gtPlusPlus.core.material;

import static gregtech.api.enums.GT_Values.M;

import java.util.*;
import java.util.Map.Entry;

import gregtech.api.enums.*;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.item.base.cell.BaseItemCell;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.ArrayUtils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import scala.xml.dtd.ELEMENTS;

public class Material {

	private String unlocalizedName;
	private String localizedName;

	private MaterialState materialState;
	private TextureSet textureSet;

	public synchronized final TextureSet getTextureSet() {
		return textureSet;
	}

	private Fluid vMoltenFluid;
	private Fluid vPlasma;

	private boolean vGenerateCells;

	protected Object dataVar = MathUtils.generateSingularRandomHexValue();

	private ArrayList<MaterialStack> vMaterialInput = new ArrayList<>();
	public long[] vSmallestRatio;
	public short vComponentCount;

	private short[] RGBA;

	private boolean usesBlastFurnace;
	public boolean isRadioactive;
	public byte vRadiationLevel;

	private int meltingPointK;
	private int boilingPointK;
	private int meltingPointC;
	private int boilingPointC;
	private long vProtons;
	private long vNeutrons;
	private long vMass;
	public int smallestStackSizeWhenProcessing; //Add a check for <=0 || > 64
	public int vTier;
	public int vVoltageMultiplier;
	public String vChemicalFormula;
	public String vChemicalSymbol;

	public long vDurability;
	public int vToolQuality;
	public int vHarvestLevel;


	public static Map<Integer, Materials> invalidMaterials = new HashMap<Integer, Materials>();

	public Material(String materialName, MaterialState defaultState, short[] rgba, int radiationLevel, MaterialStack[] materialStacks) {
		this(materialName, defaultState, null, 0, rgba, -1, -1, -1, -1, false, "", radiationLevel, false, materialStacks);
	}	

	public Material(String materialName, MaterialState defaultState, short[] rgba, int j, int k, int l, int m, int radiationLevel, MaterialStack[] materialStacks){
		this(materialName, defaultState, null, 0, rgba, j, k, l, m, false, "", radiationLevel, false, materialStacks);
	}
	
	public Material(String materialName, MaterialState defaultState, final TextureSet set, short[] rgba, int j, int k, int l, int m, int radiationLevel, MaterialStack[] materialStacks){
		this(materialName, defaultState, set, 0, rgba, j, k, l, m, false, "", radiationLevel, false, materialStacks);
	}

	public Material(final String materialName, final MaterialState defaultState,final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final MaterialStack... inputs){
		this(materialName, defaultState, 0, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", 0, inputs);
	}

	public Material(final String materialName, final MaterialState defaultState, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, boolean generateCells, final MaterialStack... inputs){
		this(materialName, defaultState, null, 0, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", 0, generateCells, inputs);
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

	public Material(final String materialName, final MaterialState defaultState,final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel, boolean addCells,final MaterialStack... inputs) {
		this (materialName, defaultState, null, 0, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, chemicalSymbol, radiationLevel, addCells, inputs);
	}

	public Material(final String materialName, final MaterialState defaultState, final long durability, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel, final MaterialStack... inputs){
		this (materialName, defaultState, null, durability, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, chemicalSymbol, radiationLevel, true, inputs);
	}

	public Material(final String materialName, final MaterialState defaultState, final TextureSet set, final long durability, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel, boolean generateCells, final MaterialStack... inputs){
		try {
			this.unlocalizedName = Utils.sanitizeString(materialName);
			this.localizedName = materialName;

			this.materialState = defaultState;

			Logger.MATERIALS(this.getLocalizedName()+" is "+defaultState.name()+".");

			this.RGBA = rgba;
			this.vGenerateCells = generateCells;

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
			this.textureSet = setTextureSet(set);


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
			if (radiationLevel > 0){
				Logger.MATERIALS(this.getLocalizedName()+" is radioactive. Level: "+radiationLevel+".");
				this.isRadioactive = true;
				this.vRadiationLevel = (byte) radiationLevel;
			}
			else {
				Logger.MATERIALS(this.getLocalizedName()+" is not radioactive.");
				this.isRadioactive = false;
				this.vRadiationLevel = 0;
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
				Logger.INFO("materialInput is null, using a valid chemical symbol.");
				this.vChemicalFormula = this.vChemicalSymbol;
			}
			else{
				Logger.INFO("MaterialInput == null && chemicalSymbol probably equals nothing");
				this.vChemicalFormula = "??";
			}


			final Materials isValid = Materials.get(this.getLocalizedName());
			if (FluidUtils.getFluidStack(localizedName, 1) != null){
				this.vMoltenFluid = FluidUtils.getFluidStack(localizedName, 1).getFluid();
			}
			else if (isValid == null || isValid == Materials._NULL){
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

			Logger.INFO("Creating a Material instance for "+materialName);
			Logger.INFO("Formula: "+this.vChemicalFormula + " Smallest Stack: "+this.smallestStackSizeWhenProcessing+" Smallest Ratio:"+ratio);
			Logger.INFO("Protons: "+this.vProtons);
			Logger.INFO("Neutrons: "+this.vNeutrons);
			Logger.INFO("Mass: "+this.vMass+"/units");
			Logger.INFO("Melting Point: "+this.meltingPointC+"C.");
			Logger.INFO("Boiling Point: "+this.boilingPointC+"C.");
		}
		catch (Throwable t){
			t.printStackTrace();
		}
	}

	private TextureSet setTextureSet(TextureSet set) {
		if (set != null) {
			 return set;			
		}
		else {
			// build hash table with count
			AutoMap<Material> sets = new AutoMap<Material>();
			if (this.vMaterialInput != null) {				
				for (MaterialStack r : this.vMaterialInput) {
					if (r.getStackMaterial().getTextureSet().mSetName.toLowerCase().contains("fluid")) {
						sets.put(ELEMENT.getInstance().GOLD);
					}
					else {
						sets.put(r.getStackMaterial());
					}
				}				
				TextureSet mostUsedTypeTextureSet = (TextureSet) MaterialUtils.getMostCommonTextureSet(new ArrayList<Material>(sets.values()));		        
				if (mostUsedTypeTextureSet != null && mostUsedTypeTextureSet instanceof TextureSet) {
					Logger.MATERIALS("Set textureset for "+this.localizedName+" to be "+mostUsedTypeTextureSet.mSetName+".");
					return mostUsedTypeTextureSet;
				}		        
			}
		}
		Logger.MATERIALS("Set textureset for "+this.localizedName+" to be "+Materials.Iron.mIconSet.mSetName+". [Fallback]");
		return Materials.Iron.mIconSet;		
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

	final public short[] getRGB(){
		if (this.RGBA != null) {
			return this.RGBA;
		}
		return new short[] {255,0,0};
	}

	final public short[] getRGBA(){
		if (this.RGBA != null) {
			if (this.RGBA.length == 4){
				return this.RGBA;				
			}
			else {
				return new short[]{this.RGBA[0], this.RGBA[1], this.RGBA[2], 0};
			}
		}
		return new short[] {255,0,0, 0};
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
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("gearGt"+this.unlocalizedName, stacksize);
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

	/**
	 * Ore Components
	 * @return
	 */

	public final ItemStack getOre(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ore"+Utils.sanitizeString(this.getUnlocalizedName()), stacksize);
	}
	public final Block getOreBlock(final int stacksize){
		Logger.DEBUG_MATERIALS("Trying to get ore block for "+this.getLocalizedName()+". Looking for '"+"ore"+Utils.sanitizeString(this.getUnlocalizedName())+"'.");
		try{
			ItemStack a1 = getOre(1);
			Item a2 = a1.getItem();
			Block a3 = Block.getBlockFromItem(a2);

			Logger.DEBUG_MATERIALS("[Invalid Ore] Is a1 valid? "+(a1 != null));
			Logger.DEBUG_MATERIALS("[Invalid Ore] Is a2 valid? "+(a2 != null));
			Logger.DEBUG_MATERIALS("[Invalid Ore] Is a3 valid? "+(a3 != null));

			Block x = Block.getBlockFromItem(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ore"+Utils.sanitizeString(this.unlocalizedName), stacksize).getItem());
			if (x != null){
				return x;
			}
		}
		catch (Throwable t){
			//t.printStackTrace();
		}
		Logger.MATERIALS("Failed getting the Ore Block for "+this.getLocalizedName()+".");
		return Blocks.stone;
	}
	public final ItemStack getCrushed(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("crushed"+this.unlocalizedName, stacksize);
	}
	public final ItemStack getCrushedPurified(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("crushedPurified"+this.unlocalizedName, stacksize);
	}
	public final ItemStack getCrushedCentrifuged(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("crushedCentrifuged"+this.unlocalizedName, stacksize);
	}	
	public final ItemStack getDustPurified(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustPure"+this.unlocalizedName, stacksize);
	}
	public final ItemStack getDustImpure(final int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustImpure"+this.unlocalizedName, stacksize);
	}

	final public ItemStack[] getMaterialComposites(){
		if (this.vMaterialInput != null){
			if (!this.vMaterialInput.isEmpty()){
				final ItemStack[] temp = new ItemStack[this.vMaterialInput.size()];
				for (int i=0;i<this.vMaterialInput.size();i++){
					//Utils.LOG_INFO("i:"+i);
					ItemStack testNull = null;
					try {
						testNull = this.vMaterialInput.get(i).getValidStack();
					} catch (final Throwable r){
						Logger.INFO("Failed gathering material stack for "+this.localizedName+".");
						Logger.INFO("What Failed: Length:"+this.vMaterialInput.size()+" current:"+i);
					}
					try {
						if (testNull != null){
							//Utils.LOG_INFO("not null");
							temp[i] = this.vMaterialInput.get(i).getValidStack();
						}
					} catch (final Throwable r){
						Logger.INFO("Failed setting slot "+i+", using "+this.localizedName);
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
		
		if (inputs == null || inputs.length < 1) {
			return 1;
		}
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


	public final long[] getSmallestRatio(final ArrayList<MaterialStack> tempInput){
		if (tempInput != null){
			if (!tempInput.isEmpty()){
				Logger.INFO("length: "+tempInput.size());
				Logger.INFO("(inputs != null): "+(tempInput != null));
				//Utils.LOG_INFO("length: "+inputs.length);
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
					Logger.INFO("Default Ratio: "+tempRatioStringThing1);

					String tempRatioStringThing = "";
					int tempSmallestCraftingUseSize = 0;
					for (int r=0;r<smallestRatio.length;r++){
						tempRatioStringThing = tempRatioStringThing + smallestRatio[r] +" : ";
						tempSmallestCraftingUseSize = (int) (tempSmallestCraftingUseSize + smallestRatio[r]);
					}
					//this.smallestStackSizeWhenProcessing = tempSmallestCraftingUseSize;
					Logger.INFO("Smallest Ratio: "+tempRatioStringThing);
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
		Logger.INFO("===============| Calculating Atomic Formula for "+this.localizedName+" |===============");
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
					Logger.INFO("dummyFormulaArray <= 0");
				}
				Logger.INFO("dummyFormulaArray == null");
			}
			Logger.INFO("tempInput.length <= 0");
		}
		Logger.INFO("tempInput == null");
		return "??";

	}

	public final Fluid generateFluid(){
		if (this.materialState == MaterialState.ORE){
			return null;
		}

		final Materials isValid = Materials.get(this.getLocalizedName());
		//Logger.MATERIALS("Is "+this.getLocalizedName()+" a Gregtech material? "+(isValid != null && isValid != Materials._NULL)+" | Found "+isValid.mDefaultLocalName);
		if (isValid != Materials._NULL){
			for (Materials m : invalidMaterials.values()){
				if (isValid == m){
					Logger.INFO("Trying to generate a fluid for blacklisted material: "+m.mDefaultLocalName);
					FluidStack a1 = m.getFluid(1);
					FluidStack a2 = m.getGas(1);
					FluidStack a3 = m.getMolten(1);
					FluidStack a4 = m.getSolid(1);
					FluidStack a5 = m.getPlasma(1);
					if (a1 != null){
						Logger.INFO("Using a pre-defined Fluid from GT. Fluid.");
						return a1.getFluid();
					}
					if (a2 != null){
						Logger.INFO("Using a pre-defined Fluid from GT. Gas.");
						return a2.getFluid();
					}
					if (a3 != null){
						Logger.INFO("Using a pre-defined Fluid from GT. Molten.");
						return a3.getFluid();
					}
					if (a4 != null){
						Logger.INFO("Using a pre-defined Fluid from GT. Solid.");
						return a4.getFluid();
					}
					if (a5 != null){
						Logger.INFO("Using a pre-defined Fluid from GT. Plasma.");
						return a5.getFluid();
					}			
					Logger.INFO("Using null.");	
					return null;
				}
			}
		}

		if (this.materialState == MaterialState.SOLID){
			if (isValid.mFluid != null){
				Logger.INFO("Using a pre-defined Fluid from GT. mFluid.");
				return isValid.mFluid;			
			}	
			else if (isValid.mStandardMoltenFluid != null){
				Logger.INFO("Using a pre-defined Fluid from GT. mStandardMoltenFluid.");
				return isValid.mStandardMoltenFluid;		
			}
		}
		else if (this.materialState == MaterialState.GAS){
			if (isValid.mGas != null){
				Logger.INFO("Using a pre-defined Fluid from GT. mGas.");
				return isValid.mGas;			
			}	
		}
		else if (this.materialState == MaterialState.LIQUID || this.materialState == MaterialState.PURE_LIQUID){
			if (isValid.mFluid != null){
				Logger.INFO("Using a pre-defined Fluid from GT. mFluid.");
				return isValid.mFluid;			
			}	
			else if (isValid.mGas != null){
				Logger.INFO("Using a pre-defined Fluid from GT. mGas.");
				return isValid.mGas;			
			}
			else if (isValid.mStandardMoltenFluid != null){
				Logger.INFO("Using a pre-defined Fluid from GT. mStandardMoltenFluid.");
				return isValid.mStandardMoltenFluid;		
			}
		}		

		Logger.INFO("Generating our own fluid.");
		//Generate a Cell if we need to
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+this.getUnlocalizedName(), 1) == null){			
			if (this.vGenerateCells){
				new BaseItemCell(this);
				Logger.INFO("Generated a cell for "+this.getUnlocalizedName());
			}
			else {
				Logger.INFO("Did not generate a cell for "+this.getUnlocalizedName());
			}
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

	public final Fluid generatePlasma(){
		if (this.materialState == MaterialState.ORE){
			return null;
		}
		final Materials isValid = Materials.get(this.getLocalizedName());		

		if (!this.vGenerateCells){
			return null;
		}
		for (Materials m : invalidMaterials.values()){
			if (isValid == m){
				return (m.mPlasma != null ? m.mPlasma : null);
			}
		}
		if (isValid.mPlasma != null){
			Logger.INFO("Using a pre-defined Plasma from GT.");
			return isValid.mPlasma;			
		}	

		Logger.INFO("Generating our own Plasma.");
		return FluidUtils.addGTPlasma(this);
	}



	final public FluidStack getFluid(final int fluidAmount) {		
		if (this.vMoltenFluid == null){
			return null;
		}		
		final FluidStack moltenFluid = new FluidStack(this.vMoltenFluid, fluidAmount);
		return moltenFluid;
	}


	final public int calculateMeltingPoint(){
		try {
			int meltingPoint = 0;
			for (MaterialStack  part : this.vMaterialInput){
				if (part != null){
					int incrementor = part.getStackMaterial().getMeltingPointC();
					meltingPoint += incrementor;
					Logger.INFO("Melting Point for "+this.getLocalizedName()+" increased to "+ incrementor);
				}
				else {
					Logger.MATERIALS(this.getLocalizedName()+" has a really invalid composition.");
				}
			}
			int divisor = (this.vMaterialInput.size()>0 ? this.vMaterialInput.size() : 1);
			Logger.INFO("Dividing "+meltingPoint+" / "+divisor+" to get average melting point.");
			meltingPoint = (meltingPoint/divisor);
			return meltingPoint;
		}
		catch (Throwable r){
			return 500;
		}
	}

	final public int calculateBoilingPoint(){
		try {
			int boilingPoint = 0;
			for (MaterialStack  part : this.vMaterialInput){
				if (part != null){
					boilingPoint += part.getStackMaterial().getBoilingPointC();
				}
				else {
					Logger.MATERIALS(this.getLocalizedName()+" has a really invalid composition.");
				}
			}
			int divisor = (this.vMaterialInput.size()>0 ? this.vMaterialInput.size() : 1);
			boilingPoint = (boilingPoint/divisor);
			return boilingPoint;
		}
		catch (Throwable r){
			return 2500;
		}
	}

	final public long calculateProtons(){
		try {
			long protonCount = 0;
			for (MaterialStack  part : this.vMaterialInput){
				if (part != null){
					protonCount += (part.getStackMaterial().getProtons());
				}
				else {
					Logger.MATERIALS(this.getLocalizedName()+" has a really invalid composition.");
				}
			}
			int divisor = (this.vMaterialInput.size()>0 ? this.vMaterialInput.size() : 1);
			protonCount = (protonCount/divisor);
			return protonCount;
		}
		catch (Throwable r){
			return 50;
		}
	}

	final public long calculateNeutrons(){
		try {
			long neutronCount = 0;
			for (MaterialStack  part : this.vMaterialInput){
				if (part != null){
					neutronCount += (part.getStackMaterial().getNeutrons());
				}
				else {
					Logger.MATERIALS(this.getLocalizedName()+" has a really invalid composition.");
				}
			}
			int divisor = (this.vMaterialInput.size()>0 ? this.vMaterialInput.size() : 1);
			neutronCount = (neutronCount/divisor);
			return neutronCount;
		}
		catch (Throwable r){
			return 75;
		}
	}

}
