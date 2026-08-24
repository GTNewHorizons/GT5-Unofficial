package tectech.voidcraft.ship;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.GTItemStack;

/**
 * Maps Voidcraft cover item stacks (item + damage) to {@link VoidcraftCoverComponent}.
 *
 * <p>
 * Populated once during {@code preLoad} (the cover item must exist first); read afterwards by the cover class, the
 * component MTE (placement gate) and the assembler scan.
 */
public final class VoidcraftCoverRegistry {

    private static final Map<GTItemStack, VoidcraftCoverComponent> COVERS = new HashMap<>();
    private static boolean ready = false;

    private VoidcraftCoverRegistry() {}

    public static int getCount() {
        return VoidcraftCoverComponent.ALL.length;
    }

    public static int getMaxTier() {
        int max = 0;
        for (VoidcraftCoverComponent component : VoidcraftCoverComponent.ALL) {
            max = Math.max(max, component.getTier());
        }
        return max;
    }

    /** Registers one cover item stack (call once per cover, during preLoad). */
    public static void register(ItemStack coverItem, VoidcraftCoverComponent component) {
        COVERS.put(new GTItemStack(coverItem), component);
    }

    /** Marks registration complete; after this point {@link #isCover(ItemStack)} starts answering. */
    public static void markReady() {
        ready = true;
    }

    /** The cover behind this stack, or null if the stack is not a registered Voidcraft cover. */
    @Nullable
    public static VoidcraftCoverComponent byStack(@Nullable ItemStack stack) {
        if (!ready || stack == null || stack.getItemDamage() < 0) {
            return null;
        }
        return COVERS.get(new GTItemStack(stack));
    }

    /** True if this stack is a registered Voidcraft cover item (used to gate cover placement on hull blocks). */
    public static boolean isCover(@Nullable ItemStack stack) {
        return byStack(stack) != null;
    }

    /** Unmodifiable snapshot of all registered cover stacks (for diagnostics/tests). */
    public static Map<GTItemStack, VoidcraftCoverComponent> all() {
        return Collections.unmodifiableMap(COVERS);
    }
}
