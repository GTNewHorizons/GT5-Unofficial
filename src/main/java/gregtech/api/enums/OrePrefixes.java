package gregtech.api.enums;

import static gregtech.api.enums.GTValues.D2;
import static gregtech.api.enums.GTValues.M;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
import gregtech.common.config.Gregtech;
import gregtech.loaders.materialprocessing.ProcessingModSupport;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

@SuppressWarnings({ "PointlessArithmeticExpression", "unused" })
public class OrePrefixes {

    private static List<OrePrefixes> VALUES_LIST = new ArrayList<>();

    private static final int ORE_STACK_SIZE = GTUtility.clamp(Gregtech.features.maxOreStackSize, 1, 64);
    private static final int PLANK_STACK_SIZE = GTUtility.clamp(Gregtech.features.maxPlankStackSize, 16, 64);
    private static final int LOG_STACK_SIZE = GTUtility.clamp(Gregtech.features.maxLogStackSize, 16, 64);
    private static final int OTHER_STACK_SIZE = GTUtility.clamp(Gregtech.features.maxOtherBlocksStackSize, 16, 64);

    private static final int DUST = 1;
    private static final int METAL = 2;
    private static final int GEM = 4;
    private static final int ORE = 8;
    private static final int CELL = 16;
    private static final int PLASMA = 32;
    private static final int TOOL = 64;
    private static final int GEAR = 128;
    private static final int EMPTY = 256;

    /** Used for removed prefixes to prevent id shifts. */
    public static final OrePrefixes ___placeholder___ = new OrePrefixBuilder("___placeholder___")
        .withDefaultLocalName("Placeholder")
        .materialAmount(0)
        .build();

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreBlackgranite = new OrePrefixBuilder("oreBlackgranite")
        .withDefaultLocalName("Black Granite Ores")
        .withPrefix("Granite ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreRedgranite = new OrePrefixBuilder("oreRedgranite")
        .withDefaultLocalName("Red Granite Ores")
        .withPrefix("Granite ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreMarble = new OrePrefixBuilder("oreMarble").withDefaultLocalName("Marble Ores")
        .withPrefix("Marble ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreBasalt = new OrePrefixBuilder("oreBasalt").withDefaultLocalName("Basalt Ores")
        .withPrefix("Basalt ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** Prefix of the Nether-Ores Mod. Causes Ores to double. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreNetherrack = new OrePrefixBuilder("oreNetherrack")
        .withDefaultLocalName("Netherrack Ores")
        .withPrefix("Nether ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** Prefix of the Nether-Ores Mod. Causes Ores to double. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreNether = new OrePrefixBuilder("oreNether").withDefaultLocalName("Nether Ores")
        .withPrefix("Nether ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** Prefix of the Dense-Ores Mod. Causes Ores to double. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreDense = new OrePrefixBuilder("oreDense").withDefaultLocalName("Dense Ores")
        .withPrefix("Dense ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** Prefix of TFC */
    public static final OrePrefixes oreRich = new OrePrefixBuilder("oreRich").withDefaultLocalName("Rich Ores")
        .withPrefix("Rich ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** Prefix of TFC */
    public static final OrePrefixes oreNormal = new OrePrefixBuilder("oreNormal").withDefaultLocalName("Normal Ores")
        .withPrefix("Normal ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** Prefix of Railcraft. */
    public static final OrePrefixes oreSmall = new OrePrefixBuilder("oreSmall").withDefaultLocalName("Small Ores")
        .withPrefix("Small ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.ORE_SMALL)
        .build();

    /** Prefix of Railcraft. */
    public static final OrePrefixes orePoor = new OrePrefixBuilder("orePoor").withDefaultLocalName("Poor Ores")
        .withPrefix("Poor ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreEndstone = new OrePrefixBuilder("oreEndstone")
        .withDefaultLocalName("Endstone Ores")
        .withPrefix("End ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** In case of an End-Ores Mod. Ore -> Material is a Oneway Operation! */
    public static final OrePrefixes oreEnd = new OrePrefixBuilder("oreEnd").withDefaultLocalName("End Ores")
        .withPrefix("End ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** Regular Ore Prefix. Ore -> Material is a Oneway Operation! Introduced by Eloraam */
    public static final OrePrefixes ore = new OrePrefixBuilder("ore").withDefaultLocalName("Ores")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.ORE)
        .build();

    public static final OrePrefixes crushedCentrifuged = new OrePrefixBuilder("crushedCentrifuged")
        .withDefaultLocalName("Centrifuged Ores")
        .withPrefix("Centrifuged ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.CRUSHED_CENTRIFUGED)
        .build();

    public static final OrePrefixes crushedPurified = new OrePrefixBuilder("crushedPurified")
        .withDefaultLocalName("Purified Ores")
        .withPrefix("Purified ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.CRUSHED_PURIFIED)
        .build();

    public static final OrePrefixes crushed = new OrePrefixBuilder("crushed").withDefaultLocalName("Crushed Ores")
        .withPrefix("Crushed ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.CRUSHED)
        .build();

    public static final OrePrefixes rawOre = new OrePrefixBuilder("rawOre").withDefaultLocalName("Raw Ore")
        .withPrefix("Raw ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.ORE_RAW)
        .build();

    // Introduced by Mekanism
    public static final OrePrefixes shard = new OrePrefixBuilder("shard").withDefaultLocalName("Crystallised Shards")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    public static final OrePrefixes clump = new OrePrefixBuilder("clump").withDefaultLocalName("Clumps")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    public static final OrePrefixes reduced = new OrePrefixBuilder("reduced").withDefaultLocalName("Reduced Gravels")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    public static final OrePrefixes crystalline = new OrePrefixBuilder("crystalline")
        .withDefaultLocalName("Crystallised Metals")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    public static final OrePrefixes cleanGravel = new OrePrefixBuilder("cleanGravel")
        .withDefaultLocalName("Clean Gravels")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    public static final OrePrefixes dirtyGravel = new OrePrefixBuilder("dirtyGravel")
        .withDefaultLocalName("Dirty Gravels")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    /** A hot Ingot, which has to be cooled down by a Vacuum Freezer. */
    public static final OrePrefixes ingotHot = new OrePrefixBuilder("ingotHot").withDefaultLocalName("Hot Ingots")
        .withPrefix("Hot ")
        .withSuffix(" Ingot")
        .unifiable()
        .materialBased()
        .materialGenerationBits(METAL)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.INGOT_HOT)
        .build();

    /** A regular Ingot. Introduced by Eloraam */
    public static final OrePrefixes ingot = new OrePrefixBuilder("ingot").withDefaultLocalName("Ingots")
        .withSuffix(" Ingot")
        .unifiable()
        .materialBased()
        .materialGenerationBits(METAL)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.INGOT)
        .build();

    /** A regular Gem worth one small Dust. Introduced by TerraFirmaCraft */
    public static final OrePrefixes gemChipped = new OrePrefixBuilder("gemChipped")
        .withDefaultLocalName("Chipped Gemstones")
        .withPrefix("Chipped ")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .recyclable()
        .materialGenerationBits(GEM)
        .materialAmount(M / 4)
        .textureIndex(OrePrefixTextureID.GEM_CHIPPED)
        .build();

    /** A regular Gem worth two small Dusts. Introduced by TerraFirmaCraft */
    public static final OrePrefixes gemFlawed = new OrePrefixBuilder("gemFlawed")
        .withDefaultLocalName("Flawed Gemstones")
        .withPrefix("Flawed ")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .recyclable()
        .materialGenerationBits(GEM)
        .materialAmount(M / 2)
        .textureIndex(OrePrefixTextureID.GEM_FLAWED)
        .build();

    /** A regular Gem worth two Dusts. Introduced by TerraFirmaCraft */
    public static final OrePrefixes gemFlawless = new OrePrefixBuilder("gemFlawless")
        .withDefaultLocalName("Flawless Gemstones")
        .withPrefix("Flawless ")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .recyclable()
        .materialGenerationBits(GEM)
        .materialAmount(M * 2)
        .textureIndex(OrePrefixTextureID.GEM_FLAWLESS)
        .build();

    /** A regular Gem worth four Dusts. Introduced by TerraFirmaCraft */
    public static final OrePrefixes gemExquisite = new OrePrefixBuilder("gemExquisite")
        .withDefaultLocalName("Exquisite Gemstones")
        .withPrefix("Exquisite ")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .recyclable()
        .materialGenerationBits(GEM)
        .materialAmount(M * 4)
        .textureIndex(OrePrefixTextureID.GEM_EXQUISITE)
        .build();

    /** A regular Gem worth one Dust. Introduced by Eloraam */
    public static final OrePrefixes gem = new OrePrefixBuilder("gem").withDefaultLocalName("Gemstones")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .recyclable()
        .materialGenerationBits(GEM)
        .materialAmount(M * 1)
        .defaultStackSize(OTHER_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.GEM)
        .build();

    /** 1/9th of a Dust. */
    public static final OrePrefixes dustTiny = new OrePrefixBuilder("dustTiny").withDefaultLocalName("Tiny Dusts")
        .withPrefix("Tiny Pile of ")
        .withSuffix(" Dust")
        .unifiable()
        .materialBased()
        .materialGenerationBits(DUST | METAL | GEM | ORE)
        .materialAmount(M / 9)
        .textureIndex(OrePrefixTextureID.DUST_TINY)
        .build();

    /** 1/4th of a Dust. */
    public static final OrePrefixes dustSmall = new OrePrefixBuilder("dustSmall").withDefaultLocalName("Small Dusts")
        .withPrefix("Small Pile of ")
        .withSuffix(" Dust")
        .unifiable()
        .materialBased()
        .materialGenerationBits(DUST | METAL | GEM | ORE)
        .materialAmount(M / 4)
        .textureIndex(OrePrefixTextureID.DUST_SMALL)
        .build();

    /** Dust with impurities. 1 Unit of Main Material and 1/9 - 1/4 Unit of secondary Material */
    public static final OrePrefixes dustImpure = new OrePrefixBuilder("dustImpure").withDefaultLocalName("Impure Dusts")
        .withPrefix("Impure Pile of ")
        .withSuffix(" Dust")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .materialAmount(M * 1)
        .defaultStackSize(ORE_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.DUST_IMPURE)
        .build();

    public static final OrePrefixes dustRefined = new OrePrefixBuilder("dustRefined")
        .withDefaultLocalName("Refined Dusts")
        .withPrefix("Refined Pile of ")
        .withSuffix(" Dust")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .materialAmount(M * 1)
        .defaultStackSize(ORE_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.DUST)
        .build();

    public static final OrePrefixes dustPure = new OrePrefixBuilder("dustPure").withDefaultLocalName("Purified Dusts")
        .withPrefix("Purified Pile of ")
        .withSuffix(" Dust")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .materialAmount(M * 1)
        .defaultStackSize(ORE_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.DUST_PURE)
        .build();

    /** Pure Dust worth of one Ingot or Gem. Introduced by Alblaka. */
    public static final OrePrefixes dust = new OrePrefixBuilder("dust").withDefaultLocalName("Dusts")
        .withSuffix(" Dust")
        .unifiable()
        .materialBased()
        .materialGenerationBits(DUST | METAL | GEM | ORE)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.DUST)
        .build();

    /** A Nugget. Introduced by Eloraam */
    public static final OrePrefixes nugget = new OrePrefixBuilder("nugget").withDefaultLocalName("Nuggets")
        .withSuffix(" Nugget")
        .unifiable()
        .materialBased()
        .materialGenerationBits(METAL)
        .materialAmount(M / 9)
        .textureIndex(OrePrefixTextureID.NUGGET)
        .build();

    /** Special Alloys have this prefix. */
    public static final OrePrefixes plateAlloy = new OrePrefixBuilder("plateAlloy").withDefaultLocalName("Alloy Plates")
        .unifiable()
        .materialGenerationBits(METAL)
        .textureIndex(OrePrefixTextureID.PLATE)
        .build();

    public static final OrePrefixes plateSteamcraft = new OrePrefixBuilder("plateSteamcraft")
        .withDefaultLocalName("Steamcraft Plates")
        .materialGenerationBits(METAL)
        .textureIndex(OrePrefixTextureID.PLATE)
        .build();

    /** 9 Plates combined in one Item. */
    public static final OrePrefixes plateDense = new OrePrefixBuilder("plateDense").withDefaultLocalName("Dense Plates")
        .withPrefix("Dense ")
        .withSuffix(" Plate")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M * 9)
        .textureIndex(OrePrefixTextureID.PLATE_DENSE)
        .build();

    public static final OrePrefixes plateSuperdense = new OrePrefixBuilder("plateSuperdense")
        .withDefaultLocalName("Superdense Plates")
        .withPrefix("Superdense ")
        .withSuffix(" Plate")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M * 64)
        .textureIndex(OrePrefixTextureID.PLATE_SUPERDENSE)
        .build();

    public static final OrePrefixes plateQuintuple = new OrePrefixBuilder("plateQuintuple")
        .withDefaultLocalName("5x Plates")
        .withPrefix("Quintuple ")
        .withSuffix(" Plate")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M * 5)
        .textureIndex(OrePrefixTextureID.PLATE_QUINTUPLE)
        .build();

    public static final OrePrefixes plateQuadruple = new OrePrefixBuilder("plateQuadruple")
        .withDefaultLocalName("4x Plates")
        .withPrefix("Quadruple ")
        .withSuffix(" Plate")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M * 4)
        .textureIndex(OrePrefixTextureID.PLATE_QUADRUPLE)
        .build();

    public static final OrePrefixes plateTriple = new OrePrefixBuilder("plateTriple").withDefaultLocalName("3x Plates")
        .withPrefix("Triple ")
        .withSuffix(" Plate")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M * 3)
        .textureIndex(OrePrefixTextureID.PLATE_TRIPLE)
        .build();

    public static final OrePrefixes plateDouble = new OrePrefixBuilder("plateDouble").withDefaultLocalName("2x Plates")
        .withPrefix("Double ")
        .withSuffix(" Plate")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M * 2)
        .textureIndex(OrePrefixTextureID.PLATE_DOUBLE)
        .build();

    public static final OrePrefixes plate = new OrePrefixBuilder("plate").withDefaultLocalName("Plates")
        .withSuffix(" Plate")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL | GEM)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.PLATE)
        .build();

    /** Casing made of 1/2 Ingot/Dust */
    public static final OrePrefixes itemCasing = new OrePrefixBuilder("itemCasing").withDefaultLocalName("Casings")
        .withSuffix(" Casing")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL | GEM)
        .materialAmount(M / 2)
        .textureIndex(OrePrefixTextureID.CASING_SMALL)
        .build();

    /** Foil made of 1/4 Ingot/Dust. */
    public static final OrePrefixes foil = new OrePrefixBuilder("foil").withDefaultLocalName("Foils")
        .withSuffix(" Foil")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M / 4)
        .textureIndex(OrePrefixTextureID.FOIL)
        .build();

    /** Stick made of an Ingot. */
    public static final OrePrefixes stickLong = new OrePrefixBuilder("stickLong")
        .withDefaultLocalName("Long Sticks/Rods")
        .withPrefix("Long ")
        .withSuffix(" Rod")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL | GEM)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.STICK_LONG)
        .build();

    /** Stick made of half an Ingot. Introduced by Eloraam */
    public static final OrePrefixes stick = new OrePrefixBuilder("stick").withDefaultLocalName("Sticks/Rods")
        .withSuffix(" Rod")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL | GEM)
        .materialAmount(M / 2)
        .textureIndex(OrePrefixTextureID.STICK)
        .build();

