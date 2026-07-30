package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class ElectrolyzerRecipes implements Runnable {

    @Override
    public void run() {
        // H2O = 2H + O

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1L))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (1)))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1L))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (1)))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(2L))
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (2)))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (1_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(2L))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (2)))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (1_000)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L), ItemList.Cell_Empty.get(2L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (2)),
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (1)))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Dye_Bonemeal.get(3L))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, (int) (1)))
            .duration(4 * SECONDS + 18 * TICKS)
            .eut(26)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 8, 0))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (3)))
            .duration(25 * SECONDS)
            .eut(25)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 8, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, (int) (3)))
            .duration(25 * SECONDS)
            .eut(25)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (4)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(electrolyzerRecipes);
        // ZnS = Zn + S + 1 Ga(9.17%)

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sphalerite, Shapes.dust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Gallium, Shapes.dust, (int) (1)))
            .outputChances(10000, 10000, 917)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // NaOH = Na + O + H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (1)))
            .outputChances(10000, 10000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (1_000)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // CO2 = C + 2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (2)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // CO = C + O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CarbonMonoxide, CellShapes.cell, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (1)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // H2S = S + 2H

        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (2)))
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HydricSulfide, CellShapes.cell, (int) (1)),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // SO2 = S + 2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (2)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);
        // NaCl = Na +Cl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, (int) (2)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, (int) (1)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (1_000)))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // (NaCl·H2O)= NaOH + H

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Chlorine, CellShapes.cell, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // HCl = H + Cl

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chlorine, CellShapes.cell, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (1)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chlorine, CellShapes.cell, (int) (1)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (1_000)))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);
        // 2NaHSO4 = 2H + Na2S2O8

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumBisulfate, Shapes.dust, (int) (14)),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (2)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SodiumPersulfate, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Lead, Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (4)))
            .fluidInputs(new FluidStack(ItemList.sLeadZincSolution, 8000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(15 * SECONDS)
            .eut(192)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (1)))
            .fluidInputs(new FluidStack(ItemList.sBlueVitriol, 2000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (1)))
            .fluidInputs(new FluidStack(ItemList.sNickelSulfate, 2000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (1)))
            .fluidInputs(new FluidStack(ItemList.sGreenVitriol, 2000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PhosphoricAcidGT5U, CellShapes.cell, 1),
                ItemList.Cell_Empty.get(6L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (3)),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (4)))
            .duration(27 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.CalciumChloride, FluidShapes.fluidLiquid, (int) (3_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, (int) (1)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (2_000)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

    }
}
