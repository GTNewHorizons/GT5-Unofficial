package gregtech.api.ModernMaterials;

import static gregtech.api.ModernMaterials.Fluids.FluidEnum.*;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerAllMaterialsItems;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerMaterial;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Custom;
import static gregtech.api.ModernMaterials.PartsClasses.PartsEnum.Gear;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.PartsClasses.CustomPartInfo;
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

        ModernMaterial iron = new ModernMaterial().setName("Iron").setColor(0, 255, 255, 255).addAllParts()
                .addPartsCustom(new CustomPartInfo(Gear).setTextureType(Custom)).build();

        ModernMaterial amazium = new ModernMaterial().setName("Amazium").setColor(100, 0, 200, 255).addAllParts()
                .setMaterialTier(TierEU.UXV).setMaterialTimeMultiplier(2.5).addFluids(Molten, Plasma, Gas).build();

        ModernMaterial samarium = new ModernMaterial().setName("Samarium").setColor(100, 200, 200, 255).addAllParts()
                .setMaterialTier(TierEU.UMV).setMaterialTimeMultiplier(0.5).build();

        ModernMaterial copper = new ModernMaterial().setName("Copper").setColor(255, 0, 0, 255).addAllParts()
                .addPartsCustom(
                        new CustomPartInfo(Gear).setTextureType(Custom).setCustomPartTextureOverride("FALLBACK"))
                .addFluids(Molten, Plasma).build();

        registerMaterial(samarium);
        registerMaterial(iron);
        registerMaterial(amazium);
        registerMaterial(copper);

        registerAllMaterialsItems();
        GregTech_API.sModernMaterialIDs.mConfig
                .get(ConfigCategories.ModernMaterials.materialID.name(), "LastMaterialID", 0)
                .set(GregTech_API.mLastMaterialID);
    }
}
