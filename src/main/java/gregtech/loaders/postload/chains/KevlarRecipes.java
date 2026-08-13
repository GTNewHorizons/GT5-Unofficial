package gregtech.loaders.postload.chains;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.preciseAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalPlantRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerNonCellRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.PRECISE_ASSEMBLER_CASING_TIER;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TierEU;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemEssence;

public class KevlarRecipes {

    public static void run() {

        // Part 1

        // Terephthalic Acid
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BlueMetalCatalyst.get(0))
            .fluidInputs(Materials.Dimethylbenzene.getFluid(1000), Materials.Oxygen.getGas(6000))
            .fluidOutputs(Materials.TerephthalicAcid.getFluid(1000), Materials.Water.getFluid(2000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Hexachloroxylene
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Chlorine.getGas(12000), Materials.IVDimethylbenzene.getFluid(1000))
            .fluidOutputs(Materials.Hexachloroxylene.getFluid(1000), Materials.HydrochloricAcid.getFluid(6000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // TerephthaloylChloride
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.RedMetalCatalyst.get(0))
            .fluidInputs(Materials.Hexachloroxylene.getFluid(1000), Materials.TerephthalicAcid.getFluid(1000))
            .fluidOutputs(Materials.TerephthaloylChloride.getFluid(2000), Materials.HydrochloricAcid.getFluid(2000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Nitroaniline
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Nitrochlorobenzene.getFluid(1000), Materials.Ammonia.getGas(2000))
            .fluidOutputs(Materials.IVNitroaniline.getFluid(1000), WerkstoffLoader.AmmoniumChloride.getFluidOrGas(1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Paraphenylene Diamine
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PinkMetalCatalyst.get(0), Materials.Iron.getDust(1))
            .fluidInputs(Materials.Hydrogen.getGas(6000), Materials.IVNitroaniline.getFluid(1000))
            .fluidOutputs(Materials.ParaPhenylenediamine.getFluid(1000), Materials.Water.getFluid(2000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Liquid Crystal Kevlar
        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(Materials.ParaPhenylenediamine.getFluid(1000), Materials.TerephthaloylChloride.getFluid(1000))
            .fluidOutputs(Materials.LiquidCrystalKevlar.getFluid(1296L), Materials.HydrochloricAcid.getFluid(2000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Part 2

        // Phosgenated MDI Mixture
        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(GTPPFluids.Aniline, 2000),
                new FluidStack(GTPPFluids.Formaldehyde, 1000),
                Materials.HydrochloricAcid.getFluid(1000))
            .fluidOutputs(Materials.PhosgenatedMDIMixture.getFluid(4000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Methylene Diphenyl Diisocyanate
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.PhosgenatedMDIMixture.getFluid(1000))
            .itemOutputs(Materials.MethyleneDiphenylDiisocyanate.getDust(1))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(250))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        // Polyurethane Resin
        GTValues.RA.stdBuilder()
            .circuit(24)
            .itemInputs(Materials.MethyleneDiphenylDiisocyanate.getDust(1))
            .fluidInputs(Materials.Ethyleneglycol.getFluid(1000))
            .fluidOutputs(Materials.PolyurethaneResin.getFluid(1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(Materials.Ethylene.getGas(1000), Materials.Water.getFluid(1000))
            .fluidOutputs(Materials.Ethyleneglycol.getFluid(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerNonCellRecipes);

        // Drawn Kevlar Fiber
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Spinneret.get(0L))
            .itemOutputs(ItemList.DrawnKevlarFiber.get(8L))
            .fluidInputs(Materials.LiquidCrystalKevlar.getFluid(500))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidSolidifierRecipes);

        // Fiber bath recipes
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DrawnKevlarFiber.get(8))
            .itemOutputs(ItemList.SpunKevlarFiber.get(8))
            .fluidInputs(Materials.Grade3PurifiedWater.getFluid(1_000))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DrawnKevlarFiber.get(8))
            .itemOutputs(ItemList.SpunKevlarFiber.get(8))
            .fluidInputs(Materials.Grade4PurifiedWater.getFluid(1_000))
            .duration(90 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DrawnKevlarFiber.get(8))
            .itemOutputs(ItemList.SpunKevlarFiber.get(8))
            .fluidInputs(Materials.Grade5PurifiedWater.getFluid(1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DrawnKevlarFiber.get(8))
            .itemOutputs(ItemList.SpunKevlarFiber.get(8))
            .fluidInputs(Materials.Grade6PurifiedWater.getFluid(1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DrawnKevlarFiber.get(16))
            .itemOutputs(ItemList.SpunKevlarFiber.get(16))
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DrawnKevlarFiber.get(32))
            .itemOutputs(ItemList.SpunKevlarFiber.get(32))
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(chemicalBathRecipes);

        // Woven Kevlar
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.SpunKevlarFiber.get(24))
            .circuit(24)
            .itemOutputs(ItemList.WovenKevlar.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 2)
            .addTo(preciseAssemblerRecipes);

        // Byproduct Super Glue

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.MethyleneDiphenylDiisocyanate.getDust(1))
            .fluidOutputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(fluidHeaterRecipes);

        // Improved/Boosted recipes
        // Bees
        if (Mods.Forestry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.SpunKevlarFiber.get(24), GTBees.combs.getStackForType(CombType.KEVLAR, 48))
                .circuit(23)
                .itemOutputs(ItemList.WovenKevlar.get(2))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 2)
                .addTo(preciseAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .circuit(23)
                .itemInputs(
                    Materials.MethyleneDiphenylDiisocyanate.getDust(1),
                    GTBees.combs.getStackForType(CombType.KEVLAR, 16))
                .fluidInputs(Materials.Ethyleneglycol.getFluid(1000))
                .fluidOutputs(Materials.PolyurethaneResin.getFluid(2000))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(multiblockChemicalReactorRecipes);
        }
        // Magic
        if (Mods.Thaumcraft.isModLoaded()) {
            final ItemStack filledPhial = new ItemStack(ConfigItems.itemEssence, 1, 1);
            final AspectList phialContent = new AspectList().add(TCAspects.GELUM.getAspect(), 8);
            ((ItemEssence) ConfigItems.itemEssence).setAspects(filledPhial, phialContent);

            GTValues.RA.stdBuilder()
                .circuit(23)
                .itemInputs(filledPhial)
                .fluidInputs(
                    Materials.ParaPhenylenediamine.getFluid(1000),
                    Materials.TerephthaloylChloride.getFluid(1000))
                .fluidOutputs(
                    Materials.LiquidCrystalKevlar.getFluid(2 * 1296L),
                    Materials.HydrochloricAcid.getFluid(2000))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(multiblockChemicalReactorRecipes);
        }
    }

}
