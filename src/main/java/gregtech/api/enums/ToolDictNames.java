package gregtech.api.enums;

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
    craftingToolSolderingMetal;

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
}
