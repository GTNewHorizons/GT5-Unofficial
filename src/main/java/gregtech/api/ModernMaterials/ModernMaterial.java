package gregtech.api.ModernMaterials;

import gregtech.api.ModernMaterials.Fluids.FluidEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.PartProperties.Textures.TextureType;
import gregtech.api.ModernMaterials.PartsClasses.CustomPartInfo;
import gregtech.api.ModernMaterials.PartsClasses.PartsEnum;
import net.minecraftforge.fluids.Fluid;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerMaterial;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Metallic;

@SuppressWarnings("unused")
public final class ModernMaterial {

    private final HashMap<PartsEnum, CustomPartInfo> existingPartsForMaterial = new HashMap<>();
    public final ArrayList<ModernMaterialFluid> existingFluids = new ArrayList<>();
    private Color color;
    private int materialID;
    private String materialName;
    private long materialTier;
    private double materialTimeMultiplier;
    private TextureType textureType = Metallic;

    public ModernMaterial(final String materialName) {
        this.materialName = materialName;
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

    public CustomPartInfo getCustomPartInfo(final PartsEnum part) {
        return existingPartsForMaterial.get(part);
    }

    private ModernMaterial() {}

    public static class Builder {

        public static ModernMaterial materialToBuild;

        public Builder(String materialName) {
            materialToBuild = new ModernMaterial(materialName);
        }

        ModernMaterial build() {
            ModernMaterial builtMaterial = materialToBuild;
            materialToBuild = new ModernMaterial();

            registerMaterial(builtMaterial);
            return builtMaterial;
        }

        public Builder setColor(int red, int green, int blue) {
            this.materialToBuild.color = new Color(red, green, blue);
            return this;
        }

        public Builder setMaterialTimeMultiplier(double materialTimeMultiplier) {
            this.materialToBuild.materialTimeMultiplier = materialTimeMultiplier;
            return this;
        }

        public Builder addParts(PartsEnum... parts) {
            for (PartsEnum part : parts) {
                part.addAssociatedMaterial(this.materialToBuild);
                addPart(part);
            }
            return this;
        }

        public Builder addPart(PartsEnum part) {
            this.materialToBuild.existingPartsForMaterial.put(part, new CustomPartInfo(part, this.materialToBuild.textureType));
            return this;
        }

        // This will override all existing parts settings and enable ALL possible parts. Be careful!
        public Builder addAllParts() {
            addParts(PartsEnum.values());
            return this;
        }

        public Builder addFluids(FluidEnum... fluids) {

            for (FluidEnum fluidEnum : fluids) {
                ModernMaterialFluid modernMaterialFluid = new ModernMaterialFluid(fluidEnum, this.materialToBuild);
                modernMaterialFluid.setTemperature(fluidEnum.getTemperature());
                modernMaterialFluid.setGaseous(fluidEnum.isGas());

                this.materialToBuild.existingFluids.add(new ModernMaterialFluid(fluidEnum, this.materialToBuild));
            }

            return this;
        }

        public Builder addCustomFluid(ModernMaterialFluid modernMaterialFluid) {
            this.materialToBuild.existingFluids.add(modernMaterialFluid);
            return this;
        }

        public Builder setMaterialTier(long tier) {
            this.materialToBuild.materialTier = tier;
            return this;
        }

        public Builder addPart(CustomPartInfo customPartInfo) {
            this.materialToBuild.existingPartsForMaterial.put(customPartInfo.part, customPartInfo);
            return this;
        }

        public Builder setTextureMode(@NotNull final TextureType textureType) {
            this.materialToBuild.textureType = textureType;
            return this;
        }

    }


}
