package gregtech.loaders.misc;

import net.minecraft.potion.Potion;

import gregtech.common.config.Gregtech;
import gregtech.loaders.misc.potions.GTPotionMagLev;

public class GTPotions implements Runnable {

    public static Potion potionMagLev;

    public GTPotions() {}

    @Override
    public void run() {
        potionMagLev = (new GTPotionMagLev(Gregtech.potionIDs.magLevPotionID, false, 0)).setIconIndex(0, 0)
            .setPotionName("MagLev");
    }
}
