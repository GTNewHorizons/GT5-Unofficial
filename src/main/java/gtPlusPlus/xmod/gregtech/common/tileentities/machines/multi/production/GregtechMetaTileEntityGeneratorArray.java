package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Dynamo;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase.GTPPHatchElement.TTDynamo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntityGeneratorArray
        extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntityGeneratorArray> implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntityGeneratorArray> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntityGeneratorArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntityGeneratorArray(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntityGeneratorArray(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Processing Array";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Generator Array")
                .addInfo("Runs supplied generators as if placed in the world")
                .addInfo("Place up to 16 Single Block GT Generators into the Controller").addSeparator()
                .beginStructureBlock(3, 3, 3, true).addController("Front center")
                .addCasingInfo("Robust Tungstensteel Machine Casings", 10).addInputBus("Any casing", 1)
                .addOutputBus("Any casing", 1).addInputHatch("Any Casing", 1).addOutputHatch("Any Casing", 1)
                .addDynamoHatch("Any casing", 1).addMaintenanceHatch("Any casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    @Override
    protected int getCasingTextureId() {
        return 48;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        this.mMode = getModeFromInventorySlot(this.getGUIItemStack());
        if (this.mMode == MODE_NONE) {
            return null;
        } else if (this.mMode == MODE_STEAM) {
            return GTPP_Recipe.GTPP_Recipe_Map.sSteamTurbineFuels;
        } else if (this.mMode == MODE_DIESEL) {
            return GT_Recipe.GT_Recipe_Map.sDieselFuels;
        } else if (this.mMode == MODE_GAS) {
            return GT_Recipe.GT_Recipe_Map.sTurbineFuels;
        } else if (this.mMode == MODE_SEMIFLUID) {
            return GTPP_Recipe.GTPP_Recipe_Map.sSemiFluidLiquidFuels;
        } else if (this.mMode == MODE_GEOTHERMAL) {
            return GTPP_Recipe.GTPP_Recipe_Map.sThermalFuels;
        } else if (this.mMode == MODE_ROCKETFUEL) {
            return GTPP_Recipe.GTPP_Recipe_Map.sRocketFuels;
        } else if (this.mMode == MODE_MAGIC_A) {
            return GT_Recipe.GT_Recipe_Map.sMagicFuels;
        } else if (this.mMode == MODE_PLASMA) {
            return GT_Recipe.GT_Recipe_Map.sPlasmaFuels;
        } else if (this.mMode == MODE_NAQUADAH) {
            return GT_Recipe.GT_Recipe_Map.sFluidNaquadahReactorFuels;
        }
        return null;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        if (aStack != null && aStack.getUnlocalizedName().startsWith("gt.blockmachines.")) {
            return true;
        }
        return false;
    }

    protected int fuelConsumption = 0;
    protected int fuelValue = 0;
    protected int fuelRemaining = 0;
    protected boolean boostEu = false;

    int mMode = 0;

    private static final int MODE_STEAM = 0;
    private static final int MODE_DIESEL = 1;
    private static final int MODE_GAS = 2;
    private static final int MODE_SEMIFLUID = 3;
    private static final int MODE_GEOTHERMAL = 4;
    private static final int MODE_ROCKETFUEL = 5;
    private static final int MODE_MAGIC_A = 6;
    private static final int MODE_MAGIC_B_DISABLED = 7;
    private static final int MODE_PLASMA = 8;
    private static final int MODE_NAQUADAH = 9;
    private static final int MODE_NONE = 100;

    private static final int[] ID_STEAM = new int[] { 962, 1120, 1121, 1122 };
    private static final int[] ID_GAS = new int[] { 961, 1115, 1116, 1117 };
    private static final int[] ID_DIESEL = new int[] { 960, 1110, 1111, 1112 };
    private static final int[] ID_SEMIFLUID = new int[] { 837, 838, 839 };
    private static final int[] ID_GEOTHERMAL = new int[] { 830, 831, 832 };
    private static final int[] ID_ROCKETFUEL = new int[] { 793, 794, 795 };
    private static final int[] ID_MAGIC_A = new int[] { 1123, 1124, 1125 };
    private static final int[] ID_MAGIC_B_DISABLED = new int[] { 1127, 1128, 1129, 1130 };
    private static final int[] ID_PLASMA = new int[] { 1196, 1197, 1198 };
    private static final int[] ID_NAQUADAH = new int[] { 1190, 1191, 1192 };

    private static final int getModeFromInventorySlot(ItemStack aStack) {

        if (aStack == null) {
            return MODE_NONE;
        }

        String aItemStackName = aStack == null ? "" : aStack.getUnlocalizedName();
        // Logger.INFO("Item Name: "+aItemStackName+" ("+aStack.getItemDamage()+")");
        if (aItemStackName.toLowerCase().contains("gt.blockmachines")) {
            for (int g : ID_STEAM) {
                if (aStack.getItemDamage() == g) {
                    return MODE_STEAM;
                }
            }
            for (int g : ID_GAS) {
                if (aStack.getItemDamage() == g) {
                    return MODE_GAS;
                }
            }
            for (int g : ID_DIESEL) {
                if (aStack.getItemDamage() == g) {
                    return MODE_DIESEL;
                }
            }
            for (int g : ID_SEMIFLUID) {
                if (aStack.getItemDamage() == g) {
                    return MODE_SEMIFLUID;
                }
            }
            for (int g : ID_GEOTHERMAL) {
                if (aStack.getItemDamage() == g) {
                    return MODE_GEOTHERMAL;
                }
            }
            for (int g : ID_ROCKETFUEL) {
                if (aStack.getItemDamage() == g) {
                    return MODE_ROCKETFUEL;
                }
            }
            for (int g : ID_MAGIC_A) {
                if (aStack.getItemDamage() == g) {
                    return MODE_MAGIC_A;
                }
            }
            for (int g : ID_PLASMA) {
                if (aStack.getItemDamage() == g) {
                    return MODE_PLASMA;
                }
            }
            for (int g : ID_NAQUADAH) {
                if (aStack.getItemDamage() == g) {
                    return MODE_NAQUADAH;
                }
            }
        }
        return MODE_NONE;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        return false;

        /**
         * this.resetRecipeMapForAllInputHatches(); this.mMode = getModeFromInventorySlot(aStack); if (mMode ==
         * MODE_NONE) { Logger.INFO("Did not find valid generator."); return false; } else { Logger.INFO("Changed Mode
         * to "+mMode); } int aMulti = this.getGUIItemStack() != null ? this.getGUIItemStack().stackSize : 0; if (aMulti
         * > 16 || aMulti == 0) { return false; }
         *
         *
         *
         * ArrayList<FluidStack> tFluids = this.getStoredFluids();
         *
         * Collection<GT_Recipe> tRecipeList = this.getRecipeMap().mRecipeList; Logger.INFO("Got Recipe Map"); if
         * (tFluids.size() > 0 && tRecipeList != null) { Logger.INFO("Found Fuels for Map."); Iterator<FluidStack> arg3
         * = tFluids.iterator(); int aCount = 0; while (arg3.hasNext()) { FluidStack hatchFluid1 = (FluidStack)
         * arg3.next(); Logger.INFO("Iterating Fluid Found "+(aCount++)+" | "+hatchFluid1.getLocalizedName());
         * Iterator<GT_Recipe> arg5 = tRecipeList.iterator();
         *
         * int Hatch = 0; int totalFuelValue = 0; while (arg5.hasNext()) { Logger.INFO("Iterating Recipe "+(Hatch++));
         * GT_Recipe aFuel = (GT_Recipe) arg5.next(); FluidStack tLiquid; boolean addedFuelOnce = false; for (int a = 0;
         * a < aMulti; a++) { if ((tLiquid = GT_Utility.getFluidForFilledItem(aFuel.getRepresentativeInput(0), true)) !=
         * null && hatchFluid1.isFluidEqual(tLiquid)) { this.fuelConsumption = tLiquid.amount = this.boostEu ? 4096 /
         * aFuel.mSpecialValue : 2048 / aFuel.mSpecialValue; if (this.depleteInput(tLiquid)) { Logger.INFO("Depleted
         * Fuel"); this.boostEu = this.depleteInput(Materials.Oxygen.getGas(2L));
         *
         * /*if (!tFluids.contains(Materials.Lubricant.getFluid(1L))) { Logger.INFO("No Lube."); return false; }
         */
        /**
         * if (this.mRuntime % 72 == 0 || this.mRuntime == 0) {
         * this.depleteInput(Materials.Lubricant.getFluid(this.boostEu ? 2L : 1L)); } Logger.INFO("ADDING POWER");
         * this.fuelRemaining = hatchFluid1.amount; totalFuelValue++; } } } //Do things after consuming Fuel if
         * (totalFuelValue == aMulti) { this.fuelValue = aFuel.mSpecialValue*aMulti; this.mEUt = this.mEfficiency < 2000
         * ? 0 : aFuel.mSpecialValue*aMulti; this.mProgresstime = aMulti; this.mMaxProgresstime = aMulti;
         * this.mEfficiencyIncrease = 15*aMulti; return true; } } } }
         *
         * this.mEUt = 0; this.mEfficiency = 0; return false;
         **/
    }

    public static ItemStack[] clean(final ItemStack[] v) {
        List<ItemStack> list = new ArrayList<ItemStack>(Arrays.asList(v));
        list.removeAll(Collections.singleton(null));
        return list.toArray(new ItemStack[list.size()]);
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntityGeneratorArray> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntityGeneratorArray>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntityGeneratorArray.class).atLeast(
                                    InputHatch,
                                    OutputHatch,
                                    InputBus,
                                    OutputBus,
                                    Maintenance,
                                    Dynamo.or(TTDynamo),
                                    Muffler).casingIndex(48).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(GregTech_API.sBlockCasings4, 0))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 10;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
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
    public int getAmountOfOutputs() {
        return 1;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 0;
    }

    /**
     * Code from single blocks
     */
    public void tryProcessFuelItems(IGregTechTileEntity aBaseMetaTileEntity, ItemStack a) {
        if (a != null
                && aBaseMetaTileEntity.getUniversalEnergyStored() < this.maxEUOutput() * 20L + this.getMinimumStoredEU()
                && GT_Utility.getFluidForFilledItem(a, true) == null) {
            int tFuelValue;
            tFuelValue = this.getFuelValue(a);
            if (tFuelValue > 0) {
                ItemStack tEmptyContainer1 = this.getEmptyContainer(a);
                if (this.addOutput(tEmptyContainer1)) {
                    aBaseMetaTileEntity.increaseStoredEnergyUnits((long) tFuelValue, true);
                    this.depleteInput(a);
                    PollutionUtils.addPollution(this.getBaseMetaTileEntity(), 10 * this.getPollutionPerTick(null));
                }
            }
        }
    }

    public void tryProcessFuel(IGregTechTileEntity aBaseMetaTileEntity, long aTick, FluidStack mFluid) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && aTick % 10L == 0L) {
            int tFuelValue;
            if (mFluid != null) {
                tFuelValue = this.getFuelValue(mFluid);
                int tEmptyContainer = this.consumedFluidPerOperation(mFluid);
                if (tFuelValue > 0 && tEmptyContainer > 0 && mFluid.amount > tEmptyContainer) {
                    long tFluidAmountToUse = Math.min(
                            (long) (mFluid.amount / tEmptyContainer),
                            (this.maxEUStore() - aBaseMetaTileEntity.getUniversalEnergyStored()) / (long) tFuelValue);
                    if (tFluidAmountToUse > 0L && aBaseMetaTileEntity
                            .increaseStoredEnergyUnits(tFluidAmountToUse * (long) tFuelValue, true)) {
                        PollutionUtils.addPollution(this.getBaseMetaTileEntity(), 10 * this.getPollutionPerTick(null));
                        mFluid.amount = (int) ((long) mFluid.amount - tFluidAmountToUse * (long) tEmptyContainer);
                    }
                }
            }

            // Check items
        }

        if (aBaseMetaTileEntity.isServerSide()) {
            aBaseMetaTileEntity.setActive(
                    aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.getUniversalEnergyStored()
                            >= this.maxEUOutput() + this.getMinimumStoredEU());
        }
    }

    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return this.getFuelValue(aFluid) > 0;
    }

    public int consumedFluidPerOperation(FluidStack aLiquid) {
        return 1;
    }

    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid != null && this.getRecipeMap() != null) {
            Collection<GT_Recipe> tRecipeList = this.getRecipeMap().mRecipeList;
            if (tRecipeList != null) {
                Iterator<GT_Recipe> arg3 = tRecipeList.iterator();

                while (arg3.hasNext()) {
                    GT_Recipe tFuel = (GT_Recipe) arg3.next();
                    FluidStack tLiquid;
                    if ((tLiquid = GT_Utility.getFluidForFilledItem(tFuel.getRepresentativeInput(0), true)) != null
                            && aLiquid.isFluidEqual(tLiquid)) {
                        return (int) ((long) tFuel.mSpecialValue * (long) this.mEfficiency
                                * (long) this.consumedFluidPerOperation(tLiquid)
                                / 100L);
                    }
                }
            }

            return 0;
        } else {
            return 0;
        }
    }

    public int getFuelValue(ItemStack aStack) {
        if (!GT_Utility.isStackInvalid(aStack) && this.getRecipeMap() != null) {
            GT_Recipe tFuel = this.getRecipeMap().findRecipe(
                    this.getBaseMetaTileEntity(),
                    false,
                    Long.MAX_VALUE,
                    (FluidStack[]) null,
                    new ItemStack[] { aStack });
            return tFuel != null ? (int) ((long) tFuel.mSpecialValue * 1000L * (long) this.mEfficiency / 100L) : 0;
        } else {
            return 0;
        }
    }

    public ItemStack getEmptyContainer(ItemStack aStack) {
        if (!GT_Utility.isStackInvalid(aStack) && this.getRecipeMap() != null) {
            GT_Recipe tFuel = this.getRecipeMap().findRecipe(
                    this.getBaseMetaTileEntity(),
                    false,
                    Long.MAX_VALUE,
                    (FluidStack[]) null,
                    new ItemStack[] { aStack });
            return tFuel != null ? GT_Utility.copy(new Object[] { tFuel.getOutput(0) })
                    : GT_Utility.getContainerItem(aStack, true);
        } else {
            return null;
        }
    }
}
