package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_ControlCore;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Plasma;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_MiniFusionPlant extends GregtechMeta_MultiBlockBase {

	public long currentVoltage = GT_Values.V[7];
	public byte currentTier = 8;
	
	public void upvolt() {
		byte f = currentTier;
		if ((f+1) > 10) {
			f = 8;
		}
		else {
			f++;
		}
		this.currentTier = f;	
		updateVoltage();
	}
	
	public void downvolt() {
		byte f = currentTier;
		if ((f-1) < 8) {
			f = 10;
		}
		else {
			f--;
		}
		this.currentTier = f;	
		updateVoltage();
	}
	
	private long updateVoltage() {
		this.currentVoltage = GT_Values.V[this.currentTier-1];
		return currentVoltage;
	}
	
	public GregtechMTE_MiniFusionPlant(String aName) {
		super(aName);
	}
	
	public GregtechMTE_MiniFusionPlant(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_MiniFusionPlant(this.mName);
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == this.getBaseMetaTileEntity().getBackFacing()) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(28)],
					Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[(int) this.getInputTier()]};
		}
		if (aSide == 1) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(28)],
					Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[(int) this.getOutputTier()]};
		}
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(28)],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(28)]};
	}
	

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {		
		return Recipe_GT.Gregtech_Recipe_Map.sSlowFusionRecipes;
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public String getMachineType() {
		return "Fusion Reactor";
	}

	@Override
	public String[] getTooltip() {
		return new String[] { 
				"Small scale fusion", 
				"16x slower than using Multiblock of the same voltage",
				//"Input voltage can be changed within the GUI",				
				"Place Input/Output Hatches on sides and bottom",
				"Power can only be inserted into the back",
				//e"Power can only be extracted from the top",
				TAG_HIDE_HATCHES
				};
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
	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		int xDir2 = ForgeDirection.getOrientation(aBaseMetaTileEntity.getFrontFacing()).offsetX;
		int zDir2 = ForgeDirection.getOrientation(aBaseMetaTileEntity.getFrontFacing()).offsetZ;
		int tAmount = 0;

		ForgeDirection aDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing());
		
		//Require air in front, I think
		if (!aBaseMetaTileEntity.getAirOffset(xDir2, 0, zDir2)) {
			Logger.INFO("Did not find air in front");
			return false;
		} else {
			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					for (int h = -1; h < 2; ++h) {
						if (h != 0 || (xDir + i != 0 || zDir + j != 0) && (i != 0 || j != 0)) {
							IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i,
									h, zDir + j);
							if (this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(28))) {
								tAmount++;								
							}
						}						
					}
				}
			}
		}
		Logger.INFO("Tanks found: "+tAmount);
		return tAmount == 3;
	}

	@Override
	public boolean checkRecipe(ItemStack arg0) {
		

		ArrayList tFluidList = this.getStoredFluids();
		int tFluidList_sS = tFluidList.size();

		for (int tFluids = 0; tFluids < tFluidList_sS - 1; ++tFluids) {
			for (int tRecipe = tFluids + 1; tRecipe < tFluidList_sS; ++tRecipe) {
				if (GT_Utility.areFluidsEqual((FluidStack) tFluidList.get(tFluids),
						(FluidStack) tFluidList.get(tRecipe))) {
					if (((FluidStack) tFluidList.get(tFluids)).amount < ((FluidStack) tFluidList.get(tRecipe)).amount) {
						tFluidList.remove(tFluids--);
						tFluidList_sS = tFluidList.size();
						break;
					}

					tFluidList.remove(tRecipe--);
					tFluidList_sS = tFluidList.size();
				}
			}
		}
		int aStep = 0;
		//Logger.INFO("Step "+aStep++);
		if (tFluidList.size() > 1) {
			//Logger.INFO("Step "+aStep++);
			FluidStack[] arg5 = (FluidStack[]) tFluidList.toArray(new FluidStack[tFluidList.size()]);
			GT_Recipe arg6 = getRecipeMap().findRecipe(this.getBaseMetaTileEntity(), this.mLastRecipe,
					false, this.getMaxInputVoltage(), arg5, new ItemStack[0]);
			if (arg6 == null && !this.mRunningOnLoad || this.maxEUStore() < (long) arg6.mSpecialValue) {
				//Logger.INFO("Bad Step "+aStep++);
				//this.turnCasingActive(false);
				this.mLastRecipe = null;
				return false;
			}
			//Logger.INFO("Step "+aStep++);

			if (this.mRunningOnLoad || arg6.isRecipeInputEqual(true, arg5, new ItemStack[0])) {
				//Logger.INFO("Step "+aStep++);
				this.mLastRecipe = arg6;
				this.mEUt = this.mLastRecipe.mEUt * 1;
				this.mMaxProgresstime = this.mLastRecipe.mDuration / 1;
				this.mEfficiencyIncrease = 10000;
				this.mOutputFluids = this.mLastRecipe.mFluidOutputs;
				//this.turnCasingActive(true);
				this.mRunningOnLoad = false;
				return true;
			}
			//Logger.INFO("Step "+aStep++);
		}
		//Logger.INFO("Step "+aStep++);

		return false;
	
		
		
		
		//return this.checkRecipeGeneric(this.getMaxParallelRecipes(), getEuDiscountForParallelism(), 0);
	}

	@Override
	public int getMaxEfficiency(ItemStack arg0) {
		return 10000;
	}

	@Override
	public boolean drainEnergyInput(long aEU) {
		// Not applicable to this machine
		return true;
	}

	@Override
	public boolean addEnergyOutput(long aEU) {
		// Not applicable to this machine
		return true;
	}

	@Override
	public long maxEUStore() {
		return this.getMaxInputVoltage() * 256 * 512;
	}

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public String[] getExtraInfoData() {
		String mode = EnumChatFormatting.BLUE + "" + currentVoltage + EnumChatFormatting.RESET;	
		String aOutput = EnumChatFormatting.BLUE + "" + mEUt + EnumChatFormatting.RESET;		
		String storedEnergyText;
		if (this.getEUVar() > maxEUStore()) {
			storedEnergyText = EnumChatFormatting.RED + GT_Utility.formatNumbers(this.getEUVar()) + EnumChatFormatting.RESET;
		} else {
			storedEnergyText = EnumChatFormatting.GREEN + GT_Utility.formatNumbers(this.getEUVar()) + EnumChatFormatting.RESET;
		}
		
		return new String[]{
				"Stored EU: " + storedEnergyText,
				"Capacity: " + EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(this.maxEUStore()) + EnumChatFormatting.RESET,
				"Voltage: " + mode,
				"Output Voltage: " + aOutput
		};
	}

	@Override
	public void explodeMultiblock() {
		super.explodeMultiblock();
	}

	@Override
	public void doExplosion(long aExplosionPower) {
		super.doExplosion(aExplosionPower);
	}

	@Override
	public long getMaxInputVoltage() {
		return updateVoltage();
	}

	@Override
	public long getInputTier() {
		return (long) GT_Utility.getTier(maxEUInput());
	}
	
	@Override
	public boolean isElectric() {
		return true;
	}

	@Override
	public boolean isEnetInput() {
		return true;
	}

	@Override
	public boolean isEnetOutput() {
		return false;
	}

	@Override
	public boolean isInputFacing(byte aSide) {
		return (aSide == this.getBaseMetaTileEntity().getBackFacing());
	}

	@Override
	public boolean isOutputFacing(byte aSide) {
		return aSide == 1;
	}

	@Override
	public long maxAmperesIn() {
		return 32;
	}

	@Override
	public long maxAmperesOut() {
		return 1;
	}

	@Override
	public long maxEUInput() {
		return updateVoltage();
	}

	@Override
	public long maxEUOutput() {
		return mEUt > 0 ? mEUt : 0;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		this.mWrench = true;
		this.mScrewdriver = true;
		this.mSoftHammer = true;
		this.mHardHammer = true;
		this.mSolderingTool = true;
		this.mCrowbar = true;
	}

	@Override
	public boolean causeMaintenanceIssue() {
		return true;
	}

	@Override
	public int getControlCoreTier() {
		return this.currentTier;
	}

	@Override
	public int getPollutionPerTick(ItemStack arg0) {
		return 0;
	}	

	@Override
	public GT_MetaTileEntity_Hatch_ControlCore getControlCoreBus() {
		GT_MetaTileEntity_Hatch_ControlCore x = new GT_MetaTileEntity_Hatch_ControlCore("", 0, "", null);		
		return (GT_MetaTileEntity_Hatch_ControlCore) x;
	}

}
