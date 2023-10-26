package gregtech.api.ModernMaterials;

import static gregtech.api.ModernMaterials.Blocks.Registration.SimpleBlockRegistration.registerSimpleBlock;
import static gregtech.api.ModernMaterials.Blocks.Registration.SpecialBlockRegistration.registerTESRBlock;

import java.util.ArrayList;
import java.util.HashMap;

import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialItemBlock;
import gregtech.api.ModernMaterials.PartsClasses.IEnumPart;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.PartProperties.Rendering.ModernMaterialItemRenderer;
import gregtech.api.ModernMaterials.PartRecipeGenerators.ModernMaterialsPlateRecipeGenerator;
import gregtech.api.ModernMaterials.PartsClasses.MaterialPart;
import gregtech.api.ModernMaterials.PartsClasses.ItemsEnum;

public class ModernMaterialUtilities {

    public static final HashMap<Integer, ModernMaterial> materialIDToMaterial = new HashMap<>();
    public static final HashMap<String, ModernMaterial> materialNameToMaterialMap = new HashMap<>();

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

    public static void registerAllMaterialsItems() {

        for (ItemsEnum part : ItemsEnum.values()) {

            MaterialPart materialPart = new MaterialPart(part);
            materialPart.setUnlocalizedName(part.partName);

            // Registers the item with the game, only available in preInit.
            GameRegistry.registerItem(materialPart, part.partName);

            // Store the Item so these parts can be retrieved later for recipe generation etc.
            part.setAssociatedItem(materialPart);

            // Registers the renderer which allows for part colouring.
            MinecraftForgeClient.registerItemRenderer(materialPart, new ModernMaterialItemRenderer());
        }

        // Register all material parts.
        for (ModernMaterial material : materialIDToMaterial.values()) {
            registerAllMaterialPartRecipes(material);
        }

    }

    public static void registerAllMaterialsBlocks() {
        BlocksEnum.FrameBox.getAssociatedMaterials()
            .addAll(materialIDToMaterial.values());
//        BlocksEnum.EarthOreNormal.getAssociatedMaterials()
//            .addAll(materialIDToMaterial.values());

        for (BlocksEnum blockType : BlocksEnum.values()) {
            registerSimpleBlock(blockType);
            registerTESRBlock(blockType);
        }

    }

    public static void registerAllMaterialsFluids() {

        // Register the fluids with forge.
        for (ModernMaterial material : materialIDToMaterial.values()) {
            for (ModernMaterialFluid fluid : material.existingFluids) {
                FluidRegistry.registerFluid(fluid);
            }
        }
    }

    private static void registerAllMaterialPartRecipes(ModernMaterial material) {
        new ModernMaterialsPlateRecipeGenerator().run(material);
    }

    public static ItemStack getPart(final ModernMaterial material, final IEnumPart part, final int stackSize) {
        return new ItemStack(part.getItem(), stackSize, material.getMaterialID());
    }

    public static ItemStack getPart(final String materialName, final IEnumPart part, final int stackSize) {
        return getPart(getMaterialFromName(materialName), part, stackSize);
    }

    public static ModernMaterial getMaterialFromName(final String materialName) {

        ModernMaterial modernMaterial = materialNameToMaterialMap.getOrDefault(materialName, null);

        if (modernMaterial == null) {
            throw new IllegalArgumentException(
                "Material % does not exist. Make sure you spelt it correctly.".replace("%", materialName));
        }

        return modernMaterial;
    }

    public static ModernMaterial getMaterialFromID(final int materialID) {

        ModernMaterial modernMaterial = materialIDToMaterial.getOrDefault(materialID, null);

        if (modernMaterial == null) {
            throw new IllegalArgumentException("Material with ID " + materialID + " does not exist.");
        }

        return modernMaterial;
    }

    public static ArrayList<String> tooltipGenerator(Item part, ModernMaterial material) {
        // Todo, this is just temporary as a proof of concept/debug info.
        // Probably will put radioactive warning here. Not sure what else yet, if any.

        ArrayList<String> tooltip = new ArrayList<>();

        tooltip.add("Generic Tooltip");
        tooltip.add("Material Name: " + material.getMaterialName());

        if (part instanceof BaseMaterialItemBlock blockPart) {
            tooltip.add("Material Part Type: " + blockPart.getPart());
        } else if (part instanceof MaterialPart itemPart) {
            tooltip.add("Material Part Type: " + itemPart.getPart());
        }

        return tooltip;
    }

    public static void drawBlock(Block block, int meta, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));

        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));

        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));

        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));

        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));

    }

    public static void renderBlock(IIcon texture) {
        Tessellator tes = Tessellator.instance;

        double x = 0;
        double y = 0;
        double z = 0;

        double[] X = { x - 0.5, x - 0.5, x + 0.5, x + 0.5, x + 0.5, x + 0.5, x - 0.5, x - 0.5 };
        double[] Y = { y + 0.5, y - 0.5, y - 0.5, y + 0.5, y + 0.5, y - 0.5, y - 0.5, y + 0.5 };
        double[] Z = { z + 0.5, z + 0.5, z + 0.5, z + 0.5, z - 0.5, z - 0.5, z - 0.5, z - 0.5 };

        tes.startDrawingQuads();

        double minU;
        double maxU;
        double minV;
        double maxV;

        {
            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[1], Y[1], Z[1], maxU, maxV);
            tes.addVertexWithUV(X[0], Y[0], Z[0], maxU, minV);
            tes.addVertexWithUV(X[7], Y[7], Z[7], minU, minV);
            tes.addVertexWithUV(X[6], Y[6], Z[6], minU, maxV);
        }

        {
            // Bottom face.

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[1], Y[1], Z[1], minU, maxV);
            tes.addVertexWithUV(X[6], Y[6], Z[6], minU, minV);
            tes.addVertexWithUV(X[5], Y[5], Z[5], maxU, minV);
            tes.addVertexWithUV(X[2], Y[2], Z[2], maxU, maxV);
        }

        {

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[6], Y[6], Z[6], maxU, maxV);
            tes.addVertexWithUV(X[7], Y[7], Z[7], maxU, minV);
            tes.addVertexWithUV(X[4], Y[4], Z[4], minU, minV);
            tes.addVertexWithUV(X[5], Y[5], Z[5], minU, maxV);
        }

        {

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[5], Y[5], Z[5], maxU, maxV);
            tes.addVertexWithUV(X[4], Y[4], Z[4], maxU, minV);
            tes.addVertexWithUV(X[3], Y[3], Z[3], minU, minV);
            tes.addVertexWithUV(X[2], Y[2], Z[2], minU, maxV);
        }

        {

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[3], Y[3], Z[3], maxU, maxV);
            tes.addVertexWithUV(X[4], Y[4], Z[4], maxU, minV);
            tes.addVertexWithUV(X[7], Y[7], Z[7], minU, minV);
            tes.addVertexWithUV(X[0], Y[0], Z[0], minU, maxV);
        }

        {

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[2], Y[2], Z[2], maxU, maxV);
            tes.addVertexWithUV(X[3], Y[3], Z[3], maxU, minV);
            tes.addVertexWithUV(X[0], Y[0], Z[0], minU, minV);
            tes.addVertexWithUV(X[1], Y[1], Z[1], minU, maxV);
        }

        tes.draw();

        // ----------------------------------------------
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

}
