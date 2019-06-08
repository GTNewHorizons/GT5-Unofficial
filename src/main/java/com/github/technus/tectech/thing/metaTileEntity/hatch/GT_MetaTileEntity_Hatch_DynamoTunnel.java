package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Energy;
import com.github.technus.tectech.thing.metaTileEntity.pipe.IConnectsToEnergyTunnel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.CommonValues.TRANSFER_AT;
import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT;

/**
 * Created by danie_000 on 16.12.2016.
 */
public class GT_MetaTileEntity_Hatch_DynamoTunnel extends GT_MetaTileEntity_Hatch_DynamoMulti implements IConnectsToEnergyTunnel {
    public GT_MetaTileEntity_Hatch_DynamoTunnel(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(aID, aName, aNameRegional, aTier, 0, "Energy extracting terminal for Multiblocks",aAmp);
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_Hatch_DynamoTunnel(String aName, int aTier, int aAmp, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aAmp, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, OVERLAYS_ENERGY_OUT_LASER_TT[mTier]};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, OVERLAYS_ENERGY_OUT_LASER_TT[mTier]};
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
        return new GT_MetaTileEntity_Hatch_DynamoTunnel(mName, mTier, Amperes, mDescription, mTextures);
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
                "Throughput: "+ EnumChatFormatting.YELLOW +(Amperes*maxEUOutput())+EnumChatFormatting.RESET+" EU/t"
        };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            byte Tick = (byte) (aTick % 20);
            if (TRANSFER_AT == Tick) {
                if(aBaseMetaTileEntity.getStoredEU()>0){
                    setEUVar(aBaseMetaTileEntity.getStoredEU()-Amperes);
                    if(aBaseMetaTileEntity.getStoredEU()<0){
                        setEUVar(0);
                    }
                }
                if(aBaseMetaTileEntity.getStoredEU()>getMinimumStoredEU()){
                    moveAround(aBaseMetaTileEntity);
                }
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
                            setEUVar(aBaseMetaTileEntity.getStoredEU()-maxEUOutput());
                            return;
                        }else if(maxEUOutput()==((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUInput()) {
                            long diff=Math.min(
                                    Amperes*20,
                                    Math.min(
                                            ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUStore()-
                                                    aMetaTileEntity.getBaseMetaTileEntity().getStoredEU(),
                                            maxEUStore()-aBaseMetaTileEntity.getStoredEU()
                                    )/maxEUOutput()
                            )*maxEUOutput();

                            setEUVar(aBaseMetaTileEntity.getStoredEU()-diff);

                            ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity)
                                    .setEUVar(aMetaTileEntity.getBaseMetaTileEntity().getStoredEU()+diff);
                        }
                        return;
                    } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_Energy) {
                        if (((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).connectionCount != 2) {
                            return;
                        }else {
                            ((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).markUsed();
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
}
