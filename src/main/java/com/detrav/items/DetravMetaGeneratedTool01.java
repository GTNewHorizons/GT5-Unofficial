package com.detrav.items;

import com.detrav.DetravScannerMod;
import com.detrav.tools.DetravToolProPick;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.items.GT_MetaGenerated_Tool;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravMetaGeneratedTool01 extends GT_MetaGenerated_Tool {
    public static DetravMetaGeneratedTool01 INSTANCE;

    public DetravMetaGeneratedTool01() {
        super("detrav.metatool.01");
        INSTANCE = this;
        addTool(0, "ProPick", "", new DetravToolProPick(), new Object[]{new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)});
        setCreativeTab(DetravScannerMod.TAB_DETRAV);
        //addItemBehavior(0,)
    }


}
