package gtPlusPlus.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map_Internal;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class NEI_GT_Config
implements IConfigureNEI {
	
	public static boolean sIsAdded = false;
	
	private static final AutoMap<String> mUniqueRecipeMapHandling = new AutoMap<String>();
	
	@Override
	public synchronized void loadConfig() {

		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sFissionFuelProcessing.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sChemicalPlantRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sOreMillRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sFlotationCellRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sVacuumFurnaceRecipes.mUnlocalizedName);		

		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sAdvFreezerRecipes_GT.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sMultiblockMixerRecipes_GT.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sAlloyBlastSmelterRecipes.mUnlocalizedName);
		
		// Standard GT Recipe Maps
		Logger.INFO("NEI Registration: "+GTPP_Recipe_Map_Internal.sMappingsEx.size()+" sMappingEx");
		for (final GT_Recipe_Map tMap : GTPP_Recipe_Map_Internal.sMappingsEx) {
			if (tMap.mNEIAllowed) {				
				if (!mUniqueRecipeMapHandling.contains(tMap.mUnlocalizedName)) {
					Logger.INFO("NEI Registration: Registering NEI handler for "+tMap.mNEIName);
					new GTPP_NEI_DefaultHandler(tMap);					
				}	
				else {
					Logger.INFO("NEI Registration: Not allowed to register NEI handler for "+tMap.mNEIName);					
				}			
			}
			else {
				Logger.INFO("NEI Registration: Skipping registration of NEI handler for "+tMap.mNEIName);
			}
		}
		
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sChemicalPlantRecipes.mNEIName);
		new GT_NEI_FluidReactor();
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sOreMillRecipes.mNEIName);
		new GT_NEI_MillingMachine();
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sFlotationCellRecipes.mNEIName);
		new GT_NEI_FlotationCell();
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.mNEIName);
		new GT_NEI_LFTR();
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sFissionFuelProcessing.mNEIName);
		new GT_NEI_MultiNoCell(GTPP_Recipe_Map.sFissionFuelProcessing);
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sVacuumFurnaceRecipes.mNEIName);
		new GT_NEI_MultiNoCell(GTPP_Recipe_Map.sVacuumFurnaceRecipes);
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sAdvFreezerRecipes_GT.mNEIName);
		new GT_NEI_MultiNoCell(GTPP_Recipe_Map.sAdvFreezerRecipes_GT);					
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.mNEIName);
		new GT_NEI_MultiNoCell(GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT);					
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT.mNEIName);
		new GT_NEI_MultiNoCell(GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT);
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sMultiblockMixerRecipes_GT.mNEIName);
		new GT_NEI_MultiNoCell(GTPP_Recipe_Map.sMultiblockMixerRecipes_GT);
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe_Map.sAlloyBlastSmelterRecipes.mNEIName);
		new GT_NEI_MultiNoCell(GTPP_Recipe_Map.sAlloyBlastSmelterRecipes);

		Logger.INFO("NEI Registration: Registering NEI handler for "+DecayableRecipeHandler.mNEIName);
		API.registerRecipeHandler(new DecayableRecipeHandler());
		API.registerUsageHandler(new DecayableRecipeHandler());
		
		Logger.INFO("NEI Registration: Registering NEI handler for "+GT_NEI_LFTR_Sparging.mNEIName);
		new GT_NEI_LFTR_Sparging();
		
		// Hide Flasks
		if (Utils.isClient()) {
			codechicken.nei.api.API.addItemListEntry(GregtechItemList.VOLUMETRIC_FLASK_8k.get(1));
			codechicken.nei.api.API.addItemListEntry(GregtechItemList.VOLUMETRIC_FLASK_32k.get(1));			
		}
		sIsAdded = true;
	}

	@Override
	public String getName() {
		return "GT++ NEI Plugin";
	}

	@Override
	public String getVersion() {
		return "(1.12)";
	}
}
