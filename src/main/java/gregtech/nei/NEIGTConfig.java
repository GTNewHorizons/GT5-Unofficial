package gregtech.nei;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.event.NEIRegisterHandlerInfosEvent;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.HandlerInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.common.items.MetaGeneratedItem01;
import gregtech.common.items.MetaGeneratedItem02;
import gregtech.common.items.MetaGeneratedItem03;
import gregtech.nei.dumper.BatchModeSupportDumper;
import gregtech.nei.dumper.InputSeparationSupportDumper;
import gregtech.nei.dumper.MaterialDumper;
import gregtech.nei.dumper.MetaItemDumper;
import gregtech.nei.dumper.MetaTileEntityDumper;
import gregtech.nei.dumper.RecipeLockingSupportDumper;
import gregtech.nei.dumper.VoidProtectionSupportDumper;

public class NEIGTConfig implements IConfigureNEI {

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

    private static final Comparator<GTNEIDefaultHandler> RECIPE_MAP_HANDLER_COMPARATOR = Comparator
        .comparingInt(handler -> RECIPE_MAP_ORDERING.getOrDefault(handler.getRecipeMap(), 0));

    private static ListMultimap<RecipeCategory, RecipeMapWorkable> RECIPE_CATALYST_INDEX;

    public static boolean sIsAdded = true;

    private static void addHandler(TemplateRecipeHandler handler) {
        FMLInterModComms.sendRuntimeMessage(
            GTValues.GT,
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
        RecipeCategory.ALL_RECIPE_CATEGORIES.values()
            .stream()
            .filter(
                recipeCategory -> recipeCategory.recipeMap.getFrontend()
                    .getNEIProperties().registerNEI)
            .map(GTNEIDefaultHandler::new)
            .sorted(RECIPE_MAP_HANDLER_COMPARATOR)
            .forEach(NEIGTConfig::addHandler);
    }

    private void registerCatalysts() {
        for (Map.Entry<RecipeCategory, Collection<RecipeMapWorkable>> entry : RECIPE_CATALYST_INDEX.asMap()
            .entrySet()) {
            entry.getValue()
                .forEach(
                    recipeMapWorkable -> API.addRecipeCatalyst(
                        recipeMapWorkable.getStackForm(1),
                        entry.getKey().unlocalizedName,
                        recipeMapWorkable.getRecipeCatalystPriority()));
        }
        API.addRecipeCatalyst(
            GTModHandler.getIC2Item("nuclearReactor", 1, null),
            RecipeMaps.ic2NuclearFakeRecipes.unlocalizedName);
    }

    private void registerItemEntries() {
        API.addItemListEntry(ItemList.VOLUMETRIC_FLASK.get(1));
    }

    private void registerDumpers() {
        API.addOption(new MetaTileEntityDumper());
        API.addOption(new MaterialDumper());
        API.addOption(new MetaItemDumper(MetaGeneratedItem01.INSTANCE, "metaitem01"));
        API.addOption(new MetaItemDumper(MetaGeneratedItem02.INSTANCE, "metaitem02"));
        API.addOption(new MetaItemDumper(MetaGeneratedItem03.INSTANCE, "metaitem03"));
        API.addOption(new VoidProtectionSupportDumper());
        API.addOption(new InputSeparationSupportDumper());
        API.addOption(new BatchModeSupportDumper());
        API.addOption(new RecipeLockingSupportDumper());
    }

    @SubscribeEvent
    public void registerHandlerInfo(NEIRegisterHandlerInfosEvent event) {
        if (RECIPE_CATALYST_INDEX == null) {
            // This method will be called earlier than #loadConfig
            generateRecipeCatalystIndex();
        }
        RecipeCategory.ALL_RECIPE_CATEGORIES.values()
            .forEach(recipeCategory -> {
                HandlerInfo.Builder builder = createHandlerInfoBuilderTemplate(recipeCategory);
                HandlerInfo handlerInfo;
                if (recipeCategory.handlerInfoCreator != null) {
                    handlerInfo = recipeCategory.handlerInfoCreator.apply(builder)
                        .build();
                } else {
                    // Infer icon from recipe catalysts
                    RECIPE_CATALYST_INDEX.get(recipeCategory)
                        .stream()
                        .findFirst()
                        .ifPresent(catalyst -> builder.setDisplayStack(catalyst.getStackForm(1)));
                    handlerInfo = builder.build();
                }
                event.registerHandlerInfo(handlerInfo);
            });
    }

    private HandlerInfo.Builder createHandlerInfoBuilderTemplate(RecipeCategory recipeCategory) {
        return new HandlerInfo.Builder(
            recipeCategory.unlocalizedName,
            recipeCategory.ownerMod.getName(),
            recipeCategory.ownerMod.getModId()).setShiftY(6)
                .setHeight(135)
                .setMaxRecipesPerPage(2);
    }

    private static void generateRecipeCatalystIndex() {
        ImmutableListMultimap.Builder<RecipeCategory, RecipeMapWorkable> builder = new ImmutableListMultimap.Builder<>();
        builder
            .orderValuesBy(Comparator.comparing(recipeMapWorkable -> -recipeMapWorkable.getRecipeCatalystPriority()));
        for (int i = 1; i < GregTechAPI.METATILEENTITIES.length; i++) {
            IMetaTileEntity mte = GregTechAPI.METATILEENTITIES[i];
            if (!(mte instanceof RecipeMapWorkable recipeMapWorkable)) continue;
            for (RecipeMap<?> recipeMap : recipeMapWorkable.getAvailableRecipeMaps()) {
                for (RecipeCategory recipeCategory : recipeMap.getAssociatedCategories()) {
                    builder.put(recipeCategory, recipeMapWorkable);
                }
            }
        }
        RECIPE_CATALYST_INDEX = builder.build();
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
