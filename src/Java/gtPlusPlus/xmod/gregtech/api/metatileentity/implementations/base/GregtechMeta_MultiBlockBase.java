package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.GTplusplus.INIT_PHASE;
import gtPlusPlus.api.helpers.GregtechPlusPlus_API.Multiblock_API;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.ConcurrentHashSet;
import gtPlusPlus.api.objects.data.ConcurrentSet;
import gtPlusPlus.api.objects.data.FlexiblePair;
import gtPlusPlus.api.objects.data.Triplet;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.api.objects.minecraft.multi.SpecialMultiBehaviour;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_MultiMachine_NoPlayerInventory;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine_Default;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_Multi_Basic_Slotted;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_AirIntake;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_ControlCore;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBattery;
import gtPlusPlus.xmod.gregtech.api.objects.MultiblockRequirements;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public abstract class GregtechMeta_MultiBlockBase extends GT_MetaTileEntity_MultiBlockBase {

	public static final boolean DEBUG_DISABLE_CORES_TEMPORARILY = true;

	static {

		Method a08 = findRecipe08 = ReflectionUtils.getMethod(GT_Recipe_Map.class, "findRecipe", IHasWorldObjectAndCoords.class, GT_Recipe.class, boolean.class, long.class, FluidStack[].class, ItemStack.class, ItemStack[].class);
		Method a09 = findRecipe09 = ReflectionUtils.getMethod(GT_Recipe_Map.class, "findRecipe", IHasWorldObjectAndCoords.class, GT_Recipe.class, boolean.class, boolean.class, long.class, FluidStack[].class, ItemStack.class, ItemStack[].class);
		Logger.MACHINE_INFO("Found .08 findRecipe method? "+(a08 != null));
		Logger.MACHINE_INFO("Found .09 findRecipe method? "+(a09 != null));

		if (CORE.DEBUG) {
			aLogger = ReflectionUtils.getMethod(Logger.class, "INFO", String.class);				
		}
		else {
			aLogger = ReflectionUtils.getMethod(Logger.class, "MACHINE_INFO", String.class);				
		}

		try {
			calculatePollutionReduction = GT_MetaTileEntity_Hatch_Muffler.class.getDeclaredMethod("calculatePollutionReduction", int.class);
		} 
		catch (NoSuchMethodException | SecurityException e) {}		
		
	}

	//Find Recipe Methods
	private static final Method findRecipe08;
	private static final Method findRecipe09;

	public GT_Recipe mLastRecipe;
	private MultiblockRequirements mRequirements;
	private boolean mInternalCircuit = false;
	protected long mTotalRunTime = 0;

	//Control Core Hatch
	public ArrayList<GT_MetaTileEntity_Hatch_ControlCore> mControlCoreBus = new ArrayList<GT_MetaTileEntity_Hatch_ControlCore>();
	public ArrayList<GT_MetaTileEntity_Hatch_AirIntake> mAirIntakes = new ArrayList<GT_MetaTileEntity_Hatch_AirIntake>();
	public ArrayList<GT_MetaTileEntity_Hatch_InputBattery> mChargeHatches = new ArrayList<GT_MetaTileEntity_Hatch_InputBattery>();
	public ArrayList<GT_MetaTileEntity_Hatch_OutputBattery> mDischargeHatches = new ArrayList<GT_MetaTileEntity_Hatch_OutputBattery>();

	// Custom Behaviour Map
	private static final HashMap<String, SpecialMultiBehaviour> mCustomBehviours = new HashMap<String, SpecialMultiBehaviour>();;
	
	
	public GregtechMeta_MultiBlockBase(final int aID, final String aName,
			final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMeta_MultiBlockBase(final String aName) {
		super(aName);
	}

	public static boolean isValidMetaTileEntity(
			final MetaTileEntity aMetaTileEntity) {
		return (aMetaTileEntity.getBaseMetaTileEntity() != null)
				&& (aMetaTileEntity.getBaseMetaTileEntity()
						.getMetaTileEntity() == aMetaTileEntity)
				&& !aMetaTileEntity.getBaseMetaTileEntity().isDead();
	}

	public abstract boolean hasSlotInGUI();

	public long getTotalRuntimeInTicks() {
		return this.mTotalRunTime;
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		if (hasSlotInGUI()) {
			return new GT_Container_MultiMachine(aPlayerInventory,	aBaseMetaTileEntity);			
		}
		else {			
			String aCustomGUI = getCustomGUIResourceName();
			if (aCustomGUI == null) {
				return new CONTAINER_MultiMachine_NoPlayerInventory(aPlayerInventory,	aBaseMetaTileEntity);	
			}
			else {
				return new CONTAINER_MultiMachine(aPlayerInventory,	aBaseMetaTileEntity);	
			}					
		}
	}

	public abstract String getCustomGUIResourceName();

	public boolean requiresVanillaGtGUI() {
		return false;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {		
		String aCustomGUI = getCustomGUIResourceName();
		aCustomGUI = aCustomGUI != null ? aCustomGUI : hasSlotInGUI() ? "MultiblockDisplay" : "MultiblockDisplay_Generic";
		aCustomGUI = aCustomGUI + ".png";
		if (hasSlotInGUI()) {
			if (!requiresVanillaGtGUI()) {
				return new GUI_Multi_Basic_Slotted(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), aCustomGUI);			
			}
			else {
				return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), aCustomGUI);			
			}			
		}
		else {
			if (getCustomGUIResourceName() == null && !hasSlotInGUI()) {
				return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), aCustomGUI);
			}
			else {				
				return new GUI_MultiMachine_Default(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), aCustomGUI);
			}						
		}		
	}

	public abstract String getMachineType();

	public String getMachineTooltip() {
		return "Machine Type: " + EnumChatFormatting.YELLOW + getMachineType() + EnumChatFormatting.RESET;
	}

	public String[] getExtraInfoData() {
		return new String[0];
	};

	@Override
	public final String[] getInfoData() {
		ArrayList<String> mInfo = new ArrayList<String>();
		if (!this.getMetaName().equals("")) {
			mInfo.add(this.getMetaName());
		}

		String[] extra = getExtraInfoData();

		if (extra == null) {
			extra = new String[0];
		}
		if (extra.length > 0) {
			for (String s : extra) {
				mInfo.add(s);
			}
		}

		long seconds = (this.mTotalRunTime/20);
		int weeks = (int) (TimeUnit.SECONDS.toDays(seconds) / 7);
		int days = (int) (TimeUnit.SECONDS.toDays(seconds) - 7 * weeks);
		long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(days) - TimeUnit.DAYS.toHours(7*weeks);
		long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

		int mPollutionReduction = getPollutionReductionForAllMufflers();
		long storedEnergy = getStoredEnergyInAllEnergyHatches();
		long maxEnergy = getMaxEnergyStorageOfAllEnergyHatches();
		int tTier = this.getControlCoreTier();
		mInfo.add(getMachineTooltip());



		//Lets borrow the GTNH handling

		mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.progress")+": "+
				EnumChatFormatting.GREEN + Integer.toString(mProgresstime/20) + EnumChatFormatting.RESET +" s / "+
				EnumChatFormatting.YELLOW + Integer.toString(mMaxProgresstime/20) + EnumChatFormatting.RESET +" s");

		mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.energy")+": "+
				EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET +" EU / "+
				EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET +" EU");

		mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.usage")+": "+
				EnumChatFormatting.RED + Integer.toString(-mEUt) + EnumChatFormatting.RESET + " EU/t");

		mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.mei")+": "+
				EnumChatFormatting.YELLOW+Long.toString(getMaxInputVoltage())+EnumChatFormatting.RESET+ " EU/t(*2A) "+StatCollector.translateToLocal("GTPP.machines.tier")+": "+
				EnumChatFormatting.YELLOW+GT_Values.VN[GT_Utility.getTier(getMaxInputVoltage())]+ EnumChatFormatting.RESET);

		mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.problems")+": "+
				EnumChatFormatting.RED+ (getIdealStatus() - getRepairStatus())+EnumChatFormatting.RESET+
				" "+StatCollector.translateToLocal("GTPP.multiblock.efficiency")+": "+
				EnumChatFormatting.YELLOW+Float.toString(mEfficiency / 100.0F)+EnumChatFormatting.RESET + " %");


		mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.pollution")+": "+ EnumChatFormatting.RED + this.getPollutionPerTick(null)*20+ EnumChatFormatting.RESET+"/sec");
		mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.pollutionreduced")+": "+ EnumChatFormatting.GREEN + mPollutionReduction+ EnumChatFormatting.RESET+" %");


		mInfo.add(StatCollector.translateToLocal("GTPP.CC.machinetier")+": "+
				EnumChatFormatting.GREEN+tTier+EnumChatFormatting.RESET);

		mInfo.add(StatCollector.translateToLocal("GTPP.CC.discount")+": "+
				EnumChatFormatting.GREEN+(getEuDiscountForParallelism())+EnumChatFormatting.RESET + "%");

		mInfo.add(StatCollector.translateToLocal("GTPP.CC.parallel")+": "+EnumChatFormatting.GREEN+(getMaxParallelRecipes())+EnumChatFormatting.RESET);


		mInfo.add("Total Time Since Built: " + EnumChatFormatting.DARK_GREEN + Integer.toString(weeks)+EnumChatFormatting.RESET+" Weeks, " + EnumChatFormatting.DARK_GREEN+ Integer.toString(days) +EnumChatFormatting.RESET+ " Days, ");
		mInfo.add(EnumChatFormatting.DARK_GREEN + Long.toString(hours) +EnumChatFormatting.RESET + " Hours, " + EnumChatFormatting.DARK_GREEN+ Long.toString(minutes) +EnumChatFormatting.RESET+ " Minutes, " + EnumChatFormatting.DARK_GREEN+ Long.toString(second) +EnumChatFormatting.RESET+ " Seconds.");
		mInfo.add("Total Time in ticks: " + EnumChatFormatting.DARK_GREEN + Long.toString(this.mTotalRunTime));


		String[] mInfo2 = new String[mInfo.size()];
		mInfo.toArray(mInfo2);
		return mInfo2;



	}

	public int getPollutionReductionForAllMufflers() {
		int mPollutionReduction=0;
		for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				mPollutionReduction=Math.max(calculatePollutionReductionForHatch(tHatch, 100),mPollutionReduction);
			}
		}
		return mPollutionReduction;
	}

	public long getStoredEnergyInAllEnergyHatches() {
		long storedEnergy=0;
		for(GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
			}
		}
		return storedEnergy;
	}

	public long getMaxEnergyStorageOfAllEnergyHatches() {
		long maxEnergy=0;
		for(GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
			}
		}
		return maxEnergy;
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	private String[] aCachedToolTip;

	/*private final String aRequiresMuffler = "1x Muffler Hatch";
	private final String aRequiresCoreModule = "1x Core Module";
	private final String aRequiresMaint = "1x Maintanence Hatch";*/

	public final static String TAG_HIDE_HATCHES = "TAG_HIDE_HATCHES";
	public final static String TAG_HIDE_POLLUTION = "TAG_HIDE_POLLUTION";
	public final static String TAG_HIDE_MACHINE_TYPE = "TAG_HIDE_MACHINE_TYPE";

	@Override
	public final String[] getDescription() {		
		/*if (aCachedToolTip != null) {
			boolean uuuu = false;
			for (String s : aCachedToolTip) {
				if (s.toLowerCase().contains(".")) {
					uuuu = true;
					break;
				}
			}
			if (!uuuu) {
				return aCachedToolTip;				
			}
			else {
				aCachedToolTip = null;
			}			
		}*/

		String aRequiresMuffler = "1x Muffler Hatch";
		//String aRequiresCoreModule = "1x Core Module";
		String aRequiresMaint = "1x Maintanence Hatch";

		String[] x = getTooltip();

		//Filter List, toggle switches, rebuild map without flags
		boolean showHatches = true;
		boolean showMachineType = true;
		boolean showPollution = getPollutionPerTick(null) > 0;
		AutoMap<String> aTempMap = new AutoMap<String>();	
		for (int ee = 0; ee < x.length; ee++) {
			String hh = x[ee];
			if (hh.equals(TAG_HIDE_HATCHES)) {
				showHatches = false;
			}
			else if (hh.equals(TAG_HIDE_POLLUTION)) {
				showPollution = false;
			}
			else if (hh.equals(TAG_HIDE_MACHINE_TYPE)) {
				showMachineType = false;
			}
			else {
				aTempMap.put(x[ee]);
			}
		}
		//Rebuild
		x = new String[aTempMap.size()];
		for (int ee = 0; ee < x.length; ee++) {
			x[ee] = aTempMap.get(ee);
		}


		//Assemble ordered map for misc tooltips
		AutoMap<String> aOrderedMap = new AutoMap<String>();		
		if (showHatches) {
			aOrderedMap.put(aRequiresMaint);
			//aOrderedMap.put(aRequiresCoreModule);
			if (showPollution) {
				aOrderedMap.put(aRequiresMuffler);				
			}
		}

		if (showMachineType) {
			aOrderedMap.put(getMachineTooltip());			
		}

		if (showPollution) {
			aOrderedMap.put(getPollutionTooltip());				
		}





		//Add Stock Tooltip to bottom of list
		String[] z;	
		z = new String[aOrderedMap.size()];
		for (int ee = 0; ee < z.length; ee++) {
			z[ee] = aOrderedMap.get(ee);
		}

		int a2, a3;
		a2 = x != null ? x.length : 0;
		a3 = z != null ? z.length : 0; 
		String[] aToolTip = new String[(a2 + a3)];
		aToolTip = ArrayUtils.addAll(aToolTip, x);
		aToolTip = ArrayUtils.addAll(aToolTip, z);

		if (aCachedToolTip == null || aCachedToolTip.length <= 0) {
			aCachedToolTip = aToolTip;			
		}		
		return aCachedToolTip;
	}

	public abstract String[] getTooltip();

	public synchronized final MultiblockRequirements getRequirements() {
		return mRequirements;
	}

	//public abstract MultiblockRequirements setRequirements();

	public synchronized final void setRequirementsInternal() {
		//this.mRequirements = setRequirements();
		this.mRequirements = null;
	}

	public int getAmountOfOutputs() {
		return 1;
	}

	public abstract int getMaxParallelRecipes();
	public abstract int getEuDiscountForParallelism();

	@Override
	public boolean isCorrectMachinePart(final ItemStack paramItemStack) {
		return true;
	}

	@Override
	public int getDamageToComponent(final ItemStack paramItemStack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack p0) {
		return false;
	}

	@Override
	public void startSoundLoop(final byte aIndex, final double aX, final double aY, final double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 1) {
			GT_Utility.doSoundAtClient(getSound(), 10, 1.0F, aX, aY, aZ);
		}
	}

	public void startProcess() {
		if(GT_Utility.isStringValid(getSound())) this.sendLoopStart((byte) 1);
	}

	public String getSound() { return ""; }


	public int canBufferOutputs(final GT_Recipe aRecipe, int aParallelRecipes) {
		return canBufferOutputs(aRecipe, aParallelRecipes, true);
	}
	
	public int canBufferOutputs(final GT_Recipe aRecipe, int aParallelRecipes, boolean aAllow16SlotWithoutCheck) {

		Logger.INFO("Determining if we have space to buffer outputs. Parallel: "+aParallelRecipes);

		// Null recipe or a recipe with lots of outputs? 
		// E.G. Gendustry custom comb with a billion centrifuge outputs? 
		// Do it anyway, provided the multi allows it. Default behaviour is aAllow16SlotWithoutCheck = true.
		if (aRecipe == null || aRecipe.mOutputs.length > 16) {
			if (aRecipe == null) {
				return 0;
			}
			else if (aRecipe.mOutputs.length > 16) {
				if (aAllow16SlotWithoutCheck) {
					return aParallelRecipes;					
				}
				else {
					// Do nothing, we want to check this recipe properly.					
				}
			}			
		}		

		// Do we even need to check for item outputs?
		boolean aDoesOutputItems = aRecipe.mOutputs.length > 0;
		// Do we even need to check for fluid outputs?
		boolean aDoesOutputFluids = aRecipe.mFluidOutputs.length > 0;



		/* ========================================
		 * Item Management
		 * ========================================
		 */

		if (aDoesOutputItems) {
			Logger.INFO("We have items to output.");

			// How many slots are free across all the output buses?
			int aInputBusSlotsFree = 0;		

			/*
			 * Create Variables for Item Output 
			 */

			AutoMap<FlexiblePair<ItemStack, Integer>> aItemMap = new AutoMap<FlexiblePair<ItemStack, Integer>>();
			AutoMap<ItemStack> aOutputs = new AutoMap<ItemStack>(aRecipe.mOutputs);

			for (final GT_MetaTileEntity_Hatch_OutputBus tBus : this.mOutputBusses) {
				if (!isValidMetaTileEntity(tBus)) {
					continue;
				}
				final IInventory tBusInv = tBus.getBaseMetaTileEntity();
				for (int i = 0; i < tBusInv.getSizeInventory(); i++) {
					if (tBus.getStackInSlot(i) == null) {
						aInputBusSlotsFree++;
					}
					else {
						ItemStack aT = tBus.getStackInSlot(i);
						int aSize = aT.stackSize;
						aT = aT.copy();
						aT.stackSize = 0;
						aItemMap.put(new FlexiblePair<ItemStack, Integer>(aT, aSize));
					}
				}		
			}

			// Count the slots we need, later we can check if any are able to merge with existing stacks
			int aRecipeSlotsRequired = 0;		

			// A map to hold the items we will be 'inputting' into the output buses. These itemstacks are actually the recipe outputs.
			ConcurrentSet<FlexiblePair<ItemStack, Integer>> aInputMap = new ConcurrentHashSet<FlexiblePair<ItemStack, Integer>>();

			// Iterate over the outputs, calculating require stack spacing they will require.
			for (int i=0;i<aOutputs.size();i++) {
				ItemStack aY = aOutputs.get(i);
				if (aY == null) {
					continue;
				}
				else {
					int aStackSize = aY.stackSize * aParallelRecipes;
					if (aStackSize > 64) {
						int aSlotsNeedsForThisStack = (int) Math.ceil((double) ((float) aStackSize / 64f)); 
						// Should round up and add as many stacks as required nicely.
						aRecipeSlotsRequired += aSlotsNeedsForThisStack;					
						for (int o=0;o<aRecipeSlotsRequired;o++) {
							int aStackToRemove = (aStackSize -= 64) > 64 ? 64 : aStackSize;		
							aY = aY.copy();
							aY.stackSize = 0;
							aInputMap.add(new FlexiblePair<ItemStack, Integer>(aY, aStackToRemove));
						}					
					}
					else {
						// Only requires one slot
						aRecipeSlotsRequired++;
						aY = aY.copy();
						aY.stackSize = 0;
						aInputMap.add(new FlexiblePair<ItemStack, Integer>(aY, aStackSize));
					}	
				}			
			}

			// We have items to add to the output buses. See if any are not full stacks and see if we can make them full.
			if (aInputMap.size() > 0) {
				// Iterate over the current stored items in the Output busses, if any match and are not full, we can try account for merging.
				busItems: for (FlexiblePair<ItemStack, Integer> y : aItemMap) {
					// Iterate over the 'inputs', we can safely remove these as we go.
					outputItems: for (FlexiblePair<ItemStack, Integer> u : aInputMap) {
						// Create local vars for readability.
						ItemStack aOutputBusStack = y.getKey();
						ItemStack aOutputStack = u.getKey();
						// Stacks match, including NBT.
						if (GT_Utility.areStacksEqual(aOutputBusStack, aOutputStack, false)) {							
							// Stack Matches, but it's full, continue.
							if (aOutputBusStack.stackSize >= 64) {
								// This stack is full, no point checking it.
								continue busItems;
							}
							else {
								// We can merge these two stacks without any hassle.
								if ((aOutputBusStack.stackSize + aOutputStack.stackSize) <= 64) {							
									// Update the stack size in the bus storage map.
									y.setValue(aOutputBusStack.stackSize + aOutputStack.stackSize);							
									// Remove the 'input' stack from the recipe outputs, so we don't try count it again.
									aInputMap.remove(u);
									continue outputItems;
								}
								// Stack merging is too much, so we fill this stack, leave the remainder.
								else {
									int aRemainder = (aOutputBusStack.stackSize + aOutputStack.stackSize) - 64;									
									// Update the stack size in the bus storage map.
									y.setValue(64);									
									// Create a new object to iterate over later, with the remainder data;
									FlexiblePair<ItemStack, Integer> t = new FlexiblePair<ItemStack, Integer>(u.getKey(), aRemainder);									
									// Remove the 'input' stack from the recipe outputs, so we don't try count it again.
									aInputMap.remove(u);
									// Add the remainder stack.
									aInputMap.add(t);
									continue outputItems;									
								}
							}							
						}
						else {
							continue outputItems;
						}
					}
				}		
			}

			// We have stacks that did not merge, do we have space for them?
			if (aInputMap.size() > 0) {			
				if (aInputMap.size() > aInputBusSlotsFree) {
					aParallelRecipes = (int) Math.floor((double) aInputBusSlotsFree/aInputMap.size() * aParallelRecipes);
					// We do not have enough free slots in total to accommodate the remaining managed stacks.
					Logger.INFO(" Free: "+aInputBusSlotsFree+", Required: "+aInputMap.size());
					if(aParallelRecipes == 0) {
						Logger.INFO("Failed to find enough space for all item outputs.");
						return 0;
					}
					
				}			
			}			

			/*
			 * End Item Management
			 */

		}





		/* ========================================
		 * Fluid Management
		 * ========================================
		 */



		if (aDoesOutputFluids) {
			Logger.INFO("We have Fluids to output.");
			// How many slots are free across all the output buses?
			int aFluidHatches = 0;
			int aEmptyFluidHatches = 0;
			int aFullFluidHatches = 0;
			// Create Map for Fluid Output 
			ArrayList<Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>> aOutputHatches = new ArrayList<Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>>();
			for (final GT_MetaTileEntity_Hatch_Output tBus : this.mOutputHatches) {
				if (!isValidMetaTileEntity(tBus)) {
					continue;
				}
				aFluidHatches++;			
				// Map the Hatch with the space left for easy checking later.
				if (tBus.getFluid() == null) {
					aOutputHatches.add(new Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>(tBus, null, tBus.getCapacity()));
				}
				else {				
					int aSpaceLeft = tBus.getCapacity() - tBus.getFluidAmount();				
					aOutputHatches.add(new Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>(tBus, tBus.getFluid(), aSpaceLeft));				
				}
			}
			// Create a map of all the fluids we would like to output, we can iterate over this and see how many we can merge into existing hatch stacks.
			ArrayList<FluidStack> aOutputFluids = new ArrayList<FluidStack>();
			// Ugly ass boxing
			aOutputFluids.addAll(new AutoMap<FluidStack>(aRecipe.mFluidOutputs));
			// Iterate the Hatches, updating their 'stored' data.
			//for (Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer> aHatchData : aOutputHatches) {
			for (int i = 0;i<aOutputHatches.size();i++) {		
				// The Hatch Itself
				GT_MetaTileEntity_Hatch_Output aHatch = aOutputHatches.get(i).getValue_1();
				// Fluid in the Hatch
				FluidStack aHatchStack = aOutputHatches.get(i).getValue_2();
				// Space left in Hatch
				int aSpaceLeftInHatch = aHatch.getCapacity() - aHatch.getFluidAmount();		
				// Hatch is full,
				if (aSpaceLeftInHatch <= 0) {
					aFullFluidHatches++;
					aOutputHatches.remove(aOutputHatches.get(i));
					i--;
					continue;
				}			
				// Hatch has space
				else {	
					// Check if any fluids match
					//aFluidMatch: for (FluidStack aOutputStack : aOutputFluids) {
					for(int j = 0;j<aOutputFluids.size();j++) {
						//log(" aHatchStack "+aHatchStack.getLocalizedName()+" aOutput stack "+aOutputStack.getLocalizedName());
						if (GT_Utility.areFluidsEqual(aHatchStack, aOutputFluids.get(j))) {
							int aFluidToPutIntoHatch = aOutputFluids.get(j).amount * aParallelRecipes;
							// Not Enough space to insert all of the fluid.
							// We fill this hatch and add a smaller Fluidstack back to the iterator.
							if (aSpaceLeftInHatch < aFluidToPutIntoHatch) {	
								// Copy existing Hatch Stack
								FluidStack aNewHatchStack = aHatchStack.copy();
								aNewHatchStack.amount = 0;
								// Copy existing Hatch Stack again
								FluidStack aNewOutputStack = aHatchStack.copy();
								aNewOutputStack.amount = 0;								
								// How much fluid do we have left after we fill the hatch?
								int aFluidLeftAfterInsert = aFluidToPutIntoHatch - aSpaceLeftInHatch;								
								// Set new stacks to appropriate values
								aNewHatchStack.amount = aHatch.getCapacity();
								aNewOutputStack.amount = aFluidLeftAfterInsert;								
								// Remove fluid from output list, merge success
								aOutputFluids.remove(aOutputFluids.get(j));	
								j--;
								// Remove hatch from hatch list, data is now invalid.
								aOutputHatches.remove(aOutputHatches.get(i));	
								i--;
								// Add remaining Fluid to Output list
								aOutputFluids.add(aNewOutputStack);								
								// Re-add hatch to hatch list, with new data.
								//Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer> aNewHatchData = new Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>(aHatch, aNewHatchStack, aNewHatchStack.amount);
								//aOutputHatches.add(aNewHatchData);								
								break;								
							}
							// We can fill this hatch perfectly (rare case), may as well add it directly to the full list.
							else if (aSpaceLeftInHatch == aFluidToPutIntoHatch) {
								// Copy Old Stack
								FluidStack aNewHatchStack = aHatchStack.copy();
								// Add in amount from output stack
								aNewHatchStack.amount += aFluidToPutIntoHatch;
								// Remove fluid from output list, merge success
								aOutputFluids.remove(aOutputFluids.get(j));
								j--;
								// Remove hatch from hatch list, data is now invalid.
								aOutputHatches.remove(aOutputHatches.get(i));
								i--;
								// Re-add hatch to hatch list, with new data.
								Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer> aNewHatchData = new Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>(aHatch, aNewHatchStack, aNewHatchStack.amount);
								aOutputHatches.add(aNewHatchData);
								break;
							}
							// We have more space than we need to merge, so we remove the stack from the output list and update the hatch list.
							else {	
								// Copy Old Stack
								FluidStack aNewHatchStack = aHatchStack.copy();
								// Add in amount from output stack
								aNewHatchStack.amount += aFluidToPutIntoHatch;
								// Remove fluid from output list, merge success
								aOutputFluids.remove(aOutputFluids.get(j));
								j--;
								// Remove hatch from hatch list, data is now invalid.
								aOutputHatches.remove(aOutputHatches.get(i));
								i--;
								// Re-add hatch to hatch list, with new data.
								Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer> aNewHatchData = new Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>(aHatch, aNewHatchStack, aNewHatchStack.amount);
								aOutputHatches.add(aNewHatchData);
								// Check next fluid
								continue;
							}

						}
						else {
							continue;
						}
					}					
				}
			}

			for (Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer> aFreeHatchCheck : aOutputHatches) {
				// Free Hatch
				if (aFreeHatchCheck.getValue_2() == null || aFreeHatchCheck.getValue_3() == 0 || aFreeHatchCheck.getValue_1().getFluid() == null) {
					aEmptyFluidHatches++;
				}
			}

			// We have Fluid Stacks we did not merge. Do we have space?
			Logger.INFO("fluids to output "+aOutputFluids.size()+" empty hatches "+aEmptyFluidHatches);
			if (aOutputFluids.size() > 0) {
				// Not enough space to add fluids.
				if (aOutputFluids.size() > aEmptyFluidHatches) {
					aParallelRecipes = (int) Math.floor((double) aEmptyFluidHatches/aOutputFluids.size() * aParallelRecipes);
					Logger.INFO("Failed to find enough space for all fluid outputs. Free: "+aEmptyFluidHatches+", Required: "+aOutputFluids.size());
					return 0;
					
				}
			}

			/*
			 * End Fluid Management
			 */			
		}

		return aParallelRecipes;
	}

	/**
	 * A Static {@link Method} object which holds the current status of logging.
	 */
	public static Method aLogger = null;

	public void log(String s) {
		boolean reset = false;
		if (reset || aLogger == null) {
			if (!AsmConfig.disableAllLogging) {
				aLogger = ReflectionUtils.getMethod(
						Logger.class, "INFO", String.class
						);
			}
			else {
				aLogger = ReflectionUtils.getMethod(
						Logger.class, "MACHINE_INFO", String.class
						);
			}
		}
		try {
			aLogger.invoke(null, s);
		}
		catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	public boolean checkRecipeGeneric() {
		return checkRecipeGeneric(1, 100, 0);
	}

	public boolean checkRecipeGeneric(int aMaxParallelRecipes, int aEUPercent, int aSpeedBonusPercent) {
		return checkRecipeGeneric(aMaxParallelRecipes, aEUPercent, aSpeedBonusPercent, 10000);
	}

	public boolean checkRecipeGeneric(int aMaxParallelRecipes, int aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll) {
		ArrayList<ItemStack> tItems = getStoredInputs();
		ArrayList<FluidStack> tFluids = getStoredFluids();
		ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
		FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);
		return checkRecipeGeneric(tItemInputs, tFluidInputs, aMaxParallelRecipes, aEUPercent, aSpeedBonusPercent, aOutputChanceRoll);
	}


	public boolean checkRecipeGeneric(GT_Recipe aRecipe, 
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll) {		
		if (aRecipe == null) {
			return false;
		}		
		ArrayList<ItemStack> tItems = getStoredInputs();
		ArrayList<FluidStack> tFluids = getStoredFluids();
		ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
		FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);
		return checkRecipeGeneric(tItemInputs, tFluidInputs, aMaxParallelRecipes, aEUPercent, aSpeedBonusPercent, aOutputChanceRoll, aRecipe);
	}

	public boolean checkRecipeGeneric(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll) {
		return checkRecipeGeneric(aItemInputs, aFluidInputs, aMaxParallelRecipes, aEUPercent, aSpeedBonusPercent, aOutputChanceRoll, null);
	}


	/*
	 * public boolean checkRecipeGeneric( ItemStack[] aItemInputs, FluidStack[]
	 * aFluidInputs, int aMaxParallelRecipes, int aEUPercent, int
	 * aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) { // Based on
	 * the Processing Array. A bit overkill, but very flexible.
	 * 
	 * 
	 * if (this.doesMachineBoostOutput()) { log("Boosting."); return
	 * checkRecipeBoostedOutputs(aItemInputs, aFluidInputs, aMaxParallelRecipes,
	 * aEUPercent, aSpeedBonusPercent, aOutputChanceRoll, aRecipe); }
	 * 
	 * 
	 * //Control Core to control the Multiblocks behaviour. int aControlCoreTier =
	 * getControlCoreTier();
	 * 
	 * //If no core, return false; if (aControlCoreTier > 0) {
	 * log("Control core found."); }
	 * 
	 * 
	 * // Reset outputs and progress stats this.mEUt = 0; this.mMaxProgresstime = 0;
	 * this.mOutputItems = new ItemStack[]{}; this.mOutputFluids = new
	 * FluidStack[]{};
	 * 
	 * long tVoltage = getMaxInputVoltage(); byte tTier = (byte) Math.max(1,
	 * GT_Utility.getTier(tVoltage)); log("Running checkRecipeGeneric(0)");
	 * 
	 * //Check to see if Voltage Tier > Control Core Tier if (tTier >
	 * aControlCoreTier) {
	 * log("Control core found is lower tier than power tier. OK"); tTier = (byte)
	 * aControlCoreTier; }
	 * 
	 * tTier = (byte) MathUtils.getValueWithinRange(tTier, 0, 9);
	 * 
	 * GT_Recipe tRecipe = aRecipe != null ? aRecipe : findRecipe(
	 * getBaseMetaTileEntity(), mLastRecipe, false,
	 * gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);
	 * 
	 * log("Running checkRecipeGeneric(1)"); // Remember last recipe - an
	 * optimization for findRecipe() this.mLastRecipe = tRecipe;
	 * 
	 * if (tRecipe == null) { log("BAD RETURN - 1"); return false; }
	 * 
	 * if (!this.canBufferOutputs(tRecipe, aMaxParallelRecipes)) {
	 * log("BAD RETURN - 2"); return false; }
	 * 
	 * // EU discount float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f; float
	 * tTotalEUt = 0.0f;
	 * 
	 * int parallelRecipes = 0;
	 * 
	 * log("parallelRecipes: "+parallelRecipes);
	 * log("aMaxParallelRecipes: "+aMaxParallelRecipes);
	 * log("tTotalEUt: "+tTotalEUt); log("tVoltage: "+tVoltage);
	 * log("tRecipeEUt: "+tRecipeEUt); Logger.INFO("EU1: "+tRecipeEUt); // Count
	 * recipes to do in parallel, consuming input items and fluids and considering
	 * input voltage limits for (; parallelRecipes < aMaxParallelRecipes &&
	 * tTotalEUt < (tVoltage - tRecipeEUt); parallelRecipes++) { if
	 * (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
	 * log("Broke at "+parallelRecipes+"."); break; }
	 * log("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+"."); tTotalEUt
	 * += tRecipeEUt; Logger.INFO("EU2: "+tTotalEUt); }
	 * 
	 * if (parallelRecipes == 0) { log("BAD RETURN - 3"); return false; }
	 * 
	 * Logger.INFO("EU3: "+tTotalEUt);
	 * 
	 * // -- Try not to fail after this point - inputs have already been consumed!
	 * --
	 * 
	 * 
	 * // Convert speed bonus to duration multiplier // e.g. 100% speed bonus = 200%
	 * speed = 100%/200% = 50% recipe duration. aSpeedBonusPercent = Math.max(-99,
	 * aSpeedBonusPercent); float tTimeFactor = 100.0f / (100.0f +
	 * aSpeedBonusPercent); this.mMaxProgresstime = (int)(tRecipe.mDuration *
	 * tTimeFactor * 10000);
	 * 
	 * int aTempEu = (int) Math.floor(tTotalEUt); Logger.INFO("EU4: "+aTempEu);
	 * this.mEUt = (int) aTempEu;
	 * 
	 * 
	 * this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
	 * this.mEfficiencyIncrease = 10000;
	 * 
	 * // Overclock if (this.mEUt <= 16) { this.mEUt = (this.mEUt * (1 << tTier - 1)
	 * * (1 << tTier - 1)); this.mMaxProgresstime = (this.mMaxProgresstime / (1 <<
	 * tTier - 1)); } else { while (this.mEUt <=
	 * gregtech.api.enums.GT_Values.V[(tTier - 1)]) { this.mEUt *= 4;
	 * this.mMaxProgresstime /= 2; } }
	 * 
	 * if (this.mEUt > 0) { this.mEUt = (-this.mEUt); }
	 * 
	 * this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
	 * 
	 * // Collect fluid outputs FluidStack[] tOutputFluids = new
	 * FluidStack[tRecipe.mFluidOutputs.length]; for (int h = 0; h <
	 * tRecipe.mFluidOutputs.length; h++) { if (tRecipe.getFluidOutput(h) != null) {
	 * tOutputFluids[h] = tRecipe.getFluidOutput(h).copy(); tOutputFluids[h].amount
	 * *= parallelRecipes; } }
	 * 
	 * // Collect output item types ItemStack[] tOutputItems = new
	 * ItemStack[tRecipe.mOutputs.length]; for (int h = 0; h <
	 * tRecipe.mOutputs.length; h++) { if (tRecipe.getOutput(h) != null) {
	 * tOutputItems[h] = tRecipe.getOutput(h).copy(); tOutputItems[h].stackSize = 0;
	 * } }
	 * 
	 * // Set output item stack sizes (taking output chance into account) for (int f
	 * = 0; f < tOutputItems.length; f++) { if (tRecipe.mOutputs[f] != null &&
	 * tOutputItems[f] != null) { for (int g = 0; g < parallelRecipes; g++) { if
	 * (getBaseMetaTileEntity().getRandomNumber(aOutputChanceRoll) <
	 * tRecipe.getOutputChance(f)) tOutputItems[f].stackSize +=
	 * tRecipe.mOutputs[f].stackSize; } } }
	 * 
	 * tOutputItems = removeNulls(tOutputItems);
	 * 
	 * // Sanitize item stack size, splitting any stacks greater than max stack size
	 * List<ItemStack> splitStacks = new ArrayList<ItemStack>(); for (ItemStack
	 * tItem : tOutputItems) { while (tItem.getMaxStackSize() < tItem.stackSize) {
	 * ItemStack tmp = tItem.copy(); tmp.stackSize = tmp.getMaxStackSize();
	 * tItem.stackSize = tItem.stackSize - tItem.getMaxStackSize();
	 * splitStacks.add(tmp); } }
	 * 
	 * if (splitStacks.size() > 0) { ItemStack[] tmp = new
	 * ItemStack[splitStacks.size()]; tmp = splitStacks.toArray(tmp); tOutputItems =
	 * ArrayUtils.addAll(tOutputItems, tmp); }
	 * 
	 * // Strip empty stacks List<ItemStack> tSList = new ArrayList<ItemStack>();
	 * for (ItemStack tS : tOutputItems) { if (tS.stackSize > 0) tSList.add(tS); }
	 * tOutputItems = tSList.toArray(new ItemStack[tSList.size()]);
	 * 
	 * // Commit outputs this.mOutputItems = tOutputItems; this.mOutputFluids =
	 * tOutputFluids; updateSlots();
	 * 
	 * // Play sounds (GT++ addition - GT multiblocks play no sounds)
	 * startProcess();
	 * 
	 * log("GOOD RETURN - 1"); return true; }
	 */

	public long getMaxInputEnergy() {
		long rEnergy = 0;
		if (mEnergyHatches.size() < 2) // so it only takes 1 amp is only 1 hatch is present so it works like most gt multies
			return mEnergyHatches.get(0).getBaseMetaTileEntity().getInputVoltage();
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) rEnergy += tHatch.getBaseMetaTileEntity().getInputVoltage() * tHatch.getBaseMetaTileEntity().getInputAmperage();
        return rEnergy;
	}

	public boolean checkRecipeGeneric(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) {
		// Based on the Processing Array. A bit overkill, but very flexible.		

		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[]{};
		this.mOutputFluids = new FluidStack[]{};

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		long tEnergy = getMaxInputEnergy();
		log("Running checkRecipeGeneric(0)");
		
		GT_Recipe tRecipe = findRecipe(
				getBaseMetaTileEntity(), mLastRecipe, false,
				gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);		
		
		log("Running checkRecipeGeneric(1)");
		// Remember last recipe - an optimization for findRecipe()
		this.mLastRecipe = tRecipe;

		if (tRecipe == null) {
			log("BAD RETURN - 1");
			return false;
		}
		

		/*
		 *  Check for Special Behaviours
		 */
		
		// First populate the map if we need to.
		if (mCustomBehviours.isEmpty()) {
			mCustomBehviours.putAll(Multiblock_API.getSpecialBehaviourItemMap());
		}
		
		// We have a special slot object in the recipe
		if (tRecipe.mSpecialItems != null) {
			// The special slot is an item
			if (tRecipe.mSpecialItems instanceof ItemStack) {
				// Make an Itemstack instance of this.
				ItemStack aSpecialStack = (ItemStack) tRecipe.mSpecialItems;	
				// Check if this item is in an input bus.			
				boolean aDidFindMatch = false;
				for (ItemStack aInputItemsToCheck : aItemInputs) {
					// If we find a matching stack, continue.		
					if (GT_Utility.areStacksEqual(aSpecialStack, aInputItemsToCheck, false)) {
						// Iterate all special behaviour items, to see if we need to utilise one.
						aDidFindMatch = true;
						break;
					}					
				}	
				// Try prevent needless iteration loops if we don't have the required inputs at all.
				if (aDidFindMatch) {
					// Iterate all special behaviour items, to see if we need to utilise one.
					for (SpecialMultiBehaviour aBehaviours : mCustomBehviours.values()) {	
						// Found a match, let's adjust this recipe now.
						if (aBehaviours.isTriggerItem(aSpecialStack)) {						
							// Adjust this recipe to suit special item
							aMaxParallelRecipes = aBehaviours.getMaxParallelRecipes();
							aEUPercent = aBehaviours.getEUPercent();
							aSpeedBonusPercent = aBehaviours.getSpeedBonusPercent();
							aOutputChanceRoll = aBehaviours.getOutputChanceRoll();
							break;						
						}					
					}
				}							
			}			
		}		
		
		aMaxParallelRecipes = this.canBufferOutputs(tRecipe, aMaxParallelRecipes);
		if (aMaxParallelRecipes == 0) {
			log("BAD RETURN - 2");
			return false;
		}

		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;

		int parallelRecipes = 0;

		log("parallelRecipes: "+parallelRecipes);
		log("aMaxParallelRecipes: "+aMaxParallelRecipes);
		log("tTotalEUt: "+tTotalEUt);
		log("tVoltage: "+tVoltage);
		log("tRecipeEUt: "+tRecipeEUt);
		// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
				log("Broke at "+parallelRecipes+".");
				break;
			}
			log("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+".");
			tTotalEUt += tRecipeEUt;
		}

		if (parallelRecipes == 0) {
			log("BAD RETURN - 3");
			return false;
		}

		// -- Try not to fail after this point - inputs have already been consumed! --



		// Convert speed bonus to duration multiplier
		// e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
		aSpeedBonusPercent = Math.max(-99, aSpeedBonusPercent);
		float tTimeFactor = 100.0f / (100.0f + aSpeedBonusPercent);
		this.mMaxProgresstime = (int)(tRecipe.mDuration * tTimeFactor);

		this.mEUt = (int)Math.ceil(tTotalEUt);

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

		this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

		// Collect fluid outputs
		FluidStack[] tOutputFluids = new FluidStack[tRecipe.mFluidOutputs.length];
		for (int h = 0; h < tRecipe.mFluidOutputs.length; h++) {
			if (tRecipe.getFluidOutput(h) != null) {
				tOutputFluids[h] = tRecipe.getFluidOutput(h).copy();
				tOutputFluids[h].amount *= parallelRecipes;
			}
		}

		// Collect output item types
		ItemStack[] tOutputItems = new ItemStack[tRecipe.mOutputs.length];
		for (int h = 0; h < tRecipe.mOutputs.length; h++) {
			if (tRecipe.getOutput(h) != null) {
				tOutputItems[h] = tRecipe.getOutput(h).copy();
				tOutputItems[h].stackSize = 0;
			}
		}

		// Set output item stack sizes (taking output chance into account)
		for (int f = 0; f < tOutputItems.length; f++) {
			if (tRecipe.mOutputs[f] != null && tOutputItems[f] != null) {
				for (int g = 0; g < parallelRecipes; g++) {
					if (getBaseMetaTileEntity().getRandomNumber(aOutputChanceRoll) < tRecipe.getOutputChance(f))
						tOutputItems[f].stackSize += tRecipe.mOutputs[f].stackSize;
				}
			}
		}

		tOutputItems = removeNulls(tOutputItems);

		// Sanitize item stack size, splitting any stacks greater than max stack size
		List<ItemStack> splitStacks = new ArrayList<ItemStack>();
		for (ItemStack tItem : tOutputItems) {
			while (tItem.getMaxStackSize() < tItem.stackSize) {
				ItemStack tmp = tItem.copy();
				tmp.stackSize = tmp.getMaxStackSize();
				tItem.stackSize = tItem.stackSize - tItem.getMaxStackSize();
				splitStacks.add(tmp);
			}
		}

		if (splitStacks.size() > 0) {
			ItemStack[] tmp = new ItemStack[splitStacks.size()];
			tmp = splitStacks.toArray(tmp);
			tOutputItems = ArrayUtils.addAll(tOutputItems, tmp);
		}

		// Strip empty stacks
		List<ItemStack> tSList = new ArrayList<ItemStack>();
		for (ItemStack tS : tOutputItems) {
			if (tS.stackSize > 0) tSList.add(tS);
		}
		tOutputItems = tSList.toArray(new ItemStack[tSList.size()]);

		// Commit outputs
		this.mOutputItems = tOutputItems;
		this.mOutputFluids = tOutputFluids;
		updateSlots();

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		log("GOOD RETURN - 1");
		return true;
	}





	/*
	 * Here we handle recipe boosting, which grants additional output %'s to recipes that do not have 100%.
	 */

	private boolean mHasBoostedCurrentRecipe = false;
	private GT_Recipe mBoostedRecipe = null;
	private ItemStack[] mInputVerificationForBoosting = null;

	/**
	 * Does this machine boost it's output?
	 * @return - if true, gives additional % to output chances.
	 */
	protected boolean doesMachineBoostOutput() {
		return false;
	}



	private int boostOutput(int aAmount) {		
		if (aAmount <= 0) {
			return 10000;
		}		
		if (aAmount <= 250) {
			aAmount += MathUtils.randInt(Math.max(aAmount/2, 1), aAmount*2);
		}
		else if (aAmount <= 500) {
			aAmount += MathUtils.randInt(Math.max(aAmount/2, 1), aAmount*2);			
		}
		else if (aAmount <= 750) {
			aAmount += MathUtils.randInt(Math.max(aAmount/2, 1), aAmount*2);			
		}
		else if (aAmount <= 1000) {
			aAmount = (aAmount*2);
		}
		else if (aAmount <= 1500) {
			aAmount = (aAmount*2);			
		}
		else if (aAmount <= 2000) {
			aAmount = (int) (aAmount*1.5);		
		}
		else if (aAmount <= 3000) {
			aAmount = (int) (aAmount*1.5);			
		}
		else if (aAmount <= 4000) {
			aAmount = (int) (aAmount*1.2);			
		}
		else if (aAmount <= 5000) {
			aAmount = (int) (aAmount*1.2);			
		}
		else if (aAmount <= 7000) {
			aAmount = (int) (aAmount*1.2);			
		}
		else if (aAmount <= 9000) {
			aAmount = (int) (aAmount*1.1);			
		}		
		return Math.min(10000, aAmount);
	}

	public GT_Recipe generateAdditionalOutputForRecipe(GT_Recipe aRecipe) {
		AutoMap<Integer> aNewChances = new AutoMap<Integer>();
		for (int chance : aRecipe.mChances) {
			aNewChances.put(boostOutput(chance));
		}
		GT_Recipe aClone = aRecipe.copy();
		int[] aTemp = new int[aNewChances.size()];
		int slot = 0;
		for (int g : aNewChances) {
			aTemp[slot] = g;
			slot++;
		}		
		aClone.mChances = aTemp;		
		return aClone;
	}


	/**
	 * Processes recipes but provides a bonus to the output % of items if they are < 100%.
	 * 
	 * @param aItemInputs
	 * @param aFluidInputs
	 * @param aMaxParallelRecipes
	 * @param aEUPercent
	 * @param aSpeedBonusPercent
	 * @param aOutputChanceRoll
	 * @param aRecipe
	 * @return
	 */
	public boolean checkRecipeBoostedOutputs(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) {

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		long tEnergy = getMaxInputEnergy();
		log("Running checkRecipeGeneric(0)");

		GT_Recipe tRecipe = aRecipe != null ? aRecipe : findRecipe(
				getBaseMetaTileEntity(), mLastRecipe, false,
				gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);

		log("Running checkRecipeGeneric(1)");

		//First we check whether or not we have an input cached for boosting.
		//If not, we set it to the current recipe.
		//If we do, we compare it against the current recipe, if thy are the same, we try return a boosted recipe, if not, we boost a new recipe.
		boolean isRecipeInputTheSame = false;	

		//No cached recipe inputs, assume first run.
		if (mInputVerificationForBoosting == null) {
			mInputVerificationForBoosting = tRecipe.mInputs;	
			isRecipeInputTheSame = true;
		}
		//If the inputs match, we are good.
		else {
			if (tRecipe.mInputs == mInputVerificationForBoosting) {
				isRecipeInputTheSame = true;
			}
			else {
				isRecipeInputTheSame = false;
			}
		}

		//Inputs are the same, let's see if there's a boosted version.
		if (isRecipeInputTheSame) {
			//Yes, let's just set that as the recipe
			if (mHasBoostedCurrentRecipe && mBoostedRecipe != null) {
				tRecipe = mBoostedRecipe;
			}
			//We have yet to generate a new boosted recipe
			else {
				GT_Recipe aBoostedRecipe = this.generateAdditionalOutputForRecipe(tRecipe);
				if (aBoostedRecipe != null) {
					mBoostedRecipe = aBoostedRecipe;
					mHasBoostedCurrentRecipe = true;
					tRecipe = mBoostedRecipe;
				}
				//Bad boost
				else {
					mBoostedRecipe = null;
					mHasBoostedCurrentRecipe = false;				
				}
			}
		}
		//We have changed inputs, so we should generate a new boosted recipe
		else {
			GT_Recipe aBoostedRecipe = this.generateAdditionalOutputForRecipe(tRecipe);
			if (aBoostedRecipe != null) {
				mBoostedRecipe = aBoostedRecipe;
				mHasBoostedCurrentRecipe = true;
				tRecipe = mBoostedRecipe;
			}
			//Bad boost
			else {
				mBoostedRecipe = null;
				mHasBoostedCurrentRecipe = false;				
			}
		}

		//Bad modify, let's just use the original recipe.
		if (!mHasBoostedCurrentRecipe || mBoostedRecipe == null) {
			tRecipe = aRecipe != null ? aRecipe : findRecipe(
					getBaseMetaTileEntity(), mLastRecipe, false,
					gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);
		}		

		// Remember last recipe - an optimization for findRecipe()
		this.mLastRecipe = tRecipe;

		if (tRecipe == null) {
			log("BAD RETURN - 1");
			return false;
		}

		aMaxParallelRecipes = this.canBufferOutputs(tRecipe, aMaxParallelRecipes);
		if (aMaxParallelRecipes == 0) {
			log("BAD RETURN - 2");
			return false;
		}

		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;

		int parallelRecipes = 0;

		log("parallelRecipes: "+parallelRecipes);
		log("aMaxParallelRecipes: "+aMaxParallelRecipes);
		log("tTotalEUt: "+tTotalEUt);
		log("tVoltage: "+tVoltage);
		log("tRecipeEUt: "+tRecipeEUt);
		// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
				log("Broke at "+parallelRecipes+".");
				break;
			}
			log("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+".");
			tTotalEUt += tRecipeEUt;
		}

		if (parallelRecipes == 0) {
			log("BAD RETURN - 3");
			return false;
		}

		// -- Try not to fail after this point - inputs have already been consumed! --


		// Convert speed bonus to duration multiplier
		// e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
		aSpeedBonusPercent = Math.max(-99, aSpeedBonusPercent);
		float tTimeFactor = 100.0f / (100.0f + aSpeedBonusPercent);
		this.mMaxProgresstime = (int)(tRecipe.mDuration * tTimeFactor);

		this.mEUt = (int)Math.ceil(tTotalEUt);

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

		this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

		// Collect fluid outputs
		FluidStack[] tOutputFluids = new FluidStack[tRecipe.mFluidOutputs.length];
		for (int h = 0; h < tRecipe.mFluidOutputs.length; h++) {
			if (tRecipe.getFluidOutput(h) != null) {
				tOutputFluids[h] = tRecipe.getFluidOutput(h).copy();
				tOutputFluids[h].amount *= parallelRecipes;
			}
		}

		// Collect output item types
		ItemStack[] tOutputItems = new ItemStack[tRecipe.mOutputs.length];
		for (int h = 0; h < tRecipe.mOutputs.length; h++) {
			if (tRecipe.getOutput(h) != null) {
				tOutputItems[h] = tRecipe.getOutput(h).copy();
				tOutputItems[h].stackSize = 0;
			}
		}

		// Set output item stack sizes (taking output chance into account)
		for (int f = 0; f < tOutputItems.length; f++) {
			if (tRecipe.mOutputs[f] != null && tOutputItems[f] != null) {
				for (int g = 0; g < parallelRecipes; g++) {
					if (getBaseMetaTileEntity().getRandomNumber(aOutputChanceRoll) < tRecipe.getOutputChance(f))
						tOutputItems[f].stackSize += tRecipe.mOutputs[f].stackSize;
				}
			}
		}

		tOutputItems = removeNulls(tOutputItems);

		// Sanitize item stack size, splitting any stacks greater than max stack size
		List<ItemStack> splitStacks = new ArrayList<ItemStack>();
		for (ItemStack tItem : tOutputItems) {
			while (tItem.getMaxStackSize() < tItem.stackSize) {
				ItemStack tmp = tItem.copy();
				tmp.stackSize = tmp.getMaxStackSize();
				tItem.stackSize = tItem.stackSize - tItem.getMaxStackSize();
				splitStacks.add(tmp);
			}
		}

		if (splitStacks.size() > 0) {
			ItemStack[] tmp = new ItemStack[splitStacks.size()];
			tmp = splitStacks.toArray(tmp);
			tOutputItems = ArrayUtils.addAll(tOutputItems, tmp);
		}

		// Strip empty stacks
		List<ItemStack> tSList = new ArrayList<ItemStack>();
		for (ItemStack tS : tOutputItems) {
			if (tS.stackSize > 0) tSList.add(tS);
		}
		tOutputItems = tSList.toArray(new ItemStack[tSList.size()]);

		// Commit outputs
		this.mOutputItems = tOutputItems;
		this.mOutputFluids = tOutputFluids;
		updateSlots();

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		log("GOOD RETURN - 1");
		return true;
	}


























	public boolean isMachineRunning() {
		boolean aRunning = this.getBaseMetaTileEntity().isActive();
		log("Queried Multiblock is currently running: "+aRunning);
		return aRunning;
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity,
			final long aTick) {

		//Time Counter
		if (aBaseMetaTileEntity.isServerSide()){
			this.mTotalRunTime++;
		}

		if (aBaseMetaTileEntity.isServerSide()) {
			if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
				this.mChargeHatches.clear();
				this.mDischargeHatches.clear();
				this.mControlCoreBus.clear();
				this.mAirIntakes.clear();
				this.mTecTechEnergyHatches.clear();
				this.mTecTechDynamoHatches.clear();
			}
		}


		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public void explodeMultiblock() {
		MetaTileEntity tTileEntity;
		for (final Iterator<GT_MetaTileEntity_Hatch_InputBattery> localIterator = this.mChargeHatches
				.iterator(); localIterator.hasNext(); tTileEntity
				.getBaseMetaTileEntity()
				.doExplosion(gregtech.api.enums.GT_Values.V[8])) {
			tTileEntity = localIterator.next();
		}
		tTileEntity = null;
		for (final Iterator<GT_MetaTileEntity_Hatch_OutputBattery> localIterator = this.mDischargeHatches
				.iterator(); localIterator.hasNext(); tTileEntity
				.getBaseMetaTileEntity()
				.doExplosion(gregtech.api.enums.GT_Values.V[8])) {
			tTileEntity = localIterator.next();
		}
		tTileEntity = null;
		for (final Iterator<GT_MetaTileEntity_Hatch> localIterator = this.mTecTechDynamoHatches
				.iterator(); localIterator.hasNext(); tTileEntity
				.getBaseMetaTileEntity()
				.doExplosion(gregtech.api.enums.GT_Values.V[8])) {
			tTileEntity = localIterator.next();
		}
		tTileEntity = null;
		for (final Iterator<GT_MetaTileEntity_Hatch> localIterator = this.mTecTechEnergyHatches
				.iterator(); localIterator.hasNext(); tTileEntity
				.getBaseMetaTileEntity()
				.doExplosion(gregtech.api.enums.GT_Values.V[8])) {
			tTileEntity = localIterator.next();
		}



		super.explodeMultiblock();
	}

	protected int getGUICircuit(ItemStack[] t) {
		Item g = CI.getNumberedCircuit(0).getItem();
		ItemStack guiSlot = this.mInventory[1];
		int mMode = -1;		
		if (guiSlot != null && guiSlot.getItem() == g) {
			this.mInternalCircuit = true;	
			return guiSlot.getItemDamage();
		}
		else {
			this.mInternalCircuit = false;
		}

		if (!this.mInternalCircuit) {
			for (ItemStack j : t) {
				if (j.getItem() == g) {
					mMode = j.getItemDamage();
					break;
				}
			}
		}
		return mMode;
	}

	protected ItemStack getGUIItemStack() {
		ItemStack guiSlot = this.mInventory[1];		
		return guiSlot;
	}

	protected boolean setGUIItemStack(ItemStack aNewGuiSlotContents) {
		boolean result = false;
		if (this.mInventory[1] == null) {
			this.mInventory[1] = aNewGuiSlotContents != null ? aNewGuiSlotContents.copy() : null;
			this.depleteInput(aNewGuiSlotContents);
			this.updateSlots();
			result = true;
		}	
		return result;
	}

	protected boolean clearGUIItemSlot() {
		return setGUIItemStack(null);
	}


	public ItemStack findItemInInventory(Item aSearchStack) {
		return findItemInInventory(aSearchStack, 0);
	}

	public ItemStack findItemInInventory(Item aSearchStack, int aMeta) {
		return findItemInInventory(ItemUtils.simpleMetaStack(aSearchStack, aMeta, 1));
	}

	public ItemStack findItemInInventory(ItemStack aSearchStack) {
		if (aSearchStack != null && this.mInputBusses.size() > 0) {
			for (GT_MetaTileEntity_Hatch_InputBus bus : this.mInputBusses) {
				if (bus != null) {
					for (ItemStack uStack : bus.mInventory) {
						if (uStack != null) {
							if (aSearchStack.getClass().isInstance(uStack.getItem())) {
								return uStack;
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public void updateSlots() {
		for (final GT_MetaTileEntity_Hatch_InputBattery tHatch : this.mChargeHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				tHatch.updateSlots();
			}
		}
		for (final GT_MetaTileEntity_Hatch_OutputBattery tHatch : this.mDischargeHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				tHatch.updateSlots();
			}
		}
		super.updateSlots();
	}

	public boolean isToolCreative(ItemStack mStack){
		Materials t1 = GT_MetaGenerated_Tool.getPrimaryMaterial(mStack);
		Materials t2 = GT_MetaGenerated_Tool.getSecondaryMaterial(mStack);
		if (t1 == Materials._NULL && t2 == Materials._NULL){
			return true;
		}
		return false;
	}

	/**
	 * Causes a Random Maint. Issue.
	 * @return {@link boolean} - Returns whether or not an issue was caused, should always be true.
	 */
	public boolean causeMaintenanceIssue() {
		boolean b = false;
		switch (this.getBaseMetaTileEntity().getRandomNumber(6)) {
			case 0 : {
				this.mWrench = false;
				b = true;
				break;
			}
			case 1 : {
				this.mScrewdriver = false;
				b = true;
				break;
			}
			case 2 : {
				this.mSoftHammer = false;
				b = true;
				break;
			}
			case 3 : {
				this.mHardHammer = false;
				b = true;
				break;
			}
			case 4 : {
				this.mSolderingTool = false;
				b = true;
				break;
			}
			case 5 : {
				this.mCrowbar = false;
				b = true;
				break;
			}
		}
		return b;
	}

	public void fixAllMaintenanceIssue() {
		this.mCrowbar = true;
		this.mWrench = true;
		this.mHardHammer = true;
		this.mSoftHammer = true;
		this.mSolderingTool = true;
		this.mScrewdriver = true;		
	}


	public <E> boolean addToMachineListInternal(ArrayList<E> aList, final IMetaTileEntity aTileEntity,
			final int aBaseCasingIndex) {		
		if (aTileEntity == null) {
			return false;
		}		

		//Check type
		/*
		 * Class <?> aHatchType = ReflectionUtils.getTypeOfGenericObject(aList); if
		 * (!aHatchType.isInstance(aTileEntity)) { return false; }
		 */


		if (aList.isEmpty()) {
			if (aTileEntity instanceof GT_MetaTileEntity_Hatch) {
				if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
					log("Adding " + aTileEntity.getInventoryName() + " at " + new BlockPos(aTileEntity.getBaseMetaTileEntity()).getLocationString());				
				}
				updateTexture(aTileEntity, aBaseCasingIndex);
				return aList.add((E) aTileEntity);
			}
		} else {
			IGregTechTileEntity aCur = aTileEntity.getBaseMetaTileEntity();
			BlockPos aCurPos = new BlockPos(aCur);
			boolean aExists = false;
			for (E m : aList) {
				IGregTechTileEntity b = ((IMetaTileEntity) m).getBaseMetaTileEntity();
				if (b != null) {
					BlockPos aPos = new BlockPos(b);
					if (aPos != null) {
						if (aCurPos.equals(aPos)) {
							if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
								log("Found Duplicate "+b.getInventoryName()+" at " + aPos.getLocationString());
							}
							return false;
						}
					}
				}
			}
			if (aTileEntity instanceof GT_MetaTileEntity_Hatch) {
				if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
					log("Adding " + aCur.getInventoryName() + " at " + aCurPos.getLocationString());
				}
				updateTexture(aTileEntity, aBaseCasingIndex);
				return aList.add((E) aTileEntity);
			}
		}
		return false;
	}

	public int getControlCoreTier() {	

		//Always return best tier if config is off.
		/*boolean aCoresConfig = gtPlusPlus.core.lib.CORE.ConfigSwitches.requireControlCores;
		if (!aCoresConfig) {
			return 10;
		}*/

		if (mControlCoreBus.isEmpty()) {
			log("No Control Core Modules Found.");
			return 0;
		}		
		GT_MetaTileEntity_Hatch_ControlCore i = getControlCoreBus();
		if (i != null) {
			ItemStack x = i.mInventory[0];
			if (x != null) {
				return x.getItemDamage();
			}
		}
		log("Control Core Module was null.");
		return 0;
	}

	public GT_MetaTileEntity_Hatch_ControlCore getControlCoreBus() {		
		if (this.mControlCoreBus == null || this.mControlCoreBus.isEmpty()) {
			return null;
		}		
		GT_MetaTileEntity_Hatch_ControlCore x = this.mControlCoreBus.get(0);
		if (x != null) {
			log("getControlCore(ok)");
			return x;
		}
		log("getControlCore(bad)");
		return null;
	}

	//mControlCoreBus
	public boolean addControlCoreToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {		
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			log("Tried to add null module entity.");
			return false;
		}
		if (!mControlCoreBus.isEmpty()) {
			log("Tried to add a secondary control core module.");
			return false;
		}

		GT_MetaTileEntity_Hatch_ControlCore Module = (GT_MetaTileEntity_Hatch_ControlCore) aMetaTileEntity;

		if (Module != null) {
			if (Module.setOwner(aTileEntity)) {
				log("Adding control core module.");
				return addToMachineListInternal(mControlCoreBus, aMetaTileEntity, aBaseCasingIndex);	
			}
		}
		return false;
	}

	@Override
	public boolean addToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}

		//Use this to determine the correct value, then update the hatch texture after.
		boolean aDidAdd = false;		

		//Handle Custom Hustoms
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_ControlCore) {
			log("Found GT_MetaTileEntity_Hatch_ControlCore");
			aDidAdd = addControlCoreToMachineList(aTileEntity, aBaseCasingIndex);
		}
		else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBattery) {
			log("Found GT_MetaTileEntity_Hatch_InputBattery");
			aDidAdd = addToMachineListInternal(mChargeHatches, aMetaTileEntity, aBaseCasingIndex);
		}
		else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBattery) {
			log("Found GT_MetaTileEntity_Hatch_OutputBattery");
			aDidAdd = addToMachineListInternal(mDischargeHatches, aMetaTileEntity, aBaseCasingIndex);
		}

		//Handle TT Multi-A Energy Hatches
		else if (LoadedMods.TecTech && isThisHatchMultiEnergy(aMetaTileEntity)) {
			log("Found isThisHatchMultiEnergy");
			aDidAdd = addToMachineListInternal(mTecTechEnergyHatches, aMetaTileEntity, aBaseCasingIndex);
		}		

		//Handle TT Multi-A Dynamos
		else if (LoadedMods.TecTech && isThisHatchMultiDynamo(aMetaTileEntity)) {
			log("Found isThisHatchMultiDynamo");
			aDidAdd = addToMachineListInternal(mTecTechDynamoHatches, aMetaTileEntity, aBaseCasingIndex);
		}		

		//Handle Fluid Hatches using seperate logic
		else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input)
			aDidAdd = addFluidInputToMachineList(aMetaTileEntity, aBaseCasingIndex);
		else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
			aDidAdd = addToMachineListInternal(mOutputHatches, aMetaTileEntity, aBaseCasingIndex);

		//Process Remaining hatches using Vanilla GT Logic
		else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus)
			aDidAdd = addToMachineListInternal(mInputBusses, aMetaTileEntity, aBaseCasingIndex);
		else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
			aDidAdd = addToMachineListInternal(mOutputBusses, aMetaTileEntity, aBaseCasingIndex);
		else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy)
			aDidAdd = addToMachineListInternal(mEnergyHatches, aMetaTileEntity, aBaseCasingIndex);
		else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo)
			aDidAdd = addToMachineListInternal(mDynamoHatches, aMetaTileEntity, aBaseCasingIndex);
		else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
			aDidAdd = addToMachineListInternal(mMaintenanceHatches, aMetaTileEntity, aBaseCasingIndex);
		else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler)
			aDidAdd = addToMachineListInternal(mMufflerHatches, aMetaTileEntity, aBaseCasingIndex);

		//return super.addToMachineList(aTileEntity, aBaseCasingIndex);
		return aDidAdd;
	}



	@Override
	public boolean addMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		return addToMachineList(aTileEntity, aBaseCasingIndex);
	}

	@Override
	public boolean addMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		return addToMachineList(aTileEntity, aBaseCasingIndex);
	}

	@Override
	public boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		return addToMachineList(aTileEntity, aBaseCasingIndex);
	}

	@Override
	public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		return addToMachineList(aTileEntity, aBaseCasingIndex);
	}

	public boolean addAirIntakeToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_AirIntake) {
			this.mAirIntakes.add((GT_MetaTileEntity_Hatch_AirIntake)aMetaTileEntity);
		}
		return addToMachineList(aTileEntity, aBaseCasingIndex);
	}

	public boolean addFluidInputToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		return addFluidInputToMachineList(aMetaTileEntity, aBaseCasingIndex);
	}

	public boolean addFluidInputToMachineList(final IMetaTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity;
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
			((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
			return addToMachineListInternal(mInputHatches, aMetaTileEntity, aBaseCasingIndex);			
		}
		return false;
	}

	public boolean clearRecipeMapForAllInputHatches() {
		return resetRecipeMapForAllInputHatches(null);
	}

	public boolean resetRecipeMapForAllInputHatches() {
		return resetRecipeMapForAllInputHatches(this.getRecipeMap());
	}

	public boolean resetRecipeMapForAllInputHatches(GT_Recipe_Map aMap) {
		int cleared = 0;
		for (GT_MetaTileEntity_Hatch_Input g : this.mInputHatches) {
			if (resetRecipeMapForHatch(g, aMap)) {
				cleared++;
			}
		}
		for (GT_MetaTileEntity_Hatch_InputBus g : this.mInputBusses) {
			if (resetRecipeMapForHatch(g, aMap)) {
				cleared++;
			}
		}
		return cleared > 0;
	}
	public boolean resetRecipeMapForHatch(IGregTechTileEntity aTileEntity, GT_Recipe_Map aMap) {
		try {
			if (aTileEntity == null) {
				return false;
			}
			final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			}
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
				return resetRecipeMapForHatch((GT_MetaTileEntity_Hatch)aMetaTileEntity, aMap);
			}
			else {
				return false;
			}
		}
		catch (Throwable t) {
			return false;
		}
	}

	public boolean resetRecipeMapForHatch(GT_MetaTileEntity_Hatch aTileEntity, GT_Recipe_Map aMap) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity;
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input){				
				((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = null;	
				((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = aMap;				
				if (aMap != null && aMap.mNEIName != null) {
					log("Remapped Input Hatch to "+aMap.mNEIName+".");					
				}
				else {
					log("Cleared Input Hatch.");					
				}				
			}
			else {	
				((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = null;	
				((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = aMap;					
				if (aMap != null && aMap.mNEIName != null) {
					log("Remapped Input Bus to "+aMap.mNEIName+".");					
				}
				else {
					log("Cleared Input Bus.");					
				}							
			}
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public final void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
		clearRecipeMapForAllInputHatches();
		onModeChangeByScrewdriver(aSide, aPlayer, aX, aY, aZ);
		resetRecipeMapForAllInputHatches();
	}

	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {

	}





	/**
	 * Enable Texture Casing Support if found in GT 5.09
	 */

	public boolean updateTexture(final IGregTechTileEntity aTileEntity, int aCasingID){
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}	
		return updateTexture(aMetaTileEntity, aCasingID);
	}

	/**
	 * Enable Texture Casing Support if found in GT 5.09
	 */

	@SuppressWarnings("deprecation")
	public boolean updateTexture(final IMetaTileEntity aTileEntity, int aCasingID){
		try { //gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch.updateTexture(int)

			final IMetaTileEntity aMetaTileEntity = aTileEntity;
			if (aMetaTileEntity == null) {
				return false;
			}			
			Method mProper = ReflectionUtils.getMethod(GT_MetaTileEntity_Hatch.class, "updateTexture", int.class);					
			if (mProper != null){
				if (GT_MetaTileEntity_Hatch.class.isInstance(aMetaTileEntity)){
					mProper.setAccessible(true);
					mProper.invoke(aMetaTileEntity, aCasingID);
					log("Good Method Call for updateTexture.");
					return true;
				}
			}
			else {
				log("Bad Method Call for updateTexture.");
				if (GT_MetaTileEntity_Hatch.class.isInstance(aMetaTileEntity)){
					if (aCasingID <= Byte.MAX_VALUE) {
						((GT_MetaTileEntity_Hatch) aTileEntity).mMachineBlock = (byte) aCasingID;
						log("Good Method Call for updateTexture. Used fallback method of setting mMachineBlock as casing id was <= 128.");
						return true;
					}
					else {
						log("updateTexture returning false. 1.2");
					}
				}
				else {
					log("updateTexture returning false. 1.3");
				}
			}
			log("updateTexture returning false. 1");
			return false;
		}
		catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log("updateTexture returning false.");
			log("updateTexture returning false. 2");
			e.printStackTrace();
			return false;	
		}	

	}









	/**
	 * TecTech Support
	 */


	/**
	 * This is the array Used to Store the Tectech Multi-Amp Dynamo hatches.
	 */

	public ArrayList<GT_MetaTileEntity_Hatch> mTecTechDynamoHatches = new ArrayList<GT_MetaTileEntity_Hatch>();	
	
	/**
	 * This is the array Used to Store the Tectech Multi-Amp Energy hatches.
	 */

	public ArrayList<GT_MetaTileEntity_Hatch> mTecTechEnergyHatches = new ArrayList<GT_MetaTileEntity_Hatch>();	

	/**
	 * TecTech Multi-Amp Dynamo Support
	 * @param aTileEntity - The Dynamo Hatch
	 * @param aBaseCasingIndex - Casing Texture
	 * @return
	 */

	public boolean addMultiAmpDynamoToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex){
		//GT_MetaTileEntity_Hatch_DynamoMulti
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (isThisHatchMultiDynamo(aTileEntity)) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mTecTechDynamoHatches.add((GT_MetaTileEntity_Hatch) aMetaTileEntity);
		}
		return false;
	}

	public boolean isThisHatchMultiDynamo(Object aMetaTileEntity){
		Class<?> mDynamoClass;
		mDynamoClass = ReflectionUtils.getClass("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti");
		if (mDynamoClass != null){
			if (mDynamoClass.isInstance(aMetaTileEntity)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (LoadedMods.TecTech){
			if (isThisHatchMultiDynamo(aTileEntity)) {
				return addMultiAmpDynamoToMachineList(aTileEntity, aBaseCasingIndex);
			}

		}
		return super.addDynamoToMachineList(aTileEntity, aBaseCasingIndex);
	}
	
	
	/**
	 * TecTech Multi-Amp Energy Hatch Support
	 * @param aTileEntity - The Energy Hatch
	 * @param aBaseCasingIndex - Casing Texture
	 * @return
	 */

	public boolean addMultiAmpEnergyToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex){
		//GT_MetaTileEntity_Hatch_DynamoMulti
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (isThisHatchMultiEnergy(aTileEntity)) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mTecTechEnergyHatches.add((GT_MetaTileEntity_Hatch) aMetaTileEntity);
		}
		return false;
	}
	
	public boolean isThisHatchMultiEnergy(Object aMetaTileEntity){
		Class<?> mDynamoClass;
		mDynamoClass = ReflectionUtils.getClass("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti");
		if (mDynamoClass != null){
			if (mDynamoClass.isInstance(aMetaTileEntity)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (LoadedMods.TecTech){
			if (isThisHatchMultiEnergy(aTileEntity)) {
				return addMultiAmpEnergyToMachineList(aTileEntity, aBaseCasingIndex);
			}

		}
		return super.addEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
	}


	/**
	 * Pollution Management
	 */

	public int getPollutionPerTick(ItemStack arg0) {
		return 0;
	}

	public String getPollutionTooltip() {
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			return "Causes " + 20 * this.getPollutionPerTick(null) + " Pollution per second";
		}
		else {
			return "";
		}
	}

	private static Method calculatePollutionReduction = null;
	public int calculatePollutionReductionForHatch(GT_MetaTileEntity_Hatch_Muffler i , int g) {		
		if (calculatePollutionReduction != null) {
			try {
				return (int) calculatePollutionReduction.invoke(i, g);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			}
		}
		return 0;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setLong("mTotalRunTime", this.mTotalRunTime);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.mTotalRunTime = aNBT.getLong("mTotalRunTime");
		super.loadNBTData(aNBT);
	}













	/**
	 * Custom Find Recipe with Debugging
	 */


	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final boolean aNotUnificated,
			final boolean aDontCheckStackSizes, final long aVoltage, final FluidStack[] aFluids,
			final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, null, aNotUnificated, aDontCheckStackSizes, aVoltage, aFluids,
				(ItemStack) null, aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final boolean aNotUnificated,
			final long aVoltage, final FluidStack[] aFluids, final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, (ItemStack) null, aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
			final boolean aNotUnificated, final boolean aDontCheckStackSizes, final long aVoltage,
			final FluidStack[] aFluids, final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, aRecipe, aNotUnificated, aDontCheckStackSizes, aVoltage, aFluids,
				(ItemStack) null, aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
			final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids,
			final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids, (ItemStack) null, aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
			final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids,
			final ItemStack aSpecialSlot, final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, aRecipe, aNotUnificated, true, aVoltage, aFluids, aSpecialSlot,
				aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
			final boolean aNotUnificated, final boolean aDontCheckStackSizes, final long aVoltage,
			final FluidStack[] aFluids, final ItemStack aSpecialSlot, ItemStack... aInputs) {
		if (this.getRecipeMap().mRecipeList.isEmpty()) {
			log("No Recipes in Map to search through.");
			return null;
		}
		GT_Recipe mRecipeResult = null;
		try {
			if (GregTech_API.sPostloadFinished) {
				if (this.getRecipeMap().mMinimalInputFluids > 0) {
					if (aFluids == null) {
						log("aFluids == null && minFluids > 0");
						return null;
					}
					int tAmount = 0;
					for (final FluidStack aFluid : aFluids) {
						if (aFluid != null) {
							++tAmount;
						}
					}
					if (tAmount < this.getRecipeMap().mMinimalInputFluids) {
						log("Not enough fluids?");
						return null;
					}
				}
				if (this.getRecipeMap().mMinimalInputItems > 0) {
					if (aInputs == null) {
						log("No inputs and minItems > 0");
						return null;
					}
					int tAmount = 0;
					for (final ItemStack aInput : aInputs) {
						if (aInput != null) {
							++tAmount;
						}
					}
					if (tAmount < this.getRecipeMap().mMinimalInputItems) {
						log("Not enough items?");
						return null;
					}
				}
			}
			else {
				log("Game Not Loaded properly for recipe lookup.");
			}
			if (aNotUnificated) {
				aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);
			}
			if (aRecipe != null && !aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered
					&& aRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
				mRecipeResult = (aRecipe.mEnabled/* && aVoltage * this.getRecipeMap().mAmperage >= aRecipe.mEUt*/) ? aRecipe : null;
				log("x) Found Recipe? "+(mRecipeResult != null ? "true" : "false"));
				if (mRecipeResult != null) {
					return mRecipeResult;
				}
			}
			if (mRecipeResult == null && this.getRecipeMap().mUsualInputCount >= 0 && aInputs != null && aInputs.length > 0) {
				for (final ItemStack tStack : aInputs) {
					if (tStack != null) {
						Collection<GT_Recipe> tRecipes = this.getRecipeMap().mRecipeItemMap.get(new GT_ItemStack(tStack));
						if (tRecipes != null) {
							for (final GT_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
									mRecipeResult = (tRecipe.mEnabled/* && aVoltage * this.getRecipeMap().mAmperage >= tRecipe.mEUt*/)
											? tRecipe
													: null;
									log("1) Found Recipe? "+(mRecipeResult != null ? "true" : "false"));
									//return mRecipeResult;
								}
							}
						}

						//TODO - Investigate if this requires to be in it's own block
						tRecipes = this.getRecipeMap().mRecipeItemMap
								.get(new GT_ItemStack(GT_Utility.copyMetaData(32767L, new Object[]{tStack})));
						if (tRecipes != null) {
							for (final GT_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe
										&& tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
									mRecipeResult = (tRecipe.mEnabled /*&& aVoltage * this.getRecipeMap().mAmperage >= tRecipe.mEUt*/)
											? tRecipe
													: null;
									log("2) Found Recipe? "+(mRecipeResult != null ? "true" : "false"));
									//return mRecipeResult;
								}
							}
						}
					}
				}
			}
			if (mRecipeResult == null && this.getRecipeMap().mMinimalInputItems == 0 && aFluids != null && aFluids.length > 0) {
				for (final FluidStack aFluid2 : aFluids) {
					if (aFluid2 != null) {
						final Collection<GT_Recipe> tRecipes = this.getRecipeMap().mRecipeFluidMap.get(aFluid2.getFluid());
						if (tRecipes != null) {
							for (final GT_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe
										&& tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
									mRecipeResult = (tRecipe.mEnabled/* && aVoltage * this.getRecipeMap().mAmperage >= tRecipe.mEUt*/)
											? tRecipe
													: null;
									log("3) Found Recipe? "+(mRecipeResult != null ? "true" : "false"));
									//return mRecipeResult;
								}
							}
						}
					}
				}
			}
		}
		catch (Throwable t) {
			log("Invalid recipe lookup.");			
		}		


		if (mRecipeResult == null) {
			log("Invalid recipe, Fallback lookup. "+this.getRecipeMap().mRecipeList.size()+" | "+this.getRecipeMap().mNEIName);	
			if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
				try {
					return (GT_Recipe) findRecipe08.invoke(getRecipeMap(), aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids, aSpecialSlot, aInputs);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					return null;
				}
			}
			else {
				try {
					return (GT_Recipe) findRecipe09.invoke(getRecipeMap(), aTileEntity, aRecipe, aNotUnificated, aDontCheckStackSizes, aVoltage,	aFluids, aSpecialSlot, aInputs);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		else {
			return mRecipeResult;
		}




	}




	/**
	 * Custom Tool Handling
	 */

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX,
			float aY, float aZ) {		
		//Do Super
		boolean aSuper = super.onRightclick(aBaseMetaTileEntity, aPlayer, aSide, aX, aY, aZ);
		// Do Things
		if (this.getBaseMetaTileEntity().isServerSide()) {
			ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
			if (tCurrentItem != null) {				
				if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSoftHammerList)) {	

				}
			}
		}
		return aSuper;
	}


	public final boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {	
		boolean aStructureCheck = checkMultiblock(aBaseMetaTileEntity, aStack);	
		boolean aHasCore = DEBUG_DISABLE_CORES_TEMPORARILY; //(requireControlCores ? (this.getControlCoreBus() != null) : true);	
		return aStructureCheck && aHasCore;
	}

	public abstract boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack);


	public boolean isValidBlockForStructure(IGregTechTileEntity aBaseMetaTileEntity, int aCasingID, boolean canBeHatch,
			Block aFoundBlock, int aFoundMeta, Block aExpectedBlock, int aExpectedMeta) {
		boolean isHatch = false;
		if (aBaseMetaTileEntity != null) {

			if (aCasingID < 64) {
				aCasingID = TAE.GTPP_INDEX(aCasingID);
			}

			isHatch = this.addToMachineList(aBaseMetaTileEntity, aCasingID);
			if (isHatch) {
				return true;
			}
			else {
				int aMetaTileID = aBaseMetaTileEntity.getMetaTileID();
				//Found a controller
				if (aMetaTileID >= 750 && aMetaTileID < 1000 && aFoundBlock == GregTech_API.sBlockMachines) {
					return true;
				}
				//Vanilla Hatches/Busses
				else if (aMetaTileID >= 10 && aMetaTileID <= 99 && aFoundBlock == GregTech_API.sBlockMachines) {
					return true;
				}				
				//Adv Mufflers
				else if (aMetaTileID >= 30001 && aMetaTileID <= 30009 && aFoundBlock == GregTech_API.sBlockMachines) {
					return true;
				}
				//Control Core, Super IO
				else if (aMetaTileID >= 30020 && aMetaTileID <= 30040 && aFoundBlock == GregTech_API.sBlockMachines) {
					return true;
				}
				//Auto maint
				else if (aMetaTileID == 111 && aFoundBlock == GregTech_API.sBlockMachines) {
					return true;
				}
				//Data Ports
				else if ((aMetaTileID == 131 || aMetaTileID == 132) && aFoundBlock == GregTech_API.sBlockMachines) {
					return true;
				}
				else {
					log("Found meta Tile: "+aMetaTileID);
				}
			}
		}
		if (!isHatch) {
			if (aFoundBlock == aExpectedBlock && aFoundMeta == aExpectedMeta) {
				return true;
			}
			else if (aFoundBlock != aExpectedBlock) {
				if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
					log("A1 - Found: "+aFoundBlock.getLocalizedName()+":"+aFoundMeta+", Expected: "+aExpectedBlock.getLocalizedName()+":"+aExpectedMeta);	
					log("Loc: "+(new BlockPos(aBaseMetaTileEntity).getLocationString()));
				}
				return false;
			}
			else if (aFoundMeta != aExpectedMeta) {
				log("A2");
				return false;
			}

		}
		log("A3");
		return false;
	}

	@Override
	public boolean depleteInput(final FluidStack aLiquid) {
		if (aLiquid == null) {
			return false;
		}
		for (final GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches) {
			tHatch.mRecipeMap = this.getRecipeMap();
			if (isValidMetaTileEntity(tHatch)) {
				FluidStack tLiquid = tHatch.getFluid();
				if (tLiquid == null || !tLiquid.isFluidEqual(aLiquid) || tLiquid.amount < aLiquid.amount) {
					continue;
				}
				tLiquid = tHatch.drain(aLiquid.amount, false);
				if (tLiquid != null && tLiquid.amount >= aLiquid.amount) {
					tLiquid = tHatch.drain(aLiquid.amount, true);
					return tLiquid != null && tLiquid.amount >= aLiquid.amount;
				}
				continue;
			}
		}
		return false;
	}

	@Override
	public void onServerStart() {
		super.onServerStart();
		tryTickWaitTimerDown();
	}

	@Override
	public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
		super.onFirstTick(aBaseMetaTileEntity);
		tryTickWaitTimerDown();
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);
		tryTickWaitTimerDown();
	}

	@Override
	public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		super.onCreated(aStack, aWorld, aPlayer);
		tryTickWaitTimerDown();
	}

	private final void tryTickWaitTimerDown() {		
		/*if (mStartUpCheck > 10) {
			mStartUpCheck = 10;			
		}*/		
	}




}
