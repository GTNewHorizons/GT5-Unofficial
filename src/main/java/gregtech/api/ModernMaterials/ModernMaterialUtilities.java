package gregtech.api.ModernMaterials;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxBlock;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxItemBlock;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxTileEntity;
import gregtech.api.ModernMaterials.Blocks.FrameBox2.FrameBoxBlock2;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.PartProperties.Rendering.ModernMaterialItemRenderer;
import gregtech.api.ModernMaterials.PartRecipeGenerators.ModernMaterialsPlateRecipeGenerator;
import gregtech.api.ModernMaterials.PartsClasses.IGetItem;
import gregtech.api.ModernMaterials.PartsClasses.MaterialPart;
import gregtech.api.ModernMaterials.PartsClasses.PartsEnum;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static gregtech.api.enums.ConfigCategories.ModernMaterials.materialID;
import static gregtech.api.enums.GT_Values.W;

public class ModernMaterialUtilities {

    private static final List<ModernMaterial> mNewMaterials = new ArrayList<>();
    public static final HashMap<Integer, ModernMaterial> materialIDToMaterial = new HashMap<>();
    public static final HashMap<String, ModernMaterial> materialNameToMaterialMap = new HashMap<>();

    public static void registerMaterial(ModernMaterial aMaterial) {
        final int tCurrentMaterialID = GregTech_API.modernMaterialIDs.mConfig
                .get(materialID.name(), aMaterial.getMaterialName(), -1).getInt();
        if (tCurrentMaterialID == -1) {
            mNewMaterials.add(aMaterial);
        } else {
            aMaterial.setMaterialID(tCurrentMaterialID);
            materialIDToMaterial.put(tCurrentMaterialID, aMaterial);
            if (tCurrentMaterialID > GregTech_API.lastMaterialID) {
                GregTech_API.lastMaterialID = tCurrentMaterialID;
            }
        }
        materialNameToMaterialMap.put(aMaterial.getMaterialName(), aMaterial);
    }


    public static void registerAllMaterialsItems() {
        for (ModernMaterial tMaterial : mNewMaterials) {
            tMaterial.setMaterialID(++GregTech_API.lastMaterialID);
            GregTech_API.modernMaterialIDs.mConfig.get(materialID.name(), tMaterial.getMaterialName(), 0)
                    .set(GregTech_API.lastMaterialID);
            materialIDToMaterial.put(GregTech_API.lastMaterialID, tMaterial);
        }

        for (PartsEnum part : PartsEnum.values()) {

            MaterialPart materialPart = new MaterialPart(part);
            materialPart.setUnlocalizedName(part.partName);

            // Registers the item with the game, only available in preInit.
            GameRegistry.registerItem(materialPart, part.partName);

            // Store the Item so these parts can be retrieved later.
            part.setAssociatedItem(materialPart);

            // Registers the renderer which allows for part colouring.
            MinecraftForgeClient.registerItemRenderer(materialPart, new ModernMaterialItemRenderer());
        }

        // Register all material parts.
        for (ModernMaterial material : materialIDToMaterial.values()) {
            registerAllMaterialPartRecipes(material);
        }

    }

    public static void registerAllMaterialsFluids() {

        // Register the fluids with forge.
        for (ModernMaterial material : materialIDToMaterial.values()) {
            for (ModernMaterialFluid fluid : material.existingFluids) {
                FluidRegistry.registerFluid(fluid);
            }
        }

        BlocksEnum.FrameBox.getAssociatedMaterials().addAll(materialIDToMaterial.values());

        //GameRegistry.registerBlock(new FrameBoxBlock2(), FrameBoxItemBlock.class, "exampleBlock");
        //GameRegistry.registerTileEntity(FrameBoxTileEntity.class, "exampleTileEntity");



        //(new FrameBoxBlock()).registerBlock(FrameBoxTileEntity.class, FrameBoxItemBlock.class);
        //new FrameBoxRenderer();
        // GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + W + ".name", "Any Sub Block of this one");

        GameRegistry.registerBlock(new FrameBoxBlock(), FrameBoxItemBlock.class, "frameBoxBlock");
        GameRegistry.registerTileEntity(FrameBoxTileEntity.class, "frameBoxBlockTile");
    }

    private static void registerAllMaterialPartRecipes(ModernMaterial material) {
        new ModernMaterialsPlateRecipeGenerator().run(material);
    }

    public static ItemStack getPart(final ModernMaterial material, final IGetItem part, final int stackSize) {
        return new ItemStack(part.getItem(), stackSize, material.getMaterialID());
    }

    public static ItemStack getPart(final String materialName, final IGetItem part, final int stackSize) {
        return getPart(getMaterialFromName(materialName), part, stackSize);
    }

    public static ModernMaterial getMaterialFromName(final String materialName) {

        ModernMaterial modernMaterial = materialNameToMaterialMap.getOrDefault(materialName, null);

        if (modernMaterial == null) {
            throw new IllegalArgumentException("Material % does not exist. Make sure you spelt it correctly.".replace("%", materialName));
        }

        return modernMaterial;
    }

    public static ArrayList<String> tooltipGenerator(MaterialPart materialPart, ModernMaterial material) {
        // Todo, this is just temporary as a proof of concept/debug info.
        // Probably will put radioactive warning here. Not sure what else yet.
        ArrayList<String> tooltip = new ArrayList<>();
        tooltip.add("Generic Tooltip");
        tooltip.add("Material Name: " + material.getMaterialName());
        tooltip.add("Material Part Type: " + material.getCustomPartInfo(materialPart.getPart()).getTextureType());

        return tooltip;
    }
}