    /** consisting out of one Nugget. */
    public static final OrePrefixes round = new OrePrefixBuilder("round").withDefaultLocalName("Rounds")
        .withSuffix(" Round")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M / 9)
        .textureIndex(OrePrefixTextureID.ROUND)
        .build();

    /** consisting out of 1/8 Ingot or 1/4 Stick. */
    public static final OrePrefixes bolt = new OrePrefixBuilder("bolt").withDefaultLocalName("Bolts")
        .withSuffix(" Bolt")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL | GEM)
        .materialAmount(M / 8)
        .textureIndex(OrePrefixTextureID.BOLT)
        .build();

    /** contain dusts */
    public static final OrePrefixes comb = new OrePrefixBuilder("comb").withDefaultLocalName("Combs")
        .withSuffix(" Comb")
        .materialGenerationBits(METAL | GEM)
        .materialAmount(M)
        .textureIndex(OrePrefixTextureID.COMB)
        .build();

    /** consisting out of a Bolt. */
    public static final OrePrefixes screw = new OrePrefixBuilder("screw").withDefaultLocalName("Screws")
        .withSuffix(" Screw")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL | GEM)
        .materialAmount(M / 8)
        .textureIndex(OrePrefixTextureID.SCREW)
        .build();

    /** consisting out of 1/2 Stick. */
    public static final OrePrefixes ring = new OrePrefixBuilder("ring").withDefaultLocalName("Rings")
        .withSuffix(" Ring")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M / 4)
        .textureIndex(OrePrefixTextureID.RING)
        .build();

    /** consisting out of 1 Fine Wire. */
    public static final OrePrefixes springSmall = new OrePrefixBuilder("springSmall")
        .withDefaultLocalName("Small Springs")
        .withPrefix("Small ")
        .withSuffix(" Spring")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M / 4)
        .textureIndex(OrePrefixTextureID.SPRING_SMALL)
        .build();

    /** consisting out of 2 Sticks. */
    public static final OrePrefixes spring = new OrePrefixBuilder("spring").withDefaultLocalName("Springs")
        .withSuffix(" Spring")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.SPRING)
        .build();

    /** consisting out of 1/8 Ingot or 1/4 Wire. */
    public static final OrePrefixes wireFine = new OrePrefixBuilder("wireFine").withDefaultLocalName("Fine Wires")
        .withPrefix("Fine ")
        .withSuffix(" Wire")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL)
        .materialAmount(M / 8)
        .textureIndex(OrePrefixTextureID.WIRE_FINE)
        .build();

    /** consisting out of 4 Plates, 1 Ring and 1 Screw. */
    public static final OrePrefixes rotor = new OrePrefixBuilder("rotor").withDefaultLocalName("Rotors")
        .withSuffix(" Rotor")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(GEAR)
        .materialAmount(M * 4 + M / 4)
        .textureIndex(OrePrefixTextureID.ROTOR)
        .build();

    public static final OrePrefixes gearGtSmall = new OrePrefixBuilder("gearGtSmall")
        .withDefaultLocalName("Small Gears")
        .withPrefix("Small ")
        .withSuffix(" Gear")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(GEAR)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.GEAR_SMALL)
        .build();

    /** Introduced by me because BuildCraft has ruined the gear Prefix... */
    public static final OrePrefixes gearGt = new OrePrefixBuilder("gearGt").withDefaultLocalName("Gears")
        .withSuffix(" Gear")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(GEAR)
        .materialAmount(M * 4)
        .textureIndex(OrePrefixTextureID.GEAR)
        .build();

    /** 3/4 of a Plate or Gem used to shape a Lense. Normally only used on Transparent Materials. */
    public static final OrePrefixes lens = new OrePrefixBuilder("lens").withDefaultLocalName("Lenses")
        .withSuffix(" Lens")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(GEM)
        .materialAmount((M * 3) / 4)
        .textureIndex(OrePrefixTextureID.LENS)
        .build();

    /** Hot Cell full of Plasma, which can be used in the Plasma Generator. */
    public static final OrePrefixes cellPlasma = new OrePrefixBuilder("cellPlasma")
        .withDefaultLocalName("Cells of Plasma")
        .withSuffix(" Plasma Cell")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialGenerationBits(PLASMA)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.CELL_PLASMA)
        .build();

    /** Hot Cell full of molten stuff, which can be used in the Plasma Generator. */
    public static final OrePrefixes cellMolten = new OrePrefixBuilder("cellMolten")
        .withDefaultLocalName("Cells of Molten stuff")
        .withPrefix("Molten ")
        .withSuffix(" Cell")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.CELL_PLASMA)
        .build();

    public static final OrePrefixes cell = new OrePrefixBuilder("cell").withDefaultLocalName("Cells")
        .withSuffix(" Cell")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .recyclable()
        .materialGenerationBits(CELL | EMPTY)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.CELL)
        .build();

    /** A vanilla Iron Bucket filled with the Material. */
    public static final OrePrefixes bucket = new OrePrefixBuilder("bucket").withDefaultLocalName("Buckets")
        .withSuffix(" Bucket")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .recyclable()
        .materialGenerationBits(CELL | EMPTY)
        .materialAmount(M * 1)
        .build();

    /** An Iguana Tweaks Clay Bucket filled with the Material. */
    public static final OrePrefixes bucketClay = new OrePrefixBuilder("bucketClay").withDefaultLocalName("Clay Buckets")
        .withSuffix(" Clay Bucket")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .recyclable()
        .materialGenerationBits(CELL | EMPTY)
        .materialAmount(M * 1)
        .build();

    /** Glass Bottle containing a Fluid. */
    public static final OrePrefixes bottle = new OrePrefixBuilder("bottle").withDefaultLocalName("Bottles")
        .withSuffix(" Bottle")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialGenerationBits(CELL | EMPTY)
        .defaultStackSize(1)
        .build();

    public static final OrePrefixes capsule = new OrePrefixBuilder("capsule").withDefaultLocalName("Capsules")
        .withSuffix(" Capsule")
        .materialBased()
        .selfReferencing()
        .container()
        .materialGenerationBits(CELL | EMPTY)
        .materialAmount(M * 1)
        .build();

    public static final OrePrefixes crystal = new OrePrefixBuilder("crystal").withDefaultLocalName("Crystals")
        .withSuffix(" Crystal")
        .materialBased()
        .recyclable()
        .materialGenerationBits(GEM)
        .materialAmount(M * 1)
        .build();

    public static final OrePrefixes bulletGtSmall = new OrePrefixBuilder("bulletGtSmall")
        .withDefaultLocalName("Small Bullets")
        .withPrefix("Small ")
        .withSuffix(" Bullet")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL | EMPTY)
        .materialAmount(M / 9)
        .build();

    public static final OrePrefixes bulletGtMedium = new OrePrefixBuilder("bulletGtMedium")
        .withDefaultLocalName("Medium Bullets")
        .withPrefix("Medium ")
        .withSuffix(" Bullet")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL | EMPTY)
        .materialAmount(M / 6)
        .build();

    public static final OrePrefixes bulletGtLarge = new OrePrefixBuilder("bulletGtLarge")
        .withDefaultLocalName("Large Bullets")
        .withPrefix("Large ")
        .withSuffix(" Bullet")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL | EMPTY)
        .materialAmount(M / 3)
        .build();

    /** consisting out of 2 Ingots. */
    public static final OrePrefixes toolHeadFile = new OrePrefixBuilder("toolHeadFile")
        .withDefaultLocalName("File Heads")
        .withSuffix(" File Head")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 2)
        .textureIndex(OrePrefixTextureID.TOOL_HEAD_FILE)
        .build();

    /** consisting out of 6 Ingots. */
    public static final OrePrefixes toolHeadHammer = new OrePrefixBuilder("toolHeadHammer")
        .withDefaultLocalName("Hammer Heads")
        .withSuffix(" Hammer Head")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 6)
        .textureIndex(OrePrefixTextureID.TOOL_HEAD_HAMMER)
        .build();

    /** consisting out of 2 Ingots. */
    public static final OrePrefixes toolHeadSaw = new OrePrefixBuilder("toolHeadSaw").withDefaultLocalName("Saw Blades")
        .withSuffix(" Saw Blade")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 2)
        .textureIndex(OrePrefixTextureID.TOOL_HEAD_SAW)
        .build();

    /** consisting out of 4 Ingots. */
    public static final OrePrefixes toolHeadBuzzSaw = new OrePrefixBuilder("toolHeadBuzzSaw")
        .withDefaultLocalName("Buzzsaw Blades")
        .withSuffix(" Buzzsaw Blade")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 4)
        .textureIndex(OrePrefixTextureID.TOOL_HEAD_BUZZ_SAW)
        .build();

    /** consisting out of 1 Ingots. */
    public static final OrePrefixes toolHeadScrewdriver = new OrePrefixBuilder("toolHeadScrewdriver")
        .withDefaultLocalName("Screwdriver Tips")
        .withSuffix(" Screwdriver Tip")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.TOOL_HEAD_SCREWDRIVER)
        .build();

    /** consisting out of 4 Ingots. */
    public static final OrePrefixes toolHeadDrill = new OrePrefixBuilder("toolHeadDrill")
        .withDefaultLocalName("Drill Tips")
        .withSuffix(" Drill Tip")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 4)
        .textureIndex(OrePrefixTextureID.TOOL_HEAD_DRILL)
        .build();

    /** consisting out of 2 Ingots. */
    public static final OrePrefixes toolHeadChainsaw = new OrePrefixBuilder("toolHeadChainsaw")
        .withDefaultLocalName("Chainsaw Tips")
        .withSuffix(" Chainsaw Tip")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 2)
        .textureIndex(OrePrefixTextureID.TOOL_HEAD_CHAINSAW)
        .build();

    /** consisting out of 4 Ingots. */
    public static final OrePrefixes toolHeadWrench = new OrePrefixBuilder("toolHeadWrench")
        .withDefaultLocalName("Wrench Tips")
        .withSuffix(" Wrench Tip")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 4)
        .textureIndex(OrePrefixTextureID.TOOL_HEAD_WRENCH)
        .build();

    /** consisting out of 6 Ingots. */
    public static final OrePrefixes turbineBlade = new OrePrefixBuilder("turbineBlade")
        .withDefaultLocalName("Turbine Blades")
        .withSuffix(" Turbine Blade")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 6)
        .textureIndex(OrePrefixTextureID.TURBINE_BLADE)
        .build();

    /** vanilly Sword */
    public static final OrePrefixes toolSword = new OrePrefixBuilder("toolSword").withDefaultLocalName("Swords")
        .materialBased()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 2)
        .defaultStackSize(1)
        .build();

    /** vanilly Pickaxe */
    public static final OrePrefixes toolPickaxe = new OrePrefixBuilder("toolPickaxe").withDefaultLocalName("Pickaxes")
        .materialBased()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 3)
        .defaultStackSize(1)
        .build();

    /** vanilly Shovel */
    public static final OrePrefixes toolShovel = new OrePrefixBuilder("toolShovel").withDefaultLocalName("Shovels")
        .materialBased()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 1)
        .defaultStackSize(1)
        .build();

    /** vanilly Axe */
    public static final OrePrefixes toolAxe = new OrePrefixBuilder("toolAxe").withDefaultLocalName("Axes")
        .materialBased()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 3)
        .defaultStackSize(1)
        .build();

    /** vanilly Hoe */
    public static final OrePrefixes toolHoe = new OrePrefixBuilder("toolHoe").withDefaultLocalName("Hoes")
        .materialBased()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 2)
        .defaultStackSize(1)
        .build();

    /** vanilly Shears */
    public static final OrePrefixes toolShears = new OrePrefixBuilder("toolShears").withDefaultLocalName("Shears")
        .materialBased()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 2)
        .defaultStackSize(1)
        .build();

    /**
     * toolPot, toolSkillet, toolSaucepan, toolBakeware, toolCuttingboard, toolMortarandpestle, toolMixingbowl,
     * toolJuicer
     */
    public static final OrePrefixes tool = new OrePrefixBuilder("tool").withDefaultLocalName("Tools")
        .enchantable()
        .materialGenerationBits(TOOL)
        .defaultStackSize(1)
        .build();

    public static final OrePrefixes compressedCobblestone = new OrePrefixBuilder("compressedCobblestone")
        .withDefaultLocalName("9^X Compressed Cobblestones")
        .build();

    public static final OrePrefixes compressedStone = new OrePrefixBuilder("compressedStone")
        .withDefaultLocalName("9^X Compressed Stones")
        .build();

    public static final OrePrefixes compressedDirt = new OrePrefixBuilder("compressedDirt")
        .withDefaultLocalName("9^X Compressed Dirt")
        .build();

    public static final OrePrefixes compressedGravel = new OrePrefixBuilder("compressedGravel")
        .withDefaultLocalName("9^X Compressed Gravel")
        .build();

    public static final OrePrefixes compressedSand = new OrePrefixBuilder("compressedSand")
        .withDefaultLocalName("9^X Compressed Sand")
        .build();

    /** Compressed Material, worth 1 Unit. Introduced by Galacticraft */
    public static final OrePrefixes compressed = new OrePrefixBuilder("compressed")
        .withDefaultLocalName("Compressed Materials")
        .withPrefix("Compressed ")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 3)
        .build();

    public static final OrePrefixes glass = new OrePrefixBuilder("glass").withDefaultLocalName("Glasses")
        .selfReferencing()
        .skipActiveUnification()
        .build();

    public static final OrePrefixes paneGlass = new OrePrefixBuilder("paneGlass").withDefaultLocalName("Glass Panes")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    public static final OrePrefixes blockGlass = new OrePrefixBuilder("blockGlass").withDefaultLocalName("Glass Blocks")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    public static final OrePrefixes blockWool = new OrePrefixBuilder("blockWool").withDefaultLocalName("Wool Blocks")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** IGNORE */
    public static final OrePrefixes block_ = new OrePrefixBuilder("block_").withDefaultLocalName("Random Blocks")
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** A decorative sheet metal block. */
    public static final OrePrefixes sheetmetal = new OrePrefixBuilder("sheetmetal")
        .withDefaultLocalName("Sheetmetal Blocks")
        .withNameKey("gt.component.sheetmetal")
        .unifiable()
        .recyclable()
        .materialBased()
        .materialAmount(M * 2)
        .materialGenerationBits(METAL)
        .defaultStackSize(OTHER_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.BLOCK_SHEETMETAL)
        .build();

    /** Storage Block consisting out of 9 Ingots/Gems/Dusts. Introduced by CovertJaguar */
    public static final OrePrefixes block = new OrePrefixBuilder("block").withDefaultLocalName("Storage Blocks")
        .withPrefix("Block of ")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 9)
        .defaultStackSize(OTHER_STACK_SIZE)
        .textureIndex(OrePrefixTextureID.BLOCK)
        .build();

    /** Special Prefix used mainly for the Crafting Handler. */
    public static final OrePrefixes craftingTool = new OrePrefixBuilder("craftingTool")
        .withDefaultLocalName("Crafting Tools")
        .enchantable()
        .build();

    /** Special Prefix used mainly for the Crafting Handler. */
    public static final OrePrefixes crafting = new OrePrefixBuilder("crafting")
        .withDefaultLocalName("Crafting Ingredients")
        .build();

    /** Special Prefix used mainly for the Crafting Handler. */
    public static final OrePrefixes craft = new OrePrefixBuilder("craft").withDefaultLocalName("Crafting Stuff?")
        .build();

    /** Prefix used for Logs. Usually as "logWood". Introduced by Eloraam */
    public static final OrePrefixes log = new OrePrefixBuilder("log").withDefaultLocalName("Logs")
        .defaultStackSize(LOG_STACK_SIZE)
        .build();

    /** Prefix used for Slabs. Usually as "slabWood" or "slabStone". Introduced by SirSengir */
    public static final OrePrefixes slab = new OrePrefixBuilder("slab").withDefaultLocalName("Slabs")
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** Prefix used for Stairs. Usually as "stairWood" or "stairStone". Introduced by SirSengir */
    public static final OrePrefixes stair = new OrePrefixBuilder("stair").withDefaultLocalName("Stairs")
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** Prefix used for Fences. Usually as "fenceWood". Introduced by Forge */
    public static final OrePrefixes fence = new OrePrefixBuilder("fence").withDefaultLocalName("Fences")
        .build();

    /** Prefix for Planks. Usually "plankWood". Introduced by Eloraam */
    public static final OrePrefixes plank = new OrePrefixBuilder("plank").withDefaultLocalName("Planks")
        .defaultStackSize(PLANK_STACK_SIZE)
        .build();

    /** Prefix for Saplings. */
    public static final OrePrefixes treeSapling = new OrePrefixBuilder("treeSapling").withDefaultLocalName("Saplings")
        .selfReferencing()
        .defaultStackSize(LOG_STACK_SIZE)
        .build();

    /** Prefix for Leaves. */
    public static final OrePrefixes treeLeaves = new OrePrefixBuilder("treeLeaves").withDefaultLocalName("Leaves")
        .selfReferencing()
        .defaultStackSize(LOG_STACK_SIZE)
        .build();

    /** Prefix for Tree Parts. */
    public static final OrePrefixes tree = new OrePrefixBuilder("tree").withDefaultLocalName("Tree Parts")
        .build();

    /** Cobblestone Prefix for all Cobblestones. */
    public static final OrePrefixes stoneCobble = new OrePrefixBuilder("stoneCobble")
        .withDefaultLocalName("Cobblestones")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** Smoothstone Prefix. */
    public static final OrePrefixes stoneSmooth = new OrePrefixBuilder("stoneSmooth")
        .withDefaultLocalName("Smoothstones")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** Mossy Stone Bricks. */
    public static final OrePrefixes stoneMossyBricks = new OrePrefixBuilder("stoneMossyBricks")
        .withDefaultLocalName("mossy Stone Bricks")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** Mossy Cobble. */
    public static final OrePrefixes stoneMossy = new OrePrefixBuilder("stoneMossy").withDefaultLocalName("Mossy Stones")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** Stone Bricks. */
    public static final OrePrefixes stoneBricks = new OrePrefixBuilder("stoneBricks")
        .withDefaultLocalName("Stone Bricks")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** Cracked Bricks. */
    public static final OrePrefixes stoneCracked = new OrePrefixBuilder("stoneCracked")
        .withDefaultLocalName("Cracked Stones")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** Chiseled Stone. */
    public static final OrePrefixes stoneChiseled = new OrePrefixBuilder("stoneChiseled")
        .withDefaultLocalName("Chiseled Stones")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** Prefix to determine which kind of Rock this is. */
    public static final OrePrefixes stone = new OrePrefixBuilder("stone").withDefaultLocalName("Stones")
        .materialBased()
        .selfReferencing()
        .skipActiveUnification()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    public static final OrePrefixes cobblestone = new OrePrefixBuilder("cobblestone").materialBased()
        .withDefaultLocalName("Cobblestones")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** Prefix to determine which kind of Rock this is. */
    public static final OrePrefixes rock = new OrePrefixBuilder("rock").withDefaultLocalName("Rocks")
        .materialBased()
        .selfReferencing()
        .skipActiveUnification()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    public static final OrePrefixes record = new OrePrefixBuilder("record").withDefaultLocalName("Records")
        .selfReferencing()
        .defaultStackSize(1)
        .build();

    public static final OrePrefixes rubble = new OrePrefixBuilder("rubble").withDefaultLocalName("Rubbles")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .build();

    public static final OrePrefixes scraps = new OrePrefixBuilder("scraps").withDefaultLocalName("Scraps")
        .unifiable()
        .materialBased()
        .build();

    public static final OrePrefixes scrap = new OrePrefixBuilder("scrap").withDefaultLocalName("Scraps")
        .build();

    /** IGNORE */
    public static final OrePrefixes item_ = new OrePrefixBuilder("item_").withDefaultLocalName("Items")
        .build();

    /** Random Item. Introduced by Alblaka */
    public static final OrePrefixes item = new OrePrefixBuilder("item").withDefaultLocalName("Items")
        .build();

    /** Used for Books of any kind. */
    public static final OrePrefixes book = new OrePrefixBuilder("book").withDefaultLocalName("Books")
        .build();

    /** Used for Papers of any kind. */
    public static final OrePrefixes paper = new OrePrefixBuilder("paper").withDefaultLocalName("Papers")
        .build();

    /** Used for the 16 dyes. Introduced by Eloraam */
    public static final OrePrefixes dye = new OrePrefixBuilder("dye").withDefaultLocalName("Dyes")
        .selfReferencing()
        .build();

    /** Used for the 16 colors of Stained Clay. Introduced by Forge */
    public static final OrePrefixes stainedClay = new OrePrefixBuilder("stainedClay")
        .withDefaultLocalName("Stained Clays")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    /** vanilly Helmet */
    public static final OrePrefixes armorHelmet = new OrePrefixBuilder("armorHelmet").withDefaultLocalName("Helmets")
        .materialBased()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 5)
        .defaultStackSize(1)
        .build();

    /** vanilly Chestplate */
    public static final OrePrefixes armorChestplate = new OrePrefixBuilder("armorChestplate").materialBased()
        .withDefaultLocalName("Chestplates")
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 8)
        .defaultStackSize(1)
        .build();

    /** vanilly Pants */
    public static final OrePrefixes armorLeggings = new OrePrefixBuilder("armorLeggings").materialBased()
        .withDefaultLocalName("Leggings")
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 7)
        .defaultStackSize(1)
        .build();

    /** vanilly Boots */
    public static final OrePrefixes armorBoots = new OrePrefixBuilder("armorBoots").withDefaultLocalName("Boots")
        .materialBased()
        .recyclable()
        .enchantable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 4)
        .defaultStackSize(1)
        .build();

    public static final OrePrefixes armor = new OrePrefixBuilder("armor").withDefaultLocalName("Armor Parts")
        .enchantable()
        .materialGenerationBits(TOOL)
        .defaultStackSize(1)
        .build();

    public static final OrePrefixes frameGt = new OrePrefixBuilder("frameGt").withDefaultLocalName("Frame Boxes")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 2)
        .textureIndex(OrePrefixTextureID.FRAME)
        .build();

    public static final OrePrefixes pipeTiny = new OrePrefixBuilder("pipeTiny").withDefaultLocalName("Tiny Pipes")
        .withPrefix("Tiny ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M / 2)
        .textureIndex(OrePrefixTextureID.PIPE_TINY)
        .build();

    public static final OrePrefixes pipeSmall = new OrePrefixBuilder("pipeSmall").withDefaultLocalName("Small Pipes")
        .withPrefix("Small ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.PIPE_SMALL)
        .build();

    public static final OrePrefixes pipeMedium = new OrePrefixBuilder("pipeMedium").withDefaultLocalName("Medium Pipes")
        .withPrefix("Medium ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 3)
        .textureIndex(OrePrefixTextureID.PIPE_MEDIUM)
        .build();

    public static final OrePrefixes pipeLarge = new OrePrefixBuilder("pipeLarge").withDefaultLocalName("Large pipes")
        .withPrefix("Large ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 6)
        .textureIndex(OrePrefixTextureID.PIPE_LARGE)
        .build();

    public static final OrePrefixes pipeHuge = new OrePrefixBuilder("pipeHuge").withDefaultLocalName("Huge Pipes")
        .withPrefix("Huge ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 12)
        .textureIndex(OrePrefixTextureID.PIPE_HUGE)
        .build();

    public static final OrePrefixes pipeQuadruple = new OrePrefixBuilder("pipeQuadruple")
        .withDefaultLocalName("Quadruple Pipes")
        .withPrefix("Quadruple ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 12)
        .textureIndex(OrePrefixTextureID.PIPE_QUADRUPLE)
        .build();

    public static final OrePrefixes pipeNonuple = new OrePrefixBuilder("pipeNonuple")
        .withDefaultLocalName("Nonuple Pipes")
        .withPrefix("Nonuple ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 9)
        .textureIndex(OrePrefixTextureID.PIPE_NONUPLE)
        .build();

    public static final OrePrefixes pipeRestrictiveTiny = new OrePrefixBuilder("pipeRestrictiveTiny")
        .withDefaultLocalName("Tiny Restrictive Pipes")
        .withPrefix("Tiny Restrictive ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M / 2)
        .textureIndex(OrePrefixTextureID.PIPE_TINY)
        .build();

    public static final OrePrefixes pipeRestrictiveSmall = new OrePrefixBuilder("pipeRestrictiveSmall")
        .withDefaultLocalName("Small Restrictive Pipes")
        .withPrefix("Small Restrictive ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.PIPE_SMALL)
        .build();

    public static final OrePrefixes pipeRestrictiveMedium = new OrePrefixBuilder("pipeRestrictiveMedium")
        .withDefaultLocalName("Medium Restrictive Pipes")
        .withPrefix("Medium Restrictive ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 3)
        .textureIndex(OrePrefixTextureID.PIPE_MEDIUM)
        .build();

    public static final OrePrefixes pipeRestrictiveLarge = new OrePrefixBuilder("pipeRestrictiveLarge")
        .withDefaultLocalName("Large Restrictive Pipes")
        .withPrefix("Large Restrictive ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 6)
        .textureIndex(OrePrefixTextureID.PIPE_LARGE)
        .build();

    public static final OrePrefixes pipeRestrictiveHuge = new OrePrefixBuilder("pipeRestrictiveHuge")
        .withDefaultLocalName("Huge Restrictive Pipes")
        .withPrefix("Huge Restrictive ")
        .withSuffix(" Pipe")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .recyclable()
        .materialAmount(M * 12)
        .textureIndex(OrePrefixTextureID.PIPE_HUGE)
        .build();

    public static final OrePrefixes pipe = new OrePrefixBuilder("pipe").withDefaultLocalName("Pipes")
        .withSuffix(" Pipe")
        .unifiable()
        .textureIndex(OrePrefixTextureID.PIPE_SIDE)
        .build();

    public static final OrePrefixes wireGt16 = new OrePrefixBuilder("wireGt16").withDefaultLocalName("16x Wires")
        .withPrefix("16x ")
        .withSuffix(" Wire")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 8)
        .build();

    public static final OrePrefixes wireGt12 = new OrePrefixBuilder("wireGt12").withDefaultLocalName("12x Wires")
        .withPrefix("12x ")
        .withSuffix(" Wire")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 6)
        .build();

    public static final OrePrefixes wireGt08 = new OrePrefixBuilder("wireGt08").withDefaultLocalName("8x Wires")
        .withPrefix("8x ")
        .withSuffix(" Wire")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 4)
        .build();

    public static final OrePrefixes wireGt04 = new OrePrefixBuilder("wireGt04").withDefaultLocalName("4x Wires")
        .withPrefix("4x ")
        .withSuffix(" Wire")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 2)
        .build();

    public static final OrePrefixes wireGt02 = new OrePrefixBuilder("wireGt02").withDefaultLocalName("2x Wires")
        .withPrefix("2x ")
        .withSuffix(" Wire")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 1)
        .build();

    public static final OrePrefixes wireGt01 = new OrePrefixBuilder("wireGt01").withDefaultLocalName("1x Wires")
        .withPrefix("1x ")
        .withSuffix(" Wire")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M / 2)
        .build();

    public static final OrePrefixes cableGt16 = new OrePrefixBuilder("cableGt16").withDefaultLocalName("16x Cables")
        .withPrefix("16x ")
        .withSuffix(" Cable")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 8)
        .build();

    public static final OrePrefixes cableGt12 = new OrePrefixBuilder("cableGt12").withDefaultLocalName("12x Cables")
        .withPrefix("12x ")
        .withSuffix(" Cable")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 6)
        .build();

    public static final OrePrefixes cableGt08 = new OrePrefixBuilder("cableGt08").withDefaultLocalName("8x Cables")
        .withPrefix("8x ")
        .withSuffix(" Cable")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 4)
        .build();

    public static final OrePrefixes cableGt04 = new OrePrefixBuilder("cableGt04").withDefaultLocalName("4x Cables")
        .withPrefix("4x ")
        .withSuffix(" Cable")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 2)
        .build();

    public static final OrePrefixes cableGt02 = new OrePrefixBuilder("cableGt02").withDefaultLocalName("2x Cables")
        .withPrefix("2x ")
        .withSuffix(" Cable")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M * 1)
        .build();

    public static final OrePrefixes cableGt01 = new OrePrefixBuilder("cableGt01").withDefaultLocalName("1x Cables")
        .withPrefix("1x ")
        .withSuffix(" Cable")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialAmount(M / 2)
        .build();

    /*
     * Electric Components. usual Materials for this are: Primitive (Tier 1) Basic (Tier 2) as used by UE as well : IC2
     * Circuit and RE-Battery Good (Tier 3) Advanced (Tier 4) as used by UE as well : Advanced Circuit, Advanced Battery
     * and Lithium Battery Data (Tier 5) : Data Storage Circuit Elite (Tier 6) as used by UE as well : Energy Crystal
     * and Data Control Circuit Master (Tier 7) : Energy Flow Circuit and Lapotron Crystal Ultimate (Tier 8) : Data Orb
     * and Lapotronic Energy Orb Infinite (Cheaty)
     */
    public static final OrePrefixes batterySingleuse = new OrePrefixBuilder("batterySingleuse").materialBased()
        .withDefaultLocalName("Single Use Batteries")
        .build();

    public static final OrePrefixes battery = new OrePrefixBuilder("battery").withDefaultLocalName("Reusable Batteries")
        .materialBased()
        .build();

    public static final OrePrefixes circuit = new OrePrefixBuilder("circuit").withDefaultLocalName("Circuits")
        .unifiable()
        .materialBased()
        .build();

    /** Introduced by Buildcraft */
    public static final OrePrefixes chipset = new OrePrefixBuilder("chipset").withDefaultLocalName("Chipsets")
        .unifiable()
        .materialBased()
        .build();

    /** A whole Computer. "computerMaster" = ComputerCube */
    public static final OrePrefixes computer = new OrePrefixBuilder("computer").withDefaultLocalName("Computers")
        .unifiable()
        .materialBased()
        .skipActiveUnification()
        .build();

    // random known prefixes without special abilities.
    public static final OrePrefixes skull = new OrePrefixBuilder("skull").withDefaultLocalName("Skulls")
        .build();

    public static final OrePrefixes plating = new OrePrefixBuilder("plating").withDefaultLocalName("Platings")
        .build();

    public static final OrePrefixes dinosaur = new OrePrefixBuilder("dinosaur").withDefaultLocalName("Dinosaurs")
        .build();

    public static final OrePrefixes travelgear = new OrePrefixBuilder("travelgear").withDefaultLocalName("Travel Gear")
        .build();

    public static final OrePrefixes bauble = new OrePrefixBuilder("bauble").withDefaultLocalName("Baubles")
        .build();

    public static final OrePrefixes cluster = new OrePrefixBuilder("cluster").withDefaultLocalName("Clusters")
        .build();

    public static final OrePrefixes grafter = new OrePrefixBuilder("grafter").withDefaultLocalName("Grafters")
        .build();

    public static final OrePrefixes scoop = new OrePrefixBuilder("scoop").withDefaultLocalName("Scoops")
        .build();

    public static final OrePrefixes frame = new OrePrefixBuilder("frame").withDefaultLocalName("Frames")
        .build();

    public static final OrePrefixes tome = new OrePrefixBuilder("tome").withDefaultLocalName("Tomes")
        .build();

    public static final OrePrefixes junk = new OrePrefixBuilder("junk").withDefaultLocalName("Junk")
        .build();

    public static final OrePrefixes bee = new OrePrefixBuilder("bee").withDefaultLocalName("Bees")
        .build();

    public static final OrePrefixes rod = new OrePrefixBuilder("rod").withDefaultLocalName("Rods")
        .build();

    public static final OrePrefixes dirt = new OrePrefixBuilder("dirt").withDefaultLocalName("Dirts")
        .build();

    public static final OrePrefixes sand = new OrePrefixBuilder("sand").withDefaultLocalName("Sands")
        .selfReferencing()
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    public static final OrePrefixes grass = new OrePrefixBuilder("grass").withDefaultLocalName("Grasses")
        .build();

    public static final OrePrefixes gravel = new OrePrefixBuilder("gravel").withDefaultLocalName("Gravels")
        .build();

    public static final OrePrefixes mushroom = new OrePrefixBuilder("mushroom").withDefaultLocalName("Mushrooms")
        .build();

    /** Introduced by Eloraam */
    public static final OrePrefixes wood = new OrePrefixBuilder("wood").withDefaultLocalName("Woods")
        .defaultStackSize(LOG_STACK_SIZE)
        .build();

    public static final OrePrefixes drop = new OrePrefixBuilder("drop").withDefaultLocalName("Drops")
        .build();

    public static final OrePrefixes fuel = new OrePrefixBuilder("fuel").withDefaultLocalName("Fuels")
        .build();

    public static final OrePrefixes panel = new OrePrefixBuilder("panel").withDefaultLocalName("Panels")
        .build();

    public static final OrePrefixes brick = new OrePrefixBuilder("brick").withDefaultLocalName("Bricks")
        .build();

    public static final OrePrefixes chunk = new OrePrefixBuilder("chunk").withDefaultLocalName("Chunks")
        .build();

    public static final OrePrefixes wire = new OrePrefixBuilder("wire").withDefaultLocalName("Wires")
        .skipActiveUnification()
        .build();

    public static final OrePrefixes seed = new OrePrefixBuilder("seed").withDefaultLocalName("Seeds")
        .build();

    public static final OrePrefixes reed = new OrePrefixBuilder("reed").withDefaultLocalName("Reeds")
        .build();

    public static final OrePrefixes sheetDouble = new OrePrefixBuilder("sheetDouble").withDefaultLocalName("2x Sheets")
        .build();

    public static final OrePrefixes sheet = new OrePrefixBuilder("sheet").withDefaultLocalName("Sheets")
        .build();

    public static final OrePrefixes crop = new OrePrefixBuilder("crop").withDefaultLocalName("Crops")
        .build();

    public static final OrePrefixes plant = new OrePrefixBuilder("plant").withDefaultLocalName("Plants")
        .build();

    public static final OrePrefixes coin = new OrePrefixBuilder("coin").withDefaultLocalName("Coins")
        .build();

    public static final OrePrefixes lumar = new OrePrefixBuilder("lumar").withDefaultLocalName("Lumars")
        .build();

    public static final OrePrefixes ground = new OrePrefixBuilder("ground").withDefaultLocalName("Grounded Stuff")
        .build();

    public static final OrePrefixes cable = new OrePrefixBuilder("cable").withDefaultLocalName("Cables")
        .build();

    public static final OrePrefixes component = new OrePrefixBuilder("component").withDefaultLocalName("Components")
        .build();

    public static final OrePrefixes wax = new OrePrefixBuilder("wax").withDefaultLocalName("Waxes")
        .build();

    public static final OrePrefixes wall = new OrePrefixBuilder("wall").withDefaultLocalName("Walls")
        .build();

    public static final OrePrefixes tube = new OrePrefixBuilder("tube").withDefaultLocalName("Tubes")
        .build();

    public static final OrePrefixes list = new OrePrefixBuilder("list").withDefaultLocalName("Lists")
        .build();

    public static final OrePrefixes food = new OrePrefixBuilder("food").withDefaultLocalName("Foods")
        .build();

    /** Introduced by SirSengir */
    public static final OrePrefixes gear = new OrePrefixBuilder("gear").withDefaultLocalName("Gears")
        .build();

    public static final OrePrefixes coral = new OrePrefixBuilder("coral").withDefaultLocalName("Corals")
        .build();

    public static final OrePrefixes flower = new OrePrefixBuilder("flower").withDefaultLocalName("Flowers")
        .build();

    public static final OrePrefixes storage = new OrePrefixBuilder("storage").withDefaultLocalName("Storages")
        .build();

    public static final OrePrefixes material = new OrePrefixBuilder("material").withDefaultLocalName("Materials")
        .build();

    public static final OrePrefixes plasma = new OrePrefixBuilder("plasma").withDefaultLocalName("Plasmas")
        .build();

    public static final OrePrefixes element = new OrePrefixBuilder("element").withDefaultLocalName("Elements")
        .build();

    public static final OrePrefixes molecule = new OrePrefixBuilder("molecule").withDefaultLocalName("Molecules")
        .build();

    public static final OrePrefixes wafer = new OrePrefixBuilder("wafer").withDefaultLocalName("Wafers")
        .build();

    public static final OrePrefixes orb = new OrePrefixBuilder("orb").withDefaultLocalName("Orbs")
        .build();

    public static final OrePrefixes handle = new OrePrefixBuilder("handle").withDefaultLocalName("Handles")
        .build();

    public static final OrePrefixes blade = new OrePrefixBuilder("blade").withDefaultLocalName("Blades")
        .build();

    public static final OrePrefixes head = new OrePrefixBuilder("head").withDefaultLocalName("Heads")
        .build();

    public static final OrePrefixes motor = new OrePrefixBuilder("motor").withDefaultLocalName("Motors")
        .build();

    public static final OrePrefixes bit = new OrePrefixBuilder("bit").withDefaultLocalName("Bits")
        .build();

    public static final OrePrefixes shears = new OrePrefixBuilder("shears").withDefaultLocalName("Shears")
        .build();

    public static final OrePrefixes turbine = new OrePrefixBuilder("turbine").withDefaultLocalName("Turbines")
        .build();

    public static final OrePrefixes fertilizer = new OrePrefixBuilder("fertilizer").withDefaultLocalName("Fertilizers")
        .build();

    public static final OrePrefixes chest = new OrePrefixBuilder("chest").withDefaultLocalName("Chests")
        .build();

    public static final OrePrefixes raw = new OrePrefixBuilder("raw").withDefaultLocalName("Raw Things")
        .build();

    public static final OrePrefixes stainedGlass = new OrePrefixBuilder("stainedGlass")
        .withDefaultLocalName("Stained Glasses")
        .build();

    public static final OrePrefixes mystic = new OrePrefixBuilder("mystic").withDefaultLocalName("Mystic Stuff")
        .build();

    public static final OrePrefixes mana = new OrePrefixBuilder("mana").withDefaultLocalName("Mana Stuff")
        .build();

    public static final OrePrefixes rune = new OrePrefixBuilder("rune").withDefaultLocalName("Runes")
        .build();

    public static final OrePrefixes petal = new OrePrefixBuilder("petal").withDefaultLocalName("Petals")
        .build();

    public static final OrePrefixes pearl = new OrePrefixBuilder("pearl").withDefaultLocalName("Pearls")
        .build();

    public static final OrePrefixes powder = new OrePrefixBuilder("powder").withDefaultLocalName("Powders")
        .build();

    public static final OrePrefixes soulsand = new OrePrefixBuilder("soulsand").withDefaultLocalName("Soulsands")
        .build();

    public static final OrePrefixes obsidian = new OrePrefixBuilder("obsidian").withDefaultLocalName("Obsidians")
        .build();

    public static final OrePrefixes glowstone = new OrePrefixBuilder("glowstone").withDefaultLocalName("Glowstones")
        .build();

    public static final OrePrefixes beans = new OrePrefixBuilder("beans").withDefaultLocalName("Beans")
        .build();

    public static final OrePrefixes br = new OrePrefixBuilder("br").withDefaultLocalName("br")
        .build();

    public static final OrePrefixes essence = new OrePrefixBuilder("essence").withDefaultLocalName("Essences")
        .build();

    public static final OrePrefixes alloy = new OrePrefixBuilder("alloy").withDefaultLocalName("Alloys")
        .build();

    public static final OrePrefixes cooking = new OrePrefixBuilder("cooking").withDefaultLocalName("Cooked Things")
        .build();

    public static final OrePrefixes elven = new OrePrefixBuilder("elven").withDefaultLocalName("Elven Stuff")
        .build();

    public static final OrePrefixes reactor = new OrePrefixBuilder("reactor").withDefaultLocalName("Reactors")
        .build();

    public static final OrePrefixes mffs = new OrePrefixBuilder("mffs").withDefaultLocalName("MFFS")
        .build();

    public static final OrePrefixes projred = new OrePrefixBuilder("projred").withDefaultLocalName("Project Red")
        .build();

    public static final OrePrefixes ganys = new OrePrefixBuilder("ganys").withDefaultLocalName("Ganys Stuff")
        .build();

    public static final OrePrefixes liquid = new OrePrefixBuilder("liquid").withDefaultLocalName("Liquids")
        .build();

    public static final OrePrefixes bars = new OrePrefixBuilder("bars").withDefaultLocalName("Bars")
        .build();

    public static final OrePrefixes bar = new OrePrefixBuilder("bar").withDefaultLocalName("Bars")
        .build();

    /** Reverse Head consisting out of 6 Ingots. */
    public static final OrePrefixes toolHeadMallet = new OrePrefixBuilder("toolHeadMallet")
        .withDefaultLocalName("Mallet Heads")
        .withSuffix(" Mallet Head")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 6)
        .textureIndex(OrePrefixTextureID.TOOL_HEAD_MALLET)
        .build();

    /** Reverse Stick made of half an Ingot. Introduced by Eloraam */
    public static final OrePrefixes handleMallet = new OrePrefixBuilder("handleMallet")
        .withDefaultLocalName("Mallet Handle")
        .withSuffix(" Handle")
        .unifiable()
        .materialBased()
        .recyclable()
        .materialGenerationBits(METAL | GEM)
        .materialAmount(M / 2)
        .textureIndex(OrePrefixTextureID.HANDLE_MALLET)
        .build();

    // Cracked fluids
    public static final OrePrefixes cellHydroCracked1 = new OrePrefixBuilder("cellHydroCracked1")
        .withDefaultLocalName("Cells")
        .withPrefix("Lightly Hydro-Cracked ")
        .withSuffix(" Cell")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.CELL)
        .build();

    public static final OrePrefixes cellHydroCracked2 = new OrePrefixBuilder("cellHydroCracked2")
        .withDefaultLocalName("Cells")
        .withPrefix("Moderately Hydro-Cracked ")
        .withSuffix(" Cell")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.CELL)
        .build();

    public static final OrePrefixes cellHydroCracked3 = new OrePrefixBuilder("cellHydroCracked3")
        .withDefaultLocalName("Cells")
        .withPrefix("Severely Hydro-Cracked ")
        .withSuffix(" Cell")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.CELL)
        .build();

    public static final OrePrefixes cellSteamCracked1 = new OrePrefixBuilder("cellSteamCracked1")
        .withDefaultLocalName("Cells")
        .withPrefix("Lightly Steam-Cracked ")
        .withSuffix(" Cell")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.CELL)
        .build();

    public static final OrePrefixes cellSteamCracked2 = new OrePrefixBuilder("cellSteamCracked2")
        .withDefaultLocalName("Cells")
        .withPrefix("Moderately Steam-Cracked ")
        .withSuffix(" Cell")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.CELL)
        .build();

    public static final OrePrefixes cellSteamCracked3 = new OrePrefixBuilder("cellSteamCracked3")
        .withDefaultLocalName("Cells")
        .withPrefix("Severely Steam-Cracked ")
        .withSuffix(" Cell")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialAmount(M * 1)
        .textureIndex(OrePrefixTextureID.CELL)
        .build();

    public static final OrePrefixes componentCircuit = new OrePrefixBuilder("componentCircuit")
        .withDefaultLocalName("Circuit Parts")
        .unifiable()
        .materialBased()
        .build();

    public static final OrePrefixes apiaryUpgrade = new OrePrefixBuilder("apiaryUpgrade")
        .withDefaultLocalName("Industrial Apiary Upgrade")
        .selfReferencing()
        .build();

    public static final OrePrefixes beeComb = new OrePrefixBuilder("beeComb").withDefaultLocalName("Bee Combs")
        .unifiable()
        .selfReferencing()
        .build();

    public static final OrePrefixes nanite = new OrePrefixBuilder("nanite").withDefaultLocalName("Nanites")
        .withSuffix(" Nanites")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .textureIndex(OrePrefixTextureID.NANITES)
        .build();

    // migrated from GT++
    public static final OrePrefixes milled = new OrePrefixBuilder("milled").withDefaultLocalName("Milled Ores")
        .withPrefix("Milled ")
        .withSuffix(" Ore")
        .unifiable()
        .materialBased()
        .materialGenerationBits(ORE)
        .defaultStackSize(ORE_STACK_SIZE)
        .build();

    // migrated from bartworks
    public static final OrePrefixes blockCasing = new OrePrefixBuilder("blockCasing")
        .withDefaultLocalName("A Casing block for a Multiblock-Machine")
        .withPrefix("Bolted ")
        .withSuffix(" Casing")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialAmount(M * 9)
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    public static final OrePrefixes blockCasingAdvanced = new OrePrefixBuilder("blockCasingAdvanced")
        .withDefaultLocalName("An Advanced Casing block for a Multiblock-Machine")
        .withPrefix("Rebolted ")
        .withSuffix(" Casing")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialAmount(M * 9)
        .defaultStackSize(OTHER_STACK_SIZE)
        .build();

    public static final OrePrefixes capsuleMolten = new OrePrefixBuilder("capsuleMolten")
        .withDefaultLocalName("Capsule of Molten stuff")
        .withPrefix("Molten ")
        .withSuffix(" Capsule")
        .unifiable()
        .materialBased()
        .selfReferencing()
        .container()
        .materialGenerationBits(TOOL)
        .materialAmount(M * 1)
        .build();

    // subatomic particles
    public static final OrePrefixes particle = new OrePrefixBuilder("particle")
        .withDefaultLocalName("A Subatomic Particle")
        .selfReferencing()
        .build();

    // Beamline Masks
    public static final OrePrefixes mask = new OrePrefixBuilder("mask").withDefaultLocalName("A Photolithographic Mask")
        .selfReferencing()
        .defaultStackSize(1)
        .build();

    public static final OrePrefixes wrapCircuit = new OrePrefixBuilder("wrapCircuit")
        .withDefaultLocalName("A Circuit Wrap")
        .selfReferencing()
        .build();

    public static final OrePrefixes[] VALUES;

    // Convert the prefix list to a fixed array and release the temporary reference.
    static {
        VALUES = VALUES_LIST.toArray(new OrePrefixes[0]);
        VALUES_LIST = null;
    }

    private final @NotNull String name;
    private final @NotNull String defaultLocalName;
    private final @NotNull String materialPrefix;
    private final @NotNull String materialPostfix;
    private final @Nullable String nameKey;
    private final boolean isUnifiable;
    private final boolean isMaterialBased;
    private final boolean isSelfReferencing;
    private final boolean isContainer;
    private final boolean skipActiveUnification;
    private final boolean isRecyclable;
    private final boolean isEnchantable;
    private final int materialGenerationBits;
    private final long materialAmount;
    private final int defaultStackSize;
    private final int textureIndex;

    OrePrefixes(
        // spotless:off
        @NotNull String name,
        @NotNull String defaultLocalName,
        @NotNull String materialPrefix,
        @NotNull String materialPostfix,
        @Nullable String nameKey,
        boolean isUnifiable,
        boolean isMaterialBased,
        boolean isSelfReferencing,
        boolean isContainer,
        boolean skipActiveUnification,
        boolean isRecyclable,
        boolean isEnchantable,
        int materialGenerationBits,
        long materialAmount,
        int defaultStackSize,
        int textureIndex
        // spotless:on
    ) {
        this.name = name;
        this.defaultLocalName = defaultLocalName;
        this.materialPrefix = materialPrefix;
        this.materialPostfix = materialPostfix;
        this.nameKey = nameKey;
        this.isUnifiable = isUnifiable;
        this.isMaterialBased = isMaterialBased;
        this.isSelfReferencing = isSelfReferencing;
        this.isContainer = isContainer;
        this.skipActiveUnification = skipActiveUnification;
        this.isRecyclable = isRecyclable;
        this.isEnchantable = isEnchantable;
        this.materialGenerationBits = materialGenerationBits;
        this.materialAmount = materialAmount;
        this.defaultStackSize = defaultStackSize;
        this.textureIndex = textureIndex;

        addAspectForName();

        VALUES_LIST.add(this);
    }

    private void addAspectForName() {
        // spotless:off
        if      (name.startsWith(     "ore")) addAspect(       TCAspects.TERRA, 1);
        else if (name.startsWith(    "wire")) addAspect(    TCAspects.ELECTRUM, 1);
        else if (name.startsWith(   "cable")) addAspect(    TCAspects.ELECTRUM, 1);
        else if (name.startsWith(    "dust")) addAspect(    TCAspects.PERDITIO, 1);
        else if (name.startsWith( "crushed")) addAspect(    TCAspects.PERFODIO, 1);
        else if (name.startsWith(   "ingot")) addAspect(    TCAspects.METALLUM, 1);
        else if (name.startsWith(  "nugget")) addAspect(    TCAspects.METALLUM, 1);
        else if (name.startsWith(   "armor")) addAspect(     TCAspects.TUTAMEN, 1);
        else if (name.startsWith(   "stone")) addAspect(       TCAspects.TERRA, 1);
        else if (name.startsWith(    "pipe")) addAspect(        TCAspects.ITER, 1);
        else if (name.startsWith(    "gear")){addAspect(       TCAspects.MOTUS, 1);
                                              addAspect(     TCAspects.MACHINA, 1);}
        else if (name.startsWith(   "frame")) addAspect(     TCAspects.FABRICO, 1);
        else if (name.startsWith(   "plate")) addAspect(     TCAspects.FABRICO, 1);
        else if (name.startsWith(    "tool")) addAspect(TCAspects.INSTRUMENTUM, 2);
        else if (name.startsWith(     "gem")) addAspect(     TCAspects.VITREUS, 1);
        else if (name.startsWith( "crystal")) addAspect(     TCAspects.VITREUS, 1);
        else if (name.startsWith(    "lens")) addAspect(     TCAspects.VITREUS, 1);
        else if (name.startsWith( "circuit")) addAspect(    TCAspects.COGNITIO, 1);
        else if (name.startsWith("computer")) addAspect(    TCAspects.COGNITIO, 4);
        else if (name.startsWith( "battery")) addAspect(    TCAspects.ELECTRUM, 1);
        // spotless:on
    }

    private void addAspect(TCAspects aspect, int amount) {
        new TC_AspectStack(aspect, amount).addToAspectList(mAspects);
    }

    public @NotNull String getDefaultLocalName() {
        return defaultLocalName;
    }

    public @NotNull String getMaterialPrefix() {
        return materialPrefix;
    }

    public @NotNull String getMaterialPostfix() {
        return materialPostfix;
    }

    public boolean skipActiveUnification() {
        return skipActiveUnification;
    }

    public boolean isUnifiable() {
        return isUnifiable;
    }

    public boolean isSelfReferencing() {
        return isSelfReferencing;
    }

    public boolean isMaterialBased() {
        return isMaterialBased;
    }

    public boolean isContainer() {
        return isContainer;
    }

    public boolean isRecyclable() {
        return isRecyclable;
    }

    public boolean isEnchantable() {
        return isEnchantable;
    }

    public int getMaterialGenerationBits() {
        return materialGenerationBits;
    }

    public int getDefaultStackSize() {
        return defaultStackSize;
    }

    public long getMaterialAmount() {
        return materialAmount;
    }

    public int getTextureIndex() {
        return textureIndex;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    /** @deprecated Use {@link OrePrefixes#getName()} instead. */
    @Deprecated
    public String name() {
        return getName();
    }

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

        for (OrePrefixes tPrefix1 : VALUES) {
            if (tPrefix1.name.startsWith("ore")) {
                for (OrePrefixes tPrefix2 : VALUES) {
                    if (tPrefix2.name.startsWith("ore")) {
                        tPrefix1.addFamiliarPrefix(tPrefix2);
                    }
                }
            }
        }

        cell.disableComponent(Materials.GravitonShard);

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

        sheetmetal.mCondition = new ICondition.And<>(
            obj -> obj instanceof Materials mat && mat.hasMetalItems(),
            new ICondition.Nor<>(SubTag.STRETCHY, SubTag.SOFT, SubTag.BOUNCY, SubTag.NO_SMASHING));
        // -----

        pipeRestrictiveTiny.mSecondaryMaterial = new MaterialStack(Materials.Steel, ring.materialAmount);
        pipeRestrictiveSmall.mSecondaryMaterial = new MaterialStack(Materials.Steel, ring.materialAmount * 2);
        pipeRestrictiveMedium.mSecondaryMaterial = new MaterialStack(Materials.Steel, ring.materialAmount * 3);
        pipeRestrictiveLarge.mSecondaryMaterial = new MaterialStack(Materials.Steel, ring.materialAmount * 4);
        pipeRestrictiveHuge.mSecondaryMaterial = new MaterialStack(Materials.Steel, ring.materialAmount * 5);
        bucket.mSecondaryMaterial = new MaterialStack(Materials.Iron, ingot.materialAmount * 3);
        bucketClay.mSecondaryMaterial = new MaterialStack(Materials.Clay, dust.materialAmount * 5);
        CELL_TYPES
            .forEach(prefix -> prefix.mSecondaryMaterial = new MaterialStack(Materials.Tin, plate.materialAmount * 2));
        oreRedgranite.mSecondaryMaterial = new MaterialStack(Materials.GraniteRed, dust.materialAmount);
        oreBlackgranite.mSecondaryMaterial = new MaterialStack(Materials.GraniteBlack, dust.materialAmount);
        oreNetherrack.mSecondaryMaterial = new MaterialStack(Materials.Netherrack, dust.materialAmount);
        oreNether.mSecondaryMaterial = new MaterialStack(Materials.Netherrack, dust.materialAmount);
        oreEndstone.mSecondaryMaterial = new MaterialStack(Materials.Endstone, dust.materialAmount);
        oreEnd.mSecondaryMaterial = new MaterialStack(Materials.Endstone, dust.materialAmount);
        oreMarble.mSecondaryMaterial = new MaterialStack(Materials.Marble, dust.materialAmount);
        oreBasalt.mSecondaryMaterial = new MaterialStack(Materials.Basalt, dust.materialAmount);
        oreDense.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.materialAmount);
        orePoor.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.materialAmount * 2);
        oreSmall.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.materialAmount * 2);
        oreNormal.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.materialAmount * 2);
        rawOre.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.materialAmount);
        oreRich.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.materialAmount * 2);
        ore.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.materialAmount);
        crushed.mSecondaryMaterial = new MaterialStack(Materials.Stone, dust.materialAmount);
        toolHeadChainsaw.mSecondaryMaterial = new MaterialStack(
            Materials.Steel,
            plate.materialAmount * 4 + ring.materialAmount * 2);
        toolHeadWrench.mSecondaryMaterial = new MaterialStack(
            Materials.Steel,
            ring.materialAmount + screw.materialAmount * 2);
        bulletGtSmall.mSecondaryMaterial = new MaterialStack(Materials.Brass, ingot.materialAmount / 9);
        bulletGtMedium.mSecondaryMaterial = new MaterialStack(Materials.Brass, ingot.materialAmount / 6);
        bulletGtLarge.mSecondaryMaterial = new MaterialStack(Materials.Brass, ingot.materialAmount / 3);
    }

    public final ArrayList<ItemStack> mPrefixedItems = new GTArrayList<>(false, 16);

    public final List<TC_AspectStack> mAspects = new ArrayList<>();
    public final Collection<OrePrefixes> mFamiliarPrefixes = new HashSet<>();

    public final Collection<Materials> mDisabledItems = new HashSet<>(), mNotGeneratedItems = new HashSet<>(),
        mIgnoredMaterials = new HashSet<>(), mGeneratedItems = new HashSet<>();
    private final ArrayList<IOreRecipeRegistrator> mOreProcessing = new ArrayList<>();
    public ItemStack mContainerItem = null;
    public ICondition<ISubTagContainer> mCondition = null;
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

    public static boolean isInstanceOf(String aName, OrePrefixes aPrefix) {
        return aName != null && aName.startsWith(aPrefix.toString());
    }

    public void disableComponent(Materials aMaterial) {
        if (!this.mDisabledItems.contains(aMaterial)) this.mDisabledItems.add(aMaterial);
    }

    public static OrePrefixes getOrePrefix(String aOre) {
        for (OrePrefixes tPrefix : VALUES) if (aOre.startsWith(tPrefix.toString())) {
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
        for (OrePrefixes tPrefix : VALUES) {
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
            return Objects.equals(material, other.material);
        }
    }

    public static ParsedOreDictName detectPrefix(String oredictName) {
        for (OrePrefixes prefix : VALUES) {
            if (oredictName.startsWith(prefix.name)) {
                return new ParsedOreDictName(prefix, oredictName.substring(prefix.name.length()));
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
        for (OrePrefixes tPrefix : VALUES) {
            if (aOre.startsWith(tPrefix.toString())) {
                return aOre.replaceFirst(tPrefix.toString(), aPrefix.toString());
            }
        }
        return "";
    }

    private static final Map<String, OrePrefixes> NAME_TO_OREPREFIX = new ConcurrentHashMap<>();

    static {
        for (OrePrefixes value : VALUES) {
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
            if ((materialGenerationBits & DUST) == 0 || !aMaterial.hasDustItems())
                if ((materialGenerationBits & METAL) == 0 || !aMaterial.hasMetalItems())
                    if ((materialGenerationBits & GEM) == 0 || !aMaterial.hasGemItems())
                        if ((materialGenerationBits & ORE) == 0 || !aMaterial.hasOresItems())
                            if ((materialGenerationBits & CELL) == 0 || !aMaterial.hasCell())
                                if ((materialGenerationBits & PLASMA) == 0 || !aMaterial.hasPlasma())
                                    if ((materialGenerationBits & TOOL) == 0 || !aMaterial.hasToolHeadItems())
                                        if ((materialGenerationBits & GEAR) == 0 || !aMaterial.hasGearItems())
                                            if ((materialGenerationBits & EMPTY) == 0 || !aMaterial.hasEmpty())
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

        if (aMaterial == null) return;
        if (aMaterial.contains(SubTag.NO_RECIPES)) return;
        if (aMaterial == Materials._NULL && !isSelfReferencing && isMaterialBased) return;
        if (!GTUtility.isStackValid(aStack)) return;

        for (IOreRecipeRegistrator tRegistrator : mOreProcessing) {
            if (D2) {
                GTLog.ore.println(
                    "Processing '" + aOreDictName
                        + "' with the Prefix '"
                        + name
                        + "' and the Material '"
                        + aMaterial.mName
                        + "' at "
                        + GTUtility.getClassName(tRegistrator));
            }
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
                if (name.startsWith("gem")) return materialPrefix + "%material" + " Crystal";
                if (name.startsWith("plate")) return materialPrefix + "%material" + " Pane";
                if (name.startsWith("ingot")) return materialPrefix + "%material" + " Bar";
                if (name.startsWith("nugget")) return materialPrefix + "%material" + " Chip";
            }
            case "Wheat" -> {
                if (name.startsWith("dust")) return materialPrefix + "Flour";
            }
            case "Ice" -> {
                if (name.startsWith("dust")) return materialPrefix + "Crushed Ice";
            }
            case "Wood", "WoodSealed" -> {
                if (name.startsWith("bolt")) return "Short " + "%material" + " Stick";
                if (name.startsWith("stick")) return materialPrefix + "%material" + " Stick";
                if (name.startsWith("dust")) return materialPrefix + "%material" + " Pulp";
                if (name.startsWith("nugget")) return materialPrefix + "%material" + " Chip";
                if (name.startsWith("plate")) return materialPrefix + "%material" + " Plank";
            }
            case "Plastic", "Rubber", "Polyethylene", "Epoxid", "EpoxidFiberReinforced", "Polydimethylsiloxane", "Silicone", "Polysiloxane", "Polycaprolactam", "Polytetrafluoroethylene", "PolyvinylChloride", "Polystyrene", "StyreneButadieneRubber" -> {
                if (name.startsWith("dust")) return materialPrefix + "%material" + " Pulp";
                if (name.startsWith("plate")) return materialPrefix + "%material" + " Sheet";
                if (name.startsWith("ingot")) return materialPrefix + "%material" + " Bar";
                if (name.startsWith("nugget")) return materialPrefix + "%material" + " Chip";
                if (name.startsWith("foil")) return "Thin " + "%material" + " Sheet";
            }
            case "FierySteel" -> {
                if (isContainer) return materialPrefix + "Fiery Blood" + materialPostfix;
            }
            case "Steeleaf" -> {
                if (name.startsWith("ingot")) return materialPrefix + "%material";
            }
            case "Bone" -> {
                if (name.startsWith("dust")) return materialPrefix + "Bone Meal";
            }
            case "Blaze", "Milk", "Cocoa", "Chocolate", "Coffee", "Chili", "Cheese", "Snow" -> {
                if (name.startsWith("dust")) return materialPrefix + "%material" + " Powder";
            }
            case "Paper" -> {
                if (name.startsWith("dust")) return materialPrefix + "Chad";
                switch (name) {
                    case "plate" -> {
                        return "Sheet of Paper";
                    }
                    case "plateDouble" -> {
                        return "Paperboard";
                    }
                    case "plateTriple" -> {
                        return "Carton";
                    }
                    case "plateQuadruple" -> {
                        return "Cardboard";
                    }
                    case "plateQuintuple" -> {
                        return "Thick Cardboard";
                    }
                    case "plateDense" -> {
                        return "Strong Cardboard";
                    }
                    case "plateSuperdense" -> {
                        return "Impossibly Strong Cardboard";
                    }
                }
            }
            case "MeatRaw" -> {
                if (name.startsWith("dust")) return materialPrefix + "Mince Meat";
            }
            case "MeatCooked" -> {
                if (name.startsWith("dust")) return materialPrefix + "Cooked Mince Meat";
            }
            case "Ash", "DarkAsh", "Gunpowder", "Sugar", "Salt", "RockSalt", "VolcanicAsh", "RareEarth" -> {
                if (name.startsWith("dust")) return materialPrefix + "%material";
            }
            case "Vermiculite", "Bentonite", "Kaolinite", "Talc", "BasalticMineralSand", "GraniticMineralSand", "GlauconiteSand", "CassiteriteSand", "GarnetSand", "QuartzSand", "Pitchblende", "FullersEarth" -> {
                if (name.startsWith("dust")) return materialPrefix + "%material";
                switch (name) {
                    case "crushedCentrifuged", "crushedPurified" -> {
                        return materialPrefix + "%material";
                    }
                    case "crushed" -> {
                        return "Ground " + "%material";
                    }
                }
            }
        }
        if (ProcessingModSupport.aEnableThaumcraftMats) {
            switch (aMaterial.mName) {
                case "InfusedAir", "InfusedDull", "InfusedEarth", "InfusedEntropy", "InfusedFire", "InfusedOrder", "InfusedVis", "InfusedWater" -> {
                    if (name.startsWith("gem")) return materialPrefix + "Shard of " + "%material";
                    if (name.startsWith("crystal")) return materialPrefix + "Shard of " + "%material";
                    if (name.startsWith("plate")) return materialPrefix + "%material" + " Crystal Plate";
                    if (name.startsWith("dust")) return materialPrefix + "%material" + " Crystal Powder";
                    switch (name) {
                        case "crushedCentrifuged", "crushedPurified", "crushed" -> {
                            return materialPrefix + "%material" + " Crystals";
                        }
                    }
                }
            }
        }

        if (aMaterial.contains(SubTag.ICE_ORE) && (this == rawOre || this == ore)) {
            return materialPrefix + "%material" + " Ice";
        }

        if (this == ore) {
            return switch (aMaterial.mName) {
                case "InfusedAir", "InfusedDull", "InfusedEarth", "InfusedEntropy", "InfusedFire", "InfusedOrder", "InfusedVis", "InfusedWater" -> "%material Infused Stone";
                case "Vermiculite", "Bentonite", "Kaolinite", "Talc", "BasalticMineralSand", "GraniticMineralSand", "GlauconiteSand", "CassiteriteSand", "GarnetSand", "QuartzSand", "Pitchblende", "FullersEarth" -> "%material";
                default -> materialPrefix + "%material" + materialPostfix;
            };
        }

        if (nameKey != null) {
            // Replace the %s with %material so that it works with the existing system.
            return GTUtility.translate(nameKey, "%material");
        }

        // Use Standard Localization
        return materialPrefix + "%material" + materialPostfix;
    }
}
