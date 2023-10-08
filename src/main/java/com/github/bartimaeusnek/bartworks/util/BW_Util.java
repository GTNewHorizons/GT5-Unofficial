/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.util;

import static gregtech.api.enums.GT_Values.D1;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.M;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.GT_Values.W;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.bartimaeusnek.bartworks.API.BioVatLogicAdder;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureElement;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Shaped_Recipe;
import gregtech.api.util.GT_Utility;

public class BW_Util {

    public static final int STANDART = 0;
    public static final int LOWGRAVITY = -100;
    public static final int CLEANROOM = -200;

    public static String translateGTItemStack(ItemStack itemStack) {
        if (!GT_Utility.isStackValid(itemStack)) return "Not a Valid ItemStack:" + itemStack;
        String ret = GT_LanguageManager.getTranslation(GT_LanguageManager.getTranslateableItemStackName(itemStack));
        if (!ret.contains("%material")) return ret;
        String matname = "";
        if (BW_Util.checkStackAndPrefix(itemStack))
            matname = GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial.mDefaultLocalName;
        return ret.replace("%material", matname);
    }

    public static void set2DCoordTo1DArray(int indexX, int indexY, int sizeY, Object value, Object[] array) {
        int index = indexX * sizeY + indexY;
        array[index] = value;
    }

    public static Object get2DCoordFrom1DArray(int indexX, int indexY, int sizeY, Object[] array) {
        int index = indexX * sizeY + indexY;
        return array[index];
    }

    public static GT_Recipe copyAndSetTierToNewRecipe(GT_Recipe recipe, byte tier) {
        byte oldTier = GT_Utility.getTier(recipe.mEUt);
        int newTime = recipe.mDuration;
        int newVoltage = recipe.mEUt;
        if (tier < oldTier) {
            newTime <<= oldTier - tier;
            newVoltage >>= 2 * (oldTier - tier);
        } else {
            newTime >>= tier - oldTier;
            newVoltage <<= 2 * (tier - oldTier);
        }
        recipe.mEUt = newVoltage;
        recipe.mDuration = newTime;
        return recipe;
    }

