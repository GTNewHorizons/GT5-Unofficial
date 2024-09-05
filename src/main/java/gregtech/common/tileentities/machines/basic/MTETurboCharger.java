package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

/**
 * Created by danie_000 on 15.10.2016.
 */
public class MTETurboCharger extends MTECharger {

    public MTETurboCharger(int aID, String aName, String aNameRegional, int aTier, String aDescription,
        int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
    }

    public MTETurboCharger(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTETurboCharger(mName, mTier, mDescriptionArray, mTextures, mInventory.length);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[2][17][];
        for (byte b = -1; b < 16; b++) {
            rTextures[0][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1] };
            rTextures[1][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_POWER[mTier] };
        }
        return rTextures;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 1536L * mInventory.length;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 6144L * mInventory.length;
    }

    @Override
    public long maxAmperesIn() {
        return 16L * mInventory.length;
    }

    @Override
    public long maxAmperesOut() {
        return 4L * mInventory.length;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {

            mCharge = aBaseMetaTileEntity.getStoredEU() / 2 > aBaseMetaTileEntity.getEUCapacity() / 3
                || !aBaseMetaTileEntity.isAllowedToWork();
            mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3
                && aBaseMetaTileEntity.isAllowedToWork();
            mBatteryCount = 0;
            mChargeableCount = 0;
            for (ItemStack tStack : mInventory) {
                if (GTModHandler.isElectricItem(tStack, mTier)) {
                    if (GTModHandler.isChargerItem(tStack)) {
                        mBatteryCount++;
                    }
                    mChargeableCount++;
                }
            }

            if (getBaseMetaTileEntity() instanceof BaseMetaTileEntity) {
                BaseMetaTileEntity mBaseMetaTileEntity = (BaseMetaTileEntity) getBaseMetaTileEntity();
                if (mBaseMetaTileEntity.getMetaTileEntity() instanceof MetaTileEntity) {
                    MetaTileEntity mMetaTileEntity = (MetaTileEntity) mBaseMetaTileEntity.getMetaTileEntity();
                    if (mMetaTileEntity.dechargerSlotCount() > 0
                        && mBaseMetaTileEntity.getStoredEU() < mBaseMetaTileEntity.getEUCapacity()) {
                        for (int i = mMetaTileEntity.dechargerSlotStartIndex(),
                            k = mMetaTileEntity.dechargerSlotCount() + i; i < k; i++) {
                            if (mMetaTileEntity.mInventory[i] != null
                                && mBaseMetaTileEntity.getStoredEU() < mBaseMetaTileEntity.getEUCapacity()) {
                                // CODE
                                mBaseMetaTileEntity.increaseStoredEnergyUnits(
                                    GTModHandler.dischargeElectricItem(
                                        mMetaTileEntity.mInventory[i],
                                        GTUtility.safeInt(
                                            Math.min(
                                                V[mTier] * 120,
                                                mBaseMetaTileEntity.getEUCapacity()
                                                    - mBaseMetaTileEntity.getStoredEU())),
                                        (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getInputTier()),
                                        true,
                                        false,
                                        false),
                                    true);
                                if (mMetaTileEntity.mInventory[i].stackSize <= 0) {
                                    mMetaTileEntity.mInventory[i] = null;
                                }
                            }
                        }
                    }
                    if (mMetaTileEntity.rechargerSlotCount() > 0 && mBaseMetaTileEntity.getStoredEU() > 0) {
                        for (int i = mMetaTileEntity.rechargerSlotStartIndex(),
                            k = mMetaTileEntity.rechargerSlotCount() + i; i < k; i++) {
                            if (mBaseMetaTileEntity.getStoredEU() > 0 && mMetaTileEntity.mInventory[i] != null) {
                                // CODE
                                mBaseMetaTileEntity
                                    .decreaseStoredEU(
                                        GTModHandler.chargeElectricItem(
                                            mMetaTileEntity.mInventory[i],
                                            GTUtility
                                                .safeInt(Math.min(V[mTier] * 120, mBaseMetaTileEntity.getStoredEU())),
                                            (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getOutputTier()),
                                            true,
                                            false),
                                        true);
                                if (mMetaTileEntity.mInventory[i].stackSize <= 0) {
                                    mMetaTileEntity.mInventory[i] = null;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
