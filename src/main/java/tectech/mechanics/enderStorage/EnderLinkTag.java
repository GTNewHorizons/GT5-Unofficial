package tectech.mechanics.enderStorage;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

public class EnderLinkTag implements Serializable {

    private static final long serialVersionUID = 6884008436570077863L;
    private final String frequency;
    private final UUID player;

    public EnderLinkTag(String frequency, UUID player) {
        this.frequency = frequency;
        this.player = player;
    }

    public String getFrequency() {
        return frequency;
    }

    public UUID getUUID() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnderLinkTag that = (EnderLinkTag) o;
        return Objects.equals(frequency, that.frequency) && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        final int hash = 31 + (frequency == null ? 0 : frequency.hashCode());
        return hash * 31 + (player == null ? 0 : player.hashCode());
    }

    public NBTTagCompound save() {
        NBTTagCompound data = new NBTTagCompound();

        data.setString("Freq", frequency);
        if (player != null) {
            data.setLong("ID1", player.getLeastSignificantBits());
            data.setLong("ID2", player.getMostSignificantBits());
        }

        return data;
    }

    public static EnderLinkTag load(NBTTagCompound data) {
        String freq = data.getString("Freq");
        UUID player = data.hasKey("ID1") ? new UUID(data.getLong("ID2"), data.getLong("ID1")) : null;

        return new EnderLinkTag(freq, player);
    }
}
