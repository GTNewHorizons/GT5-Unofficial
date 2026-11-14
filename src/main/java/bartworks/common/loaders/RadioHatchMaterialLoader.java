package bartworks.common.loaders;

import static bartworks.API.recipe.BartWorksRecipeMaps.radioHatchFakeRecipes;
import static gregtech.api.util.GTRecipeConstants.MASS;
import static gregtech.api.util.GTRecipeConstants.SIEVERT;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Sievert;

public class RadioHatchMaterialLoader {

    static List<RadioHatchMaterial> radioHatchMaterials = new ArrayList<>();

    /**
     * This is for keeping compatibility with potential calls outside GT5U.
     *
     * @deprecated Use {@link RadioHatchMaterialLoader#radioHatchMaterialAdder(ItemStack, long, int)} instead to avoid
     *             casting
     */
    @Deprecated
    public static void radioHatchMaterialAdder(ItemStack recipeInput, int recipeSievert, byte recipeMass) {
        radioHatchMaterials.add(new RadioHatchMaterial(recipeInput, recipeSievert, recipeMass));
    }

    public static void radioHatchMaterialAdder(ItemStack recipeInput, long recipeSievert, int recipeMass) {
        radioHatchMaterials.add(new RadioHatchMaterial(recipeInput, recipeSievert, recipeMass));
    }

    public static class RadioHatchMaterial {

        private final ItemStack recipeInput;
        public final int recipeSievert;
        public final byte recipeMass;

        /**
         * This is for keeping compatibility with potential calls outside GT5U.
         *
         * @deprecated Use {@link RadioHatchMaterial#RadioHatchMaterial(ItemStack, long, int)} instead to avoid casting
         */
        @Deprecated
        public RadioHatchMaterial(ItemStack recipeInput, int recipeSievert, byte recipeMass) {
            this.recipeInput = recipeInput;
            this.recipeSievert = recipeSievert;
            this.recipeMass = recipeMass;
        }

        public RadioHatchMaterial(ItemStack recipeInput, long recipeSievert, int recipeMass) {
            this.recipeInput = recipeInput;
            this.recipeSievert = (int) recipeSievert;
            this.recipeMass = (byte) recipeMass;
        }

        public ItemStack getRecipeInput() {
            return recipeInput.copy();
        }
    }

    public static void run() {
        addRadioHatchMaterials();
        loadRadioHatchNEI();
    }

