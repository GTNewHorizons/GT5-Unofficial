package gregtech.api.metatileentity.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

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
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

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
            if (mMachine) stopMachine(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE);
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
        float aX, float aY, float aZ) {
        if (wrenchingSide != getBaseMetaTileEntity().getFrontFacing())
            return super.onWrenchRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ);
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

    /**
     * When an old {@link ExtendedFacing} is loaded upon MTE deserialization, and the loaded facing cannot pass test of
     * current {@link #getAlignmentLimits()}, this method will be called to retrieve a corrected version. This method
     * is currently only intended to be used as a mean to migrate alignment limits, so if you never change the alignment
     * limit then you can probably just use the default implementation.
     *
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
        return getCastedStructureDefinition().check(
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
    protected final int survivialBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
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

    protected final int survivialBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
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
    protected final int survivialBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, IItemSource source, EntityPlayerMP actor, boolean check,
        boolean checkIfPlaced) {
        int built = survivialBuildPiece(
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

    protected final int survivialBuildPiece(String piece, ItemStack trigger, int horizontalOffset, int verticalOffset,
        int depthOffset, int elementsBudget, ISurvivalBuildEnvironment env, boolean check, boolean checkIfPlaced) {
        int built = survivialBuildPiece(
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

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isClientSide())
            StructureLibAPI.queryAlignment((IAlignmentProvider) aBaseMetaTileEntity);
    }
}
