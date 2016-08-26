package miscutil.xmod.gregtech.common.items;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.ToolDictNames;
import miscutil.xmod.gregtech.api.enums.GregtechToolDictNames;
import miscutil.xmod.gregtech.api.items.Gregtech_MetaTool;
import miscutil.xmod.gregtech.common.tools.TOOL_Gregtech_Choocher;

public class MetaGeneratedGregtechTools extends Gregtech_MetaTool {
    
    public static final short TURBINE_SMALL = 10;
    public static final short TURBINE = 12;
    public static final short TURBINE_LARGE = 14;
    public static final short HUGE_ITEM = 16;
    public static final short TURBINE_BLADE = 18;
    
    public static final short HARDHAMMER = 20;
    public static final short SOFTHAMMER = 22;
    public static final short WRENCH = 24;
    
    public static MetaGeneratedGregtechTools INSTANCE;

    public MetaGeneratedGregtechTools() {
        super("MU-metaitem.02");
        INSTANCE = this;
        
        GregTech_API.registerTool(addTool(7734, "Skookum Choocher", "Can Really Chooch. Does a Skookum job at Hammering and Wrenching stuff.", new TOOL_Gregtech_Choocher(), new Object[]{GregtechToolDictNames.craftingToolSkookumChoocher, ToolDictNames.craftingToolHardHammer, ToolDictNames.craftingToolWrench, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)}), GregTech_API.sWrenchList);
        
        
        //GregTech_API.registerTool(addTool(WRENCH, "Wrench", "Hold Leftclick to dismantle Machines", (Interface_ToolStats) new GT_Tool_Wrench(), new Object[]{ToolDictNames.craftingToolWrench, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)}), GregTech_API.sWrenchList);
        
        //addTool(TURBINE_SMALL, "Small Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Small(), new Object[]{});
        //addTool(TURBINE, "Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Normal(), new Object[]{});
        //addTool(TURBINE_LARGE, "Large Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Large(), new Object[]{});
        //addTool(HUGE_ITEM, "Huge Item", "Item 4 for your power station", new TOOL_Gregtech_MaxEfficiencyMultiBlockItem(), new Object[]{});

    }
}
