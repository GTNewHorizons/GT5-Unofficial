package gtPlusPlus.core.material.nuclear;

import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialReconstruction;

public class MaterialsFluorides {

    public static final Material FLUORITE = MaterialReconstruction.byName("FluoriteF");

    // ThF4
    public static final Material THORIUM_TETRAFLUORIDE = MaterialReconstruction.byName("ThoriumTetrafluoride");

    // ThF6
    public static final Material THORIUM_HEXAFLUORIDE = MaterialReconstruction.byName("ThoriumHexafluoride");

    // UF4
    public static final Material URANIUM_TETRAFLUORIDE = MaterialReconstruction.byName("UraniumTetrafluoride");

    // UF6
    public static final Material URANIUM_HEXAFLUORIDE = MaterialReconstruction.byName("UraniumHexafluoride");

    // ZrF4

    public static final Material ZIRCONIUM_TETRAFLUORIDE = MaterialReconstruction.byName("ZirconiumTetrafluoride");

    /*
     * public static final Material ZIRCONIUM_TETRAFLUORIDE = new Material( "Zirconium Tetrafluoride", //Material Name
     * MaterialState.LIQUID, //State null, //Material Colour -1, //Melting Point in C -1, //Boiling Point in C -1,
     * //Protons -1, //Neutrons false, //Uses Blast furnace? //Material Stacks with Percentage of required elements. new
     * MaterialStack[]{ new MaterialStack(ELEMENT.getInstance().ZIRCONIUM, 1), new
     * MaterialStack(MaterialsElements.getInstance().FLUORINE, 4) });
     */

    // BeF2
    public static final Material BERYLLIUM_FLUORIDE = MaterialReconstruction.byName("BerylliumFluoride");

    // LiF
    public static final Material LITHIUM_FLUORIDE = MaterialReconstruction.byName("LithiumFluoride");

    // LFTR sub components

    // (NH4)HF2
    public static final Material AMMONIUM_BIFLUORIDE = MaterialReconstruction.byName("AmmoniumBifluoride");

    // Be(OH)2
    public static final Material BERYLLIUM_HYDROXIDE = MaterialReconstruction.byName("BerylliumHydroxide");

    // (NH4)2BeF4
    public static final Material AMMONIUM_TETRAFLUOROBERYLLATE = MaterialReconstruction
        .byName("AmmoniumTetrafluoroberyllate");

    // LFTR Output
    public static final Material NEPTUNIUM_HEXAFLUORIDE = MaterialReconstruction.byName("NeptuniumHexafluoride");

    public static final Material TECHNETIUM_HEXAFLUORIDE = MaterialReconstruction.byName("TechnetiumHexafluoride");

    public static final Material SELENIUM_HEXAFLUORIDE = MaterialReconstruction.byName("SeleniumHexafluoride");

    public static final Material SODIUM_FLUORIDE = MaterialReconstruction.byName("SodiumFluoride");
}
