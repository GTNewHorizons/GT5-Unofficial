package gregtech.loaders.postload;

import galacticgreg.WorldGeneratorSpace;
import gregtech.api.enums.GTStones;
import gregtech.api.enums.OreMixes;
import gregtech.api.enums.SmallOres;
import gregtech.api.util.GTLog;
import gregtech.common.GTWorldgenerator;

public class GTWorldgenloader implements Runnable {

    public void run() {
        new GTWorldgenerator();
        new WorldGeneratorSpace();

        // GT Stones
        for (GTStones stone : GTStones.values()) {
            stone.addGTStone();
        }

        // GT Default Small Ores
        for (SmallOres smallOre : SmallOres.values()) {
            smallOre.addGTSmallOre();
        }

        // GT Veins registration
        for (OreMixes oreMix : OreMixes.values()) {
            oreMix.addGTOreLayer();
        }

        GTLog.out.println("Started Galactic Greg ore gen code");
    }
}
