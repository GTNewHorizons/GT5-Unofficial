package gregtech.api.enums;

import static gregtech.api.enums.GTValues.D2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.interfaces.ICondition;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.GTArrayList;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.loaders.materialprocessing.ProcessingModSupport;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OrePrefixes {

    /** Used for removed prefixes to prevent id shifts. */
    public static final OrePrefixes ___placeholder___ = new OrePrefixBuilder("___placeholder___").setDefaultLocalName("Placeholder").build();
    // ___placeholder___("Placeholder", "", "", false, false, false, false, false, false, false, false, false, false, 0, 0, 64, -1)

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreBlackgranite = new OrePrefixBuilder("oreBlackgranite").setDefaultLocalName("Black Granite Ores").setLocalMaterialPre("Granite ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreBlackgranite("Black Granite Ores", "Granite ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreRedgranite = new OrePrefixBuilder("oreRedgranite").setDefaultLocalName("Red Granite Ores").setLocalMaterialPre("Granite ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreRedgranite("Red Granite Ores", "Granite ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreMarble = new OrePrefixBuilder("oreMarble").setDefaultLocalName("Marble Ores").setLocalMaterialPre("Marble ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreMarble("Marble Ores", "Marble ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreBasalt = new OrePrefixBuilder("oreBasalt").setDefaultLocalName("Basalt Ores").setLocalMaterialPre("Basalt ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreBasalt("Basalt Ores", "Basalt ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** Prefix of the Nether-Ores Mod. Causes Ores to double. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreNetherrack = new OrePrefixBuilder("oreNetherrack").setDefaultLocalName("Netherrack Ores").setLocalMaterialPre("Nether ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreNetherrack("Netherrack Ores", "Nether ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** Prefix of the Nether-Ores Mod. Causes Ores to double. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreNether = new OrePrefixBuilder("oreNether").setDefaultLocalName("Nether Ores").setLocalMaterialPre("Nether ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreNether("Nether Ores", "Nether ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** Prefix of the Dense-Ores Mod. Causes Ores to double. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreDense = new OrePrefixBuilder("oreDense").setDefaultLocalName("Dense Ores").setLocalMaterialPre("Dense ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreDense("Dense Ores", "Dense ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** Prefix of TFC */
    public static final OrePrefixes oreRich = new OrePrefixBuilder("oreRich").setDefaultLocalName("Rich Ores").setLocalMaterialPre("Rich ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreRich("Rich Ores", "Rich ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** Prefix of TFC */
    public static final OrePrefixes oreNormal = new OrePrefixBuilder("oreNormal").setDefaultLocalName("Normal Ores").setLocalMaterialPre("Normal ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreNormal("Normal Ores", "Normal ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** Prefix of Railcraft. */
    public static final OrePrefixes oreSmall = new OrePrefixBuilder("oreSmall").setDefaultLocalName("Small Ores").setLocalMaterialPre("Small ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreSmall("Small Ores", "Small ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, 67)

    /** Prefix of Railcraft. */
    public static final OrePrefixes orePoor = new OrePrefixBuilder("orePoor").setDefaultLocalName("Poor Ores").setLocalMaterialPre("Poor ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // orePoor("Poor Ores", "Poor ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreEndstone = new OrePrefixBuilder("oreEndstone").setDefaultLocalName("Endstone Ores").setLocalMaterialPre("End ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreEndstone("Endstone Ores", "End ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreEnd = new OrePrefixBuilder("oreEnd").setDefaultLocalName("End Ores").setLocalMaterialPre("End ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // oreEnd("End Ores", "End ", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, -1)

    /** Regular Ore Prefix. Ore -> Material is a Oneway Operation! Introduced by Eloraam */
    public static final OrePrefixes ore = new OrePrefixBuilder("ore").setDefaultLocalName("Ores").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // ore("Ores", "", " Ore", true, true, false, false, false, true, false, false, false, true, B[3], -1, 64, 68)

    public static final OrePrefixes crushedCentrifuged = new OrePrefixBuilder("crushedCentrifuged").setDefaultLocalName("Centrifuged Ores").setLocalMaterialPre("Centrifuged ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().build();
    // crushedCentrifuged("Centrifuged Ores", "Centrifuged ", " Ore", true, true, false, false, false, false, false, true, false, true, B[3], -1, 64, 7)

    public static final OrePrefixes crushedPurified = new OrePrefixBuilder("crushedPurified").setDefaultLocalName("Purified Ores").setLocalMaterialPre("Purified ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().build();
    // crushedPurified("Purified Ores", "Purified ", " Ore", true, true, false, false, false, false, false, true, false, true, B[3], -1, 64, 6)

    public static final OrePrefixes crushed = new OrePrefixBuilder("crushed").setDefaultLocalName("Crushed Ores").setLocalMaterialPre("Crushed ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().build();
    // crushed("Crushed Ores", "Crushed ", " Ore", true, true, false, false, false, false, false, true, false, true, B[3], -1, 64, 5)

    public static final OrePrefixes rawOre = new OrePrefixBuilder("rawOre").setDefaultLocalName("Raw Ore").setLocalMaterialPre("Raw ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().build();
    // rawOre("Raw Ore", "Raw ", " Ore", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, 64)

    // Introduced by Mekanism
    public static final OrePrefixes shard = new OrePrefixBuilder("shard").setDefaultLocalName("Crystallised Shards").setUnifiable().setMaterialBased().build();
    // shard("Crystallised Shards", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, -1)

    public static final OrePrefixes clump = new OrePrefixBuilder("clump").setDefaultLocalName("Clumps").setUnifiable().setMaterialBased().build();
    // clump("Clumps", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, -1)

    public static final OrePrefixes reduced = new OrePrefixBuilder("reduced").setDefaultLocalName("Reduced Gravels").setUnifiable().setMaterialBased().build();
    // reduced("Reduced Gravels", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, -1)

    public static final OrePrefixes crystalline = new OrePrefixBuilder("crystalline").setDefaultLocalName("Crystallised Metals").setUnifiable().setMaterialBased().build();
    // crystalline("Crystallised Metals", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, -1)

    public static final OrePrefixes cleanGravel = new OrePrefixBuilder("cleanGravel").setDefaultLocalName("Clean Gravels").setUnifiable().setMaterialBased().build();
    // cleanGravel("Clean Gravels", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, -1)

    public static final OrePrefixes dirtyGravel = new OrePrefixBuilder("dirtyGravel").setDefaultLocalName("Dirty Gravels").setUnifiable().setMaterialBased().build();
    // dirtyGravel("Dirty Gravels", "", "", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, -1)

    /** A hot Ingot, which has to be cooled down by a Vacuum Freezer. */
    public static final OrePrefixes ingotHot = new OrePrefixBuilder("ingotHot").setDefaultLocalName("Hot Ingots").setLocalMaterialPre("Hot ").setLocalMaterialPost(" Ingot").setUnifiable().setMaterialBased().build();
    // ingotHot("Hot Ingots", "Hot ", " Ingot", true, true, false, false, false, false, false, true, false, false, B[1], M * 1, 64, 12)

    /** A regular Ingot. Introduced by Eloraam */
    public static final OrePrefixes ingot = new OrePrefixBuilder("ingot").setDefaultLocalName("Ingots").setLocalMaterialPost(" Ingot").setUnifiable().setMaterialBased().build();
    // ingot("Ingots", "", " Ingot", true, true, false, false, false, false, false, true, false, false, B[1], M * 1, 64, 11)

    /** A regular Gem worth one small Dust. Introduced by TerraFirmaCraft */
    public static final OrePrefixes gemChipped = new OrePrefixBuilder("gemChipped").setDefaultLocalName("Chipped Gemstones").setLocalMaterialPre("Chipped ").setUnifiable().setMaterialBased().setSelfReferencing().build();
    // gemChipped("Chipped Gemstones", "Chipped ", "", true, true, true, false, false, false, true, true, false, false, B[2], M / 4, 64, 59)

    /** A regular Gem worth two small Dusts. Introduced by TerraFirmaCraft */
    public static final OrePrefixes gemFlawed = new OrePrefixBuilder("gemFlawed").setDefaultLocalName("Flawed Gemstones").setLocalMaterialPre("Flawed ").setUnifiable().setMaterialBased().setSelfReferencing().build();
    // gemFlawed("Flawed Gemstones", "Flawed ", "", true, true, true, false, false, false, true, true, false, false, B[2], M / 2, 64, 60)

    /** A regular Gem worth two Dusts. Introduced by TerraFirmaCraft */
    public static final OrePrefixes gemFlawless = new OrePrefixBuilder("gemFlawless").setDefaultLocalName("Flawless Gemstones").setLocalMaterialPre("Flawless ").setUnifiable().setMaterialBased().setSelfReferencing().build();
    // gemFlawless("Flawless Gemstones", "Flawless ", "", true, true, true, false, false, false, true, true, false, false, B[2], M * 2, 64, 61)

    /** A regular Gem worth four Dusts. Introduced by TerraFirmaCraft */
    public static final OrePrefixes gemExquisite = new OrePrefixBuilder("gemExquisite").setDefaultLocalName("Exquisite Gemstones").setLocalMaterialPre("Exquisite ").setUnifiable().setMaterialBased().setSelfReferencing().build();
    // gemExquisite("Exquisite Gemstones", "Exquisite ", "", true, true, true, false, false, false, true, true, false, false, B[2], M * 4, 64, 62)

    /** A regular Gem worth one Dust. Introduced by Eloraam */
    public static final OrePrefixes gem = new OrePrefixBuilder("gem").setDefaultLocalName("Gemstones").setUnifiable().setMaterialBased().setSelfReferencing().build();
    // gem("Gemstones", "", "", true, true, true, false, false, false, true, true, false, false, B[2], M * 1, 64, 8)

    /** 1/9th of a Dust. */
    public static final OrePrefixes dustTiny = new OrePrefixBuilder("dustTiny").setDefaultLocalName("Tiny Dusts").setLocalMaterialPre("Tiny Pile of ").setLocalMaterialPost(" Dust").setUnifiable().setMaterialBased().build();
    // dustTiny("Tiny Dusts", "Tiny Pile of ", " Dust", true, true, false, false, false, false, false, true, false, false, B[0] | B[1] | B[2] | B[3], M / 9, 64, 0)

    /** 1/4th of a Dust. */
    public static final OrePrefixes dustSmall = new OrePrefixBuilder("dustSmall").setDefaultLocalName("Small Dusts").setLocalMaterialPre("Small Pile of ").setLocalMaterialPost(" Dust").setUnifiable().setMaterialBased().build();
    // dustSmall("Small Dusts", "Small Pile of ", " Dust", true, true, false, false, false, false, false, true, false, false, B[0] | B[1] | B[2] | B[3], M / 4, 64, 1)

    /** Dust with impurities. 1 Unit of Main Material and 1/9 - 1/4 Unit of secondary Material */
    public static final OrePrefixes dustImpure = new OrePrefixBuilder("dustImpure").setDefaultLocalName("Impure Dusts").setLocalMaterialPre("Impure Pile of ").setLocalMaterialPost(" Dust").setUnifiable().setMaterialBased().build();
    // dustImpure("Impure Dusts", "Impure Pile of ", " Dust", true, true, false, false, false, false, false, true, false, true, B[3], M * 1, 64, 3)

    public static final OrePrefixes dustRefined = new OrePrefixBuilder("dustRefined").setDefaultLocalName("Refined Dusts").setLocalMaterialPre("Refined Pile of ").setLocalMaterialPost(" Dust").setUnifiable().setMaterialBased().build();
    // dustRefined("Refined Dusts", "Refined Pile of ", " Dust", true, true, false, false, false, false, false, true, false, true, B[3], M * 1, 64, 2)

    public static final OrePrefixes dustPure = new OrePrefixBuilder("dustPure").setDefaultLocalName("Purified Dusts").setLocalMaterialPre("Purified Pile of ").setLocalMaterialPost(" Dust").setUnifiable().setMaterialBased().build();
    // dustPure("Purified Dusts", "Purified Pile of ", " Dust", true, true, false, false, false, false, false, true, false, true, B[3], M * 1, 64, 4)

    /** Pure Dust worth of one Ingot or Gem. Introduced by Alblaka. */
    public static final OrePrefixes dust = new OrePrefixBuilder("dust").setDefaultLocalName("Dusts").setLocalMaterialPost(" Dust").setUnifiable().setMaterialBased().build();
    // dust("Dusts", "", " Dust", true, true, false, false, false, false, false, true, false, false, B[0] | B[1] | B[2] | B[3], M * 1, 64, 2)

    /** A Nugget. Introduced by Eloraam */
    public static final OrePrefixes nugget = new OrePrefixBuilder("nugget").setDefaultLocalName("Nuggets").setLocalMaterialPost(" Nugget").setUnifiable().setMaterialBased().build();
    // nugget("Nuggets", "", " Nugget", true, true, false, false, false, false, false, true, false, false, B[1], M / 9, 64, 9)

    /** Special Alloys have this prefix. */
    public static final OrePrefixes plateAlloy = new OrePrefixBuilder("plateAlloy").setDefaultLocalName("Alloy Plates").setUnifiable().build();
    // plateAlloy("Alloy Plates", "", "", true, false, false, false, false, false, false, false, false, false, B[1], -1, 64, 17)

    public static final OrePrefixes plateSteamcraft = new OrePrefixBuilder("plateSteamcraft").setDefaultLocalName("Steamcraft Plates").build();
    // plateSteamcraft("Steamcraft Plates", "", "", false, false, false, false, false, false, false, false, false, false, B[1], -1, 64, 17)

    /** 9 Plates combined in one Item. */
    public static final OrePrefixes plateDense = new OrePrefixBuilder("plateDense").setDefaultLocalName("Dense Plates").setLocalMaterialPre("Dense ").setLocalMaterialPost(" Plate").setUnifiable().setMaterialBased().build();
    // plateDense("Dense Plates", "Dense ", " Plate", true, true, false, false, false, false, true, true, false, false, B[1], M * 9, 64, 22)

    public static final OrePrefixes plateSuperdense = new OrePrefixBuilder("plateSuperdense").setDefaultLocalName("Superdense Plates").setLocalMaterialPre("Superdense ").setLocalMaterialPost(" Plate").setUnifiable().setMaterialBased().build();
    // plateSuperdense("Superdense Plates", "Superdense ", " Plate", true, true, false, false, false, false, true, true, false, false, B[1], M * 64, 64, 125)

    public static final OrePrefixes plateQuintuple = new OrePrefixBuilder("plateQuintuple").setDefaultLocalName("5x Plates").setLocalMaterialPre("Quintuple ").setLocalMaterialPost(" Plate").setUnifiable().setMaterialBased().build();
    // plateQuintuple("5x Plates", "Quintuple ", " Plate", true, true, false, false, false, false, true, true, false, false, B[1], M * 5, 64, 21)

    public static final OrePrefixes plateQuadruple = new OrePrefixBuilder("plateQuadruple").setDefaultLocalName("4x Plates").setLocalMaterialPre("Quadruple ").setLocalMaterialPost(" Plate").setUnifiable().setMaterialBased().build();
    // plateQuadruple("4x Plates", "Quadruple ", " Plate", true, true, false, false, false, false, true, true, false, false, B[1], M * 4, 64, 20)

    public static final OrePrefixes plateTriple = new OrePrefixBuilder("plateTriple").setDefaultLocalName("3x Plates").setLocalMaterialPre("Triple ").setLocalMaterialPost(" Plate").setUnifiable().setMaterialBased().build();
    // plateTriple("3x Plates", "Triple ", " Plate", true, true, false, false, false, false, true, true, false, false, B[1], M * 3, 64, 19)

    public static final OrePrefixes plateDouble = new OrePrefixBuilder("plateDouble").setDefaultLocalName("2x Plates").setLocalMaterialPre("Double ").setLocalMaterialPost(" Plate").setUnifiable().setMaterialBased().build();
    // plateDouble("2x Plates", "Double ", " Plate", true, true, false, false, false, false, true, true, false, false, B[1], M * 2, 64, 18)

    public static final OrePrefixes plate = new OrePrefixBuilder("plate").setDefaultLocalName("Plates").setLocalMaterialPost(" Plate").setUnifiable().setMaterialBased().build();
    // plate("Plates", "", " Plate", true, true, false, false, false, false, true, true, false, false, B[1] | B[2], M * 1, 64, 17)

    /** Casing made of 1/2 Ingot/Dust */
    public static final OrePrefixes itemCasing = new OrePrefixBuilder("itemCasing").setDefaultLocalName("Casings").setLocalMaterialPost(" Casing").setUnifiable().setMaterialBased().build();
    // itemCasing("Casings", "", " Casing", true, true, false, false, false, false, true, true, false, false, B[1] | B[2], M / 2, 64, 10)

    /** Foil made of 1/4 Ingot/Dust. */
    public static final OrePrefixes foil = new OrePrefixBuilder("foil").setDefaultLocalName("Foils").setLocalMaterialPost(" Foil").setUnifiable().setMaterialBased().build();
    // foil("Foils", "", " Foil", true, true, false, false, false, false, true, true, false, false, B[1], M / 4, 64, 29)

    /** Stick made of an Ingot. */
    public static final OrePrefixes stickLong = new OrePrefixBuilder("stickLong").setDefaultLocalName("Long Sticks/Rods").setLocalMaterialPre("Long ").setLocalMaterialPost(" Rod").setUnifiable().setMaterialBased().build();
    // stickLong("Long Sticks/Rods", "Long ", " Rod", true, true, false, false, false, false, true, true, false, false, B[1] | B[2], M * 1, 64, 54)

    /** Stick made of half an Ingot. Introduced by Eloraam */
    public static final OrePrefixes stick = new OrePrefixBuilder("stick").setDefaultLocalName("Sticks/Rods").setLocalMaterialPost(" Rod").setUnifiable().setMaterialBased().build();
    // stick("Sticks/Rods", "", " Rod", true, true, false, false, false, false, true, true, false, false, B[1] | B[2], M / 2, 64, 23)

    /** consisting out of one Nugget. */
    public static final OrePrefixes round = new OrePrefixBuilder("round").setDefaultLocalName("Rounds").setLocalMaterialPost(" Round").setUnifiable().setMaterialBased().build();
    // round("Rounds", "", " Round", true, true, false, false, false, false, true, true, false, false, B[1], M / 9, 64, 25)

    /** consisting out of 1/8 Ingot or 1/4 Stick. */
    public static final OrePrefixes bolt = new OrePrefixBuilder("bolt").setDefaultLocalName("Bolts").setLocalMaterialPost(" Bolt").setUnifiable().setMaterialBased().build();
    // bolt("Bolts", "", " Bolt", true, true, false, false, false, false, true, true, false, false, B[1] | B[2], M / 8, 64, 26)

    /** contain dusts */
    public static final OrePrefixes comb = new OrePrefixBuilder("comb").setDefaultLocalName("Combs").setLocalMaterialPost(" Comb").build();
    // comb("Combs", "", " Comb", false, false, false, false, false, false, false, true, false, false, B[1] | B[2], M, 64, 101)

    /** consisting out of a Bolt. */
    public static final OrePrefixes screw = new OrePrefixBuilder("screw").setDefaultLocalName("Screws").setLocalMaterialPost(" Screw").setUnifiable().setMaterialBased().build();
    // screw("Screws", "", " Screw", true, true, false, false, false, false, true, true, false, false, B[1] | B[2], M / 8, 64, 27)

    /** consisting out of 1/2 Stick. */
    public static final OrePrefixes ring = new OrePrefixBuilder("ring").setDefaultLocalName("Rings").setLocalMaterialPost(" Ring").setUnifiable().setMaterialBased().build();
    // ring("Rings", "", " Ring", true, true, false, false, false, false, true, true, false, false, B[1], M / 4, 64, 28)

    /** consisting out of 1 Fine Wire. */
    public static final OrePrefixes springSmall = new OrePrefixBuilder("springSmall").setDefaultLocalName("Small Springs").setLocalMaterialPre("Small ").setLocalMaterialPost(" Spring").setUnifiable().setMaterialBased().build();
    // springSmall("Small Springs", "Small ", " Spring", true, true, false, false, false, false, true, true, false, false, B[1], M / 4, 64, 55)

    /** consisting out of 2 Sticks. */
    public static final OrePrefixes spring = new OrePrefixBuilder("spring").setDefaultLocalName("Springs").setLocalMaterialPost(" Spring").setUnifiable().setMaterialBased().build();
    // spring("Springs", "", " Spring", true, true, false, false, false, false, true, true, false, false, B[1], M * 1, 64, 56)

    /** consisting out of 1/8 Ingot or 1/4 Wire. */
    public static final OrePrefixes wireFine = new OrePrefixBuilder("wireFine").setDefaultLocalName("Fine Wires").setLocalMaterialPre("Fine ").setLocalMaterialPost(" Wire").setUnifiable().setMaterialBased().build();
    // wireFine("Fine Wires", "Fine ", " Wire", true, true, false, false, false, false, true, true, false, false, B[1], M / 8, 64, 51)

    /** consisting out of 4 Plates, 1 Ring and 1 Screw. */
    public static final OrePrefixes Rotor = new OrePrefixBuilder("Rotor").setDefaultLocalName("Rotors").setLocalMaterialPost(" Rotor").setUnifiable().setMaterialBased().build();
    // Rotor("Rotors", "", " Rotor", true, true, false, false, false, false, true, true, false, false, B[7], M * 4 + M / 4, 64, 53)

    public static final OrePrefixes gearGtSmall = new OrePrefixBuilder("gearGtSmall").setDefaultLocalName("Small Gears").setLocalMaterialPre("Small ").setLocalMaterialPost(" Gear").setUnifiable().setMaterialBased().build();
    // gearGtSmall("Small Gears", "Small ", " Gear", true, true, false, false, false, false, true, true, false, false, B[7], M * 1, 64, 52)

    /** Introduced by me because BuildCraft has ruined the gear Prefix... */
    public static final OrePrefixes gearGt = new OrePrefixBuilder("gearGt").setDefaultLocalName("Gears").setLocalMaterialPost(" Gear").setUnifiable().setMaterialBased().build();
    // gearGt("Gears", "", " Gear", true, true, false, false, false, false, true, true, false, false, B[7], M * 4, 64, 63)

    /** 3/4 of a Plate or Gem used to shape a Lense. Normally only used on Transparent Materials. */
    public static final OrePrefixes lens = new OrePrefixBuilder("lens").setDefaultLocalName("Lenses").setLocalMaterialPost(" Lens").setUnifiable().setMaterialBased().build();
    // lens("Lenses", "", " Lens", true, true, false, false, false, false, true, true, false, false, B[2], (M * 3) / 4, 64, 24)

    /** Hot Cell full of Plasma, which can be used in the Plasma Generator. */
    public static final OrePrefixes cellPlasma = new OrePrefixBuilder("cellPlasma").setDefaultLocalName("Cells of Plasma").setLocalMaterialPost(" Plasma Cell").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // cellPlasma("Cells of Plasma", "", " Plasma Cell", true, true, true, true, false, false, false, true, false, false, B[5], M * 1, 64, 31)

    /** Hot Cell full of molten stuff, which can be used in the Plasma Generator. */
    public static final OrePrefixes cellMolten = new OrePrefixBuilder("cellMolten").setDefaultLocalName("Cells of Molten stuff").setLocalMaterialPre("Molten ").setLocalMaterialPost(" Cell").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // cellMolten("Cells of Molten stuff", "Molten ", " Cell", true, true, true, true, false, false, false, true, false, false, 0, M * 1, 64, 31)

    public static final OrePrefixes cell = new OrePrefixBuilder("cell").setDefaultLocalName("Cells").setLocalMaterialPost(" Cell").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // cell("Cells", "", " Cell", true, true, true, true, false, false, true, true, false, false, B[4] | B[8], M * 1, 64, 30)

    /** A vanilla Iron Bucket filled with the Material. */
    public static final OrePrefixes bucket = new OrePrefixBuilder("bucket").setDefaultLocalName("Buckets").setLocalMaterialPost(" Bucket").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // bucket("Buckets", "", " Bucket", true, true, true, true, false, false, true, false, false, false, B[4] | B[8], M * 1, 64, -1)

    /** An Iguana Tweaks Clay Bucket filled with the Material. */
    public static final OrePrefixes bucketClay = new OrePrefixBuilder("bucketClay").setDefaultLocalName("Clay Buckets").setLocalMaterialPost(" Clay Bucket").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // bucketClay("Clay Buckets", "", " Clay Bucket", true, true, true, true, false, false, true, false, false, false, B[4] | B[8], M * 1, 64, -1)

    /** Glass Bottle containing a Fluid. */
    public static final OrePrefixes bottle = new OrePrefixBuilder("bottle").setDefaultLocalName("Bottles").setLocalMaterialPost(" Bottle").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // bottle("Bottles", "", " Bottle", true, true, true, true, false, false, false, false, false, false, B[4] | B[8], -1, 64, -1)

    public static final OrePrefixes capsule = new OrePrefixBuilder("capsule").setDefaultLocalName("Capsules").setLocalMaterialPost(" Capsule").setSelfReferencing().setContainer().build();
    // capsule("Capsules", "", " Capsule", false, true, true, true, false, false, false, false, false, false, B[4] | B[8], M * 1, 64, -1)

    public static final OrePrefixes crystal = new OrePrefixBuilder("crystal").setDefaultLocalName("Crystals").setLocalMaterialPost(" Crystal").build();
    // crystal("Crystals", "", " Crystal", false, true, false, false, false, false, true, false, false, false, B[2], M * 1, 64, -1)

    public static final OrePrefixes bulletGtSmall = new OrePrefixBuilder("bulletGtSmall").setDefaultLocalName("Small Bullets").setLocalMaterialPre("Small ").setLocalMaterialPost(" Bullet").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // bulletGtSmall("Small Bullets", "Small ", " Bullet", true, true, false, false, true, false, true, false, true, false, B[6] | B[8], M / 9, 64, -1)

    public static final OrePrefixes bulletGtMedium = new OrePrefixBuilder("bulletGtMedium").setDefaultLocalName("Medium Bullets").setLocalMaterialPre("Medium ").setLocalMaterialPost(" Bullet").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // bulletGtMedium("Medium Bullets", "Medium ", " Bullet", true, true, false, false, true, false, true, false, true, false, B[6] | B[8], M / 6, 64, -1)

    public static final OrePrefixes bulletGtLarge = new OrePrefixBuilder("bulletGtLarge").setDefaultLocalName("Large Bullets").setLocalMaterialPre("Large ").setLocalMaterialPost(" Bullet").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // bulletGtLarge("Large Bullets", "Large ", " Bullet", true, true, false, false, true, false, true, false, true, false, B[6] | B[8], M / 3, 64, -1)

    /** consisting out of 2 Ingots. */
    public static final OrePrefixes toolHeadFile = new OrePrefixBuilder("toolHeadFile").setDefaultLocalName("File Heads").setLocalMaterialPost(" File Head").setUnifiable().setMaterialBased().build();
    // toolHeadFile("File Heads", "", " File Head", true, true, false, false, false, false, true, true, false, false, B[6], M * 2, 64, 38)

    /** consisting out of 6 Ingots. */
    public static final OrePrefixes toolHeadHammer = new OrePrefixBuilder("toolHeadHammer").setDefaultLocalName("Hammer Heads").setLocalMaterialPost(" Hammer Head").setUnifiable().setMaterialBased().build();
    // toolHeadHammer("Hammer Heads", "", " Hammer Head", true, true, false, false, false, false, true, true, false, false, B[6], M * 6, 64, 37)

    /** consisting out of 2 Ingots. */
    public static final OrePrefixes toolHeadSaw = new OrePrefixBuilder("toolHeadSaw").setDefaultLocalName("Saw Blades").setLocalMaterialPost(" Saw Blade").setUnifiable().setMaterialBased().build();
    // toolHeadSaw("Saw Blades", "", " Saw Blade", true, true, false, false, false, false, true, true, false, false, B[6], M * 2, 64, 39)

    /** consisting out of 4 Ingots. */
    public static final OrePrefixes toolHeadBuzzSaw = new OrePrefixBuilder("toolHeadBuzzSaw").setDefaultLocalName("Buzzsaw Blades").setLocalMaterialPost(" Buzzsaw Blade").setUnifiable().setMaterialBased().build();
    // toolHeadBuzzSaw("Buzzsaw Blades", "", " Buzzsaw Blade", true, true, false, false, false, false, true, true, false, false, B[6], M * 4, 64, 48)

    /** consisting out of 1 Ingots. */
    public static final OrePrefixes toolHeadScrewdriver = new OrePrefixBuilder("toolHeadScrewdriver").setDefaultLocalName("Screwdriver Tips").setLocalMaterialPost(" Screwdriver Tip").setUnifiable().setMaterialBased().build();
    // toolHeadScrewdriver("Screwdriver Tips", "", " Screwdriver Tip", true, true, false, false, false, false, true, false, false, false, B[6], M * 1, 64, 47)

    /** consisting out of 4 Ingots. */
    public static final OrePrefixes toolHeadDrill = new OrePrefixBuilder("toolHeadDrill").setDefaultLocalName("Drill Tips").setLocalMaterialPost(" Drill Tip").setUnifiable().setMaterialBased().build();
    // toolHeadDrill("Drill Tips", "", " Drill Tip", true, true, false, false, false, false, true, true, false, false, B[6], M * 4, 64, 40)

    /** consisting out of 2 Ingots. */
    public static final OrePrefixes toolHeadChainsaw = new OrePrefixBuilder("toolHeadChainsaw").setDefaultLocalName("Chainsaw Tips").setLocalMaterialPost(" Chainsaw Tip").setUnifiable().setMaterialBased().build();
    // toolHeadChainsaw("Chainsaw Tips", "", " Chainsaw Tip", true, true, false, false, false, false, true, true, false, false, B[6], M * 2, 64, 41)

    /** consisting out of 4 Ingots. */
    public static final OrePrefixes toolHeadWrench = new OrePrefixBuilder("toolHeadWrench").setDefaultLocalName("Wrench Tips").setLocalMaterialPost(" Wrench Tip").setUnifiable().setMaterialBased().build();
    // toolHeadWrench("Wrench Tips", "", " Wrench Tip", true, true, false, false, false, false, true, true, false, false, B[6], M * 4, 64, 42)

    /** consisting out of 6 Ingots. */
    public static final OrePrefixes turbineBlade = new OrePrefixBuilder("turbineBlade").setDefaultLocalName("Turbine Blades").setLocalMaterialPost(" Turbine Blade").setUnifiable().setMaterialBased().build();
    // turbineBlade("Turbine Blades", "", " Turbine Blade", true, true, false, false, false, false, true, true, false, false, B[6], M * 6, 64, 100)

    /** vanilly Sword */
    public static final OrePrefixes toolSword = new OrePrefixBuilder("toolSword").setDefaultLocalName("Swords").build();
    // toolSword("Swords", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 2, 1, -1)

    /** vanilly Pickaxe */
    public static final OrePrefixes toolPickaxe = new OrePrefixBuilder("toolPickaxe").setDefaultLocalName("Pickaxes").build();
    // toolPickaxe("Pickaxes", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 3, 1, -1)

    /** vanilly Shovel */
    public static final OrePrefixes toolShovel = new OrePrefixBuilder("toolShovel").setDefaultLocalName("Shovels").build();
    // toolShovel("Shovels", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 1, 1, -1)

    /** vanilly Axe */
    public static final OrePrefixes toolAxe = new OrePrefixBuilder("toolAxe").setDefaultLocalName("Axes").build();
    // toolAxe("Axes", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 3, 1, -1)

    /** vanilly Hoe */
    public static final OrePrefixes toolHoe = new OrePrefixBuilder("toolHoe").setDefaultLocalName("Hoes").build();
    // toolHoe("Hoes", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 2, 1, -1)

    /** vanilly Shears */
    public static final OrePrefixes toolShears = new OrePrefixBuilder("toolShears").setDefaultLocalName("Shears").build();
    // toolShears("Shears", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 2, 1, -1)

    /** toolPot, toolSkillet, toolSaucepan, toolBakeware, toolCuttingboard, toolMortarandpestle, toolMixingbowl, toolJuicer */
    public static final OrePrefixes tool = new OrePrefixBuilder("tool").setDefaultLocalName("Tools").build();
    // tool("Tools", "", "", false, false, false, false, false, false, false, false, true, false, B[6], -1, 1, -1)

    public static final OrePrefixes compressedCobblestone = new OrePrefixBuilder("compressedCobblestone").setDefaultLocalName("9^X Compressed Cobblestones").build();
    // compressedCobblestone("9^X Compressed Cobblestones", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes compressedStone = new OrePrefixBuilder("compressedStone").setDefaultLocalName("9^X Compressed Stones").build();
    // compressedStone("9^X Compressed Stones", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes compressedDirt = new OrePrefixBuilder("compressedDirt").setDefaultLocalName("9^X Compressed Dirt").build();
    // compressedDirt("9^X Compressed Dirt", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes compressedGravel = new OrePrefixBuilder("compressedGravel").setDefaultLocalName("9^X Compressed Gravel").build();
    // compressedGravel("9^X Compressed Gravel", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes compressedSand = new OrePrefixBuilder("compressedSand").setDefaultLocalName("9^X Compressed Sand").build();
    // compressedSand("9^X Compressed Sand", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Compressed Material, worth 1 Unit. Introduced by Galacticraft */
    public static final OrePrefixes compressed = new OrePrefixBuilder("compressed").setDefaultLocalName("Compressed Materials").setLocalMaterialPre("Compressed ").setUnifiable().setMaterialBased().build();
    // compressed("Compressed Materials", "Compressed ", "", true, true, false, false, false, false, true, false, false, false, 0, M * 3, 64, -1)

    public static final OrePrefixes glass = new OrePrefixBuilder("glass").setDefaultLocalName("Glasses").setSelfReferencing().setDoNotUnifyActively().build();
    // glass("Glasses", "", "", false, false, true, false, true, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes paneGlass = new OrePrefixBuilder("paneGlass").setDefaultLocalName("Glass Panes").setSelfReferencing().setUsedForBlocks().build();
    // paneGlass("Glass Panes", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes blockGlass = new OrePrefixBuilder("blockGlass").setDefaultLocalName("Glass Blocks").setSelfReferencing().setUsedForBlocks().build();
    // blockGlass("Glass Blocks", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64,-1)

    public static final OrePrefixes blockWool = new OrePrefixBuilder("blockWool").setDefaultLocalName("Wool Blocks").setSelfReferencing().setUsedForBlocks().build();
    // blockWool("Wool Blocks", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** IGNORE */
    public static final OrePrefixes block_ = new OrePrefixBuilder("block_").setDefaultLocalName("Random Blocks").setUsedForBlocks().build();
    // block_("Random Blocks", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Storage Block consisting out of 9 Ingots/Gems/Dusts. Introduced by CovertJaguar */
    public static final OrePrefixes block = new OrePrefixBuilder("block").setDefaultLocalName("Storage Blocks").setLocalMaterialPre("Block of ").setUnifiable().setMaterialBased().setUsedForBlocks().build();
    // block("Storage Blocks", "Block of ", "", true, true, false, false, false, true, true, false, false, false, 0, M * 9, 64, 71)

    /** Special Prefix used mainly for the Crafting Handler. */
    public static final OrePrefixes craftingTool = new OrePrefixBuilder("craftingTool").setDefaultLocalName("Crafting Tools").build();
    // craftingTool("Crafting Tools", "", "", false, false, false, false, false, false, false, false, true, false, 0, -1, 64, -1)

    /** Special Prefix used mainly for the Crafting Handler. */
    public static final OrePrefixes crafting = new OrePrefixBuilder("crafting").setDefaultLocalName("Crafting Ingredients").build();
    // crafting("Crafting Ingredients", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Special Prefix used mainly for the Crafting Handler. */
    public static final OrePrefixes craft = new OrePrefixBuilder("craft").setDefaultLocalName("Crafting Stuff?").build();
    // craft("Crafting Stuff?", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Prefix used for Logs. Usually as "logWood". Introduced by Eloraam */
    public static final OrePrefixes log = new OrePrefixBuilder("log").setDefaultLocalName("Logs").setUsedForBlocks().build();
    // log("Logs", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Prefix used for Slabs. Usually as "slabWood" or "slabStone". Introduced by SirSengir */
    public static final OrePrefixes slab = new OrePrefixBuilder("slab").setDefaultLocalName("Slabs").setUsedForBlocks().build();
    // slab("Slabs", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Prefix used for Stairs. Usually as "stairWood" or "stairStone". Introduced by SirSengir */
    public static final OrePrefixes stair = new OrePrefixBuilder("stair").setDefaultLocalName("Stairs").setUsedForBlocks().build();
    // stair("Stairs", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Prefix used for Fences. Usually as "fenceWood". Introduced by Forge */
    public static final OrePrefixes fence = new OrePrefixBuilder("fence").setDefaultLocalName("Fences").build();
    // fence("Fences", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Prefix for Planks. Usually "plankWood". Introduced by Eloraam */
    public static final OrePrefixes plank = new OrePrefixBuilder("plank").setDefaultLocalName("Planks").setUsedForBlocks().build();
    // plank("Planks", "", "", false, false, false, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Prefix for Saplings. */
    public static final OrePrefixes treeSapling = new OrePrefixBuilder("treeSapling").setDefaultLocalName("Saplings").setSelfReferencing().setUsedForBlocks().build();
    // treeSapling("Saplings", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Prefix for Leaves. */
    public static final OrePrefixes treeLeaves = new OrePrefixBuilder("treeLeaves").setDefaultLocalName("Leaves").setSelfReferencing().setUsedForBlocks().build();
    // treeLeaves("Leaves", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Prefix for Tree Parts. */
    public static final OrePrefixes tree = new OrePrefixBuilder("tree").setDefaultLocalName("Tree Parts").build();
    // tree("Tree Parts", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Cobblestone Prefix for all Cobblestones. */
    public static final OrePrefixes stoneCobble = new OrePrefixBuilder("stoneCobble").setDefaultLocalName("Cobblestones").setSelfReferencing().setUsedForBlocks().build();
    // stoneCobble("Cobblestones", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Smoothstone Prefix. */
    public static final OrePrefixes stoneSmooth = new OrePrefixBuilder("stoneSmooth").setDefaultLocalName("Smoothstones").setSelfReferencing().setUsedForBlocks().build();
    // stoneSmooth("Smoothstones", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Mossy Stone Bricks. */
    public static final OrePrefixes stoneMossyBricks = new OrePrefixBuilder("stoneMossyBricks").setDefaultLocalName("mossy Stone Bricks").setSelfReferencing().setUsedForBlocks().build();
    // stoneMossyBricks("mossy Stone Bricks", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Mossy Cobble. */
    public static final OrePrefixes stoneMossy = new OrePrefixBuilder("stoneMossy").setDefaultLocalName("Mossy Stones").setSelfReferencing().setUsedForBlocks().build();
    // stoneMossy("Mossy Stones", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Stone Bricks. */
    public static final OrePrefixes stoneBricks = new OrePrefixBuilder("stoneBricks").setDefaultLocalName("Stone Bricks").setSelfReferencing().setUsedForBlocks().build();
    // stoneBricks("Stone Bricks", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Cracked Bricks. */
    public static final OrePrefixes stoneCracked = new OrePrefixBuilder("stoneCracked").setDefaultLocalName("Cracked Stones").setSelfReferencing().setUsedForBlocks().build();
    // stoneCracked("Cracked Stones", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Chiseled Stone. */
    public static final OrePrefixes stoneChiseled = new OrePrefixBuilder("stoneChiseled").setDefaultLocalName("Chiseled Stones").setSelfReferencing().setUsedForBlocks().build();
    // stoneChiseled("Chiseled Stones", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Prefix to determine which kind of Rock this is. */
    public static final OrePrefixes stone = new OrePrefixBuilder("stone").setDefaultLocalName("Stones").setSelfReferencing().setDoNotUnifyActively().setUsedForBlocks().build();
    // stone("Stones", "", "", false, true, true, false, true, true, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes cobblestone = new OrePrefixBuilder("cobblestone").setDefaultLocalName("Cobblestones").setSelfReferencing().setUsedForBlocks().build();
    // cobblestone("Cobblestones", "", "", false, true, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** Prefix to determine which kind of Rock this is. */
    public static final OrePrefixes rock = new OrePrefixBuilder("rock").setDefaultLocalName("Rocks").setSelfReferencing().setDoNotUnifyActively().setUsedForBlocks().build();
    // rock("Rocks", "", "", false, true, true, false, true, true, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes record = new OrePrefixBuilder("record").setDefaultLocalName("Records").setSelfReferencing().build();
    // record("Records", "", "", false, false, true, false, false, false, false, false, false, false, 0, -1, 1, -1)

    public static final OrePrefixes rubble = new OrePrefixBuilder("rubble").setDefaultLocalName("Rubbles").setUnifiable().setMaterialBased().setSelfReferencing().build();
    // rubble("Rubbles", "", "", true, true, true, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes scraps = new OrePrefixBuilder("scraps").setDefaultLocalName("Scraps").setUnifiable().setMaterialBased().build();
    // scraps("Scraps", "", "", true, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes scrap = new OrePrefixBuilder("scrap").setDefaultLocalName("Scraps").build();
    // scrap("Scraps", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** IGNORE */
    public static final OrePrefixes item_ = new OrePrefixBuilder("item_").setDefaultLocalName("Items").build();
    // item_("Items", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Random Item. Introduced by Alblaka */
    public static final OrePrefixes item = new OrePrefixBuilder("item").setDefaultLocalName("Items").build();
    // item("Items", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Used for Books of any kind. */
    public static final OrePrefixes book = new OrePrefixBuilder("book").setDefaultLocalName("Books").build();
    // book("Books", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Used for Papers of any kind. */
    public static final OrePrefixes paper = new OrePrefixBuilder("paper").setDefaultLocalName("Papers").build();
    // paper("Papers", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Used for the 16 dyes. Introduced by Eloraam */
    public static final OrePrefixes dye = new OrePrefixBuilder("dye").setDefaultLocalName("Dyes").setSelfReferencing().build();
    // dye("Dyes", "", "", false, false, true, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Used for the 16 colors of Stained Clay. Introduced by Forge */
    public static final OrePrefixes stainedClay = new OrePrefixBuilder("stainedClay").setDefaultLocalName("Stained Clays").setSelfReferencing().setUsedForBlocks().build();
    // stainedClay("Stained Clays", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    /** vanilly Helmet */
    public static final OrePrefixes armorHelmet = new OrePrefixBuilder("armorHelmet").setDefaultLocalName("Helmets").build();
    // armorHelmet("Helmets", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 5, 1, -1)

    /** vanilly Chestplate */
    public static final OrePrefixes armorChestplate = new OrePrefixBuilder("armorChestplate").setDefaultLocalName("Chestplates").build();
    // armorChestplate("Chestplates", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 8, 1, -1)

    /** vanilly Pants */
    public static final OrePrefixes armorLeggings = new OrePrefixBuilder("armorLeggings").setDefaultLocalName("Leggings").build();
    // armorLeggings("Leggings", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 7, 1, -1)

    /** vanilly Boots */
    public static final OrePrefixes armorBoots = new OrePrefixBuilder("armorBoots").setDefaultLocalName("Boots").build();
    // armorBoots("Boots", "", "", false, true, false, false, false, false, true, false, true, false, B[6], M * 4, 1, -1)

    public static final OrePrefixes armor = new OrePrefixBuilder("armor").setDefaultLocalName("Armor Parts").build();
    // armor("Armor Parts", "", "", false, false, false, false, false, false, false, false, true, false, B[6], -1, 1, -1)

    public static final OrePrefixes frameGt = new OrePrefixBuilder("frameGt").setDefaultLocalName("Frame Boxes").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // frameGt("Frame Boxes", "", "", true, true, false, false, true, false, true, false, false, false, 0, M * 2, 64, 83)

    public static final OrePrefixes pipeTiny = new OrePrefixBuilder("pipeTiny").setDefaultLocalName("Tiny Pipes").setLocalMaterialPre("Tiny ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeTiny("Tiny Pipes", "Tiny ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M / 2, 64, 78)

    public static final OrePrefixes pipeSmall = new OrePrefixBuilder("pipeSmall").setDefaultLocalName("Small Pipes").setLocalMaterialPre("Small ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeSmall("Small Pipes", "Small ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M * 1, 64, 79)

    public static final OrePrefixes pipeMedium = new OrePrefixBuilder("pipeMedium").setDefaultLocalName("Medium Pipes").setLocalMaterialPre("Medium ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeMedium("Medium Pipes", "Medium ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M * 3, 64, 80)

    public static final OrePrefixes pipeLarge = new OrePrefixBuilder("pipeLarge").setDefaultLocalName("Large pipes").setLocalMaterialPre("Large ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeLarge("Large pipes", "Large ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M * 6, 64, 81)

    public static final OrePrefixes pipeHuge = new OrePrefixBuilder("pipeHuge").setDefaultLocalName("Huge Pipes").setLocalMaterialPre("Huge ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeHuge("Huge Pipes", "Huge ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M * 12, 64, 82)

    public static final OrePrefixes pipeQuadruple = new OrePrefixBuilder("pipeQuadruple").setDefaultLocalName("Quadruple Pipes").setLocalMaterialPre("Quadruple ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeQuadruple("Quadruple Pipes", "Quadruple ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M * 12, 64, 84)

    public static final OrePrefixes pipeNonuple = new OrePrefixBuilder("pipeNonuple").setDefaultLocalName("Nonuple Pipes").setLocalMaterialPre("Nonuple ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeNonuple("Nonuple Pipes", "Nonuple ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M * 9, 64, 85)

    public static final OrePrefixes pipeRestrictiveTiny = new OrePrefixBuilder("pipeRestrictiveTiny").setDefaultLocalName("Tiny Restrictive Pipes").setLocalMaterialPre("Tiny Restrictive ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeRestrictiveTiny("Tiny Restrictive Pipes", "Tiny Restrictive ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M / 2, 64, 78)

    public static final OrePrefixes pipeRestrictiveSmall = new OrePrefixBuilder("pipeRestrictiveSmall").setDefaultLocalName("Small Restrictive Pipes").setLocalMaterialPre("Small Restrictive ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeRestrictiveSmall("Small Restrictive Pipes", "Small Restrictive ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M * 1, 64, 79)

    public static final OrePrefixes pipeRestrictiveMedium = new OrePrefixBuilder("pipeRestrictiveMedium").setDefaultLocalName("Medium Restrictive Pipes").setLocalMaterialPre("Medium Restrictive ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeRestrictiveMedium("Medium Restrictive Pipes", "Medium Restrictive ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M * 3, 64, 80)

    public static final OrePrefixes pipeRestrictiveLarge = new OrePrefixBuilder("pipeRestrictiveLarge").setDefaultLocalName("Large Restrictive Pipes").setLocalMaterialPre("Large Restrictive ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeRestrictiveLarge("Large Restrictive Pipes", "Large Restrictive ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M * 6, 64, 81)

    public static final OrePrefixes pipeRestrictiveHuge = new OrePrefixBuilder("pipeRestrictiveHuge").setDefaultLocalName("Huge Restrictive Pipes").setLocalMaterialPre("Huge Restrictive ").setLocalMaterialPost(" Pipe").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // pipeRestrictiveHuge("Huge Restrictive Pipes", "Huge Restrictive ", " Pipe", true, true, false, false, true, false, true, false, false, false, 0, M * 12, 64, 82)

    public static final OrePrefixes pipe = new OrePrefixBuilder("pipe").setDefaultLocalName("Pipes").setLocalMaterialPost(" Pipe").setUnifiable().build();
    // pipe("Pipes", "", " Pipe", true, false, false, false, false, false, false, false, false, false, 0, -1, 64, 77)

    public static final OrePrefixes wireGt16 = new OrePrefixBuilder("wireGt16").setDefaultLocalName("16x Wires").setLocalMaterialPre("16x ").setLocalMaterialPost(" Wire").setUnifiable().setMaterialBased().build();
    // wireGt16("16x Wires", "16x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M * 8, 64, -1)

    public static final OrePrefixes wireGt12 = new OrePrefixBuilder("wireGt12").setDefaultLocalName("12x Wires").setLocalMaterialPre("12x ").setLocalMaterialPost(" Wire").setUnifiable().setMaterialBased().build();
    // wireGt12("12x Wires", "12x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M * 6, 64, -1)

    public static final OrePrefixes wireGt08 = new OrePrefixBuilder("wireGt08").setDefaultLocalName("8x Wires").setLocalMaterialPre("8x ").setLocalMaterialPost(" Wire").setUnifiable().setMaterialBased().build();
    // wireGt08("8x Wires", "8x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M * 4, 64, -1)

    public static final OrePrefixes wireGt04 = new OrePrefixBuilder("wireGt04").setDefaultLocalName("4x Wires").setLocalMaterialPre("4x ").setLocalMaterialPost(" Wire").setUnifiable().setMaterialBased().build();
    // wireGt04("4x Wires", "4x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M * 2, 64, -1)

    public static final OrePrefixes wireGt02 = new OrePrefixBuilder("wireGt02").setDefaultLocalName("2x Wires").setLocalMaterialPre("2x ").setLocalMaterialPost(" Wire").setUnifiable().setMaterialBased().build();
    // wireGt02("2x Wires", "2x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M * 1, 64, -1)

    public static final OrePrefixes wireGt01 = new OrePrefixBuilder("wireGt01").setDefaultLocalName("1x Wires").setLocalMaterialPre("1x ").setLocalMaterialPost(" Wire").setUnifiable().setMaterialBased().build();
    // wireGt01("1x Wires", "1x ", " Wire", true, true, false, false, false, false, true, false, false, false, 0, M / 2, 64, -1)

    public static final OrePrefixes cableGt16 = new OrePrefixBuilder("cableGt16").setDefaultLocalName("16x Cables").setLocalMaterialPre("16x ").setLocalMaterialPost(" Cable").setUnifiable().setMaterialBased().build();
    // cableGt16("16x Cables", "16x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0, M * 8, 64, -1)

    public static final OrePrefixes cableGt12 = new OrePrefixBuilder("cableGt12").setDefaultLocalName("12x Cables").setLocalMaterialPre("12x ").setLocalMaterialPost(" Cable").setUnifiable().setMaterialBased().build();
    // cableGt12("12x Cables", "12x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0, M * 6, 64, -1)

    public static final OrePrefixes cableGt08 = new OrePrefixBuilder("cableGt08").setDefaultLocalName("8x Cables").setLocalMaterialPre("8x ").setLocalMaterialPost(" Cable").setUnifiable().setMaterialBased().build();
    // cableGt08("8x Cables", "8x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0, M * 4,64, -1)

    public static final OrePrefixes cableGt04 = new OrePrefixBuilder("cableGt04").setDefaultLocalName("4x Cables").setLocalMaterialPre("4x ").setLocalMaterialPost(" Cable").setUnifiable().setMaterialBased().build();
    // cableGt04("4x Cables", "4x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0, M * 2, 64, -1)

    public static final OrePrefixes cableGt02 = new OrePrefixBuilder("cableGt02").setDefaultLocalName("2x Cables").setLocalMaterialPre("2x ").setLocalMaterialPost(" Cable").setUnifiable().setMaterialBased().build();
    // cableGt02("2x Cables", "2x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0, M * 1, 64, -1)

    public static final OrePrefixes cableGt01 = new OrePrefixBuilder("cableGt01").setDefaultLocalName("1x Cables").setLocalMaterialPre("1x ").setLocalMaterialPost(" Cable").setUnifiable().setMaterialBased().build();
    // cableGt01("1x Cables", "1x ", " Cable", true, true, false, false, false, false, true, false, false, false, 0, M / 2, 64, -1)

    /*
     * Electric Components. usual Materials for this are: Primitive (Tier 1) Basic (Tier 2) as used by UE as well : IC2
     * Circuit and RE-Battery Good (Tier 3) Advanced (Tier 4) as used by UE as well : Advanced Circuit, Advanced Battery
     * and Lithium Battery Data (Tier 5) : Data Storage Circuit Elite (Tier 6) as used by UE as well : Energy Crystal
     * and Data Control Circuit Master (Tier 7) : Energy Flow Circuit and Lapotron Crystal Ultimate (Tier 8) : Data Orb
     * and Lapotronic Energy Orb Infinite (Cheaty)
     */
    public static final OrePrefixes batterySingleuse = new OrePrefixBuilder("batterySingleuse").setDefaultLocalName("Single Use Batteries").build();
    // batterySingleuse("Single Use Batteries", "", "", false, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes battery = new OrePrefixBuilder("battery").setDefaultLocalName("Reusable Batteries").build();
    // battery("Reusable Batteries", "", "", false, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes circuit = new OrePrefixBuilder("circuit").setDefaultLocalName("Circuits").setUnifiable().setMaterialBased().build();
    // circuit("Circuits", "", "", true, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Introduced by Buildcraft */
    public static final OrePrefixes chipset = new OrePrefixBuilder("chipset").setDefaultLocalName("Chipsets").setUnifiable().setMaterialBased().build();
    // chipset("Chipsets", "", "", true, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** A whole Computer. "computerMaster" = ComputerCube */
    public static final OrePrefixes computer = new OrePrefixBuilder("computer").setDefaultLocalName("Computers").setUnifiable().setMaterialBased().setDoNotUnifyActively().build();
    // computer("Computers", "", "", true, true, false, false, true, false, false, false, false, false, 0, -1, 64, -1)

    // random known prefixes without special abilities.
    public static final OrePrefixes skull = new OrePrefixBuilder("skull").setDefaultLocalName("Skulls").build();
    // skull("Skulls", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes plating = new OrePrefixBuilder("plating").setDefaultLocalName("Platings").build();
    // plating("Platings", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes dinosaur = new OrePrefixBuilder("dinosaur").setDefaultLocalName("Dinosaurs").build();
    // dinosaur("Dinosaurs", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes travelgear = new OrePrefixBuilder("travelgear").setDefaultLocalName("Travel Gear").build();
    // travelgear("Travel Gear", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes bauble = new OrePrefixBuilder("bauble").setDefaultLocalName("Baubles").build();
    // bauble("Baubles", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes cluster = new OrePrefixBuilder("cluster").setDefaultLocalName("Clusters").build();
    // cluster("Clusters", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes grafter = new OrePrefixBuilder("grafter").setDefaultLocalName("Grafters").build();
    // grafter("Grafters", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes scoop = new OrePrefixBuilder("scoop").setDefaultLocalName("Scoops").build();
    // scoop("Scoops", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes frame = new OrePrefixBuilder("frame").setDefaultLocalName("Frames").build();
    // frame("Frames", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes tome = new OrePrefixBuilder("tome").setDefaultLocalName("Tomes").build();
    // tome("Tomes", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes junk = new OrePrefixBuilder("junk").setDefaultLocalName("Junk").build();
    // junk("Junk", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes bee = new OrePrefixBuilder("bee").setDefaultLocalName("Bees").build();
    // bee("Bees", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes rod = new OrePrefixBuilder("rod").setDefaultLocalName("Rods").build();
    // rod("Rods", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes dirt = new OrePrefixBuilder("dirt").setDefaultLocalName("Dirts").build();
    // dirt("Dirts", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes sand = new OrePrefixBuilder("sand").setDefaultLocalName("Sands").setSelfReferencing().setUsedForBlocks().build();
    // sand("Sands", "", "", false, false, true, false, false, true, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes grass = new OrePrefixBuilder("grass").setDefaultLocalName("Grasses").build();
    // grass("Grasses", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes gravel = new OrePrefixBuilder("gravel").setDefaultLocalName("Gravels").build();
    // gravel("Gravels", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes mushroom = new OrePrefixBuilder("mushroom").setDefaultLocalName("Mushrooms").build();
    // mushroom("Mushrooms", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Introduced by Eloraam */
    public static final OrePrefixes wood = new OrePrefixBuilder("wood").setDefaultLocalName("Woods").build();
    // wood("Woods", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes drop = new OrePrefixBuilder("drop").setDefaultLocalName("Drops").build();
    // drop("Drops", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes fuel = new OrePrefixBuilder("fuel").setDefaultLocalName("Fuels").build();
    // fuel("Fuels", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes panel = new OrePrefixBuilder("panel").setDefaultLocalName("Panels").build();
    // panel("Panels", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes brick = new OrePrefixBuilder("brick").setDefaultLocalName("Bricks").build();
    // brick("Bricks", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes chunk = new OrePrefixBuilder("chunk").setDefaultLocalName("Chunks").build();
    // chunk("Chunks", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes wire = new OrePrefixBuilder("wire").setDefaultLocalName("Wires").setDoNotUnifyActively().build();
    // wire("Wires", "", "", false, false, false, false, true, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes seed = new OrePrefixBuilder("seed").setDefaultLocalName("Seeds").build();
    // seed("Seeds", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes reed = new OrePrefixBuilder("reed").setDefaultLocalName("Reeds").build();
    // reed("Reeds", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes sheetDouble = new OrePrefixBuilder("sheetDouble").setDefaultLocalName("2x Sheets").build();
    // sheetDouble("2x Sheets", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes sheet = new OrePrefixBuilder("sheet").setDefaultLocalName("Sheets").build();
    // sheet("Sheets", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes crop = new OrePrefixBuilder("crop").setDefaultLocalName("Crops").build();
    // crop("Crops", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes plant = new OrePrefixBuilder("plant").setDefaultLocalName("Plants").build();
    // plant("Plants", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes coin = new OrePrefixBuilder("coin").setDefaultLocalName("Coins").build();
    // coin("Coins", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes lumar = new OrePrefixBuilder("lumar").setDefaultLocalName("Lumars").build();
    // lumar("Lumars", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes ground = new OrePrefixBuilder("ground").setDefaultLocalName("Grounded Stuff").build();
    // ground("Grounded Stuff", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes cable = new OrePrefixBuilder("cable").setDefaultLocalName("Cables").build();
    // cable("Cables", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes component = new OrePrefixBuilder("component").setDefaultLocalName("Components").build();
    // component("Components", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes wax = new OrePrefixBuilder("wax").setDefaultLocalName("Waxes").build();
    // wax("Waxes", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes wall = new OrePrefixBuilder("wall").setDefaultLocalName("Walls").build();
    // wall("Walls", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes tube = new OrePrefixBuilder("tube").setDefaultLocalName("Tubes").build();
    // tube("Tubes", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes list = new OrePrefixBuilder("list").setDefaultLocalName("Lists").build();
    // list("Lists", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes food = new OrePrefixBuilder("food").setDefaultLocalName("Foods").build();
    // food("Foods", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Introduced by SirSengir */
    public static final OrePrefixes gear = new OrePrefixBuilder("gear").setDefaultLocalName("Gears").build();
    // gear("Gears", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes coral = new OrePrefixBuilder("coral").setDefaultLocalName("Corals").build();
    // coral("Corals", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes flower = new OrePrefixBuilder("flower").setDefaultLocalName("Flowers").build();
    // flower("Flowers", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes storage = new OrePrefixBuilder("storage").setDefaultLocalName("Storages").build();
    // storage("Storages", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes material = new OrePrefixBuilder("material").setDefaultLocalName("Materials").build();
    // material("Materials", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes plasma = new OrePrefixBuilder("plasma").setDefaultLocalName("Plasmas").build();
    // plasma("Plasmas", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes element = new OrePrefixBuilder("element").setDefaultLocalName("Elements").build();
    // element("Elements", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes molecule = new OrePrefixBuilder("molecule").setDefaultLocalName("Molecules").build();
    // molecule("Molecules", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes wafer = new OrePrefixBuilder("wafer").setDefaultLocalName("Wafers").build();
    // wafer("Wafers", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes orb = new OrePrefixBuilder("orb").setDefaultLocalName("Orbs").build();
    // orb("Orbs", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes handle = new OrePrefixBuilder("handle").setDefaultLocalName("Handles").build();
    // handle("Handles", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes blade = new OrePrefixBuilder("blade").setDefaultLocalName("Blades").build();
    // blade("Blades", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes head = new OrePrefixBuilder("head").setDefaultLocalName("Heads").build();
    // head("Heads", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes motor = new OrePrefixBuilder("motor").setDefaultLocalName("Motors").build();
    // motor("Motors", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes bit = new OrePrefixBuilder("bit").setDefaultLocalName("Bits").build();
    // bit("Bits", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes shears = new OrePrefixBuilder("shears").setDefaultLocalName("Shears").build();
    // shears("Shears", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes turbine = new OrePrefixBuilder("turbine").setDefaultLocalName("Turbines").build();
    // turbine("Turbines", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes fertilizer = new OrePrefixBuilder("fertilizer").setDefaultLocalName("Fertilizers").build();
    // fertilizer("Fertilizers", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes chest = new OrePrefixBuilder("chest").setDefaultLocalName("Chests").build();
    // chest("Chests", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes raw = new OrePrefixBuilder("raw").setDefaultLocalName("Raw Things").build();
    // raw("Raw Things", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes stainedGlass = new OrePrefixBuilder("stainedGlass").setDefaultLocalName("Stained Glasses").build();
    // stainedGlass("Stained Glasses", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes mystic = new OrePrefixBuilder("mystic").setDefaultLocalName("Mystic Stuff").build();
    // mystic("Mystic Stuff", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes mana = new OrePrefixBuilder("mana").setDefaultLocalName("Mana Stuff").build();
    // mana("Mana Stuff", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes rune = new OrePrefixBuilder("rune").setDefaultLocalName("Runes").build();
    // rune("Runes", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes petal = new OrePrefixBuilder("petal").setDefaultLocalName("Petals").build();
    // petal("Petals", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes pearl = new OrePrefixBuilder("pearl").setDefaultLocalName("Pearls").build();
    // pearl("Pearls", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes powder = new OrePrefixBuilder("powder").setDefaultLocalName("Powders").build();
    // powder("Powders", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes soulsand = new OrePrefixBuilder("soulsand").setDefaultLocalName("Soulsands").build();
    // soulsand("Soulsands", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes obsidian = new OrePrefixBuilder("obsidian").setDefaultLocalName("Obsidians").build();
    // obsidian("Obsidians", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes glowstone = new OrePrefixBuilder("glowstone").setDefaultLocalName("Glowstones").build();
    // glowstone("Glowstones", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes beans = new OrePrefixBuilder("beans").setDefaultLocalName("Beans").build();
    // beans("Beans", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes br = new OrePrefixBuilder("br").setDefaultLocalName("br").build();
    // br("br", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes essence = new OrePrefixBuilder("essence").setDefaultLocalName("Essences").build();
    // essence("Essences", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes alloy = new OrePrefixBuilder("alloy").setDefaultLocalName("Alloys").build();
    // alloy("Alloys", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes cooking = new OrePrefixBuilder("cooking").setDefaultLocalName("Cooked Things").build();
    // cooking("Cooked Things", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes elven = new OrePrefixBuilder("elven").setDefaultLocalName("Elven Stuff").build();
    // elven("Elven Stuff", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes reactor = new OrePrefixBuilder("reactor").setDefaultLocalName("Reactors").build();
    // reactor("Reactors", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes mffs = new OrePrefixBuilder("mffs").setDefaultLocalName("MFFS").build();
    // mffs("MFFS", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes projred = new OrePrefixBuilder("projred").setDefaultLocalName("Project Red").build();
    // projred("Project Red", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes ganys = new OrePrefixBuilder("ganys").setDefaultLocalName("Ganys Stuff").build();
    // ganys("Ganys Stuff", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes liquid = new OrePrefixBuilder("liquid").setDefaultLocalName("Liquids").build();
    // liquid("Liquids", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes bars = new OrePrefixBuilder("bars").setDefaultLocalName("Bars").build();
    // bars("Bars", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes bar = new OrePrefixBuilder("bar").setDefaultLocalName("Bars").build();
    // bar("Bars", "", "", false, false, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    /** Reverse Head consisting out of 6 Ingots. */
    public static final OrePrefixes toolHeadMallet = new OrePrefixBuilder("toolHeadMallet").setDefaultLocalName("Mallet Heads").setLocalMaterialPost(" Mallet Head").setUnifiable().setMaterialBased().build();
    // toolHeadMallet("Mallet Heads", "", " Mallet Head", true, true, false, false, false, false, true, true, false, false, B[6], M * 6, 64, 127)

    /** Reverse Stick made of half an Ingot. Introduced by Eloraam */
    public static final OrePrefixes handleMallet = new OrePrefixBuilder("handleMallet").setDefaultLocalName("Mallet Handle").setLocalMaterialPost(" Handle").setUnifiable().setMaterialBased().build();
    // handleMallet("Mallet Handle", "", " Handle", true, true, false, false, false, false, true, true, false, false, B[1] | B[2], M / 2, 64, 126)

    // Cracked fluids
    public static final OrePrefixes cellHydroCracked1 = new OrePrefixBuilder("cellHydroCracked1").setDefaultLocalName("Cells").setLocalMaterialPre("Lightly Hydro-Cracked ").setLocalMaterialPost(" Cell").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // cellHydroCracked1("Cells", "Lightly Hydro-Cracked ", " Cell", true, true, true, true, false, false, false, true, false, false, 0, M * 1, 64, 30)

    public static final OrePrefixes cellHydroCracked2 = new OrePrefixBuilder("cellHydroCracked2").setDefaultLocalName("Cells").setLocalMaterialPre("Moderately Hydro-Cracked ").setLocalMaterialPost(" Cell").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // cellHydroCracked2("Cells", "Moderately Hydro-Cracked ", " Cell", true, true, true, true, false, false, false, true, false, false, 0, M * 1, 64, 30)

    public static final OrePrefixes cellHydroCracked3 = new OrePrefixBuilder("cellHydroCracked3").setDefaultLocalName("Cells").setLocalMaterialPre("Severely Hydro-Cracked ").setLocalMaterialPost(" Cell").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // cellHydroCracked3("Cells", "Severely Hydro-Cracked ", " Cell", true, true, true, true, false, false, false, true, false, false, 0, M * 1, 64, 30)

    public static final OrePrefixes cellSteamCracked1 = new OrePrefixBuilder("cellSteamCracked1").setDefaultLocalName("Cells").setLocalMaterialPre("Lightly Steam-Cracked ").setLocalMaterialPost(" Cell").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // cellSteamCracked1("Cells", "Lightly Steam-Cracked ", " Cell", true, true, true, true, false, false, false, true, false, false, 0, M * 1, 64, 30)

    public static final OrePrefixes cellSteamCracked2 = new OrePrefixBuilder("cellSteamCracked2").setDefaultLocalName("Cells").setLocalMaterialPre("Moderately Steam-Cracked ").setLocalMaterialPost(" Cell").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // cellSteamCracked2("Cells", "Moderately Steam-Cracked ", " Cell", true, true, true, true, false, false, false, true, false, false, 0, M * 1, 64, 30)

    public static final OrePrefixes cellSteamCracked3 = new OrePrefixBuilder("cellSteamCracked3").setDefaultLocalName("Cells").setLocalMaterialPre("Severely Steam-Cracked ").setLocalMaterialPost(" Cell").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // cellSteamCracked3("Cells", "Severely Steam-Cracked ", " Cell", true, true, true, true, false, false, false, true, false, false, 0, M * 1, 64, 30)

    public static final OrePrefixes componentCircuit = new OrePrefixBuilder("componentCircuit").setDefaultLocalName("Circuit Parts").setUnifiable().setMaterialBased().build();
    // componentCircuit("Circuit Parts", "", "", true, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes apiaryUpgrade = new OrePrefixBuilder("apiaryUpgrade").setDefaultLocalName("Industrial Apiary Upgrade").setSelfReferencing().build();
    // apiaryUpgrade("Industrial Apiary Upgrade", "", "", false, false, true, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes beeComb = new OrePrefixBuilder("beeComb").setDefaultLocalName("Bee Combs").setUnifiable().setSelfReferencing().build();
    // beeComb("Bee Combs", "", "", true, false, true, false, false, false, false, false, false, false, 0, -1, 64, -1)

    public static final OrePrefixes nanite = new OrePrefixBuilder("nanite").setDefaultLocalName("Nanites").setLocalMaterialPost(" Nanites").setUnifiable().setMaterialBased().setSelfReferencing().build();
    // nanite("Nanites", "", " Nanites", true, true, true, false, false, false, false, false, false, false, 0, -1, 64, 50)

    // migrated from GT++
    public static final OrePrefixes milled = new OrePrefixBuilder("milled").setDefaultLocalName("Milled Ores").setLocalMaterialPre("Milled ").setLocalMaterialPost(" Ore").setUnifiable().setMaterialBased().build();
    // milled("Milled Ores", "Milled ", " Ore", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, -1)

    // migrated from bartworks
    public static final OrePrefixes blockCasing = new OrePrefixBuilder("blockCasing").setDefaultLocalName("A Casing block for a Multiblock-Machine").setLocalMaterialPre("Bolted ").setLocalMaterialPost(" Casing").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().setUsedForBlocks().build();
    // blockCasing("A Casing block for a Multiblock-Machine", "Bolted ", " Casing", true, true, true, true, false, true, false, true, false, false, 0, M * 9, 64, -1)

    public static final OrePrefixes blockCasingAdvanced = new OrePrefixBuilder("blockCasingAdvanced").setDefaultLocalName("An Advanced Casing block for a Multiblock-Machine").setLocalMaterialPre("Rebolted ").setLocalMaterialPost(" Casing").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().setUsedForBlocks().build();
    // blockCasingAdvanced("An Advanced Casing block for a Multiblock-Machine", "Rebolted ", " Casing", true, true, true, true, false, true, false, true, false, false, 0, M * 9, 64, -1)

    public static final OrePrefixes capsuleMolten = new OrePrefixBuilder("capsuleMolten").setDefaultLocalName("Capsule of Molten stuff").setLocalMaterialPre("Molten ").setLocalMaterialPost(" Capsule").setUnifiable().setMaterialBased().setSelfReferencing().setContainer().build();
    // capsuleMolten("Capsule of Molten stuff", "Molten ", " Capsule", true, true, true, true, false, false, false, true, false, false, 0, M * 1, 64, -1)

    // subatomic particles
    public static final OrePrefixes particle = new OrePrefixBuilder("particle").setDefaultLocalName("A Subatomic Particle").setSelfReferencing().build();
    // particle("A Subatomic Particle", "", "", false, false, true, false, false, false, false, false, false, false, 0, -1, 64, -1)

    // Beamline Masks
    public static final OrePrefixes mask = new OrePrefixBuilder("mask").setDefaultLocalName("A Photolithographic Mask").setSelfReferencing().build();
    // mask("A Photolithographic Mask", "", "", false, false, true, false, false, false, false, false, false, false, 0, -1, 1, -1)

    public static final OrePrefixes wrapCircuit = new OrePrefixBuilder("wrapCircuit").setDefaultLocalName("A Circuit Wrap").setSelfReferencing().build();
    // wrapCircuit("A Circuit Wrap", "", "", false, false, true, false, false, false, false, false, false, false, 0, -1, 64, -1)

    private final String name;
    private final String defaultLocalName;
    private final String localMaterialPre;
    private final String localMaterialPost;
    private final boolean isUnifiable;
    private final boolean isMaterialBased;
    private final boolean isSelfReferencing;
    private final boolean isContainer;
    private final boolean doNotUnifyActively;
    private final boolean isUsedForBlocks;

    OrePrefixes(
        // spotless:off
        @NotNull String name,
        @Nullable String defaultLocalName,
        @Nullable String localMaterialPre,
        @Nullable String localMaterialPost,
        boolean isUnifiable,
        boolean isMaterialBased,
        boolean isSelfReferencing,
        boolean isContainer,
        boolean doNotUnifyActively,
        boolean isUsedForBlocks
        // spotless:on
    ) {
        this.name = name;
        this.defaultLocalName = defaultLocalName;
        this.localMaterialPre = localMaterialPre;
        this.localMaterialPost = localMaterialPost;
        this.isUnifiable = isUnifiable;
        this.isMaterialBased = isMaterialBased;
        this.isSelfReferencing = isSelfReferencing;
        this.isContainer = isContainer;
        this.doNotUnifyActively = doNotUnifyActively;
        this.isUsedForBlocks = isUsedForBlocks;
    }

//    OrePrefixes(String aRegularLocalName, String aLocalizedMaterialPre, String aLocalizedMaterialPost,
//        boolean aIsUnificatable, boolean aIsMaterialBased, boolean aIsSelfReferencing, boolean aIsContainer,
//        boolean aDontUnificateActively, boolean aIsUsedForBlocks, boolean aAllowNormalRecycling,
//        boolean aGenerateDefaultItem, boolean aIsEnchantable, boolean aIsUsedForOreProcessing,
//        int aMaterialGenerationBits, long aMaterialAmount, int aDefaultStackSize, int aTextureindex) {
//        mIsUnificatable = aIsUnificatable;
//        mIsMaterialBased = aIsMaterialBased;
//        mIsSelfReferencing = aIsSelfReferencing;
//        mIsContainer = aIsContainer;
//        mDontUnificateActively = aDontUnificateActively;
//        mIsUsedForBlocks = aIsUsedForBlocks;
//        mAllowNormalRecycling = aAllowNormalRecycling;
//        mGenerateDefaultItem = aGenerateDefaultItem;
//        mIsEnchantable = aIsEnchantable;
//        mIsUsedForOreProcessing = aIsUsedForOreProcessing;
//        mMaterialGenerationBits = aMaterialGenerationBits;
//        mMaterialAmount = aMaterialAmount;
//        mRegularLocalName = aRegularLocalName;
//        mLocalizedMaterialPre = aLocalizedMaterialPre;
//        mLocalizedMaterialPost = aLocalizedMaterialPost;
//        mDefaultStackSize = (byte) aDefaultStackSize;
//        mTextureIndex = (short) aTextureindex;
//
//        if (name().startsWith("ore")) {
//            new TC_AspectStack(TCAspects.TERRA, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("wire") || name().startsWith("cable")) {
//            new TC_AspectStack(TCAspects.ELECTRUM, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("dust")) {
//            new TC_AspectStack(TCAspects.PERDITIO, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("crushed")) {
//            new TC_AspectStack(TCAspects.PERFODIO, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("ingot") || name().startsWith("nugget")) {
//            new TC_AspectStack(TCAspects.METALLUM, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("armor")) {
//            new TC_AspectStack(TCAspects.TUTAMEN, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("stone")) {
//            new TC_AspectStack(TCAspects.TERRA, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("pipe")) {
//            new TC_AspectStack(TCAspects.ITER, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("gear")) {
//            new TC_AspectStack(TCAspects.MOTUS, 1).addToAspectList(mAspects);
//            new TC_AspectStack(TCAspects.MACHINA, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("frame") || name().startsWith("plate")) {
//            new TC_AspectStack(TCAspects.FABRICO, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("tool")) {
//            new TC_AspectStack(TCAspects.INSTRUMENTUM, 2).addToAspectList(mAspects);
//        } else if (name().startsWith("gem") || name().startsWith("crystal") || name().startsWith("lens")) {
//            new TC_AspectStack(TCAspects.VITREUS, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("circuit")) {
//            new TC_AspectStack(TCAspects.COGNITIO, 1).addToAspectList(mAspects);
//        } else if (name().startsWith("computer")) {
//            new TC_AspectStack(TCAspects.COGNITIO, 4).addToAspectList(mAspects);
//        } else if (name().startsWith("battery")) {
//            new TC_AspectStack(TCAspects.ELECTRUM, 1).addToAspectList(mAspects);
//        }
//    }

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

    static {
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

        final OrePrefixes[] THIS_VALUES = values();
        for (OrePrefixes tPrefix1 : THIS_VALUES) {
            if (tPrefix1.name
                .startsWith("ore")) {
                for (OrePrefixes tPrefix2 : THIS_VALUES) {
                    if (tPrefix2.name
                        .startsWith("ore")) {
                        tPrefix1.addFamiliarPrefix(tPrefix2);
                    }
                }
            }
        }

        cell.disableComponent(Materials.GravitonShard);

        // ingot.mNotGeneratedItems.add(Materials.Ichorium);

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

        ingotHot.mGeneratedItems.add(Materials.TranscendentMetal);

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
        nanite.mGeneratedItems.add(Materials.TranscendentMetal);
        nanite.mGeneratedItems.add(Materials.Neutronium);
        nanite.mGeneratedItems.add(Materials.Universium);
        nanite.mGeneratedItems.add(Materials.WhiteDwarfMatter);
        nanite.mGeneratedItems.add(Materials.BlackDwarfMatter);
        nanite.mGeneratedItems.add(Materials.Glowstone);
        nanite.mGeneratedItems.add(Materials.Eternity);
        nanite.mGeneratedItems.add(Materials.SixPhasedCopper);
        nanite.mGeneratedItems.add(Materials.MagMatter);
        // -----

        gear.mGeneratedItems.add(Materials.MagnetohydrodynamicallyConstrainedStarMatter);
        ingot.mGeneratedItems.add(Materials.MagnetohydrodynamicallyConstrainedStarMatter);
        toolHeadHammer.mGeneratedItems.add(Materials.MagnetohydrodynamicallyConstrainedStarMatter);
        frame.mGeneratedItems.add(Materials.MagnetohydrodynamicallyConstrainedStarMatter);
        frameGt.mGeneratedItems.add(Materials.MagnetohydrodynamicallyConstrainedStarMatter);

        gear.mGeneratedItems.add(Materials.HotProtoHalkonite);
        ingot.mGeneratedItems.add(Materials.HotProtoHalkonite);
        toolHeadHammer.mGeneratedItems.add(Materials.HotProtoHalkonite);
        frame.mGeneratedItems.add(Materials.HotProtoHalkonite);
        frameGt.mGeneratedItems.add(Materials.HotProtoHalkonite);

        gear.mGeneratedItems.add(Materials.ProtoHalkonite);
        ingot.mGeneratedItems.add(Materials.ProtoHalkonite);
        toolHeadHammer.mGeneratedItems.add(Materials.ProtoHalkonite);
        frame.mGeneratedItems.add(Materials.ProtoHalkonite);
        frameGt.mGeneratedItems.add(Materials.ProtoHalkonite);

        gear.mGeneratedItems.add(Materials.HotExoHalkonite);
        ingot.mGeneratedItems.add(Materials.HotExoHalkonite);
        toolHeadHammer.mGeneratedItems.add(Materials.HotExoHalkonite);
        frame.mGeneratedItems.add(Materials.HotExoHalkonite);
        frameGt.mGeneratedItems.add(Materials.HotExoHalkonite);

        gear.mGeneratedItems.add(Materials.ExoHalkonite);
        ingot.mGeneratedItems.add(Materials.ExoHalkonite);
        toolHeadHammer.mGeneratedItems.add(Materials.ExoHalkonite);
        frame.mGeneratedItems.add(Materials.ExoHalkonite);
        frameGt.mGeneratedItems.add(Materials.ExoHalkonite);

        gem.mGeneratedItems.add(Materials.GravitonShard);

        dust.mGeneratedItems.addAll(dustPure.mGeneratedItems);
        dust.mGeneratedItems.addAll(dustImpure.mGeneratedItems);
        dust.mGeneratedItems.addAll(dustRefined.mGeneratedItems);
        dustTiny.mGeneratedItems.addAll(dust.mGeneratedItems);
        dustSmall.mGeneratedItems.addAll(dust.mGeneratedItems);
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

        plateDouble.mCondition = new ICondition.Or<>(
            SubTag.PAPER,
            new ICondition.Not<>(SubTag.NO_SMASHING),
            SubTag.STRETCHY);
        plateTriple.mCondition = new ICondition.And<>(SubTag.MULTI_PLATE);
        plateQuadruple.mCondition = new ICondition.And<>(SubTag.MULTI_PLATE);
        plateQuintuple.mCondition = new ICondition.And<>(SubTag.MULTI_PLATE);

        plateDense.mCondition = new ICondition.Or<>(new ICondition.Not<>(SubTag.NO_SMASHING), SubTag.STRETCHY);
        plateSuperdense.mCondition = new ICondition.Or<>(new ICondition.Not<>(SubTag.NO_SMASHING), SubTag.STRETCHY);

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
        rawOre.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount);
        oreRich.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount * 2);
        ore.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount);
        crushed.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.mMaterialAmount);
        toolHeadChainsaw.mSecondaryMaterial = new MaterialStack(
            Materials.Steel,
            plate.mMaterialAmount * 4 + ring.mMaterialAmount * 2);
        toolHeadWrench.mSecondaryMaterial = new MaterialStack(
            Materials.Steel,
            ring.mMaterialAmount + screw.mMaterialAmount * 2);
        bulletGtSmall.mSecondaryMaterial = new MaterialStack(Materials.Brass, ingot.mMaterialAmount / 9);
        bulletGtMedium.mSecondaryMaterial = new MaterialStack(Materials.Brass, ingot.mMaterialAmount / 6);
        bulletGtLarge.mSecondaryMaterial = new MaterialStack(Materials.Brass, ingot.mMaterialAmount / 3);
    }

    public final ArrayList<ItemStack> mPrefixedItems = new GTArrayList<>(false, 16);
    public final short mTextureIndex;
    public final String mLocalizedMaterialPre, mLocalizedMaterialPost;
    public final boolean mIsUsedForOreProcessing, mIsEnchantable, mIsUnificatable, mIsMaterialBased, mIsSelfReferencing,
        mIsContainer, mDontUnificateActively, mIsUsedForBlocks, mAllowNormalRecycling, mGenerateDefaultItem;
    public final List<TC_AspectStack> mAspects = new ArrayList<>();
    public final Collection<OrePrefixes> mFamiliarPrefixes = new HashSet<>();
    /**
     * Used to determine the amount of Material this Prefix contains. Multiply or Divide GT_Values.M to get the Amounts
     * in comparision to one Ingot. 0 = Null Negative = Undefined Amount
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
    private final ObjectSet<ItemStack> mContainsTestCache = new ObjectOpenCustomHashSet<>(
        512,
        0.5f,
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    public static final List<OrePrefixes> mPreventableComponents = new LinkedList<>(
        Arrays.asList(
            OrePrefixes.gem,
            OrePrefixes.ingotHot,
            OrePrefixes.plate,
            OrePrefixes.plateDouble,
            OrePrefixes.plateTriple,
            OrePrefixes.plateQuadruple,
            OrePrefixes.plateQuintuple,
            OrePrefixes.plateDense,
            OrePrefixes.plateSuperdense,
            OrePrefixes.stick,
            OrePrefixes.round,
            OrePrefixes.bolt,
            OrePrefixes.screw,
            OrePrefixes.ring,
            OrePrefixes.foil,
            OrePrefixes.toolHeadHammer,
            OrePrefixes.toolHeadFile,
            OrePrefixes.toolHeadSaw,
            OrePrefixes.toolHeadDrill,
            OrePrefixes.toolHeadChainsaw,
            OrePrefixes.toolHeadWrench,
            OrePrefixes.toolHeadBuzzSaw,
            OrePrefixes.turbineBlade,
            OrePrefixes.wireFine,
            OrePrefixes.gearGtSmall,
            OrePrefixes.rotor,
            OrePrefixes.stickLong,
            OrePrefixes.springSmall,
            OrePrefixes.spring,
            OrePrefixes.gemChipped,
            OrePrefixes.gemFlawed,
            OrePrefixes.gemFlawless,
            OrePrefixes.gemExquisite,
            OrePrefixes.gearGt,
            OrePrefixes.itemCasing,
            OrePrefixes.nanite,
            OrePrefixes.cell));
    /**
     * Yes this Value can be changed to add Bits for the MetaGenerated-Item-Check.
     */
    public int mMaterialGenerationBits = 0;

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

    public static class ParsedOreDictName {

        public final OrePrefixes prefix;
        public final String material;

        public ParsedOreDictName(OrePrefixes prefix, String material) {
            this.prefix = prefix;
            this.material = material;
        }

        @Override
        public String toString() {
            return "ParsedOreDictName [prefix=" + prefix + ", material=" + material + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
            result = prime * result + ((material == null) ? 0 : material.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            ParsedOreDictName other = (ParsedOreDictName) obj;
            if (prefix != other.prefix) return false;
            if (material == null) {
                if (other.material != null) return false;
            } else if (!material.equals(other.material)) return false;
            return true;
        }
    }

    public static ParsedOreDictName detectPrefix(String oredictName) {
        for (OrePrefixes prefix : values()) {
            if (oredictName.startsWith(prefix.name)) {
                return new ParsedOreDictName(
                    prefix,
                    oredictName.substring(
                        prefix.name
                            .length()));
            }
        }

        return null;
    }

    private static final ThreadLocal<Object2ObjectLinkedOpenHashMap<ItemId, ImmutableList<ParsedOreDictName>>> PREFIX_CACHE = ThreadLocal
        .withInitial(Object2ObjectLinkedOpenHashMap::new);

    public static List<ParsedOreDictName> detectPrefix(ItemStack stack) {
        Object2ObjectLinkedOpenHashMap<ItemId, ImmutableList<ParsedOreDictName>> cache = PREFIX_CACHE.get();

        ItemId itemId = ItemId.create(stack);

        var cacheResult = cache.getAndMoveToFirst(itemId);

        if (cacheResult != null) return cacheResult;

        ImmutableList.Builder<ParsedOreDictName> result = ImmutableList.builder();

        for (int id : OreDictionary.getOreIDs(stack)) {
            ParsedOreDictName p = detectPrefix(OreDictionary.getOreName(id));

            if (p != null) {
                result.add(p);
            }
        }

        ImmutableList<ParsedOreDictName> prefixes = result.build();

        cache.putAndMoveToFirst(itemId, prefixes);

        while (cache.size() > 1024) {
            cache.removeLast();
        }

        return prefixes;
    }

    public static String replacePrefix(String aOre, OrePrefixes aPrefix) {
        for (OrePrefixes tPrefix : values()) {
            if (aOre.startsWith(tPrefix.toString())) {
                return aOre.replaceFirst(tPrefix.toString(), aPrefix.toString());
            }
        }
        return "";
    }

    private static final Map<String, OrePrefixes> NAME_TO_OREPREFIX = new ConcurrentHashMap<>();

    static {
        for (OrePrefixes value : OrePrefixes.values()) {
            NAME_TO_OREPREFIX.put(value.name, value);
        }
    }

    public static OrePrefixes getPrefix(String aPrefixName) {
        return getPrefix(aPrefixName, null);
    }

    public static OrePrefixes getPrefix(String aPrefixName, OrePrefixes aReplacement) {
        final OrePrefixes value = NAME_TO_OREPREFIX.get(aPrefixName);
        if (value != null) {
            return value;
        }
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
            mContainsTestCache.add(aStack);
        }
        return true;
    }

    public boolean contains(ItemStack aStack) {
        return !GTUtility.isStackInvalid(aStack) && mContainsTestCache.contains(aStack);
    }

    public boolean containsUnCached(ItemStack aStack) {
        // In case someone needs this
        for (ItemStack tStack : mPrefixedItems) {
            if (GTUtility.areStacksEqual(aStack, tStack, !tStack.hasTagCompound())) {
                return true;
            }
        }
        return false;
    }

    public boolean doGenerateItem(Materials aMaterial) {
        if (aMaterial == null) return false;
        if (aMaterial == Materials._NULL) return false;
        if (aMaterial.mMetaItemSubID == -1) return false;
        if (!aMaterial.mHasParentMod) return false;

        // This only falls through, returning false, when the material has no overlap with `mMaterialGenerationBits`.
        // spotless:off
        if (!mGeneratedItems.contains(aMaterial))
            if ((mMaterialGenerationBits & 1) == 0 || !aMaterial.hasDustItems())
                if ((mMaterialGenerationBits & 2) == 0 || !aMaterial.hasMetalItems())
                    if ((mMaterialGenerationBits & 4) == 0 || !aMaterial.hasGemItems())
                        if ((mMaterialGenerationBits & 8) == 0 || !aMaterial.hasOresItems())
                            if ((mMaterialGenerationBits & 16) == 0 || !aMaterial.hasCell())
                                if ((mMaterialGenerationBits & 32) == 0 || !aMaterial.hasPlasma())
                                    if ((mMaterialGenerationBits & 64) == 0 || !aMaterial.hasToolHeadItems())
                                        if ((mMaterialGenerationBits & 128) == 0 || !aMaterial.hasGearItems())
                                            if ((mMaterialGenerationBits & 256) == 0 || !aMaterial.hasEmpty())
                                                return false;
        // spotless:on

        if (mNotGeneratedItems.contains(aMaterial)) return false;
        if (mDisabledItems.contains(aMaterial)) return false;
        return mCondition == null || mCondition.isTrue(aMaterial);
    }

    public boolean ignoreMaterials(Materials... aMaterials) {
        for (Materials tMaterial : aMaterials) if (tMaterial != null) mIgnoredMaterials.add(tMaterial);
        return true;
    }

    public boolean isIgnored(Materials aMaterial) {
        if (aMaterial != null && (!aMaterial.mUnifiable || aMaterial != aMaterial.mMaterialInto)) return true;
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

        if (!((aMaterial != Materials._NULL || mIsSelfReferencing || !mIsMaterialBased)
            && GTUtility.isStackValid(aStack))) {
            return;
        }

        for (IOreRecipeRegistrator tRegistrator : mOreProcessing) {
            if (D2) GTLog.ore.println(
                "Processing '" + aOreDictName
                    + "' with the Prefix '"
                    + name
                    + "' and the Material '"
                    + aMaterial.mName
                    + "' at "
                    + GTUtility.getClassName(tRegistrator));
            tRegistrator.registerOre(this, aMaterial, aOreDictName, aModName, GTUtility.copyAmount(1, aStack));
        }
    }

    public Object get(Object aMaterial) {
        if (aMaterial instanceof Materials) return new ItemData(this, (Materials) aMaterial);
        return name + aMaterial;
    }

    public String getDefaultLocalNameForItem(Materials aMaterial) {
        return aMaterial.getDefaultLocalizedNameForItem(getDefaultLocalNameFormatForItem(aMaterial));
    }

    @SuppressWarnings("incomplete-switch")
    public String getDefaultLocalNameFormatForItem(Materials aMaterial) {
        // Certain Materials have slightly different Localizations.
        switch (aMaterial.mName) {
            case "Glass", "BorosilicateGlass" -> {
                if (name.startsWith("gem")) return mLocalizedMaterialPre + "%material" + " Crystal";
                if (name.startsWith("plate")) return mLocalizedMaterialPre + "%material" + " Pane";
                if (name.startsWith("ingot")) return mLocalizedMaterialPre + "%material" + " Bar";
                if (name.startsWith("nugget")) return mLocalizedMaterialPre + "%material" + " Chip";
            }
            case "Wheat" -> {
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "Flour";
            }
            case "Ice" -> {
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "Crushed Ice";
            }
            case "Wood", "WoodSealed" -> {
                if (name.startsWith("bolt")) return "Short " + "%material" + " Stick";
                if (name.startsWith("stick")) return mLocalizedMaterialPre + "%material" + " Stick";
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "%material" + " Pulp";
                if (name.startsWith("nugget")) return mLocalizedMaterialPre + "%material" + " Chip";
                if (name.startsWith("plate")) return mLocalizedMaterialPre + "%material" + " Plank";
            }
            case "Plastic", "Rubber", "Polyethylene", "Epoxid", "EpoxidFiberReinforced", "Polydimethylsiloxane", "Silicone", "Polysiloxane", "Polycaprolactam", "Polytetrafluoroethylene", "PolyvinylChloride", "Polystyrene", "StyreneButadieneRubber" -> {
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "%material" + " Pulp";
                if (name.startsWith("plate")) return mLocalizedMaterialPre + "%material" + " Sheet";
                if (name.startsWith("ingot")) return mLocalizedMaterialPre + "%material" + " Bar";
                if (name.startsWith("nugget")) return mLocalizedMaterialPre + "%material" + " Chip";
                if (name.startsWith("foil")) return "Thin " + "%material" + " Sheet";
            }
            case "FierySteel" -> {
                if (mIsContainer) return mLocalizedMaterialPre + "Fiery Blood" + mLocalizedMaterialPost;
            }
            case "Steeleaf" -> {
                if (name.startsWith("ingot")) return mLocalizedMaterialPre + "%material";
            }
            case "Bone" -> {
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "Bone Meal";
            }
            case "Blaze", "Milk", "Cocoa", "Chocolate", "Coffee", "Chili", "Cheese", "Snow" -> {
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "%material" + " Powder";
            }
            case "Paper" -> {
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "Chad";
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
                    case plateSuperdense -> {
                        return "Impossibly Strong Cardboard";
                    }
                }
            }
            case "MeatRaw" -> {
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "Mince Meat";
            }
            case "MeatCooked" -> {
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "Cooked Mince Meat";
            }
            case "Ash", "DarkAsh", "Gunpowder", "Sugar", "Salt", "RockSalt", "VolcanicAsh", "RareEarth" -> {
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "%material";
            }
            case "Vermiculite", "Bentonite", "Kaolinite", "Talc", "BasalticMineralSand", "GraniticMineralSand", "GlauconiteSand", "CassiteriteSand", "GarnetSand", "QuartzSand", "Pitchblende", "FullersEarth" -> {
                if (name.startsWith("dust")) return mLocalizedMaterialPre + "%material";
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
                    if (name.startsWith("gem")) return mLocalizedMaterialPre + "Shard of " + "%material";
                    if (name.startsWith("crystal")) return mLocalizedMaterialPre + "Shard of " + "%material";
                    if (name.startsWith("plate")) return mLocalizedMaterialPre + "%material" + " Crystal Plate";
                    if (name.startsWith("dust")) return mLocalizedMaterialPre + "%material" + " Crystal Powder";
                    switch (this) {
                        case crushedCentrifuged, crushedPurified, crushed -> {
                            return mLocalizedMaterialPre + "%material" + " Crystals";
                        }
                    }
                }
            }
        }

        if (aMaterial.contains(SubTag.ICE_ORE) && (this == rawOre || this == ore)) {
            return mLocalizedMaterialPre + "%material" + " Ice";
        }

        if (this == ore) {
            return switch (aMaterial.mName) {
                case "InfusedAir", "InfusedDull", "InfusedEarth", "InfusedEntropy", "InfusedFire", "InfusedOrder", "InfusedVis", "InfusedWater" -> "%material Infused Stone";
                case "Vermiculite", "Bentonite", "Kaolinite", "Talc", "BasalticMineralSand", "GraniticMineralSand", "GlauconiteSand", "CassiteriteSand", "GarnetSand", "QuartzSand", "Pitchblende", "FullersEarth" -> "%material";
                default -> mLocalizedMaterialPre + "%material" + mLocalizedMaterialPost;
            };
        }

        // Use Standard Localization
        return mLocalizedMaterialPre + "%material" + mLocalizedMaterialPost;
    }
}
