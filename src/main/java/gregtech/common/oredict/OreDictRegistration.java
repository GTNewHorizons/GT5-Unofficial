package gregtech.common.oredict;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.MaterialLib;
import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OreDictUtils;

/// One buffered ore-dictionary registration: the stack, the prefix its name resolved to, and the material its
/// name names -- the `NULL` placeholder under a prefix that is not material-based.
final class OreDictRegistration {

    @NotNull
    final String oreName;
    @NotNull
    final ItemStack stack;
    @NotNull
    final OrePrefixes prefix;
    @NotNull
    final Material material;
    @Nullable
    final String modId;

    OreDictRegistration(@NotNull String oreName, @NotNull ItemStack stack, @NotNull OrePrefixes prefix,
        @NotNull Material material, @Nullable String modId) {

        this.oreName = oreName;
        this.stack = stack;
        this.prefix = prefix;
        this.material = material;
        this.modId = modId == null || modId.equals("UNKNOWN") ? null : modId;
    }

    /// Runs the prefix's registrators for this registration. A MaterialLib item is skipped unless its ore name is
    /// the target of an [OreDictAliases] prefix alias (`crystalCertusQuartz`, `plankWood`, ...): the shape consumers
    /// in `gregtech.loaders.shapeconsumers` process every MaterialLib item once per (shape, material), but not
    /// under an alias prefix.
    void registerRecipes() {
        if (stack.getItem() == null || prefix.isIgnored(material)
            || isMaterialLibItem(stack) && !OreDictAliases.isPrefixAlias(oreName)
            || isShapeOverride()
            || OreDictUtils.isUnifiedCopy(stack)) {
            return;
        }

        if (stack.stackSize != 1) {
            stack.stackSize = 1;
        }

        ItemStack single = GTUtility.copyAmount(1, stack);
        if (OreDictUtils.isInterchangeableWithTarget(stack)) {
            GTRecipeBuilder.withoutRecipesTaking(single, () -> prefix.processOre(material, oreName, modId, single));
        } else {
            prefix.processOre(material, oreName, modId, single);
        }
    }

    /// Whether `stack` is the foreign item MaterialLib serves for this registration's (prefix, material) pair,
    /// such as the vanilla diamond for `gem` Diamond. The shape consumers receive that pair like any other, so
    /// processing it here as well would register its recipes twice.
    private boolean isShapeOverride() {
        if (!MaterialParts.isCutOver(prefix, material) || MaterialParts.isMinted(prefix, material)) return false;
        return GTUtility.areStacksEqual(MaterialParts.stack(prefix, material, 1), stack);
    }

    /// Whether `stack`'s item belongs to MaterialLib. The test is the item's own registered domain, not [#modId],
    /// which names whichever mod's code was active when the underlying `OreDictionary.OreRegisterEvent` fired.
    private static boolean isMaterialLibItem(ItemStack stack) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return id != null && MaterialLib.MODID.equals(id.modId);
    }
}
