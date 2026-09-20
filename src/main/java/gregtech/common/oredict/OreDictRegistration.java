package gregtech.common.oredict;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.MaterialLib;
import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTUtility;

/// One buffered ore-dictionary registration: the stack, the prefix its name resolved to, the material the
/// census resolved it to (the `NULL` placeholder when the name carries none), and the recognition marker it
/// resolved through when the name was not a material at all (see
/// [OreDictRegistrationHandler#resolveCensusMaterial]). When the marker is set, [#registerRecipes] dispatches
/// ore processing through it rather than through [#material].
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
    final Material recognitionMarker;
    @Nullable
    final String modId;

    OreDictRegistration(@NotNull String oreName, @NotNull ItemStack stack, @NotNull OrePrefixes prefix,
        @NotNull Material material, @Nullable Material recognitionMarker, @Nullable String modId) {

        this.oreName = oreName;
        this.stack = stack;
        this.prefix = prefix;
        this.material = material;
        this.recognitionMarker = recognitionMarker;
        this.modId = modId == null || modId.equals("UNKNOWN") ? null : modId;
    }

    void registerRecipes() {
        if (stack.getItem() == null || prefix.isIgnored(recognitionMarker != null ? recognitionMarker : material)
            || isMaterialLibItem(stack)
            || isShapeOverride()) {
            return;
        }

        if (stack.stackSize != 1) {
            stack.stackSize = 1;
        }

        ItemStack copy = GTUtility.copyAmount(1, stack);
        if (recognitionMarker != null) {
            prefix.processRecognitionOre(recognitionMarker, oreName, modId, copy);
        } else {
            prefix.processOre(material == null ? Materials.NULL : material, oreName, modId, copy);
        }
    }

    /// Whether `stack` is the foreign item MaterialLib serves for this registration's (prefix, material) pair,
    /// such as the vanilla diamond for `gem` Diamond. The shape consumers receive that pair like any other, so
    /// processing it here as well would register its recipes twice.
    private boolean isShapeOverride() {
        if (recognitionMarker != null || !MaterialParts.isCutOver(prefix, material)
            || MaterialParts.isMinted(prefix, material)) return false;
        return GTUtility.areStacksEqual(MaterialParts.stack(prefix, material, 1), stack);
    }

    /// Whether `stack`'s item belongs to MaterialLib. Such stacks are processed by
    /// `gregtech.loaders.shapeconsumers` instead, once per (shape, material). The test is the item's own
    /// registered domain, not [#modId], which names whichever mod's code was active when the underlying
    /// `OreDictionary.OreRegisterEvent` fired.
    private static boolean isMaterialLibItem(ItemStack stack) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return id != null && MaterialLib.MODID.equals(id.modId);
    }
}
