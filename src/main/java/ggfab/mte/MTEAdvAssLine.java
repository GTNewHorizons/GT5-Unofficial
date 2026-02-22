package ggfab.mte;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE;
import static ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE_ACTIVE;
import static ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE_ACTIVE_GLOW;
import static ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE_GLOW;
import static ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE_STUCK;
import static ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE_STUCK_GLOW;
import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofHatchAdder;
import static gregtech.api.util.GTUtility.getTier;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.widget.ISyncedWidget;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import ggfab.ConfigurationHandler;
import ggfab.mui.ClickableTextWidget;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/*
 * Dev note: 1. This multi will be an assline but with greater throughput. it will take one input every 2.
 */
public class MTEAdvAssLine extends MTEExtendedPowerMultiBlockBase<MTEAdvAssLine> implements ISurvivalConstructable {

    public static final double LASER_OVERCLOCK_PENALTY_FACTOR = ConfigurationHandler.laserOCPenaltyFactor;
    private static final String STRUCTURE_PIECE_FIRST = "first";
    private static final String STRUCTURE_PIECE_LATER = "later";
    private static final String STRUCTURE_PIECE_LAST = "last";
    public static final String TAG_KEY_CURRENT_STICK = "mCurrentStick";
    public static final String TAG_KEY_CURRENT_RECIPE = "mCurrentRecipe";
    public static final String TAG_KEY_RECIPE_HASH = "mRecipeHash";
    public static final String TAG_KEY_PROGRESS_TIMES = "mProgressTimeArray";

