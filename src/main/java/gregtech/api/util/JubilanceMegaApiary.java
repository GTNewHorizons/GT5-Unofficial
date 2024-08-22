package gregtech.api.util;

import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IJubilanceProvider;

public class JubilanceMegaApiary implements IJubilanceProvider {

    public static final JubilanceMegaApiary instance = new JubilanceMegaApiary();

    protected JubilanceMegaApiary() {}

    @Override
    public boolean isJubilant(IAlleleBeeSpecies species, IBeeGenome genome, IBeeHousing housing) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Will only be produced in mega Apiary";
    }
}
