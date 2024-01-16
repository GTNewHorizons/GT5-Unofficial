package gregtech.api.modernmaterials;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.modernmaterials.items.partproperties.TextureType.Custom;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.modernmaterials.blocks.blocktypes.framebox.special.CustomTextureRegister;
import gregtech.api.modernmaterials.fluids.FluidEnum;
import gregtech.api.modernmaterials.fluids.ModernMaterialFluid;
import gregtech.api.modernmaterials.items.partclasses.ItemsEnum;
import gregtech.api.modernmaterials.items.partproperties.IconWrapper;
import gregtech.api.modernmaterials.items.partproperties.TextureType;

public class ModernMaterialsTextureRegister {

    private Map<String, PartTextureConfig> loadJson(String jsonLocation) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, PartTextureConfig>>(){}.getType();
        ;
        InputStream is = getClass().getResourceAsStream(jsonLocation);
        if (is == null) {
            throw new RuntimeException("Missing files at " + jsonLocation);
        }

        try (InputStreamReader reader = new InputStreamReader(is)) {
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonParseException("Invalid json file. Cannot parse.");
        }
    }

    private void loadTextureConfigs() {

        // All normal textures.
        for (TextureType textureType : TextureType.values()) {
            if (textureType == Custom) continue;
            textureType.textureConfigs = loadJson("/assets/gregtech/modernmaterialsjson/" + textureType.name().toLowerCase() + "/TextureInfo.json");
        }

        // Custom textures
        for (ModernMaterial material : TextureType.getCustomTextureMaterials()) {
            Map<String, PartTextureConfig> textureConfigs = loadJson("/assets/gregtech/modernmaterialsjson/custom/" + material.getMaterialName() + "/TextureInfo.json");
            Custom.setCustomJson(material, textureConfigs);
        }

    }


    // This event is solely used for ModernMaterials icon registration.
    // That means all blocks, items and fluids for ModernMaterials.
    @SubscribeEvent
    public void registerIcons(TextureStitchEvent.Pre event) {

        loadTextureConfigs();

        if (event.map.getTextureType() == 1) {
            standardItemTextures(event.map);
            customItemTextures(event.map);
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

    private void customItemTextures(TextureMap map) {

//        // Pre sort this by part name not enum name to save computation later.
//        ItemsEnum[] partsEnum = ItemsEnum.values();
//        Arrays.sort(partsEnum, Comparator.comparing(part -> part.partName));
//
//        // Iterate over all texture types and their associated item textures to register them and any
//        // additional layers. A file can be identified as PartName_0_c.png, the 0 indicates the layer and c indicates if
//        // the part should have standard GT colouring applied to it for this material.
//
//        // PartName_1 for example is layer 1 and has no colouring. This can be useful for e.g. fine wire
//        // where you want the lower layer to be coloured but not the overlay.
//
//        HashMap<String, ItemsEnum> partNameToEnumMap = new HashMap<>();
//        for (ItemsEnum part : partsEnum) {
//            partNameToEnumMap.put(part.partName, part);
//        }
//
//        for (ModernMaterial material : TextureType.getCustomTextureMaterials()) {
//
//            ArrayList<String> fileList = new ArrayList<>();
//
//            int priority = 0;
//
//            for (String filename : fileList) {
//
//                boolean isColoured;
//
//                ItemsEnum itemEnum = partNameToEnumMap.getOrDefault(partName, null);
//
//                if (itemEnum == null) continue;
//
//                IIcon icon = map.registerIcon(
//                    GregTech.getResourcePath() + "ModernMaterialsIcons/"
//                        + Custom
//                        + "/"
//                        + material.getMaterialName()
//                        + "/"
//                        + partName
//                        + "_"
//                        + priority
//                        + "_"
//                        + (isColoured ? "c" : "n"));
//
//                // textures/items
//
//                IconWrapper iconWrapper = new IconWrapper(priority, isColoured, icon);
//                TextureType.addCustomTexture(material, itemEnum, iconWrapper);
//
//                priority++;
//            }
//        }

    }

    private void standardItemTextures(TextureMap map) {

        for (TextureType textureType : TextureType.values()) {
            if (textureType == Custom) continue;

            for (Map.Entry<String, PartTextureConfig> entry : textureType.textureConfigs.entrySet()) {

                String enumString = entry.getKey();
                PartTextureConfig textureConfig = entry.getValue();

                int priority = 0;
                for (PartTextureConfig.Layer textureLayer : textureConfig.getLayers()) {
                    IIcon icon = map.registerIcon(
                    GregTech.getResourcePath() + "ModernMaterialsIcons/"
                        + textureType
                        + "/"
                        + textureLayer.getFile()
                    );

                    IconWrapper iconWrapper = new IconWrapper(priority++, textureLayer.isColored(), icon);
                    textureType.addStandardTexture(Enum.valueOf(ItemsEnum.class, enumString) , iconWrapper);
                }
            }
        }
    }
}
