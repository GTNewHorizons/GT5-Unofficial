/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material.werkstoff_loaders.recipe;

import static gregtech.api.enums.OrePrefixes.bolt;
import static gregtech.api.enums.OrePrefixes.cellMolten;
import static gregtech.api.enums.OrePrefixes.gearGt;
import static gregtech.api.enums.OrePrefixes.gearGtSmall;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.OrePrefixes.plateDouble;
import static gregtech.api.enums.OrePrefixes.ring;
import static gregtech.api.enums.OrePrefixes.screw;
import static gregtech.api.enums.OrePrefixes.stick;
import static gregtech.api.enums.OrePrefixes.stickLong;
import static gregtech.api.enums.OrePrefixes.toolHeadHammer;
import static gregtech.api.enums.OrePrefixes.toolHeadSaw;
import static gregtech.api.enums.OrePrefixes.toolHeadWrench;
import static gregtech.api.enums.OrePrefixes.turbineBlade;
import static gregtech.api.enums.OrePrefixes.wireFine;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffReconstruction;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;

/// Tool crafting-table recipes and turbine-blade crafting for werkstoffe. Turbine-blade shape generation
/// (extruder, fluid-solidifier mold, forming press) and the turbine-blade-to-tool assembler recipes
/// (`TURBINE_SMALL`/`TURBINE`/`TURBINE_LARGE`/`TURBINE_HUGE`) are covered by the canonical autogen
/// (`ProcessingShaping`/`ProcessingToolHead`, dispatched by `gregtech.loaders.shapeconsumers`).
public class ToolLoader implements IWerkstoffRunnable {

    // GTNH-Specific
    public static final short SCREWDRIVER_MV = 152;
    public static final short SCREWDRIVER_HV = 154;
    public static final short SOLDERING_IRON_MV = 162;
    public static final short SOLDERING_IRON_HV = 164;

