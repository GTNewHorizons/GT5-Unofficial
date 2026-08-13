package gregtech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gregtech.api.util.GTLog;

public final class GTLoggers {

    public static final Logger GT_FML_LOGGER = LogManager.getLogger("GregTech GTNH");
    public static final Logger GT_EXPLOSION_LOGGER = GTLog.disabledLogger("GregTech Explosions");
    public static final Logger GT_ICON_LOGGER = GTLog.disabledLogger("GregTech Icons");
    public static final Logger GT_ORE_DICT_LOGGER = GTLog.disabledLogger("GregTech Ore Dictionary");
    public static final Logger GT_SHADER_LOGGER = LogManager.getLogger("ShaderAPI");

    private GTLoggers() {}
}
