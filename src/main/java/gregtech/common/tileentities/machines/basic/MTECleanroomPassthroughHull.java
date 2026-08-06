package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import appeng.api.networking.IGridNode;
import appeng.api.parts.IPartHost;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicHull;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.api.util.PassthroughChainWalker;
import gregtech.api.util.PassthroughChainWalker.StepKind;
import gregtech.common.config.MachineStats;
import gregtech.crossmod.logisticspipes.CleanroomPassthroughLPConnection;
import logisticspipes.pipes.basic.LogisticsTileGenericPipe;

/**
 * A machine hull that also carries an ME network and Logistics Pipes routing through a cleanroom wall. Everything a
 * basic hull does is inherited unchanged.
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTECleanroomPassthroughHull extends MTEBasicHull implements IGridProxyable {

    protected @Nullable AENetworkProxy gridProxy = null;

    public MTECleanroomPassthroughHull(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTECleanroomPassthroughHull(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECleanroomPassthroughHull(mName, mTier, mInventory.length, mDescriptionArray, mTextures);
    }

    /** The two faces networks pass through: the facing and its opposite. */
    public boolean isAxisFace(ForgeDirection side) {
        ForgeDirection front = getBaseMetaTileEntity().getFrontFacing();
        return side == front || side == front.getOpposite();
    }

    public static boolean isPassthroughHull(TileEntity tile) {
        return tile instanceof IGregTechTileEntity gte
            && gte.getMetaTileEntity() instanceof MTECleanroomPassthroughHull;
    }

    /** Chains are straight lines: a neighbouring hull only counts if its own facing lies on the same axis. */
    public static boolean isChainableHull(TileEntity tile, ForgeDirection axis) {
        if (!isPassthroughHull(tile)) return false;
        ForgeDirection front = ((IGregTechTileEntity) tile).getFrontFacing();
        return front == axis || front == axis.getOpposite();
    }

    // ------------------------------------------------------------------ AE2

    @Override
    public @NotNull AENetworkProxy getProxy() {
        if (gridProxy == null) {
            gridProxy = new AENetworkProxy(this, "proxy", ItemList.Hull_Cleanroom_Passthrough.get(1), true);
            // No REQUIRE_CHANNEL: this is infrastructure. Consumes no channel, carries AE2's default 8.
            gridProxy.setFlags();
            gridProxy.setIdlePowerUsage(0);
            updateValidGridProxySides();
        }
        return gridProxy;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {
        return getProxy().getNode();
    }

    @Override
    public void gridChanged() {}

    @Override
    public void securityBreak() {}

    @Override
    public DimensionalCoord getLocation() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        return new DimensionalCoord(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord());
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection side) {
        return isAxisFace(side) && hasAe2Endpoint(side) ? AECableType.SMART : AECableType.NONE;
    }

    /** Only the axis faces that actually lead to an AE2 cable bus, so this never bridges to a GT ME hatch. */
    protected void updateValidGridProxySides() {
        if (gridProxy == null) return;
        EnumSet<ForgeDirection> sides = EnumSet.noneOf(ForgeDirection.class);
        ForgeDirection front = getBaseMetaTileEntity().getFrontFacing();
        for (ForgeDirection side : new ForgeDirection[] { front, front.getOpposite() }) {
            if (hasAe2Endpoint(side)) sides.add(side);
        }
        gridProxy.setValidSides(sides);
    }

    protected boolean hasAe2Endpoint(ForgeDirection side) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null || base.getWorld() == null) return false;
        return PassthroughChainWalker.walk(step -> {
            TileEntity tile = base.getWorld()
                .getTileEntity(
                    base.getXCoord() + side.offsetX * step,
                    base.getYCoord() + side.offsetY * step,
                    base.getZCoord() + side.offsetZ * step);
            if (tile instanceof IPartHost) return StepKind.ENDPOINT;
            if (isChainableHull(tile, side)) return StepKind.HULL;
            return StepKind.OTHER;
        }, MachineStats.cleanroom.passthroughChainLimit) > 0;
    }

    protected void updateAE2ProxyColor() {
        byte color = getColor();
        getProxy().setColor(color == -1 ? AEColor.Transparent : AEColor.values()[Dyes.transformDyeIndex(color)]);
    }

    // -------------------------------------------------------------- Lifecycle

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
        updateValidGridProxySides();
        updateAE2ProxyColor();
    }

    @Override
    public void onFacingChange() {
        super.onFacingChange();
        updateValidGridProxySides();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        // MetaTileEntity has no neighbour-change hook, so re-check the endpoints periodically.
        if (aTick % 20 == 0) updateValidGridProxySides();
        relayToLogisticsPipes(aBaseMetaTileEntity);
    }

    /**
     * Pushes the buffered stack out of the far axis face when that side leads to an LP pipe. Without an LP neighbour
     * this never fires and the block stays a passive buffer, exactly like a basic hull.
     */
    protected void relayToLogisticsPipes(IGregTechTileEntity base) {
        ItemStack stack = mInventory[0];
        if (stack == null || stack.stackSize <= 0) return;

        ForgeDirection front = base.getFrontFacing();
        for (ForgeDirection side : new ForgeDirection[] { front, front.getOpposite() }) {
            TileEntity neighbour = base.getTileEntityAtSide(side);
            // Only relay onward - never back into whatever inserted the stack.
            if (!(neighbour instanceof LogisticsTileGenericPipe) && !isChainableHull(neighbour, side)) continue;
            LogisticsTileGenericPipe target = CleanroomPassthroughLPConnection.findPipe((TileEntity) base, side);
            if (target == null) continue;
            if (target.injectItem(stack, true, side.getOpposite()) == stack.stackSize) {
                mInventory[0] = null;
                return;
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("proxy")) getProxy().readFromNBT(aNBT);
        updateAE2ProxyColor();
    }

    // ---------------------------------------------------------------- Display

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aConnected, boolean redstoneLevel) {
        ITexture casing = Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1];
        if (side == aFacing || side == aFacing.getOpposite()) {
            // TODO custom texture?
            return new ITexture[] { casing, TextureFactory.of(OVERLAY_ME_INPUT_HATCH) };
        }
        return new ITexture[] { casing };
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalized("gt.blockmachines.cleanroom_passthrough_hull.desc");
    }
}
