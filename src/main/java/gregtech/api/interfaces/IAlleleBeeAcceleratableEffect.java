package gregtech.api.interfaces;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IEffectData;

public interface IAlleleBeeAcceleratableEffect {
    IEffectData doEffectAccelerated(IBeeGenome genome, IEffectData storedData, IBeeHousing housing, float did);
}
