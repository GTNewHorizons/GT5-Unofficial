package gregtech.api.util;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.enums.GTValues.VP;
import static gregtech.api.enums.Materials.Bronze;
import static gregtech.api.enums.Materials.Cobalt;
import static gregtech.api.enums.Materials.DarkSteel;
import static gregtech.api.enums.Materials.Diamond;
import static gregtech.api.enums.Materials.FierySteel;
import static gregtech.api.enums.Materials.Gold;
import static gregtech.api.enums.Materials.Iron;
import static gregtech.api.enums.Materials.IronWood;
import static gregtech.api.enums.Materials.Knightmetal;
import static gregtech.api.enums.Materials.Lead;
import static gregtech.api.enums.Materials.Ruby;
import static gregtech.api.enums.Materials.Sapphire;
import static gregtech.api.enums.Materials.Steel;
import static gregtech.api.enums.Materials.Steeleaf;
import static gregtech.api.enums.Materials.Thaumium;
import static gregtech.api.enums.Materials.Void;
import static gregtech.api.recipe.RecipeMaps.arcFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.RECYCLE;
import static gregtech.api.util.GTRecipeConstants.UniversalArcFurnace;
import static gregtech.api.util.GTUtility.calculateRecipeEU;
import static gregtech.api.util.GTUtility.getTier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;
import com.ruling_0.materiallib.api.Material;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.recipe.RecipeCategory;
import gregtech.mixin.interfaces.accessors.ShapedOreRecipeAccessor;
import ic2.api.reactor.IReactorComponent;

/**
 * Class for Automatic Recipe registering.
 */
public class GTRecipeRegistrator {

    private static Supplier<RecipeCategory> arcFurnaceRecyclingCategorySupplier = () -> RecipeCategories.arcFurnaceRecycling;

    /**
     * List of Materials, which are used in the Creation of Sticks. All Rod Materials are automatically added to this
     * List.
     */
    public static final List<Materials> sRodMaterialList = new ArrayList<>();

