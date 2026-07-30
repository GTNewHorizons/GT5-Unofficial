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
import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
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
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (25)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (8)))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, (int) (8)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (2)))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, (int) (8)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (2)))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oil, FluidShapes.fluidLiquid, (int) (12)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (6)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilLight, FluidShapes.fluidLiquid, (int) (12)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (3)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilMedium, FluidShapes.fluidLiquid, (int) (12)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (6)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, (int) (12)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (9)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Biomass, FluidShapes.fluidLiquid, (int) (40)))
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
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SluiceJuice, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(GTUtility.getWater(500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (200)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 30))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, (int) (200)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (200)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 30))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, (int) (200)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);

        // (NaCl·H2O) = NaCl + H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, (int) (1_000)))
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
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, (int) (25)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(GTUtility.getWater(375))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, (int) (150)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, (int) (150)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, (int) (100)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (400)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (600)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(17)
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 1_800))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (100)))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("ic2biogas"), 300))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, (int) (100)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (40)))
            .duration(8 * SECONDS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, (int) (100)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, (int) (25)))
            .duration(8 * SECONDS)
            .eut(24)
            .addTo(distilleryRecipes);
        // Dimethylbenzene

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, (int) (200)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, (int) (20)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(ItemList.sOilExtraHeavy, 10))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, (int) (15)))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, (int) (10)))
            .fluidOutputs(new FluidStack(ItemList.sToluene, 4))
            .duration(16 * TICKS)
            .eut(24)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(ItemList.sToluene, 30))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, (int) (30)))
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
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glue, FluidShapes.fluidLiquid, (int) (8)))
                .fluidOutputs(getFluidStack("glue", 8))
                .duration(1 * TICKS)
                .eut(24)
                .addTo(distilleryRecipes);

            GTValues.RA.stdBuilder()
                .circuit(1)
                .fluidInputs(getFluidStack("glue", 8))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glue, FluidShapes.fluidLiquid, (int) (4)))
                .duration(1 * TICKS)
                .eut(24)
                .addTo(distilleryRecipes);

        }
    }

    public void distillationTowerRecipes() {
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (500)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, (int) (1_400)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (500)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, (int) (1_200)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (500)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials.Wood, Shapes.dustSmall, (int) (2L)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Biomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, (int) (600)),
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
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilLight, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (250)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilMedium, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (500)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, (int) (750)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        // C15H10N2O2(5HCl) = C15H10N2O2 + 5HCl

        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials.DiphenylmethaneDiisocyanate, Shapes.dust, (int) (29L)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DiphenylmethaneDiisocyanateMixture,
                    FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 5_000))
            .duration(2 * MINUTES + 5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dustSmall, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 100))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 150))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 150))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dustSmall, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 100))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dustSmall, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, (int) (20)),
                MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 60),
                MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 20))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, (int) (30)),
                MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 90),
                MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 30))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilLight, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, (int) (70)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, (int) (130)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, (int) (15)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, (int) (1600)))
            .duration(1 * SECONDS + 1 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilMedium, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, (int) (1_500)),
                MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, (int) (25)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, (int) (600)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oil, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, (int) (1_200)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, (int) (450)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, (int) (600)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        // 9C5H12O = 4C6H14O + 5CH4O + 4C4H8

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.MTBEReactionMixtureButene, FluidShapes.fluidGas, 900))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.EthylTertButylEther, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (400)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.MTBEReactionMixtureButane, FluidShapes.fluidGas, 900))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.EthylTertButylEther, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (400)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        ItemStack[] fertOutput = Mods.CropsNH.isModLoaded()
            ? new ItemStack[] { GTModHandler.getModItem(Mods.CropsNH.ID, "fertilizer", 1L) }
            : GTValues.emptyItemStackArray;
        GTValues.RA.stdBuilder()
            .itemOutputs(fertOutput)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.FermentedBiomass, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, (int) (25)),
                GTUtility.getWater(375),
                MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (600)))
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
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, (int) (1_500)))
            .duration(16 * TICKS)
            .eut(2400)
            .addTo(distillationTowerRecipes);
    }

    public void universalDistillationTowerRecipes() {
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.CharcoalByproducts, FluidShapes.fluidGas, (int) (1_000)),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, (int) (100)) },
            MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dustSmall, 1),
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.WoodGas, FluidShapes.fluidGas, (int) (1_000)),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (390)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (120)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (130)),
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, (int) (240)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (120)) },
            GTValues.NI,
            40,
            256);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.WoodVinegar, FluidShapes.fluidLiquid, (int) (1_000)),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, (int) (100)),
                GTUtility.getWater(500),
                MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.MethylAcetate, FluidShapes.fluidLiquid, (int) (10)) },
            GTValues.NI,
            40,
            256);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, (int) (1_000)),
            1,
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, (int) (150)) },
            GTValues.NI,
            40,
            256);

        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.OilLight, FluidShapes.fluidLiquid, (int) (100)),
            1,
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, (int) (7)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, (int) (13)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, (int) (20)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, (int) (160)) },
            null,
            10,
            96);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.OilMedium, FluidShapes.fluidLiquid, (int) (100)),
            1,
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, (int) (60)) },
            null,
            20,
            96);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.Oil, FluidShapes.fluidLiquid, (int) (100)),
            1,
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, (int) (30)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, (int) (40)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, (int) (120)) },
            null,
            30,
            96);
        addUniversalDistillationRecipewithCircuit(
            MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, (int) (100)),
            1,
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, (int) (45)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, (int) (15)),
                MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, (int) (60)) },
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
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, (int) (5)),
                GTUtility.getWater(35) },
            GTValues.NI,
            20,
            64);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.CalciumAcetateSolution, FluidShapes.fluidLiquid, (int) (1_000)),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, (int) (1_000)) },
            MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 2),
            80,
            480);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, (int) (3_000)),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, (int) (2_000)),
                GTUtility.getWater(1_000) },
            GTValues.NI,
            600,
            120);

        // C3H6O = C2H2O + CH4
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, (int) (1_000)),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethenone, FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_000)) },
            GTValues.NI,
            80,
            640);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidGas, (int) (1_000)),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (60)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (70)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (20)) },
            GTValues.NI,
            240,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (1_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (2_000)) },
            null,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_000)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_000)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_000)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (4_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_250)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_375)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 6),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_500)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 2),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (500)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (3_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (500)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (750)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 3),
            180,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_500)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 6),
            180,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (3_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (3_000)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_250)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_500)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_750)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 4),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (667)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (667)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (223)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (223)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (445)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (223)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (260)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (926)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (389)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (2_667)) },
            GTValues.NI,
            112,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (188)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (188)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 3),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (1_125)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (188)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 3),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (188)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_125)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (334)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (334)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (334)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (334)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (334)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (389)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (556)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (334)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_056)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (250)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (1_300)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (400)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 1),
            192,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (313)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_500)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustSmall, 6),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (667)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (667)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (667)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (2_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_000)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_063)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (750)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (438)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (2_000)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 11),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_300)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (1_500)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (100)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_400)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (3_000)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (150)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_500)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (4_000)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (200)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (50)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (600)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (70)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (700)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (100)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (800)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (250)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (1_100)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (400)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (1_500)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_500)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, (int) (75)),
                MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (40)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (80)),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (15)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (35)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (200)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (30)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (65)),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (30)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (350)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (350)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, (int) (25)),
                MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (20)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (15)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (65)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (500)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 3),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (800)),
                MaterialLibAPI.getFluidStack(Materials.Octane, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (125)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (500)),
                MaterialLibAPI.getFluidStack(Materials.Octane, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (1_100)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (400)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Octane, FluidShapes.fluidLiquid, (int) (20)),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (1_500)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (1_500)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (40)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (75)),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, (int) (60)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (20)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (50)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (90)),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, (int) (75)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (35)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (30)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (150)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (30)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (65)),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (250)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 3),
            120,
            120);

        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidHydroCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, (int) (600)),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (75)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (75)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidHydroCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (100)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidHydroCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (250)),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (175)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (175)) },
            GTValues.NI,
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidSteamCracked1, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, (int) (300)),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (25)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (25)),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, (int) (15)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (3)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (30)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (5)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (50)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 1),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidSteamCracked2, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (40)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (200)),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (40)),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, (int) (25)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (5)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (7)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (75)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (75)) },
            MaterialLibAPI.getStack(Materials.Carbon, Shapes.dustTiny, 2),
            120,
            120);
        addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidSteamCracked3, 1_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, (int) (125)),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, (int) (80)),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, (int) (80)),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, (int) (50)),
                MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, (int) (10)),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Ethane, FluidShapes.fluidGas, (int) (15)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (150)),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, (int) (150)) },
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
