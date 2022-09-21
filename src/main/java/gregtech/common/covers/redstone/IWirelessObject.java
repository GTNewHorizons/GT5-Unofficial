package gregtech.common.covers.redstone;

import gregtech.api.util.ISerializableObject;

import java.util.UUID;

public interface IWirelessObject extends ISerializableObject {

    int getFrequency();
    void setFrequency(int frequency);

    UUID getUuid();
    void setUuid(UUID uuid);
}
