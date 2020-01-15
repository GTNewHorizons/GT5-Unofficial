package gtPlusPlus.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import gregtech.api.util.CustomRecipeMap;
import gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.GregtechMTE_ChemicalPlant;

public class NEI_GT_Config
implements IConfigureNEI {
	public static boolean sIsAdded = true;

	@Override
	public synchronized void loadConfig() {
		sIsAdded = false;
		for (final CustomRecipeMap tMap : gregtech.api.util.CustomRecipeMap.sMappings) {
			if (tMap.mNEIAllowed) {
				new GT_NEI_DefaultHandler(tMap);
			}
		}
		for (final Gregtech_Recipe_Map tMap : gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map.sMappings) {
			if (tMap.mNEIAllowed) {				
				if (!tMap.mUnlocalizedName.equals(Gregtech_Recipe_Map.sChemicalPlantRecipes.mUnlocalizedName)) {
					new GT_NEI_MultiBlockHandler(tMap);					
				}				
			}
		}
		new GT_NEI_FluidReactor();
		sIsAdded = true;
		API.registerRecipeHandler(new DecayableRecipeHandler());
		API.registerUsageHandler(new DecayableRecipeHandler());
	}

	@Override
	public String getName() {
		return "GT++ NEI Plugin";
	}

	@Override
	public String getVersion() {
		return "(1.01)";
	}
}
