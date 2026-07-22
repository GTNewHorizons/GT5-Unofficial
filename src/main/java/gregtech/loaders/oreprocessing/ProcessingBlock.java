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
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
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
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        Materials legacyMaterial = MU.materialOf(material);
        if (legacyMaterial == null) return;

        if (material == MU.material(Materials.Ichorium) || material == MU.material(Materials.NetherQuartz)) {
            return;
        }

        Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV
            && GTOreDictUnificator.get(OrePrefixes.plate, material, 1L) != null) {

            if (material == MU.material(Materials.Livingrock) || material == MU.material(Materials.Livingwood)
                || material == MU.material(Materials.Dreamwood)) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                    .fluidInputs(
                        Materials.Water.getFluid(
                            Math.max(
                                4,
                                Math.min(
                                    1000,
                                    ((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 320))))
                    .duration(2 * ((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS)
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
                                    ((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 426))))
                    .duration(2 * ((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS)
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
                                    ((int) Math.max(legacyMaterial.getMass() * 10, 1)) * TICKS * 30 / 1280)))))
                    .duration(((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(
                            Math.max(
                                1,
                                Math.min(
                                    10,
                                    ((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 4000))))
                    .duration(((int) Math.max(legacyMaterial.getMass() * 10L / 2.5, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(cutterRecipes);

            }

            else if (material != MU.material(Materials.Clay) && material != MU.material(Materials.Basalt)
                && material != MU.material(Materials.Obsidian)) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                        .fluidInputs(
                            Materials.Water.getFluid(
                                Math.max(
                                    4,
                                    Math.min(
                                        1000,
                                        ((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 320))))
                        .duration(2 * ((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS)
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
                                        ((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 426))))
                        .duration(2 * ((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS)
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
                                        ((int) Math.max(legacyMaterial.getMass() * 10, 1)) * TICKS * 30 / 1280)))))
                        .duration(((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS)
                        .eut(calculateRecipeEU(material, 16))
                        .addTo(cutterRecipes);

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
                        .fluidInputs(
                            Materials.DimensionallyShiftedSuperfluid.getFluid(
                                Math.max(
                                    1,
                                    Math.min(
                                        10,
                                        ((int) Math.max(legacyMaterial.getMass() * 10L, 1L)) * TICKS * 30 / 4000))))
                        .duration(((int) Math.max(legacyMaterial.getMass() * 10L / 2.5, 1L)) * TICKS)
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

        if (MU.hasMolten(material)) {
            if (!(material == MU.material(Materials.AnnealedCopper) || material == MU.material(Materials.CastIron)
                || material == MU.material(Materials.Obsidian))) {
                if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Block.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, material, 1L))
                        .fluidInputs(MU.molten(material, 9 * INGOTS))
                        .duration(legacyMaterial.getMass() * 9 * TICKS)
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

        if (ingot != null && !OrePrefixes.block.isIgnored(legacyMaterial)
            && material != MU.material(Materials.Obsidian)) {
            // 9 ingots -> 1 block
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, material, 9L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, material, 1L))
                .duration(legacyMaterial.getMass() * 2 * TICKS)
                .eut(calculateRecipeEU(material, 2))
                .addTo(compressorRecipes);
        }

        if (MU.internalName(material)
            .equals("Mercury")) {
            System.err.println(
                "'blockQuickSilver'?, In which Ice Desert can you actually place this as a solid Block? On Pluto Greg :)");
        }
    }
}
