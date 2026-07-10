package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class Assembler implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0),
                MaterialLibAPI.getStack(Materials2Materials.Lapis, Materials2Shapes.shapePlate, (int) (9)),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2L))
            .circuit(17)
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1))
            .fluidInputs(GTModHandler.getIC2Coolant(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1), Materials.Lapis.getBlocks(8))
            .circuit(17)
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[1]))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder() // DEHP
            .itemInputs(
                ItemList.OilDrill4.get(1),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.ZPM, 8),
                GGMaterial.incoloy903.get(OrePrefixes.gearGt, 32),
                MaterialLibAPI.getStack(
                    Materials2Materials.Polytetrafluoroethylene,
                    Materials2Shapes.shapePlateDense,
                    (int) (16)),
                ItemList.Field_Generator_IV.get(1))
            .itemOutputs(ItemRegistry.dehp)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HSSE,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (32 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials2Materials.AnnealedCopper, Materials2Shapes.shapeWireFine, (int) (64L)))
            .circuit(17)
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[2], 1, 1))
            .fluidInputs(Materials.Polyethylene.getMolten(8 * INGOTS))
            .duration(1 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1L),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapePlate, (int) (1)),
                ItemList.Circuit_Board_Plastic.get(1L),
                ItemList.Battery_RE_LV_Lithium.get(1L))
            .itemOutputs(new ItemStack(ItemRegistry.CIRCUIT_PROGRAMMER))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SolderingAlloy,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MACHINE_HULLS[3].get(1),
                ItemList.Electric_Pump_HV.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 4),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 2))
            .itemOutputs(ItemList.Distillation_Tower.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_HV.get(64),
                MaterialLibAPI.getStack(Materials2Materials.LiquidAir, Materials2CellShapes.shapeCell, (int) (1)))
            .circuit(17)
            .itemOutputs(ItemRegistry.compressedHatch.copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Hatch_Output_HV.get(64))
            .circuit(17)
            .itemOutputs(ItemRegistry.giantOutputHatch.copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.shapePlateDense, (int) (6)),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1))
            .itemOutputs(ItemList.Casing_RadiationProof.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Concrete,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (9 * INGOTS)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_RadiationProof.get(1),
                ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(4),
                MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.shapeFoil, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.shapeScrew, (int) (24)))
            .itemOutputs(ItemList.Casing_AdvancedRadiationProof.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Lead,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (6 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }
}
