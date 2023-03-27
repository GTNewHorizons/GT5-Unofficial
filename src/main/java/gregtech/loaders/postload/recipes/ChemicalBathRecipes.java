package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.MOD_ID_RC;
import static gregtech.api.util.GT_ModHandler.getModItem;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class ChemicalBathRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addChemicalBathRecipe(
                ItemList.Food_Raw_Fries.get(1L),
                Materials.FryingOilHot.getFluid(10L),
                ItemList.Food_Fries.get(1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                16,
                4);
        GT_Values.RA.addChemicalBathRecipe(
                GT_ModHandler.getIC2Item("dynamite", 1L),
                Materials.Glue.getFluid(10L),
                GT_ModHandler.getIC2Item("stickyDynamite", 1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                16,
                4);

        GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1L),
                Materials.Concrete.getMolten(144L),
                GT_ModHandler.getIC2Item("reinforcedStone", 1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                200,
                4);
        GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L),
                Materials.Water.getFluid(125L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                12,
                4);

        // paper creation recipes
        ItemStack[] paperSources = new ItemStack[]{
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Paper, 1L),
            new ItemStack(Items.reeds, 1, 32767)
        };
        for (ItemStack paperSource: paperSources) {
            GT_Values.RA.addChemicalBathRecipe(
                paperSource,
                Materials.Water.getFluid(100L),
                new ItemStack(Items.paper, 1, 0),
                GT_Values.NI,
                GT_Values.NI,
                null,
                200,
                4);

            GT_Values.RA.addChemicalBathRecipe(
                paperSource,
                GT_ModHandler.getDistilledWater(100L),
                new ItemStack(Items.paper, 1, 0),
                GT_Values.NI,
                GT_Values.NI,
                null,
                200,
                4);
        }

        GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L),
                GT_ModHandler.getDistilledWater(125L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                12,
                4);

        for (int i=1;i<16;i++) {
            // wool cleaning recipes
            GT_Values.RA.addChemicalBathRecipe(
                new ItemStack(Blocks.wool, 1, i),
                Materials.Chlorine.getGas(50L),
                new ItemStack(Blocks.wool, 1, 0),
                GT_Values.NI,
                GT_Values.NI,
                null,
                400,
                2);

            // carpet cleaning recipes
            GT_Values.RA.addChemicalBathRecipe(
                new ItemStack(Blocks.carpet, 1, 32767),
                Materials.Chlorine.getGas(25L),
                new ItemStack(Blocks.carpet, 1, 0),
                GT_Values.NI,
                GT_Values.NI,
                null,
                400,
                2);
        }

        // stained hardened clay cleaning
        GT_Values.RA.addChemicalBathRecipe(
                new ItemStack(Blocks.stained_hardened_clay, 1, 32767),
                Materials.Chlorine.getGas(50L),
                new ItemStack(Blocks.hardened_clay, 1, 0),
                GT_Values.NI,
                GT_Values.NI,
                null,
                400,
                2);

        // stained glass cleaning
        GT_Values.RA.addChemicalBathRecipe(
                new ItemStack(Blocks.stained_glass, 1, 32767),
                Materials.Chlorine.getGas(50L),
                new ItemStack(Blocks.glass, 1, 0),
                GT_Values.NI,
                GT_Values.NI,
                null,
                400,
                2);

        // stained glass pane cleaning
        GT_Values.RA.addChemicalBathRecipe(
                new ItemStack(Blocks.stained_glass_pane, 1, 32767),
                Materials.Chlorine.getGas(20L),
                new ItemStack(Blocks.glass_pane, 1, 0),
                GT_Values.NI,
                GT_Values.NI,
                null,
                400,
                2);

        // light -> dark concrete recipes
        for (int i=0;i<8;i++) {
            GT_Values.RA.addChemicalBathRecipe(
                new ItemStack(GregTech_API.sBlockConcretes, 1, i+8),
                Materials.Water.getFluid(250L),
                new ItemStack(GregTech_API.sBlockConcretes, 1, i),
                GT_Values.NI,
                GT_Values.NI,
                null,
                200,
                4);


            GT_Values.RA.addChemicalBathRecipe(
                new ItemStack(GregTech_API.sBlockConcretes, 1, i+8),
                GT_ModHandler.getDistilledWater(250L),
                new ItemStack(GregTech_API.sBlockConcretes, 1, i),
                GT_Values.NI,
                GT_Values.NI,
                null,
                200,
                4);
        }

        // reinforced blocks
        {
            GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 1L),
                Materials.Concrete.getMolten(144L),
                ItemList.Block_BronzePlate.get(1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                200,
                4);
            GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1L),
                Materials.Steel.getMolten(288L),
                ItemList.Block_SteelPlate.get(1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                250,
                16);
            GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1L),
                Materials.Titanium.getMolten(144L),
                ItemList.Block_TitaniumPlate.get(1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                300,
                (int) TierEU.RECIPE_LV);
            GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L),
                Materials.TungstenSteel.getMolten(144L),
                ItemList.Block_TungstenSteelReinforced.get(1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                350,
                64);
            GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1L),
                Materials.Iridium.getMolten(144L),
                ItemList.Block_IridiumTungstensteel.get(1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                400,
                (int) TierEU.RECIPE_MV);
            GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah, 1L),
                Materials.Osmium.getMolten(144L),
                ItemList.Block_NaquadahPlate.get(1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                450,
                256);
            GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                Materials.Naquadria.getMolten(144L),
                ItemList.Block_NeutroniumPlate.get(1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                500,
                (int) TierEU.RECIPE_HV);

            GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L),
                Materials.Concrete.getMolten(144L),
                ItemList.Block_TungstenSteelReinforced.get(1L),
                GT_Values.NI,
                GT_Values.NI,
                null,
                200,
                4);
        }

        for (int j = 0; j < Dyes.dyeRed.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L),
                    Dyes.dyeRed.getFluidDye(j, 72L),
                    getModItem("BuildCraft|Transport", "pipeWire", 4L, 0),
                    GT_Values.NI,
                    GT_Values.NI,
                    null,
                    32,
                    16);
        }
        for (int j = 0; j < Dyes.dyeBlue.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L),
                    Dyes.dyeBlue.getFluidDye(j, 72L),
                    getModItem("BuildCraft|Transport", "pipeWire", 4L, 1),
                    GT_Values.NI,
                    GT_Values.NI,
                    null,
                    32,
                    16);
        }
        for (int j = 0; j < Dyes.dyeGreen.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L),
                    Dyes.dyeGreen.getFluidDye(j, 72L),
                    getModItem("BuildCraft|Transport", "pipeWire", 4L, 2),
                    GT_Values.NI,
                    GT_Values.NI,
                    null,
                    32,
                    16);
        }
        for (int j = 0; j < Dyes.dyeYellow.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L),
                    Dyes.dyeYellow.getFluidDye(j, 72L),
                    getModItem("BuildCraft|Transport", "pipeWire", 4L, 3),
                    GT_Values.NI,
                    GT_Values.NI,
                    null,
                    32,
                    16);
        }
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            for (int j = 0; j < Dyes.VALUES[i].getSizeOfFluidList(); j++) {
                if (i != 15) {
                    GT_Values.RA.addChemicalBathRecipe(
                            new ItemStack(Blocks.wool, 1, 0),
                            Dyes.VALUES[i].getFluidDye(j, 72L),
                            new ItemStack(Blocks.wool, 1, 15 - i),
                            GT_Values.NI,
                            GT_Values.NI,
                            null,
                            64,
                            2);
                }

                GT_Values.RA.addChemicalBathRecipe(
                        new ItemStack(Blocks.glass, 1, 0),
                        Dyes.VALUES[i].getFluidDye(j, 18L),
                        new ItemStack(Blocks.stained_glass, 1, 15 - i),
                        GT_Values.NI,
                        GT_Values.NI,
                        null,
                        64,
                        2);

                GT_Values.RA.addChemicalBathRecipe(
                        new ItemStack(Blocks.hardened_clay, 1, 0),
                        Dyes.VALUES[i].getFluidDye(j, 18L),
                        new ItemStack(Blocks.stained_hardened_clay, 1, 15 - i),
                        GT_Values.NI,
                        GT_Values.NI,
                        null,
                        64,
                        2);
            }
        }

        // Rn relate quantum recipe
        GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderEye, 1),
                Materials.Radon.getGas(250),
                ItemList.QuantumEye.get(1L),
                null,
                null,
                null,
                480,
                384);
        GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1),
                Materials.Radon.getGas(1250),
                ItemList.QuantumStar.get(1L),
                null,
                null,
                null,
                1920,
                384);

        GT_Values.RA.addChemicalBathRecipe(
                ItemList.WovenKevlar.get(1L),
                MaterialsKevlar.PolyurethaneResin.getFluid(1000),
                GT_Values.NF,
                MaterialsKevlar.Kevlar.getPlates(1),
                GT_Values.NI,
                GT_Values.NI,
                null,
                1200,
                (int) TierEU.RECIPE_LV);

        // Na + H2O = NaOH + H
        GT_Values.RA.addChemicalBathRecipe(
                Materials.Sodium.getDust(1),
                Materials.Water.getFluid(1000),
                Materials.Hydrogen.getGas(1000),
                Materials.SodiumHydroxide.getDust(3),
                GT_Values.NI,
                GT_Values.NI,
                null,
                100,
                4);

        // Custom Sodium Persulfate Ore Processing Recipes
        Materials[] materialList = new Materials[]{
            Materials.Tantalite,
            Materials.Pyrolusite,
            Materials.Quartzite,
            Materials.CertusQuartz,
            Materials.Bauxite,
            Materials.Thorium,
            Materials.Stibnite
        };

        for (Materials material : materialList){
            GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.crushed, material, 1),
                Materials.SodiumPersulfate.getFluid(100L),
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, material, 1),
                Materials.Tantalum.getDust(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L),
                new int[]{10000, 3000, 4000},
                800,
                8);
        }

        GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 1L),
                Materials.Creosote.getFluid(100L),
                getModItem(MOD_ID_RC, "cube", 1L, 8),
                GT_Values.NI,
                GT_Values.NI,
                null,
                100,
                4);
    }
}
