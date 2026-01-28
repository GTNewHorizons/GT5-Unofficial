package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.VP;
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
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;

public class MTEMultiFurnace extends MTEAbstractMultiFurnace<MTEMultiFurnace> implements ISurvivalConstructable {

    private int mLevel = 0;

    private static final long RECIPE_EUT = 4;
    private static final int RECIPE_DURATION = 128;
    private static final int CASING_INDEX = 11;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEMultiFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMultiFurnace>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "ccc", "cmc", "ccc" }, { "CCC", "C-C", "CCC" }, { "b~b", "bbb", "bbb" } }))
        .addElement('c', ofBlock(GregTechAPI.sBlockCasings1, CASING_INDEX))
        .addElement('m', Muffler.newAny(CASING_INDEX, 2))
        .addElement(
            'C',
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTEMultiFurnace::setCoilLevel, MTEMultiFurnace::getCoilLevel))))
        .addElement(
            'b',
            ofChain(
                buildHatchAdder(MTEMultiFurnace.class).atLeast(Maintenance, InputBus, OutputBus, Energy)
                    .casingIndex(CASING_INDEX)
                    .hint(1)
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
            .addStaticParallelInfo(4)
            .addDynamicMultiplicativeParallelInfo(2, TooltipTier.COIL)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front bottom")
            .addCasingInfoRange("Heat Proof Machine Casing", 8, 14, false)
            .addOtherStructurePart("Heating Coils", "Middle layer")
            .addEnergyHatch("Any bottom casing", 1)
            .addMaintenanceHatch("Any bottom casing", 1)
            .addMufflerHatch("Top Middle", 2)
            .addInputBus("Any bottom casing", 1)
            .addOutputBus("Any bottom casing", 1)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
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

    /*
     * NOTE: If you are wondering why your machine is not showing up in the NEIHandler for furnaces...
     * it is handled in the NEI fork's catalysts.csv . so that multiple mods can show up in the same handler.
     */
    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.furnaceRecipes;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTMod.proxy.mPollutionMultiSmelterPerSecond;
    }

    // Not GPL
    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        List<ItemStack> tInput = getAllStoredInputs();
        long availableEUt = GTUtility.roundUpVoltage(getMaxInputVoltage());
        if (availableEUt < RECIPE_EUT) {
            return CheckRecipeResultRegistry.insufficientPower(RECIPE_EUT);
        }
        if (tInput.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        int maxParallel = this.mLevel;
        int originalMaxParallel = this.mLevel;

        OverclockCalculator calculator = new OverclockCalculator().setEUt(availableEUt)
            .setRecipeEUt(RECIPE_EUT)
            .setDuration(RECIPE_DURATION)
            .setParallel(originalMaxParallel);

        maxParallel = GTUtility.longToInt((long) (maxParallel * calculator.calculateMultiplierUnderOneTick()));

        int maxParallelBeforeBatchMode = maxParallel;
        if (isBatchModeEnabled()) {
            maxParallel = GTUtility.longToInt((long) maxParallel * getMaxBatchSize());
        }

        maxParallel = Math.min(maxParallel, GTUtility.longToInt(availableEUt / RECIPE_EUT));

        int currentParallel = 0;
        for (ItemStack item : tInput) {
            ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(item, false, null);

            if (smeltedOutput == null) continue;

            int parallelsLeft = maxParallel - currentParallel;
            if (parallelsLeft <= 0) break;

            currentParallel += Math.min(item.stackSize, parallelsLeft);
        }

        if (currentParallel <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        int currentParallelBeforeBatchMode = Math.min(currentParallel, maxParallelBeforeBatchMode);
        calculator.setCurrentParallel(currentParallelBeforeBatchMode)
            .calculate();

        double batchMultiplierMax = 1;
        // In case batch mode enabled
        if (currentParallel > maxParallelBeforeBatchMode && calculator.getDuration() < getMaxBatchSize()) {
            batchMultiplierMax = (double) getMaxBatchSize() / calculator.getDuration();
            batchMultiplierMax = Math.min(batchMultiplierMax, (double) currentParallel / maxParallelBeforeBatchMode);
        }

        int finalParallel = (int) (batchMultiplierMax * currentParallelBeforeBatchMode);

        ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(this);

        // Consume items and generate outputs
        HashMap<GTUtility.ItemId, ItemStack> smeltedOutputs = new HashMap<>();
        int toSmelt = finalParallel;

        for (ItemStack item : tInput) {
            ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(item, false, null);

            if (smeltedOutput == null) continue;

            int remainingToSmelt = Math.min(toSmelt, item.stackSize);

            int smeltable = ejectionHelper
                .ejectItems(Collections.singletonList(smeltedOutput.copy()), remainingToSmelt);

            if (smeltable == 0) continue;

            ItemStack outputStack = smeltedOutputs
                .computeIfAbsent(GTUtility.ItemId.create(smeltedOutput), x -> GTUtility.copyAmount(0, smeltedOutput));
            outputStack.stackSize += smeltedOutput.stackSize * smeltable;

            item.stackSize -= smeltable;
            toSmelt -= smeltable;
            if (toSmelt <= 0) break;
        }

        if (smeltedOutputs.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        this.mOutputItems = smeltedOutputs.values()
            .toArray(new ItemStack[0]);

        this.mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = (int) (calculator.getDuration() * batchMultiplierMax);
        this.lEUt = Math.min(VP[GTUtility.getTier(getAverageInputVoltage())], calculator.getConsumption());
        if (this.lEUt > 0) {
            this.lEUt = -this.lEUt;
        }
        this.updateSlots();
        if (this.recipesDone == 0) this.recipesDone++;
        // Multiblock base already includes 1 parallel
        this.recipesDone += finalParallel - 1;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public IStructureDefinition<MTEMultiFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mLevel = 0;

        setCoilLevel(HeatingCoilLevel.None);

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0)) return false;

        if (getCoilLevel() == HeatingCoilLevel.None) return false;

        if (mMaintenanceHatches.size() != 1) return false;

        this.mLevel = 4 << (getCoilLevel().ordinal() - 1);

        return true;
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(
            StatCollector.translateToLocal("GT5U.MS.multismelting") + ": "
                + EnumChatFormatting.GREEN
                + mLevel
                + EnumChatFormatting.RESET);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }
}
