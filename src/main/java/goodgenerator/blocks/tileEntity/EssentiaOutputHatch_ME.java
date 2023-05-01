package goodgenerator.blocks.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import cpw.mods.fml.common.Optional;
import goodgenerator.util.ItemRefer;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumicenergistics.api.grid.IEssentiaGrid;
import thaumicenergistics.api.grid.IMEEssentiaMonitor;

@Optional.InterfaceList(
        value = {
                @Optional.Interface(
                        iface = "appeng.api.networking.security.IActionHost",
                        modid = "appliedenergistics2",
                        striprefs = true),
                @Optional.Interface(
                        iface = "appeng.me.helpers.IGridProxyable",
                        modid = "appliedenergistics2",
                        striprefs = true), })
public class EssentiaOutputHatch_ME extends EssentiaOutputHatch implements IActionHost, IGridProxyable {

    private AENetworkProxy gridProxy = null;
    private IMEEssentiaMonitor monitor = null;
    private MachineSource asMachineSource = new MachineSource(this);

    @Override
    public void updateEntity() {
        getProxy();
        super.updateEntity();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.invalidateAE();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        this.onChunkUnloadAE();
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    @Optional.Method(modid = "appliedenergistics2")
    public void readFromNBT_AENetwork(final NBTTagCompound data) {
        AENetworkProxy gp = getProxy();
        if (gp != null) getProxy().readFromNBT(data);
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    @Optional.Method(modid = "appliedenergistics2")
    public void writeToNBT_AENetwork(final NBTTagCompound data) {
        AENetworkProxy gp = getProxy();
        if (gp != null) gp.writeToNBT(data);
    }

    @Optional.Method(modid = "appliedenergistics2")
    void onChunkUnloadAE() {
        AENetworkProxy gp = getProxy();
        if (gp != null) gp.onChunkUnload();
    }

    @Optional.Method(modid = "appliedenergistics2")
    void invalidateAE() {
        AENetworkProxy gp = getProxy();
        if (gp != null) gp.invalidate();
    }

    @Optional.Method(modid = "appliedenergistics2")
    public IGridNode getGridNode(ForgeDirection forgeDirection) {
        AENetworkProxy gp = getProxy();
        return gp != null ? gp.getNode() : null;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public void gridChanged() {}

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.SMART;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public void securityBreak() {}

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            gridProxy = new AENetworkProxy(this, "proxy", ItemRefer.Essentia_Output_Hatch_ME.get(1), true);
            gridProxy.onReady();
            gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
        }
        return this.gridProxy;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IGridNode getActionableNode() {
        AENetworkProxy gp = getProxy();
        return gp != null ? gp.getNode() : null;
    }

    @Override
    public boolean takeFromContainer(AspectList aspects) {
        return false;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int amount) {
        return false;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, ForgeDirection side) {
        return this.addEssentia(aspect, amount, side, Actionable.MODULATE);
    }

    public int addEssentia(Aspect aspect, int amount, ForgeDirection side, Actionable mode) {
        long rejectedAmount = amount;
        if (this.getEssentiaMonitor()) {
            rejectedAmount = this.monitor.injectEssentia(aspect, amount, mode, this.getMachineSource(), true);
        }

        long acceptedAmount = (long) amount - rejectedAmount;
        return (int) acceptedAmount;
    }

    protected boolean getEssentiaMonitor() {
        IMEEssentiaMonitor essentiaMonitor = null;
        IGrid grid = null;
        IGridNode node = this.getProxy().getNode();

        if (node != null) {
            grid = node.getGrid();
            if (grid != null) essentiaMonitor = grid.getCache(IEssentiaGrid.class);
        }
        this.monitor = essentiaMonitor;
        return (this.monitor != null);
    }

    public MachineSource getMachineSource() {
        return this.asMachineSource;
    }
}
