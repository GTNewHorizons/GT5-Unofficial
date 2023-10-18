package gtPlusPlus.xmod.gregtech.common.items;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechToolDictNames;
import gtPlusPlus.xmod.gregtech.common.tools.TOOL_Gregtech_AngleGrinder;
import gtPlusPlus.xmod.gregtech.common.tools.TOOL_Gregtech_ElectricSnips;

public class MetaGeneratedGregtechTools extends GT_MetaGenerated_Tool {

    public static final short ANGLE_GRINDER = 7834;
    public static final short ELECTRIC_SNIPS = 7934;
    public static GT_MetaGenerated_Tool INSTANCE;

    static {
        INSTANCE = new MetaGeneratedGregtechTools();
    }

    public static GT_MetaGenerated_Tool getInstance() {
        return INSTANCE;
    }

    private MetaGeneratedGregtechTools() {
        super("plusplus.metatool.01");

        // Electric File
        this.addTool(
                ANGLE_GRINDER,
                "Angle Grinder",
                "Hand-held electric filing device",
                new TOOL_Gregtech_AngleGrinder(),
                GregtechToolDictNames.craftingToolAngleGrinder,
                ToolDictNames.craftingToolFile,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L));

        // Electric Wire Cutter
        GregTech_API.registerWireCutter(
                this.addTool(
                        ELECTRIC_SNIPS,
                        "Automatic Snips",
                        "Hand-held electric wire cutter",
                        new TOOL_Gregtech_ElectricSnips(),
                        GregtechToolDictNames.craftingToolElectricSnips,
                        ToolDictNames.craftingToolWireCutter,
                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 4L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 4L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 4L)));

    }
}
