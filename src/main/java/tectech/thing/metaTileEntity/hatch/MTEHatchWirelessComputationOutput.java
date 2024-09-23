package tectech.thing.metaTileEntity.hatch;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.WirelessComputationPacket;
import tectech.mechanics.dataTransport.QuantumDataPacket;

public class MTEHatchWirelessComputationOutput extends MTEHatchDataOutput {

    private int clearDelay = 0;

    public MTEHatchWirelessComputationOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);

    }

    public MTEHatchWirelessComputationOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);

    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchWirelessComputationOutput(mName, mTier, mDescriptionArray, mTextures);
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
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("clearDelay")) {
            this.clearDelay = aNBT.getInteger("clearDelay");
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("clearDelay", this.clearDelay);
    }

    @Override
    public void providePacket(QuantumDataPacket packet) {
        super.providePacket(packet);
        // Keep providing to wireless net for 21 ticks, because after this time a new packet from the computer should
        // have arrived
        this.clearDelay = 21;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && q != null) {
            WirelessComputationPacket.uploadData(aBaseMetaTileEntity.getOwnerUuid(), q.getContent(), aTick);
            if (clearDelay-- == 0) {
                q = null;
            }
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
