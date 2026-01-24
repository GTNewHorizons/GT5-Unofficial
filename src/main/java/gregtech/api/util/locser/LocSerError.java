package gregtech.api.util.locser;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import gregtech.GTMod;

public class LocSerError implements ILocSer {

    @Override
    public void encode(PacketBuffer out) {
        GTMod.GT_FML_LOGGER.error("Encoding LocSerError, this should never happen!");
        throw new UnsupportedOperationException("LocSerError encode not implemented!");
    }

    @Override
    public void decode(PacketBuffer in) {
        // No data to decode
    }

    @Override
    public String localize() {
        return StatCollector.translateToLocal("GT5U.chat.locser.error");
    }

    @Override
    public String getId() {
        return "gt:error";
    }
}
