package kekztech.common.recipeLoaders;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import kekztech.Items;
import kekztech.common.Blocks;
import kekztech.common.TileEntities;
import kekztech.common.items.ErrorItem;
import kekztech.common.items.MetaItemCraftingComponent;

public class Assembler implements Runnable {

    @Override
    public void run() {
        // TFFT Casing

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.DarkSteel, 3),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.EnderPearl, 3))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(144))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // TFFT Multi Hatch

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1),
                ItemList.Cover_FluidStorageMonitor.get(1),
                ItemList.Field_Generator_LV.get(4),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Polytetrafluoroethylene, 25))
            .itemOutputs(TileEntities.tfftHatch.getStackForm(1))
            .fluidInputs(Materials.Plastic.getMolten(432))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // TFFTStorageField1

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 6),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Steel, 3),
                ItemList.FluidRegulator_LV.get(1))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 1))
            .fluidInputs(Materials.Glass.getMolten(144))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // TFFTStorageField2

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                ItemList.Casing_Tank_1.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.EnergeticSilver, 6),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Plastic, 3),
                ItemList.FluidRegulator_MV.get(1))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 2))
            .fluidInputs(Materials.Plastic.getMolten(288))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // TFFTStorageField3

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                ItemList.Casing_Tank_3.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.VividAlloy, 6),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.StainlessSteel, 3),
                ItemList.Field_Generator_LV.get(1),
                ItemList.FluidRegulator_HV.get(1))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 3))
            .fluidInputs(Materials.Plastic.getMolten(432))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // TFFTStorageField4

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                ItemList.Casing_Tank_5.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Enderium, 6),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Polytetrafluoroethylene, 3),
                ItemList.Field_Generator_MV.get(2),
                ItemList.FluidRegulator_EV.get(1))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 4))
            .fluidInputs(Materials.Epoxid.getMolten(864))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // TFFTStorageField5

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                ItemList.Casing_Tank_7.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CrystallineAlloy, 6),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Enderium, 3),
                ItemList.Field_Generator_HV.get(4),
                ItemList.FluidRegulator_IV.get(1))
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 5))
            .fluidInputs(Materials.Epoxid.getMolten(1152))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // LSC Casing

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Tantalum, 4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 2),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 2),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lapis, 1))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // EV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
                GTModHandler.getIC2Item("lapotronCrystal", 1L, GTValues.W),
                GTUtility.getIntegratedCircuit(7))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // IV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
                ItemList.Energy_LapotronicOrb.get(1L),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // LuV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Energy_LapotronicOrb2.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24),
                GTUtility.getIntegratedCircuit(6))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // ZPM Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Energy_Module.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24),
                GTUtility.getIntegratedCircuit(6))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // UV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Energy_Cluster.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24),
                GTUtility.getIntegratedCircuit(6))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        // UHV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.ZPM3.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24),
                GTUtility.getIntegratedCircuit(6))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5))
            .duration(5 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
        // UEV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.ZPM4.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24),
                GTUtility.getIntegratedCircuit(6))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8))
            .duration(10 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        // UIV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.ZPM5.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 24),
                GTUtility.getIntegratedCircuit(6))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9))
            .duration(10 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        // UMV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.ZPM6.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 24),
                GTUtility.getIntegratedCircuit(6))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10))
            .duration(10 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);

        final MetaItemCraftingComponent craftingItem = MetaItemCraftingComponent.getInstance();

        // YSZ Unit

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicPlate.getMetaID(), 4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Yttrium, 1),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1),
                ItemList.Electric_Motor_HV.get(1L))
            .itemOutputs(new ItemStack(Blocks.yszUnit, 1))
            .fluidInputs(Materials.Hydrogen.getGas(4000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // GDC Unit

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 8),
                GTOreDictUnificator
                    .get(OrePrefixes.frameGt, Materials.Gadolinium, new ItemStack(ErrorItem.getInstance(), 1), 1),
                GTOreDictUnificator
                    .get(OrePrefixes.rotor, Materials.Desh, new ItemStack(ErrorItem.getInstance(), 1), 1),
                ItemList.Electric_Motor_IV.get(1L))
            .itemOutputs(new ItemStack(Blocks.gdcUnit, 1))
            .fluidInputs(Materials.Hydrogen.getGas(16000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // Hex Tiles

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                GTOreDictUnificator.get(OrePrefixes.stone, Materials.Concrete, 1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.DarkSteel, 2))
            .itemOutputs(new ItemStack(Blocks.largeHexPlate, 2))
            .fluidInputs(FluidRegistry.getFluidStack("molten.plastic", 36))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

    }
}
