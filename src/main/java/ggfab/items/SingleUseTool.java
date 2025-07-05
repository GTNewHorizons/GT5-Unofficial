package ggfab.items;

import static ggfab.GGItemList.SingleUseCrowbar;
import static ggfab.GGItemList.SingleUseCrowbarMold;
import static ggfab.GGItemList.SingleUseFile;
import static ggfab.GGItemList.SingleUseFileMold;
import static ggfab.GGItemList.SingleUseHardHammer;
import static ggfab.GGItemList.SingleUseHardHammerMold;
import static ggfab.GGItemList.SingleUseSaw;
import static ggfab.GGItemList.SingleUseSawMold;
import static ggfab.GGItemList.SingleUseScrewdriver;
import static ggfab.GGItemList.SingleUseScrewdriverMold;
import static ggfab.GGItemList.SingleUseSoftMallet;
import static ggfab.GGItemList.SingleUseSoftMalletMold;
import static ggfab.GGItemList.SingleUseWireCutter;
import static ggfab.GGItemList.SingleUseWireCutterMold;
import static ggfab.GGItemList.SingleUseWrench;
import static ggfab.GGItemList.SingleUseWrenchMold;
import static gregtech.api.enums.ToolDictNames.craftingToolCrowbar;
import static gregtech.api.enums.ToolDictNames.craftingToolFile;
import static gregtech.api.enums.ToolDictNames.craftingToolHardHammer;
import static gregtech.api.enums.ToolDictNames.craftingToolSaw;
import static gregtech.api.enums.ToolDictNames.craftingToolScrewdriver;
import static gregtech.api.enums.ToolDictNames.craftingToolSoftMallet;
import static gregtech.api.enums.ToolDictNames.craftingToolWireCutter;
import static gregtech.api.enums.ToolDictNames.craftingToolWrench;

import java.util.List;

import com.google.common.collect.ImmutableList;

import ggfab.GGItemList;
import gregtech.api.enums.ToolDictNames;

public enum SingleUseTool {

    // Hard tools
    File(SingleUseFile, 0, SingleUseFileMold, 30, craftingToolFile),
    Wrench(SingleUseWrench, 1, SingleUseWrenchMold, 31, craftingToolWrench),
    Crowbar(SingleUseCrowbar, 2, SingleUseCrowbarMold, 32, craftingToolCrowbar),
    WireCutter(SingleUseWireCutter, 3, SingleUseWireCutterMold, 33, craftingToolWireCutter),
    HardHammer(SingleUseHardHammer, 4, SingleUseHardHammerMold, 34, craftingToolHardHammer),
    Screwdriver(SingleUseScrewdriver, 6, SingleUseScrewdriverMold, 36, craftingToolScrewdriver),
    Saw(SingleUseSaw, 7, SingleUseSawMold, 37, craftingToolSaw),

    // Soft tools
    SoftMallet(SingleUseSoftMallet, 5, SingleUseSoftMalletMold, 35, craftingToolSoftMallet);

    public static final List<SingleUseTool> HARD_TOOLS = ImmutableList
        .of(File, Wrench, Crowbar, WireCutter, HardHammer, Screwdriver, Saw);

    public static final List<SingleUseTool> SOFT_TOOLS = ImmutableList.of(SoftMallet);

    public final GGItemList tool;
    public final int toolID;
    public final GGItemList mold;
    public final int moldID;
    public final ToolDictNames toolDictName;

    SingleUseTool(GGItemList tool, int toolID, GGItemList mold, int moldID, ToolDictNames toolDictName) {
        this.tool = tool;
        this.toolID = toolID;
        this.mold = mold;
        this.moldID = moldID;
        this.toolDictName = toolDictName;
    }
}
