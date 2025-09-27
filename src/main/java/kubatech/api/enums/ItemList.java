package kubatech.api.enums;

import static gregtech.api.enums.GTValues.NI;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public enum ItemList implements IItemContainer {

    ExtremeEntityCrusher,
    ExtremeIndustrialApiary,
    ExtremeIndustrialGreenhouse,
    DraconicEvolutionFusionCrafter,
    HighTemperatureGasCooledReactor,
    LegendaryBlackTea,
    LegendaryButterflyTea,
    LegendaryEarlGrayTea,
    LegendaryGreenTea,
    LegendaryLemonTea,
    LegendaryMilkTea,
    LegendaryOolongTea,
    LegendaryPeppermintTea,
    LegendaryPuerhTea,
    LegendaryRedTea,
    LegendaryWhiteTea,
    LegendaryYellowTea,
    LegendaryUltimateTea,
    BlackTea,
    EarlGrayTea,
    GreenTea,
    LemonTea,
    MilkTea,
    OolongTea,
    PeppermintTea,
    PuerhTea,
    WhiteTea,
    YellowTea,
    BlackTeaLeaf,
    GreenTeaLeaf,
    OolongTeaLeaf,
    PuerhTeaLeaf,
    WhiteTeaLeaf,
    YellowTeaLeaf,
    TeaLeafDehydrated,
    SteamedTeaLeaf,
    RolledTeaLeaf,
    OxidizedTeaLeaf,
    FermentedTeaLeaf,
    BruisedTeaLeaf,
    PartiallyOxidizedTeaLeaf,
    TeaAcceptorResearchNote,
    TeaAcceptor,
    TeaStorage,
    Beeeeee,
    DEFCCasingBase,
    DEFCCasingT1,
    DEFCCasingT2,
    DEFCCasingT3,
    DEFCCasingT4,
    DEFCCasingT5,
    DEFCDraconicSchematic,
    DEFCWyvernSchematic,
    DEFCAwakenedSchematic,
    DEFCChaoticSchematic,
    KubaFakeItemEECVoid,
    PlaceHolderItem;

    private @Nullable ItemStack mStack;
    private boolean mHasNotBeenSet = true;

    @Override
    public @NotNull IItemContainer set(@Nullable Item aItem) {
        mHasNotBeenSet = false;
        if (aItem == null) return this;
        ItemStack aStack = new ItemStack(aItem, 1, 0);
        mStack = GTUtility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public @NotNull IItemContainer set(@NotNull ItemStack aStack) {
        mHasNotBeenSet = false;
        mStack = GTUtility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public @NotNull IItemContainer hidden() {
        codechicken.nei.api.API.hideItem(get(1L));
        return this;
    }

    @Override
    public @Nullable Item getItem() {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (GTUtility.isStackInvalid(mStack)) return null;
        return mStack.getItem();
    }

    @Override
    public Block getBlock() {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        return GTUtility.getBlockFromItem(getItem());
    }

    @Override
    public final boolean hasBeenSet() {
        return !mHasNotBeenSet;
    }

    @Override
    public boolean isStackEqual(Object aStack) {
        return isStackEqual(aStack, false, false);
    }

    @Override
    public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT) {
        if (GTUtility.isStackInvalid(aStack)) return false;
        return GTUtility.areUnificationsEqual((ItemStack) aStack, aWildcard ? getWildcard(1) : get(1), aIgnoreNBT);
    }

    @Override
    public ItemStack get(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (GTUtility.isStackInvalid(mStack)) return GTUtility.copyAmount(aAmount, aReplacements);
        return GTUtility.copyAmount(aAmount, GTOreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getWildcard(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (GTUtility.isStackInvalid(mStack)) return GTUtility.copyAmount(aAmount, aReplacements);
        return GTUtility.copyAmountAndMetaData(aAmount, WILDCARD, GTOreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getUndamaged(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (GTUtility.isStackInvalid(mStack)) return GTUtility.copyAmount(aAmount, aReplacements);
        return GTUtility.copyAmountAndMetaData(aAmount, 0, GTOreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getAlmostBroken(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (GTUtility.isStackInvalid(mStack)) return GTUtility.copyAmount(aAmount, aReplacements);
        return GTUtility.copyAmountAndMetaData(aAmount, mStack.getMaxDamage() - 1, GTOreDictUnificator.get(mStack));
    }

    @Override
    public @Nullable ItemStack getWithName(long aAmount, @NotNull String aDisplayName, Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        if (GTUtility.isStackInvalid(rStack)) return NI;

        // CamelCase alphanumeric words from aDisplayName
        StringBuilder tCamelCasedDisplayNameBuilder = new StringBuilder();
        final String[] tDisplayNameWords = aDisplayName.split("\\W");
        for (String tWord : tDisplayNameWords) {
            if (!tWord.isEmpty()) tCamelCasedDisplayNameBuilder.append(
                tWord.substring(0, 1)
                    .toUpperCase(Locale.US));
            if (tWord.length() > 1) tCamelCasedDisplayNameBuilder.append(
                tWord.substring(1)
                    .toLowerCase(Locale.US));
        }
        if (tCamelCasedDisplayNameBuilder.length() == 0) {
            // CamelCased DisplayName is empty, so use hash of aDisplayName
            tCamelCasedDisplayNameBuilder.append(((Long) (long) aDisplayName.hashCode()));
        }

        // Construct a translation key from UnlocalizedName and CamelCased DisplayName
        final String tKey = rStack.getUnlocalizedName() + ".with." + tCamelCasedDisplayNameBuilder + ".name";

        rStack.setStackDisplayName(GTLanguageManager.addStringLocalization(tKey, aDisplayName));
        return GTUtility.copyAmount(aAmount, rStack);
    }

    @Override
    public @Nullable ItemStack getWithCharge(long aAmount, int aEnergy, Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        if (GTUtility.isStackInvalid(rStack)) return null;
        GTModHandler.chargeElectricItem(rStack, aEnergy, Integer.MAX_VALUE, true, false);
        return GTUtility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements) {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (GTUtility.isStackInvalid(mStack)) return GTUtility.copyAmount(aAmount, aReplacements);
        return GTUtility.copyAmountAndMetaData(aAmount, aMetaValue, GTOreDictUnificator.get(mStack));
    }

    @Override
    public @NotNull IItemContainer registerOre(Object @NotNull... aOreNames) {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        for (Object tOreName : aOreNames) GTOreDictUnificator.registerOre(tOreName, get(1));
        return this;
    }

    @Override
    public @NotNull IItemContainer registerWildcardAsOre(Object @NotNull... aOreNames) {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        for (Object tOreName : aOreNames) GTOreDictUnificator.registerOre(tOreName, getWildcard(1));
        return this;
    }

    /**
     * Returns the internal stack. This method is unsafe. It's here only for quick operations. DON'T CHANGE THE RETURNED
     * VALUE!
     */
    public ItemStack getInternalStack_unsafe() {
        return mStack;
    }
}
