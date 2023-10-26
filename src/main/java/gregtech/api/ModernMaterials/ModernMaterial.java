package gregtech.api.ModernMaterials;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerMaterial;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Metallic;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.client.IItemRenderer;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum;
import gregtech.api.ModernMaterials.Fluids.FluidEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.PartProperties.Textures.TextureType;
import gregtech.api.ModernMaterials.PartsClasses.CustomPartInfo;
import gregtech.api.ModernMaterials.PartsClasses.ItemsEnum;

@SuppressWarnings("unused")
public final class ModernMaterial {

    private final HashMap<ItemsEnum, CustomPartInfo> existingPartsForMaterial = new HashMap<>();
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

    public boolean doesPartExist(BlocksEnum blocksEnum) {
        return blocksEnum.getAssociatedMaterials()
            .contains(this);
    }

    public double getMaterialTimeMultiplier() {
        return materialTimeMultiplier;
    }

    public long getMaterialTier() {
        return materialTier;
    }

    public CustomPartInfo getCustomPartInfo(final ItemsEnum part) {
        return existingPartsForMaterial.get(part);
    }

    private ModernMaterial() {}

    public static class ModernMaterialBuilder {

        public static ModernMaterial materialToBuild;

        public ModernMaterialBuilder(String materialName) {
            materialToBuild = new ModernMaterial(materialName);
        }

        void build() {
            ModernMaterial builtMaterial = materialToBuild;
            materialToBuild = new ModernMaterial();

            registerMaterial(builtMaterial);
        }

        public ModernMaterialBuilder setColor(int red, int green, int blue) {
            materialToBuild.color = new Color(red, green, blue);
            return this;
        }

        public ModernMaterialBuilder setMaterialTimeMultiplier(double materialTimeMultiplier) {
            materialToBuild.materialTimeMultiplier = materialTimeMultiplier;
            return this;
        }

        public ModernMaterialBuilder addParts(ItemsEnum... parts) {
            for (ItemsEnum part : parts) {
                addPart(part);
            }

            return this;
        }

        public ModernMaterialBuilder addPart(ItemsEnum part) {
            part.addAssociatedMaterial(materialToBuild);
            materialToBuild.existingPartsForMaterial.put(part, new CustomPartInfo(part, materialToBuild.textureType));

            return this;
        }

        private final HashMap<BlocksEnum, TileEntitySpecialRenderer> TESRMap = new HashMap<>();

        public ModernMaterialBuilder addBlockTESR(BlocksEnum block, TileEntitySpecialRenderer TESR) {
            TESRMap.put(block, TESR);
            return this;
        }

        // This will override all existing parts settings and enable ALL possible parts. Be careful!
        public ModernMaterialBuilder addAllParts() {
            addParts(ItemsEnum.values());
            return this;
        }

        public ModernMaterialBuilder addFluid(FluidEnum fluidEnum, int temperature) {

            ModernMaterialFluid modernMaterialFluid = new ModernMaterialFluid(fluidEnum, materialToBuild);
            modernMaterialFluid.setTemperature(temperature);
            modernMaterialFluid.setGaseous(fluidEnum.isGas());
            // Determines fluid texture.
            modernMaterialFluid.setFluidEnum(fluidEnum);
            // Add fluid to list in material.
            materialToBuild.existingFluids.add(new ModernMaterialFluid(fluidEnum, materialToBuild));

            return this;
        }

        public ModernMaterialBuilder addCustomFluid(ModernMaterialFluid.Builder modernMaterialFluidBuilder) {
            ModernMaterialFluid modernMaterialFluid = modernMaterialFluidBuilder.setMaterial(materialToBuild)
                .build();
            materialToBuild.existingFluids.add(modernMaterialFluid);
            return this;
        }

        public ModernMaterialBuilder setMaterialTier(long tier) {
            materialToBuild.materialTier = tier;
            return this;
        }

        public ModernMaterialBuilder addPart(CustomPartInfo customPartInfo) {
            materialToBuild.existingPartsForMaterial.put(customPartInfo.part, customPartInfo);
            return this;
        }

        public ModernMaterialBuilder setTextureMode(@NotNull final TextureType textureType) {
            materialToBuild.textureType = textureType;
            return this;
        }

        public ModernMaterialBuilder setMaterialID(int materialID) {
            materialToBuild.materialID = materialID;
            return this;
        }

        public ModernMaterialBuilder setCustomBlockRenderer(@NotNull final BlocksEnum blocksEnum,
            @NotNull final IItemRenderer itemRenderer,
            @NotNull final TileEntitySpecialRenderer tileEntitySpecialRenderer) {
            blocksEnum.addSpecialBlockRenderAssociatedMaterial(materialToBuild);
            blocksEnum.setAssociatedItem(blocksEnum.getItem());
            blocksEnum.setItemRenderer(materialToBuild.materialID, itemRenderer);
            blocksEnum.setBlockRenderer(materialToBuild.materialID, tileEntitySpecialRenderer);
            return this;
        }

    }

}
