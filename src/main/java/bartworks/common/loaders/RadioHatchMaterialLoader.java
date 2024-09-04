package bartworks.common.loaders;

import static bartworks.API.recipe.BartWorksRecipeMaps.radioHatchRecipes;
import static bartworks.util.BWRecipes.calcDecayTicks;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.util.GTRecipeConstants.DECAY_TICKS;

import bartworks.system.material.BWNonMetaMaterialItems;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

// cursed way of using a recipe map...
public class RadioHatchMaterialLoader {

    public static void run() {

        for (Werkstoff material : Werkstoff.werkstoffHashSet) {
            if (material == null || !material.getStats()
                .isRadioactive()) {
                continue;
            }

            int level = (int) material.getStats()
                .getProtons();
            if (material.hasItemType(OrePrefixes.stick)) GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.stick))
                .duration(1)
                .eut(level)
                .metadata(DECAY_TICKS, (int) calcDecayTicks(level))
                .noOptimize()
                .addTo(radioHatchRecipes);
            if (material.hasItemType(OrePrefixes.stickLong)) GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.stickLong))
                .duration(2)
                .eut(level)
                .metadata(DECAY_TICKS, (int) calcDecayTicks(level))
                .noOptimize()
                .addTo(radioHatchRecipes);
        }

        for (Materials material : Materials.getAll()) {
            if (material == null || material.mElement == null) continue;
            boolean validProton = material.getProtons() >= 83 && !material.equals(Materials.Tritanium) // No Tritanium
                && !material.equals(Materials.Naquadah); // Naquadah needs spacial value
            if (validProton) {
                int level = (int) material.getProtons();
                if (GTOreDictUnificator.get(OrePrefixes.stick, material, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, material, 1))
                        .duration(1)
                        .eut(level)
                        .metadata(DECAY_TICKS, (int) calcDecayTicks(level))
                        .noOptimize()
                        .addTo(radioHatchRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1))
                        .duration(2)
                        .eut(level)
                        .metadata(DECAY_TICKS, (int) calcDecayTicks(level))
                        .noOptimize()
                        .addTo(radioHatchRecipes);
                }
            }
        }

        Materials[] spacialMaterial = { Materials.Naquadah, Materials.NaquadahEnriched, Materials.Naquadria };
        int[] spacialValue = { 130, 140, 150 };

        for (int i = 0; i < spacialMaterial.length; i++) {
            if (GTOreDictUnificator.get(OrePrefixes.stick, spacialMaterial[i], 1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, spacialMaterial[i], 1))
                    .duration(1)
                    .eut(spacialValue[i])
                    .metadata(DECAY_TICKS, (int) calcDecayTicks(spacialValue[i]))
                    .noOptimize()
                    .addTo(radioHatchRecipes);
            }

            if (GTOreDictUnificator.get(OrePrefixes.stickLong, spacialMaterial[i], 1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.stickLong, spacialMaterial[i], 1))
                    .duration(2)
                    .eut(spacialValue[i])
                    .metadata(DECAY_TICKS, (int) calcDecayTicks(spacialValue[i]))
                    .noOptimize()
                    .addTo(radioHatchRecipes);
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ThoriumCell_1.get(1))
            .duration(3)
            .eut((int) Materials.Thorium.getProtons())
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Thorium.getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ThoriumCell_2.get(1))
            .duration(6)
            .eut((int) Materials.Thorium.getProtons())
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Thorium.getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ThoriumCell_4.get(1))
            .duration(12)
            .eut((int) Materials.Thorium.getProtons())
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Thorium.getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.NaquadahCell_1.get(1))
            .duration(3)
            .eut(140)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(140))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.NaquadahCell_2.get(1))
            .duration(6)
            .eut(140)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(140))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.NaquadahCell_4.get(1))
            .duration(12)
            .eut(140)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(140))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Moxcell_1.get(1))
            .duration(3)
            .eut((int) Materials.Plutonium.getProtons())
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Plutonium.getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Moxcell_2.get(1))
            .duration(6)
            .eut((int) Materials.Plutonium.getProtons())
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Plutonium.getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Moxcell_4.get(1))
            .duration(12)
            .eut((int) Materials.Plutonium.getProtons())
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Plutonium.getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Uraniumcell_1.get(1))
            .duration(3)
            .eut((int) Materials.Uranium.getProtons())
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Uranium.getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Uraniumcell_2.get(1))
            .duration(6)
            .eut((int) Materials.Uranium.getProtons())
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Uranium.getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Uraniumcell_4.get(1))
            .duration(12)
            .eut((int) Materials.Uranium.getProtons())
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Uranium.getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.TiberiumCell_1.get(1))
            .duration(3)
            .eut(
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                    .getProtons())
            .metadata(
                DECAY_TICKS,
                (int) calcDecayTicks(
                    (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                        .getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.TiberiumCell_2.get(1))
            .duration(6)
            .eut(
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                    .getProtons())
            .metadata(
                DECAY_TICKS,
                (int) calcDecayTicks(
                    (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                        .getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.TiberiumCell_4.get(1))
            .duration(12)
            .eut(
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                    .getProtons())
            .metadata(
                DECAY_TICKS,
                (int) calcDecayTicks(
                    (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                        .getProtons()))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.TheCoreCell.get(1))
            .duration(96)
            .eut(140)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(140))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Thorium_1.get(1))
            .duration(3)
            .eut((int) Materials.Thorium.getProtons() / 10)
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Thorium.getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Thorium_2.get(1))
            .duration(6)
            .eut((int) Materials.Thorium.getProtons() / 10)
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Thorium.getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Thorium_4.get(1))
            .duration(12)
            .eut((int) Materials.Thorium.getProtons() / 10)
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Thorium.getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Naquadah_1.get(1))
            .duration(3)
            .eut(14)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(14))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Naquadah_2.get(1))
            .duration(6)
            .eut(14)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(14))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_Naquadah_4.get(1))
            .duration(12)
            .eut(14)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(14))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(IndustrialCraft2.ID, "reactorMOXSimpledepleted", 1))
            .duration(3)
            .eut((int) Materials.Plutonium.getProtons() / 10)
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Plutonium.getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(IndustrialCraft2.ID, "reactorMOXDualdepleted", 1))
            .duration(6)
            .eut((int) Materials.Plutonium.getProtons() / 10)
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Plutonium.getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(IndustrialCraft2.ID, "reactorMOXQuaddepleted", 1))
            .duration(12)
            .eut((int) Materials.Plutonium.getProtons() / 10)
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Plutonium.getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(IndustrialCraft2.ID, "reactorUraniumSimpledepleted", 1))
            .duration(3)
            .eut((int) Materials.Uranium.getProtons() / 10)
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Uranium.getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(IndustrialCraft2.ID, "reactorUraniumDualdepleted", 1))
            .duration(6)
            .eut((int) Materials.Uranium.getProtons() / 10)
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Uranium.getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(IndustrialCraft2.ID, "reactorUraniumQuaddepleted", 1))
            .duration(12)
            .eut((int) Materials.Uranium.getProtons() / 10)
            .metadata(DECAY_TICKS, (int) calcDecayTicks((int) Materials.Uranium.getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.Depleted_Tiberium_1.get(1))
            .duration(3)
            .eut(
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                    .getProtons() / 10)
            .metadata(
                DECAY_TICKS,
                (int) calcDecayTicks(
                    (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                        .getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.Depleted_Tiberium_2.get(1))
            .duration(6)
            .eut(
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                    .getProtons() / 10)
            .metadata(
                DECAY_TICKS,
                (int) calcDecayTicks(
                    (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                        .getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.Depleted_Tiberium_4.get(1))
            .duration(12)
            .eut(
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                    .getProtons() / 10)
            .metadata(
                DECAY_TICKS,
                (int) calcDecayTicks(
                    (int) WerkstoffLoader.Tiberium.getBridgeMaterial()
                        .getProtons() / 10))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(BWNonMetaMaterialItems.Depleted_TheCoreCell.get(1))
            .duration(96)
            .eut(13)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(13))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.MNqCell_1.get(1))
            .duration(3)
            .eut(150)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(150))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.MNqCell_2.get(1))
            .duration(6)
            .eut(150)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(150))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.MNqCell_4.get(1))
            .duration(12)
            .eut(150)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(150))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_MNq_1.get(1))
            .duration(3)
            .eut(15)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(15))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_MNq_2.get(1))
            .duration(6)
            .eut(15)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(15))
            .addTo(radioHatchRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Depleted_MNq_4.get(1))
            .duration(12)
            .eut(15)
            .metadata(DECAY_TICKS, (int) calcDecayTicks(15))
            .addTo(radioHatchRecipes);

    }
}
