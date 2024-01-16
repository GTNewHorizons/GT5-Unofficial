package gregtech.api.modernmaterials;

import static gregtech.api.modernmaterials.fluids.FluidEnum.Gas;
import static gregtech.api.modernmaterials.fluids.FluidEnum.Molten;
import static gregtech.api.modernmaterials.fluids.FluidEnum.NoPrefix;
import static gregtech.api.modernmaterials.fluids.FluidEnum.Plasma;
import static gregtech.api.modernmaterials.items.partproperties.TextureType.Custom;
import static gregtech.api.modernmaterials.items.partproperties.TextureType.Dust;
import static gregtech.api.modernmaterials.items.partproperties.TextureType.Gem;
import static gregtech.api.modernmaterials.items.partproperties.TextureType.Metal_Dull;
import static gregtech.api.modernmaterials.items.partproperties.TextureType.Metal_Shiny;

import gregtech.api.enums.TierEU;
import gregtech.api.modernmaterials.effects.Effects;
import gregtech.api.modernmaterials.fluids.ModernMaterialFluid;
import gregtech.api.modernmaterials.items.partproperties.customitemrenderers.UniversiumItemRenderer;
import gregtech.api.modernmaterials.recipegenerators.Metal;
import gregtech.api.modernmaterials.tooltips.CustomTooltips;

public class ModernMaterialsRegistration {

    public static final ModernMaterial Copper = new ModernMaterial.ModernMaterialBuilder("copper").setMaterialID(35)
        .setColor(120, 100, 0)
        .setTextureMode(Metal_Dull)
        .addAllParts()
        .addFluid(Gas, 100_000)
        .addFluid(NoPrefix, 3_000)
        .addFluid(Molten, 10_000)
        .addFluid(Plasma, 100_000)
        .addCustomFluid(new ModernMaterialFluid.Builder("zebra", "zebra.").setTemperature(120_000_000), false)
        .setMaterialTier(TierEU.MAX)
        .build();

    public static final ModernMaterial damascusSteel = new ModernMaterial.ModernMaterialBuilder("GERE")
        .setMaterialID(335)
        .setColor(3, 100, 97)
        .setTextureMode(Metal_Shiny)
        .addAllParts()
        .addFluid(Gas, 1_000)
        .addFluid(NoPrefix, 3_000)
        .addFluid(Molten, 10_000)
        .addFluid(Plasma, 100_000)
        .build();

    public void run() {

        new ModernMaterial.ModernMaterialBuilder("EWAD").setMaterialID(16)
            .setColor(120, 100, 123)
            .setTextureMode(Gem)
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .build();

        new ModernMaterial.ModernMaterialBuilder("universium").setColor(255, 255, 255)
            .setMaterialID(139)
            .setTextureMode(Custom)
            .setMaterialTier(TierEU.MAX)
            .addPlayerEffect(Effects::radiation)
            .addCustomRenderers(UniversiumRendererRegistration::initRenderersSafely)
            .setCustomItemRenderer(new UniversiumItemRenderer())
            .addCustomTooltip(CustomTooltips::createRainbowText)
            .setRecipeGenerator(Metal::generateExtruderRecipesWithoutTools)
            .addAllParts()
            .addFluid(Gas, 1_000)
            .addFluid(NoPrefix, 3_000)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000)
            .addCustomFluid(
                new ModernMaterialFluid.Builder("hydro_cracked", "hydrocracked.").setTemperature(121_000_000),
                true)
            .addCustomFluid(
                new ModernMaterialFluid.Builder("steam_cracked", "steamcracked.").setTemperature(122_000_000),
                true)
            .addCustomFluid(
                new ModernMaterialFluid.Builder("extremely_hydro_cracked", "extremelyhydrocracked.")
                    .setTemperature(123_000_000),
                false)
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
            .setTextureMode(Dust)
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

    }

}
