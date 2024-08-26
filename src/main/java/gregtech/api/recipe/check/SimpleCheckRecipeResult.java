package gregtech.api.recipe.check;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

/**
 * Simple implementation of {@link CheckRecipeResult}. You can create new object without registering it.
 */
public class SimpleCheckRecipeResult implements CheckRecipeResult {

    private boolean success;
    private String key;
    private boolean persistsOnShutdown;

    SimpleCheckRecipeResult(boolean success, String key, boolean persistsOnShutdown) {
        this.success = success;
        this.key = key;
        this.persistsOnShutdown = persistsOnShutdown;
    }

    @Override
    public @NotNull String getID() {
        return "simple_result";
    }

    @Override
    public boolean wasSuccessful() {
        return success;
    }

    @Override
    @Nonnull
    public @NotNull String getDisplayString() {
        return Objects.requireNonNull(StatCollector.translateToLocal("GT5U.gui.text." + key));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setBoolean("success", success);
        tag.setString("key", key);
        tag.setBoolean("persistsOnShutdown", persistsOnShutdown);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        success = tag.getBoolean("success");
        key = tag.getString("key");
        persistsOnShutdown = tag.getBoolean("persistsOnShutdown");
    }

    @Override
    @Nonnull
    public @NotNull CheckRecipeResult newInstance() {
        return new SimpleCheckRecipeResult(false, "", false);
    }

    @Override
    public void encode(@Nonnull PacketBuffer buffer) {
        buffer.writeBoolean(success);
        NetworkUtils.writeStringSafe(buffer, key);
        buffer.writeBoolean(persistsOnShutdown);
    }

    @Override
    public void decode(@Nonnull PacketBuffer buffer) {
        success = buffer.readBoolean();
        key = NetworkUtils.readStringSafe(buffer);
        persistsOnShutdown = buffer.readBoolean();
    }

    @Override
    public boolean persistsOnShutdown() {
        return persistsOnShutdown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleCheckRecipeResult that = (SimpleCheckRecipeResult) o;
        return success == that.success && Objects.equals(key, that.key)
            && persistsOnShutdown == that.persistsOnShutdown;
    }

    /**
     * Creates new result with successful state. Add your localized description with `GT5U.gui.text.{key}`.
     * This is already registered to registry.
     */
    @Nonnull
    public static CheckRecipeResult ofSuccess(String key) {
        return new SimpleCheckRecipeResult(true, key, false);
    }

    /**
     * Creates new result with failed state. Add your localized description with `GT5U.gui.text.{key}`.
     * This is already registered to registry.
     */
    @Nonnull
    public static CheckRecipeResult ofFailure(String key) {
        return new SimpleCheckRecipeResult(false, key, false);
    }

    /**
     * Creates new result object with failed state that does not get reset on shutdown. Add your localized description
     * with `GT5U.gui.text.{key}`. This is already registered to registry.
     */
    public static CheckRecipeResult ofFailurePersistOnShutdown(String key) {
        return new SimpleCheckRecipeResult(false, key, true);
    }
}
