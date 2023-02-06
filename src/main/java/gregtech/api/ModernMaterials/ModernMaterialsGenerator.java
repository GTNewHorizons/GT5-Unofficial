package gregtech.api.ModernMaterials;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.PartsClasses.CustomPartInfo;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.util.GT_Config;
import net.minecraftforge.common.config.Configuration;

import java.awt.*;
import java.io.File;

import static gregtech.api.ModernMaterials.ModernMaterials.registerAllMaterials;
import static gregtech.api.ModernMaterials.ModernMaterials.registerMaterial;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Custom;
import static gregtech.api.ModernMaterials.PartsClasses.PartsEnum.Gear;

public class ModernMaterialsGenerator {

    public void run(FMLPreInitializationEvent aEvent) {

        GregTech_API.sModernMaterialIDs = new GT_Config(new Configuration(
            new File(new File(aEvent.getModConfigurationDirectory(), "GregTech"), "ModerMaterialIDs.cfg")));
        GregTech_API.mLastMaterialID = GregTech_API.sModernMaterialIDs
            .mConfig
            .get(ConfigCategories.ModernMaterials.materialID.name(), "LastMaterialID", 0)
            .getInt();
        ModernMaterial tIron = new ModernMaterial()
            .setName("Iron")
            .setColor(0, 255, 255, 255)
            .addAllParts()
            .addPartsCustom(new CustomPartInfo(Gear).setTextureType(Custom))
            .build();
        ModernMaterial tin = new ModernMaterial()
            .setName("Tin")
            .setColor(190, 190, 190, 255)
            .addPartsCustom(new CustomPartInfo(Gear).setTextureType(Custom))
            .build();
        ModernMaterial copper = new ModernMaterial()
            .setName("Copper")
            .setColor(new Color(255, 0, 0, 255))
            .addPartsCustom(
                new CustomPartInfo(Gear).setTextureType(Custom).setCustomPartTextureOverride("FALLBACK"))
            .build();
        registerMaterial(tIron);
        // registerMaterial(tin);
        registerMaterial(copper);

        registerAllMaterials();
        GregTech_API.sModernMaterialIDs
            .mConfig
            .get(ConfigCategories.ModernMaterials.materialID.name(), "LastMaterialID", 0)
            .set(GregTech_API.mLastMaterialID);
    }
}
