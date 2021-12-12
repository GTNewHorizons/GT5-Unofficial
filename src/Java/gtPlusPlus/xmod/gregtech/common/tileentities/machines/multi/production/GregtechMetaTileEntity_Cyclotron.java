package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GTPP_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMetaTileEntity_Cyclotron extends GregtechMeta_MultiBlockBase {

	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_Cyclotron> STRUCTURE_DEFINITION = null;

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
		return null;
	}	

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return super.getServerGUI(aID, aPlayerInventory, aBaseMetaTileEntity);
		//return new CONTAINER_Cyclotron(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return super.getClientGUI(aID, aPlayerInventory, aBaseMetaTileEntity);
		//return new GUI_Cyclotron(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), Recipe_GT.Gregtech_Recipe_Map.sCyclotronRecipes.mNEIName);
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
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_Cyclotron> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_Cyclotron>builder()
					.addShape(mName, transpose(new String[][]{
							{
									"               ",
									"      hhh      ",
									"    hh   hh    ",
									"   h       h   ",
									"  h         h  ",
									"  h         h  ",
									" h           h ",
									" h           h ",
									" h           h ",
									"  h         h  ",
									"  h         h  ",
									"   h       h   ",
									"    hh   hh    ",
									"      hhh      ",
									"               ",
							},
							{
									"      hhh      ",
									"    hhccchh    ",
									"   hcchhhcch   ",
									"  hchh   hhch  ",
									" hch       hch ",
									" hch       hch ",
									"hch         hch",
									"hch         hch",
									"hch         hch",
									" hch       hch ",
									" hch       hch ",
									"  hchh   hhch  ",
									"   hcch~hcch   ",
									"    hhccchh    ",
									"      hhh      ",
							},
							{
									"               ",
									"      hhh      ",
									"    hh   hh    ",
									"   h       h   ",
									"  h         h  ",
									"  h         h  ",
									" h           h ",
									" h           h ",
									" h           h ",
									"  h         h  ",
									"  h         h  ",
									"   h       h   ",
									"    hh   hh    ",
									"      hhh      ",
									"               ",
							}
					}))
					.addElement(
							'h',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_Cyclotron::addCyclotronList, 44, 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													getCasing(), getCasingMeta()
											)
									)
							)
					)
					.addElement(
							'c',
							ofBlock(
									getCyclotronCoil(), getCyclotronCoilMeta()
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 7, 1, 12);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 7, 1, 12) && mCasing >= 40 && checkHatch();
	}

	public final boolean addCyclotronList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus && ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mTier >= 5){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy && ((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity).mTier >= 5){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus && ((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity).mTier >= 5) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler && ((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity).mTier >= 5) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input && ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mTier >= 5) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
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
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Super Magnetic Speed Shooter")
				.addSeparator()
				.addInfo("Particles are accelerated over 186 revolutions to 80% light speed")
				.addInfo("Can produce a continuous beam current of 2.2 mA at 590 MeV")
				.addInfo("Which will be extracted from the Isochronous Cyclotron")
				.addSeparator()
				.addInfo("Consists of the same layout as a Fusion Reactor")
				.addInfo("Any external casing can be a hatch/bus, unlike Fusion")
				.addInfo("Cyclotron Machine Casings around Cyclotron Coil Blocks")
				.addInfo("All Hatches must be IV or better")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.addCasingInfo("Cyclotron Machine Casings", 40)
				.addCasingInfo("Cyclotron Coil", 32)
				.addInputBus("Any Casing", 1)
				.addOutputBus("Any Casing", 1)
				.addInputHatch("Any Casing", 1)
				.addEnergyHatch("Any Casing", 1)
				.addMaintenanceHatch("Any Casing", 1)
				.addMufflerHatch("Any Casing", 1)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		ITexture[] sTexture;
		if (aSide == aFacing) {
			sTexture = new ITexture[]{Textures.BlockIcons.getCasingTextureForId(44), new GT_RenderedTexture(getIconOverlay())};
		} else {
			if (!aActive) {
				sTexture = new ITexture[]{Textures.BlockIcons.getCasingTextureForId(44)};
			} else {
				sTexture = new ITexture[]{Textures.BlockIcons.getCasingTextureForId(44)};
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
		this.fixAllMaintenanceIssue();		
		
		//log("Recipe Check.");
		ArrayList<ItemStack> tItemList = getStoredInputs();
		ItemStack[] tItemInputs = tItemList.toArray(new ItemStack[tItemList.size()]);
		ArrayList<FluidStack> tInputList = getStoredFluids();
		FluidStack[] tFluidInputs = tInputList.toArray(new FluidStack[tInputList.size()]);
		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

		GT_Recipe tRecipe = GTPP_Recipe.GTPP_Recipe_Map.sCyclotronRecipes.findRecipe(getBaseMetaTileEntity(), false,
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
		this.fixAllMaintenanceIssue();
		return super.onRunningTick(aStack);
	}


	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerSecond(ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiCyclotron;
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
				"Stored EU: "+this.getEUVar()+" / "+maxEUStore()};
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@SuppressWarnings("deprecation")
	public boolean turnCasingActive(final boolean status) {
		if (this.mEnergyHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Muffler hatch : this.mMufflerHatches) {
				hatch.mMachineBlock = status ? (byte) 44 : (byte) 44;
			}
		}
		if (this.mOutputHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Output hatch : this.mOutputHatches) {
				hatch.mMachineBlock = status ? (byte) 44 : (byte) 44;
			}
		}
		if (this.mInputHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Input hatch : this.mInputHatches) {
				hatch.mMachineBlock = status ? (byte) 44 : (byte) 44;
			}
		}
		if (this.mMaintenanceHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Maintenance hatch : this.mMaintenanceHatches) {
				hatch.mMachineBlock = status ? (byte) 44 : (byte) 44;
			}
		}
		return true;
	}
}