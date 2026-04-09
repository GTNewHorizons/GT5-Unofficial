package gregtech.api.enums;

import static gregtech.api.enums.ItemList.Shape_Mold_SingleUseCrowbar;
import static gregtech.api.enums.ItemList.Shape_Mold_SingleUseFile;
import static gregtech.api.enums.ItemList.Shape_Mold_SingleUseHardHammer;
import static gregtech.api.enums.ItemList.Shape_Mold_SingleUseSaw;
import static gregtech.api.enums.ItemList.Shape_Mold_SingleUseScrewdriver;
import static gregtech.api.enums.ItemList.Shape_Mold_SingleUseSoftMallet;
import static gregtech.api.enums.ItemList.Shape_Mold_SingleUseWireCutter;
import static gregtech.api.enums.ItemList.Shape_Mold_SingleUseWrench;
import static gregtech.api.enums.ItemList.SingleUseCrowbar;
import static gregtech.api.enums.ItemList.SingleUseFile;
import static gregtech.api.enums.ItemList.SingleUseHardHammer;
import static gregtech.api.enums.ItemList.SingleUseSaw;
import static gregtech.api.enums.ItemList.SingleUseScrewdriver;
import static gregtech.api.enums.ItemList.SingleUseSoftMallet;
import static gregtech.api.enums.ItemList.SingleUseWireCutter;
import static gregtech.api.enums.ItemList.SingleUseWrench;
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

public enum SingleUseTool {

    // Hard tools
    File(SingleUseFile, 0, Shape_Mold_SingleUseFile, 30, craftingToolFile),
    Wrench(SingleUseWrench, 1, Shape_Mold_SingleUseWrench, 31, craftingToolWrench),
    Crowbar(SingleUseCrowbar, 2, Shape_Mold_SingleUseCrowbar, 32, craftingToolCrowbar),
    WireCutter(SingleUseWireCutter, 3, Shape_Mold_SingleUseWireCutter, 33, craftingToolWireCutter),
    HardHammer(SingleUseHardHammer, 4, Shape_Mold_SingleUseHardHammer, 34, craftingToolHardHammer),
    Screwdriver(SingleUseScrewdriver, 6, Shape_Mold_SingleUseScrewdriver, 36, craftingToolScrewdriver),
    Saw(SingleUseSaw, 7, Shape_Mold_SingleUseSaw, 37, craftingToolSaw),

    // Soft tools
    SoftMallet(SingleUseSoftMallet, 5, Shape_Mold_SingleUseSoftMallet, 35, craftingToolSoftMallet);

    public static final List<SingleUseTool> HARD_TOOLS = ImmutableList
        .of(File, Wrench, Crowbar, WireCutter, HardHammer, Screwdriver, Saw);

    public static final List<SingleUseTool> SOFT_TOOLS = ImmutableList.of(SoftMallet);

    public final ItemList tool;
    public final int toolID;
    public final ItemList mold;
    public final int moldID;
    public final ToolDictNames toolDictName;

    SingleUseTool(ItemList tool, int toolID, ItemList mold, int moldID, ToolDictNames toolDictName) {
        this.tool = tool;
        this.toolID = toolID;
        this.mold = mold;
        this.moldID = moldID;
        this.toolDictName = toolDictName;
    }
}
