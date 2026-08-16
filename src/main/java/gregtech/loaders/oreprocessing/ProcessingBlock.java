package gregtech.loaders.oreprocessing;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableMap;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingBlock implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private static final int BLOCK_RECIPE_REMOVAL = 1;
    private static final int INGOT_RECIPE_REMOVAL = 1 << 1;
    private static final int GEM_RECIPE_REMOVAL = 1 << 2;
    private static final int DUST_RECIPE_REMOVAL = 1 << 3;

    private static final ImmutableMap<Materials, Integer> RECIPE_REMOVALS = ImmutableMap.<Materials, Integer>builder()
        .put(Materials.Aluminium, BLOCK_RECIPE_REMOVAL)
        .put(Materials.AluminiumBrass, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Alumite, BLOCK_RECIPE_REMOVAL)
        .put(Materials.Amber, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.Ardite, BLOCK_RECIPE_REMOVAL)
        .put(Materials.BloodInfusedIron, BLOCK_RECIPE_REMOVAL)
        .put(Materials.Bronze, BLOCK_RECIPE_REMOVAL)
        .put(Materials.ClayCompound, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Coal, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.Cobalt, BLOCK_RECIPE_REMOVAL)
        .put(Materials.ConductiveIron, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Copper, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.CosmicNeutronium, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.CrystallineAlloy, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.CrystallinePinkSlime, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.DarkSteel, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Diamond, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.ElectricalSteel, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Electrotine, BLOCK_RECIPE_REMOVAL | DUST_RECIPE_REMOVAL)
        .put(Materials.Emerald, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.EndSteel, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.EnergeticAlloy, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.EnergeticSilver, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Galgadorian, BLOCK_RECIPE_REMOVAL)
        .put(Materials.GalgadorianEnhanced, BLOCK_RECIPE_REMOVAL)
        .put(Materials.Gold, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Infinity, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Iron, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Lapis, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.Lead, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Malachite, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.Manyullyn, BLOCK_RECIPE_REMOVAL)
        .put(Materials.MelodicAlloy, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.NaquadahAlloy, INGOT_RECIPE_REMOVAL)
        .put(Materials.NetherStar, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.Olivine, BLOCK_RECIPE_REMOVAL)
        .put(Materials.PulsatingIron, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Redstone, BLOCK_RECIPE_REMOVAL | DUST_RECIPE_REMOVAL)
        .put(Materials.RedstoneAlloy, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Reinforced, BLOCK_RECIPE_REMOVAL)
        .put(Materials.Ruby, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.Salt, BLOCK_RECIPE_REMOVAL)
        .put(Materials.Sapphire, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.Shadow, BLOCK_RECIPE_REMOVAL)
        .put(Materials.Silver, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Soularium, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Steel, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.StellarAlloy, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Tanzanite, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.Thaumium, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Tin, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Topaz, BLOCK_RECIPE_REMOVAL | GEM_RECIPE_REMOVAL)
        .put(Materials.Unstable, INGOT_RECIPE_REMOVAL)
        .put(Materials.VibrantAlloy, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.VividAlloy, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .put(Materials.Void, BLOCK_RECIPE_REMOVAL | INGOT_RECIPE_REMOVAL)
        .build();

    private final Set<GTUtility.ItemId> queuedPackingRemovals = new HashSet<>();

    public ProcessingBlock() {
        OrePrefixes.block.add(this);
    }

    private void removePackingRecipe(ItemStack input) {
        if (input == null || (GTModHandler.isBufferingCraftingRecipes()
            && !queuedPackingRemovals.add(GTUtility.ItemId.createWithStackSize(input)))) {
            return;
        }

        removeRecipeIfPresent(input, input, input, input, input, input, input, input, input);
    }

    private static void removeRecipeIfPresent(ItemStack... shape) {
        if (!GTModHandler.hasRemovableRecipe(shape)) return;
        if (GTModHandler.isBufferingCraftingRecipes()) GTModHandler.removeRecipeDelayed(shape);
        else GTModHandler.removeRecipe(shape);
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
                    .eut(calculateRecipeEU(aMaterial, 16))
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
                    .eut(calculateRecipeEU(aMaterial, 16))
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
                    .eut(calculateRecipeEU(aMaterial, 16))
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
                    .eut(calculateRecipeEU(aMaterial, 16))
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
                    .eut(calculateRecipeEU(aMaterial, 16))
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
                    .eut(calculateRecipeEU(aMaterial, 16))
                    .addTo(cutterRecipes);
            }
        }

        ItemStack ingot = GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L);
        ItemStack gem = GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
        ItemStack dust = GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L);

        int recipeRemovals = RECIPE_REMOVALS.getOrDefault(aMaterial, 0);
        if ((recipeRemovals & BLOCK_RECIPE_REMOVAL) != 0) removeRecipeIfPresent(GTUtility.copyAmount(1, aStack));
        if ((recipeRemovals & INGOT_RECIPE_REMOVAL) != 0) removePackingRecipe(ingot);
        if ((recipeRemovals & GEM_RECIPE_REMOVAL) != 0) removePackingRecipe(gem);
        if ((recipeRemovals & DUST_RECIPE_REMOVAL) != 0) removePackingRecipe(dust);

        if (aMaterial.mStandardMoltenFluid != null) {
            if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.CastIron
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
            GT_FML_LOGGER.error(
                "'blockQuickSilver'?, In which Ice Desert can you actually place this as a solid Block? On Pluto Greg :)");
        }
    }
}
