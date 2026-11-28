package gregtech.common.ec.multiblock;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.component.multiblock.IMultiblockEnergyOutputLogicComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class EnergyOutputLogicComponent extends BaseHatchContainerComponent
    implements IMultiblockEnergyOutputLogicComponent {

    private final List<MTEHatchDynamo> mDynamoHatches = new ArrayList<>();
    private final List<MTEHatch> mExoticDynamoHatches = new ArrayList<>();
    private final List<MTEHatch> mAllDynamoHatches = new ArrayList<>();
    private boolean explodeOnFailure = true;
    private boolean strictCapacityLimit = true;

    public EnergyOutputLogicComponent setExplodeOnFailure(boolean explodeOnFailure) {
        this.explodeOnFailure = explodeOnFailure;
        return this;
    }

    public EnergyOutputLogicComponent setStrictCapacityLimit(boolean strictCapacityLimit) {
        this.strictCapacityLimit = strictCapacityLimit;
        return this;
    }

    @Override
    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        if (aEU <= 0) return true;
        if (mAllDynamoHatches.isEmpty()) {
            return false;
        }
        long injected = 0;
        long totalOutput = 0;
        long aFirstVoltageFound = -1;
        boolean aFoundMixedDynamos = false;
        for (MTEHatch aDynamo : mAllDynamoHatches) {
            if (aDynamo == null || !aDynamo.isValid()) continue;
            long aVoltage = aDynamo.maxEUOutput();
            long aTotal = aDynamo.maxAmperesOut() * aVoltage;
            // Check against voltage to check when hatch mixing
            if (aFirstVoltageFound == -1) {
                aFirstVoltageFound = aVoltage;
            } else if (aFirstVoltageFound != aVoltage) {
                aFoundMixedDynamos = true;
            }
            totalOutput += aTotal;
        }

        if (totalOutput < aEU || (aFoundMixedDynamos && !aAllowMixedVoltageDynamos)) {
            if (explodeOnFailure) {
                getMachine().explodeMultiblock();
            }
            return false;
        }

        long leftToInject;
        long aVoltage;
        long aAmpsToInject;
        long aRemainder;
        long ampsOnCurrentHatch;
        for (MTEHatch aDynamo : mAllDynamoHatches) {
            if (aDynamo == null || !aDynamo.isValid()) continue;
            leftToInject = aEU - injected;
            if (leftToInject <= 0) break;
            aVoltage = aDynamo.maxEUOutput();
            aAmpsToInject = leftToInject / aVoltage;
            aRemainder = leftToInject - (aAmpsToInject * aVoltage);
            ampsOnCurrentHatch = Math.min(aDynamo.maxAmperesOut(), aAmpsToInject);
            var mte = aDynamo.getBaseMetaTileEntity();
            if (strictCapacityLimit) {
                if (ampsOnCurrentHatch <= 1 || !isFullNativeDynamo(aDynamo)) {
                    for (long i = 0; i < ampsOnCurrentHatch; i++) {
                        mte.increaseStoredEnergyUnits(aVoltage, false);
                    }
                }
            } else {
                mte.increaseStoredEnergyUnits(aVoltage * ampsOnCurrentHatch, false);
            }
            injected += aVoltage * ampsOnCurrentHatch;
            if (aRemainder > 0 && ampsOnCurrentHatch < aDynamo.maxAmperesOut()) {
                mte.increaseStoredEnergyUnits(aRemainder, false);
                injected += aRemainder;
            }
        }
        return injected > 0;
    }

    @Override
    public @NotNull List<MTEHatchDynamo> getNormalDynamoHatches() {
        return mDynamoHatches;
    }

    @Override
    public @NotNull List<MTEHatch> getExoticDynamoHatches() {
        return mExoticDynamoHatches;
    }

    @Override
    public @NotNull List<MTEHatch> getEnergyOutputHatches() {
        return mAllDynamoHatches;
    }

    @Override
    public boolean tryAttachNormalDynamo(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatchDynamo hatch && hatch.maxAmperesOut() <= 4) {
            if (!mDynamoHatches.contains(hatch)) {
                onHatchAttached(metaTile, baseCasingIndex);
                mDynamoHatches.add(hatch);
                mAllDynamoHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean tryAttachExoticDynamo(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatchDynamoMulti hatch) {
            if (!mExoticDynamoHatches.contains(hatch)) {
                onHatchAttached(metaTile, baseCasingIndex);
                mExoticDynamoHatches.add(hatch);
                mAllDynamoHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean tryAttachLaserSource(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatchDynamoTunnel hatch) {
            if (!mExoticDynamoHatches.contains(hatch)) {
                onHatchAttached(metaTile, baseCasingIndex);
                mExoticDynamoHatches.add(hatch);
                mAllDynamoHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public void detachAllHatches() {
        clearCraftingIcons(mAllDynamoHatches);
        mDynamoHatches.clear();
        mExoticDynamoHatches.clear();
        mAllDynamoHatches.clear();
    }

    @Override
    public void explodeAllHatches(long power) {
        for (MetaTileEntity tTileEntity : getValidNormalDynamoHatches()) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(power);
        }
    }

    protected static boolean isFullNativeDynamo(MTEHatch dynamo) {
        if (dynamo.getClass() != MTEHatchDynamoMulti.class || dynamo.maxAmperesOut() <= 1) return false;
        IGregTechTileEntity base = dynamo.getBaseMetaTileEntity();
        return base != null && base.getClass() == BaseMetaTileEntity.class
            && base.getMetaTileEntity() == dynamo
            && base.getStoredEU() >= base.getEUCapacity();
    }
}