    private static final IStructureDefinition<MTEAdvAssLine> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEAdvAssLine>builder()
        // @formatter:off
            .addShape(
                    STRUCTURE_PIECE_FIRST,
                    transpose(new String[][] {
                                    { " ", "e", " " },
                                    { "~", "l", "G" },
                                    { "g", "m", "g" },
                                    { "b", "i", "b" },
                            }))
            .addShape(
                    STRUCTURE_PIECE_LATER,
                    transpose(new String[][] {
                                    { " ", "e", " " },
                                    { "d", "l", "d" },
                                    { "g", "m", "g" },
                                    { "b", "I", "b" },
                            }))
            .addShape(
                    STRUCTURE_PIECE_LAST,
                    transpose(new String[][] {
                                    { " ", "e", " " },
                                    { "d", "l", "d" },
                                    { "g", "m", "g" },
                                    { "o", "i", "b" },
                            }))
            // @formatter:on
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings3, 10)) // grate machine casing
        .addElement('l', ofBlock(GregTechAPI.sBlockCasings2, 9)) // assembler machine casing
        .addElement('m', ofBlock(GregTechAPI.sBlockCasings2, 5)) // assembling line casing
        .addElement('g', chainAllGlasses())
        .addElement(
            'e',
            buildHatchAdder(MTEAdvAssLine.class).anyOf(Energy, ExoticEnergy)
                .hint(1)
                .casingIndex(16)
                .allowOnly(ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings2, 0)))
        .addElement(
            'd',
            buildHatchAdder(MTEAdvAssLine.class).atLeast(DataHatchElement.DataAccess)
                .hint(2)
                .casingIndex(42)
                .allowOnly(ForgeDirection.NORTH)
                .buildAndChain(GregTechAPI.sBlockCasings3, 10))
        .addElement(
            'b',
            buildHatchAdder(MTEAdvAssLine.class).atLeast(InputHatch, InputHatch, InputHatch, InputHatch, Maintenance)
                .casingIndex(16)
                .hint(3)
                .allowOnly(ForgeDirection.DOWN)
                .buildAndChain(
                    ofBlock(GregTechAPI.sBlockCasings2, 0),
                    ofHatchAdder(MTEAdvAssLine::addOutputToMachineList, 16, 4)))
        .addElement(
            'I',
            ofChain(
                // all blocks nearby use solid steel casing, so let's use the texture of that
                InputBus.newAny(16, 4, ForgeDirection.DOWN),
                ofHatchAdder(MTEAdvAssLine::addOutputToMachineList, 16, 3)))
        .addElement('i', InputBus.newAny(16, 4, ForgeDirection.DOWN))
        .addElement('o', OutputBus.newAny(16, 3, ForgeDirection.DOWN))
        .build();
    private GTRecipe.RecipeAssemblyLine currentRecipe;
    private final Slice[] slices = IntStream.range(0, 16)
        .mapToObj(Slice::new)
        .toArray(Slice[]::new);
    private boolean processing;
    private long inputVoltage;
    // surely no one is using more EUt than this, no?
    private long inputEUt;
    private long baseEUt;
    private boolean stuck;

    private final List<MTEHatchDataAccess> mDataAccessHatches = new ArrayList<>();
    private Map<GTUtility.ItemId, ItemStack> curBatchItemsFromME;
    private Map<Fluid, FluidStack> curBatchFluidsFromME;
    private int currentInputLength;
    private String lastStopReason = "";
    private int currentRecipeParallel = 1;

    public MTEAdvAssLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAdvAssLine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvAssLine(mName);
    }

    public boolean addDataAccessToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchDataAccess) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDataAccessHatches.add((MTEHatchDataAccess) aMetaTileEntity);
        }
        return false;
    }

    private boolean checkMachine() {
        mDataAccessHatches.clear();
        if (!checkPiece(STRUCTURE_PIECE_FIRST, 0, 1, 0)) return false;
        return checkMachine(true) || checkMachine(false);
    }

    private boolean checkMachine(boolean leftToRight) {
        for (int i = 1; i < 16; i++) {
            if (!checkPiece(STRUCTURE_PIECE_LATER, leftToRight ? -i : i, 1, 0)) return false;
            if (!mOutputBusses.isEmpty())
                return (!mEnergyHatches.isEmpty() || !mExoticEnergyHatches.isEmpty()) && mMaintenanceHatches.size() == 1
                    && mDataAccessHatches.size() <= 1;
        }
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_FIRST, stackSize, hintsOnly, 0, 1, 0);
        int tLength = GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(stackSize, 5, 16); // render 5 slices at
                                                                                              // minimal
        for (int i = 1; i < tLength; i++) {
            buildPiece(STRUCTURE_PIECE_LATER, stackSize, hintsOnly, -i, 1, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int build = survivalBuildPiece(STRUCTURE_PIECE_FIRST, stackSize, 0, 1, 0, elementBudget, env, false, true);
        if (build >= 0) return build;
        int tLength = GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(stackSize, 5, 16); // render 5 slices at
                                                                                              // minimal
        for (int i = 1; i < tLength; i++) {
            build = survivalBuildPiece(STRUCTURE_PIECE_LATER, stackSize, -i, 1, 0, elementBudget, env, false, true);
            if (build >= 0) return build;
        }
        return survivalBuildPiece(STRUCTURE_PIECE_LAST, stackSize, 1 - tLength, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (stuck) {
                return new ITexture[] { casingTexturePages[0][16], TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ADV_ASSLINE_STUCK)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ADV_ASSLINE_STUCK_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
            if (aActive) return new ITexture[] { casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ADV_ASSLINE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ADV_ASSLINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ADV_ASSLINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ADV_ASSLINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][16] };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Assembly Line, AAL")
            .addInfo("Assembly Line with item pipelining")
            .addInfo("All fluids are consumed at the start of the recipe")
            .addInfo("Recipe tier is limited by the lowest Energy Hatch tier")
            .addSeparator(EnumChatFormatting.GOLD, 67)
            .addInfo("Runs imperfect overclocks until Energy Hatch tier")
            .addInfo("Additional overclocks are increasingly more expensive")
            .addInfo(
                EnumChatFormatting.AQUA
                    + "Multiplier = 4^(Regular Overclocks) × 4.3 × 4.6 × … × (4 + 0.3 × Extra Overclocks)"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                EnumChatFormatting.AQUA + "Power usage = Multiplier × (Active Slices) × (Recipe EU/t)"
                    + EnumChatFormatting.GRAY)
            .addInfo("Overclocking assumes all recipe slices are active")
            .addInfo(EnumChatFormatting.BOLD + "Does not overclock beyond 1 tick")
            .addSeparator(EnumChatFormatting.GOLD, 67)
            .addInfo("Constructed identically to the Assembly Line")
            .addTecTechHatchInfo()
            .beginVariableStructureBlock(5, 16, 4, 4, 3, 3, false)
            .addStructureInfo("From Bottom to Top, Left to Right")
            .addStructureInfo("Layer 1 - Solid Steel Machine Casing, Input Bus, Solid Steel Machine Casing")
            .addStructureInfo("Layer 2 - Glass, Assembly Line Casing, Glass")
            .addStructureInfo("Layer 3 - Grate Machine Casing, Assembler Machine Casing, Grate Machine Casing")
            .addStructureInfo("Layer 4 - Empty, Solid Steel Machine Casing, Empty")
            .addStructureInfo("Up to 16 repeating slices, each one allows for 1 more item in recipes")
            .addController("Either Grate on layer 3 of the first slice")
            .addEnergyHatch("Any layer 4 casing", 1)
            .addMaintenanceHatch("Any layer 1 casing", 3)
            .addInputBus("As specified on layer 1", 4)
            .addInputHatch("Any layer 1 casing", 3)
            .addOutputBus("Replaces Input Bus or Solid Steel Machine casing on layer 1 of last slice", 3)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.data_access_hatch"),
                "Any Grate Machine Casing NOT on the first slice",
                2)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(EnumChatFormatting.GRAY, 67);
        return tt;
    }

    private void setCurrentRecipe(RecipeAssemblyLine recipe) {
        currentRecipe = recipe;
        currentInputLength = recipe.mInputs.length;
    }

    private void clearCurrentRecipe() {
        currentRecipe = null;
        currentInputLength = -1;
        currentRecipeParallel = 1;
        stuck = false;
        baseEUt = 0;
        for (Slice slice : slices) {
            slice.reset();
        }
        mMaxProgresstime = 0;
        getBaseMetaTileEntity().issueClientUpdate();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setString("lastStop", lastStopReason);
        // we need to check for active here.
        // if machine was turned off via soft mallet it will not call checkRecipe() on recipe end
        // in that case we don't have a current recipe, so this should be ignored
        if (getBaseMetaTileEntity().isActive() && currentRecipe != null) {
            aNBT.setTag(TAG_KEY_CURRENT_RECIPE, AssemblyLineUtils.saveRecipe(currentRecipe));
            aNBT.setInteger(TAG_KEY_RECIPE_HASH, currentRecipe.getPersistentHash());
            aNBT.setIntArray(
                TAG_KEY_PROGRESS_TIMES,
                Arrays.stream(slices)
                    .limit(currentInputLength)
                    .mapToInt(s -> s.progress)
                    .toArray());
            aNBT.setBoolean("stuck", stuck);
            aNBT.setLong("inputV", inputVoltage);
            aNBT.setLong("inputEU", inputEUt);
            aNBT.setLong("baseEU", baseEUt);
            aNBT.setInteger("currentParallel", currentRecipeParallel);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        lastStopReason = aNBT.getString("lastStop");

        if (aNBT.hasKey(TAG_KEY_PROGRESS_TIMES, Constants.NBT.TAG_INT_ARRAY)) {
            int[] arr = aNBT.getIntArray(TAG_KEY_PROGRESS_TIMES);
            for (int i = 0; i < slices.length; i++) {
                if (i < arr.length) {
                    slices[i].progress = arr[i];
                    if (arr[i] == 0) {
                        // this will be synced to client by first MTE packet to client
                        stuck = true;
                    }
                } else {
                    slices[i].reset();
                }
            }
        }

        RecipeAssemblyLine recipe = null;

        if (aNBT.hasKey(TAG_KEY_CURRENT_RECIPE, Constants.NBT.TAG_COMPOUND)) {
            recipe = AssemblyLineUtils
                .assertSingleRecipe(AssemblyLineUtils.loadRecipe(aNBT.getCompoundTag(TAG_KEY_CURRENT_RECIPE)));
        } else if (aNBT.hasKey(TAG_KEY_CURRENT_STICK, Constants.NBT.TAG_COMPOUND)) {
            recipe = AssemblyLineUtils.assertSingleRecipe(
                AssemblyLineUtils.findALRecipeFromDataStick(
                    ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(TAG_KEY_CURRENT_STICK))));
        }

        if (recipe != null) {
            stuck = aNBT.getBoolean("stuck");
            inputVoltage = aNBT.getLong("inputV");
            inputEUt = aNBT.getLong("inputEU");
            baseEUt = aNBT.getLong("baseEU");
            currentRecipeParallel = aNBT.getInteger("currentParallel");
            if (inputVoltage <= 0 || inputEUt <= 0 || baseEUt >= 0) {
                criticalStopMachine("ggfab.gui.advassline.shutdown.load.energy");
                recipe = null;
            }
            if (recipe != null && aNBT.hasKey(TAG_KEY_RECIPE_HASH, Constants.NBT.TAG_INT)) {
                if (aNBT.getInteger(TAG_KEY_RECIPE_HASH) != recipe.getPersistentHash()) {
                    criticalStopMachine("ggfab.gui.advassline.shutdown.load.recipe");
                    recipe = null;
                }
            }
        }

        if (recipe == null) {
            clearCurrentRecipe();
        } else {
            setCurrentRecipe(recipe);
        }
    }

    /**
     * Does a critical shutdown of the machine, but does not attempt to send a halting sound if world is not loaded.
     * also supports setting a stop reason
     */
    private void criticalStopMachine(String reason) {
        int oMaxProgresstime = mMaxProgresstime;
        stopMachine(ShutDownReasonRegistry.NONE);
        // don't do these at all if the machine wasn't working before anyway
        if (oMaxProgresstime > 0) {
            if (getBaseMetaTileEntity().getWorld() != null) sendSound(INTERRUPT_SOUND_INDEX);
            getBaseMetaTileEntity().setShutdownStatus(true);
            lastStopReason = reason;
        }
    }

    @Override
    public IStructureDefinition<MTEAdvAssLine> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mExoticEnergyHatches.clear();
        mDataAccessHatches.clear();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (checkMachine() && (!mEnergyHatches.isEmpty() || !mExoticEnergyHatches.isEmpty())) {
            long oV = inputVoltage, oEut = inputEUt;
            inputVoltage = Integer.MAX_VALUE;
            inputEUt = 0;
            mEnergyHatches.forEach(this::recordEnergySupplier);
            mExoticEnergyHatches.forEach(this::recordEnergySupplier);
            if (mMaxProgresstime > 0 && (oV != inputVoltage || oEut != inputEUt)) {
                criticalStopMachine("ggfab.gui.advassline.shutdown.structure");
            }
            return true;
        } else {
            inputVoltage = V[0];
            return false;
        }
    }

    private void recordEnergySupplier(MTEHatch hatch) {
        if (!hatch.isValid()) return;
        inputEUt += hatch.maxEUInput() * hatch.maxWorkingAmperesIn();
        inputVoltage = Math.min(inputVoltage, hatch.maxEUInput());
        if (inputEUt < 0) inputEUt = Long.MAX_VALUE;
    }

    @Override
    public void startRecipeProcessing() {
        if (!processing) {
            super.startRecipeProcessing();
            curBatchItemsFromME = getStoredInputsFromME();
            curBatchFluidsFromME = getStoredFluidsFromME();
            processing = true;
        }
    }

    @Override
    public void endRecipeProcessing() {
        if (!processing) return;
        super.endRecipeProcessing();
        processing = false;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        boolean oStuck = stuck;
        stuck = (aValue & 1) == 1;
        if (oStuck != stuck) getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    public byte getUpdateData() {
        return (byte) (stuck ? 1 : 0);
    }

    @Override
    protected boolean useMui2() {
        return false;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        /*
         * SliceStatusWidget[] arr =
         * Arrays.stream(slices).map(SliceStatusWidget::new).toArray(SliceStatusWidget[]::new);
         * screenElements.widgets(arr); screenElements.widget(new FakeSyncWidget.IntegerSyncer(() -> currentInputLength,
         * l -> { currentInputLength = l; for (SliceStatusWidget w : arr) { w.updateText(); } }));
         */
        screenElements.widget(
            new TextWidget(Text.localised("ggfab.gui.advassline.shutdown")).setTextAlignment(Alignment.CenterLeft)
                .setEnabled(this::hasAbnormalStopReason));
        screenElements.widget(
            new TextWidget().setTextSupplier(() -> Text.localised(lastStopReason))
                .setTextAlignment(Alignment.CenterLeft)
                .attachSyncer(
                    new FakeSyncWidget.StringSyncer(() -> lastStopReason, r -> this.lastStopReason = r),
                    screenElements)
                .setEnabled(this::hasAbnormalStopReason));
        screenElements.widget(
            new ClickableTextWidget(
                Text.localised("ggfab.gui.advassline.shutdown_clear")
                    .alignment(Alignment.CenterLeft)).setMarginInLines(0)
                        .setOnClick((d, w) -> lastStopReason = "")
                        .setSize(36, 20)
                        .setEnabled(this::hasAbnormalStopReason));
    }

    private Boolean hasAbnormalStopReason(Widget w) {
        return !StringUtils.isNullOrEmpty(lastStopReason);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.assemblylineVisualRecipes;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (currentRecipe == null) {
            criticalStopMachine("ggfab.gui.advassline.shutdown.recipe_null");
            return false;
        }
        for (MTEHatchDataAccess hatch_dataAccess : mDataAccessHatches) {
            hatch_dataAccess.setActive(true);
        }

        if (mInputBusses.size() < currentInputLength) {
            criticalStopMachine("ggfab.gui.advassline.shutdown.input_buses");
            return false;
        }
        boolean oStuck = stuck;
        stuck = false;

        for (int i = slices.length - 1; i >= 0; i--) {
            slices[i].tick();
        }

        if (oStuck != stuck)
            // send the status as it has changed
            getBaseMetaTileEntity().issueClientUpdate();

        if (getBaseMetaTileEntity().isAllowedToWork() && slices[0].progress < 0) {
            startRecipeProcessing();
            if (hasAllItems(currentRecipe, this.currentRecipeParallel)
                && hasAllFluids(currentRecipe, this.currentRecipeParallel)
                && slices[0].start()) {
                drainAllFluids(currentRecipe, this.currentRecipeParallel);
                mProgresstime = 0;
            }
        }

        endRecipeProcessing();

        boolean foundWorking = false;
        int working = 0;
        for (Slice slice : slices) {
            if (slice.progress >= 0) {
                if (!foundWorking) {
                    foundWorking = true;
                    mProgresstime = (slice.id + 1) * (mMaxProgresstime / currentInputLength) - slice.progress;
                }
            }
            if (slice.progress > 0) working++;
        }
        lEUt = working * baseEUt;

        if (lEUt > 0) {
            // overflow again :(
            lEUt = Long.MIN_VALUE;
            for (int i = 0; i < working; i++) {
                if (!drainEnergyInput(-baseEUt)) {
                    criticalStopMachine("ggfab.gui.advassline.shutdown.energy");
                    return false;
                }
            }
        } else {
            return super.onRunningTick(aStack);
        }

        return true;
    }

    private ItemStack getInputBusContent(int index) {
        if (index < 0 || index >= mInputBusses.size()) return null;
        MTEHatchInputBus inputBus = mInputBusses.get(index);
        if (!inputBus.isValid()) return null;
        if (inputBus instanceof MTEHatchInputBusME meBus) {
            ItemStack item = meBus.getFirstValidStack(true);
            if (item == null) return null;
            GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(item);
            if (!curBatchItemsFromME.containsKey(id)) return null;
            return curBatchItemsFromME.get(id);
        }
        return inputBus.getFirstStack();

    }

    private FluidStack getInputHatchContent(int index) {
        if (index < 0 || index >= mInputHatches.size()) return null;
        MTEHatchInput inputHatch = mInputHatches.get(index);
        if (!inputHatch.isValid()) return null;
        if (inputHatch instanceof MTEHatchInputME meHatch) {
            FluidStack fluid = meHatch.getFirstValidStack(true);
            if (fluid == null) return null;
            if (!curBatchFluidsFromME.containsKey(fluid.getFluid())) return null;
            return curBatchFluidsFromME.get(fluid.getFluid());
        }
        if (inputHatch instanceof MTEHatchMultiInput multiHatch) {
            return multiHatch.getFluid();
        }
        return inputHatch.getFillableStack();
    }

    private int maxParallelCalculatedByInputItems(GTRecipe.RecipeAssemblyLine tRecipe, int maxParallel) {
        int aItemCount = tRecipe.mInputs.length;
        if (mInputBusses.size() < aItemCount) return 0;
        int[] itemConsumptions = GTRecipe.RecipeAssemblyLine.getItemConsumptionAmountArray(mInputBusses, tRecipe);
        if (itemConsumptions == null || itemConsumptions.length == 0) {
            return 0;
        }
        return (int) GTRecipe.RecipeAssemblyLine
            .maxParallelCalculatedByInputItems(mInputBusses, maxParallel, itemConsumptions, curBatchItemsFromME);
    }

    private int maxParallelCalculatedByInputFluids(GTRecipe.RecipeAssemblyLine tRecipe, int maxParallel) {
        int aFluidCount = tRecipe.mFluidInputs.length;
        if (mInputHatches.size() < aFluidCount) return 0;
        return (int) GTRecipe.RecipeAssemblyLine
            .maxParallelCalculatedByInputFluids(mInputHatches, maxParallel, tRecipe.mFluidInputs, curBatchFluidsFromME);
    }

    private boolean hasAllItems(GTRecipe.RecipeAssemblyLine tRecipe, int parallel) {
        return maxParallelCalculatedByInputItems(tRecipe, parallel) >= parallel;
    }

    private boolean hasAllFluids(GTRecipe.RecipeAssemblyLine tRecipe, int parallel) {
        return maxParallelCalculatedByInputFluids(tRecipe, parallel) >= parallel;
    }

    // this is only called when all slices have finished their work
    // and the first slice cannot find a input/fluid cannot be found
    // so we are safe to assume the old recipe no longer works
    @Override
    public @Nonnull CheckRecipeResult checkProcessing() {
        if (GTValues.D1) {
            GT_FML_LOGGER.info("Start Adv ALine recipe check");
        }
        clearCurrentRecipe();
        CheckRecipeResult result = CheckRecipeResultRegistry.NO_DATA_STICKS;

        ArrayList<RecipeAssemblyLine> availableRecipes = new ArrayList<>();

        if (AssemblyLineUtils.isItemDataStick(mInventory[1])) {
            availableRecipes.addAll(AssemblyLineUtils.findALRecipeFromDataStick(mInventory[1]));
        }

        for (MTEHatchDataAccess dataAccess : validMTEList(mDataAccessHatches)) {
            availableRecipes.addAll(dataAccess.getAssemblyLineRecipes());
        }

        if (availableRecipes.isEmpty()) {
            return result;
        }

        if (GTValues.D1) {
            GT_FML_LOGGER.info("Stick accepted, " + availableRecipes.size() + " Data Sticks found");
        }

        for (RecipeAssemblyLine recipe : availableRecipes) {
            // Check item Inputs align. For this we do not need to consider batch mode parallels yet, this will be done
            // later on during recipe start.
            if (!hasAllItems(recipe, 1)) {
                if (result == CheckRecipeResultRegistry.NO_DATA_STICKS) result = CheckRecipeResultRegistry.NO_RECIPE;
                continue;
            }

            // Check Fluid Inputs align. Again, do not consider parallels
            if (!hasAllFluids(recipe, 1)) {
                if (result == CheckRecipeResultRegistry.NO_DATA_STICKS) result = CheckRecipeResultRegistry.NO_RECIPE;
                continue;
            }

            // Check voltage tier is at least recipe tier.
            if (recipe.mEUt > inputVoltage) {
                result = CheckRecipeResultRegistry.insufficientVoltage(recipe.mEUt);
                continue;
            }

            // Check all slices can run with provided power.
            if ((long) recipe.mInputs.length * recipe.mEUt > inputEUt) {
                result = CheckRecipeResultRegistry.insufficientPower((long) recipe.mInputs.length * recipe.mEUt);
                continue;
            }

            int originalMaxParallel = 1;
            int maxParallel = originalMaxParallel;

            int maxRegularOverclock = getTier(inputVoltage) - getTier(recipe.mEUt);

            // Delete this one before enable overclocking under one tick.
            int maxOverclockTo1Tick = GTUtility.log2(recipe.mDuration / recipe.mInputs.length);

            OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(recipe.mEUt)
                .setDurationUnderOneTickSupplier(() -> ((double) (recipe.mDuration) / recipe.mInputs.length))
                .setParallel(originalMaxParallel)
                .setEUt(inputEUt / recipe.mInputs.length)
                .setLaserOC(true)
                .setMaxRegularOverclocks(Math.min(maxRegularOverclock, maxOverclockTo1Tick));

            // Disabled to disable overclocking under one tick.
            /*
             * maxParallel = GTUtility.safeInt((long) (maxParallel * calculator.calculateMultiplierUnderOneTick()), 0);
             */

            int maxParallelBeforeBatchMode = maxParallel;
            if (isBatchModeEnabled()) {
                maxParallel = GTUtility.safeInt((long) maxParallel * getMaxBatchSize(), 0);
            }

            if (protectsExcessItem()) {
                VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper();
                voidProtectionHelper.setMachine(this)
                    .setItemOutputs(new ItemStack[] { recipe.mOutput })
                    .setMaxParallel(maxParallel)
                    .build();
                maxParallel = Math.min(voidProtectionHelper.getMaxParallel(), maxParallel);
                if (voidProtectionHelper.isItemFull()) {
                    result = CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
                    continue;
                }
            }

            FluidStack firstFluidSlot = getInputHatchContent(0);
            if (firstFluidSlot == null) {
                result = CheckRecipeResultRegistry.INTERNAL_ERROR;
                break;
            }

            int currentParallel = firstFluidSlot.amount / recipe.mFluidInputs[0].amount;
            if (isBatchModeEnabled()) {
                // Divide recipes available by the amount of slices in the recipe. This will prevent the AAL from
                // batching instead of parallelizing, which would make it effectively slower.
                currentParallel /= recipe.mInputs.length;
            }
            currentParallel = Math.min(currentParallel, maxParallel);
            // Sanity check to avoid this being zero when there is only one recipe available.
            currentParallel = Math.max(currentParallel, 1);

            currentParallel = Math.min(currentParallel, maxParallelCalculatedByInputItems(recipe, currentParallel));
            currentParallel = Math.min(currentParallel, maxParallelCalculatedByInputFluids(recipe, currentParallel));

            if (currentParallel <= 0) {
                result = CheckRecipeResultRegistry.INTERNAL_ERROR;
                continue;
            }

            int currentParallelBeforeBatchMode = Math.min(currentParallel, maxParallelBeforeBatchMode);

            calculator.setCurrentParallel(currentParallelBeforeBatchMode)
                .calculate();

            double batchMultiplierMax = 1;
            // In case batch mode enabled
            if (currentParallel > maxParallelBeforeBatchMode && calculator.getDuration() < getMaxBatchSize()) {
                batchMultiplierMax = (double) getMaxBatchSize() / calculator.getDuration();
                batchMultiplierMax = Math
                    .min(batchMultiplierMax, (double) currentParallel / maxParallelBeforeBatchMode);
            }
            int batchMultiplierMaxInt = (int) batchMultiplierMax;
            currentRecipeParallel = (int) (currentParallelBeforeBatchMode * batchMultiplierMaxInt);
            lEUt = calculator.getConsumption();
            mMaxProgresstime = (int) (calculator.getDuration() * batchMultiplierMaxInt) * recipe.mInputs.length;
            setCurrentRecipe(recipe);
            result = CheckRecipeResultRegistry.SUCCESSFUL;
            break;
        }
        if (!result.wasSuccessful()) {
            clearCurrentRecipe();
            return result;
        }
        if (currentRecipe == null || !slices[0].start() || currentRecipeParallel <= 0) {
            clearCurrentRecipe();
            // something very very wrong...
            return CheckRecipeResultRegistry.INTERNAL_ERROR;
        }

        if (GTValues.D1) {
            GT_FML_LOGGER.info("All checked start consuming inputs");
        }
        drainAllFluids(currentRecipe, this.currentRecipeParallel);

        // Apply parallel
        mOutputItems = new ItemStack[] { currentRecipe.mOutput.copy() };
        mOutputItems[0].stackSize *= this.currentRecipeParallel;

        if (this.lEUt > 0) {
            this.lEUt = -this.lEUt;
        }
        baseEUt = lEUt;
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        if (GTValues.D1) {
            GT_FML_LOGGER.info("Recipe successful");
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public Set<VoidingMode> getAllowedVoidingModes() {
        return VoidingMode.ITEM_ONLY_MODES;
    }

    @Override
    protected boolean supportsSlotAutomation(int aSlot) {
        return aSlot == getControllerSlotIndex();
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        String machineProgressString = GTWaila.getMachineProgressString(
            tag.getBoolean("isActive"),
            tag.getBoolean("isAllowedToWork"),
            tag.getInteger("maxProgress"),
            tag.getInteger("progress"));
        currentTip.remove(machineProgressString);

        int duration = tag.getInteger("mDuration");
        if (tag.hasKey(TAG_KEY_PROGRESS_TIMES, Constants.NBT.TAG_LIST)) {
            NBTTagList tl = tag.getTagList(TAG_KEY_PROGRESS_TIMES, Constants.NBT.TAG_INT);
            @SuppressWarnings("unchecked")
            List<NBTTagInt> list = tl.tagList;
            for (int i = 0, listSize = list.size(); i < listSize; i++) {
                NBTTagInt t = list.get(i);
                int progress = t.func_150287_d();
                if (progress == 0) {
                    currentTip.add(I18n.format("ggfab.waila.advassline.slice.stuck", i + 1));
                } else if (progress < 0) {
                    currentTip.add(I18n.format("ggfab.waila.advassline.slice.idle", i + 1));
                } else if (duration > 40) {
                    currentTip.add(
                        I18n.format("ggfab.waila.advassline.slice", i + 1, (duration - progress) / 20, duration / 20));
                } else {
                    currentTip
                        .add(I18n.format("ggfab.waila.advassline.slice.small", i + 1, duration - progress, duration));
                }
            }
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (currentRecipe == null || !getBaseMetaTileEntity().isActive()) return;
        NBTTagList l = new NBTTagList();
        for (int i = 0; i < currentInputLength; i++) {
            l.appendTag(new NBTTagInt(slices[i].progress));
        }
        tag.setTag(TAG_KEY_PROGRESS_TIMES, l);
        tag.setInteger("mDuration", mMaxProgresstime / currentInputLength);
    }

    /**
     * Caller is responsible to check and ensure the hatches are there and has all the fluid needed. You will usually
     * want to ensure hasAllFluid was called right before calling this, otherwise very bad things can happen.
     */
    private void drainAllFluids(GTRecipe.RecipeAssemblyLine recipe, int parallel) {
        GTRecipe.RecipeAssemblyLine
            .consumeInputFluids(mInputHatches, parallel, recipe.mFluidInputs, curBatchFluidsFromME);
        for (MTEHatchInput tHatch : validMTEList(mInputHatches)) tHatch.updateSlots();
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        clearCurrentRecipe();
        super.stopMachine(reason);
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

    private class SliceStatusWidget extends TextWidget implements ISyncedWidget {

        private final Slice slice;
        private int lastProgress = -2;
        private Text text;

        private SliceStatusWidget(Slice slice) {
            this.slice = slice;
            updateText();
            setEnabled(w -> slice.progress == 0 && currentInputLength > slice.id);
        }

        @Override
        public Text getText() {
            return text;
        }

        @Override
        public void readOnClient(int id, PacketBuffer buf) {
            if (id == 0) {
                slice.progress = buf.readVarIntFromBuffer();
                updateText();
                checkNeedsRebuild();
            }
        }

        public void updateText() {
            String type = "unknown";
            if (slice.progress == 0) type = "stuck";
            else if (slice.progress < 0) type = "idle";
            text = Text.localised("ggfab.gui.advassline.slice." + type, slice.id);
        }

        @Override
        public void readOnServer(int id, PacketBuffer buf) {}

        @Override
        public void detectAndSendChanges(boolean init) {
            if (slice.progress != lastProgress) {
                // suppress small normal progress update
                if (slice.progress > 0 && lastProgress > 0 && lastProgress - slice.progress < 10) return;
                lastProgress = slice.progress;
                syncToClient(0, b -> b.writeVarIntToBuffer(slice.progress));
            }
        }

        @Override
        public void markForUpdate() {}

        @Override
        public void unMarkForUpdate() {}

        @Override
        public boolean isMarkedForUpdate() {
            return false;
        }
    }

    private class Slice {

        private final int id;
        private int progress = -1;

        public Slice(int id) {
            this.id = id;
        }

        public void reset() {
            progress = -1;
        }

        public void tick() {
            if (progress < 0) return;
            if (progress == 0 || --progress == 0) {
                // id==0 will be end of chain if 1 input, so we need a +1 here
                if (id + 1 >= currentInputLength) {
                    // use previously calculated parallel output
                    ItemStack output = mOutputItems[0];
                    if (addOutputAtomic(GTUtility.copy(output)) || !voidingMode.protectItem) {
                        recipesDone += currentRecipeParallel;
                        reset();
                    } else {
                        stuck = true;
                    }
                } else {
                    if (slices[id + 1].start()) {
                        reset();
                    } else {
                        stuck = true;
                    }
                }
            }
        }

        public boolean start() {
            if (progress >= 0) return false;
            startRecipeProcessing();
            ItemStack stack = getInputBusContent(id);
            if (stack == null) return false;
            int size = GTRecipe.RecipeAssemblyLine
                .getMatchedIngredientAmount(stack, currentRecipe.mInputs[id], currentRecipe.mOreDictAlt[id]);
            if (size < 0 || stack.stackSize < size * currentRecipeParallel) return false;
            progress = mMaxProgresstime / currentInputLength;
            stack.stackSize -= size * currentRecipeParallel;
            mInputBusses.get(id)
                .updateSlots();
            return true;
        }

        @Override
        public String toString() {
            return "Slice{" + "id=" + id + ", progress=" + progress + '}';
        }
    }

    private enum DataHatchElement implements IHatchElement<MTEAdvAssLine> {

        DataAccess;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchDataAccess.class);
        }

        @Override
        public IGTHatchAdder<MTEAdvAssLine> adder() {
            return MTEAdvAssLine::addDataAccessToMachineList;
        }

        @Override
        public long count(MTEAdvAssLine t) {
            return t.mDataAccessHatches.size();
        }
    }
}
