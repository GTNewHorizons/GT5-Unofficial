package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.bedrock;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.ORES;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MiningUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public abstract class GregtechMetaTileEntity_BedrockMiningPlatformBase extends GregtechMeta_MultiBlockBase {

	protected double mProductionModifier = 0;


	private static final ItemStack miningPipe;
	private static final ItemStack miningPipeTip;
	
	private Block casingBlock;
	private int casingMeta;
	// private int frameMeta;
	private int casingTextureIndex;

	private ForgeDirection back;

	private int xDrill;
	private int yDrill;
	private int zDrill;

	private int[] xCenter = new int[5];
	private int[] zCenter = new int[5];

	public GregtechMetaTileEntity_BedrockMiningPlatformBase(final int aID, final String aName,
			final String aNameRegional) {
		super(aID, aName, aNameRegional);
		this.initFields();
	}

	public GregtechMetaTileEntity_BedrockMiningPlatformBase(final String aName) {
		super(aName);
		this.initFields();
	}

	private void initFields() {
		this.casingBlock = this.getCasingBlockItem().getBlock();
		this.casingMeta = this.getCasingBlockItem().get(0L, new Object[0]).getItemDamage();
		this.casingTextureIndex = this.getCasingTextureIndex();
	}

	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] { Textures.BlockIcons.CASING_BLOCKS[this.casingTextureIndex],
					new GT_RenderedTexture(
							(IIconContainer) (aActive ? Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE
									: Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT)) };
		}
		return new ITexture[] { Textures.BlockIcons.CASING_BLOCKS[this.casingTextureIndex] };
	}

	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"OreDrillingPlant.png");
	}
	

	public int getAmountOfOutputs() {
		return 1;
	}

	public void saveNBTData(final NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setDouble("mProductionModifier", mProductionModifier);
	}

	public void loadNBTData(final NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		this.mProductionModifier = aNBT.getDouble("mProductionModifier");
	}

	public boolean checkRecipe(final ItemStack aStack) {
		//this.setElectricityStats();

		boolean[] didWork = new boolean[5];
		
		if (!this.tryConsumeDrillingFluid()) {
			Logger.INFO("No drilling Fluid.");
			return false;
		}
		
		if (MathUtils.isNumberEven((int) this.mProductionModifier)) {
			if (!this.tryConsumePyrotheum()) {
				Logger.INFO("No tryConsumePyrotheum Fluid.");
				return false;
			}
			else {
				mProductionModifier++;						
			}
		}
		else {
			if (!this.tryConsumeCryotheum()) {
				Logger.INFO("No tryConsumeCryotheum Fluid.");
				return false;
			}
			else {
				mProductionModifier++;					
			}
		}
		
		for (int i = 0; i < 5; i++) {			
			process();						
			didWork[i] = true;		
		}

		// Fail recipe handling if one pipe didn't handle properly, to try again
		// next run.
		for (boolean y : didWork) {
			if (!y) {
				Logger.INFO("[Bedrock Miner] Fail [x]");
				return false;
			}
		}
		
		this.mEUt = -8000;
		this.mMaxProgresstime = 1;
		this.mEfficiencyIncrease = 10000;
		
		return true;
	}

	private boolean isEnergyEnough() {
		long requiredEnergy = 512L + this.getMaxInputVoltage() * 4L;
		for (final GT_MetaTileEntity_Hatch_Energy energyHatch : this.mEnergyHatches) {
			requiredEnergy -= energyHatch.getEUVar();
			if (requiredEnergy <= 0L) {
				return true;
			}
		}
		return false;
	}

	private void setElectricityStats() {
		//this.mEfficiency = this.getCurrentEfficiency((ItemStack) null);
		this.mEfficiencyIncrease = 10000;
		final int overclock = 8 << GT_Utility.getTier(this.getMaxInputVoltage());
		//this.mEUt = -12 * overclock * overclock;
		Logger.INFO("Trying to set EU to "+(12 * overclock * overclock));
		int mCombinedAvgTime = 0;
		for (int g = 0; g < 5; g++) {
			mCombinedAvgTime += this.getBaseProgressTime() / overclock;
		}
		Logger.INFO("Trying to set Max Time to "+(mCombinedAvgTime));
		//this.mMaxProgresstime = (mCombinedAvgTime / 5);
	}

	private boolean tryConsumeDrillingFluid() {
		boolean consumed = false;
		boolean g = (this.getBaseMetaTileEntity().getWorld().getTotalWorldTime() % 2 == 0);
		consumed = (g ? tryConsumePyrotheum() : tryConsumeCryotheum());
		if (consumed) {
			//increaseProduction(g ? 2 : 1);
		}
		else {
			//lowerProduction(g ? 5 : 3);
		}
		return consumed;
	}

	private boolean tryConsumePyrotheum() {
		return this.depleteInput(FluidUtils.getFluidStack("pyrotheum", 4));
	}

	private boolean tryConsumeCryotheum() {
		return this.depleteInput(FluidUtils.getFluidStack("cryotheum", 4));
	}

	private void putMiningPipesFromInputsInController() {
		final int maxPipes = 64;
		if (this.isHasMiningPipes(maxPipes)) {
			return;
		}
		ItemStack pipes = this.getStackInSlot(1);
		for (final ItemStack storedItem : this.getStoredInputs()) {
			if (!storedItem.isItemEqual(GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipe)) {
				continue;
			}
			if (pipes == null) {
				this.setInventorySlotContents(1,
						GT_Utility.copy(new Object[] { GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipe }));
				pipes = this.getStackInSlot(1);
			}
			if (pipes.stackSize == maxPipes) {
				break;
			}
			final int needPipes = maxPipes - pipes.stackSize;
			final int transferPipes = (storedItem.stackSize < needPipes) ? storedItem.stackSize : needPipes;
			final ItemStack itemStack = pipes;
			itemStack.stackSize += transferPipes;
			final ItemStack itemStack2 = storedItem;
			itemStack2.stackSize -= transferPipes;
		}
		this.updateSlots();
	}



	private boolean isHasMiningPipes(final int minCount) {
		final ItemStack pipe = this.getStackInSlot(1);
		return pipe != null && pipe.stackSize > minCount - 1
				&& pipe.isItemEqual(GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipe);
	}

	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		this.updateCoordinates();		
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		int tAmount = 0;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		} else {
			
			Block aCasing = Block.getBlockFromItem(getCasingBlockItem().getItem());
			
			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					for (int h = -1; h < 2; ++h) {
						if (h != 0 || (xDir + i != 0 || zDir + j != 0) && (i != 0 || j != 0)) {
							IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i,
									h, zDir + j);
							Block aBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							int aMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);

							if (!this.addToMachineList(tTileEntity, 48)) {
								if (aBlock != aCasing) {
									Logger.INFO("Found Bad Casing");
									return false;
								}
								if (aMeta != 3) {
									Logger.INFO("Found Bad Meta");
									return false;
								}
							}
							++tAmount;
							
							
							
							/*if (!isValidBlockForStructure(tTileEntity, 48, true, aBlock, aMeta,	sBlockCasings4, 0)) {
								Logger.INFO("Bad centrifuge casing");
								return false;
							}*/

						}
					}
				}
			}
			return tAmount >= 10;
		}
	
	}

	private void updateCoordinates() {
		this.xDrill = this.getBaseMetaTileEntity().getXCoord();
		this.yDrill = this.getBaseMetaTileEntity().getYCoord()-1;
		this.zDrill = this.getBaseMetaTileEntity().getZCoord();
		this.back = ForgeDirection.getOrientation((int) this.getBaseMetaTileEntity().getBackFacing());

		// Middle
		this.xCenter[0] = this.xDrill + this.back.offsetX;
		this.zCenter[0] = this.zDrill + this.back.offsetZ;

		this.xCenter[1] = xCenter[0] + 1;
		this.zCenter[1] = zCenter[0];

		this.xCenter[2] = xCenter[0] - 1;
		this.zCenter[2] = zCenter[0];

		this.xCenter[3] = xCenter[0];
		this.zCenter[3] = zCenter[0] + 1;

		this.xCenter[4] = xCenter[0];
		this.zCenter[4] = zCenter[0] - 1;

	}

	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	protected GregtechItemList getCasingBlockItem() {
		return GregtechItemList.Casing_BedrockMiner;
	}

	protected abstract Material getFrameMaterial();

	protected abstract int getCasingTextureIndex();

	protected abstract int getRadiusInChunks();

	protected abstract int getMinTier();

	protected abstract int getBaseProgressTime();

	protected String[] getDescriptionInternal(final String tierSuffix) {
		final String casings = this.getCasingBlockItem().get(0L, new Object[0]).getDisplayName();
		return new String[] {
				"Controller Block for the Experimental Deep Earth Drilling Platform - MK "
						+ ((tierSuffix != null) ? tierSuffix : ""),
				"Size(WxHxD): 3x7x3, Controller (Front middle bottom)", "3x1x3 Base of " + casings,
				"1x3x1 " + casings + " pillar (Center of base)",
				"1x3x1 " + this.getFrameMaterial().getLocalizedName() + " Frame Boxes (Each pillar side and on top)",
				"2x Input Hatch (Any bottom layer casing)",
				"1x Input Bus for mining pipes (Any bottom layer casing; not necessary)",
				"1x Output Bus (Any bottom layer casing)", "1x Maintenance Hatch (Any bottom layer casing)",
				"1x " + GT_Values.VN[this.getMinTier()] + "+ Energy Hatch (Any bottom layer casing)",
				"Radius is " + (this.getRadiusInChunks() << 4) + " blocks",
				"Every tick, this machine altenates betweem consumption of Pyrotheum & Cryotheum",
				"Pyrotheum is used to bore through the Mantle of the world",
				"Cryotheum is used to keep the internal components cool",};
	}

	static {
		miningPipe = GT_ModHandler.getIC2Item("miningPipe", 0L);
		miningPipeTip = GT_ModHandler.getIC2Item("miningPipeTip", 0L);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private AutoMap<ItemStack> mOutputs;
	
	public void process() {
		ItemStack aOutput = generateOutputWithchance();
		if (aOutput != null) {
			this.addOutput(aOutput);
			Logger.INFO("Mined some "+aOutput.getDisplayName());
		}
		this.updateSlots();
	}
	
	public ItemStack generateOutputWithchance() {
		int aChance = MathUtils.randInt(0, 7500);
		if (aChance < 100) {
			return generateOutput();
		}
		else {
			return null;
		}
	}

	public ItemStack generateOutput() {
		AutoMap<ItemStack> aData = generateOreForOutput();
		int aMax = aData.size()-1;
		return aData.get(MathUtils.randInt(0, aMax));
	}
	
	/**
	 * Here we generate valid ores and also a basic loot set
	 */
	
	public AutoMap<ItemStack> generateOreForOutput() {
		
		if (mOutputs != null) {
			return mOutputs;
		}

		AutoMap<GT_Worldgen_GT_Ore_Layer> aOverWorldOres = MiningUtils.getOresForDim(0);
		AutoMap<GT_Worldgen_GT_Ore_Layer> aNetherOres = MiningUtils.getOresForDim(-1);
		AutoMap<GT_Worldgen_GT_Ore_Layer> aEndOres = MiningUtils.getOresForDim(1);
		
		AutoMap<ItemStack> aTempMap = new AutoMap<ItemStack>();
		Block tOreBlock = GregTech_API.sBlockOres1;
		Logger.INFO("Ore Map contains "+aTempMap.size()+" values. [Initial]");
		
		for (GT_Worldgen_GT_Ore_Layer layer : aOverWorldOres) {
			if (layer.mEnabled) {
				ItemStack aTempOreStack1 = ItemUtils.simpleMetaStack(tOreBlock, layer.mPrimaryMeta, 1);	
				ItemStack aTempOreStack2 = ItemUtils.simpleMetaStack(tOreBlock, layer.mSecondaryMeta, 1);	
				ItemStack aTempOreStack3 = ItemUtils.simpleMetaStack(tOreBlock, layer.mBetweenMeta, 1);	
				ItemStack aTempOreStack4 = ItemUtils.simpleMetaStack(tOreBlock, layer.mSporadicMeta, 1);
				aTempMap.put(aTempOreStack1);
				aTempMap.put(aTempOreStack2);
				aTempMap.put(aTempOreStack3);
				aTempMap.put(aTempOreStack4);				
				aTempMap.put(aTempOreStack1);
				aTempMap.put(aTempOreStack2);
				aTempMap.put(aTempOreStack3);
				aTempMap.put(aTempOreStack4);				
				aTempMap.put(aTempOreStack1);
				aTempMap.put(aTempOreStack2);
				aTempMap.put(aTempOreStack3);
				aTempMap.put(aTempOreStack4);				
			}
		}
		Logger.INFO("Ore Map contains "+aTempMap.size()+" values. [Overworld]");
		for (GT_Worldgen_GT_Ore_Layer layer : aNetherOres) {
			if (layer.mEnabled) {
				ItemStack aTempOreStack1 = ItemUtils.simpleMetaStack(tOreBlock, layer.mPrimaryMeta, 1);	
				ItemStack aTempOreStack2 = ItemUtils.simpleMetaStack(tOreBlock, layer.mSecondaryMeta, 1);	
				ItemStack aTempOreStack3 = ItemUtils.simpleMetaStack(tOreBlock, layer.mBetweenMeta, 1);	
				ItemStack aTempOreStack4 = ItemUtils.simpleMetaStack(tOreBlock, layer.mSporadicMeta, 1);
				aTempMap.put(aTempOreStack1);
				aTempMap.put(aTempOreStack2);
				aTempMap.put(aTempOreStack3);
				aTempMap.put(aTempOreStack4);	
				aTempMap.put(aTempOreStack1);
				aTempMap.put(aTempOreStack2);
				aTempMap.put(aTempOreStack3);
				aTempMap.put(aTempOreStack4);			
			}
		}
		Logger.INFO("Ore Map contains "+aTempMap.size()+" values. [Nether]");
		for (GT_Worldgen_GT_Ore_Layer layer : aEndOres) {
			if (layer.mEnabled) {
				ItemStack aTempOreStack1 = ItemUtils.simpleMetaStack(tOreBlock, layer.mPrimaryMeta, 1);	
				ItemStack aTempOreStack2 = ItemUtils.simpleMetaStack(tOreBlock, layer.mSecondaryMeta, 1);	
				ItemStack aTempOreStack3 = ItemUtils.simpleMetaStack(tOreBlock, layer.mBetweenMeta, 1);	
				ItemStack aTempOreStack4 = ItemUtils.simpleMetaStack(tOreBlock, layer.mSporadicMeta, 1);
				aTempMap.put(aTempOreStack1);
				aTempMap.put(aTempOreStack2);
				aTempMap.put(aTempOreStack3);
				aTempMap.put(aTempOreStack4);				
			}
		}
		Logger.INFO("Ore Map contains "+aTempMap.size()+" values. [End]");

		addOreTypeToMap(ELEMENT.getInstance().IRON, 200, aTempMap);
		addOreTypeToMap(ELEMENT.getInstance().COPPER, 175, aTempMap);
		addOreTypeToMap(ELEMENT.getInstance().TIN, 150, aTempMap);
		addOreTypeToMap(ELEMENT.getInstance().GOLD, 150, aTempMap);
		addOreTypeToMap(ELEMENT.getInstance().SILVER, 110, aTempMap);
		addOreTypeToMap(ELEMENT.getInstance().NICKEL, 40, aTempMap);
		addOreTypeToMap(ELEMENT.getInstance().ZINC, 40, aTempMap);
		addOreTypeToMap(ELEMENT.getInstance().LEAD, 40, aTempMap);
		addOreTypeToMap(ELEMENT.getInstance().ALUMINIUM, 30, aTempMap);
		addOreTypeToMap(ELEMENT.getInstance().THORIUM, 20, aTempMap);
		Logger.INFO("Ore Map contains "+aTempMap.size()+" values. [Extra Common Ores]");
		
		AutoMap<Pair<String, Integer>> mMixedOreData = new AutoMap<Pair<String, Integer>>();
		mMixedOreData.put(new Pair<String, Integer>("oreRuby", 30));
		mMixedOreData.put(new Pair<String, Integer>("oreSapphire", 25));
		mMixedOreData.put(new Pair<String, Integer>("oreEmerald", 25));
		mMixedOreData.put(new Pair<String, Integer>("oreLapis", 40));
		mMixedOreData.put(new Pair<String, Integer>("oreRedstone", 40));
		
		if (LoadedMods.Thaumcraft || (OreDictionary.doesOreNameExist("oreAmber") && OreDictionary.doesOreNameExist("oreCinnabar"))) {
			mMixedOreData.put(new Pair<String, Integer>("oreAmber", 20));
			mMixedOreData.put(new Pair<String, Integer>("oreCinnabar", 20));
		}
		if (LoadedMods.Railcraft || OreDictionary.doesOreNameExist("oreSaltpeter")) {	
			mMixedOreData.put(new Pair<String, Integer>("oreSaltpeter", 10));
		}
		if (LoadedMods.IndustrialCraft2 || OreDictionary.doesOreNameExist("oreUranium")) {
			mMixedOreData.put(new Pair<String, Integer>("oreUranium", 10));	
		}
		if (OreDictionary.doesOreNameExist("oreSulfur")) {
			mMixedOreData.put(new Pair<String, Integer>("oreSulfur", 15));			
		}
		if (OreDictionary.doesOreNameExist("oreSilicon")) {
			mMixedOreData.put(new Pair<String, Integer>("oreSilicon", 15));			
		}
		if (OreDictionary.doesOreNameExist("oreApatite")) {
			mMixedOreData.put(new Pair<String, Integer>("oreApatite", 25));			
		}
		
		mMixedOreData.put(new Pair<String, Integer>("oreFirestone", 2));
		mMixedOreData.put(new Pair<String, Integer>("oreBismuth", 20));
		mMixedOreData.put(new Pair<String, Integer>("oreLithium", 20));
		mMixedOreData.put(new Pair<String, Integer>("oreManganese", 20));
		mMixedOreData.put(new Pair<String, Integer>("oreBeryllium", 20));
		mMixedOreData.put(new Pair<String, Integer>("oreCoal", 75));
		mMixedOreData.put(new Pair<String, Integer>("oreLignite", 75));
		mMixedOreData.put(new Pair<String, Integer>("oreSalt", 15));
		mMixedOreData.put(new Pair<String, Integer>("oreCalcite", 15));
		mMixedOreData.put(new Pair<String, Integer>("oreBauxite", 20));
		mMixedOreData.put(new Pair<String, Integer>("oreAlmandine", 15));
		mMixedOreData.put(new Pair<String, Integer>("oreGraphite", 25));
		mMixedOreData.put(new Pair<String, Integer>("oreGlauconite", 15));
		mMixedOreData.put(new Pair<String, Integer>("orePyrolusite", 15));
		mMixedOreData.put(new Pair<String, Integer>("oreGrossular", 15));
		mMixedOreData.put(new Pair<String, Integer>("oreTantalite", 15));
		
		for (Pair<String, Integer> g : mMixedOreData) {
			for (int i=0; i<g.getValue();i++) {
				aTempMap.put(ItemUtils.getItemStackOfAmountFromOreDict(g.getKey(), 1));
			}
		}
		Logger.INFO("Ore Map contains "+aTempMap.size()+" values. [Extra Mixed Ores]");
		
		
		addOreTypeToMap(ELEMENT.STANDALONE.RUNITE, 2, aTempMap);
		addOreTypeToMap(ELEMENT.STANDALONE.GRANITE, 8, aTempMap);
		Logger.INFO("Ore Map contains "+aTempMap.size()+" values. [OSRS Ores]");
		
		
		AutoMap<Material> aMyOreMaterials = new AutoMap<Material>();		
		aMyOreMaterials.add(ORES.CROCROITE);
		aMyOreMaterials.add(ORES.GEIKIELITE);
		aMyOreMaterials.add(ORES.NICHROMITE);
		aMyOreMaterials.add(ORES.TITANITE);
		aMyOreMaterials.add(ORES.ZIMBABWEITE);
		aMyOreMaterials.add(ORES.ZIRCONILITE);
		aMyOreMaterials.add(ORES.GADOLINITE_CE);
		aMyOreMaterials.add(ORES.GADOLINITE_Y);
		aMyOreMaterials.add(ORES.LEPERSONNITE);
		aMyOreMaterials.add(ORES.SAMARSKITE_Y);
		aMyOreMaterials.add(ORES.SAMARSKITE_YB);
		aMyOreMaterials.add(ORES.XENOTIME);
		aMyOreMaterials.add(ORES.YTTRIAITE);
		aMyOreMaterials.add(ORES.YTTRIALITE);
		aMyOreMaterials.add(ORES.YTTROCERITE);
		aMyOreMaterials.add(ORES.ZIRCON);
		aMyOreMaterials.add(ORES.POLYCRASE);
		aMyOreMaterials.add(ORES.ZIRCOPHYLLITE);
		aMyOreMaterials.add(ORES.ZIRKELITE);
		aMyOreMaterials.add(ORES.LANTHANITE_LA);
		aMyOreMaterials.add(ORES.LANTHANITE_CE);
		aMyOreMaterials.add(ORES.LANTHANITE_ND);
		aMyOreMaterials.add(ORES.AGARDITE_Y);
		aMyOreMaterials.add(ORES.AGARDITE_CD);
		aMyOreMaterials.add(ORES.AGARDITE_LA);
		aMyOreMaterials.add(ORES.AGARDITE_ND);
		aMyOreMaterials.add(ORES.HIBONITE);
		aMyOreMaterials.add(ORES.CERITE);
		aMyOreMaterials.add(ORES.FLUORCAPHITE);
		aMyOreMaterials.add(ORES.FLORENCITE);
		aMyOreMaterials.add(ORES.CRYOLITE);
		aMyOreMaterials.add(ORES.LAUTARITE);
		aMyOreMaterials.add(ORES.LAFOSSAITE);
		aMyOreMaterials.add(ORES.DEMICHELEITE_BR);
		aMyOreMaterials.add(ORES.COMANCHEITE);
		aMyOreMaterials.add(ORES.PERROUDITE);
		aMyOreMaterials.add(ORES.HONEAITE);
		aMyOreMaterials.add(ORES.ALBURNITE);
		aMyOreMaterials.add(ORES.MIESSIITE);
		aMyOreMaterials.add(ORES.KASHINITE);
		aMyOreMaterials.add(ORES.IRARSITE);
		aMyOreMaterials.add(ORES.RADIOBARITE);
		aMyOreMaterials.add(ORES.DEEP_EARTH_REACTOR_FUEL_DEPOSIT);		
		
		for (Material aOreType : aMyOreMaterials) {			
			if (aOreType == ORES.DEEP_EARTH_REACTOR_FUEL_DEPOSIT || aOreType == ORES.RADIOBARITE) {
				addOreTypeToMap(aOreType, 4, aTempMap);					
			}
			else {
				addOreTypeToMap(aOreType, 7, aTempMap);					
			}					
		}		
		
		//Cleanup Map
		Logger.INFO("Ore Map contains "+aTempMap.size()+" values. [GT++]");
		AutoMap<ItemStack> aCleanUp = new AutoMap<ItemStack>();
		for (ItemStack verify : aTempMap) {
			if (!ItemUtils.checkForInvalidItems(verify)) {
				aCleanUp.put(verify);
			}
		}		
		Logger.INFO("Cleanup Map contains "+aCleanUp.size()+" values.");
		for (ItemStack remove : aCleanUp) {
			aTempMap.remove(remove);
		}		
		
		//Generate Massive Map
		AutoMap<ItemStack> aFinalMap = new AutoMap<ItemStack>();		
		for (ItemStack aTempItem : aTempMap) {			
			int aTempMulti = MathUtils.randInt(20, 50);
			for (int i=0;i<aTempMulti;i++) {
				aFinalMap.put(aTempItem.copy());
			}			
		}		
		Logger.INFO("Final Ore Map contains "+aFinalMap.size()+" values.");
		mOutputs = aFinalMap;
		return mOutputs;
	}
	
	
	private static void addOreTypeToMap(Material aMaterial, int aAmount, AutoMap<ItemStack> aMap) {
		for (int i=0; i<aAmount;i++) {
			aMap.add(aMaterial.getOre(1));
		}
	}
	


	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public String getMachineType() {
		return "Miner";
	}

	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}
	
	
	
	
	
	
	
}