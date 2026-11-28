package gregtech.common.ec.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.interfaces.tileentity.IGregTechDeviceInformation.encode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.GTMod;
import gregtech.api.ec.IECMultiblock;
import gregtech.api.ec.OrderedSectionBuilder;
import gregtech.api.ec.component.IOnActiveStateChangeComponent;
import gregtech.api.ec.component.IOnFirstTickComponent;
import gregtech.api.ec.component.IPollutionEmitterComponent;
import gregtech.api.ec.component.ISavableComponent;
import gregtech.api.ec.component.IScannerInfoComponent;
import gregtech.api.ec.component.multiblock.IMultiblockPollutionLogicComponent;
import gregtech.api.ec.component.multiblock.IOnStructureStateChangeComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;

public class PollutionLogicComponent extends BaseHatchContainerComponent
    implements IMultiblockPollutionLogicComponent, IOnActiveStateChangeComponent, IOnStructureStateChangeComponent,
    ISavableComponent, IScannerInfoComponent, IOnFirstTickComponent {

    private final List<MTEHatchMuffler> mMufflerHatches = new ArrayList<>();
    private int mPollution = 0;
    private boolean oldMufflerState = false;

    @Override
    public @NotNull List<MTEHatchMuffler> getPollutionHatches() {
        return mMufflerHatches;
    }

    @Override
    public boolean tryAttachMuffler(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatchMuffler hatch) {
            if (!mMufflerHatches.contains(hatch)) {
                onHatchAttached(metaTile, baseCasingIndex);
                mMufflerHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public void detachAllHatches() {
        clearCraftingIcons(mMufflerHatches);
        setMufflers(false);
        mMufflerHatches.clear();
    }

    @Override
    public void explodeAllHatches(long power) {
        for (MetaTileEntity tTileEntity : getValidPollutionHatches()) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(power);
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private void setMufflers(boolean state) {
        oldMufflerState = state;
        final int size = mMufflerHatches.size();
        for (int i = 0; i < size; i++) {
            final MTEHatchMuffler muffler = mMufflerHatches.get(i);
            final IGregTechTileEntity tile = muffler.getBaseMetaTileEntity();
            if (tile == null || tile.isDead()) continue;
            tile.setActive(state);
        }
    }

    private void setMufflersIfChanged(boolean newState) {
        if (newState != oldMufflerState) {
            setMufflers(newState);
        }
    }

    @Override
    public int getAveragePollutionPercentage() {
        int pollutionPercent = 0;
        int mufflerCount = 0;
        for (MTEHatchMuffler muffler : getValidPollutionHatches()) {
            pollutionPercent += muffler.calculatePollutionReduction(100);
            mufflerCount++;
        }
        if (mufflerCount > 0) {
            pollutionPercent /= mufflerCount;
        } else {
            pollutionPercent = 100;
        }
        return pollutionPercent;
    }

    @Override
    public boolean polluteEnvironment(int aPollutionLevel) {
        final int VENT_AMOUNT = 10_000;
        // Early exit if pollution is disabled
        if (!GTMod.proxy.mPollution) return true;
        mPollution += aPollutionLevel;
        if (mPollution < VENT_AMOUNT) {
            setMufflersIfChanged(
                getMachine().getBaseMetaTileEntity()
                    .isActive() && mPollution > 0);
            return true;
        }
        if (mMufflerHatches.isEmpty()) {
            // No muffler present. Fail.
            return false;
        } else if (mMufflerHatches.size() == 1) {
            // One muffler, use simple method for performance.
            MTEHatchMuffler muffler = mMufflerHatches.getFirst();
            if (muffler == null || !muffler.isValid()) {
                // Muffler invalid. Fail.
                mMufflerHatches.removeFirst();
                return false;
            }
            if (muffler.polluteEnvironment((MetaTileEntity) getMachine(), VENT_AMOUNT)) {
                mPollution -= VENT_AMOUNT;
            } else {
                // Muffler blocked. Fail.
                return false;
            }
        } else {
            // Multiple mufflers, split pollution output evenly between all of them.
            int mufflerCount = 0;
            int ventAmount = 0; // Allow venting of up to VENT_AMOUNT of pollution per muffler.
            for (MTEHatchMuffler _ : getValidPollutionHatches()) {
                mufflerCount++;
                if (ventAmount + VENT_AMOUNT <= mPollution) {
                    ventAmount += VENT_AMOUNT;
                }
            }
            if (mufflerCount == 0) return false;
            // This might lose some small amount of pollution due to rounding, this is fine.
            ventAmount /= mufflerCount;

            for (MTEHatchMuffler muffler : getValidPollutionHatches()) {
                if (muffler.polluteEnvironment((MetaTileEntity) getMachine(), ventAmount)) {
                    mPollution -= ventAmount;
                } else {
                    // Muffler blocked. Fail.
                    return false;
                }
            }
        }
        boolean success = mPollution < VENT_AMOUNT;
        if (success) {
            setMufflersIfChanged(
                getMachine().getBaseMetaTileEntity()
                    .isActive() && mPollution > 0);
        }
        return success;
    }

    @Override
    public void onMachineActivated(@NotNull IGregTechTileEntity igte) {
        if (igte.isServerSide()) setMufflersIfChanged(igte.isActive() && mPollution > 0);
    }

    @Override
    public void onMachineDeactivated(@NotNull IGregTechTileEntity igte) {
        if (igte.isServerSide()) setMufflersIfChanged(igte.isActive() && mPollution > 0);
    }

    @Override
    public @NotNull String getSaveKey() {
        return "pollution";
    }

    @Override
    public @Nullable NBTTagCompound saveComponentData() {
        var nbt = new NBTTagCompound();
        nbt.setInteger("mPollution", mPollution);
        return nbt;
    }

    @Override
    public void loadLegacyNBTData(@NotNull NBTTagCompound nbt) {
        mPollution = nbt.getInteger("mPollution");
    }

    @Override
    public void loadComponentData(@NotNull NBTTagCompound nbt) {
        mPollution = nbt.getInteger("mPollution");
    }

    @Override
    public void appendInfoData(@NotNull OrderedSectionBuilder<Consumer<List<String>>> builder) {
        builder.addIf(
            hasComponent(IPollutionEmitterComponent.KEY),
            Section.POLLUTION,
            data -> {
                data.add(encode("GT5U.multiblock.scanner.pollution", formatNumber(getAveragePollutionPercentage())));
            });
    }

    @Override
    public void appendInfoMap(@NotNull Map<String, String> map) {
        map.put("pollution", Double.toString(getAveragePollutionPercentage() / 100F));
    }

    @Override
    public void onStructureFormed(@NotNull IGregTechTileEntity igte) {
        setMufflersIfChanged(igte.isActive() && mPollution > 0);
    }

    @Override
    protected IECMultiblock validMultiblock(@NotNull IECMultiblock multiblock) {
        if (multiblock instanceof MetaTileEntity) {
            return multiblock;
        }
        throw new IllegalArgumentException(
            "Multiblock must implement MetaTileEntity, but got: " + multiblock.getClass()
                .getName());
    }

    @Override
    public void onFirstTick(@NotNull IGregTechTileEntity igte) {
        if (igte.isServerSide()) setMufflersIfChanged(igte.isActive() && mPollution > 0);
    }
}
