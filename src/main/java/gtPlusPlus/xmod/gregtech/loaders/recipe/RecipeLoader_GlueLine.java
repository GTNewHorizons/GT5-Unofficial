package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gtPlusPlus.core.lib.CORE.GTNH;
import static gtPlusPlus.core.material.MISC_MATERIALS.CYANOACETIC_ACID;
import static gtPlusPlus.core.material.MISC_MATERIALS.SODIUM_CHLORIDE;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
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
import net.minecraft.init.Items;
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
        electrolyzerRecipes();
        fluidHeaterRecipes();
        mixerRecipes();

        glueUsageRecipes();
    }



    private static void chemicalPlantRecipes() {
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {

                        CI.getNumberedCircuit(17),
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
                500,
                3);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(18)
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
                1000,
                4);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(19),
                        ItemUtils.getSimpleStack(AgriculturalChem.mSodiumCarbonate, 2),
                        MISC_MATERIALS.SODIUM_CYANIDE.getDust(6)
                },
                new FluidStack[] {
                        MISC_MATERIALS.CHLOROACETIC_ACID.getFluidStack(1000),
                        FluidUtils.getFluidStack("hydrochloricacid_gt5u", 2000)
                },
                new ItemStack[] {
                        MISC_MATERIALS.CYANOACETIC_ACID.getDust(6)
                },
                new FluidStack[] {
                        FluidUtils.getFluidStack("dilutedhydrochloricacid_gt5u", 2000)
                },
                20*20,
                1000,
                4);

        BioRecipes.mEthanol = FluidUtils.getFluidStack("bioethanol", 1).getFluid();

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(20),
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
                6000,
                5);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(21),
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
                6000,
                5);

        BioRecipes.mFormaldehyde = FluidUtils.getFluidStack("fluid.formaldehyde", 1).getFluid();

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(22),
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
                10*20,
                8000,
                5);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] {
                        CI.getNumberedCircuit(23),
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
                500,
                3);
    }

    private static void chemicalReactorRecipes() {
        GT_Values.RA.addChemicalRecipe(
                CI.getNumberedCircuit(17),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSodiumHydroxide", 1),
                MISC_MATERIALS.HYDROGEN_CYANIDE.getFluidStack(200),
                FluidUtils.getWater(200),
                MISC_MATERIALS.SODIUM_CYANIDE.getDust(1),
                10 * 20);

        if (LoadedMods.BartWorks) {
            GT_Values.RA.addChemicalRecipe(
                    CI.getNumberedCircuit(18),
                    ItemUtils.getSimpleStack(ModItems.dustCalciumCarbonate, 1),
                    Materials.SaltWater.getFluid(1000L),
                    FluidUtils.getWater(1000),
                    Materials.SodiumCarbonate.getDust(1),
                    ItemUtils.getItemStackFromFQRN("bartworks:gt.bwMetaGenerateddust:63", 1),
                    5 * 20);
        }

        else {
            GT_Values.RA.addChemicalRecipe(
                    CI.getNumberedCircuit(18),
                    ItemUtils.getSimpleStack(ModItems.dustCalciumCarbonate, 1),
                    Materials.SaltWater.getFluid(1000L),
                    FluidUtils.getWater(1000),
                    Materials.SodiumCarbonate.getDust(1),
                    MISC_MATERIALS.CALCIUM_CHLORIDE.getDust(1),
                    5 * 20);
        }

        GT_Values.RA.addChemicalRecipe(
                CI.getNumberedCircuit(19),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 1),
                FluidUtils.getFluidStack("sulfuricacid", 500),
                FluidUtils.getFluidStack("dilutedsulfuricacid", 500),
                MISC_MATERIALS.COPPER_SULFATE.getDust(1),
                5 * 20);
    }

    private static void dehydratorRecipes() {
        CORE.RA.addDehydratorRecipe(
                new ItemStack[]{
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
                10);
    }

    private static void distillationTowerRecipes() {
        GT_Values.RA.addDistillationTowerRecipe(
                MISC_MATERIALS.CHLOROACETIC_MIXTURE.getFluidStack(1000),
                new FluidStack[] {
                        MISC_MATERIALS.CHLOROACETIC_ACID.getFluidStack(100),
                        MISC_MATERIALS.DICHLOROACETIC_ACID.getFluidStack(450),
                        MISC_MATERIALS.TRICHLOROACETIC_ACID.getFluidStack(450)
                },
                null,
                4 * 20,
                MaterialUtils.getVoltageForTier(5));
    }

    private static void electrolyzerRecipes() {
        if (!LoadedMods.BartWorks) {
            GT_Values.RA.addElectrolyzerRecipe(
                    CI.getNumberedCircuit(1),
                    MISC_MATERIALS.CALCIUM_CHLORIDE.getDust(3),
                    null,
                    Materials.Chlorine.getFluid(2000), // Out
                    Materials.Calcium.getDust(1),
                    null,
                    null,
                    null,
                    null,
                    null,
                    new int[]{10000, 10000, 10000},
                    20 * 30,
                    120);
        }
    }

    private static void fluidHeaterRecipes() {

        CORE.RA.addFluidHeaterRecipe(
                CI.getNumberedCircuit(16),
                MISC_MATERIALS.CYANOACRYLATE_POLYMER.getFluidStack(100),
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(100),
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
                100,
                100);

        GT_Values.RA.addMixerRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("cellSulfurTrioxide", 1),
                CI.getNumberedCircuit(2),
                null,
                null,
                FluidUtils.getFluidStack("sulfuricacid", 1000),
                MISC_MATERIALS.SOLID_ACID_MIXTURE.getFluidStack(1000),
                CI.emptyCells(1),
                100,
                40);
    }

    private static void glueUsageRecipes() {
        // Braintech Tape recipe, PBI and superglue make 16 tape at once
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 1L),
                        GT_ModHandler.getIC2Item("carbonMesh", 1L),
                        CI.getNumberedCircuit(10)
                },
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(100),
                ItemList.Duct_Tape.get(16L),
                120,
                30
        );

        // Graphene recipes from later wafer tiers, using superglue instead of the bronze age glue
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemUtils.getItemStackOfAmountFromOreDict("dustGraphite", 32),
                        ItemList.Circuit_Silicon_Wafer4.get(1L),
                        CI.getNumberedCircuit(2)
                },
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(200),
                ItemUtils.getItemStackOfAmountFromOreDict("dustGraphene", 32),
                120,
                30
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemUtils.getItemStackOfAmountFromOreDict("dustGraphite", 64),
                        ItemList.Circuit_Silicon_Wafer5.get(1L),
                        CI.getNumberedCircuit(2)
                },
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(200),
                ItemUtils.getItemStackOfAmountFromOreDict("dustGraphene", 64),
                120,
                30
        );

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L),
                GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(100),
                null,
                ItemList.SFMixture.get(32),
                1600,
                16
        );

        GT_Values.RA.addMixerRecipe(
                ItemList.GelledToluene.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Polybenzimidazole, 1L),
                GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(100),
                null,
                ItemList.SFMixture.get(64),
                1600,
                16
        );

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyvinylChloride,8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 32),
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(200),
                new ItemStack(Items.book,64,0),
                32,
                8
        );

        if (LoadedMods.TecTech) {
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedSteel, 18),
                    MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(144),
                    ItemUtils.getItemStackFromFQRN("gregtech:gt.metaitem.01:32505", 1),
                    300,
                    7680
            );

            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 6),
                    MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(288),
                    ItemUtils.getItemStackFromFQRN("gregtech:gt.metaitem.01:32506", 1),
                    200,
                    30720
            );

            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.ElectrumFlux, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 18),
                    MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(576),
                    ItemUtils.getItemStackFromFQRN("gregtech:gt.metaitem.01:32507", 1),
                    300,
                    122880
            );

            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.ElectrumFlux, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Naquadah, 24),
                    MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(1152),
                    ItemUtils.getItemStackFromFQRN("gregtech:gt.metaitem.01:32561", 1),
                    100,
                    491520
            );

            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.ElectrumFlux, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahEnriched, 36),
                    MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(2304),
                    ItemUtils.getItemStackFromFQRN("gregtech:gt.metaitem.01:32562", 1),
                    200,
                    1966080
            );

            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt08, Materials.ElectrumFlux, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 48),
                    MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(4608),
                    ItemUtils.getItemStackFromFQRN("gregtech:gt.metaitem.01:32563", 1),
                    300,
                    1966080
            );

            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 56),
                    MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(9216),
                    ItemUtils.getItemStackFromFQRN("gregtech:gt.metaitem.01:32564", 1),
                    600,
                    1966080
            );

            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 64),
                    MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(18432),
                    ItemUtils.getItemStackFromFQRN("gregtech:gt.metaitem.01:32565", 1),
                    1200,
                    1966080
            );
        }
        if (LoadedMods.DreamCraft && LoadedMods.GalacticraftCore) {
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[]{
                            ItemUtils.getItemStackFromFQRN("GalacticraftMars:item.itemBasicAsteroids:7", 1),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Titanium, 8),
                            ItemUtils.getItemStackFromFQRN("dreamcraft:item.TungstenString", 8),
                            CI.getNumberedCircuit(1)
                    },
                    MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(576),
                    ItemUtils.getItemStackFromFQRN("GalaxySpace:item.ThermalClothT2", 1),
                    600,
                    1024
            );
        }
    }
}
