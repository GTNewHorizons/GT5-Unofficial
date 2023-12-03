package gtPlusPlus.nei;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.event.NEIRegisterHandlerInfosEvent;
import codechicken.nei.recipe.HandlerInfo;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeMaps;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class NEI_GTPP_Config implements IConfigureNEI {

    public static boolean sIsAdded = true;

    @Override
    public synchronized void loadConfig() {
        sIsAdded = false;

        Logger.INFO("NEI Registration: Registering NEI handler for " + DecayableRecipeHandler.mNEIName);
        API.registerRecipeHandler(new DecayableRecipeHandler());
        API.registerUsageHandler(new DecayableRecipeHandler());
        API.addRecipeCatalyst(new ItemStack(ModBlocks.blockDecayablesChest, 1), "GTPP_Decayables");

        Logger.INFO("NEI Registration: Registering NEI handler for " + GT_NEI_LFTR_Sparging.mNEIName);
        new GT_NEI_LFTR_Sparging();
        API.addRecipeCatalyst(GregtechItemList.Controller_Sparge_Tower.get(1), "gtpp.recipe.lftr.sparging");

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
        API.removeRecipeCatalyst(ItemUtils.getItemStackFromFQRN("gregtech:gt.blockmachines:31081", 1), "crafting");
        API.removeRecipeCatalyst(ItemUtils.getItemStackFromFQRN("gregtech:gt.blockmachines:31082", 1), "crafting");

        // Moved to its own handler
        API.removeRecipeCatalyst(
                GregtechItemList.Controller_Vacuum_Furnace.get(1),
                GTPPRecipeMaps.chemicalDehydratorRecipes.unlocalizedName);

        // ULV simple washer
        API.removeRecipeCatalyst(
                ItemUtils.getItemStackFromFQRN("gregtech:gt.blockmachines:767", 1),
                GTPPRecipeMaps.simpleWasherRecipes.unlocalizedName);

        // ULV combustion generator
        API.removeRecipeCatalyst(
                ItemUtils.getItemStackFromFQRN("gregtech:gt.blockmachines:960", 1),
                RecipeMaps.dieselFuels.unlocalizedName);

        // ULV gas turbine
        API.removeRecipeCatalyst(
                ItemUtils.getItemStackFromFQRN("gregtech:gt.blockmachines:961", 1),
                RecipeMaps.gasTurbineFuels.unlocalizedName);

        // Hide Flasks
        if (Utils.isClient()) {
            API.addItemListEntry(GregtechItemList.VOLUMETRIC_FLASK_8k.get(1));
            API.addItemListEntry(GregtechItemList.VOLUMETRIC_FLASK_32k.get(1));
        }
        sIsAdded = true;
    }

    @SubscribeEvent
    public void registerHandlerInfo(NEIRegisterHandlerInfosEvent event) {
        event.registerHandlerInfo(
                new HandlerInfo.Builder("gtpp.recipe.lftr.sparging", CORE.name, Mods.GTPlusPlus.ID)
                        .setDisplayStack(GregtechItemList.Controller_Sparge_Tower.get(1)).setShiftY(6).setHeight(135)
                        .setMaxRecipesPerPage(1).build());
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
