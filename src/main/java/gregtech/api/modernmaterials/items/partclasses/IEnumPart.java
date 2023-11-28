package gregtech.api.modernmaterials.items.partclasses;

import java.util.HashSet;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.modernmaterials.ModernMaterial;

public interface IEnumPart {

    HashSet<ModernMaterial> getAssociatedMaterials();

    void addAssociatedMaterial(final ModernMaterial modernMaterial);

    @NotNull
    ItemStack getPart(@NotNull ModernMaterial material, int stackSize);

}
