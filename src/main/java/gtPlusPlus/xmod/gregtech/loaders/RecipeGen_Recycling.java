package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.enums.GT_Values.M;
import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.reflect.FieldUtils;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class RecipeGen_Recycling implements Runnable {

    public static AutoMap<Runnable> mQueuedRecyclingGenerators = new AutoMap<>();

    public static void executeGenerators() {
        if (mQueuedRecyclingGenerators.size() > 0) {
            for (Runnable R : mQueuedRecyclingGenerators.values()) {
                R.run();
            }
        }
    }

    final Material toGenerate;
    public static Map<String, ItemStack> mNameMap;

    public RecipeGen_Recycling(final Material M) {
        this.toGenerate = M;
        if (mNameMap == null) {
            mNameMap = this.getNameMap();
        }
        mQueuedRecyclingGenerators.put(this);
    }

    @Override
    public void run() {
        if (mNameMap != null) {
            generateRecipes(this.toGenerate);
        }
    }

    public static void generateRecipes(final Material material) {

        if (material != null) Logger.WARNING("Generating Recycling recipes for " + material.getLocalizedName());

        final OrePrefixes[] mValidPrefixesAsString = { OrePrefixes.ingot, OrePrefixes.ingotHot, OrePrefixes.nugget,
            OrePrefixes.plate, OrePrefixes.plateDense, OrePrefixes.plateDouble, OrePrefixes.plateTriple,
            OrePrefixes.plateQuadruple, OrePrefixes.plateQuintuple, OrePrefixes.stick, OrePrefixes.stickLong,
            OrePrefixes.bolt, OrePrefixes.screw, OrePrefixes.ring, OrePrefixes.rotor, OrePrefixes.gearGt,
            OrePrefixes.gearGtSmall, OrePrefixes.gear, OrePrefixes.block, OrePrefixes.cableGt01, OrePrefixes.cableGt02,
            OrePrefixes.cableGt04, OrePrefixes.cableGt08, OrePrefixes.cableGt12, OrePrefixes.wireFine,
            OrePrefixes.wireGt01, OrePrefixes.wireGt02, OrePrefixes.wireGt04, OrePrefixes.wireGt08,
            OrePrefixes.wireGt12, OrePrefixes.wireGt16, OrePrefixes.foil, OrePrefixes.frameGt, OrePrefixes.pipeHuge,
            OrePrefixes.pipeLarge, OrePrefixes.pipeMedium, OrePrefixes.pipeSmall, OrePrefixes.pipeTiny, };

        int mSlotIndex = 0;
        Pair<OrePrefixes, ItemStack>[] mValidPairs = new Pair[mValidPrefixesAsString.length];

        for (int r = 0; r < mValidPairs.length; r++) {
            ItemStack temp = getItemStackOfAmountFromOreDictNoBroken(
                mValidPrefixesAsString[r].name() + Utils.sanitizeString(material.getLocalizedName()),
                1);
            if (temp != null) {
                mValidPairs[mSlotIndex++] = new Pair<>(mValidPrefixesAsString[r], temp.copy());
            }
        }

        int validCounter = 0;
        Pair<OrePrefixes, ItemStack>[] temp = mValidPairs;
        for (Pair<OrePrefixes, ItemStack> temp2 : mValidPairs) {
            if (temp2 == null) {
                continue;
            }
            Logger.WARNING(
                "Valid: " + temp2.getValue()
                    .getDisplayName());
            validCounter++;
        }
        Pair<OrePrefixes, ItemStack> temp3[] = new Pair[validCounter];
        int temp4 = 0;
        for (Pair<OrePrefixes, ItemStack> r : mValidPairs) {
            if (r == null) {
                continue;
            }

            temp3[temp4++] = r;
        }
        if (temp3.length > 0) {
            mValidPairs = temp3.clone();
        }

        for (final Pair<OrePrefixes, ItemStack> validPrefix : mValidPairs) {
            if (material == null || validPrefix == null
                || (material.getState() != MaterialState.SOLID && material.getState() != MaterialState.LIQUID)
                || validPrefix.getKey() == OrePrefixes.ingotHot) {
                continue;
            }

            final ItemStack tempStack = validPrefix.getValue();
            final ItemStack mDust = getDust(material, validPrefix.getKey());

            // Maceration
            if (ItemUtils.checkForInvalidItems(tempStack) && mDust != null) {
                RA.stdBuilder()
                    .itemInputs(tempStack)
                    .itemOutputs(mDust)
                    .eut(2)
                    .duration(20 * SECONDS)
                    .addTo(maceratorRecipes);
                Logger.WARNING(
                    "Recycle Recipe: " + material.getLocalizedName()
                        + " - Success - Recycle "
                        + tempStack.getDisplayName()
                        + " and obtain "
                        + mDust.getDisplayName());
            }

            // Fluid Extractor
            if (ItemUtils.checkForInvalidItems(tempStack)) {
                int aFluidAmount = (int) ((144 * validPrefix.getKey().mMaterialAmount) / (M * tempStack.stackSize));
                int aDuration = (int) Math.max(1, (24 * validPrefix.getKey().mMaterialAmount) / M);
                FluidStack fluidInput = material.getFluidStack(aFluidAmount);
                if (fluidInput != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(tempStack)
                        .fluidOutputs()
                        .duration(aDuration)
                        .eut(material.vVoltageMultiplier)
                        .addTo(fluidExtractionRecipes);

                    Logger.WARNING(
                        "Fluid Recycle Recipe: " + material.getLocalizedName()
                            + " - Success - Recycle "
                            + tempStack.getDisplayName()
                            + " and obtain "
                            + aFluidAmount
                            + "mb of "
                            + material.getFluidStack(1)
                                .getLocalizedName()
                            + ". Time: "
                            + aDuration
                            + ", Voltage: "
                            + material.vVoltageMultiplier);
                }
            }
        }

    }

    public static Pair<OrePrefixes, ItemStack> getDustData(final Material aMaterial, final OrePrefixes aPrefix) {
        return getDustData(aMaterial, aPrefix.mMaterialAmount);
    }

    public static Pair<OrePrefixes, ItemStack> getDustData(final Material aMaterial, final long aMaterialAmount) {
        ItemStack mDust = null;
        OrePrefixes mPrefix = null;

        if (aMaterial == null || aMaterialAmount <= 0) {
            return null;
        }
        if ((((aMaterialAmount % M) == 0) || (aMaterialAmount >= (M * 16)))) {
            mDust = get(OrePrefixes.dust, aMaterial, aMaterialAmount / M);
            mPrefix = OrePrefixes.dust;
        }
        if ((mDust == null) && ((((aMaterialAmount * 4) % M) == 0) || (aMaterialAmount >= (M * 8)))) {
            mDust = get(OrePrefixes.dustSmall, aMaterial, (aMaterialAmount * 4) / M);
            mPrefix = OrePrefixes.dustSmall;
        }
        if ((mDust == null) && (((aMaterialAmount * 9) >= M))) {
            mDust = get(OrePrefixes.dustTiny, aMaterial, (aMaterialAmount * 9) / M);
            mPrefix = OrePrefixes.dustTiny;
        }

        if (mPrefix != null && mDust != null) {
            Logger.WARNING("Built valid dust pair.");
            return new Pair<>(mPrefix, mDust);
        } else {
            Logger.WARNING("mPrefix: " + (mPrefix != null));
            Logger.WARNING("mDust: " + (mDust != null));
        }
        Logger.WARNING("Failed to build valid dust pair.");
        return null;
    }

    public static ItemStack getDust(final Material aMaterial, final OrePrefixes aPrefix) {
        return aMaterial == null ? null : getDust(aMaterial, aPrefix.mMaterialAmount);
    }

    public static ItemStack getDust(final Material aMaterial, final long aMaterialAmount) {
        if (aMaterialAmount <= 0) {
            return null;
        }
        ItemStack rStack = null;
        if ((((aMaterialAmount % M) == 0) || (aMaterialAmount >= (M * 16)))) {
            Logger.WARNING("Trying to get a Dust");
            rStack = get(OrePrefixes.dust, aMaterial, aMaterialAmount / M);
        }
        if ((rStack == null) && ((((aMaterialAmount * 4) % M) == 0) || (aMaterialAmount >= (M * 8)))) {
            Logger.WARNING("Trying to get a Small Dust");
            rStack = get(OrePrefixes.dustSmall, aMaterial, (aMaterialAmount * 4) / M);
        }
        if ((rStack == null) && (((aMaterialAmount * 9) >= M))) {
            Logger.WARNING("Trying to get a Tiny Dust");
            rStack = get(OrePrefixes.dustTiny, aMaterial, (aMaterialAmount * 9) / M);
        }
        return rStack;
    }

    public static ItemStack get(final Object aName, final long aAmount) {
        return get(aName, null, aAmount, true, true);
    }

    public static ItemStack get(final Object aName, final ItemStack aReplacement, final long aAmount) {
        return get(aName, aReplacement, aAmount, true, true);
    }

    public static ItemStack get(final OrePrefixes aPrefix, final Material aMaterial, final long aAmount) {
        return get(aPrefix, aMaterial, null, aAmount);
    }

    public static ItemStack get(final OrePrefixes aPrefix, final Material aMaterial, final ItemStack aReplacement,
        final long aAmount) {
        return get(
            aPrefix.name() + Utils.sanitizeString(aMaterial.getLocalizedName()),
            aReplacement,
            aAmount,
            false,
            true);
    }

    public static ItemStack get(final Object aName, final ItemStack aReplacement, final long aAmount,
        final boolean aMentionPossibleTypos, final boolean aNoInvalidAmounts) {
        if (aNoInvalidAmounts && (aAmount < 1L)) {
            Logger.WARNING("Returning Null. Method: " + ReflectionUtils.getMethodName(0));
            Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(1));
            Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(2));
            Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(3));
            Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(4));
            return null;
        }
        if (!mNameMap.containsKey(aName.toString()) && aMentionPossibleTypos) {
            Logger.WARNING("Unknown Key for Unification, Typo? " + aName);
        }
        return GT_Utility.copyAmount(
            aAmount,
            new Object[] { mNameMap.get(aName.toString()), getFirstOre(aName, aAmount), aReplacement });
    }

    public static ItemStack getFirstOre(final Object aName, final long aAmount) {
        if (GT_Utility.isStringInvalid(aName)) {
            Logger.WARNING("Returning Null. Method: " + ReflectionUtils.getMethodName(0));
            Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(1));
            Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(2));
            Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(3));
            Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(4));
            return null;
        }
        final ItemStack tStack = mNameMap.get(aName.toString());
        if (GT_Utility.isStackValid(tStack)) {
            Logger.WARNING("Found valid stack.");
            return GT_Utility.copyAmount(aAmount, new Object[] { tStack });
        }
        return GT_Utility.copyAmount(aAmount, getOres(aName).toArray());
    }

    public static ArrayList<ItemStack> getOres(final Object aOreName) {
        final String aName = (aOreName == null) ? "" : aOreName.toString();
        final ArrayList<ItemStack> rList = new ArrayList<>();
        if (GT_Utility.isStringValid(aName)) {
            Logger.WARNING("Making a list of all OreDict entries for " + aOreName + ".");
            if (rList.addAll(OreDictionary.getOres(aName))) {
                Logger.WARNING("Added " + rList.size() + " elements to list.");
            } else {
                Logger.WARNING("Failed to Add Collection from oreDictionary, forcing an entry.");
                rList.add(ItemUtils.getItemStackOfAmountFromOreDict((String) aOreName, 1));
            }
        }
        return rList;
    }

    @SuppressWarnings("unchecked")
    public Map<String, ItemStack> getNameMap() {
        Map<String, ItemStack> tempMap;
        try {
            tempMap = (Map<String, ItemStack>) FieldUtils
                .readStaticField(GT_OreDictUnificator.class, "sName2StackMap", true);
            if (tempMap != null) {
                Logger.WARNING("Found 'sName2StackMap' in GT_OreDictUnificator.class.");
                return tempMap;
            }
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        Logger.WARNING("Invalid map stored in GT_OreDictUnificator.class, unable to find sName2StackMap field.");
        return null;
    }

    public static ItemStack getItemStackOfAmountFromOreDictNoBroken(String oredictName, final int amount) {

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
                    returnValue = ItemUtils.getGregtechDust(m, amount);
                    if (ItemUtils.checkForInvalidItems(returnValue)) {
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
            return null;
        } catch (final Throwable t) {
            return null;
        }
    }

    public static ItemStack getItemStackOfAmountFromOreDict(String oredictName, final int amount) {
        String mTemp = oredictName;

        // Banned Materials and replacements for GT5.8 compat.

        if (oredictName.toLowerCase()
            .contains("ingotclay")) {
            return ItemUtils.getSimpleStack(Items.clay_ball, amount);
        }

        final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(mTemp);
        if (!oreDictList.isEmpty()) {
            final ItemStack returnValue = oreDictList.get(0)
                .copy();
            returnValue.stackSize = amount;
            return returnValue;
        }
        return null;
        // return getItemStackOfAmountFromOreDictNoBroken(mTemp, amount);
    }
}
