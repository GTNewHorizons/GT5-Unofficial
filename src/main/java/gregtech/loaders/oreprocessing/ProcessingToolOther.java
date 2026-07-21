package gregtech.loaders.oreprocessing;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
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
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        if ((material == Materials.Stone) || (material == Materials.Flint)) {
            return;
        }

        if (material != Materials.Rubber) {
            // Crafting recipes
            {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.PLUNGER.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "xRR", " SR", "S f", 'S', OrePrefixes.stick.ingredient(material), 'R',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber) });
            }
        }

        if ((!MU.hasFlag(material, GTMaterialFlag.WOOD)) && (!MU.hasFlag(material, GTMaterialFlag.BOUNCY))
            && (!MU.hasFlag(material, GTMaterialFlag.NO_SMASHING))) {
            // Crafting recipes
            {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WRENCH.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "IhI", "III", " I ", 'I', OrePrefixes.ingot.ingredient(material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.CROWBAR.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "hDS", "DSD", "SDf", 'S', OrePrefixes.stick.ingredient(material), 'D',
                        Dyes.dyeBlue });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.SCREWDRIVER.ID, 1, material, material.mHandleMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " fS", " Sh", "W  ", 'S', OrePrefixes.stick.ingredient(material), 'W',
                        OrePrefixes.stick.ingredient(material.mHandleMaterial) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PfP", "hPd", "STS", 'S', OrePrefixes.stick.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(material), 'T', OrePrefixes.screw.ingredient(material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.SCOOP.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SWS", "SSS", "xSh", 'S', OrePrefixes.stick.ingredient(material), 'W',
                        new ItemStack(Blocks.wool, 1, 32767) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.BRANCHCUTTER.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PfP", "PdP", "STS", 'S', OrePrefixes.stick.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(material), 'T', OrePrefixes.screw.ingredient(material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.KNIFE.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "fPh", " S ", 'S', OrePrefixes.stick.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.BUTCHERYKNIFE.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PPf", "PP ", "Sh ", 'S', OrePrefixes.stick.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_LV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Iron), 'L', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_LV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Iron), 'L', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_LV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Iron), 'L', ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_MV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Steel), 'L', ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_MV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Steel), 'L', ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_MV.ID,
                        1,
                        material,
                        Materials.Rubber,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Steel), 'L', ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_HV.ID,
                        1,
                        material,
                        Materials.AnySyntheticRubber,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnySyntheticRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.StainlessSteel), 'L',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_HV.ID,
                        1,
                        material,
                        Materials.AnySyntheticRubber,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnySyntheticRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.StainlessSteel), 'L',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_HV.ID,
                        1,
                        material,
                        Materials.AnySyntheticRubber,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.ingredient(material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnySyntheticRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.StainlessSteel), 'L',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });

                GTModHandler.addCraftingRecipe(
                    (MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.POCKET_MULTITOOL.ID, 1, material, material, null)),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "ABC", "DEF", "CFG", 'A', OrePrefixes.stickLong.ingredient(material), 'B',
                        OrePrefixes.toolHeadSaw.ingredient(material), 'C', OrePrefixes.ring.ingredient(material), 'D',
                        OrePrefixes.toolHeadFile.ingredient(material), 'E', OrePrefixes.plate.ingredient(material), 'F',
                        OrePrefixes.spring.ingredient(material), 'G', Dyes.dyeBlue });

                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.TROWEL.ID, 1, material, material.mHandleMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "  d", "SSC", "fPP", 'S', OrePrefixes.stick.ingredient(material.mHandleMaterial),
                        'C', OrePrefixes.screw.ingredient(material), 'P', OrePrefixes.plate.ingredient(material) });

            }
        }
    }
}
