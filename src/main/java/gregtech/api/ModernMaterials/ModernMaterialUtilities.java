package gregtech.api.ModernMaterials;

import static gregtech.api.enums.ConfigCategories.ModernMaterials.*;
import static gregtech.api.enums.GT_Values.RES_PATH_BLOCK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.DumbItemBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.DumbTileEntity;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxBlock;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxItemBlock;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.Fluids.FluidEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.PartProperties.Rendering.ModernMaterialItemRenderer;
import gregtech.api.ModernMaterials.PartRecipeGenerators.ModernMaterialsPlateRecipeGenerator;
import gregtech.api.ModernMaterials.PartsClasses.MaterialPart;
import gregtech.api.ModernMaterials.PartsClasses.PartsEnum;

public class ModernMaterialUtilities {

    private static final List<ModernMaterial> mNewMaterials = new ArrayList<>();
    public static final HashMap<Integer, ModernMaterial> materialIDToMaterial = new HashMap<>();
    public static final HashMap<String, ModernMaterial> materialNameToMaterialMap = new HashMap<>();
    public static final HashMap<PartsEnum, MaterialPart> materialPartItemMap = new HashMap<>();

    public static void registerMaterial(ModernMaterial aMaterial) {
        final int tCurrentMaterialID = GregTech_API.sModernMaterialIDs.mConfig
                .get(materialID.name(), aMaterial.getName(), -1).getInt();
        if (tCurrentMaterialID == -1) {
            mNewMaterials.add(aMaterial);
        } else {
            aMaterial.setMaterialID(tCurrentMaterialID);
            materialIDToMaterial.put(tCurrentMaterialID, aMaterial);
            if (tCurrentMaterialID > GregTech_API.mLastMaterialID) {
                GregTech_API.mLastMaterialID = tCurrentMaterialID;
            }
        }
        materialNameToMaterialMap.put(aMaterial.getName(), aMaterial);
    }

    public static void registerAllMaterialsItems() {
        for (ModernMaterial tMaterial : mNewMaterials) {
            tMaterial.setMaterialID(++GregTech_API.mLastMaterialID);
            GregTech_API.sModernMaterialIDs.mConfig.get(materialID.name(), tMaterial.getName(), 0)
                    .set(GregTech_API.mLastMaterialID);
            materialIDToMaterial.put(GregTech_API.mLastMaterialID, tMaterial);
        }

        for (PartsEnum tPart : PartsEnum.values()) {
            MaterialPart materialPart = new MaterialPart(tPart);
            materialPart.setUnlocalizedName(tPart.partName);

            // Registers the item with the game, only available in preInit.
            GameRegistry.registerItem(materialPart, tPart.partName);

            // Store the Item so these parts can be retrieved later.
            materialPartItemMap.put(tPart, materialPart);

            // Registers the renderer which allows for part colouring.
            MinecraftForgeClient.registerItemRenderer(materialPart, new ModernMaterialItemRenderer());
        }

        // Register all material parts.
        for (ModernMaterial material : materialIDToMaterial.values()) {
            registerAllMaterialPartRecipes(material);
        }

    }

    public static void registerAllMaterialsFluids() {

        // todo fix
        // Register the icons for the ModernMaterial fluids.
        TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
        final String defaultPath = RES_PATH_BLOCK + "ModernMaterialsIcons/Fluids/";
        for (FluidEnum fluidEnum : FluidEnum.values()) {
            fluidEnum.stillIcon = null;
            fluidEnum.flowingIcon = null;
        }

        // Register the fluids with forge.
        for (ModernMaterial material : materialIDToMaterial.values()) {
            for (ModernMaterialFluid fluid : material.existingFluids) {
                FluidRegistry.registerFluid(fluid);
            }
        }

        BlocksEnum.FrameBox.associatedMaterials.addAll(materialIDToMaterial.values());
//        (new FrameBoxBlock()).registerBlock(FrameBoxTileEntity.class, FrameBoxItemBlock.class);
        FrameBoxBlock block = new FrameBoxBlock();
        GameRegistry.registerBlock(block, DumbItemBlock.class, BlocksEnum.FrameBox.name());
        GameRegistry.registerTileEntity(FrameBoxTileEntity.class, BlocksEnum.FrameBox.name());

    }

    private static void registerAllMaterialPartRecipes(ModernMaterial material) {
        new ModernMaterialsPlateRecipeGenerator().run(material);
    }

    public static ItemStack getPart(final ModernMaterial material, final PartsEnum part, final int stackSize) {

        if (!material.doesPartExist(part)) {
            throw new RuntimeException(
                    "Registered material " + material.getName() + " does not have a " + part.toString() + " part.");
        }

        return new ItemStack(materialPartItemMap.get(part), stackSize, material.getMaterialID());
    }

    public static ItemStack getPart(final String materialName, final PartsEnum part, final int stackSize) {
        return getPart(getMaterialFromName(materialName), part, stackSize);
    }

    public static ModernMaterial getMaterialFromName(final String materialName) {
        return materialNameToMaterialMap.get(materialName);
    }
}
