package gregtech.api.ModernMaterials;

import static gregtech.api.ModernMaterials.Blocks.Registration.SimpleBlockRegistration.registerSimpleBlock;
import static gregtech.api.ModernMaterials.Blocks.Registration.SpecialBlockRegistration.registerTESRBlock;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialItemBlock;
import gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.PartProperties.Rendering.ModernMaterialItemRenderer;
import gregtech.api.ModernMaterials.PartRecipeGenerators.ModernMaterialsPlateRecipeGenerator;
import gregtech.api.ModernMaterials.PartsClasses.IEnumPart;
import gregtech.api.ModernMaterials.PartsClasses.ItemsEnum;
import gregtech.api.ModernMaterials.PartsClasses.MaterialPart;

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

}
