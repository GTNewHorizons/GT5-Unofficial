package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;

import java.util.ArrayList;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Turbine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

public abstract class GregtechMetaTileEntity_LargerTurbineBase extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_LargerTurbineBase> {

	protected int baseEff = 0;
	protected int optFlow = 0;
	protected double realOptFlow = 0;
	protected int storedFluid = 0;
	protected int counter = 0;
	protected boolean looseFit=false;
	
	public ITexture frontFace;
	public ITexture frontFaceActive;	


	public ArrayList<GT_MetaTileEntity_Hatch_Turbine> mTurbineRotorHatches = new ArrayList<GT_MetaTileEntity_Hatch_Turbine>();

	public GregtechMetaTileEntity_LargerTurbineBase(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		frontFace = getTextureFrontFace();
		frontFaceActive = getTextureFrontFaceActive();

	}
	public GregtechMetaTileEntity_LargerTurbineBase(String aName) {
		super(aName);
		frontFace = getTextureFrontFace();
		frontFaceActive = getTextureFrontFaceActive();
	}
	

	protected abstract ITexture getTextureFrontFace();
	
	protected abstract ITexture getTextureFrontFaceActive();
	
	protected abstract String getTurbineType();
	
	protected abstract String getCasingName();

	@Override
	protected final GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the XL "+getTurbineType()+" Turbine")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(3, 3, 9, false)
				.addController("Top Middle")
				.addCasingInfo(getCasingName(), 64)
				.addOtherStructurePart("Rotor Assembly", "Any 1 dot hint", 1)
				.addInputBus("Any 4 dot hint", 4)
				.addInputHatch("Any 4 dot hint", 4)
				.addOutputHatch("Any 4 dot hint", 4)
				.addDynamoHatch("Any 4 dot hint", 4)
				.addMaintenanceHatch("Any 4 dot hint", 4)
				.addMufflerHatch("Any 7 dot hint x4", 7)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}
	
	private static final String STRUCTURE_PIECE_MAIN = "main";
	private static final ClassValue<IStructureDefinition<GregtechMetaTileEntity_LargerTurbineBase>> STRUCTURE_DEFINITION = new ClassValue<IStructureDefinition<GregtechMetaTileEntity_LargerTurbineBase>>() {
		@Override
		protected IStructureDefinition<GregtechMetaTileEntity_LargerTurbineBase> computeValue(Class<?> type) {
			return StructureDefinition.<GregtechMetaTileEntity_LargerTurbineBase>builder()
					// c = turbine casing
					// s = turbine shaft            		
					// t = turbine housing   
					// h = dynamo/maint
					// m = muffler
					.addShape(STRUCTURE_PIECE_MAIN, (new String[][]{
						{"ccchccc", "ccccccc", "ccmmmcc", "ccm~mcc", "ccmmmcc", "ccccccc", "ccchccc"},
						{"ctchctc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "ctchctc"},
						{"ccchccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccchccc"},
						{"ccchccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccchccc"},
						{"ctchctc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "ctchctc"},
						{"ccchccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccchccc"},
						{"ccchccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccchccc"},
						{"ctchctc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "ctchctc"},
						{"ccchccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccchccc"},
					}))
					.addElement('c', lazy(t -> ofBlock(t.getCasingBlock(), t.getCasingMeta())))
					.addElement('s', lazy(t -> ofBlock(t.getCasingBlock(), t.getTurbineShaftMeta())))
					.addElement('t', lazy(t -> ofHatchAdder(GregtechMetaTileEntity_LargerTurbineBase::addTurbineHatch, t.getCasingTextureIndex(), 1)))
                    .addElement('h', lazy(t -> ofHatchAdderOptional(GregtechMetaTileEntity_LargerTurbineBase::addGenericHatch, t.getCasingTextureIndex(), 4, t.getCasingBlock(), t.getCasingMeta())))
                    .addElement('m', lazy(t -> ofHatchAdderOptional(GregtechMetaTileEntity_LargerTurbineBase::addMuffler, t.getCasingTextureIndex(), 7, t.getCasingBlock(), t.getCasingMeta())))
					.build();
		}
	};

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_LargerTurbineBase> getStructureDefinition() {
		return STRUCTURE_DEFINITION.get(getClass());
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		this.mDynamoHatches.clear();
		this.mTurbineRotorHatches.clear();
		this.mMaintenanceHatches.clear();
		this.mMufflerHatches.clear();
		this.mInputBusses.clear();
		this.mInputHatches.clear();
		this.mOutputHatches.clear();
		
		boolean aStructure = checkPiece(STRUCTURE_PIECE_MAIN, 3, 3, 0);	
		log("Structure Check: "+aStructure);	
		if (mTurbineRotorHatches.size() != 12 ||
				mMaintenanceHatches.size() != 1 || 
				mDynamoHatches.size() < 1 || 
				mMufflerHatches.size() != 4 ||
				mInputBusses.size() < 1 ||
				mInputHatches.size() < 1 ||
				mOutputHatches.size() < 1
				) {
			log("Bad Hatches - Turbine Housings: "+mTurbineRotorHatches.size()+
					", Maint: "+mMaintenanceHatches.size()+
					", Dynamo: "+mDynamoHatches.size()+
					", Muffler: "+mMufflerHatches.size()+
					", Input Buses: "+mInputBusses.size()+
					", Input Hatches: "+mInputHatches.size()+
					", Output Hatches: "+mOutputHatches.size()+
					", ");
			return false;
		}		
		log("Built "+this.getLocalName());	
		return  aStructure;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 3, 0);
	}

	public boolean addTurbineHatch(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine) {
			log("Found GT_MetaTileEntity_Hatch_Turbine");
			updateTexture(aTileEntity, aBaseCasingIndex);
			GT_MetaTileEntity_Hatch_Turbine aTurbineHatch = (GT_MetaTileEntity_Hatch_Turbine) aMetaTileEntity;
			IGregTechTileEntity g = this.getBaseMetaTileEntity();
			if (aTurbineHatch.setController(new BlockPos(g.getXCoord(), g.getYCoord(), g.getZCoord(), g.getWorld()))) {
				Logger.INFO("Injected Controller into Turbine Assembly.");
				return this.mTurbineRotorHatches.add(aTurbineHatch);
			}
			else {
				Logger.INFO("Failed to inject controller into Turbine Assembly Hatch.");
			}
		}
		log("Bad Turbine Housing");
		return false;
	}

	public final boolean addMuffler(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		log("Bad Muffler");
		return false;
	}

	public final boolean addGenericHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} 
		else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		log("Bad Hatch");
		return false;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return getMaxEfficiency(aStack) > 0;
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeTurbine.png");
	}

	public boolean isValidCasingBlock(Block aBlock, int aMeta) {
		if (Block.isEqualTo(aBlock, getCasingBlock()) && aMeta == getCasingMeta()) {
			return true;
		}return false;
	}

	public boolean isValidTurbineShaft(Block aBlock, int aMeta) {
		if (aBlock == getCasingBlock() && aMeta == getTurbineShaftMeta()) {
			return true;
		}
		return false;
	}

	public final Block getCasingBlock() {
		return ModBlocks.blockSpecialMultiCasings;
	}	
	
	@Override
	public final boolean hasSlotInGUI() {
		return false;
	}

	public abstract int getCasingMeta();

	public byte getTurbineShaftMeta() {
		return 0;
	}

	public abstract byte getCasingTextureIndex();

    public abstract int getFuelValue(FluidStack aLiquid);
	
    public static boolean isValidTurbine(ItemStack aTurbine) {    	
    	return (aTurbine !=null && aTurbine.getItem() instanceof GT_MetaGenerated_Tool  && aTurbine.getItemDamage() >= 170 && aTurbine.getItemDamage() <= 176);
    }
    
    private ArrayList<ItemStack> getAllBufferedTurbines(){
    	ArrayList<ItemStack> aTurbinesInStorage = new ArrayList<ItemStack>();
    	for (GT_MetaTileEntity_Hatch_InputBus aBus: this.mInputBusses) {
    		if (isValidMetaTileEntity(aBus)) {
    			for (ItemStack aContent : aBus.mInventory) {
    				if (isValidTurbine(aContent)) {
    					aTurbinesInStorage.add(aContent);
    				}
    			}
    		}
    	}
    	return aTurbinesInStorage;
    }
    
	@Override
	public boolean checkRecipe(ItemStack aStack) {
		
		ArrayList<GT_MetaTileEntity_Hatch_Turbine> aEmptyTurbineRotorHatches = new ArrayList<GT_MetaTileEntity_Hatch_Turbine>();
		for (GT_MetaTileEntity_Hatch_Turbine aTurbineHatch : this.mTurbineRotorHatches) {
			if (!aTurbineHatch.hasTurbine()) {
				aEmptyTurbineRotorHatches.add(aTurbineHatch);
			}
		}
		if (aEmptyTurbineRotorHatches.size() > 0) {
			ArrayList<ItemStack> aTurbines = getAllBufferedTurbines();
			for (GT_MetaTileEntity_Hatch_Turbine aHatch : aEmptyTurbineRotorHatches) {
				for (ItemStack aTurbineItem : aTurbines) {
					if (aHatch.insertTurbine(aTurbineItem) && this.depleteInput(aTurbineItem)) {
						continue;
					}
					else {
						break;
					}
				}
			}
		}		
		
		if((counter&7)==0 && (aStack==null || !(aStack.getItem() instanceof GT_MetaGenerated_Tool)  || aStack.getItemDamage() < 170 || aStack.getItemDamage() >179)) {
			stopMachine();
			return false;
		}
		ArrayList<FluidStack> tFluids = getStoredFluids();
		if (tFluids.size() > 0) {
			if (baseEff == 0 || optFlow == 0 || counter >= 512 || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()
					|| this.getBaseMetaTileEntity().hasInventoryBeenModified()) {
				counter = 0;
				baseEff = MathUtils.safeInt((long)((5F + ((GT_MetaGenerated_Tool) aStack.getItem()).getToolCombatDamage(aStack)) * 1000F));
				optFlow = MathUtils.safeInt((long)Math.max(Float.MIN_NORMAL,
						((GT_MetaGenerated_Tool) aStack.getItem()).getToolStats(aStack).getSpeedMultiplier()
						* GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mToolSpeed
						* 50));
				if(optFlow<=0 || baseEff<=0){
					stopMachine();//in case the turbine got removed
					return false;
				}
			} else {
				counter++;
			}
		}
		else {
			Logger.INFO("Did not find any valid input fluids.");
		}

		int newPower = fluidIntoPower(tFluids, optFlow, baseEff);  // How much the turbine should be producing with this flow
		int difference = newPower - this.mEUt; // difference between current output and new output

		// Magic numbers: can always change by at least 10 eu/t, but otherwise by at most 1 percent of the difference in power level (per tick)
		// This is how much the turbine can actually change during this tick
		int maxChangeAllowed = Math.max(10, MathUtils.safeInt((long)Math.abs(difference)/100));

		if (Math.abs(difference) > maxChangeAllowed) { // If this difference is too big, use the maximum allowed change
			int change = maxChangeAllowed * (difference > 0 ? 1 : -1); // Make the change positive or negative.
			this.mEUt += change; // Apply the change
		} else {
			this.mEUt = newPower;
		}

		if (this.mEUt <= 0) {
			//stopMachine();
			this.mEUt=0;
			this.mEfficiency=0;
			return false;
		} else {
			this.mMaxProgresstime = 1;
			this.mEfficiencyIncrease = 10;
			if(this.mDynamoHatches.size()>0){
				for(GT_MetaTileEntity_Hatch dynamo:mDynamoHatches)
					if(isValidMetaTileEntity(dynamo) && dynamo.maxEUOutput() < mEUt)
						explodeMultiblock();
			}
			return true;
		}
	}

	abstract int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff);

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 1;
	}

	public int getMaxEfficiency(ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return 0;
		}
		if (aStack.getItem() instanceof GT_MetaGenerated_Tool_01) {
			return 10000;
		}
		return 0;
	}
	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return true;
	}

	@Override
	public String[] getExtraInfoData() {
		int mPollutionReduction=0;
		for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				mPollutionReduction=Math.max(StaticFields59.calculatePollutionReducation(tHatch, 100),mPollutionReduction);
			}
		}

		String tRunning = mMaxProgresstime>0 ?

				EnumChatFormatting.GREEN+StatCollector.translateToLocal("GT5U.turbine.running.true")+EnumChatFormatting.RESET :
					EnumChatFormatting.RED+StatCollector.translateToLocal("GT5U.turbine.running.false")+EnumChatFormatting.RESET;
		String tMaintainance = getIdealStatus() == getRepairStatus() ?
				EnumChatFormatting.GREEN+StatCollector.translateToLocal("GT5U.turbine.maintenance.false")+EnumChatFormatting.RESET :
					EnumChatFormatting.RED+StatCollector.translateToLocal("GT5U.turbine.maintenance.true")+EnumChatFormatting.RESET ;
		int tDura = 0;

		if (mInventory[1] != null && mInventory[1].getItem() instanceof GT_MetaGenerated_Tool_01) {
			tDura = MathUtils.safeInt((long)(100.0f / GT_MetaGenerated_Tool.getToolMaxDamage(mInventory[1]) * (GT_MetaGenerated_Tool.getToolDamage(mInventory[1]))+1));
		}

		long storedEnergy=0;
		long maxEnergy=0;
		for(GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
				maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
			}
		}
		String[] ret = new String[]{
				// 8 Lines available for information panels
				tRunning + ": " + EnumChatFormatting.RED+mEUt+EnumChatFormatting.RESET+" EU/t",
				/* 1 */
				tMaintainance,
				/* 2 */
				StatCollector.translateToLocal("GT5U.turbine.efficiency")+": "+EnumChatFormatting.YELLOW+(mEfficiency/100F)+EnumChatFormatting.RESET+"%",
				/* 2 */
				StatCollector.translateToLocal("GT5U.multiblock.energy")+": " + EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET +" EU / "+
				/* 3 */
				EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET +" EU", 
				StatCollector.translateToLocal("GT5U.turbine.flow")+": "+EnumChatFormatting.YELLOW+MathUtils.safeInt((long)realOptFlow)+EnumChatFormatting.RESET+" L/t" +
						/* 4 */
						EnumChatFormatting.YELLOW+" ("+(looseFit?StatCollector.translateToLocal("GT5U.turbine.loose"):StatCollector.translateToLocal("GT5U.turbine.tight"))+")",
						/* 5 */
						StatCollector.translateToLocal("GT5U.turbine.fuel")+": "+EnumChatFormatting.GOLD+storedFluid+EnumChatFormatting.RESET+"L",
						/* 6 */
						StatCollector.translateToLocal("GT5U.turbine.dmg")+": "+EnumChatFormatting.RED+Integer.toString(tDura)+EnumChatFormatting.RESET+"%",
						/* 7 */
						StatCollector.translateToLocal("GT5U.multiblock.pollution")+": "+ EnumChatFormatting.GREEN + mPollutionReduction+ EnumChatFormatting.RESET+" %"
						/* 8 */
		};
		if (!this.getClass().getName().contains("Steam"))
			ret[4]=StatCollector.translateToLocal("GT5U.turbine.flow")+": "+EnumChatFormatting.YELLOW+MathUtils.safeInt((long)realOptFlow)+EnumChatFormatting.RESET+" L/t";
		return ret;


	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}


	public boolean polluteEnvironment(int aPollutionLevel) {
		mPollution += aPollutionLevel;
		for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				if (mPollution >= 1000) {					
					if (PollutionUtils.addPollution(this.getBaseMetaTileEntity(), 1000)) {
						mPollution -= 1000;
					}
				} else {
					break;
				}
			}
		}
		return mPollution < 1000;
	}
	@Override
	public long maxAmperesOut() {
		return 16;
	}


	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (!KeyboardUtils.isShiftKeyDown()) {
			//super.onModeChangeByScrewdriver(aSide, aPlayer, aX, aY, aZ);		
		}
		else {
			/*
			this.mIsAnimated = Utils.invertBoolean(mIsAnimated);
			if (this.mIsAnimated) {
			PlayerUtils.messagePlayer(aPlayer, "Using Animated Turbine Texture.");
			}
			else {
			PlayerUtils.messagePlayer(aPlayer, "Using Static Turbine Texture.");			
			}		
			if (mTurbineRotorHatches.size() > 0) {
			for (GT_MetaTileEntity_Hatch_Turbine h : mTurbineRotorHatches) {
				if (h != null) {
					h.mUsingAnimation = mIsAnimated;
				}
			}
			}	
			*/}			
	}

	@Override
	public final ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][aColorIndex + 1], aFacing == aSide ? getFrontFacingTurbineTexture(aActive) : Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex())};
	}

	protected ITexture getFrontFacingTurbineTexture(boolean isActive) {
		if (isActive) {
			return frontFaceActive;
		}
		return frontFace;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (aBaseMetaTileEntity.isServerSide()) {
			if (mUpdate == 0 || this.mStartUpCheck == 0) {
				this.mTurbineRotorHatches.clear();
			}
		}		
		if (aTick % 20 == 0 || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()) {
			enableAllTurbineHatches();
		}

	}
	@Override
	public void startProcess() {
		super.startProcess();
		enableAllTurbineHatches();
	}
	
	@Override
	public boolean onRunningTick(ItemStack aStack) {	
		return super.onRunningTick(aStack);		
	}
	
	@Override
	public void stopMachine() {
		super.stopMachine();
		disableAllTurbineHatches();
	}
	@Override
	public void onRemoval() {
		super.onRemoval();
		for (GT_MetaTileEntity_Hatch_Turbine h : this.mTurbineRotorHatches) {
			h.clearController();
		}
		disableAllTurbineHatches();
		this.mTurbineRotorHatches.clear();
	}

	public boolean enableAllTurbineHatches() {
		return updateTurbineHatches(this.isMachineRunning()) > 0;
	}
	public boolean disableAllTurbineHatches() {
		return updateTurbineHatches(false) > 0;
	}

	private Long mLastHatchUpdate;	
	public int updateTurbineHatches(boolean aState) {
		int aUpdated = 0;		
		if (mLastHatchUpdate == null) {
			mLastHatchUpdate = System.currentTimeMillis()/1000;
		}			
		if (this.mTurbineRotorHatches.isEmpty() || ((System.currentTimeMillis()/1000)-mLastHatchUpdate) <= 2) {
			return 0;
		}		
		for (GT_MetaTileEntity_Hatch_Turbine h : this.mTurbineRotorHatches) {
			h.setActive(aState);
			aUpdated++;
		}

		mLastHatchUpdate = System.currentTimeMillis()/1000;
		return aUpdated;
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
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);		
		// Fix GT bug
		if (this.getBaseMetaTileEntity().getFrontFacing() != 1) {
			log("Fixing Bad Facing. (GT Bug)");
			this.getBaseMetaTileEntity().setFrontFacing((byte) 1); 
		}
	}

}
