package gtPlusPlus.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class NEI_GT_Config implements IConfigureNEI {

    public static boolean sIsAdded = true;

    @Override
    public synchronized void loadConfig() {
        sIsAdded = false;

        Logger.INFO("NEI Registration: Registering NEI handler for " + GTPP_Recipe_Map.sChemicalPlantRecipes.mNEIName);
        new GT_NEI_FluidReactor();
        Logger.INFO("NEI Registration: Registering NEI handler for "
                + GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.mNEIName);
        new GT_NEI_LFTR();
        Logger.INFO("NEI Registration: Registering NEI handler for " + GTPP_Recipe_Map.sTreeSimFakeRecipes.mNEIName);
        new GT_NEI_MultiTreeGrowthSimulator();
        Logger.INFO("NEI Registration: Registering NEI handler for " + GTPP_Recipe_Map.sOreMillRecipes.mNEIName);
        new GTPP_NEI_DefaultHandler(GTPP_Recipe_Map.sOreMillRecipes);
        Logger.INFO("NEI Registration: Registering NEI handler for " + GTPP_Recipe_Map.sSolarTowerRecipes.mNEIName);
        new GT_NEI_MultiSolarTower(GTPP_Recipe_Map.sSolarTowerRecipes);

        Logger.INFO("NEI Registration: Registering NEI handler for " + DecayableRecipeHandler.mNEIName);
        API.registerRecipeHandler(new DecayableRecipeHandler());
        API.registerUsageHandler(new DecayableRecipeHandler());

        Logger.INFO("NEI Registration: Registering NEI handler for " + GT_NEI_LFTR_Sparging.mNEIName);
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
