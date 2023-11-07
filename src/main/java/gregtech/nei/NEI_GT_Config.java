package gregtech.nei;

import java.util.Comparator;
import java.util.function.UnaryOperator;

import com.google.common.collect.ImmutableMap;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.event.NEIRegisterHandlerInfosEvent;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.HandlerInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.recipe.NEIRecipeProperties;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;
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
    private static final ImmutableMap<RecipeMap<?>, Integer> RECIPE_MAP_ORDERING = ImmutableMap
        .<RecipeMap<?>, Integer>builder()
        .put(RecipeMaps.assemblylineVisualRecipes, 1)
        .put(RecipeMaps.scannerFakeRecipes, 2)
        .build();

    private static final Comparator<GT_NEI_DefaultHandler> RECIPE_MAP_HANDLER_COMPARATOR = Comparator
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
        registerHandlers();
        registerCatalysts();
        registerItemEntries();
        registerDumpers();
        sIsAdded = true;
    }

    private void registerHandlers() {
        RecipeMap.ALL_RECIPE_MAPS.values()
            .stream()
            .filter(
                recipeMap -> recipeMap.getFrontend()
                    .getNEIProperties().registerNEI)
            .map(GT_NEI_DefaultHandler::new)
            .sorted(RECIPE_MAP_HANDLER_COMPARATOR)
            .forEach(NEI_GT_Config::addHandler);
    }

    private void registerCatalysts() {
        for (int i = 1; i < GregTech_API.METATILEENTITIES.length; i++) {
            IMetaTileEntity mte = GregTech_API.METATILEENTITIES[i];
            if (!(mte instanceof RecipeMapWorkable recipeMapWorkable)) continue;
            for (RecipeMap<?> recipeMap : recipeMapWorkable.getAvailableRecipeMaps()) {
                API.addRecipeCatalyst(
                    mte.getStackForm(1),
                    recipeMap.unlocalizedName,
                    recipeMapWorkable.getRecipeCatalystPriority());
            }
        }
        API.addRecipeCatalyst(
            GT_ModHandler.getIC2Item("nuclearReactor", 1, null),
            RecipeMaps.ic2NuclearFakeRecipes.unlocalizedName);
    }

    private void registerItemEntries() {
        API.addItemListEntry(ItemList.VOLUMETRIC_FLASK.get(1));
    }

    private void registerDumpers() {
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

    @SubscribeEvent
    public void registerNEIHandlerInfo(NEIRegisterHandlerInfosEvent event) {
        RecipeMap.ALL_RECIPE_MAPS.values()
            .forEach(recipeMap -> {
                NEIRecipeProperties neiProperties = recipeMap.getFrontend()
                    .getNEIProperties();
                UnaryOperator<HandlerInfo.Builder> handlerInfoCreator = neiProperties.handlerInfoCreator;
                if (handlerInfoCreator != null) {
                    event.registerHandlerInfo(
                        handlerInfoCreator
                            .apply(createHandlerInfoBuilderTemplate(recipeMap.unlocalizedName, neiProperties.ownerMod))
                            .build());
                }
            });
    }

    private HandlerInfo.Builder createHandlerInfoBuilderTemplate(String unlocalizedName, ModContainer ownerMod) {
        return new HandlerInfo.Builder(unlocalizedName, ownerMod.getName(), ownerMod.getModId()).setShiftY(6)
            .setHeight(135)
            .setMaxRecipesPerPage(2);
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