    @Override
    public void run(Werkstoff werkstoff) {
        Material material = WerkstoffReconstruction.materialLibOf(werkstoff);
        if (werkstoff.getDurability() == 0) return;

        if (werkstoff.hasItemType(gem)) {
            if (!werkstoff.getGenerationFeatures()
                .isExtension())
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.HARDHAMMER.ID, 1, material, MU.handleMaterial(material), null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "XX ", "XXS", "XX ", 'X', MU.craftIngredient(gem, material), 'S',
                        stick.ingredient(MU.materialOf(MU.handleMaterial(material))) });
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(toolHeadSaw, material, 1L),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "GGf", 'G', MU.craftIngredient(gem, material) });
        }

        if (!werkstoff.hasItemType(plate)) return;

        // Disable recipe gen with handle Material for GT Materials
        if (!werkstoff.getGenerationFeatures()
            .isExtension()) {
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE
                    .getToolWithStats(IDMetaTool01.SCREWDRIVER.ID, 1, material, MU.handleMaterial(material), null),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { " fS", " Sh", "W  ", 'S', MU.craftIngredient(stick, material), 'W',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))) });
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(toolHeadWrench, material, 1L),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "hXW", "XRX", "WXd", 'X', MU.craftIngredient(plate, material), 'S',
                    plate.ingredient(MU.materialOf(MU.handleMaterial(material))), 'R',
                    ring.ingredient(MU.materialOf(MU.handleMaterial(material))), 'W',
                    screw.ingredient(MU.materialOf(MU.handleMaterial(material))) });
            GTModHandler.addShapelessCraftingRecipe(
                MetaGeneratedTool01.INSTANCE
                    .getToolWithStats(IDMetaTool01.HARDHAMMER.ID, 1, material, MU.handleMaterial(material), null),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { MU.craftIngredient(toolHeadHammer, material),
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE
                    .getToolWithStats(IDMetaTool01.FILE.ID, 1, material, MU.handleMaterial(material), null),
                GTModHandler.RecipeBits.MIRRORED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "P", "P", "S", 'P', MU.craftIngredient(plate, material), 'S',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))) });
            GTModHandler.addShapelessCraftingRecipe(
                MetaGeneratedTool01.INSTANCE
                    .getToolWithStats(IDMetaTool01.SAW.ID, 1, material, MU.handleMaterial(material), null),
                new Object[] { MU.craftIngredient(toolHeadSaw, material),
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))) });

            // LV Soldering Iron
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    IDMetaTool01.SOLDERING_IRON_LV.ID,
                    1,
                    material,
                    MU.material(Materials.Rubber),
                    new long[] { 100000L, 32L, 1L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(bolt, material), 'P',
                    plate.ingredient(Materials.AnyRubber), 'S',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))), 'L',
                    ItemList.Battery_RE_LV_Lithium.get(1L) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    IDMetaTool01.SOLDERING_IRON_LV.ID,
                    1,
                    material,
                    MU.material(Materials.Rubber),
                    new long[] { 75000L, 32L, 1L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(bolt, material), 'P',
                    plate.ingredient(Materials.AnyRubber), 'S',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))), 'L',
                    ItemList.Battery_RE_LV_Cadmium.get(1L) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    IDMetaTool01.SOLDERING_IRON_LV.ID,
                    1,
                    material,
                    MU.material(Materials.Rubber),
                    new long[] { 50000L, 32L, 1L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(bolt, material), 'P',
                    plate.ingredient(Materials.AnyRubber), 'S',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))), 'L',
                    ItemList.Battery_RE_LV_Sodium.get(1L) });
            // MV Soldering Iron
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_MV,
                    1,
                    material,
                    MU.material(Materials.Rubber),
                    new long[] { 400000L, 128L, 2L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(bolt, material), 'P',
                    plate.ingredient(Materials.AnyRubber), 'S',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))), 'L',
                    ItemList.Battery_RE_MV_Lithium.get(1L) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_MV,
                    1,
                    material,
                    MU.material(Materials.Rubber),
                    new long[] { 300000L, 128L, 2L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(bolt, material), 'P',
                    plate.ingredient(Materials.AnyRubber), 'S',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))), 'L',
                    ItemList.Battery_RE_MV_Cadmium.get(1L) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_MV,
                    1,
                    material,
                    MU.material(Materials.Rubber),
                    new long[] { 200000L, 128L, 2L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(bolt, material), 'P',
                    plate.ingredient(Materials.AnyRubber), 'S',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))), 'L',
                    ItemList.Battery_RE_MV_Sodium.get(1L) });
            // HV Soldering Iron
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_HV,
                    1,
                    material,
                    MU.material(Materials.StyreneButadieneRubber),
                    new long[] { 1600000L, 512L, 3L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(bolt, material), 'P',
                    plate.ingredient(Materials.StyreneButadieneRubber), 'S',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))), 'L',
                    ItemList.Battery_RE_HV_Lithium.get(1L) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_HV,
                    1,
                    material,
                    MU.material(Materials.StyreneButadieneRubber),
                    new long[] { 1200000L, 512L, 3L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(bolt, material), 'P',
                    plate.ingredient(Materials.StyreneButadieneRubber), 'S',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))), 'L',
                    ItemList.Battery_RE_HV_Cadmium.get(1L) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_HV,
                    1,
                    material,
                    MU.material(Materials.StyreneButadieneRubber),
                    new long[] { 800000L, 512L, 3L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', MU.craftIngredient(bolt, material), 'P',
                    plate.ingredient(Materials.StyreneButadieneRubber), 'S',
                    stick.ingredient(MU.materialOf(MU.handleMaterial(material))), 'L',
                    ItemList.Battery_RE_HV_Sodium.get(1L) });

            if (!werkstoff.hasItemType(gem)) {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE
                        .getToolWithStats(IDMetaTool01.HARDHAMMER.ID, 1, material, MU.handleMaterial(material), null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "XX ", "XXS", "XX ", 'X', MU.craftIngredient(ingot, material), 'S',
                        stick.ingredient(MU.materialOf(MU.handleMaterial(material))) });
            }
        }

        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.PLUNGER.ID, 1, material, material, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "xRR", " SR", "S f", 'S', MU.craftIngredient(stick, material), 'R',
                plate.ingredient(Materials.AnyRubber) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WRENCH.ID, 1, material, material, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "IhI", "III", " I ", 'I', MU.craftIngredient(ingot, material) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.CROWBAR.ID, 1, material, material, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "hDS", "DSD", "SDf", 'S', MU.craftIngredient(stick, material), 'D', Dyes.dyeBlue });

        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PfP", "hPd", "STS", 'S', MU.craftIngredient(stick, material), 'P',
                MU.craftIngredient(plate, material), 'T', MU.craftIngredient(screw, material) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.SCOOP.ID, 1, material, material, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SWS", "SSS", "xSh", 'S', MU.craftIngredient(stick, material), 'W',
                new ItemStack(Blocks.wool, 1, 32767) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.BRANCHCUTTER.ID, 1, material, material, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PfP", "PdP", "STS", 'S', MU.craftIngredient(stick, material), 'P',
                MU.craftIngredient(plate, material), 'T', MU.craftIngredient(screw, material) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.KNIFE.ID, 1, material, material, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "fPh", " S ", 'S', MU.craftIngredient(stick, material), 'P',
                MU.craftIngredient(plate, material) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.BUTCHERYKNIFE.ID, 1, material, material, null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPf", "PP ", "Sh ", 'S', MU.craftIngredient(stick, material), 'P',
                MU.craftIngredient(plate, material) });

        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 100000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', MU.craftIngredient(toolHeadWrench, material), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.ingredient(Materials.Steel), 'P',
                plate.ingredient(Materials.Steel), 'G', gearGtSmall.ingredient(Materials.Steel), 'B',
                ItemList.Battery_RE_LV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 75000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', MU.craftIngredient(toolHeadWrench, material), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.ingredient(Materials.Steel), 'P',
                plate.ingredient(Materials.Steel), 'G', gearGtSmall.ingredient(Materials.Steel), 'B',
                ItemList.Battery_RE_LV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 50000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', MU.craftIngredient(toolHeadWrench, material), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.ingredient(Materials.Steel), 'P',
                plate.ingredient(Materials.Steel), 'G', gearGtSmall.ingredient(Materials.Steel), 'B',
                ItemList.Battery_RE_LV_Sodium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_MV.ID,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 400000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', MU.craftIngredient(toolHeadWrench, material), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.ingredient(Materials.Aluminium), 'P',
                plate.ingredient(Materials.Aluminium), 'G', gearGtSmall.ingredient(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_MV.ID,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 300000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', MU.craftIngredient(toolHeadWrench, material), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.ingredient(Materials.Aluminium), 'P',
                plate.ingredient(Materials.Aluminium), 'G', gearGtSmall.ingredient(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_MV.ID,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 200000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', MU.craftIngredient(toolHeadWrench, material), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.ingredient(Materials.Aluminium), 'P',
                plate.ingredient(Materials.Aluminium), 'G', gearGtSmall.ingredient(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Sodium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_HV.ID,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 1600000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', MU.craftIngredient(toolHeadWrench, material), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.ingredient(Materials.StainlessSteel), 'P',
                plate.ingredient(Materials.StainlessSteel), 'G', gearGtSmall.ingredient(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_HV.ID,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 1200000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', MU.craftIngredient(toolHeadWrench, material), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.ingredient(Materials.StainlessSteel), 'P',
                plate.ingredient(Materials.StainlessSteel), 'G', gearGtSmall.ingredient(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_HV.ID,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 800000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', MU.craftIngredient(toolHeadWrench, material), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.ingredient(Materials.StainlessSteel), 'P',
                plate.ingredient(Materials.StainlessSteel), 'G', gearGtSmall.ingredient(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Sodium.get(1L) });

        // LV Electric Wirecutter
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WIRECUTTER_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 100000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X',
                MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                'M', ItemList.Electric_Motor_LV.get(1L), 'S', wireFine.ingredient(Materials.Electrum), 'P',
                MU.craftIngredient(plate, material), 'G', gearGt.ingredient(Materials.Steel), 'B',
                ItemList.Battery_RE_LV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WIRECUTTER_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 75000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X',
                MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                'M', ItemList.Electric_Motor_LV.get(1L), 'S', wireFine.ingredient(Materials.Electrum), 'P',
                MU.craftIngredient(plate, material), 'G', gearGt.ingredient(Materials.Steel), 'B',
                ItemList.Battery_RE_LV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WIRECUTTER_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 50000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X',
                MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                'M', ItemList.Electric_Motor_LV.get(1L), 'S', wireFine.ingredient(Materials.Electrum), 'P',
                MU.craftIngredient(plate, material), 'G', gearGt.ingredient(Materials.Steel), 'B',
                ItemList.Battery_RE_LV_Sodium.get(1L) });
        // MV Electric Wirecutter
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WIRECUTTER_MV.ID,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 400000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X',
                MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                'M', ItemList.Electric_Motor_MV.get(1L), 'S', wireFine.ingredient(Materials.Electrum), 'P',
                MU.craftIngredient(plate, material), 'G', gearGt.ingredient(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WIRECUTTER_MV.ID,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 300000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X',
                MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                'M', ItemList.Electric_Motor_MV.get(1L), 'S', wireFine.ingredient(Materials.Electrum), 'P',
                MU.craftIngredient(plate, material), 'G', gearGt.ingredient(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WIRECUTTER_MV.ID,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 200000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X',
                MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                'M', ItemList.Electric_Motor_MV.get(1L), 'S', wireFine.ingredient(Materials.Electrum), 'P',
                MU.craftIngredient(plate, material), 'G', gearGt.ingredient(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Sodium.get(1L) });
        // HV Electric Wirecutter
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WIRECUTTER_HV.ID,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 1600000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X',
                MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                'M', ItemList.Electric_Motor_HV.get(1L), 'S', wireFine.ingredient(Materials.Electrum), 'P',
                MU.craftIngredient(plate, material), 'G', gearGt.ingredient(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WIRECUTTER_HV.ID,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 1200000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X',
                MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                'M', ItemList.Electric_Motor_HV.get(1L), 'S', wireFine.ingredient(Materials.Electrum), 'P',
                MU.craftIngredient(plate, material), 'G', gearGt.ingredient(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WIRECUTTER_HV.ID,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 800000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X',
                MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                'M', ItemList.Electric_Motor_HV.get(1L), 'S', wireFine.ingredient(Materials.Electrum), 'P',
                MU.craftIngredient(plate, material), 'G', gearGt.ingredient(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Sodium.get(1L) });

        // LV Electric File
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.FILE_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 100000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.ingredient(Materials.Steel), 'L',
                MU.craftIngredient(stickLong, material), 'P', MU.craftIngredient(plate, material), 'G',
                OrePrefixes.gearGt.ingredient(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.FILE_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 75000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.ingredient(Materials.Steel), 'L',
                MU.craftIngredient(stickLong, material), 'P', MU.craftIngredient(plate, material), 'G',
                OrePrefixes.gearGt.ingredient(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.FILE_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 50000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.ingredient(Materials.Steel), 'L',
                MU.craftIngredient(stickLong, material), 'P', MU.craftIngredient(plate, material), 'G',
                OrePrefixes.gearGt.ingredient(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
        // MV Electric File
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.FILE_MV.ID,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 400000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.ingredient(Materials.Aluminium), 'L',
                MU.craftIngredient(stickLong, material), 'P', MU.craftIngredient(plate, material), 'G',
                OrePrefixes.gearGt.ingredient(Materials.Aluminium), 'B', ItemList.Battery_RE_MV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.FILE_MV.ID,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 300000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.ingredient(Materials.Aluminium), 'L',
                MU.craftIngredient(stickLong, material), 'P', MU.craftIngredient(plate, material), 'G',
                OrePrefixes.gearGt.ingredient(Materials.Aluminium), 'B', ItemList.Battery_RE_MV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.FILE_MV.ID,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 200000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.ingredient(Materials.Aluminium), 'L',
                MU.craftIngredient(stickLong, material), 'P', MU.craftIngredient(plate, material), 'G',
                OrePrefixes.gearGt.ingredient(Materials.Aluminium), 'B', ItemList.Battery_RE_MV_Sodium.get(1L) });
        // HV Electric File
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.FILE_HV.ID,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 1600000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Tungsten.get(1), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.ingredient(Materials.StainlessSteel), 'L',
                MU.craftIngredient(stickLong, material), 'P', MU.craftIngredient(plate, material), 'G',
                OrePrefixes.gearGt.ingredient(Materials.StainlessSteel), 'B', ItemList.Battery_RE_HV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.FILE_HV.ID,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 1200000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Tungsten.get(1), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.ingredient(Materials.StainlessSteel), 'L',
                MU.craftIngredient(stickLong, material), 'P', MU.craftIngredient(plate, material), 'G',
                OrePrefixes.gearGt.ingredient(Materials.StainlessSteel), 'B', ItemList.Battery_RE_HV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.FILE_HV.ID,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 800000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Tungsten.get(1), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.ingredient(Materials.StainlessSteel), 'L',
                MU.craftIngredient(stickLong, material), 'P', MU.craftIngredient(plate, material), 'G',
                OrePrefixes.gearGt.ingredient(Materials.StainlessSteel), 'B', ItemList.Battery_RE_HV_Sodium.get(1L) });

        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.SCREWDRIVER_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 100000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', MU.craftIngredient(stickLong, material), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.ingredient(Materials.Steel), 'P',
                plate.ingredient(Materials.Steel), 'G', gearGtSmall.ingredient(Materials.Steel), 'B',
                ItemList.Battery_RE_LV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.SCREWDRIVER_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 75000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', MU.craftIngredient(stickLong, material), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.ingredient(Materials.Steel), 'P',
                plate.ingredient(Materials.Steel), 'G', gearGtSmall.ingredient(Materials.Steel), 'B',
                ItemList.Battery_RE_LV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.SCREWDRIVER_LV.ID,
                1,
                material,
                MU.material(Materials.Steel),
                new long[] { 50000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', MU.craftIngredient(stickLong, material), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.ingredient(Materials.Steel), 'P',
                plate.ingredient(Materials.Steel), 'G', gearGtSmall.ingredient(Materials.Steel), 'B',
                ItemList.Battery_RE_LV_Sodium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_MV,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 400000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', MU.craftIngredient(stickLong, material), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.ingredient(Materials.Aluminium), 'P',
                plate.ingredient(Materials.Aluminium), 'G', gearGtSmall.ingredient(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_MV,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 300000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', MU.craftIngredient(stickLong, material), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.ingredient(Materials.Aluminium), 'P',
                plate.ingredient(Materials.Aluminium), 'G', gearGtSmall.ingredient(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_MV,
                1,
                material,
                MU.material(Materials.Aluminium),
                new long[] { 200000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', MU.craftIngredient(stickLong, material), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.ingredient(Materials.Aluminium), 'P',
                plate.ingredient(Materials.Aluminium), 'G', gearGtSmall.ingredient(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Sodium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_HV,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 1600000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', MU.craftIngredient(stickLong, material), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.ingredient(Materials.StainlessSteel), 'P',
                plate.ingredient(Materials.StainlessSteel), 'G', gearGtSmall.ingredient(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_HV,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 1200000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', MU.craftIngredient(stickLong, material), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.ingredient(Materials.StainlessSteel), 'P',
                plate.ingredient(Materials.StainlessSteel), 'G', gearGtSmall.ingredient(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_HV,
                1,
                material,
                MU.material(Materials.StainlessSteel),
                new long[] { 800000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', MU.craftIngredient(stickLong, material), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.ingredient(Materials.StainlessSteel), 'P',
                plate.ingredient(Materials.StainlessSteel), 'G', gearGtSmall.ingredient(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Sodium.get(1L) });

        GTModHandler.addCraftingRecipe(
            GTOreDictUnificator.get(toolHeadHammer, material, 1L),
            GTModHandler.RecipeBits.BITS_STD,
            new Object[] { "II ", "IIh", "II ", 'P', MU.craftIngredient(plate, material), 'I',
                MU.craftIngredient(ingot, material) });
        if (werkstoff.hasItemType(plateDouble) && werkstoff.hasItemType(cellMolten)) {
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(turbineBlade, material, 1L),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "fPd", "SPS", " P ", 'P', MU.craftIngredient(plateDouble, material), 'S',
                    MU.craftIngredient(screw, material) });
        }

        if (!werkstoff.hasItemType(gem)) {
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(toolHeadSaw, material, 1L),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "PP ", "fh ", 'P', MU.craftIngredient(plate, material), 'I',
                    MU.craftIngredient(ingot, material) });
        }
    }
}
