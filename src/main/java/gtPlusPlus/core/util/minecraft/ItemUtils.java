package gtPlusPlus.core.util.minecraft;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Minecraft;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.dusts.BaseItemDustUnique;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.xmod.gregtech.api.items.Gregtech_MetaTool;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_DustGeneration;

public class ItemUtils {

    public static ItemStack getSimpleStack(final Item x) {
        return getSimpleStack(x, 1);
    }

    public static ItemStack getSimpleStack(final Block x) {
        return simpleMetaStack(Item.getItemFromBlock(x), 0, 1);
    }

    public static ItemStack getSimpleStack(final Block x, int i) {
        if (i == 0) {
            return getSimpleStack(x, i, 1);
        }

        return getSimpleStack(x, 0, i);
    }

    public static ItemStack getSimpleStack(final Block x, int meta, int i) {
        return simpleMetaStack(Item.getItemFromBlock(x), meta, i);
    }

    public static ItemStack getSimpleStack(final Item x, final int i) {
        return new ItemStack(x, i);
    }

    public static ItemStack getSimpleStack(final ItemStack x, final int i) {
        if (x == null) {
            return null;
        }
        final ItemStack r = x.copy();
        r.stackSize = i;
        return r;
    }

    public static final int WILDCARD_VALUE = Short.MAX_VALUE;

    public static ItemStack getWildcardStack(final ItemStack x) {
        return ItemUtils.simpleMetaStack(x, WILDCARD_VALUE, 1);
    }

    public static ItemStack getIC2Cell(final int meta) {
        return GT_ModHandler.getModItem(IndustrialCraft2.ID, "itemCellEmpty", 1L, meta);
    }

    public static ItemStack getEmptyCell() {
        return getEmptyCell(1);
    }

    public static ItemStack getEmptyCell(int i) {
        if (ItemList.Cell_Empty.hasBeenSet()) {
            return ItemList.Cell_Empty.get(i);
        }
        final ItemStack temp = GT_ModHandler.getModItem(IndustrialCraft2.ID, "itemCellEmpty", i, 0);
        return temp != null ? temp : null;
    }

    public static void getItemForOreDict(final String FQRN, final String oreDictName, final String itemName,
        final int meta) {
        try {
            Item em = null;
            final Item em1 = getItemFromFQRN(FQRN);

            if (em1 != null) {
                em = em1;
            }

            if (em != null) {

                final ItemStack metaStack = new ItemStack(em, 1, meta);
                GT_OreDictUnificator.registerOre(oreDictName, metaStack);

            }
        } catch (final NullPointerException e) {
            Logger.ERROR(itemName + " not found. [NULL]");
        }
    }

    public static void addItemToOreDictionary(ItemStack stack, final String oreDictName, boolean useWildcardMeta) {
        if (useWildcardMeta) {
            stack = ItemUtils.getWildcardStack(stack);
        }
        try {
            OreDictionary.registerOre(oreDictName, stack);
        } catch (final NullPointerException e) {
            Logger.ERROR(ItemUtils.getItemName(stack) + " not registered. [NULL]");
        }
    }

    public static void addItemToOreDictionary(final ItemStack stack, final String oreDictName) {
        addItemToOreDictionary(stack, oreDictName, false);
    }

    public static ItemStack getItemStackWithMeta(final boolean MOD, final String FQRN, final String itemName,
        final int meta, final int itemstackSize) {
        if (MOD) {
            try {
                Item em = null;
                final Item em1 = getItemFromFQRN(FQRN);

                if (em1 != null) {
                    if (null == em) {
                        em = em1;
                    }
                    if (em != null) {
                        final ItemStack metaStack = new ItemStack(em, itemstackSize, meta);
                        return metaStack;
                    }
                }
                return null;
            } catch (final NullPointerException e) {
                Logger.ERROR(itemName + " not found. [NULL]");
                return null;
            }
        }
        return null;
    }

