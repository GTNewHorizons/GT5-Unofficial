package gregtech.api.ModernMaterials;

import static gregtech.api.ModernMaterials.Fluids.FluidEnum.Gas;
import static gregtech.api.ModernMaterials.Fluids.FluidEnum.Molten;
import static gregtech.api.ModernMaterials.Fluids.FluidEnum.NoPrefix;
import static gregtech.api.ModernMaterials.Fluids.FluidEnum.Plasma;
import static gregtech.api.ModernMaterials.Items.PartProperties.TextureType.Custom;
import static gregtech.api.ModernMaterials.Items.PartProperties.TextureType.Metal_Dull;
import static gregtech.api.ModernMaterials.Items.PartProperties.TextureType.Metal_Shiny;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.registerAllMaterialsItems;

import gregtech.api.ModernMaterials.Blocks.BlockTypes.FrameBox.Special.UniversiumFrameBlockRenderer;
import gregtech.api.ModernMaterials.Blocks.BlockTypes.FrameBox.Special.UniversiumFrameItemRenderer;
import gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.Items.PartProperties.CustomItemRenderers.UniversiumItemRenderer;
import gregtech.api.ModernMaterials.RecipeGenerators.Metal;
import gregtech.api.enums.TierEU;

public class ModernMaterialsRegistration {

    public void run() {

        new ModernMaterial.ModernMaterialBuilder("Copper").setMaterialID(1)
            .setColor(120, 100, 0)
            .setTextureMode(Metal_Dull)
            .addAllParts()
            .addFluid(Gas, 100_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .addCustomFluid(new ModernMaterialFluid.Builder("Zebra % Fluid %").setTemperature(120_000_000), false)
            .setMaterialTier(TierEU.MAX)
            .build();

        new ModernMaterial.ModernMaterialBuilder("GERE").setMaterialID(2)
            .setColor(3, 100, 97)
            .setTextureMode(Metal_Shiny)
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.ModernMaterialBuilder("EWAD").setMaterialID(16)
            .setColor(120, 100, 123)
            .setTextureMode(Metal_Shiny)
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.ModernMaterialBuilder("Universium").setColor(255, 255, 255)
            .setMaterialID(17)
            .setTextureMode(Custom)
            .setMaterialTier(TierEU.MAX)
            .setCustomBlockRenderer(
                BlocksEnum.FrameBox,
                new UniversiumFrameItemRenderer(),
                new UniversiumFrameBlockRenderer())
            .setCustomItemRenderer(new UniversiumItemRenderer())
            .setRecipeGenerator(Metal::generateExtruderRecipesWithoutTools)
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.ModernMaterialBuilder("TEST3232").setColor(120, 2, 0)
            .setMaterialID(18)
            .setTextureMode(Metal_Shiny)
            .setMaterialTier(TierEU.MAX)
            .addAllParts()
            .setRecipeGenerator(Metal::generateExtruderRecipesWithoutTools)
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.ModernMaterialBuilder("TEST2").setColor(120, 200, 0)
            .setMaterialID(61)
            .setTextureMode(Metal_Shiny)
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
