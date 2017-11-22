package gtPlusPlus.xmod.gregtech.common.command.regen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import bloodasp.galacticgreg.GT_Worldgenerator_Space;
import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Materials;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;
import gregtech.common.GT_Worldgen_Stone;
import gregtech.common.GT_Worldgenerator;

public class HANDLER_GT_Commands {

	public static void preInit(){

	}

	public static void init(){

	}

	public static void postInit(){
		final boolean tPFAA = (GregTech_API.sWorldgenFile.get(ConfigCategories.general, "AutoDetectPFAA", true)) && (Loader.isModLoaded("PFAAGeologica"));
		new GT_Worldgenerator();
		if (Loader.isModLoaded("GalacticraftCore") && Loader.isModLoaded("GalacticraftMars")) {
			new GT_Worldgenerator_Space();
		}

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
		for (final int j = GregTech_API.sWorldgenFile.get("worldgen", "AmountOfCustomSmallOreSlots", 16); i < j; i++) {
			new GT_Worldgen_GT_Ore_SmallPieces("ore.small.custom." + (i < 10 ? "0" : "") + i, false, 0, 0, 0, false, false, false, false, false, false, Materials._NULL);
		}
		if (GregTech_API.mImmersiveEngineering && GT_Mod.gregtechproxy.mImmersiveEngineeringRecipes) {
			Class<?> rC;
			try {
				rC = Class.forName("blusunrize.immersiveengineering.api.tool.ExcavatorHandler");
				final Method method = rC.getMethod("recalculateChances", new Class[]{boolean.class});
				method.invoke(null, true);
			}
			catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}
	}


}
