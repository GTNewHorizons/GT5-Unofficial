package com.detrav.tools;

import gregtech.api.enums.TC_Aspects;
import gregtech.api.items.GT_MetaGenerated_Tool;

/**
 * Created by wital_000 on 18.03.2016.
 */
public class Detrav_MetaGenerated_Tool_01 extends GT_MetaGenerated_Tool {
    public static Detrav_MetaGenerated_Tool_01 INSTANCE;

    public Detrav_MetaGenerated_Tool_01() {
        super("detrav.metatool.01");
        INSTANCE = this;
        addTool(1, "Prospector's Pick", "", new DetravToolProPick(),
                new Object[]
                        {
                                ToolDictNames.craftingToolProPick,
                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 4L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 4L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 1L)
                        });
    }
}
