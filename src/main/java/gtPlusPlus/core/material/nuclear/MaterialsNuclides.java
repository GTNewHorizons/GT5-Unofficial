package gtPlusPlus.core.material.nuclear;

import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialReconstruction;

public final class MaterialsNuclides {

    public static final Material Li2BeF4 = MaterialReconstruction.byName("LithiumTetrafluoroberyllateLFTB");

    public static final Material LiFBeF2ThF4UF4 = MaterialReconstruction.byName("LFTRFuel3");

    public static final Material LiFBeF2ZrF4UF4 = MaterialReconstruction.byName("LFTRFuel2");

    public static final Material LiFBeF2ZrF4U235 = MaterialReconstruction.byName("LFTRFuel1");

    // Misc
    public static final Material BurntLftrFuel_MK1 = MaterialReconstruction.byName("BurntReactorFuelI");

    public static final Material BurntLftrFuel_MK2 = MaterialReconstruction.byName("BurntReactorFuelII");

    // LFTR Core Fluids
    public static final Material LiFBeF2UF4FP = MaterialReconstruction.byName("UraniumDepletedMoltenSaltUSalt");

    public static final Material Sparged_LiFBeF2UF4FP = MaterialReconstruction.byName("HeliumSpargedUSalt");

    public static final Material UF6F2FP = MaterialReconstruction.byName("PhosphorousUraniumHexafluoridePUF6");

    public static final Material LiFBeF2 = MaterialReconstruction.byName("StableMoltenSaltBase");

    public static final Material LiFBeF2UF4 = MaterialReconstruction.byName("LFTRFuelBase");

    // LFTR Blanket Fluids

    // Tier 1 Fuel blanket output
    public static final Material LiFThF4 = MaterialReconstruction.byName("ThoriumDepletedMoltenSaltTSalt");

    // Tier 2 Fuel blanket output
    public static final Material LiFBeF2ThF4 = MaterialReconstruction
        .byName("ThoriumBerylliumDepletedMoltenSaltTBSalt");

    // Tier 1 Fuel blanket output
    public static final Material Sparged_LiFThF4 = MaterialReconstruction.byName("FluorineSpargedTSalt");

    // Tier 2 Fuel blanket output
    public static final Material Sparged_LiFBeF2ThF4 = MaterialReconstruction.byName("FluorineSpargedTBSalt");

    public static final Material UF6F2 = MaterialReconstruction.byName("FluorinatedUraniumHexafluorideFUF6");
}
