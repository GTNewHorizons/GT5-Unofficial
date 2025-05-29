package gtPlusPlus.nei;

import java.util.Arrays;

import gregtech.api.enums.Mods;
import net.minecraft.item.ItemStack;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import gregtech.api.recipe.RecipeMaps;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

import static gregtech.api.util.GTModHandler.getModItem;

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
        // Bronze workbench
        API.removeRecipeCatalyst(getModItem(Mods.GregTech.ID, "gt.blockmachines", 1, 31081), "crafting");
        API.removeRecipeCatalyst(getModItem(Mods.GregTech.ID, "gt.blockmachines", 1, 31082), "crafting");

        // Moved to its own handler
        API.removeRecipeCatalyst(
            GregtechItemList.Controller_Vacuum_Furnace.get(1),
            GTPPRecipeMaps.chemicalDehydratorRecipes.unlocalizedName);

        // ULV simple washer
        API.removeRecipeCatalyst(
            getModItem(Mods.GregTech.ID, "gt.blockmachines", 1, 767),
            GTPPRecipeMaps.simpleWasherRecipes.unlocalizedName);

        // ULV combustion generator
        API.removeRecipeCatalyst(
            getModItem(Mods.GregTech.ID, "gt.blockmachines", 1, 960),
            RecipeMaps.dieselFuels.unlocalizedName);

        // ULV gas turbine
        API.removeRecipeCatalyst(
            getModItem(Mods.GregTech.ID, "gt.blockmachines", 1, 961),
            RecipeMaps.gasTurbineFuels.unlocalizedName);

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
