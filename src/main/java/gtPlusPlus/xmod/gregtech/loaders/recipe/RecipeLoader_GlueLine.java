package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gtPlusPlus.core.lib.CORE.GTNH;
import static gtPlusPlus.core.material.MISC_MATERIALS.CYANOACETIC_ACID;
import static gtPlusPlus.core.material.MISC_MATERIALS.SODIUM_CHLORIDE;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.item.chemistry.NuclearChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.plugin.agrichem.BioRecipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeLoader_GlueLine {

    public static void generate() {
        createRecipes();
    }

    private static void createRecipes() {
        chemicalPlantRecipes();
        chemicalReactorRecipes();
        dehydratorRecipes();
        distillationTowerRecipes();
        fluidHeaterRecipes();
        mixerRecipes();
    }



    private static void chemicalPlantRecipes() {
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        ItemUtils.getSimpleStack(GenericChem.mBlueCatalyst, 1)
                },
                new FluidStack[] {
                        FluidUtils.getFluidStack("carbonmonoxide", 1000),
                        FluidUtils.getFluidStack("methylacetate", 1000),
                },
                new ItemStack[] {

                },
                new FluidStack[] {
                        MISC_MATERIALS.ACETIC_ANHYDRIDE.getFluidStack(1000)
                },
                10*20,
                2000,
                4);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(16)
                },
                new FluidStack[] {
                        FluidUtils.getFluidStack("aceticacid", 1000),
                        FluidUtils.getFluidStack("chlorine", 1000),
                        MISC_MATERIALS.ACETIC_ANHYDRIDE.getFluidStack(1000)
                },
                new ItemStack[] {

                },
                new FluidStack[] {
                        MISC_MATERIALS.CHLOROACETIC_MIXTURE.getFluidStack(1000),
                        MISC_MATERIALS.ACETIC_ANHYDRIDE.getFluidStack(950)
                },
                150*20,
                256,
                4);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(16),
                        ItemUtils.getSimpleStack(AgriculturalChem.mSodiumCarbonate, 2)
                },
                new FluidStack[] {
                        MISC_MATERIALS.CHLOROACETIC_ACID.getFluidStack(1000),
                        MISC_MATERIALS.SODIUM_CYANIDE.getFluidStack(1000),
                        FluidUtils.getFluidStack("hydrochloricacid_gt5u", 2000)
                },
                new ItemStack[] {
                        MISC_MATERIALS.CYANOACETIC_ACID.getDust(6)
                },
                new FluidStack[] {
                        FluidUtils.getFluidStack("dilutedhydrochloricacid_gt5u", 2000)
                },
                20*20,
                120,
                4);

        BioRecipes.mEthanol = FluidUtils.getFluidStack("bioethanol", 1).getFluid();

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(16),
                        ItemUtils.getSimpleStack(GenericChem.mSolidAcidCatalyst, 1),
                        MISC_MATERIALS.COPPER_SULFATE.getDust(1),
                        MISC_MATERIALS.CYANOACETIC_ACID.getDust(1)
                },
                new FluidStack[] {
                        FluidUtils.getFluidStack(BioRecipes.mEthanol, 100)
                },
                new ItemStack[] {
                        MISC_MATERIALS.COPPER_SULFATE_HYDRATED.getDust(1)
                },
                new FluidStack[] {
                        MISC_MATERIALS.ETHYL_CYANOACETATE.getFluidStack(100)
                },
                10*20,
                3000,
                5);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(16),
                        MISC_MATERIALS.CYANOACETIC_ACID.getDust(1)
                },
                new FluidStack[] {
                        FluidUtils.getFluidStack(BioRecipes.mEthanol, 100)
                },
                new ItemStack[] {

                },
                new FluidStack[] {
                        MISC_MATERIALS.ETHYL_CYANOACETATE.getFluidStack(100)
                },
                100*20,
                3000,
                5);

        BioRecipes.mFormaldehyde = FluidUtils.getFluidStack("fluid.formaldehyde", 1).getFluid();

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(16),
                        ItemUtils.getSimpleStack(GenericChem.mSolidAcidCatalyst, 1)
                },
                new FluidStack[] {
                        MISC_MATERIALS.ETHYL_CYANOACETATE.getFluidStack(100),
                        FluidUtils.getFluidStack(BioRecipes.mFormaldehyde, 100)
                },
                new ItemStack[] {
                },
                new FluidStack[] {
                        MISC_MATERIALS.CYANOACRYLATE_POLYMER.getFluidStack(100),
                        FluidUtils.getWater(1000)
                },
                2*20,
                10000,
                5);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(17),
                        ItemUtils.getSimpleStack(GenericChem.mPinkCatalyst, 1)
                },
                new FluidStack[] {
                        FluidUtils.getFluidStack("methane", 2000),
                        FluidUtils.getFluidStack("ammonia", 2000),
                        FluidUtils.getFluidStack("oxygen", 3000)
                },
                new ItemStack[] {
                },
                new FluidStack[] {
                        MISC_MATERIALS.HYDROGEN_CYANIDE.getFluidStack(2000),
                        FluidUtils.getWater(6000)
                },
                10*20,
                2000,
                4);
    }

    private static void chemicalReactorRecipes() {
        GT_Values.RA.addChemicalRecipe(
                CI.getNumberedCircuit(17),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSodiumHydroxide", 1),
                MISC_MATERIALS.HYDROGEN_CYANIDE.getFluidStack(200),
                FluidUtils.getWater(200),
                MISC_MATERIALS.SODIUM_CYANIDE.getDust(1),
                60 * 20);

        GT_Values.RA.addChemicalRecipe(
                CI.getNumberedCircuit(18),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 1),
                FluidUtils.getFluidStack("sulfuricacid", 500),
                FluidUtils.getFluidStack("dilutedsulfuricacid", 500),
                MISC_MATERIALS.COPPER_SULFATE.getDust(1),
                60 * 20);
    }

    private static void dehydratorRecipes() {
        CORE.RA.addDehydratorRecipe(
                new ItemStack[]{
                        CI.getNumberedCircuit(19),
                        MISC_MATERIALS.COPPER_SULFATE_HYDRATED.getDust(1),
                        ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1)
                },
                null,
                null,
                new ItemStack[]{
                        MISC_MATERIALS.COPPER_SULFATE.getDust(1),
                        ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 1)
                },
                new int[]{10000, 10000, 10000},
                300*20,
                400);
    }

    private static void distillationTowerRecipes() {
        GT_Values.RA.addDistillationTowerRecipe(
                MISC_MATERIALS.CHLOROACETIC_MIXTURE.getFluidStack(1000),
                new FluidStack[] {
                        MISC_MATERIALS.CHLOROACETIC_ACID.getFluidStack(200),
                        MISC_MATERIALS.DICHLOROACETIC_ACID.getFluidStack(400),
                        MISC_MATERIALS.TRICHLOROACETIC_ACID.getFluidStack(400)
                },
                null,
                4 * 20,
                MaterialUtils.getVoltageForTier(4));
    }

    private static void fluidHeaterRecipes() {

        CORE.RA.addFluidHeaterRecipe(
                CI.getNumberedCircuit(16),
                MISC_MATERIALS.CYANOACRYLATE_POLYMER.getFluidStack(1000),
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(1000),
                30 * 30,
                500);

    }

    private static void mixerRecipes() {
        GT_Values.RA.addMixerRecipe(
                CI.getNumberedCircuit(1),
                MISC_MATERIALS.DICHLOROACETIC_ACID.getCell(1),
                null,
                null,
                MISC_MATERIALS.TRICHLOROACETIC_ACID.getFluidStack(1000),
                MISC_MATERIALS.CHLOROACETIC_MIXTURE.getFluidStack(2000),
                null,
                120,
                30);

        GT_Values.RA.addMixerRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("cellSulfurTrioxide", 1),
                CI.getNumberedCircuit(2),
                null,
                null,
                FluidUtils.getFluidStack("sulfuricacid", 1000),
                MISC_MATERIALS.SOLID_ACID_MIXTURE.getFluidStack(1000),
                CI.emptyCells(1),
                120,
                30);
    }

}
