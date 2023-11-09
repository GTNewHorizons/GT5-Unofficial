package gregtech.api.ModernMaterials;

import static gregtech.api.ModernMaterials.Fluids.FluidEnum.Gas;
import static gregtech.api.ModernMaterials.Fluids.FluidEnum.Molten;
import static gregtech.api.ModernMaterials.Fluids.FluidEnum.NoPrefix;
import static gregtech.api.ModernMaterials.Fluids.FluidEnum.Plasma;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerAllMaterialsItems;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Metallic;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.ModernMaterials.Blocks.BlockTypes.FrameBox.Special.UniversiumFrameBlockRenderer;
import gregtech.api.ModernMaterials.Blocks.BlockTypes.FrameBox.Special.UniversiumFrameItemRenderer;
import gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.PartRecipeGenerators.Metal;
import gregtech.api.enums.TierEU;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class ModernMaterialsRegistration {

    public void run(FMLPreInitializationEvent event) {

        new ModernMaterial.ModernMaterialBuilder("Copper").setMaterialID(1)
            .setColor(120, 100, 0)
            .setTextureMode(Metallic)
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .addCustomFluid(new ModernMaterialFluid.Builder("Zebra % Fluid %").setTemperature(120_000_000), false)
            .setMaterialTier(TierEU.MAX)
            .build();

        new ModernMaterial.ModernMaterialBuilder("GERE").setMaterialID(2)
            .setColor(3, 100, 97)
            .setTextureMode(Metallic)
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.ModernMaterialBuilder("EWAD").setMaterialID(16)
            .setColor(120, 100, 123)
            .setTextureMode(Metallic)
            .setCustomBlockRenderer(
                BlocksEnum.FrameBox,
                new UniversiumFrameItemRenderer(),
                new UniversiumFrameBlockRenderer())
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.ModernMaterialBuilder("TEST").setColor(120, 2, 0)
            .setMaterialID(17)
            .setTextureMode(Metallic)
            .setCustomBlockRenderer(
                BlocksEnum.FrameBox,
                new UniversiumFrameItemRenderer(),
                new UniversiumFrameBlockRenderer())
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.ModernMaterialBuilder("TEST3232").setColor(120, 2, 0)
            .setMaterialID(18)
            .setTextureMode(Metallic)
            .setMaterialTier(TierEU.MAX)
            .setRecipeGenerator(new Metal())
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.ModernMaterialBuilder("TEST2").setColor(120, 200, 0)
            .setMaterialID(61)
            .setTextureMode(Metallic)
            .setRecipeGenerator(new Metal())
            .setMaterialTier(TierEU.MAX)
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        // int[] test = new SplittableRandom().ints(100, 0, Short.MAX_VALUE).parallel().toArray();
        // for (int ID : test) {
        // new ModernMaterial.ModernMaterialBuilder(UUID.randomUUID().toString())
        // .setColor(120, 2, 0)
        // .setMaterialID(ID)
        // .setTextureMode(Metallic)
        // .addAllParts()
        // .addFluid(Gas, 1_000)
        // .addFluid(NoPrefix, 3_000)
        // .addFluid(Molten, 10_000)
        // .addFluid(Plasma, 100_000)
        // .build();
        // }
        // .addCustomFluid(
        // new ModernMaterialFluid.Builder("Zebra % Fluid %")
        // .setTemperature(120_000_000)
        // )

        registerAllMaterialsItems();

    }

}
