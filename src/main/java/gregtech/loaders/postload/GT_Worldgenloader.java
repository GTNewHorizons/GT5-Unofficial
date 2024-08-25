package gregtech.loaders.postload;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import bloodasp.galacticgreg.WorldGenGaGT;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Materials.*;
import gregtech.api.util.GT_Log;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;
import gregtech.common.GT_Worldgen_Stone;
import gregtech.common.GT_Worldgenerator;
import gregtech.common.OreMixBuilder;

import static gregtech.api.enums.Materials.Almandine;
import static gregtech.api.enums.Materials.Aluminium;
import static gregtech.api.enums.Materials.Apatite;
import static gregtech.api.enums.Materials.BandedIron;
import static gregtech.api.enums.Materials.Barite;
import static gregtech.api.enums.Materials.Bastnasite;
import static gregtech.api.enums.Materials.Bauxite;
import static gregtech.api.enums.Materials.Bentonite;
import static gregtech.api.enums.Materials.Beryllium;
import static gregtech.api.enums.Materials.BrownLimonite;
import static gregtech.api.enums.Materials.Calcite;
import static gregtech.api.enums.Materials.Cassiterite;
import static gregtech.api.enums.Materials.CertusQuartz;
import static gregtech.api.enums.Materials.Chalcopyrite;
import static gregtech.api.enums.Materials.Cinnabar;
import static gregtech.api.enums.Materials.Coal;
import static gregtech.api.enums.Materials.Cobaltite;
import static gregtech.api.enums.Materials.Cooperite;
import static gregtech.api.enums.Materials.Copper;
import static gregtech.api.enums.Materials.Diamond;
import static gregtech.api.enums.Materials.Emerald;
import static gregtech.api.enums.Materials.Galena;
import static gregtech.api.enums.Materials.Garnierite;
import static gregtech.api.enums.Materials.Glauconite;
import static gregtech.api.enums.Materials.Gold;
import static gregtech.api.enums.Materials.Graphite;
import static gregtech.api.enums.Materials.GreenSapphire;
import static gregtech.api.enums.Materials.Grossular;
import static gregtech.api.enums.Materials.Ilmenite;
import static gregtech.api.enums.Materials.Iridium;
import static gregtech.api.enums.Materials.Iron;
import static gregtech.api.enums.Materials.Lapis;
import static gregtech.api.enums.Materials.Lazurite;
import static gregtech.api.enums.Materials.Lead;
import static gregtech.api.enums.Materials.Lepidolite;
import static gregtech.api.enums.Materials.Lignite;
import static gregtech.api.enums.Materials.Lithium;
import static gregtech.api.enums.Materials.Magnesite;
import static gregtech.api.enums.Materials.Magnetite;
import static gregtech.api.enums.Materials.Malachite;
import static gregtech.api.enums.Materials.Molybdenite;
import static gregtech.api.enums.Materials.Molybdenum;
import static gregtech.api.enums.Materials.Monazite;
import static gregtech.api.enums.Materials.Neodymium;
import static gregtech.api.enums.Materials.NetherQuartz;
import static gregtech.api.enums.Materials.Nickel;
import static gregtech.api.enums.Materials.Oilsands;
import static gregtech.api.enums.Materials.Olivine;
import static gregtech.api.enums.Materials.Palladium;
import static gregtech.api.enums.Materials.Pentlandite;
import static gregtech.api.enums.Materials.Pitchblende;
import static gregtech.api.enums.Materials.Platinum;
import static gregtech.api.enums.Materials.Powellite;
import static gregtech.api.enums.Materials.Pyrite;
import static gregtech.api.enums.Materials.Pyrochlore;
import static gregtech.api.enums.Materials.Pyrolusite;
import static gregtech.api.enums.Materials.Pyrope;
import static gregtech.api.enums.Materials.Quartzite;
import static gregtech.api.enums.Materials.Redstone;
import static gregtech.api.enums.Materials.RockSalt;
import static gregtech.api.enums.Materials.Ruby;
import static gregtech.api.enums.Materials.Salt;
import static gregtech.api.enums.Materials.Sapphire;
import static gregtech.api.enums.Materials.Scheelite;
import static gregtech.api.enums.Materials.Silver;
import static gregtech.api.enums.Materials.Soapstone;
import static gregtech.api.enums.Materials.Sodalite;
import static gregtech.api.enums.Materials.Spessartine;
import static gregtech.api.enums.Materials.Sphalerite;
import static gregtech.api.enums.Materials.Spodumene;
import static gregtech.api.enums.Materials.Stibnite;
import static gregtech.api.enums.Materials.Sulfur;
import static gregtech.api.enums.Materials.Talc;
import static gregtech.api.enums.Materials.Tantalite;
import static gregtech.api.enums.Materials.Tetrahedrite;
import static gregtech.api.enums.Materials.Thorium;
import static gregtech.api.enums.Materials.Tin;
import static gregtech.api.enums.Materials.TricalciumPhosphate;
import static gregtech.api.enums.Materials.Tungstate;
import static gregtech.api.enums.Materials.Uraninite;
import static gregtech.api.enums.Materials.Uranium;
import static gregtech.api.enums.Materials.VanadiumMagnetite;
import static gregtech.api.enums.Materials.Wulfenite;
import static gregtech.api.enums.Materials.YellowLimonite;

