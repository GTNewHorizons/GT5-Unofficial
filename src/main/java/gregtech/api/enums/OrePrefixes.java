package gregtech.api.enums;

import static gregtech.api.enums.GT_Values.*;

import java.util.*;

import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableList;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.interfaces.ICondition;
import gregtech.api.interfaces.IMaterialHandler;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.GT_ArrayList;
import gregtech.api.objects.GT_HashSet;
import gregtech.api.objects.GT_ItemStack2;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gregtech.loaders.materialprocessing.ProcessingModSupport;

public enum OrePrefixes {

    @Deprecated
    pulp("Pulps", "", "", false, false, false, false, false, false, false, false, false, false,
        B[0] | B[1] | B[2] | B[3], -1, 64, -1),
    @Deprecated
    leaves("Leaves", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    @Deprecated
    sapling("Saplings", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    @Deprecated
    itemDust("Dusts", "", "", false, false, false, false, false, false, false, false, false, false,
        B[0] | B[1] | B[2] | B[3], -1, 64, -1),
    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    oreBlackgranite("Black Granite Ores", "Granite ", " Ore", true, true, false, false, false, true, false, false,
        false, true, B[3], -1, 64, -1),
    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    oreRedgranite("Red Granite Ores", "Granite ", " Ore", true, true, false, false, false, true, false, false, false,
        true, B[3], -1, 64, -1),
    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    oreMarble("Marble Ores", "Marble ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3],
        -1, 64, -1),
    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    oreBasalt("Basalt Ores", "Basalt ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3],
        -1, 64, -1),
    /** Prefix of the Nether-Ores Mod. Causes Ores to double. Ore -> Material is a Oneway Operation! */
    oreNetherrack("Netherrack Ores", "Nether ", " Ore", true, true, false, false, false, true, false, false, false,
        true, B[3], -1, 64, -1),
    /** Prefix of the Nether-Ores Mod. Causes Ores to double. Ore -> Material is a Oneway Operation! */
    oreNether("Nether Ores", "Nether ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3],
        -1, 64, -1),
    @Deprecated
    denseore("Dense Ores", "", "", false, false, false, false, false, true, false, false, false, true, B[3], -1, 64,
        -1),
    /** Prefix of the Dense-Ores Mod. Causes Ores to double. Ore -> Material is a Oneway Operation! */
    oreDense("Dense Ores", "Dense ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1,
        64, -1),
    /** Prefix of TFC */
    oreRich("Rich Ores", "Rich ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1,
        64, -1),
    /** Prefix of TFC */
    oreNormal("Normal Ores", "Normal ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3],
        -1, 64, -1),
    /** Prefix of Railcraft. */
    oreSmall("Small Ores", "Small ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1,
        64, 67),
    /** Prefix of Railcraft. */
    orePoor("Poor Ores", "Poor ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1,
        64, -1),
    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    oreEndstone("Endstone Ores", "End ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3],
        -1, 64, -1),
    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    oreEnd("End Ores", "End ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64,
        -1),
    @Deprecated
    oreGem("Ores", "", "", false, false, false, false, false, true, false, false, false, true, B[3], -1, 64, -1),
    /** Regular Ore Prefix. Ore -> Material is a Oneway Operation! Introduced by Eloraam */
    ore("Ores", "", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, 68),
    crushedCentrifuged("Centrifuged Ores", "Centrifuged ", " Ore", true, true, false, false, false, false, false, true,
        false, true, B[3], -1, 64, 7),
    crushedPurified("Purified Ores", "Purified ", " Ore", true, true, false, false, false, false, false, true, false,
        true, B[3], -1, 64, 6),
    crushed("Crushed Ores", "Crushed ", " Ore", true, true, false, false, false, false, false, true, false, true, B[3],
        -1, 64, 5),
    /** Introduced by Mekanism */
    shard("Crystallised Shards", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1,
        64, -1),
    clump("Clumps", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, -1),
    reduced("Reduced Gravels", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64,
        -1),
    crystalline("Crystallised Metals", "", "", true, true, false, false, false, false, false, false, false, true, B[3],
        -1, 64, -1),
    cleanGravel("Clean Gravels", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1,
        64, -1),
    dirtyGravel("Dirty Gravels", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1,
        64, -1),
    /** A quintuple Ingot. */
    ingotQuintuple("5x Ingots", "Quintuple ", " Ingot", true, true, false, false, false, false, true, true, false,
        false, B[1], M * 5, 64, 16),
    /** A quadruple Ingot. */
    ingotQuadruple("4x Ingots", "Quadruple ", " Ingot", true, true, false, false, false, false, true, true, false,
        false, B[1], M * 4, 64, 15),
    @Deprecated
    ingotQuad("4x Ingots", "Quadruple ", " Ingot", false, false, false, false, false, false, false, false, false, false,
        B[1], -1, 64, 15),
    /** A triple Ingot. */
    ingotTriple("3x Ingots", "Triple ", " Ingot", true, true, false, false, false, false, true, false, false, false,
        B[1], M * 3, 64, 14),
    /** A double Ingot. Introduced by TerraFirmaCraft */
    ingotDouble("2x Ingots", "Double ", " Ingot", true, true, false, false, false, false, true, true, false, false,
        B[1], M * 2, 64, 13),
    /** A hot Ingot, which has to be cooled down by a Vacuum Freezer. */
    ingotHot("Hot Ingots", "Hot ", " Ingot", true, true, false, false, false, false, false, true, false, false, B[1],
        M * 1, 64, 12),
    /** A regular Ingot. Introduced by Eloraam */
    ingot("Ingots", "", " Ingot", true, true, false, false, false, false, false, true, false, false, B[1], M * 1, 64,
        11),
    /** A regular Gem worth one small Dust. Introduced by TerraFirmaCraft */
    gemChipped("Chipped Gemstones", "Chipped ", "", true, true, true, false, false, false, true, true, false, false,
        B[2], M / 4, 64, 59),
    /** A regular Gem worth two small Dusts. Introduced by TerraFirmaCraft */
    gemFlawed("Flawed Gemstones", "Flawed ", "", true, true, true, false, false, false, true, true, false, false, B[2],
        M / 2, 64, 60),
    /** A regular Gem worth two Dusts. Introduced by TerraFirmaCraft */
    gemFlawless("Flawless Gemstones", "Flawless ", "", true, true, true, false, false, false, true, true, false, false,
        B[2], M * 2, 64, 61),
    /** A regular Gem worth four Dusts. Introduced by TerraFirmaCraft */
    gemExquisite("Exquisite Gemstones", "Exquisite ", "", true, true, true, false, false, false, true, true, false,
        false, B[2], M * 4, 64, 62),
    /** A regular Gem worth one Dust. Introduced by Eloraam */
    gem("Gemstones", "", "", true, true, true, false, false, false, true, true, false, false, B[2], M * 1, 64, 8),
    @Deprecated
    dustDirty("Impure Dusts", "", "", false, false, false, false, false, false, false, false, false, true, B[3], -1, 64,
        3),
    /** 1/9th of a Dust. */
    dustTiny("Tiny Dusts", "Tiny Pile of ", " Dust", true, true, false, false, false, false, false, true, false, false,
        B[0] | B[1] | B[2] | B[3], M / 9, 64, 0),
    /** 1/4th of a Dust. */
    dustSmall("Small Dusts", "Small Pile of ", " Dust", true, true, false, false, false, false, false, true, false,
        false, B[0] | B[1] | B[2] | B[3], M / 4, 64, 1),
    /** Dust with impurities. 1 Unit of Main Material and 1/9 - 1/4 Unit of secondary Material */
    dustImpure("Impure Dusts", "Impure Pile of ", " Dust", true, true, false, false, false, false, false, true, false,
        true, B[3], M * 1, 64, 3),
    dustRefined("Refined Dusts", "Refined Pile of ", " Dust", true, true, false, false, false, false, false, true,
        false, true, B[3], M * 1, 64, 2),
    dustPure("Purified Dusts", "Purified Pile of ", " Dust", true, true, false, false, false, false, false, true, false,
        true, B[3], M * 1, 64, 4),
    /** Pure Dust worth of one Ingot or Gem. Introduced by Alblaka. */
    dust("Dusts", "", " Dust", true, true, false, false, false, false, false, true, false, false,
        B[0] | B[1] | B[2] | B[3], M * 1, 64, 2),
    /** A Nugget. Introduced by Eloraam */
    nugget("Nuggets", "", " Nugget", true, true, false, false, false, false, false, true, false, false, B[1], M / 9, 64,
        9),
    /** Special Alloys have this prefix. */
    plateAlloy("Alloy Plates", "", "", true, false, false, false, false, false, false, false, false, false, B[1], -1,
        64, 17),
    plateSteamcraft("Steamcraft Plates", "", "", false, false, false, false, false, false, false, false, false, false,
        B[1], -1, 64, 17),
    /** 9 Plates combined in one Item. */
    plateDense("Dense Plates", "Dense ", " Plate", true, true, false, false, false, false, true, true, false, false,
        B[1], M * 9, 64, 22),
    plateQuintuple("5x Plates", "Quintuple ", " Plate", true, true, false, false, false, false, true, true, false,
        false, B[1], M * 5, 64, 21),
    plateQuadruple("4x Plates", "Quadruple ", " Plate", true, true, false, false, false, false, true, true, false,
        false, B[1], M * 4, 64, 20),
    @Deprecated
    plateQuad("4x Plates", "", "", false, false, false, false, false, false, false, false, false, false, B[1], -1, 64,
        20),
    plateTriple("3x Plates", "Triple ", " Plate", true, true, false, false, false, false, true, true, false, false,
        B[1], M * 3, 64, 19),
    plateDouble("2x Plates", "Double ", " Plate", true, true, false, false, false, false, true, true, false, false,
        B[1], M * 2, 64, 18),
    /** Regular Plate made of one Ingot/Dust. Introduced by Calclavia */
    plate("Plates", "", " Plate", true, true, false, false, false, false, true, true, false, false, B[1] | B[2], M * 1,
        64, 17),
    /** Casing made of 1/2 Ingot/Dust */
    itemCasing("Casings", "", " Casing", true, true, false, false, false, false, true, true, false, false, B[1] | B[2],
        M / 2, 64, 10),
    /** Foil made of 1/4 Ingot/Dust. */
    foil("Foils", "", " Foil", true, true, false, false, false, false, true, true, false, false, B[1], M / 4, 64, 29),
    /** Stick made of an Ingot. */
    stickLong("Long Sticks/Rods", "Long ", " Rod", true, true, false, false, false, false, true, true, false, false,
        B[1] | B[2], M * 1, 64, 54),
    /** Stick made of half an Ingot. Introduced by Eloraam */
    stick("Sticks/Rods", "", " Rod", true, true, false, false, false, false, true, true, false, false, B[1] | B[2],
        M / 2, 64, 23),
    /** consisting out of one Nugget. */
    round("Rounds", "", " Round", true, true, false, false, false, false, true, true, false, false, B[1], M / 9, 64,
        25),
    /** consisting out of 1/8 Ingot or 1/4 Stick. */
    bolt("Bolts", "", " Bolt", true, true, false, false, false, false, true, true, false, false, B[1] | B[2], M / 8, 64,
        26),
    /** contain dusts */
    comb("Combs", "", " Comb", false, false, false, false, false, false, false, true, false, false, B[1] | B[2], M, 64,
        101),
    /** consisting out of a Bolt. */
    screw("Screws", "", " Screw", true, true, false, false, false, false, true, true, false, false, B[1] | B[2], M / 9,
        64, 27),
    /** consisting out of 1/2 Stick. */
    ring("Rings", "", " Ring", true, true, false, false, false, false, true, true, false, false, B[1], M / 4, 64, 28),
    /** consisting out of 1 Fine Wire. */
    springSmall("Small Springs", "Small ", " Spring", true, true, false, false, false, false, true, true, false, false,
        B[1], M / 4, 64, 55),
    /** consisting out of 2 Sticks. */
    spring("Springs", "", " Spring", true, true, false, false, false, false, true, true, false, false, B[1], M * 1, 64,
        56),
    /** consisting out of 1/8 Ingot or 1/4 Wire. */
    wireFine("Fine Wires", "Fine ", " Wire", true, true, false, false, false, false, true, true, false, false, B[1],
        M / 8, 64, 51),
    /** consisting out of 4 Plates, 1 Ring and 1 Screw. */
    rotor("Rotors", "", " Rotor", true, true, false, false, false, false, true, true, false, false, B[7], M * 4 + M / 4,
        64, 53),
    gearGtSmall("Small Gears", "Small ", " Gear", true, true, false, false, false, false, true, true, false, false,
        B[7], M * 1, 64, 52),
    /** Introduced by me because BuildCraft has ruined the gear Prefix... */
    gearGt("Gears", "", " Gear", true, true, false, false, false, false, true, true, false, false, B[7], M * 4, 16, 63),
    /** 3/4 of a Plate or Gem used to shape a Lense. Normally only used on Transparent Materials. */
    lens("Lenses", "", " Lens", true, true, false, false, false, false, true, true, false, false, B[2], (M * 3) / 4, 64,
        24),
    /** consisting out of 16 Dusts. */
    crateGtDust("Crates of Dust", "Crate of ", " Dust", true, true, false, true, false, false, false, true, false,
        false, B[0] | B[1] | B[2] | B[3], -1, 64, 96),
    /** consisting out of 16 Plates. */
    crateGtPlate("Crates of Plates", "Crate of ", " Plate", true, true, false, true, false, false, false, true, false,
        false, B[1] | B[2], -1, 64, 99),
    /** consisting out of 16 Ingots. */
    crateGtIngot("Crates of Ingots", "Crate of ", " Ingot", true, true, false, true, false, false, false, true, false,
        false, B[1], -1, 64, 97),
    /** consisting out of 16 Gems. */
    crateGtGem("Crates of Gems", "Crate of ", " Gem", true, true, false, true, false, false, false, true, false, false,
        B[2], -1, 64, 98),
    /** Hot Cell full of Plasma, which can be used in the Plasma Generator. */
    cellPlasma("Cells of Plasma", "", " Plasma Cell", true, true, true, true, false, false, false, true, false, false,
        B[5], M * 1, 64, 31),
    /** Hot Cell full of molten stuff, which can be used in the Plasma Generator. */
    cellMolten("Cells of Molten stuff", "Molten ", " Cell", true, true, true, true, false, false, false, true, false,
        false, 0, M * 1, 64, 31),
    /** Regular Gas/Fluid Cell. Introduced by Calclavia */
    cell("Cells", "", " Cell", true, true, true, true, false, false, true, true, false, false, B[4] | B[8], M * 1, 64,
        30),
    /** A vanilla Iron Bucket filled with the Material. */
    bucket("Buckets", "", " Bucket", true, true, true, true, false, false, true, false, false, false, B[4] | B[8],
        M * 1, 64, -1),
    /** An Iguana Tweaks Clay Bucket filled with the Material. */
    bucketClay("Clay Buckets", "", " Clay Bucket", true, true, true, true, false, false, true, false, false, false,
        B[4] | B[8], M * 1, 64, -1),
    /** Glass Bottle containing a Fluid. */
    bottle("Bottles", "", " Bottle", true, true, true, true, false, false, false, false, false, false, B[4] | B[8], -1,
        64, -1),
    capsule("Capsules", "", " Capsule", false, true, true, true, false, false, false, false, false, false, B[4] | B[8],
        M * 1, 64, -1),
    crystal("Crystals", "", " Crystal", false, true, false, false, false, false, true, false, false, false, B[2], M * 1,
        64, -1),
    bulletGtSmall("Small Bullets", "Small ", " Bullet", true, true, false, false, true, false, true, false, true, false,
        B[6] | B[8], M / 9, 64, -1),
    bulletGtMedium("Medium Bullets", "Medium ", " Bullet", true, true, false, false, true, false, true, false, true,
        false, B[6] | B[8], M / 6, 64, -1),
    bulletGtLarge("Large Bullets", "Large ", " Bullet", true, true, false, false, true, false, true, false, true, false,
        B[6] | B[8], M / 3, 64, -1),
    /** Arrow made of 1/4 Ingot/Dust + Wooden Stick. */
    arrowGtWood("Regular Arrows", "", " Arrow", true, true, false, false, true, false, true, false, true, false, B[6],
        M / 4, 64, 57),
    /** Arrow made of 1/4 Ingot/Dust + Plastic Stick. */
    arrowGtPlastic("Light Arrows", "Light ", " Arrow", true, true, false, false, true, false, true, false, true, false,
        B[6], M / 4, 64, 58),
    arrow("Arrows", "", "", false, false, true, false, false, false, false, false, true, false, B[6], -1, 64, 57),
    /** consisting out of 1/4 Ingot. */
    toolHeadArrow("Arrow Heads", "", " Arrow Head", true, true, false, false, false, false, true, true, false, false,
        B[6], M / 4, 64, 46),
    /** consisting out of 2 Ingots. */
    toolHeadSword("Sword Blades", "", " Sword Blade", true, true, false, false, false, false, true, true, false, false,
        B[6], M * 2, 64, 32),
    /** consisting out of 3 Ingots. */
    toolHeadPickaxe("Pickaxe Heads", "", " Pickaxe Head", true, true, false, false, false, false, true, true, false,
        false, B[6], M * 3, 64, 33),
    /** consisting out of 1 Ingots. */
    toolHeadShovel("Shovel Heads", "", " Shovel Head", true, true, false, false, false, false, true, true, false, false,
        B[6], M * 1, 64, 34),
    /** consisting out of 1 Ingots. */
    toolHeadUniversalSpade("Universal Spade Heads", "", " Universal Spade Head", true, true, false, false, false, false,
        true, true, false, false, B[6], M * 1, 64, 43),
    /** consisting out of 3 Ingots. */
    toolHeadAxe("Axe Heads", "", " Axe Head", true, true, false, false, false, false, true, true, false, false, B[6],
        M * 3, 64, 35),
    /** consisting out of 2 Ingots. */
    toolHeadHoe("Hoe Heads", "", " Hoe Head", true, true, false, false, false, false, true, true, false, false, B[6],
        M * 2, 64, 36),
    /** consisting out of 3 Ingots. */
    toolHeadSense("Sense Blades", "", " Sense Blade", true, true, false, false, false, false, true, true, false, false,
        B[6], M * 3, 64, 44),
    /** consisting out of 2 Ingots. */
    toolHeadFile("File Heads", "", " File Head", true, true, false, false, false, false, true, true, false, false, B[6],
        M * 2, 64, 38),
    /** consisting out of 6 Ingots. */
    toolHeadHammer("Hammer Heads", "", " Hammer Head", true, true, false, false, false, false, true, true, false, false,
        B[6], M * 6, 64, 37),
    /** consisting out of 4 Ingots. */
    toolHeadPlow("Plow Heads", "", " Plow Head", true, true, false, false, false, false, true, true, false, false, B[6],
        M * 4, 64, 45),
    /** consisting out of 2 Ingots. */
    toolHeadSaw("Saw Blades", "", " Saw Blade", true, true, false, false, false, false, true, true, false, false, B[6],
        M * 2, 64, 39),
    /** consisting out of 4 Ingots. */
    toolHeadBuzzSaw("Buzzsaw Blades", "", " Buzzsaw Blade", true, true, false, false, false, false, true, true, false,
        false, B[6], M * 4, 64, 48),
    /** consisting out of 1 Ingots. */
    toolHeadScrewdriver("Screwdriver Tips", "", " Screwdriver Tip", true, true, false, false, false, false, true, false,
        false, false, B[6], M * 1, 64, 47),
    /** consisting out of 4 Ingots. */
    toolHeadDrill("Drill Tips", "", " Drill Tip", true, true, false, false, false, false, true, true, false, false,
        B[6], M * 4, 64, 40),
    /** consisting out of 2 Ingots. */
    toolHeadChainsaw("Chainsaw Tips", "", " Chainsaw Tip", true, true, false, false, false, false, true, true, false,
        false, B[6], M * 2, 64, 41),
    /** consisting out of 4 Ingots. */
    toolHeadWrench("Wrench Tips", "", " Wrench Tip", true, true, false, false, false, false, true, true, false, false,
        B[6], M * 4, 64, 42),
    /** consisting out of 6 Ingots. */
    turbineBlade("Turbine Blades", "", " Turbine Blade", true, true, false, false, false, false, true, true, false,
        false, B[6], M * 6, 64, 100),
    /** vanilly Sword */
    toolSword("Swords", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 2, 1, -1),
    /** vanilly Pickaxe */
    toolPickaxe("Pickaxes", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 3, 1,
        -1),
    /** vanilly Shovel */
    toolShovel("Shovels", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 1, 1,
        -1),
    /** vanilly Axe */
    toolAxe("Axes", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 3, 1, -1),
    /** vanilly Hoe */
    toolHoe("Hoes", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 2, 1, -1),
    /** vanilly Shears */
    toolShears("Shears", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 2, 1, -1),
    /**
     * toolPot, toolSkillet, toolSaucepan, toolBakeware, toolCuttingboard, toolMortarandpestle, toolMixingbowl,
     * toolJuicer
     */
    tool("Tools", "", "", false, false, false, false, false, false, false, false, true, false, B[6], -1, 1, -1),
    compressedCobblestone("9^X Compressed Cobblestones", "", "", false, false, false, false, false, false, false, false,
        false, false, 0, -1, 64, -1),
    compressedStone("9^X Compressed Stones", "", "", false, false, false, false, false, false, false, false, false,
        false, 0, -1, 64, -1),
    compressedDirt("9^X Compressed Dirt", "", "", false, false, false, false, false, false, false, false, false, false,
        0, -1, 64, -1),
    compressedGravel("9^X Compressed Gravel", "", "", false, false, false, false, false, false, false, false, false,
        false, 0, -1, 64, -1),
    compressedSand("9^X Compressed Sand", "", "", false, false, false, false, false, false, false, false, false, false,
        0, -1, 64, -1),
    /** Compressed Material, worth 1 Unit. Introduced by Galacticraft */
    compressed("Compressed Materials", "Compressed ", "", true, true, false, false, false, false, true, false, false,
        false, 0, M * 3, 64, -1),
    glass("Glasses", "", "", false, false, true, false, true, false, false, false, false, false, 0, -1, 64, -1),
    paneGlass("Glass Panes", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1),
    blockGlass("Glass Blocks", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64,
        -1),
    blockWool("Wool Blocks", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1),
    /** IGNORE */
    block_("Random Blocks", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64, -1),
    /** Storage Block consisting out of 9 Ingots/Gems/Dusts. Introduced by CovertJaguar */
    block("Storage Blocks", "Block of ", "", true, true, false, false, false, true, true, false, false, false, 0, M * 9,
        64, 71),
    /** Special Prefix used mainly for the Crafting Handler. */
    craftingTool("Crafting Tools", "", "", false, false, false, false, false, false, false, false, true, false, 0, -1,
        64, -1),
    /** Special Prefix used mainly for the Crafting Handler. */
    crafting("Crafting Ingredients", "", "", false, false, false, false, false, false, false, false, false, false, 0,
        -1, 64, -1),
    /** Special Prefix used mainly for the Crafting Handler. */
    craft("Crafting Stuff?", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64,
        -1),
    /** Prefix used for Logs. Usually as "logWood". Introduced by Eloraam */
    log("Logs", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64, -1),
    /** Prefix used for Slabs. Usually as "slabWood" or "slabStone". Introduced by SirSengir */
    slab("Slabs", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64, -1),
    /** Prefix used for Stairs. Usually as "stairWood" or "stairStone". Introduced by SirSengir */
    stair("Stairs", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64, -1),
    /** Prefix used for Fences. Usually as "fenceWood". Introduced by Forge */
    fence("Fences", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Prefix for Planks. Usually "plankWood". Introduced by Eloraam */
    plank("Planks", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64, -1),
    /** Prefix for Saplings. */
    treeSapling("Saplings", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1),
    /** Prefix for Leaves. */
    treeLeaves("Leaves", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1),
    /** Prefix for Tree Parts. */
    tree("Tree Parts", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Cobblestone Prefix for all Cobblestones. */
    stoneCobble("Cobblestones", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64,
        -1),
    /** Smoothstone Prefix. */
    stoneSmooth("Smoothstones", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64,
        -1),
    /** Mossy Stone Bricks. */
    stoneMossyBricks("mossy Stone Bricks", "", "", false, false, true, false, false, true, false, false, false, false,
        0, -1, 64, -1),
    /** Mossy Cobble. */
    stoneMossy("Mossy Stones", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64,
        -1),
    @Deprecated
    stoneBricksMossy("Mossy Stone Bricks", "", "", false, false, false, false, false, true, false, false, false, false,
        0, -1, 64, -1),
    /** Stone Bricks. */
    stoneBricks("Stone Bricks", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64,
        -1),
    @Deprecated
    stoneBrick("Stone Bricks", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64,
        -1),
    /** Cracked Bricks. */
    stoneCracked("Cracked Stones", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1,
        64, -1),
    /** Chiseled Stone. */
    stoneChiseled("Chiseled Stones", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1,
        64, -1),
    /** Prefix to determine which kind of Rock this is. */
    stone("Stones", "", "", false, true, true, false, true, true, false, false, false, false, 0, -1, 64, -1),
    cobblestone("Cobblestones", "", "", false, true, true, false, false, true, false, false, false, false, 0, -1, 64,
        -1),
    /** Prefix to determine which kind of Rock this is. */
    rock("Rocks", "", "", false, true, true, false, true, true, false, false, false, false, 0, -1, 64, -1),
    record("Records", "", "", false, false, true, false, false, false, false, false, false, false, 0, -1, 1, -1),
    rubble("Rubbles", "", "", true, true, true, false, false, false, false, false, false, false, 0, -1, 64, -1),
    scraps("Scraps", "", "", true, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    scrap("Scraps", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** IGNORE */
    item_("Items", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Random Item. Introduced by Alblaka */
    item("Items", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Used for Books of any kind. */
    book("Books", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Used for Papers of any kind. */
    paper("Papers", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Used for the 16 dyes. Introduced by Eloraam */
    dye("Dyes", "", "", false, false, true, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Used for the 16 colors of Stained Clay. Introduced by Forge */
    stainedClay("Stained Clays", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64,
        -1),
    /** vanilly Helmet */
    armorHelmet("Helmets", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 5, 1,
        -1),
    /** vanilly Chestplate */
    armorChestplate("Chestplates", "", "", false, true, false, false, false, false, true, false, true, false, B[6],
        M * 8, 1, -1),
    /** vanilly Pants */
    armorLeggings("Leggings", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 7, 1,
        -1),
    /** vanilly Boots */
    armorBoots("Boots", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 4, 1, -1),
    armor("Armor Parts", "", "", false, false, false, false, false, false, false, false, true, false, B[6], -1, 1, -1),
    frameGt("Frame Boxes", "", "", true, true, false, false, true, false, true, false, false, false, 0, M * 2, 64, 83),
    pipeTiny("Tiny Pipes", "Tiny ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M / 2,
        64, 78),
    pipeSmall("Small Pipes", "Small ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0,
        M * 1, 64, 79),
    pipeMedium("Medium Pipes", "Medium ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0,
        M * 3, 64, 80),
    pipeLarge("Large pipes", "Large ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0,
        M * 6, 64, 81),
    pipeHuge("Huge Pipes", "Huge ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0,
        M * 12, 64, 82),
    pipeQuadruple("Quadruple Pipes", "Quadruple ", " Pipe", true, true, false, false, true, false, true, false, false,
        false, 0, M * 12, 64, 84),
    pipeNonuple("Nonuple Pipes", "Nonuple ", " Pipe", true, true, false, false, true, false, true, false, false, false,
        0, M * 9, 64, 85),
    pipeRestrictiveTiny("Tiny Restrictive Pipes", "Tiny Restrictive ", " Pipe", true, true, false, false, true, false,
        true, false, false, false, 0, M / 2, 64, 78),
    pipeRestrictiveSmall("Small Restrictive Pipes", "Small Restrictive ", " Pipe", true, true, false, false, true,
        false, true, false, false, false, 0, M * 1, 64, 79),
    pipeRestrictiveMedium("Medium Restrictive Pipes", "Medium Restrictive ", " Pipe", true, true, false, false, true,
        false, true, false, false, false, 0, M * 3, 64, 80),
    pipeRestrictiveLarge("Large Restrictive Pipes", "Large Restrictive ", " Pipe", true, true, false, false, true,
        false, true, false, false, false, 0, M * 6, 64, 81),
    pipeRestrictiveHuge("Huge Restrictive Pipes", "Huge Restrictive ", " Pipe", true, true, false, false, true, false,
        true, false, false, false, 0, M * 12, 64, 82),
    pipe("Pipes", "", " Pipe", true, false, false, false, false, false, false, false, false, false, 0, -1, 64, 77),
    wireGt16("16x Wires", "16x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M * 8,
        64, -1),
    wireGt12("12x Wires", "12x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M * 6,
        64, -1),
    wireGt08("8x Wires", "8x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M * 4,
        64, -1),
    wireGt04("4x Wires", "4x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M * 2,
        64, -1),
    wireGt02("2x Wires", "2x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M * 1,
        64, -1),
    wireGt01("1x Wires", "1x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M / 2,
        64, -1),
    cableGt16("16x Cables", "16x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0,
        M * 8, 64, -1),
    cableGt12("12x Cables", "12x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0,
        M * 6, 64, -1),
    cableGt08("8x Cables", "8x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0, M * 4,
        64, -1),
    cableGt04("4x Cables", "4x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0, M * 2,
        64, -1),
    cableGt02("2x Cables", "2x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0, M * 1,
        64, -1),
    cableGt01("1x Cables", "1x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0, M / 2,
        64, -1),

    /*
     * Electric Components. usual Materials for this are: Primitive (Tier 1) Basic (Tier 2) as used by UE as well : IC2
     * Circuit and RE-Battery Good (Tier 3) Advanced (Tier 4) as used by UE as well : Advanced Circuit, Advanced Battery
     * and Lithium Battery Data (Tier 5) : Data Storage Circuit Elite (Tier 6) as used by UE as well : Energy Crystal
     * and Data Control Circuit Master (Tier 7) : Energy Flow Circuit and Lapotron Crystal Ultimate (Tier 8) : Data Orb
     * and Lapotronic Energy Orb Infinite (Cheaty)
     */
    batterySingleuse("Single Use Batteries", "", "", false, true, false, false, false, false, false, false, false,
        false, 0, -1, 64, -1),
    /** Introduced by Calclavia */
    battery("Reusable Batteries", "", "", false, true, false, false, false, false, false, false, false, false, 0, -1,
        64, -1),
    /** Introduced by Calclavia */
    circuit("Circuits", "", "", true, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Introduced by Buildcraft */
    chipset("Chipsets", "", "", true, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** A whole Computer. "computerMaster" = ComputerCube */
    computer("Computers", "", "", true, true, false, false, true, false, false, false, false, false, 0, -1, 64, -1),

    // random known prefixes without special abilities.
    skull("Skulls", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    plating("Platings", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    dinosaur("Dinosaurs", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    travelgear("Travel Gear", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64,
        -1),
    bauble("Baubles", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    cluster("Clusters", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    grafter("Grafters", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    scoop("Scoops", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    frame("Frames", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    tome("Tomes", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    junk("Junk", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    bee("Bees", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    rod("Rods", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    dirt("Dirts", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    sand("Sands", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1),
    grass("Grasses", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    gravel("Gravels", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    mushroom("Mushrooms", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Introduced by Eloraam */
    wood("Woods", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    drop("Drops", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    fuel("Fuels", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    panel("Panels", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    brick("Bricks", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    chunk("Chunks", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    wire("Wires", "", "", false, false, false, false, true, false, false, false, false, false, 0, -1, 64, -1),
    seed("Seeds", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    reed("Reeds", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    sheetDouble("2x Sheets", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64,
        -1),
    sheet("Sheets", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    crop("Crops", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    plant("Plants", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    coin("Coins", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    lumar("Lumars", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    ground("Grounded Stuff", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64,
        -1),
    cable("Cables", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    component("Components", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64,
        -1),
    wax("Waxes", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    wall("Walls", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    tube("Tubes", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    list("Lists", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    food("Foods", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Introduced by SirSengir */
    gear("Gears", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    coral("Corals", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    flower("Flowers", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    storage("Storages", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    material("Materials", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    plasma("Plasmas", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    element("Elements", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    molecule("Molecules", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    wafer("Wafers", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    orb("Orbs", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    handle("Handles", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    blade("Blades", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    head("Heads", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    motor("Motors", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    bit("Bits", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    shears("Shears", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    turbine("Turbines", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    fertilizer("Fertilizers", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64,
        -1),
    chest("Chests", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    raw("Raw Things", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    stainedGlass("Stained Glasses", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1,
        64, -1),
    mystic("Mystic Stuff", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    mana("Mana Stuff", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    rune("Runes", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    petal("Petals", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    pearl("Pearls", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    powder("Powders", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    soulsand("Soulsands", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    obsidian("Obsidians", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    glowstone("Glowstones", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64,
        -1),
    beans("Beans", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    br("br", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    essence("Essences", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    alloy("Alloys", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    cooking("Cooked Things", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64,
        -1),
    elven("Elven Stuff", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    reactor("Reactors", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    mffs("MFFS", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    projred("Project Red", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    ganys("Ganys Stuff", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    liquid("Liquids", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    bars("Bars", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    bar("Bars", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
    /** Reverse Head consisting out of 6 Ingots. */
    toolHeadMallet("Mallet Heads", "", " Mallet Head", true, true, false, false, false, false, true, true, false, false,
        B[6], M * 6, 64, 127),
    /** Reverse Stick made of half an Ingot. Introduced by Eloraam */
    handleMallet("Mallet Handle", "", " Handle", true, true, false, false, false, false, true, true, false, false,
        B[1] | B[2], M / 2, 64, 126),

    // Cracked fluids
    cellHydroCracked1("Cells", "Lightly Hydro-Cracked ", " Cell", true, true, true, true, false, false, false, true,
        false, false, 0, M * 1, 64, 30),
    cellHydroCracked2("Cells", "Moderately Hydro-Cracked ", " Cell", true, true, true, true, false, false, false, true,
        false, false, 0, M * 1, 64, 30),
    cellHydroCracked3("Cells", "Severely Hydro-Cracked ", " Cell", true, true, true, true, false, false, false, true,
        false, false, 0, M * 1, 64, 30),
    cellSteamCracked1("Cells", "Lightly Steam-Cracked ", " Cell", true, true, true, true, false, false, false, true,
        false, false, 0, M * 1, 64, 30),
    cellSteamCracked2("Cells", "Moderately Steam-Cracked ", " Cell", true, true, true, true, false, false, false, true,
        false, false, 0, M * 1, 64, 30),
    cellSteamCracked3("Cells", "Severely Steam-Cracked ", " Cell", true, true, true, true, false, false, false, true,
        false, false, 0, M * 1, 64, 30),

    componentCircuit("Circuit Parts", "", "", true, true, false, false, false, false, false, false, false, false, 0, -1,
        64, -1),

    apiaryUpgrade("Industrial Apiary Upgrade", "", "", false, false, true, false, false, false, false, false, false,
        false, 0, -1, 64, -1),
    beeComb("Bee Combs", "", "", true, false, true, false, false, false, false, false, false, false, 0, -1, 64, -1),
    nanite("Nanites", "", " Nanites", true, true, true, false, false, false, false, false, false, false, 0, -1, 64, 50),
    // migrated from GT++
    milled("Milled Ores", "Milled ", " Ore", true, true, false, false, false, false, false, false, false, true, B[3],
        -1, 64, -1),
    // migrated from bartworks
    blockCasing("A Casing block for a Multiblock-Machine", "Bolted ", " Casing", true, true, true, true, false, true,
        false, true, false, false, 0, M * 9, 64, -1),
    blockCasingAdvanced("An Advanced Casing block for a Multiblock-Machine", "Rebolted ", " Casing", true, true, true,
        true, false, true, false, true, false, false, 0, M * 9, 64, -1),
    capsuleMolten("Capsule of Molten stuff", "Molten ", " Capsule", true, true, true, true, false, false, false, true,
        false, false, 0, M * 1, 64, -1);

    public static final ImmutableList<OrePrefixes> CELL_TYPES = ImmutableList.of(
        cell,
        cellMolten,
        cellPlasma,
        cellHydroCracked1,
        cellHydroCracked2,
        cellHydroCracked3,
        cellSteamCracked1,
        cellSteamCracked2,
        cellSteamCracked3);

    public static volatile int VERSION = 509;

    static {
        pulp.mPrefixInto = dust;
        oreGem.mPrefixInto = ore;
        leaves.mPrefixInto = treeLeaves;
        sapling.mPrefixInto = treeSapling;
        itemDust.mPrefixInto = dust;
        dustDirty.mPrefixInto = dustImpure;
        denseore.mPrefixInto = oreDense;
        ingotQuad.mPrefixInto = ingotQuadruple;
        plateQuad.mPrefixInto = plateQuadruple;
        stoneBrick.mPrefixInto = stoneBricks;
        stoneBricksMossy.mPrefixInto = stoneMossyBricks;

        ingotHot.mHeatDamage = 3.0F;
        cellMolten.mHeatDamage = 3;
        cellPlasma.mHeatDamage = 6.0F;

        block.ignoreMaterials(
            Materials.Ice,
            Materials.Snow,
            Materials.Concrete,
            Materials.Glass,
            Materials.Glowstone,
            Materials.DarkIron,
            Materials.Marble,
            Materials.Quartz,
            Materials.CertusQuartz,
            Materials.Limestone);
        ingot.ignoreMaterials(Materials.Brick, Materials.NetherBrick);

        dust.addFamiliarPrefix(dustTiny);
        dust.addFamiliarPrefix(dustSmall);
        dustTiny.addFamiliarPrefix(dust);
        dustTiny.addFamiliarPrefix(dustSmall);
        dustSmall.addFamiliarPrefix(dust);
        dustSmall.addFamiliarPrefix(dustTiny);

        ingot.addFamiliarPrefix(nugget);
        nugget.addFamiliarPrefix(ingot);

        for (OrePrefixes tPrefix1 : values()) if (tPrefix1.name()
            .startsWith("ore"))
            for (OrePrefixes tPrefix2 : values()) if (tPrefix2.name()
                .startsWith("ore")) tPrefix1.addFamiliarPrefix(tPrefix2);

        // These are only the important ones.
        gem.mNotGeneratedItems.add(Materials.Coal);
        gem.mNotGeneratedItems.add(Materials.Charcoal);
        gem.mNotGeneratedItems.add(Materials.NetherStar);
        gem.mNotGeneratedItems.add(Materials.Diamond);
        gem.mNotGeneratedItems.add(Materials.Emerald);
        gem.mNotGeneratedItems.add(Materials.NetherQuartz);
        gem.mNotGeneratedItems.add(Materials.EnderPearl);
        gem.mNotGeneratedItems.add(Materials.EnderEye);
        gem.mNotGeneratedItems.add(Materials.Flint);
        gem.mNotGeneratedItems.add(Materials.Lapis);
        dust.mNotGeneratedItems.add(Materials.Bone);
        dust.mNotGeneratedItems.add(Materials.Redstone);
        dust.mNotGeneratedItems.add(Materials.Glowstone);
        dust.mNotGeneratedItems.add(Materials.Gunpowder);
        dust.mNotGeneratedItems.add(Materials.Sugar);
        dust.mNotGeneratedItems.add(Materials.Blaze);
        // dust.mNotGeneratedItems.add(Materials.Ichorium);
        // dustSmall.mNotGeneratedItems.add(Materials.Ichorium);
        // dustTiny.mNotGeneratedItems.add(Materials.Ichorium);
        stick.mNotGeneratedItems.add(Materials.Wood);
        stick.mNotGeneratedItems.add(Materials.Bone);
        stick.mNotGeneratedItems.add(Materials.Blaze);
        ingot.mNotGeneratedItems.add(Materials.Iron);
        ingot.mNotGeneratedItems.add(Materials.Gold);
        ingot.mNotGeneratedItems.add(Materials.Brick);
        ingot.mNotGeneratedItems.add(Materials.BrickNether);
        ingot.mNotGeneratedItems.add(Materials.WoodSealed);
        ingot.mNotGeneratedItems.add(Materials.Wood);

        frame.mNotGeneratedItems.add(MaterialsUEVplus.Universium);
        frameGt.mNotGeneratedItems.add(MaterialsUEVplus.Universium);

        plateDouble.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        plateTriple.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        plateQuadruple.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        plateQuintuple.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        cell.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        ingotDouble.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        ingotTriple.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        ingotQuadruple.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        ingotQuintuple.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        turbineBlade.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        dust.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        dustSmall.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        dustTiny.mNotGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);

        // ingot.mNotGeneratedItems.add(Materials.Ichorium);
        nugget.mNotGeneratedItems.add(Materials.Gold);
        plate.mNotGeneratedItems.add(Materials.Paper);
        cell.mNotGeneratedItems.add(Materials.Empty);
        cell.mNotGeneratedItems.add(Materials.Water);
        cell.mNotGeneratedItems.add(Materials.Lava);
        cell.mNotGeneratedItems.add(Materials.ConstructionFoam);
        cell.mNotGeneratedItems.add(Materials.UUMatter);
        cell.mNotGeneratedItems.add(Materials.CoalFuel);
        bucket.mNotGeneratedItems.add(Materials.Empty);
        bucket.mNotGeneratedItems.add(Materials.Lava);
        bucket.mNotGeneratedItems.add(Materials.Milk);
        bucket.mNotGeneratedItems.add(Materials.Water);
        bucketClay.mNotGeneratedItems.add(Materials.Empty);
        bucketClay.mNotGeneratedItems.add(Materials.Lava);
        bucketClay.mNotGeneratedItems.add(Materials.Milk);
        bucketClay.mNotGeneratedItems.add(Materials.Water);
        bottle.mNotGeneratedItems.add(Materials.Empty);
        bottle.mNotGeneratedItems.add(Materials.Water);
        bottle.mNotGeneratedItems.add(Materials.Milk);
        block.mNotGeneratedItems.add(Materials.Iron);
        block.mNotGeneratedItems.add(Materials.Gold);
        block.mNotGeneratedItems.add(Materials.Lapis);
        block.mNotGeneratedItems.add(Materials.Emerald);
        block.mNotGeneratedItems.add(Materials.Redstone);
        block.mNotGeneratedItems.add(Materials.Diamond);
        block.mNotGeneratedItems.add(Materials.Coal);
        toolHeadArrow.mNotGeneratedItems.add(Materials.Glass);
        toolHeadArrow.mNotGeneratedItems.add(MaterialsUEVplus.TranscendentMetal);
        arrowGtPlastic.mNotGeneratedItems.add(MaterialsUEVplus.TranscendentMetal);
        arrow.mNotGeneratedItems.add(MaterialsUEVplus.TranscendentMetal);
        arrowGtWood.mNotGeneratedItems.add(MaterialsUEVplus.TranscendentMetal);

        // -----

        dustImpure.mGeneratedItems.add(Materials.GraniteRed);
        dustImpure.mGeneratedItems.add(Materials.GraniteBlack);
        dustImpure.mGeneratedItems.add(Materials.Quartzite);
        dustImpure.mGeneratedItems.add(Materials.Flint);
        dustImpure.mGeneratedItems.add(Materials.Redrock);
        dustImpure.mGeneratedItems.add(Materials.Basalt);
        dustImpure.mGeneratedItems.add(Materials.Marble);
        dustImpure.mGeneratedItems.add(Materials.Netherrack);
        dustImpure.mGeneratedItems.add(Materials.Endstone);
        dustImpure.mGeneratedItems.add(Materials.Stone);

        plate.mGeneratedItems.add(Materials.Redstone);
        plate.mGeneratedItems.add(Materials.Concrete);
        plate.mGeneratedItems.add(Materials.GraniteRed);
        plate.mGeneratedItems.add(Materials.GraniteBlack);
        plate.mGeneratedItems.add(Materials.Basalt);
        plate.mGeneratedItems.add(Materials.Marble);
        plate.mGeneratedItems.add(Materials.Glowstone);
        plate.mGeneratedItems.add(Materials.Electrotine);
        plate.mGeneratedItems.add(Materials.Obsidian);

        ingotHot.mGeneratedItems.add(MaterialsUEVplus.TranscendentMetal);

        plate.mGeneratedItems.add(Materials.Paper);
        plateDouble.mGeneratedItems.add(Materials.Paper);
        plateTriple.mGeneratedItems.add(Materials.Paper);
        plateQuadruple.mGeneratedItems.add(Materials.Paper);
        plateQuintuple.mGeneratedItems.add(Materials.Paper);
        ring.mGeneratedItems.add(Materials.Paper);

        lens.mGeneratedItems.add(Materials.EnderPearl);
        lens.mGeneratedItems.add(Materials.EnderEye);

        stickLong.mGeneratedItems.add(Materials.Blaze);

        nanite.mGeneratedItems.add(Materials.Carbon);
        nanite.mGeneratedItems.add(Materials.Gold);
        nanite.mGeneratedItems.add(Materials.Iron);
        nanite.mGeneratedItems.add(Materials.Copper);
        nanite.mGeneratedItems.add(Materials.Silver);
        nanite.mGeneratedItems.add(MaterialsUEVplus.TranscendentMetal);
        nanite.mGeneratedItems.add(Materials.Neutronium);
        nanite.mGeneratedItems.add(MaterialsUEVplus.Universium);
        nanite.mGeneratedItems.add(MaterialsUEVplus.WhiteDwarfMatter);
        nanite.mGeneratedItems.add(MaterialsUEVplus.BlackDwarfMatter);
        nanite.mGeneratedItems.add(Materials.Glowstone);
        // -----

        gear.mGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        ingot.mGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        toolHeadHammer.mGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        frame.mGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);
        frameGt.mGeneratedItems.add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);

        dust.mGeneratedItems.addAll(dustPure.mGeneratedItems);
        dust.mGeneratedItems.addAll(dustImpure.mGeneratedItems);
        dust.mGeneratedItems.addAll(dustRefined.mGeneratedItems);
        dustTiny.mGeneratedItems.addAll(dust.mGeneratedItems);
        dustSmall.mGeneratedItems.addAll(dust.mGeneratedItems);
        crateGtDust.mGeneratedItems.addAll(dust.mGeneratedItems);
        crateGtIngot.mGeneratedItems.addAll(ingot.mGeneratedItems);
        crateGtGem.mGeneratedItems.addAll(gem.mGeneratedItems);
        crateGtPlate.mGeneratedItems.addAll(plate.mGeneratedItems);
        itemCasing.mGeneratedItems.addAll(itemCasing.mGeneratedItems);
        // -----

        toolHeadFile.mCondition = new ICondition.And<>(
            new ICondition.Not<>(SubTag.NO_SMASHING),
            new ICondition.Not<>(SubTag.BOUNCY));
        toolHeadSaw.mCondition = new ICondition.And<>(
            new ICondition.Not<>(SubTag.NO_SMASHING),
            new ICondition.Not<>(SubTag.BOUNCY));
        toolHeadDrill.mCondition = new ICondition.And<>(
            new ICondition.Not<>(SubTag.NO_SMASHING),
            new ICondition.Not<>(SubTag.BOUNCY));
        toolHeadChainsaw.mCondition = new ICondition.And<>(
            new ICondition.Not<>(SubTag.NO_SMASHING),
            new ICondition.Not<>(SubTag.BOUNCY));
        toolHeadWrench.mCondition = new ICondition.And<>(
            new ICondition.Not<>(SubTag.NO_SMASHING),
            new ICondition.Not<>(SubTag.BOUNCY));
        toolHeadBuzzSaw.mCondition = new ICondition.And<>(
            new ICondition.Not<>(SubTag.NO_SMASHING),
            new ICondition.Not<>(SubTag.BOUNCY));
        turbineBlade.mCondition = new ICondition.And<>(
            new ICondition.Not<>(SubTag.NO_SMASHING),
            new ICondition.Not<>(SubTag.BOUNCY));

        rotor.mCondition = new ICondition.Nor<>(SubTag.CRYSTAL, SubTag.STONE, SubTag.BOUNCY);

        spring.mCondition = new ICondition.Or<>(
            SubTag.STRETCHY,
            SubTag.BOUNCY,
            new ICondition.Not<>(SubTag.NO_SMASHING));
        springSmall.mCondition = new ICondition.Or<>(
            SubTag.STRETCHY,
            SubTag.BOUNCY,
            new ICondition.Not<>(SubTag.NO_SMASHING));

        gemChipped.mCondition = new ICondition.And<>(
            SubTag.TRANSPARENT,
            SubTag.CRYSTAL,
            new ICondition.Not<>(SubTag.QUARTZ),
            new ICondition.Not<>(SubTag.PEARL),
            new ICondition.Not<>(SubTag.MAGICAL));
        gemFlawed.mCondition = new ICondition.And<>(
            SubTag.TRANSPARENT,
            SubTag.CRYSTAL,
            new ICondition.Not<>(SubTag.QUARTZ),
            new ICondition.Not<>(SubTag.PEARL),
            new ICondition.Not<>(SubTag.MAGICAL));
        gemFlawless.mCondition = new ICondition.And<>(
            SubTag.TRANSPARENT,
            SubTag.CRYSTAL,
            new ICondition.Not<>(SubTag.QUARTZ),
            new ICondition.Not<>(SubTag.PEARL),
            new ICondition.Not<>(SubTag.MAGICAL));
        gemExquisite.mCondition = new ICondition.And<>(
            SubTag.TRANSPARENT,
            SubTag.CRYSTAL,
            new ICondition.Not<>(SubTag.QUARTZ),
            new ICondition.Not<>(SubTag.PEARL),
            new ICondition.Not<>(SubTag.MAGICAL));

        lens.mCondition = new ICondition.Or<>(
            SubTag.MAGICAL,
            new ICondition.And<>(SubTag.TRANSPARENT, SubTag.HAS_COLOR));

        plateDouble.mCondition = new ICondition.Or<>(SubTag.PAPER, new ICondition.Not<>(SubTag.NO_SMASHING));
        plateTriple.mCondition = new ICondition.Or<>(SubTag.PAPER, new ICondition.Not<>(SubTag.NO_SMASHING));
        plateQuadruple.mCondition = new ICondition.Or<>(SubTag.PAPER, new ICondition.Not<>(SubTag.NO_SMASHING));
        plateQuintuple.mCondition = new ICondition.Or<>(SubTag.PAPER, new ICondition.Not<>(SubTag.NO_SMASHING));

        plateDense.mCondition = new ICondition.Not<>(SubTag.NO_SMASHING);

        ingotDouble.mCondition = new ICondition.Not<>(SubTag.NO_SMASHING);
        ingotTriple.mCondition = new ICondition.Not<>(SubTag.NO_SMASHING);
        ingotQuadruple.mCondition = new ICondition.Not<>(SubTag.NO_SMASHING);
        ingotQuintuple.mCondition = new ICondition.Not<>(SubTag.NO_SMASHING);

        wireFine.mCondition = SubTag.METAL;

        // -----

        pipeRestrictiveTiny.mSecondaryMaterial = new MaterialStack(Materials.Steel, ring.mMaterialAmount);
        pipeRestrictiveSmall.mSecondaryMaterial = new MaterialStack(Materials.Steel, ring.mMaterialAmount * 2);
        pipeRestrictiveMedium.mSecondaryMaterial = new MaterialStack(Materials.Steel, ring.mMaterialAmount * 3);
        pipeRestrictiveLarge.mSecondaryMaterial = new MaterialStack(Materials.Steel, ring.mMaterialAmount * 4);
        pipeRestrictiveHuge.mSecondaryMaterial = new MaterialStack(Materials.Steel, ring.mMaterialAmount * 5);
        cableGt16.mSecondaryMaterial = new MaterialStack(Materials.Rubber, plate.mMaterialAmount * 5);
        cableGt12.mSecondaryMaterial = new MaterialStack(Materials.Rubber, plate.mMaterialAmount * 4);
        cableGt08.mSecondaryMaterial = new MaterialStack(Materials.Rubber, plate.mMaterialAmount * 3);
        cableGt04.mSecondaryMaterial = new MaterialStack(Materials.Rubber, plate.mMaterialAmount * 2);
        cableGt02.mSecondaryMaterial = new MaterialStack(Materials.Rubber, plate.mMaterialAmount);
        cableGt01.mSecondaryMaterial = new MaterialStack(Materials.Rubber, plate.mMaterialAmount);
        bucket.mSecondaryMaterial = new MaterialStack(Materials.Iron, ingot.mMaterialAmount * 3);
        bucketClay.mSecondaryMaterial = new MaterialStack(Materials.Clay, dust.mMaterialAmount * 5);
        CELL_TYPES
            .forEach(prefix -> prefix.mSecondaryMaterial = new MaterialStack(Materials.Tin, plate.mMaterialAmount * 2));
        oreRedgranite.mSecondaryMaterial = new MaterialStack(Materials.GraniteRed, dust.mMaterialAmount);
        oreBlackgranite.mSecondaryMaterial = new MaterialStack(Materials.GraniteBlack, dust.mMaterialAmount);
        oreNetherrack.mSecondaryMaterial = new MaterialStack(Materials.Netherrack, dust.mMaterialAmount);
        oreNether.mSecondaryMaterial = new MaterialStack(Materials.Netherrack, dust.mMaterialAmount);
        oreEndstone.mSecondaryMaterial = new MaterialStack(Materials.Endstone, dust.mMaterialAmount);
        oreEnd.mSecondaryMaterial = new MaterialStack(Materials.Endstone, dust.mMaterialAmount);
        oreMarble.mSecondaryMaterial = new MaterialStack(Materials.Marble, dust.mMaterialAmount);
        oreBasalt.mSecondaryMaterial = new MaterialStack(Materials.Basalt, dust.mMaterialAmount);
        oreDense.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount);
        orePoor.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount * 2);
        oreSmall.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount * 2);
        oreNormal.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount * 2);
        oreRich.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount * 2);
        ore.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount);
        crushed.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount);
        toolHeadDrill.mSecondaryMaterial = new MaterialStack(Materials.Steel, plate.mMaterialAmount * 4);
        toolHeadChainsaw.mSecondaryMaterial = new MaterialStack(
            Materials.Steel,
            plate.mMaterialAmount * 4 + ring.mMaterialAmount * 2);
        toolHeadWrench.mSecondaryMaterial = new MaterialStack(
            Materials.Steel,
            ring.mMaterialAmount + screw.mMaterialAmount * 2);
        arrowGtWood.mSecondaryMaterial = new MaterialStack(Materials.Wood, stick.mMaterialAmount);
        arrowGtPlastic.mSecondaryMaterial = new MaterialStack(Materials.Plastic, stick.mMaterialAmount);
        bulletGtSmall.mSecondaryMaterial = new MaterialStack(Materials.Brass, ingot.mMaterialAmount / 9);
        bulletGtMedium.mSecondaryMaterial = new MaterialStack(Materials.Brass, ingot.mMaterialAmount / 6);
        bulletGtLarge.mSecondaryMaterial = new MaterialStack(Materials.Brass, ingot.mMaterialAmount / 3);
    }

    public final ArrayList<ItemStack> mPrefixedItems = new GT_ArrayList<>(false, 16);
    public final short mTextureIndex;
    public final String mRegularLocalName, mLocalizedMaterialPre, mLocalizedMaterialPost;
    public final boolean mIsUsedForOreProcessing, mIsEnchantable, mIsUnificatable, mIsMaterialBased, mIsSelfReferencing,
        mIsContainer, mDontUnificateActively, mIsUsedForBlocks, mAllowNormalRecycling, mGenerateDefaultItem;
    public final List<TC_AspectStack> mAspects = new ArrayList<>();
    public final Collection<OrePrefixes> mFamiliarPrefixes = new HashSet<>();
    /**
     * Used to determine the amount of Material this Prefix contains. Multiply or Divide GregTech_API.MATERIAL_UNIT to
     * get the Amounts in comparision to one Ingot. 0 = Null Negative = Undefined Amount
     */
    public final long mMaterialAmount;

    public final Collection<Materials> mDisabledItems = new HashSet<>(), mNotGeneratedItems = new HashSet<>(),
        mIgnoredMaterials = new HashSet<>(), mGeneratedItems = new HashSet<>();
    private final ArrayList<IOreRecipeRegistrator> mOreProcessing = new ArrayList<>();
    public ItemStack mContainerItem = null;
    public ICondition<ISubTagContainer> mCondition = null;
    public byte mDefaultStackSize = 64;
    public MaterialStack mSecondaryMaterial = null;
    public OrePrefixes mPrefixInto = this;
    public float mHeatDamage = 0.0F; // Negative for Frost Damage
    private final GT_HashSet<GT_ItemStack2> mContainsTestCache = new GT_HashSet<>(512, 0.5f);
    public static List<OrePrefixes> mPreventableComponents = new LinkedList<>(
        Arrays.asList(
            OrePrefixes.gem,
            OrePrefixes.ingotHot,
            OrePrefixes.ingotDouble,
            OrePrefixes.ingotTriple,
            OrePrefixes.ingotQuadruple,
            OrePrefixes.ingotQuintuple,
            OrePrefixes.plate,
            OrePrefixes.plateDouble,
            OrePrefixes.plateTriple,
            OrePrefixes.plateQuadruple,
            OrePrefixes.plateQuintuple,
            OrePrefixes.plateDense,
            OrePrefixes.stick,
            OrePrefixes.round,
            OrePrefixes.bolt,
            OrePrefixes.screw,
            OrePrefixes.ring,
            OrePrefixes.foil,
            OrePrefixes.toolHeadSword,
            OrePrefixes.toolHeadPickaxe,
            OrePrefixes.toolHeadShovel,
            OrePrefixes.toolHeadAxe,
            OrePrefixes.toolHeadHoe,
            OrePrefixes.toolHeadHammer,
            OrePrefixes.toolHeadFile,
            OrePrefixes.toolHeadSaw,
            OrePrefixes.toolHeadDrill,
            OrePrefixes.toolHeadChainsaw,
            OrePrefixes.toolHeadWrench,
            OrePrefixes.toolHeadUniversalSpade,
            OrePrefixes.toolHeadSense,
            OrePrefixes.toolHeadPlow,
            OrePrefixes.toolHeadArrow,
            OrePrefixes.toolHeadBuzzSaw,
            OrePrefixes.turbineBlade,
            OrePrefixes.wireFine,
            OrePrefixes.gearGtSmall,
            OrePrefixes.rotor,
            OrePrefixes.stickLong,
            OrePrefixes.springSmall,
            OrePrefixes.spring,
            OrePrefixes.arrowGtWood,
            OrePrefixes.arrowGtPlastic,
            OrePrefixes.gemChipped,
            OrePrefixes.gemFlawed,
            OrePrefixes.gemFlawless,
            OrePrefixes.gemExquisite,
            OrePrefixes.gearGt,
            OrePrefixes.crateGtDust,
            OrePrefixes.crateGtIngot,
            OrePrefixes.crateGtGem,
            OrePrefixes.crateGtPlate,
            OrePrefixes.itemCasing,
            OrePrefixes.nanite));
    /**
     * Yes this Value can be changed to add Bits for the MetaGenerated-Item-Check.
     */
    public int mMaterialGenerationBits = 0;

    OrePrefixes(String aRegularLocalName, String aLocalizedMaterialPre, String aLocalizedMaterialPost,
        boolean aIsUnificatable, boolean aIsMaterialBased, boolean aIsSelfReferencing, boolean aIsContainer,
        boolean aDontUnificateActively, boolean aIsUsedForBlocks, boolean aAllowNormalRecycling,
        boolean aGenerateDefaultItem, boolean aIsEnchantable, boolean aIsUsedForOreProcessing,
        int aMaterialGenerationBits, long aMaterialAmount, int aDefaultStackSize, int aTextureindex) {
        mIsUnificatable = aIsUnificatable;
        mIsMaterialBased = aIsMaterialBased;
        mIsSelfReferencing = aIsSelfReferencing;
        mIsContainer = aIsContainer;
        mDontUnificateActively = aDontUnificateActively;
        mIsUsedForBlocks = aIsUsedForBlocks;
        mAllowNormalRecycling = aAllowNormalRecycling;
        mGenerateDefaultItem = aGenerateDefaultItem;
        mIsEnchantable = aIsEnchantable;
        mIsUsedForOreProcessing = aIsUsedForOreProcessing;
        mMaterialGenerationBits = aMaterialGenerationBits;
        mMaterialAmount = aMaterialAmount;
        mRegularLocalName = aRegularLocalName;
        mLocalizedMaterialPre = aLocalizedMaterialPre;
        mLocalizedMaterialPost = aLocalizedMaterialPost;
        mDefaultStackSize = (byte) aDefaultStackSize;
        mTextureIndex = (short) aTextureindex;

        if (name().startsWith("ore")) {
            new TC_AspectStack(TC_Aspects.TERRA, 1).addToAspectList(mAspects);
        } else if (name().startsWith("wire") || name().startsWith("cable")) {
            new TC_AspectStack(TC_Aspects.ELECTRUM, 1).addToAspectList(mAspects);
        } else if (name().startsWith("dust")) {
            new TC_AspectStack(TC_Aspects.PERDITIO, 1).addToAspectList(mAspects);
        } else if (name().startsWith("crushed")) {
            new TC_AspectStack(TC_Aspects.PERFODIO, 1).addToAspectList(mAspects);
        } else if (name().startsWith("ingot") || name().startsWith("nugget")) {
            new TC_AspectStack(TC_Aspects.METALLUM, 1).addToAspectList(mAspects);
        } else if (name().startsWith("armor")) {
            new TC_AspectStack(TC_Aspects.TUTAMEN, 1).addToAspectList(mAspects);
        } else if (name().startsWith("stone")) {
            new TC_AspectStack(TC_Aspects.TERRA, 1).addToAspectList(mAspects);
        } else if (name().startsWith("pipe")) {
            new TC_AspectStack(TC_Aspects.ITER, 1).addToAspectList(mAspects);
        } else if (name().startsWith("gear")) {
            new TC_AspectStack(TC_Aspects.MOTUS, 1).addToAspectList(mAspects);
            new TC_AspectStack(TC_Aspects.MACHINA, 1).addToAspectList(mAspects);
        } else if (name().startsWith("frame") || name().startsWith("plate")) {
            new TC_AspectStack(TC_Aspects.FABRICO, 1).addToAspectList(mAspects);
        } else if (name().startsWith("tool")) {
            new TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2).addToAspectList(mAspects);
        } else if (name().startsWith("gem") || name().startsWith("crystal") || name().startsWith("lens")) {
            new TC_AspectStack(TC_Aspects.VITREUS, 1).addToAspectList(mAspects);
        } else if (name().startsWith("crate")) {
            new TC_AspectStack(TC_Aspects.ITER, 2).addToAspectList(mAspects);
        } else if (name().startsWith("circuit")) {
            new TC_AspectStack(TC_Aspects.COGNITIO, 1).addToAspectList(mAspects);
        } else if (name().startsWith("computer")) {
            new TC_AspectStack(TC_Aspects.COGNITIO, 4).addToAspectList(mAspects);
        } else if (name().startsWith("battery")) {
            new TC_AspectStack(TC_Aspects.ELECTRUM, 1).addToAspectList(mAspects);
        }
    }

    public static void initMaterialComponents() {
        boolean enablePerItemSettings = GregTech_API.sMaterialComponents.get("general", "enablePerItemSettings", false);
        boolean enableUnusedIngotHot = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedIngotHot", false);
        boolean enableUnusedPlates = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedPlates", false);
        boolean enableUnusedDoubleIngots = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedDoubleIngots", false);
        boolean enableUnusedTripleIngots = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedTripleIngots", false);
        boolean enableUnusedQuadIngots = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedQuadIngots", false);
        boolean enableUnusedQuinIngots = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedQuinIngots", false);
        boolean enableUnusedDoublePlates = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedDoublePlates", false);
        boolean enableUnusedTriplePlates = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedTriplePlates", false);
        boolean enableUnusedQuadPlates = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedQuadPlates", false);
        boolean enableUnusedQuinPlates = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedQuinPlates", false);
        boolean enableUnusedDensePlates = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedDensePlates", false);
        boolean enableUnusedGears = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedGears", false);
        boolean enableUnusedSmallGears = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedSmallGears", false);
        boolean enableUnusedRings = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedRings", false);
        boolean enableUnusedSprings = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedSprings", false);
        boolean enableUnusedSmallSprings = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedSmallSprings", false);
        boolean enableUnusedRounds = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedRounds", false);
        boolean enableUnusedRotors = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedRotors", false);
        boolean enableUnusedFineWires = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedFineWires", false);
        boolean enableUnusedFoil = GregTech_API.sMaterialComponents.get("globalcomponents", "enableUnusedFoil", false);
        boolean enableUnusedArrows = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedArrowHeads", false);
        boolean enableUnusedCrates = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedCrates", false);
        boolean enableUnusedBolts = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedBolts", false);
        boolean enableUnusedScrews = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedScrews", false);
        boolean enableUnusedRods = GregTech_API.sMaterialComponents.get("globalcomponents", "enableUnusedRods", false);
        boolean enableUnusedLongRods = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedLongRods", false);
        boolean enableUnusedGems = GregTech_API.sMaterialComponents.get("globalcomponents", "enableUnusedGems", false);
        boolean enableUnusedItemCasing = GregTech_API.sMaterialComponents
            .get("globalcomponents", "enableUnusedItemCasing", false);

        // TODO possibly use OrePrefix mNotGeneratedItems/mGeneratedItems instead of a static List for every material
        // instance?
        // TODO Make sure stuff like gem plates / standard plates / paper plates all generate with the current condition
        for (Materials aMaterial : Materials.values()) {
            if (aMaterial.mMetaItemSubID > 0) {
                if (aMaterial.mBlastFurnaceTemp <= 1750) ingotHot.mDisabledItems.add(aMaterial); // Moved HotIngot code
                                                                                                 // from
                                                                                                 // GT_MetaGenerated_Item_01
                                                                                                 // so all this is in
                                                                                                 // once
                // place
                if (!enableUnusedSprings && (aMaterial != Materials.Titanium)) spring.mDisabledItems.add(aMaterial);
                if (!enableUnusedSmallSprings) springSmall.mDisabledItems.add(aMaterial);
                if (!enableUnusedRounds && !(aMaterial == Materials.HSSE || aMaterial == Materials.Neutronium
                    || aMaterial == Materials.HSSG)) round.mDisabledItems.add(aMaterial);
                if (!enableUnusedCrates) {
                    if (!(aMaterial == Materials.DamascusSteel || aMaterial == Materials.Steel
                        || aMaterial == Materials.Bronze
                        || aMaterial == Materials.Manganese)) crateGtIngot.mDisabledItems.add(aMaterial);
                    if (!(aMaterial == Materials.Neodymium || aMaterial == Materials.Chrome))
                        crateGtDust.mDisabledItems.add(aMaterial);
                    crateGtGem.mDisabledItems.add(aMaterial);
                    crateGtPlate.mDisabledItems.add(aMaterial);
                }
                if (!enableUnusedArrows) {
                    toolHeadArrow.mDisabledItems.add(aMaterial);
                    arrowGtPlastic.mDisabledItems.add(aMaterial);
                    if (!(aMaterial == Materials.DamascusSteel || aMaterial == Materials.SterlingSilver))
                        arrowGtWood.mDisabledItems.add(aMaterial);
                }
                // Plates
                if (!enableUnusedPlates && ((aMaterial.mTypes & 0x40) == 0)
                    && !(aMaterial == Materials.Silicon || aMaterial == Materials.Zinc
                        || aMaterial == Materials.Europium
                        || aMaterial == Materials.Americium
                        || aMaterial == Materials.RedAlloy
                        || aMaterial == Materials.SolderingAlloy
                        || aMaterial == Materials.BatteryAlloy
                        || aMaterial == Materials.AnnealedCopper
                        || aMaterial == Materials.Firestone
                        || aMaterial == Materials.VanadiumGallium
                        || aMaterial == Materials.YttriumBariumCuprate
                        || aMaterial == Materials.NiobiumTitanium
                        || aMaterial == Materials.CertusQuartz
                        || aMaterial == Materials.NetherQuartz
                        || aMaterial == Materials.Lazurite
                        || aMaterial == Materials.Lapis
                        || aMaterial == Materials.Paper
                        || aMaterial == Materials.Jasper
                        || aMaterial == Materials.Dilithium
                        || aMaterial == Materials.Forcicium
                        || aMaterial == Materials.Forcillium
                        || aMaterial == Materials.EnderPearl
                        || aMaterial == Materials.EnderEye
                        || aMaterial == Materials.Glass
                        || aMaterial == Materials.Copper
                        || aMaterial == Materials.Tin
                        || aMaterial == Materials.Redstone
                        || aMaterial == Materials.Sodalite
                        || aMaterial == Materials.Gallium
                        || aMaterial == Materials.GalliumArsenide
                        || aMaterial == Materials.IndiumGalliumPhosphide))
                    plate.mDisabledItems.add(aMaterial);
                if (!enableUnusedIngotHot) {
                    ingotHot.mDisabledItems.add(aMaterial);
                }
                // Ingot/Plate Storage
                if (!enableUnusedDoubleIngots) ingotDouble.mDisabledItems.add(aMaterial);
                if (!enableUnusedTripleIngots) ingotTriple.mDisabledItems.add(aMaterial);
                if (!enableUnusedQuadIngots) ingotQuadruple.mDisabledItems.add(aMaterial);
                if (!enableUnusedQuinIngots) ingotQuintuple.mDisabledItems.add(aMaterial);
                if (!enableUnusedDoublePlates && ((aMaterial.mTypes & 0x40) == 0)
                    && !(aMaterial == Materials.Paper || aMaterial == Materials.Aluminium
                        || aMaterial == Materials.Steel
                        || aMaterial == Materials.TungstenSteel))
                    plateDouble.mDisabledItems.add(aMaterial);
                if (!enableUnusedTriplePlates && !(aMaterial == Materials.Paper))
                    plateTriple.mDisabledItems.add(aMaterial);
                if (!enableUnusedQuadPlates && !(aMaterial == Materials.Paper))
                    plateQuadruple.mDisabledItems.add(aMaterial);
                if (!enableUnusedQuinPlates && !(aMaterial == Materials.Paper))
                    plateQuintuple.mDisabledItems.add(aMaterial);
                if (!(enableUnusedDensePlates || GregTech_API.mGTPlusPlus)
                    && !(aMaterial == Materials.Iron || aMaterial == Materials.Copper
                        || aMaterial == Materials.Lead
                        || aMaterial == Materials.Paper
                        || aMaterial == Materials.Thaumium
                        || aMaterial == Materials.Titanium))
                    plateDense.mDisabledItems.add(aMaterial);
                // Rotors
                if (!enableUnusedRotors && !(aMaterial == Materials.Titanium || aMaterial == Materials.Chrome
                    || aMaterial == Materials.Tin
                    || aMaterial == Materials.Osmium
                    || aMaterial == Materials.Iridium
                    || aMaterial == Materials.Bronze
                    || aMaterial == Materials.Steel
                    || aMaterial == Materials.StainlessSteel
                    || aMaterial == Materials.TungstenSteel
                    || aMaterial == Materials.HSSG
                    || aMaterial == Materials.HSSE
                    || aMaterial == Materials.Neutronium)) rotor.mDisabledItems.add(aMaterial);
                // Rings
                if (!enableUnusedRings && !(aMaterial == Materials.Titanium || aMaterial == Materials.Chrome
                    || aMaterial == Materials.Iron
                    || aMaterial == Materials.Tin
                    || aMaterial == Materials.Osmium
                    || aMaterial == Materials.Iridium
                    || aMaterial == Materials.Bronze
                    || aMaterial == Materials.WroughtIron
                    || aMaterial == Materials.Steel
                    || aMaterial == Materials.StainlessSteel
                    || aMaterial == Materials.PigIron
                    || aMaterial == Materials.TungstenSteel
                    || aMaterial == Materials.Rubber
                    || aMaterial == Materials.HSSE
                    || aMaterial == Materials.Neutronium
                    || aMaterial == Materials.HSSG
                    || aMaterial == Materials.Aluminium
                    || aMaterial == Materials.Invar
                    || aMaterial == Materials.Brass
                    || aMaterial == Materials.Paper
                    || aMaterial == Materials.Silicone
                    || aMaterial == Materials.StyreneButadieneRubber)) ring.mDisabledItems.add(aMaterial);
                // Foil
                if (!enableUnusedFoil && !(aMaterial == Materials.Zinc || aMaterial == Materials.Aluminium
                    || aMaterial == Materials.Silicon
                    || aMaterial == Materials.Gold
                    || aMaterial == Materials.Electrum
                    || aMaterial == Materials.Platinum
                    || aMaterial == Materials.Osmiridium
                    || aMaterial == Materials.Osmium
                    || aMaterial == Materials.AnnealedCopper
                    || aMaterial == Materials.Steel
                    || aMaterial == Materials.Copper
                    || aMaterial == Materials.YttriumBariumCuprate
                    || aMaterial == Materials.VanadiumGallium
                    || aMaterial == Materials.NiobiumTitanium
                    || aMaterial == Materials.Naquadah
                    || aMaterial == Materials.Manganese
                    || aMaterial == Materials.Plastic
                    || aMaterial == Materials.Silicone
                    || aMaterial == Materials.PolyvinylChloride
                    || aMaterial == Materials.PolyphenyleneSulfide
                    || aMaterial == Materials.Nichrome
                    || aMaterial == Materials.BlackSteel
                    || aMaterial == Materials.Titanium
                    || aMaterial == Materials.TungstenSteel
                    || aMaterial == Materials.Tungsten
                    || aMaterial == Materials.HSSG
                    || aMaterial == Materials.NaquadahAlloy
                    || aMaterial == Materials.Duranium
                    || aMaterial == Materials.Europium
                    || aMaterial == Materials.Bedrockium)) foil.mDisabledItems.add(aMaterial);
                // Fine Wire
                if (!enableUnusedFineWires && !(aMaterial == Materials.Steel || aMaterial == Materials.AnnealedCopper
                    || aMaterial == Materials.Platinum
                    || aMaterial == Materials.Osmium
                    || aMaterial == Materials.Tin
                    || aMaterial == Materials.Lead
                    || aMaterial == Materials.SolderingAlloy
                    || aMaterial == Materials.Copper
                    || aMaterial == Materials.Electrum
                    || aMaterial == Materials.Gold
                    || aMaterial == Materials.RedAlloy
                    || aMaterial == Materials.Graphene
                    || aMaterial == Materials.NiobiumTitanium
                    || aMaterial == Materials.YttriumBariumCuprate
                    || aMaterial == Materials.BloodInfusedIron
                    || aMaterial == MaterialsUEVplus.Universium
                    || aMaterial == MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter
                    || aMaterial == MaterialsUEVplus.Eternity)) wireFine.mDisabledItems.add(aMaterial);
                // Gears
                if (!enableUnusedGears && !(aMaterial == Materials.Aluminium || aMaterial == Materials.Titanium
                    || aMaterial == Materials.Iron
                    || aMaterial == Materials.Copper
                    || aMaterial == Materials.Tin
                    || aMaterial == Materials.Gold
                    || aMaterial == Materials.Stone
                    || aMaterial == Materials.Bronze
                    || aMaterial == Materials.Steel
                    || aMaterial == Materials.StainlessSteel
                    || aMaterial == Materials.TungstenSteel
                    || aMaterial == Materials.CobaltBrass
                    || aMaterial == Materials.Diamond
                    || aMaterial == Materials.Wood
                    || aMaterial == Materials.HSSG
                    || aMaterial == Materials.HSSE
                    || aMaterial == Materials.Neutronium)) gearGt.mDisabledItems.add(aMaterial);
                // Small Gears
                if (!enableUnusedSmallGears && !(aMaterial == Materials.Aluminium || aMaterial == Materials.Titanium
                    || aMaterial == Materials.Steel
                    || aMaterial == Materials.StainlessSteel
                    || aMaterial == Materials.TungstenSteel
                    || aMaterial == Materials.HSSG
                    || aMaterial == Materials.HSSE
                    || aMaterial == Materials.Neutronium
                    || aMaterial == Materials.VanadiumGallium
                    || aMaterial == Materials.Naquadah)) gearGtSmall.mDisabledItems.add(aMaterial);
                // Bolts
                if (!enableUnusedBolts && ((aMaterial.mTypes & 0x40) == 0)
                    && !(aMaterial == Materials.Titanium || aMaterial == Materials.Chrome
                        || aMaterial == Materials.Iron
                        || aMaterial == Materials.Tin
                        || aMaterial == Materials.Osmium
                        || aMaterial == Materials.Iridium
                        || aMaterial == Materials.Neutronium
                        || aMaterial == Materials.Bronze
                        || aMaterial == Materials.WroughtIron
                        || aMaterial == Materials.Steel
                        || aMaterial == Materials.StainlessSteel
                        || aMaterial == Materials.PigIron
                        || aMaterial == Materials.TungstenSteel
                        || aMaterial == Materials.Tungsten
                        || aMaterial == Materials.HSSE
                        || aMaterial == Materials.HSSG))
                    bolt.mDisabledItems.add(aMaterial);
                // Screws
                if (!enableUnusedScrews && ((aMaterial.mTypes & 0x40) == 0)
                    && !(aMaterial == Materials.Titanium || aMaterial == Materials.Chrome
                        || aMaterial == Materials.Iron
                        || aMaterial == Materials.Tin
                        || aMaterial == Materials.Osmium
                        || aMaterial == Materials.Iridium
                        || aMaterial == Materials.Neutronium
                        || aMaterial == Materials.Bronze
                        || aMaterial == Materials.WroughtIron
                        || aMaterial == Materials.Steel
                        || aMaterial == Materials.StainlessSteel
                        || aMaterial == Materials.PigIron
                        || aMaterial == Materials.TungstenSteel
                        || aMaterial == Materials.HSSE
                        || aMaterial == Materials.HSSG))
                    screw.mDisabledItems.add(aMaterial);
                // Rods
                if (!enableUnusedRods && ((aMaterial.mTypes & 0x40) == 0)
                    && !(aMaterial == Materials.Titanium || aMaterial == Materials.Chrome
                        || aMaterial == Materials.Iron
                        || aMaterial == Materials.Tin
                        || aMaterial == Materials.Osmium
                        || aMaterial == Materials.Iridium
                        || aMaterial == Materials.Neutronium
                        || aMaterial == Materials.Bronze
                        || aMaterial == Materials.WroughtIron
                        || aMaterial == Materials.Steel
                        || aMaterial == Materials.StainlessSteel
                        || aMaterial == Materials.PigIron
                        || aMaterial == Materials.TungstenSteel
                        || aMaterial == Materials.HSSE
                        || aMaterial == Materials.HSSG
                        || aMaterial == Materials.Aluminium
                        || aMaterial == Materials.Copper
                        || aMaterial == Materials.Neodymium
                        || aMaterial == Materials.Europium
                        || aMaterial == Materials.Platinum
                        || aMaterial == Materials.Gold
                        || aMaterial == Materials.Uranium235
                        || aMaterial == Materials.Plutonium241
                        || aMaterial == Materials.Americium
                        || aMaterial == Materials.Brass
                        || aMaterial == Materials.Electrum
                        || aMaterial == Materials.NaquadahEnriched
                        || aMaterial == Materials.CobaltBrass
                        || aMaterial == Materials.IronMagnetic
                        || aMaterial == Materials.SteelMagnetic
                        || aMaterial == Materials.NeodymiumMagnetic
                        || aMaterial == Materials.Samarium
                        || aMaterial == Materials.SamariumMagnetic
                        || aMaterial == Materials.VanadiumGallium
                        || aMaterial == Materials.Diamond
                        || aMaterial == Materials.Wood
                        || aMaterial == Materials.Plastic
                        || aMaterial == Materials.Lead
                        || aMaterial == Materials.SolderingAlloy
                        || aMaterial == Materials.Lapis
                        || aMaterial == Materials.Lazurite
                        || aMaterial == Materials.Sodalite
                        || aMaterial == Materials.PolyvinylChloride))
                    stick.mDisabledItems.add(aMaterial);
                // Long Rods
                if (!enableUnusedLongRods && ((aMaterial.mTypes & 0x40) == 0)
                    && !(aMaterial == Materials.Titanium || aMaterial == Materials.NeodymiumMagnetic
                        || aMaterial == Materials.SamariumMagnetic
                        || aMaterial == Materials.HSSG
                        || aMaterial == Materials.HSSE
                        || aMaterial == Materials.Neutronium
                        || aMaterial == Materials.Americium
                        || aMaterial == Materials.WroughtIron
                        || aMaterial == Materials.Magnalium
                        || aMaterial == Materials.TungstenSteel))
                    stickLong.mDisabledItems.add(aMaterial);

                if (!enableUnusedGems && ((aMaterial.mTypes & 0x04) == 0)) {
                    gem.mDisabledItems.add(aMaterial);
                    gemChipped.mDisabledItems.add(aMaterial);
                    gemFlawless.mDisabledItems.add(aMaterial);
                    gemFlawed.mDisabledItems.add(aMaterial);
                    gemExquisite.mDisabledItems.add(aMaterial);
                }
                // itemCasing
                if (!enableUnusedItemCasing) itemCasing.mDisabledItems.add(aMaterial);
            }
        }
        for (IMaterialHandler aRegistrator : Materials.mMaterialHandlers) {
            aRegistrator.onComponentInit();
        }
        for (Materials aMaterial : Materials.values()) {
            if (aMaterial.mMetaItemSubID > 0) {
                for (IMaterialHandler aRegistrator : Materials.mMaterialHandlers) {
                    aRegistrator.onComponentIteration(aMaterial);
                }
                if (enablePerItemSettings) {
                    StringBuilder aConfigPathSB = new StringBuilder();
                    aConfigPathSB.append("materialcomponents.")
                        .append(aMaterial.mConfigSection)
                        .append(".")
                        .append(aMaterial.mName);
                    String aConfigPath = aConfigPathSB.toString();
                    for (OrePrefixes aPrefix : mPreventableComponents) {
                        boolean aEnableComponent = GregTech_API.sMaterialComponents
                            .get(aConfigPath, aPrefix.toString(), !aPrefix.mDisabledItems.contains(aMaterial));
                        if (!aEnableComponent) { // Disable component if false and is not already in disabled list
                            aPrefix.disableComponent(aMaterial);
                        } else { // Enable component if true and is not already in enabled list
                            aPrefix.enableComponent(aMaterial);
                        }
                    }
                    aConfigPathSB.setLength(0);
                }
            }
        }
    }

    public static boolean isInstanceOf(String aName, OrePrefixes aPrefix) {
        return aName != null && aName.startsWith(aPrefix.toString());
    }

    public void disableComponent(Materials aMaterial) {
        if (!this.mDisabledItems.contains(aMaterial)) this.mDisabledItems.add(aMaterial);
    }

    public static OrePrefixes getOrePrefix(String aOre) {
        for (OrePrefixes tPrefix : values()) if (aOre.startsWith(tPrefix.toString())) {
            if (tPrefix == oreNether && aOre.equals("oreNetherQuartz")) return ore;
            if (tPrefix == oreNether && aOre.equals("oreNetherStar")) return ore;
            if (tPrefix == oreBasalt && aOre.equals("oreBasalticMineralSand")) return ore;
            if (tPrefix == stickLong && aOre.equals("stickLongasssuperconductornameforuvwire")) return stick;
            if (tPrefix == stickLong && aOre.equals("stickLongasssuperconductornameforuhvwire")) return stick;
            return tPrefix;
        }
        return null;
    }

    public static String stripPrefix(String aOre) {
        for (OrePrefixes tPrefix : values()) {
            if (aOre.startsWith(tPrefix.toString())) {
                return aOre.replaceFirst(tPrefix.toString(), "");
            }
        }
        return aOre;
    }

    public static String replacePrefix(String aOre, OrePrefixes aPrefix) {
        for (OrePrefixes tPrefix : values()) {
            if (aOre.startsWith(tPrefix.toString())) {
                return aOre.replaceFirst(tPrefix.toString(), aPrefix.toString());
            }
        }
        return "";
    }

    public static OrePrefixes getPrefix(String aPrefixName) {
        return getPrefix(aPrefixName, null);
    }

    public static OrePrefixes getPrefix(String aPrefixName, OrePrefixes aReplacement) {
        Object tObject = GT_Utility.getFieldContent(OrePrefixes.class, aPrefixName, false, false);
        if (tObject instanceof OrePrefixes) return (OrePrefixes) tObject;
        return aReplacement;
    }

    public static Materials getMaterial(String aOre) {
        return Materials.get(stripPrefix(aOre));
    }

    public static Materials getMaterial(String aOre, OrePrefixes aPrefix) {
        return Materials.get(aOre.replaceFirst(aPrefix.toString(), ""));
    }

    public static Materials getRealMaterial(String aOre, OrePrefixes aPrefix) {
        return Materials.getRealMaterial(aOre.replaceFirst(aPrefix.toString(), ""));
    }

    public void enableComponent(Materials aMaterial) {
        this.mDisabledItems.remove(aMaterial);
    }

    public boolean add(ItemStack aStack) {
        if (aStack == null) return false;
        if (!contains(aStack)) {
            mPrefixedItems.add(aStack);
            // It's now in there... so update the cache
            mContainsTestCache.add(new GT_ItemStack2(aStack));
        }
        return true;
    }

    public boolean contains(ItemStack aStack) {
        return !GT_Utility.isStackInvalid(aStack) && mContainsTestCache.contains(new GT_ItemStack2(aStack));
    }

    public boolean containsUnCached(ItemStack aStack) {
        // In case someone needs this
        for (ItemStack tStack : mPrefixedItems) {
            if (GT_Utility.areStacksEqual(aStack, tStack, !tStack.hasTagCompound())) {
                return true;
            }
        }
        return false;
    }

    public boolean doGenerateItem(Materials aMaterial) {
        return aMaterial != null && aMaterial != Materials._NULL
            && ((aMaterial.mTypes & mMaterialGenerationBits) != 0 || mGeneratedItems.contains(aMaterial))
            && !mNotGeneratedItems.contains(aMaterial)
            && !mDisabledItems.contains(aMaterial)
            && (mCondition == null || mCondition.isTrue(aMaterial));
    }

    public boolean ignoreMaterials(Materials... aMaterials) {
        for (Materials tMaterial : aMaterials) if (tMaterial != null) mIgnoredMaterials.add(tMaterial);
        return true;
    }

    public boolean isIgnored(Materials aMaterial) {
        if (aMaterial != null && (!aMaterial.mUnificatable || aMaterial != aMaterial.mMaterialInto)) return true;
        return mIgnoredMaterials.contains(aMaterial);
    }

    public boolean addFamiliarPrefix(OrePrefixes aPrefix) {
        if (aPrefix == null || mFamiliarPrefixes.contains(aPrefix) || aPrefix == this) return false;
        return mFamiliarPrefixes.add(aPrefix);
    }

    public boolean add(IOreRecipeRegistrator aRegistrator) {
        if (aRegistrator == null) return false;
        return mOreProcessing.add(aRegistrator);
    }

    public void processOre(Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {

        if (aMaterial == null) {
            return;
        }

        if (aMaterial.contains(SubTag.NO_RECIPES)) {
            return;
        }

        if ((aMaterial != Materials._NULL || mIsSelfReferencing || !mIsMaterialBased)
            && GT_Utility.isStackValid(aStack)) {
            // if (Materials.mPreventableComponents.contains(this) && !this.mDynamicItems.contains(aMaterial)) return;
            for (IOreRecipeRegistrator tRegistrator : mOreProcessing) {
                if (D2) GT_Log.ore.println(
                    "Processing '" + aOreDictName
                        + "' with the Prefix '"
                        + name()
                        + "' and the Material '"
                        + aMaterial.mName
                        + "' at "
                        + GT_Utility.getClassName(tRegistrator));
                tRegistrator.registerOre(this, aMaterial, aOreDictName, aModName, GT_Utility.copyAmount(1, aStack));
            }
        }
    }

    public Object get(Object aMaterial) {
        if (aMaterial instanceof Materials) return new ItemData(this, (Materials) aMaterial);
        return name() + aMaterial;
    }

    public String getDefaultLocalNameForItem(Materials aMaterial) {
        return aMaterial.getDefaultLocalizedNameForItem(getDefaultLocalNameFormatForItem(aMaterial));
    }

    @SuppressWarnings("incomplete-switch")
    public String getDefaultLocalNameFormatForItem(Materials aMaterial) {
        // Certain Materials have slightly different Localizations.
        switch (this) {
            case crateGtDust -> {
                return mLocalizedMaterialPre + OrePrefixes.dust.getDefaultLocalNameFormatForItem(aMaterial);
            }
            case crateGtIngot -> {
                return mLocalizedMaterialPre + OrePrefixes.ingot.getDefaultLocalNameFormatForItem(aMaterial);
            }
            case crateGtGem -> {
                return mLocalizedMaterialPre + OrePrefixes.gem.getDefaultLocalNameFormatForItem(aMaterial);
            }
            case crateGtPlate -> {
                return mLocalizedMaterialPre + OrePrefixes.plate.getDefaultLocalNameFormatForItem(aMaterial);
            }
        }
        switch (aMaterial.mName) {
            case "Glass", "BorosilicateGlass" -> {
                if (name().startsWith("gem")) return mLocalizedMaterialPre + "%material" + " Crystal";
                if (name().startsWith("plate")) return mLocalizedMaterialPre + "%material" + " Pane";
                if (name().startsWith("ingot")) return mLocalizedMaterialPre + "%material" + " Bar";
                if (name().startsWith("nugget")) return mLocalizedMaterialPre + "%material" + " Chip";
            }
            case "Wheat" -> {
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "Flour";
            }
            case "Ice" -> {
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "Crushed Ice";
            }
            case "Wood", "WoodSealed" -> {
                if (name().startsWith("bolt")) return "Short " + "%material" + " Stick";
                if (name().startsWith("stick")) return mLocalizedMaterialPre + "%material" + " Stick";
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "%material" + " Pulp";
                if (name().startsWith("nugget")) return mLocalizedMaterialPre + "%material" + " Chip";
                if (name().startsWith("plate")) return mLocalizedMaterialPre + "%material" + " Plank";
            }
            case "Plastic", "Rubber", "Polyethylene", "Epoxid", "EpoxidFiberReinforced", "Polydimethylsiloxane", "Silicone", "Polysiloxane", "Polycaprolactam", "Polytetrafluoroethylene", "PolyvinylChloride", "Polystyrene", "StyreneButadieneRubber" -> {
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "%material" + " Pulp";
                if (name().startsWith("plate")) return mLocalizedMaterialPre + "%material" + " Sheet";
                if (name().startsWith("ingot")) return mLocalizedMaterialPre + "%material" + " Bar";
                if (name().startsWith("nugget")) return mLocalizedMaterialPre + "%material" + " Chip";
                if (name().startsWith("foil")) return "Thin " + "%material" + " Sheet";
            }
            case "FierySteel" -> {
                if (mIsContainer) return mLocalizedMaterialPre + "Fiery Blood" + mLocalizedMaterialPost;
            }
            case "Steeleaf" -> {
                if (name().startsWith("ingot")) return mLocalizedMaterialPre + "%material";
            }
            case "Bone" -> {
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "Bone Meal";
            }
            case "Blaze", "Milk", "Cocoa", "Chocolate", "Coffee", "Chili", "Cheese", "Snow" -> {
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "%material" + " Powder";
            }
            case "Paper" -> {
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "Chad";
                switch (this) {
                    case plate -> {
                        return "Sheet of Paper";
                    }
                    case plateDouble -> {
                        return "Paperboard";
                    }
                    case plateTriple -> {
                        return "Carton";
                    }
                    case plateQuadruple -> {
                        return "Cardboard";
                    }
                    case plateQuintuple -> {
                        return "Thick Cardboard";
                    }
                    case plateDense -> {
                        return "Strong Cardboard";
                    }
                }
            }
            case "MeatRaw" -> {
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "Mince Meat";
            }
            case "MeatCooked" -> {
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "Cooked Mince Meat";
            }
            case "Ash", "DarkAsh", "Gunpowder", "Sugar", "Salt", "RockSalt", "VolcanicAsh", "RareEarth" -> {
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "%material";
            }
            case "Vermiculite", "Bentonite", "Kaolinite", "Talc", "BasalticMineralSand", "GraniticMineralSand", "GlauconiteSand", "CassiteriteSand", "GarnetSand", "QuartzSand", "Pitchblende", "FullersEarth" -> {
                if (name().startsWith("dust")) return mLocalizedMaterialPre + "%material";
                switch (this) {
                    case crushedCentrifuged, crushedPurified -> {
                        return mLocalizedMaterialPre + "%material";
                    }
                    case crushed -> {
                        return "Ground " + "%material";
                    }
                }
            }
        }
        if (ProcessingModSupport.aEnableThaumcraftMats) {
            switch (aMaterial.mName) {
                case "InfusedAir", "InfusedDull", "InfusedEarth", "InfusedEntropy", "InfusedFire", "InfusedOrder", "InfusedVis", "InfusedWater" -> {
                    if (name().startsWith("gem")) return mLocalizedMaterialPre + "Shard of " + "%material";
                    if (name().startsWith("crystal")) return mLocalizedMaterialPre + "Shard of " + "%material";
                    if (name().startsWith("plate")) return mLocalizedMaterialPre + "%material" + " Crystal Plate";
                    if (name().startsWith("dust")) return mLocalizedMaterialPre + "%material" + " Crystal Powder";
                    switch (this) {
                        case crushedCentrifuged, crushedPurified, crushed -> {
                            return mLocalizedMaterialPre + "%material" + " Crystals";
                        }
                    }
                }
            }
        }
        // Use Standard Localization
        return mLocalizedMaterialPre + "%material" + mLocalizedMaterialPost;
    }
}
