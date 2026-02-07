package gregtech.api.metatileentity.implementations;

import javax.annotation.Nonnegative;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureWalker;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.client.GTSoundLoop;
import gregtech.client.volumetric.ISoundPosition;

/**
 * Enhanced multiblock base class, featuring following improvement over {@link MTEMultiBlockBase}
 * <p>
 * 1. TecTech style declarative structure check utilizing StructureLib. 2. Arbitrarily rotating the whole structure, if
 * allowed to.
 *
 * @param <T> type of this
 */
public abstract class MTEEnhancedMultiBlockBase<T extends MTEEnhancedMultiBlockBase<T>> extends MTETooltipMultiBlockBase
    implements IAlignment, IConstructable {

    private ExtendedFacing mExtendedFacing = ExtendedFacing.DEFAULT;
    private IAlignmentLimits mLimits = getInitialAlignmentLimits();

    private final StructureCenterWalker centerWalker = new StructureCenterWalker();
    private final Vector3f center = new Vector3f();
    private int structureRadius;

    protected MTEEnhancedMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEEnhancedMultiBlockBase(String aName) {
        super(aName);
    }

    @Override
    public ExtendedFacing getExtendedFacing() {
        return mExtendedFacing;
    }

    @Override
    public void setExtendedFacing(ExtendedFacing newExtendedFacing) {
        if (mExtendedFacing != newExtendedFacing) {
            if (mMachine && isAllowedToWork()) stopMachine(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE);
            mExtendedFacing = newExtendedFacing;
            final IGregTechTileEntity base = getBaseMetaTileEntity();
            mMachine = false;
            mUpdated = false;
            mUpdate = 50;
            if (getBaseMetaTileEntity().isServerSide()
                && !GregTechAPI.isDummyWorld(getBaseMetaTileEntity().getWorld())) {
                StructureLibAPI.sendAlignment(
                    (IAlignmentProvider) base,
                    new NetworkRegistry.TargetPoint(
                        base.getWorld().provider.dimensionId,
                        base.getXCoord(),
                        base.getYCoord(),
                        base.getZCoord(),
                        512));
            } else {
                base.issueTextureUpdate();
            }
        }
    }

    @Override
    public final boolean isFacingValid(ForgeDirection facing) {
        return canSetToDirectionAny(facing);
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (wrenchingSide != getBaseMetaTileEntity().getFrontFacing())
            return super.onWrenchRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ, aTool);
        if (entityPlayer.isSneaking()) {
            if (isFlipChangeAllowed()) {
                toolSetFlip(getFlip().isHorizontallyFlipped() ? Flip.NONE : Flip.HORIZONTAL);
            } else {
                return false;
            }
        } else {
            if (isRotationChangeAllowed()) {
                toolSetRotation(null);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onFacingChange() {
        toolSetDirection(getBaseMetaTileEntity().getFrontFacing());
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        return mLimits;
    }

    protected void setAlignmentLimits(IAlignmentLimits mLimits) {
        this.mLimits = mLimits;
    }

    /**
     * Due to limitation of Java type system, you might need to do an unchecked cast. HOWEVER, the returned
     * IStructureDefinition is expected to be evaluated against current instance only, and should not be used against
     * other instances, even for those of the same class.
     */
    @Override
    public abstract IStructureDefinition<T> getStructureDefinition();

    protected abstract MultiblockTooltipBuilder createTooltip();

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return getTooltip().getStructureHint();
    }

    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> !f.isVerticallyFliped();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte(
            "eRotation",
            (byte) mExtendedFacing.getRotation()
                .getIndex());
        aNBT.setByte(
            "eFlip",
            (byte) mExtendedFacing.getFlip()
                .getIndex());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mExtendedFacing = ExtendedFacing.of(
            getBaseMetaTileEntity().getFrontFacing(),
            Rotation.byIndex(aNBT.getByte("eRotation")),
            Flip.byIndex(aNBT.getByte("eFlip")));
        if (!getAlignmentLimits().isNewExtendedFacingValid(mExtendedFacing)) {
            mExtendedFacing = getCorrectedAlignment(mExtendedFacing);
        }
    }

    @Nullable
    public Vector3f getCenter() {
        return center;
    }

    /// Gets the approximate radius of this multi in blocks.
    @Nonnegative
    public int getApproximateRadius() {
        return structureRadius;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected @NotNull GTSoundLoop createSoundLoop(SoundResource activitySound) {
        GTSoundLoop loop = super.createSoundLoop(activitySound);

        // 16 blocks = 1f volume, doubled so that you can hear the sound outside of large multis
        loop.setVolume(Math.max(1f, structureRadius / 16f * 2f));

        return loop;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected ISoundPosition getSoundPosition() {
        return this::getCenter;
    }

    @Override
    protected void onStructureCheckFinished() {
        super.onStructureCheckFinished();

        StructureSize size = centerWalker.finish();

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (size != null) {
            this.center.set(size.centerX, size.centerY, size.centerZ);
            this.structureRadius = size.radius;
        } else {
            this.center.set(igte.getXCoord(), igte.getYCoord(), igte.getZCoord());
            this.structureRadius = 1;
        }

        if (mMachine) {
            igte.issueTileUpdate();
        }
    }

    /**
     * When an old {@link ExtendedFacing} is loaded upon MTE deserialization, and the loaded facing cannot pass test of
     * current {@link #getAlignmentLimits()}, this method will be called to retrieve a corrected version. This method is
     * currently only intended to be used as a mean to migrate alignment limits, so if you never change the alignment
     * limit then you can probably just use the default implementation.
     * <p>
     * The returned new facing must be able to pass the test of {@link #isNewExtendedFacingValid(ExtendedFacing)}
     */
    protected ExtendedFacing getCorrectedAlignment(ExtendedFacing aOldFacing) {
        if (isNewExtendedFacingValid(ExtendedFacing.DEFAULT)) {
            return ExtendedFacing.DEFAULT;
        }
        for (ExtendedFacing facing : ExtendedFacing.VALUES) {
            if (facing.getFlip()
                .isVerticallyFliped()) {
                continue;
            }
            if (isNewExtendedFacingValid(facing)) {
                return facing;
            }
        }
        throw new AssertionError("No ExtendedFacing can pass the test of isNewExtendedFacingValid");
    }

    @SuppressWarnings("unchecked")
    private IStructureDefinition<MTEEnhancedMultiBlockBase<T>> getCastedStructureDefinition() {
        return (IStructureDefinition<MTEEnhancedMultiBlockBase<T>>) getStructureDefinition();
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
        final IGregTechTileEntity tTile = getBaseMetaTileEntity();
        boolean success = getCastedStructureDefinition().check(
            this,
            piece,
            tTile.getWorld(),
            getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            !mMachine);

        if (success) {
            getCastedStructureDefinition().iterate(
                piece,
                tTile.getWorld(),
                getExtendedFacing(),
                tTile.getXCoord(),
                tTile.getYCoord(),
                tTile.getZCoord(),
                horizontalOffset,
                verticalOffset,
                depthOffset,
                centerWalker);
        }

        return success;
    }

    protected final boolean buildPiece(String piece, ItemStack trigger, boolean hintOnly, int horizontalOffset,
        int verticalOffset, int depthOffset) {
        final IGregTechTileEntity tTile = getBaseMetaTileEntity();
        return getCastedStructureDefinition().buildOrHints(
            this,
            trigger,
            piece,
            tTile.getWorld(),
            getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            hintOnly);
    }

    @Deprecated
    protected final int survivalBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, IItemSource source, EntityPlayerMP actor, boolean check) {
        final IGregTechTileEntity tTile = getBaseMetaTileEntity();
        return getCastedStructureDefinition().survivalBuild(
            this,
            trigger,
            piece,
            tTile.getWorld(),
            getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementsBudget,
            source,
            actor,
            check);
    }

    protected final int survivalBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, ISurvivalBuildEnvironment env, boolean check) {
        final IGregTechTileEntity tTile = getBaseMetaTileEntity();
        return getCastedStructureDefinition().survivalBuild(
            this,
            trigger,
            piece,
            tTile.getWorld(),
            getExtendedFacing(),
            tTile.getXCoord(),
            tTile.getYCoord(),
            tTile.getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementsBudget,
            env,
            check);
    }

    @Deprecated
    protected final int survivalBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, IItemSource source, EntityPlayerMP actor, boolean check,
        boolean checkIfPlaced) {
        int built = survivalBuildPiece(
            piece,
            trigger,
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementsBudget,
            source,
            actor,
            check);
        if (checkIfPlaced && built > 0) checkStructure(true, getBaseMetaTileEntity());
        return built;
    }

    protected final int survivalBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, ISurvivalBuildEnvironment env, boolean check, boolean checkIfPlaced) {
        int built = survivalBuildPiece(
            piece,
            trigger,
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementsBudget,
            env,
            check);
        if (checkIfPlaced && built > 0) checkStructure(true, getBaseMetaTileEntity());
        return built;
    }

    /**
     * @deprecated typo in the name. Use
     *             {@link #survivalBuildPiece(String, ItemStack, int, int, int, int, IItemSource, EntityPlayerMP, boolean)}
     *             instead.
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    protected final int survivialBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, IItemSource source, EntityPlayerMP actor, boolean check) {
        return survivalBuildPiece(
            piece,
            trigger,
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementsBudget,
            source,
            actor,
            check);
    }

    /**
     * @deprecated typo in the name. Use
     *             {@link #survivalBuildPiece(String, ItemStack, int, int, int, int, ISurvivalBuildEnvironment, boolean)}
     *             instead.
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    protected final int survivialBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, ISurvivalBuildEnvironment env, boolean check) {
        return survivalBuildPiece(
            piece,
            trigger,
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementsBudget,
            env,
            check);
    }

    /**
     * @deprecated typo in the name. Use
     *             {@link #survivalBuildPiece(String, ItemStack, int, int, int, int, IItemSource, EntityPlayerMP, boolean, boolean)}
     *             instead.
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    protected final int survivialBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, IItemSource source, EntityPlayerMP actor, boolean check,
        boolean checkIfPlaced) {
        return survivalBuildPiece(
            piece,
            trigger,
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementsBudget,
            source,
            actor,
            check,
            checkIfPlaced);
    }

    /**
     * @deprecated typo in the name. Use
     *             {@link #survivalBuildPiece(String, ItemStack, int, int, int, int, ISurvivalBuildEnvironment, boolean, boolean)}
     *             instead.
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    protected final int survivialBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, ISurvivalBuildEnvironment env, boolean check, boolean checkIfPlaced) {
        return survivalBuildPiece(
            piece,
            trigger,
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementsBudget,
            env,
            check,
            checkIfPlaced);
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = super.getDescriptionData();

        if (data == null) data = new NBTTagCompound();

        data.setFloat("centerX", center.x);
        data.setFloat("centerY", center.y);
        data.setFloat("centerZ", center.z);
        data.setInteger("radius", structureRadius);

        data.setByte(
            "eRotation",
            (byte) mExtendedFacing.getRotation()
                .getIndex());
        data.setByte(
            "eFlip",
            (byte) mExtendedFacing.getFlip()
                .getIndex());
        return data;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDescriptionPacket(NBTTagCompound data) {
        int oldRadius = structureRadius;

        center.set(data.getFloat("centerX"), data.getFloat("centerY"), data.getFloat("centerZ"));
        structureRadius = data.getInteger("radius");

        if (oldRadius != structureRadius) {
            restartActivitySound();
        }

        mExtendedFacing = ExtendedFacing.of(
            getBaseMetaTileEntity().getFrontFacing(),
            Rotation.byIndex(data.getByte("eRotation")),
            Flip.byIndex(data.getByte("eFlip")));
    }

    public static class StructureSize {

        public int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        public int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

        public float centerX, centerY, centerZ;
        public int radius;
    }

    private class StructureCenterWalker implements IStructureWalker<MTEEnhancedMultiBlockBase<T>> {

        private StructureSize size;

        public StructureSize finish() {
            // When `size` is null, for whatever reason the piece method was never called, which never called [#visit].
            // We'll just fall back to the default behaviour in this case - play the sound at the center with the
            // default radius.
            // This usually happens when a multi has its own structure check logic.
            if (this.size == null) return null;

            size.centerX = (size.minX + size.maxX) * 0.5f;
            size.centerY = (size.minY + size.maxY) * 0.5f;
            size.centerZ = (size.minZ + size.maxZ) * 0.5f;

            size.radius = GTUtility.max(size.maxX - size.minX, size.maxY - size.minY, size.maxZ - size.minZ) / 2;

            return size;
        }

        @Override
        public boolean visit(IStructureElement<MTEEnhancedMultiBlockBase<T>> element, World world, int x, int y, int z,
            int a, int b, int c) {
            if (size == null) {
                size = new StructureSize();
            }

            size.minX = Math.min(size.minX, x);
            size.minY = Math.min(size.minY, y);
            size.minZ = Math.min(size.minZ, z);

            size.maxX = Math.max(size.maxX, x);
            size.maxY = Math.max(size.maxY, y);
            size.maxZ = Math.max(size.maxZ, z);

            return true;
        }
    }
}
