package gregtech.api.net;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;

public final class ClientMetaTrackerRegistry {

    private static final Map<Block, IClientMetaTracker> registry = new HashMap<>();

    private ClientMetaTrackerRegistry() {}

    public static void register(Block block, IClientMetaTracker tracker) {
        registry.put(block, tracker);
    }

    public static IClientMetaTracker get(Block block) {
        return registry.get(block);
    }

    public static Iterable<IClientMetaTracker> getAllTrackers() {
        return registry.values();
    }
}
