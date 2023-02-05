package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_ModHandler.getModItem;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class AutoclaveRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addAutoclaveRecipe(
                ItemList.IC2_Energium_Dust.get(9L),
                Materials.EnergeticAlloy.getMolten(288),
                ItemList.IC2_EnergyCrystal.get(1L),
                10000,
                600,
                256);
        GT_Values.RA.addAutoclaveRecipe(
                ItemList.IC2_Energium_Dust.get(9L),
                Materials.ConductiveIron.getMolten(576),
                ItemList.IC2_EnergyCrystal.get(1L),
                10000,
                1200,
                256);
        GT_Values.RA.addAutoclaveRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 0),
                Materials.Water.getFluid(200L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 10),
                8000,
                2000,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 600),
                Materials.Water.getFluid(200L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 11),
                8000,
                2000,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 1200),
                Materials.Water.getFluid(200L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 12),
                8000,
                2000,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 0),
                GT_ModHandler.getDistilledWater(100L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 10),
                9000,
                1000,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 600),
                GT_ModHandler.getDistilledWater(100L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 11),
                9000,
                1000,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 1200),
                GT_ModHandler.getDistilledWater(100L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 12),
                9000,
                1000,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 0),
                Materials.Void.getMolten(36L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 10),
                10000,
                500,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 600),
                Materials.Void.getMolten(36L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 11),
                10000,
                500,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 1200),
                Materials.Void.getMolten(36L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 12),
                10000,
                500,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 32),
                Materials.Polybenzimidazole.getMolten(36L),
                GT_ModHandler.getIC2Item("carbonFiber", 64L),
                10000,
                150,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64),
                Materials.Epoxid.getMolten(144L),
                GT_ModHandler.getIC2Item("carbonFiber", 64L),
                10000,
                300,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64),
                Materials.Polytetrafluoroethylene.getMolten(288L),
                GT_ModHandler.getIC2Item("carbonFiber", 32L),
                10000,
                400,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64),
                Materials.Plastic.getMolten(576L),
                GT_ModHandler.getIC2Item("carbonFiber", 16L),
                10000,
                600,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 1),
                Materials.UUMatter.getFluid(576L),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1),
                3333,
                72000,
                (int) TierEU.RECIPE_HV);

        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(ItemList.QuantumStar.get(1L)),
                Materials.Neutronium.getMolten(288),
                ItemList.Gravistar.get(1L),
                10000,
                480,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(ItemList.Gravistar.get(16L)),
                Materials.Infinity.getMolten(288),
                ItemList.NuclearStar.get(1L),
                10000,
                480,
                7864320);

        // SiO2 ->Quartzite
        GT_Values.RA.addAutoclaveRecipe(
                Materials.SiliconDioxide.getDust(1),
                Materials.Water.getFluid(200L),
                Materials.Quartzite.getGems(1),
                750,
                2000,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                Materials.SiliconDioxide.getDust(1),
                GT_ModHandler.getDistilledWater(100L),
                Materials.Quartzite.getGems(1),
                1000,
                1500,
                24);
        GT_Values.RA.addAutoclaveRecipe(
                Materials.SiliconDioxide.getDust(1),
                Materials.Void.getMolten(36L),
                Materials.Quartzite.getGems(1),
                10000,
                1000,
                24);

        GT_Values.RA.addAutoclave4Recipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Water.getFluid(1000L),
                GT_Values.NF,
                new ItemStack[] { GT_Values.NI,
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3) },
                new int[] { 0, 7500 },
                1200,
                (int) TierEU.RECIPE_LV,
                false);
        GT_Values.RA.addAutoclave4Recipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getDistilledWater(1000L),
                GT_Values.NF,
                new ItemStack[] { GT_Values.NI,
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3) },
                new int[] { 0, 9000 },
                1200,
                (int) TierEU.RECIPE_LV,
                false);
    }
}
