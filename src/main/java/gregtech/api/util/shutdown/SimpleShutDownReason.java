package gregtech.api.util.shutdown;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

/**
 * Simple implementation of {@link ShutDownReason}. You can create new object without registering it.
 */
public class SimpleShutDownReason implements ShutDownReason {

    private String key;
    private boolean wasCritical;

    public SimpleShutDownReason(String key, boolean isCritical) {
        this.key = key;
        this.wasCritical = isCritical;
    }

    @NotNull
    @Override
    public String getID() {
        return "simple_result";
    }

    @NotNull
    @Override
    public String getDisplayString() {
        return Objects.requireNonNull(StatCollector.translateToLocal("GT5U.gui.text." + key));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setString("key", key);
        tag.setBoolean("wasCritical", wasCritical);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        key = tag.getString("key");
        wasCritical = tag.getBoolean("wasCritical");
    }

    @NotNull
    @Override
    public ShutDownReason newInstance() {
        return new SimpleShutDownReason("", false);
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeBoolean(wasCritical);
        NetworkUtils.writeStringSafe(buffer, key);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        wasCritical = buffer.readBoolean();
        key = NetworkUtils.readStringSafe(buffer);
    }

    @Override
    public boolean wasCritical() {
        return wasCritical;
    }

    /**
     * Creates new reason with critical state. Add your localized description with `GT5U.gui.text.{key}`.
     * This is already registered to registry.
     */
    @Nonnull
    public static ShutDownReason ofCritical(String key) {
        return new SimpleShutDownReason(key, true);
    }

    /**
     * Creates new reason with normal state. Add your localized description with `GT5U.gui.text.{key}`.
     * This is already registered to registry.
     */
    @Nonnull
    public static ShutDownReason ofNormal(String key) {
        return new SimpleShutDownReason(key, false);
    }
}
