package gtPlusPlus.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import gregtech.api.util.CustomRecipeMap;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;

public class NEI_GT_Config
implements IConfigureNEI {
	
	public static boolean sIsAdded = false;
	
	private static final AutoMap<String> mUniqueRecipeMapHandling = new AutoMap<String>();
	
	@Override
	public synchronized void loadConfig() {

		mUniqueRecipeMapHandling.add(CustomRecipeMap.sFissionFuelProcessing.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sChemicalPlantRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sOreMillRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sFlotationCellRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sChemicalDehydratorRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sVacuumFurnaceRecipes.mUnlocalizedName);		

		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sAdvFreezerRecipes_GT.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT.mUnlocalizedName);
		
		// Custom Recipe Maps
		Logger.INFO("NEI Registration: "+CustomRecipeMap.sMappings.size()+" CustomRecipeMaps");
		for (final CustomRecipeMap tMap : CustomRecipeMap.sMappings) {
			if (tMap.mNEIAllowed) {
				if (!mUniqueRecipeMapHandling.contains(tMap.mUnlocalizedName)) {
					Logger.INFO("NEI Registration: Registering NEI handler for "+tMap.mNEIName);
					new GTPP_NEI_CustomMapHandler(tMap);
				}	
				else {
					Logger.INFO("NEI Registration: Not allowed to register NEI handler for "+tMap.mNEIName);					
				}
			}
			else {
				Logger.INFO("NEI Registration: Not allowed to register NEI handler for "+tMap.mNEIName);				
			}
		}
		
		// Custom Recipe maps
		/*Logger.INFO("NEI Registration: "+GTPP_Recipe.GTPP_Recipe_Map.sMappings.size()+" sMappings");
		for (final GTPP_Recipe_Map tMap : GTPP_Recipe.GTPP_Recipe_Map.sMappings) {
			if (tMap.mNEIAllowed) {				
				if (!mUniqueRecipeMapHandling.contains(tMap.mUnlocalizedName)) {
					Logger.INFO("NEI Registration: Registering NEI handler for "+tMap.mNEIName);
					//new GTPP_NEI_DefaultHandler_Fix(tMap);					
				}	
				else {
					Logger.INFO("NEI Registration: Not allowed to register NEI handler for "+tMap.mNEIName);					
				}			
			}
			else {
				Logger.INFO("NEI Registration: Skipping registration of NEI handler for "+tMap.mNEIName);
				
			}
		}*/
		
		// Standard GT Recipe Maps
		Logger.INFO("NEI Registration: "+GTPP_Recipe.GTPP_Recipe_Map_Internal.sMappingsEx.size()+" sMappingEx");
		for (final GT_Recipe_Map tMap : GTPP_Recipe.GTPP_Recipe_Map_Internal.sMappingsEx) {
			if (tMap.mNEIAllowed) {				
				if (!mUniqueRecipeMapHandling.contains(tMap.mUnlocalizedName)) {
					Logger.INFO("NEI Registration: Registering NEI handler for "+tMap.mNEIName);
					new GT_NEI_DefaultHandler(tMap);					
				}	
				else {
					Logger.INFO("NEI Registration: Not allowed to register NEI handler for "+tMap.mNEIName);					
				}			
			}
			else {
				Logger.INFO("NEI Registration: Skipping registration of NEI handler for "+tMap.mNEIName);
			}
		}
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe.GTPP_Recipe_Map.sChemicalPlantRecipes.mNEIName);
		new GT_NEI_FluidReactor();
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe.GTPP_Recipe_Map.sOreMillRecipes.mNEIName);
		new GT_NEI_MillingMachine();
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe.GTPP_Recipe_Map.sFlotationCellRecipes.mNEIName);
		new GT_NEI_FlotationCell();
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe.GTPP_Recipe_Map.sVacuumFurnaceRecipes.mNEIName);
		new GT_NEI_VacFurnace();
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.mNEIName);
		new GT_NEI_Dehydrator();
		Logger.INFO("NEI Registration: Registering NEI handler for "+CustomRecipeMap.sFissionFuelProcessing.mNEIName);
		new GT_NEI_RFPP();

		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe.GTPP_Recipe_Map.sAdvFreezerRecipes_GT.mNEIName);
		new GT_NEI_DefaultHandler(GTPP_Recipe.GTPP_Recipe_Map.sAdvFreezerRecipes_GT);					
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.mNEIName);
		new GT_NEI_DefaultHandler(GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT);					
		Logger.INFO("NEI Registration: Registering NEI handler for "+GTPP_Recipe.GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT.mNEIName);
		new GT_NEI_DefaultHandler(GTPP_Recipe.GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT);					
		
		Logger.INFO("NEI Registration: Registering NEI handler for "+DecayableRecipeHandler.mNEIName);
		API.registerRecipeHandler(new DecayableRecipeHandler());
		API.registerUsageHandler(new DecayableRecipeHandler());
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
