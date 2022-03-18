package gregtech.nei;

import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NEI_GT_Config implements IConfigureNEI {
    /**
     * This map determines the order in which NEI handlers will be registered and displayed in tabs.
     *
     * <p>Handlers will be displayed in ascending order of integer value. Any recipe map that is not
     * present in this map will be assigned a value of 0. Negative values are fine.
     */
    private static final ImmutableMap<GT_Recipe.GT_Recipe_Map, Integer> RECIPE_MAP_ORDERING =
            ImmutableMap.<GT_Recipe.GT_Recipe_Map, Integer>builder()
                    .put(GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes, 1)
                    .put(GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes, 2)
                    .build();

    private static final Comparator<RecipeMapHandler> RECIPE_MAP_HANDLER_COMPARATOR =
            Comparator.comparingInt(
                    handler -> RECIPE_MAP_ORDERING.getOrDefault(handler.getRecipeMap(), 0));

    public static boolean sIsAdded = true;
    public static GT_NEI_AssLineHandler ALH;

    private static void addHandler(TemplateRecipeHandler handler) {
        FMLInterModComms.sendRuntimeMessage(
                GT_Values.GT, "NEIPlugins", "register-crafting-handler",
                "gregtech@" + handler.getRecipeName() + "@" + handler.getOverlayIdentifier());
        GuiCraftingRecipe.craftinghandlers.add(handler);
        GuiUsageRecipe.usagehandlers.add(handler);
    }

    @Override
    public void loadConfig() {
        sIsAdded = false;
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            List<RecipeMapHandler> handlers = new ArrayList<>();

            ALH = new GT_NEI_AssLineHandler(GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes);
            handlers.add(ALH);

            for (GT_Recipe.GT_Recipe_Map tMap : GT_Recipe.GT_Recipe_Map.sMappings) {
                if (tMap.mNEIAllowed) {
                    handlers.add(new GT_NEI_DefaultHandler(tMap));
                }
            }

            handlers.sort(RECIPE_MAP_HANDLER_COMPARATOR);
            handlers.forEach(NEI_GT_Config::addHandler);

            codechicken.nei.api.API.addItemListEntry(ItemList.VOLUMETRIC_FLASK.get(1));
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
