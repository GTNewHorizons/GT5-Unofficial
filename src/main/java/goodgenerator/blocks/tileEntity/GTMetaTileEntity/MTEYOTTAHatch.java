package goodgenerator.blocks.tileEntity.GTMetaTileEntity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkCellArrayUpdate;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.ICellContainer;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IItemList;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.item.AEFluidStack;
import goodgenerator.blocks.tileEntity.MTEYottaFluidTank;
import goodgenerator.loader.Loaders;
import goodgenerator.util.StackUtils;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public class MTEYOTTAHatch extends MTEHatch implements IGridProxyable, IActionHost, ICellContainer,
    IMEInventory<IAEFluidStack>, IMEInventoryHandler<IAEFluidStack>, IPowerChannelState {

    private static final IIconContainer textureFont = Textures.BlockIcons.CustomIcon.create("icons/YOTTAHatch");
    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
    private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);

    private MTEYottaFluidTank host;
    private AENetworkProxy gridProxy = null;
    private int priority;
    private boolean isSticky = false;
    private byte tickRate = 20;
    private FluidStack lastFluid = null;
    private BigInteger lastAmt = BigInteger.ZERO;
    private AccessRestriction readMode = AccessRestriction.READ_WRITE;
    private final AccessRestriction[] AEModes = new AccessRestriction[] { AccessRestriction.NO_ACCESS,
        AccessRestriction.READ, AccessRestriction.WRITE, AccessRestriction.READ_WRITE };
    private final BaseActionSource mySrc = new MachineSource(this);

    private static final BigInteger MAX_LONG_BIGINT = BigInteger.valueOf(Long.MAX_VALUE);

    public MTEYOTTAHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { "Special I/O port for AE2FC.", "Directly connected YOTTank with AE fluid storage system.",
                "Use screwdriver to set storage priority", "Use soldering iron to set read/write mode",
                "Use wire cutter to enable/disable sticky mode" });
    }

    public MTEYOTTAHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public void setTank(MTEYottaFluidTank te) {
        if (host != te) {
            this.host = te;
            this.postStorageUpdateToAE2();
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mAEPriority", this.priority);
        aNBT.setInteger("mAEMode", this.readMode.ordinal());
        aNBT.setBoolean("mAESticky", this.isSticky);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.priority = aNBT.getInteger("mAEPriority");
        this.readMode = AEModes[aNBT.getInteger("mAEMode")];
        this.isSticky = aNBT.getBoolean("mAESticky");
        getProxy().readFromNBT(aNBT);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack toolStack) {
        if (aPlayer.isSneaking()) this.priority -= 10;
        else this.priority += 10;
        try {
            AENetworkProxy proxy = getProxy();
            if (proxy != null && proxy.isActive()) {
                proxy.getGrid()
                    .postEvent(new MENetworkCellArrayUpdate());
            }
        } catch (GridAccessException e) {
            // :P
        }
        GTUtility.sendChatTrans(aPlayer, "yothatch.chat.0", this.priority);
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack toolStack) {
        this.readMode = AEModes[(readMode.ordinal() + 1) % 4];
        GTUtility.sendChatTrans(
            aPlayer,
            "yothatch.chat.1",
            new ChatComponentTranslation(getUnlocalizedReadMode(this.readMode)));
        return true;
    }

    private static String getUnlocalizedReadMode(AccessRestriction readMode) {
        return switch (readMode) {
            case NO_ACCESS -> "gg.chat.ae_modes.no_access";
            case READ -> "gg.chat.ae_modes.read";
            case WRITE -> "gg.chat.ae_modes.write";
            case READ_WRITE -> "gg.chat.ae_modes.read_write";
        };
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        this.isSticky = !this.isSticky;
        GTUtility.sendChatTrans(aPlayer, this.isSticky ? "yothatch.chat.2" : "yothatch.chat.3");
        return true;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection forgeDirection) {
        AENetworkProxy gp = getProxy();
        return gp != null ? gp.getNode() : null;
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.SMART;
    }

    @Override
    public void securityBreak() {}

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            gridProxy = new AENetworkProxy(this, "proxy", Loaders.YFH, true);

            gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
            if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
                getBaseMetaTileEntity().getWorld()
                    .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
        }
        return this.gridProxy;
    }

    @Override
    public boolean isPowered() {
        return getProxy() != null && getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        return getProxy() != null && getProxy().isActive();
    }

    // not sure if needed
    @MENetworkEventSubscribe
    public void powerRender(final MENetworkPowerStatusChange c) {
        try {
            AENetworkProxy proxy = getProxy();
            if (proxy != null && proxy.isActive()) {
                proxy.getGrid()
                    .postEvent(new MENetworkCellArrayUpdate());
            }
        } catch (GridAccessException e) {
            // :P
        }
    }

    @MENetworkEventSubscribe
    public void channelRender(final MENetworkChannelsChanged c) {
        try {
            AENetworkProxy proxy = getProxy();
            if (proxy != null && proxy.isActive()) {
                proxy.getGrid()
                    .postEvent(new MENetworkCellArrayUpdate());
            }
        } catch (GridAccessException e) {
            // :P
        }
    }

    @Override
    public DimensionalCoord getLocation() {
        IGregTechTileEntity gtm = this.getBaseMetaTileEntity();
        return new DimensionalCoord(gtm.getWorld(), gtm.getXCoord(), gtm.getYCoord(), gtm.getZCoord());
    }

    @Override
    public IItemList<IAEFluidStack> getAvailableItems(IItemList<IAEFluidStack> out, int iteration) {
        if (host == null || host.getBaseMetaTileEntity() == null
            || !host.getBaseMetaTileEntity()
                .isActive())
            return out;
        if (host.mFluid == null || host.mStorageCurrent.signum() <= 0) return out;
        long ready;
        if (host.mStorageCurrent.compareTo(MAX_LONG_BIGINT) >= 0) {
            ready = Long.MAX_VALUE;
        } else ready = host.mStorageCurrent.longValue();
        out.add(StackUtils.createAEFluidStack(host.mFluid.getFluid(), ready));
        return out;
    }

    @Override
    public IAEFluidStack injectItems(IAEFluidStack input, Actionable type, BaseActionSource src) {
        long amt = fill(null, input, type.equals(Actionable.MODULATE));
        if (type.equals(Actionable.MODULATE)) {
            // prevent unnecessary network updates
            update();
        }
        if (amt == 0) {
            return input;
        }
        input = input.copy();
        input.decStackSize(amt);
        if (input.getStackSize() <= 0) return null;
        return input;
    }

    @Override
    public IAEFluidStack extractItems(IAEFluidStack request, Actionable mode, BaseActionSource src) {
        IAEFluidStack ready = drain(null, request, false);
        if (ready != null) {
            if (mode.equals(Actionable.MODULATE)) {
                drain(null, ready, true);
                // prevent unnecessary network updates
                update();
            }
            return ready;
        } else return null;
    }

    @Override
    public StorageChannel getChannel() {
        return StorageChannel.FLUIDS;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    private void postUpdate(AENetworkProxy proxy, FluidStack fluid, BigInteger amt) {
        try {
            proxy.getStorage()
                .postAlterationOfStoredItems(
                    StorageChannel.FLUIDS,
                    Collections.singletonList(
                        AEFluidStack.create(fluid)
                            .setStackSize(
                                amt.min(LONG_MAX)
                                    .max(LONG_MIN)
                                    .longValue())),
                    this.mySrc);
        } catch (GridAccessException e) {
            // :P
        }
    }

    private void postStorageUpdateToAE2() {
        AENetworkProxy proxy = getProxy();
        if (proxy != null && proxy.isActive()) {
            if (this.lastFluid != null && this.host.mFluid != null) {
                if (this.lastFluid != this.host.mFluid) {
                    // post removal of last fluid
                    postUpdate(proxy, this.lastFluid, this.lastAmt.negate());
                    // post new fluid
                    postUpdate(proxy, this.host.mFluid, this.host.mStorageCurrent);
                } else {
                    // post difference
                    postUpdate(proxy, this.host.mFluid, this.host.mStorageCurrent.subtract(this.lastAmt));
                }
            } else if (this.lastFluid != null) {
                // post removal of last fluid
                postUpdate(proxy, this.lastFluid, this.lastAmt.negate());
            } else if (this.host.mFluid != null) {
                // post new fluid
                postUpdate(proxy, this.host.mFluid, this.host.mStorageCurrent);
            }
        }
        update();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (shouldTick(aTick)) {
            if (isChanged()) {
                postStorageUpdateToAE2();
                faster();
            } else {
                slower();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public int getCapacity() {
        if (host == null || host.getBaseMetaTileEntity() == null
            || !host.getBaseMetaTileEntity()
                .isActive())
            return 0;
        if (host.mStorage.compareTo(MTEYottaFluidTank.MAX_INT_BIGINT) >= 0) {
            return Integer.MAX_VALUE;
        } else return host.mStorage.intValue();
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (host == null || host.getBaseMetaTileEntity() == null
            || !host.getBaseMetaTileEntity()
                .isActive())
            return 0;
        if (host.mLockedFluid != null && !host.mLockedFluid.isFluidEqual(resource)) return 0;
        if (host.mFluid == null || host.mFluid.isFluidEqual(resource)) {
            if (host.mFluid == null) {
                host.mFluid = resource.copy();
                host.mFluid.amount = 1;
            }

            if (host.addFluid(resource.amount, doFill)) {
                return resource.amount;
            } else {
                final int returned;
                if (host.getIsVoidExcessEnabled()) {
                    returned = resource.amount;
                } else {
                    final BigInteger delta = host.mStorage.subtract(host.mStorageCurrent);
                    returned = delta.intValueExact();
                }
                if (doFill) host.mStorageCurrent = host.mStorage;
                return returned;
            }
        }
        return 0;
    }

    public long fill(@SuppressWarnings("unused") ForgeDirection from, IAEFluidStack resource, boolean doFill) {
        if (host == null || host.getBaseMetaTileEntity() == null
            || !host.getBaseMetaTileEntity()
                .isActive())
            return 0;
        if (host.mLockedFluid != null && host.mLockedFluid.getFluid() != resource.getFluid()) return 0;
        if (host.mFluid == null || host.mFluid.getFluid() == resource.getFluid()) {
            if (host.mFluid == null) {
                host.mFluid = resource.getFluidStack(); // makes a copy internally
                host.mFluid.amount = 1;
            }

            if (host.addFluid(resource.getStackSize(), doFill)) {
                return resource.getStackSize();
            } else {
                final long returned;
                if (host.getIsVoidExcessEnabled()) {
                    returned = resource.getStackSize();
                } else {
                    final BigInteger delta = host.mStorage.subtract(host.mStorageCurrent);
                    returned = delta.longValueExact();
                }
                if (doFill) host.mStorageCurrent = host.mStorage;
                return returned;
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (host == null || host.getBaseMetaTileEntity() == null
            || !host.getBaseMetaTileEntity()
                .isActive())
            return null;
        if (host.mFluid == null || host.mFluid.getFluid() != resource.getFluid()) return null;
        int ready;
        if (host.mStorageCurrent.compareTo(MTEYottaFluidTank.MAX_INT_BIGINT) >= 0) {
            ready = Integer.MAX_VALUE;
        } else ready = host.mStorageCurrent.intValue();
        ready = Math.min(ready, resource.amount);
        if (doDrain) {
            host.reduceFluid(ready);
        }
        return new FluidStack(resource.getFluid(), ready);
    }

    public IAEFluidStack drain(@SuppressWarnings("unused") ForgeDirection from, IAEFluidStack resource,
        boolean doDrain) {
        if (host == null || host.getBaseMetaTileEntity() == null
            || !host.getBaseMetaTileEntity()
                .isActive())
            return null;
        if (host.mFluid == null || host.mFluid.getFluid() != resource.getFluid()) return null;
        long ready;
        if (host.mStorageCurrent.compareTo(MAX_LONG_BIGINT) > 0) {
            ready = Long.MAX_VALUE;
        } else ready = host.mStorageCurrent.longValue();
        ready = Math.min(ready, resource.getStackSize());
        if (doDrain) {
            host.reduceFluid(ready);
        }
        IAEFluidStack copy = resource.copy();
        copy.setStackSize(ready);
        return copy;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (host == null || host.getBaseMetaTileEntity() == null
            || !host.getBaseMetaTileEntity()
                .isActive())
            return null;
        if (host.mFluid == null) return null;
        final FluidStack drainStack = host.mFluid.copy();
        drainStack.amount = maxDrain;
        return this.drain(from, drainStack, doDrain);
    }

    private static final FluidTankInfo[] EMPTY_TANK_INFO = new FluidTankInfo[] { new FluidTankInfo(null, 0) };

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        if (host == null || host.getBaseMetaTileEntity() == null
            || !host.getBaseMetaTileEntity()
                .isActive())
            return EMPTY_TANK_INFO;

        return host.getTankInfo(from);
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(textureFont), };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(textureFont), };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEYOTTAHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public IGridNode getActionableNode() {
        AENetworkProxy gp = getProxy();
        return gp != null ? gp.getNode() : null;
    }

    @Override
    public AccessRestriction getAccess() {
        return this.readMode;
    }

    @Override
    public boolean isPrioritized(IAEFluidStack input) {
        return true;
    }

    @Override
    public boolean canAccept(IAEFluidStack input) {
        if (this.host == null) return false;
        FluidStack rInput = input.getFluidStack();
        return (host.mLockedFluid != null && !host.mLockedFluid.isFluidEqual(rInput)) || host.mFluid == null
            || host.mFluid.isFluidEqual(rInput);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List<IMEInventoryHandler> getCellArray(StorageChannel channel) {
        List<IMEInventoryHandler> list = new ArrayList<>();
        if (channel == StorageChannel.FLUIDS) {
            list.add(this);
        }
        return list;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    public boolean getSticky() {
        return this.isSticky;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public boolean validForPass(int i) {
        return true;
    }

    @Override
    public void saveChanges(IMEInventory cellInventory) {
        // This is handled by host itself.
    }

    private boolean isChanged() {
        if (this.host == null) return false;
        return !this.lastAmt.equals(this.host.mStorageCurrent) || this.lastFluid != this.host.mFluid;
    }

    private void update() {
        if (this.host == null) return;
        this.lastAmt = this.host.mStorageCurrent;
        this.lastFluid = this.host.mFluid;
    }

    private void faster() {
        if (this.tickRate > 15) {
            this.tickRate -= 5;
        }
    }

    private void slower() {
        if (this.tickRate < 100) {
            this.tickRate += 5;
        }
    }

    private boolean shouldTick(long tick) {
        if (this.host == null) return false;
        return tick % this.tickRate == 0;
    }
}
