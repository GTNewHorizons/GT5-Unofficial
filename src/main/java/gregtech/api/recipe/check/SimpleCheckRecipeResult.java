package gregtech.api.recipe.check;

import java.util.Objects;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

/**
 * Simple implementation of {@link CheckRecipeResult}. You can create new object without registering it.
 */
public class SimpleCheckRecipeResult implements CheckRecipeResult {

    private boolean success;
    private String key;

    SimpleCheckRecipeResult(boolean success, String key) {
        this.success = success;
        this.key = key;
    }

    @Override
    public String getID() {
        return "simple_result";
    }

    @Override
    public boolean wasSuccessful() {
        return success;
    }

    @Override
    public String getDisplayString() {
        return StatCollector.translateToLocal("GT5U.gui.text." + key);
    }

    @Override
    public CheckRecipeResult newInstance() {
        return new SimpleCheckRecipeResult(false, "");
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(success);
        // NetworkUtils.writeStringSafe(buffer, key);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        success = buffer.readBoolean();
        // key = NetworkUtils.readStringSafe(buffer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleCheckRecipeResult that = (SimpleCheckRecipeResult) o;
        return success == that.success && Objects.equals(key, that.key);
    }

    /**
     * Creates new result with successful state. Add your localized description with `GT5U.gui.text.{key}`.
     * This is already registered to registry.
     */
    public static CheckRecipeResult ofSuccess(String key) {
        return new SimpleCheckRecipeResult(true, key);
    }

    /**
     * Creates new result object with failed state. Add your localized description with `GT5U.gui.text.{key}`.
     * This is already registered to registry.
     */
    public static CheckRecipeResult ofFailure(String key) {
        return new SimpleCheckRecipeResult(false, key);
    }
}
