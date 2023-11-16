package gregtech.api.ModernMaterials;

import static gregtech.api.ModernMaterials.Fluids.FluidEnum.Molten;
import static gregtech.api.ModernMaterials.Fluids.FluidEnum.Plasma;
import static gregtech.api.ModernMaterials.Items.PartProperties.TextureType.Metal_Shiny;
import static gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum.Ingot;
import static gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum.Plate;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ModernMaterials.RecipeGenerators.Metal;

public class ModernMaterialsQuickMaterialGenerator {

    ModernMaterial.ModernMaterialBuilder generateMetalWithEBF(@NotNull String materialName, int materialID,
        @NotNull Color color) {
        return new ModernMaterial.ModernMaterialBuilder(materialName).setMaterialID(materialID)
            .setColor(color)
            .setTextureMode(Metal_Shiny)
            .setRecipeGenerator(Metal::generateExtruderRecipesWithoutTools, Metal::EBFRecipeGeneratorWithFreezer)
            .addPart(Ingot, Plate)
            .addFluid(Molten, 10_000)
            .addFluid(Plasma, 100_000);
    }
}
