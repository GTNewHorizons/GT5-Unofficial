package goodgenerator.loader;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.PRECISE_ASSEMBLER_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import goodgenerator.util.MaterialFix;
import gregtech.api.enums.CircuitComponents;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.MaterialFacades;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.SubstituteFluidStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class RecipeLoader {

    public static void RecipeLoad() {

        // Radiation Protection Plate
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.plateDense, 8),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.plate, 8))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidMolten, 8 * INGOTS))
            .itemOutputs(ItemRefer.Radiation_Protection_Plate.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Lanthanum, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.plate, 8))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidMolten, 8 * INGOTS))
            .itemOutputs(ItemRefer.Radiation_Protection_Plate.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // LNR Controller - Large Naquadah Reactor
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemList.Generator_Naquadah_Mark_III.get(1)
                    .copy())
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Adamantium, 8),
                ItemRefer.Radiation_Protection_Plate.get(16),
                ItemList.Field_Generator_ZPM.get(2),
                ItemList.Electric_Pump_ZPM.get(8),
                new Object[] { Circuits.UV.getIngredient(), 4 },
                GTOreDictUnificator.get(OrePrefixes.wireGt08, MaterialFacades.SuperconductorLuV, 8),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Naquadah, 4),
                MaterialLibAPI.getStack(Materials.prismaticnaquadah, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.screw, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Trinium, FluidShapes.fluidMolten, 4 * INGOTS),
                MaterialUtils.anyFluid(Materials.Indalloy140, 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 8_000))
            .itemOutputs(ItemList.LargeNaquadahReactor.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // LNR Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Radiation_Protection_Plate.get(6),
                ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 1),
                ItemList.Field_Generator_MV.get(1))
            .circuit(1)
            .itemOutputs(ItemRefer.Field_Restriction_Casing.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // LNR Frame
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.prismaticnaquadah, Shapes.stickLong, 8),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSE, 4))
            .circuit(24)
            .itemOutputs(ItemRefer.Radiation_Proof_Steel_Frame_Box.get(1))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Uranium Liquid Fuel Process Line
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.GraphiteUraniumMixture, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.TungstenCarbide, Shapes.foil, 2))
            .circuit(1)
            .itemOutputs(ItemRefer.Wrapped_Uranium_Ingot.get(1))
            .duration(1 * MINUTES + 10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Wrapped_Uranium_Ingot.get(4))
            .itemOutputs(
                ItemRefer.High_Density_Uranium_Nugget.get(1),
                MaterialLibAPI.getStack(Materials.TungstenCarbide, Shapes.dustTiny, 8))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 8)
            .addTo(implosionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.High_Density_Uranium_Nugget.get(9))
            .itemOutputs(ItemRefer.High_Density_Uranium.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.High_Density_Uranium.get(1),
                MaterialLibAPI.getStack(Materials.Potassium, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Quantium, Shapes.dust, 4))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.UraniumBasedLiquidFuel, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.UraniumBasedLiquidFuel, FluidShapes.fluidLiquid, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 100))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.UraniumBasedLiquidFuelExcitedState, FluidShapes.fluidLiquid, (int) (10)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes);

        // Thorium Liquid Process Line
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.UraniumCarbideThoriumMixture, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.foil, 4))
            .circuit(1)
            .itemOutputs(ItemRefer.Wrapped_Thorium_Ingot.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Wrapped_Thorium_Ingot.get(1))
            .itemOutputs(
                ItemRefer.High_Density_Thorium_Nugget.get(1),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dustTiny, 8))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(implosionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.High_Density_Thorium_Nugget.get(9))
            .itemOutputs(ItemRefer.High_Density_Thorium.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.High_Density_Thorium.get(1),
                MaterialLibAPI.getStack(Materials.Lithium, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Draconium, Shapes.dust, 2))
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.ThoriumBasedLiquidFuel, FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ThoriumBasedLiquidFuel, CellShapes.cell, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 250))
            .itemOutputs(MaterialLibAPI.getStack(Materials.ThoriumBasedLiquidFuelExcitedState, CellShapes.cell, 1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(mixerRecipes);

        // Liquid Plutonium Process Line
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PlutoniumOxideUraniumMixture, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.HSSS, Shapes.foil, 4))
            .circuit(1)
            .itemOutputs(ItemRefer.Wrapped_Plutonium_Ingot.get(1))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Wrapped_Plutonium_Ingot.get(2))
            .itemOutputs(
                ItemRefer.High_Density_Plutonium_Nugget.get(1),
                MaterialLibAPI.getStack(Materials.HSSS, Shapes.dustTiny, 8))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 16)
            .addTo(implosionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.High_Density_Plutonium_Nugget.get(9))
            .itemOutputs(ItemRefer.High_Density_Plutonium.get(1))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.High_Density_Plutonium.get(1),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Caesium, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 2))
            .circuit(1)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PlutoniumBasedLiquidFuel, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Lutetium, FluidShapes.fluidMolten, 1 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials.PlutoniumBasedLiquidFuel, FluidShapes.fluidLiquid, (int) (20)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PlutoniumBasedLiquidFuelExcitedState, FluidShapes.fluidLiquid, (int) (20)))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LuV / 2)
            .metadata(FUSION_THRESHOLD, 220_000_000L)
            .addTo(fusionRecipes);

        // Th-232
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 24),
                MaterialLibAPI.getStack(Materials.VanadiumPentoxide, Shapes.dust, 0))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 6_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.OxalicAcid, FluidShapes.fluidLiquid, (int) (3_000)),
                MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 6_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        // Th + 2O = ThO2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Thorianite, Shapes.dust, 3))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        // Th + 8HNO3 =HF= Th(NO3)4 + 4NO2 + 4H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 8_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.ThoriumNitrate, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 4_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        // 4NaOH + Th(NO3)4 = Th(OH)4 + 4NaNO3
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 12))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ThoriumNitrate, FluidShapes.fluidLiquid, (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ThoriumHydroxide, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 20))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // 2 NaNO3 + H2SO4 = Na2SO4 + 2HNO3
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 10))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 2_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumSulfate, Shapes.dust, 7))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Th(OH)4 + 4HF = ThF4 + 4H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ThoriumHydroxide, Shapes.dust, 9))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.ThoriumTetrafluoride, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zn + 2Cl = ZnCl2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.ZincChloride, Shapes.dust, 3))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // ZnCl2 + 3Ca + ThF4 = ZnTh + CaCl2 + 2CaF2
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ZincChloride, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Thorium232Tetrafluoride, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CalciumChloride, FluidShapes.fluidLiquid, (int) (3_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ZnThAlloy, Shapes.ingot, 1),
                MaterialLibAPI.getStack(Materials.Fluorspar, Shapes.dust, 6))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ZnThAlloy, Shapes.ingot, 1))
            .circuit(11)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Zinc, FluidShapes.fluidMolten, 1 * INGOTS))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Thorium232, Shapes.dust, 1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1900)
            .metadata(ADDITIVE_AMOUNT, 250)
            .addTo(BlastFurnaceWithGas);

        // 2V + 5O = V2O5
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 2))
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 5_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.VanadiumPentoxide, Shapes.dust, 7))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 2500)
            .addTo(blastFurnaceRecipes);

        // Atomic Separation Catalyst
        ItemStack[] mat1 = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 32),
            MaterialLibAPI.getStack(Materials.Firestone, Shapes.dust, 4) };
        ItemStack[] mat2 = new ItemStack[] { MaterialLibAPI.getStack(Materials.Draconium, Shapes.dust, 4),
            MaterialLibAPI.getStack(Materials.Quantium, Shapes.dust, 4), };
        ItemStack[] mat3 = new ItemStack[] { MaterialLibAPI.getStack(Materials.Ardite, Shapes.dust, 4),
            MaterialLibAPI.getStack(Materials.Manyullyn, Shapes.dust, 4) };

        for (ItemStack m1 : mat1) {
            for (ItemStack m2 : mat2) {
                for (ItemStack m3 : mat3) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(m1, m2, m3)
                        .circuit(4)
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(Materials.Naquadah, FluidShapes.fluidMolten, 2 * INGOTS))
                        .itemOutputs(ItemRefer.Raw_Atomic_Separation_Catalyst.get(63))
                        .duration(15 * SECONDS)
                        .eut(TierEU.RECIPE_HV)
                        .addTo(mixerRecipes);
                }
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.plate, 1),
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.plate, 8))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Orundum, Shapes.plate, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Orundum, Shapes.plate, 2),
                ItemRefer.Raw_Atomic_Separation_Catalyst.get(4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plutonium, FluidShapes.fluidMolten, 1 * INGOTS))
            .itemOutputs(MaterialLibAPI.getStack(Materials.AtomicSeparationCatalyst, Shapes.ingotHot, 1))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 5000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AtomicSeparationCatalyst, Shapes.ingotHot, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.AtomicSeparationCatalyst, Shapes.ingot, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(vacuumFreezerRecipes);

        CrackRecipeAdder.crackerAdder(
            MaterialLibAPI.getFluidStack(Materials.NaquadahGas, FluidShapes.fluidLiquid, (int) (1_000)),
            MaterialLibAPI.getFluidStack(Materials.AtomicSeparationCatalyst, FluidShapes.fluidMolten, (int) (4)),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 300),
                MaterialLibAPI.getFluidStack(Materials.Neon, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Argon, FluidShapes.fluidGas, 80),
                MaterialLibAPI.getFluidStack(Materials.Krypton, FluidShapes.fluidLiquid, (int) (20)),
                MaterialLibAPI.getFluidStack(Materials.Xenon, FluidShapes.fluidLiquid, (int) (40)),
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 14_000) },
            MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dustTiny, 1),
            6,
            4020,
            300);

        CrackRecipeAdder.crackerAdder(
            MaterialLibAPI.getFluidStack(Materials.LightNaquadahFuel, FluidShapes.fluidLiquid, (int) (1_000)),
            MaterialLibAPI.getFluidStack(Materials.AtomicSeparationCatalyst, FluidShapes.fluidMolten, (int) (4)),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 1_400),
                MaterialLibAPI.getFluidStack(Materials.NaquadahGas, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Uranium, FluidShapes.fluidMolten, 9 * HALF_INGOTS),
                MaterialLibAPI.getFluidStack(Materials.HeavyNaquadahFuel, FluidShapes.fluidLiquid, (int) (280)),
                MaterialLibAPI.getFluidStack(Materials.Plutonium, FluidShapes.fluidMolten, 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.NaquadahAsphalt, FluidShapes.fluidLiquid, (int) (140)) },
            MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dustTiny, 1),
            6,
            4020,
            450);

        CrackRecipeAdder.crackerAdder(
            MaterialLibAPI.getFluidStack(Materials.HeavyNaquadahFuel, FluidShapes.fluidLiquid, (int) (1_000)),
            MaterialLibAPI.getFluidStack(Materials.AtomicSeparationCatalyst, FluidShapes.fluidMolten, (int) (6)),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.NaquadahGas, FluidShapes.fluidLiquid, (int) (450)),
                MaterialLibAPI.getFluidStack(Materials.LightNaquadahFuel, FluidShapes.fluidLiquid, (int) (560)),
                MaterialLibAPI.getFluidStack(Materials.Uranium, FluidShapes.fluidMolten, 5 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lutetium, FluidShapes.fluidMolten, 9 * HALF_INGOTS),
                MaterialLibAPI.getFluidStack(Materials.NaquadahAsphalt, FluidShapes.fluidLiquid, (int) (240)) },
            MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dustTiny, 1),
            6,
            4020,
            500);

        CrackRecipeAdder.crackerAdder(
            MaterialLibAPI.getFluidStack(Materials.NaquadahAsphalt, FluidShapes.fluidLiquid, (int) (1_000)),
            MaterialLibAPI.getFluidStack(Materials.AtomicSeparationCatalyst, FluidShapes.fluidMolten, (int) (12)),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.LightNaquadahFuel, FluidShapes.fluidLiquid, (int) (600)),
                MaterialLibAPI.getFluidStack(Materials.Uranium, FluidShapes.fluidMolten, 8 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Thorium, FluidShapes.fluidMolten, 6 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Plutonium, FluidShapes.fluidMolten, 11 * HALF_INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Thulium, FluidShapes.fluidMolten, 216),
                MaterialLibAPI.getFluidStack(Materials.HeavyNaquadahFuel, FluidShapes.fluidLiquid, (int) (350)) },
            MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dustTiny, 1),
            6,
            4020,
            800);

        // 2C2H6O =H2SO4= C4H10O + H2O
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ether, FluidShapes.fluidLiquid, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_500))
            .duration(25 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RawGasoline, CellShapes.cell, 9),
                MaterialLibAPI.getStack(Materials.Ethanol, CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.EthanolGasoline, CellShapes.cell, 10))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ether, CellShapes.cell, 1))
            .metadata(FUEL_VALUE, 537)
            .metadata(FUEL_TYPE, 0)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ether, CellShapes.cell, 1))
            .metadata(FUEL_VALUE, 537)
            .metadata(FUEL_TYPE, 1)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.EthanolGasoline, CellShapes.cell, 1))
            .metadata(FUEL_VALUE, 1100)
            .metadata(FUEL_TYPE, 0)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Cyclopentadiene, CellShapes.cell, 1))
            .metadata(FUEL_VALUE, 70)
            .metadata(FUEL_TYPE, 1)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.JetFuelA, CellShapes.cell, 1))
            .metadata(FUEL_VALUE, 2248)
            .metadata(FUEL_TYPE, 0)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.JetFuelNo3, CellShapes.cell, 1))
            .metadata(FUEL_VALUE, 1824)
            .metadata(FUEL_TYPE, 0)
            .addTo(GTRecipeConstants.Fuel);

        // Sb + 3Cl = SbCl3
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Antimony, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ether, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.AntimonyTrichlorideSolution, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // SbCl3 + 2Cl = SbCl5
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.AntimonyTrichlorideSolution, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.AntimonyPentachlorideSolution, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            MaterialLibAPI
                .getFluidStack(Materials.AntimonyPentachlorideSolution, FluidShapes.fluidLiquid, (int) (1_000)),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ether, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.AntimonyPentachloride, FluidShapes.fluidLiquid, (int) (1_000)) },
            GTValues.NI,
            30 * SECONDS,
            TierEU.RECIPE_MV);

        // SbCl5 + 5HF = SbF5 + 5HCl
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AntimonyPentachloride, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 5_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.AntimonyPentafluoride, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 5_000))
            .duration(21 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // SbH5 + HF = HSbF6
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AntimonyPentafluoride, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.FluoroantimonicAcid, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(42 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, 16))
            .circuit(16)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.AcidNaquadahEmulsion, FluidShapes.fluidLiquid, (int) (2_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.RadioactiveSludge, Shapes.dust, 3))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 8))
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AcidNaquadahEmulsion, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NaquadahEmulsion, FluidShapes.fluidLiquid, (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.AntimonyTrioxide, Shapes.dustSmall, 1),
                MaterialLibAPI.getStack(Materials.Fluorspar, Shapes.dust, 4))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.NaquadahSolution, FluidShapes.fluidLiquid, (int) (20)),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.NaquadahAsphalt, FluidShapes.fluidLiquid, (int) (2)),
                MaterialLibAPI.getFluidStack(Materials.HeavyNaquadahFuel, FluidShapes.fluidLiquid, (int) (5)),
                MaterialLibAPI.getFluidStack(Materials.LightNaquadahFuel, FluidShapes.fluidLiquid, (int) (10)),
                FluidRegistry.getFluidStack("water", 10),
                MaterialLibAPI.getFluidStack(Materials.NaquadahGas, FluidShapes.fluidLiquid, (int) (60)) },
            GTValues.NI,
            1 * SECONDS,
            TierEU.RECIPE_EV);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NaquadahGas, CellShapes.cell, 1))
            .metadata(FUEL_VALUE, 1024)
            .metadata(FUEL_TYPE, 1)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.LightNaquadahFuel, FluidShapes.fluidLiquid, (int) (780)),
                MaterialLibAPI.getFluidStack(Materials.HeavyNaquadahFuel, FluidShapes.fluidLiquid, (int) (360)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkI, FluidShapes.fluidLiquid, (int) (100)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 320_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NetherStar, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.dust, 32))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkI, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.NaquadahGas, FluidShapes.fluidLiquid, (int) (1_500)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkII, FluidShapes.fluidLiquid, (int) (100)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_UHV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 32))
            .circuit(16)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.FluoroantimonicAcid, FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.AcidNaquadahEmulsion, FluidShapes.fluidLiquid, (int) (8_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.ExtremelyUnstableNaquadah, Shapes.dust, 17))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_IV / 2)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        // Naquadah Fuel Refinery
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemList.Generator_Naquadah_Mark_V.get(1)
                    .copy())
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 8),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(64),
                ItemList.Field_Generator_UV.get(8),
                ItemList.Electric_Pump_UHV.get(2),
                new Object[] { Circuits.UEV.getIngredient(), 4 },
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.MysteriousCrystal, 8),
                ItemList.Circuit_Wafer_NPIC.get(16),
                ItemList.UHV_Coil.get(64),
                new Object[] { "craftingLensYellow", 16 },
                MaterialLibAPI.getStack(Materials.Thulium, Shapes.screw, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Quantium, FluidShapes.fluidMolten, 1 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidMolten, 32 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.ExtremelyUnstableNaquadah, FluidShapes.fluidMolten, (int) (10 * INGOTS)),
                MaterialUtils.anyFluid(Materials.Indalloy140, 1 * STACKS + 36 * INGOTS))
            .itemOutputs(ItemList.NaquadahFuelRefinery.get(1))
            .eut(TierEU.RECIPE_UHV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Advanced Radiation Proof Plate
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Radiation_Protection_Plate.get(1))
            .metadata(SCANNING, new Scanning(40 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                ItemRefer.Radiation_Protection_Plate.get(2),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Trinium, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.VibrantAlloy, Shapes.plate, 4),
                ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(4))
            .fluidInputs(MaterialUtils.anyFluid(Materials.Indalloy140, 8 * INGOTS))
            .itemOutputs(ItemRefer.Advanced_Radiation_Protection_Plate.get(1))
            .eut(TierEU.RECIPE_ZPM / 2)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        // Naquadah Fuel Refinery Casing
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Casing.get(1))
            .metadata(SCANNING, new Scanning(30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Thulium, 1),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(6),
                ItemList.Field_Generator_IV.get(2),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 16),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Manyullyn, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Orundum, Shapes.plate, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.TungstenSteel, FluidShapes.fluidMolten, 8 * INGOTS),
                MaterialUtils.anyFluid(Materials.Indalloy140, 16 * INGOTS))
            .itemOutputs(ItemRefer.Naquadah_Fuel_Refinery_Casing.get(1))
            .eut(TierEU.RECIPE_ZPM / 2)
            .duration(25 * SECONDS)
            .addTo(AssemblyLine);

        if (NewHorizonsCoreMod.isModLoaded()) {

            // Field Restriction Coil
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, MaterialLibAPI.getStack(Materials.ExtremelyUnstableNaquadah, Shapes.ingot, 1))
                .metadata(SCANNING, new Scanning(40 * SECONDS, TierEU.RECIPE_ZPM))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmium, 1),
                    ItemList.Field_Generator_UV.get(2),
                    ItemList.Electric_Pump_UV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, MaterialFacades.SuperconductorZPM, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, MaterialFacades.SuperconductorZPM, 64),
                    MaterialLibAPI.getStack(Materials.Americium, Shapes.plateDense, 8),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.BlackPlutonium, 16),
                    ItemList.Circuit_Wafer_PPIC.get(32),
                    new Object[] { Circuits.UHV.getIngredient(), 1L })
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.Krypton, FluidShapes.fluidLiquid, (int) (1_000)),
                    MaterialLibAPI.getFluidStack(Materials.ElectrumFlux, FluidShapes.fluidMolten, 1 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 128_000))
                .itemOutputs(ItemRefer.Field_Restriction_Coil_T1.get(1))
                .eut(TierEU.RECIPE_ZPM)
                .duration(60 * SECONDS)
                .addTo(AssemblyLine);

            // Advanced Field Restriction Coil
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Coil_T1.get(1))
                .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_UV))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                    ItemList.Field_Generator_UHV.get(2),
                    ItemList.Electric_Pump_UHV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, MaterialFacades.SuperconductorUV, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, MaterialFacades.SuperconductorUV, 64),
                    MaterialLibAPI.getStack(Materials.Infinity, Shapes.plateDense, 8),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 16),
                    ItemList.Circuit_Wafer_PPIC.get(48),
                    new Object[] { Circuits.UEV.getIngredient(), 1L })
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidMolten, 1 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 128_000))
                .itemOutputs(ItemRefer.Field_Restriction_Coil_T2.get(1))
                .eut(TierEU.RECIPE_UV)
                .duration(60 * SECONDS)
                .addTo(AssemblyLine);

            // Ultimate Field Restriction Coil
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Coil_T2.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_UHV))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                    ItemList.Field_Generator_UEV.get(2),
                    ItemList.Electric_Pump_UEV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorUHV, 64),
                    MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.plateDense, 8),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Infinity, 16),
                    ItemList.Circuit_Wafer_PPIC.get(64),
                    new Object[] { Circuits.UIV.getIngredient(), 1L })
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, (int) (1_000)),
                    MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, 1 * STACKS),
                    MaterialLibAPI
                        .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 64_000))
                .itemOutputs(ItemRefer.Field_Restriction_Coil_T3.get(1))
                .eut(TierEU.RECIPE_UHV)
                .duration(60 * SECONDS)
                .addTo(AssemblyLine);

            // Temporal Field Restriction Coil
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Coil_T3.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES + 10 * SECONDS, TierEU.RECIPE_UEV))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1),
                    ItemList.Field_Generator_UIV.get(2),
                    ItemList.Electric_Pump_UIV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorUEV, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorUEV, 64),
                    MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.plateDense, 8),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.SpaceTime, 16),
                    ItemList.Circuit_Wafer_PPIC.get(64),
                    new Object[] { Circuits.UXV.getIngredient(), 1L })
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, (int) (1_000)),
                    MaterialLibAPI.getFluidStack(Materials.TranscendentMetal, FluidShapes.fluidMolten, 1 * STACKS),
                    MaterialLibAPI
                        .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 64_000))
                .itemOutputs(ItemRefer.Field_Restriction_Coil_T4.get(1))
                .eut(TierEU.RECIPE_UEV)
                .duration(60 * SECONDS)
                .addTo(AssemblyLine);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BorosilicateGlass, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.ReinforcedGlass, 6),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.ring, 32),
                ItemList.Field_Generator_HV.get(4))
            .circuit(6)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 2 * INGOTS))
            .itemOutputs(ItemRefer.Field_Restriction_Glass.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Ca + O = CaO
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 2))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // AlN = Al + N
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Aluminum_Nitride_Dust.get(4),
                MaterialLibAPI.getStack(Materials.YttriumOxide, Shapes.dust, 5))
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AdvancedGlue, FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(ItemRefer.Special_Ceramics_Dust.get(9))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Aluminum_Nitride_Dust.get(4),
                MaterialLibAPI.getStack(Materials.Uraninite, Shapes.dust, 5))
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AdvancedGlue, FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(ItemRefer.Special_Ceramics_Dust.get(9))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Special_Ceramics_Dust.get(2), ItemList.Shape_Extruder_Plate.get(0L))
            .itemOutputs(ItemRefer.Special_Ceramics_Plate.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(extruderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Special_Ceramics_Dust.get(2), ItemList.Shape_Mold_Casing.get(0))
            .itemOutputs(ItemRefer.Special_Ceramics_Plate.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(formingPressRecipes);

        // Advanced Recipe with netherite
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.plate, 1),
                ItemList.Shape_Mold_Casing.get(0))
            .itemOutputs(ItemRefer.Special_Ceramics_Plate.get(32))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(formingPressRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Raw_Cylinder.get(1),
            GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "PFP", "PPP", 'P', ItemRefer.Special_Ceramics_Plate.get(1), 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Raw_Cylinder.get(1),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.gearGt, 1),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.plate, 6))
            .circuit(4)
            .itemOutputs(ItemRefer.Titanium_Plated_Cylinder.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 2))
            .circuit(16)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.LiquidOxygen, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 2))
            .circuit(16)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.LiquidOxygen, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 2))
            .circuit(16)
            .fluidInputs(
                FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 1_000),
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 2))
            .circuit(16)
            .fluidInputs(
                FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 1_000),
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.UniversalChemicalFuelEngine.get(1),
            GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "TZT", "ALB", "WGW", 'T', MaterialLibAPI.getStack(Materials.Titanium, Shapes.plateDense, 1),
                'Z', "circuitUltimate", 'A', ItemList.LargeCombustionEngine.get(1), 'B',
                ItemList.ExtremeCombustionEngine.get(1), 'L', ItemList.Hull_LuV, 'W',
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 1), 'G',
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.gearGt, 1), });

        // neutron activator
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PolyvinylChloride, Shapes.stick, 4),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Plastic, 4))
            .circuit(8)
            .fluidInputs(FluidRegistry.getFluidStack("dye.chemical.dyecyan", 1 * INGOTS))
            .itemOutputs(ItemRefer.Plastic_Case.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PolyvinylChloride, Shapes.stick, 4),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Plastic, 4))
            .circuit(8)
            .fluidInputs(FluidRegistry.getFluidStack("dye.watermixed.dyecyan", 1 * INGOTS))
            .itemOutputs(ItemRefer.Plastic_Case.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Plastic_Case.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "CDC", "PCP", 'P',
                MaterialLibAPI.getStack(Materials.PolyvinylChloride, Shapes.stick, 1), 'C',
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Plastic, 1), 'D', "dyeCyan" });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Coated_Basic.get(1),
                ItemList.Circuit_Chip_ULPIC.get(1),
                ItemList.ULV_Coil.get(2),
                ItemList.Battery_RE_ULV_Tantalum.get(1))
            .itemOutputs(ItemRefer.Micro_Heater.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.RedAlloy, FluidShapes.fluidMolten, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Aluminium, FluidShapes.fluidMolten, 1 * INGOTS))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Quartzite, Shapes.plate, 1),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 4))
            .fluidInputs(GTUtility.getWater(1_000))
            .itemOutputs(ItemRefer.Quartz_Wafer.get(1))
            .outputChances(3333)
            .requiresCleanRoom()
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Quartzite, Shapes.plate, 1),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 4))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .itemOutputs(ItemRefer.Quartz_Wafer.get(1))
            .requiresCleanRoom()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Quartz_Wafer.get(1),
                ItemRefer.Special_Ceramics_Plate.get(2),
                ItemRefer.Micro_Heater.get(1),
                ItemList.Circuit_Chip_ILC.get(4))
            .itemOutputs(ItemRefer.Quartz_Crystal_Resonator.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.EnergeticAlloy, FluidShapes.fluidMolten, 1 * HALF_INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Silver, FluidShapes.fluidMolten, 1 * EIGHTH_INGOTS))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Quartz_Crystal_Resonator.get(2),
                ItemRefer.Plastic_Case.get(1),
                Circuits.MV.get(1),
                ItemList.Cover_Screen.get(1),
                CircuitComponents.DIODE.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8))
            .fluidInputs(SubstituteFluidStack.soldering(1 * INGOTS))
            .itemOutputs(ItemRefer.Inverter.get(1))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Quartz_Crystal_Resonator.get(2),
                ItemRefer.Plastic_Case.get(1),
                Circuits.MV.get(1),
                ItemList.Cover_Screen.get(1),
                ItemList.Circuit_Parts_DiodeASMD.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8))
            .fluidInputs(SubstituteFluidStack.soldering(1 * INGOTS))
            .itemOutputs(ItemRefer.Inverter.get(1))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        if (NewHorizonsCoreMod.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedGoldChip", 16),
                    ItemList.Circuit_Chip_SoC2.get(8),
                    ItemList.Circuit_Chip_NOR.get(32),
                    MaterialLibAPI.getStack(Materials.Signalium, Shapes.bolt, 32),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8),
                    GTModHandler.getIC2Item("reactorVent", 1L, 1))
                .fluidInputs(SubstituteFluidStack.soldering(2 * INGOTS))
                .itemOutputs(ItemRefer.HiC_T1.get(1))
                .duration(1 * MINUTES)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedGoldChip", 16),
                    ItemList.Circuit_Chip_SoC2.get(8),
                    ItemList.Circuit_Chip_NOR.get(32),
                    MaterialLibAPI.getStack(Materials.Signalium, Shapes.bolt, 32),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8),
                    MaterialLibAPI.getStack(Materials.TinAlloy, Shapes.rotor, 1))
                .fluidInputs(SubstituteFluidStack.soldering(2 * INGOTS))
                .itemOutputs(ItemRefer.HiC_T1.get(1))
                .duration(1 * MINUTES)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);
        }

        // Neutron Accelerator ULV
        GTModHandler.addCraftingRecipe(
            Loaders.NeutronAccelerators[0].copy(),
            GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WPM", "CHI", "WPM", 'W', GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 1),
                'P', MaterialLibAPI.getStack(Materials.Lead, Shapes.plate, 1), 'M',
                MaterialLibAPI.getStack(Materials.Lead, Shapes.rotor, 1), 'C',
                MaterialLibAPI.getStack(Materials.Wood, Shapes.plate, 1), 'H', ItemList.Hull_ULV, 'I',
                ItemRefer.Inverter.get(1), });

        // Neutron Accelerator LV
        GTModHandler.addCraftingRecipe(
            Loaders.NeutronAccelerators[1].copy(),
            GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WPM", "CHI", "WPM", 'W',
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.RedAlloy, 1), 'P',
                MaterialLibAPI.getStack(Materials.Lead, Shapes.plateDouble, 1), 'M', ItemList.Electric_Motor_LV, 'C',
                "plateAnyRubber", 'H', ItemList.Hull_LV, 'I', ItemRefer.Inverter.get(1), });

        // Neutron Accelerator MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Inverter.get(1),
                ItemList.Hull_MV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, MaterialFacades.SuperconductorMV, 2),
                MaterialLibAPI.getStack(Materials.Plastic, Shapes.plate, 1),
                MaterialLibAPI.getStack(Materials.Beryllium, Shapes.plate, 2),
                ItemList.Electric_Motor_MV.get(2))
            .itemOutputs(Loaders.NeutronAccelerators[2].copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Neutron Accelerator HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Inverter.get(1),
                ItemList.Hull_HV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, MaterialFacades.SuperconductorHV, 2),
                MaterialLibAPI.getStack(Materials.PolyvinylChloride, Shapes.plate, 1),
                MaterialLibAPI.getStack(Materials.Beryllium, Shapes.plateDouble, 2),
                ItemList.Electric_Motor_HV.get(2))
            .itemOutputs(Loaders.NeutronAccelerators[3].copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Neutron Accelerator EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Inverter.get(1),
                ItemList.Hull_EV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, MaterialFacades.SuperconductorEV, 2),
                MaterialLibAPI.getStack(Materials.StyreneButadieneRubber, Shapes.plate, 1),
                MaterialLibAPI.getStack(Materials.IronMagnetic, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.TungstenCarbide, Shapes.plate, 2),
                ItemList.Electric_Motor_EV.get(2))
            .itemOutputs(Loaders.NeutronAccelerators[4].copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Neutron Accelerator IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Inverter.get(1),
                ItemList.Hull_IV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, MaterialFacades.SuperconductorIV, 2),
                MaterialLibAPI.getStack(Materials.Silicone, Shapes.plate, 1),
                MaterialLibAPI.getStack(Materials.SteelMagnetic, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.TungstenCarbide, Shapes.plateDouble, 2),
                ItemList.Electric_Motor_IV.get(2))
            .itemOutputs(Loaders.NeutronAccelerators[5].copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Neutron Accelerator LuV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[5].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
            .itemInputs(
                ItemRefer.Inverter.get(2),
                ItemList.Hull_LuV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorLuV, 4),
                MaterialLibAPI.getStack(Materials.NetherStar, Shapes.plate, 1),
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.NeodymiumMagnetic, Shapes.plate, 8),
                ItemList.Electric_Motor_LuV.get(2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Argon, FluidShapes.fluidGas, 3_000))
            .itemOutputs(Loaders.NeutronAccelerators[6].copy())
            .eut(TierEU.RECIPE_LuV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator ZPM
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[6].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemRefer.Inverter.get(2),
                ItemList.Hull_ZPM.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorZPM, 4),
                MaterialLibAPI.getStack(Materials.NetherStar, Shapes.plate, 1),
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.plateDouble, 4),
                MaterialLibAPI.getStack(Materials.SamariumMagnetic, Shapes.plate, 8),
                ItemList.Electric_Motor_ZPM.get(2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Xenon, FluidShapes.fluidLiquid, (int) (3_000)))
            .itemOutputs(Loaders.NeutronAccelerators[7].copy())
            .eut(TierEU.RECIPE_ZPM)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator UV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[7].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_ZPM))
            .itemInputs(
                ItemRefer.Inverter.get(4),
                ItemList.Hull_UV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorUV, 4),
                MaterialLibAPI.getStack(Materials.NetherStar, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.plateTriple, 4),
                ItemList.ZPM_Coil.get(8),
                MaterialLibAPI.getStack(Materials.NickelZincFerrite, Shapes.stickLong, 16),
                ItemList.Electric_Motor_UV.get(2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, (int) (3_000)))
            .itemOutputs(Loaders.NeutronAccelerators[8].copy())
            .eut(TierEU.RECIPE_UV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator UHV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[8].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_UV))
            .itemInputs(
                ItemRefer.Inverter.get(8),
                ItemList.Hull_MAX.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorUHV, 4),
                MaterialLibAPI.getStack(Materials.Orundum, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.Kevlar, Shapes.plateTriple, 8),
                ItemList.UV_Coil.get(8),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.stickLong, 16),
                ItemList.Electric_Motor_UHV.get(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, (int) (12_000)),
                MaterialLibAPI.getFluidStack(Materials.Grade5PurifiedWater, FluidShapes.fluidLiquid, 16_000))
            .itemOutputs(Loaders.NeutronAccelerators[9].copy())
            .eut(TierEU.RECIPE_UHV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator UEV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[9].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_UHV))
            .itemInputs(
                ItemRefer.Inverter.get(16),
                ItemList.Hull_UEV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorUEV, 4),
                MaterialLibAPI.getStack(Materials.Orundum, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Kevlar, Shapes.plateDense, 16),
                ItemList.UHV_Coil.get(8),
                MaterialLibAPI.getStack(Materials.TengamAttuned, Shapes.stickLong, 16),
                ItemList.Electric_Motor_UEV.get(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, (int) (3 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Grade6PurifiedWater, FluidShapes.fluidLiquid, 16_000))
            .itemOutputs(Loaders.NeutronAccelerators[10].copy())
            .eut(TierEU.RECIPE_UEV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator UIV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[10].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_UEV))
            .itemInputs(
                ItemRefer.Inverter.get(32),
                ItemList.Hull_UIV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorUIV, 4),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.RadoxPoly, Shapes.plateDouble, 16),
                ItemList.UEV_Coil.get(8),
                ItemList.Tesseract.get(8),
                ItemList.Electromagnet_Tengam.get(1),
                ItemList.Electric_Motor_UIV.get(2),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.stickLong, 16))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, (int) (12 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 4_000),
                MaterialLibAPI.getFluidStack(Materials.Grade7PurifiedWater, FluidShapes.fluidLiquid, 16_000))
            .itemOutputs(Loaders.NeutronAccelerators[11].copy())
            .eut(TierEU.RECIPE_UIV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator UMV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[11].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_UIV))
            .itemInputs(
                ItemRefer.Inverter.get(64),
                ItemList.Hull_UMV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorUMV, 4),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.plate, 32),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.RadoxPoly, 1),
                ItemList.UIV_Coil.get(8),
                ItemList.EnergisedTesseract.get(12),
                ItemList.Electromagnet_Tengam.get(1),
                ItemList.Electric_Motor_UMV.get(2),
                MaterialLibAPI.getStack(Materials.Creon, Shapes.stickLong, 16),
                MaterialLibAPI.getStack(Materials.Mellion, Shapes.stickLong, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Shirabon, FluidShapes.fluidMolten, (int) (3 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 8_000),
                MaterialLibAPI.getFluidStack(Materials.Grade8PurifiedWater, FluidShapes.fluidLiquid, 16_000))
            .itemOutputs(Loaders.NeutronAccelerators[12].copy())
            .eut(TierEU.RECIPE_UMV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_IV.get(1L),
                ItemList.Cover_ActivityDetector.get(1L),
                ItemList.Cover_Screen.get(1L),
                MaterialLibAPI.getStack(Materials.VibrantAlloy, Shapes.plate, 4),
                Circuits.EV.get(1),
                ItemList.Sensor_HV.get(2))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 1_000))
            .itemOutputs(Loaders.NS.copy())
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Neutron_Source.get(1),
            GTModHandler.RecipeBits.REVERSIBLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { " P ", "PUP", " P ", 'P', MaterialLibAPI.getStack(Materials.Steel, Shapes.plateDense, 1),
                'U', ItemRefer.High_Density_Uranium.get(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.HiC_T2.get(2), ItemList.Emitter_EV.get(2), ItemRefer.Neutron_Source.get(1))
            .itemOutputs(Loaders.NA.copy())
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.StainlessSteel, FluidShapes.fluidMolten, 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.TungstenCarbide, FluidShapes.fluidMolten, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);

        // Fridge Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_FrostProof.get(4),
                MaterialLibAPI.getStack(Materials.Snow, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Ledox, Shapes.itemCasing, 2),
                MaterialLibAPI.getStack(Materials.CallistoIce, Shapes.gearGt, 2))
            .itemOutputs(ItemList.CasingFridge.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 4000),
                new FluidStack(TFFluids.fluidCryotheum, 1_000),
                MaterialLibAPI.getFluidStack(Materials.TungstenSteel, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);

        // Hearth Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_HeatProof.get(4),
                MaterialLibAPI.getStack(Materials.DarkAsh, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.itemCasing, 1),
                GTModHandler.getIC2Item("reactorVentDiamond", 1L, 1))
            .itemOutputs(ItemList.CasingHearth.get(4))
            .fluidInputs(
                new FluidStack(GTPPFluids.Pyrotheum, 1_000),
                MaterialLibAPI.getFluidStack(Materials.TungstenCarbide, FluidShapes.fluidMolten, 2 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.prismaticnaquadah, FluidShapes.fluidMolten, 72))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);
    }

    public static void InitLoadRecipe() {

        // Al2O3 + 2N + 3C = 2AlN + 3CO
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sapphire, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.LiquidNitrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 3_000))
            .itemOutputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4600)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.GreenSapphire, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.LiquidNitrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 3_000))
            .itemOutputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4600)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.LiquidNitrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 3_000))
            .itemOutputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4600)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ExtremelyUnstableNaquadah, Shapes.ingotHot, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.ExtremelyUnstableNaquadah, Shapes.ingot, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NaquadahBasedLiquidFuelMkVIDepleted,
                    FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1))
            .outputChances(1000, 500, 400, 50, 20, 5)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, (int) (122)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NaquadahBasedLiquidFuelMkVDepleted,
                    FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1))
            .outputChances(1000, 500, 400, 50, 20, 5)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, (int) (182)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NaquadahBasedLiquidFuelMkIVDepleted,
                    FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Sunnarium, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Sunnarium, Shapes.dust, 32))
            .outputChances(9900, 9500, 9000, 8000, 5000, 3000)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, (int) (6 * INGOTS)))
            .duration(2 * MINUTES + 5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NaquadahBasedLiquidFuelMkIIIDepleted,
                    FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Bedrockium, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Bedrockium, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Bedrockium, Shapes.dust, 32))
            .outputChances(9500, 9000, 8000, 7000, 5000, 4000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, (int) (720)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NaquadahBasedLiquidFuelMkIIDepleted,
                    FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Americium, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Californium, Shapes.dust, 32))
            .outputChances(9000, 8500, 5000, 4000, 2000)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, (int) (1 * INGOTS)))
            .duration(6 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.UraniumBasedLiquidFuelDepleted, FluidShapes.fluidLiquid, (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Lead, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.Bismuth, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Barium, Shapes.dust, 6))
            .outputChances(6000, 1000, 5000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Xenon, FluidShapes.fluidLiquid, (int) (10)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.ThoriumBasedLiquidFuelDepleted, FluidShapes.fluidLiquid, (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Thorium232, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Thorium232, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.Praseodymium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Praseodymium, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Boron, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, 4))
            .outputChances(10000, 8000, 10000, 8000, 3000, 5000)
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PlutoniumBasedLiquidFuelDepleted, FluidShapes.fluidLiquid, (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 2))
            .outputChances(5000, 8000, 7500)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Krypton, FluidShapes.fluidLiquid, (int) (1 * INGOTS)))
            .duration(2 * MINUTES + 5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(MaterialUtils.anyFluid(Materials.ThoriumTetrafluoride, 4_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Thorium232Tetrafluoride, FluidShapes.fluidLiquid, (int) (3_000)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NaquadahEmulsion, FluidShapes.fluidLiquid, (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RadioactiveSludge, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.RadioactiveSludge, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.RadioactiveSludge, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RadioactiveSludge, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RadioactiveSludge, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RadioactiveSludge, Shapes.dust, 1))
            .outputChances(8000, 7500, 5000, 2000, 500, 100)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NaquadahSolution, FluidShapes.fluidLiquid, (int) (500)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NaquadahBasedLiquidFuelMkIDepleted,
                    FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 48),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Neodymium, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Europium, Shapes.dust, 32))
            .outputChances(9000, 8500, 5000, 4000, 2000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Xenon, FluidShapes.fluidLiquid, (int) (1 * INGOTS)))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RadioactiveSludge, Shapes.dust, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.dust, 1))
            .outputChances(10000, 9500, 8000, 2500, 2000, 2000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 20))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);
    }

    public static void Fixer() {
        MaterialFix.MaterialFluidExtractionFix(Materials.AtomicSeparationCatalyst);
        MaterialFix.MaterialFluidExtractionFix(Materials.ExtremelyUnstableNaquadah);
        MaterialFix.MaterialFluidExtractionFix(Materials.MetastableOganesson);
        MaterialFix.MaterialFluidExtractionFix(Materials.Shirabon);
    }
}
