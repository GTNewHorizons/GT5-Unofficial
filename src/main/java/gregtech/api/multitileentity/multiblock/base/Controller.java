package gregtech.api.multitileentity.multiblock.base;

import static gregtech.api.util.GT_Utility.moveMultipleItemStacks;
import static mcp.mobius.waila.api.SpecialChars.*;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

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
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.*;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.TabButton;
import com.gtnewhorizons.modularui.common.widget.TabContainer;

import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.InventoryType;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IDescribable;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.modularui.ControllerWithOptionalFeatures;
import gregtech.api.logic.ControllerFluidLogic;
import gregtech.api.logic.ControllerItemLogic;
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.multitileentity.enums.MultiTileCasingPurpose;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_AddToolTips;
import gregtech.api.multitileentity.machine.MultiTileBasicMachine;
import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;
import gregtech.api.net.GT_Packet_MultiTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Waila;
import gregtech.common.tileentities.casings.upgrade.Inventory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Multi Tile Entities - or MuTEs - don't have dedicated hatches, but their casings can become hatches.
 */
public abstract class Controller<T extends Controller<T>> extends MultiTileBasicMachine
    implements IAlignment, IMultiBlockController, IDescribable, IMTE_AddToolTips, ISurvivalConstructable,
    ControllerWithOptionalFeatures, IGlobalWirelessEnergy {

    public static final String ALL_INVENTORIES_NAME = "all";
    protected static final int AUTO_OUTPUT_FREQUENCY_TICK = 20;

    private static final Map<Integer, GT_Multiblock_Tooltip_Builder> tooltip = new ConcurrentHashMap<>();
    private final List<UpgradeCasing> upgradeCasings = new ArrayList<>();
    private final List<FunctionalCasing> functionalCasings = new ArrayList<>();
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
    protected List<LinkedList<WeakReference<IMultiBlockPart>>> registeredCoveredParts = Arrays.asList(
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>());

    // A list for each purpose that a casing can register to, to be ticked
    protected List<LinkedList<WeakReference<IMultiBlockPart>>> registeredTickableParts = new ArrayList<>();

    public Controller() {
        for (int i = 0; i < MultiTileCasingPurpose.values().length; i++) {
            registeredTickableParts.add(new LinkedList<>());
        }
    }

    /** Registry ID of the required casing */
    public abstract short getCasingRegistryID();

    /** Meta ID of the required casing */
    public abstract int getCasingMeta();

    /**
     * Create the tooltip for this multi block controller.
     */
    protected abstract GT_Multiblock_Tooltip_Builder createTooltip();

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
    public abstract IStructureDefinition<T> getStructureDefinition();

    /**
     * Checks the Machine.
     * <p>
     * NOTE: If using `buildState` be sure to `startBuilding()` and either `endBuilding()` or `failBuilding()`
     */
    public boolean checkMachine() {
        calculateTier();
        return tier > 0;
    }

    protected void calculateTier() {
        double sum = 0;
        if (functionalCasings == null || functionalCasings.size() == 0) {
            return;
        }
        for (FunctionalCasing casing : functionalCasings) {
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
        } else {
            return getTooltip().getInformation();
        }
    }

    @Override
    protected void addDebugInfo(EntityPlayer aPlayer, int aLogLevel, ArrayList<String> tList) {
        super.addDebugInfo(aPlayer, aLogLevel, tList);
        tList.add("Structure ok: " + checkStructure(false));
    }

    protected int getToolTipID() {
        return getMultiTileEntityRegistryID() << 16 + getMultiTileEntityID();
    }

    protected GT_Multiblock_Tooltip_Builder getTooltip() {
        GT_Multiblock_Tooltip_Builder builder = tooltip.get(getToolTipID());
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
    private IStructureDefinition<Controller<T>> getCastedStructureDefinition() {
        return (IStructureDefinition<Controller<T>>) getStructureDefinition();
    }

    @Override
    public ExtendedFacing getExtendedFacing() {
        return extendedFacing;
    }

    @Override
    public void setExtendedFacing(ExtendedFacing newExtendedFacing) {
        if (extendedFacing != newExtendedFacing) {
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
    }

    @Override
    public boolean onWrenchRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide, float aX,
        float aY, float aZ) {
        if (wrenchSide != getFrontFacing())
            return super.onWrenchRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ);
        if (aPlayer.isSneaking()) {
            // we won't be allowing horizontal flips, as it can be perfectly emulated by rotating twice and flipping
            // horizontally allowing an extra round of flip make it hard to draw meaningful flip markers in
            // GT_Proxy#drawGrid
            toolSetFlip(getFlip().isHorizontallyFlipped() ? Flip.NONE : Flip.HORIZONTAL);
        } else {
            toolSetRotation(null);
        }
        return true;
    }

    @Override
    public void registerCoveredPartOnSide(final ForgeDirection side, IMultiBlockPart part) {
        if (side == ForgeDirection.UNKNOWN) return;

        final LinkedList<WeakReference<IMultiBlockPart>> registeredCovers = registeredCoveredParts.get(side.ordinal());
        // TODO: Make sure that we're not already registered on this side
        registeredCovers.add(new WeakReference<>(part));
    }

    @Override
    public void unregisterCoveredPartOnSide(final ForgeDirection side, IMultiBlockPart aPart) {
        if (side == ForgeDirection.UNKNOWN) return;

        final LinkedList<WeakReference<IMultiBlockPart>> coveredParts = registeredCoveredParts.get(side.ordinal());
        final Iterator<WeakReference<IMultiBlockPart>> it = coveredParts.iterator();
        while (it.hasNext()) {
            final IMultiBlockPart part = (it.next()).get();
            if (part == null || part == aPart) it.remove();
        }
    }

    @Override
    public void registerCaseWithPurpose(MultiTileCasingPurpose purpose, IMultiBlockPart part) {
        final LinkedList<WeakReference<IMultiBlockPart>> tickableParts = registeredTickableParts.get(purpose.ordinal());
        final Iterator<WeakReference<IMultiBlockPart>> it = tickableParts.iterator();
        while (it.hasNext()) {
            final IMultiBlockPart next = (it.next()).get();
            if (next == null) {
                it.remove();
            } else if (next == part) {
                return;
            }
        }
        tickableParts.add(new WeakReference<>(part));
    }

    @Override
    public void unregisterCaseWithPurpose(MultiTileCasingPurpose purpose, IMultiBlockPart part) {
        final LinkedList<WeakReference<IMultiBlockPart>> tickableParts = registeredTickableParts.get(purpose.ordinal());
        final Iterator<WeakReference<IMultiBlockPart>> it = tickableParts.iterator();
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
            final LinkedList<WeakReference<IMultiBlockPart>> coveredParts = this.registeredCoveredParts
                .get(side.ordinal());
            final Iterator<WeakReference<IMultiBlockPart>> it = coveredParts.iterator();
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
        if (!tickCovers()) {
            return;
        }
    }

    @Override
    public void onPostTick(long tick, boolean isServerSide) {
        if (isServerSide) {
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
        } else {
            doActivitySound(getActivitySoundLoop());
        }
    }

    protected void pushItemOutputs(long tick) {
        if (tick % AUTO_OUTPUT_FREQUENCY_TICK != 0) return;
        final LinkedList<WeakReference<IMultiBlockPart>> registeredItemOutputs = registeredTickableParts
            .get(MultiTileCasingPurpose.ItemOutput.ordinal());
        final Iterator<WeakReference<IMultiBlockPart>> itemOutputIterator = registeredItemOutputs.iterator();
        while (itemOutputIterator.hasNext()) {
            final IMultiBlockPart part = (itemOutputIterator.next()).get();
            if (part == null) {
                itemOutputIterator.remove();
                continue;
            }
            if (!part.shouldTick(mTickTimer)) {
                itemOutputIterator.remove();
            } else {
                final IInventory facingInventory = part.getIInventoryAtSide(part.getFrontFacing());
                if (facingInventory != null) {
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
                        if (part.getStackInSlot(i) != null && part.getStackInSlot(i).stackSize <= 0)
                            part.setInventorySlotContents(i, null);
                    }
                }
            }
        }
    }

    protected void pushFluidOutputs(long tick) {
        if (tick % AUTO_OUTPUT_FREQUENCY_TICK != 0) return;
        final LinkedList<WeakReference<IMultiBlockPart>> registeredFluidOutputs = registeredTickableParts
            .get(MultiTileCasingPurpose.FluidOutput.ordinal());
        final Iterator<WeakReference<IMultiBlockPart>> fluidOutputIterator = registeredFluidOutputs.iterator();
        while (fluidOutputIterator.hasNext()) {
            final IMultiBlockPart part = (fluidOutputIterator.next()).get();
            if (part == null) {
                fluidOutputIterator.remove();
                continue;
            }
            if (!part.shouldTick(mTickTimer)) {
                fluidOutputIterator.remove();
            } else {

            }
        }
    }

    @Override
    public void setCleanroom(boolean cleanroom) {
        isCleanroom = cleanroom;
    }

    @Override
    public void setWirelessSupport(boolean canUse) {
        if (canUse) {
            strongCheckOrAddUser(getOwnerUuid(), getOwnerName());
        }
        canUseWireless = canUse;
    }

    @Override
    public void setLaserSupport(boolean canUse) {
        canUseLaser = canUse;
    }

    @Override
    public void setMaxAmperage(long amperage) {
        this.amperage = amperage;
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
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aCoverID) {
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
        if (part instanceof UpgradeCasing) {
            upgradeCasings.add((UpgradeCasing) part);
        }
        if (part instanceof FunctionalCasing) {
            functionalCasings.add((FunctionalCasing) part);
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

    @Nullable
    public FluidInventoryLogic getFluidLogic(@Nonnull InventoryType type, @Nullable UUID id) {
        return switch (type) {
            case Input -> controllerFluidInput.getInventoryLogic(id);
            case Output -> controllerFluidOutput.getInventoryLogic(id);
            default -> null;
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
                    Arrays.asList(input, output)
                        .stream()
                        .map(inv -> inv.getInventory())
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
    @Nullable
    public ItemInventoryLogic getItemLogic(@Nonnull InventoryType type, @Nullable UUID id) {
        return switch (type) {
            case Input -> controllerItemInput.getInventoryLogic(id);
            case Output -> controllerItemOutput.getInventoryLogic(id);
            default -> null;
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

    @Override
    public PowerLogic getPowerLogic() {
        if (!(this instanceof PowerLogicHost powerLogicHost)) {
            return null;
        }

        return powerLogicHost.getPowerLogic();
    }

    // #endregion Energy

    /*
     * Helper Methods For Recipe checking
     */

    protected void setItemOutputs(String inventory, ItemStack... itemOutputs) {

    }

    @Override
    protected void setItemOutputs(ItemStack... outputs) {
        super.setItemOutputs(outputs);

    }

    @Override
    protected void outputItems(@Nullable UUID inventoryID) {
        if (itemsToOutput == null) return;
        ItemInventoryLogic inventory = controllerItemOutput.getInventoryLogic(inventoryID);
        for (int i = 0; i < itemsToOutput.length; i++) {
            inventory.insertItem(itemsToOutput[i]);
        }
        itemsToOutput = null;
    }

    protected void setFluidOutputs(String tank, FluidStack... fluidOutputs) {}

    @Override
    protected void setFluidOutputs(FluidStack... outputs) {
        super.setFluidOutputs(outputs);
    }

    @Override
    protected void outputFluids(@Nullable UUID inventoryID) {
        if (fluidsToOutput == null) return;
        FluidInventoryLogic inventory = controllerFluidOutput.getInventoryLogic(inventoryID);
        for (int i = 0; i < fluidsToOutput.length; i++) {
            inventory.fill(fluidsToOutput[i]);
        }
        fluidsToOutput = null;
    }

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

    @Override
    protected boolean checkRecipe() {
        return false;
    }

    /*
     * GUI Work - Multiblock GUI related methods
     */
    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext) {
        System.out.println("MultiBlockController::createWindow");
        if (!useModularUI()) return null;

        buildContext.setValidator(getValidator());
        final ModularWindow.Builder builder = ModularWindow.builder(getGUIWidth(), getGUIHeight());
        builder.setBackground(getGUITextureSet().getMainBackground());
        builder.setGuiTint(getGUIColorization());
        if (doesBindPlayerInventory()) {
            bindPlayerInventoryUI(builder, buildContext);
        }
        addUIWidgets(builder, buildContext);
        addTitleToUI(builder);
        addCoverTabs(builder, buildContext);
        return builder.build();
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
    public int getGUIHeight() {
        return 192;
    }

    protected Widget getGregTechLogo() {
        return new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
            .setSize(17, 17);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (isServerSide()) {
            for (UpgradeCasing tPart : upgradeCasings) {
                if (!(tPart instanceof Inventory)) continue;
                tPart.issueClientUpdate();
            }
        }
        int page = 0;
        TabContainer tabs = new TabContainer().setButtonSize(20, 24);
        tabs.addTabButton(
            new TabButton(page++)
                .setBackground(
                    false,
                    ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(0, 0, 1f, 0.5f),
                    new ItemDrawable(getStackForm(1)).withFixedSize(16, 16)
                        .withOffset(2, 4))
                .setBackground(
                    true,
                    ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(0, 0.5f, 1f, 1f),
                    new ItemDrawable(getStackForm(1)).withFixedSize(16, 16)
                        .withOffset(2, 4))
                .addTooltip(getLocalName())
                .setPos(20 * (page - 1), -20))
            .addPage(createMainPage(builder).setSize(getGUIWidth(), getGUIHeight()));
        if (hasItemInput()) {
            tabs.addTabButton(
                new TabButton(page++)
                    .setBackground(
                        false,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0, 1f, 0.5f),
                        GT_UITextures.PICTURE_ITEM_IN.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .setBackground(
                        true,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0.5f, 1f, 1f),
                        GT_UITextures.PICTURE_ITEM_IN.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .addTooltip("Item Input Inventory")
                    .setPos(20 * (page - 1), -20))
                .addPage(
                    new MultiChildWidget().addChild(
                        controllerItemInput.getAllInventoryLogics()
                            .getGuiPart()
                            .setSize(18 * 4 + 4, 18 * 5)
                            .setPos(52, 7))
                        .addChild(getGregTechLogo().setPos(147, 86))
                        .setSize(getGUIWidth(), getGUIHeight()));
        }

        if (hasItemOutput()) {
            tabs.addTabButton(
                new TabButton(page++)
                    .setBackground(
                        false,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0, 1f, 0.5f),
                        GT_UITextures.PICTURE_ITEM_OUT.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .setBackground(
                        true,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0.5f, 1f, 1f),
                        GT_UITextures.PICTURE_ITEM_OUT.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .addTooltip("Item Output Inventory")
                    .setPos(20 * (page - 1), -20))
                .addPage(
                    new MultiChildWidget().addChild(
                        controllerItemOutput.getAllInventoryLogics()
                            .getGuiPart()
                            .setSize(18 * 4 + 4, 18 * 5)
                            .setPos(52, 7))
                        .addChild(getGregTechLogo().setPos(147, 86))
                        .setSize(getGUIWidth(), getGUIHeight()));
        }

        if (hasFluidInput()) {
            tabs.addTabButton(
                new TabButton(page++)
                    .setBackground(
                        false,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0, 1f, 0.5f),
                        GT_UITextures.PICTURE_FLUID_IN.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .setBackground(
                        true,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0.5f, 1f, 1f),
                        GT_UITextures.PICTURE_FLUID_IN.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .addTooltip("Fluid Input Tanks")
                    .setPos(20 * (page - 1), -20))
                .addPage(
                    new MultiChildWidget().addChild(
                        controllerFluidInput.getAllInventoryLogics()
                            .getGuiPart()
                            .setSize(18 * 4 + 4, 18 * 5)
                            .setPos(52, 7))
                        .addChild(getGregTechLogo().setPos(147, 86))
                        .setSize(getGUIWidth(), getGUIHeight()));
        }

        if (hasFluidOutput()) {
            tabs.addTabButton(
                new TabButton(page++)
                    .setBackground(
                        false,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0, 1f, 0.5f),
                        GT_UITextures.PICTURE_FLUID_OUT.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .setBackground(
                        true,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0.5f, 1f, 1f),
                        GT_UITextures.PICTURE_FLUID_OUT.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .addTooltip("Fluid Output Tanks")
                    .setPos(20 * (page - 1), -20))
                .addPage(
                    new MultiChildWidget().addChild(
                        controllerFluidOutput.getAllInventoryLogics()
                            .getGuiPart()
                            .setSize(18 * 4 + 4, 18 * 5)
                            .setPos(52, 7))
                        .addChild(getGregTechLogo().setPos(147, 86))
                        .setSize(getGUIWidth(), getGUIHeight()));
        }
        builder.widget(tabs);
    }

    protected MultiChildWidget createMainPage(IWidgetBuilder<?> builder) {
        MultiChildWidget page = new MultiChildWidget();
        page.addChild(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 4)
                .setSize(160, 75))
            .addChild(createButtons(builder));
        return page;
    }

    protected MultiChildWidget createButtons(IWidgetBuilder<?> builder) {
        MultiChildWidget buttons = new MultiChildWidget();
        buttons.setSize(16, 167)
            .setPos(7, 86);
        buttons.addChild(createPowerSwitchButton(builder))
            .addChild(createVoidExcessButton(builder))
            .addChild(createInputSeparationButton(builder))
            .addChild(createBatchModeButton(builder))
            .addChild(createLockToSingleRecipeButton(builder));

        return buttons;
    }

    @Override
    public Pos2d getPowerSwitchButtonPos() {
        return new Pos2d(144, 0);
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
    public Pos2d getVoidingModeButtonPos() {
        return new Pos2d(54, 0);
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return separateInputs;
    }

    @Override
    public void setInputSeparation(boolean enabled) {
        this.separateInputs = enabled;
    }

    @Override
    public Pos2d getInputSeparationButtonPos() {
        return new Pos2d(36, 0);
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
    public void setBatchMode(boolean mode) {
        this.batchMode = mode;
    }

    @Override
    public Pos2d getBatchModeButtonPos() {
        return new Pos2d(18, 0);
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
    public void setRecipeLocking(boolean enabled) {
        this.recipeLock = enabled;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }

    @Override
    public Pos2d getRecipeLockingButtonPos() {
        return new Pos2d(0, 0);
    }

    @Override
    public ModularWindow createWindowGUI(UIBuildContext buildContext) {
        return createWindow(buildContext);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("progress", progressTime);
        tag.setInteger("maxProgress", maxProgressTime);
        tag.setBoolean("structureOkay", structureOkay);
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
                GT_Waila.getMachineProgressString(isActive, tag.getInteger("maxProgress"), tag.getInteger("progress")));
        }
    }

    @Override
    public GT_Packet_MultiTileEntity getClientDataPacket() {
        final GT_Packet_MultiTileEntity packet = super.getClientDataPacket();

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
}
