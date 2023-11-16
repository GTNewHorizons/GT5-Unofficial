package gregtech.api.ModernMaterials;

import static gregtech.api.ModernMaterials.Items.PartProperties.TextureType.Custom;
import static gregtech.api.enums.Mods.GregTech;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.ModernMaterials.Blocks.BlockTypes.FrameBox.Special.CustomTextureRegister;
import gregtech.api.ModernMaterials.Fluids.FluidEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.Items.PartProperties.IconWrapper;
import gregtech.api.ModernMaterials.Items.PartProperties.TextureType;
import gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum;

public class ModernMaterialsTextureRegister {

    // This event is solely used for ModernMaterials icon registration.
    // That means all blocks, items and fluids for ModernMaterials.
    @SubscribeEvent
    public void registerIcons(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 1) {
            standardItemTextures(event.map);
            customItenTextures(event.map);
        } else if (event.map.getTextureType() == 0) {
            // These are both stored in the same texture atlas.
            blockTextures(event.map);
            fluidTextures(event.map);
        }

        // This is ONLY to be used for unavoidable situations. Do not dump all your crap here.
        CustomTextureRegister.registerIcons(event);
    }

    private void blockTextures(TextureMap map) {

    }

    private void fluidTextures(TextureMap map) {
        for (FluidEnum fluidEnum : FluidEnum.values()) {

            fluidEnum.stillIcon = map.registerIcon(
                GregTech.getResourcePath() + "ModernMaterialsIcons/Fluids/still_"
                    + fluidEnum.name()
                        .toLowerCase());

            fluidEnum.flowingIcon = map.registerIcon(
                GregTech.getResourcePath() + "ModernMaterialsIcons/Fluids/flowing_"
                    + fluidEnum.name()
                        .toLowerCase());

        }

        // Custom fluid textures.
        ModernMaterial.getMaterialNameToMaterialMap()
            .forEach((materialName, modernMaterial) -> {

                for (ModernMaterialFluid fluid : modernMaterial.getAssociatedFluids()) {

                    // Try retrieve still fluid texture.
                    if (fluid.getStillIcon() == null) {

                        final String path = GregTech.getResourcePath() + "ModernMaterialsIcons/Fluids/CustomIcons/"
                            + materialName
                            + "/still_"
                            + fluid.getName();
                        IIcon stillIcon = map.registerIcon(path);

                        fluid.setStillIcon(stillIcon);
                    }

                    // Try retrieve flowing fluid texture.
                    if (fluid.getFlowingIcon() == null) {

                        final String path = GregTech.getResourcePath() + "ModernMaterialsIcons/Fluids/CustomIcons/"
                            + materialName
                            + "/flowing_"
                            + fluid.getName();
                        IIcon flowingIcon = map.registerIcon(path);

                        fluid.setFlowingIcon(flowingIcon);
                    }

                }

            });
    }

    private ArrayList<File> getFiles(String path) {

        final File[] files = Paths.get(path)
            .toFile()
            .listFiles();
        if (files == null) throw new RuntimeException("No files found at " + path + ".");

        ArrayList<File> fileList = new ArrayList<>(Arrays.asList(files));
        // Remove .png.mcmeta files, these are irrelevant.
        fileList.removeIf(
            file -> !file.getName()
                .toLowerCase()
                .endsWith(".png"));

        // Sort according to the actual unlocalised name rather than enum name.
        fileList.sort(Comparator.comparing(File::getName));

        return fileList;
    }

    private void customItenTextures(TextureMap map) {

        // Pre sort this by part name not enum name to save computation later.
        ItemsEnum[] partsEnum = ItemsEnum.values();
        Arrays.sort(partsEnum, Comparator.comparing(part -> part.partName));

        // Iterate over all texture types and their associated item textures to register them and any
        // additional layers. A file can be identified as PartName_0_c.png, the 0 indicates the layer and c indicates if
        // the part should have standard GT colouring applied to it for this material.

        // PartName_1 for example is layer 1 and has no colouring. This can be useful for e.g. fine wire
        // where you want the lower layer to be coloured but not the overlay.

        HashMap<String, ItemsEnum> partNameToEnumMap = new HashMap<>();
        for (ItemsEnum part : partsEnum) {
            partNameToEnumMap.put(part.partName, part);
        }

        for (ModernMaterial material : TextureType.getCustomTextureMaterials()) {

            ArrayList<File> fileList = getFiles(
                "../src/main/resources/assets/gregtech/textures/items/ModernMaterialsIcons/Custom/"
                    + material.getMaterialName());

            for (File file : fileList) {

                String[] breakdown = file.getName()
                    .replace(".png", "")
                    .split("_");

                String partName = breakdown[0];
                int priority = Integer.parseInt(breakdown[1]);
                boolean isColoured = breakdown[2].equals("c");

                ItemsEnum itemEnum = partNameToEnumMap.getOrDefault(partName, null);

                if (itemEnum == null) continue;

                IIcon icon = map.registerIcon(
                    GregTech.getResourcePath() + "ModernMaterialsIcons/"
                        + Custom
                        + "/"
                        + material.getMaterialName()
                        + "/"
                        + partName
                        + "_"
                        + priority
                        + "_"
                        + (isColoured ? "c" : "n"));

                // textures/items

                IconWrapper iconWrapper = new IconWrapper(priority, isColoured, icon);
                TextureType.addCustomTexture(material, itemEnum, iconWrapper);
            }
        }

    }

    private void standardItemTextures(TextureMap map) {

        // Pre sort this by part name not enum name to save computation later.
        ItemsEnum[] partsEnum = ItemsEnum.values();
        Arrays.sort(partsEnum, Comparator.comparing(part -> part.partName));

        // Iterate over all texture types and their associated item textures to register them and any
        // additional layers. A file can be identified as PartName_0c.png, the 0 indicates the layer and c indicates if
        // the part should have standard GT colouring applied to it for this material.

        // PartName_1 for example is layer 1 and has no colouring. This can be useful for e.g. fine wire
        // where you want the lower layer to be coloured but not the overlay. You can use 0-9 layers.

        HashMap<String, ItemsEnum> partNameToEnumMap = new HashMap<>();
        for (ItemsEnum part : partsEnum) {
            partNameToEnumMap.put(part.partName, part);
        }

        for (TextureType textureType : TextureType.values()) {

            // We will handle these elsewhere.
            if (textureType.equals(TextureType.Custom)) continue;

            ArrayList<File> fileList = getFiles(
                "../src/main/resources/assets/gregtech/textures/items/ModernMaterialsIcons/" + textureType.name());

            for (File file : fileList) {

                String[] breakdown = file.getName()
                    .replace(".png", "")
                    .split("_");

                String partName = breakdown[0];
                int priority = Integer.parseInt(breakdown[1]);
                boolean isColoured = breakdown[2].equals("c");

                ItemsEnum itemEnum = partNameToEnumMap.getOrDefault(partName, null);

                if (itemEnum == null) continue;

                // textures/items
                IIcon icon = map.registerIcon(
                    GregTech.getResourcePath() + "ModernMaterialsIcons/"
                        + textureType
                        + "/"
                        + partName
                        + "_"
                        + priority
                        + "_"
                        + (isColoured ? "c" : "n"));

                IconWrapper iconWrapper = new IconWrapper(priority, isColoured, icon);
                textureType.addStandardTexture(itemEnum, iconWrapper);
            }
        }
    }

}
