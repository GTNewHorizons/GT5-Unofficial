package gregtech.api.factory.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import it.unimi.dsi.fastutil.Pair;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.hatch.MTEBaseFactoryHatch;
import tectech.util.CommonValues;

public class TestFactoryHatch extends MTEBaseFactoryHatch implements TestFactoryElement {

    public TestFactoryHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { CommonValues.TEC_MARK_EM,
                StatCollector.translateToLocal("gt.blockmachines.hatch.datain.desc.0"),
                StatCollector.translateToLocal("gt.blockmachines.hatch.datain.desc.1"),
                EnumChatFormatting.AQUA + StatCollector.translateToLocal("gt.blockmachines.hatch.datain.desc.2") });
    }

    protected TestFactoryHatch(TestFactoryHatch prototype) {
        super(prototype);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TestFactoryHatch(this);
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
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        List<String> data = new ArrayList<>(Arrays.asList(super.getInfoData()));

        if (network == null) {
            data.add(StatCollector.translateToLocal("GT5U.infodata.no_network"));
        } else {
            for (TestFactoryHatch hatch : network.getComponents(TestFactoryHatch.class)) {
                IGregTechTileEntity base = hatch.getBaseMetaTileEntity();

                data.add(
                    base.getXCoord() + ", " + base.getYCoord() + ", " + base.getZCoord() + ": " + hatch.toString());
            }
        }

        return data.toArray(new String[data.size()]);
    }

    @Override
    public List<Pair<Class<?>, Object>> getComponents() {
        return Collections.singletonList(Pair.of(TestFactoryHatch.class, this));
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void getNeighbours(Collection<TestFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base.getTileEntityAtSide(base.getFrontFacing()) instanceof IGregTechTileEntity igte
            && igte.getColorization() == base.getColorization()
            && igte.getMetaTileEntity() instanceof TestFactoryElement element) {
            neighbours.add(element);
        }
    }

    private TestFactoryNetwork network;

    @Override
    public TestFactoryNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(TestFactoryNetwork network) {
        this.network = network;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);

        TestFactoryGrid.INSTANCE.addElement(this);
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        TestFactoryGrid.INSTANCE.removeElement(this);
    }

    @Override
    public void onFacingChange() {
        super.onFacingChange();

        TestFactoryGrid.INSTANCE.addElement(this);
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        TestFactoryGrid.INSTANCE.addElement(this);
    }
}
