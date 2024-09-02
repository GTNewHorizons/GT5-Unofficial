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
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.GTProxy;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;

public class ToolLoader implements IWerkstoffRunnable {

    // GTNH-Specific
    public static final short SCREWDRIVER_MV = 152;
    public static final short SCREWDRIVER_HV = 154;
    public static final short SOLDERING_IRON_MV = 162;
    public static final short SOLDERING_IRON_HV = 164;

    public void run(Werkstoff werkstoff) {
        if (werkstoff.getBridgeMaterial().mDurability == 0) return;

        if (werkstoff.hasItemType(gem)) {
            if (!werkstoff.getGenerationFeatures()
                .isExtension())
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.HARDHAMMER.ID,
                        1,
                        werkstoff.getBridgeMaterial(),
                        werkstoff.getBridgeMaterial().mHandleMaterial,
                        null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "XX ", "XXS", "XX ", 'X', gem.get(werkstoff.getBridgeMaterial()), 'S',
                        stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(toolHeadSaw, werkstoff.getBridgeMaterial(), 1L),
                GTProxy.tBits,
                new Object[] { "GGf", 'G', gem.get(werkstoff.getBridgeMaterial()) });
        }

        if (!werkstoff.hasItemType(plate)) return;

        // Disable recipe gen with handle Material for GT Materials
        if (!werkstoff.getGenerationFeatures()
            .isExtension()) {
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    IDMetaTool01.SCREWDRIVER.ID,
                    1,
                    werkstoff.getBridgeMaterial(),
                    werkstoff.getBridgeMaterial().mHandleMaterial,
                    null),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { " fS", " Sh", "W  ", 'S', stick.get(werkstoff.getBridgeMaterial()), 'W',
                    stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(toolHeadWrench, werkstoff.getBridgeMaterial(), 1L),
                GTProxy.tBits,
                new Object[] { "hXW", "XRX", "WXd", 'X', plate.get(werkstoff.getBridgeMaterial()), 'S',
                    plate.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'R',
                    ring.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'W',
                    screw.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            GTModHandler.addShapelessCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    IDMetaTool01.HARDHAMMER.ID,
                    1,
                    werkstoff.getBridgeMaterial(),
                    werkstoff.getBridgeMaterial().mHandleMaterial,
                    null),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { toolHeadHammer.get(werkstoff.getBridgeMaterial()),
                    stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    IDMetaTool01.FILE.ID,
                    1,
                    werkstoff.getBridgeMaterial(),
                    werkstoff.getBridgeMaterial().mHandleMaterial,
                    null),
                GTModHandler.RecipeBits.MIRRORED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                    | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "P", "P", "S", 'P', plate.get(werkstoff.getBridgeMaterial()), 'S',
                    stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            GTModHandler.addShapelessCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    IDMetaTool01.SAW.ID,
                    1,
                    werkstoff.getBridgeMaterial(),
                    werkstoff.getBridgeMaterial().mHandleMaterial,
                    null),
                new Object[] { toolHeadSaw.get(werkstoff.getBridgeMaterial()),
                    stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    IDMetaTool01.SOLDERING_IRON_LV.ID,
                    1,
                    werkstoff.getBridgeMaterial(),
                    Materials.Rubber,
                    new long[] { 100000L, 32L, 1L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', bolt.get(werkstoff.getBridgeMaterial()), 'P',
                    plate.get(Materials.AnyRubber), 'S', stick.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'L',
                    ItemList.Battery_RE_LV_Lithium.get(1L) });

            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_MV,
                    1,
                    werkstoff.getBridgeMaterial(),
                    Materials.Rubber,
                    new long[] { 400000L, 128L, 2L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', bolt.get(werkstoff.getBridgeMaterial()), 'P',
                    plate.get(Materials.AnyRubber), 'S', stick.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'L',
                    ItemList.Battery_RE_MV_Lithium.get(1L) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_HV,
                    1,
                    werkstoff.getBridgeMaterial(),
                    Materials.StyreneButadieneRubber,
                    new long[] { 1600000L, 512L, 3L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', bolt.get(werkstoff.getBridgeMaterial()), 'P',
                    plate.get(Materials.StyreneButadieneRubber), 'S',
                    stick.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'L',
                    ItemList.Battery_RE_HV_Lithium.get(1L) });

