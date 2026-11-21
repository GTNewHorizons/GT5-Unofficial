package gregtech.crossmod.ae2;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.network.PacketBuffer;

import com.gtnewhorizon.gtnhlib.chat.AbstractChatComponentCustom;
import com.gtnewhorizon.gtnhlib.chat.customcomponents.AbstractChatComponentBuffer;

import gregtech.api.enums.Mods;
import gregtech.common.config.Gregtech;

/**
 * Carries the ghost and physical circuit numbers of a machine, so that the interface name suffix is formatted with the
 * viewing client's own {@link Gregtech.Machines#ghostCircuitSuffixFormat} rather than the server's.
 */
public class ChatComponentGhostCircuitSuffix extends AbstractChatComponentBuffer<ChatComponentGhostCircuitSuffix> {

    public List<Integer> circuitNumbers = new ArrayList<>();

    public ChatComponentGhostCircuitSuffix() {}

    public ChatComponentGhostCircuitSuffix(List<Integer> circuitNumbers) {
        this.circuitNumbers = circuitNumbers;
    }

    @Override
    public String getID() {
        return Mods.GregTech.ID + ":ChatComponentGhostCircuitSuffix";
    }

    @Override
    protected AbstractChatComponentCustom copySelf() {
        return new ChatComponentGhostCircuitSuffix(new ArrayList<>(circuitNumbers));
    }

    @Override
    public String getUnformattedTextForChat() {
        if (circuitNumbers.isEmpty()) return "";
        String joined = circuitNumbers.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
        try {
            return String.format(Gregtech.machines.ghostCircuitSuffixFormat, joined);
        } catch (IllegalFormatException e) {
            return "";
        }
    }

    @Override
    public void encode(PacketBuffer buf) {
        buf.writeVarIntToBuffer(circuitNumbers.size());
        for (int number : circuitNumbers) {
            buf.writeVarIntToBuffer(number);
        }
    }

    @Override
    public void decode(PacketBuffer buf) {
        final int size = buf.readVarIntFromBuffer();
        circuitNumbers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            circuitNumbers.add(buf.readVarIntFromBuffer());
        }
    }
}
