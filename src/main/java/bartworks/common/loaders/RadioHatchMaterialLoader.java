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

    public static void radioHatchMaterialAdder(ItemStack recipeInput, int recipeSievert, byte recipeMass) {
        radioHatchMaterials.add(new RadioHatchMaterial(recipeInput, recipeSievert, recipeMass));
    }

    public static class RadioHatchMaterial {

        private final ItemStack recipeInput;
        public final int recipeSievert;
        public final byte recipeMass;

        public RadioHatchMaterial(ItemStack recipeInput, int recipeSievert, byte recipeMass) {
            this.recipeInput = recipeInput;
            this.recipeSievert = recipeSievert;
            this.recipeMass = recipeMass;
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

        for (Werkstoff material : Werkstoff.werkstoffHashSet) {
            if (material == null || !material.getStats()
                .isRadioactive() || material == WerkstoffLoader.Tiberium) {
                continue;
            }

            int level = (int) material.getStats()
                .getProtons();
            if (material.hasItemType(OrePrefixes.stick))
                radioHatchMaterialAdder(material.get(OrePrefixes.stick), level, (byte) 1);
            if (material.hasItemType(OrePrefixes.stickLong))
                radioHatchMaterialAdder(material.get(OrePrefixes.stickLong), level, (byte) 2);

        }

        for (Materials material : Materials.getAll()) {
            if (material == null || material.mElement == null) continue;
            boolean validProton = material.getProtons() >= 83 && !material.equals(Materials.Tritanium) // No Tritanium
                && !material.equals(Materials.Naquadah); // Naquadah needs spacial value
            if (validProton) {
                int level = (int) material.getProtons();
                if (GTOreDictUnificator.get(OrePrefixes.stick, material, 1) != null) {
                    radioHatchMaterialAdder(GTOreDictUnificator.get(OrePrefixes.stick, material, 1), level, (byte) 1);

                }
                if (GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1) != null) {
                    radioHatchMaterialAdder(
                        GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1),
                        level,
                        (byte) 2);
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
                    (byte) 1);
            }

            if (GTOreDictUnificator.get(OrePrefixes.stickLong, specialMaterial[i], 1) != null) {
                radioHatchMaterialAdder(
                    GTOreDictUnificator.get(OrePrefixes.stickLong, specialMaterial[i], 1),
                    specialValue[i],
                    (byte) 2);
            }
        }

        radioHatchMaterialAdder(ItemList.RodThorium.get(1), (int) Materials.Thorium.getProtons(), (byte) 3);
        radioHatchMaterialAdder(ItemList.RodThorium2.get(1), (int) Materials.Thorium.getProtons(), (byte) 6);
        radioHatchMaterialAdder(ItemList.RodThorium4.get(1), (int) Materials.Thorium.getProtons(), (byte) 12);
        radioHatchMaterialAdder(ItemList.RodNaquadah.get(1), 140, (byte) 3);
        radioHatchMaterialAdder(ItemList.RodNaquadah2.get(1), 140, (byte) 6);
        radioHatchMaterialAdder(ItemList.RodNaquadah4.get(1), 140, (byte) 12);
        radioHatchMaterialAdder(ItemList.RodMOX.get(1), (int) Materials.Plutonium.getProtons(), (byte) 3);
        radioHatchMaterialAdder(ItemList.RodMOX2.get(1), (int) Materials.Plutonium.getProtons(), (byte) 6);
        radioHatchMaterialAdder(ItemList.RodMOX4.get(1), (int) Materials.Plutonium.getProtons(), (byte) 12);
        radioHatchMaterialAdder(ItemList.RodUranium.get(1), (int) Materials.Uranium.getProtons(), (byte) 3);
        radioHatchMaterialAdder(ItemList.RodUranium2.get(1), (int) Materials.Uranium.getProtons(), (byte) 6);
        radioHatchMaterialAdder(ItemList.RodUranium4.get(1), (int) Materials.Uranium.getProtons(), (byte) 12);
        radioHatchMaterialAdder(
            ItemList.RodTiberium.get(1),
            (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons(),
            (byte) 3);
        radioHatchMaterialAdder(
            ItemList.RodTiberium2.get(1),
            (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons(),
            (byte) 6);
        radioHatchMaterialAdder(
            ItemList.RodTiberium4.get(1),
            (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons(),
            (byte) 12);
        radioHatchMaterialAdder(
            ItemList.DepletedRodThorium.get(1),
            (int) Materials.Thorium.getProtons() / 10,
            (byte) 3);
        radioHatchMaterialAdder(
            ItemList.DepletedRodThorium2.get(1),
            (int) Materials.Thorium.getProtons() / 10,
            (byte) 6);
        radioHatchMaterialAdder(
            ItemList.DepletedRodThorium4.get(1),
            (int) Materials.Thorium.getProtons() / 10,
            (byte) 12);
        radioHatchMaterialAdder(ItemList.RodNaquadah32.get(1), 140, (byte) 96);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah.get(1), 14, (byte) 3);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah2.get(1), 14, (byte) 6);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah4.get(1), 14, (byte) 12);
        radioHatchMaterialAdder(ItemList.DepletedRodMOX.get(1), (int) Materials.Plutonium.getProtons() / 10, (byte) 3);
        radioHatchMaterialAdder(ItemList.DepletedRodMOX2.get(1), (int) Materials.Plutonium.getProtons() / 10, (byte) 6);
        radioHatchMaterialAdder(
            ItemList.DepletedRodMOX4.get(1),
            (int) Materials.Plutonium.getProtons() / 10,
            (byte) 12);
        radioHatchMaterialAdder(
            ItemList.DepletedRodUranium.get(1),
            (int) Materials.Uranium.getProtons() / 10,
            (byte) 3);
        radioHatchMaterialAdder(
            ItemList.DepletedRodUranium2.get(1),
            (int) Materials.Uranium.getProtons() / 10,
            (byte) 6);
        radioHatchMaterialAdder(
            ItemList.DepletedRodUranium4.get(1),
            (int) Materials.Uranium.getProtons() / 10,
            (byte) 12);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium.get(1),
            (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons() / 10,
            (byte) 3);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium2.get(1),
            (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons() / 10,
            (byte) 6);
        radioHatchMaterialAdder(
            ItemList.DepletedRodTiberium4.get(1),
            (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                .getProtons() / 10,
            (byte) 12);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadah32.get(1), 13, (byte) 96);
        radioHatchMaterialAdder(ItemList.RodNaquadria.get(1), 150, (byte) 3);
        radioHatchMaterialAdder(ItemList.RodNaquadria2.get(1), 150, (byte) 6);
        radioHatchMaterialAdder(ItemList.RodNaquadria4.get(1), 150, (byte) 12);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadria.get(1), 15, (byte) 3);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadria2.get(1), 15, (byte) 6);
        radioHatchMaterialAdder(ItemList.DepletedRodNaquadria4.get(1), 15, (byte) 12);
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
