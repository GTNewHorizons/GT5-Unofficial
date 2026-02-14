package gregtech.loaders.postload.chains;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.preciseAssemblerRecipes;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.recipe.RecipeMaps.*;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.*;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.mixerNonCellRecipes;

import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.BotWerkstoffMaterialPool;

public class KevlarRecipes {

    public static void run() {

        // TPA, Precursor to TPA Dust
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BlueMetalCatalyst.get(0))
            .fluidInputs(Materials.Dimethylbenzene.getFluid(1000), Materials.Oxygen.getGas(6000))
            .fluidOutputs(Materials.TerephthalicAcid.getFluid(1000), Materials.Water.getFluid(2000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // TPA Dust, Precursor to TLC
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.TerephthalicAcid.getFluid(1000))
            .itemOutputs(Materials.TerephthalicAcid.getDust(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(vacuumFreezerRecipes);

        // Precursor to TLC
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Chlorine.getGas(12000), Materials.Dimethylbenzene.getFluid(1000))
            .fluidOutputs(Materials.Hexachloroxylene.getFluid(1000), Materials.HydrochloricAcid.getFluid(6000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // TLC, Precursor to LCK
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.RedMetalCatalyst.get(0), Materials.TerephthalicAcid.getDust(1))
            .fluidInputs(Materials.Hexachloroxylene.getFluid(1000))
            .itemOutputs(Materials.TerephthaloylChloride.getDust(2))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(2000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Precursor to PPD
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.Aniline, 1000), Materials.NitricAcid.getFluid(1000))
            .fluidOutputs(Materials.Nitroaniline.getFluid(1000), Materials.Water.getFluid(1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // PPD, Precursor to LCK
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PinkMetalCatalyst.get(0), Materials.Iron.getDust(1))
            .fluidInputs(Materials.Hydrogen.getGas(6000), Materials.Nitroaniline.getFluid(1000))
            .fluidOutputs(Materials.ParaPhenylenediamine.getFluid(1000), Materials.Water.getFluid(2000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // LCK, First taste of Kevlar
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.TerephthaloylChloride.getDust(1), WerkstoffLoader.CalciumChloride.get(dust, 1))
            .fluidInputs(Materials.ParaPhenylenediamine.getFluid(1000))
            .fluidOutputs(Materials.LiquidCrystalKevlar.getFluid(1000), Materials.HydrochloricAcid.getFluid(2000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // MDA, Precursor to MDI
        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(GTPPFluids.Aniline, 2000),
                new FluidStack(GTPPFluids.Formaldehyde, 1000),
                Materials.Hydrogen.getGas(12000),
                Materials.HydrochloricAcid.getFluid(4000))
            .fluidOutputs(
                Materials.Methylenedianiline.getFluid(1000),
                Materials.Water.getFluid(6000),
                Materials.DilutedHydrochloricAcid.getFluid(4000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerNonCellRecipes);

        // MDI, Precursor to Polyurethane
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.Methylenedianiline.getFluid(1000),
                BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(2000))
            .itemOutputs(Materials.MethyleneDiphenylDiisocyanate.getDust(1))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(4000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Polyurethane Resin, Precursor for Kevlar Plates
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.MethyleneDiphenylDiisocyanate.getDust(1))
            .fluidInputs(Materials.Ethyleneglycol.getFluid(1000), Materials.Glycerol.getFluid(1000))
            .fluidOutputs(Materials.PolyurethaneResin.getFluid(1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(Materials.Ethylene.getGas(1000), Materials.Water.getFluid(1000))
            .fluidOutputs(Materials.Ethyleneglycol.getFluid(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerNonCellRecipes);

        // TODO - Balance TBD, also must change spinneret recipe
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Spinneret.get(0L))
            .itemOutputs(ItemList.KevlarFiber.get(8L))
            .fluidInputs(Materials.LiquidCrystalKevlar.getFluid(500))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DrawnKevlarFiber.get(24))
            .circuit(24)
            .itemOutputs(ItemList.WovenKevlar.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 2)
            .addTo(preciseAssemblerRecipes);

        // Fiber bath recipes
        // TODO - Actual proper grade water recipes
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.KevlarFiber.get(1L))
            .itemOutputs(ItemList.DrawnKevlarFiber.get(1))
            .fluidInputs(Materials.Grade3PurifiedWater.getFluid(250))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.KevlarFiber.get(1L))
            .itemOutputs(ItemList.DrawnKevlarFiber.get(1))
            .fluidInputs(Materials.Grade4PurifiedWater.getFluid(250))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.KevlarFiber.get(1L))
            .itemOutputs(ItemList.DrawnKevlarFiber.get(1), ItemList.DrawnKevlarFiber.get(1))
            .outputChances(10000, 2500)
            .fluidInputs(Materials.Grade5PurifiedWater.getFluid(250))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.KevlarFiber.get(1L))
            .itemOutputs(ItemList.DrawnKevlarFiber.get(1), ItemList.DrawnKevlarFiber.get(1))
            .outputChances(10000, 2500)
            .fluidInputs(Materials.Grade6PurifiedWater.getFluid(250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.KevlarFiber.get(1L))
            .itemOutputs(ItemList.DrawnKevlarFiber.get(1), ItemList.DrawnKevlarFiber.get(1))
            .outputChances(10000, 5000)
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.KevlarFiber.get(1L))
            .itemOutputs(ItemList.DrawnKevlarFiber.get(1), ItemList.DrawnKevlarFiber.get(1))
            .outputChances(10000, 5000)
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(250))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);
    }
}
