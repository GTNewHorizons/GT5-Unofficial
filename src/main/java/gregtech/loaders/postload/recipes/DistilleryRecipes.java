package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class DistilleryRecipes implements Runnable {

    @Override
    public void run() {

        distillationTowerRecipes();
        universalDistillationTowerRecipes();

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, 25))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 8))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, 8))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 2))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, 8))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 2))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oil, FluidShapes.fluidLiquid, 12))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 6))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilLight, FluidShapes.fluidLiquid, 12))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 3))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilMedium, FluidShapes.fluidLiquid, 12))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 6))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, 12))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 9))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Biomass, FluidShapes.fluidLiquid, 40))
            .fluidOutputs(GTUtility.getWater(12))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(GTUtility.getWater(10))
            .fluidOutputs(GTModHandler.getDistilledWater(10))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(getFluidStack("potion.potatojuice", 2))
            .fluidOutputs(getFluidStack("potion.vodka", 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(getFluidStack("potion.lemonade", 2))
            .fluidOutputs(getFluidStack("potion.alcopops", 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SluiceSand, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SluiceJuice, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 200))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 30))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, 200))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 200))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 30))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, 200))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        // (NaCl·H2O) = NaCl + H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTModHandler.getDistilledWater(1_000))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        ItemStack[] fertOutput = Mods.CropsNH.isModLoaded()
            ? new ItemStack[] { GTModHandler.getModItem(Mods.CropsNH.ID, "fertilizer", 1L) }
            : GTValues.emptyItemStackArray;
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(fertOutput)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 25))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(fertOutput)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(375))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(fertOutput)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 150))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(fertOutput)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 150))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .itemOutputs(fertOutput)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 100))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .itemOutputs(fertOutput)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 400))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .itemOutputs(fertOutput)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 600))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(17)
            .itemOutputs(fertOutput)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 1_800))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 100))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 300))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 100))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 40))
            .duration(8 * SECONDS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 100))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 25))
            .duration(8 * SECONDS)
            .eut(24)
            .addTo(distilleryRecipes);
        // Dimethylbenzene

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, 200))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, 20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(ItemList.sOilExtraHeavy, 10))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, 15))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 10))
            .fluidOutputs(new FluidStack(ItemList.sToluene, 4))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(ItemList.sToluene, 30))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 30))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 20))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 32))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 4))
            .fluidOutputs(GTUtility.getWater(2))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(getFluidStack("potion.wheatyjuice", 75))
            .fluidOutputs(getFluidStack("potion.scotch", 1))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        if (TinkerConstruct.isModLoaded()) {

            GTValues.RA.stdBuilder()
                .circuit(1)
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glue, FluidShapes.fluidLiquid, 8))
                .fluidOutputs(getFluidStack("glue", 8))
                .duration(1 * TICKS)
                .eut(24)
                .addTo(distilleryRecipes);

            GTValues.RA.stdBuilder()
                .circuit(1)
                .fluidInputs(getFluidStack("glue", 8))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glue, FluidShapes.fluidLiquid, 4))
                .duration(1 * TICKS)
                .eut(24)
                .addTo(distilleryRecipes);

        }
    }

    public void distillationTowerRecipes() {
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 500))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, 1_400))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 500))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, 1_200))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 500))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials.Wood, Shapes.dustSmall, (int) (2L)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Biomass, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 600),
                GTUtility.getWater(300))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(400)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(GTModHandler.getDistilledWater(1_000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilLight, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 250))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilMedium, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 500))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 750))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        // C15H10N2O2(5HCl) = C15H10N2O2 + 5HCl

        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials.DiphenylmethaneDiisocyanate, Shapes.dust, (int) (29L)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DiphenylmethaneDiisocyanateMixture, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 5_000))
            .duration(2 * MINUTES + 5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dustSmall, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, 250),
                MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 100))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 150))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 150))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dustSmall, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, 250),
                MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 100))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dustSmall, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, 250),
                MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, 20),
                MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 60),
                MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 20))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, 30),
                MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 90),
                MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 30))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilLight, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, 70),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, 130),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, 200),
                MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, 15),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, 1600))
            .duration(1 * SECONDS + 1 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilMedium, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, 500),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, 1_500),
                MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, 25),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, 600))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oil, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, 300),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, 1_200))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, 450),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, 150),
                MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, 600))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        // 9C5H12O = 4C6H14O + 5CH4O + 4C4H8

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.MTBEReactionMixtureButene, FluidShapes.fluidGas, 900))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.EthylTertButylEther, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 500),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 400))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.MTBEReactionMixtureButane, FluidShapes.fluidGas, 900))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.EthylTertButylEther, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 500),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 400))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        ItemStack[] fertOutput = Mods.CropsNH.isModLoaded()
            ? new ItemStack[] { GTModHandler.getModItem(Mods.CropsNH.ID, "fertilizer", 1L) }
            : GTValues.emptyItemStackArray;
        GTValues.RA.stdBuilder()
            .itemOutputs(fertOutput)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 25),
                GTUtility.getWater(375),
                MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 150),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 150),
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 400),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 600))
            .duration(3 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(fertOutput)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 3_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 8_000), GTUtility.getWater(125))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(ItemList.sOilExtraHeavy, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, 1_500))
            .duration(16 * TICKS)
            .eut(2400)
            .addTo(distillationTowerRecipes);
    }

    public void universalDistillationTowerRecipes() {
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, 1_000),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, 250),
                MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, 100) },
            MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dustSmall, 1),
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, 1_000),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 390),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 120),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 130),
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 240),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 120) },
            GTValues.NI,
            40,
            256);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, 1_000),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 100),
                GTUtility.getWater(500), MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 10),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 300),
                MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.MethylAcetate, FluidShapes.fluidLiquid, 10) },
            GTValues.NI,
            40,
            256);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 1_000),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, 150) },
            GTValues.NI,
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.OilLight, FluidShapes.fluidLiquid, 100),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, 7),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, 13),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, 20),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, 160) },
            null,
            10,
            96);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.OilMedium, FluidShapes.fluidLiquid, 100),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, 10),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, 150),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, 60) },
            null,
            20,
            96);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.Oil, FluidShapes.fluidLiquid, 100),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, 30),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, 40),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, 120) },
            null,
            30,
            96);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, 100),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, 45),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, 15),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, 60) },
            null,
            40,
            288);

        // 2 0.5HCl(Diluted) = HCl + H2O
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000),
            new FluidStack[] { GTUtility.getWater(1_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000) },
            GTValues.NI,
            600,
            64);

        addUniversalDistillationRecipe(
            getFluidStack("potion.vinegar", 40),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 5),
                GTUtility.getWater(35) },
            GTValues.NI,
            20,
            64);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.CalciumAcetateSolution, FluidShapes.fluidLiquid, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000) },
            MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 2),
            80,
            480);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 3_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 2_000),
                GTUtility.getWater(1_000) },
            GTValues.NI,
            600,
            120);

        // C3H6O = C2H2O + CH4
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethenone, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000) },
            GTValues.NI,
            80,
            640);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidGas, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 60),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 70),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 750),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 20) },
            GTValues.NI,
            240,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 1_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000) },
            null,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 250),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_250) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_375) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 6),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_500) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 2),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 500),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 500),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 500) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 3_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 500) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 750),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 750) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 3),
            180,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_500) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 6),
            180,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 3_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 3_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 750),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_250) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 500),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_500) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 250),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_750) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 4),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 667),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 667) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 223),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 223),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 400),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 445),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 223) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 260),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 926),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 389),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_667) },
            GTValues.NI,
            112,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 750),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 188),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 188) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 3),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_125),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 188) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 3),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 188),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_125) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 334),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 334),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 334),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 334),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 334) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 389),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 556),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 334),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_056) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 750),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 500),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 250) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 200),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_300),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 400) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 1),
            192,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 313),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_500) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 6),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 667),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 667),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 667) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 750),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_063) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 750),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 750),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 438) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 11),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_300),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_500),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 100) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_400),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 3_000),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 150) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_500),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 200) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 10),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 500),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 50) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 10),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 200),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 600),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 70) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 10),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 10),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 300),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 700),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 100) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 800),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 300),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 250),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 250) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 200),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 1_100),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 400),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 400) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 1_500),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_500) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 75),
                MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 150),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 40),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 150),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 80),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 150),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 15),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 200),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 35),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 200),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 200) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 30),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 125),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 65),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 30),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 400),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 350),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 350) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 25),
                MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 20),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 15),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 300),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 65),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 500),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 500) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 3),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 800),
                MaterialLibAPI.getFluidStack(Materials.Octane, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 150),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 200),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 125) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 500),
                MaterialLibAPI.getFluidStack(Materials.Octane, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 200),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 1_100),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 400),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 400) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 200),
                MaterialLibAPI.getFluidStack(Materials.Octane, FluidShapes.fluidLiquid, 20),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 1_500),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_500) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 150),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 40),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 200),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 75),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 60),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 20),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 150),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 10),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 50) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 300),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 90),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 75),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 35),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 200),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 30),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 150),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 150) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 30),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 150),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 65),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 250),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 250),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 250) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 3),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 600),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 75),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 75) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 150),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 150),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 100) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 200),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 250),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 300),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 300),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 175),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 175) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 300),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 25),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 125),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 25),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 15),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 3),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 30),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 5),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 50) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 200),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 200),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 40),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 200),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 40),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 25),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 5),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 7),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 75),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 75) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 125),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 80),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 80),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 50),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 10),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, 15),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 150),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 150) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 3),
            120,
            120);
    }

    public void addUniversalDistillationRecipewithCircuit(FluidStack aInput, int aCircuit, FluidStack[] aOutputs,
        ItemStack aOutput2, int aDuration, int aEUt) {
        GTRecipeBuilder buildDT = GTValues.RA.stdBuilder()
            .circuit(aCircuit);
        if (aOutput2 != GTValues.NI) {
            buildDT.itemOutputs(aOutput2);
        }
        buildDT.fluidInputs(aInput)
            .fluidOutputs(aOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .addTo(distillationTowerRecipes);

        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            GTRecipeBuilder buildDistillation = GTValues.RA.stdBuilder()
                .circuit(i + 1);
            int ratio = getRatioForDistillery(aInput, aOutputs[i], aOutput2);

            FluidStack aInputDivided = new FluidStack(aInput, Math.max(1, aInput.amount / ratio));
            FluidStack aOutputDivided = new FluidStack(aOutputs[i], Math.max(1, aOutputs[i].amount / ratio));
            ItemStack aOutput2Divided;
            aOutput2Divided = aOutput2;

            if (aOutput2Divided != GTValues.NI) {
                aOutput2Divided.stackSize /= ratio;
                buildDistillation.itemOutputs(aOutput2Divided);
            }

            buildDistillation.fluidInputs(aInputDivided)
                .fluidOutputs(aOutputDivided)
                .duration(2 * aDuration / ratio)
                .eut(aEUt / 4)
                .addTo(distilleryRecipes);
        }
    }

    public void addUniversalDistillationRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2,
        int aDuration, int aEUt) {
        GTRecipeBuilder buildDT = GTValues.RA.stdBuilder();
        if (aOutput2 != GTValues.NI) {
            buildDT.itemOutputs(aOutput2);
        }
        buildDT.fluidInputs(aInput)
            .fluidOutputs(aOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .addTo(distillationTowerRecipes);

        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            GTRecipeBuilder buildDistillation = GTValues.RA.stdBuilder()
                .circuit(i + 1);
            int ratio = getRatioForDistillery(aInput, aOutputs[i], aOutput2);

            FluidStack aInputDivided = new FluidStack(aInput, Math.max(1, aInput.amount / ratio));
            FluidStack aOutputDivided = new FluidStack(aOutputs[i], Math.max(1, aOutputs[i].amount / ratio));
            ItemStack aOutput2Divided;
            aOutput2Divided = aOutput2;

            if (aOutput2Divided != GTValues.NI) {
                aOutput2Divided.stackSize /= ratio;
                buildDistillation.itemOutputs(aOutput2Divided);
            }

            buildDistillation.fluidInputs(aInputDivided)
                .fluidOutputs(aOutputDivided)
                .duration(2 * aDuration / ratio)
                .eut(aEUt / 4)
                .addTo(distilleryRecipes);
        }
    }

    private static int getRatioForDistillery(FluidStack aInput, FluidStack aOutput, ItemStack aOutput2) {
        int[] divisors = new int[] { 2, 5, 10, 25, 50 };
        int ratio = -1;

        for (int divisor : divisors) {

            if (!isFluidStackDivisibleForDistillery(aInput, divisor)) continue;

            if (!isFluidStackDivisibleForDistillery(aOutput, divisor)) continue;

            if (aOutput2 != null && aOutput2.stackSize % divisor != 0) continue;

            ratio = divisor;
        }

        return Math.max(1, ratio);
    }

    private static boolean isFluidStackDivisibleForDistillery(FluidStack fluidStack, int divisor) {
        return fluidStack.amount % divisor == 0 && fluidStack.amount / divisor >= 25;
    }
}
