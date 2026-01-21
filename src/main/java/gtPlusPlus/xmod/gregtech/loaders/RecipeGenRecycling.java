package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGenRecycling implements Runnable {

    public static ArrayList<Runnable> mQueuedRecyclingGenerators = new ArrayList<>();

    public static void executeGenerators() {
        if (!mQueuedRecyclingGenerators.isEmpty()) {
            for (Runnable R : mQueuedRecyclingGenerators) {
                R.run();
            }
        }
    }

    final Material toGenerate;

    public RecipeGenRecycling(final Material M) {
        this.toGenerate = M;
        mQueuedRecyclingGenerators.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    public static void generateRecipes(final Material material) {
        if (material == null) return;

        Logger.WARNING("Generating Recycling recipes for " + material.getLocalizedName());

        final OrePrefixes[] mValidPrefixesAsString = { OrePrefixes.ingot, OrePrefixes.ingotHot, OrePrefixes.nugget,
            OrePrefixes.plate, OrePrefixes.plateDense, OrePrefixes.plateDouble, OrePrefixes.plateTriple,
            OrePrefixes.plateQuadruple, OrePrefixes.plateQuintuple, OrePrefixes.stick, OrePrefixes.stickLong,
            OrePrefixes.bolt, OrePrefixes.screw, OrePrefixes.ring, OrePrefixes.rotor, OrePrefixes.gearGt,
            OrePrefixes.gearGtSmall, OrePrefixes.block, OrePrefixes.cableGt01, OrePrefixes.cableGt02,
            OrePrefixes.cableGt04, OrePrefixes.cableGt08, OrePrefixes.cableGt12, OrePrefixes.wireFine,
            OrePrefixes.wireGt01, OrePrefixes.wireGt02, OrePrefixes.wireGt04, OrePrefixes.wireGt08,
            OrePrefixes.wireGt12, OrePrefixes.wireGt16, OrePrefixes.foil, OrePrefixes.frameGt, OrePrefixes.pipeHuge,
            OrePrefixes.pipeLarge, OrePrefixes.pipeMedium, OrePrefixes.pipeSmall, OrePrefixes.pipeTiny,
            OrePrefixes.dust, };

        int mSlotIndex = 0;
        Pair<OrePrefixes, ItemStack>[] mValidPairs = new Pair[mValidPrefixesAsString.length];

        for (int r = 0; r < mValidPairs.length; r++) {
            ItemStack temp = getItemStackOfAmountFromOreDictNoBroken(
                mValidPrefixesAsString[r].getName() + StringUtils.sanitizeString(material.getLocalizedName()),
                1);
            if (temp != null) {
                mValidPairs[mSlotIndex++] = Pair.of(mValidPrefixesAsString[r], temp.copy());
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
        Pair<OrePrefixes, ItemStack>[] temp3 = new Pair[validCounter];
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
            if (validPrefix == null) continue;

            final OrePrefixes orePrefix = validPrefix.getKey();

            if (material.getState() != MaterialState.SOLID && material.getState() != MaterialState.LIQUID) continue;
            if (orePrefix == OrePrefixes.ingotHot) continue;

            final ItemStack tempStack = validPrefix.getValue();
            final boolean isDustInput = orePrefix == OrePrefixes.dust;
            final ItemStack mDust = isDustInput ? null : getDust(material, orePrefix);

            // Maceration
            if (!isDustInput && tempStack != null && mDust != null) {
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
            if (tempStack == null) continue;
            if (isDustInput && material.requiresBlastFurnace()) {
                continue;
            }

            final long materialAmount = orePrefix.getMaterialAmount();
            final int aFluidAmount = (int) ((materialAmount * INGOTS) / (M * tempStack.stackSize));
            final int aDuration = (int) Math.max(1, (24 * materialAmount) / M);
            final FluidStack fluidOutput = material.getFluidStack(aFluidAmount);

            if (fluidOutput == null) continue;

            GTValues.RA.stdBuilder()
                .itemInputs(tempStack)
                .fluidOutputs(fluidOutput)
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

    public static Pair<OrePrefixes, ItemStack> getDustData(final Material aMaterial, final OrePrefixes aPrefix) {
        return getDustData(aMaterial, aPrefix.getMaterialAmount());
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
            return Pair.of(mPrefix, mDust);
        } else {
            Logger.WARNING("mPrefix: " + (mPrefix != null));
            Logger.WARNING("mDust: " + (mDust != null));
        }
        Logger.WARNING("Failed to build valid dust pair.");
        return null;
    }

    public static ItemStack getDust(final Material aMaterial, final OrePrefixes aPrefix) {
        return aMaterial == null ? null : getDust(aMaterial, aPrefix.getMaterialAmount());
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
            aPrefix.getName() + StringUtils.sanitizeString(aMaterial.getLocalizedName()),
            aReplacement,
            aAmount,
            false,
            true);
    }

    public static ItemStack get(final Object aName, final ItemStack aReplacement, final long aAmount,
        final boolean aMentionPossibleTypos, final boolean aNoInvalidAmounts) {
        if (aNoInvalidAmounts && (aAmount < 1L)) {
            Logger.modLogger.warn("Returning Null. Method: ", new Exception());
            return null;
        }
        if (!GTOreDictUnificator.getName2StackMap()
            .containsKey(aName.toString()) && aMentionPossibleTypos) {
            Logger.WARNING("Unknown Key for Unification, Typo? " + aName);
        }
        return GTUtility.copyAmount(
            aAmount,
            GTOreDictUnificator.getName2StackMap()
                .get(aName.toString()),
            getFirstOre(aName, aAmount),
            aReplacement);
    }

    public static ItemStack getFirstOre(final Object aName, final long aAmount) {
        if (GTUtility.isStringInvalid(aName)) {
            Logger.modLogger.warn("Returning Null. Method: ", new Exception());
            return null;
        }
        final ItemStack tStack = GTOreDictUnificator.getName2StackMap()
            .get(aName.toString());
        if (GTUtility.isStackValid(tStack)) {
            Logger.WARNING("Found valid stack.");
            return GTUtility.copyAmount(aAmount, new Object[] { tStack });
        }
        return GTUtility.copyAmount(aAmount, getOres(aName).toArray());
    }

    public static ArrayList<ItemStack> getOres(final Object aOreName) {
        final String aName = (aOreName == null) ? "" : aOreName.toString();
        final ArrayList<ItemStack> rList = new ArrayList<>();
        if (GTUtility.isStringValid(aName)) {
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

    public static ItemStack getItemStackOfAmountFromOreDictNoBroken(String oredictName, final int amount) {
        if (oredictName.contains("-") || oredictName.contains("_")) {
            oredictName = StringUtils.sanitizeStringKeepDashes(oredictName);
        } else {
            oredictName = StringUtils.sanitizeString(oredictName);
        }

        // Adds a check to grab dusts using GT methodology if possible.
        ItemStack returnValue = null;
        if (oredictName.toLowerCase()
            .contains("dust")) {
            final String MaterialName = oredictName.toLowerCase()
                .replace("dust", "");
            final Materials m = Materials.get(MaterialName);
            if (m != null && m != Materials._NULL) {
                returnValue = GTOreDictUnificator.get(OrePrefixes.dust, m, 1L);
                if (returnValue != null) {
                    return returnValue;
                }
            }
        }
        if (returnValue == null) {
            returnValue = getItemStackOfAmountFromOreDict(oredictName, amount);
            if (returnValue != null) {
                return returnValue.copy();
            }
        }
        return null;
    }

    public static ItemStack getItemStackOfAmountFromOreDict(String oredictName, final int amount) {

        // Banned Materials and replacements for GT5.8 compat.

        if (oredictName.toLowerCase()
            .contains("ingotclay")) {
            return new ItemStack(Items.clay_ball, amount);
        }

        final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
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
