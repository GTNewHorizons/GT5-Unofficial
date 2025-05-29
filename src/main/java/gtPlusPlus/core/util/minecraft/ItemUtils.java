package gtPlusPlus.core.util.minecraft;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Minecraft;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gregtech.api.util.GTRecipeBuilder;
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

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.config.ASMConfiguration;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.dusts.BaseItemDustUnique;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenDustGeneration;

public class ItemUtils {
    public static void addItemToOreDictionary(ItemStack stack, final String oreDictName, boolean useWildcardMeta) {
        if (useWildcardMeta) {
            stack = new ItemStack(stack.getItem(), stack.stackSize, GTRecipeBuilder.WILDCARD);
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


    public static ItemStack simpleMetaStack(final String FQRN, final int meta, final int itemstackSize) {
        try {
            final Item em1 = getItemFromFQRN(FQRN);
            if (em1 != null) {
                return new ItemStack(em1, itemstackSize, meta);
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

    public static Item getItemFromFQRN(final String fqrn) // fqrn = fully qualified resource name
    {
        final String[] fqrnSplit = fqrn.split(":");
        return GameRegistry.findItem(fqrnSplit[0], fqrnSplit[1]);
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
            oredictName = "stick" + oredictName.substring(3);
        }

        // Banned Materials and replacements for GT5.8 compat.

        if (oredictName.toLowerCase()
            .contains("ingotclay")) {
            return new ItemStack(Items.clay_ball, amount);
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
        if (ASMConfiguration.debug.debugMode) {
            Logger.modLogger.warn("Looking up: " + oredictName + " - from : ", new Exception());
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
                if (m != Materials._NULL) {
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
        final ItemStack returnValue = GTOreDictUnificator.get(OrePrefixes.dust, material, 1L);
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
        GTLanguageManager.addStringLocalization("gtplusplus.material." + materialName, materialName);
        final Item[] output = {
            new BaseItemDustUnique("itemDust" + unlocalizedName, materialName, mChemForm, Colour, "Dust"),
            new BaseItemDustUnique("itemDustSmall" + unlocalizedName, materialName, mChemForm, Colour, "Small"),
            new BaseItemDustUnique("itemDustTiny" + unlocalizedName, materialName, mChemForm, Colour, "Tiny") };

        // Generate Shaped/Shapeless Recipes

        final ItemStack normalDust = new ItemStack(output[0]);
        final ItemStack smallDust = new ItemStack(output[1]);
        final ItemStack tinyDust = new ItemStack(output[2]);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(4, smallDust), ItemList.Schematic_Dust.get(0))
            .itemOutputs(normalDust)
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(9, tinyDust), ItemList.Schematic_Dust.get(0))
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
                GTUtility.copyAmount(9, tinyDust))) {
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
                GTUtility.copyAmount(4, smallDust))) {
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
        final boolean isChemFormvalid = (aChemForm != null && !aChemForm.isEmpty());
        Item[] output = null;
        if (!onlyLargeDust) {
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

        new RecipeGenDustGeneration(material, disableExtraRecipes);

        return output;
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

            final String temp = itemNames;
            itemNames = temp + (aPos > 0 ? ", " : "") + alph.getDisplayName() + " x" + alph.stackSize;
            aPos++;

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
                value = (id.modId.isEmpty()) ? Minecraft.ID : modname;
            }
        } catch (final Throwable t) {
            try {
                final UniqueIdentifier t2 = GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(item));
                if (t2 != null) {
                    final String modname = (t2.modId == null ? t2.name : t2.modId);
                    value = (t2.modId.isEmpty()) ? Minecraft.ID : modname;
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
        return ItemUtils.getItemStackOfAmountFromOreDictNoBroken(mItemName, mAmount);
    }

    public static ItemStack getOrePrefixStack(OrePrefixes mPrefix, Materials mMat, int mAmount) {
        if (mPrefix == OrePrefixes.rod) {
            mPrefix = OrePrefixes.stick;
        }
        ItemStack aGtStack = GTOreDictUnificator.get(mPrefix, mMat, mAmount);
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
        ItemStack g = new ItemStack(ModItems.AAA_Broken, 1);
        if (aName != null) {
            NBTUtils.setBookTitle(g, EnumChatFormatting.RED + aName);
        }
        return g;
    }

    public static boolean registerFuel(ItemStack aBurnable, int burn) {
        return GTPPCore.burnables.add(Pair.of(burn, aBurnable));
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

        for (ItemStack stack : mInputs) {
            if (stack != null) {
                if (stack.getItem() != null) {
                    if (stack.getItem() == ModItems.AAA_Broken || stack.getItem()
                        .getClass() == ModItems.AAA_Broken.getClass()) {
                        return false;
                    } else if (stack.getItem() == ModItems.ZZZ_Empty || stack.getItem()
                        .getClass() == ModItems.ZZZ_Empty.getClass()) {
                            return false;
                        }
                }
            } else {
                return false;
            }
        }
        for (ItemStack stack : mOutputs) {
            if (stack != null) {
                if (stack.getItem() != null) {
                    if (stack.getItem() == ModItems.AAA_Broken || stack.getItem()
                        .getClass() == ModItems.AAA_Broken.getClass()) {
                        return false;
                    } else if (stack.getItem() == ModItems.ZZZ_Empty || stack.getItem()
                        .getClass() == ModItems.ZZZ_Empty.getClass()) {
                            return false;
                        }
                }
            } else {
                return false;
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

        for (int i = 0; i < p.length; ++i) {
            for (int j = i + 1; j < p.length; ++j) {
                if (p[j] != null && (p[i] == null || GTUtility.areStacksEqual(p[i], p[j]))) {
                    GTUtility.moveStackFromSlotAToSlotB(
                        aInputInventory,
                        aInputInventory,
                        j,
                        i,
                        (byte) 64,
                        (byte) 1,
                        (byte) 64,
                        (byte) 1);
                }
            }
        }

        return aInputInventory;
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
            aDisplay = (StatCollector.translateToLocal(
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
        if (aDisplay == null || aDisplay.length() == 0) {
            aDisplay = aStack.getUnlocalizedName() + ":" + aStack.getItemDamage();
        } else {
            aDisplay += " | Meta: " + aStack.getItemDamage();
        }
        return aDisplay;
    }

    public static ItemStack[] cleanItemStackArray(ItemStack[] input) {
        int aArraySize = input.length;
        ItemStack[] aOutput = new ItemStack[aArraySize];
        ArrayList<ItemStack> aCleanedItems = new ArrayList<>();
        for (ItemStack checkStack : input) {
            if (ItemUtils.checkForInvalidItems(checkStack)) {
                aCleanedItems.add(checkStack);
            }
        }
        for (int i = 0; i < aCleanedItems.size(); i++) {
            ItemStack aMappedStack = aCleanedItems.get(i);
            if (aMappedStack != null) {
                aOutput[i] = aMappedStack;
            }
        }
        return aOutput;
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
        return null;
    }
}
