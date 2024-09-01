package tectech.thing.metaTileEntity.hatch;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.WirelessComputationPacket;

public class GT_MetaTileEntity_Hatch_WirelessComputation_Output extends GT_MetaTileEntity_Hatch_OutputData {

    public GT_MetaTileEntity_Hatch_WirelessComputation_Output(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);

    }

    public GT_MetaTileEntity_Hatch_WirelessComputation_Output(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);

    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_WirelessComputation_Output(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && q != null) {
            WirelessComputationPacket.uploadData(aBaseMetaTileEntity.getOwnerUuid(), q.getContent(), aTick);
            q = null;
        }
    }

    private static String[] tooltips;

    @Override
    public String[] getDescription() {
        if (tooltips == null) {
            tooltips = new String[] { "Wireless Computation Output for Multiblocks" };
        }
        return tooltips;
    }
}