    private static final ItemStack sMt1 = new ItemStack(Blocks.dirt, 1, 0), sMt2 = new ItemStack(Blocks.dirt, 1, 0);
    private static final String s_H = "h", s_F = "f", s_I = "I", s_P = "P", s_R = "R";
    private static final RecipeShape[] sShapes = new RecipeShape[] {
        new RecipeShape(sMt1, null, sMt1, sMt1, sMt1, sMt1, null, sMt1, null),
        new RecipeShape(sMt1, null, sMt1, sMt1, null, sMt1, sMt1, sMt1, sMt1),
        new RecipeShape(null, sMt1, null, sMt1, sMt1, sMt1, sMt1, null, sMt1),
        new RecipeShape(sMt1, sMt1, sMt1, sMt1, null, sMt1, null, null, null),
        new RecipeShape(sMt1, null, sMt1, sMt1, sMt1, sMt1, sMt1, sMt1, sMt1),
        new RecipeShape(sMt1, sMt1, sMt1, sMt1, null, sMt1, sMt1, null, sMt1),
        new RecipeShape(null, null, null, sMt1, null, sMt1, sMt1, null, sMt1),
        new RecipeShape(null, sMt1, null, null, sMt1, null, null, sMt2, null),
        new RecipeShape(sMt1, sMt1, sMt1, null, sMt2, null, null, sMt2, null),
        new RecipeShape(null, sMt1, null, null, sMt2, null, null, sMt2, null),
        new RecipeShape(sMt1, sMt1, null, sMt1, sMt2, null, null, sMt2, null),
        new RecipeShape(null, sMt1, sMt1, null, sMt2, sMt1, null, sMt2, null),
        new RecipeShape(sMt1, sMt1, null, null, sMt2, null, null, sMt2, null),
        new RecipeShape(null, sMt1, sMt1, null, sMt2, null, null, sMt2, null),
        new RecipeShape(null, sMt1, null, sMt1, null, null, null, sMt1, sMt2),
        new RecipeShape(null, sMt1, null, null, null, sMt1, sMt2, sMt1, null),
        new RecipeShape(null, sMt1, null, sMt1, null, sMt1, null, null, sMt2),
        new RecipeShape(null, sMt1, null, sMt1, null, sMt1, sMt2, null, null),
        new RecipeShape(null, sMt2, null, null, sMt1, null, null, sMt1, null),
        new RecipeShape(null, sMt2, null, null, sMt2, null, sMt1, sMt1, sMt1),
        new RecipeShape(null, sMt2, null, null, sMt2, null, null, sMt1, null),
        new RecipeShape(null, sMt2, null, sMt1, sMt2, null, sMt1, sMt1, null),
        new RecipeShape(null, sMt2, null, null, sMt2, sMt1, null, sMt1, sMt1),
        new RecipeShape(null, sMt2, null, null, sMt2, null, sMt1, sMt1, null),
        new RecipeShape(sMt1, null, null, null, sMt2, null, null, null, sMt2),
        new RecipeShape(null, null, sMt1, null, sMt2, null, sMt2, null, null),
        new RecipeShape(sMt1, null, null, null, sMt2, null, null, null, null),
        new RecipeShape(null, null, sMt1, null, sMt2, null, null, null, null),
        new RecipeShape(sMt1, sMt2, null, null, null, null, null, null, null),
        new RecipeShape(sMt2, sMt1, null, null, null, null, null, null, null),
        new RecipeShape(sMt1, null, null, sMt2, null, null, null, null, null),
        new RecipeShape(sMt2, null, null, sMt1, null, null, null, null, null),
        new RecipeShape(sMt1, sMt1, sMt1, sMt1, sMt1, sMt1, null, sMt2, null),
        new RecipeShape(sMt1, sMt1, null, sMt1, sMt1, sMt2, sMt1, sMt1, null),
        new RecipeShape(null, sMt1, sMt1, sMt2, sMt1, sMt1, null, sMt1, sMt1),
        new RecipeShape(null, sMt2, null, sMt1, sMt1, sMt1, sMt1, sMt1, sMt1),
        new RecipeShape(sMt1, sMt1, sMt1, sMt1, sMt2, sMt1, null, sMt2, null),
        new RecipeShape(sMt1, sMt1, null, sMt1, sMt2, sMt2, sMt1, sMt1, null),
        new RecipeShape(null, sMt1, sMt1, sMt2, sMt2, sMt1, null, sMt1, sMt1),
        new RecipeShape(null, sMt2, null, sMt1, sMt2, sMt1, sMt1, sMt1, sMt1),
        new RecipeShape(sMt1, null, null, null, sMt1, null, null, null, null),
        new RecipeShape(null, sMt1, null, sMt1, null, null, null, null, null),
        new RecipeShape(sMt1, sMt1, null, sMt2, null, sMt1, sMt2, null, null),
        new RecipeShape(null, sMt1, sMt1, sMt1, null, sMt2, null, null, sMt2) };
    private static volatile Map<RecipeShape, List<IRecipe>> indexedRecipeListCache;
    private static final String[][] sShapesA = new String[][] { null, null, null,
        { "Helmet", s_P + s_P + s_P, s_P + s_H + s_P },
        { "ChestPlate", s_P + s_H + s_P, s_P + s_P + s_P, s_P + s_P + s_P },
        { "Pants", s_P + s_P + s_P, s_P + s_H + s_P, s_P + " " + s_P }, { "Boots", s_P + " " + s_P, s_P + s_H + s_P },
        { "Sword", " " + s_P + " ", s_F + s_P + s_H, " " + s_R + " " },
        { "Pickaxe", s_P + s_I + s_I, s_F + s_R + s_H, " " + s_R + " " },
        { "Shovel", s_F + s_P + s_H, " " + s_R + " ", " " + s_R + " " },
        { "Axe", s_P + s_I + s_H, s_P + s_R + " ", s_F + s_R + " " },
        { "Axe", s_P + s_I + s_H, s_P + s_R + " ", s_F + s_R + " " },
        { "Hoe", s_P + s_I + s_H, s_F + s_R + " ", " " + s_R + " " },
        { "Hoe", s_P + s_I + s_H, s_F + s_R + " ", " " + s_R + " " },
        { "Sickle", " " + s_P + " ", s_P + s_F + " ", s_H + s_P + s_R },
        { "Sickle", " " + s_P + " ", s_P + s_F + " ", s_H + s_P + s_R },
        { "Sickle", " " + s_P + " ", s_P + s_F + " ", s_H + s_P + s_R },
        { "Sickle", " " + s_P + " ", s_P + s_F + " ", s_H + s_P + s_R },
        { "Sword", " " + s_R + " ", s_F + s_P + s_H, " " + s_P + " " },
        { "Pickaxe", " " + s_R + " ", s_F + s_R + s_H, s_P + s_I + s_I },
        { "Shovel", " " + s_R + " ", " " + s_R + " ", s_F + s_P + s_H },
        { "Axe", s_F + s_R + " ", s_P + s_R + " ", s_P + s_I + s_H },
        { "Axe", s_F + s_R + " ", s_P + s_R + " ", s_P + s_I + s_H },
        { "Hoe", " " + s_R + " ", s_F + s_R + " ", s_P + s_I + s_H },
        { "Hoe", " " + s_R + " ", s_F + s_R + " ", s_P + s_I + s_H },
        { "Spear", s_P + s_H + " ", s_F + s_R + " ", " " + " " + s_R },
        { "Spear", s_P + s_H + " ", s_F + s_R + " ", " " + " " + s_R }, { "Knive", s_H + s_P, s_R + s_F },
        { "Knive", s_F + s_H, s_P + s_R }, { "Knive", s_F + s_H, s_P + s_R }, { "Knive", s_P + s_F, s_R + s_H },
        { "Knive", s_P + s_F, s_R + s_H }, null, null, null, null,
        { "WarAxe", s_P + s_P + s_P, s_P + s_R + s_P, s_F + s_R + s_H }, null, null, null,
        { "Shears", s_H + s_P, s_P + s_F }, { "Shears", s_H + s_P, s_P + s_F },
        { "Scythe", s_I + s_P + s_H, s_R + s_F + s_P, s_R + " " + " " },
        { "Scythe", s_H + s_P + s_I, s_P + s_F + s_R, " " + " " + s_R } };

    static {
        // flush the cache on post load finish
        GregTechAPI.sAfterGTPostload.add(() -> indexedRecipeListCache = null);
    }

    public static void registerMaterialRecycling(ItemStack stack, Materials material, long materialAmount,
        MaterialStack byproduct) {
        if (GTUtility.isStackInvalid(stack)) return;
        if (byproduct != null) {
            byproduct = byproduct.clone();
            byproduct.mAmount /= stack.stackSize;
        }
        GTOreDictUnificator.addItemData(
            GTUtility.copyAmount(1, stack),
            new ItemData(material, materialAmount / stack.stackSize, byproduct));
    }

    public static void registerMaterialRecycling(ItemStack stack, ItemData data) {
        if (GTUtility.isStackInvalid(stack) || GTUtility.areStacksEqual(new ItemStack(Items.blaze_rod), stack)
            || GTUtility.areStacksEqual(GTOreDictUnificator.get(OrePrefixes.block, Materials.Ichorium, 1L), stack)
            || GTUtility.areStacksEqual(new ItemStack(Blocks.quartz_block, 1), stack)
            || GTUtility.areStacksEqual(new ItemStack(Blocks.obsidian), stack)
            || data == null
            || !data.hasValidMaterialData()
            || (MU.materialOf(data.mMaterial.mMaterial) instanceof Materials primary
                && !primary.mAutoGenerateRecycleRecipes)
            || data.mMaterial.mAmount <= 0
            || GTUtility.getFluidForFilledItem(stack, false) != null) return;
        registerReverseMacerating(GTUtility.copyAmount(1, stack), data, data.mPrefix == null, true);
        if (!GTUtility.areStacksEqual(GTModHandler.getIC2Item("iridiumOre", 1L), stack)) {
            registerReverseSmelting(
                GTUtility.copyAmount(1, stack),
                data.mMaterial.mMaterial,
                data.mMaterial.mAmount,
                true);
            registerReverseFluidSmelting(
                GTUtility.copyAmount(1, stack),
                data.mMaterial.mMaterial,
                data.mMaterial.mAmount,
                data.getByProduct(0),
                true);
            registerReverseArcSmelting(GTUtility.copyAmount(1, stack), data);
        }
    }

