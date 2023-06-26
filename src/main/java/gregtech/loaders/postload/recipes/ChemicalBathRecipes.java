package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.BuildCraftTransport;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class ChemicalBathRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Raw_Fries.get(1))
            .itemOutputs(ItemList.Food_Fries.get(1))
            .fluidInputs(Materials.FryingOilHot.getFluid(10))
            .noFluidOutputs()
            .duration(16 * TICKS)
            .eut(4)
            .addTo(sChemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getIC2Item("dynamite", 1))
            .itemOutputs(GT_ModHandler.getIC2Item("stickyDynamite", 1))
            .fluidInputs(Materials.Glue.getFluid(10))
            .noFluidOutputs()
            .duration(16 * TICKS)
            .eut(4)
            .addTo(sChemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1))
            .itemOutputs(GT_ModHandler.getIC2Item("reinforcedStone", 1))
            .fluidInputs(Materials.Concrete.getMolten(144))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(sChemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1))
            .fluidInputs(Materials.Water.getFluid(125))
            .noFluidOutputs()
            .duration(12 * TICKS)
            .eut(4)
            .addTo(sChemicalBathRecipes);

        // paper creation recipes
        ItemStack[] paperSources = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Paper, 1L), new ItemStack(Items.reeds, 1, 32767) };
        for (ItemStack paperSource : paperSources) {
            GT_Values.RA.stdBuilder()
                .itemInputs(paperSource)
                .itemOutputs(new ItemStack(Items.paper, 1, 0))
                .fluidInputs(Materials.Water.getFluid(100))
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(paperSource)
                .itemOutputs(new ItemStack(Items.paper, 1, 0))
                .fluidInputs(GT_ModHandler.getDistilledWater(100))
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(sChemicalBathRecipes);
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1))
            .fluidInputs(GT_ModHandler.getDistilledWater(125))
            .noFluidOutputs()
            .duration(12 * TICKS)
            .eut(4)
            .addTo(sChemicalBathRecipes);

        for (int i = 1; i < 16; i++) {
            // wool cleaning recipes
            GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.wool, 1, i))
                .itemOutputs(new ItemStack(Blocks.wool, 1, 0))
                .fluidInputs(Materials.Chlorine.getGas(50))
                .noFluidOutputs()
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sChemicalBathRecipes);

            // carpet cleaning recipes
            GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.carpet, 1, 32767))
                .itemOutputs(new ItemStack(Blocks.carpet, 1, 0))
                .fluidInputs(Materials.Chlorine.getGas(25))
                .noFluidOutputs()
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sChemicalBathRecipes);
        }

        // stained hardened clay cleaning
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_hardened_clay, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.hardened_clay, 1, 0))
            .fluidInputs(Materials.Chlorine.getGas(50))
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sChemicalBathRecipes);

        // stained glass cleaning
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_glass, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.glass, 1, 0))
            .fluidInputs(Materials.Chlorine.getGas(50))
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sChemicalBathRecipes);

        // stained glass pane cleaning
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_glass_pane, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.glass_pane, 1, 0))
            .fluidInputs(Materials.Chlorine.getGas(20))
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(sChemicalBathRecipes);

        // light -> dark concrete recipes
        for (int i = 0; i < 8; i++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(GregTech_API.sBlockConcretes, 1, i + 8))
                .itemOutputs(new ItemStack(GregTech_API.sBlockConcretes, 1, i))
                .fluidInputs(Materials.Water.getFluid(250))
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(GregTech_API.sBlockConcretes, 1, i + 8))
                .itemOutputs(new ItemStack(GregTech_API.sBlockConcretes, 1, i))
                .fluidInputs(GT_ModHandler.getDistilledWater(250))
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(sChemicalBathRecipes);
        }

        // reinforced blocks
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 1))
                .itemOutputs(ItemList.Block_BronzePlate.get(1))
                .fluidInputs(Materials.Concrete.getMolten(144))
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1))
                .itemOutputs(ItemList.Block_SteelPlate.get(1))
                .fluidInputs(Materials.Steel.getMolten(288))
                .noFluidOutputs()
                .duration(12 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1))
                .itemOutputs(ItemList.Block_TitaniumPlate.get(1))
                .fluidInputs(Materials.Titanium.getMolten(144))
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1))
                .itemOutputs(ItemList.Block_TungstenSteelReinforced.get(1))
                .fluidInputs(Materials.TungstenSteel.getMolten(144))
                .noFluidOutputs()
                .duration(17 * SECONDS + 10 * TICKS)
                .eut(64)
                .addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1))
                .itemOutputs(ItemList.Block_IridiumTungstensteel.get(1))
                .fluidInputs(Materials.Iridium.getMolten(144))
                .noFluidOutputs()
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah, 1))
                .itemOutputs(ItemList.Block_NaquadahPlate.get(1))
                .fluidInputs(Materials.Osmium.getMolten(144))
                .noFluidOutputs()
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(256)
                .addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1))
                .itemOutputs(ItemList.Block_NeutroniumPlate.get(1))
                .fluidInputs(Materials.Naquadria.getMolten(144))
                .noFluidOutputs()
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1))
                .itemOutputs(ItemList.Block_TungstenSteelReinforced.get(1))
                .fluidInputs(Materials.Concrete.getMolten(144))
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(sChemicalBathRecipes);
        }

        for (int j = 0; j < Dyes.dyeRed.getSizeOfFluidList(); j++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 0))
                .fluidInputs(Dyes.dyeRed.getFluidDye(j, 72))
                .noFluidOutputs()
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(16)
                .addTo(sChemicalBathRecipes);
        }
        for (int j = 0; j < Dyes.dyeBlue.getSizeOfFluidList(); j++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 1))
                .fluidInputs(Dyes.dyeBlue.getFluidDye(j, 72))
                .noFluidOutputs()
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(16)
                .addTo(sChemicalBathRecipes);
        }
        for (int j = 0; j < Dyes.dyeGreen.getSizeOfFluidList(); j++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 2))
                .fluidInputs(Dyes.dyeGreen.getFluidDye(j, 72))
                .noFluidOutputs()
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(16)
                .addTo(sChemicalBathRecipes);
        }
        for (int j = 0; j < Dyes.dyeYellow.getSizeOfFluidList(); j++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 3))
                .fluidInputs(Dyes.dyeYellow.getFluidDye(j, 72))
                .noFluidOutputs()
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(16)
                .addTo(sChemicalBathRecipes);
        }
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            for (int j = 0; j < Dyes.VALUES[i].getSizeOfFluidList(); j++) {
                if (i != 15) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(new ItemStack(Blocks.wool, 1, 0))
                        .itemOutputs(new ItemStack(Blocks.wool, 1, 15 - i))
                        .fluidInputs(Dyes.VALUES[i].getFluidDye(j, 72))
                        .noFluidOutputs()
                        .duration(3 * SECONDS + 4 * TICKS)
                        .eut(2)
                        .addTo(sChemicalBathRecipes);
                }

                GT_Values.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.glass, 1, 0))
                    .itemOutputs(new ItemStack(Blocks.stained_glass, 1, 15 - i))
                    .fluidInputs(Dyes.VALUES[i].getFluidDye(j, 18))
                    .noFluidOutputs()
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(2)
                    .addTo(sChemicalBathRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.hardened_clay, 1, 0))
                    .itemOutputs(new ItemStack(Blocks.stained_hardened_clay, 1, 15 - i))
                    .fluidInputs(Dyes.VALUES[i].getFluidDye(j, 18))
                    .noFluidOutputs()
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(2)
                    .addTo(sChemicalBathRecipes);
            }
        }

        // Rn relate quantum recipe
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderEye, 1))
            .itemOutputs(ItemList.QuantumEye.get(1))
            .fluidInputs(Materials.Radon.getGas(250))
            .noFluidOutputs()
            .duration(24 * SECONDS)
            .eut(384)
            .addTo(sChemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1))
            .itemOutputs(ItemList.QuantumStar.get(1))
            .fluidInputs(Materials.Radon.getGas(1250))
            .noFluidOutputs()
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(384)
            .addTo(sChemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.WovenKevlar.get(1))
            .itemOutputs(MaterialsKevlar.Kevlar.getPlates(1))
            .fluidInputs(MaterialsKevlar.PolyurethaneResin.getFluid(1000))
            .noFluidOutputs()
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sChemicalBathRecipes);

        // Na + H2O = NaOH + H
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Sodium.getDust(1))
            .itemOutputs(Materials.SodiumHydroxide.getDust(3))
            .fluidInputs(Materials.Water.getFluid(1000))
            .fluidOutputs(Materials.Hydrogen.getGas(1000))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(sChemicalBathRecipes);

        // Custom Sodium Persulfate Ore Processing Recipes

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Tantalite, 1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Tantalite, 1),
                Materials.Tantalum.getDust(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 3000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(sChemicalBathRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Pyrolusite, 1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Pyrolusite, 1),
                Materials.Manganese.getDust(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 7000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(sChemicalBathRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Quartzite, 1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Quartzite, 1),
                Materials.CertusQuartz.getDust(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 3000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(sChemicalBathRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.CertusQuartz, 1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.CertusQuartz, 1),
                Materials.Barium.getDust(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 7000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(sChemicalBathRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Bauxite, 1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Bauxite, 1),
                Materials.Rutile.getDust(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 5000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(sChemicalBathRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Thorium, 1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Thorium, 1),
                Materials.Uranium.getDust(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 3000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(sChemicalBathRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Stibnite, 1))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Stibnite, 1),
                Materials.Antimony.getDust(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 5000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(sChemicalBathRecipes);
    }
}
