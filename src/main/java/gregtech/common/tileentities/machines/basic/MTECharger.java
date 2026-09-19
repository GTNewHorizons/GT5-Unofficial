package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;

import java.util.List;

import net.minecraft.util.StatCollector;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicBatteryBuffer;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.tooltip.TooltipHelper;

@IMetaTileEntity.SkipGenerateDescription
@IMetaTileEntity.SkipGenerateName
public class MTECharger extends MTEBasicBatteryBuffer {

    /** The amperage a single chargeable battery lets this charger pull from the network. */
    public static final long AMPERES_IN_PER_BATTERY = 8L;

    /** The amperage this charger pulls even without a single chargeable battery installed. */
    public static final long MINIMUM_AMPERES_IN = 4L;

    /** The amperage a single battery lets this charger push into the network. */
    public static final long AMPERES_OUT_PER_BATTERY = 4L;

    /** The amperage this charger pushes even without a single battery installed. */
    public static final long MINIMUM_AMPERES_OUT = 2L;

    public MTECharger(int aID, String aName, String aNameRegional, int aTier, String aDescription, int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
    }

    public MTECharger(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("gt.blockmachines.batterycharger.desc"),
            StatCollector.translateToLocalFormatted("gt.blockmachines.slot_count.desc", mInventory.length) };
    }

    @Override
    public String getLocalName() {
        if (!hasOwnLocalName()) return super.getLocalName();
        return StatCollector.translateToLocalFormatted(
            "gt.blockmachines.batterycharger.name",
            GTValues.getLocalizedLongVoltageName(mTier),
            GTValues.VN[mTier]);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECharger(mName, mTier, mDescriptionArray, mTextures, mInventory.length);
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 64L * mInventory.length;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 256L * mInventory.length;
    }

    @Override
    public long maxAmperesIn() {
        return Math.max(mChargeableCount * AMPERES_IN_PER_BATTERY, MINIMUM_AMPERES_IN);
    }

    @Override
    public long maxAmperesOut() {
        return Math.max(mBatteryCount * AMPERES_OUT_PER_BATTERY, MINIMUM_AMPERES_OUT);
    }

    @Override
    public void addEnergyTooltipInformation(List<String> tooltip) {
        // The charger works with a higher amperage per battery than a plain battery buffer.
        addBatteryBufferVoltageLines(tooltip);
        tooltip.add(
            energyLine(
                "gt.tileentity.amperage_in.charger",
                TooltipHelper.ampText(AMPERES_IN_PER_BATTERY),
                TooltipHelper.ampText(MINIMUM_AMPERES_IN)));
        tooltip.add(
            energyLine(
                "gt.tileentity.amperage_out.charger",
                TooltipHelper.ampText(AMPERES_OUT_PER_BATTERY),
                TooltipHelper.ampText(MINIMUM_AMPERES_OUT)));
    }

    protected long getTransferMultiplier() {
        return 15;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (!(getBaseMetaTileEntity() instanceof BaseMetaTileEntity mBaseMetaTileEntity)) return;
        if (!(mBaseMetaTileEntity.getMetaTileEntity() instanceof MetaTileEntity mMetaTileEntity)) return;

        if (mMetaTileEntity.dechargerSlotCount() > 0
            && mBaseMetaTileEntity.getStoredEU() < mBaseMetaTileEntity.getEUCapacity()) {
            for (int i = mMetaTileEntity.dechargerSlotStartIndex(), k = mMetaTileEntity.dechargerSlotCount() + i; i
                < k; i++) {
                if (mMetaTileEntity.mInventory[i] != null
                    && mBaseMetaTileEntity.getStoredEU() < mBaseMetaTileEntity.getEUCapacity()) {
                    mBaseMetaTileEntity.increaseStoredEnergyUnits(
                        GTModHandler.dischargeElectricItem(
                            mMetaTileEntity.mInventory[i],
                            GTUtility.safeInt(
                                Math.min(
                                    V[mTier] * getTransferMultiplier(),
                                    mBaseMetaTileEntity.getEUCapacity() - mBaseMetaTileEntity.getStoredEU())),
                            (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getInputTier()),
                            true,
                            false,
                            false),
                        true);
                    if (mMetaTileEntity.mInventory[i].stackSize <= 0) mMetaTileEntity.mInventory[i] = null;
                }
            }
        }

        if (mMetaTileEntity.rechargerSlotCount() > 0 && mBaseMetaTileEntity.getStoredEU() > 0) {
            for (int i = mMetaTileEntity.rechargerSlotStartIndex(), k = mMetaTileEntity.rechargerSlotCount() + i; i
                < k; i++) {
                if (mBaseMetaTileEntity.getStoredEU() > 0 && mMetaTileEntity.mInventory[i] != null) {
                    mBaseMetaTileEntity.decreaseStoredEU(
                        GTModHandler.chargeElectricItem(
                            mMetaTileEntity.mInventory[i],
                            GTUtility.safeInt(
                                Math.min(V[mTier] * getTransferMultiplier(), mBaseMetaTileEntity.getStoredEU())),
                            (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getOutputTier()),
                            true,
                            false),
                        true);
                    if (mMetaTileEntity.mInventory[i].stackSize <= 0) mMetaTileEntity.mInventory[i] = null;
                }
            }
        }
    }
}
