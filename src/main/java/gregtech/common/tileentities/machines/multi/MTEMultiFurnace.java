package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTUtility.filterValidMTEs;

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

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;

public class MTEMultiFurnace extends MTEAbstractMultiFurnace<MTEMultiFurnace> implements ISurvivalConstructable {

    private int mLevel = 0;
    private int mCostDiscount = 1;

    private static final long RECIPE_EUT = 4;
    private static final int RECIPE_DURATION = 512;
    private static final int CASING_INDEX = 11;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEMultiFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMultiFurnace>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "ccc", "cmc", "ccc" }, { "CCC", "C-C", "CCC" }, { "b~b", "bbb", "bbb" } }))
        .addElement('c', ofBlock(GregTechAPI.sBlockCasings1, CASING_INDEX))
        .addElement('m', Muffler.newAny(CASING_INDEX, 2))
        .addElement('C', GTStructureUtility.ofCoil(MTEMultiFurnace::setCoilLevel, MTEMultiFurnace::getCoilLevel))
        .addElement(
            'b',
            ofChain(
                GTStructureUtility.<MTEMultiFurnace>buildHatchAdder()
                    .atLeast(Maintenance, InputBus, OutputBus, Energy)
                    .casingIndex(CASING_INDEX)
                    .dot(1)
                    .build(),
                ofBlock(GregTechAPI.sBlockCasings1, CASING_INDEX)))
        .build();

    public MTEMultiFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMultiFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMultiFurnace(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
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
        return GTMod.gregtechproxy.mPollutionMultiSmelterPerSecond;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ArrayList<ItemStack> tInputList = getAllStoredInputs();
        if (tInputList.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        long inputVoltage = getMaxInputVoltage();

        int fakeOriginalMaxParallel = 1;
        OverclockCalculator calculator = new OverclockCalculator().setEUt(inputVoltage)
            .setRecipeEUt(RECIPE_EUT)
            .setDuration(RECIPE_DURATION)
            .setParallel(fakeOriginalMaxParallel);

        int maxParallel = this.mLevel;
        int originalMaxParallel = maxParallel;
        double tickTimeAfterOC = calculator.calculateDurationUnderOneTick();
        if (tickTimeAfterOC < 1) {
            maxParallel = GTUtility.safeInt((long) (maxParallel / tickTimeAfterOC), 0);
        }

        int maxParallelBeforeBatchMode = maxParallel;
        if (isBatchModeEnabled()) {
            maxParallel = GTUtility.safeInt((long) maxParallel * getMaxBatchSize(), 0);
        }

        // Calculate parallel
        int currentParallel = 0;
        for (ItemStack item : tInputList) {
            ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(item, false, null);
            if (smeltedOutput != null) {
                if (item.stackSize <= (maxParallel - currentParallel)) {
                    currentParallel += item.stackSize;
                } else {
                    currentParallel = maxParallel;
                    break;
                }
            }
        }
        if (currentParallel <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        int currentParallelBeforeBatchMode = Math.min(currentParallel, maxParallelBeforeBatchMode);
        int fakeCurrentParallel = (int) Math.ceil((double) currentParallelBeforeBatchMode / originalMaxParallel);

        calculator.setCurrentParallel(fakeCurrentParallel)
            .calculate();

        double batchMultiplierMax = 1;
        // In case batch mode enabled
        if (currentParallel > maxParallelBeforeBatchMode && calculator.getDuration() < getMaxBatchSize()) {
            batchMultiplierMax = (double) getMaxBatchSize() / calculator.getDuration();
            batchMultiplierMax = Math.min(batchMultiplierMax, (double) currentParallel / maxParallelBeforeBatchMode);
        }
        int finalParallel = (int) (batchMultiplierMax * maxParallelBeforeBatchMode);

        // Consume inputs and generate outputs
        ArrayList<ItemStack> smeltedOutputs = new ArrayList<>();
        int remainingCost = finalParallel;
        for (ItemStack item : tInputList) {
            ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(item, false, null);
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
        this.lEUt = calculator.getConsumption();

        if (this.lEUt > 0) this.lEUt = -this.lEUt;

        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public IStructureDefinition<MTEMultiFurnace> getStructureDefinition() {
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
                && aBaseMetaTileEntity.getBlock(xPos, tY + 1, zPos) == GregTechAPI.sBlockCasings1)
                aBaseMetaTileEntity.getWorld()
                    .setBlock(xPos, tY + 1, zPos, GregTechAPI.sBlockCasings5, tUsedMeta - 12, 3);
        }
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (final MTEHatchMuffler tHatch : filterValidMTEs(mMufflerHatches))
            mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (final MTEHatchEnergy tHatch : filterValidMTEs(mEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(-lEUt)
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*2A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GTUtility.getTier(getMaxInputVoltage())]
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
                + GTUtility.formatNumbers(mCostDiscount)
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
