package gregtech.common.tileentities.machines.multi.artificialorganisms.hatches;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.common.tileentities.machines.multi.artificialorganisms.util.IConnectsToBioPipe;

public class GT_MetaTileEntity_Hatch_BioOutput extends GT_MetaTileEntity_Hatch implements IConnectsToBioPipe {

    ArrayList<IConnectsToBioPipe> pipenetwork;
    ArtificialOrganism currentSpecies = new ArtificialOrganism();

    public GT_MetaTileEntity_Hatch_BioOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Distributes Artificial Organisms");
    }

    public GT_MetaTileEntity_Hatch_BioOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
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
        return new GT_MetaTileEntity_Hatch_BioOutput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public ArrayList<IConnectsToBioPipe> getConnected(GT_MetaTileEntity_Hatch_BioOutput output,
        ArrayList<IConnectsToBioPipe> connections) {
        IGregTechTileEntity baseTE = getBaseMetaTileEntity();
        TileEntity next = baseTE.getTileEntityAtSide(baseTE.getFrontFacing());
        IMetaTileEntity meta = ((IGregTechTileEntity) next).getMetaTileEntity();
        pipenetwork = new ArrayList<>();
        pipenetwork.add(this);
        if (meta instanceof IConnectsToBioPipe) return ((IConnectsToBioPipe) meta).getConnected(output, pipenetwork);
        return null;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        pipenetwork = getConnected(this, new ArrayList<>());
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 1200 == 0) pipenetwork = getConnected(this, new ArrayList<>());
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isComponentsInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.BIO;
    }
}
