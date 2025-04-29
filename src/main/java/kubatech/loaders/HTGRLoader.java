package kubatech.loaders;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;
import gregtech.api.util.GTOreDictUnificator;
import kubatech.Tags;
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
        // .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("kubatech.defusioncrafter.tier"))
        .slotOverlays(
            (index, isFluid, isOutput,
                isSpecial) -> !isFluid && !isOutput ? UITexture.fullImage(Tags.MODID, "gui/slot/fusion_crafter") : null)
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
            }

            // mixer
            GTValues.RA.stdBuilder()
                .itemInputs(items.toArray(new ItemStack[0]))
                .itemOutputs(HTGR_ITEM.createTRISOMixture(material))
                .eut(1337)
                .duration(1337)
                .nbtSensitive()
                .addTo(mixerRecipes);

            // forming shell
            GTValues.RA.stdBuilder()
                .itemInputs(HTGR_ITEM.createTRISOMixture(material), shells[0])
                .itemOutputs(HTGR_ITEM.createIncompleteBISOFuel(material))
                .eut(1337)
                .duration(1337)
                .nbtSensitive()
                .addTo(formingPressRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(HTGR_ITEM.createIncompleteBISOFuel(material), shells[1])
                .itemOutputs(HTGR_ITEM.createIncompleteTRISOFuel(material))
                .eut(1337)
                .duration(1337)
                .nbtSensitive()
                .addTo(formingPressRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(HTGR_ITEM.createIncompleteTRISOFuel(material), shells[2])
                .itemOutputs(HTGR_ITEM.createTRISOFuel(material))
                .eut(1337)
                .duration(1337)
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
                .eut(1337)
                .duration(1337)
                .nbtSensitive()
                .addTo(centrifugeRecipes);
        })
        .build();

    public static void load() {

        GameRegistry.registerItem(HTGR_ITEM, "htgr_item");

        /*
            .inputs(Silver, 2)
            .outputs(Silver, 1, Indium, 1)
            .fuel(Uranium 235, 2, 1, Plutonium, 241, 3, 2)
            .TRISO(Inner Layer, Middle Layer, Outer layer)
         */

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 2L))
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

        /*
            .inputs(Uranium 235, 1)
            .outputs(Plutonium 239, 3, Uranium 238,  2)
            .fuel(Uranium 238, 5)
            .TRISO(Inner Layer, Middle Layer, Outer layer)
         */

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 3L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 2L))
            .metadata(
                FUEL,
                new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 5L), 1) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);

        /*
            .inputs(Plutonium 239, 1)
            .outputs(Plutonium 239, 3, Uranium 238,  2)
            .fuel(Uranium 238, 5)
            .TRISO(Inner Layer, Middle Layer, Outer layer)
         */

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 3L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 2L))
            .metadata(
                FUEL,
                new Pair[] { Pair.of(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 5L), 1) })
            .metadata(
                SHELL,
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L) })
            .duration(1)
            .eut(1)
            .addTo(HTGRRecipes);
    }

}
