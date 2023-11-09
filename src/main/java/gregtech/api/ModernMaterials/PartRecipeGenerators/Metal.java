package gregtech.api.ModernMaterials.PartRecipeGenerators;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import static gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum.FrameBox;
import static gregtech.api.ModernMaterials.PartsClasses.ItemsEnum.Ingot;
import static gregtech.api.ModernMaterials.PartsClasses.ItemsEnum.Plate;
import static gregtech.api.ModernMaterials.PartsClasses.ItemsEnum.Rod;
import static gregtech.api.ModernMaterials.PartsClasses.ItemsEnum.SmallGear;
import static gregtech.api.enums.ItemList.Shape_Extruder_Small_Gear;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBenderRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sExtruderRecipes;

public class Metal extends BaseRecipeGenerator {

    @Override
    public void generateRecipes(ModernMaterial material) {


        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Plate.getPart(material, 1))
            .duration((int) (1L * material.getMaterialTimeMultiplier()))
            .eut((int) (material.getMaterialTier() * 0.95))
            .addTo(sBenderRecipes);


        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Small_Gear.get(1))
            .itemOutputs(SmallGear.getPart(material, 1))
            .duration((int) (1L * material.getMaterialTimeMultiplier()))
            .eut((int) (material.getMaterialTier() * 0.95))
            .addTo(sExtruderRecipes);

        ItemStack frameBox = FrameBox.getPart(material, 1);
        ItemStack rod = Rod.getPart(material, 4);

        GT_Values.RA.stdBuilder()
            .itemInputs(rod, GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(frameBox)
            .duration((int) (1L * material.getMaterialTimeMultiplier()))
            .eut((int) (material.getMaterialTier() * 0.95))
            .addTo(sAssemblerRecipes);

    }

}
