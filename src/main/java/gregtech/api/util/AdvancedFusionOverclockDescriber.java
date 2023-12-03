package gregtech.api.util;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.objects.overclockdescriber.FusionOverclockDescriber;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AdvancedFusionOverclockDescriber extends FusionOverclockDescriber {

    public AdvancedFusionOverclockDescriber(byte energyTier, long capableStartup) {
        super(energyTier, capableStartup);
    }

    @Override
    protected int getEUtIncreasePerOC() {
        return 2;
    }

    protected int getDurationDecreasePerOC() {
        return 2;
    }
}
