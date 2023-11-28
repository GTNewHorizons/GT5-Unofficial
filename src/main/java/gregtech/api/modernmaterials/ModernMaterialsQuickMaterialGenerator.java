package gregtech.api.modernmaterials;

import static gregtech.api.modernmaterials.fluids.FluidEnum.Molten;
import static gregtech.api.modernmaterials.fluids.FluidEnum.Plasma;
import static gregtech.api.modernmaterials.items.partproperties.TextureType.Metal_Shiny;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Ingot;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Plate;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import gregtech.api.modernmaterials.recipegenerators.Metal;

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
