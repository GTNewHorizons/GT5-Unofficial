package bartworks.common.loaders;

import static bartworks.API.recipe.BartWorksRecipeMaps.radioHatchFakeRecipes;
import static gregtech.api.util.GTRecipeConstants.MASS;
import static gregtech.api.util.GTRecipeConstants.SIEVERT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialUtils;
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
        Material[] specialMaterial = { Materials.Naquadah, Materials.NaquadahEnriched, Materials.Naquadria };
        int[] specialValue = { 130, 140, 150 };

        for (Material material : MaterialLibAPI.getMaterials()) {
            if (!isRadioactive(material) || material == Materials.Tiberium
                || material == Materials.Tritanium
                || Arrays.asList(specialMaterial)
                    .contains(material)) {
                continue;
            }
            addRods(material, (int) MaterialUtils.protons(material));
        }

        for (int i = 0; i < specialMaterial.length; i++) {
            addRods(specialMaterial[i], specialValue[i]);
        }

        radioHatchMaterialAdder(ItemList.RodThorium.get(1), MaterialUtils.protons(Materials.Thorium), 3);
        radioHatchMaterialAdder(ItemList.RodThorium2.get(1), MaterialUtils.protons(Materials.Thorium), 6);
        radioHatchMaterialAdder(ItemList.RodThorium4.get(1), MaterialUtils.protons(Materials.Thorium), 12);
        radioHatchMaterialAdder(ItemList.RodNaquadah.get(1), 140, 3);
        radioHatchMaterialAdder(ItemList.RodNaquadah2.get(1), 140, 6);
        radioHatchMaterialAdder(ItemList.RodNaquadah4.get(1), 140, 12);
        radioHatchMaterialAdder(ItemList.RodMOX.get(1), MaterialUtils.protons(Materials.Plutonium), 3);
        radioHatchMaterialAdder(ItemList.RodMOX2.get(1), MaterialUtils.protons(Materials.Plutonium), 6);
        radioHatchMaterialAdder(ItemList.RodMOX4.get(1), MaterialUtils.protons(Materials.Plutonium), 12);
        radioHatchMaterialAdder(ItemList.RodUranium.get(1), MaterialUtils.protons(Materials.Uranium), 3);
        radioHatchMaterialAdder(ItemList.RodUranium2.get(1), MaterialUtils.protons(Materials.Uranium), 6);
        radioHatchMaterialAdder(ItemList.RodUranium4.get(1), MaterialUtils.protons(Materials.Uranium), 12);
        radioHatchMaterialAdder(ItemList.RodTiberium.get(1), MaterialUtils.protons(Materials.Tiberium), 3);
        radioHatchMaterialAdder(ItemList.RodTiberium2.get(1), MaterialUtils.protons(Materials.Tiberium), 6);
        radioHatchMaterialAdder(ItemList.RodTiberium4.get(1), MaterialUtils.protons(Materials.Tiberium), 12);
        radioHatchMaterialAdder(ItemList.DepletedRodThorium.get(1), MaterialUtils.protons(Materials.Thorium) / 10, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodThorium2.get(1), MaterialUtils.protons(Materials.Thorium) / 10, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodThorium4.get(1), MaterialUtils.protons(Materials.Thorium) / 10, 12);
        radioHatchMaterialAdder(ItemList.RodNaquadah32.get(1), 140, 96);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah.get(1), 14, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah2.get(1), 14, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah4.get(1), 14, 12);
        radioHatchMaterialAdder(ItemList.DepletedRodMOX.get(1), MaterialUtils.protons(Materials.Plutonium) / 10, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodMOX2.get(1), MaterialUtils.protons(Materials.Plutonium) / 10, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodMOX4.get(1), MaterialUtils.protons(Materials.Plutonium) / 10, 12);
        radioHatchMaterialAdder(ItemList.DepletedRodUranium.get(1), MaterialUtils.protons(Materials.Uranium) / 10, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodUranium2.get(1), MaterialUtils.protons(Materials.Uranium) / 10, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodUranium4.get(1), MaterialUtils.protons(Materials.Uranium) / 10, 12);
        radioHatchMaterialAdder(ItemList.DepletedRodTiberium.get(1), MaterialUtils.protons(Materials.Tiberium) / 10, 3);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium2.get(1),
            MaterialUtils.protons(Materials.Tiberium) / 10,
            6);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium4.get(1),
            MaterialUtils.protons(Materials.Tiberium) / 10,
            12);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah32.get(1), 13, 96);
        radioHatchMaterialAdder(ItemList.RodNaquadria.get(1), 150, 3);
        radioHatchMaterialAdder(ItemList.RodNaquadria2.get(1), 150, 6);
        radioHatchMaterialAdder(ItemList.RodNaquadria4.get(1), 150, 12);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadria.get(1), 15, 3);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadria2.get(1), 15, 6);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadria4.get(1), 15, 12);
    }

    /// Whether `material`'s rods are radio hatch fuel: it has a [GTMaterialProperties#RADIATION_LEVEL], or it is an
    /// element at or above bismuth.
    private static boolean isRadioactive(Material material) {
        return material.getProperty(GTMaterialProperties.RADIATION_LEVEL) != null
            || MaterialUtils.element(material) != null && MaterialUtils.protons(material) >= 83;
    }

    private static void addRods(Material material, int level) {
        ItemStack stick = MaterialLibAPI.findStack(material, Shapes.stick, 1);
        if (stick != null) radioHatchMaterialAdder(stick, level, 1);
        ItemStack stickLong = MaterialLibAPI.findStack(material, Shapes.stickLong, 1);
        if (stickLong != null) radioHatchMaterialAdder(stickLong, level, 2);
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
