package gregtech.common.ec.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.VN;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.OrderedSectionBuilder;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.IScannerInfoComponent;
import gregtech.api.ec.component.IWailaDataComponent;
import gregtech.api.ec.component.multiblock.IMultiblockEnergyInputLogicComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchEnergyDebug;
import gregtech.api.util.ExoticEnergyInputHelper;
import gregtech.api.util.GTUtility;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

public class EnergyInputLogicComponent extends BaseHatchContainerComponent
    implements IMultiblockEnergyInputLogicComponent, IScannerInfoComponent, IWailaDataComponent {

    private final List<MTEHatchEnergy> mEnergyHatches = new ArrayList<>();
    private final List<MTEHatch> mExoticEnergyHatches = new ArrayList<>();
    private final List<MTEHatch> mAllEnergyHatches = new ArrayList<>();
    private boolean debugEnergyPresent = false;

    @Override
    public @NotNull List<MTEHatchEnergy> getNormalEnergyHatches() {
        return mEnergyHatches;
    }

    @Override
    public @NotNull List<MTEHatch> getExoticEnergyHatches() {
        return mExoticEnergyHatches;
    }

    @Override
    public void detachAllHatches() {
        clearCraftingIcons(mAllEnergyHatches);
        mEnergyHatches.clear();
        mExoticEnergyHatches.clear();
        mAllEnergyHatches.clear();
        debugEnergyPresent = false;
    }

    @Override
    public void explodeAllHatches(long power) {
        for (MetaTileEntity tTileEntity : getValidNormalEnergyHatches()) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(power);
        }
    }

    @Override
    public @NotNull List<MTEHatch> getEnergyInputHatches() {
        return mAllEnergyHatches;
    }

    public boolean tryAttachEnergyHatch(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatchEnergy hatch) {
            if (metaTile instanceof MTEHatchEnergyDebug) {
                debugEnergyPresent = true;
            }
            if (!mEnergyHatches.contains(hatch)) {
                onHatchAttached(hatch, baseCasingIndex);
                mEnergyHatches.add(hatch);
                mAllEnergyHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    public boolean tryAttachMultiAmpEnergyInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatchEnergyMulti hatch && hatch.getHatchType() == 1) {
            if (!mExoticEnergyHatches.contains(hatch)) {
                onHatchAttached(hatch, baseCasingIndex);
                mExoticEnergyHatches.add(hatch);
                mAllEnergyHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    public boolean tryAttachExoticEnergyInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatch hatch && ExoticEnergyInputHelper.isExoticEnergyInput(metaTile)) {
            if (!mExoticEnergyHatches.contains(hatch)) {
                onHatchAttached(hatch, baseCasingIndex);
                mExoticEnergyHatches.add(hatch);
                mAllEnergyHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isDebugEnergyPresent() {
        return debugEnergyPresent;
    }

    @Override
    public long getMaxInputVoltage() {
        return ExoticEnergyInputHelper.getMaxInputVoltageMulti(mAllEnergyHatches);
    }

    @Override
    public long getAverageInputVoltage() {
        return ExoticEnergyInputHelper.getAverageInputVoltageMulti(mAllEnergyHatches);
    }

    @Override
    public long getMaxInputAmps() {
        return ExoticEnergyInputHelper.getMaxWorkingInputAmpsMulti(mAllEnergyHatches);
    }

    @Override
    public long getMaxInputEu() {
        return ExoticEnergyInputHelper.getTotalEuMulti(mAllEnergyHatches);
    }

    @Override
    public long getMaxInputPower() {
        return ExoticEnergyInputHelper.getMaxInputPower(mEnergyHatches);
    }

    @Override
    public long getInputVoltageTier() {
        return ExoticEnergyInputHelper.getInputVoltageTier(mAllEnergyHatches);
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        return ExoticEnergyInputHelper.drainEnergy(aEU, mAllEnergyHatches);
    }

    @Override
    public void appendInfoData(@NotNull OrderedSectionBuilder<Consumer<List<String>>> builder) {
        builder.add(Section.ENERGY_INPUT, data -> {
            long storedEnergy = 0;
            long maxEnergy = 0;
            int hatchCount = 0;
            for (MTEHatch tHatch : getValidEnergyHatches()) {
                final IGregTechTileEntity te = tHatch.getBaseMetaTileEntity();
                if (te != null) {
                    storedEnergy += te.getStoredEU();
                    maxEnergy += te.getEUCapacity();
                    hatchCount++;
                }
            }
            if (hatchCount <= 0) return;
            data.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.multiblock.scanner.energy",
                    formatNumber(storedEnergy),
                    formatNumber(maxEnergy)));
            long totalInputPower = getMaxInputEu();
            byte voltageTier = getExoticEnergyHatches().isEmpty() ? (byte) getInputVoltageTier()
                : GTUtility.getTier(getAverageInputVoltage());
            byte powerTier = getExoticEnergyHatches().isEmpty() ? GTUtility.getTier(getMaxInputVoltage())
                : GTUtility.getTier(totalInputPower);
            if (voltageTier != 0) {
                data.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.multiblock.scanner.mei_amp",
                        formatNumber(totalInputPower),
                        GTUtility.getAmperageForTier(totalInputPower, voltageTier),
                        VN[voltageTier],
                        VN[powerTier]));
            } else {
                data.add(
                    StatCollector
                        .translateToLocalFormatted("GT5U.multiblock.scanner.mei", formatNumber(totalInputPower)));
            }
            var recipeLogic = tryGetComponent(IRecipeLogicComponent.KEY);
            if (recipeLogic != null) {
                long maxRecipeEUt = recipeLogic.getMaxAllowedRecipeEUt();
                if (maxRecipeEUt != -1) {
                    byte recipeTier = GTUtility.getTier(maxRecipeEUt);
                    if (recipeTier == 15) {
                        data.add(StatCollector.translateToLocal("GT5U.multiblock.scanner.mrv_infinite"));
                    } else {
                        data.add(
                            StatCollector.translateToLocalFormatted(
                                "GT5U.multiblock.scanner.mrv",
                                formatNumber(maxRecipeEUt),
                                VN[recipeTier]));
                    }
                }
            }
        });
    }

    @Override
    public void appendInfoMap(@NotNull Map<String, String> map) {
        long energy = 0, maxEnergy = 0, maxEnergyUsage = 0, minEnergyTier = Long.MAX_VALUE;

        for (MTEHatchEnergy tHatch : getValidNormalEnergyHatches()) {
            IGregTechTileEntity energyHatch = tHatch.getBaseMetaTileEntity();
            energy += energyHatch.getStoredEU();
            maxEnergy += energyHatch.getEUCapacity();
            maxEnergyUsage += energyHatch.getInputAmperage() * energyHatch.getInputVoltage();
            minEnergyTier = Math.min(minEnergyTier, energyHatch.getInputVoltage());
        }

        minEnergyTier = minEnergyTier == Long.MAX_VALUE ? 0 : minEnergyTier;

        map.put("energy", Long.toString(energy));
        map.put("maxEnergy", Long.toString(maxEnergy));
        map.put("maxEnergyUsage", Long.toString(maxEnergyUsage));
        map.put("minEnergyTier", Long.toString(minEnergyTier));
    }

    @Override
    public void appendWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x,
        int y, int z) {
        if (getMachine().isActive()) {
            tag.setLong("energyTier", getInputVoltageTier());
        }
    }
}
