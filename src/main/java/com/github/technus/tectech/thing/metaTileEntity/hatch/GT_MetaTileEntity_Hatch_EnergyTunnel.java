package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.pipe.IConnectsToEnergyTunnel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.CommonValues.TRANSFER_AT;
import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT;

/**
 * Created by danie_000 on 16.12.2016.
 */
public class GT_MetaTileEntity_Hatch_EnergyTunnel extends GT_MetaTileEntity_Hatch_EnergyMulti implements IConnectsToEnergyTunnel {
    private final long upkeep;
    private long packetsCount=0;

    public GT_MetaTileEntity_Hatch_EnergyTunnel(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(aID, aName, aNameRegional, aTier, 0, "Energy injecting terminal for Multiblocks",aAmp);
        upkeep=Math.max(V[mTier]/Amperes,V[4]);
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_Hatch_EnergyTunnel(String aName, int aTier, int aAmp, long aUpkeep, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aAmp, aDescription, aTextures);
        upkeep=aUpkeep;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, OVERLAYS_ENERGY_IN_LASER_TT[mTier]};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, OVERLAYS_ENERGY_IN_LASER_TT[mTier]};
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        packetsCount=aNBT.getLong("ePackets");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("ePackets",packetsCount);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier];
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier] * 4L * Amperes;
    }

    @Override
    public long maxAmperesIn() {
        return 0;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_EnergyTunnel(mName, mTier, Amperes, upkeep, mDescription, mTextures);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL,
                mDescription,
        };
    }

    @Override
    public boolean canConnect(byte side) {
        return isInputFacing(side);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            byte Tick = (byte) (aTick % 20);
            if (TRANSFER_AT == Tick) {
                if(packetsCount>0){
                    long diff=(maxEUStore()-aBaseMetaTileEntity.getStoredEU())/maxEUInput();
                    if(diff>0) {
                        setEUVar(aBaseMetaTileEntity.getStoredEU() + takePackets(diff) * maxEUInput());
                    }
                }
                if(aBaseMetaTileEntity.getStoredEU()>0){
                    setEUVar(aBaseMetaTileEntity.getStoredEU()-upkeep);
                    if(aBaseMetaTileEntity.getStoredEU()<0){
                        setEUVar(0);
                    }
                }
                getBaseMetaTileEntity().setActive(packetsCount>0);
            }
        }
    }

    public void addPackets(long count){
        packetsCount+=count;
        if(packetsCount>Amperes<<2){
            packetsCount=Amperes<<2;
        }
    }

    public long takePackets(long count){
        if(packetsCount>count){
            packetsCount-=count;
            return count;
        }else {
            count=packetsCount;
            packetsCount=0;
            return count;
        }
    }
}