    public static ItemStack simpleMetaStack(final String FQRN, final int meta, final int itemstackSize) {
        try {
            Item em = null;
            final Item em1 = getItemFromFQRN(FQRN);
            // Utils.LOG_WARNING("Found: "+em1.getUnlocalizedName()+":"+meta);
            if (em1 != null) {
                if (null == em) {
                    em = em1;
                }
                if (em != null) {
                    final ItemStack metaStack = new ItemStack(em, itemstackSize, meta);
                    return metaStack;
                }
            }
            return null;
        } catch (final NullPointerException e) {
            Logger.ERROR(FQRN + " not found. [NULL]");
            return null;
        }
    }

    public static ItemStack simpleMetaStack(ItemStack simpleStack, int meta, int size) {
        return simpleMetaStack(simpleStack.getItem(), meta, size);
    }

    public static ItemStack simpleMetaStack(final Item item, int meta, int size) {
        if (item == null) {
            return null;
        }
        if (meta < 0 || meta > Short.MAX_VALUE) {
            meta = 0;
        }
        if (size < 0 || size > 64) {
            size = 1;
        }
        return new ItemStack(item, size, meta);
    }

    public static ItemStack simpleMetaStack(final Block block, final int meta, final int size) {
        return simpleMetaStack(Item.getItemFromBlock(block), meta, size);
    }

    public static ItemStack getCorrectStacktype(final String fqrn, final int stackSize) {
        final String oreDict = "ore:";
        ItemStack temp;
        if (fqrn.toLowerCase()
            .contains(oreDict.toLowerCase())) {
            final String sanitizedName = fqrn.replace(oreDict, "");
            temp = ItemUtils.getItemStackFromFQRN(sanitizedName, stackSize);
            return temp;
        }
        final String[] fqrnSplit = fqrn.split(":");
        String temp1;
        String temp2;
        temp1 = fqrnSplit[1];
        if (fqrnSplit.length < 3) {
            temp2 = "0";
        } else {
            temp2 = fqrnSplit[2];
        }
        temp = ItemUtils.getItemStackWithMeta(true, fqrn, temp1, Integer.parseInt(temp2), stackSize);
        return temp;
    }

    public static Item getItemFromFQRN(final String fqrn) // fqrn = fully qualified resource name
    {
        final String[] fqrnSplit = fqrn.split(":");
        return GameRegistry.findItem(fqrnSplit[0], fqrnSplit[1]);
    }

    public static ItemStack getItemStackFromFQRN(final String fqrn, final int Size) // fqrn = fully qualified resource
                                                                                    // name
    {
        Logger.INFO("Trying to split string '" + fqrn + "'.");
        final String[] fqrnSplit = fqrn.split(":");
        if (fqrnSplit.length < 2) {
            return null;
        } else {
            if (fqrnSplit.length == 2) {
                Logger.INFO("Mod: " + fqrnSplit[0] + ", Item: " + fqrnSplit[1]);
                return GameRegistry.findItemStack(fqrnSplit[0], fqrnSplit[1], Size);
            } else if (fqrnSplit.length == 3 && fqrnSplit[2] != null && fqrnSplit[2].length() > 0) {
                Logger.INFO("Mod: " + fqrnSplit[0] + ", Item: " + fqrnSplit[1] + ", Meta: " + fqrnSplit[2]);
                ItemStack aStack = GameRegistry.findItemStack(fqrnSplit[0], fqrnSplit[1], Size);
                int aMeta = Integer.parseInt(fqrnSplit[2]);
                if (aStack != null && (aMeta >= 0 && aMeta <= Short.MAX_VALUE)) {
                    return ItemUtils.simpleMetaStack(aStack, aMeta, Size);
                } else {
                    Logger.INFO("Could not find instance of Item: " + fqrnSplit[1]);
                }
            }
        }
        return null;
    }

