package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_MultiFurnace
    extends GT_MetaTileEntity_AbstractMultiFurnace<GT_MetaTileEntity_MultiFurnace> implements ISurvivalConstructable {

    private int mLevel = 0;
    private int mCostDiscount = 1;

    private static final long RECIPE_EUT = 4;
    private static final int RECIPE_DURATION = 512;
    private static final int CASING_INDEX = 11;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_MultiFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_MultiFurnace>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "ccc", "cmc", "ccc" }, { "CCC", "C-C", "CCC" }, { "b~b", "bbb", "bbb" } }))
        .addElement('c', ofBlock(GregTech_API.sBlockCasings1, CASING_INDEX))
        .addElement('m', Muffler.newAny(CASING_INDEX, 2))
        .addElement(
            'C',
            GT_StructureUtility
                .ofCoil(GT_MetaTileEntity_MultiFurnace::setCoilLevel, GT_MetaTileEntity_MultiFurnace::getCoilLevel))
        .addElement(
            'b',
            ofChain(
                GT_StructureUtility.<GT_MetaTileEntity_MultiFurnace>buildHatchAdder()
                    .atLeast(Maintenance, InputBus, OutputBus, Energy)
                    .casingIndex(CASING_INDEX)
                    .dot(1)
                    .build(),
                ofBlock(GregTech_API.sBlockCasings1, CASING_INDEX)))
        .build();

    public GT_MetaTileEntity_MultiFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MultiFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MultiFurnace(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Furnace")
            .addInfo("Controller Block for the Multi Smelter")
            .addInfo("Smelts up to 8-8192 items at once")
            .addInfo("Items smelted increases with coil tier")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front bottom")
            .addCasingInfoRange("Heat Proof Machine Casing", 8, 14, false)
            .addOtherStructurePart("Heating Coils", "Middle layer")
            .addEnergyHatch("Any bottom casing", 1)
            .addMaintenanceHatch("Any bottom casing", 1)
            .addMufflerHatch("Top Middle", 2)
            .addInputBus("Any bottom casing", 1)
            .addOutputBus("Any bottom casing", 1)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection != facingDirection) return new ITexture[] { casingTexturePages[0][CASING_INDEX] };
        if (active) return new ITexture[] { casingTexturePages[0][CASING_INDEX], TextureFactory.builder()
            .addIcon(OVERLAY_FRONT_MULTI_SMELTER_ACTIVE)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_MULTI_SMELTER_ACTIVE_GLOW)
                .extFacing()
                .glow()
                .build() };
        return new ITexture[] { casingTexturePages[0][CASING_INDEX], TextureFactory.builder()
            .addIcon(OVERLAY_FRONT_MULTI_SMELTER)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_MULTI_SMELTER_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.furnaceRecipes;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GT_Mod.gregtechproxy.mPollutionMultiSmelterPerSecond;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ArrayList<ItemStack> tInputList = getAllStoredInputs();
        if (tInputList.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        int mVoltage = GT_Utility.safeInt(getMaxInputVoltage());

        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setEUt(mVoltage)
            .setRecipeEUt(RECIPE_EUT)
            .setDuration(RECIPE_DURATION);
        int maxParallel = this.mLevel;
        int originalMaxParallel = maxParallel;
        double tickTimeAfterOC = calculator.calculateDurationUnderOneTick();
        if (tickTimeAfterOC < 1) {
            maxParallel = GT_Utility.safeInt((long) (maxParallel / tickTimeAfterOC), 0);
        }

        int maxParallelBeforeBatchMode = maxParallel;
        if (isBatchModeEnabled()) {
            maxParallel = GT_Utility.safeInt((long) maxParallel * getMaxBatchSize(), 0);
        }

        // Calculate parallel
        int tCurrentParallel = 0;
        for (ItemStack item : tInputList) {
            ItemStack smeltedOutput = GT_ModHandler.getSmeltingOutput(item, false, null);
            if (smeltedOutput != null) {
                if (item.stackSize <= (maxParallel - tCurrentParallel)) {
                    tCurrentParallel += item.stackSize;
                } else {
                    tCurrentParallel = maxParallel;
                    break;
                }
            }
        }
        if (tCurrentParallel <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        int currentParallelBeforeBatchMode = Math.min(tCurrentParallel, maxParallelBeforeBatchMode);

        long eutUseAfterOC = calculator.calculateEUtConsumptionUnderOneTick(
            1,
            (int) Math.ceil((double) currentParallelBeforeBatchMode / originalMaxParallel));
        calculator
            .setParallel(Math.min((int) Math.ceil((double) currentParallelBeforeBatchMode / originalMaxParallel), 1))
            .calculate();

        double batchMultiplierMax = 1;
        // In case batch mode enabled
        if (tCurrentParallel > maxParallelBeforeBatchMode && calculator.getDuration() < getMaxBatchSize()) {
            batchMultiplierMax = (double) getMaxBatchSize() / calculator.getDuration();
            batchMultiplierMax = Math.min(batchMultiplierMax, (double) tCurrentParallel / maxParallelBeforeBatchMode);
        }
        int finalParallel = (int) (batchMultiplierMax * maxParallelBeforeBatchMode);

        // Consume inputs and generate outputs
        ArrayList<ItemStack> smeltedOutputs = new ArrayList<>();
        int remainingCost = finalParallel;
        for (ItemStack item : tInputList) {
            ItemStack smeltedOutput = GT_ModHandler.getSmeltingOutput(item, false, null);
            if (smeltedOutput != null) {
                if (remainingCost >= item.stackSize) {
                    remainingCost -= item.stackSize;
                    smeltedOutput.stackSize *= item.stackSize;
                    item.stackSize = 0;
                    smeltedOutputs.add(smeltedOutput);
                } else {
                    smeltedOutput.stackSize *= remainingCost;
                    item.stackSize -= remainingCost;
                    smeltedOutputs.add(smeltedOutput);
                    break;
                }
            }
        }
        this.mOutputItems = smeltedOutputs.toArray(new ItemStack[0]);

        this.mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = (int) (calculator.getDuration() * batchMultiplierMax);
        this.lEUt = eutUseAfterOC;

        if (this.lEUt > 0) this.lEUt = -this.lEUt;

        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_MultiFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mLevel = 0;
        this.mCostDiscount = 1;

        replaceDeprecatedCoils(aBaseMetaTileEntity);

        setCoilLevel(HeatingCoilLevel.None);

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0)) return false;

        if (getCoilLevel() == HeatingCoilLevel.None) return false;

        if (mMaintenanceHatches.size() != 1) return false;

        if (getCoilLevel().getHeat() < 9000) {
            this.mLevel = 8 * getCoilLevel().getLevel();
        } else {
            this.mLevel = 1 << (getCoilLevel().getTier());
        }
        this.mCostDiscount = getCoilLevel().getCostDiscount();
        return true;
    }

    private void replaceDeprecatedCoils(IGregTechTileEntity aBaseMetaTileEntity) {
        final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX;
        final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ;
        final int tX = aBaseMetaTileEntity.getXCoord() + xDir;
        final int tY = aBaseMetaTileEntity.getYCoord();
        final int tZ = aBaseMetaTileEntity.getZCoord() + zDir;
        int tUsedMeta;
        for (int xPos = tX - 1; xPos <= tX + 1; xPos++) for (int zPos = tZ - 1; zPos <= tZ + 1; zPos++) {
            if ((xPos == tX) && (zPos == tZ)) continue;
            tUsedMeta = aBaseMetaTileEntity.getMetaID(xPos, tY + 1, zPos);
            if (tUsedMeta >= 12 && tUsedMeta <= 14
                && aBaseMetaTileEntity.getBlock(xPos, tY + 1, zPos) == GregTech_API.sBlockCasings1)
                aBaseMetaTileEntity.getWorld()
                    .setBlock(xPos, tY + 1, zPos, GregTech_API.sBlockCasings5, tUsedMeta - 12, 3);
        }
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (final GT_MetaTileEntity_Hatch_Muffler tHatch : filterValidMTEs(mMufflerHatches))
            mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (final GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(-lEUt)
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*2A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GT_Utility.getTier(getMaxInputVoltage())]
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            StatCollector.translateToLocal("GT5U.MS.multismelting") + ": "
                + EnumChatFormatting.GREEN
                + mLevel
                + EnumChatFormatting.RESET
                + " Discount: (EU/t) / "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mCostDiscount)
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + mPollutionReduction
                + EnumChatFormatting.RESET
                + " %" };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
}
