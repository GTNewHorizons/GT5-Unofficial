package gregtech.loaders.oreprocessing;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GTModHandler;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;

public class ProcessingToolOther implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingToolOther() {
        OrePrefixes.toolHeadHammer.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if ((aMaterial == Materials.Stone) || (aMaterial == Materials.Flint)) {
            return;
        }

        if (aMaterial != Materials.Rubber) {
            // Crafting recipes
            {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.PLUNGER.ID, 1, aMaterial, aMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "xRR", " SR", "S f", 'S', OrePrefixes.stick.get(aMaterial), 'R',
                        OrePrefixes.plate.get(Materials.AnyRubber) });
            }
        }

        if ((!aMaterial.contains(SubTag.WOOD)) && (!aMaterial.contains(SubTag.BOUNCY))
            && (!aMaterial.contains(SubTag.NO_SMASHING))) {
            // Crafting recipes
            {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.WRENCH.ID, 1, aMaterial, aMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "IhI", "III", " I ", 'I', OrePrefixes.ingot.get(aMaterial) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.CROWBAR.ID, 1, aMaterial, aMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "hDS", "DSD", "SDf", 'S', OrePrefixes.stick.get(aMaterial), 'D', Dyes.dyeBlue });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.SCREWDRIVER.ID, 1, aMaterial, aMaterial.mHandleMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " fS", " Sh", "W  ", 'S', OrePrefixes.stick.get(aMaterial), 'W',
                        OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, aMaterial, aMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PfP", "hPd", "STS", 'S', OrePrefixes.stick.get(aMaterial), 'P',
                        OrePrefixes.plate.get(aMaterial), 'T', OrePrefixes.screw.get(aMaterial) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.SCOOP.ID, 1, aMaterial, aMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SWS", "SSS", "xSh", 'S', OrePrefixes.stick.get(aMaterial), 'W',
                        new ItemStack(Blocks.wool, 1, 32767) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.BRANCHCUTTER.ID, 1, aMaterial, aMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PfP", "PdP", "STS", 'S', OrePrefixes.stick.get(aMaterial), 'P',
                        OrePrefixes.plate.get(aMaterial), 'T', OrePrefixes.screw.get(aMaterial) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.KNIFE.ID, 1, aMaterial, aMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "fPh", " S ", 'S', OrePrefixes.stick.get(aMaterial), 'P',
                        OrePrefixes.plate.get(aMaterial) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.BUTCHERYKNIFE.ID, 1, aMaterial, aMaterial, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PPf", "PP ", "Sh ", 'S', OrePrefixes.stick.get(aMaterial), 'P',
                        OrePrefixes.plate.get(aMaterial) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_LV.ID,
                        1,
                        aMaterial,
                        Materials.Rubber,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.get(aMaterial), 'P',
                        OrePrefixes.plate.get(Materials.AnyRubber), 'S', OrePrefixes.stick.get(Materials.Iron), 'L',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_MV.ID,
                        1,
                        aMaterial,
                        Materials.Rubber,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.get(aMaterial), 'P',
                        OrePrefixes.plate.get(Materials.AnyRubber), 'S', OrePrefixes.stick.get(Materials.Steel), 'L',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_HV.ID,
                        1,
                        aMaterial,
                        Materials.AnySyntheticRubber,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.get(aMaterial), 'P',
                        OrePrefixes.plate.get(Materials.AnySyntheticRubber), 'S',
                        OrePrefixes.stick.get(Materials.StainlessSteel), 'L', ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    (MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.POCKET_MULTITOOL.ID, 1, aMaterial, aMaterial, null)),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "ABC", "DEF", "CFG", 'A', OrePrefixes.stickLong.get(aMaterial), 'B',
                        OrePrefixes.toolHeadSaw.get(aMaterial), 'C', OrePrefixes.ring.get(aMaterial), 'D',
                        OrePrefixes.toolHeadFile.get(aMaterial), 'E', OrePrefixes.plate.get(aMaterial), 'F',
                        OrePrefixes.toolHeadSword.get(aMaterial), 'G', Dyes.dyeBlue });
            }
        }
    }
}
