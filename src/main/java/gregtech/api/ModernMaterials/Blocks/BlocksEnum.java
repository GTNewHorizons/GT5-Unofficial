package gregtech.api.ModernMaterials.Blocks;

import gregtech.api.ModernMaterials.ModernMaterial;

import java.util.ArrayList;

public enum BlocksEnum {

    FrameBox("dwakodpo");

    final private String nameToBeLocalised;
    public final ArrayList<ModernMaterial> associatedMaterials = new ArrayList<>();

    BlocksEnum(final String nameToBeLocalised) {
        this.nameToBeLocalised = nameToBeLocalised;
    }
}
