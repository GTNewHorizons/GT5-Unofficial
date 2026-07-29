package bartworks.common.loaders;

import static bartworks.API.recipe.BartWorksRecipeMaps.radioHatchFakeRecipes;
import static gregtech.api.util.GTRecipeConstants.MASS;
import static gregtech.api.util.GTRecipeConstants.SIEVERT;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Sievert;
import gregtech.loaders.materials.LegacyNameDomain;

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

        for (Material material : MaterialLibAPI.getMaterials()) {
            if (material.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null
                || !Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.IS_RADIOACTIVE))
                || material == Materials2Materials.Tiberium) {
                continue;
            }

            int level = (int) MaterialUtils.protons(material);
            if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.stick)) {
                radioHatchMaterialAdder(MU.stack(OrePrefixes.stick, material, 1), level, 1);
            }
            if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.stickLong)) {
                radioHatchMaterialAdder(MU.stack(OrePrefixes.stickLong, material, 1), level, 2);
            }
        }

        for (Material material : MaterialLibAPI.getMaterials()) {
            if (!LegacyNameDomain.contains(material) || MaterialUtils.element(material) == null) continue;

            boolean validProton = MaterialUtils.protons(material) >= 83 && material != Materials2Materials.Tritanium // No
            // Tritanium
                && material != Materials2Materials.Naquadah; // Naquadah needs spacial value
            if (validProton) {
                int level = (int) MaterialUtils.protons(material);
                if (GTOreDictUnificator.get(OrePrefixes.stick, material, 1) != null) {
                    radioHatchMaterialAdder(GTOreDictUnificator.get(OrePrefixes.stick, material, 1), level, 1);

                }
                if (GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1) != null) {
                    radioHatchMaterialAdder(GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1), level, 2);
                }
            }
        }

        Material[] specialMaterial = { Materials2Materials.Naquadah, Materials2Materials.NaquadahEnriched,
            Materials2Materials.Naquadria };
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

        radioHatchMaterialAdder(ItemList.RodThorium.get(1), MaterialUtils.protons(Materials2Materials.Thorium), 3);
        radioHatchMaterialAdder(ItemList.RodThorium2.get(1), MaterialUtils.protons(Materials2Materials.Thorium), 6);
        radioHatchMaterialAdder(ItemList.RodThorium4.get(1), MaterialUtils.protons(Materials2Materials.Thorium), 12);
        radioHatchMaterialAdder(ItemList.RodNaquadah.get(1), 140, 3);
        radioHatchMaterialAdder(ItemList.RodNaquadah2.get(1), 140, 6);
        radioHatchMaterialAdder(ItemList.RodNaquadah4.get(1), 140, 12);
        radioHatchMaterialAdder(ItemList.RodMOX.get(1), MaterialUtils.protons(Materials2Materials.Plutonium), 3);
        radioHatchMaterialAdder(ItemList.RodMOX2.get(1), MaterialUtils.protons(Materials2Materials.Plutonium), 6);
        radioHatchMaterialAdder(ItemList.RodMOX4.get(1), MaterialUtils.protons(Materials2Materials.Plutonium), 12);
        radioHatchMaterialAdder(ItemList.RodUranium.get(1), MaterialUtils.protons(Materials2Materials.Uranium), 3);
        radioHatchMaterialAdder(ItemList.RodUranium2.get(1), MaterialUtils.protons(Materials2Materials.Uranium), 6);
        radioHatchMaterialAdder(ItemList.RodUranium4.get(1), MaterialUtils.protons(Materials2Materials.Uranium), 12);
        radioHatchMaterialAdder(ItemList.RodTiberium.get(1), MaterialUtils.protons(Materials2Materials.Tiberium), 3);
        radioHatchMaterialAdder(ItemList.RodTiberium2.get(1), MaterialUtils.protons(Materials2Materials.Tiberium), 6);
        radioHatchMaterialAdder(ItemList.RodTiberium4.get(1), MaterialUtils.protons(Materials2Materials.Tiberium), 12);
        radioHatchMaterialAdder(
            ItemList.DepletedRodThorium.get(1),
            MaterialUtils.protons(Materials2Materials.Thorium) / 10,
            3);
        radioHatchMaterialAdder(
            ItemList.DepletedRodThorium2.get(1),
            MaterialUtils.protons(Materials2Materials.Thorium) / 10,
            6);
        radioHatchMaterialAdder(
            ItemList.DepletedRodThorium4.get(1),
            MaterialUtils.protons(Materials2Materials.Thorium) / 10,
            12);
        radioHatchMaterialAdder(ItemList.RodNaquadah32.get(1), 140, 96);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah.get(1), 14, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah2.get(1), 14, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah4.get(1), 14, 12);
        radioHatchMaterialAdder(
            ItemList.DepletedRodMOX.get(1),
            MaterialUtils.protons(Materials2Materials.Plutonium) / 10,
            3);
        radioHatchMaterialAdder(
            ItemList.DepletedRodMOX2.get(1),
            MaterialUtils.protons(Materials2Materials.Plutonium) / 10,
            6);
        radioHatchMaterialAdder(
            ItemList.DepletedRodMOX4.get(1),
            MaterialUtils.protons(Materials2Materials.Plutonium) / 10,
            12);
        radioHatchMaterialAdder(
            ItemList.DepletedRodUranium.get(1),
            MaterialUtils.protons(Materials2Materials.Uranium) / 10,
            3);
        radioHatchMaterialAdder(
            ItemList.DepletedRodUranium2.get(1),
            MaterialUtils.protons(Materials2Materials.Uranium) / 10,
            6);
        radioHatchMaterialAdder(
            ItemList.DepletedRodUranium4.get(1),
            MaterialUtils.protons(Materials2Materials.Uranium) / 10,
            12);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium.get(1),
            MaterialUtils.protons(Materials2Materials.Tiberium) / 10,
            3);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium2.get(1),
            MaterialUtils.protons(Materials2Materials.Tiberium) / 10,
            6);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium4.get(1),
            MaterialUtils.protons(Materials2Materials.Tiberium) / 10,
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
