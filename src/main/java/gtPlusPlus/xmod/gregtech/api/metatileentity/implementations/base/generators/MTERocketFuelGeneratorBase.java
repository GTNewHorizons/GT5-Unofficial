package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators;

import static gregtech.api.enums.GTValues.V;

import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
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
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getFront(i);
            rTextures[1][i + 1] = this.getBack(i);
            rTextures[2][i + 1] = this.getBottom(i);
            rTextures[3][i + 1] = this.getTop(i);
            rTextures[4][i + 1] = this.getSides(i);
            rTextures[5][i + 1] = this.getFrontActive(i);
            rTextures[6][i + 1] = this.getBackActive(i);
            rTextures[7][i + 1] = this.getBottomActive(i);
            rTextures[8][i + 1] = this.getTopActive(i);
            rTextures[9][i + 1] = this.getSidesActive(i);
        }
        return rTextures;
    }

    @Override
    public String[] getDescription() {
        String aPollution = "Causes between " + pollMin + " and " + pollMax + " Pollution per second";
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Fuel Efficiency: " + this.getEfficiency() + "%",
            aPollution,
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