    public static String subscriptNumbers(String b) {
        char[] chars = b.toCharArray();
        char[] nu = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            nu[i] = switch (chars[i]) {
                case '0' -> '\u2080';
                case '1' -> '\u2081';
                case '2' -> '\u2082';
                case '3' -> '\u2083';
                case '4' -> '\u2084';
                case '5' -> '\u2085';
                case '6' -> '\u2086';
                case '7' -> '\u2087';
                case '8' -> '\u2088';
                case '9' -> '\u2089';
                default -> chars[i];
            };
        }
        return new String(nu);
    }

    public static String subscriptNumber(Number b) {
        char[] chars = Long.toString(b.longValue()).toCharArray();
        char[] nu = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            nu[i] = switch (chars[i]) {
                case '0' -> '\u2080';
                case '1' -> '\u2081';
                case '2' -> '\u2082';
                case '3' -> '\u2083';
                case '4' -> '\u2084';
                case '5' -> '\u2085';
                case '6' -> '\u2086';
                case '7' -> '\u2087';
                case '8' -> '\u2088';
                case '9' -> '\u2089';
                default -> chars[i];
            };
        }
        return new String(nu);
    }

    public static String superscriptNumbers(String b) {
        char[] chars = b.toCharArray();
        char[] nu = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            nu[i] = switch (chars[i]) {
                case '0' -> '\u2070';
                case '1' -> '\u2071';
                case '2' -> '\u00B2';
                case '3' -> '\u00B3';
                case '4' -> '\u2074';
                case '5' -> '\u2075';
                case '6' -> '\u2076';
                case '7' -> '\u2077';
                case '8' -> '\u2078';
                case '9' -> '\u2079';
                default -> chars[i];
            };
        }
        return new String(nu);
    }

    public static String superscriptNumber(Number b) {
        char[] chars = Long.toString(b.longValue()).toCharArray();
        char[] nu = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            nu[i] = switch (chars[i]) {
                case '0' -> '\u2070';
                case '1' -> '\u2071';
                case '2' -> '\u00B2';
                case '3' -> '\u00B3';
                case '4' -> '\u2074';
                case '5' -> '\u2075';
                case '6' -> '\u2076';
                case '7' -> '\u2077';
                case '8' -> '\u2078';
                case '9' -> '\u2079';
                default -> chars[i];
            };
        }
        return new String(nu);
    }

    public static byte specialToByte(int aSpecialValue) {
        byte special = 0;
        switch (aSpecialValue) {
            case LOWGRAVITY:
                special = 1;
                break;
            case CLEANROOM:
                special = 2;
                break;
            case LOWGRAVITY | CLEANROOM:
                special = 3;
                break;
            default:
                break;
        }
        return special;
    }

    public static int calculateSv(Materials materials) {
        for (BioVatLogicAdder.MaterialSvPair pair : BioVatLogicAdder.RadioHatch.getMaSv()) {
            if (pair.getMaterials().equals(materials)) return pair.getSievert();
        }
        return (int) (materials.getProtons() == 43L
                ? materials.equals(Materials.NaquadahEnriched) ? 140
                        : materials.equals(Materials.Naquadria) ? 150 : materials.equals(Materials.Naquadah) ? 130 : 43
                : materials.getProtons());
    }

    public static ItemStack setStackSize(ItemStack stack, int size) {
        if (stack != null) stack.stackSize = size;
        return stack;
    }

    public static boolean checkStackAndPrefix(ItemStack itemStack) {
        return itemStack != null && GT_OreDictUnificator.getAssociation(itemStack) != null
                && GT_OreDictUnificator.getAssociation(itemStack).mPrefix != null
                && GT_OreDictUnificator.getAssociation(itemStack).mMaterial != null
                && GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial != null;
    }

    public static int abstractHashGTRecipe(GT_Recipe recipe) {
        int hash = 31;
        hash += recipe.mDuration / 20 * 31;
        hash += GT_Utility.getTier(recipe.mEUt) * 31;
        hash += BW_Util.specialToByte(recipe.mSpecialValue) * 31;
        hash += recipe.mInputs.length * 31;
        for (ItemStack mInput : recipe.mInputs) {
            if (mInput != null) {
                hash += mInput.stackSize * 31;
                hash += Item.getIdFromItem(mInput.getItem()) * 31;
            }
        }
        hash += recipe.mOutputs.length * 31;
        for (ItemStack mOutput : recipe.mOutputs) {
            if (mOutput != null) {
                hash += mOutput.stackSize * 31;
                hash += Item.getIdFromItem(mOutput.getItem()) * 31;
            }
        }
        hash += recipe.mFluidInputs.length * 31;
        for (FluidStack mInput : recipe.mFluidInputs) {
            if (mInput != null) {
                hash += mInput.amount * 31;
                hash += mInput.getFluidID() * 31;
            }
        }
        hash += recipe.mFluidOutputs.length * 31;
        for (FluidStack mOutput : recipe.mFluidOutputs) {
            if (mOutput != null) {
                hash += mOutput.amount * 31;
                hash += mOutput.getFluidID() * 31;
            }
        }
        return hash;
    }

    @SuppressWarnings({ "unchecked" })
    public static <T> T[] copyAndRemoveNulls(T[] input, Class<T> clazz) {
        List<T> ret = Arrays.stream(input).filter(Objects::nonNull).collect(Collectors.toList());

        if (ret.size() <= 0) return (T[]) Array.newInstance(clazz, 0);

        T[] retArr = (T[]) Array.newInstance(clazz, ret.size());

        for (int i = 0; i < ret.size(); i++) retArr[i] = ret.get(i);

        return retArr;
    }

    @Deprecated
    public static int getMachineVoltageFromTier(int tier) {
        return (int) (30 * Math.pow(4, tier - 1));
    }

    public static long getTierVoltage(int tier) {
        return getTierVoltage((byte) tier);
    }

    public static long getTierVoltage(byte tier) {
        return 8L << 2 * tier;
    }

    public static byte getTier(long voltage) {
        if (voltage <= Integer.MAX_VALUE - 7) return GT_Utility.getTier(voltage);
        byte t = 0;
        while (voltage > 8L) {
            voltage >>= 2;
            t++;
        }
        return t;
    }

    public static String getTierName(byte tier) {
        if (VN.length - 1 <= tier) return "MAX+";
        return VN[tier];
    }

    public static String getTierNameFromVoltage(long voltage) {
        return getTierName(getTier(voltage));
    }

    public static boolean areStacksEqualOrNull(ItemStack aStack1, ItemStack aStack2) {
        return aStack1 == null && aStack2 == null || GT_Utility.areStacksEqual(aStack1, aStack2);
    }

    public static boolean areStacksEqualOrEachNull(ItemStack aStack1, ItemStack aStack2) {
        return aStack1 == null || aStack2 == null || GT_Utility.areStacksEqual(aStack1, aStack2);
    }

    public static byte getByteFromRarity(EnumRarity rarity) {
        if (EnumRarity.uncommon.equals(rarity)) return 1;
        if (EnumRarity.epic.equals(rarity)) return 2;
        else if (EnumRarity.rare.equals(rarity)) return 3;
        return 0;
    }

    /**
     * @deprecated Use stuff in {@link com.github.bartimaeusnek.bartworks.API.BorosilicateGlass} instead
     */
    @Deprecated
    public static byte getTierFromGlasMeta(int meta) {
        return switch (meta) {
            case 1 -> 4;
            case 2, 12 -> 5;
            case 3 -> 6;
            case 4 -> 7;
            case 5 -> 8;
            case 13 -> 9;
            case 14 -> 10;
            default -> 3;
        };
    }

    public static EnumRarity getRarityFromByte(byte b) {
        return switch (b) {
            case 1 -> EnumRarity.uncommon;
            case 2 -> EnumRarity.rare;
            case 3 -> EnumRarity.epic;
            default -> EnumRarity.common;
        };
    }

    public static byte getCircuitTierFromOreDictName(String name) {
        return switch (name) {
            case "circuitPrimitive" -> 0;
            case "circuitBasic" -> 1;
            case "circuitGood" -> 2;
            case "circuitAdvanced" -> 3;
            case "circuitData" -> 4;
            case "circuitElite" -> 5;
            case "circuitMaster" -> 6;
            case "circuitUltimate" -> 7;
            case "circuitSuperconductor" -> 8;
            case "circuitInfinite" -> 9;
            case "circuitBio" -> 10;
            case "circuitNano", "circuitOptical" -> 11;
            case "circuitPiko", "circuitExotic" -> 12;
            case "circuitQuantum", "circuitCosmic" -> 13;
            case "circuitTranscendent" -> 14;
            default -> -1;
        };
    }

    public static byte getCircuitTierFromItemStack(ItemStack stack) {
        for (String oreName : getOreNames(stack)) {
            byte tier = getCircuitTierFromOreDictName(oreName);
            if (tier != -1) {
                return tier;
            }
        }
        return -1;
    }

    public static boolean isTieredCircuit(ItemStack stack) {
        return getCircuitTierFromItemStack(stack) != -1;
    }

    public static List<String> getOreNames(ItemStack stack) {
        List<String> ret = new ArrayList<>();
        for (int oreID : OreDictionary.getOreIDs(stack)) {
            ret.add(OreDictionary.getOreName(oreID));
        }
        return ret;
    }

    public static byte calculateGlassTier(@Nonnull Block block, @Nonnegative byte meta) {

        byte boroTier = BorosilicateGlass.getTier(block, meta);
        if (boroTier != -1) return boroTier;

        if ("blockAlloyGlass".equals(block.getUnlocalizedName())) return 4;

        if (block.equals(Blocks.glass)) return 3;

        for (BioVatLogicAdder.BlockMetaPair B : BioVatLogicAdder.BioVatGlass.getGlassMap().keySet())
            if (B.getBlock().equals(block) && B.getaByte().equals(meta))
                return BioVatLogicAdder.BioVatGlass.getGlassMap().get(B);

        if (block.getMaterial().equals(Material.glass)) return 3;

        return 0;
    }

    public static <T> IStructureElement<T> ofGlassTiered(byte mintier, byte maxtier, byte notset,
            BiConsumer<T, Byte> setter, Function<T, Byte> getter, int aDots) {
        return new IStructureElement<>() {

            private final IStructureElement<T> placementDelegate = BorosilicateGlass
                    .ofBoroGlass(notset, mintier, maxtier, setter, getter);

            @Override
            public boolean check(T te, World world, int x, int y, int z) {
                if (world.isAirBlock(x, y, z)) return false;
                byte glasstier = BW_Util
                        .calculateGlassTier(world.getBlock(x, y, z), (byte) world.getBlockMetadata(x, y, z));
                // is not a glass ?
                if (glasstier == 0 || glasstier == notset || glasstier < mintier || glasstier > maxtier) return false;
                if (getter.apply(te) == notset) setter.accept(te, glasstier);
                return getter.apply(te) == glasstier;
            }

            @Override
            public boolean spawnHint(T te, World world, int x, int y, int z, ItemStack itemStack) {
                StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), aDots - 1);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return this.placementDelegate.placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                    AutoPlaceEnvironment env) {
                return this.placementDelegate.survivalPlaceBlock(t, world, x, y, z, trigger, env);
            }
        };
    }

    public static <T> IStructureElement<T> ofGlassTieredMixed(byte mintier, byte maxtier, int aDots) {
        return new IStructureElement<>() {

            private final IStructureElement<T> placementDelegate = BorosilicateGlass
                    .ofBoroGlass((byte) 0, mintier, maxtier, (v1, v2) -> {}, v1 -> (byte) 0);

            @Override
            public boolean check(T te, World world, int x, int y, int z) {
                if (world.isAirBlock(x, y, z)) return false;
                byte glasstier = BW_Util
                        .calculateGlassTier(world.getBlock(x, y, z), (byte) world.getBlockMetadata(x, y, z));
                if (glasstier == 0) return false; // is not a glass ?
                return glasstier >= mintier && glasstier <= maxtier;
            }

            @Override
            public boolean spawnHint(T te, World world, int x, int y, int z, ItemStack itemStack) {
                StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), aDots - 1);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return this.placementDelegate.placeBlock(t, world, x, y, z, trigger);
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                    AutoPlaceEnvironment env) {
                return this.placementDelegate.survivalPlaceBlock(t, world, x, y, z, trigger, env);
            }
        };
    }

    private static Field sBufferedRecipeList;

    @SuppressWarnings("unchecked")
    public static List<IRecipe> getGTBufferedRecipeList()
            throws SecurityException, IllegalArgumentException, IllegalAccessException {
        if (sBufferedRecipeList == null) {
            sBufferedRecipeList = FieldUtils.getDeclaredField(GT_ModHandler.class, "sBufferRecipeList", true);
        }
        if (sBufferedRecipeList == null) {
            sBufferedRecipeList = FieldUtils.getField(GT_ModHandler.class, "sBufferRecipeList", true);
        }
        return (List<IRecipe>) sBufferedRecipeList.get(null);
    }

    public static ShapedOreRecipe createGTCraftingRecipe(ItemStack aResult, long aBitMask, Object[] aRecipe) {
        return createGTCraftingRecipe(
                aResult,
                new Enchantment[0],
                new int[0],
                (aBitMask & GT_ModHandler.RecipeBits.MIRRORED) != 0L,
                (aBitMask & GT_ModHandler.RecipeBits.BUFFERED) != 0L,
                (aBitMask & GT_ModHandler.RecipeBits.KEEPNBT) != 0L,
                (aBitMask & GT_ModHandler.RecipeBits.DISMANTLEABLE) != 0L,
                (aBitMask & GT_ModHandler.RecipeBits.NOT_REMOVABLE) == 0L,
                (aBitMask & GT_ModHandler.RecipeBits.REVERSIBLE) != 0L,
                (aBitMask & GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES) != 0L,
                (aBitMask & GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES_IF_SAME_NBT) != 0L,
                (aBitMask & GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES) != 0L,
                (aBitMask & GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_NATIVE_RECIPES) != 0L,
                (aBitMask & GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS) == 0L,
                (aBitMask & GT_ModHandler.RecipeBits.ONLY_ADD_IF_THERE_IS_ANOTHER_RECIPE_FOR_IT) != 0L,
                (aBitMask & GT_ModHandler.RecipeBits.ONLY_ADD_IF_RESULT_IS_NOT_NULL) != 0L,
                aRecipe);
    }

    public static ShapedOreRecipe createGTCraftingRecipe(ItemStack aResult, Enchantment[] aEnchantmentsAdded,
            int[] aEnchantmentLevelsAdded, boolean aMirrored, boolean aBuffered, boolean aKeepNBT,
            boolean aDismantleable, boolean aRemovable, boolean aReversible, boolean aRemoveAllOthersWithSameOutput,
            boolean aRemoveAllOthersWithSameOutputIfTheyHaveSameNBT, boolean aRemoveAllOtherShapedsWithSameOutput,
            boolean aRemoveAllOtherNativeRecipes, boolean aCheckForCollisions,
            boolean aOnlyAddIfThereIsAnyRecipeOutputtingThis, boolean aOnlyAddIfResultIsNotNull, Object[] aRecipe) {
        aResult = GT_OreDictUnificator.get(true, aResult);
        if (aOnlyAddIfResultIsNotNull && aResult == null) return null;
        if (aResult != null && Items.feather.getDamage(aResult) == W) Items.feather.setDamage(aResult, 0);
        if (aRecipe == null || aRecipe.length <= 0) return null;

        boolean tThereWasARecipe = false;

        for (byte i = 0; i < aRecipe.length; i++) {
            if (aRecipe[i] instanceof IItemContainer itemContainer) {
                aRecipe[i] = itemContainer.get(1);
                continue;
            }
            if (aRecipe[i] instanceof Enum<?>enum_) {
                aRecipe[i] = enum_.name();
                continue;
            }
            if (aRecipe[i] != null && !(aRecipe[i] instanceof ItemStack)
                    && !(aRecipe[i] instanceof ItemData)
                    && !(aRecipe[i] instanceof String)
                    && !(aRecipe[i] instanceof Character)) {
                aRecipe[i] = aRecipe[i].toString();
            }
        }

        try {
            StringBuilder shape = new StringBuilder(E);
            int idx = 0;
            if (aRecipe[idx] instanceof Boolean) {
                throw new IllegalArgumentException();
            }

            ArrayList<Object> tRecipeList = new ArrayList<>(Arrays.asList(aRecipe));

            while (aRecipe[idx] instanceof String string) {
                StringBuilder s = new StringBuilder(string);
                idx++;
                shape.append(s);
                while (s.length() < 3) s.append(" ");
                if (s.length() > 3) throw new IllegalArgumentException();

                for (char c : s.toString().toCharArray()) {
                    switch (c) {
                        case 'b':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolBlade.name());
                            break;
                        case 'c':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolCrowbar.name());
                            break;
                        case 'd':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolScrewdriver.name());
                            break;
                        case 'f':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolFile.name());
                            break;
                        case 'h':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolHardHammer.name());
                            break;
                        case 'i':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolSolderingIron.name());
                            break;
                        case 'j':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolSolderingMetal.name());
                            break;
                        case 'k':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolKnife.name());
                            break;
                        case 'm':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolMortar.name());
                            break;
                        case 'p':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolDrawplate.name());
                            break;
                        case 'r':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolSoftHammer.name());
                            break;
                        case 's':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolSaw.name());
                            break;
                        case 'w':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolWrench.name());
                            break;
                        case 'x':
                            tRecipeList.add(c);
                            tRecipeList.add(ToolDictNames.craftingToolWireCutter.name());
                            break;
                    }
                }
            }

            aRecipe = tRecipeList.toArray();

            if (aRecipe[idx] instanceof Boolean) {
                idx++;
            }
            Map<Character, ItemStack> tItemStackMap = new HashMap<>();
            Map<Character, ItemData> tItemDataMap = new HashMap<>();
            tItemStackMap.put(' ', null);

            boolean tRemoveRecipe = true;

            for (; idx < aRecipe.length; idx += 2) {
                if (aRecipe[idx] == null || aRecipe[idx + 1] == null) {
                    if (D1) {
                        GT_Log.err.println(
                                "WARNING: Missing Item for shaped Recipe: "
                                        + (aResult == null ? "null" : aResult.getDisplayName()));
                        for (Object tContent : aRecipe) GT_Log.err.println(tContent);
                    }
                    return null;
                }
                Character chr = (Character) aRecipe[idx];
                Object in = aRecipe[idx + 1];
                if (in instanceof ItemStack) {
                    tItemStackMap.put(chr, GT_Utility.copy(in));
                    tItemDataMap.put(chr, GT_OreDictUnificator.getItemData((ItemStack) in));
                } else if (in instanceof ItemData) {
                    String tString = in.toString();
                    switch (tString) {
                        case "plankWood":
                            tItemDataMap.put(chr, new ItemData(Materials.Wood, M));
                            break;
                        case "stoneNetherrack":
                            tItemDataMap.put(chr, new ItemData(Materials.Netherrack, M));
                            break;
                        case "stoneObsidian":
                            tItemDataMap.put(chr, new ItemData(Materials.Obsidian, M));
                            break;
                        case "stoneEndstone":
                            tItemDataMap.put(chr, new ItemData(Materials.Endstone, M));
                            break;
                        default:
                            tItemDataMap.put(chr, (ItemData) in);
                            break;
                    }
                    ItemStack tStack = GT_OreDictUnificator.getFirstOre(in, 1);
                    if (tStack == null) tRemoveRecipe = false;
                    else tItemStackMap.put(chr, tStack);
                    aRecipe[idx + 1] = in.toString();
                } else if (in instanceof String) {
                    if (in.equals(OreDictNames.craftingChest.toString()))
                        tItemDataMap.put(chr, new ItemData(Materials.Wood, M * 8));
                    else if (in.equals(OreDictNames.craftingBook.toString()))
                        tItemDataMap.put(chr, new ItemData(Materials.Paper, M * 3));
                    else if (in.equals(OreDictNames.craftingPiston.toString()))
                        tItemDataMap.put(chr, new ItemData(Materials.Stone, M * 4, Materials.Wood, M * 3));
                    else if (in.equals(OreDictNames.craftingFurnace.toString()))
                        tItemDataMap.put(chr, new ItemData(Materials.Stone, M * 8));
                    else if (in.equals(OreDictNames.craftingIndustrialDiamond.toString()))
                        tItemDataMap.put(chr, new ItemData(Materials.Diamond, M));
                    else if (in.equals(OreDictNames.craftingAnvil.toString()))
                        tItemDataMap.put(chr, new ItemData(Materials.Iron, M * 10));
                    ItemStack tStack = GT_OreDictUnificator.getFirstOre(in, 1);
                    if (tStack == null) tRemoveRecipe = false;
                    else tItemStackMap.put(chr, tStack);
                } else {
                    throw new IllegalArgumentException();
                }
            }

            if (aReversible && aResult != null) {
                ItemData[] tData = new ItemData[9];
                int x = -1;
                for (char chr : shape.toString().toCharArray()) {
                    x++;
                    tData[x] = tItemDataMap.get(chr);
                }
                if (GT_Utility.arrayContainsNonNull(tData))
                    GT_OreDictUnificator.addItemData(aResult, new ItemData(tData));
            }

            if (aCheckForCollisions && tRemoveRecipe) {
                ItemStack[] tRecipe = new ItemStack[9];
                int x = -1;
                for (char chr : shape.toString().toCharArray()) {
                    x++;
                    tRecipe[x] = tItemStackMap.get(chr);
                    if (tRecipe[x] != null && Items.feather.getDamage(tRecipe[x]) == W)
                        Items.feather.setDamage(tRecipe[x], 0);
                }
                tThereWasARecipe = GT_ModHandler.removeRecipe(tRecipe) != null;
            }
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }

        if (aResult == null || aResult.stackSize <= 0) return null;

        if (aRemoveAllOthersWithSameOutput || aRemoveAllOthersWithSameOutputIfTheyHaveSameNBT
                || aRemoveAllOtherShapedsWithSameOutput
                || aRemoveAllOtherNativeRecipes)
            tThereWasARecipe = GT_ModHandler.removeRecipeByOutput(
                    aResult,
                    !aRemoveAllOthersWithSameOutputIfTheyHaveSameNBT,
                    aRemoveAllOtherShapedsWithSameOutput,
                    aRemoveAllOtherNativeRecipes) || tThereWasARecipe;

        if (aOnlyAddIfThereIsAnyRecipeOutputtingThis && !tThereWasARecipe) {
            ArrayList<IRecipe> tList = (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList();
            int tList_sS = tList.size();
            for (int i = 0; i < tList_sS && !tThereWasARecipe; i++) {
                IRecipe tRecipe = tList.get(i);
                if (GT_ModHandler.sSpecialRecipeClasses.contains(tRecipe.getClass().getName())) continue;
                if (GT_Utility.areStacksEqual(GT_OreDictUnificator.get(tRecipe.getRecipeOutput()), aResult, true)) {
                    tList.remove(i);
                    i--;
                    tList_sS = tList.size();
                    tThereWasARecipe = true;
                }
            }
        }

        if (Items.feather.getDamage(aResult) == W || Items.feather.getDamage(aResult) < 0)
            Items.feather.setDamage(aResult, 0);

        GT_Utility.updateItemStack(aResult);

        return new GT_Shaped_Recipe(
                GT_Utility.copy(aResult),
                aDismantleable,
                aRemovable,
                aKeepNBT,
                aEnchantmentsAdded,
                aEnchantmentLevelsAdded,
                aRecipe).setMirrored(aMirrored);
    }

    public static void shortSleep(long nanos) {
        try {
            long start = System.nanoTime();
            long end;
            do {
                end = System.nanoTime();
            } while (start + nanos >= end);
        } catch (Exception e) {
            MainMod.LOGGER.catching(e);
        }
    }
}
