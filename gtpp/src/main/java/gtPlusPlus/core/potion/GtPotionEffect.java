package gtPlusPlus.core.potion;

import net.minecraft.potion.PotionEffect;

public class GtPotionEffect extends PotionEffect {

    public GtPotionEffect(int aPotionID, int aDurationInSecs, int aLevel) {
        super(aPotionID, aDurationInSecs * 20, aLevel, false);
    }
}
