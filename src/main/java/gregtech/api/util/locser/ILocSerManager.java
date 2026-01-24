package gregtech.api.util.locser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.GTMod;
import io.netty.buffer.Unpooled;

public class ILocSerManager {

    private static final Map<String, Supplier<? extends ILocSer>> REGISTRY = new ConcurrentHashMap<>();

    static {
        register(LocSerItemName::new);
        register(LocSerFluidName::new);
        register(LocSerNumber::new);
        register(LocSerFormat::new);
    }

    public static void register(Supplier<? extends ILocSer> factory) {
        ILocSer instance = factory.get();
        String id = instance.getId();
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("LocSer ID must be non-null and non-empty.");
        }

        if (REGISTRY.putIfAbsent(id, factory) != null) {
            throw new IllegalArgumentException("LocSer with id '" + id + "' is already registered.");
        }
    }

    public static ILocSer decode(PacketBuffer buffer) {
        String id = NetworkUtils.readStringSafe(buffer);
        Supplier<? extends ILocSer> constructor = REGISTRY.get(id);
        if (constructor == null) {
            GTMod.GT_FML_LOGGER.error("Unknown LocSer id: {}", id);
            return new LocSerError();
        }

        ILocSer instance = constructor.get();
        instance.decode(buffer);
        return instance;
    }

    public static ILocSer decodeFromBytes(byte[] in) {
        return decode(new PacketBuffer(Unpooled.wrappedBuffer(in)));
    }

    public static ILocSer decodeFromBase64(String in) {
        byte[] data = java.util.Base64.getDecoder()
            .decode(in);
        return decodeFromBytes(data);
    }

}
