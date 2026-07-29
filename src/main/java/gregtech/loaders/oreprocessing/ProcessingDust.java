package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingDust implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingDust INSTANCE;

    public ProcessingDust() {
        INSTANCE = this;
        OrePrefixes.dust.add(this);
        OrePrefixes.dustPure.add(this);
        OrePrefixes.dustImpure.add(this);
        OrePrefixes.dustRefined.add(this);
        OrePrefixes.dustSmall.add(this);
        OrePrefixes.dustTiny.add(this);
    }

    /// A material's own non-molten fluid, preferring its gas form over its liquid form (mirroring the werkstoff
    /// `getFluidOrGas` the dust fluid-extractor/solidifier recipes were ported from).
    private static FluidStack fluidFor(Material material, int amount) {
        return MaterialUtils.hasCorrespondingGas(material) ? MaterialUtils.gas(material, amount)
            : MaterialUtils.fluid(material, amount);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        switch (prefix.getName()) {
            case "dust" -> {
                if (GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Schematic_Dust_Small.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 4L))
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
                if ((GTUtility.getFluidForFilledItem(GTOreDictUnificator.get(OrePrefixes.cell, material, 1L), true)
                    == null) && (GTOreDictUnificator.get(OrePrefixes.cell, material, 1L) != null)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(stack, ItemList.Cell_Empty.get(1L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, material, 1L))
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
                    != (tDustStack = GTOreDictUnificator.get(OrePrefixes.ingot, MaterialUtils.smeltInto(material), 1L)))
                    && (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING))) {
                    if (MaterialUtils.blastFurnaceRequired(material)) {
                        GTModHandler.removeFurnaceSmelting(stack);
                        if (MaterialUtils.autoGenerateBlastFurnaceRecipes(material)) {
                            // A material carrying the werkstoff AnaerobeSmelting/NobleGasSmelting SubTag blast-
                            // smelts under a gas: BlastFurnaceWithGas fans one recipe out into a gas-input variant
                            // per BlastFurnaceGasStat, so it takes circuit 11 and the base gas amount in
                            // ADDITIVE_AMOUNT rather than the plain circuit-1 recipe. Ported from the retired
                            // bartworks DustLoader.
                            boolean gasSmelting = MaterialUtils.hasSubTag(material, "AnaerobeSmelting")
                                || MaterialUtils.hasSubTag(material, "NobleGasSmelting");
                            GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                            recipeBuilder.itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(gasSmelting ? 11 : 1);
                            if (MaterialUtils.blastFurnaceTemp(material) > 1750) {
                                recipeBuilder.itemOutputs(
                                    GTOreDictUnificator
                                        .get(OrePrefixes.ingotHot, MaterialUtils.smeltInto(material), tDustStack, 1L));
                            } else {
                                recipeBuilder.itemOutputs(GTUtility.copyAmount(1, tDustStack));
                            }
                            recipeBuilder
                                .duration(
                                    (Math.max(MaterialUtils.mass(material) / 40L, 1L)
                                        * MaterialUtils.blastFurnaceTemp(material)) * TICKS)
                                .eut(TierEU.RECIPE_MV)
                                .metadata(COIL_HEAT, MaterialUtils.blastFurnaceTemp(material));
                            if (gasSmelting) {
                                recipeBuilder.metadata(ADDITIVE_AMOUNT, 1000)
                                    .addTo(BlastFurnaceWithGas);
                            } else {
                                recipeBuilder.addTo(blastFurnaceRecipes);
                            }
                        }
                    } else {
                        GTModHandler.addSmeltingRecipe(stack, tDustStack);
                    }
                } else if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
                    if ((!OrePrefixes.block.isIgnored(material))
                        && (null == GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
                        && GTOreDictUnificator.get(OrePrefixes.block, material, 1L) != null
                        && (material != Materials2Materials.Clay)
                        && (material != Materials2Materials.Netherrack)) {

                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(9, stack))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, material, 1L))
                            .duration(15 * SECONDS)
                            .eut(2)
                            .addTo(compressorRecipes);
                    }
                    if (((OrePrefixes.block.isIgnored(material))
                        || (null == GTOreDictUnificator.get(OrePrefixes.block, material, 1L)))
                        && (material != Materials2Materials.GraniteRed)
                        && (material != Materials2Materials.GraniteBlack)
                        && (material != Materials2Materials.Basalt)
                        && (material != Materials2Materials.Marble)
                        && (material != Materials2Materials.Glass)
                        && (material != Materials2Materials.Obsidian)
                        && (material != Materials2Materials.Glowstone)
                        && (material != Materials2Materials.Paper)
                        && (material != Materials2Materials.TranscendentMetal)
                        && (material != Materials2Materials.Clay)
                        && (material != Materials2Materials.Wood)
                        && (material != Materials2Materials.Carbon)
                        && (material != Materials2Materials.Stone)) {
                        // compressor recipe
                        {
                            if (GTOreDictUnificator.get(OrePrefixes.plate, material, 1L) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(1, stack))
                                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L))
                                    .duration(15 * SECONDS)
                                    .eut(2)
                                    .addTo(compressorRecipes);
                            }
                        }
                    }
                }
                // A material with its own non-molten (liquid/gas) fluid: extract that fluid from its dust and
                // solidify it back, ported from the retired bartworks CellLoader. GT's other fluid-extractor and
                // fluid-solidifier autogen only handles the molten fluid, never a material's liquid/gas form.
                if (MaterialUtils.hasCorrespondingGas(material) || MaterialUtils.hasCorrespondingFluid(material)) {
                    long fluidEut = calculateRecipeEU(material, MaterialUtils.mass(material) > 128 ? 64 : 30);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .fluidOutputs(fluidFor(material, 1000))
                        .duration((int) MaterialUtils.mass(material))
                        .eut(fluidEut)
                        .recipeCategory(RecipeCategories.fluidExtractorRecycling)
                        .addTo(fluidExtractionRecipes);
                    GTValues.RA.stdBuilder()
                        .circuit(1)
                        .itemOutputs(GTUtility.copyAmount(1, stack))
                        .fluidInputs(fluidFor(material, 1000))
                        .duration((int) MaterialUtils.mass(material))
                        .eut(fluidEut)
                        .addTo(fluidSolidifierRecipes);
                }

                List<MaterialStack> tMaterialList = MaterialUtils.materialList(material);
                if ((!tMaterialList.isEmpty())
                    && (MaterialUtils.hasElectrolyzerRecipe(material) || MaterialUtils.hasCentrifugeRecipe(material))) {
                    long tItemAmount = 0L;
                    long tCapsuleCount = 0L;
                    long tDensityMultiplier = MaterialUtils.density(material) > 3628800L
                        ? MaterialUtils.density(material) / 3628800L
                        : 1L;
                    ArrayList<ItemStack> tList = new ArrayList<>();
                    for (MaterialStack tMat : tMaterialList) if (tMat.mAmount > 0L) {
                        if (tMat.mMaterial == Materials2Materials.Air) {
                            tDustStack = ItemList.Cell_Air.get(tMat.mAmount / 2L);
                        } else {
                            tDustStack = GTOreDictUnificator.get(OrePrefixes.dust, tMat.mMaterial, tMat.mAmount);
                            if (tDustStack == null)
                                tDustStack = GTOreDictUnificator.get(OrePrefixes.cell, tMat.mMaterial, tMat.mAmount);
                        }
                        if (tItemAmount + tMat.mAmount * 3628800L
                            <= stack.getMaxStackSize() * MaterialUtils.density(material)) {
                            tItemAmount += tMat.mAmount * 3628800L;
                            if (tDustStack != null) {
                                tDustStack.stackSize = ((int) (tDustStack.stackSize * tDensityMultiplier));
                                while ((tDustStack.stackSize > 64) && (tList.size() < 6)
                                    && (tCapsuleCount + GTModHandler.getCapsuleCellContainerCount(tDustStack) * 64L
                                        <= 64L)) {
                                    tCapsuleCount += GTModHandler.getCapsuleCellContainerCount(tDustStack) * 64L;
                                    tList.add(GTUtility.copyAmount(64, tDustStack));
                                    tDustStack.stackSize -= 64;
                                }
                                if ((tDustStack.stackSize > 0) && (tList.size() < 6)
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
                            if ((!ItemList.Cell_Air.isStackEqual(tList.get(i)))
                                && ((tFluid = GTUtility.getFluidForFilledItem(tList.get(i), true)) != null)) {
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
                                    ItemStack[] outputsArray = tList.toArray(new ItemStack[Math.min(tList.size(), 6)]);
                                    recipeBuilder.itemOutputs(outputsArray);
                                }
                                if (tFluid != null) {
                                    recipeBuilder.fluidOutputs(tFluid);
                                }
                                recipeBuilder
                                    .duration(
                                        Math.max(1L, Math.abs(MaterialUtils.protons(material) * 2L * tItemAmount)))
                                    .eut(Math.min(4, tList.size()) * 30)
                                    .addTo(electrolyzerRecipes);
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
                                    recipeBuilder.itemOutputs(outputsArray);
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
                    && GTOreDictUnificator.get(OrePrefixes.gem, material, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(1)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
                        .outputChances(7000)
                        .fluidInputs(GTUtility.getWater(200L))
                        .duration(1 * MINUTES + 40 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(2)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
                        .outputChances(9000)
                        .fluidInputs(GTModHandler.getDistilledWater(100L))
                        .duration(1 * MINUTES + 15 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(3)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
                        .outputChances(10000)
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(
                                Materials2Materials.Void,
                                Materials2FluidShapes.fluidMolten,
                                (int) (1 * QUARTER_INGOTS)))
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
                            MaterialLibAPI.getStack(Materials2Materials.MeatCooked, Materials2Shapes.dust, (int) (1)));
                        break;
                    case "Oilsands":
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .fluidOutputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials2Materials.OilHeavy,
                                    Materials2FluidShapes.fluidLiquid,
                                    (int) (1_000)))
                            .duration(33 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(centrifugeRecipes);
                        break;
                    case "HydratedCoal":
                        GTModHandler.addSmeltingRecipe(
                            GTUtility.copyAmount(1, stack),
                            MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.dust, (int) (1)));
                        break;
                    case "Diamond": {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(4, stack))
                            .itemOutputs(
                                ItemList.IC2_Industrial_Diamond.get(3L),
                                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials2Materials.DarkAsh, 16L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .metadata(ADDITIVE_AMOUNT, 32)
                            .addTo(implosionRecipes);
                    }
                        break;
                    case "ManaDiamond":
                    case "BotaniaDragonstone": {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(4, stack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.gem, material, 3L),
                                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials2Materials.DarkAsh, 16L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .metadata(ADDITIVE_AMOUNT, 32)
                            .addTo(implosionRecipes);
                    }
                        break;
                    case "Opal":
                    case "Olivine":
                    case "Emerald":
                    case "Ruby":
                    case "Sapphire":
                    case "GreenSapphire":
                    case "Topaz":
                    case "BlueTopaz":
                    case "Tanzanite":
                    case "Amethyst": {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(4, stack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.gem, material, 3L),
                                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials2Materials.DarkAsh, 12L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .metadata(ADDITIVE_AMOUNT, 24)
                            .addTo(implosionRecipes);
                    }
                        break;
                    case "FoolsRuby":
                    case "GarnetRed":
                    case "GarnetYellow":
                    case "Jasper":
                    case "Amber":
                    case "Monazite":
                    case "Forcicium":
                    case "Forcillium":
                    case "Force": {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(4, stack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.gem, material, 3L),
                                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials2Materials.DarkAsh, 8L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .metadata(ADDITIVE_AMOUNT, 16)
                            .addTo(implosionRecipes);
                    }
                        break;
                    default: {
                        // Gem materials not special-cased above (every werkstoff-derived gem reaches GT here):
                        // dust -> gem implosion, ported from the retired bartworks GemLoader, skipped for NoBlast.
                        if (GTOreDictUnificator.get(OrePrefixes.gem, material, 1L) != null
                            && !MaterialUtils.hasSubTag(material, "NoBlast")) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(4, stack))
                                .itemOutputs(
                                    GTOreDictUnificator.get(OrePrefixes.gem, material, 3L),
                                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials2Materials.DarkAsh, 8L))
                                .duration(20 * TICKS)
                                .eut(TierEU.RECIPE_LV)
                                .metadata(ADDITIVE_AMOUNT, 24)
                                .addTo(implosionRecipes);
                        }
                    }
                }
            }
            case "dustPure", "dustImpure", "dustRefined" -> {
                if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
                    return;
                }

                Material tByProduct = GTUtility.selectItemInList(
                    prefix == OrePrefixes.dustRefined ? 2 : prefix == OrePrefixes.dustPure ? 1 : 0,
                    material,
                    MaterialUtils.oreByProducts(material));
                if (prefix == OrePrefixes.dustPure) {
                    if (MaterialUtils.hasFlag(material, GTMaterialFlag.ELECTROMAGNETIC_SEPERATION_GOLD)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.dust, material, 1L),
                                MaterialLibAPI
                                    .getStack(Materials2Materials.Gold, Materials2Shapes.dustSmall, (int) (1)),
                                GTOreDictUnificator.get(OrePrefixes.nugget, Materials2Materials.Gold, 1L))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                    if (MaterialUtils.hasFlag(material, GTMaterialFlag.ELECTROMAGNETIC_SEPERATION_IRON)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.dust, material, 1L),
                                MaterialLibAPI
                                    .getStack(Materials2Materials.Iron, Materials2Shapes.dustSmall, (int) (1)),
                                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.nugget, (int) (1)))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                    if (MaterialUtils.hasFlag(material, GTMaterialFlag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.dust, material, 1L),
                                MaterialLibAPI
                                    .getStack(Materials2Materials.Neodymium, Materials2Shapes.dustSmall, (int) (1)),
                                MaterialLibAPI
                                    .getStack(Materials2Materials.Neodymium, Materials2Shapes.nugget, (int) (1)))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                }
                if (MaterialUtils.hasFlag(material, GTMaterialFlag.CRYSTALLISABLE)
                    && GTOreDictUnificator.get(OrePrefixes.gem, material, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(1)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
                        .outputChances(prefix == OrePrefixes.dustPure ? 9500 : 9000)
                        .fluidInputs(GTUtility.getWater(200L))
                        .duration(1 * MINUTES + 40 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(2)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
                        .outputChances(prefix == OrePrefixes.dustPure ? 10000 : 9500)
                        .fluidInputs(GTModHandler.getDistilledWater(100L))
                        .duration(1 * MINUTES + 15 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(3)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
                        .outputChances(10000)
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(
                                Materials2Materials.Void,
                                Materials2FluidShapes.fluidMolten,
                                (int) (1 * QUARTER_INGOTS)))
                        .duration(1 * MINUTES)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                }
                ItemStack tImpureStack = GTOreDictUnificator.get(
                    OrePrefixes.dustTiny,
                    tByProduct,
                    GTOreDictUnificator.get(OrePrefixes.nugget, tByProduct, 1L),
                    1L);
                if (tImpureStack == null) {
                    tImpureStack = GTOreDictUnificator.get(OrePrefixes.dustSmall, tByProduct, 1L);
                    if (tImpureStack == null) {
                        tImpureStack = GTOreDictUnificator.get(
                            OrePrefixes.dust,
                            tByProduct,
                            GTOreDictUnificator.get(OrePrefixes.gem, tByProduct, 1L),
                            1L);
                        if (tImpureStack == null) {
                            tImpureStack = GTOreDictUnificator.get(OrePrefixes.cell, tByProduct, 1L);
                            if (tImpureStack == null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(1, stack))
                                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 1L))
                                    .duration(Math.max(1L, MaterialUtils.mass(material)))
                                    .eut(5)
                                    .addTo(centrifugeRecipes);
                            } else {
                                FluidStack tFluid = GTUtility.getFluidForFilledItem(tImpureStack, true);
                                if (tFluid == null) {
                                    GTValues.RA.stdBuilder()
                                        .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Cell_Empty.get(1))
                                        .itemOutputs(
                                            GTOreDictUnificator.get(OrePrefixes.dust, material, 9L),
                                            tImpureStack)
                                        .duration(Math.max(1L, MaterialUtils.mass(material) * 72L))
                                        .eut(5)
                                        .addTo(centrifugeRecipes);
                                } else {
                                    tFluid.amount /= 10;
                                    GTValues.RA.stdBuilder()
                                        .itemInputs(GTUtility.copyAmount(1, stack))
                                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 1L))
                                        .fluidOutputs(tFluid)
                                        .duration(Math.max(1L, MaterialUtils.mass(material) * 8L))
                                        .eut(5)
                                        .addTo(centrifugeRecipes);
                                }
                            }
                        } else {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(9, stack))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 9L), tImpureStack)
                                .duration(Math.max(1L, MaterialUtils.mass(material) * 72L))
                                .eut(5)
                                .addTo(centrifugeRecipes);
                        }
                    } else {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(2, stack))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 2L), tImpureStack)
                            .duration(Math.max(1L, MaterialUtils.mass(material) * 16L))
                            .eut(5)
                            .addTo(centrifugeRecipes);
                    }
                } else {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(
                            GTOreDictUnificator.get(OrePrefixes.dust, material, 1L),
                            GTOreDictUnificator.get(
                                OrePrefixes.dust,
                                tByProduct,
                                GTOreDictUnificator.get(OrePrefixes.nugget, tByProduct, 1L),
                                1L))
                        .outputChances(10000, 1111)
                        .duration(Math.max(1L, MaterialUtils.mass(material) * 8L))
                        .eut(5)
                        .addTo(centrifugeRecipes);
                }
            }
            case "dustSmall" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(4, stack), ItemList.Schematic_Dust.get(0L))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 1L))
                    .duration(1 * SECONDS)
                    .eut(4)
                    .addTo(packagerRecipes);
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
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Schematic_Dust.get(0L))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 1L))
                    .duration(1 * SECONDS)
                    .eut(4)
                    .addTo(packagerRecipes);
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

    /// Runs only the `dust` prefix's CRYSTALLISABLE autoclave recipes (see the `"dust"` case above) for a
    /// recognition marker -- the rest of that switch depends on legacy-only state
    /// (`mBlastFurnaceRequired`, `mMaterialList`, `mName`, ...) that a marker never carries, and no recognition
    /// marker other than `Fluix` reaches this registrator to begin with.
    @Override
    public void registerRecognitionOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (prefix != OrePrefixes.dust) {
            return;
        }
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.CRYSTALLISABLE)
            && GTOreDictUnificator.get(OrePrefixes.gem, material, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .circuit(1)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
                .outputChances(7000)
                .fluidInputs(GTUtility.getWater(200L))
                .duration(1 * MINUTES + 40 * SECONDS)
                .eut(24)
                .addTo(autoclaveRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .circuit(2)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
                .outputChances(9000)
                .fluidInputs(GTModHandler.getDistilledWater(100L))
                .duration(1 * MINUTES + 15 * SECONDS)
                .eut(24)
                .addTo(autoclaveRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .circuit(3)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, material, 1L))
                .outputChances(10000)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Void,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * QUARTER_INGOTS)))
                .duration(1 * MINUTES)
                .eut(24)
                .addTo(autoclaveRecipes);
        }
    }
}
