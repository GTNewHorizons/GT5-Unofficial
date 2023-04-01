package gregtech.api.util;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.Materials.Void;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import java.lang.reflect.Field;
import java.util.*;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;

import cpw.mods.fml.relauncher.ReflectionHelper;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import ic2.api.reactor.IReactorComponent;

/**
 * Class for Automatic Recipe registering.
 */
public class GT_RecipeRegistrator {

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
    public static final Field SHAPED_ORE_RECIPE_WIDTH = ReflectionHelper.findField(ShapedOreRecipe.class, "width");
    public static final Field SHAPED_ORE_RECIPE_HEIGHT = ReflectionHelper.findField(ShapedOreRecipe.class, "height");
    private static volatile Map<RecipeShape, List<IRecipe>> indexedRecipeListCache;
    private static final String[][] sShapesA = new String[][] { null, null, null,
            { "Helmet", s_P + s_P + s_P, s_P + s_H + s_P },
            { "ChestPlate", s_P + s_H + s_P, s_P + s_P + s_P, s_P + s_P + s_P },
            { "Pants", s_P + s_P + s_P, s_P + s_H + s_P, s_P + " " + s_P },
            { "Boots", s_P + " " + s_P, s_P + s_H + s_P },
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
    public static volatile int VERSION = 509;

    static {
        // flush the cache on post load finish
        GregTech_API.sAfterGTPostload.add(() -> indexedRecipeListCache = null);
    }

    public static void registerMaterialRecycling(ItemStack aStack, Materials aMaterial, long aMaterialAmount,
            MaterialStack aByproduct) {
        if (GT_Utility.isStackInvalid(aStack)) return;
        if (aByproduct != null) {
            aByproduct = aByproduct.clone();
            aByproduct.mAmount /= aStack.stackSize;
        }
        GT_OreDictUnificator.addItemData(
                GT_Utility.copyAmount(1, aStack),
                new ItemData(aMaterial, aMaterialAmount / aStack.stackSize, aByproduct));
    }

    public static void registerMaterialRecycling(ItemStack aStack, ItemData aData) {
        if (GT_Utility.isStackInvalid(aStack) || GT_Utility.areStacksEqual(new ItemStack(Items.blaze_rod), aStack)
                || aData == null
                || !aData.hasValidMaterialData()
                || !aData.mMaterial.mMaterial.mAutoGenerateRecycleRecipes
                || aData.mMaterial.mAmount <= 0
                || GT_Utility.getFluidForFilledItem(aStack, false) != null
                || aData.mMaterial.mMaterial.mSubTags.contains(SubTag.NO_RECIPES))
            return;
        registerReverseMacerating(GT_Utility.copyAmount(1, aStack), aData, aData.mPrefix == null);
        registerReverseSmelting(
                GT_Utility.copyAmount(1, aStack),
                aData.mMaterial.mMaterial,
                aData.mMaterial.mAmount,
                true);
        registerReverseFluidSmelting(
                GT_Utility.copyAmount(1, aStack),
                aData.mMaterial.mMaterial,
                aData.mMaterial.mAmount,
                aData.getByProduct(0));
        registerReverseArcSmelting(GT_Utility.copyAmount(1, aStack), aData);
    }

    /**
     * @param aStack          the stack to be recycled.
     * @param aMaterial       the Material.
     * @param aMaterialAmount the amount of it in Material Units.
     */
    public static void registerReverseFluidSmelting(ItemStack aStack, Materials aMaterial, long aMaterialAmount,
            MaterialStack aByproduct) {
        if (aStack == null || aMaterial == null
                || aMaterial.mSmeltInto.mStandardMoltenFluid == null
                || !aMaterial.contains(SubTag.SMELTING_TO_FLUID)
                || (L * aMaterialAmount) / (M * aStack.stackSize) <= 0)
            return;
        ItemData tData = GT_OreDictUnificator.getItemData(aStack);
        boolean tHide = aStack.getUnlocalizedName()
                              .startsWith("gt.blockmachines")
                && (GT_Mod.gregtechproxy.mHideRecyclingRecipes);
        if (GT_Mod.gregtechproxy.mHideRecyclingRecipes && tData != null
                && tData.hasValidPrefixData()
                && !(tData.mPrefix == OrePrefixes.dust || tData.mPrefix == OrePrefixes.ingot
                        || tData.mPrefix == OrePrefixes.block | tData.mPrefix == OrePrefixes.plate)) {
            tHide = true;
        }
        // boolean tHide = (aMaterial != Materials.Iron && aMaterial!=
        // Materials.Redstone)&&(GT_Mod.gregtechproxy.mHideRecyclingRecipes);
        // if(tHide && tData!=null&&tData.hasValidPrefixData()&&tData.mPrefix==OrePrefixes.ingot){
        // tHide=false;
        // }
        RA.addFluidSmelterRecipe(
                GT_Utility.copyAmount(1, aStack),
                aByproduct == null
                        || (tData != null && tData.hasValidPrefixData() && tData.mPrefix == OrePrefixes.toolHeadDrill)
                                ? null
                                : aByproduct.mMaterial.contains(SubTag.NO_SMELTING)
                                        || !aByproduct.mMaterial.contains(SubTag.METAL)
                                                ? aByproduct.mMaterial.contains(SubTag.FLAMMABLE)
                                                        ? GT_OreDictUnificator.getDust(
                                                                Materials.Ash,
                                                                aByproduct.mAmount / 2)
                                                        : aByproduct.mMaterial.contains(SubTag.UNBURNABLE)
                                                                ? GT_OreDictUnificator.getDustOrIngot(
                                                                        aByproduct.mMaterial.mSmeltInto,
                                                                        aByproduct.mAmount)
                                                                : null
                                                : GT_OreDictUnificator.getIngotOrDust(
                                                        aByproduct.mMaterial.mSmeltInto,
                                                        aByproduct.mAmount),
                aMaterial.mSmeltInto.getMolten((L * aMaterialAmount) / (M * aStack.stackSize)),
                10000,
                (int) Math.max(1, (24 * aMaterialAmount) / M),
                Math.max(8, (int) Math.sqrt(2 * aMaterial.mSmeltInto.mStandardMoltenFluid.getTemperature())),
                tHide);
    }

    /**
     * @param aStack             the stack to be recycled.
     * @param aMaterial          the Material.
     * @param aMaterialAmount    the amount of it in Material Units.
     * @param aAllowAlloySmelter if it is allowed to be recycled inside the Alloy Smelter.
     */
    public static void registerReverseSmelting(ItemStack aStack, Materials aMaterial, long aMaterialAmount,
            boolean aAllowAlloySmelter) {
        if (aStack == null || aMaterial == null
                || aMaterialAmount <= 0
                || aMaterial.contains(SubTag.NO_SMELTING)
                || (aMaterialAmount > M && aMaterial.contains(SubTag.METAL))
                || (aMaterial.getProcessingMaterialTierEU() > TierEU.IV))
            return;
        if (aMaterial == Materials.Naquadah || aMaterial == Materials.NaquadahEnriched) return;

        aMaterialAmount /= aStack.stackSize;

        boolean tHide = (aMaterial != Materials.Iron) && (GT_Mod.gregtechproxy.mHideRecyclingRecipes);
        if (aAllowAlloySmelter) GT_ModHandler.addSmeltingAndAlloySmeltingRecipe(
                GT_Utility.copyAmount(1, aStack),
                GT_OreDictUnificator.getIngot(aMaterial.mSmeltInto, aMaterialAmount),
                tHide);
        else GT_ModHandler.addSmeltingRecipe(
                GT_Utility.copyAmount(1, aStack),
                GT_OreDictUnificator.getIngot(aMaterial.mSmeltInto, aMaterialAmount));
    }

    public static void registerReverseArcSmelting(ItemStack aStack, Materials aMaterial, long aMaterialAmount,
            MaterialStack aByProduct01, MaterialStack aByProduct02, MaterialStack aByProduct03) {
        registerReverseArcSmelting(
                aStack,
                new ItemData(
                        aMaterial == null ? null : new MaterialStack(aMaterial, aMaterialAmount),
                        aByProduct01,
                        aByProduct02,
                        aByProduct03));
    }

    public static void registerReverseArcSmelting(ItemStack aStack, ItemData aData) {
        if (aStack == null || aData == null) return;
        aData = new ItemData(aData);

        if (!aData.hasValidMaterialData()) return;
        boolean tIron = false;

        for (MaterialStack tMaterial : aData.getAllMaterialStacks()) {
            if (tMaterial.mMaterial == Materials.Iron || tMaterial.mMaterial == Materials.Copper
                    || tMaterial.mMaterial == Materials.WroughtIron
                    || tMaterial.mMaterial == Materials.AnnealedCopper)
                tIron = true;

            if (tMaterial.mMaterial.contains(SubTag.UNBURNABLE)) {
                tMaterial.mMaterial = tMaterial.mMaterial.mSmeltInto.mArcSmeltInto;
                continue;
            }
            if (tMaterial.mMaterial.contains(SubTag.EXPLOSIVE)) {
                tMaterial.mMaterial = Materials.Ash;
                tMaterial.mAmount /= 16;
                continue;
            }
            if (tMaterial.mMaterial.contains(SubTag.FLAMMABLE)) {
                tMaterial.mMaterial = Materials.Ash;
                tMaterial.mAmount /= 8;
                continue;
            }
            if (tMaterial.mMaterial.contains(SubTag.NO_SMELTING)) {
                tMaterial.mAmount = 0;
                continue;
            }
            if (tMaterial.mMaterial.contains(SubTag.METAL)) {
                if (GT_Mod.gregtechproxy.mArcSmeltIntoAnnealed) {
                    tMaterial.mMaterial = tMaterial.mMaterial.mSmeltInto.mArcSmeltInto;
                } else {
                    tMaterial.mMaterial = tMaterial.mMaterial.mSmeltInto.mSmeltInto;
                }
                continue;
            }
            tMaterial.mAmount = 0;
        }

        aData = new ItemData(aData);
        if (aData.mByProducts.length > 3) for (MaterialStack tMaterial : aData.getAllMaterialStacks()) {
            if (tMaterial.mMaterial == Materials.Ash) tMaterial.mAmount = 0;
        }

        aData = new ItemData(aData);

        if (!aData.hasValidMaterialData()) return;

        long tAmount = 0;
        for (MaterialStack tMaterial : aData.getAllMaterialStacks())
            tAmount += tMaterial.mAmount * tMaterial.mMaterial.getMass();

        boolean tHide = !tIron && GT_Mod.gregtechproxy.mHideRecyclingRecipes;
        if (aData.mPrefix == OrePrefixes.toolHeadDrill) {
            RA.addArcFurnaceRecipe(
                    aStack,
                    new ItemStack[] { GT_OreDictUnificator.getIngotOrDust(aData.mMaterial) },
                    null,
                    90,
                    (int) Math.max(16, tAmount / M),
                    tHide);
        } else {
            RA.addArcFurnaceRecipe(
                    aStack,
                    new ItemStack[] { GT_OreDictUnificator.getIngotOrDust(aData.mMaterial),
                            GT_OreDictUnificator.getIngotOrDust(aData.getByProduct(0)),
                            GT_OreDictUnificator.getIngotOrDust(aData.getByProduct(1)),
                            GT_OreDictUnificator.getIngotOrDust(aData.getByProduct(2)) },
                    null,
                    (int) Math.max(16, tAmount / M),
                    90,
                    tHide);
        }
    }

    public static void registerReverseMacerating(ItemStack aStack, Materials aMaterial, long aMaterialAmount,
            MaterialStack aByProduct01, MaterialStack aByProduct02, MaterialStack aByProduct03, boolean aAllowHammer) {
        registerReverseMacerating(
                aStack,
                new ItemData(
                        aMaterial == null ? null : new MaterialStack(aMaterial, aMaterialAmount),
                        aByProduct01,
                        aByProduct02,
                        aByProduct03),
                aAllowHammer);
    }

    public static void registerReverseMacerating(ItemStack aStack, ItemData aData, boolean aAllowHammer) {
        if (aStack == null || aData == null) return;
        aData = new ItemData(aData);

        if (!aData.hasValidMaterialData()) return;

        for (MaterialStack tMaterial : aData.getAllMaterialStacks())
            tMaterial.mMaterial = tMaterial.mMaterial.mMacerateInto;

        aData = new ItemData(aData);

        if (!aData.hasValidMaterialData()) return;

        long tAmount = 0;
        for (MaterialStack tMaterial : aData.getAllMaterialStacks())
            tAmount += tMaterial.mAmount * tMaterial.mMaterial.getMass();
        boolean tHide = (aData.mMaterial.mMaterial != Materials.Iron) && (GT_Mod.gregtechproxy.mHideRecyclingRecipes);
        if (aData.mPrefix == OrePrefixes.toolHeadDrill) {
            RA.addPulveriserRecipe(
                    aStack,
                    new ItemStack[] { GT_OreDictUnificator.getDust(aData.mMaterial) },
                    null,
                    aData.mMaterial.mMaterial == Materials.Marble ? 1 : (int) Math.max(16, tAmount / M),
                    4,
                    tHide);
        } else {
            RA.addPulveriserRecipe(
                    aStack,
                    new ItemStack[] { GT_OreDictUnificator.getDust(aData.mMaterial),
                            GT_OreDictUnificator.getDust(aData.getByProduct(0)),
                            GT_OreDictUnificator.getDust(aData.getByProduct(1)),
                            GT_OreDictUnificator.getDust(aData.getByProduct(2)) },
                    null,
                    aData.mMaterial.mMaterial == Materials.Marble ? 1 : (int) Math.max(16, tAmount / M),
                    4,
                    tHide);
        }

        if (aAllowHammer) for (MaterialStack tMaterial : aData.getAllMaterialStacks())
            if (tMaterial.mMaterial.contains(SubTag.CRYSTAL) && !tMaterial.mMaterial.contains(SubTag.METAL)
                    && tMaterial.mMaterial != Materials.Glass) {
                        if (RA.addForgeHammerRecipe(
                                GT_Utility.copyAmount(1, aStack),
                                GT_OreDictUnificator.getDust(aData.mMaterial),
                                200,
                                30))
                            break;
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
     * @param aMats            Materials, for example an Ingot or a Gem.
     * @param aPlate           the Plate referenced to aMat
     * @param aRecipeReplacing allows to replace the Recipe with a Plate variant
     */
    public static synchronized void registerUsagesForMaterials(String aPlate, boolean aRecipeReplacing,
            ItemStack... aMats) {
        for (ItemStack aMat : aMats) {
            aMat = GT_Utility.copyOrNull(aMat);

            if (aMat == null) continue;

            ItemData aItemData = GT_OreDictUnificator.getItemData(aMat);
            if (aItemData == null || aItemData.mPrefix != OrePrefixes.ingot) aPlate = null;
            if (aPlate != null && GT_OreDictUnificator.getFirstOre(aPlate, 1) == null) aPlate = null;

            sMt1.func_150996_a(aMat.getItem());
            sMt1.stackSize = 1;
            Items.feather.setDamage(sMt1, Items.feather.getDamage(aMat));

            sMt2.func_150996_a(new ItemStack(Blocks.dirt).getItem());
            sMt2.stackSize = 1;
            Items.feather.setDamage(sMt2, 0);

            if (aItemData != null && aItemData.hasValidPrefixMaterialData()) {
                for (RecipeShape tRecipe : sShapes) {
                    for (ItemStack tCrafted : GT_ModHandler.getRecipeOutputsBuffered(tRecipe.shape)) {
                        GT_OreDictUnificator.addItemData(
                                tCrafted,
                                new ItemData(
                                        aItemData.mMaterial.mMaterial,
                                        aItemData.mMaterial.mAmount * tRecipe.amount1));
                        //
                        // GT_Log.out.println("###################################################################################");
                        // GT_Log.out.println("registerUsagesForMaterials used aPlate: "+aPlate);
                        // GT_Log.out.println("registerUsagesForMaterials used aPlate:
                        // "+aMat.getUnlocalizedName());
                        // GT_Log.out.println("registerUsagesForMaterials used aPlate:
                        // "+aMat.getDisplayName());
                        //
                        // GT_Log.out.println("###################################################################################");
                    }
                }
            }
            registerStickStuff(aPlate, aItemData, aRecipeReplacing);
        }
    }

    private static List<IRecipe> getRecipeList(RecipeShape shape) {
        boolean force = !GregTech_API.sPostloadStarted || GregTech_API.sPostloadFinished;
        if (force || indexedRecipeListCache == null) {
            synchronized (GT_RecipeRegistrator.class) {
                if (indexedRecipeListCache == null || force) {
                    indexedRecipeListCache = createIndexedRecipeListCache();
                }
            }
        }
        return indexedRecipeListCache.get(shape);
    }

    private static Map<RecipeShape, List<IRecipe>> createIndexedRecipeListCache() {
        Map<RecipeShape, List<IRecipe>> result = new IdentityHashMap<>();
        @SuppressWarnings("unchecked")
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
            if (GT_Utility.isStackValid(tStack) && tStack.getMaxStackSize() == 1
                    && tStack.getMaxDamage() > 0
                    && !(tStack.getItem() instanceof ItemBlock)
                    && !(tStack.getItem() instanceof IReactorComponent)
                    && !GT_ModHandler.isElectricItem(tStack)
                    && !GT_Utility.isStackInList(tStack, GT_ModHandler.sNonReplaceableItems)) {
                if (tRecipe instanceof ShapedOreRecipe) {
                    ShapedOreRecipe tShapedRecipe = (ShapedOreRecipe) tRecipe;
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
                } else if (tRecipe instanceof ShapedRecipes) {
                    ShapedRecipes tShapedRecipe = (ShapedRecipes) tRecipe;
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

    private static synchronized void registerStickStuff(String aPlate, ItemData aItemData, boolean aRecipeReplacing) {
        ItemStack tStack;
        for (Materials tMaterial : sRodMaterialList) {
            ItemStack tMt2 = GT_OreDictUnificator.get(OrePrefixes.stick, tMaterial, 1);
            if (tMt2 != null) {
                sMt2.func_150996_a(tMt2.getItem());
                sMt2.stackSize = 1;
                Items.feather.setDamage(sMt2, Items.feather.getDamage(tMt2));

                for (int i = 0; i < sShapes.length; i++) {
                    RecipeShape tRecipe = sShapes[i];

                    for (ItemStack tCrafted : GT_ModHandler.getRecipeOutputs(
                            getRecipeList(tRecipe),
                            true,
                            tRecipe.shape)) {
                        if (aItemData != null && aItemData.hasValidPrefixMaterialData())
                            GT_OreDictUnificator.addItemData(
                                    tCrafted,
                                    new ItemData(
                                            aItemData.mMaterial.mMaterial,
                                            aItemData.mMaterial.mAmount * tRecipe.amount1,
                                            new MaterialStack(
                                                    tMaterial,
                                                    OrePrefixes.stick.mMaterialAmount * tRecipe.amount2)));

                        if (aRecipeReplacing && aPlate != null && sShapesA[i] != null && sShapesA[i].length > 1) {
                            assert aItemData != null;
                            if (GregTech_API.sRecipeFile.get(
                                    ConfigCategories.Recipes.recipereplacements,
                                    aItemData.mMaterial.mMaterial + "." + sShapesA[i][0],
                                    true)) {
                                if (null != (tStack = GT_ModHandler.removeRecipe(tRecipe.shape))) {
                                    switch (sShapesA[i].length) {
                                        case 2:
                                            GT_ModHandler.addCraftingRecipe(
                                                    tStack,
                                                    GT_ModHandler.RecipeBits.BUFFERED,
                                                    new Object[] { sShapesA[i][1], s_P.charAt(0), aPlate, s_R.charAt(0),
                                                            OrePrefixes.stick.get(tMaterial), s_I.charAt(0),
                                                            aItemData });
                                            break;
                                        case 3:
                                            GT_ModHandler.addCraftingRecipe(
                                                    tStack,
                                                    GT_ModHandler.RecipeBits.BUFFERED,
                                                    new Object[] { sShapesA[i][1], sShapesA[i][2], s_P.charAt(0),
                                                            aPlate, s_R.charAt(0), OrePrefixes.stick.get(tMaterial),
                                                            s_I.charAt(0), aItemData });
                                            break;
                                        default:
                                            GT_ModHandler.addCraftingRecipe(
                                                    tStack,
                                                    GT_ModHandler.RecipeBits.BUFFERED,
                                                    new Object[] { sShapesA[i][1], sShapesA[i][2], sShapesA[i][3],
                                                            s_P.charAt(0), aPlate, s_R.charAt(0),
                                                            OrePrefixes.stick.get(tMaterial), s_I.charAt(0),
                                                            aItemData });
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Registers wiremill recipes for given material using integrated circuits.
     *
     * @param aMaterial    material to register
     * @param baseDuration base duration ticks for ingot -> 1x wire recipe
     * @param aEUt         EU/t for recipe If you provide a proper EU tier for recipe processing then aEUt will be
     *                     overriden with it.
     */
    public static void registerWiremillRecipes(Materials aMaterial, int baseDuration, int aEUt) {
        registerWiremillRecipes(
                aMaterial,
                baseDuration,
                calculateRecipeEU(aMaterial, aEUt),
                OrePrefixes.ingot,
                OrePrefixes.stick,
                2);
    }

    /**
     * Registers wiremill recipes for given material using integrated circuits.
     *
     * @param aMaterial    material to register
     * @param baseDuration base duration ticks for ingot -> 1x wire recipe
     * @param aEUt         EU/t for recipe
     * @param prefix1      prefix corresponds to ingot
     * @param prefix2      prefix corresponds to stick
     * @param multiplier   amount of wires created from 1 ingot
     */
    public static void registerWiremillRecipes(Materials aMaterial, int baseDuration, int aEUt, OrePrefixes prefix1,
            OrePrefixes prefix2, int multiplier) {
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix1, aMaterial, 1L),
                GT_Utility.getIntegratedCircuit(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, multiplier),
                baseDuration,
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix1, aMaterial, 2L / multiplier),
                GT_Utility.getIntegratedCircuit(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, aMaterial, 1L),
                (int) (baseDuration * 1.5f),
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix1, aMaterial, 4L / multiplier),
                GT_Utility.getIntegratedCircuit(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L),
                baseDuration * 2,
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix1, aMaterial, 8L / multiplier),
                GT_Utility.getIntegratedCircuit(8),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, aMaterial, 1L),
                (int) (baseDuration * 2.5f),
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix1, aMaterial, 12L / multiplier),
                GT_Utility.getIntegratedCircuit(12),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, aMaterial, 1L),
                baseDuration * 3,
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix1, aMaterial, 16L / multiplier),
                GT_Utility.getIntegratedCircuit(16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, aMaterial, 1L),
                (int) (baseDuration * 3.5f),
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix2, aMaterial, 2L / multiplier),
                GT_Utility.getIntegratedCircuit(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 1L),
                baseDuration / 2,
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix2, aMaterial, 4L / multiplier),
                GT_Utility.getIntegratedCircuit(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, aMaterial, 1L),
                baseDuration,
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix2, aMaterial, 8L / multiplier),
                GT_Utility.getIntegratedCircuit(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L),
                (int) (baseDuration * 1.5f),
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix2, aMaterial, 16L / multiplier),
                GT_Utility.getIntegratedCircuit(8),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, aMaterial, 1L),
                baseDuration * 2,
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix2, aMaterial, 24L / multiplier),
                GT_Utility.getIntegratedCircuit(12),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, aMaterial, 1L),
                (int) (baseDuration * 2.5f),
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix2, aMaterial, 32L / multiplier),
                GT_Utility.getIntegratedCircuit(16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, aMaterial, 1L),
                baseDuration * 3,
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix1, aMaterial, 1L),
                GT_Utility.getIntegratedCircuit(3),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 4L * multiplier),
                baseDuration,
                aEUt);
        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(prefix2, aMaterial, 1L),
                GT_Utility.getIntegratedCircuit(3),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 2L * multiplier),
                baseDuration / 2,
                aEUt);
    }

    public static boolean hasVanillaRecipes(Materials materials) {
        return Arrays.stream(VANILLA_MATS)
                     .anyMatch(mat -> mat == materials);
    }

    private static int getRecipeWidth(ShapedOreRecipe r) {
        try {
            return (int) SHAPED_ORE_RECIPE_WIDTH.get(r);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getRecipeHeight(ShapedOreRecipe r) {
        try {
            return (int) SHAPED_ORE_RECIPE_HEIGHT.get(r);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
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
