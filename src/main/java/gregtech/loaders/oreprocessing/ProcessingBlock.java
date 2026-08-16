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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.NBTPersist;

public class ProcessingBlock implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private static final int INGOT_RECIPE_REMOVAL = 1;
    private static final int GEM_RECIPE_REMOVAL = 1 << 1;
    private static final int DUST_RECIPE_REMOVAL = 1 << 2;

    // List of effective blocks that had a previous recipe
    private static final ImmutableSet<String> BLOCK_RECIPE_REMOVALS = ImmutableSet.of(
        "Avaritia:Resource_Block@0",
        "Avaritia:Resource_Block@1",
        "BiomesOPlenty:gemOre@11",
        "BiomesOPlenty:gemOre@13",
        "BiomesOPlenty:gemOre@15",
        "BiomesOPlenty:gemOre@3",
        "BiomesOPlenty:gemOre@7",
        "BiomesOPlenty:gemOre@9",
        "BloodArsenal:blood_infused_iron_block@0",
        "EnderIO:blockIngotStorage@0",
        "EnderIO:blockIngotStorage@1",
        "EnderIO:blockIngotStorage@2",
        "EnderIO:blockIngotStorage@3",
        "EnderIO:blockIngotStorage@4",
        "EnderIO:blockIngotStorage@5",
        "EnderIO:blockIngotStorage@6",
        "EnderIO:blockIngotStorage@7",
        "EnderIO:blockIngotStorage@8",
        "EnderIO:blockIngotStorageEndergy@0",
        "EnderIO:blockIngotStorageEndergy@1",
        "EnderIO:blockIngotStorageEndergy@2",
        "EnderIO:blockIngotStorageEndergy@3",
        "EnderIO:blockIngotStorageEndergy@4",
        "EnderIO:blockIngotStorageEndergy@5",
        "EnderIO:blockIngotStorageEndergy@6",
        "ForbiddenMagic:StarBlock@0",
        "gregtech:gt.blockgem2@4",
        "gregtech:gt.blockmetal4@2",
        "gregtech:gt.blockmetal6@10",
        "harvestcraft:spamcompressedsaltBlockalt@0",
        "minecraft:coal_block@0",
        "minecraft:diamond_block@0",
        "minecraft:emerald_block@0",
        "minecraft:gold_block@0",
        "minecraft:iron_block@0",
        "minecraft:lapis_block@0",
        "minecraft:redstone_block@0",
        "ProjRed|Exploration:projectred.exploration.stone@11",
        "ProjRed|Exploration:projectred.exploration.stone@6",
        "Railcraft:cube@10",
        "StevesCarts:BlockMetalStorage@1",
        "StevesCarts:BlockMetalStorage@2",
        "TaintedMagic:BlockShadowmetal@0",
        "TConstruct:MetalBlock@0",
        "TConstruct:MetalBlock@1",
        "TConstruct:MetalBlock@2",
        "TConstruct:MetalBlock@3",
        "TConstruct:MetalBlock@4",
        "TConstruct:MetalBlock@5",
        "TConstruct:MetalBlock@6",
        "TConstruct:MetalBlock@7",
        "TConstruct:MetalBlock@8",
        "TConstruct:MetalBlock@9",
        "Thaumcraft:blockCosmeticOpaque@0",
        "Thaumcraft:blockCosmeticSolid@4",
        "thaumicbases:voidBlock@0");

    // Map that tells which item recipe to remove. If not in this map, no previous recipes exist so skipping
    private static final ImmutableMap<Materials, Integer> PACKING_RECIPE_REMOVALS = ImmutableMap
        .<Materials, Integer>builder()
        .put(Materials.AluminiumBrass, INGOT_RECIPE_REMOVAL)
        .put(Materials.Amber, GEM_RECIPE_REMOVAL)
        .put(Materials.ClayCompound, INGOT_RECIPE_REMOVAL)
        .put(Materials.Coal, GEM_RECIPE_REMOVAL)
        .put(Materials.ConductiveIron, INGOT_RECIPE_REMOVAL)
        .put(Materials.Copper, INGOT_RECIPE_REMOVAL)
        .put(Materials.CosmicNeutronium, INGOT_RECIPE_REMOVAL)
        .put(Materials.CrystallineAlloy, INGOT_RECIPE_REMOVAL)
        .put(Materials.CrystallinePinkSlime, INGOT_RECIPE_REMOVAL)
        .put(Materials.DarkSteel, INGOT_RECIPE_REMOVAL)
        .put(Materials.Diamond, GEM_RECIPE_REMOVAL)
        .put(Materials.ElectricalSteel, INGOT_RECIPE_REMOVAL)
        .put(Materials.Electrotine, DUST_RECIPE_REMOVAL)
        .put(Materials.Emerald, GEM_RECIPE_REMOVAL)
        .put(Materials.EndSteel, INGOT_RECIPE_REMOVAL)
        .put(Materials.EnergeticAlloy, INGOT_RECIPE_REMOVAL)
        .put(Materials.EnergeticSilver, INGOT_RECIPE_REMOVAL)
        .put(Materials.Gold, INGOT_RECIPE_REMOVAL)
        .put(Materials.Infinity, INGOT_RECIPE_REMOVAL)
        .put(Materials.Iron, INGOT_RECIPE_REMOVAL)
        .put(Materials.Lapis, GEM_RECIPE_REMOVAL)
        .put(Materials.Lead, INGOT_RECIPE_REMOVAL)
        .put(Materials.Malachite, GEM_RECIPE_REMOVAL)
        .put(Materials.MelodicAlloy, INGOT_RECIPE_REMOVAL)
        .put(Materials.NaquadahAlloy, INGOT_RECIPE_REMOVAL)
        .put(Materials.NetherStar, GEM_RECIPE_REMOVAL)
        .put(Materials.PulsatingIron, INGOT_RECIPE_REMOVAL)
        .put(Materials.Redstone, DUST_RECIPE_REMOVAL)
        .put(Materials.RedstoneAlloy, INGOT_RECIPE_REMOVAL)
        .put(Materials.Ruby, GEM_RECIPE_REMOVAL)
        .put(Materials.Sapphire, GEM_RECIPE_REMOVAL)
        .put(Materials.Silver, INGOT_RECIPE_REMOVAL)
        .put(Materials.Soularium, INGOT_RECIPE_REMOVAL)
        .put(Materials.Steel, INGOT_RECIPE_REMOVAL)
        .put(Materials.StellarAlloy, INGOT_RECIPE_REMOVAL)
        .put(Materials.Tanzanite, GEM_RECIPE_REMOVAL)
        .put(Materials.Thaumium, INGOT_RECIPE_REMOVAL)
        .put(Materials.Tin, INGOT_RECIPE_REMOVAL)
        .put(Materials.Topaz, GEM_RECIPE_REMOVAL)
        .put(Materials.Unstable, INGOT_RECIPE_REMOVAL)
        .put(Materials.VibrantAlloy, INGOT_RECIPE_REMOVAL)
        .put(Materials.VividAlloy, INGOT_RECIPE_REMOVAL)
        .put(Materials.Void, INGOT_RECIPE_REMOVAL)
        .build();

    private final Set<GTUtility.ItemId> queuedPackingRemovals = new HashSet<>();

    public ProcessingBlock() {
        OrePrefixes.block.add(this);
    }

    private static String blockKey(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return null;

        UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        if (id == null) return null;

        String key = id + "@" + Items.feather.getDamage(stack);
        if (stack.hasTagCompound()) key += "#nbt=" + NBTPersist.toJsonObjectExact(stack.getTagCompound());
        return key;
    }

    private void removePackingRecipe(ItemStack input) {
        if (input == null || (GTModHandler.isBufferingCraftingRecipes()
            && !queuedPackingRemovals.add(GTUtility.ItemId.createWithStackSize(input)))) {
            return;
        }

        GTModHandler.removeRecipeDelayed(input, input, input, input, input, input, input, input, input);
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

        if (BLOCK_RECIPE_REMOVALS.contains(blockKey(aStack))) {
            GTModHandler.removeRecipeDelayed(GTUtility.copyAmount(1, aStack));
        }

        int packingRecipeRemovals = PACKING_RECIPE_REMOVALS.getOrDefault(aMaterial, 0);
        if ((packingRecipeRemovals & INGOT_RECIPE_REMOVAL) != 0) removePackingRecipe(ingot);
        if ((packingRecipeRemovals & GEM_RECIPE_REMOVAL) != 0) removePackingRecipe(gem);
        if ((packingRecipeRemovals & DUST_RECIPE_REMOVAL) != 0) removePackingRecipe(dust);

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
