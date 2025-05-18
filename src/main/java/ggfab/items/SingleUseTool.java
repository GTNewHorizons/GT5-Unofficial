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
import static ggfab.GGItemList.SingleUseSoftHammer;
import static ggfab.GGItemList.SingleUseSoftHammerMold;
import static ggfab.GGItemList.SingleUseWireCutter;
import static ggfab.GGItemList.SingleUseWireCutterMold;
import static ggfab.GGItemList.SingleUseWrench;
import static ggfab.GGItemList.SingleUseWrenchMold;
import static gregtech.api.enums.ToolDictNames.craftingToolCrowbar;
import static gregtech.api.enums.ToolDictNames.craftingToolFile;
import static gregtech.api.enums.ToolDictNames.craftingToolHardHammer;
import static gregtech.api.enums.ToolDictNames.craftingToolSaw;
import static gregtech.api.enums.ToolDictNames.craftingToolScrewdriver;
import static gregtech.api.enums.ToolDictNames.craftingToolSoftHammer;
import static gregtech.api.enums.ToolDictNames.craftingToolWireCutter;
import static gregtech.api.enums.ToolDictNames.craftingToolWrench;

import java.util.List;

import com.google.common.collect.ImmutableList;

import ggfab.GGItemList;
import gregtech.api.enums.ToolDictNames;

public enum SingleUseTool {

    // Hard tools
    File(SingleUseFile, SingleUseFileMold, craftingToolFile),
    Wrench(SingleUseWrench, SingleUseWrenchMold, craftingToolWrench),
    Crowbar(SingleUseCrowbar, SingleUseCrowbarMold, craftingToolCrowbar),
    WireCutter(SingleUseWireCutter, SingleUseWireCutterMold, craftingToolWireCutter),
    HardHammer(SingleUseHardHammer, SingleUseHardHammerMold, craftingToolHardHammer),
    Screwdriver(SingleUseScrewdriver, SingleUseScrewdriverMold, craftingToolScrewdriver),
    Saw(SingleUseSaw, SingleUseSawMold, craftingToolSaw),

    // Soft tools
    SoftHammer(SingleUseSoftHammer, SingleUseSoftHammerMold, craftingToolSoftHammer);

    public static final List<SingleUseTool> HARD_TOOLS = ImmutableList
        .of(File, Wrench, Crowbar, WireCutter, HardHammer, Screwdriver, Saw);

    public static final List<SingleUseTool> SOFT_TOOLS = ImmutableList.of(SoftHammer);

    public final GGItemList tool;
    public final GGItemList mold;
    public final ToolDictNames toolDictName;

    SingleUseTool(GGItemList tool, GGItemList mold, ToolDictNames toolDictName) {
        this.tool = tool;
        this.mold = mold;
        this.toolDictName = toolDictName;
    }
}
