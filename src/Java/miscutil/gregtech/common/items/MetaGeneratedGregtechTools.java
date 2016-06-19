package miscutil.gregtech.common.items;

import miscutil.gregtech.api.items.Gregtech_MetaTool;
import miscutil.gregtech.common.tools.TOOL_Gregtech_MaxEfficiencyMultiBlockItem;

public class MetaGeneratedGregtechTools extends Gregtech_MetaTool {
    
    public static final short TURBINE_SMALL = 10;
    public static final short TURBINE = 12;
    public static final short TURBINE_LARGE = 14;
    public static final short HUGE_ITEM = 16;
    public static final short TURBINE_BLADE = 18;
    public static MetaGeneratedGregtechTools INSTANCE;

    public MetaGeneratedGregtechTools() {
        super("MU-metaitem.02");
        INSTANCE = this;
        //addTool(TURBINE_SMALL, "Small Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Small(), new Object[]{});
        //addTool(TURBINE, "Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Normal(), new Object[]{});
        //addTool(TURBINE_LARGE, "Large Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Large(), new Object[]{});
        addTool(HUGE_ITEM, "Huge Item", "Item 4 for your power station", new TOOL_Gregtech_MaxEfficiencyMultiBlockItem(), new Object[]{});

    }
}
