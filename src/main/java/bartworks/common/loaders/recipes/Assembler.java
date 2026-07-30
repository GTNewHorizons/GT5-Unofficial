package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.TieredItems;
import gregtech.api.enums.materials2.CellShapes;
import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class Assembler implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0),
                MaterialLibAPI.getStack(Materials.Lapis, Shapes.plate, (int) (9)),
                Circuits.HV.get(2))
            .circuit(17)
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1))
            .fluidInputs(GTModHandler.getIC2Coolant(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lapis, 8))
            .circuit(17)
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[1]))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder() // DEHP
            .itemInputs(
                ItemList.OilDrill4.get(1),
                TieredItems.ZPM.getPipeLarge(8),
                MaterialLibAPI.getStack(Materials.Incoloy903, Shapes.gearGt, 32),
                MaterialLibAPI.getStack(Materials.Polytetrafluoroethylene, Shapes.plateDense, (int) (16)),
                ItemList.Field_Generator_IV.get(1))
            .itemOutputs(ItemRegistry.dehp)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HSSE, FluidShapes.fluidMolten, (int) (32 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AnnealedCopper, Shapes.wireFine, (int) (64L)))
            .circuit(17)
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[2], 1, 1))
            .fluidInputs(MaterialUtils.molten(Materials.Plastic, 8 * INGOTS))
            .duration(1 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Circuits.MV.get(1),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.plate, (int) (1)),
                ItemList.Circuit_Board_Plastic.get(1L),
                ItemList.Battery_RE_LV_Lithium.get(1L))
            .itemOutputs(new ItemStack(ItemRegistry.CIRCUIT_PROGRAMMER))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MACHINE_HULLS[3].get(1),
                ItemList.Electric_Pump_HV.get(2),
                Circuits.EV.get(4),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 2))
            .itemOutputs(ItemList.Distillation_Tower.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_HV.get(64),
                MaterialLibAPI.getStack(Materials.LiquidAir, CellShapes.cell, (int) (1)))
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
                MaterialLibAPI.getStack(Materials.Lead, Shapes.plateDense, (int) (6)),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1))
            .itemOutputs(ItemList.Casing_RadiationProof.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Concrete, FluidShapes.fluidMolten, (int) (9 * INGOTS)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_RadiationProof.get(1),
                ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(4),
                MaterialLibAPI.getStack(Materials.Europium, Shapes.foil, (int) (6)),
                MaterialLibAPI.getStack(Materials.Europium, Shapes.screw, (int) (24)))
            .itemOutputs(ItemList.Casing_AdvancedRadiationProof.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidMolten, (int) (6 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }
}
