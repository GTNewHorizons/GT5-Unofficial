package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingBlock implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingBlock() {
        OrePrefixes.block.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {

        if (aMaterial == Materials.Ichorium || aMaterial == Materials.NetherQuartz) {
            return;
        }

        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV
            && GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {

            if (aMaterial == Materials.Livingrock || aMaterial == Materials.Livingwood
                || aMaterial == Materials.Dreamwood) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 9L))
                    .fluidInputs(
                        Materials.Water.getFluid(
                            Math.max(
                                4,
                                Math.min(1000, ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 320))))
                    .duration(2 * ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 9L))
                    .fluidInputs(
                        GTModHandler.getDistilledWater(
                            Math.max(
                                3,
                                Math.min(750, ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 426))))
                    .duration(2 * ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 9L))
                    .fluidInputs(
                        Materials.Lubricant.getFluid(
                            Math.max(
                                1,
                                Math.min(250, ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 1280))))
                    .duration(((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 9L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(
                            Math.max(
                                1,
                                Math.min(10, ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 4000))))
                    .duration(((int) Math.max(aMaterial.getMass() * 10L / 2.5, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

            }

            else if (aMaterial != Materials.Clay && aMaterial != Materials.Basalt && aMaterial != Materials.Obsidian) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 9L))
                    .fluidInputs(
                        Materials.Water.getFluid(
                            Math.max(
                                4,
                                Math.min(1000, ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 320))))
                    .duration(2 * ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 9L))
                    .fluidInputs(
                        GTModHandler.getDistilledWater(
                            Math.max(
                                3,
                                Math.min(750, ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 426))))
                    .duration(2 * ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 9L))
                    .fluidInputs(
                        Materials.Lubricant.getFluid(
                            Math.max(
                                1,
                                Math.min(250, ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 1280))))
                    .duration(((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 9L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(
                            Math.max(
                                1,
                                Math.min(10, ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 4000))))
                    .duration(((int) Math.max(aMaterial.getMass() * 10L / 2.5, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);
            }
        }

        ItemStack ingot = GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L);
        ItemStack gem = GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
        ItemStack dust = GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L);

        GTModHandler.removeRecipeDelayed(GTUtility.copyAmount(1, aStack));

        if (ingot != null) {
            GTModHandler.removeRecipeDelayed(ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot);
        }
        if (gem != null) {
            GTModHandler.removeRecipeDelayed(gem, gem, gem, gem, gem, gem, gem, gem, gem);
        }
        if (dust != null) {
            GTModHandler.removeRecipeDelayed(dust, dust, dust, dust, dust, dust, dust, dust, dust);
        }

        if (aMaterial.mStandardMoltenFluid != null) {
            if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron
                || aMaterial == Materials.Obsidian)) {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Block.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, aMaterial, 1L))
                        .fluidInputs(aMaterial.getMolten(9 * INGOTS))
                        .duration(aMaterial.getMass() * 9 * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8))
                        .addTo(fluidSolidifierRecipes);
                }
            }
        }

        if (ingot != null) ingot.stackSize = 9;
        if (gem != null) gem.stackSize = 9;
        if (dust != null) dust.stackSize = 9;

        if (gem != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(aStack)
                .itemOutputs(gem)
                .duration(5 * SECONDS)
                .eut(24)
                .addTo(hammerRecipes);
        }

        if (ingot != null && !OrePrefixes.block.isIgnored(aMaterial) && aMaterial != Materials.Obsidian) {
            // 9 ingots -> 1 block
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 9L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, aMaterial, 1L))
                .duration(aMaterial.getMass() * 2 * TICKS)
                .eut(calculateRecipeEU(aMaterial, 2))
                .addTo(compressorRecipes);
        }

        if (aMaterial.mName.equals("Mercury")) {
            System.err.println(
                "'blockQuickSilver'?, In which Ice Desert can you actually place this as a solid Block? On Pluto Greg :)");
        }
    }
}