public class GT_Worldgenloader implements Runnable {

    public void run() {

        new GT_Worldgenerator();

        new GT_Worldgen_Stone(
            "overworld.stone.blackgranite.tiny",
            true,
            GregTech_API.sBlockGranites,
            0,
            0,
            1,
            75,
            5,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.blackgranite.small",
            true,
            GregTech_API.sBlockGranites,
            0,
            0,
            1,
            100,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.blackgranite.medium",
            true,
            GregTech_API.sBlockGranites,
            0,
            0,
            1,
            200,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.blackgranite.large",
            true,
            GregTech_API.sBlockGranites,
            0,
            0,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.blackgranite.huge",
            true,
            GregTech_API.sBlockGranites,
            0,
            0,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "overworld.stone.redgranite.tiny",
            true,
            GregTech_API.sBlockGranites,
            8,
            0,
            1,
            75,
            5,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.redgranite.small",
            true,
            GregTech_API.sBlockGranites,
            8,
            0,
            1,
            100,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.redgranite.medium",
            true,
            GregTech_API.sBlockGranites,
            8,
            0,
            1,
            200,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.redgranite.large",
            true,
            GregTech_API.sBlockGranites,
            8,
            0,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.redgranite.huge",
            true,
            GregTech_API.sBlockGranites,
            8,
            0,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "overworld.stone.marble.tiny",
            true,
            GregTech_API.sBlockStones,
            0,
            0,
            1,
            75,
            5,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.marble.small",
            true,
            GregTech_API.sBlockStones,
            0,
            0,
            1,
            100,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.marble.medium",
            true,
            GregTech_API.sBlockStones,
            0,
            0,
            1,
            200,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.marble.large",
            true,
            GregTech_API.sBlockStones,
            0,
            0,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.marble.huge",
            true,
            GregTech_API.sBlockStones,
            0,
            0,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "overworld.stone.basalt.tiny",
            true,
            GregTech_API.sBlockStones,
            8,
            0,
            1,
            75,
            5,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.basalt.small",
            true,
            GregTech_API.sBlockStones,
            8,
            0,
            1,
            100,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.basalt.medium",
            true,
            GregTech_API.sBlockStones,
            8,
            0,
            1,
            200,
            10,
            0,
            180,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.basalt.large",
            true,
            GregTech_API.sBlockStones,
            8,
            0,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "overworld.stone.basalt.huge",
            true,
            GregTech_API.sBlockStones,
            8,
            0,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "nether.stone.blackgranite.tiny",
            false,
            GregTech_API.sBlockGranites,
            0,
            -1,
            1,
            50,
            45,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.blackgranite.small",
            false,
            GregTech_API.sBlockGranites,
            0,
            -1,
            1,
            100,
            60,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.blackgranite.medium",
            false,
            GregTech_API.sBlockGranites,
            0,
            -1,
            1,
            200,
            80,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.blackgranite.large",
            false,
            GregTech_API.sBlockGranites,
            0,
            -1,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.blackgranite.huge",
            false,
            GregTech_API.sBlockGranites,
            0,
            -1,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "nether.stone.redgranite.tiny",
            false,
            GregTech_API.sBlockGranites,
            8,
            -1,
            1,
            50,
            45,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.redgranite.small",
            false,
            GregTech_API.sBlockGranites,
            8,
            -1,
            1,
            100,
            60,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.redgranite.medium",
            false,
            GregTech_API.sBlockGranites,
            8,
            -1,
            1,
            200,
            80,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.redgranite.large",
            false,
            GregTech_API.sBlockGranites,
            8,
            -1,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.redgranite.huge",
            false,
            GregTech_API.sBlockGranites,
            8,
            -1,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "nether.stone.marble.tiny",
            false,
            GregTech_API.sBlockStones,
            0,
            -1,
            1,
            50,
            45,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.marble.small",
            false,
            GregTech_API.sBlockStones,
            0,
            -1,
            1,
            100,
            60,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.marble.medium",
            false,
            GregTech_API.sBlockStones,
            0,
            -1,
            1,
            200,
            80,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.marble.large",
            false,
            GregTech_API.sBlockStones,
            0,
            -1,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.marble.huge",
            false,
            GregTech_API.sBlockStones,
            0,
            -1,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        new GT_Worldgen_Stone(
            "nether.stone.basalt.tiny",
            false,
            GregTech_API.sBlockStones,
            8,
            -1,
            1,
            50,
            45,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.basalt.small",
            false,
            GregTech_API.sBlockStones,
            8,
            -1,
            1,
            100,
            60,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.basalt.medium",
            false,
            GregTech_API.sBlockStones,
            8,
            -1,
            1,
            200,
            80,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.basalt.large",
            false,
            GregTech_API.sBlockStones,
            8,
            -1,
            1,
            300,
            70,
            0,
            120,
            null,
            false);
        new GT_Worldgen_Stone(
            "nether.stone.basalt.huge",
            false,
            GregTech_API.sBlockStones,
            8,
            -1,
            1,
            400,
            150,
            0,
            120,
            null,
            false);

        // GT Default Small Ores
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.copper",
            true,
            60,
            180,
            32,
            true,
            true,
            true,
            true,
            true,
            false,
            Materials.Copper);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.tin",
            true,
            80,
            220,
            32,
            true,
            true,
            true,
            true,
            true,
            true,
            Materials.Tin);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.bismuth",
            true,
            80,
            120,
            8,
            false,
            true,
            false,
            true,
            true,
            false,
            Materials.Bismuth);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.coal",
            true,
            120,
            250,
            24,
            true,
            false,
            false,
            false,
            false,
            false,
            Coal);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.iron",
            true,
            40,
            100,
            16,
            true,
            true,
            true,
            true,
            true,
            false,
            Materials.Iron);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.lead",
            true,
            40,
            180,
            16,
            false,
            true,
            true,
            true,
            true,
            true,
            Materials.Lead);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.zinc",
            true,
            80,
            210,
            24,
            true,
            true,
            true,
            true,
            true,
            false,
            Materials.Zinc);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.gold",
            true,
            20,
            60,
            8,
            true,
            false,
            true,
            true,
            true,
            true,
            Materials.Gold);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.silver",
            true,
            20,
            60,
            20,
            true,
            true,
            true,
            true,
            true,
            true,
            Materials.Silver);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.nickel",
            true,
            80,
            150,
            8,
            true,
            false,
            true,
            true,
            true,
            true,
            Materials.Nickel);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.lapis",
            true,
            10,
            50,
            4,
            true,
            false,
            false,
            true,
            false,
            true,
            Materials.Lapis);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.diamond",
            true,
            5,
            15,
            2,
            true,
            false,
            false,
            true,
            true,
            true,
            Materials.Diamond);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.emerald",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.Emerald);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.ruby",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.Ruby);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.sapphire",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.Sapphire);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.greensapphire",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.GreenSapphire);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.olivine",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.Olivine);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.topaz",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.Topaz);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.tanzanite",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.Tanzanite);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.amethyst",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.Amethyst);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.opal",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.Opal);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.jasper",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.Jasper);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.bluetopaz",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.BlueTopaz);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.amber",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.Amber);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.foolsruby",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.FoolsRuby);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.garnetred",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.GarnetRed);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.garnetyellow",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            false,
            true,
            true,
            Materials.GarnetYellow);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.redstone",
            true,
            5,
            25,
            8,
            true,
            true,
            false,
            true,
            true,
            true,
            Materials.Redstone);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.netherquartz",
            true,
            30,
            120,
            64,
            false,
            true,
            false,
            false,
            false,
            false,
            Materials.NetherQuartz);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.saltpeter",
            true,
            10,
            60,
            8,
            false,
            true,
            false,
            false,
            false,
            false,
            Materials.Saltpeter);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.sulfur",
            true,
            5,
            60,
            40,
            false,
            true,
            false,
            false,
            false,
            false,
            Materials.Sulfur);

        // TODO: GTNH Custom Small Ores
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.titanium",
            true,
            10,
            180,
            32,
            false,
            false,
            false,
            Materials.Titanium);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.tungsten",
            true,
            10,
            120,
            16,
            false,
            false,
            false,
            Materials.Tungsten);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.meteoriciron",
            true,
            50,
            70,
            8,
            false,
            false,
            false,
            Materials.MeteoricIron);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.firestone",
            true,
            5,
            15,
            2,
            false,
            false,
            false,
            Materials.Firestone);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.neutronium",
            true,
            5,
            15,
            8,
            false,
            false,
            false,
            Materials.Neutronium);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.chromite",
            true,
            20,
            40,
            8,
            false,
            false,
            false,
            Materials.Chromite);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.tungstate",
            true,
            20,
            40,
            8,
            false,
            false,
            false,
            Materials.Tungstate);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.naquadah",
            true,
            5,
            25,
            8,
            false,
            false,
            false,
            Materials.Naquadah);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.quantium",
            true,
            5,
            25,
            6,
            false,
            false,
            false,
            Materials.Quantium);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.mythril", true, 5, 25, 6, false, false, false, Materials.Mytryl);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.ledox", true, 40, 60, 4, false, false, false, Materials.Ledox);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.oriharukon",
            true,
            20,
            40,
            6,
            false,
            false,
            false,
            Materials.Oriharukon);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.draconium",
            true,
            5,
            15,
            4,
            false,
            false,
            false,
            Materials.Draconium);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.awdraconium",
            true,
            5,
            15,
            2,
            false,
            false,
            false,
            Materials.DraconiumAwakened);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.desh", true, 10, 30, 6, false, false, false, Materials.Desh);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.blackplutonium",
            true,
            25,
            45,
            6,
            false,
            false,
            false,
            Materials.BlackPlutonium);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.infinitycatalyst",
            true,
            40,
            80,
            6,
            false,
            false,
            false,
            Materials.InfinityCatalyst);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.infinity",
            true,
            2,
            40,
            2,
            false,
            false,
            false,
            Materials.Infinity);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.bedrockium",
            true,
            5,
            25,
            6,
            false,
            false,
            false,
            Materials.Bedrockium);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.realgar",
            true,
            15,
            85,
            32,
            false,
            true,
            false,
            Materials.Realgar);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.certusquartz",
            true,
            5,
            115,
            16,
            false,
            true,
            false,
            Materials.CertusQuartz);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.jade", true, 5, 35, 2, false, false, false, Materials.Jade);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.deepiron",
            true,
            5,
            40,
            8,
            false,
            false,
            false,
            Materials.DeepIron);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.redgarnet",
            true,
            5,
            35,
            2,
            false,
            false,
            false,
            Materials.GarnetRed);
        new GT_Worldgen_GT_Ore_SmallPieces(
            "ore.small.chargedcertus",
            true,
            5,
            115,
            4,
            false,
            false,
            false,
            Materials.CertusQuartzCharged);

        // GT Default Veins

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.naquadah")
            .heightRange(10,90)
            .weight(30)
            .density(4)
            .size(32)
            .primary(Materials.Naquadah)
            .secondary(Materials.Naquadah)
            .inBetween(Materials.Naquadah)
            .sporadic(Materials.NaquadahEnriched)
            .add();

        new OreMixBuilder().name("ore.mix.lignite")
            .heightRange(80,210)
            .weight(160)
            .density(7)
            .size(32)
            .inOverworld()
            .primary(Materials.Lignite)
            .secondary(Materials.Lignite)
            .inBetween(Materials.Lignite)
            .sporadic(Coal)
            .add();

        new OreMixBuilder().name("ore.mix.coal")
            .heightRange(30,80)
            .weight(80)
            .density(5)
            .size(32)
            .inOverworld()
            .primary(Coal)
            .secondary(Coal)
            .inBetween(Coal)
            .sporadic(Lignite)
            .add();

        new OreMixBuilder().name("ore.mix.magnetite")
            .heightRange(60,180)
            .weight(160)
            .density(2)
            .size(32)
            .inOverworld()
            .primary(Magnetite)
            .secondary(Magnetite)
            .inBetween(Iron)
            .sporadic(VanadiumMagnetite)
            .add();

        new OreMixBuilder().name("ore.mix.gold")
            .heightRange(30,60)
            .weight(160)
            .density(2)
            .size(32)
            .inOverworld()
            .inEnd()
            .primary(Magnetite)
            .secondary(Magnetite)
            .inBetween(VanadiumMagnetite)
            .sporadic(Gold)
            .add();

        new OreMixBuilder().name("ore.mix.iron")
            .heightRange(10,40)
            .weight(120)
            .density(3)
            .size(24)
            .inOverworld()
            .inNether()
            .primary(BrownLimonite)
            .secondary(YellowLimonite)
            .inBetween(BandedIron)
            .sporadic(Malachite)
            .add();

        new OreMixBuilder().name("ore.mix.cassiterite")
            .heightRange(60,220)
            .weight(50)
            .density(4)
            .size(24)
            .inOverworld()
            .inEnd()
            .primary(Tin)
            .secondary(Tin)
            .inBetween(Cassiterite)
            .sporadic(Tin)
            .add();

        new OreMixBuilder().name("ore.mix.tetrahedrite")
            .heightRange(80,120)
            .weight(70)
            .density(3)
            .size(24)
            .inNether()
            .inEnd()
            .primary(Tetrahedrite)
            .secondary(Tetrahedrite)
            .inBetween(Copper)
            .sporadic(Stibnite)
            .add();

        new OreMixBuilder().name("ore.mix.netherquartz")
            .heightRange(40,80)
            .weight(80)
            .density(4)
            .size(24)
            .inNether()
            .primary(NetherQuartz)
            .secondary(NetherQuartz)
            .inBetween(NetherQuartz)
            .sporadic(Quartzite)
            .add();

        new OreMixBuilder().name("ore.mix.sulfur")
            .heightRange(5,20)
            .weight(100)
            .density(4)
            .size(24)
            .inNether()
            .primary(Sulfur)
            .secondary(Sulfur)
            .inBetween(Pyrite)
            .sporadic(Sphalerite)
            .add();

        new OreMixBuilder().name("ore.mix.copper")
            .heightRange(5,60)
            .weight(80)
            .density(3)
            .size(24)
            .inOverworld()
            .inNether()
            .inEnd()
            .primary(Chalcopyrite)
            .secondary(Iron)
            .inBetween(Pyrite)
            .sporadic(Copper)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.bauxite")
            .heightRange(10,80)
            .weight(80)
            .density(3)
            .size(24)
            .primary(Bauxite)
            .secondary(Ilmenite)
            .inBetween(Aluminium)
            .sporadic(Ilmenite)
            .add();

        new OreMixBuilder().name("ore.mix.salts")
            .heightRange(50,70)
            .weight(50)
            .density(2)
            .size(24)
            .inOverworld()
            .primary(RockSalt)
            .secondary(Salt)
            .inBetween(Lepidolite)
            .sporadic(Spodumene)
            .add();

        new OreMixBuilder().name("ore.mix.redstone")
            .heightRange(5,40)
            .weight(60)
            .density(2)
            .size(24)
            .inOverworld()
            .inNether()
            .primary(Redstone)
            .secondary(Redstone)
            .inBetween(Ruby)
            .sporadic(Cinnabar)
            .add();

        new OreMixBuilder().name("ore.mix.soapstone")
            .heightRange(20,50)
            .weight(40)
            .density(2)
            .size(16)
            .inOverworld()
            .primary(Soapstone)
            .secondary(Talc)
            .inBetween(Glauconite)
            .sporadic(Pentlandite)
            .add();

        new OreMixBuilder().name("ore.mix.nickel")
            .heightRange(10,40)
            .weight(40)
            .density(2)
            .size(16)
            .inEnd()
            .primary(Garnierite)
            .secondary(Nickel)
            .inBetween(Cobaltite)
            .sporadic(Pentlandite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.platinum")
            .heightRange(40,50)
            .weight(5)
            .density(2)
            .size(16)
            .primary(Cooperite)
            .secondary(Palladium)
            .inBetween(Platinum)
            .sporadic(Iridium)
            .add();


        // handled by gagreg
        new OreMixBuilder().name("ore.mix.pitchblende")
            .heightRange(60,60)
            .weight(40)
            .density(2)
            .size(16)
            .primary(Pitchblende)
            .secondary(Pitchblende)
            .inBetween(Uraninite)
            .sporadic(Uraninite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.monazite")
            .heightRange(20,40)
            .weight(30)
            .density(2)
            .size(16)
            .primary(Bastnasite)
            .secondary(Bastnasite)
            .inBetween(Monazite)
            .sporadic(Neodymium)
            .add();

        new OreMixBuilder().name("ore.mix.molybdenum")
            .heightRange(20,50)
            .weight(5)
            .density(2)
            .size(16)
            .inNether()
            .inEnd()
            .primary(Wulfenite)
            .secondary(Molybdenite)
            .inBetween(Molybdenum)
            .sporadic(Powellite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.tungstate")
            .heightRange(20,60)
            .weight(10)
            .density(2)
            .size(16)
            .primary(Scheelite)
            .secondary(Scheelite)
            .inBetween(Tungstate)
            .sporadic(Lithium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.sapphire")
            .heightRange(10,40)
            .weight(60)
            .density(2)
            .size(16)
            .primary(Almandine)
            .secondary(Pyrope)
            .inBetween(Sapphire)
            .sporadic(GreenSapphire)
            .add();

        new OreMixBuilder().name("ore.mix.manganese")
            .heightRange(20,30)
            .weight(20)
            .density(2)
            .size(16)
            .inOverworld()
            .inNether()
            .primary(Grossular)
            .secondary(Spessartine)
            .inBetween(Pyrolusite)
            .sporadic(Tantalite)
            .add();

        new OreMixBuilder().name("ore.mix.quartz")
            .heightRange(80,120)
            .weight(20)
            .density(2)
            .size(16)
            .inNether()
            .primary(Quartzite)
            .secondary(Barite)
            .inBetween(CertusQuartz)
            .sporadic(CertusQuartz)
            .add();

        new OreMixBuilder().name("ore.mix.diamond")
            .heightRange(5,20)
            .weight(40)
            .density(1)
            .size(16)
            .inOverworld()
            .primary(Graphite)
            .secondary(Graphite)
            .inBetween(Diamond)
            .sporadic(Coal)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.olivine")
            .heightRange(10,40)
            .weight(60)
            .density(2)
            .size(16)
            .primary(Bentonite)
            .secondary(Magnesite)
            .inBetween(Olivine)
            .sporadic(Glauconite)
            .add();

        new OreMixBuilder().name("ore.mix.apatite")
            .heightRange(40,60)
            .weight(60)
            .density(2)
            .size(16)
            .inOverworld()
            .primary(Apatite)
            .secondary(Apatite)
            .inBetween(TricalciumPhosphate)
            .sporadic(Pyrochlore)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.galena")
            .heightRange(5,45)
            .weight(40)
            .density(4)
            .size(16)
            .primary(Galena)
            .secondary(Galena)
            .inBetween(Silver)
            .sporadic(Lead)
            .add();

        new OreMixBuilder().name("ore.mix.lapis")
            .heightRange(20,50)
            .weight(40)
            .density(4)
            .size(16)
            .inOverworld()
            .primary(Lazurite)
            .secondary(Sodalite)
            .inBetween(Lapis)
            .sporadic(Calcite)
            .add();

        new OreMixBuilder().name("ore.mix.beryllium")
            .heightRange(5,30)
            .weight(30)
            .density(2)
            .size(16)
            .inNether()
            .inEnd()
            .primary(Beryllium)
            .secondary(Beryllium)
            .inBetween(Emerald)
            .sporadic(Thorium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.uranium")
            .heightRange(20,30)
            .weight(20)
            .density(2)
            .size(16)
            .primary(Uraninite)
            .secondary(Uraninite)
            .inBetween(Uranium)
            .sporadic(Uranium)
            .add();

        new OreMixBuilder().name("ore.mix.oilsand")
            .heightRange(50,80)
            .weight(40)
            .density(5)
            .size(16)
            .inOverworld()
            .primary(Oilsands)
            .secondary(Oilsands)
            .inBetween(Oilsands)
            .sporadic(Oilsands)
            .add();

        /*
         * TODO: custom GTNH OreMixes WARNING: NO DUPLICATES IN aName OR DEPRECATED MATERIALS IN HERE. Materials can be
         * used unlimited, since achievements for Ores are turned off.
         */

        // aName, aDefault, aMinY, aMaxY, aWeight, aDensity, aSize, aOverworld, aNether, aEnd, aPrimary, aSecondary,
        // aBetween, aSporadic

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.neutronium")
            .heightRange(5,30)
            .weight(10)
            .density(2)
            .size(16)
            .primary(Materials.Neutronium)
            .secondary(Materials.Adamantium)
            .inBetween(Materials.Naquadah)
            .sporadic(Materials.Titanium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.aquaignis")
            .heightRange(5,35)
            .weight(16)
            .density(2)
            .size(16)
            .primary(Materials.InfusedWater)
            .secondary(Materials.InfusedFire)
            .inBetween(Materials.Amber)
            .sporadic(Materials.Cinnabar)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.terraaer")
            .heightRange(5,35)
            .weight(16)
            .density(2)
            .size(16)
            .primary(Materials.InfusedEarth)
            .secondary(Materials.InfusedAir)
            .inBetween(Materials.Amber)
            .sporadic(Materials.Cinnabar)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.perditioordo")
            .heightRange(5,35)
            .weight(16)
            .density(2)
            .size(16)
            .primary(Materials.InfusedEntropy)
            .secondary(Materials.InfusedOrder)
            .inBetween(Materials.Amber)
            .sporadic(Materials.Cinnabar)
            .add();

        new OreMixBuilder().name("ore.mix.coppertin")
            .heightRange(80,200)
            .weight(80)
            .density(3)
            .size(24)
            .inOverworld()
            .primary(Materials.Chalcopyrite)
            .secondary(Materials.Vermiculite)
            .inBetween(Materials.Cassiterite)
            .sporadic(Materials.Alunite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.titaniumchrome")
            .heightRange(10,70)
            .weight(16)
            .density(2)
            .size(16)
            .primary(Materials.Ilmenite)
            .secondary(Materials.Chromite)
            .inBetween(Materials.Uvarovite)
            .sporadic(Materials.Perlite)
            .add();

        new OreMixBuilder().name("ore.mix.mineralsand")
            .heightRange(50,60)
            .weight(80)
            .density(3)
            .size(24)
            .inOverworld()
            .primary(Materials.BasalticMineralSand)
            .secondary(Materials.GraniticMineralSand)
            .inBetween(Materials.FullersEarth)
            .sporadic(Materials.Gypsum)
            .add();

        new OreMixBuilder().name("ore.mix.garnettin")
            .heightRange(50,60)
            .weight(80)
            .density(3)
            .size(24)
            .inOverworld()
            .primary(Materials.CassiteriteSand)
            .secondary(Materials.GarnetSand)
            .inBetween(Materials.Asbestos)
            .sporadic(Materials.Diatomite)
            .add();

        new OreMixBuilder().name("ore.mix.kaolinitezeolite")
            .heightRange(50,70)
            .weight(60)
            .density(4)
            .size(16)
            .inOverworld()
            .primary(Materials.Kaolinite)
            .secondary(Materials.Zeolite)
            .inBetween(Materials.FullersEarth)
            .sporadic(Materials.GlauconiteSand)
            .add();

        new OreMixBuilder().name("ore.mix.mica")
            .heightRange(20,40)
            .weight(20)
            .density(2)
            .size(16)
            .inOverworld()
            .primary(Materials.Kyanite)
            .secondary(Materials.Mica)
            .inBetween(Materials.Cassiterite)
            .sporadic(Materials.Pollucite)
            .add();

        new OreMixBuilder().name("ore.mix.dolomite")
            .heightRange(150,200)
            .weight(40)
            .density(4)
            .size(24)
            .inOverworld()
            .primary(Materials.Dolomite)
            .secondary(Materials.Wollastonite)
            .inBetween(Materials.Trona)
            .sporadic(Materials.Andradite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.platinumchrome")
            .heightRange(5,30)
            .weight(10)
            .density(2)
            .size(16)
            .primary(Materials.Platinum)
            .secondary(Materials.Chrome)
            .inBetween(Materials.Cooperite)
            .sporadic(Materials.Palladium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.iridiummytryl")
            .heightRange(15,40)
            .weight(10)
            .density(2)
            .size(16)
            .primary(Materials.Nickel)
            .secondary(Materials.Iridium)
            .inBetween(Materials.Palladium)
            .sporadic(Materials.Mithril)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.osmium")
            .heightRange(5,30)
            .weight(10)
            .density(2)
            .size(16)
            .primary(Materials.Nickel)
            .secondary(Materials.Osmium)
            .inBetween(Materials.Iridium)
            .sporadic(Materials.Nickel)
            .add();

        new OreMixBuilder().name("ore.mix.saltpeterelectrotine")
            .heightRange(5,45)
            .weight(40)
            .density(3)
            .size(16)
            .inNether()
            .primary(Materials.Saltpeter)
            .secondary(Materials.Diatomite)
            .inBetween(Materials.Electrotine)
            .sporadic(Materials.Alunite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.desh")
            .heightRange(5,40)
            .weight(30)
            .density(2)
            .size(16)
            .primary(Materials.Desh)
            .secondary(Materials.Desh)
            .inBetween(Materials.Scheelite)
            .sporadic(Materials.Tungstate)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.draconium")
            .heightRange(20,40)
            .weight(40)
            .density(1)
            .size(16)
            .primary(Materials.Draconium)
            .secondary(Materials.Electrotine)
            .inBetween(Materials.Jade)
            .sporadic(Materials.Vinteum)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.quantium")
            .heightRange(5,25)
            .weight(30)
            .density(3)
            .size(24)
            .primary(Materials.Quantium)
            .secondary(Materials.Amethyst)
            .inBetween(Materials.Rutile)
            .sporadic(Materials.Ardite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.callistoice")
            .heightRange(40,60)
            .weight(40)
            .density(2)
            .size(16)
            .primary(Materials.CallistoIce)
            .secondary(Materials.Topaz)
            .inBetween(Materials.BlueTopaz)
            .sporadic(Materials.Alduorite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.mytryl")
            .heightRange(10,30)
            .weight(40)
            .density(2)
            .size(16)
            .primary(Materials.Mytryl)
            .secondary(Materials.Jasper)
            .inBetween(Materials.Ceruclase)
            .sporadic(Materials.Vulcanite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.ledox")
            .heightRange(55,65)
            .weight(30)
            .density(2)
            .size(24)
            .primary(Materials.Ledox)
            .secondary(Materials.Opal)
            .inBetween(Materials.Orichalcum)
            .sporadic(Materials.Rubracium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.oriharukon")
            .heightRange(30,60)
            .weight(40)
            .density(2)
            .size(16)
            .primary(Materials.Oriharukon)
            .secondary(Materials.Tanzanite)
            .inBetween(Materials.Vyroxeres)
            .sporadic(Materials.Mirabilite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.blackplutonium")
            .heightRange(5,25)
            .weight(40)
            .density(2)
            .size(24)
            .primary(Materials.BlackPlutonium)
            .secondary(Materials.GarnetRed)
            .inBetween(Materials.GarnetYellow)
            .sporadic(Materials.Borax)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.infusedgold")
            .heightRange(15,40)
            .weight(30)
            .density(2)
            .size(16)
            .primary(Materials.Gold)
            .secondary(Materials.Gold)
            .inBetween(Materials.InfusedGold)
            .sporadic(Materials.Platinum)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.niobium")
            .heightRange(5,30)
            .weight(60)
            .density(2)
            .size(24)
            .primary(Materials.Niobium)
            .secondary(Materials.Yttrium)
            .inBetween(Materials.Gallium)
            .sporadic(Materials.Gallium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.tungstenirons")
            .heightRange(5,25)
            .weight(16)
            .density(2)
            .size(30)
            .primary(Materials.Tungsten)
            .secondary(Materials.Silicon)
            .inBetween(Materials.DeepIron)
            .sporadic(Materials.ShadowIron)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.uraniumgtnh")
            .heightRange(10,30)
            .weight(60)
            .density(2)
            .size(24)
            .primary(Materials.Thorium)
            .secondary(Materials.Uranium)
            .inBetween(Materials.Plutonium241)
            .sporadic(Materials.Uranium235)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.vanadiumgold")
            .heightRange(10,50)
            .weight(60)
            .density(2)
            .size(24)
            .primary(Materials.Vanadium)
            .secondary(Materials.Magnetite)
            .inBetween(Materials.Gold)
            .sporadic(Materials.Chrome)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.netherstar")
            .heightRange(20,60)
            .weight(60)
            .density(2)
            .size(24)
            .primary(Materials.GarnetSand)
            .secondary(Materials.NetherStar)
            .inBetween(Materials.GarnetRed)
            .sporadic(Materials.GarnetYellow)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.garnet")
            .heightRange(10,30)
            .weight(40)
            .density(2)
            .size(16)
            .primary(Materials.GarnetRed)
            .secondary(Materials.GarnetYellow)
            .inBetween(Materials.Chrysotile)
            .sporadic(Materials.Realgar)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.rareearth")
            .heightRange(30,60)
            .weight(40)
            .density(2)
            .size(24)
            .primary(Materials.Cadmium)
            .secondary(Materials.Caesium)
            .inBetween(Materials.Lanthanum)
            .sporadic(Materials.Cerium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.richnuclear")
            .heightRange(55,120)
            .weight(5)
            .density(2)
            .size(8)
            .primary(Materials.Uranium)
            .secondary(Materials.Plutonium)
            .inBetween(Materials.Thorium)
            .sporadic(Materials.Thorium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.heavypentele")
            .heightRange(40,60)
            .weight(60)
            .density(5)
            .size(32)
            .primary(Materials.Arsenic)
            .secondary(Materials.Bismuth)
            .inBetween(Materials.Antimony)
            .sporadic(Materials.Antimony)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.europa")
            .heightRange(55,65)
            .weight(110)
            .density(4)
            .size(24)
            .primary(Materials.Magnesite)
            .secondary(Materials.BandedIron)
            .inBetween(Materials.Sulfur)
            .sporadic(Materials.Opal)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.europacore")
            .heightRange(5,15)
            .weight(5)
            .density(2)
            .size(16)
            .primary(Materials.Chrome)
            .secondary(Materials.Tungstate)
            .inBetween(Materials.Molybdenum)
            .sporadic(Materials.Manganese)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.secondlanthanid")
            .heightRange(10,40)
            .weight(10)
            .density(3)
            .size(24)
            .primary(Materials.Samarium)
            .secondary(Materials.Neodymium)
            .inBetween(Materials.Tartarite)
            .sporadic(Materials.Tartarite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.quartzspace")
            .heightRange(40,80)
            .weight(20)
            .density(3)
            .size(16)
            .primary(Materials.Quartzite)
            .secondary(Materials.Barite)
            .inBetween(Materials.CertusQuartz)
            .sporadic(Materials.CertusQuartz)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.rutile")
            .heightRange(5,20)
            .weight(8)
            .density(4)
            .size(12)
            .primary(Materials.Rutile)
            .secondary(Materials.Titanium)
            .inBetween(Materials.Bauxite)
            .sporadic(Materials.MeteoricIron)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.tfgalena")
            .heightRange(5,35)
            .weight(40)
            .density(4)
            .size(16)
            .primary(Materials.Galena)
            .secondary(Materials.Silver)
            .inBetween(Materials.Lead)
            .sporadic(Materials.Cryolite)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.luvtantalite")
            .heightRange(20,30)
            .weight(10)
            .density(4)
            .size(16)
            .primary(Materials.Pyrolusite)
            .secondary(Materials.Apatite)
            .inBetween(Materials.Tantalite)
            .sporadic(Materials.Pyrochlore)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.certusquartz")
            .heightRange(40,80)
            .weight(60)
            .density(5)
            .size(32)
            .primary(Materials.CertusQuartz)
            .secondary(Materials.CertusQuartz)
            .inBetween(Materials.CertusQuartzCharged)
            .sporadic(Materials.QuartzSand)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.infinitycatalyst")
            .heightRange(5,20)
            .weight(15)
            .density(2)
            .size(16)
            .primary(Materials.Neutronium)
            .secondary(Materials.Adamantium)
            .inBetween(Materials.InfinityCatalyst)
            .sporadic(Materials.Bedrockium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.cosmicneutronium")
            .heightRange(5,20)
            .weight(15)
            .density(2)
            .size(16)
            .primary(Materials.Neutronium)
            .secondary(Materials.CosmicNeutronium)
            .inBetween(Materials.BlackPlutonium)
            .sporadic(Materials.Bedrockium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.dilithium")
            .heightRange(30,100)
            .weight(30)
            .density(3)
            .size(24)
            .primary(Materials.Dilithium)
            .secondary(Materials.Dilithium)
            .inBetween(Materials.MysteriousCrystal)
            .sporadic(Materials.Vinteum)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.naquadria")
            .heightRange(10,90)
            .weight(40)
            .density(4)
            .size(24)
            .primary(Materials.Naquadah)
            .secondary(Materials.NaquadahEnriched)
            .inBetween(Materials.Naquadria)
            .sporadic(Materials.Trinium)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.awakeneddraconium")
            .heightRange(20,40)
            .weight(20)
            .density(3)
            .size(16)
            .primary(Materials.Draconium)
            .secondary(Materials.Draconium)
            .inBetween(Materials.DraconiumAwakened)
            .sporadic(Materials.NetherStar)
            .add();

        // handled by gagreg
        new OreMixBuilder().name("ore.mix.tengam")
            .heightRange(30,180)
            .weight(80)
            .density(2)
            .size(32)
            .primary(Materials.TengamRaw)
            .secondary(Materials.TengamRaw)
            .inBetween(Materials.Electrotine)
            .sporadic(Materials.Samarium)
            .add();

        new WorldGenGaGT().run();
        GT_Log.out.println("Started Galactic Greg ore gen code");
    }
}
