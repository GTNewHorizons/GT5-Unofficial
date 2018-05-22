package gregtech.loaders.postload;

import cpw.mods.fml.common.Loader;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Log;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;
import gregtech.common.GT_Worldgen_Stone;
import gregtech.common.GT_Worldgenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class GT_Worldgenloader
        implements Runnable {
    public void run() {
        boolean tPFAA = (GregTech_API.sWorldgenFile.get(ConfigCategories.general, "AutoDetectPFAA", true)) && (Loader.isModLoaded("PFAAGeologica"));

        new GT_Worldgenerator();
        //if (Loader.isModLoaded("GalacticraftCore") && Loader.isModLoaded("GalacticraftMars")) {
        //    new GT_Worldgenerator_Space();
        //}

        new GT_Worldgen_Stone("overworld.stone.blackgranite.tiny", true, GregTech_API.sBlockGranites, 0, 0, 1, 50, 48, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.small", true, GregTech_API.sBlockGranites, 0, 0, 1, 100, 96, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.medium", true, GregTech_API.sBlockGranites, 0, 0, 1, 200, 144, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.large", true, GregTech_API.sBlockGranites, 0, 0, 1, 300, 192, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.huge", true, GregTech_API.sBlockGranites, 0, 0, 1, 400, 240, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.tiny", true, GregTech_API.sBlockGranites, 8, 0, 1, 50, 48, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.small", true, GregTech_API.sBlockGranites, 8, 0, 1, 100, 96, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.medium", true, GregTech_API.sBlockGranites, 8, 0, 1, 200, 144, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.large", true, GregTech_API.sBlockGranites, 8, 0, 1, 300, 192, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.huge", true, GregTech_API.sBlockGranites, 8, 0, 1, 400, 240, 0, 120, null, false);

        new GT_Worldgen_Stone("nether.stone.blackgranite.tiny", false, GregTech_API.sBlockGranites, 0, -1, 1, 50, 48, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.small", false, GregTech_API.sBlockGranites, 0, -1, 1, 100, 96, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.medium", false, GregTech_API.sBlockGranites, 0, -1, 1, 200, 144, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.large", false, GregTech_API.sBlockGranites, 0, -1, 1, 300, 192, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.huge", false, GregTech_API.sBlockGranites, 0, -1, 1, 400, 240, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.tiny", false, GregTech_API.sBlockGranites, 8, -1, 1, 50, 48, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.small", false, GregTech_API.sBlockGranites, 8, -1, 1, 100, 96, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.medium", false, GregTech_API.sBlockGranites, 8, -1, 1, 200, 144, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.large", false, GregTech_API.sBlockGranites, 8, -1, 1, 300, 192, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.huge", false, GregTech_API.sBlockGranites, 8, -1, 1, 400, 240, 0, 120, null, false);

        new GT_Worldgen_Stone("overworld.stone.marble.tiny", true, GregTech_API.sBlockStones, 0, 0, 1, 50, 48, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.small", true, GregTech_API.sBlockStones, 0, 0, 1, 100, 96, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.medium", true, GregTech_API.sBlockStones, 0, 0, 1, 200, 144, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.large", true, GregTech_API.sBlockStones, 0, 0, 1, 300, 192, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.huge", true, GregTech_API.sBlockStones, 0, 0, 1, 400, 240, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.tiny", true, GregTech_API.sBlockStones, 8, 0, 1, 50, 48, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.small", true, GregTech_API.sBlockStones, 8, 0, 1, 100, 96, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.medium", true, GregTech_API.sBlockStones, 8, 0, 1, 200, 144, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.large", true, GregTech_API.sBlockStones, 8, 0, 1, 300, 192, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.huge", true, GregTech_API.sBlockStones, 8, 0, 1, 400, 240, 0, 120, null, false);

        new GT_Worldgen_Stone("nether.stone.marble.tiny", false, GregTech_API.sBlockStones, 0, -1, 1, 50, 48, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.small", false, GregTech_API.sBlockStones, 0, -1, 1, 100, 96, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.medium", false, GregTech_API.sBlockStones, 0, -1, 1, 200, 144, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.large", false, GregTech_API.sBlockStones, 0, -1, 1, 300, 192, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.huge", false, GregTech_API.sBlockStones, 0, -1, 1, 400, 240, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.tiny", false, GregTech_API.sBlockStones, 8, -1, 1, 50, 48, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.small", false, GregTech_API.sBlockStones, 8, -1, 1, 100, 96, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.medium", false, GregTech_API.sBlockStones, 8, -1, 1, 200, 144, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.large", false, GregTech_API.sBlockStones, 8, -1, 1, 300, 192, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.huge", false, GregTech_API.sBlockStones, 8, -1, 1, 400, 240, 0, 120, null, false);

        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.copper", true, 60, 120, 32, !tPFAA, true, true, true, true, false, Materials.Copper);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.tin", true, 60, 120, 32, !tPFAA, true, true, true, true, true, Materials.Tin);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.bismuth", true, 80, 120, 8, !tPFAA, true, false, true, true, false, Materials.Bismuth);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.coal", true, 60, 100, 24, !tPFAA, false, false, false, false, false, Materials.Coal);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.iron", true, 40, 80, 16, !tPFAA, true, true, true, true, false, Materials.Iron);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.lead", true, 40, 80, 16, !tPFAA, true, true, true, true, true, Materials.Lead);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.zinc", true, 30, 60, 12, !tPFAA, true, true, true, true, false, Materials.Zinc);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.gold", true, 20, 40, 8, !tPFAA, true, true, true, true, true, Materials.Gold);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.silver", true, 20, 40, 8, !tPFAA, true, true, true, true, true, Materials.Silver);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.nickel", true, 20, 40, 8, !tPFAA, true, true, true, true, true, Materials.Nickel);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.lapis", true, 20, 40, 4, !tPFAA, false, false, true, false, true, Materials.Lapis);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.diamond", true, 5, 10, 2, !tPFAA, true, false, true, true, true, Materials.Diamond);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.emerald", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.Emerald);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.ruby", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.Ruby);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.sapphire", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.Sapphire);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.greensapphire", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.GreenSapphire);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.olivine", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.Olivine);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.topaz", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.Topaz);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.tanzanite", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.Tanzanite);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.amethyst", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.Amethyst);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.opal", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.Opal);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.jasper", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.Jasper);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.bluetopaz", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.BlueTopaz);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.amber", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.Amber);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.foolsruby", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.FoolsRuby);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.garnetred", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.GarnetRed);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.garnetyellow", true, 5, 250, 1, !tPFAA, true, false, false, true, true, Materials.GarnetYellow);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.redstone", true, 5, 20, 8, !tPFAA, true, false, true, true, true, Materials.Redstone);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.platinum", true, 20, 40, 8, false, false, true, false, true, true, Materials.Platinum);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.iridium", true, 20, 40, 8, false, false, true, false, true, true, Materials.Iridium);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.netherquartz", true, 30, 120, 64, false, true, false, false, false, false, Materials.NetherQuartz);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.saltpeter", true, 10, 60, 8, false, true, false, false, false, false, Materials.Saltpeter);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.sulfur_n", true, 10, 60, 32, false, true, false, false, false, false, Materials.Sulfur);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.sulfur_o", true, 5, 15, 8, !tPFAA, false, false, false, false, false, Materials.Sulfur);

        int i = 0;
        for (int j = GregTech_API.sWorldgenFile.get("worldgen", "AmountOfCustomSmallOreSlots", 16); i < j; i++) {
            new GT_Worldgen_GT_Ore_SmallPieces("ore.small.custom." + (i < 10 ? "0" : "") + i, false, 0, 0, 0, false, false, false, false, false, false, Materials._NULL);
        }
        /*if (GregTech_API.mImmersiveEngineering && GT_Mod.gregtechproxy.mImmersiveEngineeringRecipes) {
            blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralList.clear();
            blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralCache.clear();
        }*/

        new GT_Worldgen_GT_Ore_Layer("ore.mix.naquadah", true, 10, 90, 30, 5, 32, false, false, false, Materials.Naquadah, Materials.Naquadah, Materials.Naquadah, Materials.NaquadahEnriched);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.lignite", true, 80, 210, 160, 8, 32, true, false, false, Materials.Lignite, Materials.Lignite, Materials.Lignite, Materials.Coal);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.coal", true, 30, 80, 80, 6, 32, true, false, false, Materials.Coal, Materials.Coal, Materials.Coal, Materials.Lignite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.magnetite", true, 60, 180, 160, 3, 32, true, false, false, Materials.Magnetite, Materials.Magnetite, Materials.Iron, Materials.VanadiumMagnetite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.gold", true, 30, 60, 160, 3, 32, true, false, true, Materials.Magnetite, Materials.Magnetite, Materials.VanadiumMagnetite, Materials.Gold);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.iron", true, 10, 40, 120, 4, 24, true, true, false, Materials.BrownLimonite, Materials.YellowLimonite, Materials.BandedIron, Materials.Malachite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.cassiterite", true, 60, 220, 50, 5, 24, true, false, true, Materials.Tin, Materials.Tin, Materials.Cassiterite, Materials.Tin);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.tetrahedrite", true, 80, 120, 70, 4, 24, false, true, true, Materials.Tetrahedrite, Materials.Tetrahedrite, Materials.Copper, Materials.Stibnite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.netherquartz", true, 40, 80, 80, 5, 24, false, true, false, Materials.NetherQuartz, Materials.NetherQuartz, Materials.NetherQuartz, Materials.NetherQuartz);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.sulfur", true, 5, 20, 100, 5, 24, false, true, false, Materials.Sulfur, Materials.Sulfur, Materials.Pyrite, Materials.Sphalerite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.copper", true, 5, 60, 80, 4, 24, true, true, true, Materials.Chalcopyrite, Materials.Iron, Materials.Pyrite, Materials.Copper);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.bauxite", true, 10, 80, 80, 4, 24, false, false, false, Materials.Bauxite, Materials.Bauxite, Materials.Aluminium, Materials.Ilmenite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.salts", true, 50, 70, 50, 3, 24, true, false, false, Materials.RockSalt, Materials.Salt, Materials.Lepidolite, Materials.Spodumene);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.redstone", true, 5, 40, 60, 3, 24, true, true, false, Materials.Redstone, Materials.Redstone, Materials.Ruby, Materials.Cinnabar);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.soapstone", true, 20, 50, 40, 3, 16, true, false, false, Materials.Soapstone, Materials.Talc, Materials.Glauconite, Materials.Pentlandite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.nickel", true, 10, 40, 40, 3, 16, false, false, true, Materials.Garnierite, Materials.Nickel, Materials.Cobaltite, Materials.Pentlandite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.platinum", true, 40, 50, 5, 3, 16, false, false, false, Materials.Cooperite, Materials.Palladium, Materials.Platinum, Materials.Iridium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.pitchblende", true, 30, 60, 40, 3, 16, false, false, false, Materials.Pitchblende, Materials.Pitchblende, Materials.Uraninite, Materials.Uraninite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.monazite", true, 20, 40, 30, 3, 16, false, false, false, Materials.Bastnasite, Materials.Bastnasite, Materials.Monazite, Materials.Neodymium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.molybdenum", true, 20, 50, 5, 3, 16, false, true, true, Materials.Wulfenite, Materials.Molybdenite, Materials.Molybdenum, Materials.Powellite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.tungstate", true, 20, 60, 10, 3, 16, false, false, false, Materials.Scheelite, Materials.Scheelite, Materials.Tungstate, Materials.Lithium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.sapphire", true, 10, 40, 60, 3, 16, false, false, false, Materials.Almandine, Materials.Pyrope, Materials.Sapphire, Materials.GreenSapphire);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.manganese", true, 20, 30, 20, 3, 16, true, true, false, Materials.Grossular, Materials.Spessartine, Materials.Pyrolusite, Materials.Tantalite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.quartz", true, 40, 80, 20, 3, 16, false, true, false, Materials.Quartzite, Materials.Barite, Materials.CertusQuartz, Materials.CertusQuartz);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.diamond", true, 5, 20, 40, 2, 16, true, false, false, Materials.Graphite, Materials.Graphite, Materials.Diamond, Materials.Coal);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.olivine", true, 10, 40, 60, 3, 16, false, false, false, Materials.Bentonite, Materials.Magnesite, Materials.Olivine, Materials.Glauconite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.apatite", true, 40, 60, 60, 3, 16, true, false, false, Materials.Apatite, Materials.Apatite, Materials.Phosphorus, Materials.Pyrochlore);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.galena", true, 5, 45, 40, 5, 16, false, false, false, Materials.Galena, Materials.Galena, Materials.Silver, Materials.Lead);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.lapis", true, 20, 50, 40, 5, 16, true, false, false, Materials.Lazurite, Materials.Sodalite, Materials.Lapis, Materials.Calcite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.beryllium", true, 5, 30, 30, 3, 16, false, true, true, Materials.Beryllium, Materials.Beryllium, Materials.Emerald, Materials.Thorium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.uranium", true, 20, 30, 20, 3, 16, false, false, false, Materials.Uraninite, Materials.Uraninite, Materials.Uranium, Materials.Uranium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.oilsand", true, 50, 80, 40, 6, 16, true, false, false, Materials.Oilsands, Materials.Oilsands, Materials.Oilsands, Materials.Oilsands);
        
        /*
         * custom GTNH OreMixes
         * WARNING: NO DUPLICATS IN aName OR DEPRECATED MATERIALS IN HERE.
         * Materials can be used unlimited, since achievements for Ores are turned off.
         */
        
        //aName, aDefault, aMinY, aMaxY, aWeight, aDensity, aSize, aOverworld, aNether, aEnd, aPrimary, aSecondary, aBetween, aSporadic
        new GT_Worldgen_GT_Ore_Layer("ore.mix.neutronium", true, 5, 30, 10, 3, 16, false, false, false, Materials.Neutronium, Materials.Adamantium, Materials.Naquadah, Materials.Titanium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.aquaignis", true, 5, 35, 16, 3, 16, false, false, false, Materials.InfusedWater, Materials.InfusedFire, Materials.Amber, Materials.Cinnabar);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.terraaer", true, 5, 35, 16, 3, 16, false, false, false, Materials.InfusedEarth, Materials.InfusedAir, Materials.Amber, Materials.Cinnabar);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.perditioordo", true, 5, 35, 16, 3, 16, false, false, false, Materials.InfusedEntropy, Materials.InfusedOrder, Materials.Amber, Materials.Cinnabar);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.coppertin", true, 180, 200, 80, 4, 24, true, false, false, Materials.Chalcopyrite, Materials.Vermiculite, Materials.Cassiterite, Materials.Alunite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.titaniumchrome", true, 10, 70, 16, 3, 16, false, false, false, Materials.Titanium, Materials.Chromite, Materials.Uvarovite, Materials.Perlite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.mineralsand", true, 50, 60, 80, 4, 24, true, false, false, Materials.BasalticMineralSand, Materials.GraniticMineralSand, Materials.FullersEarth, Materials.Gypsum);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.garnettin", true, 50, 60, 80, 4, 24, true, false, false, Materials.CassiteriteSand, Materials.GarnetSand, Materials.Asbestos, Materials.Diatomite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.kaolinitezeolite", true, 50, 70, 60, 6, 16, true, false, false, Materials.Kaolinite, Materials.Zeolite, Materials.FullersEarth, Materials.GlauconiteSand);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.mica", true, 20, 40, 20, 3, 16, true, false, false, Materials.Kyanite, Materials.Mica, Materials.Cassiterite, Materials.Pollucite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.dolomite", true, 150, 200, 40, 5, 24, true, false, false, Materials.Dolomite, Materials.Wollastonite, Materials.Trona, Materials.Andradite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.platinumchrome", true, 5, 30, 10, 3, 16, false, false, false, Materials.Platinum, Materials.Chrome, Materials.Cooperite, Materials.Palladium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.iridiummytryl", true, 15, 40, 10, 3, 16, false, false, false, Materials.Nickel, Materials.Iridium, Materials.Palladium, Materials.Mithril);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.osmium", true, 5, 30, 10, 3, 16, false, false, false, Materials.Nickel, Materials.Osmium, Materials.Iridium, Materials.Nickel);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.saltpeterelectrotine", true, 5, 45, 40, 4, 16, false, true, false, Materials.Saltpeter, Materials.Diatomite, Materials.Electrotine, Materials.Alunite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.desh", true, 5, 40, 30, 3, 16, false, false, false, Materials.Desh, Materials.Desh, Materials.Scheelite, Materials.Tungstate);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.draconium", true, 20, 40, 40, 2, 16, false, false, false, Materials.Draconium, Materials.Electrotine, Materials.Jade, Materials.Vinteum);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.quantium", true, 5, 25, 30, 3, 24, false, false, false, Materials.Quantium, Materials.Amethyst, Materials.Rutile, Materials.Ardite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.callistoice", true, 40, 60, 40, 3, 16, false, false, false, Materials.CallistoIce, Materials.Topaz, Materials.BlueTopaz, Materials.Alduorite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.mytryl", true, 10, 30, 40, 3, 16, false, false, false, Materials.Mytryl, Materials.Jasper, Materials.Ceruclase, Materials.Vulcanite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.ledox", true, 55, 65, 30, 3, 24, false, false, false, Materials.Ledox, Materials.Opal, Materials.Orichalcum, Materials.Rubracium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.oriharukon", true, 30, 60, 40, 3, 16, false, false, false, Materials.Oriharukon, Materials.Tanzanite, Materials.Vyroxeres, Materials.Mirabilite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.blackplutonium", true, 5, 25, 40, 3, 24, false, false, false, Materials.BlackPlutonium, Materials.GarnetRed, Materials.GarnetYellow, Materials.Borax);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.infusedgold", true, 15, 40, 30, 3, 16, false, false, false, Materials.Gold, Materials.Gold, Materials.InfusedGold, Materials.Platinum);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.niobium", true, 5, 30, 60, 3, 24, false, false, false, Materials.Niobium, Materials.Yttrium, Materials.Gallium, Materials.Gallium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.tungstenirons", true, 5, 25, 16, 3, 30, false, false, false, Materials.Tungsten, Materials.Silicon, Materials.DeepIron, Materials.ShadowIron);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.europium", true, 10, 30, 60, 3, 24, false, false, false, Materials.Thorium, Materials.Europium, Materials.Plutonium241, Materials.Uranium235);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.vanadiumgold", true, 10, 50, 60, 3, 24, false, false, false, Materials.Vanadium, Materials.Magnetite, Materials.Gold, Materials.Chrome);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.netherstar", true, 20, 60, 60, 3, 24, false, false, false, Materials.GarnetSand, Materials.NetherStar, Materials.GarnetRed, Materials.GarnetYellow);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.garnet", true, 10, 30, 40, 3, 16, false, false, false, Materials.GarnetRed, Materials.GarnetYellow, Materials.Chrysotile, Materials.Realgar);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.rareearth", true, 30, 60, 40, 3, 24, false, false, false, Materials.Cadmium, Materials.Caesium, Materials.Lanthanum, Materials.Cerium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.naquadriagiant", true, 5, 15, 100, 10, 32, false, false, false, Materials.Naquadria, Materials.Naquadah, Materials.Naquadria, Materials.Naquadah);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.enrichednaquadahgiant", true, 10, 20, 100, 10, 32, false, false, false, Materials.NaquadahEnriched, Materials.Neutronium, Materials.NaquadahEnriched, Materials.Neutronium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.aerterragiant", true, 15, 25, 100, 10, 32, false, false, false, Materials.InfusedAir, Materials.InfusedEarth, Materials.InfusedAir, Materials.InfusedEarth);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.aquaodrogiant", true, 20, 30, 100, 10, 32, false, false, false, Materials.InfusedOrder, Materials.InfusedWater, Materials.InfusedOrder, Materials.InfusedWater);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.ignisperditiogiant", true, 25, 35, 100, 10, 32, false, false, false, Materials.InfusedFire, Materials.InfusedEntropy, Materials.InfusedFire, Materials.InfusedEntropy);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.osmiumiridiumgiant", true, 30, 40, 100, 10, 32, false, false, false, Materials.Osmium, Materials.Iridium, Materials.Osmium, Materials.Iridium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.infinitycatalystgiant", true, 35, 45, 100, 10, 32, false, false, false, Materials.InfinityCatalyst, Materials.BlackPlutonium, Materials.InfinityCatalyst, Materials.BlackPlutonium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.palladiumgiant", true, 40, 50, 100, 10, 32, false, false, false, Materials.Palladium, Materials.Platinum, Materials.Palladium, Materials.Platinum);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.infusedgoldgiant", true, 45, 55, 100, 10, 32, false, false, false, Materials.InfusedGold, Materials.Cooperite, Materials.InfusedGold, Materials.Cooperite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.ironsgiant", true, 50, 60, 100, 10, 32, false, false, false, Materials.DeepIron, Materials.ShadowIron, Materials.DeepIron, Materials.ShadowIron);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.europiumgiant", true, 55, 65, 100, 10, 32, false, false, false, Materials.Europium, Materials.Thorium, Materials.Europium, Materials.Thorium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.pu244u238giant", true, 60, 70, 100, 10, 32, false, false, false, Materials.Plutonium, Materials.Uranium, Materials.Plutonium, Materials.Uranium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.pu241u235giant", true, 65, 75, 100, 10, 32, false, false, false, Materials.Plutonium241, Materials.Uranium235, Materials.Plutonium241, Materials.Uranium235);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.chromegiant", true, 70, 80, 100, 10, 32, false, false, false, Materials.Chrome, Materials.Vanadium, Materials.Chrome, Materials.Vanadium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.antimonygiant", true, 140, 150, 100, 10, 32, false, false, false, Materials.Stibnite, Materials.Tantalite, Materials.Stibnite, Materials.Tantalite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.tungstengiant", true, 145, 155, 100, 10, 32, false, false, false, Materials.Tungstate, Materials.Tungsten, Materials.Tungstate, Materials.Tungsten);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.powelitelitiumgiant", true, 150, 160, 100, 10, 32, false, false, false, Materials.Powellite, Materials.Lithium, Materials.Powellite, Materials.Lithium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.adamantiumaluminiumgiant", true, 155, 165, 100, 10, 32, false, false, false, Materials.Adamantium, Materials.Aluminium, Materials.Adamantium, Materials.Aluminium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.molybdengiant", true, 160, 170, 100, 10, 32, false, false, false, Materials.Molybdenite, Materials.Molybdenum, Materials.Molybdenite, Materials.Molybdenum);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.chrometitangiant", true, 165, 175, 100, 10, 32, false, false, false, Materials.Chromite, Materials.Titanium, Materials.Chromite, Materials.Titanium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.uvarovitegiant", true, 170, 180, 100, 10, 32, false, false, false, Materials.Uvarovite, Materials.Perlite, Materials.Uvarovite, Materials.Perlite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.zinctitangiant", true, 175, 185, 100, 10, 32, false, false, false, Materials.Sphalerite, Materials.Ilmenite, Materials.Sphalerite, Materials.Ilmenite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.rubynickelgiant", true, 180, 190, 100, 10, 32, false, false, false, Materials.Ruby, Materials.Pentlandite, Materials.Ruby, Materials.Pentlandite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.nickelcobaldgiant", true, 185, 195, 100, 10, 32, false, false, false, Materials.Garnierite, Materials.Cobaltite, Materials.Garnierite, Materials.Cobaltite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.pyropealmandinegiant", true, 190, 200, 100, 10, 32, false, false, false, Materials.Pyrope, Materials.Almandine, Materials.Pyrope, Materials.Almandine);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.diamondgiant", true, 195, 205, 100, 10, 32, false, false, false, Materials.Diamond, Materials.Bentonite, Materials.Diamond, Materials.Bentonite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.sapphiregiant", true, 200, 210, 100, 10, 32, false, false, false, Materials.Sapphire, Materials.GreenSapphire, Materials.Sapphire, Materials.GreenSapphire);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.certusquartzgiant", true, 205, 215, 100, 10, 32, false, false, false, Materials.CertusQuartz, Materials.Quartzite, Materials.CertusQuartz, Materials.Quartzite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.olivinegiant", true, 210, 220, 100, 10, 32, false, false, false, Materials.Magnesite, Materials.Olivine, Materials.Magnesite, Materials.Olivine);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.silvergiant", true, 215, 225, 100, 10, 32, false, false, false, Materials.Silver, Materials.Galena, Materials.Silver, Materials.Galena);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.pbmeteoricirongiant", true, 220, 230, 100, 10, 32, false, false, false, Materials.Lead, Materials.MeteoricIron, Materials.Lead, Materials.MeteoricIron);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.siliconmanganesegiant", true, 225, 235, 100, 10, 32, false, false, false, Materials.Silicon, Materials.Manganese, Materials.Silicon, Materials.Manganese);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.spessartinegiant", true, 230, 240, 100, 10, 32, false, false, false, Materials.Spessartine, Materials.Grossular, Materials.Spessartine, Materials.Grossular);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.mithrilgiant", true, 235, 245, 100, 10, 32, false, false, false, Materials.Phosphate, Materials.Mithril, Materials.Phosphate, Materials.Mithril);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.sodalitelazuritegiant", true, 240, 250, 100, 10, 32, false, false, false, Materials.Sodalite, Materials.Lazurite, Materials.Sodalite, Materials.Lazurite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.lapisemeraldgiant", true, 5, 15, 100, 10, 32, false, false, false, Materials.Lapis, Materials.Emerald, Materials.Lapis, Materials.Emerald);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.arditescheelitegiant", true, 10, 20, 100, 10, 32, false, false, false, Materials.Ardite, Materials.Scheelite, Materials.Ardite, Materials.Scheelite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.jaspermytrylgiant", true, 15, 25, 100, 10, 32, false, false, false, Materials.Jasper, Materials.Mytryl, Materials.Jasper, Materials.Mytryl);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.opalledoxgiant", true, 20, 30, 100, 10, 32, false, false, false, Materials.Opal, Materials.Ledox, Materials.Opal, Materials.Ledox);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.quantiumgiant", true, 25, 35, 100, 10, 32, false, false, false, Materials.Quantium, Materials.Amethyst, Materials.Quantium, Materials.Amethyst);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.yellowgarnetgiant", true, 30, 40, 100, 10, 32, false, false, false, Materials.GarnetYellow, Materials.Rutile, Materials.GarnetYellow, Materials.Rutile);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.galliumniobiumgiant", true, 35, 45, 100, 10, 32, false, false, false, Materials.Gallium, Materials.Niobium, Materials.Gallium, Materials.Niobium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.netherstargiant", true, 40, 50, 100, 10, 32, false, false, false, Materials.Yttrium, Materials.NetherStar, Materials.Yttrium, Materials.NetherStar);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.ceruclasevulcanitegiant", true, 45, 55, 100, 10, 32, false, false, false, Materials.Ceruclase, Materials.Vulcanite, Materials.Ceruclase, Materials.Vulcanite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.oriharukongiant", true, 50, 60, 100, 10, 32, false, false, false, Materials.Rubracium, Materials.Oriharukon, Materials.Rubracium, Materials.Oriharukon);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.tanzaniteorichalcumgiant", true, 55, 65, 100, 10, 32, false, false, false, Materials.Tanzanite, Materials.Orichalcum, Materials.Tanzanite, Materials.Orichalcum);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.vyroxeresmirabilitegiant", true, 60, 70, 100, 10, 32, false, false, false, Materials.Vyroxeres, Materials.Mirabilite, Materials.Vyroxeres, Materials.Mirabilite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.mithrilmagnesiumgiant", true, 65, 75, 100, 10, 32, false, false, false, Materials.Mithril, Materials.Magnesium, Materials.Mithril, Materials.Magnesium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.nikolitegiant", true, 70, 80, 100, 10, 32, false, false, false, Materials.Electrotine, Materials.Alunite, Materials.Electrotine, Materials.Alunite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.draconiumvinteumgiant", true, 140, 150, 100, 10, 32, false, false, false, Materials.Vinteum, Materials.Draconium, Materials.Vinteum, Materials.Draconium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.saltpetergiant", true, 145, 155, 100, 10, 32, false, false, false, Materials.Jade, Materials.Saltpeter, Materials.Jade, Materials.Saltpeter);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.callistoicegiant", true, 150, 160, 100, 10, 32, false, false, false, Materials.Alduorite, Materials.CallistoIce, Materials.Alduorite, Materials.CallistoIce);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.topazbluetopazgiant", true, 155, 165, 100, 10, 32, false, false, false, Materials.Topaz, Materials.BlueTopaz, Materials.Topaz, Materials.BlueTopaz);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.caesiumcadmiumgiant", true, 160, 170, 100, 10, 32, false, false, false, Materials.Caesium, Materials.Cadmium, Materials.Caesium, Materials.Cadmium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.lanthanumceriumgiant", true, 165, 175, 100, 10, 32, false, false, false, Materials.Lanthanum, Materials.Cerium, Materials.Lanthanum, Materials.Cerium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.cobaltboraxgiant", true, 170, 180, 100, 10, 32, false, false, false, Materials.Cobalt, Materials.Borax, Materials.Cobalt, Materials.Borax);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.richnuclear", true, 65, 120, 5, 2, 8, false, false, false, Materials.Uranium, Materials.Plutonium, Materials.Thorium, Materials.Thorium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.bedrockiumawakedraconiumgiant", true, 175, 185, 100, 10, 32, false, false, false, Materials.Bedrockium, Materials.DraconiumAwakened, Materials.Bedrockium, Materials.DraconiumAwakened);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.copperantimonygiant", true, 180, 190, 100, 10, 32, false, false, false, Materials.Copper, Materials.Antimony, Materials.Copper, Materials.Antimony);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.cassiteritearsenicgiant", true, 185, 195, 100, 10, 32, false, false, false, Materials.Cassiterite, Materials.Arsenic, Materials.Cassiterite, Materials.Arsenic);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.lepidoliteneodymiumgiant", true, 190, 200, 100, 10, 32, false, false, false, Materials.Neodymium, Materials.Lepidolite, Materials.Neodymium, Materials.Lepidolite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.rocksaltsaltgiant", true, 195, 205, 100, 10, 32, false, false, false, Materials.Salt, Materials.RockSalt, Materials.Salt, Materials.RockSalt);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.micacassiteritegiant", true, 200, 210, 100, 10, 32, false, false, false, Materials.Mica, Materials.Cassiterite, Materials.Mica, Materials.Cassiterite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.realgarneodymiumgiant", true, 205, 215, 100, 10, 32, false, false, false, Materials.Realgar, Materials.Neodymium, Materials.Realgar, Materials.Neodymium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.heavypentele", true, 40, 60, 60, 5, 32, false, false, false, Materials.Arsenic, Materials.Bismuth, Materials.Antimony, Materials.Antimony);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.europa", true, 55, 65, 110, 4, 24, false, false, false, Materials.Magnesite, Materials.BandedIron, Materials.Sulfur, Materials.Opal);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.europacore", true, 5, 15, 5, 2, 16, false, false, false, Materials.Chrome, Materials.Tungsten, Materials.Molybdenum, Materials.Manganese);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.secondlanthanid", true, 10, 40, 10, 3, 24, false, false, false, Materials.Samarium, Materials.Europium, Materials.Tartarite, Materials.Tartarite);

        i = 0;
        for (int j = GregTech_API.sWorldgenFile.get("worldgen", "AmountOfCustomLargeVeinSlots", 16); i < j; i++) {
            new GT_Worldgen_GT_Ore_Layer("ore.mix.custom." + (i < 10 ? "0" : "") + i, false, 0, 0, 0, 0, 0, false, false, false, Materials._NULL, Materials._NULL, Materials._NULL, Materials._NULL);
        }
        
        //DO NOT DELETE V THIS V - this is needed so that gregtech generates its Ore Layer's first (the ones up there), which can then be transformed into "GT_Worldgen_GT_Ore_Layer_Space". Also Reflexion is slow.
        try {
            Class clazz = Class.forName("bloodasp.galacticgreg.WorldGenGaGT");
            Constructor constructor=clazz.getConstructor();
            Method method=clazz.getMethod("run");
            method.invoke(constructor.newInstance());
            GT_Log.out.println("Started Galactic Greg ore gen code");
            //this function calls Galactic Greg and enables its generation.
        }catch (Exception e){
            GT_Log.err.println("Unable to start Galactic Greg ore gen code");
            e.printStackTrace(GT_Log.err);
        }
        //DO NOT DELETE ^ THIS ^
        
        /*if (GregTech_API.mImmersiveEngineering && GT_Mod.gregtechproxy.mImmersiveEngineeringRecipes) {
            blusunrize.immersiveengineering.api.tool.ExcavatorHandler.recalculateChances(true);
        }*/
    }
}
