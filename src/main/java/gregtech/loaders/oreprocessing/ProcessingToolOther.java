package gregtech.loaders.oreprocessing;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.MaterialFacades;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;

public class ProcessingToolOther implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingToolOther INSTANCE;

    public ProcessingToolOther() {
        INSTANCE = this;
        OrePrefixes.toolHeadHammer.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (material == null) return;

        if ((material == Materials.Stone) || (material == Materials.Flint)) {
            return;
        }

        if (material != Materials.Rubber) {
            // Crafting recipes
            {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.PLUNGER.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "xRR", " SR", "S f", 'S', MaterialParts.craftIngredient(OrePrefixes.stick, material),
                        'R', OrePrefixes.plate.ingredient(MaterialFacades.AnyRubber) });
            }
        }

        if ((!MaterialUtils.hasFlag(material, GTMaterialFlag.WOOD))
            && (!MaterialUtils.hasFlag(material, GTMaterialFlag.BOUNCY))
            && (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING))) {
            // Crafting recipes
            {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WRENCH.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "IhI", "III", " I ", 'I',
                        MaterialParts.craftIngredient(OrePrefixes.ingot, material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.CROWBAR.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "hDS", "DSD", "SDf", 'S', MaterialParts.craftIngredient(OrePrefixes.stick, material),
                        'D', Dyes.dyeBlue });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER.ID,
                        1,
                        material,
                        MaterialUtils.handleMaterial(material),
                        null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " fS", " Sh", "W  ", 'S', MaterialParts.craftIngredient(OrePrefixes.stick, material),
                        'W', OrePrefixes.stick.ingredient(MaterialUtils.handleMaterial(material)) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PfP", "hPd", "STS", 'S', MaterialParts.craftIngredient(OrePrefixes.stick, material),
                        'P', MaterialParts.craftIngredient(OrePrefixes.plate, material), 'T',
                        MaterialParts.craftIngredient(OrePrefixes.screw, material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.SCOOP.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SWS", "SSS", "xSh", 'S', MaterialParts.craftIngredient(OrePrefixes.stick, material),
                        'W', new ItemStack(Blocks.wool, 1, 32767) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.BRANCHCUTTER.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PfP", "PdP", "STS", 'S', MaterialParts.craftIngredient(OrePrefixes.stick, material),
                        'P', MaterialParts.craftIngredient(OrePrefixes.plate, material), 'T',
                        MaterialParts.craftIngredient(OrePrefixes.screw, material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.KNIFE.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "fPh", " S ", 'S', MaterialParts.craftIngredient(OrePrefixes.stick, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.BUTCHERYKNIFE.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PPf", "PP ", "Sh ", 'S', MaterialParts.craftIngredient(OrePrefixes.stick, material),
                        'P', MaterialParts.craftIngredient(OrePrefixes.plate, material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_LV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MaterialParts.craftIngredient(OrePrefixes.bolt, material),
                        'P', OrePrefixes.plate.ingredient(MaterialFacades.AnyRubber), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.stick, Materials.Iron), 'L',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_LV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MaterialParts.craftIngredient(OrePrefixes.bolt, material),
                        'P', OrePrefixes.plate.ingredient(MaterialFacades.AnyRubber), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.stick, Materials.Iron), 'L',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_LV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MaterialParts.craftIngredient(OrePrefixes.bolt, material),
                        'P', OrePrefixes.plate.ingredient(MaterialFacades.AnyRubber), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.stick, Materials.Iron), 'L',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_MV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MaterialParts.craftIngredient(OrePrefixes.bolt, material),
                        'P', OrePrefixes.plate.ingredient(MaterialFacades.AnyRubber), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.stick, Materials.Steel), 'L',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_MV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MaterialParts.craftIngredient(OrePrefixes.bolt, material),
                        'P', OrePrefixes.plate.ingredient(MaterialFacades.AnyRubber), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.stick, Materials.Steel), 'L',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_MV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MaterialParts.craftIngredient(OrePrefixes.bolt, material),
                        'P', OrePrefixes.plate.ingredient(MaterialFacades.AnyRubber), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.stick, Materials.Steel), 'L',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_HV.ID,
                        1,
                        material,
                        MaterialFacades.AnySyntheticRubber,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MaterialParts.craftIngredient(OrePrefixes.bolt, material),
                        'P', OrePrefixes.plate.ingredient(MaterialFacades.AnySyntheticRubber), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.stick, Materials.StainlessSteel), 'L',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_HV.ID,
                        1,
                        material,
                        MaterialFacades.AnySyntheticRubber,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MaterialParts.craftIngredient(OrePrefixes.bolt, material),
                        'P', OrePrefixes.plate.ingredient(MaterialFacades.AnySyntheticRubber), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.stick, Materials.StainlessSteel), 'L',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_HV.ID,
                        1,
                        material,
                        MaterialFacades.AnySyntheticRubber,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MaterialParts.craftIngredient(OrePrefixes.bolt, material),
                        'P', OrePrefixes.plate.ingredient(MaterialFacades.AnySyntheticRubber), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.stick, Materials.StainlessSteel), 'L',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });

                GTModHandler.addCraftingRecipe(
                    (MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.POCKET_MULTITOOL.ID, 1, material, material, null)),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "ABC", "DEF", "CFG", 'A',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'B',
                        MaterialParts.craftIngredient(OrePrefixes.toolHeadSaw, material), 'C',
                        MaterialParts.craftIngredient(OrePrefixes.ring, material), 'D',
                        MaterialParts.craftIngredient(OrePrefixes.toolHeadFile, material), 'E',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'F',
                        MaterialParts.craftIngredient(OrePrefixes.spring, material), 'G', Dyes.dyeBlue });

                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.TROWEL.ID,
                        1,
                        material,
                        MaterialUtils.handleMaterial(material),
                        null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "  d", "SSC", "fPP", 'S',
                        OrePrefixes.stick.ingredient(MaterialUtils.handleMaterial(material)), 'C',
                        MaterialParts.craftIngredient(OrePrefixes.screw, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material) });

            }
        }
    }
}
