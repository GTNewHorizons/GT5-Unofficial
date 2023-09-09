package gregtech.nei;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Recipe;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import gregtech.common.items.GT_MetaGenerated_Item_02;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import gregtech.nei.dumper.BatchModeSupportDumper;
import gregtech.nei.dumper.InputSeparationSupportDumper;
import gregtech.nei.dumper.MaterialDumper;
import gregtech.nei.dumper.MetaItemDumper;
import gregtech.nei.dumper.MetaTileEntityDumper;
import gregtech.nei.dumper.RecipeLockingSupportDumper;
import gregtech.nei.dumper.VoidProtectionSupportDumper;

public class NEI_GT_Config implements IConfigureNEI {

    /**
     * This map determines the order in which NEI handlers will be registered and displayed in tabs.
     *
     * <p>
     * Handlers will be displayed in ascending order of integer value. Any recipe map that is not present in this map
     * will be assigned a value of 0. Negative values are fine.
     */
    private static final ImmutableMap<GT_Recipe.GT_Recipe_Map, Integer> RECIPE_MAP_ORDERING = ImmutableMap.<GT_Recipe.GT_Recipe_Map, Integer>builder()
        .put(GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes, 1)
        .put(GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes, 2)
        .build();

    private static final Comparator<RecipeMapHandler> RECIPE_MAP_HANDLER_COMPARATOR = Comparator
        .comparingInt(handler -> RECIPE_MAP_ORDERING.getOrDefault(handler.getRecipeMap(), 0));

    public static boolean sIsAdded = true;

    private static void addHandler(TemplateRecipeHandler handler) {
        FMLInterModComms.sendRuntimeMessage(
            GT_Values.GT,
            "NEIPlugins",
            "register-crafting-handler",
            "gregtech@" + handler.getRecipeName() + "@" + handler.getOverlayIdentifier());
        GuiCraftingRecipe.craftinghandlers.add(handler);
        GuiUsageRecipe.usagehandlers.add(handler);
    }

    @Override
    public void loadConfig() {
        sIsAdded = false;
        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient()) {
            List<RecipeMapHandler> handlers = new ArrayList<>();

            for (GT_Recipe.GT_Recipe_Map tMap : GT_Recipe.GT_Recipe_Map.sMappings) {
                if (tMap.mNEIAllowed) {
                    handlers.add(new GT_NEI_DefaultHandler(tMap));
                }
            }

            handlers.sort(RECIPE_MAP_HANDLER_COMPARATOR);
            handlers.forEach(NEI_GT_Config::addHandler);

            API.addItemListEntry(ItemList.VOLUMETRIC_FLASK.get(1));

            API.addOption(new MetaTileEntityDumper());
            API.addOption(new MaterialDumper());
            API.addOption(new MetaItemDumper(GT_MetaGenerated_Item_01.INSTANCE, "metaitem01"));
            API.addOption(new MetaItemDumper(GT_MetaGenerated_Item_02.INSTANCE, "metaitem02"));
            API.addOption(new MetaItemDumper(GT_MetaGenerated_Item_03.INSTANCE, "metaitem03"));
            API.addOption(new VoidProtectionSupportDumper());
            API.addOption(new InputSeparationSupportDumper());
            API.addOption(new BatchModeSupportDumper());
            API.addOption(new RecipeLockingSupportDumper());
        }
        sIsAdded = true;
    }

    @Override
    public String getName() {
        return "GregTech NEI Plugin";
    }

    @Override
    public String getVersion() {
        return "(5.03a)";
    }
}
