package gregtech.api.ModernMaterials;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.util.GT_Config;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static gregtech.api.ModernMaterials.Fluids.FluidEnum.*;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerAllMaterialsItems;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Metallic;

public class ModernMaterialsRegistration {

    public void run(FMLPreInitializationEvent aEvent) {

        GregTech_API.modernMaterialIDs = new GT_Config(
                new Configuration(
                        new File(new File(aEvent.getModConfigurationDirectory(), "GregTech"), "ModerMaterialIDs.cfg")));
        GregTech_API.lastMaterialID = GregTech_API.modernMaterialIDs.mConfig
                .get(ConfigCategories.ModernMaterials.materialID.name(), "LastMaterialID", 0).getInt();

        ModernMaterial copper = new ModernMaterial.Builder("Copper")
            .setColor(120, 100, 0)
            .setTextureMode(Metallic)
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .addCustomFluid(
                new ModernMaterialFluid.Builder("Zebra % Fluid %")
                    .setTemperature(120_000_000)
            ).build();

//        new ModernMaterial("UwU").setColor(0, 255, 255)
//            .setTextureMode(Metallic).addParts(Gear).build();
//
//        new ModernMaterial("Amazium").setColor(100, 0, 200).setTextureMode(Dull).addAllParts() // .addPart(Ingot).setTextureMode(Metallic).addPart(Gear)
//                .setMaterialTier(TierEU.UXV).setMaterialTimeMultiplier(2.5).addFluids(Molten, Plasma, Gas).build();
//
//        new ModernMaterial("Samarium").setColor(100, 200, 200).setTextureMode(Metallic).addAllParts()
//                .setMaterialTier(TierEU.UMV).setMaterialTimeMultiplier(0.5).build();
//
//        new ModernMaterial("Copper").build();

        registerAllMaterialsItems();
        GregTech_API.modernMaterialIDs.mConfig
                .get(ConfigCategories.ModernMaterials.materialID.name(), "LastMaterialID", 0)
                .set(GregTech_API.lastMaterialID);
    }
}
