package gtPlusPlus.core.material.nuclear;

import gregtech.api.enums.TextureSet;
import gregtech.api.util.CustomGlyphs;
import gregtech.api.util.StringUtils;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.state.MaterialState;

public final class MaterialsNuclides {

    public static final Material Li2BeF4 = new Material(
        "Lithium Tetrafluoroberyllate (LFTB)", // Material Name
        MaterialState.LIQUID, // State
        TextureSet.SET_NUCLEAR,
        null, // Material Colour
        566, // Melting Point in C
        870, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "Li2BeF4"), // Chemical Formula
        4, // Radioactivity Level
           // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 2),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 1));

    public static final Material LiFBeF2ThF4UF4 = new Material(
        "LFTR Fuel 3", // Material Name
        MaterialState.LIQUID, // State
        TextureSet.SET_NUCLEAR,
        null, // Material Colour
        566, // Melting Point in C
        870, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFBeF2ThF4UF4"), // Chemical Formula
        5, // Radioactivity Level
           // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 65),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 28),
        new MaterialStack(MaterialsFluorides.THORIUM_TETRAFLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.URANIUM_TETRAFLUORIDE, 1));

    public static final Material LiFBeF2ZrF4UF4 = new Material(
        "LFTR Fuel 2", // Material Name
        MaterialState.LIQUID, // State
        TextureSet.SET_NUCLEAR,
        null, // Material Colour
        650, // Melting Point in C
        940, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFBeF2ZrF4UF4"), // Chemical Formula
        5, // Radioactivity Level
           // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 65),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 28),
        new MaterialStack(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE, 5),
        new MaterialStack(MaterialsFluorides.URANIUM_TETRAFLUORIDE, 2));

    public static final Material LiFBeF2ZrF4U235 = new Material(
        "LFTR Fuel 1", // Material Name
        MaterialState.LIQUID, // State
        TextureSet.SET_NUCLEAR,
        null, // Material Colour
        590, // Melting Point in C
        890, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFBeF2ZrF4") + StringUtils.superscript("235U"), // Chemical
                                                                                                            // Formula
        5, // Radioactivity Level
           // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 55),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 25),
        new MaterialStack(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE, 6),
        new MaterialStack(MaterialsElements.getInstance().URANIUM235, 14));

    // Misc
    public static final Material BurntLftrFuel_MK1 = new Material(
        "Burnt Reactor Fuel I", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiBeF2UF4FP"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().LITHIUM, 1),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.URANIUM_TETRAFLUORIDE, 1),
        new MaterialStack(MaterialsElements.getInstance().PROTACTINIUM, 1));

    public static final Material BurntLftrFuel_MK2 = new Material(
        "Burnt Reactor Fuel II", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiBeF2UF4FP"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsElements.getInstance().LITHIUM, 1),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.URANIUM_TETRAFLUORIDE, 1),
        new MaterialStack(MaterialsElements.getInstance().PROTACTINIUM, 1));

    // LFTR Core Fluids
    public static final Material LiFBeF2UF4FP = new Material(
        "Uranium Depleted Molten Salt (U Salt)", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFBeF2UF4FP"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.URANIUM_TETRAFLUORIDE, 1),
        new MaterialStack(MaterialsElements.getInstance().PROTACTINIUM, 1));

    public static final Material Sparged_LiFBeF2UF4FP = new Material(
        "Helium Sparged U Salt", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFBeF2UF4FP"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.URANIUM_TETRAFLUORIDE, 1),
        new MaterialStack(MaterialsElements.getInstance().PROTACTINIUM, 1));

    public static final Material UF6F2FP = new Material(
        "Phosphorous Uranium Hexafluoride (P-UF6)", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript("UF6F2FP"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.URANIUM_HEXAFLUORIDE, 1),
        new MaterialStack(MaterialsElements.getInstance().FLUORINE, 3),
        new MaterialStack(MaterialsElements.getInstance().PHOSPHORUS, 1));

    public static final Material LiFBeF2 = new Material(
        "Stable Molten Salt Base", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFBeF2"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 1));

    public static final Material LiFBeF2UF4 = new Material(
        "LFTR Fuel Base", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFBeF2UF4"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(LiFBeF2, 1),
        new MaterialStack(MaterialsFluorides.URANIUM_TETRAFLUORIDE, 1));

    // LFTR Blanket Fluids

    // Tier 1 Fuel blanket output
    public static final Material LiFThF4 = new Material(
        "Thorium Depleted Molten Salt (T Salt)", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFThF4"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.THORIUM_TETRAFLUORIDE, 1));

    // Tier 2 Fuel blanket output
    public static final Material LiFBeF2ThF4 = new Material(
        "Thorium-Beryllium Depleted Molten Salt (TB Salt)", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFBeF2ThF4"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.THORIUM_TETRAFLUORIDE, 1));

    // Tier 1 Fuel blanket output
    public static final Material Sparged_LiFThF4 = new Material(
        "Fluorine Sparged T Salt", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFThF4"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.THORIUM_TETRAFLUORIDE, 1));

    // Tier 2 Fuel blanket output
    public static final Material Sparged_LiFBeF2ThF4 = new Material(
        "Fluorine Sparged TB Salt", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript(CustomGlyphs.SUPERSCRIPT7 + "LiFBeF2ThF4"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.LITHIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.BERYLLIUM_FLUORIDE, 1),
        new MaterialStack(MaterialsFluorides.THORIUM_TETRAFLUORIDE, 1));

    public static final Material UF6F2 = new Material(
        "Fluorinated Uranium Hexafluoride (F-UF6)", // Material Name
        MaterialState.PURE_LIQUID, // State
        null, // Material Colour
        -1, // Melting Point in C
        -1, // Boiling Point in C
        -1, // Protons
        -1, // Neutrons
        false, // Uses Blast furnace?
        StringUtils.subscript("UF6F2"), // Chemical Formula
        // Material Stacks with Percentage of required elements.
        new MaterialStack(MaterialsFluorides.URANIUM_HEXAFLUORIDE, 1),
        new MaterialStack(MaterialsElements.getInstance().FLUORINE, 2));

    private static final MaterialsNuclides INSTANCE = new MaterialsNuclides();

    public static MaterialsNuclides getInstance() {
        return INSTANCE;
    }
}
