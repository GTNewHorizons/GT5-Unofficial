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

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingBlock implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingBlock INSTANCE;

    public ProcessingBlock() {
        INSTANCE = this;
        OrePrefixes.block.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (material == Materials2Materials.Ichorium || material == Materials2Materials.NetherQuartz) {
            return;
        }

        Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV
            && GTOreDictUnificator.get(OrePrefixes.plate, material, 1L) != null) {

            if (material == Materials2Materials.Livingrock || material == Materials2Materials.Livingwood
                || material == Materials2Materials.Dreamwood) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                    .fluidInputs(
                        GTUtility.getWater(
                            Math.max(
                                4,
                                Math.min(
                                    1000,
                                    ((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS * 30 / 320))))
                    .duration(2 * ((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                    .fluidInputs(
                        GTModHandler.getDistilledWater(
                            Math.max(
                                3,
                                Math.min(
                                    750,
                                    ((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS * 30 / 426))))
                    .duration(2 * ((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.Lubricant,
                            Materials2FluidShapes.fluidLiquid,
                            (int) (Math.max(
                                1,
                                Math.min(
                                    250,
                                    ((int) Math.max(MaterialUtils.mass(material) * 10, 1)) * TICKS * 30 / 1280)))))
                    .duration(((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                    .fluidInputs(
                        MaterialUtils.fluid(
                            Materials2Materials.dimensionallyshiftedsuperfluid,
                            Math.max(
                                1,
                                Math.min(
                                    10,
                                    ((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS * 30 / 4000))))
                    .duration(((int) Math.max(MaterialUtils.mass(material) * 10L / 2.5, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(cutterRecipes);

            }

            else if (material != Materials2Materials.Clay && material != Materials2Materials.Basalt
                && material != Materials2Materials.Obsidian) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                        .fluidInputs(
                            GTUtility.getWater(
                                Math.max(
                                    4,
                                    Math.min(
                                        1000,
                                        ((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS * 30 / 320))))
                        .duration(2 * ((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(cutterRecipes);

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                        .fluidInputs(
                            GTModHandler.getDistilledWater(
                                Math.max(
                                    3,
                                    Math.min(
                                        750,
                                        ((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS * 30 / 426))))
                        .duration(2 * ((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS)
                        .eut(calculateRecipeEU(material, 16))
                        .addTo(cutterRecipes);

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(
                                Materials2Materials.Lubricant,
                                Materials2FluidShapes.fluidLiquid,
                                (int) (Math.max(
                                    1,
                                    Math.min(
                                        250,
                                        ((int) Math.max(MaterialUtils.mass(material) * 10, 1)) * TICKS * 30 / 1280)))))
                        .duration(((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS)
                        .eut(calculateRecipeEU(material, 16))
                        .addTo(cutterRecipes);

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                        .fluidInputs(
                            MaterialUtils.fluid(
                                Materials2Materials.dimensionallyshiftedsuperfluid,
                                Math.max(
                                    1,
                                    Math.min(
                                        10,
                                        ((int) Math.max(MaterialUtils.mass(material) * 10L, 1L)) * TICKS * 30 / 4000))))
                        .duration(((int) Math.max(MaterialUtils.mass(material) * 10L / 2.5, 1L)) * TICKS)
                        .eut(calculateRecipeEU(material, 16))
                        .addTo(cutterRecipes);
                }
        }

        ItemStack ingot = GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L);
        ItemStack gem = GTOreDictUnificator.get(OrePrefixes.gem, material, 1L);
        ItemStack dust = GTOreDictUnificator.get(OrePrefixes.dust, material, 1L);

        GTModHandler.removeRecipeDelayed(GTUtility.copyAmount(1, stack));

        if (ingot != null) {
            GTModHandler.removeRecipeDelayed(ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot);
        }
        if (gem != null) {
            GTModHandler.removeRecipeDelayed(gem, gem, gem, gem, gem, gem, gem, gem, gem);
        }
        if (dust != null) {
            GTModHandler.removeRecipeDelayed(dust, dust, dust, dust, dust, dust, dust, dust, dust);
        }

        if (MaterialUtils.hasMolten(material)) {
            if (!(material == Materials2Materials.AnnealedCopper || material == Materials2Materials.CastIron
                || material == Materials2Materials.Obsidian)) {
                if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Block.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, material, 1L))
                        .fluidInputs(MaterialUtils.molten(material, 9 * INGOTS))
                        .duration(MaterialUtils.mass(material) * 9 * TICKS)
                        .eut(calculateRecipeEU(material, 8))
                        .addTo(fluidSolidifierRecipes);
                }
            }
        }

        if (ingot != null) ingot.stackSize = 9;
        if (gem != null) gem.stackSize = 9;
        if (dust != null) dust.stackSize = 9;

        if (gem != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(stack)
                .itemOutputs(gem)
                .duration(5 * SECONDS)
                .eut(24)
                .addTo(hammerRecipes);
        }

        if (ingot != null && !OrePrefixes.block.isIgnored(material) && material != Materials2Materials.Obsidian) {
            // 9 ingots -> 1 block
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, material, 9L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, material, 1L))
                .duration(MaterialUtils.mass(material) * 2 * TICKS)
                .eut(calculateRecipeEU(material, 2))
                .addTo(compressorRecipes);
        }

        if (MaterialUtils.internalName(material)
            .equals("Mercury")) {
            System.err.println(
                "'blockQuickSilver'?, In which Ice Desert can you actually place this as a solid Block? On Pluto Greg :)");
        }
    }
}
