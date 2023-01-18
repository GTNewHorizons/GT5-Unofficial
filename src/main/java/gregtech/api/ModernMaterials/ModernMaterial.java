package gregtech.api.ModernMaterials;

import gregtech.api.ModernMaterials.PartsClasses.CustomPartInfo;
import gregtech.api.ModernMaterials.PartsClasses.PartsEnum;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ModernMaterial {

    public Color getMaterialColor() {
        return materialColor;
    }

    private final Color materialColor;

    public int getMaterialID() {
        return materialID;
    }

    public String getMaterialName() {
        return materialName;
    }

    private int materialID;
    private static int materialIDTracker;

    private String materialName;

    public ModernMaterial addPart(final PartsEnum partsEnum) {
        existingPartsForMaterial.put(partsEnum, new CustomPartInfo(partsEnum));
        return this;
    }

    // This will override all existing parts settings and enable ALL possible parts. Be careful!
    public ModernMaterial addAllParts() {
        for (PartsEnum partsEnum : PartsEnum.values()) {
            existingPartsForMaterial.put(partsEnum, new CustomPartInfo(partsEnum));
        }
        return this;
    }

    public ModernMaterial addPart(final CustomPartInfo customPartInfo) {
        existingPartsForMaterial.put(customPartInfo.partsEnum, customPartInfo);
        return this;
    }

    public CustomPartInfo getPart(final PartsEnum partsEnum) {
        return existingPartsForMaterial.get(partsEnum);
    }

    private final HashMap<PartsEnum, CustomPartInfo> existingPartsForMaterial = new HashMap<>();

    public ModernMaterial(Color materialColor, final String materialName) {
        this.materialColor = materialColor;
        this.materialID = materialIDTracker++; // todo rework, obviously non-viable due to registration order.
        this.materialName = materialName;
    }

    public boolean doesPartExist(PartsEnum partsEnum) {
        return existingPartsForMaterial.containsKey(partsEnum);
    }

}
