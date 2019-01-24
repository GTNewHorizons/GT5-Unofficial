package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.Util;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBatteryBuffer;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

import static com.github.technus.tectech.CommonValues.V;

public class GT_MetaTileEntity_TeslaCoil extends GT_MetaTileEntity_BasicBatteryBuffer {
    public boolean ePowerPass = false;
    public boolean teslaCompatible = true;

    private int scanTime = 0;
    private int scanRadius = 64;//TODO Generate depending on power stored
    private long euTOutMax = V[9] / 8;//TODO Generate depending on count and kind of capacitors
    private ArrayList<GT_MetaTileEntity_TeslaCoil> eTeslaList = new ArrayList<>();

    public GT_MetaTileEntity_TeslaCoil(int aID, String aName, String aNameRegional, int aTier, int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, "Tesla Coil Transceiver", aSlotCount);
        Util.setTier(aTier, this);
    }

    public GT_MetaTileEntity_TeslaCoil(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TeslaCoil(mName, mTier, mDescription, mTextures, mInventory.length);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
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
            //This is where most things happen~~
        }
    }
}
