package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;
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

        if (aMaterial == Materials.Ichorium) {
            return;
        }

        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV
            && GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {

            if (aMaterial == MaterialsBotania.Livingrock || aMaterial == MaterialsBotania.Livingwood
                || aMaterial == MaterialsBotania.Dreamwood) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(3))
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
                    .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(3))
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
                    .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(3))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 9L))
                    .fluidInputs(
                        Materials.Lubricant.getFluid(
                            Math.max(
                                1,
                                Math.min(250, ((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 1280))))
                    .duration(((int) Math.max(aMaterial.getMass() * 10L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

            }

            else if (aMaterial != Materials.Clay && aMaterial != Materials.Basalt) {

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
            }
        }

        ItemStack tStack1 = GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L);
        ItemStack tStack2 = GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
        ItemStack tStack3 = GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L);

        GTModHandler.removeRecipeDelayed(GTUtility.copyAmount(1, aStack));

        if (tStack1 != null) GTModHandler
            .removeRecipeDelayed(tStack1, tStack1, tStack1, tStack1, tStack1, tStack1, tStack1, tStack1, tStack1);
        if (tStack2 != null) GTModHandler
            .removeRecipeDelayed(tStack2, tStack2, tStack2, tStack2, tStack2, tStack2, tStack2, tStack2, tStack2);
        if (tStack3 != null) {
            GTModHandler
                .removeRecipeDelayed(tStack3, tStack3, tStack3, tStack3, tStack3, tStack3, tStack3, tStack3, tStack3);
        }

        if (aMaterial.mStandardMoltenFluid != null) {
            if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Block.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, aMaterial, 1L))
                        .fluidInputs(aMaterial.getMolten(1296L))
                        .duration(14 * SECONDS + 8 * TICKS)
                        .eut(8)
                        .addTo(fluidSolidifierRecipes);
                }
            }
        }

        if (tStack1 != null) tStack1.stackSize = 9;
        if (tStack2 != null) tStack2.stackSize = 9;
        if (tStack3 != null) tStack3.stackSize = 9;

        if (tStack2 != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(aStack)
                .itemOutputs(tStack2)
                .duration(5 * SECONDS)
                .eut(24)
                .addTo(hammerRecipes);
        }

        if (tStack2 != null && aMaterial != Materials.NetherQuartz) {
            if (tStack3 != null)
                GTModHandler.addShapelessCraftingRecipe(tStack3, new Object[] { OrePrefixes.block.get(aMaterial) });
            GTModHandler.addShapelessCraftingRecipe(tStack2, new Object[] { OrePrefixes.block.get(aMaterial) });
            if (tStack1 != null)
                GTModHandler.addShapelessCraftingRecipe(tStack1, new Object[] { OrePrefixes.block.get(aMaterial) });
        }

        if (!OrePrefixes.block.isIgnored(aMaterial) && tStack1 != null) {
            // 9 ingots -> 1 block
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 9L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, aMaterial, 1L))
                .duration(15 * SECONDS)
                .eut(calculateRecipeEU(aMaterial, 2))
                .addTo(compressorRecipes);
        }

        switch (aMaterial.mName) {
            case "Mercury" -> System.err.println(
                "'blockQuickSilver'?, In which Ice Desert can you actually place this as a solid Block? On Pluto Greg :)");
        }
    }
}
