package kubatech.loaders;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.centrifugeNonCellRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import kubatech.api.gui.HighTemperatureGasCooledReactorRecipeMapFrontend;
import kubatech.api.utils.ModUtils;
import kubatech.client.renderer.HTGRItemRenderer;
import kubatech.loaders.item.htgritem.HTGRItem;

public class HTGRLoader {

    public static final HTGRItem HTGR_ITEM = new HTGRItem();

    @SuppressWarnings("unchecked")
    public static final RecipeMetadataKey<Pair<ItemStack, Integer>[]> FUEL = SimpleRecipeMetadataKey
        .create((Class<Pair<ItemStack, Integer>[]>) (Class<?>) Pair[].class, "htgr_fuel");
    public static final RecipeMetadataKey<ItemStack[]> SHELL = SimpleRecipeMetadataKey
        .create(ItemStack[].class, "htgr_shell");

    public static final RecipeMap<RecipeMapBackend> HTGRRecipes = RecipeMapBuilder.of("kubatech.htgrrecipes")
        .maxIO(9, 3, 1, 1)
        .minInputs(1, 0)
        .neiHandlerInfo(
            builder -> builder.setHeight(255)
                .setMaxRecipesPerPage(1)
                .setDisplayStack(kubatech.api.enums.ItemList.HighTemperatureGasCooledReactor.get(1)))
        .neiTransferRect(127, 50, 36, 98)
        .frontend(HighTemperatureGasCooledReactorRecipeMapFrontend::new)
        .builderTransformer(builder -> {
            ItemStack[] inputs = builder.getItemInputsBasic();
            Materials material = GTOreDictUnificator.getAssociation(inputs[0]).mMaterial.mMaterial;
            Pair<ItemStack, Integer>[] fuels = builder.getMetadata(FUEL);
            ItemStack[] shells = builder.getMetadataOrDefault(SHELL, new ItemStack[0]);

            ArrayList<ItemStack> items = new ArrayList<>();
            Collections.addAll(items, inputs);
            if (fuels != null) {
                for (Pair<ItemStack, Integer> fuel : fuels) {
                    items.add(fuel.getLeft());
                }
            } ;

            // mixer
            GTValues.RA.stdBuilder()
                .itemInputs(items.toArray(new ItemStack[0]))
                .itemOutputs(HTGR_ITEM.createTRISOMixture(material))
                .eut(TierEU.RECIPE_EV)
                .duration(100)
                .nbtSensitive()
                .addTo(mixerRecipes);

            // forming shell
            GTValues.RA.stdBuilder()
                .itemInputs(HTGR_ITEM.createTRISOMixture(material), shells[0])
                .itemOutputs(HTGR_ITEM.createIncompleteBISOFuel(material))
                .eut(TierEU.RECIPE_EV)
                .duration(50)
                .nbtSensitive()
                .addTo(formingPressRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(HTGR_ITEM.createIncompleteBISOFuel(material), shells[1])
                .itemOutputs(HTGR_ITEM.createIncompleteTRISOFuel(material))
                .eut(TierEU.RECIPE_EV)
                .duration(50)
                .nbtSensitive()
                .addTo(formingPressRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(HTGR_ITEM.createIncompleteTRISOFuel(material), shells[2])
                .itemOutputs(HTGR_ITEM.createTRISOFuel(material))
                .eut(TierEU.RECIPE_EV)
                .duration(50)
                .nbtSensitive()
                .addTo(formingPressRecipes);

            // htgr?

            // recycle

            items.clear();
            Collections.addAll(items, builder.getItemOutputs());
            if (fuels != null) {
                for (Pair<ItemStack, Integer> fuel : fuels) {
                    ItemStack fuelStack = fuel.getLeft()
                        .copy();
                    fuelStack.stackSize = fuel.getRight();
                    items.add(fuelStack);
                }
            }
            int[] chances = new int[items.size() + 3];
            Arrays.fill(chances, 0, items.size(), 10000);
            Arrays.fill(chances, items.size(), items.size() + 3, 9500);
            items.addAll(Arrays.asList(shells));

            GTValues.RA.stdBuilder()
                .itemInputs(HTGR_ITEM.createBurnedTRISOFuel(material))
                .itemOutputs(items.toArray(new ItemStack[0]))
                .outputChances(chances)
                .eut(TierEU.RECIPE_EV)
                .duration(300)
                .nbtSensitive()
                .addTo(centrifugeNonCellRecipes);
        })
        .build();

    public static void load() {

        GameRegistry.registerItem(HTGR_ITEM, "htgr_item");
        if (ModUtils.isClientSided) MinecraftForgeClient.registerItemRenderer(HTGR_ITEM, new HTGRItemRenderer());

        // silver to indium

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 2L))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 1L))
            .metadata(
                FUEL,
                new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 2L), 1),
                    Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium241, 3L), 2) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);

        // plutonium breeding

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1L))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 1L))
            .outputChances(10000, 10000, 2000, 10000)
            .metadata(FUEL, new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 5L), 3) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);

        // plutonium 241 breeding

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 2L))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium241, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 4L))
            .metadata(FUEL, new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 5L), 2) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);

        // thorium to lutetium

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 5L))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lutetium, 4L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 1L))
            .metadata(
                FUEL,
                new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1L), 0) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);

        // tungsten to osmium

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 5L))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 4L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 2L))
            .metadata(
                FUEL,
                new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium241, 3L), 0) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);

        // praseodymium to promethium

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Praseodymium, 2L))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Promethium, 2L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 3L))
            .metadata(
                FUEL,
                new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium241, 3L), 0) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);

        // lanthanum to praseodymium

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 3L))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Praseodymium, 2L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 3L))
            .metadata(
                FUEL,
                new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 3L), 0) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);

        // caesium to lanthanum

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 3L))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 2L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 3L))
            .metadata(
                FUEL,
                new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 3L), 0) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);

        // glowstone to sunnarium

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 5L))
            .circuit(2)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 4L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 3L))
            .metadata(
                FUEL,
                new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 3L), 1),
                    Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 3L), 2) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);

        // Structure blocks and items for HTGR

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Beryllium_Shielding_Plate.get(2L),
                ItemList.Casing_Refined_Graphite.get(1L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 6L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 12L))
            .itemOutputs(ItemList.Casing_Graphite_Moderator.get(1L))
            .duration(GTRecipeBuilder.SECONDS * 15)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Titanium, 1L),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Titanium, 1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MicaInsulatorFoil", 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 4L),
                ItemList.Alumina_Support_Ring.get(2L))
            .itemOutputs(ItemList.Casing_Insulated_Fluid_Pipe.get(1L))
            .duration(GTRecipeBuilder.SECONDS * 20)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Beryllium_Shielding_Plate.get(1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 8L),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L))
            .fluidInputs(Materials.Lead.getMolten(1152L))
            .itemOutputs(ItemList.Casing_Beryllium_Integrated_Reactor.get(1L))
            .duration(GTRecipeBuilder.SECONDS * 30)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Beryllium, 1L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 4L))
            .fluidInputs(Materials.Beryllium.getMolten(144L))
            .itemOutputs(ItemList.Beryllium_Shielding_Plate.get(1L))
            .duration(GTRecipeBuilder.SECONDS * 30)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Alumina_Support_Ring_Raw.get(1L))
            .itemOutputs(ItemList.Alumina_Support_Ring.get(1L))
            .duration(GTRecipeBuilder.SECONDS * 60)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Aluminiumoxide.getDust(4), ItemList.Shape_Mold_Ring.get(0))
            .itemOutputs(ItemList.Alumina_Support_Ring_Raw.get(1L))
            .duration(GTRecipeBuilder.SECONDS * 25)
            .eut(TierEU.RECIPE_IV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Graphite.getDust(18))
            .itemOutputs(ItemList.Casing_Refined_Graphite.get(1L))
            .duration(GTRecipeBuilder.SECONDS * 30)
            .eut(TierEU.RECIPE_MV)
            .addTo(compressorRecipes);

    }

}
