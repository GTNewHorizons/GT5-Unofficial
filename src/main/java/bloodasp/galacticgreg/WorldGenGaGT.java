package bloodasp.galacticgreg;

import gregtech.api.enums.OreMixes;
import gregtech.api.enums.SmallOres;

public class WorldGenGaGT implements Runnable {

    @Override
    public void run() {
        new GT_Worldgenerator_Space();

        /*
         * This part here enables every GT Small Ore for Space Dims.
         */
        for (SmallOres ore : SmallOres.values()) {
            ore.addGaGregSmallOre();
        }

        /*
         * This part here enables every GT Ore for Space Dims.
         */

        for (OreMixes mix : OreMixes.values()) {
            mix.addGaGregOreLayer();
        }
    }

}
