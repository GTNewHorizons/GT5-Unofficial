package gregtech.api.modernmaterials.recipegenerators;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.items.partclasses.IEnumPart;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

import java.util.HashSet;

import static gregtech.api.enums.ItemList.Shape_Extruder_Block;
import static gregtech.api.enums.ItemList.Shape_Extruder_Bolt;
import static gregtech.api.enums.ItemList.Shape_Extruder_Casing;
import static gregtech.api.enums.ItemList.Shape_Extruder_Gear;
import static gregtech.api.enums.ItemList.Shape_Extruder_Plate;
import static gregtech.api.enums.ItemList.Shape_Extruder_Ring;
import static gregtech.api.enums.ItemList.Shape_Extruder_Rod;
import static gregtech.api.enums.ItemList.Shape_Extruder_Small_Gear;
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
import static gregtech.api.modernmaterials.recipegenerators.Utility.AUTO_GENERATED_TIME_MULTIPLIER;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

public class Metal {

    public static void generateExtruderRecipesWithoutTools(ModernMaterial material) {

        HashSet<IEnumPart> partList = new HashSet<>();

        // Small Gear
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Small_Gear.get(0))
            .itemOutputs(SmallGear.getPart(material, 1))
            .duration(SECONDS * material.getHardness() * SmallGear.percentageOfIngot * AUTO_GENERATED_TIME_MULTIPLIER)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(extruderRecipes);
        partList.add(Ingot);
        partList.add(SmallGear);

        // Gear
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 4), Shape_Extruder_Gear.get(0))
            .itemOutputs(Gear.getPart(material, 1))
            .duration(SECONDS * material.getHardness() * Gear.percentageOfIngot * AUTO_GENERATED_TIME_MULTIPLIER)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(extruderRecipes);
        partList.add(Ingot);
        partList.add(Gear);

        // Plate
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Plate.get(0))
            .itemOutputs(Plate.getPart(material, 1))
            .duration(SECONDS * material.getHardness() * Plate.percentageOfIngot * AUTO_GENERATED_TIME_MULTIPLIER)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(extruderRecipes);
        partList.add(Ingot);
        partList.add(Plate);

        // Item Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 5), Shape_Extruder_Casing.get(0))
            .itemOutputs(ItemCasing.getPart(material, 2))
            .duration(SECONDS * material.getHardness() * ItemCasing.percentageOfIngot * AUTO_GENERATED_TIME_MULTIPLIER)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(extruderRecipes);
        partList.add(Ingot);
        partList.add(ItemCasing);

        // Ring
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Ring.get(0))
            .itemOutputs(Ring.getPart(material, 4))
            .duration(SECONDS * material.getHardness() * Ring.percentageOfIngot * AUTO_GENERATED_TIME_MULTIPLIER)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(extruderRecipes);
        partList.add(Ingot);
        partList.add(Ring);

        // Rod
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Rod.get(0))
            .itemOutputs(Rod.getPart(material, 2))
            .duration(SECONDS * material.getHardness() * Rod.percentageOfIngot * AUTO_GENERATED_TIME_MULTIPLIER)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(extruderRecipes);
        partList.add(Ingot);
        partList.add(Rod);

        // Bolt
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 1), Shape_Extruder_Bolt.get(0))
            .itemOutputs(Bolt.getPart(material, 8))
            .duration(SECONDS * material.getHardness() * Bolt.percentageOfIngot * AUTO_GENERATED_TIME_MULTIPLIER)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(extruderRecipes);
        partList.add(Ingot);
        partList.add(Bolt);

        // Full block
        GT_Values.RA.stdBuilder()
            .itemInputs(Ingot.getPart(material, 9), Shape_Extruder_Block.get(0))
            .itemOutputs(SolidBlock.getPart(material, 1))
            .duration(SECONDS * material.getHardness() * 9 * AUTO_GENERATED_TIME_MULTIPLIER)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(extruderRecipes);
        partList.add(Ingot);
        partList.add(SolidBlock);

        if (!material.partsExist(partList)) throw new RuntimeException("Recipe contained part that is not valid for " + material);
    }

    public static void metalHandCraftingRecipes(ModernMaterial material) {
        HashSet<IEnumPart> partList = new HashSet<>();

        // Ingot -> Dust via mortar
        GT_ModHandler.addShapelessCraftingRecipe(
            Dust.getPart(material, 1),
            GT_Proxy.tBits,
            new Object[] { ToolDictNames.craftingToolMortar, Ingot.getPart(material, 1) });
        partList.add(Dust);
        partList.add(Ingot);



        if (!material.partsExist(partList)) throw new RuntimeException("Recipe contained part that is not valid for " + material);
    }

    public static void EBFRecipeGeneratorWithFreezer(ModernMaterial material) {

        GT_Values.RA.stdBuilder()
            .itemInputs(Dust.getPart(material, 1), GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(Bolt.getPart(material, 8))
            .duration(SECONDS * material.getHardness() * Bolt.percentageOfIngot)
            .eut(material.getMaterialTier() * 0.95)
            .addTo(extruderRecipes);

    }


}
