package gregtech.loaders.postload;

import galacticgreg.WorldGeneratorSpace;
import gregtech.api.enums.GTStones;
import gregtech.api.enums.OreMixes;
import gregtech.api.enums.SmallOres;
import gregtech.api.util.GTLog;
import gregtech.common.GTWorldgenerator;

import static gregtech.GTLoggers.GT_FML_LOGGER;

public class GTWorldgenloader implements Runnable {

    @Override
    public void run() {
        new GTWorldgenerator();
        new WorldGeneratorSpace();

        // GT Stones
        for (GTStones stone : GTStones.VALUES) {
            stone.addGTStone();
        }

        // GT Default Small Ores
        for (SmallOres smallOre : SmallOres.values()) {
            smallOre.addGTSmallOre();
        }

        // GT Veins registration
        for (OreMixes oreMix : OreMixes.VALUES) {
            oreMix.addGTOreLayer();
        }

        GT_FML_LOGGER.debug("Started Galactic Greg ore gen code");
    }
}