    public static void addRadioHatchMaterials() {
        /*
         * Todo: map statically all the materials used instead of iterating on the pools of materials.
         * List of Werstoff materials processed dynamically as of this commit: Thorium232, UraniumBasedLiquidFuel,
         * UraniumBasedLiquidFuel(ExcitedState), ThoriumBasedLiquidFuel, ThoriumBasedLiquidFuel(ExcitedState),
         * PlutoniumBasedLiquidFuel, PlutoniumBasedLiquidFuel(ExcitedState), ExtremelyUnstableNaquadah,
         * LightNaquadahFuel,
         * HeavyNaquadahFuel, NaquadahGas, NaquadahAsphalt, RadioactiveSludge, AcidNaquadahEmulsion, NaquadahEmulsion,
         * NaquadahSolution, NaquadahBasedLiquidFuelMkI, NaquadahBasedLiquidFuelMkII, NaquadahBasedLiquidFuelMkIII,
         * NaquadahBasedLiquidFuelMkIV, NaquadahBasedLiquidFuelMkV, NaquadahBasedLiquidFuelMkVI,
         * Enriched-NaquadahOxideMixture, Enriched-Naquadah-RichSolution, ConcentratedEnriched-NaquadahSludge,
         * Enriched-NaquadahSulphate, NaquadriaOxideMixture, LowQualityNaquadriaPhosphate, Naquadria-RichSolution,
         * LowQualityNaquadriaSulphate, LowQualityNaquadriaSulphate, NaquadriaSulphate, EnrichedNaquadahGoo,
         * NaquadriaGoo, InertEnrichedNaquadah, InertNaquadria, Thorium234
         * List of GT materials processed dynamically as of this commit: Americium, Bismuth, Flerovium_GT5U, Plutonium,
         * Radon, Thorium, Uranium, Plutonium241, Uranium235, Oganesson, Californium, Tiberium
         */

        for (Werkstoff material : Werkstoff.werkstoffHashSet) {
            if (material == null || !material.getStats()
                .isRadioactive() || material == WerkstoffLoader.Tiberium) {
                continue;
            }

            int level = (int) material.getStats()
                .getProtons();
            if (material.hasItemType(OrePrefixes.stick))
                radioHatchMaterialAdder(material.get(OrePrefixes.stick), level, 1);
            if (material.hasItemType(OrePrefixes.stickLong))
                radioHatchMaterialAdder(material.get(OrePrefixes.stickLong), level, 2);

        }

        for (Materials material : Materials.getAll()) {
            if (material == null || material.mElement == null) continue;

            boolean validProton = material.getProtons() >= 83 && !material.equals(Materials.Tritanium) // No Tritanium
                && !material.equals(Materials.Naquadah); // Naquadah needs spacial value
            if (validProton) {
                int level = (int) material.getProtons();
                if (GTOreDictUnificator.get(OrePrefixes.stick, material, 1) != null) {
                    radioHatchMaterialAdder(GTOreDictUnificator.get(OrePrefixes.stick, material, 1), level, 1);

                }
                if (GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1) != null) {
                    radioHatchMaterialAdder(GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1), level, 2);
                }
            }
        }

        Materials[] specialMaterial = { Materials.Naquadah, Materials.NaquadahEnriched, Materials.Naquadria };
        int[] specialValue = { 130, 140, 150 };

        for (int i = 0; i < specialMaterial.length; i++) {
            if (GTOreDictUnificator.get(OrePrefixes.stick, specialMaterial[i], 1) != null) {
                radioHatchMaterialAdder(
                    GTOreDictUnificator.get(OrePrefixes.stick, specialMaterial[i], 1),
                    specialValue[i],
                    1);
            }

            if (GTOreDictUnificator.get(OrePrefixes.stickLong, specialMaterial[i], 1) != null) {
                radioHatchMaterialAdder(
                    GTOreDictUnificator.get(OrePrefixes.stickLong, specialMaterial[i], 1),
                    specialValue[i],
                    2);
            }
        }

        radioHatchMaterialAdder(ItemList.RodThorium.get(1), Materials.Thorium.getProtons(), 3);
        radioHatchMaterialAdder(ItemList.RodThorium2.get(1), Materials.Thorium.getProtons(), 6);
        radioHatchMaterialAdder(ItemList.RodThorium4.get(1), Materials.Thorium.getProtons(), 12);
        radioHatchMaterialAdder(ItemList.RodNaquadah.get(1), 140, 3);
        radioHatchMaterialAdder(ItemList.RodNaquadah2.get(1), 140, 6);
        radioHatchMaterialAdder(ItemList.RodNaquadah4.get(1), 140, 12);
        radioHatchMaterialAdder(ItemList.RodMOX.get(1), Materials.Plutonium.getProtons(), 3);
        radioHatchMaterialAdder(ItemList.RodMOX2.get(1), Materials.Plutonium.getProtons(), 6);
        radioHatchMaterialAdder(ItemList.RodMOX4.get(1), Materials.Plutonium.getProtons(), 12);
        radioHatchMaterialAdder(ItemList.RodUranium.get(1), Materials.Uranium.getProtons(), 3);
        radioHatchMaterialAdder(ItemList.RodUranium2.get(1), Materials.Uranium.getProtons(), 6);
        radioHatchMaterialAdder(ItemList.RodUranium4.get(1), Materials.Uranium.getProtons(), 12);
        radioHatchMaterialAdder(
            ItemList.RodTiberium.get(1),
            WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons(),
            3);
        radioHatchMaterialAdder(
            ItemList.RodTiberium2.get(1),
            WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons(),
            6);
        radioHatchMaterialAdder(
            ItemList.RodTiberium4.get(1),
            WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons(),
            12);
        radioHatchMaterialAdder(ItemList.DepletedRodThorium.get(1), Materials.Thorium.getProtons() / 10, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodThorium2.get(1), Materials.Thorium.getProtons() / 10, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodThorium4.get(1), Materials.Thorium.getProtons() / 10, 12);
        radioHatchMaterialAdder(ItemList.RodNaquadah32.get(1), 140, 96);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah.get(1), 14, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah2.get(1), 14, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah4.get(1), 14, 12);
        radioHatchMaterialAdder(ItemList.DepletedRodMOX.get(1), Materials.Plutonium.getProtons() / 10, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodMOX2.get(1), Materials.Plutonium.getProtons() / 10, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodMOX4.get(1), Materials.Plutonium.getProtons() / 10, 12);
        radioHatchMaterialAdder(ItemList.DepletedRodUranium.get(1), Materials.Uranium.getProtons() / 10, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodUranium2.get(1), Materials.Uranium.getProtons() / 10, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodUranium4.get(1), Materials.Uranium.getProtons() / 10, 12);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium.get(1),
            WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons() / 10,
            3);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium2.get(1),
            WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons() / 10,
            6);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium4.get(1),
            WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons() / 10,
            12);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah32.get(1), 13, 96);
        radioHatchMaterialAdder(ItemList.RodNaquadria.get(1), 150, 3);
        radioHatchMaterialAdder(ItemList.RodNaquadria2.get(1), 150, 6);
        radioHatchMaterialAdder(ItemList.RodNaquadria4.get(1), 150, 12);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadria.get(1), 15, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadria2.get(1), 15, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadria4.get(1), 15, 12);
    }

    public static void loadRadioHatchNEI() {
        for (RadioHatchMaterial recipes : radioHatchMaterials) {
            GTValues.RA.stdBuilder()
                .itemInputs(recipes.getRecipeInput())
                .duration(0)
                .eut(0)
                .metadata(SIEVERT, new Sievert(recipes.recipeSievert))
                .metadata(MASS, (int) recipes.recipeMass)
                .fake()
                .addTo(radioHatchFakeRecipes);
        }
    }

    public static RadioHatchMaterial getRadioHatchMaterialFromInput(RadioHatchMaterial recipe, ItemStack Material) {
        if (recipe.getRecipeInput()
            .isItemEqual(Material)) {
            return recipe;
        }
        return null;
    }

    public static List<RadioHatchMaterial> getRadioHatchMaterialList() {
        return radioHatchMaterials;
    }
}
