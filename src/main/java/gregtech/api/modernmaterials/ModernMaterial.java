package gregtech.api.modernmaterials;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.jetbrains.annotations.NotNull;

import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import gregtech.api.modernmaterials.fluids.FluidEnum;
import gregtech.api.modernmaterials.fluids.ModernMaterialFluid;
import gregtech.api.modernmaterials.items.partproperties.TextureType;
import gregtech.api.modernmaterials.items.partclasses.IEnumPart;
import gregtech.api.modernmaterials.items.partclasses.ItemsEnum;

@SuppressWarnings("unused")
public final class ModernMaterial {

    private final HashSet<IEnumPart> existingPartsForMaterial = new HashSet<>();
    private final HashSet<ModernMaterialFluid> existingFluids = new HashSet<>();
    private final HashSet<Consumer<ModernMaterial>> recipeGenerators = new HashSet<>();
    private static final HashSet<ModernMaterial> allMaterials = new HashSet<>();

    private static final HashMap<Integer, ModernMaterial> materialIDToMaterial = new HashMap<>();
    private static final HashMap<String, ModernMaterial> materialNameToMaterialMap = new HashMap<>();

    private Color color;
    private int materialID = -1;
    private String materialName;
    private long materialTier;
    private double hardness = 1;
    private TextureType textureType;
    private IItemRenderer customItemRenderer;

    public ModernMaterial(final String materialName) {
        this.materialName = materialName;
    }

    public static ModernMaterial getMaterialFromItemStack(@NotNull ItemStack itemStack) {
        return materialIDToMaterial.get(itemStack.getItemDamage());
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

    public static Set<ModernMaterial> getAllMaterials() {
        return Collections.unmodifiableSet(allMaterials);
    }

    public static Map<String, ModernMaterial> getMaterialNameToMaterialMap() {
        return Collections.unmodifiableMap(materialNameToMaterialMap);
    }

    public static Map<Integer, ModernMaterial> getMaterialIDToMaterialMap() {
        return Collections.unmodifiableMap(materialIDToMaterial);
    }

    public double getHardness() {
        return hardness;
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
        for (Consumer<ModernMaterial> recipeGenerator : recipeGenerators) {
            recipeGenerator.accept(material);
        }
    }

    public IItemRenderer getCustomItemRenderer() {
        return customItemRenderer;
    }

    public boolean hasCustomTextures() {
        return textureType == TextureType.Custom;
    }

    public static ModernMaterial getMaterialFromID(final int materialID) {
        return materialIDToMaterial.get(materialID);
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

    private static void safeguardChecks(ModernMaterial material) {

        if (material.color == null) {
            throw new IllegalArgumentException("Material " + material + " has no colour set.");
        }

        if (material.materialID == -1) {
            throw new IllegalArgumentException("Material " + material + " has no ID set.");
        }

        if (material.materialName == null) {
            throw new IllegalArgumentException("Material " + material + " has no name set.");
        }

        if (material.textureType == null) {
            throw new IllegalArgumentException("Material " + material + " has no texture type set.");
        }

        if (material.materialTier == 0 && !material.recipeGenerators.isEmpty()) {
            throw new IllegalArgumentException(
                "Material " + material + " has no tier set for its recipe generator(s).");
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModernMaterial that) {
            return that.materialID == this.materialID;
        }

        return false;
    }

    @Override
    public String toString() {
        return materialName;
    }

    public static class ModernMaterialBuilder {

        public ModernMaterial materialToBuild;

        public ModernMaterialBuilder(String materialName) {
            materialToBuild = new ModernMaterial(materialName);
        }

        void build() {
            ModernMaterial builtMaterial = materialToBuild;

            for (IEnumPart part : materialToBuild.existingPartsForMaterial) {
                part.addAssociatedMaterial(materialToBuild);
            }

            // Complete set of all materials.
            allMaterials.add(builtMaterial);

            materialToBuild = new ModernMaterial();

            safeguardChecks(builtMaterial);
            registerMaterial(builtMaterial);
        }

        public ModernMaterialBuilder setColor(int red, int green, int blue) {
            materialToBuild.color = new Color(red, green, blue);
            return this;
        }

        public ModernMaterialBuilder setMaterialTimeMultiplier(double materialTimeMultiplier) {
            materialToBuild.hardness = materialTimeMultiplier;
            return this;
        }

        // ----------------- Add and remove parts from material -----------------

        public ModernMaterialBuilder addPart(IEnumPart... parts) {
            materialToBuild.existingPartsForMaterial.addAll(Arrays.asList(parts));
            return this;
        }

        public ModernMaterialBuilder removePart(IEnumPart... parts) {
            for (IEnumPart part : parts) {
                materialToBuild.existingPartsForMaterial.remove(part);
            }
            return this;
        }

        // ----------------------------------------------------------------------

        // This will override all existing parts settings and enable ALL possible parts and blocks. Be careful!
        public ModernMaterialBuilder addAllParts() {
            addPart(ItemsEnum.values());
            addPart(BlocksEnum.values());
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

        @SafeVarargs
        public final ModernMaterialBuilder setRecipeGenerator(
            @NotNull final Consumer<ModernMaterial>... recipeGenerators) {
            materialToBuild.recipeGenerators.addAll(Arrays.asList(recipeGenerators));
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

            if (materialToBuild.materialID == -1) throw new RuntimeException("Material ID for " + materialToBuild + " must be set before you can call setCustomBlockRenderer.");

            blocksEnum.setItemRenderer(materialToBuild.materialID, itemRenderer);
            blocksEnum.setBlockRenderer(materialToBuild.materialID, tileEntitySpecialRenderer);
            return this;
        }

        public ModernMaterialBuilder setCustomItemRenderer(@NotNull IItemRenderer customItemRenderer) {
            materialToBuild.customItemRenderer = customItemRenderer;
            return this;
        }

        public ModernMaterialBuilder setColor(@NotNull Color color) {
            materialToBuild.color = color;
            return this;
        }
    }

}
