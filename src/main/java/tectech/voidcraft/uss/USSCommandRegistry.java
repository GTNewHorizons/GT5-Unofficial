package tectech.voidcraft.uss;

import java.util.HashMap;
import java.util.Map;

/**
 * The command REGISTRY (programming framework, Phase B — the modularity seam): maps command ids (
 * {@link USSCommand}) to {@link USSCommandHandler} instances.
 *
 * <p>
 * The built-ins are registered statically; additional commands register themselves (one class +
 * {@link #register} + an id — the executor and the program format never change). Handlers must be stateless
 * singletons (per-ship state lives in the executor's cursor — see {@link USSCommandHandler}).
 */
public final class USSCommandRegistry {

    private static final Map<Integer, USSCommandHandler> HANDLERS = new HashMap<Integer, USSCommandHandler>();

    static {
        register(new USSCommandMove());
        register(new USSCommandMine());
        register(new USSCommandWrite());
        register(new USSCommandRead());
        register(new USSCommandWait());
        register(new USSCommandStop());
        register(new USSCommandConstruct());
        register(new USSCommandRepair());
        register(new USSCommandScan());
        register(new USSCommandSiphon());
        register(new USSCommandSend());
        register(new USSCommandTake());
        register(new USSCommandStabilize());
    }

    private USSCommandRegistry() {}

    /** Register (or replace) the handler for its command id. */
    public static synchronized void register(USSCommandHandler handler) {
        if (handler != null) {
            HANDLERS.put(handler.commandId(), handler);
        }
    }

    /**
     * @param id the command id
     * @return the handler, or null for an unregistered id (the executor then SKIPS the instruction)
     */
    public static synchronized USSCommandHandler handler(int id) {
        return HANDLERS.get(id);
    }

    public static synchronized boolean has(int id) {
        return HANDLERS.containsKey(id);
    }
}
