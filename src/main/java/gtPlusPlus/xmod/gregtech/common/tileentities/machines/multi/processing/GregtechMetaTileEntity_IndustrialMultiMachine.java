package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialMultiMachine extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialMultiMachine> implements ISurvivalConstructable {

    protected int mInternalMode = 0;
    protected GT_Recipe[] mLastRecipeExtended = new GT_Recipe[9];
    private static final int MODE_COMPRESSOR = 0;
    private static final int MODE_LATHE = 1;
    private static final int MODE_MAGNETIC = 2;
    private static final int MODE_FERMENTER = 3;
    private static final int MODE_FLUIDEXTRACT = 4;
    private static final int MODE_EXTRACTOR = 5;
    private static final int MODE_LASER = 6;
    private static final int MODE_AUTOCLAVE = 7;
    private static final int MODE_FLUIDSOLIDIFY = 8;
    private static final int[][] MODE_MAP = new int[][] { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 } };
    public static final String[] aToolTipNames = new String[9];
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialMultiMachine> STRUCTURE_DEFINITION = null;

    static {
        for (int id = 0; id < 9; id++) {
            GT_Recipe.GT_Recipe_Map recipeMap = getRecipeMap(id);
            if (recipeMap != null) {
                String aNEI = GT_LanguageManager.getTranslation(getRecipeMap(id).mNEIName);
                aToolTipNames[id] = aNEI != null ? aNEI : "BAD NEI NAME (Report to Github)";
            }
        }
    }

    public GregtechMetaTileEntity_IndustrialMultiMachine(final int aID, final String aName,
            final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialMultiMachine(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialMultiMachine(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Nine in One";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        String[] aBuiltStrings = new String[3];
        aBuiltStrings[0] = aToolTipNames[0] + ", " + aToolTipNames[1] + ", " + aToolTipNames[2];
        aBuiltStrings[1] = aToolTipNames[3] + ", " + aToolTipNames[4] + ", " + aToolTipNames[5];
        aBuiltStrings[2] = aToolTipNames[6] + ", " + aToolTipNames[7] + ", " + aToolTipNames[8];

        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Industrial Multi-Machine")
                .addInfo("250% faster than using single block machines of the same voltage")
                .addInfo("Only uses 80% of the EU/t normally required").addInfo("Processes two items per voltage tier")
                .addInfo(
                        "Machine Type: [A] - " + EnumChatFormatting.YELLOW
                                + aBuiltStrings[0]
                                + EnumChatFormatting.RESET)
                .addInfo(
                        "Machine Type: [B] - " + EnumChatFormatting.YELLOW
                                + aBuiltStrings[1]
                                + EnumChatFormatting.RESET)
                .addInfo(
                        "Machine Type: [C] - " + EnumChatFormatting.YELLOW
                                + aBuiltStrings[2]
                                + EnumChatFormatting.RESET)
                .addInfo("Read Multi-Machine Manual for extra information")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 3, 3, true)
                .addController("Front Center").addCasingInfoMin("Multi-Use Casings", 6, false)
                .addInputBus("Any Casing", 1).addOutputBus("Any Casing", 1).addInputHatch("Any Casing", 1)
                .addOutputHatch("Any Casing", 1).addEnergyHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1)
                .addMufflerHatch("Any Casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialMultiMachine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialMultiMachine>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialMultiMachine.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                                    .casingIndex(getTextureIndex()).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 2))))
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
        return checkPiece(mName, 1, 1, 0) && mCasing >= 6 && checkHatch();
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
        return getTextureIndex();
    }

    @Override
    public boolean checkRecipe(final ItemStack aStack) {
        ArrayList<FluidStack> tFluids = getStoredFluids();
        // Logger.MACHINE_INFO("1");
        for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
            ArrayList<ItemStack> tBusItems = new ArrayList<ItemStack>();
            tBus.mRecipeMap = getRecipeMap();
            // Logger.MACHINE_INFO("2");
            if (isValidMetaTileEntity(tBus)) {
                // Logger.MACHINE_INFO("3");
                for (int i = tBus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                    if (tBus.getBaseMetaTileEntity().getStackInSlot(i) != null)
                        tBusItems.add(tBus.getBaseMetaTileEntity().getStackInSlot(i));
                }
            }

            boolean aFoundCircuitInBus = false;
            for (ItemStack aBusItem : tBusItems) {
                if (ItemUtils.isControlCircuit(aBusItem)) {
                    aFoundCircuitInBus = true;
                }
            }
            if (!aFoundCircuitInBus) {
                continue;
            }

            Object[] tempArray = tFluids.toArray(new FluidStack[] {});
            FluidStack[] properArray;
            properArray = ((tempArray != null && tempArray.length > 0) ? (FluidStack[]) tempArray
                    : new FluidStack[] {});

            // Logger.MACHINE_INFO("4");
            if (checkRecipeGeneric(
                    tBusItems.toArray(new ItemStack[] {}),
                    properArray,
                    (2 * GT_Utility.getTier(this.getMaxInputVoltage())),
                    80,
                    250,
                    10000))
                return true;
        }
        return false;

        // return checkRecipeGeneric(2*GT_Utility.getTier(this.getMaxInputVoltage()), 90, 180);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (2 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 80;
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        if (mInternalMode == 0) {
            return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal;
        } else if (mInternalMode == 1) {
            return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid;
        } else { // config 2
            return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc;
        }
    }

    public int getTextureIndex() {
        return TAE.getIndexFromPage(2, 2);
    }

    @Override
    public int getAmountOfOutputs() {
        return 1;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    private ItemStack getCircuit(ItemStack[] t) {
        for (ItemStack j : t) {
            if (j.getItem() == CI.getNumberedCircuit(0).getItem()) {
                if (j.getItemDamage() >= 20 && j.getItemDamage() <= 22) {
                    return j;
                }
            }
        }
        return null;
    }

    private final int getCircuitID(ItemStack circuit) {
        int H = circuit.getItemDamage();
        int T = (H == 20 ? 0 : (H == 21 ? 1 : (H == 22 ? 2 : -1)));
        return MODE_MAP[this.mInternalMode][T];
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }

    private final GT_Recipe.GT_Recipe_Map getRecipeMap(ItemStack circuit) {
        return getRecipeMap(getCircuitID(circuit));
    }

    private static final GT_Recipe.GT_Recipe_Map getRecipeMap(int aMode) {
        if (aMode == MODE_COMPRESSOR) {
            return GT_Recipe.GT_Recipe_Map.sCompressorRecipes;
        } else if (aMode == MODE_LATHE) {
            return GT_Recipe.GT_Recipe_Map.sLatheRecipes;
        } else if (aMode == MODE_MAGNETIC) {
            return GT_Recipe.GT_Recipe_Map.sPolarizerRecipes;
        } else if (aMode == MODE_FERMENTER) {
            return GT_Recipe.GT_Recipe_Map.sFermentingRecipes;
        } else if (aMode == MODE_FLUIDEXTRACT) {
            return GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes;
        } else if (aMode == MODE_EXTRACTOR) {
            return GT_Recipe.GT_Recipe_Map.sExtractorRecipes;
        } else if (aMode == MODE_LASER) {
            return GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes;
        } else if (aMode == MODE_AUTOCLAVE) {
            return GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes;
        } else if (aMode == MODE_FLUIDSOLIDIFY) {
            return GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes;
        } else {
            return null;
        }
    }

    @Override
    public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes,
            long aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll) {

        // Based on the Processing Array. A bit overkill, but very flexible.

        // Get Circuit info for this recipe.
        ItemStack tCircuit = getCircuit(aItemInputs);
        if (tCircuit == null) {
            return false;
        }
        int tCircuitID = getCircuitID(tCircuit);

        Logger.MACHINE_INFO("Mode: " + tCircuitID);

        // Reset outputs and progress stats
        this.lEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = new ItemStack[] {};
        this.mOutputFluids = new FluidStack[] {};

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        long tEnergy = getMaxInputEnergy();

        GT_Recipe.GT_Recipe_Map tRecipeMap = this.getRecipeMap(tCircuit);
        if (tRecipeMap == null) return false;
        GT_Recipe tRecipe = tRecipeMap.findRecipe(
                getBaseMetaTileEntity(),
                this.mLastRecipeExtended[tCircuitID],
                false,
                gregtech.api.enums.GT_Values.V[tTier],
                aFluidInputs,
                aItemInputs);

        // Remember last recipe - an optimization for findRecipe()
        // this.mLastRecipe = tRecipe; //Let's not do this, it's bad.
        // Instead, how about I use a array for types?
        this.mLastRecipeExtended[tCircuitID] = tRecipe;

        if (tRecipe == null) {
            return false;
        }

        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(tRecipe).setItemInputs(aItemInputs)
                .setFluidInputs(aFluidInputs).setAvailableEUt(tEnergy).setMaxParallel(aMaxParallelRecipes)
                .enableConsumption().enableOutputCalculation().setEUtModifier(aEUPercent / 100.0f);
        if (!voidExcess) {
            helper.enableVoidProtection(this);
        }

        if (batchMode) {
            helper.enableBatchMode(128);
        }

        helper.build();

        if (helper.getCurrentParallel() == 0) {
            return false;
        }

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(tRecipe.mEUt).setEUt(tEnergy)
                .setDuration(tRecipe.mDuration).setEUtDiscount(aEUPercent / 100.0f)
                .setSpeedBoost(100.0f / (100.0f + aSpeedBonusPercent))
                .setParallel((int) Math.floor(helper.getCurrentParallel() / helper.getDurationMultiplier()))
                .calculate();
        lEUt = -calculator.getConsumption();
        mMaxProgresstime = (int) Math.ceil(calculator.getDuration() * helper.getDurationMultiplier());

        mOutputItems = helper.getItemOutputs();
        mOutputFluids = helper.getFluidOutputs();
        updateSlots();

        // Play sounds (GT++ addition - GT multiblocks play no sounds)
        startProcess();
        return true;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (mInternalMode < 2) {
            mInternalMode++;
        } else {
            mInternalMode = 0;
        }
        String mModeString = (mInternalMode == 0 ? "Metal"
                : mInternalMode == 1 ? "Fluid" : mInternalMode == 2 ? "Misc." : "null");
        PlayerUtils.messagePlayer(aPlayer, "Multi-Machine is now in " + mModeString + " mode.");
        mLastRecipe = null;
    }

    @Override
    public String[] getInfoData() {
        String[] data = super.getInfoData();
        ArrayList<String> mInfo = new ArrayList<>(Arrays.asList(data));
        String mode;
        if (mInternalMode == 0) {
            mode = StatCollector.translateToLocal("GTPP.multiblock.multimachine.metal");
        } else if (mInternalMode == 1) {
            mode = StatCollector.translateToLocal("GTPP.multiblock.multimachine.fluid");
        } else {
            mode = StatCollector.translateToLocal("GTPP.multiblock.multimachine.misc");
        }
        mInfo.add(mode);
        return mInfo.toArray(new String[mInfo.size()]);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mInternalMode", mInternalMode);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mInternalMode = aNBT.getInteger("mInternalMode");
        super.loadNBTData(aNBT);
    }

    @Override
    protected boolean isInputSeparationEnabled() {
        return true;
    }
}
