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

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class ProcessingGem implements gregtech.api.interfaces.IOreRecipeRegistrator { // TODO COMPARE WITH GEM???
                                                                                      // generators

    public static ProcessingGem INSTANCE;

    public ProcessingGem() {
        INSTANCE = this;
        OrePrefixes.gem.add(this);
        OrePrefixes.gemChipped.add(this);
        OrePrefixes.gemExquisite.add(this);
        OrePrefixes.gemFlawed.add(this);
        OrePrefixes.gemFlawless.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        long materialMass = MaterialUtils.mass(material);
        boolean noSmashing = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING);
        boolean noWorking = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING);
        boolean noSmelting = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING);
        boolean specialRecipeReq = MaterialUtils.hasFlag(material, GTMaterialFlag.MORTAR_GRINDABLE);
        boolean fuelPower = MaterialUtils.fuelPower(material) > 0;
        boolean unifiable = !Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE));

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
                    && GTOreDictUnificator.get(OrePrefixes.block, material, 1L) != null) {
                    // Compressor recipes
                    // need to avoid iridium exploit
                    if (material != Materials.Iridium) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(9, stack))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, material, 1L))
                            .duration(15 * SECONDS)
                            .eut(2)
                            .addTo(compressorRecipes);
                    }
                }

                // Smelting recipe
                if (!noSmelting) {
                    GTModHandler.addSmeltingRecipe(
                        GTUtility.copyAmount(1, stack),
                        GTOreDictUnificator.get(OrePrefixes.ingot, MaterialUtils.smeltInto(material), 1L));
                }

                if (noSmashing) {
                    // Forge hammer recipes
                    {
                        if (GTOreDictUnificator.get(OrePrefixes.gemFlawed, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(stack)
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemFlawed, material, 2L))
                                .duration(3 * SECONDS + 4 * TICKS)
                                .eut(TierEU.RECIPE_LV / 2)
                                .addTo(hammerRecipes);
                        }
                    }
                } else {
                    // Forge hammer recipes
                    {
                        // need to avoid iridium exploit
                        if (GTOreDictUnificator.get(OrePrefixes.plate, material, 1L) != null
                            && material != Materials.Iridium) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L))
                                .duration(Math.max(materialMass, 1L))
                                .eut(calculateRecipeEU(material, 16))
                                .addTo(hammerRecipes);
                        }
                    }

                    // Bender recipes
                    if (material != Materials.Iridium) {
                        if (GTOreDictUnificator.get(OrePrefixes.plate, material, 1L) != null) {
                            // Plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(1)
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L))
                                .duration((int) Math.max(materialMass * 2L, 1L))
                                .eut(calculateRecipeEU(material, 24))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateDouble, material, 1L) != null) {
                            // Double plates
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(2, stack))
                                .circuit(2)
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDouble, material, 1L))
                                .duration(Math.max(materialMass * 2L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateTriple, material, 1L) != null) {
                            // Triple plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(3, stack))
                                .circuit(3)
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateTriple, material, 1L))
                                .duration(Math.max(materialMass * 3L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateQuadruple, material, 1L) != null) {
                            // Quadruple plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(4, stack))
                                .circuit(4)
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuadruple, material, 1L))
                                .duration(Math.max(materialMass * 4L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateQuintuple, material, 1L) != null) {
                            // Quintuple plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(5, stack))
                                .circuit(5)
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuintuple, material, 1L))
                                .duration(Math.max(materialMass * 5L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateDense, material, 1L) != null) {
                            // dense plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(9, stack))
                                .circuit(9)
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDense, material, 1L))
                                .duration(Math.max(materialMass * 9L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }
                    }
                }
                if (noWorking) {
                    Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
                    if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                        // Lathe recipes
                        if (GTOreDictUnificator.get(OrePrefixes.stick, material, 1L) != null
                            && GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .itemOutputs(
                                    GTOreDictUnificator.get(OrePrefixes.stick, material, 1L),
                                    GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 2L))
                                .duration(((int) Math.max(materialMass, 1L)) * TICKS)
                                .eut(calculateRecipeEU(material, 16))
                                .addTo(latheRecipes);
                        }
                    }
                } else {
                    if (unifiable) {
                        // Implosion compressor recipes
                        if (GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(3, stack))
                                .itemOutputs(
                                    GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, 1),
                                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                .duration(1 * SECONDS)
                                .eut(TierEU.RECIPE_LV)
                                .metadata(ADDITIVE_AMOUNT, 8)
                                .addTo(implosionRecipes);
                        }
                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                GTOreDictUnificator.get(OrePrefixes.gem, material, 2L),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h", "X", 'X',
                                    MaterialParts.craftIngredient(OrePrefixes.gemFlawless, material) });

                            if (MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)) {
                                GTModHandler.addCraftingRecipe(
                                    GTUtility.copyAmount(1, stack),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "XXX", "XXX", "XXX", 'X',
                                        MaterialParts.craftIngredient(OrePrefixes.nugget, material) });
                            }

                            if (specialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.dust, material, 1L),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "X", "m", 'X',
                                        MaterialParts.craftIngredient(OrePrefixes.gem, material) });
                            }
                        }
                    }
                }

                for (ItemStack is : OreDictionary
                    .getOres("craftingLens" + MaterialUtils.dye(material).mName.replace(" ", ""))) { // Engraver
                    // Laser engraver recipes
                    {

                        if (GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, 1L) != null) {
                            is.stackSize = 0;

                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(3, stack), is)
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, 1L))
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

                // Forge-hammer the lowest gem tier straight to dust, ported from the retired bartworks GemLoader.
                if (GTOreDictUnificator.get(OrePrefixes.dustTiny, material, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustTiny, material, 1L))
                        .duration(3 * SECONDS + 4 * TICKS)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(hammerRecipes);
                }

                if (!noWorking) {
                    // Lathe recipes
                    if (GTOreDictUnificator.get(OrePrefixes.bolt, material, 1L) != null
                        && GTOreDictUnificator.get(OrePrefixes.dustTiny, material, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.bolt, material, 1L),
                                GTOreDictUnificator.get(OrePrefixes.dustTiny, material, 1L))
                            .duration(((int) Math.max(materialMass, 1L)) * TICKS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(latheRecipes);
                    }

                    if (unifiable) {
                        // Implosion compressor recipes
                        {
                            if (GTOreDictUnificator.get(OrePrefixes.gemFlawed, material, 1) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, stack))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemFlawed, material, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .metadata(ADDITIVE_AMOUNT, 8)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                GTOreDictUnificator.get(OrePrefixes.gemChipped, material, 2L),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h", "X", 'X',
                                    MaterialParts.craftIngredient(OrePrefixes.gemFlawed, material) });
                            if (specialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 1L),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "X", "m", 'X',
                                        MaterialParts.craftIngredient(OrePrefixes.gemChipped, material) });
                            }
                        }
                    }
                }
                for (ItemStack is : OreDictionary
                    .getOres("craftingLens" + MaterialUtils.dye(material).mName.replace(" ", ""))) { // Engraver
                    is.stackSize = 0;

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, stack), is)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemFlawed, material, 1L))
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
                                    GTOreDictUnificator.get(OrePrefixes.dust, material, 4L),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "X", "m", 'X',
                                        MaterialParts.craftIngredient(OrePrefixes.gemExquisite, material) });
                            }
                        }
                    }
                }

                // Forge hammer recipes
                {
                    GTValues.RA.stdBuilder()
                        .itemInputs(stack)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, 2L))
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
                    if (GTOreDictUnificator.get(OrePrefixes.bolt, material, 1L) != null
                        && GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.bolt, material, 2L),
                                GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 1L))
                            .duration(((int) Math.max(materialMass, 1L)) * TICKS)
                            .eut(12)
                            .addTo(latheRecipes);
                    }

                    if (unifiable) {
                        // Implosion compressor recipes
                        {
                            if (GTOreDictUnificator.get(OrePrefixes.gem, material, 1) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, stack))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gem, material, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .metadata(ADDITIVE_AMOUNT, 8)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                GTOreDictUnificator.get(OrePrefixes.gemFlawed, material, 2L),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h", "X", 'X',
                                    MaterialParts.craftIngredient(OrePrefixes.gem, material) });
                            if (specialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 2L),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "X", "m", 'X',
                                        MaterialParts.craftIngredient(OrePrefixes.gemFlawed, material) });
                            }
                        }
                    }
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemChipped, material, 2L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(hammerRecipes);

                for (ItemStack is : OreDictionary
                    .getOres("craftingLens" + MaterialUtils.dye(material).mName.replace(" ", ""))) { // Engraver

                    is.stackSize = 0;

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, stack), is)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
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
                    // Lathe recipes
                    if (GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1L) != null
                        && GTOreDictUnificator.get(OrePrefixes.dust, material, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1L),
                                GTOreDictUnificator.getDust(
                                    material,
                                    prefix.getMaterialAmount() - OrePrefixes.stickLong.getMaterialAmount()))
                            .duration(((int) Math.max(materialMass * 5L, 1L)) * TICKS)
                            .eut(TierEU.RECIPE_LV / 2)
                            .addTo(latheRecipes);
                    }

                    if (unifiable) {
                        // Implosion compressor recipes
                        {
                            if (GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, stack))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .metadata(ADDITIVE_AMOUNT, 8)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, 2L),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h", "X", 'X',
                                    MaterialParts.craftIngredient(OrePrefixes.gemExquisite, material) });
                            if (specialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.dust, material, 2L),
                                    GTModHandler.RecipeBits.BITS_STD,
                                    new Object[] { "X", "m", 'X',
                                        MaterialParts.craftIngredient(OrePrefixes.gemFlawless, material) });
                            }
                        }
                    }
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 2L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(hammerRecipes);

                for (ItemStack is : OreDictionary
                    .getOres("craftingLens" + MaterialUtils.dye(material).mName.replace(" ", ""))) { // Engraver

                    is.stackSize = 0;
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, stack), is)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1L))
                        .duration(2 * MINUTES)
                        .eut(TierEU.RECIPE_EV)
                        .addTo(laserEngraverRecipes);
                }
            }
            default -> {}
        }
    }
}
