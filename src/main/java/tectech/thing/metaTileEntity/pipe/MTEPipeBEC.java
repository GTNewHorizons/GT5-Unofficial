package tectech.thing.metaTileEntity.pipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.covers.CoverShutter;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryElement;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryGrid;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryNetwork;
import tectech.mechanics.boseEinsteinCondensate.BECInventory;

public class MTEPipeBEC extends MTEBaseFactoryPipe implements BECFactoryElement {

    private BECFactoryNetwork network;
    private int oldColour;

    public MTEPipeBEC(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        mThickness = 3f / 4f;
    }

    public MTEPipeBEC(MTEPipeBEC prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEPipeBEC(this);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, int aConnections, int colorIndex,
        boolean aConnected, boolean aRedstone) {

        List<ITexture> textures = new ArrayList<>(2);

        textures.add(
            TextureFactory.builder()
                .addIcon(EM_PIPE)
                .setRGBA(Dyes.getModulation(colorIndex, new short[] { 0x81, 0xCA, 0xED, 0 }))
                .build());

        if (getActive()) {
            textures.add(
                TextureFactory.builder()
                    .addIcon(EM_BAR)
                    .setRGBA(Dyes.getModulation(colorIndex, new short[] { 0x81, 0xCA, 0xED, 0 }))
                    .build());
        }

        return textures.toArray(new ITexture[0]);
    }

    private boolean wasAllowedToWork = false;

    @Override
    public void onPostTick(IGregTechTileEntity base, long aTick) {
        super.onPostTick(base, aTick);

        if (base.isAllowedToWork() != wasAllowedToWork) {
            wasAllowedToWork = base.isAllowedToWork();

            boolean hasShutter = false;

            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (base.getCoverAtSide(side) instanceof CoverShutter) {
                    hasShutter = true;
                    break;
                }
            }

            if (hasShutter) {
                BECFactoryGrid.INSTANCE.addElement(this);
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        super.loadNBTData(nbtTagCompound);
        wasAllowedToWork = nbtTagCompound.getBoolean("wasAllowedToWork");
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        super.saveNBTData(nbtTagCompound);
        nbtTagCompound.setBoolean("wasAllowedToWork", wasAllowedToWork);
    }

    @Override
    protected void checkActive() {
        mIsActive = network != null && !network.getComponents(BECInventory.class)
            .isEmpty();
    }

    @Override
    public ConnectionType getConnectionOnSide(ForgeDirection side) {
        if (getBaseMetaTileEntity().getCoverAtSide(side) instanceof CoverShutter shutter) {
            if (shutter.letsEnergyIn()) {
                return ConnectionType.CONNECTABLE;
            }

            if (shutter.alwaysLookConnected()) {
                return ConnectionType.VISUAL_ONLY;
            }

            return ConnectionType.NONE;
        } else {
            return ConnectionType.CONNECTABLE;
        }
    }

    @Override
    public void getNeighbours(Collection<BECFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (this.getConnectionOnSide(dir) != ConnectionType.CONNECTABLE) continue;

            if (!(base.getTileEntityAtSide(dir) instanceof IGregTechTileEntity igte)) continue;

            if (igte.getColorization() != base.getColorization()) continue;

            if (!(igte.getMetaTileEntity() instanceof BECFactoryElement element)) continue;

            if (element.getConnectionOnSide(dir.getOpposite()) != ConnectionType.CONNECTABLE) continue;

            neighbours.add(element);
        }
    }

    @Override
    public void onAdjacentBlockChange(int x, int y, int z) {
        mCheckConnections = true;
    }

    @Override
    public void onNeighbourChanged(BECFactoryElement neighbour) {
        mCheckConnections = true;
    }

    @Override
    protected void checkConnections() {
        mConnections = 0;

        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (base.getTileEntityAtSide(side) instanceof IGregTechTileEntity igte) {
                if (igte.getColorization() == base.getColorization()) {
                    if (igte.getMetaTileEntity() instanceof BECFactoryElement element) {
                        if (element.getConnectionOnSide(side.getOpposite()) != ConnectionType.NONE) {
                            mConnections |= side.flag;
                        }
                    }
                }
            }
        }
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
            "Network: " + accessor.getNBTData()
                .getString("network"));
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        List<String> data = new ArrayList<>(Arrays.asList(super.getInfoData()));

        if (network == null) {
            data.add("No network");
        } else {
            for (BECInventory inv : network.getComponents(BECInventory.class)) {
                data.add(
                    inv.getContents()
                        .toString());
            }
        }

        return data.toArray(new String[data.size()]);
    }

    @Override
    public BECFactoryNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(BECFactoryNetwork network) {
        this.network = network;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);

        BECFactoryGrid.INSTANCE.addElement(this);
        oldColour = aBaseMetaTileEntity.getColorization();
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        BECFactoryGrid.INSTANCE.removeElement(this);
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.getTimer() == 0) return;

       if (oldColour == aColor) return;
       oldColour = aColor;

        BECFactoryGrid.INSTANCE.addElement(this);
    }
}
