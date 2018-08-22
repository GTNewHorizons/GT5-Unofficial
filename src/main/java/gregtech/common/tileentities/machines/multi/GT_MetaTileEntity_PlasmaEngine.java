package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;

public class GT_MetaTileEntity_PlasmaEngine extends GT_MetaTileEntity_MultiBlockBase {
    protected int plasmaConsumption = 0;
    protected int plasmaValue = 0;
    protected int plasmaRemaining = 0;
    protected boolean boostEu = false;

    public GT_MetaTileEntity_PlasmaEngine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PlasmaEngine(String aName) {
        super(aName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Large Plasma Engine",
                "Size(WxHxD): 3x3x4, Controller (front centered)",
                "3x3x4 of Radiation Proof Machine Casing (hollow, Min 16!)",
                "2x Core Chamber Casing inside the Hollow Casing",
                "8x Intermix Chamber Casing (around controller)",
                "2x Input Hatch (Plasma/Helium) (one of the Casings next to a Gear Box)",
                "1x Maintenance Hatch (one of the Casings next to a Gear Box)",
                "1x Muffler Hatch (top middle back, next to the rear Gear Box)",
                "1x Dynamo Hatch (back centered)",
                "Intermix Chamber Casings must not be obstructed in front (only air blocks)",
                "Supply Plasma and 1000L of Helium per hour to run.",
                "Supply 40L of Hydrogen per second to boost output (optional).",
                "Default: Produces 524288EU/t at 100% efficiency",
                "Boosted: Produces 1572846EU/t at 150% efficiency",
                "Causes " + 20 * getPollutionPerTick(null) + " Pollution per second"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[44], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[44]};
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
        Collection<GT_Recipe> tRecipeList = GT_Recipe.GT_Recipe_Map.sPlasmaFuels.mRecipeList;

        if(tFluids.size() > 0 && tRecipeList != null) { //Does input hatch have a plasma fuel?
            for (FluidStack hatchFluid1 : tFluids) { //Loops through hatches
                for(GT_Recipe aFuel : tRecipeList) { //Loops through plasma fuel recipes
                    FluidStack tLiquid;
                    if ((tLiquid = GT_Utility.getFluidForFilledItem(aFuel.getRepresentativeInput(0), true)) != null) { //Create fluidstack from current recipe
                        if (hatchFluid1.isFluidEqual(tLiquid)) { //Has a plasma fluid
                            plasmaConsumption = tLiquid.amount = boostEu ? (1048576 / aFuel.mSpecialValue) : (524288 / aFuel.mSpecialValue); //Calc fuel consumption
                            if(depleteInput(tLiquid)) { //Deplete that amount
                                boostEu = depleteInput(Materials.Hydrogen.getGas(4L));

                                if(tFluids.contains(Materials.Helium.getFluid(1L))) { //Has Helium?
                                    //Deplete Helium. 1000L should = 1 hour of runtime (if baseEU = 524288)
                                    if(mRuntime % 72 == 0 || mRuntime == 0) depleteInput(Materials.Helium.getFluid(boostEu ? 2 : 1));
                                } else return false;

                                plasmaValue = aFuel.mSpecialValue;
                                plasmaRemaining = hatchFluid1.amount; //Record available plasma
                                this.mEUt = mEfficiency < 512000 ? 0 : 524288; //Output 0 if startup is less than 20%
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
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
                        Block frontAir = getBaseMetaTileEntity().getBlock(tX - (tSide == 5 ? 1 : tSide == 4 ? -1 : i), tY + j, tZ - (tSide == 2 ? -1 : tSide == 3 ? 1 : i));
                        if(!(frontAir.getUnlocalizedName().equalsIgnoreCase("tile.air") || frontAir.getUnlocalizedName().equalsIgnoreCase("tile.railcraft.residual.heat"))) {
                            return false; //Fail if vent blocks are obstructed
                        }
                        if (((i == 0) || (j == 0)) && ((k == 1) || (k == 2))) {
                            if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingMeta()) {
                            } else if (!addMufflerToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? 2 : tSide == 4 ? -2 : 0), tY + 1, tZ + (tSide == 3 ? 2 : tSide == 2 ? -2 : 0)), getCasingTextureIndex())) {
                                return false; //Fail if no muffler top middle back
                            } else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)))) {
                                return false;
                            }
                        } else if (k == 0) {
                          if(!(getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getIntakeBlock() && getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getIntakeMeta())) {
                              return false;
                          }
                        } else if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingMeta()) {
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
        return GregTech_API.sBlockCasings3;
    }

    public byte getCasingMeta() {
        return 12;
    }

    public Block getIntakeBlock() {
        return GregTech_API.sBlockCasings5;
    }

    public byte getIntakeMeta() {
        return 8;
    }

    public Block getGearboxBlock() {
        return GregTech_API.sBlockCasings5;
    }

    public byte getGearboxMeta() {
        return 15;
    }

    public byte getCasingTextureIndex() {
        return 44;
    }

    private boolean addToMachineList(IGregTechTileEntity tTileEntity) {
        return ((addMaintenanceToMachineList(tTileEntity, getCasingTextureIndex())) || (addInputToMachineList(tTileEntity, getCasingTextureIndex())) || (addOutputToMachineList(tTileEntity, getCasingTextureIndex())) || (addMufflerToMachineList(tTileEntity, getCasingTextureIndex())));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PlasmaEngine(this.mName);
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
        return boostEu ? 40000 : 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 80;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{
                "Plasma Engine",
                "Current Output: " + mEUt * mEfficiency / 10000 + " EU/t",
                "Plasma Consumption: " + plasmaConsumption + "L/t",
                "Plasma Value: " + plasmaValue + " EU/L",
                "Plasma Remaining: " + plasmaRemaining + " Litres",
                "Current Efficiency: " + (mEfficiency / 100) + "%",
                getIdealStatus() == getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance"};
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }
}
