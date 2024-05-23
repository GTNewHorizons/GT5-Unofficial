package net.glease.ggfab.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

public class GigaGramFabAPI {

    private static final Logger apiLogger = LogManager.getLogger("GigaGramFabAPI");

    private static final Map<ToolDictNames, IToolStats> SINGLE_USE_TOOLS_STORE = new HashMap<>();
    public static final Map<ToolDictNames, IToolStats> SINGLE_USE_TOOLS = Collections
            .unmodifiableMap(SINGLE_USE_TOOLS_STORE);

    private static final Map<ToolDictNames, Long> COST_SINGLE_USE_TOOLS_STORE = new HashMap<>();
    public static final Map<ToolDictNames, Long> COST_SINGLE_USE_TOOLS = Collections
            .unmodifiableMap(COST_SINGLE_USE_TOOLS_STORE);

    public static void addSingleUseToolType(ToolDictNames type, IToolStats stat, long materialCost) {
        if (SINGLE_USE_TOOLS_STORE.put(type, stat) != null)
            apiLogger.warn("Replacing stat of single use tool {}", type);
        COST_SINGLE_USE_TOOLS_STORE.put(type, materialCost);
    }
}
