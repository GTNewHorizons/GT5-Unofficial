package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class ProcessingGem implements PartRecipeGenerator {

    public static final ProcessingGem INSTANCE = new ProcessingGem();

    private ProcessingGem() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        long materialMass = MaterialUtils.mass(material);
        boolean noSmashing = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING);
        boolean noWorking = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING);
        boolean noSmelting = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING);
        boolean specialRecipeReq = MaterialUtils.hasFlag(material, GTMaterialFlag.MORTAR_GRINDABLE);
        boolean fuelPower = MaterialUtils.fuelPower(material) > 0;
        boolean unifiable = MaterialUtils.unifiable(material);
        boolean implodable = !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_BLAST);

        // Nether quartz is skipped due to its 4:1 ratio.
        if (material == Materials.NetherQuartz) return;

        switch (prefix.getName()) {
            case "gem" -> {
                // fuel recipes
                if (fuelPower) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .metadata(FUEL_VALUE, MaterialUtils.fuelPower(material) * 2)
                        .metadata(FUEL_TYPE, MaterialUtils.fuelType(material))
                        .addTo(GTRecipeConstants.Fuel);
                }

                if (!OrePrefixes.block.isIgnored(material)
                    && MaterialLibAPI.findStack(material, BlockShapes.block, 1) != null) {
                    // Compressor recipes
                    // need to avoid iridium exploit
                    if (material != Materials.Iridium) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(9, stack))
                            .itemOutputs(MaterialLibAPI.findStack(material, BlockShapes.block, 1))
                            .duration(15 * SECONDS)
                            .eut(2)
                            .addTo(compressorRecipes);
                    }
                }

                // Smelting recipe
                if (!noSmelting) {
                    GTModHandler.addSmeltingRecipe(
                        GTUtility.copyAmount(1, stack),
                        MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.ingot, 1));
                }

                if (noSmashing) {
                    // Forge hammer recipes
                    {
                        if (MaterialLibAPI.findStack(material, Shapes.gemFlawed, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(stack)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gemFlawed, 2))
                                .duration(3 * SECONDS + 4 * TICKS)
                                .eut(TierEU.RECIPE_LV / 2)
                                .addTo(hammerRecipes);
                        }
                    }
                } else {
                    // Forge hammer recipes
                    {
                        // need to avoid iridium exploit
                        if (MaterialLibAPI.findStack(material, Shapes.plate, 1) != null
                            && material != Materials.Iridium) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plate, 1))
                                .duration(Math.max(materialMass, 1L))
                                .eut(calculateRecipeEU(material, 16))
                                .addTo(hammerRecipes);
                        }
                    }

                    // Bender recipes
                    if (material != Materials.Iridium) {
                        if (MaterialLibAPI.findStack(material, Shapes.plate, 1) != null) {
                            // Plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(1)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plate, 1))
                                .duration((int) Math.max(materialMass * 2L, 1L))
                                .eut(calculateRecipeEU(material, 24))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.plateDouble, 1) != null) {
                            // Double plates
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(2, stack))
                                .circuit(2)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plateDouble, 1))
                                .duration(Math.max(materialMass * 2L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.plateTriple, 1) != null) {
                            // Triple plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(3, stack))
                                .circuit(3)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plateTriple, 1))
                                .duration(Math.max(materialMass * 3L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.plateQuadruple, 1) != null) {
                            // Quadruple plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(4, stack))
                                .circuit(4)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plateQuadruple, 1))
                                .duration(Math.max(materialMass * 4L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.plateQuintuple, 1) != null) {
                            // Quintuple plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(5, stack))
                                .circuit(5)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plateQuintuple, 1))
                                .duration(Math.max(materialMass * 5L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.plateDense, 1) != null) {
                            // dense plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(9, stack))
                                .circuit(9)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plateDense, 1))
                                .duration(Math.max(materialMass * 9L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }
                    }
                }
                if (noWorking) {
                    if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                        // Lathe recipes
                        if (MaterialLibAPI.findStack(material, Shapes.stick, 1) != null
                            && MaterialLibAPI.findStack(material, Shapes.dustSmall, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .itemOutputs(
                                    MaterialLibAPI.findStack(material, Shapes.stick, 1),
                                    MaterialLibAPI.findStack(material, Shapes.dustSmall, 2))
                                .duration(((int) Math.max(materialMass, 1L)) * TICKS)
                                .eut(calculateRecipeEU(material, 16))
                                .addTo(latheRecipes);
                        }
                    }
                } else {
                    if (unifiable) {
                        if (implodable) addTierImplosion(material, stack, Shapes.gemFlawless);
                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                MaterialLibAPI.findStack(material, Shapes.gem, 2),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h", "X", 'X', OrePrefixes.gemFlawless.ingredient(material) });

                            if (MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)) {
                                GTModHandler.addCraftingRecipe(
                                    GTUtility.copyAmount(1, stack),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "XXX", "XXX", "XXX", 'X', OrePrefixes.nugget.ingredient(material) });
                            }

                            if (specialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    MaterialLibAPI.findStack(material, Shapes.dust, 1),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gem.ingredient(material) });
                            }
                        }
                    }
                }

                for (ItemStack is : OreDictionary
                    .getOres("craftingLens" + MaterialUtils.dye(material).mName.replace(" ", ""))) { // Engraver
                    // Laser engraver recipes
                    {

                        if (MaterialLibAPI.findStack(material, Shapes.gemFlawless, 1) != null) {
                            is.stackSize = 0;

                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(3, stack), is)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gemFlawless, 1))
                                .duration(60 * SECONDS)
                                .eut(TierEU.RECIPE_HV)
                                .addTo(laserEngraverRecipes);
                        }

                    }
                }
            }
            case "gemChipped" -> {
                // Fuel recipes
                if (fuelPower) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .metadata(FUEL_VALUE, MaterialUtils.fuelPower(material) / 2)
                        .metadata(FUEL_TYPE, MaterialUtils.fuelType(material))
                        .addTo(GTRecipeConstants.Fuel);
                }

                if (!noWorking) {
                    // Lathe recipes
                    if (MaterialLibAPI.findStack(material, Shapes.bolt, 1) != null
                        && MaterialLibAPI.findStack(material, Shapes.dustTiny, 1) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                MaterialLibAPI.findStack(material, Shapes.bolt, 1),
                                MaterialLibAPI.findStack(material, Shapes.dustTiny, 1))
                            .duration(((int) Math.max(materialMass, 1L)) * TICKS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(latheRecipes);
                    }

                    if (unifiable) {
                        if (implodable) addTierImplosion(material, stack, Shapes.gemFlawed);

                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                MaterialLibAPI.findStack(material, Shapes.gemChipped, 2),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h", "X", 'X', OrePrefixes.gemFlawed.ingredient(material) });
                            if (specialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    MaterialLibAPI.findStack(material, Shapes.dustSmall, 1),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemChipped.ingredient(material) });
                            }
                        }
                    }
                }
                for (ItemStack is : OreDictionary
                    .getOres("craftingLens" + MaterialUtils.dye(material).mName.replace(" ", ""))) { // Engraver
                    is.stackSize = 0;

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, stack), is)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gemFlawed, 1))
                        .duration(30 * SECONDS)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(laserEngraverRecipes);

                }
            }
            case "gemExquisite" -> {
                // Fuel recipes
                if (fuelPower) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .metadata(FUEL_VALUE, MaterialUtils.fuelPower(material) * 8)
                        .metadata(FUEL_TYPE, MaterialUtils.fuelType(material))
                        .addTo(GTRecipeConstants.Fuel);
                }

                if (!noWorking) {
                    if (unifiable) {
                        // Crafting recipes
                        {
                            if (specialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    MaterialLibAPI.findStack(material, Shapes.dust, 4),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemExquisite.ingredient(material) });
                            }
                        }
                    }
                }

                // Forge hammer recipes
                {
                    GTValues.RA.stdBuilder()
                        .itemInputs(stack)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gemFlawless, 2))
                        .duration(3 * SECONDS + 4 * TICKS)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(hammerRecipes);
                }
            }
            case "gemFlawed" -> {
                // fuel recipes
                if (fuelPower) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .metadata(FUEL_VALUE, MaterialUtils.fuelPower(material))
                        .metadata(FUEL_TYPE, MaterialUtils.fuelType(material))
                        .addTo(GTRecipeConstants.Fuel);
                }

                if (!noWorking) {
                    // Lathe recipes
                    if (MaterialLibAPI.findStack(material, Shapes.bolt, 1) != null
                        && MaterialLibAPI.findStack(material, Shapes.dustSmall, 1) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                MaterialLibAPI.findStack(material, Shapes.bolt, 2),
                                MaterialLibAPI.findStack(material, Shapes.dustSmall, 1))
                            .duration(((int) Math.max(materialMass, 1L)) * TICKS)
                            .eut(12)
                            .addTo(latheRecipes);
                    }

                    if (unifiable) {
                        if (implodable) addTierImplosion(material, stack, Shapes.gem);

                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                MaterialLibAPI.findStack(material, Shapes.gemFlawed, 2),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h", "X", 'X', OrePrefixes.gem.ingredient(material) });
                            if (specialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    MaterialLibAPI.findStack(material, Shapes.dustSmall, 2),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemFlawed.ingredient(material) });
                            }
                        }
                    }
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gemChipped, 2))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(hammerRecipes);

                for (ItemStack is : OreDictionary
                    .getOres("craftingLens" + MaterialUtils.dye(material).mName.replace(" ", ""))) { // Engraver

                    is.stackSize = 0;

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, stack), is)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gem, 1))
                        .duration(30 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(laserEngraverRecipes);

                }
            }
            case "gemFlawless" -> {

                // Fuel recipes
                if (fuelPower) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .metadata(FUEL_VALUE, MaterialUtils.fuelPower(material) * 4)
                        .metadata(FUEL_TYPE, MaterialUtils.fuelType(material))
                        .addTo(GTRecipeConstants.Fuel);
                }

                if (!noWorking) {
                    // Lathe recipes; LatheRecipes turns Lanthanum Hexaboride on its own, slower recipe
                    if (material != Materials.LanthanumHexaboride
                        && MaterialLibAPI.findStack(material, Shapes.stickLong, 1) != null
                        && MaterialLibAPI.findStack(material, Shapes.dust, 1) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                MaterialLibAPI.findStack(material, Shapes.stickLong, 1),
                                MaterialParts.dustOf(
                                    material,
                                    prefix.getMaterialAmount() - OrePrefixes.stickLong.getMaterialAmount()))
                            .duration(((int) Math.max(materialMass * 5L, 1L)) * TICKS)
                            .eut(TierEU.RECIPE_LV / 2)
                            .addTo(latheRecipes);
                    }

                    if (unifiable) {
                        if (implodable) addTierImplosion(material, stack, Shapes.gemExquisite);

                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                MaterialLibAPI.findStack(material, Shapes.gemFlawless, 2),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h", "X", 'X', OrePrefixes.gemExquisite.ingredient(material) });
                            if (specialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    MaterialLibAPI.findStack(material, Shapes.dust, 2),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemFlawless.ingredient(material) });
                            }
                        }
                    }
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gem, 2))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(hammerRecipes);

                for (ItemStack is : OreDictionary
                    .getOres("craftingLens" + MaterialUtils.dye(material).mName.replace(" ", ""))) { // Engraver

                    is.stackSize = 0;
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, stack), is)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gemExquisite, 1))
                        .duration(2 * MINUTES)
                        .eut(TierEU.RECIPE_EV)
                        .addTo(laserEngraverRecipes);
                }
            }
            default -> {}
        }
    }

    /// Implodes three gems of `stack`'s tier into one of the `next` tier, when the material has it.
    private static void addTierImplosion(Material material, ItemStack stack, Shape next) {
        ItemStack output = MaterialLibAPI.findStack(material, next, 1);
        if (output == null) return;
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(3, stack))
            .itemOutputs(output, MaterialLibAPI.getStack(Materials.DarkAsh, Shapes.dustTiny, 2))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 8)
            .addTo(implosionRecipes);
    }
}
