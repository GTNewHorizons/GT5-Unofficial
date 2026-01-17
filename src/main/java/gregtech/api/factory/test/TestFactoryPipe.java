package gregtech.api.factory.test;

import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.pipe.MTEBaseFactoryPipe;

public class TestFactoryPipe extends MTEBaseFactoryPipe implements TestFactoryElement {

    public TestFactoryPipe(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TestFactoryPipe(TestFactoryPipe prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new TestFactoryPipe(this);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("network", network == null ? "null" : network.toString());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.network",
                accessor.getNBTData()
                    .getString("network")));
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return true;
    }

    @Override
    public void getNeighbours(Collection<TestFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (base.getTileEntityAtSide(dir) instanceof IGregTechTileEntity igte) {
                if (igte.getColorization() == base.getColorization()) {
                    if (igte.getMetaTileEntity() instanceof TestFactoryElement element) {
                        if (element.canConnectOnSide(dir.getOpposite())) {
                            neighbours.add(element);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void checkConnections() {
        mConnections = 0;

        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (base.getTileEntityAtSide(dir) instanceof IGregTechTileEntity igte) {
                if (igte.getColorization() == base.getColorization()) {
                    if (igte.getMetaTileEntity() instanceof TestFactoryElement element) {
                        if (element.canConnectOnSide(dir.getOpposite())) {
                            mConnections |= dir.flag;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void checkActive() {
        mIsActive = getBaseMetaTileEntity().getTimer() % 200 > 100;
    }

    private TestFactoryNetwork network;

    @Override
    public TestFactoryNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(TestFactoryNetwork network) {
        this.network = network;
        mCheckConnections = true;
    }

    @Override
    public void onEdgeChanged(TestFactoryElement adjacent) {
        mCheckConnections = true;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);

        TestFactoryGrid.INSTANCE.updateElement(this);
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        TestFactoryGrid.INSTANCE.removeElement(this);
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        TestFactoryGrid.INSTANCE.updateElement(this);
    }
}
