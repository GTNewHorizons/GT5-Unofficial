package gtPlusPlus.xmod.gregtech.common.items;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.items.MetaGeneratedTool;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechToolDictNames;
import gtPlusPlus.xmod.gregtech.common.tools.ToolAngleGrinder;
import gtPlusPlus.xmod.gregtech.common.tools.ToolElectricSnips;

public class MetaGeneratedGregtechTools extends MetaGeneratedTool {

    public static final short ANGLE_GRINDER = 7834;
    public static final short ELECTRIC_SNIPS = 7934;
    public static MetaGeneratedTool INSTANCE;

    static {
        INSTANCE = new MetaGeneratedGregtechTools();
    }

    public static MetaGeneratedTool getInstance() {
        return INSTANCE;
    }

    private MetaGeneratedGregtechTools() {
        super("plusplus.metatool.01");

        // Electric File
        this.addTool(
            ANGLE_GRINDER,
            "Angle Grinder",
            "Hand-held electric filing device",
            new ToolAngleGrinder(),
            GregtechToolDictNames.craftingToolAngleGrinder,
            ToolDictNames.craftingToolFile,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));

        // Electric Wire Cutter
        GregTechAPI.registerWireCutter(
            this.addTool(
                ELECTRIC_SNIPS,
                "Automatic Snips",
                "Hand-held electric wire cutter",
                new ToolElectricSnips(),
                GregtechToolDictNames.craftingToolElectricSnips,
                ToolDictNames.craftingToolWireCutter,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 4L)));

    }
}
