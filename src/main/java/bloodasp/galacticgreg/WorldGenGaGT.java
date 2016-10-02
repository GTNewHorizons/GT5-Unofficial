package bloodasp.galacticgreg;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;

public class WorldGenGaGT implements Runnable {

	@Override
	public void run() {
		new GT_Worldgenerator_Space();

		// Register all well-known generators here
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.copper", true, 60, 120, 32, Materials.Copper);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.tin", true, 60, 120, 32, Materials.Tin);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.bismuth", true, 80, 120, 8, Materials.Bismuth);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.coal", true, 60, 100, 24, Materials.Coal);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.iron", true, 40, 80, 16, Materials.Iron);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.lead", true, 40, 80, 16, Materials.Lead);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.zinc", true, 30, 60, 12, Materials.Zinc);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.gold", true, 20, 40, 8, Materials.Gold);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.silver", true, 20, 40, 8, Materials.Silver);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.nickel", true, 20, 40, 8, Materials.Nickel);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.lapis", true, 20, 40, 4, Materials.Lapis);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.diamond", true, 5, 10, 2, Materials.Diamond);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.redstone", true, 5, 20, 8, Materials.Redstone);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.platinum", true, 20, 40, 8, Materials.Platinum);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.iridium", true, 20, 40, 8, Materials.Iridium);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.netherquartz", true, 30, 120, 64, Materials.NetherQuartz);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.saltpeter", true, 10, 60, 8, Materials.Saltpeter);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.sulfur_n", true, 10, 60, 32, Materials.Sulfur);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.sulfur_o", true, 5, 15, 8, Materials.Sulfur);

		// Same for gems
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.emerald", true, 5, 250, 1, Materials.Emerald);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.ruby", true, 5, 250, 1, Materials.Ruby);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.sapphire", true, 5, 250, 1, Materials.Sapphire);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.greensapphire", true, 5, 250, 1, Materials.GreenSapphire);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.olivine", true, 5, 250, 1, Materials.Olivine);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.topaz", true, 5, 250, 1, Materials.Topaz);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.tanzanite",	true, 5, 250, 1, Materials.Tanzanite);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.amethyst", true, 5, 250, 1, Materials.Amethyst);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.opal", true, 5, 250, 1, Materials.Opal);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.jasper", true, 5, 250, 1, Materials.Jasper);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.bluetopaz", true, 5, 250, 1, Materials.BlueTopaz);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.amber", true, 5, 250, 1, Materials.Amber);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.foolsruby", true, 5, 250, 1, Materials.FoolsRuby);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.garnetred", true, 5, 250, 1, Materials.GarnetRed);
		new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.garnetyellow", true, 5, 250, 1, Materials.GarnetYellow);

		// Parse all custom small ores
		int f = 0;
		for (int j = GregTech_API.sWorldgenFile.get("worldgen", "AmountOfCustomSmallOreSlots", 16); f < j; f++)
			new GT_Worldgen_GT_Ore_SmallPieces_Space("ore.small.custom." + (f < 10 ? "0" : "") + f, false, 0, 0, 0, Materials._NULL);

		
		// Register all well-known generators here, this time oreVeins
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.naquadah", false, 10, 60, 10, 5, 32, Materials.Naquadah, Materials.Naquadah, Materials.Naquadah, Materials.NaquadahEnriched);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.lignite", true, 50, 130, 160, 8, 32, Materials.Lignite, Materials.Lignite, Materials.Lignite, Materials.Coal);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.coal", true, 50, 80, 80, 6, 32, Materials.Coal, Materials.Coal, Materials.Coal, Materials.Lignite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.magnetite", true, 50, 120, 160, 3, 32, Materials.Magnetite, Materials.Magnetite, Materials.Iron, Materials.VanadiumMagnetite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.gold", true, 60, 80, 160, 3, 32, Materials.Magnetite, Materials.Magnetite, Materials.VanadiumMagnetite, Materials.Gold);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.iron", true, 10, 40, 120, 4, 24, Materials.BrownLimonite, Materials.YellowLimonite, Materials.BandedIron, Materials.Malachite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.cassiterite", true, 40, 120, 50, 5, 24, Materials.Tin, Materials.Tin, Materials.Cassiterite, Materials.Tin);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.tetrahedrite", true, 80, 120, 70, 4, 24, Materials.Tetrahedrite, Materials.Tetrahedrite, Materials.Copper, Materials.Stibnite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.netherquartz", true, 40, 80, 80, 5, 24, Materials.NetherQuartz, Materials.NetherQuartz, Materials.NetherQuartz, Materials.NetherQuartz);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.sulfur", true, 5, 20, 100, 5, 24, Materials.Sulfur, Materials.Sulfur, Materials.Pyrite, Materials.Sphalerite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.copper", true, 10, 30, 80, 4, 24, Materials.Chalcopyrite, Materials.Iron, Materials.Pyrite, Materials.Copper);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.bauxite", true, 50, 90, 80, 4, 24, Materials.Bauxite, Materials.Bauxite, Materials.Aluminium, Materials.Ilmenite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.salts", true, 50, 60, 50, 3, 24,  Materials.RockSalt, Materials.Salt, Materials.Lepidolite, Materials.Spodumene);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.redstone", true, 10, 40, 60, 3, 24, Materials.Redstone, Materials.Redstone, Materials.Ruby, Materials.Cinnabar);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.soapstone", true, 10, 40, 40, 3, 16, Materials.Soapstone, Materials.Talc, Materials.Glauconite, Materials.Pentlandite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.nickel", true, 10, 40, 40, 3, 16, Materials.Garnierite, Materials.Nickel, Materials.Cobaltite, Materials.Pentlandite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.platinum", true, 40, 50, 5, 3, 16, Materials.Cooperite, Materials.Palladium, Materials.Platinum, Materials.Iridium);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.pitchblende", true, 10, 40, 40, 3, 16, Materials.Pitchblende, Materials.Pitchblende, Materials.Uranium, Materials.Uraninite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.plutonium", true, 20, 30, 10, 3, 16, Materials.Uraninite, Materials.Uraninite, Materials.Plutonium, Materials.Uranium);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.monazite", true, 20, 40, 30, 3, 16, Materials.Bastnasite, Materials.Bastnasite, Materials.Monazite, Materials.Neodymium);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.molybdenum", true, 20, 50, 5, 3, 16, Materials.Wulfenite, Materials.Molybdenite, Materials.Molybdenum, Materials.Powellite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.tungstate", true, 20, 50, 10, 3, 16, Materials.Scheelite, Materials.Scheelite, Materials.Tungstate, Materials.Lithium);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.sapphire", true, 10, 40, 60, 3, 16, Materials.Almandine, Materials.Pyrope, Materials.Sapphire, Materials.GreenSapphire);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.manganese", true, 20, 30, 20, 3, 16, Materials.Grossular, Materials.Spessartine, Materials.Pyrolusite, Materials.Tantalite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.quartz", true, 40, 80, 60, 3, 16, Materials.Quartzite, Materials.Barite, Materials.CertusQuartz, Materials.CertusQuartz);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.diamond", true, 5, 20, 40, 2, 16, Materials.Graphite, Materials.Graphite, Materials.Diamond, Materials.Coal);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.olivine", true, 10, 40, 60, 3, 16, Materials.Bentonite, Materials.Magnesite, Materials.Olivine, Materials.Glauconite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.apatite", true, 40, 60, 60, 3, 16, Materials.Apatite, Materials.Apatite, Materials.Phosphorus, Materials.Phosphate);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.galena", true, 30, 60, 40, 5, 16, Materials.Galena, Materials.Galena, Materials.Silver, Materials.Lead);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.lapis", true, 20, 50, 40, 5, 16, Materials.Lazurite, Materials.Sodalite, Materials.Lapis, Materials.Calcite);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.beryllium", true, 5, 30, 30, 3, 16, Materials.Beryllium, Materials.Beryllium, Materials.Emerald, Materials.Thorium);
		new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.oilsand", true, 50, 80, 80, 6, 32, Materials.Oilsands, Materials.Oilsands, Materials.Oilsands, Materials.Oilsands);


		// Parse all custom ore veins
		int i = 0;
		for (int j = GregTech_API.sWorldgenFile.get("worldgen", "AmountOfCustomLargeVeinSlots", 16); i < j; i++) {
			new GT_Worldgen_GT_Ore_Layer_Space("ore.mix.custom." + (i < 10 ? "0" : "") + i, false, 0, 0, 0, 0, 0, Materials._NULL, Materials._NULL, Materials._NULL, Materials._NULL);
		}
	}
}
