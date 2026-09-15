package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface INEIPreviewModifier {

    default void onPreviewConstruct(@NotNull ItemStack trigger) {}

    default void onPreviewStructureComplete(@NotNull ItemStack trigger) {}
}
