package goodgenerator.blocks.tileEntity.GTMetaTileEntity;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkCellArrayUpdate;
import appeng.api.networking.events.MENetworkStorageEvent;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.ICellContainer;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IItemList;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import cpw.mods.fml.common.Optional;
import extracells.util.FluidUtil;
import goodgenerator.blocks.tileEntity.YottaFluidTank;
import goodgenerator.loader.Loaders;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

@Optional.InterfaceList(
        value = {
            @Optional.Interface(
                    iface = "appeng.api.networking.security.IActionHost",
                    modid = "appliedenergistics2",
                    striprefs = true),
            @Optional.Interface(
                    iface = "appeng.me.helpers.IGridProxyable",
                    modid = "appliedenergistics2",
                    striprefs = true),
            @Optional.Interface(
                    iface = "appeng.api.storage.IMEInventory",
                    modid = "appliedenergistics2",
                    striprefs = true),
            @Optional.Interface(
                    iface = "appeng.api.storage.IMEInventoryHandler",
                    modid = "appliedenergistics2",
                    striprefs = true),
            @Optional.Interface(
                    iface = "appeng.api.storage.ICellContainer",
                    modid = "appliedenergistics2",
                    striprefs = true),
        })
public class YOTTAHatch extends GT_MetaTileEntity_Hatch
        implements IGridProxyable,
                IActionHost,
                ICellContainer,
                IMEInventory<IAEFluidStack>,
                IMEInventoryHandler<IAEFluidStack> {

    private static final IIconContainer textureFont = new Textures.BlockIcons.CustomIcon("icons/YOTTAHatch");

    private YottaFluidTank host;
    private AENetworkProxy gridProxy = null;
    private int priority;
    private byte tickRate = 20;
    private String lastFluid = "";
    private BigInteger lastAmt = BigInteger.ZERO;
    private AccessRestriction readMode = AccessRestriction.READ_WRITE;
    private final AccessRestriction[] AEModes = new AccessRestriction[] {
        AccessRestriction.NO_ACCESS, AccessRestriction.READ, AccessRestriction.WRITE, AccessRestriction.READ_WRITE
    };

    public YOTTAHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, new String[] {
            "Special I/O port for EC2.",
            "Directly connected YOTTank with AE fluid storage system.",
            "Use screwdriver to set storage priority",
            "Use soldering iron to set read/write mode"
        });
    }

    public YOTTAHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public void setTank(YottaFluidTank te) {
        this.host = te;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mAEPriority", this.priority);
        aNBT.setInteger("mAEMode", this.readMode.ordinal());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.priority = aNBT.getInteger("mAEPriority");
        this.readMode = AEModes[aNBT.getInteger("mAEMode")];
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public final void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) this.priority -= 10;
        else this.priority += 10;
        GT_Utility.sendChatToPlayer(
                aPlayer, String.format(StatCollector.translateToLocal("yothatch.chat.0"), this.priority));
    }

    @Override
    public boolean onSolderingToolRightClick(
            byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.readMode = AEModes[(readMode.ordinal() + 1) % 4];
        GT_Utility.sendChatToPlayer(
                aPlayer, String.format(StatCollector.translateToLocal("yothatch.chat.1"), this.readMode));
        return true;
    }

    @Optional.Method(modid = "appliedenergistics2")
    public IGridNode getGridNode(ForgeDirection forgeDirection) {
        AENetworkProxy gp = getProxy();
        return gp != null ? gp.getNode() : null;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.SMART;
    }

    @Optional.Method(modid = "appliedenergistics2")
    public void securityBreak() {}

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            gridProxy = new AENetworkProxy(this, "proxy", Loaders.YFH, true);
            gridProxy.onReady();
            gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
        }
        return this.gridProxy;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public DimensionalCoord getLocation() {
        IGregTechTileEntity gtm = this.getBaseMetaTileEntity();
        return new DimensionalCoord(gtm.getWorld(), gtm.getXCoord(), gtm.getYCoord(), gtm.getZCoord());
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IItemList<IAEFluidStack> getAvailableItems(IItemList<IAEFluidStack> out) {
        if (host == null
                || host.getBaseMetaTileEntity() == null
                || !host.getBaseMetaTileEntity().isActive()) return out;
        if (host.mFluidName == null
                || host.mFluidName.equals("")
                || host.mStorageCurrent.compareTo(BigInteger.ZERO) <= 0) return out;
        long ready;
        if (host.mStorageCurrent.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            ready = Long.MAX_VALUE;
        } else ready = host.mStorageCurrent.longValue();
        out.add(FluidUtil.createAEFluidStack(FluidRegistry.getFluid(host.mFluidName), ready));
        return out;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IAEFluidStack injectItems(IAEFluidStack input, Actionable type, BaseActionSource src) {
        long amt = fill(null, input, false);
        if (amt == input.getStackSize()) {
            if (type.equals(Actionable.MODULATE)) fill(null, input, true);
            return null;
        }
        return input;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IAEFluidStack extractItems(IAEFluidStack request, Actionable mode, BaseActionSource src) {
        IAEFluidStack ready = drain(null, request, false);
        if (ready != null) {
            if (mode.equals(Actionable.MODULATE)) drain(null, request.getFluidStack(), true);
            return ready;
        } else return null;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public StorageChannel getChannel() {
        return StorageChannel.FLUIDS;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (shouldTick(aTick)) {
            if (isChanged()) {
                IGridNode node = getGridNode(null);
                if (node != null) {
                    IGrid grid = node.getGrid();
                    if (grid != null) {
                        grid.postEvent(new MENetworkCellArrayUpdate());
                        IStorageGrid storageGrid = grid.getCache(IStorageGrid.class);
                        if (storageGrid == null) {
                            node.getGrid().postEvent(new MENetworkStorageEvent(null, StorageChannel.FLUIDS));
                        } else {
                            node.getGrid()
                                    .postEvent(new MENetworkStorageEvent(
                                            storageGrid.getFluidInventory(), StorageChannel.FLUIDS));
                        }
                        node.getGrid().postEvent(new MENetworkCellArrayUpdate());
                    }
                }
                faster();
                update();
            } else {
                slower();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public int getCapacity() {
        if (host == null
                || host.getBaseMetaTileEntity() == null
                || !host.getBaseMetaTileEntity().isActive()) return 0;
        if (host.mStorage.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0) {
            return Integer.MAX_VALUE;
        } else return host.mStorage.intValue();
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (host == null
                || host.getBaseMetaTileEntity() == null
                || !host.getBaseMetaTileEntity().isActive()) return 0;
        if (host.mFluidName == null
                || host.mFluidName.equals("")
                || host.mFluidName.equals(resource.getFluid().getName())) {
            host.mFluidName = resource.getFluid().getName();
            if (host.mStorage.subtract(host.mStorageCurrent).compareTo(BigInteger.valueOf(resource.amount)) >= 0) {
                if (doFill) host.addFluid(resource.amount);
                return resource.amount;
            } else {
                int added = host.mStorage.subtract(host.mStorageCurrent).intValue();
                if (doFill) host.addFluid(added);
                return host.getIsVoidExcessEnabled() ? resource.amount : added;
            }
        }
        return 0;
    }

    public long fill(ForgeDirection from, IAEFluidStack resource, boolean doFill) {
        if (host == null
                || host.getBaseMetaTileEntity() == null
                || !host.getBaseMetaTileEntity().isActive()) return 0;
        if (host.mFluidName == null
                || host.mFluidName.equals("")
                || host.mFluidName.equals(resource.getFluid().getName())) {
            host.mFluidName = resource.getFluid().getName();
            if (host.mStorage.subtract(host.mStorageCurrent).compareTo(BigInteger.valueOf(resource.getStackSize()))
                    >= 0) {
                if (doFill) host.addFluid(resource.getStackSize());
                return resource.getStackSize();
            } else {
                long added = host.mStorage.subtract(host.mStorageCurrent).longValue();
                if (doFill) host.addFluid(added);
                return added;
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (host == null
                || host.getBaseMetaTileEntity() == null
                || !host.getBaseMetaTileEntity().isActive()) return null;
        if (host.mFluidName == null
                || host.mFluidName.equals("")
                || !host.mFluidName.equals(resource.getFluid().getName())) return null;
        int ready;
        if (host.mStorageCurrent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            ready = Integer.MAX_VALUE;
        } else ready = host.mStorageCurrent.intValue();
        ready = Math.min(ready, resource.amount);
        if (doDrain) {
            host.reduceFluid(ready);
        }
        return new FluidStack(resource.getFluid(), ready);
    }

    public IAEFluidStack drain(ForgeDirection from, IAEFluidStack resource, boolean doDrain) {
        if (host == null
                || host.getBaseMetaTileEntity() == null
                || !host.getBaseMetaTileEntity().isActive()) return null;
        if (host.mFluidName == null
                || host.mFluidName.equals("")
                || !host.mFluidName.equals(resource.getFluid().getName())) return null;
        long ready;
        if (host.mStorageCurrent.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
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
        if (host == null
                || host.getBaseMetaTileEntity() == null
                || !host.getBaseMetaTileEntity().isActive()) return null;
        if (host.mFluidName == null || host.mFluidName.equals("")) return null;
        return this.drain(from, FluidRegistry.getFluidStack(host.mFluidName, maxDrain), doDrain);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        FluidTankInfo[] tankInfo = new FluidTankInfo[1];
        tankInfo[0] = new FluidTankInfo(null, 0);
        if (host == null
                || host.getBaseMetaTileEntity() == null
                || !host.getBaseMetaTileEntity().isActive()) return tankInfo;
        FluidStack fluid = null;
        if (host.mFluidName != null && !host.mFluidName.equals("")) {
            int camt;
            if (host.mStorageCurrent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) camt = Integer.MAX_VALUE;
            else camt = host.mStorageCurrent.intValue();
            fluid = FluidRegistry.getFluidStack(host.mFluidName, camt);
        }

        tankInfo[0] = new FluidTankInfo(fluid, getCapacity());
        return tankInfo;
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
        return new ITexture[] {
            aBaseTexture, TextureFactory.of(textureFont),
        };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] {
            aBaseTexture, TextureFactory.of(textureFont),
        };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new YOTTAHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IGridNode getActionableNode() {
        AENetworkProxy gp = getProxy();
        return gp != null ? gp.getNode() : null;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AccessRestriction getAccess() {
        return this.readMode;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public boolean isPrioritized(IAEFluidStack input) {
        return true;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public boolean canAccept(IAEFluidStack input) {
        FluidStack rInput = input.getFluidStack();
        return fill(null, rInput, false) > 0;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public List<IMEInventoryHandler> getCellArray(StorageChannel channel) {
        List<IMEInventoryHandler> list = new ArrayList<>();
        if (channel == StorageChannel.FLUIDS) {
            list.add(this);
        }
        return list;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public int getPriority() {
        return this.priority;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public int getSlot() {
        return 0;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public boolean validForPass(int i) {
        return true;
    }

    @Override
    public void blinkCell(int slot) {}

    @Override
    public void saveChanges(IMEInventory cellInventory) {
        // This is handled by host itself.
    }

    private boolean isChanged() {
        if (this.host == null) return false;
        return !this.lastAmt.equals(this.host.mStorageCurrent) || !this.lastFluid.equals(this.host.mFluidName);
    }

    private void update() {
        if (this.host == null) return;
        this.lastAmt = this.host.mStorageCurrent;
        this.lastFluid = this.host.mFluidName;
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
