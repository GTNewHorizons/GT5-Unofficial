package tectech.thing.metaTileEntity.hatch.bec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryElement;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryGrid;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryNetwork;
import tectech.thing.metaTileEntity.hatch.MTEBaseFactoryHatch;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;

public class MTEHatchBEC extends MTEBaseFactoryHatch implements BECFactoryElement {

    private BECFactoryNetwork network;

    private final HashSet<MTEBECMultiblockBase<?>> controllers = new HashSet<>();

    protected MTEHatchBEC(MTEHatchBEC prototype) {
        super(prototype);
    }

    public MTEHatchBEC(int aID, String aName, int aTier) {
        super(aID, aName, aTier, null);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEHatchBEC(this);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        List<String> data = new ArrayList<>(Arrays.asList(super.getInfoData()));

        data.add("Network: " + (network == null ? "None" : network.id));

        return data.toArray(new String[0]);
    }

    @Override
    public BECFactoryElement.ConnectionType getConnectionOnSide(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing() ? BECFactoryElement.ConnectionType.CONNECTABLE
            : BECFactoryElement.ConnectionType.NONE;
    }

    @Override
    public void getNeighbours(Collection<BECFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        if (base.getTileEntityAtSide(base.getFrontFacing()) instanceof IGregTechTileEntity igte) {
            if (igte.getColorization() == base.getColorization()) {
                if (igte.getMetaTileEntity() instanceof BECFactoryElement element) {
                    neighbours.add(element);
                }
            }
        }

        neighbours.addAll(controllers);
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

        if (GTUtility.isServer()) {
            BECFactoryGrid.INSTANCE.updateElement(this);
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        if (GTUtility.isServer()) {
            BECFactoryGrid.INSTANCE.removeElement(this);
        }
    }

    @Override
    public void onFacingChange() {
        super.onFacingChange();

        if (GTUtility.isServer()) {
            BECFactoryGrid.INSTANCE.updateElement(this);
        }
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        if (GTUtility.isServer()) {
            BECFactoryGrid.INSTANCE.updateElement(this);
        }
    }

    public void addController(MTEBECMultiblockBase<?> controller) {
        controllers.add(controller);
        // multiblock is responsible for updating the network
    }

    public void removeController(MTEBECMultiblockBase<?> controller) {
        controllers.remove(controller);
        // multiblock is responsible for updating the network
    }
}
