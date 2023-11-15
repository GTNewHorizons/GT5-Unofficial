package gregtech.api.ModernMaterials.Items.PartsClasses;

import java.util.HashSet;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ModernMaterials.ModernMaterial;

public interface IEnumPart {

    HashSet<ModernMaterial> getAssociatedMaterials();

    void addAssociatedMaterial(final ModernMaterial modernMaterial);

    @NotNull
    ItemStack getPart(@NotNull ModernMaterial material, int stackSize);

    void setItemStack(@NotNull ModernMaterial material, ItemStack itemStack);

}
