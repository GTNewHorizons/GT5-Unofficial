package tectech.loader.recipe;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import tectech.thing.CustomItemList;

public class AssemblyLine implements Runnable {

    @Override
    public void run() {
        // Data Bank
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Hatch_DataAccess_EV.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                CustomItemList.Machine_Multi_Switch_Adv.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                ItemList.Tool_DataOrb.get(1),
                ItemList.Cover_Screen.get(1))
            .fluidInputs(GTModHandler.getIC2Coolant(2_000), Materials.Hydrogen.getGas(1_000))
            .itemOutputs(CustomItemList.Machine_Multi_DataBank.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        // Hollow Casing
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.eM_Containment.get(1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                CustomItemList.eM_Containment.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 2),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Plutonium, 4),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Uranium, 16),
                GTOreDictUnificator
                    .get(OrePrefixes.screw, BaseRecipeLoader.getOrDefault("Quantium", Materials.Europium), 16))
            .fluidInputs(
                BaseRecipeLoader.getOrDefault("Trinium", Materials.Osmium)
                    .getMolten(9 * INGOTS),
                Materials.Osmium.getMolten(9 * INGOTS),
                GTModHandler.getIC2Coolant(2_000),
                Materials.Argon.getGas(1_000))
            .itemOutputs(CustomItemList.eM_Hollow.get(2))
            .eut(200_000)
            .duration(10 * SECONDS)
            .addTo(AssemblyLine);

        // EM Coil
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.eM_Hollow.get(1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                CustomItemList.eM_Hollow.get(1),
                ItemList.Casing_Fusion_Coil.get(2),
                ItemList.Casing_Coil_NaquadahAlloy.get(2),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Europium, 64))
            .fluidInputs(
                Materials.Glass.getMolten(16 * INGOTS),
                Materials.RubberSilicone.getMolten(1872),
                GTModHandler.getIC2Coolant(2_000),
                BaseRecipeLoader.getOrDefault("Trinium", Materials.Osmium)
                    .getMolten(9 * INGOTS))
            .itemOutputs(CustomItemList.eM_Coil.get(4))
            .eut(200_000)
            .duration(40 * SECONDS)
            .addTo(AssemblyLine);

        // Object Holder
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Hatch_Input_Bus_ZPM.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 20 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                CustomItemList.eM_Computer_Bus.get(1),
                ItemList.Emitter_ZPM.get(8),
                ItemList.Robot_Arm_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 2) },
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 16) },
                CustomItemList.DATApipe.get(2))
            .fluidInputs(
                Materials.UUMatter.getFluid(500),
                Materials.Iridium.getMolten(9 * INGOTS),
                GTModHandler.getIC2Coolant(1_000))
            .itemOutputs(CustomItemList.holder_Hatch.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);

        // Quantum Computer
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Tool_DataOrb.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_LuV))
            .itemInputs(
                CustomItemList.Machine_Multi_Switch_Adv.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 2 },
                ItemList.Tool_DataOrb.get(1),
                ItemList.Cover_Screen.get(1),
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8) },
                CustomItemList.DATApipe.get(8),
                new ItemStack[] {
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Polybenzimidazole, 2) })
            .fluidInputs(
                Materials.UUMatter.getFluid(1_000),
                Materials.Iridium.getMolten(9 * INGOTS),
                GTModHandler.getIC2Coolant(2_000),
                Materials.Hydrogen.getGas(1_000))
            .itemOutputs(CustomItemList.Machine_Multi_Computer.get(1))
            .eut(100_000)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Research Station
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.ScannerZPM.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                CustomItemList.Machine_Multi_Switch_Adv.get(1),
                ItemList.Sensor_ZPM.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                ItemList.Field_Generator_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 4) },
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 32) },
                CustomItemList.DATApipe.get(16))
            .fluidInputs(
                Materials.UUMatter.getFluid(1_000),
                Materials.Iridium.getMolten(9 * INGOTS),
                GTModHandler.getIC2Coolant(2_000),
                Materials.Osmium.getMolten(9 * INGOTS))
            .itemOutputs(CustomItemList.Machine_Multi_Research.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(3 * MINUTES)
            .addTo(AssemblyLine);

        // Multi Infuser
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.Machine_Multi_Transformer.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
            .itemInputs(
                CustomItemList.Machine_Multi_Transformer.get(1),
                CustomItemList.eM_Coil.get(8),
                CustomItemList.eM_Power.get(8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.NeodymiumMagnetic, 16))
            .fluidInputs(
                Materials.Electrum.getMolten(2592),
                Materials.Europium.getMolten(1872),
                GTModHandler.getIC2Coolant(2_000))
            .itemOutputs(CustomItemList.Machine_Multi_Infuser.get(1))
            .eut(200_000)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);
    }
}
