package gregtech.api.ModernMaterials;

import gregtech.api.ModernMaterials.PartsClasses.CustomPartInfo;
import gregtech.api.ModernMaterials.PartsClasses.PartsEnum;
import java.awt.*;
import java.util.HashMap;

public class ModernMaterial {

    private final HashMap<PartsEnum, CustomPartInfo> existingPartsForMaterial = new HashMap<>();
    private Color mColor;
    private int mID;
    private String mName;

    public ModernMaterial() {}

    public ModernMaterial setColor(Color aColor) {
        mColor = aColor;
        return this;
    }

    public ModernMaterial setColor(int aRed, int aGreen, int aBlue, int aAlpha) {
        mColor = new Color(aRed, aGreen, aBlue, aAlpha);
        return this;
    }

    public ModernMaterial setColor(int aRed, int aGreen, int aBlue) {
        mColor = new Color(aRed, aGreen, aBlue);
        return this;
    }

    public ModernMaterial setName(String aName) {
        mName = aName;
        return this;
    }

    public ModernMaterial addParts(final PartsEnum... aParts) {
        for (PartsEnum aPart : aParts) {
            existingPartsForMaterial.put(aPart, new CustomPartInfo(aPart));
        }
        return this;
    }

    public ModernMaterial addPartsCustom(final CustomPartInfo... aCustomParts) {
        for (CustomPartInfo aCustomPart : aCustomParts) {
            existingPartsForMaterial.put(aCustomPart.mPart, aCustomPart);
        }
        return this;
    }

    public ModernMaterial build() {
        return this;
    }

    public void setID(int aID) {
        mID = aID;
    }

    public Color getColor() {
        return mColor;
    }

    public int getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public ModernMaterial addPart(final PartsEnum aPart) {
        existingPartsForMaterial.put(aPart, new CustomPartInfo(aPart));
        return this;
    }

    // This will override all existing parts settings and enable ALL possible parts. Be careful!
    public ModernMaterial addAllParts() {
        for (PartsEnum tPart : PartsEnum.values()) {
            existingPartsForMaterial.put(tPart, new CustomPartInfo(tPart));
        }
        return this;
    }

    public ModernMaterial addPart(final CustomPartInfo aCustomPartInfo) {
        existingPartsForMaterial.put(aCustomPartInfo.mPart, aCustomPartInfo);
        return this;
    }

    public CustomPartInfo getPart(final PartsEnum aPart) {
        return existingPartsForMaterial.get(aPart);
    }

    public boolean doesPartExist(PartsEnum aPart) {
        return existingPartsForMaterial.containsKey(aPart);
    }
}
