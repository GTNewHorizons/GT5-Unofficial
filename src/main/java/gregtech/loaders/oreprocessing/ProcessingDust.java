package gregtech.loaders.oreprocessing;

import static bartworks.system.material.gtenhancement.PlatinumSludgeOutputs.convert;
import static bartworks.system.material.gtenhancement.PlatinumSludgeOutputs.convertSmelting;
import static gregtech.api.enums.GTValues.M;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalDehydratorRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTUtility.calculateRecipeEU;
import static gtnhlanth.util.LanthanidesRecipeOutputs.convertDecomposition;
import static gtnhlanth.util.LanthanidesRecipeOutputs.convertOre;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.math.LongMath;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingDust implements PartRecipeGenerator {

    public static final ProcessingDust INSTANCE = new ProcessingDust();

    private ProcessingDust() {}

    /// The gas or liquid form a material's dust converts to and from, [GTMaterialProperties#DUST_FLUID_AMOUNT] litres
    /// of it, or null when the material declares no corresponding gas or fluid or its dust melts into a molten fluid.
    static FluidStack dustFluid(Material material) {
        if (!MaterialUtils.blastFurnaceRequired(material)
            && GTRecipeRegistrator.hasReverseFluidSmeltingRecipe(material)) {
            return null;
        }
        int amount = material.getProperty(GTMaterialProperties.DUST_FLUID_AMOUNT);
        if (MaterialUtils.hasCorrespondingGas(material)) return MaterialUtils.gas(material, amount);
        return MaterialUtils.hasCorrespondingFluid(material) ? MaterialUtils.fluid(material, amount) : null;
    }

    /// Whether a dust resolves for `material`. An ore material need not have one (ChargedCertusQuartz removes
    /// the dust shape).
    private static boolean hasDust(Material material) {
        return MaterialLibAPI.findStack(material, Shapes.dust, 1) != null;
    }

    /// `composition` with every amount divided by their greatest common divisor when the whole composition exceeds
    /// the `capacity` (in material units) of one decomposition batch, so every component fits the recipe.
    private static List<MaterialStack> decompositionBatch(List<MaterialStack> composition, long capacity) {
        long total = 0L;
        long divisor = 0L;
        for (MaterialStack component : composition) {
            if (component.mAmount <= 0L) continue;
            total += component.mAmount;
            divisor = LongMath.gcd(divisor, component.mAmount);
        }
        if (total * M <= capacity || divisor <= 1L) return composition;
        for (MaterialStack component : composition) component.mAmount /= divisor;
        return composition;
    }

    /// A blast furnace recipe smelting one `stack` into `material`'s smelt-into ingot, keyed on `circuit`.
    /// Above 1750K the output is a hot ingot instead, falling back to `ingotStack` when none resolves.
    private static GTRecipeBuilder blastFurnaceBuilder(Material material, ItemStack stack, ItemStack ingotStack,
        int circuit) {
        GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
        recipeBuilder.itemInputs(GTUtility.copyAmount(1, stack))
            .circuit(circuit);
        if (MaterialUtils.blastFurnaceTemp(material) > 1750) {
            recipeBuilder.itemOutputs(
                GTUtility.copyAmount(
                    1L,
                    MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.ingotHot, 1),
                    ingotStack));
        } else {
            recipeBuilder.itemOutputs(GTUtility.copyAmount(1, ingotStack));
        }
        Integer duration = material.getProperty(GTMaterialProperties.BLAST_DURATION);
        Integer voltage = material.getProperty(GTMaterialProperties.BLAST_VOLTAGE);
        return recipeBuilder
            .duration(
                duration != null ? duration
                    : Math.max(MaterialUtils.mass(material) / 40L, 1L) * MaterialUtils.blastFurnaceTemp(material))
            .eut(voltage != null ? voltage : TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, MaterialUtils.blastFurnaceTemp(material));
    }

    @Override
    public void generate(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        switch (prefix.getName()) {
            case "dust" -> {
                if (MaterialLibAPI.findStack(material, Shapes.dustSmall, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Schematic_Dust_Small.get(0L))
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.dustSmall, 4))
                        .duration(1 * SECONDS)
                        .eut(4)
                        .addTo(packagerRecipes);
                }
                if (MaterialUtils.fuelPower(material) > 0) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .metadata(FUEL_VALUE, MaterialUtils.fuelPower(material))
                        .metadata(FUEL_TYPE, MaterialUtils.fuelType(material))
                        .addTo(GTRecipeConstants.Fuel);
                }
                ItemStack cell = MaterialParts.stack(OrePrefixes.cell, material, 1L);
                if (cell != null && GTUtility.getFluidForFilledItem(cell, true) == null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(stack, ItemList.Cell_Empty.get(1L))
                        .itemOutputs(cell)
                        .duration(5 * SECONDS)
                        .eut(1)
                        .addTo(cannerRecipes);
                }
                if (!MaterialUtils.blastFurnaceRequired(material)) {
                    GTRecipeRegistrator
                        .registerReverseFluidSmelting(stack, material, prefix.getMaterialAmount(), null, false);
                    if (GTRecipeRegistrator.hasReverseArcSmeltingRecipe(material)) {
                        GTRecipeRegistrator.registerReverseArcSmelting(
                            GTUtility.copyAmount(1, stack),
                            material,
                            prefix.getMaterialAmount(),
                            null,
                            null,
                            null);
                    }
                }
                ItemStack tDustStack;
                if ((null
                    != (tDustStack = MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.ingot, 1)))
                    && (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING))) {
                    if (MaterialUtils.blastFurnaceRequired(material)) {
                        GTModHandler.removeFurnaceSmelting(stack);
                        if (MaterialUtils.autoGenerateBlastFurnaceRecipes(material)
                            && MaterialUtils.blastFurnaceTemp(material) > 0) {
                            // A NOBLE_GAS_SMELTING material blast-smelts under a gas: BlastFurnaceWithGas fans one
                            // recipe out into a gas-input variant per BlastFurnaceGasStat, keyed on circuit 11 and
                            // the base gas amount in ADDITIVE_AMOUNT.
                            if (MaterialUtils.hasFlag(material, GTMaterialFlag.NOBLE_GAS_SMELTING)) {
                                blastFurnaceBuilder(material, stack, tDustStack, 11).metadata(ADDITIVE_AMOUNT, 1000)
                                    .addTo(BlastFurnaceWithGas);
                            } else {
                                blastFurnaceBuilder(material, stack, tDustStack, 1).addTo(blastFurnaceRecipes);
                            }
                            // Californium is the sole blast-required material melting low enough for the
                            // coal-fired bricked blast furnace.
                            if (material == Materials.Californium) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(1, stack))
                                    .itemOutputs(GTUtility.copyAmount(1, tDustStack))
                                    .duration(
                                        Math.max(MaterialUtils.mass(material) / 40L, 1L)
                                            * MaterialUtils.blastFurnaceTemp(material))
                                    .eut(0)
                                    .metadata(ADDITIVE_AMOUNT, 9)
                                    .addTo(primitiveBlastRecipes);
                            }
                        }
                    } else {
                        GTModHandler.addSmeltingRecipe(stack, convertSmelting(material, prefix, tDustStack));
                    }
                } else if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
                    if ((!OrePrefixes.block.isIgnored(material))
                        && (null == MaterialLibAPI.findStack(material, Shapes.gem, 1))
                        && MaterialLibAPI.findStack(material, BlockShapes.block, 1) != null
                        && (material != Materials.Clay)
                        && (material != Materials.Netherrack)) {

                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(9, stack))
                            .itemOutputs(MaterialLibAPI.findStack(material, BlockShapes.block, 1))
                            .duration(15 * SECONDS)
                            .eut(2)
                            .addTo(compressorRecipes);
                    }
                    if (((OrePrefixes.block.isIgnored(material))
                        || (null == MaterialLibAPI.findStack(material, BlockShapes.block, 1)))
                        && (material != Materials.GraniteRed)
                        && (material != Materials.GraniteBlack)
                        && (material != Materials.Basalt)
                        && (material != Materials.Marble)
                        && (material != Materials.Glass)
                        && (material != Materials.Obsidian)
                        && (material != Materials.Glowstone)
                        && (material != Materials.Paper)
                        && (material != Materials.TranscendentMetal)
                        && (material != Materials.Clay)
                        && (material != Materials.Wood)
                        && (material != Materials.Carbon)
                        && (material != Materials.Stone)) {
                        // compressor recipe
                        {
                            if (MaterialLibAPI.findStack(material, Shapes.plate, 1) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(1, stack))
                                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plate, 1))
                                    .duration(15 * SECONDS)
                                    .eut(2)
                                    .addTo(compressorRecipes);
                            }
                        }
                    }
                }
                // GT's other fluid-extractor and fluid-solidifier autogen only handles the molten fluid, so a
                // material's own liquid/gas form gets its dust extraction and solidification here.
                FluidStack dustFluid = dustFluid(material);
                if (dustFluid != null) {
                    long fluidEut = calculateRecipeEU(material, MaterialUtils.mass(material) > 128 ? 64 : 30);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .fluidOutputs(dustFluid)
                        .duration((int) MaterialUtils.mass(material))
                        .eut(fluidEut)
                        .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                        .addTo(fluidExtractionRecipes);
                    GTValues.RA.stdBuilder()
                        .circuit(1)
                        .itemOutputs(GTUtility.copyAmount(1, stack))
                        .fluidInputs(dustFluid.copy())
                        .duration((int) MaterialUtils.mass(material))
                        .eut(fluidEut)
                        .addTo(fluidSolidifierRecipes);
                }

                List<MaterialStack> tMaterialList = decompositionBatch(
                    MaterialUtils.materialList(material),
                    stack.getMaxStackSize() * MaterialUtils.density(material));
                if ((!tMaterialList.isEmpty())
                    && (MaterialUtils.hasElectrolyzerRecipe(material) || MaterialUtils.hasCentrifugeRecipe(material))) {
                    long tItemAmount = 0L;
                    long tCapsuleCount = 0L;
                    long tDensityMultiplier = MaterialUtils.density(material) > 3628800L
                        ? MaterialUtils.density(material) / 3628800L
                        : 1L;
                    long tEntryCount = tMaterialList.stream()
                        .filter(tMat -> tMat.mAmount > 0L)
                        .count();
                    int outputSlots = MaterialUtils.hasElectrolyzerRecipe(material) && tEntryCount > 6 ? 9 : 6;
                    ArrayList<ItemStack> tList = new ArrayList<>();
                    for (MaterialStack tMat : tMaterialList) if (tMat.mAmount > 0L) {
                        tDustStack = GTUtility
                            .copyAmount(tMat.mAmount, MaterialLibAPI.findStack(tMat.mMaterial, Shapes.dust, 1));
                        if (tDustStack == null) tDustStack = MaterialParts
                            .stack(OrePrefixes.cell, tMat.mMaterial, Math.min(64L, tMat.mAmount));
                        if (tItemAmount + tMat.mAmount * 3628800L
                            <= stack.getMaxStackSize() * MaterialUtils.density(material)) {
                            tItemAmount += tMat.mAmount * 3628800L;
                            if (tDustStack != null) {
                                tDustStack.stackSize = ((int) (tDustStack.stackSize * tDensityMultiplier));
                                while ((tDustStack.stackSize > 64) && (tList.size() < outputSlots)
                                    && (tCapsuleCount + GTModHandler.getCapsuleCellContainerCount(tDustStack) * 64L
                                        <= 64L)) {
                                    tCapsuleCount += GTModHandler.getCapsuleCellContainerCount(tDustStack) * 64L;
                                    tList.add(GTUtility.copyAmount(64, tDustStack));
                                    tDustStack.stackSize -= 64;
                                }
                                if ((tDustStack.stackSize > 0) && (tList.size() < outputSlots)
                                    && (tCapsuleCount
                                        + GTModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(tDustStack)
                                        <= 64L)) {
                                    tCapsuleCount += GTModHandler
                                        .getCapsuleCellContainerCountMultipliedWithStackSize(tDustStack);
                                    tList.add(tDustStack);
                                }
                            }
                        }
                    }
                    tItemAmount = (tItemAmount * tDensityMultiplier % MaterialUtils.density(material) > 0L ? 1 : 0)
                        + tItemAmount * tDensityMultiplier / MaterialUtils.density(material);
                    if (!tList.isEmpty()) {
                        FluidStack tFluid = null;
                        int tList_sS = tList.size();
                        for (int i = 0; i < tList_sS; i++) {
                            if ((tFluid = GTUtility.getFluidForFilledItem(tList.get(i), true)) != null) {
                                tFluid.amount *= tList.get(i).stackSize;
                                tCapsuleCount -= GTModHandler
                                    .getCapsuleCellContainerCountMultipliedWithStackSize(tList.get(i));
                                tList.remove(i);
                                break;
                            }
                        }
                        if (MaterialUtils.hasElectrolyzerRecipe(material)) {
                            if (!tList.isEmpty() || tFluid != null) {
                                GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                                if (tCapsuleCount > 0L) {
                                    recipeBuilder.itemInputs(
                                        GTUtility.copyAmount(tItemAmount, stack),
                                        ItemList.Cell_Empty.get(tCapsuleCount));
                                } else {
                                    recipeBuilder.itemInputs(GTUtility.copyAmount(tItemAmount, stack));
                                }
                                if (!tList.isEmpty()) {
                                    ItemStack[] outputsArray = tList.toArray(new ItemStack[0]);
                                    if (material.getProperty(GTMaterialProperties.LANTHANIDE_DECOMPOSITION)) {
                                        outputsArray = convertDecomposition(material, outputsArray);
                                    } else {
                                        outputsArray = convert(material, outputsArray);
                                    }
                                    recipeBuilder.itemOutputs(outputsArray);
                                }
                                if (tFluid != null) {
                                    recipeBuilder.fluidOutputs(tFluid);
                                }
                                recipeBuilder
                                    .duration(
                                        Math.max(1L, Math.abs(MaterialUtils.protons(material) * 2L * tItemAmount)))
                                    .eut(Math.min(4, tList.size()) * 30)
                                    .addTo(tList.size() > 6 ? chemicalDehydratorRecipes : electrolyzerRecipes);
                            }
                        }
                        if (MaterialUtils.hasCentrifugeRecipe(material)) {
                            if (!tList.isEmpty() || tFluid != null) {
                                GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                                if (tCapsuleCount > 0L) {
                                    recipeBuilder.itemInputs(
                                        GTUtility.copyAmount(tItemAmount, stack),
                                        ItemList.Cell_Empty.get(tCapsuleCount));
                                } else {
                                    recipeBuilder.itemInputs(GTUtility.copyAmount(tItemAmount, stack));
                                }
                                if (!tList.isEmpty()) {
                                    ItemStack[] outputsArray = tList.toArray(new ItemStack[Math.min(tList.size(), 6)]);
                                    recipeBuilder.itemOutputs(convertOre(material, outputsArray));
                                }
                                if (tFluid != null) {
                                    recipeBuilder.fluidOutputs(tFluid);
                                }
                                recipeBuilder
                                    .duration(Math.max(1L, Math.abs(MaterialUtils.mass(material) * 4L * tItemAmount)))
                                    .eut(Math.min(4, tList.size()) * 5)
                                    .addTo(centrifugeRecipes);
                            }
                        }
                    }
                }
                if (MaterialUtils.hasFlag(material, GTMaterialFlag.CRYSTALLISABLE)
                    && MaterialLibAPI.findStack(material, Shapes.gem, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(1)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gem, 1))
                        .outputChances(7000)
                        .fluidInputs(GTUtility.getWater(200L))
                        .duration(1 * MINUTES + 40 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(2)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gem, 1))
                        .outputChances(9000)
                        .fluidInputs(GTModHandler.getDistilledWater(100L))
                        .duration(1 * MINUTES + 15 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(3)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gem, 1))
                        .outputChances(10000)
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, QUARTER_INGOTS))
                        .duration(1 * MINUTES)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                }
                switch (MaterialUtils.internalName(material)) {
                    case "NULL", "Mercury", "Coal":
                        break;
                    case "Glass":
                        GTModHandler.addSmeltingRecipe(
                            GTUtility.copyAmount(1, stack),
                            new ItemStack(net.minecraft.init.Blocks.glass));
                        break;
                    case "NetherQuartz":
                    case "Quartz":
                    case "CertusQuartz":
                        GTModHandler.removeFurnaceSmelting(stack);
                        break;
                    case "MeatRaw":
                        GTModHandler.addSmeltingRecipe(
                            GTUtility.copyAmount(1, stack),
                            MaterialLibAPI.getStack(Materials.MeatCooked, Shapes.dust, 1));
                        break;
                    case "Oilsands":
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .fluidOutputs(
                                MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, 1_000))
                            .duration(33 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(centrifugeRecipes);
                        break;
                    case "HydratedCoal":
                        GTModHandler.addSmeltingRecipe(
                            GTUtility.copyAmount(1, stack),
                            MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 1));
                        break;
                }
            }
            case "dustPure", "dustImpure", "dustRefined" -> {
                if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
                    return;
                }

                boolean hasDust = hasDust(material);
                Material tByProduct = GTUtility.selectItemInList(
                    prefix == OrePrefixes.dustRefined ? 2 : prefix == OrePrefixes.dustPure ? 1 : 0,
                    material,
                    MaterialUtils.oreByProducts(material));
                if (hasDust && prefix == OrePrefixes.dustPure) {
                    if (MaterialUtils.hasFlag(material, GTMaterialFlag.ELECTROMAGNETIC_SEPERATION_GOLD)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                convert(
                                    material,
                                    MaterialLibAPI.findStack(material, Shapes.dust, 1),
                                    MaterialLibAPI.getStack(Materials.Gold, Shapes.dustSmall, 1),
                                    MaterialLibAPI.getStack(Materials.Gold, Shapes.nugget, 1)))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                    if (MaterialUtils.hasFlag(material, GTMaterialFlag.ELECTROMAGNETIC_SEPERATION_IRON)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                convert(
                                    material,
                                    MaterialLibAPI.findStack(material, Shapes.dust, 1),
                                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dustSmall, 1),
                                    MaterialLibAPI.getStack(Materials.Iron, Shapes.nugget, 1)))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                    if (MaterialUtils.hasFlag(material, GTMaterialFlag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                convert(
                                    material,
                                    MaterialLibAPI.findStack(material, Shapes.dust, 1),
                                    MaterialLibAPI.getStack(Materials.Neodymium, Shapes.dustSmall, 1),
                                    MaterialLibAPI.getStack(Materials.Neodymium, Shapes.nugget, 1)))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                }
                if (MaterialUtils.hasFlag(material, GTMaterialFlag.CRYSTALLISABLE)
                    && MaterialLibAPI.findStack(material, Shapes.gem, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(1)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gem, 1))
                        .outputChances(prefix == OrePrefixes.dustPure ? 9500 : 9000)
                        .fluidInputs(GTUtility.getWater(200L))
                        .duration(1 * MINUTES + 40 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(2)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gem, 1))
                        .outputChances(prefix == OrePrefixes.dustPure ? 10000 : 9500)
                        .fluidInputs(GTModHandler.getDistilledWater(100L))
                        .duration(1 * MINUTES + 15 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(3)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.gem, 1))
                        .outputChances(10000)
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, QUARTER_INGOTS))
                        .duration(1 * MINUTES)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                }
                if (!hasDust) return;
                ItemStack tImpureStack = GTUtility.copyAmount(
                    1L,
                    MaterialLibAPI.findStack(tByProduct, Shapes.dustTiny, 1),
                    MaterialLibAPI.findStack(tByProduct, Shapes.nugget, 1));
                if (tImpureStack == null) {
                    tImpureStack = MaterialLibAPI.findStack(tByProduct, Shapes.dustSmall, 1);
                    if (tImpureStack == null) {
                        tImpureStack = GTUtility.copyAmount(
                            1L,
                            MaterialLibAPI.findStack(tByProduct, Shapes.dust, 1),
                            MaterialLibAPI.findStack(tByProduct, Shapes.gem, 1));
                        if (tImpureStack == null) {
                            tImpureStack = MaterialParts.stack(OrePrefixes.cell, tByProduct, 1L);
                            if (tImpureStack == null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(1, stack))
                                    .itemOutputs(
                                        convertOre(material, MaterialLibAPI.findStack(material, Shapes.dust, 1)))
                                    .duration(Math.max(1L, MaterialUtils.mass(material)))
                                    .eut(5)
                                    .addTo(centrifugeRecipes);
                            } else {
                                FluidStack tFluid = GTUtility.getFluidForFilledItem(tImpureStack, true);
                                if (tFluid == null) {
                                    GTValues.RA.stdBuilder()
                                        .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Cell_Empty.get(1))
                                        .itemOutputs(
                                            convertOre(
                                                material,
                                                MaterialLibAPI.findStack(material, Shapes.dust, 9),
                                                tImpureStack))
                                        .duration(Math.max(1L, MaterialUtils.mass(material) * 72L))
                                        .eut(5)
                                        .addTo(centrifugeRecipes);
                                } else {
                                    tFluid.amount /= 10;
                                    GTValues.RA.stdBuilder()
                                        .itemInputs(GTUtility.copyAmount(1, stack))
                                        .itemOutputs(
                                            convertOre(material, MaterialLibAPI.findStack(material, Shapes.dust, 1)))
                                        .fluidOutputs(tFluid)
                                        .duration(Math.max(1L, MaterialUtils.mass(material) * 8L))
                                        .eut(5)
                                        .addTo(centrifugeRecipes);
                                }
                            }
                        } else {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(9, stack))
                                .itemOutputs(
                                    convertOre(
                                        material,
                                        MaterialLibAPI.findStack(material, Shapes.dust, 9),
                                        tImpureStack))
                                .duration(Math.max(1L, MaterialUtils.mass(material) * 72L))
                                .eut(5)
                                .addTo(centrifugeRecipes);
                        }
                    } else {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(2, stack))
                            .itemOutputs(
                                convertOre(material, MaterialLibAPI.findStack(material, Shapes.dust, 2), tImpureStack))
                            .duration(Math.max(1L, MaterialUtils.mass(material) * 16L))
                            .eut(5)
                            .addTo(centrifugeRecipes);
                    }
                } else {
                    ItemStack tByProductStack = GTUtility.copyAmount(
                        1L,
                        MaterialLibAPI.findStack(tByProduct, Shapes.dust, 1),
                        MaterialLibAPI.findStack(tByProduct, Shapes.nugget, 1));
                    if (tByProductStack != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                convertOre(
                                    material,
                                    MaterialLibAPI.findStack(material, Shapes.dust, 1),
                                    tByProductStack))
                            .outputChances(10000, 1111)
                            .duration(Math.max(1L, MaterialUtils.mass(material) * 8L))
                            .eut(5)
                            .addTo(centrifugeRecipes);
                    }
                }
            }
            case "dustSmall" -> {
                if (hasDust(material)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(4, stack), ItemList.Schematic_Dust.get(0L))
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.dust, 1))
                        .duration(1 * SECONDS)
                        .eut(4)
                        .addTo(packagerRecipes);
                }
                if (!MaterialUtils.blastFurnaceRequired(material)) {
                    GTRecipeRegistrator
                        .registerReverseFluidSmelting(stack, material, prefix.getMaterialAmount(), null, true);
                    if (GTRecipeRegistrator.hasReverseArcSmeltingRecipe(material)) {
                        GTRecipeRegistrator.registerReverseArcSmelting(
                            GTUtility.copyAmount(1, stack),
                            material,
                            prefix.getMaterialAmount(),
                            null,
                            null,
                            null);
                    }
                }
            }
            case "dustTiny" -> {
                if (hasDust(material)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Schematic_Dust.get(0L))
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.dust, 1))
                        .duration(1 * SECONDS)
                        .eut(4)
                        .addTo(packagerRecipes);
                }
                if (!MaterialUtils.blastFurnaceRequired(material)) {
                    GTRecipeRegistrator
                        .registerReverseFluidSmelting(stack, material, prefix.getMaterialAmount(), null, true);
                    if (GTRecipeRegistrator.hasReverseArcSmeltingRecipe(material)) {
                        GTRecipeRegistrator.registerReverseArcSmelting(
                            GTUtility.copyAmount(1, stack),
                            material,
                            prefix.getMaterialAmount(),
                            null,
                            null,
                            null);
                    }
                }
                if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING)) {
                    if (MaterialUtils.blastFurnaceRequired(material)) {
                        GTModHandler.removeFurnaceSmelting(stack);
                    }
                }
            }
            default -> {}
        }
    }

}
