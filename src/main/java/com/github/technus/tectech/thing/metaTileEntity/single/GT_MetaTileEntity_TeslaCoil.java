package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_TM_teslaCoil;
import eu.usrv.yamcore.auxiliary.PlayerChatHelper;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBatteryBuffer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;


public class GT_MetaTileEntity_TeslaCoil extends GT_MetaTileEntity_BasicBatteryBuffer {
    public boolean powerPassToggle = false; //Power Pass for public viewing

    private int scanTime = 0; //Sets scan time to Z E R O :epic:
    private int scanTimeMin = 100; //Min scan time in ticks
    private int scanTimeTill = scanTimeMin; //Set default scan time

    private ArrayList<GT_MetaTileEntity_TM_teslaCoil> eTeslaTowerList = new ArrayList<>(); //Makes a list for BIGG Teslas

    private float histStep = 0.05F; //Hysteresis Resolution
    private float histLow = 0.25F; //Power pass is disabled if power is under this fraction
    private float histHigh = 0.75F; //Power pass is enabled if power is over this fraction
    private float histLowLimit = 0.05F; //How low can you configure it?
    private float histHighLimit = 0.95F; //How high can you configure it?

    private int scanRadiusTower = 64; //Radius for tower to tower transfers

    private long outputVoltage = 512; //Tesla Voltage Output
    private long outputCurrent = 1; //Tesla Current Output
    private long outputEuT = outputVoltage * outputCurrent; //Tesla Power Output


    public GT_MetaTileEntity_TeslaCoil(int aID, String aName, String aNameRegional, int aTier, int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, "Tesla Coil Transceiver", aSlotCount);
        Util.setTier(aTier, this);
    }

    public GT_MetaTileEntity_TeslaCoil(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    //TODO Redo the string formatting to be actually sane-ish
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            if (histHigh < histHighLimit && histHigh - histStep != histLow) {
                histHigh += histStep;
            } else {
                histHigh = histLow + histStep;
            }
            PlayerChatHelper.SendInfo(aPlayer, "Hysteresis High Changed to " + (histHigh * 100F)+ "%");
        } else {
            if (histLow > histLowLimit && histLow - histStep != histLow) {
                histLow -= histStep;
            } else {
                histLow = histHigh - histStep;
            }
            PlayerChatHelper.SendInfo(aPlayer, "Hysteresis Low Changed to " + (histLow * 100F)+ "%");
        }
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TeslaCoil(mName, mTier, mDescription, mTextures, mInventory.length);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            IGregTechTileEntity mte = getBaseMetaTileEntity();
            this.mCharge = aBaseMetaTileEntity.getStoredEU() / 2L > aBaseMetaTileEntity.getEUCapacity() / 3L;
            this.mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3L;
            this.mBatteryCount = 0;
            this.mChargeableCount = 0;
            ItemStack[] var4 = this.mInventory;
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                ItemStack tStack = var4[var6];
                if (GT_ModHandler.isElectricItem(tStack, this.mTier)) {
                    if (GT_ModHandler.isChargerItem(tStack)) {
                        ++this.mBatteryCount;
                    }
                    ++this.mChargeableCount;
                }
            }

            ////Hysteresis based ePowerPass Config
            long energyMax = getStoredEnergy()[1];
            long energyStored = getStoredEnergy()[0];

            float energyFrac = (float)energyStored/energyMax;
            System.err.println(energyFrac);

            //ePowerPass hist toggle
            if (!powerPassToggle && energyFrac > histHigh) {
                powerPassToggle = true;
            } else if (powerPassToggle && energyFrac < histLow) {
                powerPassToggle = false;
            }

            ////Scanning for active teslas
            scanTime++;
            if (scanTime >= scanTimeTill) {
                scanTime = 0;

                scanRadiusTower = 64; //TODO Generate depending on power stored
                eTeslaTowerList.clear();

                for (int xPosOffset = -scanRadiusTower; xPosOffset <= scanRadiusTower; xPosOffset++) {
                    for (int yPosOffset = -scanRadiusTower; yPosOffset <= scanRadiusTower; yPosOffset++) {
                        for (int zPosOffset = -scanRadiusTower; zPosOffset <= scanRadiusTower; zPosOffset++) {
                            if (xPosOffset == 0 && yPosOffset == 0 && zPosOffset == 0){
                                continue;
                            }
                            IGregTechTileEntity node = mte.getIGregTechTileEntityOffset(xPosOffset, yPosOffset, zPosOffset);
                            if (node == null) {
                                continue;
                            }
                            IMetaTileEntity nodeInside = node.getMetaTileEntity();
                            if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && node.isActive()){
                                eTeslaTowerList.add((GT_MetaTileEntity_TM_teslaCoil) nodeInside);
                            }
                        }
                    }
                }
            }

            //Stuff to do if ePowerPass
            if (powerPassToggle) {
                outputVoltage = 512;//TODO Set Depending On Tier
                outputCurrent = 1;//TODO Generate depending on count of batteries

                outputEuT = outputVoltage * outputCurrent;

                long requestedSumEU = 0;

                //Clean the large tesla list
                for (GT_MetaTileEntity_TM_teslaCoil Rx : eTeslaTowerList.toArray(new GT_MetaTileEntity_TM_teslaCoil[eTeslaTowerList.size()])) {
                    try {
                        requestedSumEU += Rx.maxEUStore() - Rx.getEUVar();
                    } catch (Exception e) {
                        eTeslaTowerList.remove(Rx);
                    }
                }

                //Try to send EU to big teslas
                for (GT_MetaTileEntity_TM_teslaCoil Rx : eTeslaTowerList) {
                    if (!Rx.powerPassToggle) {
                        long euTran = outputVoltage;
                        if (Rx.getEUVar() + euTran <= (Rx.maxEUStore()/2)) {
                            setEUVar(getEUVar() - euTran);
                            Rx.getBaseMetaTileEntity().increaseStoredEnergyUnits(euTran, true);
                            System.err.println("Energy Sent!");
                        }
                    }
                }
            }
        }
    }
}