    /**
     * @param stack          the stack to be recycled.
     * @param material       the Material.
     * @param materialAmount the amount of it in Material Units.
     * @param isRecycling    whether to put in recycling tab.
     */
    /// [#registerReverseFluidSmelting(ItemStack, Materials, long, MaterialStack, boolean)] for callers holding a
    /// [Material] directly -- the native ML body; the [Materials]-typed overload now delegates into this one.
    public static void registerReverseFluidSmelting(ItemStack stack, Material material, long materialAmount,
        MaterialStack byproduct, boolean isRecycling) {
        if (stack == null || material == null) return;
        Material smeltTarget = MU.smeltInto(material);
        if (!MU.hasMolten(smeltTarget) || !MU.hasFlag(material, GTMaterialFlag.SMELTING_TO_FLUID)
            || (materialAmount * INGOTS) / (M * stack.stackSize) <= 0) return;

        Material byproductMaterial = byproduct == null ? null : byproduct.mMaterial;
        ItemStack recipeOutput = byproductMaterial == null ? null
            : MU.hasFlag(byproductMaterial, GTMaterialFlag.NO_SMELTING)
                || !MU.hasFlag(byproductMaterial, GTMaterialFlag.METAL)
                    ? MU.hasFlag(byproductMaterial, GTMaterialFlag.FLAMMABLE)
                        ? GTOreDictUnificator.getDust(MU.material(Materials.Ash), byproduct.mAmount / 2)
                        : MU.hasFlag(byproductMaterial, GTMaterialFlag.UNBURNABLE)
                            ? GTOreDictUnificator.getDustOrIngot(MU.smeltInto(byproductMaterial), byproduct.mAmount)
                            : null
                    : GTOreDictUnificator.getIngotOrDust(MU.smeltInto(byproductMaterial), byproduct.mAmount);

        GTRecipeBuilder builder = RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack));
        if (recipeOutput != null) {
            builder.itemOutputs(recipeOutput);
        }
        FluidStack moltenFluid = MU.molten(smeltTarget, (materialAmount * INGOTS) / (M * stack.stackSize));
        long powerUsage = Math.max(
            8,
            (long) Math.sqrt(
                2 * moltenFluid.getFluid()
                    .getTemperature()));
        // avoid full amp recipes
        int powerTier = getTier(powerUsage);
        if (powerTier > 0 && powerTier < VP.length && powerUsage > VP[powerTier]) {
            powerUsage = VP[powerTier];
        }
        builder.fluidOutputs(moltenFluid)
            .duration((int) Math.max(1, (24 * materialAmount) / M))
            .eut(powerUsage);
        if (isRecycling) builder.recipeCategory(RecipeCategories.fluidExtractorRecycling);
        builder.addTo(fluidExtractionRecipes);
    }

    public static void registerReverseFluidSmelting(ItemStack stack, Materials material, long materialAmount,
        MaterialStack byproduct, boolean isRecycling) {
        registerReverseFluidSmelting(stack, MU.material(material), materialAmount, byproduct, isRecycling);
    }

    /**
     * @param stack             the stack to be recycled.
     * @param material          the Material.
     * @param materialAmount    the amount of it in Material Units.
     * @param allowAlloySmelter if it is allowed to be recycled inside the Alloy Smelter.
     */
    /// [#registerReverseSmelting(ItemStack, Materials, long, boolean)] for callers holding a [Material] directly
    /// -- the native ML body; the [Materials]-typed overload now delegates into this one.
    public static void registerReverseSmelting(ItemStack stack, Material material, long materialAmount,
        boolean allowAlloySmelter) {
        if (stack == null || material == null
            || materialAmount <= 0
            || MU.hasFlag(material, GTMaterialFlag.NO_SMELTING)
            || (materialAmount > M && MU.hasFlag(material, GTMaterialFlag.METAL))
            || (MU.processingMaterialTierEU(material) > TierEU.IV)) return;
        if (material == MU.material(Materials.Naquadah) || material == MU.material(Materials.NaquadahEnriched)) return;

        materialAmount /= stack.stackSize;

        if (allowAlloySmelter) GTModHandler.addSmeltingAndAlloySmeltingRecipe(
            GTUtility.copyAmount(1, stack),
            GTOreDictUnificator.getIngot(MU.smeltInto(material), materialAmount),
            false);
        else GTModHandler.addSmeltingRecipe(
            GTUtility.copyAmount(1, stack),
            GTOreDictUnificator.getIngot(MU.smeltInto(material), materialAmount));
    }

    public static void registerReverseSmelting(ItemStack stack, Materials material, long materialAmount,
        boolean allowAlloySmelter) {
        registerReverseSmelting(stack, MU.material(material), materialAmount, allowAlloySmelter);
    }

    /// [#registerReverseArcSmelting(ItemStack, Materials, long, MaterialStack, MaterialStack, MaterialStack)] for
    /// callers holding a [Material] directly -- the native ML body; the [Materials]-typed overload now delegates
    /// into this one. Builds the [ItemData] straight from `material`, which already carries every read the
    /// [ItemStack, ItemData] overload below needs, without a legacy round trip.
    public static void registerReverseArcSmelting(ItemStack stack, Material material, long materialAmount,
        MaterialStack byProduct01, MaterialStack byProduct02, MaterialStack byProduct03) {
        registerReverseArcSmelting(
            stack,
            new ItemData(
                material == null ? null : new MaterialStack(material, materialAmount),
                byProduct01,
                byProduct02,
                byProduct03));
    }

    public static void registerReverseArcSmelting(ItemStack stack, Materials material, long materialAmount,
        MaterialStack byProduct01, MaterialStack byProduct02, MaterialStack byProduct03) {
        registerReverseArcSmelting(stack, MU.material(material), materialAmount, byProduct01, byProduct02, byProduct03);
    }

    /// [#hasReverseArcSmeltingRecipe(Materials)] for callers holding a [Material] directly -- the native ML body;
    /// the [Materials]-typed overload now delegates into this one. The gas-conditional arc-smelting recipe
    /// (`Materials#mArcSmeltIntoWithGas`) has no MaterialLib property equivalent -- see [LegacyMaterials]'s
    /// javadoc, only `Copper`'s canonical port and the unbacked `AnyCopper` marker ever carry one -- so this
    /// keeps a [MU#materialOf] guard for that residual read alone, matching the legacy interface default of no
    /// gas-arc recipe when the material has no legacy counterpart to read it from.
    public static boolean hasReverseArcSmeltingRecipe(Material material) {
        if (material == null) return false;
        Material arcSmeltingMaterial = MU.arcSmeltInto(MU.smeltInto(material));
        if (arcSmeltingMaterial != material) return true;
        return MU.materialOf(arcSmeltingMaterial) instanceof Materials legacy && !legacy.mArcSmeltIntoWithGas.isEmpty();
    }

    public static boolean hasReverseArcSmeltingRecipe(Materials material) {
        return hasReverseArcSmeltingRecipe(MU.material(material));
    }

    static void setArcFurnaceRecyclingCategorySupplier(Supplier<RecipeCategory> supplier) {
        arcFurnaceRecyclingCategorySupplier = supplier;
    }

    static void resetArcFurnaceRecyclingCategorySupplier() {
        arcFurnaceRecyclingCategorySupplier = () -> RecipeCategories.arcFurnaceRecycling;
    }

    public static void registerReverseArcSmelting(ItemStack stack, ItemData data) {
        registerReverseArcSmelting(stack, data, UniversalArcFurnace, arcFurnaceRecipes);
    }

    static void registerReverseArcSmelting(ItemStack stack, ItemData data, IRecipeMap universalArcFurnace,
        IRecipeMap arcFurnaceRecipes) {
        registerReverseArcSmelting(stack, data, universalArcFurnace, arcFurnaceRecipes, Materials::getGas);
    }

    static void registerReverseArcSmelting(ItemStack stack, ItemData data, IRecipeMap universalArcFurnace,
        IRecipeMap arcFurnaceRecipes, BiFunction<Materials, Long, FluidStack> gasStackSupplier) {
        if (stack == null || data == null) return;
        data = new ItemData(data);

        if (!data.hasValidMaterialData()) return;

        if (MU.materialOf(data.mMaterial.mMaterial) instanceof Materials primary
            && MU.hasFlag(primary, GTMaterialFlag.NO_RECYCLING_RECIPES)) return;

        boolean isRecycle = true;

        for (MaterialStack tMaterial : data.getAllMaterialStacks()) {
            if (!(MU.materialOf(tMaterial.mMaterial) instanceof Materials material)) {
                // A material with no legacy Materials counterpart (e.g. an unbacked RecognitionMaterials/
                // LegacyMarkerMaterials marker's shapeless wildcard backing) never defaults to smelting into
                // itself: unlike a real Materials constant, it only has an arc-smelting target when one was
                // explicitly declared, mirroring MarkerMaterial#getSmeltInto/#getArcSmeltInto.
                boolean declaresSmeltTarget = tMaterial.mMaterial.getProperty(GTMaterialProperties.SMELT_INTO) != null
                    || tMaterial.mMaterial.getProperty(GTMaterialProperties.ARC_SMELT_INTO) != null;
                Material arcTarget = declaresSmeltTarget ? MU.arcSmeltInto(MU.smeltInto(tMaterial.mMaterial)) : null;
                if (arcTarget == null) {
                    tMaterial.mAmount = 0;
                } else {
                    tMaterial.mMaterial = arcTarget;
                }
                continue;
            }

            if (material == Materials.Iron || material == Materials.Copper
                || material == Materials.CastIron
                || material == Materials.AnnealedCopper) {
                ItemData stackData = GTOreDictUnificator.getItemData(stack);
                if (stackData != null
                    && (stackData.mPrefix == OrePrefixes.ingot || stackData.mPrefix == OrePrefixes.dust)) {
                    // iron ingot/dust -> cast iron, copper ingot/dust -> annealed copper
                    isRecycle = false;
                }
            }

            if (MU.hasFlag(material, GTMaterialFlag.UNBURNABLE)) {
                tMaterial.mMaterial = MU.arcSmeltInto(MU.smeltInto(tMaterial.mMaterial));
                continue;
            }
            if (MU.hasFlag(material, GTMaterialFlag.EXPLOSIVE)) {
                tMaterial.mMaterial = MU.material(Materials.Ash);
                tMaterial.mAmount /= 16;
                continue;
            }
            if (MU.hasFlag(material, GTMaterialFlag.FLAMMABLE)) {
                tMaterial.mMaterial = MU.material(Materials.Ash);
                tMaterial.mAmount /= 8;
                continue;
            }
            if (MU.hasFlag(material, GTMaterialFlag.NO_SMELTING)) {
                tMaterial.mAmount = 0;
                continue;
            }
            if (MU.hasFlag(material, GTMaterialFlag.METAL)) {

                tMaterial.mMaterial = MU.arcSmeltInto(MU.smeltInto(tMaterial.mMaterial));
                continue;
            }
            tMaterial.mAmount = 0;
        }

        data = new ItemData(data);
        if (data.mByProducts.length > 3) for (MaterialStack tMaterial : data.getAllMaterialStacks()) {
            if (tMaterial.mMaterial == MU.material(Materials.Ash)) tMaterial.mAmount = 0;
        }

        data = new ItemData(data);

        if (!data.hasValidMaterialData()) return;

        long tAmount = 0;
        for (MaterialStack tMaterial : data.getAllMaterialStacks()) {
            Materials tMaterialLegacy = MU.materialOf(tMaterial.mMaterial);
            tAmount += tMaterial.mAmount * (tMaterialLegacy == null ? 0 : tMaterialLegacy.getMass());
        }

        ArrayList<ItemStack> outputs = new ArrayList<>();
        if (GTOreDictUnificator.getIngotOrDust(data.mMaterial) != null) {
            outputs.add(GTOreDictUnificator.getIngotOrDust(data.mMaterial));
        }
        for (int i = 0; i < 8; i++) {
            if (GTOreDictUnificator.getIngotOrDust(data.getByProduct(i)) != null) {
                outputs.add(GTOreDictUnificator.getIngotOrDust(data.getByProduct(i)));
            }
        }
        if (!outputs.isEmpty()) {
            if (!isNoOpReverseArcSmelting(stack, outputs)) {
                GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                recipeBuilder.itemInputs(stack)
                    .itemOutputs(outputs.toArray(new ItemStack[0]))
                    .duration(((int) Math.max(16L, tAmount / M)) * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .metadata(RECYCLE, isRecycle)
                    .addTo(universalArcFurnace);
            }

            int gasAmount = (int) Math.max(16L, tAmount / M);
            for (Materials gas : getArcSmeltingGases(outputs)) {
                ItemStack[] gasOutputs = getArcSmeltingOutputsWithGas(outputs, gas);
                FluidStack gasStack = gasStackSupplier.apply(gas, (long) gasAmount);
                if (gasOutputs == null || gasStack == null) continue;
                GTRecipeBuilder gasRecipeBuilder = GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .circuit(11)
                    .itemOutputs(gasOutputs)
                    .fluidInputs(gasStack)
                    .duration(gasAmount * TICKS)
                    .eut(TierEU.RECIPE_LV);
                if (isRecycle) gasRecipeBuilder.recipeCategory(arcFurnaceRecyclingCategorySupplier.get());
                gasRecipeBuilder.addTo(arcFurnaceRecipes);
            }
        }

    }

    private static boolean isNoOpReverseArcSmelting(ItemStack input, List<ItemStack> outputs) {
        return outputs.size() == 1 && GTOreDictUnificator.isInputStackEqual(input, outputs.get(0))
            && input.stackSize == outputs.get(0).stackSize;
    }

    private static Set<Materials> getArcSmeltingGases(List<ItemStack> outputs) {
        Set<Materials> gases = new LinkedHashSet<>();
        for (ItemStack output : outputs) {
            ItemData outputData = GTOreDictUnificator.getAssociation(output);
            Materials outputMaterial = outputData == null ? null : MU.materialOf(outputData.mMaterial.mMaterial);
            if (outputMaterial != null) gases.addAll(
                outputMaterial.getArcSmeltIntoWithGas()
                    .keySet());
        }
        return gases;
    }

    private static ItemStack[] getArcSmeltingOutputsWithGas(List<ItemStack> outputs, Materials gas) {
        ItemStack[] gasOutputs = new ItemStack[outputs.size()];
        boolean replacedOutput = false;
        for (int i = 0; i < outputs.size(); i++) {
            ItemStack output = outputs.get(i);
            ItemData outputData = GTOreDictUnificator.getAssociation(output);
            Materials outputMaterial = outputData == null ? null : MU.materialOf(outputData.mMaterial.mMaterial);
            if (outputMaterial != null && outputMaterial.getArcSmeltIntoWithGas()
                .containsKey(gas)) {
                Materials gasSmeltingMaterial = outputMaterial.getArcSmeltIntoWithGas()
                    .get(gas);
                long materialAmount = outputData.mMaterial.mAmount * output.stackSize;
                gasOutputs[i] = GTOreDictUnificator.getIngotOrDust(gasSmeltingMaterial, materialAmount);
                if (gasOutputs[i] == null) return null;
                replacedOutput = true;
            } else {
                gasOutputs[i] = output.copy();
            }
        }
        return replacedOutput ? gasOutputs : null;
    }

    /// [#registerReverseMacerating(ItemStack, Materials, long, MaterialStack, MaterialStack, MaterialStack,
    /// boolean, boolean)] for callers holding a [Material] directly -- the native ML body; the
    /// [Materials]-typed overload now delegates into this one. Builds the [ItemData] straight from `material`,
    /// which already carries every read the [ItemStack, ItemData, boolean, boolean] overload below needs,
    /// without a legacy round trip.
    public static void registerReverseMacerating(ItemStack stack, Material material, long materialAmount,
        MaterialStack byProduct01, MaterialStack byProduct02, MaterialStack byProduct03, boolean allowHammer,
        boolean isRecycling) {
        registerReverseMacerating(
            stack,
            new ItemData(
                material == null ? null : new MaterialStack(material, materialAmount),
                byProduct01,
                byProduct02,
                byProduct03),
            allowHammer,
            isRecycling);
    }

    public static void registerReverseMacerating(ItemStack stack, Materials material, long materialAmount,
        MaterialStack byProduct01, MaterialStack byProduct02, MaterialStack byProduct03, boolean allowHammer,
        boolean isRecycling) {
        registerReverseMacerating(
            stack,
            MU.material(material),
            materialAmount,
            byProduct01,
            byProduct02,
            byProduct03,
            allowHammer,
            isRecycling);
    }

    public static void registerReverseMacerating(ItemStack stack, ItemData data, boolean allowHammer,
        boolean isRecycling) {
        if (stack == null || data == null) return;
        data = new ItemData(data);

        if (!data.hasValidMaterialData()) return;

        for (MaterialStack tMaterial : data.getAllMaterialStacks())
            if (MU.materialOf(tMaterial.mMaterial) != null) tMaterial.mMaterial = MU.macerateInto(tMaterial.mMaterial);

        data = new ItemData(data);

        if (!data.hasValidMaterialData()) return;

        long tAmount = 0;
        for (MaterialStack tMaterial : data.getAllMaterialStacks()) {
            if (MU.materialOf(tMaterial.mMaterial) instanceof Materials material)
                tAmount += tMaterial.mAmount * material.getMass();
        }

        {
            ArrayList<ItemStack> outputs = new ArrayList<>();
            if (GTOreDictUnificator.getDust(data.mMaterial) != null) {
                outputs.add(GTOreDictUnificator.getDust(data.mMaterial));
            }
            for (int i = 0; i < 3; i++) {
                if (GTOreDictUnificator.getDust(data.getByProduct(i)) != null) {
                    outputs.add(GTOreDictUnificator.getDust(data.getByProduct(i)));
                }
            }
            if (!outputs.isEmpty()) {
                ItemStack[] outputsArray = outputs.toArray(new ItemStack[0]);
                GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                recipeBuilder.itemInputs(stack)
                    .itemOutputs(outputsArray)
                    .duration(
                        (data.mMaterial.mMaterial == MU.material(Materials.Marble) ? 1
                            : (int) Math.max(16, tAmount / M)) * TICKS)
                    .eut(4);
                if (isRecycling) recipeBuilder.recipeCategory(RecipeCategories.maceratorRecycling);
                recipeBuilder.addTo(maceratorRecipes);
            }
        }

        if (!allowHammer) {
            return;
        }

        for (MaterialStack tMaterial : data.getAllMaterialStacks()) {
            if (MU.materialOf(tMaterial.mMaterial) instanceof Materials material
                && MU.hasFlag(material, GTMaterialFlag.CRYSTAL)
                && !MU.hasFlag(material, GTMaterialFlag.METAL)
                && tMaterial.mMaterial != MU.material(Materials.Glass)
                && GTOreDictUnificator.getDust(data.mMaterial) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(GTOreDictUnificator.getDust(data.mMaterial))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .recipeCategory(RecipeCategories.forgeHammerRecycling)
                    .addTo(hammerRecipes);
                break;
            }
        }

    }

    /**
     * Place Materials which you want to replace in Non-GT-Recipes here (warning HUGHE impact on loading times!)
     */
    private static final Materials[] VANILLA_MATS = { Cobalt, Gold, Iron, Lead, FierySteel, Void, Bronze, Diamond, Ruby,
        Sapphire, Steel, IronWood, Steeleaf, Knightmetal, Thaumium, DarkSteel, };

    /**
     * You give this Function a Material and it will scan almost everything for adding recycling Recipes and replacing
     * Ingots, Gems etc.
     *
     * @param mats            Materials, for example an Ingot or a Gem.
     * @param plate           the Plate referenced to mat
     * @param recipeReplacing allows to replace the Recipe with a Plate variant
     */
    public static synchronized void registerUsagesForMaterials(String plate, boolean recipeReplacing,
        ItemStack... mats) {
        for (ItemStack mat : mats) {
            mat = GTUtility.copyOrNull(mat);

            if (mat == null) continue;

            ItemData itemData = GTOreDictUnificator.getItemData(mat);
            if (itemData == null || itemData.mPrefix != OrePrefixes.ingot) plate = null;
            if (plate != null && GTOreDictUnificator.getFirstOre(plate, 1) == null) plate = null;

            sMt1.func_150996_a(mat.getItem());
            sMt1.stackSize = 1;
            Items.feather.setDamage(sMt1, Items.feather.getDamage(mat));

            sMt2.func_150996_a(new ItemStack(Blocks.dirt).getItem());
            sMt2.stackSize = 1;
            Items.feather.setDamage(sMt2, 0);

            if (itemData != null && itemData.hasValidPrefixMaterialData()) {
                for (RecipeShape tRecipe : sShapes) {
                    for (ItemStack tCrafted : GTModHandler.getRecipeOutputsBuffered(tRecipe.shape)) {
                        GTOreDictUnificator.addItemData(
                            tCrafted,
                            new ItemData(itemData.mMaterial.mMaterial, itemData.mMaterial.mAmount * tRecipe.amount1));
                        //
                        // GTLog.out.println("###################################################################################");
                        // GTLog.out.println("registerUsagesForMaterials used plate: "+plate);
                        // GTLog.out.println("registerUsagesForMaterials used plate:
                        // "+mat.getUnlocalizedName());
                        // GTLog.out.println("registerUsagesForMaterials used plate:
                        // "+mat.getDisplayName());
                        //
                        // GTLog.out.println("###################################################################################");
                    }
                }
            }
            registerStickStuff(plate, itemData, recipeReplacing);
        }
    }

    private static List<IRecipe> getRecipeList(RecipeShape shape) {
        boolean force = !GregTechAPI.sPostloadStarted || GregTechAPI.sPostloadFinished;
        if (force || indexedRecipeListCache == null) {
            synchronized (GTRecipeRegistrator.class) {
                if (indexedRecipeListCache == null || force) {
                    indexedRecipeListCache = createIndexedRecipeListCache();
                }
            }
        }
        return indexedRecipeListCache.get(shape);
    }

    private static Map<RecipeShape, List<IRecipe>> createIndexedRecipeListCache() {
        Map<RecipeShape, List<IRecipe>> result = new IdentityHashMap<>();
        ArrayList<IRecipe> allRecipeList = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();
        // filter using the empty slots in the shape.
        // if the empty slots doesn't match, the recipe will definitely fail
        SetMultimap<List<Integer>, RecipeShape> filter = HashMultimap.create();
        for (RecipeShape shape : sShapes) {
            for (List<Integer> list : shape.getEmptySlotsAllVariants()) {
                filter.put(list, shape);
            }
        }
        List<Integer> buffer = new ArrayList<>(9);
        for (IRecipe tRecipe : allRecipeList) {
            if (tRecipe instanceof ShapelessRecipes || tRecipe instanceof ShapelessOreRecipe) {
                // we don't target shapeless recipes
                continue;
            }
            buffer.clear();
            ItemStack tStack = tRecipe.getRecipeOutput();
            if (GTUtility.isStackValid(tStack) && tStack.getMaxStackSize() == 1
                && tStack.getMaxDamage() > 0
                && !(tStack.getItem() instanceof ItemBlock)
                && !(tStack.getItem() instanceof IReactorComponent)
                && !GTModHandler.isElectricItem(tStack)
                && !GTUtility.isStackInList(tStack, GTModHandler.sNonReplaceableItems)) {
                if (tRecipe instanceof ShapedOreRecipe tShapedRecipe) {
                    if (checkRecipeShape(
                        buffer,
                        tShapedRecipe.getInput(),
                        getRecipeWidth(tShapedRecipe),
                        getRecipeHeight(tShapedRecipe))) {
                        for (RecipeShape s : filter.get(buffer)) {
                            result.computeIfAbsent(s, k -> new ArrayList<>())
                                .add(tRecipe);
                        }
                    }
                } else if (tRecipe instanceof ShapedRecipes tShapedRecipe) {
                    if (checkRecipeShape(
                        buffer,
                        tShapedRecipe.recipeItems,
                        getRecipeWidth(tShapedRecipe),
                        getRecipeHeight(tShapedRecipe))) {
                        for (RecipeShape s : filter.get(buffer)) {
                            result.computeIfAbsent(s, k -> new ArrayList<>())
                                .add(tRecipe);
                        }
                    }
                } else {
                    for (RecipeShape s : sShapes) {
                        // unknown recipe type. cannot determine empty slots. we choose to add to the recipe list for
                        // all shapes
                        result.computeIfAbsent(s, k -> new ArrayList<>())
                            .add(tRecipe);
                    }
                }
            }
        }
        return result;
    }

    private static boolean checkRecipeShape(List<Integer> emptySlotIndexesBuffer, Object[] input, int tRecipeWidth,
        int tRecipeHeight) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (x >= tRecipeWidth || y >= tRecipeHeight) {
                    emptySlotIndexesBuffer.add(x + y * 3);
                    continue;
                }
                Object tObject = input[x + y * tRecipeWidth];
                if (tObject == null) {
                    emptySlotIndexesBuffer.add(x + y * 3);
                    continue;
                }
                if (tObject instanceof ItemStack
                    && (((ItemStack) tObject).getItem() == null || ((ItemStack) tObject).getMaxStackSize() < 2
                        || ((ItemStack) tObject).getMaxDamage() > 0
                        || ((ItemStack) tObject).getItem() instanceof ItemBlock)) {
                    return false;
                }
                if (tObject instanceof List && ((List<?>) tObject).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static synchronized void registerStickStuff(String plate, ItemData itemData, boolean recipeReplacing) {
        IdentityHashMap<IRecipe, Boolean> tKnownMatches = recipeReplacing && plate != null ? new IdentityHashMap<>()
            : null;
        for (Materials tMaterial : sRodMaterialList) {
            ItemStack tMt2 = GTOreDictUnificator.get(OrePrefixes.stick, tMaterial, 1);
            if (tMt2 == null) {
                continue;
            }

            sMt2.func_150996_a(tMt2.getItem());
            sMt2.stackSize = 1;
            Items.feather.setDamage(sMt2, Items.feather.getDamage(tMt2));

            for (int i = 0; i < sShapes.length; i++) {
                RecipeShape tRecipe = sShapes[i];
                boolean tCanReplace = recipeReplacing && plate != null
                    && sShapesA[i] != null
                    && sShapesA[i].length > 1;
                if (tCanReplace) tKnownMatches.clear();
                boolean tRemovalAttempted = false;

                for (ItemStack tCrafted : GTModHandler.getRecipeOutputs(
                    getRecipeList(tRecipe),
                    true,
                    tRecipe.shape,
                    tCanReplace ? tKnownMatches : null)) {
                    if (itemData != null && itemData.hasValidPrefixMaterialData()) {
                        GTOreDictUnificator.addItemData(
                            tCrafted,
                            new ItemData(
                                itemData.mMaterial.mMaterial,
                                itemData.mMaterial.mAmount * tRecipe.amount1,
                                new MaterialStack(tMaterial, OrePrefixes.stick.getMaterialAmount() * tRecipe.amount2)));
                    }

                    if (tCanReplace && !tRemovalAttempted) {
                        tRemovalAttempted = true;
                        assert itemData != null;

                        ItemStack tStack = GTModHandler.removeRecipe(tRecipe.shape, tKnownMatches);
                        if (tStack == null) {
                            continue;
                        }

                        switch (sShapesA[i].length) {
                            case 2 -> GTModHandler.addCraftingRecipe(
                                tStack,
                                GTModHandler.RecipeBits.BUFFERED,
                                new Object[] { sShapesA[i][1], s_P.charAt(0), plate, s_R.charAt(0),
                                    OrePrefixes.stick.ingredient(tMaterial), s_I.charAt(0), itemData });
                            case 3 -> GTModHandler.addCraftingRecipe(
                                tStack,
                                GTModHandler.RecipeBits.BUFFERED,
                                new Object[] { sShapesA[i][1], sShapesA[i][2], s_P.charAt(0), plate, s_R.charAt(0),
                                    OrePrefixes.stick.ingredient(tMaterial), s_I.charAt(0), itemData });
                            default -> GTModHandler.addCraftingRecipe(
                                tStack,
                                GTModHandler.RecipeBits.BUFFERED,
                                new Object[] { sShapesA[i][1], sShapesA[i][2], sShapesA[i][3], s_P.charAt(0), plate,
                                    s_R.charAt(0), OrePrefixes.stick.ingredient(tMaterial), s_I.charAt(0), itemData });
                        }
                    }
                }
            }
        }
    }

    /**
     * Registers wiremill recipes for given material using integrated circuits.
     *
     * @param material     material to register
     * @param baseDuration base duration ticks for ingot -> 1x wire recipe
     * @param eut          EU/t for recipe If you provide a proper EU tier for recipe processing then eut will be
     *                     overriden with it.
     */
    public static void registerWiremillRecipes(Materials material, int baseDuration, int eut) {
        registerWiremillRecipes(
            material,
            baseDuration,
            calculateRecipeEU(material, eut),
            OrePrefixes.ingot,
            OrePrefixes.stick,
            2);
    }

    /**
     * Registers wiremill recipes for given material using integrated circuits.
     *
     * @param material     material to register
     * @param baseDuration base duration ticks for ingot -> 1x wire recipe
     * @param eut          EU/t for recipe
     * @param prefix1      prefix corresponds to ingot
     * @param prefix2      prefix corresponds to stick
     * @param multiplier   amount of wires created from 1 ingot
     */
    public static void registerWiremillRecipes(Materials material, int baseDuration, int eut, OrePrefixes prefix1,
        OrePrefixes prefix2, int multiplier) {
        if (GTOreDictUnificator.get(prefix1, material, 1L) != null
            && GTOreDictUnificator.get(OrePrefixes.wireGt01, material, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix1, material, 1L))
                .circuit(1)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt01, material, multiplier))
                .duration(baseDuration * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix1, material, 2L / multiplier))
                .circuit(2)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt02, material, 1L))
                .duration(((int) (baseDuration * 1.5f)) * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix1, material, 4L / multiplier))
                .circuit(4)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt04, material, 1L))
                .duration(baseDuration * 2 * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix1, material, 8L / multiplier))
                .circuit(8)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt08, material, 1L))
                .duration(((int) (baseDuration * 2.5f)) * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix1, material, 12L / multiplier))
                .circuit(12)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt12, material, 1L))
                .duration(baseDuration * 3 * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix1, material, 16L / multiplier))
                .circuit(16)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt16, material, 1L))
                .duration(((int) (baseDuration * 3.5f)) * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
        }

        if (GTOreDictUnificator.get(prefix2, material, 1L) != null
            && GTOreDictUnificator.get(OrePrefixes.wireGt01, material, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix2, material, 1L))
                .circuit(1)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt01, material, 2L / multiplier))
                .duration(((int) (baseDuration * 0.5f)) * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix2, material, 4L / multiplier))
                .circuit(2)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt02, material, 1L))
                .duration(baseDuration * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix2, material, 8L / multiplier))
                .circuit(4)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt04, material, 1L))
                .duration(((int) (baseDuration * 1.5f)) * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix2, material, 16L / multiplier))
                .circuit(8)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt08, material, 1L))
                .duration(baseDuration * 2 * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix2, material, 24L / multiplier))
                .circuit(12)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt12, material, 1L))
                .duration(((int) (baseDuration * 2.5f)) * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix2, material, 32L / multiplier))
                .circuit(16)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireGt16, material, 1L))
                .duration(baseDuration * 3 * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
        }
        if (GTOreDictUnificator.get(prefix1, material, 1L) != null
            && GTOreDictUnificator.get(OrePrefixes.wireFine, material, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix1, material, 1L))
                .circuit(3)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireFine, material, 4L * multiplier))
                .duration(baseDuration * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
        }
        if (GTOreDictUnificator.get(prefix2, material, 1L) != null
            && GTOreDictUnificator.get(OrePrefixes.wireFine, material, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(prefix2, material, 1L))
                .circuit(3)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.wireFine, material, 2L * multiplier))
                .duration(((int) (baseDuration * 0.5f)) * TICKS)
                .eut(eut)
                .addTo(wiremillRecipes);
        }
    }

    public static boolean hasVanillaRecipes(Materials materials) {
        return Arrays.stream(VANILLA_MATS)
            .anyMatch(mat -> mat == materials);
    }

    private static int getRecipeWidth(ShapedOreRecipe r) {
        return ((ShapedOreRecipeAccessor) r).gt5u$getWidth();
    }

    private static int getRecipeHeight(ShapedOreRecipe r) {
        return ((ShapedOreRecipeAccessor) r).gt5u$getHeight();
    }

    private static int getRecipeHeight(ShapedRecipes r) {
        return r.recipeHeight;
    }

    private static int getRecipeWidth(ShapedRecipes r) {
        return r.recipeWidth;
    }

    private static class RecipeShape {

        private final ItemStack[] shape;
        private int amount1;
        private int amount2;

        public RecipeShape(ItemStack... shape) {
            this.shape = shape;

            for (ItemStack stack : shape) {
                if (stack == sMt1) this.amount1++;
                if (stack == sMt2) this.amount2++;
            }
        }

        public List<List<Integer>> getEmptySlotsAllVariants() {
            // "shake" the grid in 8 direction and see if the recipe shape is still valid
            // also include the "no movement" case
            ImmutableList.Builder<List<Integer>> b = ImmutableList.builder();
            for (int i = -1; i < 2; i++) {
                if (i != 0 && !isColClear(i + 1)) continue;
                for (int j = -1; j < 2; j++) {
                    if (j != 0 && !isRowClear(j + 1)) continue;
                    b.add(getEmptySlots(i, j));
                }
            }
            return b.build();
        }

        private boolean isRowClear(int row) {
            for (int i = 0; i < 3; i++) {
                if (shape[i + row * 3] != null) return false;
            }
            return true;
        }

        private boolean isColClear(int col) {
            for (int i = 0; i < 3; i++) {
                if (shape[col + i * 3] != null) return false;
            }
            return true;
        }

        private List<Integer> getEmptySlots(int offsetX, int offsetY) {
            ImmutableList.Builder<Integer> b = ImmutableList.builder();
            for (int i = 0; i < shape.length; i++) {
                int mappedIndex = i - offsetX - offsetY * 3;
                // empty slot if it either
                // 1) map to a slot outside the original shape
                // 2) map to an empty slot in original shape
                if (mappedIndex < 0 || mappedIndex > 8 || shape[mappedIndex] == null) b.add(i);
            }
            return b.build();
        }
    }
}
