package gtPlusPlus.nei;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import gregtech.api.recipe.RecipeMaps;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class NEIGTPPConfig implements IConfigureNEI {

    public static boolean sIsAdded = true;

    @Override
    public synchronized void loadConfig() {
        sIsAdded = false;

        Logger.INFO("NEI Registration: Registering NEI handler for " + DecayableRecipeHandler.mNEIName);
        API.registerRecipeHandler(new DecayableRecipeHandler());
        API.registerUsageHandler(new DecayableRecipeHandler());
        API.addRecipeCatalyst(new ItemStack(ModBlocks.blockDecayablesChest, 1), "GTPP_Decayables");

        for (GregtechItemList item : Arrays.asList(
            GregtechItemList.GT4_Electric_Auto_Workbench_LV,
            GregtechItemList.GT4_Electric_Auto_Workbench_MV,
            GregtechItemList.GT4_Electric_Auto_Workbench_HV,
            GregtechItemList.GT4_Electric_Auto_Workbench_EV,
            GregtechItemList.GT4_Electric_Auto_Workbench_IV,
            GregtechItemList.GT4_Electric_Auto_Workbench_LuV,
            GregtechItemList.GT4_Electric_Auto_Workbench_ZPM,
            GregtechItemList.GT4_Electric_Auto_Workbench_UV)) {
            API.addRecipeCatalyst(item.get(1), "crafting", -10);
        }

        // Moved to its own handler
        API.removeRecipeCatalyst(
            GregtechItemList.Controller_Vacuum_Furnace.get(1),
            GTPPRecipeMaps.chemicalDehydratorRecipes.unlocalizedName);

        API.removeRecipeCatalyst(
            GregtechItemList.Controller_IndustrialRockBreaker.get(1),
            RecipeMaps.rockBreakerFakeRecipes.unlocalizedName);

        // Hide Flasks
        if (Utils.isClient()) {
            API.addItemListEntry(GregtechItemList.VOLUMETRIC_FLASK_8k.get(1));
            API.addItemListEntry(GregtechItemList.VOLUMETRIC_FLASK_32k.get(1));
            API.addItemListEntry(GregtechItemList.KLEIN_BOTTLE.get(1));
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
