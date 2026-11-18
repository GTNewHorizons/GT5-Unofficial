package gregtech.loaders.postload;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import galacticgreg.WorldGeneratorSpace;
import gregtech.api.enums.GTStones;
import gregtech.api.enums.OreMixes;
import gregtech.api.enums.SmallOres;
import gregtech.common.GTWorldgenerator;
import gregtech.common.worldgen.WorldgenRubberTrees;

public class GTWorldgenloader implements Runnable {

    @Override
    public void run() {
        new GTWorldgenerator();
        new WorldGeneratorSpace();

        // GT Stones
        for (GTStones stone : GTStones.VALUES) {
            stone.addGTStone();
        }

        // Rubber Trees
        new WorldgenRubberTrees();

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
