package gregtech.api.modernmaterials.recipegenerators;

import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.SolidBlock;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Bolt;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Dust;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Gear;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Ingot;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.ItemCasing;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Plate;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Ring;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Rod;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.SmallGear;
import static gregtech.api.enums.ItemList.Shape_Extruder_Block;
import static gregtech.api.enums.ItemList.Shape_Extruder_Bolt;
import static gregtech.api.enums.ItemList.Shape_Extruder_Casing;
import static gregtech.api.enums.ItemList.Shape_Extruder_Gear;
import static gregtech.api.enums.ItemList.Shape_Extruder_Plate;
import static gregtech.api.enums.ItemList.Shape_Extruder_Ring;
import static gregtech.api.enums.ItemList.Shape_Extruder_Rod;
import static gregtech.api.enums.ItemList.Shape_Extruder_Small_Gear;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sExtruderRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.util.MathHelper;

import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;

public class Metal {

    public static int generateEUForRecipe(int tier) {
        int maxEU = (int) (tier * 0.95);
        int minEU = (int) (tier / 4.0 * 1.05);

        return MathHelper.clamp_int(tier, minEU, maxEU);
    }

    public static void generateExtruderRecipesWithoutTools(ModernMaterial material) {

        // Small Gear
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Small_Gear.get(0))
            .itemOutputs(SmallGear.getPart(material, 1))
            .duration(SECONDS * material.getHardness() * SmallGear.percentageOfIngot)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(sExtruderRecipes);

        // Gear
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 4), Shape_Extruder_Gear.get(0))
            .itemOutputs(Gear.getPart(material, 1))
            .duration(SECONDS * material.getHardness() * Gear.percentageOfIngot)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(sExtruderRecipes);

        // Plate
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Plate.get(0))
            .itemOutputs(Plate.getPart(material, 1))
            .duration(SECONDS * material.getHardness() * Plate.percentageOfIngot)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(sExtruderRecipes);

        // Item Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 5), Shape_Extruder_Casing.get(0))
            .itemOutputs(ItemCasing.getPart(material, 2))
            .duration(SECONDS * material.getHardness() * ItemCasing.percentageOfIngot)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(sExtruderRecipes);

        // Ring
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Ring.get(0))
            .itemOutputs(Ring.getPart(material, 4))
            .duration(SECONDS * material.getHardness() * Ring.percentageOfIngot)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(sExtruderRecipes);

        // Rod
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Rod.get(0))
            .itemOutputs(Rod.getPart(material, 2))
            .duration(SECONDS * material.getHardness() * Rod.percentageOfIngot)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(sExtruderRecipes);

        // Bolt
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Bolt.get(0))
            .itemOutputs(Bolt.getPart(material, 8))
            .duration(SECONDS * material.getHardness() * Bolt.percentageOfIngot)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(sExtruderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 9), Shape_Extruder_Block.get(0))
            .itemOutputs(SolidBlock.getPart(material, 1))
            .duration(SECONDS * material.getHardness() * Bolt.percentageOfIngot)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(sExtruderRecipes);

    }

    public static void EBFRecipeGeneratorWithFreezer(ModernMaterial material) {

        GT_Values.RA.stdBuilder()
            .itemInputs(Dust.getPart(material, 1), GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(Bolt.getPart(material, 8))
            .duration(SECONDS * material.getHardness() * Bolt.percentageOfIngot)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(sExtruderRecipes);

    }
}
