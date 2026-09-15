package gregtech.api.util;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

public interface IMachineMessage<T extends IMachineMessage<T>> {

    /**
     * @return Unique registry ID
     */
    @Nonnull
    String getID();

    /**
     * @return Actual text to show on client GUI
     */
    @Nonnull
    String getDisplayString();

    /**
     * Save info to NBT.
     */
    @NotNull
    NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag);

    /**
     * Read info from NBT.
     */
    void readFromNBT(@Nonnull NBTTagCompound tag);

    /**
     * Create new instance to receive packet.
     */
    @Nonnull
    T newInstance();

    /**
     * Encode value to sync.
     */
    void encode(@Nonnull PacketBuffer buffer);

    /**
     * Decode synced value.
     */
    void decode(PacketBuffer buffer);
}
