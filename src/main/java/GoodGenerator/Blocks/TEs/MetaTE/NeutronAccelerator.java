package GoodGenerator.Blocks.TEs.MetaTE;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import net.minecraft.nbt.NBTTagCompound;

import static gregtech.api.enums.GT_Values.V;

public class NeutronAccelerator extends GT_MetaTileEntity_Hatch_Energy {

    public boolean isRunning;

    public NeutronAccelerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public NeutronAccelerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public int getMaxEUConsume() {
        return (int)(V[mTier] * 10 / 8);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.isRunning = aNBT.getBoolean("isRunning");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("isRunning", this.isRunning);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new NeutronAccelerator(mName, mTier, this.getDescription(), mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Input EU to Accelerate the Neutron!",
                "Max EU input: " + this.maxEUInput(),
                "Max EU consumption: " + this.getMaxEUConsume(),
                "Every EU can be transformed into 0.1~0.2 KeV Neutron Kinetic Energy."
        };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (this.getBaseMetaTileEntity().isServerSide()) {
            if (aBaseMetaTileEntity.getStoredEU() >= getMaxEUConsume()) {
                setEUVar(aBaseMetaTileEntity.getStoredEU() - getMaxEUConsume());
                isRunning = true;
            } else {
                isRunning = false;
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }
}
