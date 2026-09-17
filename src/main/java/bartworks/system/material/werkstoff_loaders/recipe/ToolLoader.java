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
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import bartworks.util.BWUtil;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;
import gregtech.common.items.tools.GTToolItems;
import gregtech.common.items.tools.ToolFileElectricItem;
import gregtech.common.items.tools.ToolMaterialIndex;
import gregtech.common.items.tools.ToolScrewdriverElectricItem;
import gregtech.common.items.tools.ToolWireCutterElectricItem;
import gregtech.common.items.tools.ToolWrenchElectricItem;

public class ToolLoader implements IWerkstoffRunnable {

    // GTNH-Specific
    public static final short SOLDERING_IRON_MV = 162;
    public static final short SOLDERING_IRON_HV = 164;

    @Override
    public void run(Werkstoff werkstoff) {
        if (werkstoff.getBridgeMaterial().mDurability == 0) return;

        if (werkstoff.hasItemType(gem)) {
            if (!werkstoff.getGenerationFeatures()
                .isExtension())
                GTModHandler.addCraftingRecipe(
                    registerWerkstoffHardHammer(werkstoff),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "XX ", "XXS", "XX ", 'X', gem.get(werkstoff.getBridgeMaterial()), 'S',
                        stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(toolHeadSaw, werkstoff.getBridgeMaterial(), 1L),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "GGf", 'G', gem.get(werkstoff.getBridgeMaterial()) });
        }

        if (!werkstoff.hasItemType(plate)) return;

        // Disable recipe gen with handle Material for GT Materials
        if (!werkstoff.getGenerationFeatures()
            .isExtension()) {
            ItemStack screwdriver = GTToolItems.SCREWDRIVER.registerMaterial(
                werkstoff.getBridgeMaterial(),
                ToolMaterialIndex.WERKSTOFF_META_OFFSET + werkstoff.getmID(),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
            if (screwdriver != null) {
                GTModHandler.addCraftingRecipe(
                    screwdriver,
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " fS", " Sh", "W  ", 'S', stick.get(werkstoff.getBridgeMaterial()), 'W',
                        stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            }
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(toolHeadWrench, werkstoff.getBridgeMaterial(), 1L),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "hXW", "XRX", "WXd", 'X', plate.get(werkstoff.getBridgeMaterial()), 'S',
                    plate.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'R',
                    ring.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'W',
                    screw.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            GTModHandler.addShapelessCraftingRecipe(
                registerWerkstoffHardHammer(werkstoff),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { toolHeadHammer.get(werkstoff.getBridgeMaterial()),
                    stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            ItemStack file = GTToolItems.FILE.registerMaterial(
                werkstoff.getBridgeMaterial(),
                ToolMaterialIndex.WERKSTOFF_META_OFFSET + werkstoff.getmID(),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
            if (file != null) {
                GTModHandler.addCraftingRecipe(
                    file,
                    GTModHandler.RecipeBits.MIRRORED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "P", "P", "S", 'P', plate.get(werkstoff.getBridgeMaterial()), 'S',
                        stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });
            }
            GTModHandler.addShapelessCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    IDMetaTool01.SAW.ID,
                    1,
                    werkstoff.getBridgeMaterial(),
                    werkstoff.getBridgeMaterial().mHandleMaterial,
                    null),
                new Object[] { toolHeadSaw.get(werkstoff.getBridgeMaterial()),
                    stick.get(werkstoff.getBridgeMaterial().mHandleMaterial) });

            // LV Soldering Iron
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
                    IDMetaTool01.SOLDERING_IRON_LV.ID,
                    1,
                    werkstoff.getBridgeMaterial(),
                    Materials.Rubber,
                    new long[] { 75000L, 32L, 1L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', bolt.get(werkstoff.getBridgeMaterial()), 'P',
                    plate.get(Materials.AnyRubber), 'S', stick.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'L',
                    ItemList.Battery_RE_LV_Cadmium.get(1L) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    IDMetaTool01.SOLDERING_IRON_LV.ID,
                    1,
                    werkstoff.getBridgeMaterial(),
                    Materials.Rubber,
                    new long[] { 50000L, 32L, 1L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', bolt.get(werkstoff.getBridgeMaterial()), 'P',
                    plate.get(Materials.AnyRubber), 'S', stick.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'L',
                    ItemList.Battery_RE_LV_Sodium.get(1L) });
            // MV Soldering Iron
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
                    SOLDERING_IRON_MV,
                    1,
                    werkstoff.getBridgeMaterial(),
                    Materials.Rubber,
                    new long[] { 300000L, 128L, 2L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', bolt.get(werkstoff.getBridgeMaterial()), 'P',
                    plate.get(Materials.AnyRubber), 'S', stick.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'L',
                    ItemList.Battery_RE_MV_Cadmium.get(1L) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_MV,
                    1,
                    werkstoff.getBridgeMaterial(),
                    Materials.Rubber,
                    new long[] { 200000L, 128L, 2L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', bolt.get(werkstoff.getBridgeMaterial()), 'P',
                    plate.get(Materials.AnyRubber), 'S', stick.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'L',
                    ItemList.Battery_RE_MV_Sodium.get(1L) });
            // HV Soldering Iron
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
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_HV,
                    1,
                    werkstoff.getBridgeMaterial(),
                    Materials.StyreneButadieneRubber,
                    new long[] { 1200000L, 512L, 3L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', bolt.get(werkstoff.getBridgeMaterial()), 'P',
                    plate.get(Materials.StyreneButadieneRubber), 'S',
                    stick.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'L',
                    ItemList.Battery_RE_HV_Cadmium.get(1L) });
            GTModHandler.addCraftingRecipe(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                    SOLDERING_IRON_HV,
                    1,
                    werkstoff.getBridgeMaterial(),
                    Materials.StyreneButadieneRubber,
                    new long[] { 800000L, 512L, 3L, -1L }),
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "LBf", "Sd ", "P  ", 'B', bolt.get(werkstoff.getBridgeMaterial()), 'P',
                    plate.get(Materials.StyreneButadieneRubber), 'S',
                    stick.get(werkstoff.getBridgeMaterial().mHandleMaterial), 'L',
                    ItemList.Battery_RE_HV_Sodium.get(1L) });

