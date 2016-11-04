package gtPlusPlus.xmod.gregtech.common.blocks.fluid;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import net.minecraftforge.fluids.FluidStack;

public class GregtechFluidHandler {

	protected static int cellID = 0;

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
			FluidUtils.addFluid("cryotheum", "Gelid Cryotheum", GT_Materials.Cryotheum, 4, -1200, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.Cryotheum, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			FluidUtils.addFluid("pyrotheum", "Blazing Pyrotheum", GT_Materials.Pyrotheum, 4, 4000, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.Pyrotheum, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
		}

		if (LoadedMods.IndustrialCraft2){
			Utils.LOG_INFO("Adding in GT Fluids for various nuclear related content.");	
			
			FluidUtils.addFluid("hydrofluoricAcid", "Hydrofluoric Acid", GT_Materials.HydrofluoricAcid, 1, 120, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.HydrofluoricAcid, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("HydrofluoricAcid");

			FluidUtils.addFluid("sulfurDioxide", "Sulfur Dioxide", GT_Materials.SulfurDioxide, 4, -100, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfurDioxide, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("SulfurDioxide");

			FluidUtils.addFluid("sulfurousAcid", "Sulfurous Acid", GT_Materials.SulfurousAcid, 4, 75, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfurousAcid, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("SulfurousAcid");

			FluidUtils.addFluid("sulfuricApatite", "Sulfuric Apatite Mix", GT_Materials.SulfuricApatite, 4, 500, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfuricApatite, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("SulfuricApatite");

			FluidUtils.addFluid("uraniumHexafluoride", "Uranium Hexafluoride", GT_Materials.UraniumHexaFluoride, 4, 200, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.UraniumHexaFluoride, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("UraniumHexaFluoride");

			FluidUtils.addFluid("uraniumTetrafluoride", "Uranium Tetrafluoride", GT_Materials.UraniumTetraFluoride, 4, 950, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.UraniumTetraFluoride, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("UraniumTetraFluoride");

			FluidUtils.addFluid("thoriumTetrafluoride", "Thorium Tetrafluoride", GT_Materials.ThoriumTetraFluoride, 4, 1250, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.ThoriumTetraFluoride, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("ThoriumTetraFluoride");


			//Check for IHL Hydrogen Chloride
			if (!LoadedMods.IHL || ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenChloride", 1) == null){
				if (FluidUtils.getFluidStack("hydrogenchloride", 1) == null){
					if (LoadedMods.IHL){
						Utils.LOG_INFO("IHL Loaded but hydrogen chloride could not be found for some reason. How about we add our own.");
					}
					else {
						Utils.LOG_INFO("No Suitable versions of Hydrogen Chloride available, adding our own.");
					}
					FluidUtils.addFluid("hydrogenChloride", "Hydrogen Chloride", GT_Materials.HydrogenChloride, 4, 75, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.HydrogenChloride, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
					generateIC2FluidCell("HydrogenChloride");
				}
			}
			

			FluidUtils.addFluid("sulfuricLithium", "Sulfuric Lithium Mix", GT_Materials.SulfuricLithium, 4, 280, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfuricLithium, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("SulfuricLithium");

			FluidUtils.addFluid("lithiumHydroxide", "Lithium Hydroxide", GT_Materials.LithiumHydroxide, 4, 500, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.LithiumHydroxide, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("SulfuricApatite");

			/*Meta_GT_Proxy.addFluid("sulfuricApatite", "Sulfuric Apatite", GT_Materials.SulfuricApatite, 4, 500, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfuricApatite, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("SulfuricApatite");

			Meta_GT_Proxy.addFluid("sulfuricApatite", "Sulfuric Apatite", GT_Materials.SulfuricApatite, 4, 500, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfuricApatite, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("SulfuricApatite");

			Meta_GT_Proxy.addFluid("sulfuricApatite", "Sulfuric Apatite", GT_Materials.SulfuricApatite, 4, 500, GT_OreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfuricApatite, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
			generateIC2FluidCell("SulfuricApatite");*/

			
			//Chemical Bath Recipes
			GT_Values.RA.addChemicalRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict("dustApatite", 16),
					null,
					FluidUtils.getFluidStack("sulfuricacid", 144*32), 
					FluidUtils.getFluidStack("sulfuricapatite", 144*4),
					ItemUtils.getItemStackOfAmountFromOreDict("dustSmallSulfur", 1), 
					20*20);
			
			/*GT_Values.RA.addChemicalRecipe(
					UtilsItems.getItemStackOfAmountFromOreDict("dustLithium", 8),
					null,
					FluidUtils.getFluidStack("sulfuricacid", 133*32), 
					FluidUtils.getFluidStack("sulfuriclithium", 133*4),
					UtilsItems.getItemStackOfAmountFromOreDict("dustSmallLithium", 2), 
					20*20);*/
			
			GT_Values.RA.addChemicalRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("dustLithium", 16),
					FluidUtils.getFluidStack("water", 1000), 
					FluidUtils.getFluidStack("lithiumhydroxide", 144*4),
					null, 
					300*20);

			
			
			
			
			
			
			
			//Mixer Recipes
			GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 1), null, null, null, FluidUtils.getFluidStack("oxygen", 288), FluidUtils.getFluidStack("sulfurdioxide", 432), null, 600, 60);
			GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustUranium235", 1), ItemUtils.getItemStackOfAmountFromOreDict("dustUranium235", 1), null, null, FluidUtils.getFluidStack("hydrofluoricacid", 2000), FluidUtils.getFluidStack("uraniumtetrafluoride", 288), null, 3000, 500);
			GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine", 1), ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine", 1), null, null, FluidUtils.getFluidStack("uraniumtetrafluoride", 720), FluidUtils.getFluidStack("uraniumhexafluoride", 288), null, 5000, 2000);

			
			
			
			
			
			
			//Apatite Distillation
			/*
			 * so if you dissolve aparite in sulphuric acid
			 * you'll get a mixture of SO2, H2O, HF and HCl
			 */

			FluidStack[] apatiteOutput = {
					FluidUtils.getFluidStack("sulfurousacid", 3800),
					FluidUtils.getFluidStack("hydrogenchloride", 1000),
					FluidUtils.getFluidStack("hydrofluoricacid", 400)
			};
			GT_Values.RA.addDistillationTowerRecipe(
					FluidUtils.getFluidStack("sulfuricapatite", 5200),
					apatiteOutput,
					null,
					45*20,
					256);

			FluidStack[] sulfurousacidOutput = {
					FluidUtils.getFluidStack("sulfurdioxide", 500),
					FluidUtils.getFluidStack("water", 500)
			};
			GT_Values.RA.addDistillationTowerRecipe(
					FluidUtils.getFluidStack("sulfurousacid", 1000),
					sulfurousacidOutput,
					null,
					10*20,
					60);

			FluidStack[] sulfurdioxideOutput = {
					FluidUtils.getFluidStack("oxygen", 144*2)
			};
			GT_Values.RA.addDistillationTowerRecipe(
					FluidUtils.getFluidStack("sulfurdioxide", 144*3),
					sulfurdioxideOutput,
					ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 1),
					5*20,
					30);

		}
	}

	private static void generateIC2FluidCell(String fluidNameWithCaps){
		Utils.LOG_INFO("Adding a Cell for "+fluidNameWithCaps);
		if (LoadedMods.IndustrialCraft2){
			Utils.createInternalNameAndFluidCell(fluidNameWithCaps);
		}
	}

}
