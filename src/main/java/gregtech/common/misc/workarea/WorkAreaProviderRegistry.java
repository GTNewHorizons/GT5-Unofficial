package gregtech.common.misc.workarea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.world.World;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class WorkAreaProviderRegistry {

    private static final Set<IWorkAreaProvider> ACTIVE_PROVIDERS = Collections.newSetFromMap(new WeakHashMap<>());

    private WorkAreaProviderRegistry() {}

    public static synchronized void setActive(@NotNull IWorkAreaProvider provider, boolean active) {
        World world = provider.getWorkAreaWorld();

        // Important to avoid saving instances on the server side, especially in singleplayer
        if (world == null || !world.isRemote) {
            ACTIVE_PROVIDERS.remove(provider);
            return;
        }

        if (active) {
            ACTIVE_PROVIDERS.add(provider);
        } else {
            ACTIVE_PROVIDERS.remove(provider);
        }
    }

    public static synchronized void unregister(IWorkAreaProvider provider) {
        ACTIVE_PROVIDERS.remove(provider);
    }

    @Contract(value = " -> new", pure = true)
    public static synchronized @NotNull List<IWorkAreaProvider> getActiveProvidersSnapshot() {
        return new ArrayList<>(ACTIVE_PROVIDERS);
    }

    public static synchronized void clear() {
        ACTIVE_PROVIDERS.clear();
    }
}