    public static ItemStack[] validItemsForOreDict(final String oredictName) {
        final List<?> validNames = MaterialUtils.oreDictValuesForEntry(oredictName);
        final ItemStack[] inputs = new ItemStack[validNames.size()];
        for (int i = 0; i < validNames.size(); i++) {
            inputs[i] = (ItemStack) validNames.get(i);
        }
        return inputs;
    }

    public static ItemStack getItemStackOfAmountFromOreDict(String oredictName, final int amount) {
        String mTemp = oredictName;

        if (oredictName.contains("-") || oredictName.contains("_")) {
            mTemp = Utils.sanitizeString(mTemp, new char[] { '-', '_' });
        } else {
            mTemp = Utils.sanitizeString(mTemp);
        }

        if (oredictName.contains("rod")) {
            String s = "stick" + oredictName.substring(3);
            oredictName = s;
        }

        // Banned Materials and replacements for GT5.8 compat.

        if (oredictName.toLowerCase()
            .contains("ingotclay")) {
            return getSimpleStack(Items.clay_ball, amount);
        }

        final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(mTemp);
        if (!oreDictList.isEmpty()) {
            final ItemStack returnValue = oreDictList.get(0)
                .copy();
            returnValue.stackSize = amount;
            return returnValue;
        }
        Logger.INFO("Failed to find `" + oredictName + "` in OD.");
        return getErrorStack(amount, oredictName + " x" + amount);
        // return getItemStackOfAmountFromOreDictNoBroken(mTemp, amount);
    }

    public static ItemStack getItemStackOfAmountFromOreDictNoBroken(String oredictName, final int amount) {
        if (CORE_Preloader.DEBUG_MODE) {
            Logger.WARNING("Looking up: " + oredictName + " - from method: " + ReflectionUtils.getMethodName(1));
            Logger.WARNING("Looking up: " + oredictName + " - from method: " + ReflectionUtils.getMethodName(2));
            Logger.WARNING("Looking up: " + oredictName + " - from method: " + ReflectionUtils.getMethodName(3));
            Logger.WARNING("Looking up: " + oredictName + " - from method: " + ReflectionUtils.getMethodName(4));
            Logger.WARNING("Looking up: " + oredictName + " - from method: " + ReflectionUtils.getMethodName(5));
        }

        try {

            if (oredictName.contains("-") || oredictName.contains("_")) {
                oredictName = Utils.sanitizeString(oredictName, new char[] { '-', '_' });
            } else {
                oredictName = Utils.sanitizeString(oredictName);
            }

            // Adds a check to grab dusts using GT methodology if possible.
            ItemStack returnValue = null;
            if (oredictName.toLowerCase()
                .contains("dust")) {
                final String MaterialName = oredictName.toLowerCase()
                    .replace("dust", "");
                final Materials m = Materials.get(MaterialName);
                if (m != null && m != Materials._NULL) {
                    returnValue = getGregtechDust(m, amount);
                    if (checkForInvalidItems(returnValue)) {
                        return returnValue;
                    }
                }
            }
            if (returnValue == null) {
                returnValue = getItemStackOfAmountFromOreDict(oredictName, amount);
                if (ItemUtils.checkForInvalidItems(returnValue)) {
                    return returnValue.copy();
                }
            }

            Logger.RECIPE(oredictName + " was not valid.");
            return null;
        } catch (final Throwable t) {
            return null;
        }
    }

    public static ItemStack getGregtechDust(final Materials material, final int amount) {
        final ItemStack returnValue = GT_OreDictUnificator.get(OrePrefixes.dust, material, 1L);
        if (returnValue != null) {
            if (ItemUtils.checkForInvalidItems(returnValue)) {
                return returnValue.copy();
            }
        }
        Logger.WARNING(material + " was not valid.");
        return null;
    }

    // NullFormula
    public static Item[] generateSpecialUseDusts(final String unlocalizedName, final String materialName,
        final int Colour) {
        return generateSpecialUseDusts(unlocalizedName, materialName, "NullFormula", Colour);
    }

