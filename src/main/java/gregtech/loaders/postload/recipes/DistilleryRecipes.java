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
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class DistilleryRecipes implements Runnable {

    @Override
    public void run() {

        distillationTowerRecipes();
        universalDistillationTowerRecipes();

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.fluidLiquid, (int) (25)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (8)))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.fluidLiquid, (int) (8)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (2)))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.fluidLiquid, (int) (8)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (2)))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oil, Materials2FluidShapes.fluidLiquid, (int) (12)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (6)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilLight, Materials2FluidShapes.fluidLiquid, (int) (12)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (3)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilMedium, Materials2FluidShapes.fluidLiquid, (int) (12)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (6)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilHeavy, Materials2FluidShapes.fluidLiquid, (int) (12)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (9)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Biomass, Materials2FluidShapes.fluidLiquid, (int) (40)))
            .fluidOutputs(Materials.Water.getFluid(12))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Water.getFluid(10))
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
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SluiceSand, Materials2Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SluiceJuice, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(Materials.Water.getFluid(500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.fluidLiquid, (int) (200)))
            .fluidOutputs(Materials.IIIDimethylbenzene.getFluid(30))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CharcoalByproducts, Materials2FluidShapes.fluidGas, (int) (200)))
            .fluidOutputs(Materials.IIIDimethylbenzene.getFluid(20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.fluidLiquid, (int) (200)))
            .fluidOutputs(Materials.IVDimethylbenzene.getFluid(30))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CharcoalByproducts, Materials2FluidShapes.fluidGas, (int) (200)))
            .fluidOutputs(Materials.IVDimethylbenzene.getFluid(20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        // (NaCl·H2O) = NaCl + H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.dust, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, (int) (25)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(Materials.Water.getFluid(375))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.fluidLiquid, (int) (150)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.fluidLiquid, (int) (150)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (100)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (400)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (600)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(17)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 1_800))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (100)))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 300))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (100)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (40)))
            .duration(8 * SECONDS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (100)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.fluidLiquid, (int) (25)))
            .duration(8 * SECONDS)
            .eut(24)
            .addTo(distilleryRecipes);
        // Dimethylbenzene

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CharcoalByproducts, Materials2FluidShapes.fluidGas, (int) (200)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylbenzene, Materials2FluidShapes.fluidLiquid, (int) (20)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(ItemList.sOilExtraHeavy, 10))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilHeavy, Materials2FluidShapes.fluidLiquid, (int) (15)))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .fluidOutputs(new FluidStack(ItemList.sToluene, 4))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(ItemList.sToluene, 30))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, (int) (30)))
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
            .fluidOutputs(Materials.Water.getFluid(2))
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
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Glue, Materials2FluidShapes.fluidLiquid, (int) (8)))
                .fluidOutputs(getFluidStack("glue", 8))
                .duration(1 * TICKS)
                .eut(24)
                .addTo(distilleryRecipes);

            GTValues.RA.stdBuilder()
                .circuit(1)
                .fluidInputs(getFluidStack("glue", 8))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Glue, Materials2FluidShapes.fluidLiquid, (int) (4)))
                .duration(1 * TICKS)
                .eut(24)
                .addTo(distilleryRecipes);

        }
    }

    public void distillationTowerRecipes() {
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (500)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.fluidLiquid, (int) (1_400)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (500)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.fluidLiquid, (int) (1_200)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (500)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dustSmall, (int) (2L)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Biomass, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.fluidLiquid, (int) (600)),
                Materials.Water.getFluid(300))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(400)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(GTModHandler.getDistilledWater(1_000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilLight, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (250)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilMedium, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (500)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilHeavy, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (750)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        // C15H10N2O2(5HCl) = C15H10N2O2 + 5HCl

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials2Materials.DiphenylmethaneDiisocyanate, Materials2Shapes.dust, (int) (29L)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DiphenylmethaneDiisocyanateMixture,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(5_000))
            .duration(2 * MINUTES + 5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.dustSmall, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CharcoalByproducts,
                    Materials2FluidShapes.fluidGas,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodVinegar, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials2Materials.WoodGas, Materials2FluidShapes.fluidGas, (int) (250)),
                Materials.IIIDimethylbenzene.getFluid(100))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (100)),
                Materials.IIIDimethylbenzene.getFluid(150))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (100)),
                Materials.IVDimethylbenzene.getFluid(150))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.dustSmall, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CharcoalByproducts,
                    Materials2FluidShapes.fluidGas,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodVinegar, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials2Materials.WoodGas, Materials2FluidShapes.fluidGas, (int) (250)),
                Materials.IVDimethylbenzene.getFluid(100))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.dustSmall, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CharcoalByproducts,
                    Materials2FluidShapes.fluidGas,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodVinegar, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials2Materials.WoodGas, Materials2FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylbenzene, Materials2FluidShapes.fluidLiquid, (int) (20)),
                Materials.IIIDimethylbenzene.getFluid(60),
                Materials.IVDimethylbenzene.getFluid(20))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylbenzene, Materials2FluidShapes.fluidLiquid, (int) (30)),
                Materials.IIIDimethylbenzene.getFluid(90),
                Materials.IVDimethylbenzene.getFluid(30))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilLight, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricHeavyFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (70)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricLightFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (130)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricNaphtha, Materials2FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NaphthenicAcid, Materials2FluidShapes.fluidLiquid, (int) (15)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricGas, Materials2FluidShapes.fluidGas, (int) (1600)))
            .duration(1 * SECONDS + 1 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilMedium, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricHeavyFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricLightFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (500)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricNaphtha,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_500)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NaphthenicAcid, Materials2FluidShapes.fluidLiquid, (int) (25)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricGas, Materials2FluidShapes.fluidGas, (int) (600)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oil, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricHeavyFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (300)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricLightFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricNaphtha, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NaphthenicAcid, Materials2FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricGas, Materials2FluidShapes.fluidGas, (int) (1_200)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilHeavy, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricHeavyFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricLightFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (450)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricNaphtha, Materials2FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NaphthenicAcid, Materials2FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricGas, Materials2FluidShapes.fluidGas, (int) (600)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        // 9C5H12O = 4C6H14O + 5CH4O + 4C4H8

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.MTBEMixture.getGas(900))
            .fluidOutputs(
                Materials.AntiKnock.getFluid(400),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.fluidLiquid, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (400)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.MTBEMixtureAlt.getGas(900))
            .fluidOutputs(
                Materials.AntiKnock.getFluid(400),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.fluidLiquid, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (400)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        ItemStack[] fertOutput = Mods.CropsNH.isModLoaded()
            ? new ItemStack[] { GTModHandler.getModItem(Mods.CropsNH.ID, "fertilizer", 1L) }
            : GTValues.emptyItemStackArray;
        GTValues.RA.stdBuilder()
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FermentedBiomass,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, (int) (25)),
                Materials.Water.getFluid(375),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (600)))
            .duration(3 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(fertOutput)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 3_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 8_000), Materials.Water.getFluid(125))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(ItemList.sOilExtraHeavy, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.OilHeavy, Materials2FluidShapes.fluidLiquid, (int) (1_500)))
            .duration(16 * TICKS)
            .eut(2400)
            .addTo(distillationTowerRecipes);
    }

    public void universalDistillationTowerRecipes() {
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI
                .getFluidStack(Materials2Materials.CharcoalByproducts, Materials2FluidShapes.fluidGas, (int) (1_000)),
            1,
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodVinegar, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials2Materials.WoodGas, Materials2FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Dimethylbenzene,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)) },
            MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.dustSmall, 1),
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials2Materials.WoodGas, Materials2FluidShapes.fluidGas, (int) (1_000)),
            1,
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (390)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (120)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (130)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, (int) (240)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (120)) },
            GTValues.NI,
            40,
            256);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI
                .getFluidStack(Materials2Materials.WoodVinegar, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
            1,
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, (int) (100)),
                Materials.Water.getFluid(500),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.fluidLiquid, (int) (10)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.fluidLiquid, (int) (300)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.MethylAcetate, Materials2FluidShapes.fluidLiquid, (int) (10)) },
            GTValues.NI,
            40,
            256);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
            1,
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Dimethylbenzene,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (150)) },
            GTValues.NI,
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials2Materials.OilLight, Materials2FluidShapes.fluidLiquid, (int) (100)),
            1,
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricHeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (7)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricLightFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (13)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricNaphtha, Materials2FluidShapes.fluidLiquid, (int) (20)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricGas, Materials2FluidShapes.fluidGas, (int) (160)) },
            null,
            10,
            96);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials2Materials.OilMedium, Materials2FluidShapes.fluidLiquid, (int) (100)),
            1,
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.SulfuricHeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (10)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricLightFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (50)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricNaphtha, Materials2FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricGas, Materials2FluidShapes.fluidGas, (int) (60)) },
            null,
            20,
            96);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials2Materials.Oil, Materials2FluidShapes.fluidLiquid, (int) (100)),
            1,
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.SulfuricHeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (30)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricLightFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricNaphtha, Materials2FluidShapes.fluidLiquid, (int) (40)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricGas, Materials2FluidShapes.fluidGas, (int) (120)) },
            null,
            30,
            96);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials2Materials.OilHeavy, Materials2FluidShapes.fluidLiquid, (int) (100)),
            1,
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.SulfuricHeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricLightFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (45)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricNaphtha, Materials2FluidShapes.fluidLiquid, (int) (15)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricGas, Materials2FluidShapes.fluidGas, (int) (60)) },
            null,
            40,
            288);

        // 2 0.5HCl(Diluted) = HCl + H2O
        addUniversalDistillationRecipe(
            Materials.DilutedHydrochloricAcid.getFluid(2_000),
            new FluidStack[] { Materials.Water.getFluid(1_000), Materials.HydrochloricAcid.getFluid(1_000) },
            GTValues.NI,
            600,
            64);

        addUniversalDistillationRecipe(
            getFluidStack("potion.vinegar", 40),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, (int) (5)),
                Materials.Water.getFluid(35) },
            GTValues.NI,
            20,
            64);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(
                Materials2Materials.CalciumAcetateSolution,
                Materials2FluidShapes.fluidLiquid,
                (int) (1_000)),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)) },
            MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, 2),
            80,
            480);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(
                Materials2Materials.DilutedSulfuricAcid,
                Materials2FluidShapes.fluidLiquid,
                (int) (3_000)),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (2_000)),
                Materials.Water.getFluid(1_000) },
            GTValues.NI,
            600,
            120);

        // C3H6O = C2H2O + CH4
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethenone, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_000)) },
            GTValues.NI,
            80,
            640);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials2Materials.Gas, Materials2FluidShapes.fluidGas, (int) (1_000)),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (60)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (70)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (20)) },
            GTValues.NI,
            240,
            120);

        addUniversalDistillationRecipe(
            Materials.Ethylene.getLightlyHydroCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (1_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getModeratelyHydroCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (2_000)) },
            null,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getSeverelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getLightlySteamCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_000)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getModeratelySteamCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_000)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethylene.getSeverelySteamCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_000)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 1),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Ethane.getLightlyHydroCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getModeratelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getSeverelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (4_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getLightlySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_250)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getModeratelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_375)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 6),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Ethane.getSeverelySteamCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_500)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 2),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Propene.getLightlyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (500)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getModeratelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getSeverelyHydroCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (3_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getLightlySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (500)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getModeratelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (750)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 3),
            180,
            120);
        addUniversalDistillationRecipe(
            Materials.Propene.getSeverelySteamCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_500)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 6),
            180,
            120);

        addUniversalDistillationRecipe(
            Materials.Propane.getLightlyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getModeratelyHydroCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (3_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getSeverelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (3_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getLightlySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_250)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getModeratelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_500)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Propane.getSeverelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_750)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 4),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Butadiene.getLightlyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (667)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (667)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getModeratelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (223)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (223)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (445)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (223)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getSeverelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (260)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (926)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (389)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (2_667)) },
            GTValues.NI,
            112,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getLightlySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (188)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (188)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 3),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getModeratelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (1_125)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (188)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 3),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butadiene.getSeverelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (188)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_125)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 1),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Butene.getLightlyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (334)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (334)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (334)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (334)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (334)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getModeratelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (389)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (556)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (334)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_056)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getSeverelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getLightlySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (250)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getModeratelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (1_300)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (400)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 1),
            192,
            120);
        addUniversalDistillationRecipe(
            Materials.Butene.getSeverelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (313)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_500)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustSmall, 6),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Butane.getLightlyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (667)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (667)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (667)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getModeratelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getSeverelyHydroCracked(1_000),
            new FluidStack[] { MaterialLibAPI
                .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getLightlySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_063)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getModeratelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (438)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Butane.getSeverelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (2_000)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 11),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Gas.getLightlyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_300)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_500)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (100)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getModeratelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (3_000)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (150)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getSeverelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_500)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (4_000)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (200)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getLightlySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (50)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getModeratelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (600)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (70)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Gas.getSeverelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (700)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (100)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 1),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.Naphtha.getLightlyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (800)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (250)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getModeratelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (1_100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (400)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getSeverelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (1_500)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_500)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getLightlySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (75)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (40)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (80)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (15)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (35)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (200)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getModeratelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (30)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (65)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (30)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (350)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (350)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.Naphtha.getSeverelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (25)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (20)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (15)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (65)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (500)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 3),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.LightFuel.getLightlyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (800)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Octane, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (125)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getModeratelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Octane, Materials2FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (1_100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (400)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getSeverelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Octane, Materials2FluidShapes.fluidLiquid, (int) (20)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (1_500)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (1_500)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getLightlySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (40)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (75)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.fluidGas, (int) (60)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (20)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (50)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getModeratelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (90)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.fluidGas, (int) (75)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (35)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (30)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (150)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.LightFuel.getSeverelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (30)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (65)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (250)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 3),
            120,
            120);

        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getLightlyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, (int) (600)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (75)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (75)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getModeratelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (100)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getSeverelyHydroCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.fluidGas, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (175)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (175)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getLightlySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, (int) (300)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (25)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (25)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.fluidGas, (int) (15)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (3)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (30)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (5)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (50)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getModeratelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (40)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (40)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.fluidGas, (int) (25)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (5)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (7)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (75)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (75)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            Materials.HeavyFuel.getSeverelySteamCracked(1_000),
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.fluidLiquid, (int) (125)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.fluidLiquid, (int) (80)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.fluidGas, (int) (80)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propane, Materials2FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethane, Materials2FluidShapes.fluidGas, (int) (15)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (150)) },
            MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dustTiny, 3),
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
