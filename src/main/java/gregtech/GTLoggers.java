package gregtech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gregtech.api.util.GTLog;

public final class GTLoggers {

    public static final Logger GT_FML_LOGGER = LogManager.getLogger("GregTech GTNH");
    public static final Logger GT_EXPLOSION_LOGGER = GTLog.conditionalLogger("GregTech Explosions");
    public static final Logger GT_ICON_LOGGER = GTLog.conditionalLogger("GregTech Icons");
    public static final Logger GT_ORE_DICT_LOGGER = GTLog.conditionalLogger("GregTech Ore Dictionary");
    public static final boolean GT_RECIPE_REMOVAL_LOGGER_ENABLED = Boolean.getBoolean("gt.recipe.remove_delayed.debug");
    public static final Logger GT_RECIPE_REMOVAL_LOGGER = GTLog.conditionalLogger("GregTech Useless Recipe Removals");
    public static final Logger GT_SHADER_LOGGER = LogManager.getLogger("ShaderAPI");

    private GTLoggers() {}
}
