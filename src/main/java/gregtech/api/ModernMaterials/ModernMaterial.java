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
            materialToBuild.color = new Color(red, green, blue);
            return this;
        }

        public Builder setMaterialTimeMultiplier(double materialTimeMultiplier) {
            materialToBuild.materialTimeMultiplier = materialTimeMultiplier;
            return this;
        }

        public Builder addParts(PartsEnum... parts) {
            for (PartsEnum part : parts) {
                part.addAssociatedMaterial(materialToBuild);
                addPart(part);
            }
            return this;
        }

        public Builder addPart(PartsEnum part) {
            materialToBuild.existingPartsForMaterial.put(part, new CustomPartInfo(part, materialToBuild.textureType));
            return this;
        }

        // This will override all existing parts settings and enable ALL possible parts. Be careful!
        public Builder addAllParts() {
            addParts(PartsEnum.values());
            return this;
        }

        public Builder addFluid(FluidEnum fluidEnum, int temperature) {

            ModernMaterialFluid modernMaterialFluid = new ModernMaterialFluid(fluidEnum, materialToBuild);
            modernMaterialFluid.setTemperature(temperature);
            modernMaterialFluid.setGaseous(fluidEnum.isGas());
            // Determines fluid texture.
            modernMaterialFluid.setFluidEnum(fluidEnum);

            // Add fluid to list in material.
            materialToBuild.existingFluids.add(new ModernMaterialFluid(fluidEnum, materialToBuild));

            return this;
        }

        public Builder addCustomFluid(ModernMaterialFluid.Builder modernMaterialFluidBuilder) {
            ModernMaterialFluid modernMaterialFluid = modernMaterialFluidBuilder.setMaterial(materialToBuild).build();
            materialToBuild.existingFluids.add(modernMaterialFluid);
            return this;
        }

        public Builder setMaterialTier(long tier) {
            materialToBuild.materialTier = tier;
            return this;
        }

        public Builder addPart(CustomPartInfo customPartInfo) {
            materialToBuild.existingPartsForMaterial.put(customPartInfo.part, customPartInfo);
            return this;
        }

        public Builder setTextureMode(@NotNull final TextureType textureType) {
            materialToBuild.textureType = textureType;
            return this;
        }

    }

}
