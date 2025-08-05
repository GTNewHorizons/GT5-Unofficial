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
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
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
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.tileentities.machines.MTEHatchOutputBusME;

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
        .addElement('C', activeCoils(ofCoil(MTEMultiFurnace::setCoilLevel, MTEMultiFurnace::getCoilLevel)))
        .addElement(
            'b',
            ofChain(
                buildHatchAdder(MTEMultiFurnace.class).atLeast(Maintenance, InputBus, OutputBus, Energy)
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
            .addInfo("Smelts 4 * 2^(Coil Tier) items in parallel")
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

        maxParallel = GTUtility.safeInt((long) (maxParallel * calculator.calculateMultiplierUnderOneTick()), 0);

        int maxParallelBeforeBatchMode = maxParallel;
        if (isBatchModeEnabled()) {
            maxParallel = GTUtility.safeInt((long) maxParallel * getMaxBatchSize(), 0);
        }

        int currentParallel = (int) Math.min(maxParallel, availableEUt / RECIPE_EUT);
        int itemParallel = 0;
        for (ItemStack item : tInput) {
            ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(item, false, null);
            if (smeltedOutput != null) {
                int parallelsLeft = currentParallel - itemParallel;
                if (parallelsLeft <= 0) break;
                itemParallel += Math.min(item.stackSize, parallelsLeft);
            }
        }

        currentParallel = itemParallel;
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

        // Copy the getItemOutputSlots as to not mutate the output busses' slots.
        List<ItemStack> outputSlots = new ArrayList<>();
        for (ItemStack stack : getItemOutputSlots(null)) {
            if (stack != null) {
                outputSlots.add(stack.copy());
            } else {
                outputSlots.add(null);
            }
        }

        boolean hasMEOutputBus = false;
        for (final MTEHatch bus : validMTEList(mOutputBusses)) {
            if (bus instanceof MTEHatchOutputBusME meBus) {
                if (!meBus.isLocked() && meBus.canAcceptItem()) {
                    hasMEOutputBus = true;
                    break;
                }
            }
        }
        // Consume items and generate outputs
        ArrayList<ItemStack> smeltedOutputs = new ArrayList<>();
        int toSmelt = finalParallel;
        for (ItemStack item : tInput) {
            ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(item, false, null);
            if (smeltedOutput != null) {
                int maxOutput = 0;
                int remainingToSmelt = Math.min(toSmelt, item.stackSize);

                if (hasMEOutputBus) {
                    // Has an unlocked ME Output Bus and therefore can always fit the full stack
                    maxOutput = remainingToSmelt;
                } else {

                    // Calculate how many of this output can fit in the output slots
                    int needed = remainingToSmelt;
                    ItemStack outputType = smeltedOutput.copy();
                    outputType.stackSize = 1;

                    for (int i = 0; i < outputSlots.size(); i++) {
                        ItemStack slot = outputSlots.get(i);
                        if (slot == null) {
                            // Empty slot: can fit a full stack
                            int canFit = Math.min(needed, outputType.getMaxStackSize());
                            ItemStack newStack = outputType.copy();
                            newStack.stackSize = canFit;
                            outputSlots.set(i, newStack); // Fill the slot
                            maxOutput += canFit;
                            needed -= canFit;
                        } else if (slot.isItemEqual(outputType)) {
                            int canFit;
                            // Check for locked ME Output bus
                            if (slot.stackSize == 65) {
                                canFit = needed;
                            } else {
                                // Same type: can fit up to max stack size
                                int space = outputType.getMaxStackSize() - slot.stackSize;
                                canFit = Math.min(needed, space);
                            }
                            slot.stackSize += canFit;
                            maxOutput += canFit;
                            needed -= canFit;
                            // No need to set, since slot is a reference
                        }
                        if (needed <= 0) break;
                    }
                }

                // If void protection is enabled, only process what fits
                int toProcess = protectsExcessItem() ? maxOutput : remainingToSmelt;

                if (toProcess > 0) {
                    ItemStack outputStack = smeltedOutput.copy();
                    outputStack.stackSize *= toProcess;
                    smeltedOutputs.add(outputStack);

                    item.stackSize -= toProcess;
                    toSmelt -= toProcess;
                    if (toSmelt <= 0) break;
                }
            }
        }
        if (smeltedOutputs.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        this.mOutputItems = smeltedOutputs.toArray(new ItemStack[0]);

        this.mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = (int) (calculator.getDuration() * batchMultiplierMax);
        this.lEUt = calculator.getConsumption();
        if (this.lEUt > 0) {
            this.lEUt = -this.lEUt;
        }
        this.updateSlots();

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
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (final MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) {
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
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + getAveragePollutionPercentage()
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
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }
}
