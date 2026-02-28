package gtPlusPlus.core.handler;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTSplit;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class BookHandler {

    public static Int2ObjectMap<BookTemplate> mBookMap = new Int2ObjectOpenHashMap<>();

    public static BookTemplate book_ThermalBoiler;
    public static BookTemplate book_MultiPowerStation;
    public static BookTemplate book_ModularBauble;
    public static BookTemplate book_NuclearManual;

    public static void run() {

        Logger.INFO("Writing books.");

        // Thermal Boiler
        book_ThermalBoiler = writeBookTemplate(
            0,
            "Manual_Thermal_Boiler",
            "Thermal Boiler Manual",
            "GregoriusT",
            new String[] {
                "This Book explains how to set up and run your Thermal Boiler. We are not responsible for any Damage done by this Book itself nor its content.",
                "First you need to craft the following things for a Thermal Boiler to Function: The Main Boiler Block, 20 Thermal Containment Casings, two Input Hatches, two Output Hatches, a bunch of different Tools and a Maintenance Hatch.",
                "To begin the building, lay out the first 3x3 layer of Machine Casings on the ground (with a Hatch in the Middle), then place the Boiler Block facing outward in the middle of one of the 3m wide Sides.",
                "Now grab 3 other Hatches and place them on the remaining three 3m wide Sides also facing outwards. And now the four corners of the Machine need also a Machine Casing. There should only be a Hole left in the middle of the Cube.",
                "So, now place a 3x3 of Machine Casings on top, at the 3rd Layer with the last Hatch in the middle facing outwards as well.",
                "When accessing the Boiler Block, it should now stop telling you, that the structure is incomplete (bottom Line of that Screen). Now go with a bunch of different Tools (Metal Hammer, Rubber Hammer, Screwdriver, Wrench, Soldering Iron and Crowbar)",
                "to the Maintenance Hatch and access it. After that you grab the 6 Tools and rightclick the Slot with each of them in your Hand in the Maintenance GUI. Note that you need Soldering Tin/Lead in your Inventory to use the Soldering Iron.",
                "The Main Block should now tell you that you need to use the Rubber Hammer on it to (re)activate the Machine. The Rubber Hammer can enable and disable Machines. The Machine disables itself after something important broke.",
                "If you want to use Lava with this Device, then you should add a Lava Filter to extract additional Resources from the Lava. If the Filter breaks, the Machine won't explode like a Turbine would. If you use molten Salt, then you won't need a Filter.",
                "You will get Obsidian when processing Lava, however if a Filter is used, you will get sometimes an Ingot instead of a Block of Obsidian. When using molten Salt, you will get the Salt back.",
                "So, now for the Maintenance. After a few Hours of running nonstop, your Boiler will get small Problems, which don't prevent it from running, these Problems just decrease Efficiency. Every Problem listed on the Screen does -10% Efficiency.",
                "To fix these Problems, just go to the Maintenance Hatch and click with the problem corresponding Tool on the Slot to repair. If all six possible runtime Problems happen, the Machine will auto-shutdown no matter what. No Explosion, it's just stopping.",
                "The Thermal Boiler will produce 800 Liters of Steam per tick for about 5 or 6 Liters of Water per tick at reaching 100% Efficiency. In case of Lava it consumes 1666 Liters every Second.",
                "A Thermal Boiler is worth about 33 small Thermal Generators, and as the Boilers get much less Efficient, when not having enough Fuel, you should consider making a large Nether Pump for Lava, or a good Nuclear Reactor for molten Salt.",
                "Input and Output Slots are fully optional, you can place multiple ones of them or even none on the Machine. A Machine without Input couldn't process any Recipes, while a Machine without Output just voids all outputted Items and Liquids.",
                "It might be useful to use the Screwdriver on the Output Hatches to determine what is outputted where." });

        // Test Novel
        book_MultiPowerStation = writeBookTemplate(
            1,
            "Manual_Multi_PowerStation",
            "Power Storage & You [Version 0.64]",
            "Alkalus",
            new String[] {
                // Page 1
                """
                    So, when it comes to power storage you really have three separate options:
                    \s
                    Battery Buffers,
                    Energy Buffers,
                    The Power Sub-Station
                    """,
                // Page 2
                """
                    Battery Buffer
                    \s
                    Is rather portable. Allowing you to throw set one up and insert batteries where ever you may need.
                    They output 1A for each battery stored inside, up to a maximum of 16A.""",
                // Page 3
                """
                    Energy Buffer
                    \s
                    Is a more optimal choice for storage in your base. Once placed down, they cannot be moved without losing all stored power.
                    Energy Buffers can output 4A from the output side, however accept 16A as input.""",
                // Page 4
                """
                    The Power Sub-Station\s
                    Is used for storing Insane amounts of power later game.
                    Consumes 2% of the average voltage of all energy type hatches every tick.
                    """,
                // Page 5
                """
                    Allows Insertion/Removal of power from the rear face of the controller, swap with a screwdriver.
                    Variable Height Structure, between 4-16Y. Inserted Redox Cells dictate max energy tier of structure.
                    """,
                // Page 6
                """
                    Redox Cells cannot be placed into the Top or Bottom layer and only take up 3xhx3 internally.
                    Different Tier cells CANNOT be mixed together.
                    """,
                // Page 7
                """
                    All Hatches Must be HV at a Minimum, this minimum tier is in place to stop people abusing ULV/LV hatches to lower the avg/t.
                    Currently the GUI will NOT display anything at all until the structure forms, this is a known bug.
                    """,
                // Page 8
                """
                    Valid Hatches:
                    Energy Hatch,
                    Dynamo Hatch,
                    Charging Bus,
                    Discharging Bus,
                    Dynamo Buffer,
                    Multi-Amp Dynamo Hatch.


                    Structure MUST contain at least one energy input and one energy output hatch.""" });

        // Test Novel
        book_ModularBauble = writeBookTemplate(
            2,
            "Manual_Modular_Bauble",
            "How to: Modular Baubles",
            "Alkalus",
            new String[] {
                """
                    Concept: This idea came from wanting flexibility.\s
                    First step, Build a Modularity table to begin customisation of your Bauble.\s
                     After this has been constructed, you can now combine the upgrades listed within this book to improve the baubles level/100.""",
                """
                    Defence:
                    Can be upgraded by combining metal plates with the bauble.\s
                     | +1 | Aluminium\s
                     | +2 | Stainless Steel\s
                     | +3 | Tungsten\s
                     | +4 | Tungsten Steel\s
                     | +5 | Naquadah\s
                    """, "There was once a sad and lonely oak tree. \n", "There was once a sad and lonely oak tree. \n",
                "There was once a sad and lonely oak tree. \n" });

        book_NuclearManual = writeBookTemplate(
            4,
            "Manual_NuclearStuff_1",
            "Nuclear Chemistry [FFPP]",
            "Alkalus",
            new String[] {
                // Page 1
                """
                    Fission Fuel Processing Plant
                    Size: 3x9x3 [LxHxW]
                    Controller: Center, Bottom
                    4x Input Hatch
                    2x Output Hatch
                    1x Output Bus
                    1x ZPM+ Muffler
                    1x Maintenance Hatch
                    1x Energy Hatch
                    """,
                // Page 2
                """
                    [1] 7x Hastelloy-X or I/O
                    [2] 5x Incoloy-DS Fluid Containment
                    [3] 4x Reactor Shield Casing
                    [4] 17x Hastelloy-N Sealant Case
                    Multiblock Construction
                    Convention is [LxHxW]

                    """,
                // Page 3
                """
                    Layer 1/2:
                    [1][1][1]
                    [1][1][1]
                    [1][1][1]

                    Layer 3/5/6
                    [ ][4][ ]
                    [4][2][4]
                    [ ][4][ ]

                    """,
                // Page 4
                """
                    Layer 4
                    [ ][3][ ]
                    [3][2][3]
                    [ ][3][ ]

                    Layer 7/8/9
                    [ ][ ][ ]
                    [ ][3][ ]
                    [ ][ ][ ]
                    """,
                // Page 5
                """
                    Fission Fuel
                    Processing Plant----------------------
                    This structure is used to produce the Molten Salts required to run a Liquid Fluorine Thorium Reactor [LFTR].""" });
    }

    public static void runLater() {
        // Multiblock Manuals

        // Thermal Boiler
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(ModItems.itemCustomBook, 1, 0),
            new ItemStack[] { new ItemStack(Items.writable_book), new ItemStack(Items.lava_bucket) });

        // Power Substation
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(ModItems.itemCustomBook, 1, 1),
            new ItemStack[] { new ItemStack(Items.writable_book),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tin, 1) });

        // Nuclear Manual
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(ModItems.itemCustomBook, 1, 4),
            new ItemStack[] { new ItemStack(Items.writable_book),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1) });

        for (int meta : mBookMap.keySet()) {
            ItemStack bookstack = new ItemStack(ModItems.itemCustomBook, 1, meta);
            GTOreDictUnificator.registerOre("bookWritten", bookstack);
            GTOreDictUnificator.registerOre("craftingBook", bookstack);
        }
    }

    public static BookTemplate writeBookTemplate(int meta, String aMapping, String aTitle, String aAuthor,
        String[] aPages) {
        addBookTitleLocalization(aTitle);
        addBookPagesLocalization(aTitle, aPages);
        BookTemplate mTemp = new BookTemplate(meta, aMapping, aTitle, aAuthor, aPages);
        mBookMap.put(meta, mTemp);
        return mTemp;
    }

    @Desugar
    public record BookTemplate(int mMeta, String mMapping, String mTitle, String mAuthor, String[] mPages) {}

    public static void addBookTitleLocalization(final String aTitle) {
        GTLanguageManager.addStringLocalization("Book." + aTitle + ".Name", aTitle);
    }

    public static void addBookPagesLocalization(final String aTitle, final String[] aPages) {
        for (byte i = 0; i < aPages.length; i = (byte) (i + 1)) {
            GTLanguageManager.addStringLocalization(
                "Book." + aTitle + ".Page" + ((i < 10) ? "0" + i : Byte.valueOf(i)),
                aPages[i].replace("\n", GTSplit.LB));
        }
    }
}
