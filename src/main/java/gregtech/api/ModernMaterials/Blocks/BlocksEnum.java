package gregtech.api.ModernMaterials.Blocks;

import gregtech.api.ModernMaterials.ModernMaterial;

import java.util.ArrayList;

public enum BlocksEnum {

    FrameBox("% Frame Box");

    public String getLocalisedName(ModernMaterial material) {
        return unlocalisedName.replace("%", material.getName());
    }

    final private String unlocalisedName;
    public final ArrayList<ModernMaterial> associatedMaterials = new ArrayList<>();

    BlocksEnum(final String unlocalisedName) {
        this.unlocalisedName = unlocalisedName;
    }
}
