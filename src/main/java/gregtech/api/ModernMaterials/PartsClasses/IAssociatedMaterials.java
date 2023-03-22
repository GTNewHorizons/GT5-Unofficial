package gregtech.api.ModernMaterials.PartsClasses;

import gregtech.api.ModernMaterials.ModernMaterial;

import java.util.ArrayList;

public interface IAssociatedMaterials {

    ArrayList<ModernMaterial> getAssociatedMaterials();
    void addAssociatedMaterial(final ModernMaterial modernMaterial);

}
