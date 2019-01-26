package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;

import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gregtech.common.gui.GT_GUIContainer_FusionReactor;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_Cyclotron;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_Cyclotron extends GregtechMeta_MultiBlockBase {

	public long mEUStore;

	public GregtechMetaTileEntity_Cyclotron(int aID, String aName, String aNameRegional, int tier) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_Cyclotron(String aName) {
		super(aName);
	}

	@Override
	public String getMachineType() {
		return "Particle Accelerator";
	}

	public int tier(){
		return 5;
	}

	@Override
	public long maxEUStore() {
		return 1800000000L;
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "FusionComputer";
	}	

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_Cyclotron(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_FusionReactor(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "FusionComputer.png", Recipe_GT.Gregtech_Recipe_Map.sCyclotronRecipes.mNEIName);
		//return null;
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Cyclotron(this.mName);
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
		return aSide != getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setLong("mEUStore", mEUStore);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		mEUStore = aNBT.getLong("mEUStore");
		super.loadNBTData(aNBT);
	}

	@Override
	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		log("Checking form of Cyclotron.");
		int xCenter = getBaseMetaTileEntity().getXCoord() + ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()).offsetX * 5;
		int yCenter = getBaseMetaTileEntity().getYCoord();
		int zCenter = getBaseMetaTileEntity().getZCoord() + ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()).offsetZ * 5;
		if (((isAdvancedMachineCasing(xCenter + 5, yCenter, zCenter)) || (xCenter + 5 == getBaseMetaTileEntity().getXCoord()))
				&& ((isAdvancedMachineCasing(xCenter - 5, yCenter, zCenter)) || (xCenter - 5 == getBaseMetaTileEntity().getXCoord()))
				&& ((isAdvancedMachineCasing(xCenter, yCenter, zCenter + 5)) || (zCenter + 5 == getBaseMetaTileEntity().getZCoord()))
				&& ((isAdvancedMachineCasing(xCenter, yCenter, zCenter - 5)) || (zCenter - 5 == getBaseMetaTileEntity().getZCoord())) && (checkCoils(xCenter, yCenter, zCenter))
				&& (checkHulls(xCenter, yCenter, zCenter)) && (checkUpperOrLowerHulls(xCenter, yCenter + 1, zCenter)) && (checkUpperOrLowerHulls(xCenter, yCenter - 1, zCenter))
				&& (isAdvancedMachineCasing(xCenter + 4, yCenter, zCenter + 3)) && (isAdvancedMachineCasing(xCenter + 4, yCenter, zCenter - 3))
				&& (isAdvancedMachineCasing(xCenter + 4, yCenter, zCenter + 5)) && (isAdvancedMachineCasing(xCenter + 4, yCenter, zCenter - 5))
				&& (isAdvancedMachineCasing(xCenter - 4, yCenter, zCenter + 3)) && (isAdvancedMachineCasing(xCenter - 4, yCenter, zCenter - 3))
				&& (isAdvancedMachineCasing(xCenter - 4, yCenter, zCenter + 5)) && (isAdvancedMachineCasing(xCenter - 4, yCenter, zCenter - 5))
				&& (isAdvancedMachineCasing(xCenter + 3, yCenter, zCenter + 4)) && (isAdvancedMachineCasing(xCenter - 3, yCenter, zCenter + 4))
				&& (isAdvancedMachineCasing(xCenter + 5, yCenter, zCenter + 4)) && (isAdvancedMachineCasing(xCenter - 5, yCenter, zCenter + 4))
				&& (isAdvancedMachineCasing(xCenter + 3, yCenter, zCenter - 4)) && (isAdvancedMachineCasing(xCenter - 3, yCenter, zCenter - 4))
				&& (isAdvancedMachineCasing(xCenter + 5, yCenter, zCenter - 4)) && (isAdvancedMachineCasing(xCenter - 5, yCenter, zCenter - 4))
				&& (isAdvancedMachineCasing(xCenter + 1, yCenter, zCenter - 5)) && (isAdvancedMachineCasing(xCenter + 1, yCenter, zCenter + 5))
				&& (isAdvancedMachineCasing(xCenter - 1, yCenter, zCenter - 5)) && (isAdvancedMachineCasing(xCenter - 1, yCenter, zCenter + 5))
				&& (isAdvancedMachineCasing(xCenter + 1, yCenter, zCenter - 7)) && (isAdvancedMachineCasing(xCenter + 1, yCenter, zCenter + 7))
				&& (isAdvancedMachineCasing(xCenter - 1, yCenter, zCenter - 7)) && (isAdvancedMachineCasing(xCenter - 1, yCenter, zCenter + 7))
				&& (isAdvancedMachineCasing(xCenter + 5, yCenter, zCenter - 1)) && (isAdvancedMachineCasing(xCenter + 5, yCenter, zCenter + 1))
				&& (isAdvancedMachineCasing(xCenter - 5, yCenter, zCenter - 1)) && (isAdvancedMachineCasing(xCenter - 5, yCenter, zCenter + 1))
				&& (isAdvancedMachineCasing(xCenter + 7, yCenter, zCenter - 1)) && (isAdvancedMachineCasing(xCenter + 7, yCenter, zCenter + 1))
				&& (isAdvancedMachineCasing(xCenter - 7, yCenter, zCenter - 1)) && (isAdvancedMachineCasing(xCenter - 7, yCenter, zCenter + 1))
				&& (isAdvancedMachineCasing(xCenter + 1, yCenter + 1, zCenter - 6)) && (isAdvancedMachineCasing(xCenter + 1, yCenter + 1, zCenter + 6))
				&& (isAdvancedMachineCasing(xCenter - 1, yCenter + 1, zCenter - 6)) && (isAdvancedMachineCasing(xCenter - 1, yCenter + 1, zCenter + 6))
				&& (isAdvancedMachineCasing(xCenter - 6, yCenter + 1, zCenter + 1)) && (isAdvancedMachineCasing(xCenter + 6, yCenter + 1, zCenter + 1))
				&& (isAdvancedMachineCasing(xCenter - 6, yCenter + 1, zCenter - 1)) && (isAdvancedMachineCasing(xCenter + 6, yCenter + 1, zCenter - 1))
				&& (isAdvancedMachineCasing(xCenter + 1, yCenter - 1, zCenter - 6)) && (isAdvancedMachineCasing(xCenter + 1, yCenter - 1, zCenter + 6))
				&& (isAdvancedMachineCasing(xCenter - 1, yCenter - 1, zCenter - 6)) && (isAdvancedMachineCasing(xCenter - 1, yCenter - 1, zCenter + 6))
				&& (isAdvancedMachineCasing(xCenter - 6, yCenter - 1, zCenter + 1)) && (isAdvancedMachineCasing(xCenter + 6, yCenter - 1, zCenter + 1))
				&& (isAdvancedMachineCasing(xCenter - 6, yCenter - 1, zCenter - 1)) && (isAdvancedMachineCasing(xCenter + 6, yCenter - 1, zCenter - 1))
				&& (this.mEnergyHatches.size() >= 1) && (this.mOutputBusses.size() >= 1) && (this.mInputHatches.size() >= 1) && (this.mInputBusses.size() >= 1)) {
			int mEnergyHatches_sS = this.mEnergyHatches.size();
			for (int i = 0; i < mEnergyHatches_sS; i++) {
				if (this.mEnergyHatches.get(i).mTier < tier()){
					log("bad energy hatch");
					return false;
				}
			}
			int mOutputHatches_sS = this.mOutputBusses.size();
			for (int i = 0; i < mOutputHatches_sS; i++) {
				if (this.mOutputBusses.get(i).mTier < tier()){
					log("bad output hatch");
					return false;
				}
			}
			int mInputHatches_sS = this.mInputHatches.size();
			for (int i = 0; i < mInputHatches_sS; i++) {
				if (this.mInputHatches.get(i).mTier < tier()){
					log("bad input hatch");
					return false;
				}
			}
			int mInputBusses_sS = this.mInputBusses.size();
			for (int i = 0; i < mInputBusses_sS; i++) {
				if (this.mInputBusses.get(i).mTier < tier()){
					log("bad input hatch");
					return false;
				}
			}
			mWrench = true;
			mScrewdriver = true;
			mSoftHammer = true;
			mHardHammer = true;
			mSolderingTool = true;
			mCrowbar = true;
			log("Built Cyclotron.");
			turnCasingActive(true);
			return true;
		}    
		log("Failed building Cyclotron.");
		return false;
	}

	private boolean checkCoils(int aX, int aY, int aZ) {
		return (isCyclotronCoil(aX + 6, aY, aZ - 1)) && (isCyclotronCoil(aX + 6, aY, aZ)) && (isCyclotronCoil(aX + 6, aY, aZ + 1)) && (isCyclotronCoil(aX + 5, aY, aZ - 3)) && (isCyclotronCoil(aX + 5, aY, aZ - 2))
				&& (isCyclotronCoil(aX + 5, aY, aZ + 2)) && (isCyclotronCoil(aX + 5, aY, aZ + 3)) && (isCyclotronCoil(aX + 4, aY, aZ - 4)) && (isCyclotronCoil(aX + 4, aY, aZ + 4))
				&& (isCyclotronCoil(aX + 3, aY, aZ - 5)) && (isCyclotronCoil(aX + 3, aY, aZ + 5)) && (isCyclotronCoil(aX + 2, aY, aZ - 5)) && (isCyclotronCoil(aX + 2, aY, aZ + 5))
				&& (isCyclotronCoil(aX + 1, aY, aZ - 6)) && (isCyclotronCoil(aX + 1, aY, aZ + 6)) && (isCyclotronCoil(aX, aY, aZ - 6)) && (isCyclotronCoil(aX, aY, aZ + 6)) && (isCyclotronCoil(aX - 1, aY, aZ - 6))
				&& (isCyclotronCoil(aX - 1, aY, aZ + 6)) && (isCyclotronCoil(aX - 2, aY, aZ - 5)) && (isCyclotronCoil(aX - 2, aY, aZ + 5)) && (isCyclotronCoil(aX - 3, aY, aZ - 5))
				&& (isCyclotronCoil(aX - 3, aY, aZ + 5)) && (isCyclotronCoil(aX - 4, aY, aZ - 4)) && (isCyclotronCoil(aX - 4, aY, aZ + 4)) && (isCyclotronCoil(aX - 5, aY, aZ - 3))
				&& (isCyclotronCoil(aX - 5, aY, aZ - 2)) && (isCyclotronCoil(aX - 5, aY, aZ + 2)) && (isCyclotronCoil(aX - 5, aY, aZ + 3)) && (isCyclotronCoil(aX - 6, aY, aZ - 1))
				&& (isCyclotronCoil(aX - 6, aY, aZ)) && (isCyclotronCoil(aX - 6, aY, aZ + 1));
	}

	private boolean checkUpperOrLowerHulls(int aX, int aY, int aZ) {
		return (isAdvancedMachineCasing(aX + 6, aY, aZ)) && (isAdvancedMachineCasing(aX + 5, aY, aZ - 3)) && (isAdvancedMachineCasing(aX + 5, aY, aZ - 2))
				&& (isAdvancedMachineCasing(aX + 5, aY, aZ + 2)) && (isAdvancedMachineCasing(aX + 5, aY, aZ + 3)) && (isAdvancedMachineCasing(aX + 4, aY, aZ - 4))
				&& (isAdvancedMachineCasing(aX + 4, aY, aZ + 4)) && (isAdvancedMachineCasing(aX + 3, aY, aZ - 5)) && (isAdvancedMachineCasing(aX + 3, aY, aZ + 5))
				&& (isAdvancedMachineCasing(aX + 2, aY, aZ - 5)) && (isAdvancedMachineCasing(aX + 2, aY, aZ + 5)) && (isAdvancedMachineCasing(aX, aY, aZ - 6))
				&& (isAdvancedMachineCasing(aX, aY, aZ + 6)) && (isAdvancedMachineCasing(aX - 2, aY, aZ - 5)) && (isAdvancedMachineCasing(aX - 2, aY, aZ + 5))
				&& (isAdvancedMachineCasing(aX - 3, aY, aZ - 5)) && (isAdvancedMachineCasing(aX - 3, aY, aZ + 5)) && (isAdvancedMachineCasing(aX - 4, aY, aZ - 4))
				&& (isAdvancedMachineCasing(aX - 4, aY, aZ + 4)) && (isAdvancedMachineCasing(aX - 5, aY, aZ - 3)) && (isAdvancedMachineCasing(aX - 5, aY, aZ - 2))
				&& (isAdvancedMachineCasing(aX - 5, aY, aZ + 2)) && (isAdvancedMachineCasing(aX - 5, aY, aZ + 3)) && (isAdvancedMachineCasing(aX - 6, aY, aZ));
	}

	private boolean checkHulls(int aX, int aY, int aZ) {
		return (isAdvancedMachineCasing(aX + 6, aY, aZ - 3)) && (isAdvancedMachineCasing(aX + 6, aY, aZ - 2)) && (isAdvancedMachineCasing(aX + 6, aY, aZ + 2))
				&& (isAdvancedMachineCasing(aX + 6, aY, aZ + 3)) && (isAdvancedMachineCasing(aX + 3, aY, aZ - 6)) && (isAdvancedMachineCasing(aX + 3, aY, aZ + 6))
				&& (isAdvancedMachineCasing(aX + 2, aY, aZ - 6)) && (isAdvancedMachineCasing(aX + 2, aY, aZ + 6)) && (isAdvancedMachineCasing(aX - 2, aY, aZ - 6))
				&& (isAdvancedMachineCasing(aX - 2, aY, aZ + 6)) && (isAdvancedMachineCasing(aX - 3, aY, aZ - 6)) && (isAdvancedMachineCasing(aX - 3, aY, aZ + 6))
				&& (isAdvancedMachineCasing(aX - 7, aY, aZ)) && (isAdvancedMachineCasing(aX + 7, aY, aZ)) && (isAdvancedMachineCasing(aX, aY, aZ - 7)) && (isAdvancedMachineCasing(aX, aY, aZ + 7))
				&& (isAdvancedMachineCasing(aX - 6, aY, aZ - 3)) && (isAdvancedMachineCasing(aX - 6, aY, aZ - 2)) && (isAdvancedMachineCasing(aX - 6, aY, aZ + 2))
				&& (isAdvancedMachineCasing(aX - 6, aY, aZ + 3)) && (isAdvancedMachineCasing(aX - 4, aY, aZ - 2)) && (isAdvancedMachineCasing(aX - 4, aY, aZ + 2))
				&& (isAdvancedMachineCasing(aX + 4, aY, aZ - 2)) && (isAdvancedMachineCasing(aX + 4, aY, aZ + 2)) && (isAdvancedMachineCasing(aX - 2, aY, aZ - 4))
				&& (isAdvancedMachineCasing(aX - 2, aY, aZ + 4)) && (isAdvancedMachineCasing(aX + 2, aY, aZ - 4)) && (isAdvancedMachineCasing(aX + 2, aY, aZ + 4));
	}

	private boolean isAdvancedMachineCasing(int aX, int aY, int aZ) {
		final Block aBlock = getBaseMetaTileEntity().getBlock(aX, aY, aZ);
		final int aMeta = getBaseMetaTileEntity().getMetaID(aX, aY, aZ);	
		final IGregTechTileEntity tTileEntity2 = getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ);	
		
		boolean debug = isValidBlockForStructure(tTileEntity2, TAE.GTPP_INDEX(26), true, aBlock, aMeta, getCasing(), getCasingMeta());		
		
		/*if (!debug) {
			this.getBaseMetaTileEntity().getWorld().setBlock(aX, aY, aZ, ModBlocks.blockCompressedObsidian);
			log(""+aX+"/"+aY+"/"+aZ);
		}*/
		
		return debug;
	}

	private boolean isCyclotronCoil(int aX, int aY, int aZ) {
		
		final Block aBlock = getBaseMetaTileEntity().getBlock(aX, aY, aZ);
		final int aMeta = getBaseMetaTileEntity().getMetaID(aX, aY, aZ);	
		
		boolean debug = isValidBlockForStructure(null, 0, false, aBlock, aMeta, getCyclotronCoil(), getCyclotronCoilMeta());		
		
		/*if (!debug) {
			this.getBaseMetaTileEntity().getWorld().setBlock(aX, aY, aZ, ModBlocks.blockCompressedObsidian);
			log(""+aX+"/"+aY+"/"+aZ);
		}*/
		
		return debug;		
	}

	public Block getCasing() {
		return ModBlocks.blockCasings2Misc;
	}

	public int getCasingMeta() {
		return 10;
	}

	public Block getCyclotronCoil() {
		return ModBlocks.blockCasings2Misc;
	}

	public int getCyclotronCoilMeta() {
		return 9;
	}

	@Override
	public String[] getTooltip() {
		return new String[]{
				"Super Magnetic Speed Shooter",
				"------------------------------------------------------------",
				"Particles are accelerated over 186 revolutions to 80% light speed",
				"Can produce a continuous beam current of 2.2 mA at 590 MeV",
				"Which will be extracted from the Isochronous Cyclotron",
				"------------------------------------------------------------",
				"Consists of the same layout as a Fusion Reactor",
				"Any external casing can be a hatch/bus, unlike Fusion",
				"Cyclotron Machine Casings around Cyclotron Coil Blocks", 
				"All Hatches must be IV or better",
				"1-16 Input Hatches", 
				"1-16 Input Busses",
				"1-16 Output Busses", 
				"1-16 Energy Hatches", 
				};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		ITexture[] sTexture;
		if (aSide == aFacing) {
			sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF, Dyes.getModulation(-1, Dyes._NULL.mRGBa)), new GT_RenderedTexture(getIconOverlay())};
		} else {
			if (!aActive) {
				sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF, Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
			} else {
				sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF, Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
			}
		}
		return sTexture;
	}

	public IIconContainer getIconOverlay() {
		if (this.getBaseMetaTileEntity().isActive()){
			return TexturesGtBlock.Overlay_MatterFab_Active_Animated;
		}
		return TexturesGtBlock.Overlay_MatterFab_Animated;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		
		/*if (CORE.DEVENV) {
			return this.checkRecipeGeneric();
		}*/
		
		
		//log("Recipe Check.");
		ArrayList<ItemStack> tItemList = getStoredInputs();
		ItemStack[] tItemInputs = tItemList.toArray(new ItemStack[tItemList.size()]);
		ArrayList<FluidStack> tInputList = getStoredFluids();
		FluidStack[] tFluidInputs = tInputList.toArray(new FluidStack[tInputList.size()]);
		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

		GT_Recipe tRecipe = Recipe_GT.Gregtech_Recipe_Map.sCyclotronRecipes.findRecipe(getBaseMetaTileEntity(), false,
				gregtech.api.enums.GT_Values.V[tTier], tFluidInputs, tItemInputs);
		if (tRecipe != null){
			if (tRecipe.isRecipeInputEqual(true, tFluidInputs, tItemInputs)) {
				
				this.mEfficiency = (10000 - ((getIdealStatus() - getRepairStatus()) * 1000));
				this.mEfficiencyIncrease = 10000;
				this.mEUt = tRecipe.mEUt;
				this.mMaxProgresstime = tRecipe.mDuration;
				
				while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
					this.mEUt *= 4;
					this.mMaxProgresstime /= 2;
				}
				
				if (this.mEUt > 0) {
					this.mEUt = (-this.mEUt);
				}
				
				this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
				
				final ItemStack[] outputs = new ItemStack[tRecipe.mOutputs.length];				
				for (int i = 0; i < tRecipe.mOutputs.length; i++){
					if (this.getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(i)){
						Logger.WARNING("Adding a bonus output");
						outputs[i] = tRecipe.getOutput(i);
					}
					else {
						Logger.WARNING("Adding null output");
						outputs[i] = null;
					}
				}

				for (ItemStack s : outputs) {
					if (s != null) {
						if (s.getItem() instanceof IonParticles) {
							long aCharge = IonParticles.getChargeState(s);
							if (aCharge == 0) {
								IonParticles.setChargeState(s, MathUtils.getRandomFromArray(new int[] {
										-5, -5,
										-4, -4, -4, 
										-3, -3, -3, -3, -3,
										-2, -2, -2, -2, -2, -2, -2,
										-1, -1, -1, -1, -1, -1, -1, -1,
										1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
										2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
										3, 3, 3, 3, 3, 3, 3,
										4, 4, 4, 4,
										5, 5, 5,
										6, 6}));
							}
						}
					}
				}
				
				this.mOutputItems = outputs;
				this.mOutputFluids = new FluidStack[] {tRecipe.getFluidOutput(0)};
				return true;
			}
		}		
		return false;
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
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide()) {
			if (mEfficiency < 0)
				mEfficiency = 0;			
			
			//Time Counter
			this.mTotalRunTime++;
			
			onRunningTick(null);			
			
			if (mRunningOnLoad && checkMultiblock(aBaseMetaTileEntity, mInventory[1])) {
				this.mEUStore = (int) aBaseMetaTileEntity.getStoredEU();
				checkRecipe(mInventory[1]);
			}
			if (--mUpdate == 0 || --mStartUpCheck == 0) {
				mInputHatches.clear();
				mInputBusses.clear();
				mOutputHatches.clear();
				mOutputBusses.clear();
				mDynamoHatches.clear();
				mEnergyHatches.clear();
				mMufflerHatches.clear();
				mMaintenanceHatches.clear();
				mChargeHatches.clear();
				mDischargeHatches.clear();
				mControlCoreBus.clear();
				mMultiDynamoHatches.clear();
				mMachine = checkMultiblock(aBaseMetaTileEntity, mInventory[1]);
			}
			if (mStartUpCheck < 0) {
				if (mMachine) {
					if (this.mEnergyHatches != null) {
						for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
							if (isValidMetaTileEntity(tHatch)) {
								if (aBaseMetaTileEntity.getStoredEU() + (2048*4) < maxEUStore()
										&& tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits((2048*4), false)) {
									aBaseMetaTileEntity.increaseStoredEnergyUnits((2048*4), true);
								}
							}
					}
					if (this.mEUStore <= 0 && mMaxProgresstime > 0) {
						stopMachine();
					}
					if (getRepairStatus() > 0) {
						if (mMaxProgresstime > 0) {							
							this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(mEUt, true);
							if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
								if (mOutputItems != null)
									for (ItemStack tStack : mOutputItems) if (tStack != null) addOutput(tStack);
								if (mOutputFluids != null)
									for (FluidStack tStack : mOutputFluids) if (tStack != null) addOutput(tStack);
								mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
								mOutputItems = null;
								mProgresstime = 0;
								mMaxProgresstime = 0;
								mEfficiencyIncrease = 0;
								if (mOutputFluids != null && mOutputFluids.length > 0) {
									try {
										GT_Mod.instance.achievements.issueAchivementHatchFluid(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), mOutputFluids[0]);
									} catch (Exception e) {
									}
								}
								this.mEUStore = (int) aBaseMetaTileEntity.getStoredEU();
								if (aBaseMetaTileEntity.isAllowedToWork())
									checkRecipe(mInventory[1]);
							}
						} else {
							if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified()) {
								turnCasingActive(mMaxProgresstime > 0);
								if (aBaseMetaTileEntity.isAllowedToWork()) {
									this.mEUStore = (int) aBaseMetaTileEntity.getStoredEU();
									if (checkRecipe(mInventory[1])) {
										if (this.mEUStore < this.mLastRecipe.mSpecialValue) {
											mMaxProgresstime = 0;
											turnCasingActive(false);
										}
										aBaseMetaTileEntity.decreaseStoredEnergyUnits(this.mLastRecipe.mSpecialValue, true);
									}
								}
								if (mMaxProgresstime <= 0)
									mEfficiency = Math.max(0, mEfficiency - 1000);
							}
						}
					} else {
						this.mLastRecipe = null;
						stopMachine();
					}
				} else {
					turnCasingActive(false);
					this.mLastRecipe = null;
					stopMachine();
				}
			}
			doRandomMaintenanceDamage();
			aBaseMetaTileEntity.setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mWrench ? 0 : 1) | (mScrewdriver ? 0 : 2) | (mSoftHammer ? 0 : 4) | (mHardHammer ? 0 : 8)
					| (mSolderingTool ? 0 : 16) | (mCrowbar ? 0 : 32) | (mMachine ? 0 : 64));
			aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
		}
	}

	@Override
	public boolean onRunningTick(ItemStack aStack) {	
		if (this.mOutputBusses.size() > 0) {
			for (GT_MetaTileEntity_Hatch_OutputBus g : this.mOutputBusses) {
				if (g != null) {
					for (ItemStack s : g.mInventory) {
						if (s != null) {
							if (s.getItem() instanceof IonParticles) {
								long aCharge = IonParticles.getChargeState(s);
								if (aCharge == 0) {
									IonParticles.setChargeState(s, MathUtils.getRandomFromArray(new int[] {
											-5, -5,
											-4, -4, -4, 
											-3, -3, -3, -3, -3,
											-2, -2, -2, -2, -2, -2, -2,
											-1, -1, -1, -1, -1, -1, -1, -1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
											2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
											3, 3, 3, 3, 3, 3, 3,
											4, 4, 4, 4,
											5, 5, 5,
											6, 6}));
								}
							}
						}
					}
				}
			}
		}
		return true;
	}


	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 10;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}
	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public String[] getExtraInfoData() {
		String tier = tier() == 5 ? "I" : "II";
		float plasmaOut = 0;
		int powerRequired = 0;
		if (this.mLastRecipe != null) {
			powerRequired = this.mLastRecipe.mEUt;
			if (this.mLastRecipe.getFluidOutput(0) != null) {
				plasmaOut = (float)this.mLastRecipe.getFluidOutput(0).amount / (float)this.mLastRecipe.mDuration;
			}
		}

		return new String[]{
				"COMET - Compact Cyclotron MK "+tier,
				"EU Required: "+powerRequired+"EU/t",
				"Stored EU: "+mEUStore+" / "+maxEUStore()};
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@SuppressWarnings("deprecation")
	public boolean turnCasingActive(final boolean status) {
		if (this.mEnergyHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Muffler hatch : this.mMufflerHatches) {
				hatch.mMachineBlock = status ? (byte) TAE.GTPP_INDEX(26) : (byte) TAE.GTPP_INDEX(26);
			}
		}
		if (this.mOutputHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Output hatch : this.mOutputHatches) {
				hatch.mMachineBlock = status ? (byte) TAE.GTPP_INDEX(26) : (byte) TAE.GTPP_INDEX(26);
			}
		}
		if (this.mInputHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Input hatch : this.mInputHatches) {
				hatch.mMachineBlock = status ? (byte) TAE.GTPP_INDEX(26) : (byte) TAE.GTPP_INDEX(26);
			}
		}
		if (this.mMaintenanceHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Maintenance hatch : this.mMaintenanceHatches) {
				hatch.mMachineBlock = status ? (byte) TAE.GTPP_INDEX(26) : (byte) TAE.GTPP_INDEX(26);
			}
		}
		return true;
	}
}