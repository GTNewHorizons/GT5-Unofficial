package gregtech.api.ModernMaterials.RecipeGenerators;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;

import static gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum.FrameBox;
import static gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum.Ingot;
import static gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum.Plate;
import static gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum.Rod;
import static gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum.SmallGear;
import static gregtech.api.enums.ItemList.Shape_Extruder_Small_Gear;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBenderRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sExtruderRecipes;

public class Metal {

    public static void generateRecipes(ModernMaterial material) {

        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Plate.getPart(material, 1))
            .duration((int) (1L * material.getMaterialTimeMultiplier()))
            .eut((int) (material.getMaterialTier() * 0.95))
            .addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Small_Gear.get(0))
            .itemOutputs(SmallGear.getPart(material, 1))
            .duration((int) (1L * material.getMaterialTimeMultiplier()))
            .eut((int) (material.getMaterialTier() * 0.95))
            .addTo(sExtruderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Rod.getPart(material, 4), GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(FrameBox.getPart(material, 1))
            .duration((int) (1L * material.getMaterialTimeMultiplier()))
            .eut((int) (material.getMaterialTier() * 0.95))
            .addTo(sAssemblerRecipes);

    }

}