    public static Item[] generateSpecialUseDusts(final String unlocalizedName, final String materialName,
        String mChemForm, final int Colour) {
        GT_LanguageManager.addStringLocalization("gtplusplus.material." + materialName, materialName);
        final Item[] output = {
            new BaseItemDustUnique("itemDust" + unlocalizedName, materialName, mChemForm, Colour, "Dust"),
            new BaseItemDustUnique("itemDustSmall" + unlocalizedName, materialName, mChemForm, Colour, "Small"),
            new BaseItemDustUnique("itemDustTiny" + unlocalizedName, materialName, mChemForm, Colour, "Tiny") };

        // Generate Shaped/Shapeless Recipes

        final ItemStack normalDust = ItemUtils.getSimpleStack(output[0]);
        final ItemStack smallDust = ItemUtils.getSimpleStack(output[1]);
        final ItemStack tinyDust = ItemUtils.getSimpleStack(output[2]);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(4, smallDust), ItemList.Schematic_Dust.get(0))
            .itemOutputs(normalDust)
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(9, tinyDust), ItemList.Schematic_Dust.get(0))
            .itemOutputs(normalDust)
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        if (ItemUtils.checkForInvalidItems(tinyDust) && ItemUtils.checkForInvalidItems(normalDust)) {
            if (RecipeUtils.addShapedRecipe(
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                tinyDust,
                normalDust)) {
                Logger.WARNING("9 Tiny dust to 1 Dust Recipe: " + materialName + " - Success");
            } else {
                Logger.WARNING("9 Tiny dust to 1 Dust Recipe: " + materialName + " - Failed");
            }

            if (RecipeUtils.addShapedRecipe(
                normalDust,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                ItemUtils.getSimpleStack(tinyDust, 9))) {
                Logger.WARNING("9 Tiny dust from 1 Recipe: " + materialName + " - Success");
            } else {
                Logger.WARNING("9 Tiny dust from 1 Recipe: " + materialName + " - Failed");
            }
        }

        if (ItemUtils.checkForInvalidItems(smallDust) && ItemUtils.checkForInvalidItems(normalDust)) {
            if (RecipeUtils.addShapedRecipe(
                smallDust,
                smallDust,
                null,
                smallDust,
                smallDust,
                null,
                null,
                null,
                null,
                normalDust)) {
                Logger.WARNING("4 Small dust to 1 Dust Recipe: " + materialName + " - Success");
            } else {
                Logger.WARNING("4 Small dust to 1 Dust Recipe: " + materialName + " - Failed");
            }
            if (RecipeUtils.addShapedRecipe(
                null,
                normalDust,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                ItemUtils.getSimpleStack(smallDust, 4))) {
                Logger.WARNING("4 Small dust from 1 Dust Recipe: " + materialName + " - Success");
            } else {
                Logger.WARNING("4 Small dust from 1 Dust Recipe: " + materialName + " - Failed");
            }
        }

        return output;
    }

    public static Item[] generateSpecialUseDusts(final Material material, final boolean onlyLargeDust) {
        return generateSpecialUseDusts(material, onlyLargeDust, false);
    }

    public static Item[] generateSpecialUseDusts(final Material material, final boolean onlyLargeDust,
        final boolean disableExtraRecipes) {
        final String materialName = material.getUnlocalizedName();
        final String unlocalizedName = Utils.sanitizeString(materialName);
        final int Colour = material.getRgbAsHex();
        final String aChemForm = material.vChemicalFormula;
        final boolean isChemFormvalid = (aChemForm != null && aChemForm.length() > 0);
        Item[] output = null;
        if (onlyLargeDust == false) {
            output = new Item[] {
                new BaseItemDustUnique(
                    "itemDust" + unlocalizedName,
                    materialName,
                    isChemFormvalid ? aChemForm : "",
                    Colour,
                    "Dust"),
                new BaseItemDustUnique(
                    "itemDustSmall" + unlocalizedName,
                    materialName,
                    isChemFormvalid ? aChemForm : "",
                    Colour,
                    "Small"),
                new BaseItemDustUnique(
                    "itemDustTiny" + unlocalizedName,
                    materialName,
                    isChemFormvalid ? aChemForm : "",
                    Colour,
                    "Tiny") };
        } else {
            output = new Item[] { new BaseItemDustUnique("itemDust" + unlocalizedName, materialName, Colour, "Dust") };
        }

        new RecipeGen_DustGeneration(material, disableExtraRecipes);

        return output;
    }

    public static boolean isRadioactive(final String materialName) {
        int sRadiation = 0;
        if (materialName.toLowerCase()
            .contains("uranium")) {
            sRadiation = 2;
        } else if (materialName.toLowerCase()
            .contains("plutonium")) {
                sRadiation = 4;
            } else if (materialName.toLowerCase()
                .contains("thorium")) {
                    sRadiation = 1;
                }
        if (sRadiation >= 1) {
            return true;
        }
        return false;
    }

    public static int getRadioactivityLevel(final String materialName) {
        int sRadiation = 0;
        if (materialName.toLowerCase()
            .contains("uranium")) {
            sRadiation = 2;
        } else if (materialName.toLowerCase()
            .contains("plutonium")) {
                sRadiation = 4;
            } else if (materialName.toLowerCase()
                .contains("thorium")) {
                    sRadiation = 1;
                }
        return sRadiation;
    }

    public static String getArrayStackNames(final FluidStack[] aStack) {
        String itemNames = "Fluid Array: ";
        for (final FluidStack alph : aStack) {
            if (alph != null) {
                final String temp = itemNames;
                itemNames = temp + ", " + alph.getLocalizedName() + " x" + alph.amount;
            } else {
                final String temp = itemNames;
                itemNames = temp + ", " + "null" + " x" + "0";
            }
        }
        return itemNames;
    }

    public static String getArrayStackNames(final ItemStack[] aStack) {
        String itemNames = "";
        int aPos = 0;
        for (final ItemStack alph : aStack) {
            if (alph == null) {
                continue;
            }
            if (alph != null) {
                final String temp = itemNames;
                itemNames = temp + (aPos > 0 ? ", " : "") + alph.getDisplayName() + " x" + alph.stackSize;
                aPos++;
            }
        }
        return itemNames;
    }

    private static final Map<Item, String> mModidCache = new HashMap<>();

    private static String getModId(final Item item) {
        if (mModidCache.containsKey(item)) {
            return mModidCache.get(item);
        }
        String value = "";
        try {
            final GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(item);
            if (id != null) {
                final String modname = (id.modId == null ? id.name : id.modId);
                value = ((id == null) || id.modId.equals("")) ? Minecraft.ID : modname;
            }
        } catch (final Throwable t) {
            try {
                final UniqueIdentifier t2 = GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(item));
                if (t2 != null) {
                    final String modname = (t2.modId == null ? t2.name : t2.modId);
                    value = ((t2 == null) || t2.modId.equals("")) ? Minecraft.ID : modname;
                }
            } catch (final Throwable t3) {
                t3.printStackTrace();
                value = "bad modid";
            }
        }
        if (!mModidCache.containsKey(item)) {
            return mModidCache.put(item, value);
        }
        return value;
    }

    public static String getModId(final ItemStack key) {
        return getModId(key.getItem());
    }

    // Take 2 - GT/GT++ Dusts
    public static ItemStack getGregtechDust(final String oredictName, final int amount) {
        final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
        if (!oreDictList.isEmpty()) {
            ItemStack returnvalue;
            for (ItemStack itemStack : oreDictList) {
                final String modid = getModId(itemStack.getItem());
                if (modid != null && (modid.equals(GregTech.ID) || modid.equals(GTPlusPlus.ID))) {
                    returnvalue = itemStack.copy();
                    returnvalue.stackSize = amount;
                    return returnvalue;
                }
            }
        }
        return getNonTinkersDust(oredictName, amount);
    }

    // Anything But Tinkers Dust
    public static ItemStack getNonTinkersDust(final String oredictName, final int amount) {
        final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
        if (!oreDictList.isEmpty()) {
            ItemStack returnvalue;
            for (ItemStack itemStack : oreDictList) {
                final String modid = getModId(itemStack.getItem());
                if (modid != null && !modid.equals("tconstruct")) {
                    returnvalue = itemStack.copy();
                    returnvalue.stackSize = amount;
                    return returnvalue;
                }
            }
        }
        // If only Tinkers dust exists, bow down and just use it.
        return getItemStackOfAmountFromOreDictNoBroken(oredictName, amount);
    }

    public static ItemStack getOrePrefixStack(OrePrefixes mPrefix, Material mMat, int mAmount) {

        String mName = Utils.sanitizeString(mMat.getLocalizedName());

        String mItemName = mPrefix.name() + mName;
        ItemStack gregstack = ItemUtils.getItemStackOfAmountFromOreDictNoBroken(mItemName, mAmount);
        if (gregstack == null) {
            return null;
        }
        return (gregstack);
    }

    public static ItemStack getOrePrefixStack(OrePrefixes mPrefix, Materials mMat, int mAmount) {
        if (mPrefix == OrePrefixes.rod) {
            mPrefix = OrePrefixes.stick;
        }
        ItemStack aGtStack = GT_OreDictUnificator.get(mPrefix, mMat, mAmount);
        if (aGtStack == null) {
            Logger
                .INFO("Failed to find `" + mPrefix + MaterialUtils.getMaterialName(mMat) + "` in OD. [Prefix Search]");
            return getErrorStack(mAmount, (mPrefix.toString() + MaterialUtils.getMaterialName(mMat) + " x" + mAmount));
        } else {
            return aGtStack;
        }
    }

    public static ItemStack getErrorStack(int mAmount) {
        return getErrorStack(mAmount, null);
    }

    public static ItemStack getErrorStack(int mAmount, String aName) {
        ItemStack g = getSimpleStack(ModItems.AAA_Broken, 1);
        if (aName != null) {
            NBTUtils.setBookTitle(g, EnumChatFormatting.RED + aName);
        }
        return g;
    }

    public static ItemStack[] getStackOfAllOreDictGroup(String oredictname) {
        final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictname);
        if (!oreDictList.isEmpty()) {
            final ItemStack[] returnValues = new ItemStack[oreDictList.size()];
            for (int i = 0; i < oreDictList.size(); i++) {
                if (oreDictList.get(i) != null) {
                    returnValues[i] = oreDictList.get(i);
                }
            }
            return returnValues.length > 0 ? returnValues : null;
        } else {
            return null;
        }
    }

    public static boolean registerFuel(ItemStack aBurnable, int burn) {
        return CORE.burnables.add(new Pair<>(burn, aBurnable));
    }

    public static boolean checkForInvalidItems(ItemStack mInput) {
        return checkForInvalidItems(new ItemStack[] { mInput });
    }

    public static boolean checkForInvalidItems(ItemStack[] mInput) {
        return checkForInvalidItems(mInput, new ItemStack[] {});
    }

    /**
     *
     * @param mInputs
     * @return {@link Boolean} - True if {@link ItemStack}[] only contains valid items.
     */
    public static boolean checkForInvalidItems(ItemStack[] mInputs, ItemStack[] mOutputs) {
        if (mInputs == null || mOutputs == null) {
            return false;
        }

        if (mInputs.length > 0) {
            for (ItemStack stack : mInputs) {
                if (stack != null) {
                    if (stack.getItem() != null) {
                        if (stack.getItem() == ModItems.AAA_Broken || stack.getItem()
                            .getClass() == ModItems.AAA_Broken.getClass()) {
                            return false;
                        } else if (stack.getItem() == ModItems.ZZZ_Empty || stack.getItem()
                            .getClass() == ModItems.ZZZ_Empty.getClass()) {
                                return false;
                            } else {
                                continue;
                            }
                    } else {
                        continue;
                    }
                } else {
                    return false;
                }
            }
        }
        if (mOutputs.length > 0) {
            for (ItemStack stack : mOutputs) {
                if (stack != null) {
                    if (stack.getItem() != null) {
                        if (stack.getItem() == ModItems.AAA_Broken || stack.getItem()
                            .getClass() == ModItems.AAA_Broken.getClass()) {
                            return false;
                        } else if (stack.getItem() == ModItems.ZZZ_Empty || stack.getItem()
                            .getClass() == ModItems.ZZZ_Empty.getClass()) {
                                return false;
                            } else {
                                continue;
                            }
                    } else {
                        continue;
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    public static IInventory organiseInventory(IInventory aInputInventory) {
        ItemStack[] p = new ItemStack[aInputInventory.getSizeInventory()];
        for (int o = 0; o < aInputInventory.getSizeInventory(); o++) {
            p[o] = aInputInventory.getStackInSlot(o);
        }
        // ItemStack[] g = organiseInventory(p);

        IInventory aTemp = aInputInventory;
        for (int i = 0; i < p.length; ++i) {
            for (int j = i + 1; j < p.length; ++j) {
                if (p[j] != null && (p[i] == null || GT_Utility.areStacksEqual(p[i], p[j]))) {
                    GT_Utility.moveStackFromSlotAToSlotB(aTemp, aTemp, j, i, (byte) 64, (byte) 1, (byte) 64, (byte) 1);
                }
            }
        }

        return aTemp;
    }

    public static String getFluidName(FluidStack aFluid) {
        return aFluid != null ? aFluid.getFluid()
            .getLocalizedName(aFluid) : "NULL";
    }

    public static String getItemName(ItemStack aStack) {
        if (aStack == null) {
            return "ERROR - Empty Stack";
        }
        String aDisplay = null;
        try {
            aDisplay = ("" + StatCollector.translateToLocal(
                aStack.getItem()
                    .getUnlocalizedNameInefficiently(aStack) + ".name")).trim();
            if (aStack.hasTagCompound()) {
                if (aStack.stackTagCompound != null && aStack.stackTagCompound.hasKey("display", 10)) {
                    NBTTagCompound nbttagcompound = aStack.stackTagCompound.getCompoundTag("display");

                    if (nbttagcompound.hasKey("Name", 8)) {
                        aDisplay = nbttagcompound.getString("Name");
                    }
                }
            }
        } catch (Throwable ignored) {

        }
        if (aDisplay == null || aDisplay.length() <= 0) {
            aDisplay = aStack.getUnlocalizedName() + ":" + aStack.getItemDamage();
        } else {
            aDisplay += " | Meta: " + aStack.getItemDamage();
        }
        return aDisplay;
    }

    public static String getUnlocalizedItemName(ItemStack aStack) {
        if (aStack == null) {
            return "ERROR.Empty.Stack";
        }
        String aDisplay = null;
        try {
            aDisplay = (aStack.getUnlocalizedName()).trim();
        } catch (Throwable t) {
            aDisplay = aStack.getItem()
                .getUnlocalizedName();
        }
        if (aDisplay == null || aDisplay.length() <= 0) {
            aDisplay = aStack.getItem()
                .getUnlocalizedNameInefficiently(aStack);
        }
        return aDisplay;
    }

    public static boolean isItemGregtechTool(ItemStack aStack) {
        if (aStack == null) {
            return false;
        }
        final Item mItem = aStack.getItem();
        final Item aSkookum = ItemUtils.getItemFromFQRN("miscutils:gt.plusplus.metatool.01");
        final Class aSkookClass = aSkookum.getClass();
        if (aSkookClass.isInstance(mItem) || mItem instanceof GT_MetaGenerated_Tool_01
            || mItem instanceof MetaGeneratedGregtechTools
            || mItem instanceof Gregtech_MetaTool
            || mItem == aSkookum) {
            return true;
        }
        return false;
    }

    public static boolean isToolScrewdriver(ItemStack aScrewdriver) {
        if (isItemGregtechTool(aScrewdriver)
            && (aScrewdriver.getItemDamage() == 22 || aScrewdriver.getItemDamage() == 150)) {
            return true;
        }
        return false;
    }

    public static ItemStack[] cleanItemStackArray(ItemStack[] input) {
        int aArraySize = input.length;
        ItemStack[] aOutput = new ItemStack[aArraySize];
        AutoMap<ItemStack> aCleanedItems = new AutoMap<>();
        for (ItemStack checkStack : input) {
            if (ItemUtils.checkForInvalidItems(checkStack)) {
                aCleanedItems.put(checkStack);
            }
        }
        for (int i = 0; i < aArraySize; i++) {
            ItemStack aMappedStack = aCleanedItems.get(i);
            if (aMappedStack != null) {
                aOutput[i] = aMappedStack;
            }
        }
        return aOutput;
    }

    public static boolean doesOreDictHaveEntryFor(String string) {
        return OreDictUtils.containsValidEntries(string);
    }

    public static void hideItemFromNEI(ItemStack aItemToHide) {
        codechicken.nei.api.API.hideItem(aItemToHide);
    }

    public static ItemStack getNullStack() {
        return GT_Values.NI;
    }

    public static ItemStack depleteStack(ItemStack aStack) {
        return depleteStack(aStack, 1);
    }

    public static ItemStack depleteStack(ItemStack aStack, int aAmount) {
        final int cap = aStack.stackSize;
        if (cap >= 1 && cap >= aAmount) {
            ItemStack aDepStack = aStack.copy();
            aDepStack.stackSize = (MathUtils.balance((aDepStack.stackSize - 1), 0, 64));
            if (aDepStack.stackSize > 0) {
                return aDepStack;
            }
        }
        return getNullStack();
    }

    public static boolean isControlCircuit(ItemStack aStack) {
        if (aStack != null) {
            Item aItem = aStack.getItem();
            if (aItem == CI.getNumberedBioCircuit(0)
                .getItem() || aItem
                    == GT_Utility.getIntegratedCircuit(0)
                        .getItem()
                || aItem == CI.getNumberedAdvancedCircuit(0)
                    .getItem()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCatalyst(ItemStack aStack) {
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mBlueCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mBrownCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mOrangeCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mPurpleCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mRedCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mYellowCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mPinkCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mFormaldehydeCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mSolidAcidCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mInfiniteMutationCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, AgriculturalChem.mGreenCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mPlatinumGroupCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mPlasticPolymerCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mRubberPolymerCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mAdhesionPromoterCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mTitaTungstenIndiumCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mRadioactivityCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mRareEarthGroupCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mSimpleNaquadahCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mAdvancedNaquadahCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mRawIntelligenceCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mUltimatePlasticCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mBiologicalIntelligenceCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.TemporalHarmonyCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mLimpidWaterCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mFlawlessWaterCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mParticleAccelerationCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mSynchrotronCapableCatalyst, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mAlgagenicGrowthPromoterCatalyst, true)) {
            return true;
        }

        return false;
    }

    public static boolean isMillingBall(ItemStack aStack) {
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mMillingBallAlumina, true)) {
            return true;
        }
        if (GT_Utility.areStacksEqual(aStack, GenericChem.mMillingBallSoapstone, true)) {
            return true;
        }
        return false;
    }
}
