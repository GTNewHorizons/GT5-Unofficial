package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMetaTileEntity_LargeSemifluidGenerator extends GregtechMeta_MultiBlockBase {

	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_LargeSemifluidGenerator> STRUCTURE_DEFINITION = null;

	protected int fuelConsumption = 0;
	protected int fuelValue = 0;
	protected int fuelRemaining = 0;
	protected boolean boostEu = false;

	public GregtechMetaTileEntity_LargeSemifluidGenerator(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_LargeSemifluidGenerator(String aName) {
		super(aName);
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Large Semifluid Generator")
				.addInfo("Engine Intake Casings must not be obstructed in front (only air blocks)")
				.addInfo("Supply Semifluid Fuels and 2000L of Lubricant per hour to run.")
				.addInfo("Supply 80L of Oxygen per second to boost output (optional).")
				.addInfo("Default: Produces 2048EU/t at 100% efficiency")
				.addInfo("Boosted: Produces 6144EU/t at 150% efficiency")
				.addPollutionAmount(getPollutionPerTick(null) * 20)
				.addSeparator()
				.beginStructureBlock(3, 3, 4, false)
				.addController("Front Center")
				.addCasingInfo("Stable Titanium Machine Casing", 16)
				.addCasingInfo("Steel Gear Box Machine Casing", 2)
				.addCasingInfo("Engine Intake Machine Casing", 8)
				.addInputHatch("Any Casing", 1)
				.addMaintenanceHatch("Any Casing", 1)
				.addMufflerHatch("Any Casing", 1)
				.addDynamoHatch("Back Center", 2)
				.toolTipFinisher("GT++");
		return tt;
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(50), new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(50)};
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return getMaxEfficiency(aStack) > 0;
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeDieselEngine.png");
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ArrayList<FluidStack> tFluids = getStoredFluids();

		if(tFluids.size() > 0) { //Does input hatch have a semifluid fuel?
			for (FluidStack hatchFluid : tFluids) { //Loops through hatches
				GT_Recipe aFuel = GTPP_Recipe_Map.sSemiFluidLiquidFuels.findFuel(hatchFluid);
				if (aFuel == null) {
					// Not a valid semi-fluid fuel.
					continue;
				}

				fuelConsumption = boostEu ? (4096 / aFuel.mSpecialValue) : (2048 / aFuel.mSpecialValue); //Calc fuel consumption
				FluidStack tLiquid = new FluidStack(hatchFluid.getFluid(), fuelConsumption);
				if(depleteInput(tLiquid)) { //Deplete that amount
					boostEu = depleteInput(Materials.Oxygen.getGas(4L));
					if(tFluids.contains(Materials.Lubricant.getFluid(2L))) { //Has lubricant?
						//Deplete Lubricant. 2000L should = 1 hour of runtime (if baseEU = 2048)
						if(mRuntime % 72 == 0 || mRuntime == 0) {
							depleteInput(Materials.Lubricant.getFluid(boostEu ? 2 : 1));
						}
					}
					else {
						return false;
					}

					fuelValue = aFuel.mSpecialValue;
					fuelRemaining = hatchFluid.amount; //Record available fuel
					this.mEUt = mEfficiency < 2000 ? 0 : 2048; //Output 0 if startup is less than 20%
					this.mProgresstime = 1;
					this.mMaxProgresstime = 1;
					this.mEfficiencyIncrease = 15;
					return true;
				}
			}
		}
		this.mEUt = 0;
		this.mEfficiency = 0;
		return false;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_LargeSemifluidGenerator> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_LargeSemifluidGenerator>builder()
					.addShape(mName, transpose(new String[][]{
							{"III", "CCC", "CCC", "CCC"},
							{"I~I", "CGC", "CGC", "CMC"},
							{"III", "CCC", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_LargeSemifluidGenerator::addLargeSemifluidGeneratorList, getCasingTextureIndex(), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													getCasingBlock(), getCasingMeta()
											)
									)
							)
					)
					.addElement(
							'G',
							ofBlock(
									getGearboxBlock(), getGearboxMeta()
							)
					)
					.addElement(
							'I',
							ofBlock(
									getIntakeBlock(), getIntakeMeta()
							)
					)
					.addElement(
							'M',
							ofHatchAdder(
									GregtechMetaTileEntity_LargeSemifluidGenerator::addLargeSemifluidGeneratorBackList, getCasingTextureIndex(), 2
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 1, 1, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		mDynamoHatches.clear();
		return checkPiece(mName, 1, 1, 0) && mCasing >= 16 && checkHatch();
	}

	public final boolean addLargeSemifluidGeneratorList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
			}
		}
		return false;
	}

	public final boolean addLargeSemifluidGeneratorBackList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo)aMetaTileEntity);
			}
		}
		return false;
	}

	public Block getCasingBlock() {
		return GregTech_API.sBlockCasings4;
	}

	public byte getCasingMeta() {
		return 2;
	}

	public Block getIntakeBlock() {
		return GregTech_API.sBlockCasings4;
	}

	public byte getIntakeMeta() {
		return 13;
	}

	public Block getGearboxBlock() {
		return GregTech_API.sBlockCasings2;
	}

	public byte getGearboxMeta() {
		return 3;
	}

	public byte getCasingTextureIndex() {
		return 50;
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_LargeSemifluidGenerator(this.mName);
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
	public int getDamageToComponent(ItemStack aStack) {
		return 1;
	}

	public int getMaxEfficiency(ItemStack aStack) {
		return boostEu ? 20000 : 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 64;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return true;
	}

	@Override
	public String[] getExtraInfoData() {
		return new String[]{
				"Large Semifluid Generator",
				"Current Output: " + mEUt * mEfficiency / 10000 + " EU/t",
				"Fuel Consumption: " + fuelConsumption + "L/t",
				"Fuel Value: " + fuelValue + " EU/L",
				"Fuel Remaining: " + fuelRemaining + " Litres",
				"Current Efficiency: " + (mEfficiency / 100) + "%",
				getIdealStatus() == getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance"};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
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
		return "Semifluid Generator";
	}

	@Override
	public int getMaxParallelRecipes() {
		return 0;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}
}
