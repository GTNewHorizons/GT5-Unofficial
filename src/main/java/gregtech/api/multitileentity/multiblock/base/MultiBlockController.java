package gregtech.api.multitileentity.multiblock.base;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.GT_Values.NBT;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.forge.ListItemHandler;
import com.gtnewhorizons.modularui.api.screen.*;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IDescribable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.multitileentity.MultiTileEntityContainer;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.IMultiBlockFluidHandler;
import gregtech.api.multitileentity.interfaces.IMultiBlockInventory;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_AddToolTips;
import gregtech.api.multitileentity.machine.MultiTileBasicMachine;
import gregtech.api.multitileentity.multiblock.casing.AdvancedCasing;
import gregtech.api.multitileentity.multiblock.casing.InventoryUpgrade;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public abstract class MultiBlockController<T extends MultiBlockController<T>> extends MultiTileBasicMachine
        implements IAlignment, IConstructable, IMultiBlockController, IDescribable, IMachineProgress,
        IMultiBlockFluidHandler, IMultiBlockInventory, IMTE_AddToolTips {

    private static final int TICKS_BETWEEN_RECIPE_CHECKS = 100;
    private static final Map<Integer, GT_Multiblock_Tooltip_Builder> tooltip = new ConcurrentHashMap<>();
    private final List<AdvancedCasing> mUpgradeCasings = new ArrayList<AdvancedCasing>();
    protected BuildState buildState = new BuildState();

    protected Map<String, String> multiBlockInputInventoryNames = new LinkedHashMap<>();
    protected Map<String, String> multiBlockOutputInventoryNames = new LinkedHashMap<>();
    protected Map<String, IItemHandlerModifiable> multiBlockInputInventory = new LinkedHashMap<>();
    protected Map<String, IItemHandlerModifiable> multiBlockOutputInventory = new LinkedHashMap<>();

    protected int mMaxProgressTime = 0;
    private int mProgressTime = 0;
    private boolean mStructureOkay = false, mStructureChanged = false;
    private boolean mWorks = true, mWorkUpdate = false, mWasShutdown = false, mActive = false, mSeparateInputs = true;
    private ExtendedFacing mExtendedFacing = ExtendedFacing.DEFAULT;
    private IAlignmentLimits mLimits = getInitialAlignmentLimits();
    private ItemStack[] mItemsToOutput;
    private String mInventory;

    // A list of sides
    // Each side has a list of parts that have a cover that need to be ticked
    protected List<LinkedList<WeakReference<IMultiBlockPart>>> registeredCoveredParts = Arrays.asList(
            new LinkedList<>(),
            new LinkedList<>(),
            new LinkedList<>(),
            new LinkedList<>(),
            new LinkedList<>(),
            new LinkedList<>());

    /** Registry ID of the required casing */
    public abstract short getCasingRegistryID();

    /** Meta ID of the required casing */
    public abstract short getCasingMeta();

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
    public abstract boolean checkMachine();

    /**
     * Checks the Recipe
     */
    public abstract boolean checkRecipe(ItemStack aStack);

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        super.writeMultiTileNBT(aNBT);

        aNBT.setBoolean(NBT.STRUCTURE_OK, mStructureOkay);
        aNBT.setByte(
                NBT.ROTATION,
                (byte) mExtendedFacing.getRotation()
                                      .getIndex());
        aNBT.setByte(
                NBT.FLIP,
                (byte) mExtendedFacing.getFlip()
                                      .getIndex());

        saveUpgradeInventoriesToNBT(aNBT);
        saveItemsToOutput(aNBT);
    }

    private void saveUpgradeInventoriesToNBT(NBTTagCompound aNBT) {
        final NBTTagList tListInputInvs = new NBTTagList();
        multiBlockInputInventory.forEach((tID, tInv) -> {
            if (tID.equals("controller")) {} else {
                final NBTTagCompound tTag = new NBTTagCompound();
                tTag.setString(NBT.UPGRADE_INVENTORY_UUID, tID);
                tTag.setString(NBT.UPGRADE_INVENTORY_NAME, multiBlockInputInventoryNames.get(tID));
                tTag.setInteger(NBT.UPGRADE_INVENTORY_SIZE, tInv.getSlots());
                writeInventory(tTag, tInv, NBT.INV_INPUT_LIST);
                tListInputInvs.appendTag(tTag);
            }
        });
        final NBTTagList tListOutputInvs = new NBTTagList();
        multiBlockOutputInventory.forEach((tID, tInv) -> {
            if (tID.equals("controller")) {} else {
                final NBTTagCompound tTag = new NBTTagCompound();
                tTag.setString(NBT.UPGRADE_INVENTORY_UUID, tID);
                tTag.setString(NBT.UPGRADE_INVENTORY_NAME, multiBlockOutputInventoryNames.get(tID));
                tTag.setInteger(NBT.UPGRADE_INVENTORY_SIZE, tInv.getSlots());
                writeInventory(tTag, tInv, NBT.INV_OUTPUT_LIST);
                tListOutputInvs.appendTag(tTag);
            }
        });
        aNBT.setTag(NBT.UPGRADE_INVENTORIES_INPUT, tListInputInvs);
        aNBT.setTag(NBT.UPGRADE_INVENTORIES_OUTPUT, tListOutputInvs);
    }

    private void saveItemsToOutput(NBTTagCompound aNBT) {
        final NBTTagList tList = new NBTTagList();
        for (int tSlot = 0; tSlot < mItemsToOutput.length; tSlot++) {
            final ItemStack tStack = mItemsToOutput[tSlot];
            if (tStack != null) {
                final NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("s", (byte) tSlot);
                tStack.writeToNBT(tag);
                tList.appendTag(tag);
            }
        }
        aNBT.setTag(NBT.ITEM_OUT, tList);
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);

        // Multiblock inventories are a collection of inventories. The first inventory is the default internal
        // inventory, and the others are added by inventory extending blocks.
        if (mInputInventory != null) multiBlockInputInventory.put("controller", mInputInventory);
        if (mOutputInventory != null) multiBlockOutputInventory.put("controller", mOutputInventory);

        mStructureOkay = aNBT.getBoolean(NBT.STRUCTURE_OK);
        mExtendedFacing = ExtendedFacing.of(
                ForgeDirection.getOrientation(getFrontFacing()),
                Rotation.byIndex(aNBT.getByte(NBT.ROTATION)),
                Flip.byIndex(aNBT.getByte(NBT.FLIP)));

        loadUpgradeInventoriesFromNBT(aNBT);
        loadItemsToOutput(aNBT);
    }

    private void loadUpgradeInventoriesFromNBT(NBTTagCompound aNBT) {
        final NBTTagList tListInput = aNBT.getTagList(NBT.UPGRADE_INVENTORIES_INPUT, 10);
        for (int i = 0; i < tListInput.tagCount(); i++) {
            final NBTTagCompound tNBT = tListInput.getCompoundTagAt(i);
            String invUUID = tNBT.getString(NBT.UPGRADE_INVENTORY_UUID);
            String invName = tNBT.getString(NBT.UPGRADE_INVENTORY_NAME);
            int tInvSize = tNBT.getInteger(NBT.UPGRADE_INVENTORY_SIZE);
            IItemHandlerModifiable tInv = new ItemStackHandler(tInvSize);
            loadInventory(tNBT, tInv, NBT.INV_INPUT_LIST);
            multiBlockInputInventory.put(invUUID, tInv);
            multiBlockInputInventoryNames.put(invUUID, invName);
        }
        final NBTTagList tListOutput = aNBT.getTagList(NBT.UPGRADE_INVENTORIES_OUTPUT, 10);
        for (int i = 0; i < tListOutput.tagCount(); i++) {
            final NBTTagCompound tNBT = tListOutput.getCompoundTagAt(i);
            String invUUID = tNBT.getString(NBT.UPGRADE_INVENTORY_UUID);
            String invName = tNBT.getString(NBT.UPGRADE_INVENTORY_NAME);
            int tInvSize = tNBT.getInteger(NBT.UPGRADE_INVENTORY_SIZE);
            IItemHandlerModifiable tInv = new ItemStackHandler(tInvSize);
            loadInventory(tNBT, tInv, NBT.INV_OUTPUT_LIST);
            multiBlockOutputInventory.put(invUUID, tInv);
            multiBlockOutputInventoryNames.put(invUUID, invName);
        }
    }

    private void loadItemsToOutput(NBTTagCompound aNBT) {
        final NBTTagList tList = aNBT.getTagList(NBT.ITEM_OUT, 10);
        mItemsToOutput = new ItemStack[tList.tagCount()];
        for (int i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final int tSlot = tNBT.getByte("s");
            if (tSlot >= 0 && tSlot < mItemsToOutput.length) mItemsToOutput[tSlot] = GT_Utility.loadItem(tNBT);
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
        tList.add("Structure ok: " + checkStructure(false));
    }

    protected int getToolTipID() {
        return getMultiTileEntityRegistryID() << 16 + getMultiTileEntityID();
    }

    protected GT_Multiblock_Tooltip_Builder getTooltip() {
        return createTooltip();
    }

    @Override
    public boolean checkStructure(boolean aForceReset) {
        if (!isServerSide()) return mStructureOkay;

        // Only trigger an update if forced (from onPostTick, generally), or if the structure has changed
        if ((mStructureChanged || aForceReset)) {
            mStructureOkay = checkMachine();
        }
        mStructureChanged = false;
        return mStructureOkay;
    }

    @Override
    public void onStructureChange() {
        mStructureChanged = true;
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
                !mStructureOkay);
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

    @SuppressWarnings("unchecked")
    private IStructureDefinition<MultiBlockController<T>> getCastedStructureDefinition() {
        return (IStructureDefinition<MultiBlockController<T>>) getStructureDefinition();
    }

    @Override
    public ExtendedFacing getExtendedFacing() {
        return mExtendedFacing;
    }

    @Override
    public void setExtendedFacing(ExtendedFacing newExtendedFacing) {
        if (mExtendedFacing != newExtendedFacing) {
            onStructureChange();
            if (mStructureOkay) stopMachine();
            mExtendedFacing = newExtendedFacing;
            mStructureOkay = false;
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
    public boolean onWrenchRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, byte wrenchSide, float aX, float aY,
            float aZ) {
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
    public void registerCoveredPartOnSide(final int aSide, IMultiBlockPart part) {
        if (aSide < 0 || aSide >= 6) return;

        final LinkedList<WeakReference<IMultiBlockPart>> registeredCovers = registeredCoveredParts.get(aSide);
        // TODO: Make sure that we're not already registered on this side
        registeredCovers.add(new WeakReference<>(part));
    }

    @Override
    public void unregisterCoveredPartOnSide(final int aSide, IMultiBlockPart aPart) {
        if (aSide < 0 || aSide >= 6) return;

        final LinkedList<WeakReference<IMultiBlockPart>> coveredParts = registeredCoveredParts.get(aSide);
        final Iterator<WeakReference<IMultiBlockPart>> it = coveredParts.iterator();
        while (it.hasNext()) {
            final IMultiBlockPart part = (it.next()).get();
            if (part == null || part == aPart) it.remove();
        }
    }

    @Override
    public void onFirstTick(boolean aIsServerSide) {
        super.onFirstTick(aIsServerSide);
        if (aIsServerSide) {
            checkStructure(true);
        } else {
            StructureLibAPI.queryAlignment(this);
        }
    }

    private boolean tickCovers() {
        for (byte side : ALL_VALID_SIDES) {
            // TODO: Tick controller covers, if any
            final LinkedList<WeakReference<IMultiBlockPart>> coveredParts = this.registeredCoveredParts.get(side);
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
    public void onTick(long aTimer, boolean isServerSide) {
        // Tick all covers!
        if (!tickCovers()) {
            return;
        }
    }

    @Override
    public void onPostTick(long aTick, boolean aIsServerSide) {
        if (aIsServerSide) {
            if (aTick % 600 == 5) {
                clearSpecialLists();
                // Recheck the structure every 30 seconds or so
                if (!checkStructure(false)) checkStructure(true);
            }
            if (mStructureOkay) {
                runMachine(aTick);
            } else {
                stopMachine();
            }
        }
    }

    protected void runMachine(long aTick) {
        if (mMaxProgressTime > 0) {
            markDirty();
            if (mMaxProgressTime > 0 && ++mProgressTime >= mMaxProgressTime) {
                mProgressTime = 0;
                mMaxProgressTime = 0;
                outputItems();
                if (isAllowedToWork()) {
                    checkRecipe();
                }
            }
        } else {
            if (aTick % TICKS_BETWEEN_RECIPE_CHECKS == 0 || hasWorkJustBeenEnabled() || hasInventoryBeenModified()) {
                if (isAllowedToWork()) {
                    if (checkRecipe()) {
                        markDirty();
                    }
                }
            }
        }
    }

    protected boolean checkRecipe() {
        return checkRecipe(null);
    }

    protected void clearSpecialLists() {
        mUpgradeCasings.clear();
    }

    @Override
    public final boolean isFacingValid(byte aFacing) {
        return canSetToDirectionAny(ForgeDirection.getOrientation(aFacing));
    }

    @Override
    public void onFacingChange() {
        toolSetDirection(ForgeDirection.getOrientation(getFrontFacing()));
        onStructureChange();
    }

    @Override
    public boolean allowCoverOnSide(byte aSide, GT_ItemStack aCoverID) {
        return aSide != mFacing;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return getTooltip().getStructureHint();
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        return mLimits;
    }

    protected void setAlignmentLimits(IAlignmentLimits mLimits) {
        this.mLimits = mLimits;
    }

    // IMachineProgress
    @Override
    public int getProgress() {
        return mProgressTime;
    }

    @Override
    public int getMaxProgress() {
        return mMaxProgressTime;
    }

    @Override
    public boolean increaseProgress(int aProgressAmountInTicks) {
        return increaseProgressGetOverflow(aProgressAmountInTicks) != aProgressAmountInTicks;
    }

    @Override
    public FluidStack getDrainableFluid(byte aSide) {
        final IFluidTank tank = getFluidTankDrainable(aSide, null);
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

    @Override
    public boolean hasWorkJustBeenEnabled() {
        return mWorkUpdate;
    }

    @Override
    public void enableWorking() {
        if (!mWorks) mWorkUpdate = true;
        mWorks = true;
        mWasShutdown = false;
    }

    @Override
    public void disableWorking() {
        mWorks = false;
    }

    @Override
    public boolean isAllowedToWork() {
        return mWorks;
    }

    @Override
    public boolean isActive() {
        return mActive;
    }

    @Override
    public void setActive(boolean aActive) {
        mActive = aActive;
    }

    public boolean isSeparateInputs() {
        return mSeparateInputs;
    }

    public void setSeparateInputs(boolean aSeparateInputs) {
        mSeparateInputs = aSeparateInputs;
    }

    @Override
    public boolean wasShutdown() {
        return mWasShutdown;
    }

    // End IMachineProgress

    public void stopMachine() {
        disableWorking();
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

    public <S> IStructureElement<S> addMultiTileCasing(int aRegistryID, int aBlockMeta, int aModes) {
        return new IStructureElement<S>() {

            private final short[] DEFAULT = new short[] { 255, 255, 255, 0 };
            private IIcon[] mIcons = null;

            @Override
            public boolean check(S t, World world, int x, int y, int z) {
                final TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (!(tileEntity instanceof MultiBlockPart)) return false;

                final MultiBlockPart part = (MultiBlockPart) tileEntity;
                if (aRegistryID != part.getMultiTileEntityRegistryID() || aBlockMeta != part.getMultiTileEntityID())
                    return false;

                final IMultiBlockController tTarget = part.getTarget(false);
                if (tTarget != null && tTarget != MultiBlockController.this) return false;

                part.setTarget(MultiBlockController.this, aModes);

                if (part instanceof AdvancedCasing) {
                    mUpgradeCasings.add((AdvancedCasing) part);
                }
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
                final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry(aRegistryID);
                final MultiTileEntityContainer tContainer = tRegistry.getNewTileEntityContainer(
                        world,
                        x,
                        y,
                        z,
                        aBlockMeta,
                        null);
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
                    ((MultiBlockPart) te).setTarget(MultiBlockController.this, aModes);

                    registerSpecialCasings((MultiBlockPart) te);
                }

                return false;
            }

            public IIcon getTexture(OrePrefixes aBlock) {
                return TextureSet.SET_NONE.mTextures[OrePrefixes.block.mTextureIndex].getIcon();
            }
        };
    }

    protected void registerSpecialCasings(MultiBlockPart aPart) {
        if (aPart instanceof AdvancedCasing) {
            mUpgradeCasings.add((AdvancedCasing) aPart);
        }
    }

    /**
     * Fluid - MultiBlock related Fluid Tank behaviour.
     */
    protected IFluidTank getFluidTankFillable(MultiBlockPart aPart, byte aSide, FluidStack aFluidToFill) {
        return getFluidTankFillable(aSide, aFluidToFill);
    }

    protected IFluidTank getFluidTankDrainable(MultiBlockPart aPart, byte aSide, FluidStack aFluidToDrain) {
        return getFluidTankDrainable(aSide, aFluidToDrain);
    }

    protected IFluidTank[] getFluidTanks(MultiBlockPart aPart, byte aSide) {
        return getFluidTanks(aSide);
    }

    @Override
    public int fill(MultiBlockPart aPart, ForgeDirection aDirection, FluidStack aFluid, boolean aDoFill) {
        if (aFluid == null || aFluid.amount <= 0) return 0;
        final IFluidTank tTank = getFluidTankFillable(aPart, (byte) aDirection.ordinal(), aFluid);
        if (tTank == null) return 0;
        final int rFilledAmount = tTank.fill(aFluid, aDoFill);
        if (rFilledAmount > 0 && aDoFill) mInventoryChanged = true;
        return rFilledAmount;
    }

    @Override
    public FluidStack drain(MultiBlockPart aPart, ForgeDirection aDirection, FluidStack aFluid, boolean aDoDrain) {
        if (aFluid == null || aFluid.amount <= 0) return null;
        final IFluidTank tTank = getFluidTankDrainable(aPart, (byte) aDirection.ordinal(), aFluid);
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
        final IFluidTank tTank = getFluidTankDrainable(aPart, (byte) aDirection.ordinal(), null);
        if (tTank == null || tTank.getFluid() == null || tTank.getFluidAmount() == 0) return null;
        final FluidStack rDrained = tTank.drain(aAmountToDrain, aDoDrain);
        if (rDrained != null && aDoDrain) markInventoryBeenModified();
        return rDrained;
    }

    @Override
    public boolean canFill(MultiBlockPart aPart, ForgeDirection aDirection, Fluid aFluid) {
        if (aFluid == null) return false;
        final IFluidTank tTank = getFluidTankFillable(aPart, (byte) aDirection.ordinal(), new FluidStack(aFluid, 0));
        return tTank != null && (tTank.getFluid() == null || tTank.getFluid()
                                                                  .getFluid()
                == aFluid);
    }

    @Override
    public boolean canDrain(MultiBlockPart aPart, ForgeDirection aDirection, Fluid aFluid) {
        if (aFluid == null) return false;
        final IFluidTank tTank = getFluidTankDrainable(aPart, (byte) aDirection.ordinal(), new FluidStack(aFluid, 0));
        return tTank != null && (tTank.getFluid() != null && tTank.getFluid()
                                                                  .getFluid()
                == aFluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(MultiBlockPart aPart, ForgeDirection aDirection) {
        final IFluidTank[] tTanks = getFluidTanks(aPart, (byte) aDirection.ordinal());
        if (tTanks == null || tTanks.length <= 0) return GT_Values.emptyFluidTankInfo;
        final FluidTankInfo[] rInfo = new FluidTankInfo[tTanks.length];
        for (int i = 0; i < tTanks.length; i++) rInfo[i] = new FluidTankInfo(tTanks[i]);
        return rInfo;
    }

    @Override
    public IFluidTank[] getFluidTanksForGUI(MultiBlockPart aPart) {
        if (aPart.modeSelected(MultiBlockPart.FLUID_IN)) return mTanksInput;
        if (aPart.modeSelected(MultiBlockPart.FLUID_OUT)) return mTanksOutput;
        return GT_Values.emptyFluidTank;
    }

    /**
     * Energy - MultiBlock related Energy behavior
     */
    @Override
    public boolean isUniversalEnergyStored(MultiBlockPart aPart, long aEnergyAmount) {
        return getUniversalEnergyStored(aPart) >= aEnergyAmount;
    }

    @Override
    public long getUniversalEnergyStored(MultiBlockPart aPart) {
        return Math.min(getUniversalEnergyStored(), getUniversalEnergyCapacity());
    }

    @Override
    public long getUniversalEnergyCapacity(MultiBlockPart aPart) {
        return getUniversalEnergyCapacity();
    }

    @Override
    public long getOutputAmperage(MultiBlockPart aPart) {
        return getOutputAmperage();
    }

    @Override
    public long getOutputVoltage(MultiBlockPart aPart) {
        return getOutputVoltage();
    }

    @Override
    public long getInputAmperage(MultiBlockPart aPart) {
        return getInputAmperage();
    }

    @Override
    public long getInputVoltage(MultiBlockPart aPart) {
        return getInputVoltage();
    }

    @Override
    public boolean decreaseStoredEnergyUnits(MultiBlockPart aPart, long aEnergy, boolean aIgnoreTooLittleEnergy) {
        return decreaseStoredEnergyUnits(aEnergy, aIgnoreTooLittleEnergy);
    }

    @Override
    public boolean increaseStoredEnergyUnits(MultiBlockPart aPart, long aEnergy, boolean aIgnoreTooMuchEnergy) {
        return increaseStoredEnergyUnits(aEnergy, aIgnoreTooMuchEnergy);
    }

    @Override
    public boolean drainEnergyUnits(MultiBlockPart aPart, byte aSide, long aVoltage, long aAmperage) {
        return drainEnergyUnits(aSide, aVoltage, aAmperage);
    }

    @Override
    public long injectEnergyUnits(MultiBlockPart aPart, byte aSide, long aVoltage, long aAmperage) {
        return injectEnergyUnits(aSide, aVoltage, aAmperage);
    }

    @Override
    public long getAverageElectricInput(MultiBlockPart aPart) {
        return getAverageElectricInput();
    }

    @Override
    public long getAverageElectricOutput(MultiBlockPart aPart) {
        return getAverageElectricOutput();
    }

    @Override
    public long getStoredEU(MultiBlockPart aPart) {
        return getStoredEU();
    }

    @Override
    public long getEUCapacity(MultiBlockPart aPart) {
        return getEUCapacity();
    }

    @Override
    public boolean inputEnergyFrom(MultiBlockPart aPart, byte aSide) {
        if (aSide == GT_Values.SIDE_UNKNOWN) return true;
        if (aSide >= 0 && aSide < 6) {
            if (isInvalid()) return false;
            if (isEnetInput()) return isEnergyInputSide(aSide);
        }
        return false;
    }

    @Override
    public boolean outputsEnergyTo(MultiBlockPart aPart, byte aSide) {
        if (aSide == GT_Values.SIDE_UNKNOWN) return true;
        if (aSide >= 0 && aSide < 6) {
            if (isInvalid()) return false;
            if (isEnetOutput()) return isEnergyOutputSide(aSide);
        }
        return false;
    }

    /**
     * Item - MultiBlock related Item behaviour.
     */
    @Override
    public void registerInventory(String aName, String aID, int aInventorySize, int aType) {
        if (aType == InventoryUpgrade.INPUT || aType == InventoryUpgrade.BOTH) {
            if (multiBlockInputInventory.containsKey(aID)) return;
            multiBlockInputInventory.put(aID, new ItemStackHandler(aInventorySize));
            multiBlockInputInventoryNames.put(aID, aName);
        }
        if (aType == InventoryUpgrade.OUTPUT || aType == InventoryUpgrade.BOTH) {
            if (multiBlockOutputInventory.containsKey(aID)) return;
            multiBlockOutputInventory.put(aID, new ItemStackHandler(aInventorySize));
            multiBlockOutputInventoryNames.put(aID, aName);
        }
    }

    @Override
    public void unregisterInventory(String aName, String aID, int aType) {
        if ((aType == InventoryUpgrade.INPUT || aType == InventoryUpgrade.BOTH)
                && multiBlockInputInventory.containsKey(aID)) {
            multiBlockInputInventory.remove(aID, multiBlockInputInventory.get(aID));
            multiBlockInputInventoryNames.remove(aID, aName);
        }
        if ((aType == InventoryUpgrade.OUTPUT || aType == InventoryUpgrade.BOTH)
                && multiBlockOutputInventory.containsKey(aID)) {
            multiBlockOutputInventory.remove(aID, multiBlockOutputInventory.get(aID));
            multiBlockOutputInventoryNames.remove(aID, aName);
        }
    }

    @Override
    public void changeInventoryName(String aName, String aID, int aType) {
        if ((aType == InventoryUpgrade.INPUT || aType == InventoryUpgrade.BOTH)
                && multiBlockInputInventoryNames.containsKey(aID)) {
            multiBlockInputInventoryNames.put(aID, aName);
        }
        if ((aType == InventoryUpgrade.OUTPUT || aType == InventoryUpgrade.BOTH)
                && multiBlockOutputInventoryNames.containsKey(aID)) {
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
    public IItemHandlerModifiable getInventoryForGUI(MultiBlockPart aPart) {
        if (isServerSide()) {
            for (AdvancedCasing tPart : mUpgradeCasings) {
                if (!(tPart instanceof InventoryUpgrade)) continue;
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
    public int[] getAccessibleSlotsFromSide(MultiBlockPart aPart, byte aSide) {
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
    public boolean canInsertItem(MultiBlockPart aPart, int aSlot, ItemStack aStack, byte aSide) {
        final Pair<IItemHandlerModifiable, Integer> tInv = getInventory(aPart, aSlot);
        if (tInv == null) return false;

        final int tSlot = tInv.getRight();
        final IItemHandlerModifiable inv = tInv.getLeft();;

        return inv.getStackInSlot(tSlot) == null || GT_Utility.areStacksEqual(aStack, inv.getStackInSlot(tSlot)); // &&
                                                                                                                  // allowPutStack(getBaseMetaTileEntity(),
                                                                                                                  // aIndex,
                                                                                                                  // (byte)
                                                                                                                  // aSide,
                                                                                                                  // aStack)
    }

    @Override
    public boolean canExtractItem(MultiBlockPart aPart, int aSlot, ItemStack aStack, byte aSide) {
        final Pair<IItemHandlerModifiable, Integer> tInv = getInventory(aPart, aSlot);
        if (tInv == null) return false;

        final int tSlot = tInv.getRight();
        final IItemHandlerModifiable inv = tInv.getLeft();;

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
        inventoryNames.add("all");
        inventoryNames.add("controller");
        inventoryNames.addAll(getMultiBlockInventoryNames(aPart).values());
        return inventoryNames;
    }

    @Override
    public List<String> getInventoryIDs(MultiBlockPart aPart) {
        final List<String> tInventoryIDs = new ArrayList<>();
        tInventoryIDs.add("all");
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

    protected ItemStack[] getAllItemInputs() {
        return getInventoriesForInput().getStacks()
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

    protected void setItemOutputs(ItemStack[] aItemOutputs, String aInventory) {
        mItemsToOutput = aItemOutputs;
        mInventory = aInventory;
    }

    private void outputItems() {
        int index = 0;
        if (mItemsToOutput == null) {
            return;
        }
        if (mInventory != null) {
            for (ItemStack tItem : mItemsToOutput) {
                multiBlockOutputInventory.getOrDefault(mInventory, getInventoriesForOutput())
                                         .insertItem(index++, tItem.copy(), false);
            }
        } else {
            for (ItemStack tItem : mItemsToOutput) {
                getInventoriesForOutput().insertItem(index++, tItem.copy(), false);
            }
        }
        mItemsToOutput = null;
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
        return super.createWindow(buildContext);
    }

    @Override
    public boolean hasGui(byte aSide) {
        return true;
    }

    @Override
    protected void addTitleTextStyle(ModularWindow.Builder builder, String title) {
        // leave empty
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
                new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                                    .setSize(17, 17)
                                    .setPos(148, 60));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (isServerSide()) {
            for (AdvancedCasing tPart : mUpgradeCasings) {
                if (!(tPart instanceof InventoryUpgrade)) continue;
                tPart.issueClientUpdate();
            }
        }
        builder.widget(
                new TabContainer().setButtonSize(20, 24)
                                  .addTabButton(
                                          new TabButton(0).setBackground(
                                                  false,
                                                  ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(0, 0, 1f, 0.5f))
                                                          .setBackground(
                                                                  true,
                                                                  ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(
                                                                          0,
                                                                          0.5f,
                                                                          1f,
                                                                          1f))
                                                          .addTooltip(getLocalName())
                                                          .setPos(0, -20))
                                  .addTabButton(
                                          new TabButton(1).setBackground(
                                                  false,
                                                  ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(0, 0, 1f, 0.5f))
                                                          .setBackground(
                                                                  true,
                                                                  ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(
                                                                          0,
                                                                          0.5f,
                                                                          1f,
                                                                          1f))
                                                          .setPos(20, -20))
                                  .addTabButton(
                                          new TabButton(2).setBackground(
                                                  false,
                                                  ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(0, 0, 1f, 0.5f))
                                                          .setBackground(
                                                                  true,
                                                                  ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(
                                                                          0,
                                                                          0.5f,
                                                                          1f,
                                                                          1f))
                                                          .setPos(40, -20))
                                  .addTabButton(
                                          new TabButton(3).setBackground(
                                                  false,
                                                  ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(0, 0, 1f, 0.5f))
                                                          .setBackground(
                                                                  true,
                                                                  ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(
                                                                          0,
                                                                          0.5f,
                                                                          1f,
                                                                          1f))
                                                          .setPos(60, -20))
                                  .addTabButton(
                                          new TabButton(4).setBackground(
                                                  false,
                                                  ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(0, 0, 1f, 0.5f))
                                                          .setBackground(
                                                                  true,
                                                                  ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(
                                                                          0,
                                                                          0.5f,
                                                                          1f,
                                                                          1f))
                                                          .setPos(80, -20))
                                  .addPage(
                                          new MultiChildWidget().addChild(
                                                  new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                                                                      .setPos(7, 4)
                                                                      .setSize(160, 75)))
                                  .addPage(new MultiChildWidget().addChild(getItemInventoryInputGUI()))
                                  .addPage(new MultiChildWidget().addChild(getItemInventoryOutputGUI()))
                                  .addPage(new MultiChildWidget().addChild(getFluidInventoryInputGUI()))
                                  .addPage(new MultiChildWidget().addChild(getFluidInventoryOutputGUI())))
               .widget(
                       new ItemDrawable(getStackForm(1)).asWidget()
                                                        .setSize(16, 16)
                                                        .setPos(2, -16))
               .widget(
                       new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ITEM_IN)
                                           .setSize(16, 16)
                                           .setPos(22, -16))
               .widget(
                       new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ITEM_OUT)
                                           .setSize(16, 16)
                                           .setPos(42, -16))
               .widget(
                       new DrawableWidget().setDrawable(GT_UITextures.PICTURE_FLUID_IN)
                                           .setSize(16, 16)
                                           .setPos(62, -16))
               .widget(
                       new DrawableWidget().setDrawable(GT_UITextures.PICTURE_FLUID_OUT)
                                           .setSize(16, 16)
                                           .setPos(82, -16));
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
        return scrollable.setSize(18 * 4 + 4, 18 * 4)
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
        return scrollable.setSize(18 * 4 + 4, 18 * 4)
                         .setPos(52, 7);
    }

    protected IItemHandlerModifiable getInventoriesForInput() {
        return new ListItemHandler(multiBlockInputInventory.values());
    }

    protected IItemHandlerModifiable getInventoriesForOutput() {
        return new ListItemHandler(multiBlockOutputInventory.values());
    }

    protected Widget getFluidInventoryInputGUI() {
        final IFluidTank[] tanks = mTanksInput;
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
        return scrollable.setSize(18 * 4 + 4, 18 * 4)
                         .setPos(52, 7);
    }

    protected Widget getFluidInventoryOutputGUI() {
        final IFluidTank[] tanks = mTanksOutput;
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
        return scrollable.setSize(18 * 4 + 4, 18 * 4)
                         .setPos(52, 7);
    }
}