            if (!werkstoff.hasItemType(gem)) {
                GTModHandler.addCraftingRecipe(
                    registerWerkstoffHardHammer(werkstoff),
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
        // Werkstoff bridge materials are built at runtime and have no mMetaItemSubID, so their wrenches live in the
        // Werkstoff metadata band -- see ToolMaterialIndex.
        final int wrenchMeta = ToolMaterialIndex.WERKSTOFF_META_OFFSET + werkstoff.getmID();
        ItemStack wrench = GTToolItems.WRENCH.registerMaterial(
            werkstoff.getBridgeMaterial(),
            wrenchMeta,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
        if (wrench != null) {
            GTModHandler.addCraftingRecipe(
                wrench,
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "IhI", "III", " I ", 'I', ingot.get(werkstoff.getBridgeMaterial()) });
        }
        ItemStack crowbar = GTToolItems.CROWBAR.registerMaterial(
            werkstoff.getBridgeMaterial(),
            ToolMaterialIndex.WERKSTOFF_META_OFFSET + werkstoff.getmID(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.TELUM, 2L));
        if (crowbar != null) {
            GTModHandler.addCraftingRecipe(
                crowbar,
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "hDS", "DSD", "SDf", 'S', stick.get(werkstoff.getBridgeMaterial()), 'D', Dyes.dyeBlue });
        }

        ItemStack wireCutter = GTToolItems.WIRE_CUTTER.registerMaterial(
            werkstoff.getBridgeMaterial(),
            ToolMaterialIndex.WERKSTOFF_META_OFFSET + werkstoff.getmID(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
        if (wireCutter != null) {
            GTModHandler.addCraftingRecipe(
                wireCutter,
                GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "PfP", "hPd", "STS", 'S', stick.get(werkstoff.getBridgeMaterial()), 'P',
                    plate.get(werkstoff.getBridgeMaterial()), 'T', screw.get(werkstoff.getBridgeMaterial()) });
        }
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

        registerElectricWrench(GTToolItems.WRENCH_LV, werkstoff, wrenchMeta);
        registerElectricWrench(GTToolItems.WRENCH_MV, werkstoff, wrenchMeta);
        registerElectricWrench(GTToolItems.WRENCH_HV, werkstoff, wrenchMeta);
        addElectricWrenchRecipe(
            GTToolItems.WRENCH_LV,
            werkstoff,
            100000L,
            Materials.Steel,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Lithium);
        addElectricWrenchRecipe(
            GTToolItems.WRENCH_LV,
            werkstoff,
            75000L,
            Materials.Steel,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Cadmium);
        addElectricWrenchRecipe(
            GTToolItems.WRENCH_LV,
            werkstoff,
            50000L,
            Materials.Steel,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Sodium);
        addElectricWrenchRecipe(
            GTToolItems.WRENCH_MV,
            werkstoff,
            400000L,
            Materials.Aluminium,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Lithium);
        addElectricWrenchRecipe(
            GTToolItems.WRENCH_MV,
            werkstoff,
            300000L,
            Materials.Aluminium,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Cadmium);
        addElectricWrenchRecipe(
            GTToolItems.WRENCH_MV,
            werkstoff,
            200000L,
            Materials.Aluminium,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Sodium);
        addElectricWrenchRecipe(
            GTToolItems.WRENCH_HV,
            werkstoff,
            1600000L,
            Materials.StainlessSteel,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Lithium);
        addElectricWrenchRecipe(
            GTToolItems.WRENCH_HV,
            werkstoff,
            1200000L,
            Materials.StainlessSteel,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Cadmium);
        addElectricWrenchRecipe(
            GTToolItems.WRENCH_HV,
            werkstoff,
            800000L,
            Materials.StainlessSteel,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Sodium);

        // Electric wire cutters, built around a hand wire cutter of the same material.
        final int wireCutterMeta = ToolMaterialIndex.WERKSTOFF_META_OFFSET + werkstoff.getmID();
        registerElectricWireCutter(GTToolItems.WIRE_CUTTER_LV, werkstoff, wireCutterMeta);
        registerElectricWireCutter(GTToolItems.WIRE_CUTTER_MV, werkstoff, wireCutterMeta);
        registerElectricWireCutter(GTToolItems.WIRE_CUTTER_HV, werkstoff, wireCutterMeta);
        addElectricWireCutterRecipe(
            GTToolItems.WIRE_CUTTER_LV,
            werkstoff,
            100000L,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Lithium);
        addElectricWireCutterRecipe(
            GTToolItems.WIRE_CUTTER_LV,
            werkstoff,
            75000L,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Cadmium);
        addElectricWireCutterRecipe(
            GTToolItems.WIRE_CUTTER_LV,
            werkstoff,
            50000L,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Sodium);
        addElectricWireCutterRecipe(
            GTToolItems.WIRE_CUTTER_MV,
            werkstoff,
            400000L,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Lithium);
        addElectricWireCutterRecipe(
            GTToolItems.WIRE_CUTTER_MV,
            werkstoff,
            300000L,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Cadmium);
        addElectricWireCutterRecipe(
            GTToolItems.WIRE_CUTTER_MV,
            werkstoff,
            200000L,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Sodium);
        addElectricWireCutterRecipe(
            GTToolItems.WIRE_CUTTER_HV,
            werkstoff,
            1600000L,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Lithium);
        addElectricWireCutterRecipe(
            GTToolItems.WIRE_CUTTER_HV,
            werkstoff,
            1200000L,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Cadmium);
        addElectricWireCutterRecipe(
            GTToolItems.WIRE_CUTTER_HV,
            werkstoff,
            800000L,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Sodium);

        // Electric files.
        final int fileMeta = ToolMaterialIndex.WERKSTOFF_META_OFFSET + werkstoff.getmID();
        registerElectricFile(GTToolItems.FILE_LV, werkstoff, fileMeta);
        registerElectricFile(GTToolItems.FILE_MV, werkstoff, fileMeta);
        registerElectricFile(GTToolItems.FILE_HV, werkstoff, fileMeta);
        addElectricFileRecipe(
            GTToolItems.FILE_LV,
            werkstoff,
            100000L,
            Materials.Steel,
            ItemList.Component_Grinder_Diamond,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Lithium);
        addElectricFileRecipe(
            GTToolItems.FILE_LV,
            werkstoff,
            75000L,
            Materials.Steel,
            ItemList.Component_Grinder_Diamond,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Cadmium);
        addElectricFileRecipe(
            GTToolItems.FILE_LV,
            werkstoff,
            50000L,
            Materials.Steel,
            ItemList.Component_Grinder_Diamond,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Sodium);
        addElectricFileRecipe(
            GTToolItems.FILE_MV,
            werkstoff,
            400000L,
            Materials.Aluminium,
            ItemList.Component_Grinder_Diamond,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Lithium);
        addElectricFileRecipe(
            GTToolItems.FILE_MV,
            werkstoff,
            300000L,
            Materials.Aluminium,
            ItemList.Component_Grinder_Diamond,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Cadmium);
        addElectricFileRecipe(
            GTToolItems.FILE_MV,
            werkstoff,
            200000L,
            Materials.Aluminium,
            ItemList.Component_Grinder_Diamond,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Sodium);
        addElectricFileRecipe(
            GTToolItems.FILE_HV,
            werkstoff,
            1600000L,
            Materials.StainlessSteel,
            ItemList.Component_Grinder_Tungsten,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Lithium);
        addElectricFileRecipe(
            GTToolItems.FILE_HV,
            werkstoff,
            1200000L,
            Materials.StainlessSteel,
            ItemList.Component_Grinder_Tungsten,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Cadmium);
        addElectricFileRecipe(
            GTToolItems.FILE_HV,
            werkstoff,
            800000L,
            Materials.StainlessSteel,
            ItemList.Component_Grinder_Tungsten,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Sodium);

        final int screwdriverMeta = ToolMaterialIndex.WERKSTOFF_META_OFFSET + werkstoff.getmID();
        registerElectricScrewdriver(GTToolItems.SCREWDRIVER_LV, werkstoff, screwdriverMeta);
        registerElectricScrewdriver(GTToolItems.SCREWDRIVER_MV, werkstoff, screwdriverMeta);
        registerElectricScrewdriver(GTToolItems.SCREWDRIVER_HV, werkstoff, screwdriverMeta);
        addElectricScrewdriverRecipe(
            GTToolItems.SCREWDRIVER_LV,
            werkstoff,
            100000L,
            Materials.Steel,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Lithium);
        addElectricScrewdriverRecipe(
            GTToolItems.SCREWDRIVER_LV,
            werkstoff,
            75000L,
            Materials.Steel,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Cadmium);
        addElectricScrewdriverRecipe(
            GTToolItems.SCREWDRIVER_LV,
            werkstoff,
            50000L,
            Materials.Steel,
            ItemList.Electric_Motor_LV,
            ItemList.Battery_RE_LV_Sodium);
        addElectricScrewdriverRecipe(
            GTToolItems.SCREWDRIVER_MV,
            werkstoff,
            400000L,
            Materials.Aluminium,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Lithium);
        addElectricScrewdriverRecipe(
            GTToolItems.SCREWDRIVER_MV,
            werkstoff,
            300000L,
            Materials.Aluminium,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Cadmium);
        addElectricScrewdriverRecipe(
            GTToolItems.SCREWDRIVER_MV,
            werkstoff,
            200000L,
            Materials.Aluminium,
            ItemList.Electric_Motor_MV,
            ItemList.Battery_RE_MV_Sodium);
        addElectricScrewdriverRecipe(
            GTToolItems.SCREWDRIVER_HV,
            werkstoff,
            1600000L,
            Materials.StainlessSteel,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Lithium);
        addElectricScrewdriverRecipe(
            GTToolItems.SCREWDRIVER_HV,
            werkstoff,
            1200000L,
            Materials.StainlessSteel,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Cadmium);
        addElectricScrewdriverRecipe(
            GTToolItems.SCREWDRIVER_HV,
            werkstoff,
            800000L,
            Materials.StainlessSteel,
            ItemList.Electric_Motor_HV,
            ItemList.Battery_RE_HV_Sodium);

        GTModHandler.addCraftingRecipe(
            GTOreDictUnificator.get(toolHeadHammer, werkstoff.getBridgeMaterial(), 1L),
            GTModHandler.RecipeBits.BITS_STD,
            new Object[] { "II ", "IIh", "II ", 'P', plate.get(werkstoff.getBridgeMaterial()), 'I',
                ingot.get(werkstoff.getBridgeMaterial()) });
        if (werkstoff.hasItemType(plateDouble) && werkstoff.hasItemType(cellMolten)) {
            int voltageMultiplier = werkstoff.getStats()
                .getMeltingPoint() >= 2800 ? 60 : 15;
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(turbineBlade, werkstoff.getBridgeMaterial(), 1L),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "fPd", "SPS", " P ", 'P', plateDouble.get(werkstoff.getBridgeMaterial()), 'S',
                    screw.get(werkstoff.getBridgeMaterial()) });

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(ingot, 6), ItemList.Shape_Extruder_Turbine_Blade.get(0))
                .itemOutputs(werkstoff.get(turbineBlade, 1))
                .duration(
                    (int) Math.max(
                        werkstoff.getStats()
                            .getMass(),
                        1L))
                .eut(BWUtil.calculateRecipeEU(werkstoff, 8 * voltageMultiplier))
                .addTo(extruderRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Turbine_Blade.get(0))
                .itemOutputs(werkstoff.get(turbineBlade, 1))
                .fluidInputs(werkstoff.getMolten(6 * INGOTS))
                .duration(20 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 8 * voltageMultiplier))
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(werkstoff.get(plateDouble, 3), werkstoff.get(screw, 2))
                .itemOutputs(werkstoff.get(turbineBlade, 1))
                .duration(
                    (werkstoff.getStats()
                        .getMass() / 4) * SECONDS)
                .eut(BWUtil.calculateRecipeEU(werkstoff, 32))
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
                .eut(BWUtil.calculateRecipeEU(werkstoff, 100))
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
                .eut(BWUtil.calculateRecipeEU(werkstoff, 400))
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
                .eut(BWUtil.calculateRecipeEU(werkstoff, 1600))
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
                .eut(BWUtil.calculateRecipeEU(werkstoff, 6400))
                .addTo(assemblerRecipes);
        }

        if (!werkstoff.hasItemType(gem)) {
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(toolHeadSaw, werkstoff.getBridgeMaterial(), 1L),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "PP ", "fh ", 'P', plate.get(werkstoff.getBridgeMaterial()), 'I',
                    ingot.get(werkstoff.getBridgeMaterial()) });
        }
    }

    /**
     * Gives this Werkstoff's electric wrench of the given tier its metadata slot and Thaumcraft aspects. Must run
     * before {@link #addElectricWrenchRecipe} for that tier.
     */
    private static void registerElectricWrench(ToolWrenchElectricItem wrenchItem, Werkstoff werkstoff, int meta) {
        wrenchItem.registerMaterial(
            werkstoff.getBridgeMaterial(),
            meta,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
    }

    /**
     * Adds one electric wrench crafting recipe for a Werkstoff head material. Mirrors the GregTech-material version in
     * {@code ProcessingToolHead}; the only difference is where the head comes from.
     */
    private static void addElectricWrenchRecipe(ToolWrenchElectricItem wrenchItem, Werkstoff werkstoff, long maxCharge,
        Materials casingMaterial, ItemList motor, ItemList battery) {
        ItemStack wrench = wrenchItem.getToolWithMaterial(werkstoff.getBridgeMaterial(), maxCharge);
        if (wrench == null) return;
        GTModHandler.addCraftingRecipe(
            wrench,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', toolHeadWrench.get(werkstoff.getBridgeMaterial()), 'M',
                motor.get(1L), 'S', screw.get(casingMaterial), 'P', plate.get(casingMaterial), 'G',
                gearGtSmall.get(casingMaterial), 'B', battery.get(1L) });
    }

    private static void registerElectricScrewdriver(ToolScrewdriverElectricItem screwdriverItem, Werkstoff werkstoff,
        int meta) {
        screwdriverItem.registerMaterial(
            werkstoff.getBridgeMaterial(),
            meta,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
    }

    /**
     * Adds one electric screwdriver crafting recipe for a Werkstoff head material. Mirrors the GregTech-material
     * version in {@code ProcessingToolHead}; the only difference is where the head comes from.
     */
    private static void addElectricScrewdriverRecipe(ToolScrewdriverElectricItem screwdriverItem, Werkstoff werkstoff,
        long maxCharge, Materials casingMaterial, ItemList motor, ItemList battery) {
        ItemStack screwdriver = screwdriverItem.getToolWithMaterial(werkstoff.getBridgeMaterial(), maxCharge);
        if (screwdriver == null) return;
        GTModHandler.addCraftingRecipe(
            screwdriver,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', stickLong.get(werkstoff.getBridgeMaterial()), 'M', motor.get(1L),
                'S', screw.get(casingMaterial), 'P', plate.get(casingMaterial), 'G', gearGtSmall.get(casingMaterial),
                'B', battery.get(1L) });
    }

    private static void registerElectricWireCutter(ToolWireCutterElectricItem wireCutterItem, Werkstoff werkstoff,
        int meta) {
        wireCutterItem.registerMaterial(
            werkstoff.getBridgeMaterial(),
            meta,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 3));
    }

    /**
     * Adds one electric wire cutter crafting recipe for a Werkstoff head material. Mirrors the GregTech-material
     * version in {@code ProcessingToolHead}; the only difference is where the head comes from.
     */
    private static void addElectricWireCutterRecipe(ToolWireCutterElectricItem wireCutterItem, Werkstoff werkstoff,
        long maxCharge, ItemList motor, ItemList battery) {
        ItemStack wireCutter = wireCutterItem.getToolWithMaterial(werkstoff.getBridgeMaterial(), maxCharge);
        ItemStack handWireCutter = GTToolItems.WIRE_CUTTER.getToolWithMaterial(werkstoff.getBridgeMaterial());
        if (wireCutter == null || handWireCutter == null) return;
        GTModHandler.addCraftingRecipe(
            wireCutter,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X', handWireCutter, 'M', motor.get(1L), 'S',
                wireFine.get(Materials.Electrum), 'P', plate.get(werkstoff.getBridgeMaterial()), 'G',
                gearGt.get(Materials.Steel), 'B', battery.get(1L) });
    }

    /**
     * Declares the hard hammer for this Werkstoff and returns a stack of it, or null if it has no metadata slot.
     * Called once per recipe; registerMaterial is idempotent, and each recipe wants its own stack because
     * addCraftingRecipe can edit the one it is handed.
     */
    private static ItemStack registerWerkstoffHardHammer(Werkstoff werkstoff) {
        return GTToolItems.HARD_HAMMER.registerMaterial(
            werkstoff.getBridgeMaterial(),
            ToolMaterialIndex.WERKSTOFF_META_OFFSET + werkstoff.getmID(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
    }

    private static void registerElectricFile(ToolFileElectricItem fileItem, Werkstoff werkstoff, int meta) {
        fileItem.registerMaterial(
            werkstoff.getBridgeMaterial(),
            meta,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
    }

    /**
     * Adds one electric file crafting recipe for a Werkstoff head material. Mirrors the GregTech-material version in
     * {@code ProcessingToolHead}; the only difference is where the head comes from.
     */
    private static void addElectricFileRecipe(ToolFileElectricItem fileItem, Werkstoff werkstoff, long maxCharge,
        Materials casingMaterial, ItemList grinder, ItemList motor, ItemList battery) {
        ItemStack file = fileItem.getToolWithMaterial(werkstoff.getBridgeMaterial(), maxCharge);
        if (file == null) return;
        GTModHandler.addCraftingRecipe(
            file,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', grinder.get(1), 'M', motor.get(1L), 'S',
                OrePrefixes.screw.get(casingMaterial), 'L', OrePrefixes.stickLong.get(werkstoff.getBridgeMaterial()),
                'P', OrePrefixes.plate.get(werkstoff.getBridgeMaterial()), 'G', OrePrefixes.gearGt.get(casingMaterial),
                'B', battery.get(1L) });
    }
}
