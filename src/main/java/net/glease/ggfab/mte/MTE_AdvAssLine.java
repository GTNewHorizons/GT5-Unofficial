package net.glease.ggfab.mte;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static net.glease.ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE;
import static net.glease.ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE_ACTIVE;
import static net.glease.ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE_ACTIVE_GLOW;
import static net.glease.ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE_GLOW;
import static net.glease.ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE_STUCK;
import static net.glease.ggfab.BlockIcons.OVERLAY_FRONT_ADV_ASSLINE_STUCK_GLOW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import net.glease.ggfab.ConfigurationHandler;
import net.glease.ggfab.GGConstants;
import net.glease.ggfab.mui.ClickableTextWidget;
import net.glease.ggfab.util.OverclockHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
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

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DataAccess;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_MultiInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_AssemblyLineUtils;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Waila;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_InputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Input_ME;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/*
 * Dev note: 1. This multi will be an assline but with greater throughput. it will take one input every 2.
 */
public class MTE_AdvAssLine extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<MTE_AdvAssLine>
    implements ISurvivalConstructable {

    private static final ItemStack NOT_CHECKED = new ItemStack(Blocks.dirt);
    private static final String STRUCTURE_PIECE_FIRST = "first";
    private static final String STRUCTURE_PIECE_LATER = "later";
    private static final String STRUCTURE_PIECE_LAST = "last";
    public static final String TAG_KEY_CURRENT_STICK = "mCurrentStick";
    public static final String TAG_KEY_PROGRESS_TIMES = "mProgressTimeArray";
    private static final IStructureDefinition<MTE_AdvAssLine> STRUCTURE_DEFINITION = StructureDefinition
        .<MTE_AdvAssLine>builder()
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
        .addElement('G', ofBlock(GregTech_API.sBlockCasings3, 10)) // grate machine casing
        .addElement('l', ofBlock(GregTech_API.sBlockCasings2, 9)) // assembler machine casing
        .addElement('m', ofBlock(GregTech_API.sBlockCasings2, 5)) // assembling line casing
        .addElement(
            'g',
            ofChain(
                ofBlockUnlocalizedName("IC2", "blockAlloyGlass", 0, true),
                ofBlockUnlocalizedName("bartworks", "BW_GlasBlocks", 0, true),
                // warded glass
                ofBlockUnlocalizedName("Thaumcraft", "blockCosmeticOpaque", 2, false)))
        .addElement(
            'e',
            ofChain(
                Energy.or(ExoticEnergy)
                    .newAny(16, 1, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH),
                ofBlock(GregTech_API.sBlockCasings2, 0)))
        .addElement(
            'd',
            buildHatchAdder(MTE_AdvAssLine.class).atLeast(DataHatchElement.DataAccess)
                .dot(2)
                .casingIndex(42)
                .allowOnly(ForgeDirection.NORTH)
                .buildAndChain(GregTech_API.sBlockCasings3, 10))
        .addElement(
            'b',
            buildHatchAdder(MTE_AdvAssLine.class).atLeast(InputHatch, InputHatch, InputHatch, InputHatch, Maintenance)
                .casingIndex(16)
                .dot(3)
                .allowOnly(ForgeDirection.DOWN)
                .buildAndChain(
                    ofBlock(GregTech_API.sBlockCasings2, 0),
                    ofHatchAdder(MTE_AdvAssLine::addOutputToMachineList, 16, 4)))
        .addElement(
            'I',
            ofChain(
                // all blocks nearby use solid steel casing, so let's use the texture of that
                InputBus.newAny(16, 5, ForgeDirection.DOWN),
                ofHatchAdder(MTE_AdvAssLine::addOutputToMachineList, 16, 4)))
        .addElement('i', InputBus.newAny(16, 5, ForgeDirection.DOWN))
        .addElement('o', OutputBus.newAny(16, 4, ForgeDirection.DOWN))
        .build();
    private ItemStack currentStick;
    private GT_Recipe.GT_Recipe_AssemblyLine currentRecipe;
    private final Slice[] slices = IntStream.range(0, 16)
        .mapToObj(Slice::new)
        .toArray(Slice[]::new);
    private boolean processing;
    private long inputVoltage;
    // surely no one is using more EUt than this, no?
    private long inputEUt;
    private long baseEUt;
    private boolean stuck;

    private final List<GT_MetaTileEntity_Hatch_DataAccess> mDataAccessHatches = new ArrayList<>();
    private Map<GT_Utility.ItemId, ItemStack> curBatchItemsFromME;
    private Map<Fluid, FluidStack> curBatchFluidsFromME;
    private int currentInputLength;
    private String lastStopReason = "";
    private int currentRecipeParallel = 1;
    // Batch mode will increase parallel per slice to try to get as close as possible to this amount of ticks
    // per slice, but will never go over this amount.
    private static final int BATCH_MODE_DESIRED_TICKS_PER_SLICE = 128;

    public MTE_AdvAssLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTE_AdvAssLine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTE_AdvAssLine(mName);
    }

    public boolean addDataAccessToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DataAccess) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDataAccessHatches.add((GT_MetaTileEntity_Hatch_DataAccess) aMetaTileEntity);
        }
        return false;
    }

    private boolean checkMachine() {
        return checkMachine(true) || checkMachine(false);
    }

    private boolean checkMachine(boolean leftToRight) {
        clearHatches();
        if (!checkPiece(STRUCTURE_PIECE_FIRST, 0, 1, 0)) return false;
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
        int tLength = Math.min(stackSize.stackSize + 3, 16); // render 4 slices at minimal
        for (int i = 1; i < tLength; i++) {
            buildPiece(STRUCTURE_PIECE_LATER, stackSize, hintsOnly, -i, 1, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int build = survivialBuildPiece(STRUCTURE_PIECE_FIRST, stackSize, 0, 1, 0, elementBudget, env, false, true);
        if (build >= 0) return build;
        int tLength = Math.min(stackSize.stackSize + 3, 16); // render 4 slices at minimal
        for (int i = 1; i < tLength - 1; i++) {
            build = survivialBuildPiece(STRUCTURE_PIECE_LATER, stackSize, -i, 1, 0, elementBudget, env, false, true);
            if (build >= 0) return build;
        }
        return survivialBuildPiece(STRUCTURE_PIECE_LAST, stackSize, 1 - tLength, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        super.initDefaultModes(aNBT);
        // blockrenderer6343 seems to place the block in a weird way, let's catch that
        if (getBaseMetaTileEntity() != null && getBaseMetaTileEntity().isServerSide()) {
            UUID ownerUuid = getBaseMetaTileEntity().getOwnerUuid();
            if (ownerUuid == null) return;
            float factor = ConfigurationHandler.INSTANCE.getLaserOCPenaltyFactor();
            MinecraftServer server = MinecraftServer.getServer();
            // more blockrenderer6343 weirdness
            if (server == null) return;
            @SuppressWarnings("unchecked")
            List<EntityPlayerMP> l = server.getConfigurationManager().playerEntityList;
            for (EntityPlayerMP p : l) {
                if (p.getUniqueID()
                    .equals(ownerUuid)) {
                    for (int i = 0; i < 9; i++) {
                        // switch is stupid, but I have no better idea
                        Object[] args;
                        switch (i) {
                            case 7:
                                args = new Object[] { factor };
                                break;
                            case 8:
                                args = new Object[] { (int) (factor * 100) + 400,
                                    (int) ((4 + factor) * (4 + factor + factor) * 100), 4 + factor,
                                    4 + factor + factor };
                                break;
                            default:
                                args = new Object[0];
                        }
                        p.addChatMessage(new ChatComponentTranslation("ggfab.info.advassline." + i, args));
                    }
                }
            }
        }
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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Assembling Line")
            .addInfo("Controller block for the Advanced Assembling Line")
            .addInfo("Built exactly the same as standard Assembling Line")
            .addInfo("Place in world to get more info. It will be a lengthy read.")
            .addInfo("Assembling Line with item pipelining")
            .addInfo("All fluids are however consumed at start")
            .addInfo("Use voltage of worst energy hatch for overclocking")
            .addInfo("EU/t is (number of slices working) * (overclocked EU/t)")
            .addSeparator()
            .beginVariableStructureBlock(5, 16, 4, 4, 3, 3, false)
            .addStructureInfo("From Bottom to Top, Left to Right")
            .addStructureInfo(
                "Layer 1 - Solid Steel Machine Casing, Input Bus (last can be Output Bus), Solid Steel Machine Casing")
            .addStructureInfo(
                "Layer 2 - Borosilicate Glass(any)/Warded Glass/Reinforced Glass, Assembling Line Casing, Reinforced Glass")
            .addStructureInfo("Layer 3 - Grate Machine Casing, Assembler Machine Casing, Grate Machine Casing")
            .addStructureInfo("Layer 4 - Empty, Solid Steel Machine Casing, Empty")
            .addStructureInfo("Up to 16 repeating slices, each one allows for 1 more item in recipes")
            .addController("Either Grate on layer 3 of the first slice")
            .addEnergyHatch("Any layer 4 casing", 1)
            .addMaintenanceHatch("Any layer 1 casing", 3)
            .addInputBus("As specified on layer 1", 4, 5)
            .addInputHatch("Any layer 1 casing", 3)
            .addOutputBus("Replaces Input Bus on final slice or on any solid steel casing on layer 1", 4)
            .addOtherStructurePart("Data Access Hatch", "Optional, next to controller", 2)
            .toolTipFinisher(GGConstants.GGMARK);
        return tt;
    }

    private void setCurrentRecipe(ItemStack stick, GT_Recipe.GT_Recipe_AssemblyLine recipe) {
        currentRecipe = recipe;
        currentStick = stick;
        currentInputLength = recipe.mInputs.length;
    }

    private void clearCurrentRecipe() {
        currentRecipe = null;
        currentStick = null;
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
        if (getBaseMetaTileEntity().isActive() && GT_Utility.isStackValid(currentStick)) {
            aNBT.setTag(TAG_KEY_CURRENT_STICK, currentStick.writeToNBT(new NBTTagCompound()));
            aNBT.setInteger("mRecipeHash", currentRecipe.getPersistentHash());
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
        ItemStack loadedStack = null;
        GT_Recipe.GT_Recipe_AssemblyLine recipe = null;
        if (aNBT.hasKey(TAG_KEY_PROGRESS_TIMES, Constants.NBT.TAG_INT_ARRAY)) {
            int[] arr = aNBT.getIntArray(TAG_KEY_PROGRESS_TIMES);
            for (int i = 0; i < slices.length; i++) {
                if (i < arr.length) {
                    slices[i].progress = arr[i];
                    if (arr[i] == 0)
                        // this will be synced to client by first MTE packet to client
                        stuck = true;
                } else slices[i].reset();
            }
        }
        if (aNBT.hasKey(TAG_KEY_CURRENT_STICK, Constants.NBT.TAG_COMPOUND)) {
            loadedStack = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(TAG_KEY_CURRENT_STICK));
            GT_AssemblyLineUtils.LookupResult lookupResult = GT_AssemblyLineUtils
                .findAssemblyLineRecipeFromDataStick(loadedStack, false);
            switch (lookupResult.getType()) {
                case VALID_STACK_AND_VALID_HASH:
                    recipe = lookupResult.getRecipe();
                    stuck = aNBT.getBoolean("stuck");
                    inputVoltage = aNBT.getLong("inputV");
                    inputEUt = aNBT.getLong("inputEU");
                    baseEUt = aNBT.getLong("baseEU");
                    currentRecipeParallel = aNBT.getInteger("currentParallel");
                    if (inputVoltage <= 0 || inputEUt <= 0 || baseEUt >= 0) {
                        criticalStopMachine("ggfab.gui.advassline.shutdown.load.energy");
                        loadedStack = null;
                        recipe = null;
                    }
                    break;
                case VALID_STACK_AND_VALID_RECIPE:
                    // recipe is there, but it has been changed. to prevent issues, abort the current recipe
                    // TODO finish the last recipe instead of aborting
                default:
                    // recipe is gone. to prevent issues, abort the current recipe
                    criticalStopMachine("ggfab.gui.advassline.shutdown.load.recipe");
                    loadedStack = null;
                    break;
            }
        }
        if (loadedStack == null || recipe == null) clearCurrentRecipe();
        else setCurrentRecipe(loadedStack, recipe);
    }

    /**
     * roughly the same as {@link #criticalStopMachine()}, but does not attempt to send a halting sound if world is not
     * loaded. also supports setting a stop reason
     */
    private void criticalStopMachine(String reason) {
        int oMaxProgresstime = mMaxProgresstime;
        stopMachine();
        // don't do these at all if the machine wasn't working before anyway
        if (oMaxProgresstime > 0) {
            if (getBaseMetaTileEntity().getWorld() != null) sendSound(INTERRUPT_SOUND_INDEX);
            getBaseMetaTileEntity().setShutdownStatus(true);
            lastStopReason = reason;
        }
    }

    @Override
    public IStructureDefinition<MTE_AdvAssLine> getStructureDefinition() {
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
        if (checkMachine() && (mEnergyHatches.size() > 0 || mExoticEnergyHatches.size() > 0)) {
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

    private void recordEnergySupplier(GT_MetaTileEntity_Hatch hatch) {
        if (!hatch.isValid()) return;
        inputEUt += hatch.maxEUInput() * hatch.maxWorkingAmperesIn();
        inputVoltage = Math.min(inputVoltage, hatch.maxEUInput());
        if (inputEUt < 0) inputEUt = Long.MAX_VALUE;
    }

    @Override
    protected void startRecipeProcessing() {
        if (!processing) {
            super.startRecipeProcessing();
            curBatchItemsFromME = getStoredInputsFromME();
            curBatchFluidsFromME = getStoredFluidsFromME();
            processing = true;
        }
    }

    @Override
    protected void endRecipeProcessing() {
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
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        /*
         * SliceStatusWidget[] arr =
         * Arrays.stream(slices).map(SliceStatusWidget::new).toArray(SliceStatusWidget[]::new);
         * screenElements.widgets(arr); screenElements.widget(new FakeSyncWidget.IntegerSyncer(() -> currentInputLength,
         * l -> { currentInputLength = l; for (SliceStatusWidget w : arr) { w.updateText(); } }));
         */
        screenElements.widget(
            new TextWidget(Text.localised("ggfab.gui.advassline.shutdown")).setEnabled(this::hasAbnormalStopReason));
        screenElements.widget(
            new TextWidget().setTextSupplier(() -> Text.localised(lastStopReason))
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
        for (GT_MetaTileEntity_Hatch_DataAccess hatch_dataAccess : mDataAccessHatches) {
            hatch_dataAccess.setActive(true);
        }

        if (mInputBusses.size() < currentInputLength) {
            criticalStopMachine("ggfab.gui.advassline.shutdown.input_busses");
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
            if (!super.onRunningTick(aStack)) return false;
        }

        endRecipeProcessing();
        return true;
    }

    private ItemStack getInputBusContent(int index) {
        if (index < 0 || index >= mInputBusses.size()) return null;
        GT_MetaTileEntity_Hatch_InputBus inputBus = mInputBusses.get(index);
        if (!inputBus.isValid()) return null;
        if (inputBus instanceof GT_MetaTileEntity_Hatch_InputBus_ME meBus) {
            ItemStack item = meBus.getShadowItemStack(0);
            if (item == null) return null;
            GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(item);
            if (!curBatchItemsFromME.containsKey(id)) return null;
            return curBatchItemsFromME.get(id);
        }
        return inputBus.getStackInSlot(0);

    }

    private FluidStack getInputHatchContent(int index) {
        if (index < 0 || index >= mInputHatches.size()) return null;
        GT_MetaTileEntity_Hatch_Input inputHatch = mInputHatches.get(index);
        if (!inputHatch.isValid()) return null;
        if (inputHatch instanceof GT_MetaTileEntity_Hatch_Input_ME meHatch) {
            FluidStack fluid = meHatch.getShadowFluidStack(0);
            if (fluid == null) return null;
            if (!curBatchFluidsFromME.containsKey(fluid.getFluid())) return null;
            return curBatchFluidsFromME.get(fluid.getFluid());
        }
        if (inputHatch instanceof GT_MetaTileEntity_Hatch_MultiInput multiHatch) {
            return multiHatch.getFluid(0);
        }
        return inputHatch.getFillableStack();
    }

    private GT_Recipe.GT_Recipe_AssemblyLine findRecipe(ItemStack tDataStick) {
        GT_AssemblyLineUtils.LookupResult tLookupResult = GT_AssemblyLineUtils
            .findAssemblyLineRecipeFromDataStick(tDataStick, false);

        if (tLookupResult.getType() == GT_AssemblyLineUtils.LookupResultType.INVALID_STICK) return null;

        GT_Recipe.GT_Recipe_AssemblyLine tRecipe = tLookupResult.getRecipe();
        // Check if the recipe on the data stick is the current recipe for it's given output, if not we update it
        // and continue to next.
        if (tLookupResult.getType() != GT_AssemblyLineUtils.LookupResultType.VALID_STACK_AND_VALID_HASH) {
            tRecipe = GT_AssemblyLineUtils.processDataStick(tDataStick);
            if (tRecipe == null) {
                return null;
            }
        }

        // So here we check against the recipe found on the data stick.
        // If we run into missing buses/hatches or bad inputs, we go to the next data stick.
        // This check only happens if we have a valid up-to-date data stick.

        // Check item Inputs align. For this we do not need to consider batch mode parallels yet, this will be done
        // later on during recipe start.
        if (!hasAllItems(tRecipe, 1)) return null;

        // Check Fluid Inputs align. Again, do not consider parallels
        if (!hasAllFluids(tRecipe, 1)) return null;

        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Check overclock");
        }
        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Find available recipe");
        }
        return tRecipe;
    }

    private boolean hasAllItems(GT_Recipe.GT_Recipe_AssemblyLine tRecipe, int parallel) {
        int aItemCount = tRecipe.mInputs.length;
        if (mInputBusses.size() < aItemCount) return false;
        int[] itemConsumptions = GT_Recipe.GT_Recipe_AssemblyLine.getItemConsumptionAmountArray(mInputBusses, tRecipe);
        if (itemConsumptions == null || itemConsumptions.length == 0) {
            return false;
        }
        int maxParallel = (int) GT_Recipe.GT_Recipe_AssemblyLine
            .maxParallelCalculatedByInputItems(mInputBusses, parallel, itemConsumptions, curBatchItemsFromME);
        return maxParallel >= parallel;
    }

    private boolean hasAllFluids(GT_Recipe.GT_Recipe_AssemblyLine tRecipe, int parallel) {
        int aFluidCount = tRecipe.mFluidInputs.length;
        if (mInputHatches.size() < aFluidCount) return false;
        int maxParallel = (int) GT_Recipe.GT_Recipe_AssemblyLine
            .maxParallelCalculatedByInputFluids(mInputHatches, parallel, tRecipe.mFluidInputs, curBatchFluidsFromME);
        return maxParallel >= parallel;
    }

    /**
     * @param state using bitmask, 1 for IntegratedCircuit, 2 for DataStick, 4 for DataOrb
     */
    private boolean isCorrectDataItem(ItemStack aStack, int state) {
        if ((state & 1) != 0 && ItemList.Circuit_Integrated.isStackEqual(aStack, true, true)) return true;
        if ((state & 2) != 0 && ItemList.Tool_DataStick.isStackEqual(aStack, false, true)) return true;
        return (state & 4) != 0 && ItemList.Tool_DataOrb.isStackEqual(aStack, false, true);
    }

    /**
     * @param state using bitmask, 1 for IntegratedCircuit, 2 for DataStick, 4 for DataOrb
     */
    public ArrayList<ItemStack> getDataItems(int state) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        if (GT_Utility.isStackValid(mInventory[1]) && isCorrectDataItem(mInventory[1], state)) {
            rList.add(mInventory[1]);
        }
        for (GT_MetaTileEntity_Hatch_DataAccess tHatch : mDataAccessHatches) {
            if (tHatch.isValid()) {
                for (int i = 0; i < tHatch.getBaseMetaTileEntity()
                    .getSizeInventory(); i++) {
                    if (tHatch.getBaseMetaTileEntity()
                        .getStackInSlot(i) != null && isCorrectDataItem(
                            tHatch.getBaseMetaTileEntity()
                                .getStackInSlot(i),
                            state))
                        rList.add(
                            tHatch.getBaseMetaTileEntity()
                                .getStackInSlot(i));
                }
            }
        }
        return rList;
    }

    // this is only called when all slices have finished their work
    // and the first slice cannot find a input/fluid cannot be found
    // so we are safe to assume the old recipe no longer works
    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Start Adv ALine recipe check");
        }
        clearCurrentRecipe();
        CheckRecipeResult result = CheckRecipeResultRegistry.NO_DATA_STICKS;
        ArrayList<ItemStack> tDataStickList = getDataItems(2);
        if (tDataStickList.isEmpty()) {
            return result;
        }
        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Stick accepted, " + tDataStickList.size() + " Data Sticks found");
        }

        GT_Recipe.GT_Recipe_AssemblyLine recipe = null;

        for (ItemStack stack : tDataStickList) {
            recipe = findRecipe(stack);
            if (recipe == null) {
                result = CheckRecipeResultRegistry.NO_RECIPE;
                continue;
            }
            if (recipe.mEUt > inputVoltage) {
                result = CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                continue;
            }

            setCurrentRecipe(stack, recipe);
            // first overclock normally
            // we use the new oc calculator instead
            // calculateOverclockedNessMulti from super class has a mysterious 5% cable loss thing at the moment
            // of writing
            GT_OverclockCalculator ocCalc = new GT_OverclockCalculator().setRecipeEUt(currentRecipe.mEUt)
                .setDuration(Math.max(recipe.mDuration / recipe.mInputs.length, 1))
                .setEUt(inputVoltage)
                .calculate();
            // since we already checked mEUt <= inputVoltage, no need to check if recipe is too OP
            lEUt = ocCalc.getConsumption();
            mMaxProgresstime = ocCalc.getDuration();
            // then laser overclock if needed
            if (!mExoticEnergyHatches.isEmpty()) {
                OverclockHelper.OverclockOutput laserOverclock = OverclockHelper.laserOverclock(
                    lEUt,
                    mMaxProgresstime,
                    inputEUt / recipe.mInputs.length,
                    ConfigurationHandler.INSTANCE.getLaserOCPenaltyFactor());
                if (laserOverclock != null) {
                    lEUt = laserOverclock.getEUt();
                    mMaxProgresstime = laserOverclock.getDuration();
                }
            }
            // Save this for batch mode parallel calculations
            int timePerSlice = mMaxProgresstime;
            // correct the recipe duration
            mMaxProgresstime *= recipe.mInputs.length;

            // Finally apply batch mode parallels if possible.
            // For this we need to verify the first item slot and all fluids slots have enough resources
            // to execute parallels.
            // Note that we skip this entirely if the time for each slice is more than
            // BATCH_MODE_DESIRED_TICKS_PER_SLICE ticks, since in this case the amount of batches will always be 1
            if (super.isBatchModeEnabled() && timePerSlice < BATCH_MODE_DESIRED_TICKS_PER_SLICE) {
                // Calculate parallel based on time per slice, and the amount of fluid in the first fluid slot.
                // We use fluid, since this way players can limit parallel by controlling how much fluid
                // ends up in each AAL. This way, batch mode will not slow down setups where multiple AAL
                // are connected to the same set of input items. Note that this will still suffer from the same
                // issue if using stocking hatch, but in this case increasing pattern size can help.

                // Note that every assline recipe has a fluid ingredient.
                FluidStack firstFluidSlot = getInputHatchContent(0);
                if (firstFluidSlot == null) {
                    result = CheckRecipeResultRegistry.INTERNAL_ERROR;
                    break;
                }
                int recipesAvailable = Math.floorDiv(firstFluidSlot.amount, recipe.mFluidInputs[0].amount);
                // Divide recipes available by the amount of slices in the recipe. This will prevent the AAL from
                // batching instead of parallelizing, which would make it effectively slower.
                recipesAvailable = Math.floorDiv(recipesAvailable, recipe.mInputs.length);
                // Sanity check to avoid this being zero when there is only one recipe available.
                recipesAvailable = Math.max(recipesAvailable, 1);
                int desiredBatches = Math.floorDiv(BATCH_MODE_DESIRED_TICKS_PER_SLICE, timePerSlice);
                // Limit the amount of parallel to both the amount of recipes available and the maximum number
                // of batches we want to run. The latter is done to prevent batch mode from ever going above
                // BATCH_MODE_DESIRED_TICKS_PER_SLICE ticks per slice (see also where it is defined above).
                int parallel = Math.min(recipesAvailable, desiredBatches);
                if (hasAllFluids(recipe, parallel) && hasAllItems(recipe, parallel)) {
                    this.currentRecipeParallel = parallel;
                    // Update recipe duration with final batch mode multiplier
                    mMaxProgresstime *= this.currentRecipeParallel;
                }
            }
            result = CheckRecipeResultRegistry.SUCCESSFUL;
            break;
        }
        if (!result.wasSuccessful()) {
            clearCurrentRecipe();
            return result;
        }
        if (recipe == null || !slices[0].start() || currentRecipeParallel <= 0) {
            clearCurrentRecipe();
            // something very very wrong...
            return CheckRecipeResultRegistry.INTERNAL_ERROR;
        }

        if (GT_Values.D1) {
            GT_FML_LOGGER.info("All checked start consuming inputs");
        }
        drainAllFluids(recipe, this.currentRecipeParallel);

        // Apply parallel
        mOutputItems = new ItemStack[] { recipe.mOutput.copy() };
        mOutputItems[0].stackSize *= this.currentRecipeParallel;

        if (this.lEUt > 0) {
            this.lEUt = -this.lEUt;
        }
        baseEUt = lEUt;
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        if (GT_Values.D1) {
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
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
    protected boolean supportsSlotAutomation(int aSlot) {
        return aSlot == getControllerSlotIndex();
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        String machineProgressString = GT_Waila.getMachineProgressString(
            tag.getBoolean("isActive"),
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
    private void drainAllFluids(GT_Recipe.GT_Recipe_AssemblyLine recipe, int parallel) {
        GT_Recipe.GT_Recipe_AssemblyLine
            .consumeInputFluids(mInputHatches, parallel, recipe.mFluidInputs, curBatchFluidsFromME);
        for (GT_MetaTileEntity_Hatch_Input tHatch : filterValidMTEs(mInputHatches)) tHatch.updateSlots();
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
        float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, "Batch mode enabled");
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, "Batch mode disabled");
            }
        }
        return true;
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
                    if (addOutput(output) || !voidingMode.protectItem) reset();
                    else stuck = true;
                } else {
                    if (slices[id + 1].start()) reset();
                    else stuck = true;
                }
            }
        }

        public boolean start() {
            if (progress >= 0) return false;
            startRecipeProcessing();
            ItemStack stack = getInputBusContent(id);
            if (stack == null) return false;
            int size = GT_Recipe.GT_Recipe_AssemblyLine
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

    private enum DataHatchElement implements IHatchElement<MTE_AdvAssLine> {

        DataAccess;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(GT_MetaTileEntity_Hatch_DataAccess.class);
        }

        @Override
        public IGT_HatchAdder<MTE_AdvAssLine> adder() {
            return MTE_AdvAssLine::addDataAccessToMachineList;
        }

        @Override
        public long count(MTE_AdvAssLine t) {
            return t.mDataAccessHatches.size();
        }
    }
}
