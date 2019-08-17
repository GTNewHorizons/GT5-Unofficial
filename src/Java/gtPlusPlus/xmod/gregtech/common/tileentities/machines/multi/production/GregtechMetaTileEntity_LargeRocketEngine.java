package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;
import java.util.Collection;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.RocketFuels;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_LargeRocketEngine extends GregtechMeta_MultiBlockBase
{
    protected int fuelConsumption;
    protected int fuelValue;
    protected int fuelRemaining;
    protected boolean boostEu;

	public static String mLubricantName = "Carbon Dioxide";
	public static String mCoolantName = "Liquid Hydrogen";
	
	public static String mCasingName = "Turbodyne Casing";
	public static String mIntakeHatchName = "Tungstensteel Turbine Casing";
	public static String mGearboxName = "Inconel Reinforced Casing";

    
    private final static int CASING_ID = 50;
    
    public GregtechMetaTileEntity_LargeRocketEngine(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.fuelConsumption = 0;
        this.fuelValue = 0;
        this.fuelRemaining = 0;
        this.boostEu = false;
    }
    
    public GregtechMetaTileEntity_LargeRocketEngine(final String aName) {
        super(aName);
        this.fuelConsumption = 0;
        this.fuelValue = 0;
        this.fuelRemaining = 0;
        this.boostEu = false;
    }
    
    @Override
    public String[] getTooltip() {
        return new String[] { 
        		"Controller Block for the Large Combustion Engine",
        		"Supply Rocket Fuels and 1000L of "+mLubricantName+" per hour to run.",
        		"Supply 40L of "+mCoolantName+" per second to boost output (optional).", 
        		"Default: Produces "+GT_Values.V[5]+"EU/t at 100% efficiency", 
        		"Boosted: Produces "+(GT_Values.V[5]*3)+"EU/t at 150% efficiency",
        		"Size(WxHxD): 3x3x4, Controller (front centered)",
        		"3x3x4 of Stable "+mCasingName+" (hollow, Min 16!)",
        		"2x "+mGearboxName+" inside the Hollow Casing",
        		"8x "+mIntakeHatchName+" (around controller)",
        		""+mIntakeHatchName+" must not be obstructed in front (only air blocks)",
        		"2x Input Hatch (Fuel/Lubricant) (one of the Casings next to a Gear Box)",
        		"1x Maintenance Hatch (one of the Casings next to a Gear Box)", 
        		"1x Muffler Hatch (top middle back, next to the rear Gear Box)",
        		"1x Dynamo Hatch (back centered)",
        		};
    }
    
    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[] { Textures.BlockIcons.CASING_BLOCKS[CASING_ID], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE) };
        }
        return new ITexture[] { Textures.BlockIcons.CASING_BLOCKS[CASING_ID] };
    }
    
    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        return this.getMaxEfficiency(aStack) > 0;
    }
    
    @Override
    public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "LargeDieselEngine.png");
    }
    
    @Override
    public boolean checkRecipe(final ItemStack aStack) {
        final ArrayList<FluidStack> tFluids = this.getStoredFluids();
        final Collection<GT_Recipe> tRecipeList = Recipe_GT.Gregtech_Recipe_Map.sRocketFuels.mRecipeList;
        if (tFluids.size() > 0 && tRecipeList != null) {
            for (final FluidStack hatchFluid1 : tFluids) {
                for (final GT_Recipe aFuel : tRecipeList) {
                    final FluidStack tLiquid;
                    if ((tLiquid = GT_Utility.getFluidForFilledItem(aFuel.getRepresentativeInput(0), true)) != null && hatchFluid1.isFluidEqual(tLiquid)) {
                        final FluidStack fluidStack = tLiquid;
                        final int n = (int) (this.boostEu ? ((GT_Values.V[5]*2) / aFuel.mSpecialValue) : (GT_Values.V[5] / aFuel.mSpecialValue));
                        fluidStack.amount = n;
                        this.fuelConsumption = n;
                        if (!this.depleteInput(tLiquid)) {
                            continue;
                        }
                        this.boostEu = this.depleteInput(FluidUtils.getFluidStack(RocketFuels.Liquid_Hydrogen, 2));
                        if (tFluids.contains(MISC_MATERIALS.CARBON_DIOXIDE.getFluid(this.boostEu ? 2 : 1))) {
                            if (this.mRuntime % 72 == 0 || this.mRuntime == 0) {
                                this.depleteInput(MISC_MATERIALS.CARBON_DIOXIDE.getFluid(this.boostEu ? 2 : 1));
                            }
                            this.fuelValue = aFuel.mSpecialValue;
                            this.fuelRemaining = hatchFluid1.amount;
                            this.mEUt = (int) ((this.mEfficiency < 2000) ? 0 : GT_Values.V[5]);
                            this.mProgresstime = 1;
                            this.mMaxProgresstime = 1;
                            this.mEfficiencyIncrease = 5;
                            return true;
                        }
                        return false;
                    }
                }
            }
        }
        this.mEUt = 0;
        this.mEfficiency = 0;
        return false;
    }
    
    @Override
    public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
        byte tSide = getBaseMetaTileEntity().getBackFacing();
        int tX = getBaseMetaTileEntity().getXCoord();
        int tY = getBaseMetaTileEntity().getYCoord();
        int tZ = getBaseMetaTileEntity().getZCoord();

        if(getBaseMetaTileEntity().getBlockAtSideAndDistance(tSide, 1) != getGearboxBlock() && getBaseMetaTileEntity().getBlockAtSideAndDistance(tSide, 2) != getGearboxBlock()) {
            return false;
        }
        if(getBaseMetaTileEntity().getMetaIDAtSideAndDistance(tSide, 1) != getGearboxMeta() && getBaseMetaTileEntity().getMetaIDAtSideAndDistance(tSide, 2) != getGearboxMeta()) {
            return false;
        }
        for (byte i = -1; i < 2; i = (byte) (i + 1)) {
            for (byte j = -1; j < 2; j = (byte) (j + 1)) {
                if ((i != 0) || (j != 0)) {
                    for (byte k = 0; k < 4; k = (byte) (k + 1)) {

                        final int fX = tX - (tSide == 5 ? 1 : tSide == 4 ? -1 : i),
                                  fZ = tZ - (tSide == 2 ? -1 : tSide == 3 ? 1 : i),
                                  aY = tY + j,
                                  aX = tX + (tSide == 5 ? k : tSide == 4 ? -k : i),
                                  aZ = tZ + (tSide == 2 ? -k : tSide == 3 ? k : i);

                        final Block frontAir = getBaseMetaTileEntity().getBlock(fX, aY, fZ);
                        final String frontAirName = frontAir.getUnlocalizedName();
                        if(!(getBaseMetaTileEntity().getAir(fX, aY, fZ) || frontAirName.equalsIgnoreCase("tile.air") || frontAirName.equalsIgnoreCase("tile.railcraft.residual.heat"))) {
                            return false; //Fail if vent blocks are obstructed
                        }

                        if (((i == 0) || (j == 0)) && ((k == 1) || (k == 2))) {
                            if (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCasingMeta()) {
                                // Do nothing
                            } else if (!addMufflerToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? 2 : tSide == 4 ? -2 : 0), tY + 1, tZ + (tSide == 3 ? 2 : tSide == 2 ? -2 : 0)), getCasingTextureIndex())) {
                                return false; //Fail if no muffler top middle back
                            } else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ), getCasingTextureIndex())) {
                                return false;
                            }
                        } else if (k == 0) {
                          if(!(getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getIntakeBlock() && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getIntakeMeta())) {
                              return false;
                          }
                        } else if (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCasingMeta()) {
                            // Do nothing
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        this.mDynamoHatches.clear();
        IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 3);
        if ((tTileEntity != null) && (tTileEntity.getMetaTileEntity() != null)) {
            if ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Dynamo)) {
                this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) tTileEntity.getMetaTileEntity());
                ((GT_MetaTileEntity_Hatch) tTileEntity.getMetaTileEntity()).updateTexture(getCasingTextureIndex());
            } else {
                return false;
            }
        }
        return true;
    }
    
    public Block getCasingBlock() {
        return ModBlocks.blockCasings4Misc;
    }
    
    public byte getCasingMeta() {
        return 11;
    }
    
    public Block getIntakeBlock() {
        return GregTech_API.sBlockCasings4;
    }
    
    public byte getIntakeMeta() {
        return 12;
    }
    
    public Block getGearboxBlock() {
        return ModBlocks.blockCasings3Misc;
    }
    
    public byte getGearboxMeta() {
        return 1;
    }
    
    public byte getCasingTextureIndex() {
        return CASING_ID;
    }
    
    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_LargeRocketEngine(this.mName);
    }
    
    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }
    
    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }
    
    @Override
    public int getDamageToComponent(final ItemStack aStack) {
        return 1;
    }
    
    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return this.boostEu ? 30000 : 10000;
    }
    
    @Override
    public int getPollutionPerTick(final ItemStack aStack) {
        return this.boostEu ? 150 : 75;
    }
    
    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return true;
    }
    
    @Override
    public String[] getExtraInfoData() {
        return new String[] { 
        		"Rocket Engine",
        		"Current Pollution: " + getPollutionPerTick(null),
        		"Current Output: " + this.mEUt * this.mEfficiency / 10000 + " EU/t",
        		"Fuel Consumption: " + this.fuelConsumption + "L/t",
        		"Fuel Value: " + this.fuelValue + " EU/L",
        		"Fuel Remaining: " + this.fuelRemaining + " Litres",
        		"Current Efficiency: " + this.mEfficiency / 100 + "%", 
        		(this.getIdealStatus() == this.getRepairStatus()) ? "No Maintainance issues" : "Needs Maintainance" };
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
		return "Rocket Engine";
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
