package gregtech.loaders.postload;

import bloodasp.galacticgreg.GT_Worldgenerator_Space;
import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
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

        new GT_Worldgenerator();

        new GT_Worldgen_Stone("overworld.stone.blackgranite.tiny", true, GregTech_API.sBlockGranites, 0, 0, 1, 75, 5, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.small", true, GregTech_API.sBlockGranites, 0, 0, 1, 100, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.medium", true, GregTech_API.sBlockGranites, 0, 0, 1, 200, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.large", true, GregTech_API.sBlockGranites, 0, 0, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.blackgranite.huge", true, GregTech_API.sBlockGranites, 0, 0, 1, 400, 150, 0, 120, null, false);
        
        new GT_Worldgen_Stone("overworld.stone.redgranite.tiny", true, GregTech_API.sBlockGranites, 8, 0, 1, 75, 5, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.small", true, GregTech_API.sBlockGranites, 8, 0, 1, 100, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.medium", true, GregTech_API.sBlockGranites, 8, 0, 1, 200, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.large", true, GregTech_API.sBlockGranites, 8, 0, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.redgranite.huge", true, GregTech_API.sBlockGranites, 8, 0, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Stone("overworld.stone.marble.tiny", true, GregTech_API.sBlockStones, 0, 0, 1, 75, 5, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.small", true, GregTech_API.sBlockStones, 0, 0, 1, 100, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.medium", true, GregTech_API.sBlockStones, 0, 0, 1, 200, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.large", true, GregTech_API.sBlockStones, 0, 0, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.marble.huge", true, GregTech_API.sBlockStones, 0, 0, 1, 400, 150, 0, 120, null, false);
        
        new GT_Worldgen_Stone("overworld.stone.basalt.tiny", true, GregTech_API.sBlockStones, 8, 0, 1, 75, 5, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.small", true, GregTech_API.sBlockStones, 8, 0, 1, 100, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.medium", true, GregTech_API.sBlockStones, 8, 0, 1, 200, 10, 0, 180, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.large", true, GregTech_API.sBlockStones, 8, 0, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("overworld.stone.basalt.huge", true, GregTech_API.sBlockStones, 8, 0, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Stone("nether.stone.blackgranite.tiny", false, GregTech_API.sBlockGranites, 0, -1, 1, 50, 45, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.small", false, GregTech_API.sBlockGranites, 0, -1, 1, 100, 60, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.medium", false, GregTech_API.sBlockGranites, 0, -1, 1, 200, 80, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.large", false, GregTech_API.sBlockGranites, 0, -1, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.blackgranite.huge", false, GregTech_API.sBlockGranites, 0, -1, 1, 400, 150, 0, 120, null, false);
        
        new GT_Worldgen_Stone("nether.stone.redgranite.tiny", false, GregTech_API.sBlockGranites, 8, -1, 1, 50, 45, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.small", false, GregTech_API.sBlockGranites, 8, -1, 1, 100, 60, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.medium", false, GregTech_API.sBlockGranites, 8, -1, 1, 200, 80, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.large", false, GregTech_API.sBlockGranites, 8, -1, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.redgranite.huge", false, GregTech_API.sBlockGranites, 8, -1, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_Stone("nether.stone.marble.tiny", false, GregTech_API.sBlockStones, 0, -1, 1, 50, 45, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.small", false, GregTech_API.sBlockStones, 0, -1, 1, 100, 60, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.medium", false, GregTech_API.sBlockStones, 0, -1, 1, 200, 80, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.large", false, GregTech_API.sBlockStones, 0, -1, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.marble.huge", false, GregTech_API.sBlockStones, 0, -1, 1, 400, 150, 0, 120, null, false);
        
        new GT_Worldgen_Stone("nether.stone.basalt.tiny", false, GregTech_API.sBlockStones, 8, -1, 1, 50, 45, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.small", false, GregTech_API.sBlockStones, 8, -1, 1, 100, 60, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.medium", false, GregTech_API.sBlockStones, 8, -1, 1, 200, 80, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.large", false, GregTech_API.sBlockStones, 8, -1, 1, 300, 70, 0, 120, null, false);
        new GT_Worldgen_Stone("nether.stone.basalt.huge", false, GregTech_API.sBlockStones, 8, -1, 1, 400, 150, 0, 120, null, false);

        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.copper", true, 60, 120, 32, true, true, true, true, true, false, Materials.Copper);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.tin", true, 60, 120, 32, true, true, true, true, true, true, Materials.Tin);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.bismuth", true, 80, 120, 8, false, true, false, true, true, false, Materials.Bismuth);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.coal", true, 60, 100, 24, true, false, false, false, false, false, Materials.Coal);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.iron", true, 40, 80, 16, true, true, true, true, true, false, Materials.Iron);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.lead", true, 40, 80, 16, false, true, true, true, true, true, Materials.Lead);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.zinc", true, 30, 60, 12, true, true, true, true, true, false, Materials.Zinc);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.gold", true, 20, 40, 8, true, true, true, true, true, true, Materials.Gold);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.silver", true, 20, 40, 8, true, true, true, true, true, true, Materials.Silver);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.nickel", true, 20, 40, 8, true, true, true, true, true, true, Materials.Nickel);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.lapis", true, 20, 40, 4, true, false, false, true, false, true, Materials.Lapis);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.diamond", true, 5, 10, 2, true, true, false, true, true, true, Materials.Diamond);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.emerald", true, 5, 250, 1, false, true, false, false, true, true, Materials.Emerald);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.ruby", true, 5, 250, 1, false, true, false, false, true, true, Materials.Ruby);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.sapphire", true, 5, 250, 1, false, true, false, false, true, true, Materials.Sapphire);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.greensapphire", true, 5, 250, 1, false, true, false, false, true, true, Materials.GreenSapphire);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.olivine", true, 5, 250, 1, false, true, false, false, true, true, Materials.Olivine);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.topaz", true, 5, 250, 1, false, true, false, false, true, true, Materials.Topaz);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.tanzanite", true, 5, 250, 1, false, true, false, false, true, true, Materials.Tanzanite);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.amethyst", true, 5, 250, 1, false, true, false, false, true, true, Materials.Amethyst);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.opal", true, 5, 250, 1, false, true, false, false, true, true, Materials.Opal);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.jasper", true, 5, 250, 1, false, true, false, false, true, true, Materials.Jasper);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.bluetopaz", true, 5, 250, 1, false, true, false, false, true, true, Materials.BlueTopaz);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.amber", true, 5, 250, 1, false, true, false, false, true, true, Materials.Amber);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.foolsruby", true, 5, 250, 1, false, true, false, false, true, true, Materials.FoolsRuby);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.garnetred", true, 5, 250, 1, false, true, false, false, true, true, Materials.GarnetRed);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.garnetyellow", true, 5, 250, 1, false, true, false, false, true, true, Materials.GarnetYellow);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.redstone", true, 5, 20, 8, true, true, false, true, true, true, Materials.Redstone);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.platinum", true, 20, 40, 8, false, false, true, false, true, true, Materials.Platinum);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.iridium", true, 20, 40, 8, false, false, true, false, true, true, Materials.Iridium);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.netherquartz", true, 30, 120, 64, false, true, false, false, false, false, Materials.NetherQuartz);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.saltpeter", true, 10, 60, 8, false, true, false, false, false, false, Materials.Saltpeter);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.sulfur_n", true, 10, 60, 32, false, true, false, false, false, false, Materials.Sulfur);
        new GT_Worldgen_GT_Ore_SmallPieces("ore.small.sulfur_o", true, 5, 15, 8, false, false, false, false, false, false, Materials.Sulfur);

        
        /*if (GregTech_API.mImmersiveEngineering && GT_Mod.gregtechproxy.mImmersiveEngineeringRecipes) {
            blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralList.clear();
            blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralCache.clear();
        }*/

        new GT_Worldgen_GT_Ore_Layer("ore.mix.naquadah", true, 10, 90, 30, 4, 32, false, false, true, Materials.Naquadah, Materials.Naquadah, Materials.Naquadah, Materials.NaquadahEnriched);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.lignite", true, 80, 210, 160, 7, 32, true, false, false, Materials.Lignite, Materials.Lignite, Materials.Lignite, Materials.Coal);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.coal", true, 30, 80, 80, 5, 32, true, false, false, Materials.Coal, Materials.Coal, Materials.Coal, Materials.Lignite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.magnetite", true, 60, 180, 160, 2, 32, true, false, false, Materials.Magnetite, Materials.Magnetite, Materials.Iron, Materials.VanadiumMagnetite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.gold", true, 30, 60, 160, 2, 32, true, false, true, Materials.Magnetite, Materials.Magnetite, Materials.VanadiumMagnetite, Materials.Gold);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.iron", true, 10, 40, 120, 3, 24, true, true, false, Materials.BrownLimonite, Materials.YellowLimonite, Materials.BandedIron, Materials.Malachite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.cassiterite", true, 60, 220, 50, 4, 24, true, false, true, Materials.Tin, Materials.Tin, Materials.Cassiterite, Materials.Tin);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.tetrahedrite", true, 80, 120, 70, 3, 24, false, true, true, Materials.Tetrahedrite, Materials.Tetrahedrite, Materials.Copper, Materials.Stibnite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.netherquartz", true, 40, 80, 80, 4, 24, false, true, false, Materials.NetherQuartz, Materials.NetherQuartz, Materials.NetherQuartz, Materials.Quartzite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.sulfur", true, 5, 20, 100, 4, 24, false, true, false, Materials.Sulfur, Materials.Sulfur, Materials.Pyrite, Materials.Sphalerite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.copper", true, 5, 60, 80, 3, 24, true, true, true, Materials.Chalcopyrite, Materials.Iron, Materials.Pyrite, Materials.Copper);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.bauxite", true, 10, 80, 80, 3, 24, true, true, true, Materials.Bauxite, Materials.Ilmenite, Materials.Aluminium, Materials.Ilmenite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.salts", true, 50, 70, 50, 2, 24, true, false, false, Materials.RockSalt, Materials.Salt, Materials.Lepidolite, Materials.Spodumene);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.redstone", true, 5, 40, 60, 2, 24, true, true, false, Materials.Redstone, Materials.Redstone, Materials.Ruby, Materials.Cinnabar);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.soapstone", true, 20, 50, 40, 2, 16, true, false, false, Materials.Soapstone, Materials.Talc, Materials.Glauconite, Materials.Pentlandite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.nickel", true, 10, 40, 40, 2, 16, true, false, true, Materials.Garnierite, Materials.Nickel, Materials.Cobaltite, Materials.Pentlandite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.platinum", true, 40, 50, 5, 2, 16, true, false, true, Materials.Cooperite, Materials.Palladium, Materials.Platinum, Materials.Iridium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.pitchblende", true, 30, 60, 40, 2, 16, true, false, false, Materials.Pitchblende, Materials.Pitchblende, Materials.Uraninite, Materials.Uraninite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.monazite", true, 20, 40, 30, 2, 16, true, false, false, Materials.Bastnasite, Materials.Bastnasite, Materials.Monazite, Materials.Neodymium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.molybdenum", true, 20, 50, 5, 2, 16, false, true, true, Materials.Wulfenite, Materials.Molybdenite, Materials.Molybdenum, Materials.Powellite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.tungstate", true, 20, 60, 10, 2, 16, true, false, true, Materials.Scheelite, Materials.Scheelite, Materials.Tungstate, Materials.Lithium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.sapphire", true, 10, 40, 60, 2, 16, true, false, false, Materials.Almandine, Materials.Pyrope, Materials.Sapphire, Materials.GreenSapphire);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.manganese", true, 20, 30, 20, 2, 16, true, true, false, Materials.Grossular, Materials.Spessartine, Materials.Pyrolusite, Materials.Tantalite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.quartz", true, 80, 120, 30, 2, 16, true, true, false, Materials.Quartzite, Materials.Barite, Materials.CertusQuartz, Materials.CertusQuartz);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.diamond", true, 5, 20, 40, 1, 16, true, false, false, Materials.Graphite, Materials.Graphite, Materials.Diamond, Materials.Coal);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.olivine", true, 10, 40, 60, 2, 16, true, false, false, Materials.Bentonite, Materials.Magnesite, Materials.Olivine, Materials.Glauconite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.apatite", true, 40, 60, 60, 2, 16, true, false, false, Materials.Apatite, Materials.Apatite, Materials.Phosphorus, Materials.Pyrochlore);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.galena", true, 5, 45, 40, 4, 16, true, true, false, Materials.Galena, Materials.Galena, Materials.Silver, Materials.Lead);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.lapis", true, 20, 50, 40, 4, 16, true, false, false, Materials.Lazurite, Materials.Sodalite, Materials.Lapis, Materials.Calcite);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.beryllium", true, 5, 30, 30, 2, 16, false, true, true, Materials.Beryllium, Materials.Beryllium, Materials.Emerald, Materials.Thorium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.uranium", true, 20, 30, 20, 2, 16, true, false, false, Materials.Uraninite, Materials.Uraninite, Materials.Uranium, Materials.Uranium);
        new GT_Worldgen_GT_Ore_Layer("ore.mix.oilsand", true, 50, 80, 40, 5, 16, true, false, false, Materials.Oilsands, Materials.Oilsands, Materials.Oilsands, Materials.Oilsands);
        
        int i = 0;
        for (int j = GregTech_API.sWorldgenFile.get("worldgen", "AmountOfCustomSmallOreSlots", 16); i < j; i++) {
        	new GT_Worldgen_GT_Ore_SmallPieces("ore.small.custom." + (i < 10 ? "0" : "") + i, false, 0, 0, 0, false, false, false, Materials._NULL);
        }
        
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
        
        /*if (GregTech_API.mImmersiveEngineering && GT_Mod.gregtechproxy.mImmersiveEngineeringRecipes) {
            blusunrize.immersiveengineering.api.tool.ExcavatorHandler.recalculateChances(true);
        }*/
    }
}
