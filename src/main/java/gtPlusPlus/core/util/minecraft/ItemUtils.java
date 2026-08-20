package gtPlusPlus.core.util.minecraft;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.Minecraft;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.item.base.dusts.BaseItemDustUnique;
import gtPlusPlus.core.util.math.MathUtils;

public class ItemUtils {

    public static ItemStack getItemStackOfAmountFromOreDict(String oredictName, final int amount) {
        String mTemp = oredictName;
        if (oredictName.contains("-") || oredictName.contains("_")) {
            mTemp = StringUtils.sanitizeStringKeepDashes(mTemp);
        } else {
            mTemp = StringUtils.sanitizeString(mTemp);
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
        return null;
    }

    public static ItemStack getItemStackOfAmountFromOreDictNoBroken(String oredictName, final int amount) {
        try {
            if (oredictName.contains("-") || oredictName.contains("_")) {
                oredictName = StringUtils.sanitizeStringKeepDashes(oredictName);
            } else {
                oredictName = StringUtils.sanitizeString(oredictName);
            }
            // Adds a check to grab dusts using GT methodology if possible.
            if (oredictName.toLowerCase()
                .contains("dust")) {
                final String MaterialName = oredictName.toLowerCase()
                    .replace("dust", "");
                final Material m = gregtech.api.material.MaterialUtils.byLegacyName(MaterialName);
                if (m != null) {
                    ItemStack returnValue = GTOreDictUnificator.get(OrePrefixes.dust, m, 1);
                    if (returnValue != null) {
                        return returnValue;
                    }
                }
            }
            ItemStack returnValue = getItemStackOfAmountFromOreDict(oredictName, amount);
            if (returnValue != null) {
                return returnValue.copy();
            }
            return null;
        } catch (final Exception t) {
            return null;
        }
    }

    public static Item[] generateSpecialUseDusts(final String unlocalizedName, final String materialName,
        String mChemForm, final int Colour) {
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

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, normalDust), ItemList.Schematic_Dust_Small.get(0))
            .itemOutputs(GTUtility.copyAmount(4, smallDust))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        // Tiny Dusts
        GTModHandler.addCraftingRecipe(
            normalDust,
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "TTT", "TTT", "TTT", 'T', tinyDust });
        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(9, tinyDust),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "D  ", "   ", "   ", 'D', normalDust });

        // Small Dusts
        GTModHandler.addCraftingRecipe(
            normalDust,
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "SS ", "SS ", "   ", 'S', smallDust });
        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(4, smallDust),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { " D ", "   ", "   ", 'D', normalDust });

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
        } catch (final Exception t) {
            try {
                final UniqueIdentifier t2 = GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(item));
                if (t2 != null) {
                    final String modname = (t2.modId == null ? t2.name : t2.modId);
                    value = (t2.modId.isEmpty()) ? Minecraft.ID : modname;
                }
            } catch (final Exception t3) {
                GTplusplus.logger.error(t3);
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
        if (mPrefix == OrePrefixes.rod) {
            mPrefix = OrePrefixes.stick;
        }
        return MaterialParts.stack(mPrefix, mMat, mAmount);
    }

    /**
     *
     * @param mInput
     * @return {@link Boolean} - True if {@link ItemStack}[] only contains valid items.
     */
    public static boolean checkForInvalidItems(ItemStack[] mInput) {
        if (mInput == null) {
            return false;
        }

        for (ItemStack stack : mInput) {
            if (stack == null) return false;
        }

        return true;
    }

    public static IInventory organiseInventory(IInventory aInputInventory) {
        GTUtility.compactStandardInventory(aInputInventory);

        return aInputInventory;
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
