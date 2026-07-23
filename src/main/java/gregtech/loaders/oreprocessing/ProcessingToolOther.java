package gregtech.loaders.oreprocessing;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

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
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        Materials legacyMaterial = MU.materialOf(material);
        if (legacyMaterial == null) return;

        if ((material == MU.material(Materials.Stone)) || (material == MU.material(Materials.Flint))) {
            return;
        }

        if (material != MU.material(Materials.Rubber)) {
            // Crafting recipes
            {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.PLUNGER.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "xRR", " SR", "S f", 'S', MU.craftIngredient(OrePrefixes.stick, material), 'R',
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
                    new Object[] { "IhI", "III", " I ", 'I', MU.craftIngredient(OrePrefixes.ingot, material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.CROWBAR.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "hDS", "DSD", "SDf", 'S', MU.craftIngredient(OrePrefixes.stick, material), 'D',
                        Dyes.dyeBlue });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.SCREWDRIVER.ID, 1, material, MU.handleMaterial(material), null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " fS", " Sh", "W  ", 'S', MU.craftIngredient(OrePrefixes.stick, material), 'W',
                        OrePrefixes.stick.ingredient(MU.materialOf(MU.handleMaterial(material))) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PfP", "hPd", "STS", 'S', MU.craftIngredient(OrePrefixes.stick, material), 'P',
                        MU.craftIngredient(OrePrefixes.plate, material), 'T',
                        MU.craftIngredient(OrePrefixes.screw, material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.SCOOP.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SWS", "SSS", "xSh", 'S', MU.craftIngredient(OrePrefixes.stick, material), 'W',
                        new ItemStack(Blocks.wool, 1, 32767) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.BRANCHCUTTER.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PfP", "PdP", "STS", 'S', MU.craftIngredient(OrePrefixes.stick, material), 'P',
                        MU.craftIngredient(OrePrefixes.plate, material), 'T',
                        MU.craftIngredient(OrePrefixes.screw, material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.KNIFE.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "fPh", " S ", 'S', MU.craftIngredient(OrePrefixes.stick, material), 'P',
                        MU.craftIngredient(OrePrefixes.plate, material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.BUTCHERYKNIFE.ID, 1, material, material, null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PPf", "PP ", "Sh ", 'S', MU.craftIngredient(OrePrefixes.stick, material), 'P',
                        MU.craftIngredient(OrePrefixes.plate, material) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_LV.ID,
                        1,
                        material,
                        MU.material(Materials.Rubber),
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(OrePrefixes.bolt, material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Iron), 'L', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_LV.ID,
                        1,
                        material,
                        MU.material(Materials.Rubber),
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(OrePrefixes.bolt, material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Iron), 'L', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_LV.ID,
                        1,
                        material,
                        MU.material(Materials.Rubber),
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(OrePrefixes.bolt, material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Iron), 'L', ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_MV.ID,
                        1,
                        material,
                        MU.material(Materials.Rubber),
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(OrePrefixes.bolt, material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Steel), 'L', ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_MV.ID,
                        1,
                        material,
                        MU.material(Materials.Rubber),
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(OrePrefixes.bolt, material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Steel), 'L', ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_MV.ID,
                        1,
                        material,
                        MU.material(Materials.Rubber),
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(OrePrefixes.bolt, material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnyRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.Steel), 'L', ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_HV.ID,
                        1,
                        material,
                        MU.material(Materials.AnySyntheticRubber),
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(OrePrefixes.bolt, material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnySyntheticRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.StainlessSteel), 'L',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_HV.ID,
                        1,
                        material,
                        MU.material(Materials.AnySyntheticRubber),
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(OrePrefixes.bolt, material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnySyntheticRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.StainlessSteel), 'L',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SOLDERING_IRON_HV.ID,
                        1,
                        material,
                        MU.material(Materials.AnySyntheticRubber),
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(OrePrefixes.bolt, material), 'P',
                        OrePrefixes.plate.ingredient(Materials.AnySyntheticRubber), 'S',
                        OrePrefixes.stick.ingredient(Materials.StainlessSteel), 'L',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });

                GTModHandler.addCraftingRecipe(
                    (MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.POCKET_MULTITOOL.ID, 1, material, material, null)),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "ABC", "DEF", "CFG", 'A', MU.craftIngredient(OrePrefixes.stickLong, material), 'B',
                        MU.craftIngredient(OrePrefixes.toolHeadSaw, material), 'C',
                        MU.craftIngredient(OrePrefixes.ring, material), 'D',
                        MU.craftIngredient(OrePrefixes.toolHeadFile, material), 'E',
                        MU.craftIngredient(OrePrefixes.plate, material), 'F',
                        MU.craftIngredient(OrePrefixes.spring, material), 'G', Dyes.dyeBlue });

                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.TROWEL.ID, 1, material, MU.handleMaterial(material), null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "  d", "SSC", "fPP", 'S',
                        OrePrefixes.stick.ingredient(MU.materialOf(MU.handleMaterial(material))), 'C',
                        MU.craftIngredient(OrePrefixes.screw, material), 'P',
                        MU.craftIngredient(OrePrefixes.plate, material) });

            }
        }
    }
}
