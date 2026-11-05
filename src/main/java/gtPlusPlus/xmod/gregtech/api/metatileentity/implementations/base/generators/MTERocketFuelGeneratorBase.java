package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators;

import static gregtech.api.enums.GTValues.V;

import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
import gregtech.api.util.GTSplit;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;

public abstract class MTERocketFuelGeneratorBase extends MTEBasicGenerator {

    protected int pollMin, pollMax;

    public MTERocketFuelGeneratorBase(final int aID, final String aName, final String aNameRegional, final int aTier,
        final String aDescription, final ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aDescription, aTextures);
        pollMin = (int) (PollutionConfig.baseMinPollutionPerSecondRocketFuelGenerator
            * PollutionConfig.pollutionReleasedByTierRocketFuelGenerator[mTier]);
        pollMax = (int) (PollutionConfig.baseMaxPollutionPerSecondRocketFuelGenerator
            * PollutionConfig.pollutionReleasedByTierRocketFuelGenerator[mTier]);
    }

    public MTERocketFuelGeneratorBase(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        pollMin = (int) (PollutionConfig.baseMinPollutionPerSecondRocketFuelGenerator
            * PollutionConfig.pollutionReleasedByTierRocketFuelGenerator[mTier]);
        pollMax = (int) (PollutionConfig.baseMaxPollutionPerSecondRocketFuelGenerator
            * PollutionConfig.pollutionReleasedByTierRocketFuelGenerator[mTier]);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            GTSplit.splitLocalizedFormatted(
                "gt.blockmachines.advancedgenerator.rocketfuel.desc",
                this.getEfficiency(),
                pollMin,
                pollMax),
            GTPPCore.GT_Tooltip.get());
    }

    @Override
    public boolean isFacingValid(final ForgeDirection side) {
        return side.offsetY == 0;
    }

    @Override
    public boolean isOutputFacing(final ForgeDirection side) {
        return this.getBaseMetaTileEntity()
            .getFrontFacing() == side;
    }

    @Override
    public long maxEUStore() {
        return Math.max(this.getEUVar(), (V[this.mTier] * 500) + this.getMinimumStoredEU());
    }

    @Override
    public int getPollution() {
        return MathUtils.randInt(pollMin, pollMax);
    }

    @Override
    public int getCapacity() {
        return 32000;
    }
}
