/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe;

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.gtnhGT;
import static gregtech.api.enums.OrePrefixes.*;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.GT_Proxy;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ToolLoader implements IWerkstoffRunnable {

    // GTNH-Specific
    public static final short SCREWDRIVER_MV = 152;
    public static final short SCREWDRIVER_HV = 154;
    public static final short SOLDERING_IRON_MV = 162;
    public static final short SOLDERING_IRON_HV = 164;

    public void run(Werkstoff werkstoff) {
        if (werkstoff.getBridgeMaterial().mDurability == 0) return;

        if (werkstoff.hasItemType(gem)) {
            if (!werkstoff.getGenerationFeatures().isExtension())
                GT_ModHandler.addCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.HARDHAMMER,
                                1,
                                werkstoff.getBridgeMaterial(),
                                werkstoff.getBridgeMaterial().mHandleMaterial,
                                null),
                        GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] {
                            "XX ",
                            "XXS",
                            "XX ",
                            'X',
                            gem.get(werkstoff.getBridgeMaterial()),
                            'S',
                            stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)
                        });
            GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(toolHeadSaw, werkstoff.getBridgeMaterial(), 1L),
                    GT_Proxy.tBits,
                    new Object[] {"GGf", 'G', gem.get(werkstoff.getBridgeMaterial())});
        }

        if (!werkstoff.hasItemType(plate)) return;

        // Disable recipe gen with handle Material for GT Materials
        if (!werkstoff.getGenerationFeatures().isExtension()) {
            GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.SCREWDRIVER,
                            1,
                            werkstoff.getBridgeMaterial(),
                            werkstoff.getBridgeMaterial().mHandleMaterial,
                            null),
                    GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] {
                        " fS",
                        " Sh",
                        "W  ",
                        'S',
                        stick.get(werkstoff.getBridgeMaterial()),
                        'W',
                        stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)
                    });
            GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(toolHeadWrench, werkstoff.getBridgeMaterial(), 1L),
                    GT_Proxy.tBits,
                    new Object[] {
                        "hXW",
                        "XRX",
                        "WXd",
                        'X',
                        plate.get(werkstoff.getBridgeMaterial()),
                        'S',
                        plate.get(werkstoff.getBridgeMaterial().mHandleMaterial),
                        'R',
                        ring.get(werkstoff.getBridgeMaterial().mHandleMaterial),
                        'W',
                        screw.get(werkstoff.getBridgeMaterial().mHandleMaterial)
                    });
            GT_ModHandler.addShapelessCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.HARDHAMMER,
                            1,
                            werkstoff.getBridgeMaterial(),
                            werkstoff.getBridgeMaterial().mHandleMaterial,
                            null),
                    GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] {
                        toolHeadHammer.get(werkstoff.getBridgeMaterial()),
                        stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)
                    });
            GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.FILE,
                            1,
                            werkstoff.getBridgeMaterial(),
                            werkstoff.getBridgeMaterial().mHandleMaterial,
                            null),
                    GT_ModHandler.RecipeBits.MIRRORED
                            | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] {
                        "P",
                        "P",
                        "S",
                        'P',
                        plate.get(werkstoff.getBridgeMaterial()),
                        'S',
                        stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)
                    });
            GT_ModHandler.addShapelessCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.SAW,
                            1,
                            werkstoff.getBridgeMaterial(),
                            werkstoff.getBridgeMaterial().mHandleMaterial,
                            null),
                    new Object[] {
                        toolHeadSaw.get(werkstoff.getBridgeMaterial()),
                        stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)
                    });
            GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.SOLDERING_IRON_LV,
                            1,
                            werkstoff.getBridgeMaterial(),
                            Materials.Rubber,
                            new long[] {100000L, 32L, 1L, -1L}),
                    GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] {
                        "LBf",
                        "Sd ",
                        "P  ",
                        'B',
                        bolt.get(werkstoff.getBridgeMaterial()),
                        'P',
                        plate.get(Materials.AnyRubber),
                        'S',
                        stick.get(werkstoff.getBridgeMaterial().mHandleMaterial),
                        'L',
                        ItemList.Battery_RE_LV_Lithium.get(1L)
                    });

            if (gtnhGT) {
                GT_ModHandler.addCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                SOLDERING_IRON_MV, 1, werkstoff.getBridgeMaterial(), Materials.Rubber, new long[] {
                                    400000L, 128L, 2L, -1L
                                }),
                        GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] {
                            "LBf",
                            "Sd ",
                            "P  ",
                            'B',
                            bolt.get(werkstoff.getBridgeMaterial()),
                            'P',
                            plate.get(Materials.AnyRubber),
                            'S',
                            stick.get(werkstoff.getBridgeMaterial().mHandleMaterial),
                            'L',
                            ItemList.Battery_RE_MV_Lithium.get(1L)
                        });
                GT_ModHandler.addCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                SOLDERING_IRON_HV,
                                1,
                                werkstoff.getBridgeMaterial(),
                                Materials.StyreneButadieneRubber,
                                new long[] {1600000L, 512L, 3L, -1L}),
                        GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] {
                            "LBf",
                            "Sd ",
                            "P  ",
                            'B',
                            bolt.get(werkstoff.getBridgeMaterial()),
                            'P',
                            plate.get(Materials.StyreneButadieneRubber),
                            'S',
                            stick.get(werkstoff.getBridgeMaterial().mHandleMaterial),
                            'L',
                            ItemList.Battery_RE_HV_Lithium.get(1L)
                        });
            }

            if (!werkstoff.hasItemType(gem)) {
                GT_ModHandler.addCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.HARDHAMMER,
                                1,
                                werkstoff.getBridgeMaterial(),
                                werkstoff.getBridgeMaterial().mHandleMaterial,
                                null),
                        GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] {
                            "XX ",
                            "XXS",
                            "XX ",
                            'X',
                            ingot.get(werkstoff.getBridgeMaterial()),
                            'S',
                            stick.get(werkstoff.getBridgeMaterial().mHandleMaterial)
                        });
            }
        }

        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.PLUNGER,
                        1,
                        werkstoff.getBridgeMaterial(),
                        werkstoff.getBridgeMaterial(),
                        null),
                GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "xRR",
                    " SR",
                    "S f",
                    'S',
                    stick.get(werkstoff.getBridgeMaterial()),
                    'R',
                    plate.get(Materials.AnyRubber)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH,
                        1,
                        werkstoff.getBridgeMaterial(),
                        werkstoff.getBridgeMaterial(),
                        null),
                GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {"IhI", "III", " I ", 'I', ingot.get(werkstoff.getBridgeMaterial())});
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.CROWBAR,
                        1,
                        werkstoff.getBridgeMaterial(),
                        werkstoff.getBridgeMaterial(),
                        null),
                GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {"hDS", "DSD", "SDf", 'S', stick.get(werkstoff.getBridgeMaterial()), 'D', Dyes.dyeBlue});

        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WIRECUTTER,
                        1,
                        werkstoff.getBridgeMaterial(),
                        werkstoff.getBridgeMaterial(),
                        null),
                GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "PfP",
                    "hPd",
                    "STS",
                    'S',
                    stick.get(werkstoff.getBridgeMaterial()),
                    'P',
                    plate.get(werkstoff.getBridgeMaterial()),
                    'T',
                    screw.get(werkstoff.getBridgeMaterial())
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCOOP,
                        1,
                        werkstoff.getBridgeMaterial(),
                        werkstoff.getBridgeMaterial(),
                        null),
                GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "SWS",
                    "SSS",
                    "xSh",
                    'S',
                    stick.get(werkstoff.getBridgeMaterial()),
                    'W',
                    new ItemStack(Blocks.wool, 1, 32767)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BRANCHCUTTER,
                        1,
                        werkstoff.getBridgeMaterial(),
                        werkstoff.getBridgeMaterial(),
                        null),
                GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "PfP",
                    "PdP",
                    "STS",
                    'S',
                    stick.get(werkstoff.getBridgeMaterial()),
                    'P',
                    plate.get(werkstoff.getBridgeMaterial()),
                    'T',
                    screw.get(werkstoff.getBridgeMaterial())
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.KNIFE,
                        1,
                        werkstoff.getBridgeMaterial(),
                        werkstoff.getBridgeMaterial(),
                        null),
                GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "fPh",
                    " S ",
                    'S',
                    stick.get(werkstoff.getBridgeMaterial()),
                    'P',
                    plate.get(werkstoff.getBridgeMaterial())
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BUTCHERYKNIFE,
                        1,
                        werkstoff.getBridgeMaterial(),
                        werkstoff.getBridgeMaterial(),
                        null),
                GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "PPf",
                    "PP ",
                    "Sh ",
                    'S',
                    stick.get(werkstoff.getBridgeMaterial()),
                    'P',
                    plate.get(werkstoff.getBridgeMaterial())
                });

        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_LV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Steel,
                        new long[] {100000L, 32L, 1L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "SXd",
                    "GMG",
                    "PBP",
                    'X',
                    toolHeadWrench.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_LV.get(1L),
                    'S',
                    screw.get(Materials.Steel),
                    'P',
                    plate.get(Materials.Steel),
                    'G',
                    gearGtSmall.get(Materials.Steel),
                    'B',
                    ItemList.Battery_RE_LV_Lithium.get(1L)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_LV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Steel,
                        new long[] {75000L, 32L, 1L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "SXd",
                    "GMG",
                    "PBP",
                    'X',
                    toolHeadWrench.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_LV.get(1L),
                    'S',
                    screw.get(Materials.Steel),
                    'P',
                    plate.get(Materials.Steel),
                    'G',
                    gearGtSmall.get(Materials.Steel),
                    'B',
                    ItemList.Battery_RE_LV_Cadmium.get(1L)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_LV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Steel,
                        new long[] {50000L, 32L, 1L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "SXd",
                    "GMG",
                    "PBP",
                    'X',
                    toolHeadWrench.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_LV.get(1L),
                    'S',
                    screw.get(Materials.Steel),
                    'P',
                    plate.get(Materials.Steel),
                    'G',
                    gearGtSmall.get(Materials.Steel),
                    'B',
                    ItemList.Battery_RE_LV_Sodium.get(1L)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_MV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Aluminium,
                        new long[] {400000L, 128L, 2L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "SXd",
                    "GMG",
                    "PBP",
                    'X',
                    toolHeadWrench.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_MV.get(1L),
                    'S',
                    screw.get(Materials.Aluminium),
                    'P',
                    plate.get(Materials.Aluminium),
                    'G',
                    gearGtSmall.get(Materials.Aluminium),
                    'B',
                    ItemList.Battery_RE_MV_Lithium.get(1L)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_MV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Aluminium,
                        new long[] {300000L, 128L, 2L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "SXd",
                    "GMG",
                    "PBP",
                    'X',
                    toolHeadWrench.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_MV.get(1L),
                    'S',
                    screw.get(Materials.Aluminium),
                    'P',
                    plate.get(Materials.Aluminium),
                    'G',
                    gearGtSmall.get(Materials.Aluminium),
                    'B',
                    ItemList.Battery_RE_MV_Cadmium.get(1L)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_MV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Aluminium,
                        new long[] {200000L, 128L, 2L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "SXd",
                    "GMG",
                    "PBP",
                    'X',
                    toolHeadWrench.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_MV.get(1L),
                    'S',
                    screw.get(Materials.Aluminium),
                    'P',
                    plate.get(Materials.Aluminium),
                    'G',
                    gearGtSmall.get(Materials.Aluminium),
                    'B',
                    ItemList.Battery_RE_MV_Sodium.get(1L)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_HV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.StainlessSteel,
                        new long[] {1600000L, 512L, 3L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "SXd",
                    "GMG",
                    "PBP",
                    'X',
                    toolHeadWrench.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_HV.get(1L),
                    'S',
                    screw.get(Materials.StainlessSteel),
                    'P',
                    plate.get(Materials.StainlessSteel),
                    'G',
                    gearGtSmall.get(Materials.StainlessSteel),
                    'B',
                    ItemList.Battery_RE_HV_Lithium.get(1L)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_HV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.StainlessSteel,
                        new long[] {1200000L, 512L, 3L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "SXd",
                    "GMG",
                    "PBP",
                    'X',
                    toolHeadWrench.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_HV.get(1L),
                    'S',
                    screw.get(Materials.StainlessSteel),
                    'P',
                    plate.get(Materials.StainlessSteel),
                    'G',
                    gearGtSmall.get(Materials.StainlessSteel),
                    'B',
                    ItemList.Battery_RE_HV_Cadmium.get(1L)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_HV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.StainlessSteel,
                        new long[] {800000L, 512L, 3L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "SXd",
                    "GMG",
                    "PBP",
                    'X',
                    toolHeadWrench.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_HV.get(1L),
                    'S',
                    screw.get(Materials.StainlessSteel),
                    'P',
                    plate.get(Materials.StainlessSteel),
                    'G',
                    gearGtSmall.get(Materials.StainlessSteel),
                    'B',
                    ItemList.Battery_RE_HV_Sodium.get(1L)
                });

        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_LV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Steel,
                        new long[] {100000L, 32L, 1L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "PdX",
                    "MGS",
                    "GBP",
                    'X',
                    stickLong.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_LV.get(1L),
                    'S',
                    screw.get(Materials.Steel),
                    'P',
                    plate.get(Materials.Steel),
                    'G',
                    gearGtSmall.get(Materials.Steel),
                    'B',
                    ItemList.Battery_RE_LV_Lithium.get(1L)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_LV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Steel,
                        new long[] {75000L, 32L, 1L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "PdX",
                    "MGS",
                    "GBP",
                    'X',
                    stickLong.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_LV.get(1L),
                    'S',
                    screw.get(Materials.Steel),
                    'P',
                    plate.get(Materials.Steel),
                    'G',
                    gearGtSmall.get(Materials.Steel),
                    'B',
                    ItemList.Battery_RE_LV_Cadmium.get(1L)
                });
        GT_ModHandler.addCraftingRecipe(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_LV,
                        1,
                        werkstoff.getBridgeMaterial(),
                        Materials.Steel,
                        new long[] {50000L, 32L, 1L, -1L}),
                GT_ModHandler.RecipeBits.DISMANTLEABLE
                        | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] {
                    "PdX",
                    "MGS",
                    "GBP",
                    'X',
                    stickLong.get(werkstoff.getBridgeMaterial()),
                    'M',
                    ItemList.Electric_Motor_LV.get(1L),
                    'S',
                    screw.get(Materials.Steel),
                    'P',
                    plate.get(Materials.Steel),
                    'G',
                    gearGtSmall.get(Materials.Steel),
                    'B',
                    ItemList.Battery_RE_LV_Sodium.get(1L)
                });

        GT_ModHandler.addCraftingRecipe(
                GT_OreDictUnificator.get(toolHeadHammer, werkstoff.getBridgeMaterial(), 1L),
                GT_Proxy.tBits,
                new Object[] {
                    "II ",
                    "IIh",
                    "II ",
                    'P',
                    plate.get(werkstoff.getBridgeMaterial()),
                    'I',
                    ingot.get(werkstoff.getBridgeMaterial())
                });
        if (werkstoff.hasItemType(plateDouble) && werkstoff.hasItemType(cellMolten)) {
            GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(turbineBlade, werkstoff.getBridgeMaterial(), 1L),
                    GT_Proxy.tBits,
                    new Object[] {
                        "fPd",
                        "SPS",
                        " P ",
                        'P',
                        plateDouble.get(werkstoff.getBridgeMaterial()),
                        'S',
                        screw.get(werkstoff.getBridgeMaterial())
                    });
            GT_Values.RA.addExtruderRecipe(
                    werkstoff.get(ingot, 6),
                    ItemList.Shape_Extruder_Turbine_Blade.get(0),
                    werkstoff.get(turbineBlade, 1),
                    (int) werkstoff.getStats().getMass() / 2 * 20,
                    120);
            GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Turbine_Blade.get(0),
                    werkstoff.getMolten(864),
                    werkstoff.get(turbineBlade, 1),
                    (int) werkstoff.getStats().getMass() * 20,
                    120);
            GT_Values.RA.addFormingPressRecipe(
                    werkstoff.get(plateDouble, 3),
                    werkstoff.get(screw, 2),
                    werkstoff.get(turbineBlade, 1),
                    (int) werkstoff.getStats().getMass() / 4 * 20,
                    30);
            GT_Values.RA.addAssemblerRecipe(
                    werkstoff.get(turbineBlade, 4),
                    GT_OreDictUnificator.get(stickLong, Materials.Magnalium, 1),
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.TURBINE_SMALL,
                            1,
                            werkstoff.getBridgeMaterial(),
                            Materials.Magnalium,
                            null),
                    160,
                    100);
            GT_Values.RA.addAssemblerRecipe(
                    werkstoff.get(turbineBlade, 8),
                    GT_OreDictUnificator.get(stickLong, Materials.Titanium, 1),
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.TURBINE,
                            1,
                            werkstoff.getBridgeMaterial(),
                            Materials.Titanium,
                            null),
                    320,
                    400);
            GT_Values.RA.addAssemblerRecipe(
                    werkstoff.get(turbineBlade, 12),
                    GT_OreDictUnificator.get(stickLong, Materials.TungstenSteel, 1),
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.TURBINE_LARGE,
                            1,
                            werkstoff.getBridgeMaterial(),
                            Materials.TungstenSteel,
                            null),
                    640,
                    1600);
            GT_Values.RA.addAssemblerRecipe(
                    werkstoff.get(turbineBlade, 16),
                    GT_OreDictUnificator.get(stickLong, Materials.Americium, 1),
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.TURBINE_HUGE,
                            1,
                            werkstoff.getBridgeMaterial(),
                            Materials.Americium,
                            null),
                    1280,
                    6400);
        }

        if (!werkstoff.hasItemType(gem)) {
            GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(toolHeadSaw, werkstoff.getBridgeMaterial(), 1L),
                    GT_Proxy.tBits,
                    new Object[] {
                        "PP ",
                        "fh ",
                        'P',
                        plate.get(werkstoff.getBridgeMaterial()),
                        'I',
                        ingot.get(werkstoff.getBridgeMaterial())
                    });
        }

        if (gtnhGT) {
            GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            SCREWDRIVER_MV, 1, werkstoff.getBridgeMaterial(), Materials.Aluminium, new long[] {
                                400000L, 128L, 2L, -1L
                            }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE
                            | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] {
                        "PdX",
                        "MGS",
                        "GBP",
                        'X',
                        stickLong.get(werkstoff.getBridgeMaterial()),
                        'M',
                        ItemList.Electric_Motor_MV.get(1L),
                        'S',
                        screw.get(Materials.Aluminium),
                        'P',
                        plate.get(Materials.Aluminium),
                        'G',
                        gearGtSmall.get(Materials.Titanium),
                        'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L)
                    });
            GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            SCREWDRIVER_MV, 1, werkstoff.getBridgeMaterial(), Materials.Aluminium, new long[] {
                                300000L, 128L, 2L, -1L
                            }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE
                            | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] {
                        "PdX",
                        "MGS",
                        "GBP",
                        'X',
                        stickLong.get(werkstoff.getBridgeMaterial()),
                        'M',
                        ItemList.Electric_Motor_MV.get(1L),
                        'S',
                        screw.get(Materials.Aluminium),
                        'P',
                        plate.get(Materials.Aluminium),
                        'G',
                        gearGtSmall.get(Materials.Titanium),
                        'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L)
                    });
            GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            SCREWDRIVER_MV, 1, werkstoff.getBridgeMaterial(), Materials.Aluminium, new long[] {
                                200000L, 128L, 2L, -1L
                            }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE
                            | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] {
                        "PdX",
                        "MGS",
                        "GBP",
                        'X',
                        stickLong.get(werkstoff.getBridgeMaterial()),
                        'M',
                        ItemList.Electric_Motor_MV.get(1L),
                        'S',
                        screw.get(Materials.Aluminium),
                        'P',
                        plate.get(Materials.Aluminium),
                        'G',
                        gearGtSmall.get(Materials.Titanium),
                        'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L)
                    });
            GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            SCREWDRIVER_HV, 1, werkstoff.getBridgeMaterial(), Materials.StainlessSteel, new long[] {
                                1600000L, 512L, 3L, -1L
                            }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE
                            | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] {
                        "PdX",
                        "MGS",
                        "GBP",
                        'X',
                        stickLong.get(werkstoff.getBridgeMaterial()),
                        'M',
                        ItemList.Electric_Motor_HV.get(1L),
                        'S',
                        screw.get(Materials.StainlessSteel),
                        'P',
                        plate.get(Materials.StainlessSteel),
                        'G',
                        gearGtSmall.get(Materials.StainlessSteel),
                        'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L)
                    });
            GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            SCREWDRIVER_HV, 1, werkstoff.getBridgeMaterial(), Materials.StainlessSteel, new long[] {
                                1200000L, 512L, 3L, -1L
                            }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE
                            | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] {
                        "PdX",
                        "MGS",
                        "GBP",
                        'X',
                        stickLong.get(werkstoff.getBridgeMaterial()),
                        'M',
                        ItemList.Electric_Motor_HV.get(1L),
                        'S',
                        screw.get(Materials.StainlessSteel),
                        'P',
                        plate.get(Materials.StainlessSteel),
                        'G',
                        gearGtSmall.get(Materials.StainlessSteel),
                        'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L)
                    });
            GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            SCREWDRIVER_HV, 1, werkstoff.getBridgeMaterial(), Materials.StainlessSteel, new long[] {
                                800000L, 512L, 3L, -1L
                            }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE
                            | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] {
                        "PdX",
                        "MGS",
                        "GBP",
                        'X',
                        stickLong.get(werkstoff.getBridgeMaterial()),
                        'M',
                        ItemList.Electric_Motor_HV.get(1L),
                        'S',
                        screw.get(Materials.StainlessSteel),
                        'P',
                        plate.get(Materials.StainlessSteel),
                        'G',
                        gearGtSmall.get(Materials.StainlessSteel),
                        'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L)
                    });
        }

        // GT_ModHandler.addCraftingRecipe(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(16,1,
        // werkstoff.getBridgeMaterial(),werkstoff.getBridgeMaterial(),null),
        // GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"IhI",
        // "III", " I ", 'I', werkstoff.get(ingot)});
    }
}
