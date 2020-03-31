package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;
import java.util.Collection;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_LargeSemifluidGenerator extends GregtechMeta_MultiBlockBase {

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

	public String[] getTooltip() {
		return new String[]{
				"Controller Block for the Large Semifluid Generator",
				"Engine Intake Casings must not be obstructed in front (only air blocks)",
				"Supply Semifluid Fuels and 2000L of Lubricant per hour to run.",
				"Supply 80L of Oxygen per second to boost output (optional).",
				"Default: Produces 2048EU/t at 100% efficiency",
				"Boosted: Produces 6144EU/t at 150% efficiency",
				"Size(WxHxD): 3x3x4, Controller (front centered)",
				"3x3x4 of Stable Titanium Machine Casing (hollow, Min 16!)",
				"All hatches except dynamo can replace any Stable Titanium casing in middle two segments",
				"2x Steel Gear Box Machine Casing inside the Hollow Casing",
				"8x Engine Intake Machine Casing (around controller)",
				"2x Input Hatch (Fuel/Lubricant)",
				"1x Maintenance Hatch",
				"1x Muffler Hatch",
				"1x Dynamo Hatch (back centered)",
		};
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[50], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[50]};
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
		Collection<GT_Recipe> tRecipeList = Gregtech_Recipe_Map.sSemiFluidLiquidFuels.mRecipeList;

		if(tFluids.size() > 0 && tRecipeList != null) { //Does input hatch have a semifluid fuel?
			for (FluidStack hatchFluid1 : tFluids) { //Loops through hatches
				for(GT_Recipe aFuel : tRecipeList) { //Loops through semifluid fuel recipes
					FluidStack tLiquid;
					if ((tLiquid = GT_Utility.getFluidForFilledItem(aFuel.getRepresentativeInput(0), true)) != null) { //Create fluidstack from current recipe
						if (hatchFluid1.isFluidEqual(tLiquid)) { //Has a semifluid fluid
							fuelConsumption = tLiquid.amount = boostEu ? (4096 / aFuel.mSpecialValue) : (2048 / aFuel.mSpecialValue); //Calc fuel consumption
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
								fuelRemaining = hatchFluid1.amount; //Record available fuel
								this.mEUt = mEfficiency < 2000 ? 0 : 2048; //Output 0 if startup is less than 20%
								this.mProgresstime = 1;
								this.mMaxProgresstime = 1;
								this.mEfficiencyIncrease = 15;
								return true;
							}
						}
					}
				}
			}
		}
		this.mEUt = 0;
		this.mEfficiency = 0;
		return false;
	}

	@Override
	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		byte tSide = aBaseMetaTileEntity.getBackFacing();
		int aTileX = aBaseMetaTileEntity.getXCoord();
		int aTileY = aBaseMetaTileEntity.getYCoord();
		int aTileZ = aBaseMetaTileEntity.getZCoord();
		boolean xFacing = (tSide == 4 || tSide == 5);
		boolean zFacing = (tSide == 2 || tSide == 3);
		int aCasingCount = 0;
		// Check Intake Hatches
		for (int aHorizontalOffset = -1; aHorizontalOffset < 2; aHorizontalOffset++) {
			for (int aVerticalOffset = -1; aVerticalOffset < 2; aVerticalOffset++) {
				if (aHorizontalOffset == 0 && aVerticalOffset == 0) {
					continue;
				}			
				int aX = !xFacing ? (aTileX + aHorizontalOffset) : aTileX;
				int aY = aTileY + aVerticalOffset;
				int aZ = !zFacing ? (aTileZ + aHorizontalOffset) : aTileZ;
				Block aIntakeBlock = aBaseMetaTileEntity.getBlock(aX, aY, aZ);
				int aIntakeMeta = aBaseMetaTileEntity.getMetaID(aX, aY, aZ);				
				if (!isValidBlockForStructure(null, 0, false, aIntakeBlock, aIntakeMeta, getIntakeBlock(), getIntakeMeta())) {
					return false; // Not intake casing surrounding controller					
				}
			}
		}
		// Check Casings
		int aStartDepthOffset = (tSide == 2 || tSide == 4) ? -1 : 1;
		int aFinishDepthOffset = (tSide == 2 || tSide == 4) ? -4 : 4;
		for (int aDepthOffset = aStartDepthOffset; aDepthOffset != aFinishDepthOffset;) {
			for (int aHorizontalOffset = -1; aHorizontalOffset < 2; aHorizontalOffset++) {
				for (int aVerticalOffset = -1; aVerticalOffset < 2; aVerticalOffset++) {
					if (aHorizontalOffset == 0 && aVerticalOffset == 0) {
						continue;
					}				
					int aX = !xFacing ? (aTileX + aHorizontalOffset) : (aTileX + aDepthOffset);
					int aY = aTileY + aVerticalOffset;
					int aZ = !zFacing ? (aTileZ + aHorizontalOffset) : (aTileZ + aDepthOffset);
					Block aCasingBlock = aBaseMetaTileEntity.getBlock(aX, aY, aZ);
					int aCasingMeta = aBaseMetaTileEntity.getMetaID(aX, aY, aZ);	
					IGregTechTileEntity aTileEntity = getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ);
					// Side areas
					if (aDepthOffset < 3) {
						if (!isValidBlockForStructure(aTileEntity, getCasingTextureIndex(), true, aCasingBlock, aCasingMeta, getCasingBlock(), getCasingMeta())) {
							return false; // Not valid casing					
						}
						else {
							if (aTileEntity == null) {
								aCasingCount++;
							}
						}
					}
					else {
						if (!isValidBlockForStructure(null, 0, false, aCasingBlock, aCasingMeta, getCasingBlock(), getCasingMeta())) {
							return false; // Not valid casing					
						}
						else {
							aCasingCount++;
						}
					}
				}
			}
			// Count Backwards for 2 axis
			if (aStartDepthOffset == -1) {
				aDepthOffset--;
			}
			// Count Forwards for 2 axis
			else {
				aDepthOffset++;
			}
		}

		// Check Gear Boxes
		if(aBaseMetaTileEntity.getBlockAtSideAndDistance(tSide, 1) != getGearboxBlock() && aBaseMetaTileEntity.getBlockAtSideAndDistance(tSide, 2) != getGearboxBlock()) {
			return false;
		}
		if(aBaseMetaTileEntity.getMetaIDAtSideAndDistance(tSide, 1) != getGearboxMeta() && aBaseMetaTileEntity.getMetaIDAtSideAndDistance(tSide, 2) != getGearboxMeta()) {
			return false;
		}
		
		// Check Dynamo
		this.mDynamoHatches.clear();
		IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 3);
		if ((tTileEntity != null) && (tTileEntity.getMetaTileEntity() != null)) {
			if ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Dynamo)) {
				this.addDynamoToMachineList(tTileEntity, getCasingTextureIndex());
			} else {
				return false;
			}
		}
		return aCasingCount >= 16;
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

	private boolean addToMachineList(IGregTechTileEntity tTileEntity) {
		return ((addMaintenanceToMachineList(tTileEntity, getCasingTextureIndex())) || (addInputToMachineList(tTileEntity, getCasingTextureIndex())) || (addOutputToMachineList(tTileEntity, getCasingTextureIndex())) || (addMufflerToMachineList(tTileEntity, getCasingTextureIndex())));
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
