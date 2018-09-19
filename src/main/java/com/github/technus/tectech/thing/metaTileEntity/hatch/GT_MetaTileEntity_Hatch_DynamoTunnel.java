package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Energy;
import com.github.technus.tectech.thing.metaTileEntity.pipe.IConnectsToEnergyTunnel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.CommonValues.TRANSFER_AT;
import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_POWER_TT;

/**
 * Created by danie_000 on 16.12.2016.
 */
public class GT_MetaTileEntity_Hatch_DynamoTunnel extends GT_MetaTileEntity_Hatch implements IConnectsToEnergyTunnel {
    public final int Amperes;
    private final long upkeep;
    private long packetsCount=0;

    public GT_MetaTileEntity_Hatch_DynamoTunnel(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(aID, aName, aNameRegional, aTier, 0, "Energy extracting terminal for Multiblocks");
        Amperes = aAmp;
        Util.setTier(aTier,this);
        upkeep=Math.max(V[mTier]/Amperes,V[4]);
    }

    public GT_MetaTileEntity_Hatch_DynamoTunnel(String aName, int aTier, int aAmp, long aUpkeep, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        Amperes = aAmp;
        upkeep=aUpkeep;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, OVERLAYS_ENERGY_IN_POWER_TT[mTier]};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, OVERLAYS_ENERGY_IN_POWER_TT[mTier]};
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
    public boolean isOutputFacing(byte aSide) {
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
    public long maxEUOutput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier] * 4L * Amperes;
    }

    @Override
    public long maxAmperesOut() {
        return 0;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_DynamoTunnel(mName, mTier, Amperes, upkeep, mDescription, mTextures);
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
                mDescription
        };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            byte Tick = (byte) (aTick % 20);
            if (TRANSFER_AT == Tick) {
                if(aBaseMetaTileEntity.getStoredEU()>=maxEUOutput()) {
                    long diff = aBaseMetaTileEntity.getStoredEU() / maxEUOutput();
                    setEUVar(aBaseMetaTileEntity.getStoredEU() - diff * maxEUOutput());
                    addPackets(diff);
                }
                if(packetsCount>0){
                    moveAround(aBaseMetaTileEntity);
                }
                if(packetsCount>0){
                    long diff=(maxEUStore()-aBaseMetaTileEntity.getStoredEU())/maxEUOutput();
                    if(diff>0) {
                        setEUVar(aBaseMetaTileEntity.getStoredEU() + takePackets(diff) * maxEUOutput());
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

    private void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        byte color = getBaseMetaTileEntity().getColorization();
        if (color < 0) {
            return;
        }
        byte front = aBaseMetaTileEntity.getFrontFacing();
        byte opposite = GT_Utility.getOppositeSide(front);
        for (short dist = 1; dist < 1000; dist++) {
            IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSideAndDistance(front, dist);
            if (tGTTileEntity != null && tGTTileEntity.getColorization() == color) {
                IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                if (aMetaTileEntity != null) {
                    if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyTunnel &&
                            opposite == tGTTileEntity.getFrontFacing()) {
                        if(maxEUOutput()>((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUInput()){
                            aMetaTileEntity.doExplosion(maxEUOutput());
                        }else if(maxEUOutput()==((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUInput()){
                            long ampRx=((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).Amperes;
                            if(packetsCount > ampRx && Amperes>ampRx){
                                tGTTileEntity.setToFire();
                            }else if(Amperes>ampRx){
                                tGTTileEntity.setOnFire();
                                ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).addPackets(takePackets(Amperes));
                            }else {
                                ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).addPackets(takePackets(Amperes));
                            }
                        }
                        return;
                    } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_Energy) {
                        if (((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).connectionCount > 2) {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override
    public boolean canConnect(byte side) {
        return isOutputFacing(side);
    }

    public void addPackets(long count){
        packetsCount+=count;
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
