package gregtech.api.ModernMaterials.PartsClasses;

import java.util.HashSet;

import gregtech.api.ModernMaterials.ModernMaterial;

public interface IAssociatedMaterials {

    HashSet<ModernMaterial> getAssociatedMaterials();

    void addAssociatedMaterial(final ModernMaterial modernMaterial);

}
