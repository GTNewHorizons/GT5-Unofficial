package gregtech.api.ModernMaterials.PartsClasses;

import gregtech.api.ModernMaterials.ModernMaterial;

import java.util.HashSet;

public interface IAssociatedMaterials {

    HashSet<ModernMaterial> getAssociatedMaterials();
    void addAssociatedMaterial(final ModernMaterial modernMaterial);

}
