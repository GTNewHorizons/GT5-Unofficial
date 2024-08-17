package gregtech.common.tileentities.machines.multi.artificialorganisms.hatches;

import java.util.ArrayList;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.common.tileentities.machines.multi.artificialorganisms.util.IConnectsToBioPipe;

public class GT_MetaTileEntity_Hatch_BioInput extends GT_MetaTileEntity_Hatch implements IConnectsToBioPipe {

    private GT_MetaTileEntity_Hatch_BioOutput networkOutput;

    public GT_MetaTileEntity_Hatch_BioInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Accepts Artificial Organisms");
    }

    public GT_MetaTileEntity_Hatch_BioInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier] };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_BioInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public ArrayList<IConnectsToBioPipe> getConnected(GT_MetaTileEntity_Hatch_BioOutput output,
        ArrayList<IConnectsToBioPipe> connections) {
        networkOutput = output;
        connections.add(this);
        return connections;
    }

    @Override
    public boolean isComponentsInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.BIO;
    }

    public ArtificialOrganism getAO() {
        if (networkOutput != null) {
            return networkOutput.currentSpecies;
        }
        return null;
    }
}