            if (!werkstoff.hasItemType(gem)) {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.HARDHAMMER.ID,
                        1,
                        werkstoff.getBridgeMaterial(),
                        werkstoff.getBridgeMaterial().mHandleMaterial,
                        null),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "XX ", "XXS", "XX ", 'X', ingot.get(werkstoff.getBridgeMaterial()), 'S',
                        stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            }
        }

        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.PLUNGER.ID,
                1,
                werkstoff.getBridgeMaterial(),
                werkstoff.getBridgeMaterial(),
                null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "xRR", " SR", "S f", 'S', stick.get(werkstoff.getBridgeMaterial()), 'R',
                plate.get(Materials.AnyRubber) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH.ID,
                1,
                werkstoff.getBridgeMaterial(),
                werkstoff.getBridgeMaterial(),
                null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "IhI", "III", " I ", 'I', ingot.get(werkstoff.getBridgeMaterial()) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.CROWBAR.ID,
                1,
                werkstoff.getBridgeMaterial(),
                werkstoff.getBridgeMaterial(),
                null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "hDS", "DSD", "SDf", 'S', stick.get(werkstoff.getBridgeMaterial()), 'D', Dyes.dyeBlue });

        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WIRECUTTER.ID,
                1,
                werkstoff.getBridgeMaterial(),
                werkstoff.getBridgeMaterial(),
                null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PfP", "hPd", "STS", 'S', stick.get(werkstoff.getBridgeMaterial()), 'P',
                plate.get(werkstoff.getBridgeMaterial()), 'T', screw.get(werkstoff.getBridgeMaterial()) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.SCOOP.ID,
                1,
                werkstoff.getBridgeMaterial(),
                werkstoff.getBridgeMaterial(),
                null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SWS", "SSS", "xSh", 'S', stick.get(werkstoff.getBridgeMaterial()), 'W',
                new ItemStack(Blocks.wool, 1, 32767) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.BRANCHCUTTER.ID,
                1,
                werkstoff.getBridgeMaterial(),
                werkstoff.getBridgeMaterial(),
                null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PfP", "PdP", "STS", 'S', stick.get(werkstoff.getBridgeMaterial()), 'P',
                plate.get(werkstoff.getBridgeMaterial()), 'T', screw.get(werkstoff.getBridgeMaterial()) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.KNIFE.ID,
                1,
                werkstoff.getBridgeMaterial(),
                werkstoff.getBridgeMaterial(),
                null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "fPh", " S ", 'S', stick.get(werkstoff.getBridgeMaterial()), 'P',
                plate.get(werkstoff.getBridgeMaterial()) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.BUTCHERYKNIFE.ID,
                1,
                werkstoff.getBridgeMaterial(),
                werkstoff.getBridgeMaterial(),
                null),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPf", "PP ", "Sh ", 'S', stick.get(werkstoff.getBridgeMaterial()), 'P',
                plate.get(werkstoff.getBridgeMaterial()) });

        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_LV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Steel,
                new long[] { 100000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.get(Materials.Steel), 'P', plate.get(Materials.Steel),
                'G', gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_LV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Steel,
                new long[] { 75000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.get(Materials.Steel), 'P', plate.get(Materials.Steel),
                'G', gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_LV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Steel,
                new long[] { 50000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.get(Materials.Steel), 'P', plate.get(Materials.Steel),
                'G', gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_MV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Aluminium,
                new long[] { 400000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.get(Materials.Aluminium), 'P',
                plate.get(Materials.Aluminium), 'G', gearGtSmall.get(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_MV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Aluminium,
                new long[] { 300000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.get(Materials.Aluminium), 'P',
                plate.get(Materials.Aluminium), 'G', gearGtSmall.get(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_MV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Aluminium,
                new long[] { 200000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.get(Materials.Aluminium), 'P',
                plate.get(Materials.Aluminium), 'G', gearGtSmall.get(Materials.Aluminium), 'B',
                ItemList.Battery_RE_MV_Sodium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_HV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.StainlessSteel,
                new long[] { 1600000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.get(Materials.StainlessSteel), 'P',
                plate.get(Materials.StainlessSteel), 'G', gearGtSmall.get(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_HV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.StainlessSteel,
                new long[] { 1200000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.get(Materials.StainlessSteel), 'P',
                plate.get(Materials.StainlessSteel), 'G', gearGtSmall.get(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.WRENCH_HV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.StainlessSteel,
                new long[] { 800000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.get(Materials.StainlessSteel), 'P',
                plate.get(Materials.StainlessSteel), 'G', gearGtSmall.get(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Sodium.get(1L) });

        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.SCREWDRIVER_LV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Steel,
                new long[] { 100000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', stickLong.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.get(Materials.Steel), 'P', plate.get(Materials.Steel),
                'G', gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.SCREWDRIVER_LV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Steel,
                new long[] { 75000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', stickLong.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.get(Materials.Steel), 'P', plate.get(Materials.Steel),
                'G', gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                IDMetaTool01.SCREWDRIVER_LV.ID,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Steel,
                new long[] { 50000L, 32L, 1L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', stickLong.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_LV.get(1L), 'S', screw.get(Materials.Steel), 'P', plate.get(Materials.Steel),
                'G', gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });

        GTModHandler.addCraftingRecipe(
            GTOreDictUnificator.get(toolHeadHammer, werkstoff.getBridgeMaterial(), 1L),
            GTProxy.tBits,
            new Object[] { "II ", "IIh", "II ", 'P', plate.get(werkstoff.getBridgeMaterial()), 'I',
                ingot.get(werkstoff.getBridgeMaterial()) });
        if (werkstoff.hasItemType(plateDouble) && werkstoff.hasItemType(cellMolten)) {
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(turbineBlade, werkstoff.getBridgeMaterial(), 1L),
                GTProxy.tBits,
                new Object[] { "fPd", "SPS", " P ", 'P', plateDouble.get(werkstoff.getBridgeMaterial()), 'S',
                    screw.get(werkstoff.getBridgeMaterial()) });

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot, 6), ItemList.Shape_Extruder_Turbine_Blade.get(0))
                .itemOutputs(werkstoff.get(turbineBlade, 1))
                .duration(
                    (int) werkstoff.getStats()
                        .getMass() / 2 * 20)
                .eut(TierEU.RECIPE_MV)
                .addTo(extruderRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Turbine_Blade.get(0))
                .itemOutputs(werkstoff.get(turbineBlade, 1))
                .fluidInputs(werkstoff.getMolten(864))
                .duration(
                    (int) werkstoff.getStats()
                        .getMass() * 20)
                .eut(TierEU.RECIPE_MV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(plateDouble, 3), werkstoff.get(screw, 2))
                .itemOutputs(werkstoff.get(turbineBlade, 1))
                .duration(
                    (werkstoff.getStats()
                        .getMass() / 4) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(turbineBlade, 4), GTOreDictUnificator.get(stickLong, Materials.Magnalium, 1))
                .itemOutputs(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.TURBINE_SMALL.ID,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Magnalium,
                        null))
                .duration(8 * SECONDS)
                .eut(100)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(turbineBlade, 8), GTOreDictUnificator.get(stickLong, Materials.Titanium, 1))
                .itemOutputs(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.TURBINE.ID,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Titanium,
                        null))
                .duration(16 * SECONDS)
                .eut(400)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    werkstoff.get(turbineBlade, 12),
                    GTOreDictUnificator.get(stickLong, Materials.TungstenSteel, 1))
                .itemOutputs(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.TURBINE_LARGE.ID,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.TungstenSteel,
                        null))
                .duration(32 * SECONDS)
                .eut(1600)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(turbineBlade, 16), GTOreDictUnificator.get(stickLong, Materials.Americium, 1))
                .itemOutputs(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.TURBINE_HUGE.ID,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Americium,
                        null))
                .duration(1 * MINUTES + 4 * SECONDS)
                .eut(6400)
                .addTo(assemblerRecipes);
        }

        if (!werkstoff.hasItemType(gem)) {
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(toolHeadSaw, werkstoff.getBridgeMaterial(), 1L),
                GTProxy.tBits,
                new Object[] { "PP ", "fh ", 'P', plate.get(werkstoff.getBridgeMaterial()), 'I',
                    ingot.get(werkstoff.getBridgeMaterial()) });
        }

        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_MV,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Aluminium,
                new long[] { 400000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', stickLong.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.get(Materials.Aluminium), 'P',
                plate.get(Materials.Aluminium), 'G', gearGtSmall.get(Materials.Titanium), 'B',
                ItemList.Battery_RE_MV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_MV,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Aluminium,
                new long[] { 300000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', stickLong.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.get(Materials.Aluminium), 'P',
                plate.get(Materials.Aluminium), 'G', gearGtSmall.get(Materials.Titanium), 'B',
                ItemList.Battery_RE_MV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_MV,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.Aluminium,
                new long[] { 200000L, 128L, 2L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', stickLong.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_MV.get(1L), 'S', screw.get(Materials.Aluminium), 'P',
                plate.get(Materials.Aluminium), 'G', gearGtSmall.get(Materials.Titanium), 'B',
                ItemList.Battery_RE_MV_Sodium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_HV,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.StainlessSteel,
                new long[] { 1600000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', stickLong.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.get(Materials.StainlessSteel), 'P',
                plate.get(Materials.StainlessSteel), 'G', gearGtSmall.get(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Lithium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_HV,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.StainlessSteel,
                new long[] { 1200000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', stickLong.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.get(Materials.StainlessSteel), 'P',
                plate.get(Materials.StainlessSteel), 'G', gearGtSmall.get(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Cadmium.get(1L) });
        GTModHandler.addCraftingRecipe(
            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                SCREWDRIVER_HV,
                1,
                werkstoff.getBridgeMaterial(),
                Materials.StainlessSteel,
                new long[] { 800000L, 512L, 3L, -1L }),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', stickLong.get(werkstoff.getBridgeMaterial()), 'M',
                ItemList.Electric_Motor_HV.get(1L), 'S', screw.get(Materials.StainlessSteel), 'P',
                plate.get(Materials.StainlessSteel), 'G', gearGtSmall.get(Materials.StainlessSteel), 'B',
                ItemList.Battery_RE_HV_Sodium.get(1L) });
    }
}
