package gregtech.api.multitileentity.multiblock.base;

import static gregtech.api.util.GTUtility.moveMultipleItemStacks;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;
import static mcp.mobius.waila.api.SpecialChars.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.api.enums.GTValues.NBT;
import gregtech.api.enums.InventoryType;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.IDescribable;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.logic.ControllerFluidLogic;
import gregtech.api.logic.ControllerItemLogic;
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.logic.MuTEProcessingLogic;
import gregtech.api.logic.PowerLogic;
import gregtech.api.multitileentity.WeakTargetRef;
import gregtech.api.multitileentity.enums.MultiTileCasingPurpose;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.multitileentity.machine.MultiTileBasicMachine;
import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;
import gregtech.api.net.GTPacketMultiTileEntity;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.MultiblockTooltipBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Multi Tile Entities - or MuTEs - don't have dedicated hatches, but their casings can become hatches.
 */
public abstract class Controller<C extends Controller<C, P>, P extends MuTEProcessingLogic<P>> extends
    MultiTileBasicMachine<P> implements IAlignment, IMultiBlockController, IDescribable, ISurvivalConstructable {

    public static final String ALL_INVENTORIES_NAME = "all";
    protected static final int AUTO_OUTPUT_FREQUENCY_TICK = 20;

    private static final Map<Integer, MultiblockTooltipBuilder> tooltip = new ConcurrentHashMap<>();
    private final List<WeakTargetRef<UpgradeCasing>> upgradeCasings = new ArrayList<>();
    private final List<WeakTargetRef<FunctionalCasing>> functionalCasings = new ArrayList<>();
    protected BuildState buildState = new BuildState();

    private boolean structureOkay = false, structureChanged = false;
    private ExtendedFacing extendedFacing = ExtendedFacing.DEFAULT;
    private IAlignmentLimits limits = getInitialAlignmentLimits();
    protected boolean separateInputs = getDefaultInputSeparationMode();
    protected VoidingMode voidingMode = getDefaultVoidingMode();
    protected boolean batchMode = getDefaultBatchMode();
    protected boolean recipeLock = getDefaultRecipeLockingMode();
    protected boolean shouldSort = false;
    /** If this is set to true, the machine will get default WAILA behavior */
    protected boolean isSimpleMachine = true;

    protected boolean isCleanroom = false;
    protected ControllerItemLogic controllerItemInput = new ControllerItemLogic();
    protected ControllerItemLogic controllerItemOutput = new ControllerItemLogic();
    protected ControllerFluidLogic controllerFluidInput = new ControllerFluidLogic();
    protected ControllerFluidLogic controllerFluidOutput = new ControllerFluidLogic();

    // A list of sides
    // Each side has a list of parts that have a cover that need to be ticked
    protected List<List<WeakTargetRef<IMultiBlockPart>>> registeredCoveredParts = Arrays.asList(
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());

    // A list for each purpose that a casing can register to, to be ticked
    protected List<List<WeakTargetRef<IMultiBlockPart>>> registeredTickableParts = new ArrayList<>();

    public Controller() {
        for (int i = 0; i < MultiTileCasingPurpose.values().length; i++) {
            registeredTickableParts.add(new ArrayList<>());
        }
    }

    /** Registry ID of the required casing */
    public abstract short getCasingRegistryID();

    /** Meta ID of the required casing */
    public abstract int getCasingMeta();

    /**
     * Create the tooltip for this multi block controller.
     */
    protected abstract MultiblockTooltipBuilder createTooltip();

    /**
     * @return The starting offset for the structure builder
     */
    public abstract Vec3Impl getStartingStructureOffset();

    /**
     * Due to limitation of Java type system, you might need to do an unchecked cast. HOWEVER, the returned
     * IStructureDefinition is expected to be evaluated against current instance only, and should not be used against
     * other instances, even for those of the same class.
     */
    @Override
    public abstract IStructureDefinition<C> getStructureDefinition();

    /**
     * Checks the Machine.
     * <p>
     * NOTE: If using `buildState` be sure to `startBuilding()` and either `endBuilding()` or `failBuilding()`
     */
    public boolean checkMachine() {
        calculateTier();
        updatePowerLogic();
        return tier > 0;
    }

    protected void calculateTier() {
        if (functionalCasings.size() == 0) {
            return;
        }
        double sum = 0;
        for (WeakTargetRef<FunctionalCasing> casingRef : functionalCasings) {
            final FunctionalCasing casing = casingRef.get();
            if (casing == null) continue;
            sum += casing.getPartTier() * casing.getPartModifier();
        }
        tier = (int) Math.min(Math.floor(sum / functionalCasings.size()), 14);
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound nbt) {
        super.writeMultiTileNBT(nbt);

        nbt.setBoolean(NBT.STRUCTURE_OK, structureOkay);
        nbt.setByte(
            NBT.ROTATION,
            (byte) extendedFacing.getRotation()
                .getIndex());
        nbt.setByte(
            NBT.FLIP,
            (byte) extendedFacing.getFlip()
                .getIndex());

        nbt.setString(NBT.VOIDING_MODE, voidingMode.name);
        nbt.setBoolean(NBT.SEPARATE_INPUTS, separateInputs);
        nbt.setBoolean(NBT.RECIPE_LOCK, recipeLock);
        nbt.setBoolean(NBT.BATCH_MODE, batchMode);
    }

    @Override
    protected void saveItemLogic(NBTTagCompound nbt) {
        NBTTagCompound itemInputNBT = controllerItemInput.saveToNBT();
        nbt.setTag(NBT.INV_INPUT_LIST, itemInputNBT);
        NBTTagCompound itemOutputNBT = controllerItemOutput.saveToNBT();
        nbt.setTag(NBT.INV_OUTPUT_LIST, itemOutputNBT);
    }

    @Override
    protected void saveFluidLogic(NBTTagCompound nbt) {
        NBTTagCompound fluidInputNBT = controllerFluidInput.saveToNBT();
        nbt.setTag(NBT.TANK_IN, fluidInputNBT);
        NBTTagCompound fluidOutputNBT = controllerFluidOutput.saveToNBT();
        nbt.setTag(NBT.TANK_OUT, fluidOutputNBT);
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound nbt) {
        super.readMultiTileNBT(nbt);

        // Multiblock inventories are a collection of inventories. The first inventory is the default internal
        // inventory, and the others are added by inventory extending blocks.

        structureOkay = nbt.getBoolean(NBT.STRUCTURE_OK);
        extendedFacing = ExtendedFacing
            .of(getFrontFacing(), Rotation.byIndex(nbt.getByte(NBT.ROTATION)), Flip.byIndex(nbt.getByte(NBT.FLIP)));

        voidingMode = VoidingMode.fromName(nbt.getString(NBT.VOIDING_MODE));
        separateInputs = nbt.getBoolean(NBT.SEPARATE_INPUTS);
        recipeLock = nbt.getBoolean(NBT.RECIPE_LOCK);
        batchMode = nbt.getBoolean(NBT.BATCH_MODE);
    }

    @Override
    protected void loadItemLogic(NBTTagCompound nbt) {
        if (!nbt.hasKey(NBT.INV_INPUT_LIST) && !nbt.hasKey(NBT.INV_OUTPUT_LIST)) {
            controllerItemInput.addInventory(new ItemInventoryLogic(16));
            controllerItemOutput.addInventory(new ItemInventoryLogic(16));
            return;
        }
        controllerItemInput.loadFromNBT(nbt.getCompoundTag(NBT.INV_INPUT_LIST));
        controllerItemOutput.loadFromNBT(nbt.getCompoundTag(NBT.INV_OUTPUT_LIST));
    }

    @Override
    protected void loadFluidLogic(NBTTagCompound nbt) {
        if (!nbt.hasKey(NBT.TANK_IN) && !nbt.hasKey(NBT.TANK_OUT)) {
            controllerFluidInput.addInventory(new FluidInventoryLogic(16, 32000));
            controllerFluidOutput.addInventory(new FluidInventoryLogic(16, 32000));
            return;
        }
        controllerFluidInput.loadFromNBT(nbt.getCompoundTag(NBT.TANK_IN));
        controllerFluidOutput.loadFromNBT(nbt.getCompoundTag(NBT.TANK_OUT));
    }

    @Override
    public void addToolTips(List<String> aList, ItemStack aStack, boolean aF3_H) {
        aList.addAll(Arrays.asList(getDescription()));
    }

    @Override
    public String[] getDescription() {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            return getTooltip().getStructureInformation();
        }

        return getTooltip().getInformation();
    }

    @Override
    protected void addDebugInfo(EntityPlayer aPlayer, int aLogLevel, ArrayList<String> tList) {
        super.addDebugInfo(aPlayer, aLogLevel, tList);
        tList.add("Structure ok: " + checkStructure(false));
    }

    protected int getToolTipID() {
        return getMultiTileEntityRegistryID() << 16 + getMultiTileEntityID();
    }

    protected MultiblockTooltipBuilder getTooltip() {
        MultiblockTooltipBuilder builder = tooltip.get(getToolTipID());
        if (builder == null) {
            builder = createTooltip();
            tooltip.put(getToolTipID(), builder);
        }
        return builder;
    }

    @Override
    public boolean checkStructure(boolean aForceReset) {
        if (!isServerSide()) return structureOkay;

        // Only trigger an update if forced (from onPostTick, generally), or if the structure has changed
        if ((structureChanged || aForceReset)) {
            clearSpecialLists();
            structureOkay = checkMachine();
        }
        structureChanged = false;
        return structureOkay;
    }

    @Override
    public void onStructureChange() {
        structureChanged = true;
    }

    public final boolean checkPiece(String piece, Vec3Impl offset) {
        return checkPiece(piece, offset.get0(), offset.get1(), offset.get2());
    }

    /**
     * Explanation of the world coordinate these offset means:
     * <p>
     * Imagine you stand in front of the controller, with controller facing towards you not rotated or flipped.
     * <p>
     * The horizontalOffset would be the number of blocks on the left side of the controller, not counting controller
     * itself. The verticalOffset would be the number of blocks on the top side of the controller, not counting
     * controller itself. The depthOffset would be the number of blocks between you and controller, not counting
     * controller itself.
     * <p>
     * All these offsets can be negative.
     */
    protected final boolean checkPiece(String piece, int horizontalOffset, int verticalOffset, int depthOffset) {
        return getCastedStructureDefinition().check(
            this,
            piece,
            getWorld(),
            getExtendedFacing(),
            getXCoord(),
            getYCoord(),
            getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            !structureOkay);
    }

    public final boolean buildPiece(String piece, ItemStack trigger, boolean hintsOnly, Vec3Impl offset) {
        return buildPiece(piece, trigger, hintsOnly, offset.get0(), offset.get1(), offset.get2());
    }

    protected final boolean buildPiece(String piece, ItemStack trigger, boolean hintOnly, int horizontalOffset,
        int verticalOffset, int depthOffset) {
        return getCastedStructureDefinition().buildOrHints(
            this,
            trigger,
            piece,
            getWorld(),
            getExtendedFacing(),
            getXCoord(),
            getYCoord(),
            getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            hintOnly);
    }

    protected final int survivalBuildPiece(String piece, ItemStack trigger, Vec3Impl offset, int elementBudget,
        ISurvivalBuildEnvironment env, boolean check) {
        return survivalBuildPiece(
            piece,
            trigger,
            offset.get0(),
            offset.get1(),
            offset.get2(),
            elementBudget,
            env,
            check);
    }

    protected final Integer survivalBuildPiece(String piece, ItemStack trigger, int horizontalOffset,
        int verticalOffset, int depthOffset, int elementBudget, ISurvivalBuildEnvironment env, boolean check) {
        return getCastedStructureDefinition().survivalBuild(
            this,
            trigger,
            piece,
            getWorld(),
            getExtendedFacing(),
            getXCoord(),
            getYCoord(),
            getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementBudget,
            env,
            check);
    }

    @SuppressWarnings("unchecked")
    private IStructureDefinition<Controller<C, P>> getCastedStructureDefinition() {
        return (IStructureDefinition<Controller<C, P>>) getStructureDefinition();
    }

    @Override
    public ExtendedFacing getExtendedFacing() {
        return extendedFacing;
    }

    @Override
    public void setExtendedFacing(ExtendedFacing newExtendedFacing) {
        if (extendedFacing == newExtendedFacing) {
            return;
        }

        onStructureChange();
        if (structureOkay) stopMachine(false);
        extendedFacing = newExtendedFacing;
        structureOkay = false;
        if (isServerSide()) {
            StructureLibAPI.sendAlignment(
                this,
                new NetworkRegistry.TargetPoint(
                    getWorld().provider.dimensionId,
                    getXCoord(),
                    getYCoord(),
                    getZCoord(),
                    512));
        } else {
            issueTextureUpdate();
        }

    }

    @Override
    public boolean onWrenchRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide, float aX,
        float aY, float aZ, ItemStack aTool) {
        if (wrenchSide != getFrontFacing())
            return super.onWrenchRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ);
        if (aPlayer.isSneaking()) {
            // we won't be allowing horizontal flips, as it can be perfectly emulated by rotating twice and flipping
            // horizontally allowing an extra round of flip make it hard to draw meaningful flip markers in
            // GTProxy#drawGrid
            toolSetFlip(getFlip().isHorizontallyFlipped() ? Flip.NONE : Flip.HORIZONTAL);
        } else {
            toolSetRotation(null);
        }
        return true;
    }

    @Override
    public void registerCoveredPartOnSide(final ForgeDirection side, IMultiBlockPart part) {
        if (side == ForgeDirection.UNKNOWN) return;

        final List<WeakTargetRef<IMultiBlockPart>> registeredCovers = registeredCoveredParts.get(side.ordinal());
        // TODO: Make sure that we're not already registered on this side
        registeredCovers.add(new WeakTargetRef<>(part, true));
    }

    @Override
    public void unregisterCoveredPartOnSide(final ForgeDirection side, IMultiBlockPart aPart) {
        if (side == ForgeDirection.UNKNOWN) return;

        final List<WeakTargetRef<IMultiBlockPart>> coveredParts = registeredCoveredParts.get(side.ordinal());
        final Iterator<WeakTargetRef<IMultiBlockPart>> it = coveredParts.listIterator();
        while (it.hasNext()) {
            final IMultiBlockPart part = (it.next()).get();
            if (part == null || part == aPart) it.remove();
        }
    }

    @Override
    public void registerCaseWithPurpose(MultiTileCasingPurpose purpose, IMultiBlockPart part) {
        final List<WeakTargetRef<IMultiBlockPart>> tickableParts = registeredTickableParts.get(purpose.ordinal());
        final Iterator<WeakTargetRef<IMultiBlockPart>> it = tickableParts.listIterator();
        while (it.hasNext()) {
            final IMultiBlockPart next = (it.next()).get();
            if (next == null) {
                it.remove();
            } else if (next == part) {
                return;
            }
        }
        tickableParts.add(new WeakTargetRef<>(part, true));
    }

    @Override
    public void unregisterCaseWithPurpose(MultiTileCasingPurpose purpose, IMultiBlockPart part) {
        final List<WeakTargetRef<IMultiBlockPart>> tickableParts = registeredTickableParts.get(purpose.ordinal());
        final Iterator<WeakTargetRef<IMultiBlockPart>> it = tickableParts.listIterator();
        while (it.hasNext()) {
            final IMultiBlockPart next = (it.next()).get();
            if (next == null || next == part) it.remove();
        }
    }

    @Override
    public void onFirstTick(boolean isServerSide) {
        super.onFirstTick(isServerSide);
        if (isServerSide) {
            checkStructure(true);
        } else {
            StructureLibAPI.queryAlignment(this);
        }
    }

    private boolean tickCovers() {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            // TODO: Tick controller covers, if any
            final List<WeakTargetRef<IMultiBlockPart>> coveredParts = this.registeredCoveredParts.get(side.ordinal());
            final Iterator<WeakTargetRef<IMultiBlockPart>> it = coveredParts.listIterator();
            while (it.hasNext()) {
                final IMultiBlockPart part = (it.next()).get();
                if (part == null) {
                    it.remove();
                    continue;
                }
                if (!part.tickCoverAtSide(side, mTickTimer)) it.remove();
            }
        }

        return true;
    }

    @Override
    public void onTick(long tick, boolean isServerSide) {
        tickCovers();
    }

    @Override
    public void onPostTick(long tick, boolean isServerSide) {
        if (!isServerSide) { // client side
            doActivitySound(getActivitySoundLoop());
            return;
        }

        // server side
        if (tick % 600 == 5) {
            // Recheck the structure every 30 seconds or so
            if (!checkStructure(false)) checkStructure(true);
        }
        if (checkStructure(false)) {
            runMachine(tick);
            pushItemOutputs(tick);
            pushFluidOutputs(tick);

        } else {
            stopMachine(false);
        }

    }

    protected void pushItemOutputs(long tick) {
        if (tick % AUTO_OUTPUT_FREQUENCY_TICK != 0) return;
        final List<WeakTargetRef<IMultiBlockPart>> registeredItemOutputs = registeredTickableParts
            .get(MultiTileCasingPurpose.ItemOutput.ordinal());
        final Iterator<WeakTargetRef<IMultiBlockPart>> itemOutputIterator = registeredItemOutputs.listIterator();
        while (itemOutputIterator.hasNext()) {
            final IMultiBlockPart part = (itemOutputIterator.next()).get();
            if (part == null) {
                itemOutputIterator.remove();
                continue;
            }
            if (!part.shouldTick(mTickTimer)) {
                itemOutputIterator.remove();
                continue;
            }

            final IInventory facingInventory = part.getIInventoryAtSide(part.getFrontFacing());
            if (facingInventory == null) {
                continue;
            }

            moveMultipleItemStacks(
                part,
                facingInventory,
                part.getFrontFacing(),
                part.getBackFacing(),
                null,
                false,
                (byte) 64,
                (byte) 1,
                (byte) 64,
                (byte) 1,
                part.getSizeInventory());
            for (int i = 0; i < part.getSizeInventory(); i++) {
                final ItemStack stack = part.getStackInSlot(i);
                if (stack != null && stack.stackSize <= 0) {
                    part.setInventorySlotContents(i, null);
                }
            }

        }
    }

    protected void pushFluidOutputs(long tick) {
        if (tick % AUTO_OUTPUT_FREQUENCY_TICK != 0) return;
        final List<WeakTargetRef<IMultiBlockPart>> registeredFluidOutputs = registeredTickableParts
            .get(MultiTileCasingPurpose.FluidOutput.ordinal());
        final Iterator<WeakTargetRef<IMultiBlockPart>> fluidOutputIterator = registeredFluidOutputs.listIterator();
        while (fluidOutputIterator.hasNext()) {
            final IMultiBlockPart part = (fluidOutputIterator.next()).get();
            if (part == null) {
                fluidOutputIterator.remove();
                continue;
            }
            if (!part.shouldTick(mTickTimer)) {
                fluidOutputIterator.remove();
            }
        }
    }

    @Override
    public void setCleanroom(boolean cleanroom) {
        isCleanroom = cleanroom;
    }

    protected void clearSpecialLists() {
        upgradeCasings.clear();
        functionalCasings.clear();
    }

    @Override
    public final boolean isFacingValid(ForgeDirection facing) {
        return canSetToDirectionAny(facing);
    }

    @Override
    public void onFacingChange() {
        toolSetDirection(getFrontFacing());
        onStructureChange();
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GTItemStack aCoverID) {
        return side != facing;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return getTooltip().getStructureHint();
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        return limits;
    }

    protected void setAlignmentLimits(IAlignmentLimits mLimits) {
        this.limits = mLimits;
    }

    public boolean isSeparateInputs() {
        return separateInputs;
    }

    public void setSeparateInputs(boolean aSeparateInputs) {
        separateInputs = aSeparateInputs;
    }

    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> !f.isVerticallyFliped();
    }

    public static class BuildState {

        /**
         * Utility class to keep track of the build state of a multiblock
         */
        boolean building = false;

        Vec3Impl currentOffset;

        public void startBuilding(Vec3Impl structureOffset) {
            if (building) throw new IllegalStateException("Already building!");
            building = true;
            setCurrentOffset(structureOffset);
        }

        public Vec3Impl setCurrentOffset(Vec3Impl structureOffset) {
            verifyBuilding();
            return (currentOffset = structureOffset);
        }

        private void verifyBuilding() {
            if (!building) throw new IllegalStateException("Not building!");
        }

        public boolean failBuilding() {
            building = false;
            currentOffset = null;
            return false;
        }

        public Vec3Impl stopBuilding() {
            final Vec3Impl toReturn = getCurrentOffset();
            building = false;
            currentOffset = null;

            return toReturn;
        }

        public Vec3Impl getCurrentOffset() {
            verifyBuilding();
            return currentOffset;
        }

        public Vec3Impl addOffset(Vec3Impl offset) {
            verifyBuilding();
            return setCurrentOffset(currentOffset.add(offset));
        }
    }

    public void registerSpecialCasings(MultiBlockPart part) {
        if (part instanceof UpgradeCasing upgradeCasing) {
            upgradeCasings.add(new WeakTargetRef<>(upgradeCasing, true));
        }
        if (part instanceof FunctionalCasing functionalCasing) {
            functionalCasings.add(new WeakTargetRef<>(functionalCasing, true));
        }
    }

    // #region Fluid - MultiBlock related Fluid Tank behaviour.

    @Override
    @Nullable
    public FluidInventoryLogic getFluidLogic(@Nonnull ForgeDirection side, @Nonnull InventoryType type) {
        if (side == facing) return null;
        return switch (type) {
            case Input -> controllerFluidInput.getAllInventoryLogics();
            case Output -> controllerFluidOutput.getAllInventoryLogics();
            default -> null;
        };
    }

    @Override
    @Nonnull
    public @NotNull FluidInventoryLogic getFluidLogic(@Nonnull InventoryType type, @Nullable UUID id) {
        return switch (type) {
            case Input -> controllerFluidInput.getInventoryLogic(id);
            case Output -> controllerFluidOutput.getInventoryLogic(id);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    @Override
    @Nonnull
    public UUID registerFluidInventory(int tanks, long capacity, int tier, @Nonnull InventoryType type,
        boolean isUpgradeInventory) {
        return switch (type) {
            case Input -> controllerFluidInput
                .addInventory(new FluidInventoryLogic(tanks, capacity, tier, isUpgradeInventory));
            case Output -> controllerFluidOutput
                .addInventory(new FluidInventoryLogic(tanks, capacity, tier, isUpgradeInventory));
            case Both -> {
                UUID id = controllerFluidInput
                    .addInventory(new FluidInventoryLogic(tanks, capacity, tier, isUpgradeInventory));
                controllerFluidOutput
                    .addInventory(id, new FluidInventoryLogic(tanks, capacity, tier, isUpgradeInventory));
                yield id;
            }
        };
    }

    @Override
    @Nonnull
    public FluidInventoryLogic unregisterFluidInventory(@Nonnull UUID id, @Nonnull InventoryType type) {
        return switch (type) {
            case Input -> controllerFluidInput.removeInventory(id);
            case Output -> controllerFluidOutput.removeInventory(id);
            case Both -> {
                FluidInventoryLogic input = controllerFluidInput.removeInventory(id);
                FluidInventoryLogic output = controllerFluidOutput.removeInventory(id);
                yield new FluidInventoryLogic(
                    Stream.of(input, output)
                        .map(FluidInventoryLogic::getInventory)
                        .collect(Collectors.toList()));
            }
        };
    }

    @Override
    public void changeFluidInventoryDisplayName(@Nullable UUID id, @Nullable String displayName,
        @Nonnull InventoryType type) {
        switch (type) {
            case Input:
                controllerFluidInput.setInventoryDisplayName(id, displayName);
                break;
            case Output:
                controllerFluidOutput.setInventoryDisplayName(id, displayName);
                break;
            case Both:
                controllerFluidInput.setInventoryDisplayName(id, displayName);
                controllerFluidOutput.setInventoryDisplayName(id, displayName);
                break;
        }
    }

    // #endregion Fluid

    // #region Item - MultiBlock related Item behaviour.

    @Override
    @Nullable
    public ItemInventoryLogic getItemLogic(@Nonnull ForgeDirection side, @Nonnull InventoryType type) {
        if (side == facing) return null;
        return switch (type) {
            case Input -> controllerItemInput.getAllInventoryLogics();
            case Output -> controllerItemOutput.getAllInventoryLogics();
            default -> null;
        };
    }

    @Override
    @Nonnull
    public ItemInventoryLogic getItemLogic(@Nonnull InventoryType type, @Nullable UUID id) {
        return switch (type) {
            case Input -> controllerItemInput.getInventoryLogic(id);
            case Output -> controllerItemOutput.getInventoryLogic(id);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    @Override
    @Nonnull
    public UUID registerItemInventory(int slots, int tier, @Nonnull InventoryType type, boolean isUpgradeInventory) {
        return switch (type) {
            case Input -> controllerItemInput.addInventory(new ItemInventoryLogic(slots, tier, isUpgradeInventory));
            case Output -> controllerItemOutput.addInventory(new ItemInventoryLogic(slots, tier, isUpgradeInventory));
            case Both -> {
                UUID id = controllerItemInput.addInventory(new ItemInventoryLogic(slots, tier, isUpgradeInventory));
                controllerItemOutput.addInventory(id, new ItemInventoryLogic(slots, tier, isUpgradeInventory));
                yield id;
            }
        };
    }

    @Override
    public ItemInventoryLogic unregisterItemInventory(@Nonnull UUID id, @Nonnull InventoryType type) {
        return switch (type) {
            case Input -> controllerItemInput.removeInventory(id);
            case Output -> controllerItemOutput.removeInventory(id);
            case Both -> {
                ItemInventoryLogic input = controllerItemInput.removeInventory(id);
                ItemInventoryLogic output = controllerItemOutput.removeInventory(id);
                yield new ItemInventoryLogic(
                    Arrays.asList(input, output)
                        .stream()
                        .map(inv -> inv.getInventory())
                        .collect(Collectors.toList()));
            }
        };
    }

    @Override
    public void changeItemInventoryDisplayName(@Nullable UUID id, @Nullable String displayName,
        @Nonnull InventoryType type) {
        switch (type) {
            case Input:
                controllerItemInput.setInventoryDisplayName(id, displayName);
                break;
            case Output:
                controllerItemOutput.setInventoryDisplayName(id, displayName);
                break;
            case Both:
                controllerItemInput.setInventoryDisplayName(id, displayName);
                controllerItemOutput.setInventoryDisplayName(id, displayName);
                break;
        }
    }

    // #endregion Item

    // #region Energy

    @Nonnull
    @Override
    public PowerLogic getPowerLogic() {
        return getPowerLogic(ForgeDirection.UNKNOWN);
    }

    // #endregion Energy

    @Override
    protected void updateSlots() {
        controllerItemInput.getAllInventoryLogics()
            .update(shouldSort);
        controllerItemOutput.getAllInventoryLogics()
            .update(shouldSort);
        controllerFluidInput.getAllInventoryLogics()
            .update();
        controllerFluidOutput.getAllInventoryLogics()
            .update();
    }

    /*
     * GUI Work - Multiblock GUI related methods
     */
    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public boolean hasGui(ForgeDirection side) {
        return true;
    }

    @Override
    protected void addTitleTextStyle(ModularWindow.Builder builder, String title) {
        // leave empty
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public VoidingMode getVoidingMode() {
        return voidingMode;
    }

    @Override
    public void setVoidingMode(VoidingMode mode) {
        this.voidingMode = mode;
    }

    @Override
    public boolean canDumpItemToME() {
        return false;
    }

    @Override
    public boolean canDumpFluidToME() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean isInputSeparated() {
        return separateInputs;
    }

    @Override
    public void setInputSeparation(Boolean enabled) {
        this.separateInputs = enabled;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean isBatchModeEnabled() {
        return batchMode;
    }

    @Override
    public void setBatchMode(Boolean mode) {
        this.batchMode = mode;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean isRecipeLockingEnabled() {
        return recipeLock;
    }

    @Override
    public void setRecipeLocking(Boolean enabled) {
        this.recipeLock = enabled;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        P processing = getProcessingLogic();
        tag.setInteger("progress", processing.getProgress());
        tag.setInteger("maxProgress", processing.getDuration());
        tag.setBoolean("structureOkay", structureOkay);
        tag.setBoolean("isActive", isActive());
        if (isActive()) {
            tag.setLong("energyUsage", getProcessingLogic().getCalculatedEut());
            tag.setLong("energyTier", tier);
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (!tag.getBoolean("structureOkay")) {
            currentTip.add(RED + "** INCOMPLETE STRUCTURE **" + RESET);
        } else {
            currentTip.add((GREEN + "Running Fine") + RESET);
        }
        if (isSimpleMachine) {
            boolean isActive = tag.getBoolean("isActive");
            currentTip.add(
                GTWaila.getMachineProgressString(isActive, tag.getInteger("maxProgress"), tag.getInteger("progress")));
        }
        boolean isActive = tag.getBoolean("isActive");
        if (isActive) {
            long energyTier = tag.getLong("energyTier");
            long actualEnergyUsage = tag.getLong("energyUsage");
            if (actualEnergyUsage > 0) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.waila.energy.use_with_amperage",
                        GTUtility.formatNumbers(actualEnergyUsage),
                        GTUtility.getAmperageForTier(actualEnergyUsage, (byte) energyTier),
                        GTUtility.getColoredTierNameFromTier((byte) energyTier)));
            } else if (actualEnergyUsage < 0) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.waila.energy.produce_with_amperage",
                        GTUtility.formatNumbers(-actualEnergyUsage),
                        GTUtility.getAmperageForTier(-actualEnergyUsage, (byte) energyTier),
                        GTUtility.getColoredTierNameFromTier((byte) energyTier)));
            }
        }
    }

    @Override
    public GTPacketMultiTileEntity getClientDataPacket() {
        final GTPacketMultiTileEntity packet = super.getClientDataPacket();

        return packet;

    }

    @Override
    public void enableWorking() {
        super.enableWorking();
        if (!structureOkay) {
            checkStructure(true);
        }
    }

    @Override
    public List<ItemStack> getItemOutputSlots(ItemStack[] toOutput) {
        return new ArrayList<>(0);
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return new ArrayList<>(0);
    }

    @Override
    @Nonnull
    public Set<Entry<UUID, FluidInventoryLogic>> getAllFluidInventoryLogics(@Nonnull InventoryType type) {
        return switch (type) {
            case Input -> controllerFluidInput.getAllInventoryLogicsAsEntrySet();
            case Output -> controllerFluidOutput.getAllInventoryLogicsAsEntrySet();
            default -> super.getAllFluidInventoryLogics(type);
        };
    }

    @Override
    @Nonnull
    public Set<Entry<UUID, ItemInventoryLogic>> getAllItemInventoryLogics(@Nonnull InventoryType type) {
        return switch (type) {
            case Input -> controllerItemInput.getAllInventoryLogicsAsEntrySet();
            case Output -> controllerItemOutput.getAllInventoryLogicsAsEntrySet();
            default -> super.getAllItemInventoryLogics(type);
        };
    }

    @Override
    public void setWirelessSupport(boolean canUse) {
        if (canUse) {
            strongCheckOrAddUser(getOwnerUuid());
        }
        power.setCanUseWireless(canUse, getOwnerUuid());
    }

    @Override
    public void setLaserSupport(boolean canUse) {
        power.setCanUseLaser(canUse);
    }

    @Override
    public void setMaxAmperage(long amperage) {
        power.setMaxAmperage(amperage);
    }
}
