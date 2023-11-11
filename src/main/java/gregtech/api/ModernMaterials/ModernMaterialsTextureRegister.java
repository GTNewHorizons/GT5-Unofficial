package gregtech.api.ModernMaterials;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.ModernMaterials.Blocks.BlockTypes.FrameBox.Special.CustomTextureRegister;
import gregtech.api.ModernMaterials.Fluids.FluidEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.Items.PartProperties.IconWrapper;
import gregtech.api.ModernMaterials.Items.PartProperties.TextureType;
import gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static gregtech.api.enums.Mods.GregTech;

public class ModernMaterialsTextureRegister {

    // This event is solely used for ModernMaterials icon registration.
    // That means all blocks, items and fluids for ModernMaterials.
    @SubscribeEvent
    public void registerIcons(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 1) {
            itemTextures(event.map);
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

            fluidEnum.flowingIcon = map.registerIcon(                GregTech.getResourcePath() + "ModernMaterialsIcons/Fluids/flowing_"
                + fluidEnum.name()
                .toLowerCase());

        }


        // Custom fluid textures.
        ModernMaterialUtilities.materialNameToMaterialMap.forEach((materialName, modernMaterial) -> {

            for (ModernMaterialFluid fluid : modernMaterial.existingFluids) {

                // Try retrieve still fluid texture.
                if (fluid.getStillIcon() == null) {

                    final String path = GregTech.getResourcePath() + "ModernMaterialsIcons/Fluids/CustomIcons/" + materialName + "/still_" + fluid.getName();
                    IIcon stillIcon = map.registerIcon(path);

                    // Todo better way to detect this? Idk.
                    //if (stillIcon.getMinU() == stillIcon.getMaxU()) throw new RuntimeException("No texture for " + materialName + " icon at " + path + ".png");
                    fluid.setStillIcon(stillIcon);
                }

                // Try retrieve flowing fluid texture.
                if (fluid.getFlowingIcon() == null) {

                    final String path = GregTech.getResourcePath() + "ModernMaterialsIcons/Fluids/CustomIcons/" + materialName + "/flowing_" + fluid.getName();
                    IIcon flowingIcon = map.registerIcon(path);

                    //if (flowingIcon.getMinU() == flowingIcon.getMaxU())  throw new RuntimeException("No texture for " + materialName + " icon at " + path + ".png");
                    fluid.setFlowingIcon(flowingIcon);
                }

            }

        });
    }

    private void itemTextures(TextureMap map) {

        // Pre sort this by part name not enum name to save computation later.
        ItemsEnum[] partsEnum = ItemsEnum.values();
        Arrays.sort(partsEnum, Comparator.comparing(part -> part.partName));

        // Iterate over all texture types and their associated item textures to register them and any
        // additional layers. A file can be identified as PartName_0c.png, the 0 indicates the layer and c indicates if
        // the part should have standard GT colouring applied to it for this material.

        // PartName_1 for example is layer 1 and has no colouring. This can be useful for e.g. fine wire
        // where you want the lower layer to be coloured but not the overlay. You can use 0-9 layers.

        for (TextureType textureType : TextureType.values()) {

            String path = "../src/main/resources/assets/gregtech/textures/items/ModernMaterialsIcons/" + textureType;
            final File[] files = Paths.get(path)
                .toFile()
                .listFiles();
            if (files == null) throw new RuntimeException("No files found at " + path + ".");

            ArrayList<File> fileList = new ArrayList<>(Arrays.asList(files));
            // Remove .png.mcmeta files, these are irrelevant.
            fileList.removeIf(
                file -> file.getName()
                    .endsWith(".png.mcmeta") || file.getName()
                    .endsWith(".DS_Store"));
            // Sort according to the actual unlocalised name rather than enum name.
            fileList.sort(Comparator.comparing(File::getName));

            for (ItemsEnum part : partsEnum) {

                for (File file : fileList) {

                    // Saves computing this multiple times.
                    String trueFileName = file.getName();

                    // Codes for determining details about image.
                    String[] preProcessedName = trueFileName.split("_");
                    String fileNameWithoutCode = preProcessedName[0];
                    String code = preProcessedName[1];

                    int priority = code.charAt(0) - '0'; // Dumb way to convert code.charAt(0) to an integer from char.
                    boolean isColoured = code.charAt(1) == 'c';

                    // Do not register and move on if it is not equal.
                    if (!fileNameWithoutCode.equals(part.partName)) {
                        continue;
                    }
                    // textures/items
                    IIcon icon = map.registerIcon(
                        GregTech.getResourcePath() + "ModernMaterialsIcons/"
                            + textureType + "/"
                            + removePNG(trueFileName));

                    IconWrapper iconWrapper = new IconWrapper(priority, isColoured, icon);
                    textureType.addTexture(part, iconWrapper);
                }
            }
        }
    }

    // Remove the .png from the filename as MC does not need this to register the texture file.
    private String removePNG(final String str) {
        return str.substring(0, str.length() - 4);
    }

    private void sortItemTextureLayers() {
        for (TextureType textureType : TextureType.values()) {
            textureType.sortLayers();
        }
    }
}
