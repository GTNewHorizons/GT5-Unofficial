package gregtech.loaders.postload;

import bloodasp.galacticgreg.WorldGenGaGT;
import gregtech.api.enums.GTStones;
import gregtech.api.enums.OreMixes;
import gregtech.api.enums.SmallOres;
import gregtech.api.util.GT_Log;
import gregtech.common.GT_Worldgenerator;

public class GT_Worldgenloader implements Runnable {

    public void run() {
        new GT_Worldgenerator();

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

        new WorldGenGaGT().run();
        GT_Log.out.println("Started Galactic Greg ore gen code");
    }
}
