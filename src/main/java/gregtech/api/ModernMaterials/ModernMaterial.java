package gregtech.api.ModernMaterials;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialBlock;
import gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum;
import gregtech.api.ModernMaterials.Fluids.FluidEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.Items.PartProperties.TextureType;
import gregtech.api.ModernMaterials.Items.PartsClasses.IEnumPart;
import gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum;
import gregtech.api.ModernMaterials.Items.PartsClasses.MaterialPart;

@SuppressWarnings("unused")
public final class ModernMaterial {

    private final HashSet<IEnumPart> existingPartsForMaterial = new HashSet<>();
    private final HashSet<ModernMaterialFluid> existingFluids = new HashSet<>();
    private static final HashSet<ModernMaterial> allMaterials = new HashSet<>();

    private static final HashMap<Integer, ModernMaterial> materialIDToMaterial = new HashMap<>();
    private static final HashMap<String, ModernMaterial> materialNameToMaterialMap = new HashMap<>();

    private Color color;
    private int materialID;
    private String materialName;
    private long materialTier;
    private double materialTimeMultiplier = 1;
    private TextureType textureType;
    private Consumer<ModernMaterial> recipeGenerator;
    private IItemRenderer customItemRenderer;

    public ModernMaterial(final String materialName) {
        this.materialName = materialName;
    }

    public static ModernMaterial getMaterialFromItemStack(@NotNull ItemStack itemStack) {

        if (itemStack.getItem() instanceof MaterialPart) {
            return materialIDToMaterial.get(itemStack.getItemDamage());
        } else if (Block.getBlockFromItem(itemStack.getItem()) instanceof BaseMaterialBlock baseMaterialBlock) {
            int ID = baseMaterialBlock.getMaterialID(itemStack.getItemDamage());

            return materialIDToMaterial.get(ID);
        }

        throw new IllegalArgumentException("ItemStack " + itemStack + " is not a material part or block.");
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

    public static Set<ModernMaterial> allMaterials() {
        return Collections.unmodifiableSet(allMaterials);
    }

    public boolean doesPartExist(BlocksEnum blocksEnum) {
        return blocksEnum.getAssociatedMaterials()
            .contains(this);
    }

    public double getMaterialTimeMultiplier() {
        return materialTimeMultiplier;
    }

    public Set<ModernMaterialFluid> getAssociatedFluids() {
        return Collections.unmodifiableSet(existingFluids);
    }

    public long getMaterialTier() {
        return materialTier;
    }

    public TextureType getTextureType() {
        return textureType;
    }

    private ModernMaterial() {}

    public void registerRecipes(ModernMaterial material) {
        if (recipeGenerator == null) return;
        recipeGenerator.accept(material);
    }

    public IItemRenderer getCustomItemRenderer() {
        return customItemRenderer;
    }

    public boolean hasCustomTextures() {
        return textureType == TextureType.Custom;
    }

    public static class ModernMaterialBuilder {

        public static ModernMaterial materialToBuild;

        public ModernMaterialBuilder(String materialName) {
            materialToBuild = new ModernMaterial(materialName);
        }

        void build() {
            ModernMaterial builtMaterial = materialToBuild;
            materialToBuild = new ModernMaterial();

            // Complete set of all materials.
            allMaterials.add(builtMaterial);

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

        public ModernMaterialBuilder addParts(IEnumPart... parts) {
            for (IEnumPart part : parts) {
                addPart(part);
            }

            return this;
        }

        public ModernMaterialBuilder addPart(IEnumPart part) {
            part.addAssociatedMaterial(materialToBuild);
            materialToBuild.existingPartsForMaterial.add(part);

            return this;
        }

        private final HashMap<BlocksEnum, TileEntitySpecialRenderer> TESRMap = new HashMap<>();

        public ModernMaterialBuilder addBlockTESR(BlocksEnum block, TileEntitySpecialRenderer TESR) {
            TESRMap.put(block, TESR);
            return this;
        }

        // This will override all existing parts settings and enable ALL possible parts and blocks. Be careful!
        public ModernMaterialBuilder addAllParts() {
            addParts(ItemsEnum.values());
            addParts(BlocksEnum.values());
            return this;
        }

        public ModernMaterialBuilder addFluid(FluidEnum fluidEnum, int temperature) {

            ModernMaterialFluid modernMaterialFluid = new ModernMaterialFluid(fluidEnum, materialToBuild);
            modernMaterialFluid.setTemperature(temperature);
            modernMaterialFluid.setGaseous(fluidEnum.isGas());
            modernMaterialFluid.setFluidEnum(fluidEnum);

            // Add fluid to list in material.
            materialToBuild.existingFluids.add(new ModernMaterialFluid(fluidEnum, materialToBuild));

            return this;
        }

        public ModernMaterialBuilder addCustomFluid(ModernMaterialFluid.Builder modernMaterialFluidBuilder,
            boolean useMaterialColouringForFluid) {

            ModernMaterialFluid modernMaterialFluid = modernMaterialFluidBuilder.setMaterial(materialToBuild)
                .build();
            materialToBuild.existingFluids.add(modernMaterialFluid);

            if (!useMaterialColouringForFluid) {
                modernMaterialFluid.disableFluidColouring();
            }

            return this;
        }

        public ModernMaterialBuilder setMaterialTier(long tier) {
            materialToBuild.materialTier = tier;
            return this;
        }

        public ModernMaterialBuilder setTextureMode(@NotNull final TextureType textureType) {
            if (textureType == TextureType.Custom) TextureType.registerCustomMaterial(materialToBuild);
            materialToBuild.textureType = textureType;
            return this;
        }

        public ModernMaterialBuilder setRecipeGenerator(@NotNull final Consumer<ModernMaterial> recipeGenerator) {
            materialToBuild.recipeGenerator = recipeGenerator;
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
            blocksEnum.setItemRenderer(materialToBuild.materialID, itemRenderer);
            blocksEnum.setBlockRenderer(materialToBuild.materialID, tileEntitySpecialRenderer);
            return this;
        }

        public ModernMaterialBuilder setCustomItemRenderer(IItemRenderer customItemRenderer) {
            materialToBuild.customItemRenderer = customItemRenderer;
            return this;
        }
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ModernMaterial material) {
            return material.materialID == this.materialID;
        }

        return false;
    }

    @Override
    public String toString() {
        return materialName;
    }

    public static Map<String, ModernMaterial> getMaterialNameToMaterialMap() {
        return Collections.unmodifiableMap(materialNameToMaterialMap);
    }

    public static Map<Integer, ModernMaterial> getMaterialIDToMaterialMap() {
        return Collections.unmodifiableMap(materialIDToMaterial);
    }

    public static void registerMaterial(ModernMaterial material) {
        if (materialIDToMaterial.containsKey(material.getMaterialID())) {
            throw new IllegalArgumentException("Material with ID " + material.getMaterialID() + " already exists.");
        }

        if (materialNameToMaterialMap.containsKey(material.getMaterialName())) {
            throw new IllegalArgumentException(
                "Material with name " + material.getMaterialName()
                    + " already exists. Material was registered with ID "
                    + material.getMaterialID()
                    + ".");
        }

        materialIDToMaterial.put(material.getMaterialID(), material);
        materialNameToMaterialMap.put(material.getMaterialName(), material);
    }

    public static ModernMaterial getMaterialFromID(final int materialID) {

        ModernMaterial modernMaterial = materialIDToMaterial.getOrDefault(materialID, null);

        if (modernMaterial == null) {
            throw new IllegalArgumentException("Material with ID " + materialID + " does not exist.");
        }

        return modernMaterial;
    }
}
