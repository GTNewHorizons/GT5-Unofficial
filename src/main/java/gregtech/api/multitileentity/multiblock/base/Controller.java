package gregtech.api.multitileentity.multiblock.base;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.multitileentity.enums.GT_MultiTileComponentCasing.*;
import static gregtech.api.util.GT_Utility.moveMultipleItemStacks;
import static gregtech.loaders.preload.GT_Loader_MultiTileEntities.COMPONENT_CASING_REGISTRY_NAME;
import static mcp.mobius.waila.api.SpecialChars.*;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementChain;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.forge.ListItemHandler;
import com.gtnewhorizons.modularui.api.screen.*;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TabButton;
import com.gtnewhorizons.modularui.common.widget.TabContainer;

import cpw.mods.fml.common.network.NetworkRegistry;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TextureSet;
import gregtech.api.fluid.FluidTankGT;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IDescribable;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.multitileentity.MultiTileEntityContainer;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.enums.MultiTileCasingPurpose;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_AddToolTips;
import gregtech.api.multitileentity.machine.MultiTileBasicMachine;
import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Waila;
import gregtech.common.tileentities.casings.upgrade.Inventory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Multi Tile Entities - or MuTEs - don't have dedicated hatches, but their casings can become hatches.
 */
public abstract class Controller<T extends Controller<T>> extends MultiTileBasicMachine implements IAlignment,
    IConstructable, IMultiBlockController, IDescribable, IMTE_AddToolTips, ISurvivalConstructable {

    public static final String ALL_INVENTORIES_NAME = "all";
    protected static final int AUTO_OUTPUT_FREQUENCY_TICK = 20;

    private static final Map<Integer, GT_Multiblock_Tooltip_Builder> tooltip = new ConcurrentHashMap<>();
    private final List<UpgradeCasing> upgradeCasings = new ArrayList<>();
    private final List<FunctionalCasing> functionalCasings = new ArrayList<>();
    protected BuildState buildState = new BuildState();

    protected Map<String, String> multiBlockInputInventoryNames = new LinkedHashMap<>();
    protected Map<String, String> multiBlockOutputInventoryNames = new LinkedHashMap<>();
    protected Map<String, String> multiBlockInputInventoryToTankLink = new LinkedHashMap<>();
    protected Map<String, IItemHandlerModifiable> multiBlockInputInventory = new LinkedHashMap<>();
    protected Map<String, IItemHandlerModifiable> multiBlockOutputInventory = new LinkedHashMap<>();

    protected Map<String, String> multiBlockInputTankNames = new LinkedHashMap<>();
    protected Map<String, String> multiBlockOutputTankNames = new LinkedHashMap<>();
    protected Map<String, FluidTankGT[]> multiBlockInputTank = new LinkedHashMap<>();
    protected Map<String, FluidTankGT[]> multiBlockOutputTank = new LinkedHashMap<>();

    private boolean structureOkay = false, structureChanged = false;
    private ExtendedFacing extendedFacing = ExtendedFacing.DEFAULT;
    private IAlignmentLimits limits = getInitialAlignmentLimits();
    private String inventoryName;
    private String tankName;
    protected boolean separateInputs = false;
    protected boolean voidExcess = false;
    protected boolean batchMode = false;
    protected boolean recipeLock = false;
    /** If this is set to true, the machine will get default WAILA behavior */
    protected boolean isSimpleMachine = true;

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
    protected List<LinkedList<WeakReference<IMultiBlockPart>>> registeredTickableParts;

    public Controller() {
        registeredTickableParts = new ArrayList<>(
            Collections.nCopies(MultiTileCasingPurpose.values().length, new LinkedList<>()));
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
        double sum = 0;
        if (functionalCasings == null || functionalCasings.size() == 0) {
            return false;
        }
        for (FunctionalCasing casing : functionalCasings) {
            sum += casing.getPartTier() * casing.getPartModifier();
        }
        tier = (int) Math.floor(sum / functionalCasings.size());
        // Maximum Energy stores will have a cap of 2 minute work time of current voltage
        return tier > 0;
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

        saveUpgradeInventoriesToNBT(nbt);
        saveUpgradeTanksToNBT(nbt);

        nbt.setBoolean(NBT.VOID_EXCESS, voidExcess);
        nbt.setBoolean(NBT.SEPARATE_INPUTS, separateInputs);
        nbt.setBoolean(NBT.RECIPE_LOCK, recipeLock);
        nbt.setBoolean(NBT.BATCH_MODE, batchMode);
    }

    private void saveUpgradeInventoriesToNBT(NBTTagCompound nbt) {
        final NBTTagList inputInvList = new NBTTagList();
        multiBlockInputInventory.forEach((id, inv) -> {
            if (!id.equals("controller")) {
                final NBTTagCompound tTag = new NBTTagCompound();
                tTag.setString(NBT.UPGRADE_INVENTORY_UUID, id);
                tTag.setString(NBT.UPGRADE_INVENTORY_NAME, multiBlockInputInventoryNames.get(id));
                tTag.setInteger(NBT.UPGRADE_INVENTORY_SIZE, inv.getSlots());
                writeInventory(tTag, inv, NBT.INV_INPUT_LIST);
                inputInvList.appendTag(tTag);
            }
        });
        final NBTTagList outputInvList = new NBTTagList();
        multiBlockOutputInventory.forEach((id, inv) -> {
            if (!id.equals("controller")) {
                final NBTTagCompound tTag = new NBTTagCompound();
                tTag.setString(NBT.UPGRADE_INVENTORY_UUID, id);
                tTag.setString(NBT.UPGRADE_INVENTORY_NAME, multiBlockOutputInventoryNames.get(id));
                tTag.setInteger(NBT.UPGRADE_INVENTORY_SIZE, inv.getSlots());
                writeInventory(tTag, inv, NBT.INV_OUTPUT_LIST);
                outputInvList.appendTag(tTag);
            }
        });
        nbt.setTag(NBT.UPGRADE_INVENTORIES_INPUT, inputInvList);
        nbt.setTag(NBT.UPGRADE_INVENTORIES_OUTPUT, outputInvList);
    }

    private void saveUpgradeTanksToNBT(NBTTagCompound nbt) {
        final NBTTagList inputTankList = new NBTTagList();
        multiBlockInputTank.forEach((id, tanks) -> {
            if (!id.equals("controller") && tanks != null && tanks.length > 0) {
                final NBTTagCompound tTag = new NBTTagCompound();
                tTag.setString(NBT.UPGRADE_TANK_UUID, id);
                tTag.setString(NBT.UPGRADE_TANK_NAME, multiBlockInputTankNames.get(id));
                // We assume all tanks in the tank-array are equally sized
                tTag.setLong(NBT.UPGRADE_TANK_CAPACITY, tanks[0].capacity());
                tTag.setLong(NBT.UPGRADE_TANK_CAPACITY_MULTIPLIER, tanks[0].getCapacityMultiplier());
                tTag.setInteger(NBT.UPGRADE_TANKS_COUNT, tanks.length);
                for (int i = 0; i < tanks.length; i++) {
                    tanks[i].writeToNBT(tTag, NBT.UPGRADE_TANKS_PREFIX + i);
                }
                inputTankList.appendTag(tTag);
            }
        });
        final NBTTagList outputTankList = new NBTTagList();
        multiBlockOutputTank.forEach((id, tanks) -> {
            if (!id.equals("controller") && tanks != null && tanks.length > 0) {
                final NBTTagCompound tTag = new NBTTagCompound();
                tTag.setString(NBT.UPGRADE_TANK_UUID, id);
                tTag.setString(NBT.UPGRADE_TANK_NAME, multiBlockInputTankNames.get(id));
                // We assume all tanks in the tank-array are equally sized
                tTag.setLong(NBT.UPGRADE_TANK_CAPACITY, tanks[0].capacity());
                tTag.setLong(NBT.UPGRADE_TANK_CAPACITY_MULTIPLIER, tanks[0].getCapacityMultiplier());
                tTag.setInteger(NBT.UPGRADE_TANKS_COUNT, tanks.length);
                for (int i = 0; i < tanks.length; i++) {
                    tanks[i].writeToNBT(tTag, NBT.UPGRADE_TANKS_PREFIX + i);
                }
                outputTankList.appendTag(tTag);
            }
        });
        nbt.setTag(NBT.UPGRADE_TANKS_INPUT, inputTankList);
        nbt.setTag(NBT.UPGRADE_TANKS_OUTPUT, outputTankList);
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound nbt) {
        super.readMultiTileNBT(nbt);

        // Multiblock inventories are a collection of inventories. The first inventory is the default internal
        // inventory, and the others are added by inventory extending blocks.
        if (inputInventory != null) registerInventory("controller", "controller", inputInventory, Inventory.INPUT);
        if (outputInventory != null) registerInventory("controller", "controller", outputInventory, Inventory.OUTPUT);

        if (inputTanks != null) registerFluidInventory("controller", "controller", inputTanks, Inventory.INPUT);
        if (outputTanks != null) registerFluidInventory("controller", "controller", outputTanks, Inventory.OUTPUT);

        structureOkay = nbt.getBoolean(NBT.STRUCTURE_OK);
        extendedFacing = ExtendedFacing
            .of(getFrontFacing(), Rotation.byIndex(nbt.getByte(NBT.ROTATION)), Flip.byIndex(nbt.getByte(NBT.FLIP)));

        loadUpgradeInventoriesFromNBT(nbt);
        loadUpgradeTanksFromNBT(nbt);

        voidExcess = nbt.getBoolean(NBT.VOID_EXCESS);
        separateInputs = nbt.getBoolean(NBT.SEPARATE_INPUTS);
        recipeLock = nbt.getBoolean(NBT.RECIPE_LOCK);
        batchMode = nbt.getBoolean(NBT.BATCH_MODE);
    }

    private void loadUpgradeInventoriesFromNBT(NBTTagCompound nbt) {
        final NBTTagList listInputInventories = nbt
            .getTagList(NBT.UPGRADE_INVENTORIES_INPUT, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listInputInventories.tagCount(); i++) {
            final NBTTagCompound nbtInv = listInputInventories.getCompoundTagAt(i);
            final String invUUID = nbtInv.getString(NBT.UPGRADE_INVENTORY_UUID);
            final String invName = nbtInv.getString(NBT.UPGRADE_INVENTORY_NAME);
            final int invSize = nbtInv.getInteger(NBT.UPGRADE_INVENTORY_SIZE);
            final IItemHandlerModifiable inv = new ItemStackHandler(invSize);
            loadInventory(nbtInv, inv, NBT.INV_INPUT_LIST);
            registerInventory(invName, invUUID, invSize, Inventory.INPUT);
        }

        final NBTTagList listOutputInventories = nbt
            .getTagList(NBT.UPGRADE_INVENTORIES_OUTPUT, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listOutputInventories.tagCount(); i++) {
            final NBTTagCompound nbtInv = listOutputInventories.getCompoundTagAt(i);
            final String invUUID = nbtInv.getString(NBT.UPGRADE_INVENTORY_UUID);
            final String invName = nbtInv.getString(NBT.UPGRADE_INVENTORY_NAME);
            final int invSize = nbtInv.getInteger(NBT.UPGRADE_INVENTORY_SIZE);
            IItemHandlerModifiable inv = new ItemStackHandler(invSize);
            loadInventory(nbtInv, inv, NBT.INV_OUTPUT_LIST);
            registerInventory(invName, invUUID, invSize, Inventory.OUTPUT);
        }
    }

    private void loadUpgradeTanksFromNBT(NBTTagCompound nbt) {
        final NBTTagList listInputTanks = nbt.getTagList(NBT.UPGRADE_TANKS_INPUT, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listInputTanks.tagCount(); i++) {
            final NBTTagCompound nbtTank = listInputTanks.getCompoundTagAt(i);
            String tankUUID = nbtTank.getString(NBT.UPGRADE_TANK_UUID);
            String tankName = nbtTank.getString(NBT.UPGRADE_TANK_NAME);
            long capacity = nbtTank.getLong(NBT.UPGRADE_TANK_CAPACITY);
            long capacityMultiplier = nbtTank.getLong(NBT.UPGRADE_TANK_CAPACITY_MULTIPLIER);
            int count = nbtTank.getInteger(NBT.UPGRADE_TANKS_COUNT);
            FluidTankGT[] tanks = new FluidTankGT[count];
            for (int j = 0; j < count; j++) {
                tanks[j] = new FluidTankGT(capacity).setCapacityMultiplier(capacityMultiplier)
                    .readFromNBT(nbtTank, NBT.UPGRADE_TANKS_PREFIX + j);
            }
            registerFluidInventory(tankName, tankUUID, count, capacity, capacityMultiplier, Inventory.INPUT);
        }

        final NBTTagList listOutputTanks = nbt.getTagList(NBT.UPGRADE_TANKS_OUTPUT, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listOutputTanks.tagCount(); i++) {
            final NBTTagCompound nbtTank = listOutputTanks.getCompoundTagAt(i);
            String tankUUID = nbtTank.getString(NBT.UPGRADE_TANK_UUID);
            String tankName = nbtTank.getString(NBT.UPGRADE_TANK_NAME);
            long capacity = nbtTank.getLong(NBT.UPGRADE_TANK_CAPACITY);
            long capacityMultiplier = nbtTank.getLong(NBT.UPGRADE_TANK_CAPACITY_MULTIPLIER);
            int count = nbtTank.getInteger(NBT.UPGRADE_TANKS_COUNT);
            FluidTankGT[] tanks = new FluidTankGT[count];
            for (int j = 0; j < count; j++) {
                tanks[j] = new FluidTankGT(capacity).setCapacityMultiplier(capacityMultiplier)
                    .readFromNBT(nbtTank, NBT.UPGRADE_TANKS_PREFIX + j);
            }
            registerFluidInventory(tankName, tankUUID, count, capacity, capacityMultiplier, Inventory.OUTPUT);
        }
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
            functionalCasings.clear();
            upgradeCasings.clear();
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
    public void onTick(long timer, boolean isServerSide) {
        if (!tickCovers()) {
            return;
        }
    }

    @Override
    public void onPostTick(long tick, boolean isServerSide) {
        if (isServerSide) {
            if (tick % 600 == 5) {
                clearSpecialLists();
                // Recheck the structure every 30 seconds or so
                if (!checkStructure(false)) checkStructure(true);
            }
            if (checkStructure(false)) {
                runMachine(tick);
                pushOutputs(tick);

            } else {
                stopMachine(false);
            }
        } else {
            doActivitySound(getActivitySoundLoop());
        }
    }

    protected void pushOutputs(long tick) {
        if (tick % AUTO_OUTPUT_FREQUENCY_TICK != 0) return;
        // Auto push items
        final LinkedList<WeakReference<IMultiBlockPart>> registeredItemOutputs = registeredTickableParts
            .get(MultiTileCasingPurpose.ItemOutput.ordinal());
        final Iterator<WeakReference<IMultiBlockPart>> itemOutputIterator = registeredItemOutputs.iterator();
        while (itemOutputIterator.hasNext()) {
            final IMultiBlockPart part = (itemOutputIterator.next()).get();
            if (part == null) {
                itemOutputIterator.remove();
                continue;
            }
            if (!part.tick(mTickTimer)) {
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
        // Auto push fluids
        final LinkedList<WeakReference<IMultiBlockPart>> registeredFluidOutputs = registeredTickableParts
            .get(MultiTileCasingPurpose.FluidOutput.ordinal());
        final Iterator<WeakReference<IMultiBlockPart>> fluidOutputIterator = registeredFluidOutputs.iterator();
        while (fluidOutputIterator.hasNext()) {
            final IMultiBlockPart part = (fluidOutputIterator.next()).get();
            if (part == null) {
                fluidOutputIterator.remove();
                continue;
            }
            if (!part.tick(mTickTimer)) {
                fluidOutputIterator.remove();
            } else {
                IFluidHandler targetTank = part.getITankContainerAtSide(part.getFrontFacing());
                FluidTankGT[] controllerTanks = multiBlockOutputTank
                    .getOrDefault(part.getLockedInventory(), getOutputTanks());
                if (targetTank != null && controllerTanks != null) {
                    for (FluidTankGT tank : controllerTanks) {
                        if (tank.get() == null) continue;
                        FluidStack tDrained = part.drain(part.getFrontFacing(), Math.max(1, tank.get().amount), false);
                        if (tDrained != null) {
                            int tFilledAmount = targetTank.fill(part.getBackFacing(), tDrained, false);
                            if (tFilledAmount > 0) {
                                targetTank.fill(
                                    part.getBackFacing(),
                                    part.drain(part.getFrontFacing(), tFilledAmount, true),
                                    true);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void clearSpecialLists() {
        upgradeCasings.clear();
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

    // IMachineProgress
    @Override
    public long getProgress() {
        return progressTime;
    }

    @Override
    public long getMaxProgress() {
        return maxProgressTime;
    }

    @Override
    public boolean increaseProgress(int aProgressAmountInTicks) {
        return increaseProgressGetOverflow(aProgressAmountInTicks) != aProgressAmountInTicks;
    }

    @Override
    public FluidStack getDrainableFluid(ForgeDirection side) {
        return getDrainableFluid(side, null);
    }

    @Override
    public FluidStack getDrainableFluid(ForgeDirection side, Fluid fluidToDrain) {
        final IFluidTank tank = getFluidTankDrainable(
            side,
            fluidToDrain == null ? null : new FluidStack(fluidToDrain, 0));
        return tank == null ? null : tank.getFluid();
    }

    /**
     * Increases the Progress, returns the overflown Progress.
     */
    public int increaseProgressGetOverflow(int aProgress) {
        return 0;
    }

    @Override
    public boolean hasThingsToDo() {
        return getMaxProgress() > 0;
    }

    public boolean isSeparateInputs() {
        return separateInputs;
    }

    public void setSeparateInputs(boolean aSeparateInputs) {
        separateInputs = aSeparateInputs;
    }

    // End IMachineProgress

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

    public <S> IStructureElement<S> addMultiTileCasing(String registryName, int meta, int modes) {
        MultiTileEntityRegistry registry = MultiTileEntityRegistry.getRegistry(registryName);
        int registryID = Block.getIdFromBlock(registry.mBlock);
        return addMultiTileCasing(registryID, meta, modes);
    }

    public <S> IStructureElement<S> addMultiTileCasing(int registryID, int meta, int modes) {
        return new IStructureElement<S>() {

            private final short[] DEFAULT = new short[] { 255, 255, 255, 0 };
            private IIcon[] mIcons = null;

            @Override
            public boolean check(S t, World world, int x, int y, int z) {
                final TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (!(tileEntity instanceof MultiBlockPart part)) return false;

                if (registryID != part.getMultiTileEntityRegistryID() || meta != part.getMultiTileEntityID())
                    return false;

                final IMultiBlockController tTarget = part.getTarget(false);
                if (tTarget != null && tTarget != t) return false;

                part.setTarget((IMultiBlockController) t, modes);

                ((Controller<?>) t).registerSpecialCasings(part);
                return true;
            }

            @Override
            public boolean spawnHint(S t, World world, int x, int y, int z, ItemStack trigger) {
                if (mIcons == null) {
                    mIcons = new IIcon[6];
                    Arrays.fill(mIcons, TextureSet.SET_NONE.mTextures[OrePrefixes.block.mTextureIndex].getIcon());
                    // Arrays.fill(mIcons, getTexture(aCasing);
                    // for (byte i : ALL_VALID_SIDES) {
                    // mIcons[i] = aCasing.getIcon(i, aMeta);
                    // }
                }
                final short[] RGBA = DEFAULT;
                StructureLibAPI.hintParticleTinted(world, x, y, z, mIcons, RGBA);
                // StructureLibAPI.hintParticle(world, x, y, z, aCasing, aMeta);
                return true;
            }

            @Override
            public boolean placeBlock(S t, World world, int x, int y, int z, ItemStack trigger) {
                final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry(registryID);
                final MultiTileEntityContainer tContainer = tRegistry
                    .getNewTileEntityContainer(world, x, y, z, meta, null);
                if (tContainer == null) {
                    GT_FML_LOGGER.error("NULL CONTAINER");
                    return false;
                }
                final IMultiTileEntity te = ((IMultiTileEntity) tContainer.mTileEntity);
                if (!(te instanceof MultiBlockPart)) {
                    GT_FML_LOGGER.error("Not a multiblock part");
                    return false;
                }
                if (world.setBlock(x, y, z, tContainer.mBlock, 15 - tContainer.mBlockMetaData, 2)) {
                    tContainer.setMultiTile(world, x, y, z);
                    ((MultiBlockPart) te).setTarget(Controller.this, modes);

                    ((Controller<?>) t).registerSpecialCasings((MultiBlockPart) te);
                }

                return false;
            }

            public IIcon getTexture(OrePrefixes aBlock) {
                return TextureSet.SET_NONE.mTextures[OrePrefixes.block.mTextureIndex].getIcon();
            }
        };
    }

    protected <S> IStructureElementChain<S> addMotorCasings(int modes) {
        return ofChain(
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, HV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, EV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, IV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LuV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, ZPM_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UHV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UEV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UIV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UMV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UXV_Motor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MAX_Motor.getId(), modes));
    }

    protected <S> IStructureElementChain<S> addPumpCasings(int modes) {
        return ofChain(
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, HV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, EV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, IV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LuV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, ZPM_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UHV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UEV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UIV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UMV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UXV_Pump.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MAX_Pump.getId(), modes));
    }

    protected <S> IStructureElementChain<S> addPistonCasings(int modes) {
        return ofChain(
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, HV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, EV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, IV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LuV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, ZPM_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UHV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UEV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UIV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UMV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UXV_Piston.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MAX_Piston.getId(), modes));
    }

    protected <S> IStructureElementChain<S> addConveyorCasings(int modes) {
        return ofChain(
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, HV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, EV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, IV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LuV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, ZPM_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UHV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UEV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UIV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UMV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UXV_Conveyor.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MAX_Conveyor.getId(), modes));
    }

    protected <S> IStructureElementChain<S> addRobotArmCasings(int modes) {
        return ofChain(
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, HV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, EV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, IV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LuV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, ZPM_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UHV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UEV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UIV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UMV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UXV_RobotArm.getId(), modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MAX_RobotArm.getId(), modes));
    }

    protected <S> IStructureElementChain<S> addSensorCasings(int Modes) {
        return ofChain(
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, HV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, EV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, IV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LuV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, ZPM_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UHV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UEV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UIV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UMV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UXV_Sensor.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MAX_Sensor.getId(), Modes));
    }

    protected <S> IStructureElementChain<S> addEmitterCasings(int Modes) {
        return ofChain(
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, HV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, EV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, IV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LuV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, ZPM_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UHV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UEV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UIV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UMV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UXV_Emitter.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MAX_Emitter.getId(), Modes));
    }

    protected <S> IStructureElementChain<S> addFieldGeneratorCasings(int Modes) {
        return ofChain(
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, HV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, EV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, IV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, LuV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, ZPM_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UHV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UEV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UIV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UMV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, UXV_FieldGenerator.getId(), Modes),
            addMultiTileCasing(COMPONENT_CASING_REGISTRY_NAME, MAX_FieldGenerator.getId(), Modes));
    }

    protected void registerSpecialCasings(MultiBlockPart part) {
        if (part instanceof UpgradeCasing) {
            upgradeCasings.add((UpgradeCasing) part);
        }
        if (part instanceof FunctionalCasing) {
            functionalCasings.add((FunctionalCasing) part);
        }
    }

    /**
     * Fluid - MultiBlock related Fluid Tank behaviour.
     */
    public void registerFluidInventory(String name, String id, int numberOfSlots, long capacity,
        long capacityMultiplier, int type) {
        if (name == null || name.equals("")
            || id == null
            || id.equals("")
            || numberOfSlots < 0
            || capacity < 0
            || capacityMultiplier < 0) {
            return;
        }
        FluidTankGT[] tanks = new FluidTankGT[numberOfSlots];
        for (int i = 0; i < numberOfSlots; i++) {
            tanks[i] = new FluidTankGT(capacity).setCapacityMultiplier(capacityMultiplier);
        }
        registerFluidInventory(name, id, tanks, type);
    }

    public void registerFluidInventory(String name, String id, FluidTankGT[] fluidInventory, int type) {
        if (name == null || name.equals("")
            || id == null
            || id.equals("")
            || fluidInventory == null
            || fluidInventory.length == 0) {
            return;
        }
        if (type == Inventory.INPUT || type == Inventory.BOTH) {
            if (multiBlockInputTank.containsKey(id)) return;
            multiBlockInputTank.put(id, fluidInventory);
            multiBlockInputTankNames.put(id, name);
        }
        if (type == Inventory.OUTPUT || type == Inventory.BOTH) {
            if (multiBlockOutputTank.containsKey(id)) return;
            multiBlockOutputTank.put(id, fluidInventory);
            multiBlockOutputTankNames.put(id, name);
        }
    }

    public void unregisterFluidInventory(String aName, String aID, int aType) {
        if ((aType == Inventory.INPUT || aType == Inventory.BOTH) && multiBlockInputTank.containsKey(aID)) {
            multiBlockInputTank.remove(aID, multiBlockInputTank.get(aID));
            multiBlockInputTankNames.remove(aID, aName);
        }
        if ((aType == Inventory.OUTPUT || aType == Inventory.BOTH) && multiBlockOutputTank.containsKey(aID)) {
            multiBlockOutputTank.remove(aID, multiBlockOutputTank.get(aID));
            multiBlockOutputTankNames.remove(aID, aName);
        }
    }

    protected FluidTankGT[] getTanksForInput() {
        List<FluidTankGT> tanks = new ArrayList<>();
        for (FluidTankGT[] inputTanks : multiBlockInputTank.values()) {
            tanks.addAll(Arrays.asList(inputTanks));
        }
        return tanks.toArray(new FluidTankGT[0]);
    }

    protected FluidTankGT[] getTanksForOutput() {
        List<FluidTankGT> tanks = new ArrayList<>();
        for (FluidTankGT[] outputTanks : multiBlockOutputTank.values()) {
            tanks.addAll(Arrays.asList(outputTanks));
        }
        return tanks.toArray(new FluidTankGT[0]);
    }

    protected IFluidTank getFluidTankFillable(MultiBlockPart aPart, ForgeDirection side, FluidStack aFluidToFill) {
        return getFluidTankFillable(side, aFluidToFill);
    }

    protected IFluidTank getFluidTankDrainable(MultiBlockPart aPart, ForgeDirection side, FluidStack aFluidToDrain) {
        return getFluidTankDrainable(side, aFluidToDrain);
    }

    protected IFluidTank[] getFluidTanks(MultiBlockPart aPart, ForgeDirection side) {
        return getFluidTanks(side);
    }

    @Override
    public int fill(MultiBlockPart aPart, ForgeDirection aDirection, FluidStack aFluid, boolean aDoFill) {
        if (aFluid == null || aFluid.amount <= 0) return 0;
        final IFluidTank tTank = getFluidTankFillable(aPart, aDirection, aFluid);
        if (tTank == null) return 0;
        final int rFilledAmount = tTank.fill(aFluid, aDoFill);
        if (rFilledAmount > 0 && aDoFill) hasInventoryChanged = true;
        return rFilledAmount;
    }

    @Override
    public FluidStack drain(MultiBlockPart aPart, ForgeDirection aDirection, FluidStack aFluid, boolean aDoDrain) {
        if (aFluid == null || aFluid.amount <= 0) return null;
        final IFluidTank tTank = getFluidTankDrainable(aPart, aDirection, aFluid);
        if (tTank == null || tTank.getFluid() == null
            || tTank.getFluidAmount() == 0
            || !tTank.getFluid()
                .isFluidEqual(aFluid))
            return null;
        final FluidStack rDrained = tTank.drain(aFluid.amount, aDoDrain);
        if (rDrained != null && aDoDrain) markInventoryBeenModified();
        return rDrained;
    }

    @Override
    public FluidStack drain(MultiBlockPart aPart, ForgeDirection aDirection, int aAmountToDrain, boolean aDoDrain) {
        if (aAmountToDrain <= 0) return null;
        final IFluidTank tTank = getFluidTankDrainable(aPart, aDirection, null);
        if (tTank == null || tTank.getFluid() == null || tTank.getFluidAmount() == 0) return null;
        final FluidStack rDrained = tTank.drain(aAmountToDrain, aDoDrain);
        if (rDrained != null && aDoDrain) markInventoryBeenModified();
        return rDrained;
    }

    @Override
    public boolean canFill(MultiBlockPart aPart, ForgeDirection aDirection, Fluid aFluid) {
        if (aFluid == null) return false;
        final IFluidTank tTank = getFluidTankFillable(aPart, aDirection, new FluidStack(aFluid, 0));
        return tTank != null && (tTank.getFluid() == null || tTank.getFluid()
            .getFluid() == aFluid);
    }

    @Override
    public boolean canDrain(MultiBlockPart aPart, ForgeDirection aDirection, Fluid aFluid) {
        if (aFluid == null) return false;
        final IFluidTank tTank = getFluidTankDrainable(aPart, aDirection, new FluidStack(aFluid, 0));
        return tTank != null && (tTank.getFluid() != null && tTank.getFluid()
            .getFluid() == aFluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(MultiBlockPart aPart, ForgeDirection aDirection) {
        final IFluidTank[] tTanks = getFluidTanks(aPart, aDirection);
        if (tTanks == null || tTanks.length <= 0) return GT_Values.emptyFluidTankInfo;
        final FluidTankInfo[] rInfo = new FluidTankInfo[tTanks.length];
        for (int i = 0; i < tTanks.length; i++) rInfo[i] = new FluidTankInfo(tTanks[i]);
        return rInfo;
    }

    @Override
    public IFluidTank[] getFluidTanksForGUI(MultiBlockPart aPart) {
        final String lockedInventory = aPart.getLockedInventory();
        if (lockedInventory == null) {
            if (aPart.modeSelected(MultiBlockPart.FLUID_IN)) return getTanksForInput();
            else if (aPart.modeSelected(MultiBlockPart.FLUID_OUT)) return getTanksForOutput();
        } else {
            final Map<String, FluidTankGT[]> tankMap = getMultiBlockTankArray(aPart);
            if (tankMap == null) return GT_Values.emptyFluidTank;
            final FluidTankGT[] tanks = tankMap.get(lockedInventory);
            return tanks != null ? tanks : GT_Values.emptyFluidTank;
        }
        return GT_Values.emptyFluidTank;
    }

    // #region Energy
    @Override
    public PowerLogic getPowerLogic(IMultiBlockPart part, ForgeDirection side) {
        if (!(this instanceof PowerLogicHost powerLogicHost)) {
            return null;
        }

        if (part.getFrontFacing() != side) {
            return null;
        }

        return powerLogicHost.getPowerLogic(side);
    }
    // #endregion Energy

    /**
     * Item - MultiBlock related Item behaviour.
     */
    @Override
    public void registerInventory(String aName, String aID, int aInventorySize, int aType) {
        registerInventory(aName, aID, new ItemStackHandler(aInventorySize), aType);
    }

    public void registerInventory(String name, String id, IItemHandlerModifiable inventory, int type) {
        if (name == null || name.equals("") || id == null || id.equals("") || inventory == null) {
            return;
        }
        if (type == Inventory.INPUT || type == Inventory.BOTH) {
            if (multiBlockInputInventory.containsKey(id)) return;
            multiBlockInputInventory.put(id, inventory);
            multiBlockInputInventoryNames.put(id, name);
        }
        if (type == Inventory.OUTPUT || type == Inventory.BOTH) {
            if (multiBlockOutputInventory.containsKey(id)) return;
            multiBlockOutputInventory.put(id, inventory);
            multiBlockOutputInventoryNames.put(id, name);
        }
    }

    @Override
    public void unregisterInventory(String aName, String aID, int aType) {
        if ((aType == Inventory.INPUT || aType == Inventory.BOTH) && multiBlockInputInventory.containsKey(aID)) {
            multiBlockInputInventory.remove(aID, multiBlockInputInventory.get(aID));
            multiBlockInputInventoryNames.remove(aID, aName);
        }
        if ((aType == Inventory.OUTPUT || aType == Inventory.BOTH) && multiBlockOutputInventory.containsKey(aID)) {
            multiBlockOutputInventory.remove(aID, multiBlockOutputInventory.get(aID));
            multiBlockOutputInventoryNames.remove(aID, aName);
        }
    }

    @Override
    public void changeInventoryName(String aName, String aID, int aType) {
        if ((aType == Inventory.INPUT || aType == Inventory.BOTH) && multiBlockInputInventoryNames.containsKey(aID)) {
            multiBlockInputInventoryNames.put(aID, aName);
        }
        if ((aType == Inventory.OUTPUT || aType == Inventory.BOTH) && multiBlockOutputInventoryNames.containsKey(aID)) {
            multiBlockOutputInventoryNames.put(aID, aName);
        }
    }

    @Override
    public boolean hasInventoryBeenModified(MultiBlockPart aPart) {
        if (aPart.modeSelected(MultiBlockPart.ITEM_IN)) return hasInventoryBeenModified();
        else if (aPart.modeSelected(MultiBlockPart.ITEM_OUT)) return hasOutputInventoryBeenModified();

        return false;
    }

    @Override
    public boolean isValidSlot(MultiBlockPart aPart, int aIndex) {
        return false;
    }

    @Override
    public void enableWorking() {
        super.enableWorking();
        if (!structureOkay) {
            checkStructure(true);
        }
    }

    @Override
    public IItemHandlerModifiable getInventoryForGUI(MultiBlockPart aPart) {
        if (isServerSide()) {
            for (UpgradeCasing tPart : upgradeCasings) {
                if (!(tPart instanceof Inventory)) continue;
                tPart.issueClientUpdate();
            }
        }
        final Map<String, IItemHandlerModifiable> multiBlockInventory = getMultiBlockInventory(aPart);
        if (multiBlockInventory == null) return null;

        final String lockedInventory = aPart.getLockedInventory();
        if (lockedInventory == null) {
            return new ListItemHandler(multiBlockInventory.values());
        } else {
            final IItemHandlerModifiable inv = multiBlockInventory.get(lockedInventory);
            return inv != null ? inv : null;
        }
    }

    @Override
    public boolean addStackToSlot(MultiBlockPart aPart, int aIndex, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean addStackToSlot(MultiBlockPart aPart, int aIndex, ItemStack aStack, int aAmount) {
        return false;
    }

    protected Map<String, FluidTankGT[]> getMultiBlockTankArray(MultiBlockPart aPart) {
        if (aPart.modeSelected(MultiBlockPart.FLUID_IN)) return multiBlockInputTank;
        else if (aPart.modeSelected(MultiBlockPart.FLUID_OUT)) return multiBlockOutputTank;
        return null;
    }

    protected Map<String, String> getMultiBlockTankArrayNames(MultiBlockPart aPart) {
        if (aPart.modeSelected(MultiBlockPart.FLUID_IN)) return multiBlockInputTankNames;
        else if (aPart.modeSelected(MultiBlockPart.FLUID_OUT)) return multiBlockOutputTankNames;
        return null;
    }

    protected Map<String, IItemHandlerModifiable> getMultiBlockInventory(MultiBlockPart aPart) {
        if (aPart.modeSelected(MultiBlockPart.ITEM_IN)) return multiBlockInputInventory;
        else if (aPart.modeSelected(MultiBlockPart.ITEM_OUT)) return multiBlockOutputInventory;
        return null;
    }

    protected Map<String, String> getMultiBlockInventoryNames(MultiBlockPart aPart) {
        if (aPart.modeSelected(MultiBlockPart.ITEM_IN)) return multiBlockInputInventoryNames;
        else if (aPart.modeSelected(MultiBlockPart.ITEM_OUT)) return multiBlockOutputInventoryNames;
        return null;
    }

    protected Pair<IItemHandlerModifiable, Integer> getInventory(MultiBlockPart aPart, int aSlot) {
        final Map<String, IItemHandlerModifiable> multiBlockInventory = getMultiBlockInventory(aPart);
        if (multiBlockInventory == null) return null;

        final String invName = aPart.getLockedInventory();
        if (invName != null && !invName.isEmpty()) return new ImmutablePair<>(multiBlockInventory.get(invName), aSlot);

        int start = 0;
        for (IItemHandlerModifiable inv : multiBlockInventory.values()) {
            if (aSlot >= start && aSlot < start + inv.getSlots()) {
                return new ImmutablePair<>(inv, aSlot - start);
            }
            start += inv.getSlots();
        }
        return null;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(MultiBlockPart aPart, ForgeDirection side) {
        final TIntList tList = new TIntArrayList();
        final Map<String, IItemHandlerModifiable> multiBlockInventory = getMultiBlockInventory(aPart);
        if (multiBlockInventory == null) return tList.toArray();

        final String lockedInventory = aPart.getLockedInventory();
        // Item in --> input inv
        // Item out --> output inv

        int start = 0;
        if (lockedInventory == null) {
            for (IItemHandlerModifiable inv : multiBlockInventory.values()) {
                for (int i = start; i < inv.getSlots() + start; i++) tList.add(i);
                start += inv.getSlots();
            }
        } else {
            final IItemHandlerModifiable inv = multiBlockInventory.get(lockedInventory);
            final int len = inv != null ? inv.getSlots() : 0;
            for (int i = 0; i < len; i++) tList.add(i);
        }
        return tList.toArray();
    }

    @Override
    public boolean canInsertItem(MultiBlockPart aPart, int aSlot, ItemStack aStack, ForgeDirection side) {
        final Pair<IItemHandlerModifiable, Integer> tInv = getInventory(aPart, aSlot);
        if (tInv == null) return false;

        final int tSlot = tInv.getRight();
        final IItemHandlerModifiable inv = tInv.getLeft();

        return inv.getStackInSlot(tSlot) == null || GT_Utility.areStacksEqual(aStack, inv.getStackInSlot(tSlot)); // &&
                                                                                                                  // allowPutStack(getBaseMetaTileEntity(),
                                                                                                                  // aIndex,
                                                                                                                  // (byte)
                                                                                                                  // aSide,
                                                                                                                  // aStack)
    }

    @Override
    public boolean canExtractItem(MultiBlockPart aPart, int aSlot, ItemStack aStack, ForgeDirection side) {
        final Pair<IItemHandlerModifiable, Integer> tInv = getInventory(aPart, aSlot);
        if (tInv == null) return false;

        final int tSlot = tInv.getRight();
        final IItemHandlerModifiable inv = tInv.getLeft();

        return inv.getStackInSlot(tSlot) != null; // && allowPullStack(getBaseMetaTileEntity(), aIndex, (byte) aSide,
                                                  // aStack);
    }

    @Override
    public int getSizeInventory(MultiBlockPart aPart) {
        final Map<String, IItemHandlerModifiable> multiBlockInventory = getMultiBlockInventory(aPart);
        if (multiBlockInventory == null) return 0;

        final String lockedInventory = aPart.getLockedInventory();
        if (lockedInventory == null) {
            int len = 0;
            for (IItemHandlerModifiable inv : multiBlockInventory.values()) len += inv.getSlots();
            return len;
        } else {
            final IItemHandlerModifiable inv = multiBlockInventory.get(lockedInventory);
            return inv != null ? inv.getSlots() : 0;
        }
    }

    @Override
    public ItemStack getStackInSlot(MultiBlockPart aPart, int aSlot) {
        final Pair<IItemHandlerModifiable, Integer> tInv = getInventory(aPart, aSlot);
        if (tInv == null) return null;

        final int tSlot = tInv.getRight();
        final IItemHandlerModifiable inv = tInv.getLeft();
        if (inv == null) return null;

        return inv.getStackInSlot(tSlot);
    }

    @Override
    public ItemStack decrStackSize(MultiBlockPart aPart, int aSlot, int aDecrement) {
        final ItemStack tStack = getStackInSlot(aPart, aSlot);
        ItemStack rStack = GT_Utility.copyOrNull(tStack);
        if (tStack != null) {
            if (tStack.stackSize <= aDecrement) {
                setInventorySlotContents(aPart, aSlot, null);
            } else {
                rStack = tStack.splitStack(aDecrement);
                if (tStack.stackSize == 0) setInventorySlotContents(aPart, aSlot, null);
            }
        }
        return rStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(MultiBlockPart aPart, int aSlot) {
        final Pair<IItemHandlerModifiable, Integer> tInv = getInventory(aPart, aSlot);
        if (tInv == null) return null;

        final IItemHandlerModifiable inv = tInv.getLeft();
        final int tSlot = tInv.getRight();

        final ItemStack rStack = inv.getStackInSlot(tSlot);
        inv.setStackInSlot(tSlot, null);
        return rStack;
    }

    @Override
    public void setInventorySlotContents(MultiBlockPart aPart, int aSlot, ItemStack aStack) {
        final Pair<IItemHandlerModifiable, Integer> tInv = getInventory(aPart, aSlot);
        if (tInv == null) return;

        final IItemHandlerModifiable inv = tInv.getLeft();
        final int tSlot = tInv.getRight();
        inv.setStackInSlot(tSlot, aStack);
    }

    @Override
    public List<String> getInventoryNames(MultiBlockPart aPart) {
        final List<String> inventoryNames = new ArrayList<>();
        inventoryNames.add(ALL_INVENTORIES_NAME);
        inventoryNames.addAll(getMultiBlockInventoryNames(aPart).values());
        return inventoryNames;
    }

    @Override
    public List<String> getInventoryIDs(MultiBlockPart aPart) {
        final List<String> tInventoryIDs = new ArrayList<>();
        tInventoryIDs.add(ALL_INVENTORIES_NAME);
        tInventoryIDs.addAll(getMultiBlockInventory(aPart).keySet());
        return tInventoryIDs;
    }

    @Override
    public String getInventoryName(MultiBlockPart aPart) {
        final StringBuilder str = new StringBuilder();
        str.append(getInventoryName());
        if (aPart.modeSelected(MultiBlockPart.ITEM_IN)) {
            str.append(" Input");
        } else if (aPart.modeSelected(MultiBlockPart.ITEM_OUT)) {
            str.append(" Output");
            String a;
        } else {
            str.append(" Unknown");
        }
        final String lockedInventory = aPart.getLockedInventory();
        if (lockedInventory != null && !lockedInventory.equals("")) {
            str.append(" [Locked: ")
                .append(lockedInventory)
                .append("]");
        }

        return str.toString();
    }

    @Override
    public List<String> getTankArrayNames(MultiBlockPart aPart) {
        final List<String> inventoryNames = new ArrayList<>();
        inventoryNames.add(ALL_INVENTORIES_NAME);
        inventoryNames.addAll(getMultiBlockTankArrayNames(aPart).values());
        return inventoryNames;
    }

    @Override
    public List<String> getTankArrayIDs(MultiBlockPart aPart) {
        final List<String> inventoryIDs = new ArrayList<>();
        inventoryIDs.add(ALL_INVENTORIES_NAME);
        inventoryIDs.addAll(getMultiBlockTankArray(aPart).keySet());
        return inventoryIDs;
    }

    @Override
    public boolean hasCustomInventoryName(MultiBlockPart aPart) {
        return hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit(MultiBlockPart aPart) {
        return getInventoryStackLimit();
    }

    @Override
    public void markDirty(MultiBlockPart aPart) {
        markDirty();
        if (aPart.modeSelected(MultiBlockPart.ITEM_OUT)) markOutputInventoryBeenModified();
        else markInventoryBeenModified();
    }

    @Override
    public boolean isUseableByPlayer(MultiBlockPart aPart, EntityPlayer aPlayer) {
        return isUseableByPlayer(aPlayer);
    }

    @Override
    public void openInventory(MultiBlockPart aPart) {
        // TODO: MultiInventory - consider the part's inventory
        openInventory();
    }

    @Override
    public void closeInventory(MultiBlockPart aPart) {
        // TODO: MultiInventory - consider the part's inventory
        closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(MultiBlockPart aPart, int aSlot, ItemStack aStack) {
        return isItemValidForSlot(aSlot, aStack);
    }

    /*
     * Helper Methods For Recipe checking
     */

    @Override
    protected ItemStack[] getInputItems() {
        return getInventoriesForInput().getStacks()
            .toArray(new ItemStack[0]);
    }

    protected ItemStack[] getOutputItems() {
        return getInventoriesForOutput().getStacks()
            .toArray(new ItemStack[0]);
    }

    protected Iterable<Pair<ItemStack[], String>> getItemInputsForEachInventory() {
        return multiBlockInputInventory.entrySet()
            .stream()
            .map(
                (entry) -> Pair.of(
                    entry.getValue()
                        .getStacks()
                        .toArray(new ItemStack[0]),
                    entry.getKey()))
            .collect(Collectors.toList());
    }

    protected ItemStack[] getItemInputsForInventory(String id) {
        IItemHandlerModifiable inventory = multiBlockInputInventory.get(id);
        if (inventory != null) {
            return inventory.getStacks()
                .toArray(new ItemStack[0]);
        }
        return null;
    }

    @Override
    protected FluidStack[] getInputFluids() {
        List<FluidStack> fluidStacks = new ArrayList<>();
        for (FluidTankGT[] inputTanks : multiBlockInputTank.values()) {
            for (FluidTankGT inputTank : inputTanks) {
                FluidStack fluidStack = inputTank.get();
                if (fluidStack != null) {
                    fluidStacks.add(fluidStack);
                }
            }
        }
        return fluidStacks.toArray(new FluidStack[0]);
    }

    protected FluidStack[] getOutputFluids() {
        List<FluidStack> fluidStacks = new ArrayList<>();
        for (FluidTankGT[] inputTanks : multiBlockInputTank.values()) {
            for (FluidTankGT inputTank : inputTanks) {
                FluidStack fluidStack = inputTank.getFluid();
                if (fluidStack != null) {
                    fluidStacks.add(fluidStack);
                }
            }
        }
        return fluidStacks.toArray(new FluidStack[0]);
    }

    protected Iterable<Pair<FluidStack[], String>> getFluidInputsForEachTankArray() {
        return multiBlockInputTank.entrySet()
            .stream()
            .map((entry) -> Pair.of(FluidTankGT.getFluidsFromTanks(entry.getValue()), entry.getKey()))
            .collect(Collectors.toList());
    }

    protected FluidStack[] getFluidInputsForTankArray(String id) {
        return FluidTankGT.getFluidsFromTanks(multiBlockInputTank.get(id));
    }

    protected void setItemOutputs(String inventory, ItemStack... itemOutputs) {
        itemsToOutput = itemOutputs;
        inventoryName = inventory;
    }

    @Override
    protected void setItemOutputs(ItemStack... outputs) {
        super.setItemOutputs(outputs);
        inventoryName = null;
    }

    @Override
    protected void outputItems() {
        if (itemsToOutput == null) {
            return;
        }

        IItemHandlerModifiable inv;
        if (inventoryName != null) {
            inv = multiBlockOutputInventory.getOrDefault(inventoryName, getInventoriesForOutput());
        } else {
            inv = getInventoriesForOutput();
        }
        for (ItemStack item : itemsToOutput) {
            int index = 0;
            while (item != null && item.stackSize > 0 && index < inv.getSlots()) {
                item = inv.insertItem(index++, item.copy(), false);
            }
        }
        itemsToOutput = null;
    }

    protected void setFluidOutputs(String tank, FluidStack... fluidOuputs) {
        fluidsToOutput = fluidOuputs;
        tankName = tank;
    }

    @Override
    protected void setFluidOutputs(FluidStack... outputs) {
        super.setFluidOutputs(outputs);
        tankName = null;
    }

    @Override
    protected void outputFluids() {
        if (fluidsToOutput == null) {
            return;
        }

        List<FluidTankGT> tanks = Arrays.asList(outputTanks);
        for (FluidStack fluid : fluidsToOutput) {
            int index = 0;
            while (fluid != null && fluid.amount > 0 && index < tanks.size()) {
                int filled = tanks.get(index++)
                    .fill(fluid, true);
                fluid.amount -= filled;
            }
        }
    }

    @Override
    protected void updateSlots() {
        IItemHandlerModifiable inv = getInventoriesForInput();
        for (int i = 0; i < inv.getSlots(); i++) {
            if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).stackSize <= 0) {
                inv.setStackInSlot(i, null);
            }
        }

        for (FluidTankGT inputTank : getTanksForInput()) {
            if (inputTank == null) {
                continue;
            }

            if (inputTank.get() != null && inputTank.get().amount <= 0) {
                inputTank.setEmpty();
                continue;
            }

            FluidStack afterRecipe = inputTank.get();
            FluidStack beforeRecipe = inputTank.get(Integer.MAX_VALUE);
            if (afterRecipe == null || beforeRecipe == null) {
                continue;
            }
            int difference = beforeRecipe.amount - afterRecipe.amount;
            inputTank.remove(difference);
        }
    }

    @Override
    protected boolean checkRecipe() {
        if (!(this instanceof ProcessingLogicHost)) {
            return false;
        }
        ProcessingLogic logic = ((ProcessingLogicHost) this).getProcessingLogic();
        logic.clear();
        boolean result = false;
        if (isSeparateInputs()) {
            // TODO: Add separation with fluids
            for (Pair<ItemStack[], String> inventory : getItemInputsForEachInventory()) {
                IItemHandlerModifiable outputInventory = multiBlockOutputInventory
                    .getOrDefault(inventory.getLeft(), null);
                result = logic.setInputItems(inventory.getLeft())
                    .setCurrentOutputItems(getOutputItems())
                    .process();
                if (result) {
                    inventoryName = inventory.getRight();
                    break;
                }
                logic.clear();
            }
        } else {
            result = logic.setInputItems(getInputItems())
                .setCurrentOutputItems(getOutputItems())
                .setInputFluids(getInputFluids())
                .setCurrentOutputFluids(getOutputFluids())
                .process();
        }
        setDuration(logic.getDuration());
        setEut(logic.getEut());
        setItemOutputs(logic.getOutputItems());
        setFluidOutputs(logic.getOutputFluids());
        return result;
    }

    public IItemHandlerModifiable getOutputInventory() {
        return outputInventory;
    }

    public FluidTankGT[] getOutputTanks() {
        return outputTanks;
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
            .addPage(createMainPage().setSize(getGUIWidth(), getGUIHeight()));
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
                    .setPos(20 * (page - 1), -20))
                .addPage(
                    new MultiChildWidget().addChild(getItemInventoryInputGUI())
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
                    .setPos(20 * (page - 1), -20))
                .addPage(
                    new MultiChildWidget().addChild(getItemInventoryOutputGUI())
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
                    .setPos(20 * (page - 1), -20))
                .addPage(
                    new MultiChildWidget().addChild(getFluidInventoryInputGUI())
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
                    .setPos(20 * (page - 1), -20))
                .addPage(
                    new MultiChildWidget().addChild(getFluidInventoryOutputGUI())
                        .addChild(getGregTechLogo().setPos(147, 86))
                        .setSize(getGUIWidth(), getGUIHeight()));
        }
        builder.widget(tabs);
    }

    protected MultiChildWidget createMainPage() {
        MultiChildWidget page = new MultiChildWidget();
        page.addChild(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 4)
                .setSize(160, 75))
            .addChild(createButtons());
        return page;
    }

    protected MultiChildWidget createButtons() {
        MultiChildWidget buttons = new MultiChildWidget();
        buttons.setSize(16, 167)
            .setPos(7, 86);
        buttons.addChild(createPowerSwitchButton())
            .addChild(new FakeSyncWidget.BooleanSyncer(this::isAllowedToWork, val -> {
                if (val) enableWorking();
                else disableWorking();
            }))
            .addChild(createVoidExcessButton())
            .addChild(new FakeSyncWidget.BooleanSyncer(() -> voidExcess, val -> voidExcess = val))
            .addChild(createInputSeparationButton())
            .addChild(new FakeSyncWidget.BooleanSyncer(() -> separateInputs, val -> separateInputs = val))
            .addChild(createBatchModeButton())
            .addChild(new FakeSyncWidget.BooleanSyncer(() -> batchMode, val -> batchMode = val))
            .addChild(createLockToSingleRecipeButton())
            .addChild(new FakeSyncWidget.BooleanSyncer(() -> recipeLock, val -> recipeLock = val));

        return buttons;
    }

    protected Widget getItemInventoryInputGUI() {
        final IItemHandlerModifiable inv = getInventoriesForInput();
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int rows = 0; rows * 4 < Math.min(inv.getSlots(), 128); rows++) {
            final int columnsToMake = Math.min(Math.min(inv.getSlots(), 128) - rows * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                scrollable.widget(
                    new SlotWidget(inv, rows * 4 + column).setPos(column * 18, rows * 18)
                        .setSize(18, 18));
            }
        }
        return scrollable.setSize(18 * 4 + 4, 18 * 5)
            .setPos(52, 7);
    }

    protected Widget getItemInventoryOutputGUI() {
        final IItemHandlerModifiable inv = getInventoriesForOutput();
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int rows = 0; rows * 4 < Math.min(inv.getSlots(), 128); rows++) {
            final int columnsToMake = Math.min(Math.min(inv.getSlots(), 128) - rows * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                scrollable.widget(
                    new SlotWidget(inv, rows * 4 + column).setPos(column * 18, rows * 18)
                        .setSize(18, 18));
            }
        }
        return scrollable.setSize(18 * 4 + 4, 18 * 5)
            .setPos(52, 7);
    }

    protected IItemHandlerModifiable getInventoriesForInput() {
        return new ListItemHandler(multiBlockInputInventory.values());
    }

    protected IItemHandlerModifiable getInventoriesForOutput() {
        return new ListItemHandler(multiBlockOutputInventory.values());
    }

    protected Widget getFluidInventoryInputGUI() {
        final IFluidTank[] tanks = getTanksForInput();
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int rows = 0; rows * 4 < tanks.length; rows++) {
            final int columnsToMake = Math.min(tanks.length - rows * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                final FluidSlotWidget fluidSlot = new FluidSlotWidget(tanks[rows * 4 + column]);
                scrollable.widget(
                    fluidSlot.setPos(column * 18, rows * 18)
                        .setSize(18, 18));
            }
        }
        return scrollable.setSize(18 * 4 + 4, 18 * 5)
            .setPos(52, 7);
    }

    protected Widget getFluidInventoryOutputGUI() {
        final IFluidTank[] tanks = getTanksForOutput();
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int rows = 0; rows * 4 < tanks.length; rows++) {
            final int columnsToMake = Math.min(tanks.length - rows * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                final FluidSlotWidget fluidSlot = new FluidSlotWidget(tanks[rows * 4 + column]);
                fluidSlot.setInteraction(true, false);
                scrollable.widget(
                    fluidSlot.setPos(column * 18, rows * 18)
                        .setSize(18, 18));
            }
        }
        return scrollable.setSize(18 * 4 + 4, 18 * 5)
            .setPos(52, 7);
    }

    protected ButtonWidget createPowerSwitchButton() {
        ButtonWidget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isAllowedToWork()) {
                disableWorking();
            } else {
                enableWorking();
            }
        })
            .setPlayClickSoundResource(
                () -> isAllowedToWork() ? SoundResource.GUI_BUTTON_UP.resourceLocation
                    : SoundResource.GUI_BUTTON_DOWN.resourceLocation);
        button.setBackground(() -> {
            List<UITexture> ret = new ArrayList<>();
            ret.add(GT_UITextures.BUTTON_STANDARD);
            if (isAllowedToWork()) {
                ret.add(GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
            } else {
                ret.add(GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF);
            }
            return ret.toArray(new IDrawable[0]);
        })
            .setPos(144, 0)
            .setSize(16, 16);
        button.addTooltip(StatCollector.translateToLocal("GT5U.gui.button.power_switch"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
        return button;
    }

    protected ButtonWidget createVoidExcessButton() {
        ButtonWidget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isVoidExcessButtonEnabled()) {
                voidExcess = !voidExcess;
            }
        })
            .setPlayClickSound(true);
        button.setBackground(() -> {
            List<UITexture> ret = new ArrayList<>();
            ret.add(GT_UITextures.BUTTON_STANDARD);
            if (isVoidExcessButtonEnabled()) {
                if (isVoidExcessEnabled()) {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_VOID_EXCESS_ON);
                } else {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_VOID_EXCESS_OFF);
                }
            } else {
                if (isVoidExcessEnabled()) {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_VOID_EXCESS_ON_DISABLED);
                } else {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_VOID_EXCESS_OFF_DISABLED);
                }
            }
            return ret.toArray(new IDrawable[0]);
        })
            .setPos(54, 0)
            .setSize(16, 16);
        button.addTooltip(StatCollector.translateToLocal("GT5U.gui.button.void_excess"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
        return button;
    }

    protected boolean isVoidExcessEnabled() {
        return voidExcess;
    }

    protected boolean isVoidExcessButtonEnabled() {
        return true;
    }

    protected ButtonWidget createInputSeparationButton() {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isInputSeparationButtonEnabled()) {
                separateInputs = !separateInputs;
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GT_UITextures.BUTTON_STANDARD);
                if (isInputSeparationButtonEnabled()) {
                    if (isInputSeparationEnabled()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF);
                    }
                } else {
                    if (isInputSeparationEnabled()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF_DISABLED);
                    }
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(36, 0)
            .setSize(16, 16);
        button.addTooltip(StatCollector.translateToLocal("GT5U.gui.button.input_separation"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
        return (ButtonWidget) button;
    }

    protected boolean isInputSeparationEnabled() {
        return separateInputs;
    }

    protected boolean isInputSeparationButtonEnabled() {
        return true;
    }

    protected ButtonWidget createBatchModeButton() {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isBatchModeButtonEnabled()) {
                batchMode = !batchMode;
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GT_UITextures.BUTTON_STANDARD);
                if (isBatchModeButtonEnabled()) {
                    if (isBatchModeEnabled()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_ON);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_OFF);
                    }
                } else {
                    if (isBatchModeEnabled()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED);
                    }
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(18, 0)
            .setSize(16, 16);
        button.addTooltip(StatCollector.translateToLocal("GT5U.gui.button.batch_mode"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
        return (ButtonWidget) button;
    }

    protected boolean isBatchModeButtonEnabled() {
        return true;
    }

    protected boolean isBatchModeEnabled() {
        return batchMode;
    }

    protected ButtonWidget createLockToSingleRecipeButton() {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (supportsSingleRecipeLocking()) {
                recipeLock = !recipeLock;
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GT_UITextures.BUTTON_STANDARD);
                if (supportsSingleRecipeLocking()) {
                    if (isRecipeLockingEnabled()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_LOCKED);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED);
                    }
                } else {
                    if (isRecipeLockingEnabled()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED_DISABLED);
                    }
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(0, 0)
            .setSize(16, 16);
        button.addTooltip(StatCollector.translateToLocal("GT5U.gui.button.lock_recipe"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
        return (ButtonWidget) button;
    }

    protected boolean supportsSingleRecipeLocking() {
        return false;
    }

    protected boolean isRecipeLockingEnabled() {
        return recipeLock;
    }

    @Override
    public ModularWindow createWindowGUI(UIBuildContext buildContext) {
        return createWindow(buildContext);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("progress", progressTime);
        tag.setLong("maxProgress", maxProgressTime);
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
            currentTip
                .add(GT_Waila.getMachineProgressString(isActive, tag.getLong("maxProgress"), tag.getLong("progress")));
        }
    }
}
