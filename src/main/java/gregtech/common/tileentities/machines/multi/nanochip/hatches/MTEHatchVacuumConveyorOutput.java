package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import java.util.Collection;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.machines.multi.nanochip.MTEVacuumConveyorPipe;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryElement;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryNetwork;
import gregtech.common.tileentities.machines.multi.nanochip.util.IConnectsToVacuumConveyor;

public class MTEHatchVacuumConveyorOutput extends MTEHatchVacuumConveyor {

    public MTEHatchVacuumConveyorOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[] {});
    }

    public MTEHatchVacuumConveyorOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {

    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchVacuumConveyorOutput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return false;
    }

//    @Override
//    public boolean isComponentsInputFacing(ForgeDirection side) {
//        return isInputFacing(side);
//    }
//
//    @Override
//    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
//        IConnectsToVacuumConveyor current = this, source = this, next;
//        int range = 0;
//        while ((next = current.getNext(source)) != null && range++ < 1000) {
//            if (next instanceof MTEHatchVacuumConveyorInput) {
//                ((MTEHatchVacuumConveyorInput) next).unifyPacket(contents);
//                // Only clear contents if we actually moved the packet
//                contents = null;
//                break;
//            }
//            source = current;
//            current = next;
//        }
//    }

//    @Override
//    public IConnectsToVacuumConveyor getNext(IConnectsToVacuumConveyor source) {
//        IGregTechTileEntity base = getBaseMetaTileEntity();
//        byte color = base.getColorization();
//        if (color < 0) {
//            return null;
//        }
//        IGregTechTileEntity next = base.getIGregTechTileEntityAtSide(base.getFrontFacing());
//        if (next == null) {
//            return null;
//        }
//        IMetaTileEntity meta = next.getMetaTileEntity();
//        if (meta instanceof MTEVacuumConveyorPipe) {
//            ((MTEVacuumConveyorPipe) meta).markUsed();
//            return (IConnectsToVacuumConveyor) meta;
//        } else if (meta instanceof MTEHatchVacuumConveyorInput
//            && ((MTEHatchVacuumConveyorInput) meta).getBaseMetaTileEntity().getColorization() == color
//            && ((MTEHatchVacuumConveyorInput) meta).canConnect(
//                base.getFrontFacing()
//                    .getOpposite())) {
//                        return (IConnectsToVacuumConveyor) meta;
//                    }
//        return null;
//    }


    @Override
    public void getNeighbours(Collection<VacuumFactoryElement> neighbours) {

    }

    @Override
    public VacuumFactoryNetwork getNetwork() {
        return null;
    }

    @Override
    public void setNetwork(VacuumFactoryNetwork network) {

    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return false;
    }
}
