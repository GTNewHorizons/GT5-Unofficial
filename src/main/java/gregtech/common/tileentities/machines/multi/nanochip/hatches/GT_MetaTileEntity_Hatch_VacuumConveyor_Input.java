package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.machines.multi.nanochip.util.IConnectsToVacuumConveyor;

public class GT_MetaTileEntity_Hatch_VacuumConveyor_Input extends GT_MetaTileEntity_Hatch_VacuumConveyor
    implements IConnectsToVacuumConveyor {

    public GT_MetaTileEntity_Hatch_VacuumConveyor_Input(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[] {});
    }

    public GT_MetaTileEntity_Hatch_VacuumConveyor_Input(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_VacuumConveyor_Input(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isComponentsInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public IConnectsToVacuumConveyor getNext(IConnectsToVacuumConveyor source) {
        return null;
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {}
}
