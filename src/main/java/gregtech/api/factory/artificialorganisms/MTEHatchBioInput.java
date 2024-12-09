package gregtech.api.factory.artificialorganisms;

import java.util.Collection;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.objects.ArtificialOrganism;

public class MTEHatchBioInput extends MTEHatch implements AOFactoryElement {

    public MTEHatchBioInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Accepts Artificial Organisms");
    }

    public MTEHatchBioInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
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
        return new MTEHatchBioInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.BIO;
    }

    public ArtificialOrganism getAO() {
        return null;
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return false;
    }

    @Override
    public void getNeighbours(Collection<AOFactoryElement> neighbours) {

    }

    @Override
    public AOFactoryNetwork getNetwork() {
        return null;
    }

    @Override
    public void setNetwork(AOFactoryNetwork network) {

    }
}
