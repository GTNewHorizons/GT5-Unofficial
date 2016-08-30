package miscutil.xmod.gregtech.common.blocks.fluid;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.Utils;
import miscutil.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import miscutil.xmod.gregtech.common.Meta_GT_Proxy;

public class GregtechFluidHandler {

	public static void run(){
		start();
	}
	
	private static void start(){
		
    /*    Meta_GT_Proxy.addFluid("lubricant", "Lubricant", Materials.Lubricant, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Lubricant, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("creosote", "Creosote Oil", Materials.Creosote, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Creosote, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("seedoil", "Seed Oil", Materials.SeedOil, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SeedOil, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("fishoil", "Fish Oil", Materials.FishOil, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.FishOil, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("oil", "Oil", Materials.Oil, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oil, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("fuel", "Diesel", Materials.Fuel, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("for.honey", "Honey", Materials.Honey, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Honey, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("biomass", "Biomass", Materials.Biomass, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Biomass, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("bioethanol", "Bio Ethanol", Materials.Ethanol, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Ethanol, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("sulfuricacid", "Sulfuric Acid", Materials.SulfuricAcid, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricAcid, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("milk", "Milk", Materials.Milk, 1, 290, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Milk, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("mcguffium", "Mc Guffium 239", Materials.McGuffium239, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.McGuffium239, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        Meta_GT_Proxy.addFluid("glue", "Glue", Materials.Glue, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Glue, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
        */
		
		if (!LoadedMods.ThermalFoundation){

		Utils.LOG_INFO("Adding in our own GT versions of Thermal Foundation Fluids");		
		Meta_GT_Proxy.addFluid("cryotheum", "Gelid Cryotheum", GT_Materials.Cryotheum, 4, -1200, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.Cryotheum, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
		Meta_GT_Proxy.addFluid("pyrotheum", "Blazing Pyrotheum", GT_Materials.Pyrotheum, 4, 4000, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.Pyrotheum, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
		}
		
		if (LoadedMods.IndustrialCraft2){
			Utils.LOG_INFO("Adding in GT Fluids for Molten Salt, Cooled Salt and Various other Nuclide related content.");		
			Meta_GT_Proxy.addFluid("uraniumHexafluoride", "Uranium Hexafluoride", GT_Materials.UraniumHexaFluoride, 2, 100, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.UraniumHexaFluoride, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			Meta_GT_Proxy.addFluid("UraniumTetrafluoride", "Uranium Tetrafluoride", GT_Materials.UraniumTetraFluoride, 2, 950, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.UraniumTetraFluoride, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			}
	}
	
}
