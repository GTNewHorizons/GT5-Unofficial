package gregtech.api.structure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

public class ActiveBlockLeaseTypeRegistry {

    private static final Map<String, ActiveBlockLeaseType> REGISTRY = new ConcurrentHashMap<>();

    public static final ActiveBlockLeaseType DEFAULT = register("default");

    public static ActiveBlockLeaseType register(String id) {
        ActiveBlockLeaseType type = new ActiveBlockLeaseType(id);
        if (REGISTRY.putIfAbsent(id, type) != null) {
            throw new IllegalStateException("Active block lease type " + id + " is already registered");
        }
        return type;
    }

    @Nullable
    public static ActiveBlockLeaseType get(String id) {
        return REGISTRY.get(id);
    }
}
