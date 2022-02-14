
package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.*;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.*;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.slots.SlotBuzzSaw.SAWTOOL;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.TreeFarmHelper;
import gtPlusPlus.xmod.gregtech.common.helpers.treefarm.TreeGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMetaTileEntityTreeFarm extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntityTreeFarm> {

	public static int CASING_TEXTURE_ID;
	public static String mCasingName = "Sterile Farm Casing";
	public static TreeGenerator mTreeData;
	private ItemStack mTreeType;
	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntityTreeFarm> STRUCTURE_DEFINITION = null;
	private static HashMap<String, ItemStack> sLogCache = new HashMap<String, ItemStack>();
	
	private SAWTOOL mToolType;
	private ItemStack currSapling;
	private int currSlot = 0;
	private GT_MetaTileEntity_Hatch_InputBus currInputBus;

	public GregtechMetaTileEntityTreeFarm(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
	}

	public GregtechMetaTileEntityTreeFarm(final String aName) {
		super(aName);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityTreeFarm(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Tree Farm";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
		.addInfo("Converts EU to Logs")
		.addInfo("Eu Usage: 100% | Parallel: 1")
		.addInfo("Requires a Saw or Chainsaw in GUI slot")
		.addInfo("Output multiplier:")
		.addInfo("Saw = 1x")
		.addInfo("Buzzsaw = 2x")
		.addInfo("Chainsaw = 4x")
		.addInfo("Add a sapling in the input bus to change wood type output")
		.addInfo("Tools can also be fed to the controller via input bus")
		.addPollutionAmount(getPollutionPerSecond(null))
		.addSeparator()
		.beginStructureBlock(3, 3, 3, true)
		.addController("Front center")
		.addCasingInfo("Sterile Farm Casing", 8)
		.addInputBus("Any casing", 1)
		.addOutputBus("Any casing", 1)
		.addEnergyHatch("Any casing", 1)
		.addMaintenanceHatch("Any casing", 1)
		.addMufflerHatch("Any casing", 1)
		.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID), TextureFactory.of(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID)};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "VacuumFreezer";
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	public boolean isCorrectMachinePart(final ItemStack aStack) {
		// is correct part && either not powered tool or have enough power
		return TreeFarmHelper.isValidForGUI(aStack) && !GT_ModHandler.isElectricItem(aStack) || GT_ModHandler.canUseElectricItem(aStack, 1);
	}

	public boolean checkRecipe(final ItemStack aStack) {

		if (aStack == null && !replaceTool()) {
			// no tool
			return false;
		}
		if (!isCorrectMachinePart(aStack)) {
			// not a tool
			return false;
		}
		mToolType = TreeFarmHelper.isCorrectMachinePart(aStack);
		int aModifier = mToolType == SAWTOOL.SAW ? 1 : mToolType == SAWTOOL.BUZZSAW ? 2 : mToolType == SAWTOOL.CHAINSAW ? 4 : 0;

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

		this.mMaxProgresstime = 100;
		this.mEUt = (int) tVoltage;

		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;

		// Overclock
		if (this.mEUt <= 16) {
			this.mEUt = (this.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
			this.mMaxProgresstime = (this.mMaxProgresstime / (1 << tTier - 1));
		} else {
			while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
				this.mEUt *= 4;
				this.mMaxProgresstime /= 2;
			}
		}
		if (this.mEUt > 0) {
			this.mEUt = (-this.mEUt);
		}
		getWoodFromSapling();
		try {
			int aFert = hasLiquidFert();
			if (aFert > 0) {
				int aOutputAmount = ((2 * (tTier * tTier)) - (2 * tTier) + 5)*(mMaxProgresstime/20) / 10;
				if (aFert < aOutputAmount) {
					aOutputAmount /= 10;
				}
				if (aFert >= aOutputAmount) {
					if (tryConsumeLiquidFert(aOutputAmount)) {
						int aFullStacks = aOutputAmount / 64;
						for (int i = 0; i < aFullStacks; i++) {
							this.addOutput(ItemUtils.getSimpleStack(currSapling, 64));
							aOutputAmount -= 64;
						}
						this.addOutput(ItemUtils.getSimpleStack(currSapling, aOutputAmount));
					}					
				}
			}
			int aOutputAmount = ((2 * (tTier * tTier)) - (2 * tTier) + 5)*(mMaxProgresstime/20) * aModifier;
			int aFullStacks = aOutputAmount / 64;
			for (int i = 0; i < aFullStacks; i++) {
				this.addOutput(ItemUtils.getSimpleStack(mTreeType, 64));
				aOutputAmount -= 64;
			}
			this.addOutput(ItemUtils.getSimpleStack(mTreeType, aOutputAmount));
			this.updateSlots();
		} catch (Throwable t) {
			t.printStackTrace(GT_Log.err);
		}
		return true;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 1, 1, 0) && mCasing >= 8 && checkHatch();
	}

	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntityTreeFarm> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntityTreeFarm>builder().addShape(mName, transpose(new String[][]{{"CCC", "CCC", "CCC"}, {"C~C", "C-C", "CCC"}, {"CCC", "CCC", "CCC"},})).addElement('C', ofChain(ofHatchAdder(GregtechMetaTileEntityTreeFarm::addTreeFarmList, CASING_TEXTURE_ID, 1), onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 15)))).build();
		}
		return STRUCTURE_DEFINITION;
	}

	public final boolean addTreeFarmList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		else {
			return addToMachineList(aTileEntity, aBaseCasingIndex);
		}
	}

	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerSecond(final ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiTreeFarm;
	}

	public int getDamageToComponent(final ItemStack aStack) {
		return MathUtils.balance((int) (75 - ((GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).getMass()))), 5, 120);
	}

	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	private int mPossibleRecursion = 0;
	public boolean replaceTool() {
		if (mPossibleRecursion > 32) {
			mPossibleRecursion = 0;
			return false;
		}
		ItemStack invItem = this.mInventory[1];
		if (invItem == null) {
			ArrayList<ItemStack> aInputs = getStoredInputs();
			for (ItemStack aStack : aInputs) {
				if (aStack != null && isCorrectMachinePart(aStack)) {
					this.setGUIItemStack(aStack.copy());
					this.depleteInput(aStack);
					mPossibleRecursion = 0;
					return true;
				}
			}
		}
		else {
			if (GT_ModHandler.isElectricItem(invItem)) {
				if (ic2.api.item.ElectricItem.manager.getCharge(invItem) < 32) {
					this.addOutput(invItem.copy());
					this.setGUIItemStack(null);
					mPossibleRecursion++;
					return replaceTool();
				}
			}
		}
		mPossibleRecursion = 0;
		return false;
	}

	public void getWoodFromSapling() {
		if (sLogCache.size() == 0) {
			loadMapWoodFromSapling();
		}
		if (this.currSapling != null && this.currInputBus != null) {
			ItemStack uStack = this.currInputBus.mInventory[this.currSlot];
			if (uStack == this.currSapling)
				return;
		}
		for (GT_MetaTileEntity_Hatch_InputBus mInputBus : this.mInputBusses) {
			for (int i = 0; i < mInputBus.mInventory.length; i++) {
				ItemStack uStack = mInputBus.mInventory[i];
				if (uStack != null) {
					String registryName = Item.itemRegistry.getNameForObject(uStack.getItem());
					ItemStack aWood = sLogCache.get(registryName + ":" + uStack.getItemDamage());
					if (aWood != null) {
						this.currSapling = uStack;
						this.currInputBus = mInputBus;
						this.currSlot = i;
						this.mTreeType = aWood;
						return;
					}
				}
			}
			this.mTreeType = new ItemStack(Blocks.log, 1, 0);
		}
	}

	public static void loadMapWoodFromSapling() {

		// galaxySpace
		mapSaplingToLog("GalaxySpace:barnardaCsapling:0", GT_ModHandler.getModItem("GalaxySpace", "barnardaClog", 1)); // barnarda c
		
		// minecraft
		mapSaplingToLog("minecraft:sapling:0", new ItemStack(Blocks.log, 1, 0)); // oak
		mapSaplingToLog("minecraft:sapling:1", new ItemStack(Blocks.log, 1, 1)); // spruce
		mapSaplingToLog("minecraft:sapling:2", new ItemStack(Blocks.log, 1, 2)); // birch
		mapSaplingToLog("minecraft:sapling:3", new ItemStack(Blocks.log, 1, 3)); // jungle
		mapSaplingToLog("minecraft:sapling:4", new ItemStack(Blocks.log2, 1, 0)); // acacia
		mapSaplingToLog("minecraft:sapling:5", new ItemStack(Blocks.log2, 1, 1)); // dark oak

		
		// ic2
		mapSaplingToLog("IC2:blockRubSapling:0", GT_ModHandler.getModItem("IC2", "blockRubWood", 1)); // rubber

		// natura
		mapSaplingToLog("Natura:florasapling:1", GT_ModHandler.getModItem("Natura", "tree", 1, 0)); // eucalyptus
		mapSaplingToLog("Natura:florasapling:2", GT_ModHandler.getModItem("Natura", "tree", 1, 3)); // hopseed
		mapSaplingToLog("Natura:florasapling:3", GT_ModHandler.getModItem("Natura", "tree", 1, 1)); // sakura
		mapSaplingToLog("Natura:florasapling:4", GT_ModHandler.getModItem("Natura", "tree", 1, 2)); // ghostwood
		mapSaplingToLog("Natura:florasapling:5", GT_ModHandler.getModItem("Natura", "bloodwood", 1, 0)); // bloodwood
		mapSaplingToLog("Natura:florasapling:6", GT_ModHandler.getModItem("Natura", "Dark Tree", 1, 0)); // darkwood
		mapSaplingToLog("Natura:florasapling:7", GT_ModHandler.getModItem("Natura", "Dark Tree", 1, 1)); // fusewood

		mapSaplingToLog("Natura:Rare Sapling:0", GT_ModHandler.getModItem("Natura", "Rare Tree", 1, 0)); // maple
		mapSaplingToLog("Natura:Rare Sapling:1", GT_ModHandler.getModItem("Natura", "Rare Tree", 1, 1)); // silverbell
		mapSaplingToLog("Natura:Rare Sapling:2", GT_ModHandler.getModItem("Natura", "Rare Tree", 1, 2)); // amaranth
		mapSaplingToLog("Natura:Rare Sapling:3", GT_ModHandler.getModItem("Natura", "Rare Tree", 1, 3)); // tigerwood
		mapSaplingToLog("Natura:Rare Sapling:4", GT_ModHandler.getModItem("Natura", "willow", 1, 0)); // willow
		
		// BOP
		mapSaplingToLog("BiomesOPlenty:colorizedSaplings:0", GT_ModHandler.getModItem("BiomesOPlenty", "logs1", 1, 0)); // Sacred Oak
		mapSaplingToLog("BiomesOPlenty:colorizedSaplings:1", GT_ModHandler.getModItem("BiomesOPlenty", "logs2", 1, 2)); // Mangrove
		mapSaplingToLog("BiomesOPlenty:colorizedSaplings:2", GT_ModHandler.getModItem("BiomesOPlenty", "logs2", 1, 3)); // Palm
		mapSaplingToLog("BiomesOPlenty:colorizedSaplings:3", GT_ModHandler.getModItem("BiomesOPlenty", "logs3", 1, 0)); // Redwood
		mapSaplingToLog("BiomesOPlenty:colorizedSaplings:4", GT_ModHandler.getModItem("BiomesOPlenty", "logs3", 1, 1)); // Willow
		mapSaplingToLog("BiomesOPlenty:colorizedSaplings:5", GT_ModHandler.getModItem("BiomesOPlenty", "logs4", 1, 0)); // Pine
		mapSaplingToLog("BiomesOPlenty:colorizedSaplings:6", GT_ModHandler.getModItem("BiomesOPlenty", "logs4", 1, 3)); // Mahogany
		mapSaplingToLog("BiomesOPlenty:saplings:2", GT_ModHandler.getModItem("BiomesOPlenty", "bamboo", 1, 0)); // Bamboo
		mapSaplingToLog("BiomesOPlenty:saplings:3", GT_ModHandler.getModItem("BiomesOPlenty", "logs2", 1, 1)); // Magic
		mapSaplingToLog("BiomesOPlenty:saplings:4", GT_ModHandler.getModItem("BiomesOPlenty", "logs1", 1, 2)); // Dark
		mapSaplingToLog("BiomesOPlenty:saplings:5", GT_ModHandler.getModItem("BiomesOPlenty", "logs3", 1, 2)); // Dying/Dead
		mapSaplingToLog("BiomesOPlenty:saplings:6", GT_ModHandler.getModItem("BiomesOPlenty", "logs1", 1, 3)); // Fir
		mapSaplingToLog("BiomesOPlenty:saplings:7", GT_ModHandler.getModItem("BiomesOPlenty", "logs2", 1, 0)); // Ethereal
		mapSaplingToLog("BiomesOPlenty:saplings:10", GT_ModHandler.getModItem("BiomesOPlenty", "logs1", 1, 1)); // Pink Cherry
		mapSaplingToLog("BiomesOPlenty:saplings:12", GT_ModHandler.getModItem("BiomesOPlenty", "logs1", 1, 1)); // White Cherry
		mapSaplingToLog("BiomesOPlenty:saplings:13", GT_ModHandler.getModItem("BiomesOPlenty", "logs4", 1, 1)); // Hellbark
		mapSaplingToLog("BiomesOPlenty:saplings:14", GT_ModHandler.getModItem("BiomesOPlenty", "logs4", 1, 2)); // Jacaranda
		mapSaplingToLog("minecraft:yellow_flower:0", GT_ModHandler.getModItem("BiomesOPlenty", "logs3", 1, 3)); // Giant Flower Stem
		mapSaplingToLog("minecraft:red_flower:0", GT_ModHandler.getModItem("BiomesOPlenty", "logs3", 1, 3)); // Giant Flower Stem

		// Witchery
		mapSaplingToLog("witchery:witchsapling:0", GT_ModHandler.getModItem("witchery", "witchlog", 1, 0)); // Rowan
		mapSaplingToLog("witchery:witchsapling:1", GT_ModHandler.getModItem("witchery", "witchlog", 1, 1)); // Alder
		mapSaplingToLog("witchery:witchsapling:2", GT_ModHandler.getModItem("witchery", "witchlog", 1, 2)); // Hawthorn
		
		
		// TConstruct
		mapSaplingToLog("TConstruct:slime.sapling:0", GT_ModHandler.getModItem("TConstruct", "slime.gel", 1)); // green slime blocks

		// TaintedMagic
		mapSaplingToLog("TaintedMagic:BlockWarpwoodSapling:0", GT_ModHandler.getModItem("TaintedMagic", "BlockWarpwoodLog", 1)); // warpwood

		// Thaumcraft
		mapSaplingToLog("Thaumcraft:blockCustomPlant:0", GT_ModHandler.getModItem("Thaumcraft", "blockMagicalLog", 1, 0)); // greatwood
		mapSaplingToLog("Thaumcraft:blockCustomPlant:1", GT_ModHandler.getModItem("Thaumcraft", "blockMagicalLog", 1, 1)); // silverwood

		// gt++
		mapSaplingToLog("miscutils:blockRainforestOakSapling:0", GT_ModHandler.getModItem("miscutils", "blockRainforestOakLog", 1)); // rainforest
		mapSaplingToLog("miscutils:blockPineSapling:0", GT_ModHandler.getModItem("miscutils", "blockPineLogLog", 1)); // pine
	}

	public boolean tryDamageTool(ItemStack invItem) {
		if (invItem != null && invItem.getItem() instanceof GT_MetaGenerated_Tool) {
			long aDmg = GT_MetaGenerated_Tool.getToolDamage(invItem);
			long aDmgMax = GT_MetaGenerated_Tool.getToolMaxDamage(invItem);
			if (aDmg < aDmgMax && GT_MetaGenerated_Tool.getPrimaryMaterial(invItem) != Materials._NULL) {
				return GT_ModHandler.damageOrDechargeItem(invItem, 1, 32, null);
			}
		}
		return false;
	}

	@Override
	public boolean doRandomMaintenanceDamage() {
		ItemStack tSaw = mInventory[1];
		if (!isCorrectMachinePart(tSaw) || getRepairStatus() == 0) {
			stopMachine();
			return false;
		}
		if (CORE.RANDOM.nextInt(200) == 0) {
			if (!tryDamageTool(tSaw)) {
				this.mInventory[1] = null;
				if (!replaceTool()) {
					this.getBaseMetaTileEntity().disableWorking();
				}
				tryDamageTool(tSaw);
			}
		}
		return super.doRandomMaintenanceDamage();
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		replaceTool();
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
	}
	
	public static void mapSaplingToLog(String aSapling, ItemStack aLog) {
		ItemStack aSaplingStack = ItemUtils.getItemStackFromFQRN(aSapling, 1);
		if (aSaplingStack != null && aLog != null) {
			sLogCache.put(aSapling, aLog);
			addFakeRecipeToNEI(aSaplingStack, aLog);
		}
		else {
			Logger.INFO("Unable to add Tree Growth Simulation for "+aSapling);
		}
	}
	
	private static int sRecipeID = 0;
	public static boolean addFakeRecipeToNEI(ItemStack aSapling, ItemStack aLog) {
		if (sFertFluid == null) {
			sFertFluid = gtPlusPlus.core.item.ModItems.fluidFertBasic;
		}
		int aRecipes = GTPP_Recipe_Map.sTreeSimFakeRecipes.mRecipeList.size();
		Logger.INFO("Adding Tree Growth Simulation for "+aSapling.getDisplayName()+" -> "+aLog.getDisplayName());
		ItemStack[] aOutput = new ItemStack[] {aLog, aSapling};
		GT_Recipe aRecipe = new GT_Recipe(
				false, 
				new ItemStack[] {aSapling.copy()},
				aOutput,
				null, 
				new int[] {10000, 1000}, 
				new FluidStack[] {FluidUtils.getFluidStack(sFertFluid, 1)},
				new FluidStack[] {},
				1,
				sRecipeID++,
				0);
		aRecipe.mOutputs = aOutput;
		String aOutputs = ItemUtils.getArrayStackNames(aRecipe.mOutputs);
		Logger.INFO(""+aOutputs);
		GTPP_Recipe_Map.sTreeSimFakeRecipes.addFakeRecipe(false, aRecipe);
		return GTPP_Recipe_Map.sTreeSimFakeRecipes.mRecipeList.size() > aRecipes;
	}
	
	private static Fluid sFertFluid;
	
	public int hasLiquidFert() {
		if (sFertFluid == null) {
			sFertFluid = gtPlusPlus.core.item.ModItems.fluidFertBasic;
		}
		ArrayList<FluidStack> aFluids = this.getStoredFluids();
		for (FluidStack aFluid : aFluids) {
			if (aFluid.getFluid() == sFertFluid) {
				return aFluid.amount;
			}
		}
		return 0;
	}
	
	public boolean tryConsumeLiquidFert(int aFluidAmount) {
		if (sFertFluid == null) {
			sFertFluid = gtPlusPlus.core.item.ModItems.fluidFertBasic;
		}
		return this.depleteInput(FluidUtils.getFluidStack(sFertFluid, aFluidAmount));
	}
}
