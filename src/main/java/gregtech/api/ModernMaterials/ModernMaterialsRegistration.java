package gregtech.api.ModernMaterials;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.FrameBox.TESR.UniversiumFrameItemRenderer;
import gregtech.api.ModernMaterials.Blocks.FrameBox.TESR.UniversiumFrameBlockRenderer;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.util.GT_Config;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static gregtech.api.ModernMaterials.Fluids.FluidEnum.*;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerAllMaterialsItems;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Metallic;

public class ModernMaterialsRegistration {

    public void run(FMLPreInitializationEvent event) {

//        GregTech_API.modernMaterialIDs = new GT_Config(
//                new Configuration(
//                        new File(new File(event.getModConfigurationDirectory(), "GregTech"), "ModerMaterialIDs.cfg")));
//        GregTech_API.lastMaterialID = GregTech_API.modernMaterialIDs.mConfig
//                .get(ConfigCategories.ModernMaterials.materialID.name(), "LastMaterialID", 0).getInt();

        new ModernMaterial.Builder("Copper")
            .setMaterialID(1)
            .setColor(120, 100, 0)
            .setCustomRenderer(BlocksEnum.FrameBox, new UniversiumFrameItemRenderer(), new UniversiumFrameBlockRenderer())
            .setTextureMode(Metallic)
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.Builder("GERE")
            .setMaterialID(2)
            .setColor(3, 100, 97)
            .setTextureMode(Metallic)
            .setCustomRenderer(BlocksEnum.FrameBox, new UniversiumFrameItemRenderer(), new UniversiumFrameBlockRenderer())
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.Builder("EWAD")
            .setMaterialID(3)
            .setColor(120, 100, 123)
            .setTextureMode(Metallic)
            .setCustomRenderer(BlocksEnum.FrameBox, new UniversiumFrameItemRenderer(), new UniversiumFrameBlockRenderer())
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.Builder("TEST")
            .setColor(120, 2, 0)
            .setMaterialID(4)
            .setTextureMode(Metallic)
            .setCustomRenderer(BlocksEnum.FrameBox, new UniversiumFrameItemRenderer(), new UniversiumFrameBlockRenderer())
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

//                    .addCustomFluid(
//            new ModernMaterialFluid.Builder("Zebra % Fluid %")
//                .setTemperature(120_000_000)
//        )

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
