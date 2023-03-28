package gregtech.api.ModernMaterials;

import java.awt.*;
import java.util.*;

import gregtech.api.ModernMaterials.Fluids.FluidEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.PartProperties.Textures.TextureType;
import gregtech.api.ModernMaterials.PartsClasses.CustomPartInfo;
import gregtech.api.ModernMaterials.PartsClasses.PartsEnum;
import org.jetbrains.annotations.NotNull;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerMaterial;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Metallic;

public class ModernMaterial {

    private final HashMap<PartsEnum, CustomPartInfo> existingPartsForMaterial = new HashMap<>();
    private Color color;
    private int materialID;
    private final String materialName;
    private long materialTier;
    private double materialTimeMultiplier;
    public final ArrayList<ModernMaterialFluid> existingFluids = new ArrayList<>();

    public ModernMaterial(final String materialName) {
        this.materialName = materialName;
    }

    public ModernMaterial setColor(Color aColor) {
        color = aColor;
        return this;
    }

    public ModernMaterial setColor(int aRed, int aGreen, int aBlue) {
        color = new Color(aRed, aGreen, aBlue);
        return this;
    }

    public ModernMaterial setMaterialTimeMultiplier(double materialTimeMultiplier) {
        this.materialTimeMultiplier = materialTimeMultiplier;
        return this;
    }

    public ModernMaterial addParts(final PartsEnum... parts) {
        for (PartsEnum part : parts) {
            part.addAssociatedMaterial(this);
            addPart(part);
        }
        return this;
    }
//
//    public ModernMaterial addPartsCustom(final CustomPartInfo... customParts) {
//        for (CustomPartInfo customPartInfo : customParts) {
//            existingPartsForMaterial.put(customPartInfo.part, customPartInfo);
//        }
//        return this;
//    }

    public ModernMaterial addFluids(final FluidEnum... fluids) {

        for (FluidEnum fluidEnum : fluids) {
            ModernMaterialFluid modernMaterialFluid = new ModernMaterialFluid(fluidEnum, this);
            modernMaterialFluid.setTemperature(fluidEnum.getTemperature());
            modernMaterialFluid.setGaseous(fluidEnum.isGas());

            existingFluids.add(new ModernMaterialFluid(fluidEnum, this));
        }

        return this;
    }

    public ModernMaterial addCustomFluid(ModernMaterialFluid modernMaterialFluid) {
        existingFluids.add(modernMaterialFluid);
        return this;
    }

    public void build() {
        registerMaterial(this);
    }

    public void setMaterialID(int aID) {
        materialID = aID;
    }

    public Color getColor() {
        return color;
    }

    public int getMaterialID() {
        return materialID;
    }

    public String getMaterialName() {
        return materialName;
    }

    public double getMaterialTimeMultiplier() {
        return materialTimeMultiplier;
    }

    public long getMaterialTier() {
        return materialTier;
    }

    public ModernMaterial addPart(final PartsEnum part) {
        existingPartsForMaterial.put(part, new CustomPartInfo(part, textureType));
        return this;
    }

    TextureType textureType = Metallic;

    public ModernMaterial setTextureMode(@NotNull final TextureType textureType) {
        this.textureType = textureType;
        return this;
    }

    // This will override all existing parts settings and enable ALL possible parts. Be careful!
    public ModernMaterial addAllParts() {
        addParts(PartsEnum.values());
        return this;
    }

    public ModernMaterial setMaterialTier(final long tier) {
        this.materialTier = tier;
        return this;
    }

    public ModernMaterial addPart(final CustomPartInfo customPartInfo) {
        existingPartsForMaterial.put(customPartInfo.part, customPartInfo);
        return this;
    }

    public CustomPartInfo getCustomPartInfo(final PartsEnum part) {
        return existingPartsForMaterial.get(part);
    }


}
