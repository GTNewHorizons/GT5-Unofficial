package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.widgets.LockedWhileActiveButton;
import gregtech.api.interfaces.IChunkLoader;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

public abstract class MTEDrillerBase extends MTEEnhancedMultiBlockBase<MTEDrillerBase>
    implements IChunkLoader, ISurvivalConstructable {

    private static final ItemStack MINING_PIPE = GTModHandler.getIC2Item("miningPipe", 0);
    private static final ItemStack MINING_PIPE_TIP = GTModHandler.getIC2Item("miningPipeTip", 0);
    private static final Block MINING_PIPE_BLOCK = GTUtility.getBlockFromStack(MINING_PIPE);
    private static final Block MINING_PIPE_TIP_BLOCK = GTUtility.getBlockFromStack(MINING_PIPE_TIP);
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final ClassValue<IStructureDefinition<MTEDrillerBase>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<MTEDrillerBase> computeValue(Class<?> type) {
            return StructureDefinition.<MTEDrillerBase>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] { { "   ", " f ", "   " }, { "   ", " f ", "   " }, { "   ", " f ", "   " },
                            { " f ", "fcf", " f " }, { " f ", "fcf", " f " }, { " f ", "fcf", " f " },
                            { "b~b", "bbb", "bbb" }, }))
                .addElement('f', lazy(t -> ofFrame(t.getFrameMaterial())))
                .addElement(
                    'c',
                    lazy(
                        t -> ofBlock(
                            t.getCasingBlockItem()
                                .getBlock(),
                            t.getCasingBlockItem()
                                .get(0)
                                .getItemDamage())))
                .addElement(
                    'b',
                    lazy(
                        t -> buildHatchAdder(MTEDrillerBase.class).atLeastList(t.getAllowedHatches())
                            .casingIndex(t.casingTextureIndex)
                            .hint(1)
                            .buildAndChain(
                                t.getCasingBlockItem()
                                    .getBlock(),
                                t.getCasingBlockItem()
                                    .get(0)
                                    .getItemDamage())))
                .build();
        }
    };

    protected int casingTextureIndex;
    protected boolean isPickingPipes;
    private int xDrill, yDrill, zDrill, xPipe, zPipe, yHead;
    protected WorkState workState = WorkState.DOWNWARD;
    protected boolean mChunkLoadingEnabled = true;
    protected ChunkCoordIntPair mCurrentChunk = null;
    protected boolean mWorkChunkNeedsReload = true;

    /** Stores default result messages for success/failures of each work state. */
    private final Map<ResultRegistryKey, CheckRecipeResult> resultRegistry = new HashMap<>();

    /** Allows inheritors to supply custom runtime failure messages. */
    private CheckRecipeResult runtimeFailure = null;
    private CheckRecipeResult lastRuntimeFailure = null;

    /** Allows inheritors to supply custom shutdown failure messages. */
    private @NotNull String shutdownReason = "";

    /** Allows inheritors to suppress wiping the last error if the machine is forcibly turned off. */
    protected boolean suppressErrorWipe = false;

    public MTEDrillerBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        initFields();
    }

    public MTEDrillerBase(String aName) {
        super(aName);
        initFields();
    }

    private void initFields() {
        casingTextureIndex = getCasingTextureIndex();
        workState = WorkState.DOWNWARD;
        addOperatingMessages();
    }

    protected int getXDrill() {
        return xDrill;
    }

    protected int getZDrill() {
        return zDrill;
    }

    protected int getYHead() {
        return yHead;
    }

    protected final void setWorkState(int ordinal) {
        this.workState = WorkState.fromOrdinal(ordinal);
    }

    protected void addOperatingMessages() {
        // Inheritors can overwrite these to add custom operating messages.
        addResultMessage(WorkState.DOWNWARD, true, "deploying_pipe");
        addResultMessage(WorkState.DOWNWARD, false, "extracting_pipe");
        addResultMessage(WorkState.AT_BOTTOM, true, "drilling");
        addResultMessage(WorkState.AT_BOTTOM, false, "no_mining_pipe");
        addResultMessage(WorkState.UPWARD, true, "retracting_pipe");
        addResultMessage(WorkState.UPWARD, false, "drill_generic_finished");
        addResultMessage(WorkState.ABORT, true, "retracting_pipe");
        addResultMessage(WorkState.ABORT, false, "drill_retract_pipes_finished");
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { getCasingTextureForId(casingTextureIndex), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ORE_DRILL_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_DRILL_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { getCasingTextureForId(casingTextureIndex), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ORE_DRILL)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_DRILL_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { getCasingTextureForId(casingTextureIndex) };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("workState", workState.ordinal());
        aNBT.setBoolean("chunkLoadingEnabled", mChunkLoadingEnabled);
        aNBT.setBoolean("isChunkloading", mCurrentChunk != null);
        if (mCurrentChunk != null) {
            aNBT.setInteger("loadedChunkXPos", mCurrentChunk.chunkXPos);
            aNBT.setInteger("loadedChunkZPos", mCurrentChunk.chunkZPos);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.setWorkState(aNBT.getInteger("workState"));
        if (aNBT.hasKey("isPickingPipes"))
            workState = aNBT.getBoolean("isPickingPipes") ? WorkState.UPWARD : WorkState.DOWNWARD;
        if (aNBT.hasKey("chunkLoadingEnabled")) mChunkLoadingEnabled = aNBT.getBoolean("chunkLoadingEnabled");
        if (aNBT.getBoolean("isChunkloading")) {
            mCurrentChunk = new ChunkCoordIntPair(
                aNBT.getInteger("loadedChunkXPos"),
                aNBT.getInteger("loadedChunkZPos"));
        }
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer entityPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            mChunkLoadingEnabled = !mChunkLoadingEnabled;
            GTUtility.sendChatToPlayer(
                entityPlayer,
                mChunkLoadingEnabled ? GTUtility.trans("502", "Mining chunk loading enabled")
                    : GTUtility.trans("503", "Mining chunk loading disabled"));
            return true;
        }
        return super.onSolderingToolRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public void onRemoval() {
        if (mChunkLoadingEnabled) GTChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        super.onRemoval();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && mCurrentChunk != null
            && !mWorkChunkNeedsReload
            && !aBaseMetaTileEntity.isAllowedToWork()) {
            // if machine has stopped, stop chunkloading
            GTChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
            mWorkChunkNeedsReload = true;
        }
    }

    protected boolean tryPickPipe() {
        if (yHead == yDrill) return isPickingPipes = false;
        if (tryOutputPipe()) {
            if (checkBlockAndMeta(xPipe, yHead + 1, zPipe, MINING_PIPE_BLOCK, WILDCARD))
                getBaseMetaTileEntity().getWorld()
                    .setBlock(xPipe, yHead + 1, zPipe, MINING_PIPE_TIP_BLOCK);
            getBaseMetaTileEntity().getWorld()
                .setBlockToAir(xPipe, yHead, zPipe);
            return isPickingPipes = true;
        }
        return isPickingPipes = false;
    }

    protected enum PipeActionResult {
        SUCCESS,
        INVALID_BLOCK,
        NO_PIPE,
        CANCELED
    }

    protected PipeActionResult tryLowerPipeState() {
        return tryLowerPipeState(false);
    }

    protected PipeActionResult tryLowerPipeState(boolean isSimulating) {
        if (!hasMiningPipes()) {
            return PipeActionResult.NO_PIPE;
        }
        boolean canEraseBlock = false;
        final IGregTechTileEntity aBaseTile = getBaseMetaTileEntity();
        if (yHead > 0 && GTUtility.getBlockHardnessAt(aBaseTile.getWorld(), xPipe, yHead - 1, zPipe) >= 0) {
            if (GTUtility.eraseBlockByFakePlayer(getFakePlayer(aBaseTile), xPipe, yHead - 1, zPipe, true)) {
                canEraseBlock = true;
            } else {
                return PipeActionResult.CANCELED;
            }
        }
        if (!canEraseBlock) {
            return PipeActionResult.INVALID_BLOCK;
        }

        final Block b = getBaseMetaTileEntity().getBlock(xPipe, yHead - 1, zPipe);
        if (b != MINING_PIPE_TIP_BLOCK && !GTUtility.setBlockByFakePlayer(
            getFakePlayer(getBaseMetaTileEntity()),
            xPipe,
            yHead - 1,
            zPipe,
            MINING_PIPE_TIP_BLOCK,
            0,
            isSimulating)) {
            return PipeActionResult.CANCELED;
        }
        if (!isSimulating) {
            if (yHead != yDrill) {
                getBaseMetaTileEntity().getWorld()
                    .setBlock(xPipe, yHead, zPipe, MINING_PIPE_BLOCK);
            }
            if (b != MINING_PIPE_BLOCK && b != MINING_PIPE_TIP_BLOCK) {
                getBaseMetaTileEntity().decrStackSize(1, 1);
            }
        }
        return PipeActionResult.SUCCESS;
    }

    private void putMiningPipesFromInputsInController() {
        int maxPipes = MINING_PIPE.getMaxStackSize();
        if (hasMiningPipes(maxPipes)) return;

        ItemStack pipes = getStackInSlot(1);
        if (pipes != null && !pipes.isItemEqual(MINING_PIPE)) return;
        for (ItemStack storedItem : getStoredInputs()) {
            if (!storedItem.isItemEqual(MINING_PIPE)) continue;

            if (pipes == null) {
                setInventorySlotContents(1, GTUtility.copyOrNull(MINING_PIPE));
                pipes = getStackInSlot(1);
            }

            if (pipes.stackSize == maxPipes) break;

            int needPipes = maxPipes - pipes.stackSize;
            int transferPipes = Math.min(storedItem.stackSize, needPipes);

            pipes.stackSize += transferPipes;
            storedItem.stackSize -= transferPipes;
        }
        updateSlots();
    }

    private boolean tryOutputPipe() {
        if (!getBaseMetaTileEntity().addStackToSlot(1, GTUtility.copyAmount(1, MINING_PIPE)))
            mOutputItems = new ItemStack[] { GTUtility.copyAmount(1, MINING_PIPE) };
        return true;
    }

    protected boolean reachingVoidOrBedrock() {
        return yHead <= 0 || checkBlockAndMeta(xPipe, yHead - 1, zPipe, Blocks.bedrock, WILDCARD);
    }

    private boolean hasMiningPipes() {
        return hasMiningPipes(1);
    }

    private boolean hasMiningPipes(int minCount) {
        ItemStack stack = getStackInSlot(1);
        return stack != null && stack.stackSize > minCount - 1 && stack.isItemEqual(MINING_PIPE);
    }

    private boolean isEnergyEnough() {
        long requiredEnergy = 512 + getMaxInputVoltage() * 4;
        for (MTEHatchEnergy energyHatch : mEnergyHatches) {
            requiredEnergy -= energyHatch.getEUVar();
            if (requiredEnergy <= 0) return true;
        }
        return false;
    }

    protected boolean workingDownward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        switch (tryLowerPipeState()) {
            case NO_PIPE -> {
                mMaxProgresstime = 0;
                setRuntimeFailureReason(CheckRecipeResultRegistry.MISSING_MINING_PIPE);
                return false;
            }
            case CANCELED -> {
                workState = WorkState.UPWARD;
                return true;
            }
            case INVALID_BLOCK -> {
                workState = WorkState.AT_BOTTOM;
                return true;
            }
            default -> {
                return true;
            }
        }
    }

    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        if (tryLowerPipeState(true) == PipeActionResult.SUCCESS) {
            workState = WorkState.DOWNWARD;
            return true;
        }
        workState = WorkState.UPWARD;
        return true;
    }

    protected boolean workingUpward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        if (tryPickPipe()) {
            return true;
        } else {
            workState = WorkState.DOWNWARD;
            stopMachine(ShutDownReasonRegistry.NONE);
            return false;
        }
    }

    /** Called once when the abort button is clicked. Use to perform any needed cleanup (e.g. unloading chunks.) */
    protected void onAbort() {}

    protected void abortDrilling() {
        if (workState != WorkState.ABORT) {
            workState = WorkState.ABORT;
            onAbort();
            setShutdownReason("");

            if (!isAllowedToWork()) {
                enableWorking();
            }
        }
    }

    // This is a distinct state from workingUpward, because some inheritors (like concrete backfiller) operate
    // exclusively on the workingUpward phase. It also allows for more distinct status messages.
    protected boolean workingToAbortOperation(@NotNull ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe,
        int zPipe, int yHead, int oldYHead) {
        if (tryPickPipe()) {
            return true;
        } else {
            workState = WorkState.DOWNWARD;
            stopMachine(ShutDownReasonRegistry.NONE);
            return false;
        }
    }

    @Override
    public void enableWorking() {
        super.enableWorking();
        shutdownReason = "";
    }

    @Override
    public void onDisableWorking() {
        if (suppressErrorWipe) {
            suppressErrorWipe = false;
        } else {
            super.onDisableWorking();
        }
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ItemStack controllerStack = getControllerSlot();
        // Public pipe actions
        setElectricityStats();
        int oldYHead = yHead;
        if (!checkPipesAndSetYHead()) {
            stopMachine(ShutDownReasonRegistry.NONE);
            return SimpleCheckRecipeResult.ofFailure("no_mining_pipe");
        } else if (!isEnergyEnough()) {
            stopMachine(ShutDownReasonRegistry.NONE);
            return SimpleCheckRecipeResult.ofFailure("not_enough_energy");
        }
        putMiningPipesFromInputsInController();

        final boolean wasSuccessful;
        switch (workState) {
            case DOWNWARD -> wasSuccessful = workingDownward(
                controllerStack,
                xDrill,
                yDrill,
                zDrill,
                xPipe,
                zPipe,
                yHead,
                oldYHead);
            case AT_BOTTOM -> wasSuccessful = workingAtBottom(
                controllerStack,
                xDrill,
                yDrill,
                zDrill,
                xPipe,
                zPipe,
                yHead,
                oldYHead);
            case UPWARD -> wasSuccessful = workingUpward(
                controllerStack,
                xDrill,
                yDrill,
                zDrill,
                xPipe,
                zPipe,
                yHead,
                oldYHead);
            case ABORT -> wasSuccessful = workingToAbortOperation(
                controllerStack,
                xDrill,
                yDrill,
                zDrill,
                xPipe,
                zPipe,
                yHead,
                oldYHead);
            default -> wasSuccessful = false;
        }

        if (runtimeFailure == null) {
            if (wasSuccessful) {
                lastRuntimeFailure = null;
            }

            return resultRegistry.getOrDefault(
                new ResultRegistryKey(workState, wasSuccessful),
                SimpleCheckRecipeResult.ofFailure("no_mining_pipe"));
        } else {
            final CheckRecipeResult result;
            result = lastRuntimeFailure = runtimeFailure;
            runtimeFailure = null;
            return result;
        }
    }

    /**
     * Allow drills to set a specific failure reason specific to their situation. E.g.: out of drilling fluid. Should be
     * used when the machine doesn't turn off due to the failure.
     *
     * @param newFailureReason A new failure reason
     */
    protected void setRuntimeFailureReason(@NotNull CheckRecipeResult newFailureReason) {
        runtimeFailure = newFailureReason;
    }

    /**
     * Gets a reason for why the drill turned off, for use in UIs and such.
     *
     * @return A reason, or empty if the machine is active or there is no message set yet.
     */
    @NotNull
    protected Optional<String> getFailureReason() {
        if (getBaseMetaTileEntity().isActive()) {
            return Optional.empty();
        }

        if (!shutdownReason.isEmpty()) {
            return Optional.of(shutdownReason);
        }

        return Optional.ofNullable(lastRuntimeFailure)
            .map(CheckRecipeResult::getDisplayString);
    }

    /**
     * Sets a line in the UI to explain why the drill shut down. E.g.: operation finished. Should be used when the
     * machine has been turned off due to an operating issue or completion.
     *
     * @param newReason The reason for the machine shutdown
     */
    protected void setShutdownReason(@NotNull String newReason) {
        shutdownReason = newReason;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return false;
    }

    @Override
    public final IStructureDefinition<MTEDrillerBase> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        updateCoordinates();
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 6, 0) && checkHatches()
            && GTUtility.getTier(getMaxInputVoltage()) >= getMinTier()
            && mMaintenanceHatches.size() == 1;
    }

    private void updateCoordinates() {
        xDrill = getBaseMetaTileEntity().getXCoord();
        yDrill = getBaseMetaTileEntity().getYCoord();
        zDrill = getBaseMetaTileEntity().getZCoord();
        ForgeDirection back = getBaseMetaTileEntity().getBackFacing();
        xPipe = xDrill + back.offsetX;
        zPipe = zDrill + back.offsetZ;
    }

    private boolean checkPipesAndSetYHead() {
        yHead = yDrill - 1;
        while (checkBlockAndMeta(xPipe, yHead, zPipe, MINING_PIPE_BLOCK, WILDCARD)) yHead--; // skip pipes
        // is pipe tip OR is controller layer
        if (checkBlockAndMeta(xPipe, yHead, zPipe, MINING_PIPE_TIP_BLOCK, WILDCARD) || ++yHead == yDrill) return true;
        // pipe column is broken - try fix
        getBaseMetaTileEntity().getWorld()
            .setBlock(xPipe, yHead, zPipe, MINING_PIPE_TIP_BLOCK);
        return true;
    }

    private boolean checkBlockAndMeta(int x, int y, int z, Block block, int meta) {
        return (meta == WILDCARD || getBaseMetaTileEntity().getMetaID(x, y, z) == meta)
            && getBaseMetaTileEntity().getBlock(x, y, z) == block;
    }

    private FakePlayer mFakePlayer = null;

    protected FakePlayer getFakePlayer(IGregTechTileEntity aBaseTile) {
        if (mFakePlayer == null) mFakePlayer = GTUtility.getFakePlayer(aBaseTile);
        mFakePlayer.setWorld(aBaseTile.getWorld());
        mFakePlayer.setPosition(aBaseTile.getXCoord(), aBaseTile.getYCoord(), aBaseTile.getZCoord());
        return mFakePlayer;
    }

    protected abstract ItemList getCasingBlockItem();

    protected abstract Materials getFrameMaterial();

    protected abstract int getCasingTextureIndex();

    protected abstract int getMinTier();

    protected abstract boolean checkHatches();

    protected abstract void setElectricityStats();

    public int calculateMaxProgressTime(int tier) {
        return calculateMaxProgressTime(tier, false);
    }

    public abstract int calculateMaxProgressTime(int tier, boolean simulateWorking);

    public int getTotalConfigValue() {
        int config = 0;
        ArrayList<ItemStack> tCircuitList = getDataItems(1);
        for (ItemStack tCircuit : tCircuitList) config += tCircuit.getItemDamage();
        return config;
    }

    public ArrayList<MTEHatchDataAccess> mDataAccessHatches = new ArrayList<>();

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
        if (GTUtility.isStackValid(mInventory[1]) && isCorrectDataItem(mInventory[1], state)) {
            rList.add(mInventory[1]);
        }
        for (MTEHatchDataAccess tHatch : validMTEList(mDataAccessHatches)) {
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
        return rList;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addDataAccessToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addDataAccessToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchDataAccess) {
            ((MTEHatch) aMetaTileEntity).updateTexture((byte) aBaseCasingIndex);
            return mDataAccessHatches.add((MTEHatchDataAccess) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public ChunkCoordIntPair getActiveChunk() {
        return mCurrentChunk;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 6, 0, elementBudget, env, false, true);
    }

    @Override
    protected boolean useMui2() {
        return false;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements.widget(
            TextWidget.dynamicString(() -> shutdownReason)
                .setSynced(false)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(widget -> !(getBaseMetaTileEntity().isActive() || shutdownReason.isEmpty())))
            .widget(new FakeSyncWidget.StringSyncer(() -> shutdownReason, newString -> shutdownReason = newString));
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    /**
     * Adds additional buttons to the main button row. You do not need to set the position.
     *
     * @param builder      Only use to attach SyncWidgets.
     * @param buildContext Context for things like the player.
     */
    protected List<ButtonWidget> getAdditionalButtons(ModularWindow.Builder builder, UIBuildContext buildContext) {
        return ImmutableList.of();
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        final int BUTTON_Y_LEVEL = 91;

        builder.widget(
            new LockedWhileActiveButton(this.getBaseMetaTileEntity(), builder)
                .setOnClick((clickData, widget) -> mChunkLoadingEnabled = !mChunkLoadingEnabled)
                .setPlayClickSound(true)
                .setBackground(() -> {
                    if (mChunkLoadingEnabled) {
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                            GTUITextures.OVERLAY_BUTTON_CHUNK_LOADING };
                    }
                    return new IDrawable[] { GTUITextures.BUTTON_STANDARD,
                        GTUITextures.OVERLAY_BUTTON_CHUNK_LOADING_OFF };
                })
                .attachSyncer(
                    new FakeSyncWidget.BooleanSyncer(
                        () -> mChunkLoadingEnabled,
                        newBoolean -> mChunkLoadingEnabled = newBoolean),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange())
                .dynamicTooltip(
                    () -> ImmutableList.of(
                        StatCollector.translateToLocal(
                            mChunkLoadingEnabled ? "GT5U.gui.button.chunk_loading_on"
                                : "GT5U.gui.button.chunk_loading_off")))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(new Pos2d(80, BUTTON_Y_LEVEL))
                .setSize(16, 16))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> abortDrilling())
                    .setPlayClickSound(true)
                    .setBackground(() -> {
                        if (workState == WorkState.ABORT) {
                            return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                                GTUITextures.OVERLAY_BUTTON_RETRACT_PIPE, GTUITextures.OVERLAY_BUTTON_LOCKED };
                        }
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD,
                            GTUITextures.OVERLAY_BUTTON_RETRACT_PIPE };
                    })
                    .attachSyncer(
                        new FakeSyncWidget.IntegerSyncer(() -> workState.ordinal(), this::setWorkState),
                        builder,
                        (widget, integer) -> widget.notifyTooltipChange())
                    .dynamicTooltip(
                        () -> ImmutableList.of(
                            StatCollector.translateToLocalFormatted(
                                workState == WorkState.ABORT ? "GT5U.gui.button.drill_retract_pipes_active"
                                    : "GT5U.gui.button.drill_retract_pipes")))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY)
                    .setPos(new Pos2d(174, 112))
                    .setSize(16, 16));

        int left = 98;
        for (ButtonWidget button : getAdditionalButtons(builder, buildContext)) {
            button.setPos(new Pos2d(left, BUTTON_Y_LEVEL))
                .setSize(16, 16);
            builder.widget(button);
            left += 18;
        }
    }

    protected List<IHatchElement<? super MTEDrillerBase>> getAllowedHatches() {
        return ImmutableList.of(
            InputHatch,
            OutputHatch,
            InputBus,
            OutputBus,
            Muffler,
            Maintenance,
            Energy,
            DataHatchElement.DataAccess);
    }

    protected enum DataHatchElement implements IHatchElement<MTEDrillerBase> {

        DataAccess;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchDataAccess.class);
        }

        @Override
        public IGTHatchAdder<MTEDrillerBase> adder() {
            return MTEDrillerBase::addDataAccessToMachineList;
        }

        @Override
        public long count(MTEDrillerBase t) {
            return t.mDataAccessHatches.size();
        }
    }

    protected enum WorkState {

        DOWNWARD,
        AT_BOTTOM,
        UPWARD,
        ABORT;

        private static final WorkState[] VALUES = values();

        public static WorkState fromOrdinal(int ordinal) {
            if (0 <= ordinal && ordinal < VALUES.length) {
                return VALUES[ordinal];
            }
            return DOWNWARD;
        }
    }

    /**
     * Sets or overrides the {@link CheckRecipeResult} for a given work state
     *
     * @param state  A work state.
     * @param result A previously registered recipe result.
     */
    protected void addResultMessage(final WorkState state, @NotNull final CheckRecipeResult result) {
        resultRegistry.put(new ResultRegistryKey(state, result.wasSuccessful()), result);
    }

    /**
     * Sets or overrides the {@link CheckRecipeResult} for a given work state and operation success type.
     *
     * @param state         A work state.
     * @param wasSuccessful Whether the operation was successful.
     * @param resultKey     An I18N key for the message.
     */
    protected void addResultMessage(final WorkState state, final boolean wasSuccessful,
        @NotNull final String resultKey) {
        addResultMessage(
            state,
            wasSuccessful ? SimpleCheckRecipeResult.ofSuccess(resultKey)
                : SimpleCheckRecipeResult.ofFailure(resultKey));
    }

    @SuppressWarnings("ClassCanBeRecord")
    private final static class ResultRegistryKey {

        private final int state;
        private final boolean successful;

        public ResultRegistryKey(final WorkState state, final boolean successful) {
            this.state = state.ordinal();
            this.successful = successful;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ResultRegistryKey other)) {
                return false;
            }

            return (state == other.state && successful == other.successful);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, successful);
        }
    }
}
