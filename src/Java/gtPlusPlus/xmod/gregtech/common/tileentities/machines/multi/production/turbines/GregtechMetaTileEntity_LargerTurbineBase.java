package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import java.util.ArrayList;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Turbine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.turbine.LargeTurbineTextureHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

public abstract class GregtechMetaTileEntity_LargerTurbineBase extends GregtechMeta_MultiBlockBase {

	protected int baseEff = 0;
	protected int optFlow = 0;
	protected double realOptFlow = 0;
	protected int storedFluid = 0;
	protected int counter = 0;
	protected boolean looseFit=false;

	private final int mCasingTextureID;
	public static String mCasingName;
	
    public ArrayList<GT_MetaTileEntity_Hatch_Turbine> mTurbineRotorHatches = new ArrayList<GT_MetaTileEntity_Hatch_Turbine>();

	public GregtechMetaTileEntity_LargerTurbineBase(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, getCasingMeta());
		mCasingTextureID = getTAE();
		GT9_5_Active = getCasingMeta() == 8 ? LargeTurbineTextureHandler.frontFaceActive_4 : LargeTurbineTextureHandler.frontFaceHPActive_4;
		GT9_5 = getCasingMeta() == 8 ? LargeTurbineTextureHandler.frontFace_4 : LargeTurbineTextureHandler.frontFaceHP_4;
		frontFaceActive = new GT_RenderedTexture(GT9_5_Active);
		frontFace = new GT_RenderedTexture(GT9_5);
		
	}
	public GregtechMetaTileEntity_LargerTurbineBase(String aName) {
		super(aName);
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, getCasingMeta());
		mCasingTextureID = getTAE();
		GT9_5_Active = getCasingMeta() == 8 ? LargeTurbineTextureHandler.frontFaceActive_4 : LargeTurbineTextureHandler.frontFaceHPActive_4;
		GT9_5 = getCasingMeta() == 8 ? LargeTurbineTextureHandler.frontFace_4 : LargeTurbineTextureHandler.frontFaceHP_4;
		frontFaceActive = new GT_RenderedTexture(GT9_5_Active);
		frontFace = new GT_RenderedTexture(GT9_5);
	}

	public final int getTAE() {
		return TAE.getIndexFromPage(3, getCasingMeta());
	}

	public final String getCasingNaming() {
		return ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, getCasingMeta());
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return getMaxEfficiency(aStack) > 0;
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeTurbine.png");
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {	
		return checkMachine2(aBaseMetaTileEntity, aStack);
	}

	public boolean checkMachine2(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		//9 high
		//7x7		
		
		this.mDynamoHatches.clear();
		this.mTurbineRotorHatches.clear();
		this.mMaintenanceHatches.clear();
		this.mMufflerHatches.clear();
		this.mInputHatches.clear();
		this.mOutputHatches.clear();
		
		for (int i=0;i>-9;i--) {
			if (!getLayer(i)) {
				Logger.INFO("Bad Layer: "+(+i));
				return false;
			}
		}	
		
		Logger.INFO("Hatches | Found "+mTurbineRotorHatches.size()+" Rotor Assemblies, "+12+" are required.");
		Logger.INFO("Hatches | Found "+mMaintenanceHatches.size()+" Maint. hatches, "+1+" are required.");
		Logger.INFO("Hatches | Found "+mDynamoHatches.size()+"  Dynamos, "+1+" or more are required.");
		Logger.INFO("Hatches | Found "+mMufflerHatches.size()+" Mufflers, "+4+" are required.");
		Logger.INFO("Hatches | Found "+mInputHatches.size()+" Input Hatches, "+1+" or more are required.");
		Logger.INFO("Hatches | Found "+mOutputHatches.size()+" Output Hatches, "+1+" ore more are required.");
		
		if (mTurbineRotorHatches.size() != 12 ||
				mMaintenanceHatches.size() != 1 || 
				mDynamoHatches.size() < 1 || 
				mMufflerHatches.size() != 4 ||
				mInputHatches.size() < 1 ||
				mOutputHatches.size() < 1
				) {
			return false;
		}
		Logger.INFO("Built Structure");
		return true;
	}


	public boolean getLayer(int aY) {
		if (aY == 0 || aY == -2 || aY == -3 || aY == -5 || aY == -6 || aY == -8) {
			return checkNormalLayer(aY);
		}
		else {
			return checkTurbineLayer(aY);
		}
	}

	public boolean checkNormalLayer(int aY) {
		Block tBlock;
		int tMeta;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				tBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
				tMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);
				IGregTechTileEntity tTileEntity;

				if (aY == 0 && x == 0 && z == 0) {
					continue;
				}				
				else if ((x == 0 && z == -3) || (x == 0 && z == 3) || (x == 3 && z == 0) || (x == -3 && z == 0) || 
						((aY == 0) && (x == 0 && z == -2) || (x == 0 && z == 2) || (x == 2 && z == 0) || (x == -2 && z == 0))) {
					tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityOffset(x, aY, z);    					
					if (this.addToMachineList(tTileEntity, this.mCasingTextureID)) {
						Logger.INFO("Added Hatch at offset "+x+", "+aY+", "+z+" | Type: "+tTileEntity.getInventoryName());
						continue;
					}
				}				
				else if (isValidCasingBlock(tBlock, tMeta)) {
					continue;
				}  else {					
					if (tBlock != null) {
						log("Offset: "+x+", "+aY+", "+z);
						log("Found "+tBlock.getLocalizedName()+" with Meta "+tMeta);
						log("Expected "+getCasingBlock().getLocalizedName()+" with Meta "+getCasingMeta());
					}
					return false;
				}
			}
		}
		return true;
	}


	public boolean checkTurbineLayer(int aY) {
		if (!checkTurbineLayerX(aY)) {
			return checkTurbineLayerZ(aY);
		}
		else {
			return true;
		}
	}

	public boolean checkTurbineLayerX(int aY) {		
		Logger.INFO("checking X");
		Block tBlock;
		int tMeta;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				tBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
				tMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);   

				if ((x == 0 && z == -3) || (x == 0 && z == 3) || (x == 3 && z == 0) || (x == -3 && z == 0) || 
						((aY == 0) && (x == 0 && z == -2) || (x == 0 && z == 2) || (x == 2 && z == 0) || (x == -2 && z == 0))) {
					IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityOffset(x, aY, z);    					
					if (this.addToMachineList(tTileEntity, this.mCasingTextureID)) {
						Logger.INFO("Added Hatch at offset "+x+", "+aY+", "+z+" | Type: "+tTileEntity.getInventoryName());
						continue;
					}
				}				    	


				if (x == -2 || x == 2) {
					
					//Find Hatches on the ends
					if (z == -3 || z == 3) {
						IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityOffset(x, aY, z);    					
						if (this.addTurbineHatch(tTileEntity, this.mCasingTextureID)) {
							log("Found x axis Turbine Assembly at Offset: "+x+", "+aY+", "+z);
							continue;
						}
						else {
							log("Missing x axis Turbine Assembly at Offset: "+x+", "+aY+", "+z);							
						}
					}					
					
					if (isValidTurbineBlock(tBlock, tMeta)) {
						continue;
					}
					else {
						return false;
					}
				}
				else {
					if (isValidCasingBlock(tBlock, tMeta)) {
						continue;
					}
					else {
						return false;
					}  
				}    			  			
			}	
		}    	
		return true;
	}

	public boolean checkTurbineLayerZ(int aY) {
		Logger.INFO("checking Z");
		Block tBlock;
		int tMeta;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				tBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
				tMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);

				if ((x == 0 && z == -3) || (x == 0 && z == 3) || (x == 3 && z == 0) || (x == -3 && z == 0) || 
						((aY == 0) && (x == 0 && z == -2) || (x == 0 && z == 2) || (x == 2 && z == 0) || (x == -2 && z == 0))) {
					IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityOffset(x, aY, z);    					
					if (this.addToMachineList(tTileEntity, this.mCasingTextureID)) {
						Logger.INFO("Added Hatch at offset "+x+", "+aY+", "+z+" | Type: "+tTileEntity.getInventoryName());
						continue;
					}
				}

				if (z == -2 || z == 2) {
					
					//Find Hatches on the ends
					if (x == -3 || x == 3) {
						IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityOffset(x, aY, z);    					
						if (this.addTurbineHatch(tTileEntity, this.mCasingTextureID)) {
							log("Found z axis Turbine Assembly at Offset: "+x+", "+aY+", "+z);
							continue;
						}
						else {
							log("Missing z axis Turbine Assembly at Offset: "+x+", "+aY+", "+z);							
						}
					}					
					
					if (isValidTurbineBlock(tBlock, tMeta)) {
						continue;
					}
					else {
						return false;
					}
				}
				else {
					if (isValidCasingBlock(tBlock, tMeta)) {
						continue;
					}
					else {
						return false;
					}  
				}    			  			
			}	
		}    	
		return true;
	}   

	public boolean isValidCasingBlock(Block aBlock, int aMeta) {
		if (GregTech_API.sBlockMachines == aBlock) {
			return true;
		}
		if (Block.isEqualTo(aBlock, getCasingBlock()) && (int) aMeta == (int) getCasingMeta()) {
			return true;
		}
		log("Found "+(aBlock != null ? aBlock.getLocalizedName() : "Air") + " With Meta "+aMeta+", Expected "+getCasingBlock().getLocalizedName()+" With Meta "+getCasingMeta());
		return false;
	}

	public boolean isValidTurbineBlock(Block aBlock, int aMeta) {
		if (aBlock == getCasingBlock() && aMeta == getCasingMetaTurbine()) {
			return true;
		}
		log("Found "+(aBlock != null ? aBlock.getLocalizedName() : "Air") + " With Meta "+aMeta+", Expected "+getCasingBlock().getLocalizedName()+" With Meta "+getCasingMetaTurbine());
		return false;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockCasings4Misc;
	}

	public abstract byte getCasingMeta();

	public byte getCasingMetaTurbine() {
		return 7;
	}

	public abstract byte getCasingTextureIndex();

	@Override
	public boolean checkRecipe(ItemStack aStack) {
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
				tRunning + ": " + EnumChatFormatting.RED+mEUt+EnumChatFormatting.RESET+" EU/t", /* 1 */
				tMaintainance, /* 2 */
				StatCollector.translateToLocal("GT5U.turbine.efficiency")+": "+EnumChatFormatting.YELLOW+(mEfficiency/100F)+EnumChatFormatting.RESET+"%", /* 2 */
				StatCollector.translateToLocal("GT5U.multiblock.energy")+": " + EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET +" EU / "+ /* 3 */
				EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET +" EU", 
				StatCollector.translateToLocal("GT5U.turbine.flow")+": "+EnumChatFormatting.YELLOW+MathUtils.safeInt((long)realOptFlow)+EnumChatFormatting.RESET+" L/t" + /* 4 */
						EnumChatFormatting.YELLOW+" ("+(looseFit?StatCollector.translateToLocal("GT5U.turbine.loose"):StatCollector.translateToLocal("GT5U.turbine.tight"))+")", /* 5 */
						StatCollector.translateToLocal("GT5U.turbine.fuel")+": "+EnumChatFormatting.GOLD+storedFluid+EnumChatFormatting.RESET+"L", /* 6 */
						StatCollector.translateToLocal("GT5U.turbine.dmg")+": "+EnumChatFormatting.RED+Integer.toString(tDura)+EnumChatFormatting.RESET+"%", /* 7 */
						StatCollector.translateToLocal("GT5U.multiblock.pollution")+": "+ EnumChatFormatting.GREEN + mPollutionReduction+ EnumChatFormatting.RESET+" %" /* 8 */
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
					if (tHatch.polluteEnvironment()) {
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
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (!KeyboardUtils.isShiftKeyDown()) {
			super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);		
		}
		else {
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
		}			
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setBoolean("mIsAnimated", mIsAnimated);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mIsAnimated = aNBT.getBoolean("mIsAnimated");
	}
	
	private boolean mIsAnimated = true;
	public ITexture frontFace;
	public ITexture frontFaceActive;
	private CustomIcon GT9_5_Active;
	private CustomIcon GT9_5;	
	
	public boolean usingAnimations() {
		return mIsAnimated;
	}	

    @Override
    public final ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][aColorIndex + 1], aFacing == aSide ? getFrontFacingTurbineTexture(aActive) : Textures.BlockIcons.CASING_BLOCKS[getTAE()]};
    }
	
	protected ITexture getFrontFacingTurbineTexture(boolean isActive) {
		if (usingAnimations()) {
			if (isActive) {
				return frontFaceActive;
			}
		}
		return frontFace;
	}
	
	public boolean addTurbineHatch(final IGregTechTileEntity aTileEntity,
			final int aBaseCasingIndex) {
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
		return false;
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
	public void onMachineBlockUpdate() {
		super.onMachineBlockUpdate();
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
	

}
