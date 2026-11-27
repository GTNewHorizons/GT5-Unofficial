package gregtech.nei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;

import bartworks.system.material.CircuitGeneration.CircuitPartsItem;
import codechicken.lib.config.ConfigTagParent;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.SearchField;
import codechicken.nei.SearchField.SearchParserProvider;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.config.OptionCycled;
import codechicken.nei.event.NEIRegisterHandlerInfosEvent;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.HandlerInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.MetaGeneratedItem01;
import gregtech.common.items.MetaGeneratedItem02;
import gregtech.common.items.MetaGeneratedItem03;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.tileentities.machines.multi.nanochip.util.CCNEIRepresentation;
import gregtech.nei.dumper.BatchModeSupportDumper;
import gregtech.nei.dumper.InputSeparationSupportDumper;
import gregtech.nei.dumper.MaterialDumper;
import gregtech.nei.dumper.MetaItemDumper;
import gregtech.nei.dumper.MetaTileEntityDumper;
import gregtech.nei.dumper.RecipeLockingSupportDumper;
import gregtech.nei.dumper.TurbineFuelDumper;
import gregtech.nei.dumper.TurbineRotorDumper;
import gregtech.nei.dumper.VoidProtectionSupportDumper;
import gregtech.nei.searchprovider.ChemicalFormulaFilter;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.item.base.dusts.BaseItemDustUnique;
import gtPlusPlus.core.item.base.ore.BaseOreComponent;

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
    private static ListMultimap<RecipeCategory, RecipeMapWorkable> DEPRECATED_CATALYST_INDEX;

    private static final GTNEIImprintHandler CAL_IMPRINT_HANDLER = new GTNEIImprintHandler();

    public static boolean sIsAdded = true;

    private static void addHandler(TemplateRecipeHandler handler) {
        FMLInterModComms.sendRuntimeMessage(
            GTMod.GT,
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
        registerItemCategories();
        registerDumpers();
        registerFilters();
        hideItems();
        CCNEIRepresentation.init();
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

        GuiCraftingRecipe.craftinghandlers.add(CAL_IMPRINT_HANDLER);
        GuiUsageRecipe.usagehandlers.add(CAL_IMPRINT_HANDLER);
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
        for (Map.Entry<RecipeCategory, RecipeMapWorkable> entry : DEPRECATED_CATALYST_INDEX.entries()) {
            API.removeRecipeCatalyst(
                entry.getValue()
                    .getStackForm(1),
                entry.getKey().unlocalizedName);
        }
        API.addRecipeCatalyst(
            GTModHandler.getIC2Item("nuclearReactor", 1, null),
            RecipeMaps.ic2NuclearFakeRecipes.unlocalizedName);

        API.addRecipeCatalyst(new ItemStack(Items.cauldron), RecipeMaps.cauldronRecipe.unlocalizedName);

        for (ItemList item : Arrays.asList(
            ItemList.Machine_LV_Cutter,
            ItemList.Machine_MV_Cutter,
            ItemList.Machine_HV_Cutter,
            ItemList.Machine_EV_Cutter,
            ItemList.Machine_IV_Cutter,
            ItemList.CuttingMachineLuV,
            ItemList.CuttingMachineZPM,
            ItemList.CuttingMachineUV,
            ItemList.CuttingMachineUHV,
            ItemList.CuttingMachineUEV,
            ItemList.CuttingMachineUIV,
            ItemList.CuttingMachineUMV)) {
            API.addRecipeCatalyst(item.get(1), RecipeMaps.cutterFakeRecipes.unlocalizedName);
        }

        API.addRecipeCatalyst(ItemList.IndustrialCuttingMachine.get(1), RecipeMaps.cutterFakeRecipes.unlocalizedName);
    }

    private static final OrePrefixes[] NEI_CATEGORY_PREFIXES = { OrePrefixes.ingotHot, OrePrefixes.dustTiny,
        OrePrefixes.dustSmall, OrePrefixes.dustImpure, OrePrefixes.dustPure, OrePrefixes.crushed,
        OrePrefixes.crushedPurified, OrePrefixes.crushedCentrifuged };
    private final Set<OrePrefixes> categorized = new HashSet<>(Arrays.asList(NEI_CATEGORY_PREFIXES));

    private void registerItemEntries() {
        API.addItemListEntry(ItemList.VOLUMETRIC_FLASK.get(1));

        final MetaGeneratedItem01 item = MetaGeneratedItem01.INSTANCE;
        final List<ItemStack> entries = new ArrayList<>();
        item.addSubItems(
            item,
            null,
            entries,
            (prefix, material) -> categorized.contains(prefix)
                || item.doesShowInCreative(prefix, material, GregTechAPI.sDoShowAllItemsInCreative));
        API.setItemListEntries(item, entries);
    }

    private static boolean hasPrefix(ItemStack stack, OrePrefixes prefix) {
        ItemData data = GTOreDictUnificator.getAssociation(stack);
        if (data != null) {
            return data.mPrefix == prefix;
        }

        Item item = stack.getItem();
        if (item instanceof BaseItemComponent) { // GT++ hot ingots, tiny/small dusts
            return ((BaseItemComponent) item).componentType.getGtOrePrefix() == prefix;
        }
        if (item instanceof BaseOreComponent) { // GT++ impure/pure dust and crushed ore
            return prefix.getName()
                .equals(((BaseOreComponent) item).componentType.getOrePrefix());
        }
        if (item instanceof BaseItemDustUnique) { // GT++ unique Dusts
            String name = item.getUnlocalizedName();
            if (prefix == OrePrefixes.dustTiny) return name.contains("DustTiny");
            if (prefix == OrePrefixes.dustSmall) return name.contains("DustSmall");
        }
        return false;
    }

    private void registerItemCategories() {
        for (OrePrefixes prefix : NEI_CATEGORY_PREFIXES) {
            API.addItemCategory(
                "gregtech." + prefix.getName(),
                "gt.nei.category." + prefix.getName(),
                stack -> hasPrefix(stack, prefix),
                true);
        }
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
        API.addOption(new TurbineFuelDumper());
        API.addOption(new TurbineRotorDumper());
    }

    private void registerFilters() {
        API.addSearchProvider(
            new SearchParserProvider('=', "chemicalFormula", EnumChatFormatting.GREEN, ChemicalFormulaFilter::new));

        ConfigTagParent tag = NEIClientConfig.global.config;
        tag.getTag("inventory.search.chemicalFormulaSearchMode")
            .setComment("Search mode for chemical formulas (prefix: =)")
            .getIntValue(1);
        API.addOption(new OptionCycled("inventory.search.chemicalFormulaSearchMode", 3, true) {

            @Override
            public boolean onClick(int button) {
                if (!super.onClick(button)) {
                    return false;
                }
                SearchField.searchParser.clearCache();
                return true;
            }

            @Override
            public String getButtonText() {
                return translateN(
                    name + "." + value(),
                    EnumChatFormatting.GREEN + String.valueOf(SearchField.searchParser.getRedefinedPrefix('=')));
            }

            @Override
            public boolean isEnabled() {
                return !tag.getTag("inventory.search.format")
                    .getBooleanValue();
            }

        });
    }

    private void hideItems() {
        GTOreAdapter.INSTANCE.hideOres();
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

                if (recipeCategory.handlerInfoCreator != null) {
                    builder = recipeCategory.handlerInfoCreator.apply(builder);
                }

                event.registerHandlerInfo(builder.build());
            });

        event.registerHandlerInfo(
            new HandlerInfo.Builder(CAL_IMPRINT_HANDLER.getOverlayIdentifier(), "GregTech", Mods.ModIDs.GREG_TECH)
                .setMultipleWidgetsAllowed(true)
                .setDisplayStack(
                    CircuitPartsItem.getCircuitParts()
                        .getStack(0))
                .build());
    }

    private HandlerInfo.Builder createHandlerInfoBuilderTemplate(RecipeCategory recipeCategory) {
        // Infer icon from recipe catalysts
        final RecipeMapWorkable catalyst = RECIPE_CATALYST_INDEX.get(recipeCategory)
            .stream()
            .findFirst()
            .orElse(null);

        return new HandlerInfo.Builder(
            recipeCategory.unlocalizedName,
            recipeCategory.ownerMod.getName(),
            recipeCategory.ownerMod.getModId()).setShiftY(8)
                .setHeight(135)
                .setShowBadge(true)
                .setDisplayStack(catalyst != null ? catalyst.getStackForm(1) : null)
                .setMultipleWidgetsAllowed(true);
    }

    private static void generateRecipeCatalystIndex() {
        ImmutableListMultimap.Builder<RecipeCategory, RecipeMapWorkable> builder = new ImmutableListMultimap.Builder<>();
        builder
            .orderValuesBy(Comparator.comparing(recipeMapWorkable -> -recipeMapWorkable.getRecipeCatalystPriority()));
        ImmutableListMultimap.Builder<RecipeCategory, RecipeMapWorkable> deprecatedBuilder = new ImmutableListMultimap.Builder<>();
        for (int i = 1; i < GregTechAPI.METATILEENTITIES.length; i++) {
            IMetaTileEntity mte = GregTechAPI.METATILEENTITIES[i];
            if (!(mte instanceof RecipeMapWorkable recipeMapWorkable)) continue;
            boolean deprecated = recipeMapWorkable.isDeprecated();
            for (RecipeMap<?> recipeMap : recipeMapWorkable.getAvailableRecipeMaps()) {
                for (RecipeCategory recipeCategory : recipeMap.getAssociatedCategories()) {
                    if (deprecated) {
                        deprecatedBuilder.put(recipeCategory, recipeMapWorkable);
                    } else {
                        builder.put(recipeCategory, recipeMapWorkable);
                    }
                }
            }
        }
        RECIPE_CATALYST_INDEX = builder.build();
        DEPRECATED_CATALYST_INDEX = deprecatedBuilder.build();
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
