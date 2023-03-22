package gregtech.api.ModernMaterials;

import static gregtech.api.ModernMaterials.Fluids.FluidEnum.*;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerAllMaterialsItems;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerMaterial;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.*;
import static gregtech.api.ModernMaterials.PartsClasses.PartsEnum.*;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_Config;

public class ModernMaterialsRegistration {

    public void run(FMLPreInitializationEvent aEvent) {

        GregTech_API.sModernMaterialIDs = new GT_Config(
                new Configuration(
                        new File(new File(aEvent.getModConfigurationDirectory(), "GregTech"), "ModerMaterialIDs.cfg")));
        GregTech_API.mLastMaterialID = GregTech_API.sModernMaterialIDs.mConfig
                .get(ConfigCategories.ModernMaterials.materialID.name(), "LastMaterialID", 0).getInt();

        new ModernMaterial("UwU").setColor(0, 255, 255, 255)
            .setTextureMode(CustomIndividual).addParts(Gear).build();

        new ModernMaterial("Amazium").setColor(100, 0, 200, 255).setTextureMode(Metallic).addAllParts()
                .setMaterialTier(TierEU.UXV).setMaterialTimeMultiplier(2.5).addFluids(Molten, Plasma, Gas).build();

        new ModernMaterial("Samarium").setColor(100, 200, 200, 255).setTextureMode(Metallic).addAllParts()
                .setMaterialTier(TierEU.UMV).setMaterialTimeMultiplier(0.5).build();

        new ModernMaterial("Copper").setColor(255, 0, 0, 255).setTextureMode(CustomUnified).addAllParts()
                .addFluids(Molten, Plasma).build();

        registerAllMaterialsItems();
        GregTech_API.sModernMaterialIDs.mConfig
                .get(ConfigCategories.ModernMaterials.materialID.name(), "LastMaterialID", 0)
                .set(GregTech_API.mLastMaterialID);
    }
}
