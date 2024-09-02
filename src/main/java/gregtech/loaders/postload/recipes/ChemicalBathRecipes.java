package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.BuildCraftTransport;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class ChemicalBathRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Food_Raw_Fries.get(1))
            .itemOutputs(ItemList.Food_Fries.get(1))
            .fluidInputs(Materials.FryingOilHot.getFluid(10))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("dynamite", 1))
            .itemOutputs(GTModHandler.getIC2Item("stickyDynamite", 1))
            .fluidInputs(Materials.Glue.getFluid(10))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1))
            .itemOutputs(GTModHandler.getIC2Item("reinforcedStone", 1))
            .fluidInputs(Materials.Concrete.getMolten(144))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1))
            .fluidInputs(Materials.Water.getFluid(125))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        // paper creation recipes
        ItemStack[] paperSources = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Paper, 1L), new ItemStack(Items.reeds, 1, 32767) };
        for (ItemStack paperSource : paperSources) {
            GTValues.RA.stdBuilder()
                .itemInputs(paperSource)
                .itemOutputs(new ItemStack(Items.paper, 1, 0))
                .fluidInputs(Materials.Water.getFluid(100))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(paperSource)
                .itemOutputs(new ItemStack(Items.paper, 1, 0))
                .fluidInputs(GTModHandler.getDistilledWater(100))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1))
            .fluidInputs(GTModHandler.getDistilledWater(125))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        for (int i = 1; i < 16; i++) {
            // wool cleaning recipes
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.wool, 1, i))
                .itemOutputs(new ItemStack(Blocks.wool, 1, 0))
                .fluidInputs(Materials.Chlorine.getGas(50))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(chemicalBathRecipes);

            // carpet cleaning recipes
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.carpet, 1, 32767))
                .itemOutputs(new ItemStack(Blocks.carpet, 1, 0))
                .fluidInputs(Materials.Chlorine.getGas(25))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(chemicalBathRecipes);
        }

        // stained hardened clay cleaning
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_hardened_clay, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.hardened_clay, 1, 0))
            .fluidInputs(Materials.Chlorine.getGas(50))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(chemicalBathRecipes);

        // stained glass cleaning
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_glass, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.glass, 1, 0))
            .fluidInputs(Materials.Chlorine.getGas(50))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(chemicalBathRecipes);

        // stained glass pane cleaning
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_glass_pane, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.glass_pane, 1, 0))
            .fluidInputs(Materials.Chlorine.getGas(20))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(chemicalBathRecipes);

        // light -> dark concrete recipes
        for (int i = 0; i < 8; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, i + 8))
                .itemOutputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, i))
                .fluidInputs(Materials.Water.getFluid(250))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, i + 8))
                .itemOutputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, i))
                .fluidInputs(GTModHandler.getDistilledWater(250))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);
        }

        // reinforced blocks
        {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 1))
                .itemOutputs(ItemList.Block_BronzePlate.get(1))
                .fluidInputs(Materials.Concrete.getMolten(144))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1))
                .itemOutputs(ItemList.Block_SteelPlate.get(1))
                .fluidInputs(Materials.Steel.getMolten(288))
                .duration(12 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1))
                .itemOutputs(ItemList.Block_TitaniumPlate.get(1))
                .fluidInputs(Materials.Titanium.getMolten(144))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1))
                .itemOutputs(ItemList.Block_TungstenSteelReinforced.get(1))
                .fluidInputs(Materials.TungstenSteel.getMolten(144))
                .duration(17 * SECONDS + 10 * TICKS)
                .eut(64)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1))
                .itemOutputs(ItemList.Block_IridiumTungstensteel.get(1))
                .fluidInputs(Materials.Iridium.getMolten(144))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah, 1))
                .itemOutputs(ItemList.Block_NaquadahPlate.get(1))
                .fluidInputs(Materials.Osmium.getMolten(144))
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(256)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1))
                .itemOutputs(ItemList.Block_NeutroniumPlate.get(1))
                .fluidInputs(Materials.Naquadria.getMolten(144))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1))
                .itemOutputs(ItemList.Block_TungstenSteelReinforced.get(1))
                .fluidInputs(Materials.Concrete.getMolten(144))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);
        }

        for (int j = 0; j < Dyes.dyeRed.getSizeOfFluidList(); j++) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 0))
                .fluidInputs(Dyes.dyeRed.getFluidDye(j, 72))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(16)
                .addTo(chemicalBathRecipes);
        }
        for (int j = 0; j < Dyes.dyeBlue.getSizeOfFluidList(); j++) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 1))
                .fluidInputs(Dyes.dyeBlue.getFluidDye(j, 72))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(16)
                .addTo(chemicalBathRecipes);
        }
        for (int j = 0; j < Dyes.dyeGreen.getSizeOfFluidList(); j++) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 2))
                .fluidInputs(Dyes.dyeGreen.getFluidDye(j, 72))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(16)
                .addTo(chemicalBathRecipes);
        }
        for (int j = 0; j < Dyes.dyeYellow.getSizeOfFluidList(); j++) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 3))
                .fluidInputs(Dyes.dyeYellow.getFluidDye(j, 72))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(16)
                .addTo(chemicalBathRecipes);
        }
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            for (int j = 0; j < Dyes.VALUES[i].getSizeOfFluidList(); j++) {
                if (i != 15) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(new ItemStack(Blocks.wool, 1, 0))
                        .itemOutputs(new ItemStack(Blocks.wool, 1, 15 - i))
                        .fluidInputs(Dyes.VALUES[i].getFluidDye(j, 72))
                        .duration(3 * SECONDS + 4 * TICKS)
                        .eut(2)
                        .addTo(chemicalBathRecipes);
                }

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.glass, 1, 0))
                    .itemOutputs(new ItemStack(Blocks.stained_glass, 1, 15 - i))
                    .fluidInputs(Dyes.VALUES[i].getFluidDye(j, 18))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(2)
                    .addTo(chemicalBathRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.hardened_clay, 1, 0))
                    .itemOutputs(new ItemStack(Blocks.stained_hardened_clay, 1, 15 - i))
                    .fluidInputs(Dyes.VALUES[i].getFluidDye(j, 18))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(2)
                    .addTo(chemicalBathRecipes);
            }
        }

        // Rn relate quantum recipe
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.EnderEye, 1))
            .itemOutputs(ItemList.QuantumEye.get(1))
            .fluidInputs(Materials.Radon.getGas(250))
            .duration(24 * SECONDS)
            .eut(384)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1))
            .itemOutputs(ItemList.QuantumStar.get(1))
            .fluidInputs(Materials.Radon.getGas(1250))
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(384)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.WovenKevlar.get(1))
            .itemOutputs(MaterialsKevlar.Kevlar.getPlates(1))
            .fluidInputs(MaterialsKevlar.PolyurethaneResin.getFluid(1000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.CubicZirconia.get(OrePrefixes.gemExquisite, 1))
            .itemOutputs(Materials.Firestone.getGems(1))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2hotcoolant"), 250))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(chemicalBathRecipes);

        // Na + H2O = NaOH + H
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sodium.getDust(1))
            .itemOutputs(Materials.SodiumHydroxide.getDust(3))
            .fluidInputs(Materials.Water.getFluid(1000))
            .fluidOutputs(Materials.Hydrogen.getGas(1000))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        // Custom Sodium Persulfate Ore Processing Recipes

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Tantalite, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Tantalite, 1),
                Materials.Tantalum.getDust(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 3000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Pyrolusite, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Pyrolusite, 1),
                Materials.Manganese.getDust(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 7000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Quartzite, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Quartzite, 1),
                Materials.CertusQuartz.getDust(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 3000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.CertusQuartz, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.CertusQuartz, 1),
                Materials.Barium.getDust(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 7000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Bauxite, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Bauxite, 1),
                Materials.Rutile.getDust(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 5000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Thorium, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Thorium, 1),
                Materials.Uranium.getDust(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 3000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Stibnite, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Stibnite, 1),
                Materials.Antimony.getDust(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .outputChances(10000, 5000, 4000)
            .fluidInputs(Materials.SodiumPersulfate.getFluid(100))
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(chemicalBathRecipes);
    }
}
