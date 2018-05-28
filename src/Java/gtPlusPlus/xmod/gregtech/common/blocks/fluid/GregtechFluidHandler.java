package gtPlusPlus.xmod.gregtech.common.blocks.fluid;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;

public class GregtechFluidHandler {

	protected static int cellID = 0;

	public static void run(){
		start();
	}

	private static void start(){

		/*    Meta_GT_Proxy.addFluid("lubricant", "Lubricant", Materials.Lubricant, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Lubricant, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("creosote", "Creosote Oil", Materials.Creosote, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Creosote, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("seedoil", "Seed Oil", Materials.SeedOil, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SeedOil, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("fishoil", "Fish Oil", Materials.FishOil, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.FishOil, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("oil", "Oil", Materials.Oil, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oil, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("fuel", "Diesel", Materials.Fuel, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("for.honey", "Honey", Materials.Honey, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Honey, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("biomass", "Biomass", Materials.Biomass, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Biomass, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("bioethanol", "Bio Ethanol", Materials.Ethanol, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Ethanol, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("sulfuricacid", "Sulfuric Acid", Materials.SulfuricAcid, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricAcid, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("milk", "Milk", Materials.Milk, 1, 290, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Milk, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("mcguffium", "Mc Guffium 239", Materials.McGuffium239, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.McGuffium239, 1L), ItemUtils.getEmptyCell(), 1000);
        Meta_GT_Proxy.addFluid("glue", "Glue", Materials.Glue, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Glue, 1L), ItemUtils.getEmptyCell(), 1000);
		 */

		if (!LoadedMods.ThermalFoundation){

			Logger.INFO("Adding in our own GT versions of Thermal Foundation Fluids");
			FluidUtils.addFluid("cryotheum", "Gelid Cryotheum", GT_Materials.Cryotheum, 4, -1200, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.Cryotheum, 1L), ItemUtils.getEmptyCell(), 1000);
			FluidUtils.addFluid("pyrotheum", "Blazing Pyrotheum", GT_Materials.Pyrotheum, 4, 4000, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.Pyrotheum, 1L), ItemUtils.getEmptyCell(), 1000);
			FluidUtils.addFluid("ender", "Resonant Ender", GT_Materials.Ender, 4, 4000, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.Ender, 1L), ItemUtils.getEmptyCell(), 1000);
			}

		if (LoadedMods.IndustrialCraft2){
			Logger.INFO("Adding in GT Fluids for various nuclear related content.");

			FluidUtils.addFluid("hydrofluoricAcid", "Industrial Strength Hydrofluoric Acid", GT_Materials.HydrofluoricAcid, 1, 120, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.HydrofluoricAcid, 1L), ItemUtils.getEmptyCell(), 1000);
			generateIC2FluidCell("HydrofluoricAcid");
			
			FluidUtils.generateFluidNoPrefix("SulfurDioxide", "High Quality Sulfur Dioxide", 263, GT_Materials.SulfurDioxide.mRGBa);
						
			FluidUtils.addFluid("sulfurousAcid", "Sulfurous Acid", GT_Materials.SulfurousAcid, 4, 75, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfurousAcid, 1L), ItemUtils.getEmptyCell(), 1000);
			generateIC2FluidCell("SulfurousAcid");

			FluidUtils.addFluid("sulfuricApatite", "Sulfuric Apatite Mix", GT_Materials.SulfuricApatite, 4, 500, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfuricApatite, 1L), ItemUtils.getEmptyCell(), 1000);
			generateIC2FluidCell("SulfuricApatite");


			//Check for IHL Hydrogen Chloride
			if (!LoadedMods.IHL || (ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenChloride", 1) == null)){
				if (FluidUtils.getFluidStack("hydrogenchloride", 1) == null){
					if (LoadedMods.IHL){
						Logger.INFO("IHL Loaded but hydrogen chloride could not be found for some reason. How about we add our own.");
					}
					else {
						Logger.INFO("No Suitable versions of Hydrogen Chloride available, adding our own.");
					}
					FluidUtils.addFluid("hydrogenChloride", "Industrial Strength Hydrogen Chloride", GT_Materials.HydrogenChloride, 4, 75, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.HydrogenChloride, 1L), ItemUtils.getEmptyCell(), 1000);
					generateIC2FluidCell("HydrogenChloride");
				}
			}


			FluidUtils.addFluid("sulfuricLithium", "Sulfuric Lithium Mix", GT_Materials.SulfuricLithium, 4, 280, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfuricLithium, 1L), ItemUtils.getEmptyCell(), 1000);
			generateIC2FluidCell("SulfuricLithium");

			FluidUtils.addFluid("lithiumHydroxide", "Lithium Hydroxide", GT_Materials.LithiumHydroxide, 4, 500, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.LithiumHydroxide, 1L), ItemUtils.getEmptyCell(), 1000);
			generateIC2FluidCell("LithiumHydroxide");


		}
	}

	private static ItemStack generateIC2FluidCell(final String fluidNameWithCaps){
		Logger.INFO("Adding a Cell for "+fluidNameWithCaps);
		if (LoadedMods.IndustrialCraft2 && !LoadedMods.IndustrialCraft2Classic){
			return Utils.createInternalNameAndFluidCell(fluidNameWithCaps);
		}
		return null;
	}
	
	private static ItemStack generateIC2FluidCellNoOreDict(final String fluidNameWithCaps){
		Logger.INFO("Adding a Cell for "+fluidNameWithCaps);
		if (LoadedMods.IndustrialCraft2 && !LoadedMods.IndustrialCraft2Classic){
			return Utils.createInternalNameAndFluidCellNoOreDict(fluidNameWithCaps);
		}
		return null;
	}

}
