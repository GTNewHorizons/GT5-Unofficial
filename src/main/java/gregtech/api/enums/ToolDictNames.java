package gregtech.api.enums;

import java.util.List;

import com.google.common.collect.ImmutableList;

public enum ToolDictNames {

    craftingToolSaw,
    craftingToolHoe,
    craftingToolAxe,
    craftingToolFile,
    craftingToolPlow,
    craftingToolDrill,
    craftingToolSword,
    craftingToolScoop,
    craftingToolKnife,
    craftingToolBlade,
    craftingToolMortar,
    craftingToolShovel,
    craftingToolWrench,
    craftingToolPlunger,
    craftingToolCrowbar,
    craftingToolPickaxe,
    craftingToolDrawplate,
    craftingToolRollingPin,
    craftingToolWireCutter,
    craftingToolBranchCutter,
    craftingToolHardHammer,
    craftingToolSoftHammer,
    craftingToolJackHammer,
    craftingToolMiningDrill,
    craftingToolForgeHammer,
    craftingToolScrewdriver,
    craftingToolSolderingIron,
    craftingToolSolderingMetal,
    craftingToolHandPump,
    craftingToolAngleGrinder,
    craftingToolElectricSnips,
    craftingToolElectricLighter,
    craftingToolElectricButcherKnife;

    public static boolean contains(String aName) {
        if (!aName.startsWith("craftingTool")) return false;
        for (ToolDictNames tool : ToolDictNames.values()) {
            if (tool.toString()
                .equals(aName)) {
                return true;
            }
        }
        return false;
    }

    public static final List<ToolDictNames> HARD_TOOLS = ImmutableList.of(
        craftingToolHardHammer,
        craftingToolScrewdriver,
        craftingToolWrench,
        craftingToolCrowbar,
        craftingToolWireCutter,
        craftingToolFile,
        craftingToolSaw);

    public static final List<ToolDictNames> SOFT_TOOLS = ImmutableList.of(craftingToolSoftHammer);
}
